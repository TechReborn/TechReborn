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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import team.reborn.energy.EnergyTier;
import techreborn.TechReborn;
import techreborn.blockentity.cable.CableBlockEntity;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public final class ElectricNetwork {
	private final EnergyTier networkTier;
	private final HashSet<CableBlockEntity> cableBlockEntities = new HashSet<>();
	private boolean isDirty;
	private boolean isEnergized;

	public ElectricNetwork(EnergyTier tier) {
		networkTier = tier;
		isDirty = true;
	}

	public boolean isEmpty() {
		return cableBlockEntities.isEmpty();
	}

	public void addBlockEntity(CableBlockEntity entity) {
		isDirty = cableBlockEntities.add(entity) || isDirty;
	}

	public void removeBlockEntity(CableBlockEntity entity) {
		isDirty = cableBlockEntities.remove(entity) || isDirty;
	}

	public void tick() {
		if (isEmpty()) {
			return;
		}

		if (isDirty) {
			TechReborn.LOGGER.debug("Electric Network {} is dirty, checking for network and connection changes", this);
			walkNetwork();

			isDirty = false;
		}

		distributePower();
	}

	public boolean isEnergized() {
		return isEnergized;
	}

	private void walkNetwork() {
		// If the network is empty, why bother?
		if (cableBlockEntities.isEmpty()) {
			return;
		}

		TechReborn.LOGGER.debug("Walking the Electric Network to detect for changes in neighbors...");

		// Keep track of the nodes we've seen and visited
		HashSet<CableBlockEntity> seenNodes = new HashSet<>();
		HashSet<CableBlockEntity> visitedNodes = new HashSet<>();

		// "See" the first node in the list
		CableBlockEntity firstNode = cableBlockEntities.iterator().next();
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
				cableBlockEntities.size(),
				seenNodes.size());

		cableBlockEntities
				.stream()
				.filter(node -> !seenNodes.contains(node))
				.forEach(node -> {
					TechReborn.LOGGER.debug(
							"Block {} being ejected from the network {}",
							node.getPos().asLong(),
							this);

					node.setElectricNetwork(null);
				});

		TechReborn.LOGGER.debug("Finished walking network for cable connection changes.");
	}

	private HashSet<CableBlockEntity> visitCableNode(CableBlockEntity currentEntity, HashSet<CableBlockEntity> visitedNodes) {
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
		List<PowerAcceptorBlockEntityFace> powerAcceptors = cableBlockEntities
				.stream()
				.flatMap(entityFace -> entityFace.getConnectedPowerAcceptors().stream())
				.collect(Collectors.toList());

		double networkTransferCapacity = Math.min(networkTier.getMaxInput(), networkTier.getMaxOutput());

		List<PowerAcceptorBlockEntityFace> fromList = powerAcceptors
				.stream()
				.filter(entityFace -> entityFace.canProvideEnergy() && entityFace.getEnergy() > 0)
				.sorted(Comparator.comparing(entityFace -> entityFace.canAcceptEnergy() ? 1 : 0))
				.collect(Collectors.toList());

		List<PowerAcceptorBlockEntityFace> toList = powerAcceptors
				.stream()
				.filter(entityFace -> entityFace.canAcceptEnergy() &&
						!entityFace.canProvideEnergy() &&
						entityFace.hasFreeSpace())
				.collect(Collectors.toList());

		isEnergized = fromList
				.stream()
				.anyMatch(entityFace -> entityFace.canProvideEnergy());

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
}
