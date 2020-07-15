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

package techreborn.enet;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import team.reborn.energy.EnergyTier;
import techreborn.TechReborn;
import techreborn.blockentity.cable.CableBlockEntity;
import techreborn.config.TechRebornConfig;

import java.util.*;
import java.util.stream.Collectors;

public final class ElectricNetwork {

	private boolean isActive = true;
	private BlockPos lastCentroid = null;

	private final EnergyTier networkTier;

	// Chunkpos and cables for a world
	private final Set<CableBlockEntity> cables = new HashSet<>();

	// World where network is located
	private final RegistryKey<World> dimension;

	private boolean isDirty;
	private boolean isEnergized;

	public ElectricNetwork(RegistryKey<World> dimension, EnergyTier tier) {
		this.dimension = dimension;
		networkTier = tier;
		isDirty = true;
	}

	public void refreshState(MinecraftServer server){
		ServerWorld serverWorld = server.getWorld(dimension);

		if(serverWorld == null || cables.size() == 0){
			return;
		}

		if(isActive){
			checkActive(serverWorld, getCentroid());
		}else{
			if(lastCentroid != null){
				checkActive(serverWorld, lastCentroid);
			}else{
				checkActive(serverWorld, getCentroid());
			}
		}
	}

	public void tick() {
		if (isDirty) {
			TechReborn.LOGGER.debug("Electric Network {} is dirty, checking for network and connection changes", this);
			walkNetwork();

			isDirty = false;
		}

		distributePower();
	}

	public boolean isEmpty() {
		return cables.isEmpty();
	}

	public void addBlockEntity(CableBlockEntity entity) {
			isDirty = cables.add(entity) || isDirty;
	}

	public void removeBlockEntity(CableBlockEntity entity) {
			isDirty = cables.remove(entity) || isDirty;
	}

	public boolean isEnergized() {
		return isEnergized;
	}

	private void walkNetwork() {
		// If the network is empty, why bother?
		if (cables.isEmpty()) {
			return;
		}

		TechReborn.LOGGER.debug("Walking the Electric Network to detect for changes in neighbors...");

		// Keep track of the nodes we've seen and visited
		HashSet<CableBlockEntity> visitedNodes = new HashSet<>();

		// "See" the first node in the list
		CableBlockEntity firstNode = cables.iterator().next(); // TODO er
		TechReborn.LOGGER.debug("First node in the walk: {}", firstNode);

		HashSet<CableBlockEntity> seenNodes = new HashSet<>(visitCableNode(firstNode, visitedNodes));

		// "remainingNodesToVisit" is our seen nodes, excluding the ones we've visited
		HashSet<CableBlockEntity> remainingNodesToVisit = new HashSet<>(seenNodes);

		while (!remainingNodesToVisit.isEmpty()) {
			for (CableBlockEntity nodeToVisit : remainingNodesToVisit) {
				seenNodes.addAll(visitCableNode(nodeToVisit, visitedNodes));
			}

			remainingNodesToVisit.clear();
			remainingNodesToVisit.addAll(seenNodes);
			remainingNodesToVisit.removeAll(visitedNodes);
		}

		for (CableBlockEntity nodeConnected : seenNodes) {
			if (nodeConnected.getElectricNetwork() != this) {
				TechReborn.LOGGER.debug(
						"Block {} network change: {} -> {}",
						nodeConnected.getPos().asLong(),
						nodeConnected.getElectricNetwork(),
						this);

				nodeConnected.setElectricNetwork(this);
			}
		}

		TechReborn.LOGGER.debug(
				"Starting purge of disconnected entities. Have {}, seen {}",
				getCableCount(),
				seenNodes.size());

			cables
				.stream()
				.filter(node -> !seenNodes.contains(node) && node != null)
				.collect(Collectors.toList())
				.forEach(node -> {
					TechReborn.LOGGER.debug(
							"Block {} being ejected from the network {}",
							node,
							this);

					node.setElectricNetwork(null);
				});


		TechReborn.LOGGER.debug("Finished walking network for cable connection changes.");
	}

	private Set<CableBlockEntity> visitCableNode(CableBlockEntity currentEntity, HashSet<CableBlockEntity> visitedNodes) {
		visitedNodes.add(currentEntity);

		HashSet<CableBlockEntity> seenNodes = new HashSet<>();

		if (currentEntity != null) {
			TechReborn.LOGGER.debug(
					"Visiting node {} on behalf of {} ({} visited total)",
					currentEntity.getPos().asLong(),
					this,
					visitedNodes.size());

			seenNodes.add(currentEntity);

			for (Direction face : Direction.values()) {
				BlockPos blockToCheckPos = currentEntity.getPos().offset(face);
				BlockEntity blockEntity = currentEntity.getWorld().getBlockEntity(blockToCheckPos);

				if (!(blockEntity instanceof CableBlockEntity) || visitedNodes.contains(blockEntity)) {
					continue;
				}

				CableBlockEntity targetEntity = (CableBlockEntity) blockEntity;

				TechReborn.LOGGER.debug(
						"Node {} is on network {}",
						targetEntity.getPos().asLong(),
						targetEntity.getElectricNetwork());

				if (currentEntity.canConnectTo(targetEntity)) {
					TechReborn.LOGGER.debug("Seen another node -- {}", blockToCheckPos.asLong());
					seenNodes.add(targetEntity);
				}
			}
		}

		return seenNodes;
	}

	private void distributePower() {
		List<PowerAcceptorBlockEntityFace> powerAcceptors = new ArrayList<>();

		double networkTransferCapacity = Math.min(networkTier.getMaxInput(), networkTier.getMaxOutput());

		for (CableBlockEntity cableBlockEntity : cables) {
			powerAcceptors.addAll(cableBlockEntity.getConnectedPowerAcceptors());
		}

		List<PowerAcceptorBlockEntityFace> fromList = new ArrayList<>();
		List<PowerAcceptorBlockEntityFace> toList = new ArrayList<>();

		for (PowerAcceptorBlockEntityFace entityFace : powerAcceptors) {

			// If can provide energy add to FROM list otherwise TO (if conditions are met)
			if (entityFace.canProvideEnergy()) {
				// If it actually has energy, add it to FROM
				if (entityFace.getEnergy() > 0) {
					fromList.add(entityFace);
				}
			} else {
				// If it can accept energy and have free space, add to the TO list
				if (entityFace.canAcceptEnergy() && entityFace.hasFreeSpace()) {
					toList.add(entityFace);
				}
			}
		}

		// Was in original code,
		fromList.sort(Comparator.comparing(entityFace -> entityFace.canAcceptEnergy() ? 1 : 0));

		isEnergized = fromList
				.stream()
				.anyMatch(PowerAcceptorBlockEntityFace::canProvideEnergy);

		transferPower(fromList, toList, networkTransferCapacity);
	}

	private void transferPower(List<PowerAcceptorBlockEntityFace> fromList, List<PowerAcceptorBlockEntityFace> toList, double networkTransferCapacity) {
		double remainingTransfer = networkTransferCapacity;

		for (PowerAcceptorBlockEntityFace to : toList) {
			for (PowerAcceptorBlockEntityFace from : fromList) {
				if (from.isSameEntity(to)) {
					continue;
				}

				double transferAmount = Math.min(to.getFreeSpace(), remainingTransfer);
				double extracted = from.useEnergy(transferAmount);
				to.addEnergy(extracted);

				remainingTransfer -= extracted;

				if (remainingTransfer <= 0) {
					break;
				}
			}

			if (remainingTransfer <= 0) {
				break;
			}
		}
	}

	public boolean isActive(){
		return isActive;
	}

	public void drop(){
		cables.forEach(CableBlockEntity::clearElectricNetwork);
		cables.clear();
	}

	// Checks if there is a player near the centroid of the network, this will determine if the network should be chunk loaded
	public void checkActive(ServerWorld world, BlockPos centroid){
		boolean playerWithinRange = world.isPlayerInRange(centroid.getX(), centroid.getY(), centroid.getZ(), 500);
		boolean isCentroidForcedLoaded = world.getForcedChunks().contains(new ChunkPos(centroid).toLong());

		this.isActive = playerWithinRange || isCentroidForcedLoaded;
	}

	private BlockPos getCentroid()  {
		int centroidX = 0;
		int centroidY = 0;
		int centroidZ = 0;


		for(CableBlockEntity cableBlockEntity : cables) {
			centroidX += cableBlockEntity.getPos().getX();
			centroidY += cableBlockEntity.getPos().getY();
			centroidZ +=  cableBlockEntity.getPos().getZ();
		}

		int size = cables.size();

		lastCentroid = new BlockPos(centroidX / size, centroidY / size, centroidZ / size);

		return lastCentroid;
	}

	// Debug functions

	public int getCableCount() {
		return cables.size();
	}
}
