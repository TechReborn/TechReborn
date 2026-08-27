/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.common.multiblock;

import reborncore.RebornCore;
import reborncore.common.util.WorldUtils;

import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

/**
 * This class manages all the multiblock controllers that exist in a server
 * level.
 *
 * @author Erogenous Beef
 */
final class MultiblockWorldRegistry {
	// Active controllers
	private final Set<MultiblockControllerBase> controllers;
	// Controllers whose parts lists have changed
	private final Set<MultiblockControllerBase> dirtyControllers;
	// Controllers which are empty
	private final Set<MultiblockControllerBase> deadControllers;

	// A list of orphan parts - parts which currently have no master, but should
	// seek one this tick
	private Set<IMultiblockPart> orphanedParts;

	// A list of parts which have been detached during internal operations
	private final Set<IMultiblockPart> detachedParts;

	private final Object orphanedPartsMutex;

	MultiblockWorldRegistry() {
		controllers = new HashSet<>();
		deadControllers = new HashSet<>();
		dirtyControllers = new HashSet<>();

		detachedParts = new HashSet<>();
		orphanedParts = new HashSet<>();

		orphanedPartsMutex = new Object();
	}

	/**
	 * Processes pending changes and ticks every multiblock in this level.
	 */
	void tick(ServerLevel world) {
		processMultiblockChanges(world);
		tickStart(world);
	}

	/**
	 * Called before Tile Entities are ticked in the world. Run game logic.
	 */
	private void tickStart(ServerLevel world) {
		if (controllers.size() > 0) {
			for (MultiblockControllerBase controller : controllers) {
				if (controller.worldObj == world) {
					if (controller.isEmpty()) {
						// This happens on the server when the user breaks the
						// last block. It's fine.
						// Mark 'er dead and move on.
						deadControllers.add(controller);
					} else {
						// Run the game logic for this world
						controller.updateMultiblockEntity();
					}
				}
			}
		}
	}

	/**
	 * Called prior to processing multiblock controllers. Do bookkeeping.
	 */
	private void processMultiblockChanges(ServerLevel world) {
		BlockPos coord;

		// Merge pools - sets of adjacent machines which should be merged later
		// on in processing
		List<Set<MultiblockControllerBase>> mergePools = null;
		if (orphanedParts.size() > 0) {
			Set<IMultiblockPart> orphansToProcess = null;

			// Keep the synchronized block small. We can't iterate over
			// orphanedParts directly
			// while another thread may add newly-loaded parts.
			synchronized (orphanedPartsMutex) {
				if (orphanedParts.size() > 0) {
					orphansToProcess = orphanedParts;
					orphanedParts = new HashSet<>();
				}
			}

			if (orphansToProcess != null && orphansToProcess.size() > 0) {
				Set<MultiblockControllerBase> compatibleControllers;

				// Process orphaned blocks
				// These are blocks that exist in a valid chunk and require a
				// controller
				for (IMultiblockPart orphan : orphansToProcess) {
					coord = orphan.getWorldLocation();
					if (!WorldUtils.isChunkLoaded(world, coord)) {
						continue;
					}

					// This can occur on slow machines.
					if (orphan.isInvalid()) {
						continue;
					}

					// This block has been replaced by another.
					if (world.getBlockEntity(coord) != orphan) {
						continue;
					}

					// THIS IS THE ONLY PLACE WHERE PARTS ATTACH TO MACHINES
					// Try to attach to a neighbor's master controller
					compatibleControllers = orphan.attachToNeighbors();
					if (compatibleControllers == null) {
						// FOREVER ALONE! Create and register a new controller.
						// THIS IS THE ONLY PLACE WHERE NEW CONTROLLERS ARE
						// CREATED.
						MultiblockControllerBase newController = orphan.createNewMultiblock();
						newController.attachBlock(orphan);
						this.controllers.add(newController);
					} else if (compatibleControllers.size() > 1) {
						if (mergePools == null) {
							mergePools = new ArrayList<>();
						}

						// THIS IS THE ONLY PLACE WHERE MERGES ARE DETECTED
						// Multiple compatible controllers indicates an
						// impending merge.
						// Locate the appropriate merge pool(s)
						//boolean hasAddedToPool = false;
						List<Set<MultiblockControllerBase>> candidatePools = new ArrayList<>();
						for (Set<MultiblockControllerBase> candidatePool : mergePools) {
							if (!Collections.disjoint(candidatePool, compatibleControllers)) {
								// They share at least one element, so that
								// means they will all touch after the merge
								candidatePools.add(candidatePool);
							}
						}

						if (candidatePools.size() <= 0) {
							// No pools nearby, create a new merge pool
							mergePools.add(compatibleControllers);
						} else if (candidatePools.size() == 1) {
							// Only one pool nearby, simply add to that one
							candidatePools.get(0).addAll(compatibleControllers);
						} else {
							// Multiple pools- merge into one, then add the
							// compatible controllers
							Set<MultiblockControllerBase> masterPool = candidatePools.get(0);
							Set<MultiblockControllerBase> consumedPool;
							for (int i = 1; i < candidatePools.size(); i++) {
								consumedPool = candidatePools.get(i);
								masterPool.addAll(consumedPool);
								mergePools.remove(consumedPool);
							}
							masterPool.addAll(compatibleControllers);
						}
					}
				}
			}
		}

		if (mergePools != null && mergePools.size() > 0) {
			// Process merges - any machines that have been marked for merge
			// should be merged
			// into the "master" machine.
			// To do this, we combine lists of machines that are touching one
			// another and therefore
			// should voltron the fuck up.
			for (Set<MultiblockControllerBase> mergePool : mergePools) {
				// Search for the new master machine, which will take over all
				// the blocks contained in the other machines
				MultiblockControllerBase newMaster = null;
				for (MultiblockControllerBase controller : mergePool) {
					if (newMaster == null || controller.shouldConsume(newMaster)) {
						newMaster = controller;
					}
				}

				if (newMaster == null) {
					RebornCore.LOGGER.error("Multiblock system checked a merge pool of size %d, found no master candidates. This should never happen., {}", mergePool.size());
				} else {
					// Merge all the other machines into the master machine,
					// then unregister them
					addDirtyController(newMaster);
					for (MultiblockControllerBase controller : mergePool) {
						if (controller != newMaster) {
							newMaster.assimilate(controller);
							addDeadController(controller);
							addDirtyController(newMaster);
						}
					}
				}
			}
		}

		// Process splits and assembly
		// Any controllers which have had parts removed must be checked to see
		// if some parts are no longer
		// physically connected to their master.
		if (dirtyControllers.size() > 0) {
			Set<IMultiblockPart> newlyDetachedParts = null;
			for (MultiblockControllerBase controller : dirtyControllers) {
				// Tell the machine to check if any parts are disconnected.
				// It should return a set of parts which are no longer
				// connected.
				// POSTCONDITION: The controller must have informed those parts
				// that
				// they are no longer connected to this machine.
				newlyDetachedParts = controller.checkForDisconnections();

				if (!controller.isEmpty()) {
					controller.recalculateMinMaxCoords();
					controller.checkIfMachineIsWhole();
				} else {
					addDeadController(controller);
				}

				if (newlyDetachedParts != null && newlyDetachedParts.size() > 0) {
					// Controller has shed some parts - add them to the detached
					// list for delayed processing
					detachedParts.addAll(newlyDetachedParts);
				}
			}

			dirtyControllers.clear();
		}

		// Unregister dead controllers
		if (deadControllers.size() > 0) {
			for (MultiblockControllerBase controller : deadControllers) {
				// Go through any controllers which have marked themselves as
				// potentially dead.
				// Validate that they are empty/dead, then unregister them.
				if (!controller.isEmpty()) {
					RebornCore.LOGGER.error("Found a non-empty controller. Forcing it to shed its blocks and die. This should never happen!");
					detachedParts.addAll(controller.detachAllBlocks());
				}

				// THIS IS THE ONLY PLACE WHERE CONTROLLERS ARE UNREGISTERED.
				this.controllers.remove(controller);
			}

			deadControllers.clear();
		}

		// Process detached blocks
		// Any blocks which have been detached this tick should be moved to the
		// orphaned
		// list, and will be checked next tick to see if their chunk is still
		// loaded.
		for (IMultiblockPart part : detachedParts) {
			// Ensure parts know they're detached
			part.assertDetached();
		}

		addAllOrphanedPartsThreadsafe(detachedParts);
		detachedParts.clear();
	}

	/**
	 * Called when a multiblock part is added to the world, either via
	 * chunk-load or user action. It will be processed during the next tick.
	 *
	 * @param part {@link IMultiblockPart} The part which is being added to this world.
	 */
	void onPartAdded(IMultiblockPart part) {
		addOrphanedPartThreadsafe(part);
	}

	/**
	 * Called when a part is removed from the world, via user action or via
	 * chunk unloads. This part is removed from any lists in which it may be,
	 * and its machine is marked for recalculation.
	 *
	 * @param part {@link IMultiblockPart} The part which is being removed.
	 */
	void onPartRemovedFromWorld(IMultiblockPart part) {
		detachedParts.remove(part);
		if (orphanedParts.contains(part)) {
			synchronized (orphanedPartsMutex) {
				orphanedParts.remove(part);
			}
		}

		part.assertDetached();
	}

	/**
	 * Registers a controller as dead. It will be cleaned up at the end of the
	 * next world tick. Note that a controller must shed all of its blocks
	 * before being marked as dead, or the system will complain at you.
	 *
	 * @param deadController {@link MultiblockControllerBase} The controller which is dead.
	 */
	void addDeadController(MultiblockControllerBase deadController) {
		this.deadControllers.add(deadController);
	}

	/**
	 * Registers a controller as dirty - its list of attached blocks has
	 * changed, and it must be re-checked for assembly and, possibly, for
	 * orphans.
	 *
	 * @param dirtyController {@link MultiblockControllerBase} The dirty controller.
	 */
	void addDirtyController(MultiblockControllerBase dirtyController) {
		this.dirtyControllers.add(dirtyController);
	}

	/* *** PRIVATE HELPERS *** */

	private void addOrphanedPartThreadsafe(IMultiblockPart part) {
		synchronized (orphanedPartsMutex) {
			orphanedParts.add(part);
		}
	}

	private void addAllOrphanedPartsThreadsafe(Collection<? extends IMultiblockPart> parts) {
		synchronized (orphanedPartsMutex) {
			orphanedParts.addAll(parts);
		}
	}
}
