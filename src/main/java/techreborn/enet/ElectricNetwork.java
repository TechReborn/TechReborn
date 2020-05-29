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
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import team.reborn.energy.EnergyTier;
import techreborn.TechReborn;
import techreborn.blockentity.cable.CableBlockEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class ElectricNetwork {
	private int network_id;
	private EnergyTier network_tier;
	private HashSet<CableBlockEntity> enetCableBEs = new HashSet<>();
	private boolean isDirty;
	private boolean isEnergized;

	public ElectricNetwork(int id, EnergyTier tier) {
		network_id = id;
		network_tier = tier;
		isDirty = true;
	}

	public int getId() {
		return network_id;
	}

	public boolean isEmpty() {
		return enetCableBEs.isEmpty();
	}

	public void addBlockEntity(CableBlockEntity entity) {
		this.enetCableBEs.add(entity);
		isDirty = true;
	}

	public void removeBlockEntity(CableBlockEntity entity) {
		this.enetCableBEs.remove(entity);
		isDirty = true;
	}

	public void tick() {
		if (this.isEmpty()) {
			return;
		}

		if (isDirty) {
			TechReborn.LOGGER.debug("Electric Network {} is dirty, checking for network and connection changes", network_id);
			this.walkNetwork();

			isDirty = false;
		}

		this.distributePower();
	}

	public boolean isEnergized() {
		return isEnergized;
	}

	private void walkNetwork() {
		// If the network is empty, why bother?
		if (enetCableBEs.isEmpty()) {
			return;
		}

		TechReborn.LOGGER.debug("Walking the Electric Network to detect for changes in neighbors...");

		// Keep track of the nodes we've seen and visited
		HashSet<CableBlockEntity> seenNodes = new HashSet<>();
		HashSet<CableBlockEntity> visitedNodes = new HashSet<>();

		// "See" the first node in the list
		CableBlockEntity firstNode = enetCableBEs.iterator().next();
		TechReborn.LOGGER.debug("First node in the walk: {}", firstNode);
		seenNodes.addAll(visitCableNode(firstNode, visitedNodes));

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
			if (nodeConnected.getElectricNetworkId() != network_id) {
				TechReborn.LOGGER.debug(
						"Block {} network change: {} -> {}",
						nodeConnected.getPos().asLong(),
						nodeConnected.getElectricNetworkId(),
						network_id);

				nodeConnected.setElectricNetwork(network_id);
			}
		}

		TechReborn.LOGGER.debug(
				"Starting purge of disconnected entities. Have {}, seen {}",
				enetCableBEs.size(),
				seenNodes.size());

		HashSet<CableBlockEntity> nodesToRemove = new HashSet<>(enetCableBEs);
		nodesToRemove.removeAll(seenNodes);

		for (CableBlockEntity nodeRemoved : nodesToRemove) {
			TechReborn.LOGGER.debug(
					"Block {} being ejected from the network {}",
					nodeRemoved.getPos().asLong(),
					network_id);

			nodeRemoved.setElectricNetwork(0);
		}

		TechReborn.LOGGER.debug("Finished walking network for cable connection changes.");
	}

	private HashSet<CableBlockEntity> visitCableNode(CableBlockEntity currentEntity, HashSet<CableBlockEntity> visitedNodes) {
		visitedNodes.add(currentEntity);

		HashSet<CableBlockEntity> seenNodes = new HashSet<>();

		if (currentEntity != null) {
			TechReborn.LOGGER.debug(
					"Visiting node {} on behalf of {} ({} visited total)",
					currentEntity.getPos().asLong(),
					network_id,
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
						targetEntity.getElectricNetworkId());

				if (currentEntity.canConnectTo(targetEntity)) {
					TechReborn.LOGGER.debug("Seen another node -- {}", blockToCheckPos.asLong());
					seenNodes.add(targetEntity);
				}
			}
		}

		return seenNodes;
	}

	private void distributePower() {
		ArrayList<Pair<PowerAcceptorBlockEntity, Direction>> powerAcceptors = new ArrayList<>();

		for (CableBlockEntity entity : enetCableBEs) {
			powerAcceptors.addAll(entity.getConnectedPowerAcceptors());
		}

		double remainingTransfer = Math.min(network_tier.getMaxInput(), network_tier.getMaxOutput());

		// Prioritize transferring energy to entities that cannot also supply it on the same connection side (like a buffer)
		List<Pair<PowerAcceptorBlockEntity, Direction>> fromList = powerAcceptors
				.stream()
				.filter(v -> v.getLeft().canProvideEnergy(v.getRight()) && v.getLeft().getEnergy() > 0)
				.sorted(Comparator.comparing(v -> v.getLeft().canAcceptEnergy(v.getRight()) ? 1 : 0))
				.collect(Collectors.toList());

		List<Pair<PowerAcceptorBlockEntity, Direction>> toList = powerAcceptors
				.stream()
				.filter(v -> v.getLeft().canAcceptEnergy(v.getRight()) && !v.getLeft().canProvideEnergy(v.getRight()) && v.getLeft().getFreeSpace() > 0)
				.collect(Collectors.toList());

		isEnergized = fromList.stream().anyMatch(v -> v.getLeft().canProvideEnergy(v.getRight()));

		remainingTransfer = this.transferPower(fromList, toList, remainingTransfer);

		if (remainingTransfer > 0) {
			// Handle transfers directly from generators to buffers
			fromList = powerAcceptors
					.stream()
					.filter(v -> v.getLeft().canProvideEnergy(v.getRight()) && !v.getLeft().canAcceptEnergy(v.getRight()))
					.collect(Collectors.toList());

			toList = powerAcceptors
					.stream()
					.filter(v -> v.getLeft().canAcceptEnergy(v.getRight()) && v.getLeft().canProvideEnergy(v.getRight()) && v.getLeft().getFreeSpace() > 0)
					.collect(Collectors.toList());

			this.transferPower(fromList, toList, remainingTransfer);
		}
	}

	private double transferPower(List<Pair<PowerAcceptorBlockEntity, Direction>> fromList, List<Pair<PowerAcceptorBlockEntity, Direction>> toList, double transferLimit) {
		double remainingTransfer = transferLimit;

		for (Pair<PowerAcceptorBlockEntity, Direction> to : toList) {
			for (Pair<PowerAcceptorBlockEntity, Direction> from : fromList) {
				PowerAcceptorBlockEntity fromEntity = from.getLeft();
				PowerAcceptorBlockEntity toEntity = to.getLeft();

				if (fromEntity == toEntity) {
					TechReborn.LOGGER.debug("Skipping power transfer as source and target are the same");
					continue;
				}

				double transferAmount = Math.min(toEntity.getFreeSpace(), remainingTransfer);
				double extracted = fromEntity.useEnergy(transferAmount);
				toEntity.addEnergy(extracted);

				remainingTransfer -= extracted;
			}
		}

		return remainingTransfer;
	}
}
