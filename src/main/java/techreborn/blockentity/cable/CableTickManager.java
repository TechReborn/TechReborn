/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.blockentity.cable;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import team.reborn.energy.api.EnergyStorage;
import techreborn.init.TRContent;

import java.util.*;

@SuppressWarnings("UnstableApiUsage")
class CableTickManager {
	private static long tickCounter = 0;
	private static final HashSet<HashSet<CableBlockEntity>> cableTickCache = new HashSet<>(1024);
	private static final List<OfferedEnergyStorage> targetStorages = new ArrayList<>();
	private static final HashMap<CableBlockEntity, HashSet<CableBlockEntity>> cableLinkedCache = new HashMap<>();


	static void handleCableTick(CableBlockEntity startingCable) {
		if (!(startingCable.getWorld() instanceof ServerWorld)) throw new IllegalStateException();
		if(startingCable.lastTick == tickCounter){
			return;
		}
		HashSet<CableBlockEntity> cableSet;
		try {
			boolean cacheState = startingCable.targets != null && isCacheValid(startingCable);
			if (cacheState){
				cableSet = cableLinkedCache.get(startingCable);
			}
			else {
				cableSet = gatherCables(startingCable);
				if(cableSet == null){
					return;
				}
				cableLinkedCache.put(startingCable,cableSet);
				cableTickCache.add(cableSet);
			}
			if (cableSet == null || cableSet.size() == 0) return;

			// Group all energy into the network.
			long networkCapacity = 0;
			long networkAmount = 0;

			for (CableBlockEntity cable : cableSet) {
				cableLinkedCache.put(cable, cableSet);
				networkAmount += cable.energyContainer.amount;
				networkCapacity += cable.energyContainer.getCapacity();

				// Update cable connections.
				cable.appendTargets(targetStorages);
				// Block any cable I/O while we access the network amount directly.
				// Some things might try to access cables, for example a p2p tunnel pointing back at a cable.
				// If the cables and the network go out of sync, we risk duping or voiding energy.
				cable.ioBlocked = true;
			}

			// Just in case.
			if (networkAmount > networkCapacity) {
				networkAmount = networkCapacity;
			}

			// Pull energy from storages.
			networkAmount += dispatchTransfer(startingCable.getCableType(), EnergyStorage::extract, networkCapacity - networkAmount);
			// Push energy into storages.
			networkAmount -= dispatchTransfer(startingCable.getCableType(), EnergyStorage::insert, networkAmount);
			if (networkAmount < 0) {
				networkAmount = 0;
			}
			// Split energy evenly across cables.
			int cableCount = cableSet.size();
			for (CableBlockEntity cable : cableSet) {
				cable.lastTick = tickCounter;
				cable.energyContainer.amount = networkAmount / cableCount;
				networkAmount -= cable.energyContainer.amount;
				cableCount--;
				cable.markDirty();
				cable.ioBlocked = false;
			}
		} finally {
			//cableTickCache.clear();
			targetStorages.clear();
		}
	}
	//target is null when it finds or losts its connection
	private static boolean isCacheValid(CableBlockEntity cableBlockEntity){
		HashSet<CableBlockEntity> cableList = cableLinkedCache.get(cableBlockEntity);
		if (cableList != null){
			boolean mark = true;
			ArrayList<CableBlockEntity> listToRemove = new ArrayList<>();
			for (CableBlockEntity cables : cableList){
				if (cables.targets == null || cables.isRemoved() ){
					listToRemove.add(cables);
					mark = false;
				}
			}
			for (CableBlockEntity removeCable : listToRemove){
				cableLinkedCache.remove(removeCable);
			}
			return mark;
		}
		return false;
	}

	private static boolean shouldTickCable(CableBlockEntity current) {
		// Make sure we only gather and tick each cable once per tick.
		if (current.lastTick == tickCounter) return false;
		// Make sure we ignore cables in non-ticking chunks.
		return current.getWorld() instanceof ServerWorld sw && sw.isChunkLoaded(current.getPos());
	}

	/**
	 * Perform a BFS to gather all connected ticking cables.
	 */
	private static HashSet<CableBlockEntity> gatherCables(CableBlockEntity start) {
		Deque<CableBlockEntity> bfsQueue = new ArrayDeque<>();
		HashSet<CableBlockEntity> cableList = new HashSet<>();
		if (!shouldTickCable(start)) return null;
		Optional <HashSet<CableBlockEntity>> cachedResult = cableTickCache.stream().filter(a->a!= null && a.contains(start)).findAny();
		if (cachedResult.isPresent()){
			return cachedResult.get();
		}
		cableList.add(start);
		bfsQueue.add(start);
		World world = start.getWorld();
		TRContent.Cables cableType = start.getCableType();
		while (!bfsQueue.isEmpty()){
			CableBlockEntity where = bfsQueue.removeFirst();
			for (Direction direction : Direction.values()){
				if (world.getBlockEntity(where.getPos().offset(direction)) instanceof CableBlockEntity adjCable && !cableList.contains(adjCable) && adjCable.getCableType() == cableType ){
					bfsQueue.add(adjCable);
					cableList.add(adjCable);
				}
			}
		}
		return cableList;
	}
	/**
	 * Perform a transfer operation across a list of targets.
	 */
	private static long dispatchTransfer(TRContent.Cables cableType, TransferOperation operation, long maxAmount) {
		// Build target list.
		List<SortableStorage> sortedTargets = new ArrayList<>();
		for (var storage : targetStorages) {
			sortedTargets.add(new SortableStorage(operation, storage));
		}
		// Why do we need shuffle?
		Collections.shuffle(sortedTargets);
		// Sort by lowest simulation target.
		sortedTargets.sort(Comparator.comparingLong(sortableStorage -> sortableStorage.simulationResult));
		// Actually perform the transfer.
		try (Transaction transaction = Transaction.openOuter()) {
			long transferredAmount = 0;
			for (int i = 0; i < sortedTargets.size(); ++i) {
				SortableStorage target = sortedTargets.get(i);
				int remainingTargets = sortedTargets.size() - i;
				long remainingAmount = maxAmount - transferredAmount;
				// Limit max amount to the cable transfer rate.
				long targetMaxAmount = Math.min(remainingAmount / remainingTargets, cableType.transferRate);

				long localTransferred = operation.transfer(target.storage.storage, targetMaxAmount, transaction);
				if (localTransferred > 0) {
					transferredAmount += localTransferred;
					// Block duplicate operations.
					target.storage.afterTransfer();
				}
			}
			transaction.commit();
			return transferredAmount;
		}
	}

	private interface TransferOperation {
		long transfer(EnergyStorage storage, long maxAmount, Transaction transaction);
	}

	private static class SortableStorage {
		private final OfferedEnergyStorage storage;
		private final long simulationResult;

		SortableStorage(TransferOperation operation, OfferedEnergyStorage storage) {
			this.storage = storage;
			try (Transaction tx = Transaction.openOuter()) {
				this.simulationResult = operation.transfer(storage.storage, Long.MAX_VALUE, tx);
			}
		}
	}

	static {
		ServerTickEvents.START_SERVER_TICK.register(server -> {tickCounter++; cableTickCache.clear();});
	}
}
