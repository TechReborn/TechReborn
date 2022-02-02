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

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import reborncore.RebornCore;
import reborncore.common.util.WorldUtils;

import java.util.*;

/**
 * This class manages all the multiblock controllers that exist in a given
 * world, either client- or server-side. You must create different registries
 * for server and client worlds.
 *
 * @author Erogenous Beef
 */
public class MultiblockWorldRegistry {

	private World worldObj;

	// Active controllers
	private Set<MultiblockControllerBase> controllers;
	// Controllers whose parts lists have changed
	private Set<MultiblockControllerBase> dirtyControllers;
	// Controllers which are empty
	private Set<MultiblockControllerBase> deadControllers;

	// A list of orphan parts - parts which currently have no master, but should
	// seek one this tick
	// Indexed by the hashed chunk coordinate
	// This can be added-to asynchronously via chunk loads!
	private Set<IMultiblockPart> orphanedParts;

	// A list of parts which have been detached during internal operations
	private Set<IMultiblockPart> detachedParts;

	// A list of parts whose chunks have not yet finished loading
	// They will be added to the orphan list when they are finished loading.
	// Indexed by the hashed chunk coordinate
	// This can be added-to asynchronously via chunk loads!
	private HashMap<Integer, Set<IMultiblockPart>> partsAwaitingChunkLoad;

	// Mutexes to protect lists which may be changed due to asynchronous events,
	// such as chunk loads
	private Object partsAwaitingChunkLoadMutex;
	private Object orphanedPartsMutex;

	public MultiblockWorldRegistry(final World world) {
		worldObj = world;

		controllers = new HashSet<>();
		deadControllers = new HashSet<>();
		dirtyControllers = new HashSet<>();

		detachedParts = new HashSet<>();
		orphanedParts = new HashSet<>();

		partsAwaitingChunkLoad = new HashMap<>();
		partsAwaitingChunkLoadMutex = new Object();
		orphanedPartsMutex = new Object();
	}

	/**
	 * Called before Tile Entities are ticked in the world. Run game logic.
	 */
	public void tickStart() {
		if (controllers.size() > 0) {
			for (MultiblockControllerBase controller : controllers) {
				if (controller.worldObj == worldObj && controller.worldObj.isClient == worldObj.isClient) {
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
	public void processMultiblockChanges() {
		BlockPos coord;

		// Merge pools - sets of adjacent machines which should be merged later
		// on in processing
		List<Set<MultiblockControllerBase>> mergePools = null;
		if (orphanedParts.size() > 0) {
			Set<IMultiblockPart> orphansToProcess = null;

			// Keep the synchronized block small. We can't iterate over
			// orphanedParts directly
			// because the client does not know which chunks are actually
			// loaded, so attachToNeighbors()
			// is not chunk-safe on the client, because Minecraft is stupid.
			// It's possible to polyfill this, but the polyfill is too slow for
			// comfort.
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
					if (!WorldUtils.isChunkLoaded(worldObj, coord)) {
						continue;
					}

					// This can occur on slow machines.
					if (orphan.isInvalid()) {
						continue;
					}

					// This block has been replaced by another.
					if (worldObj.getBlockEntity(coord) != orphan) {
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
	 * chunk-load or user action. If its chunk is loaded, it will be processed
	 * during the next tick. If the chunk is not loaded, it will be added to a
	 * list of objects waiting for a chunkload.
	 *
	 * @param part {@link IMultiblockPart} The part which is being added to this world.
	 */
	public void onPartAdded(IMultiblockPart part) {
		BlockPos pos = part.getWorldLocation();

		if (!WorldUtils.isChunkLoaded(worldObj, pos)) {
			// Part goes into the waiting-for-chunk-load list
			Set<IMultiblockPart> partSet;
			int chunkHash = new ChunkPos(pos).hashCode();

			synchronized (partsAwaitingChunkLoadMutex) {
				if (!partsAwaitingChunkLoad.containsKey(chunkHash)) {
					partSet = new HashSet<>();
					partsAwaitingChunkLoad.put(chunkHash, partSet);
				} else {
					partSet = partsAwaitingChunkLoad.get(chunkHash);
				}

				partSet.add(part);
			}
		} else {
			// Part goes into the orphan queue, to be checked this tick
			addOrphanedPartThreadsafe(part);
		}
	}

	/**
	 * Called when a part is removed from the world, via user action or via
	 * chunk unloads. This part is removed from any lists in which it may be,
	 * and its machine is marked for recalculation.
	 *
	 * @param part {@link IMultiblockPart} The part which is being removed.
	 */
	public void onPartRemovedFromWorld(IMultiblockPart part) {
		BlockPos pos = part.getWorldLocation();
		if (pos != null) {
			int chunkHash = new ChunkPos(pos).hashCode();

			if (partsAwaitingChunkLoad.containsKey(chunkHash)) {
				synchronized (partsAwaitingChunkLoadMutex) {
					if (partsAwaitingChunkLoad.containsKey(chunkHash)) {
						partsAwaitingChunkLoad.get(chunkHash).remove(part);
						if (partsAwaitingChunkLoad.get(chunkHash).size() <= 0) {
							partsAwaitingChunkLoad.remove(chunkHash);
						}
					}
				}
			}
		}

		detachedParts.remove(part);
		if (orphanedParts.contains(part)) {
			synchronized (orphanedPartsMutex) {
				orphanedParts.remove(part);
			}
		}

		part.assertDetached();
	}

	/**
	 * Called when the world which this World Registry represents is fully
	 * unloaded from the system. Does some housekeeping just to be nice.
	 */
	public void onWorldUnloaded() {
		controllers.clear();
		deadControllers.clear();
		dirtyControllers.clear();

		detachedParts.clear();

		synchronized (partsAwaitingChunkLoadMutex) {
			partsAwaitingChunkLoad.clear();
		}

		synchronized (orphanedPartsMutex) {
			orphanedParts.clear();
		}

		worldObj = null;
	}

	/**
	 * Called when a chunk has finished loading. Adds all the parts which are
	 * awaiting load to the list of parts which are orphans and therefore will
	 * be added to the machines after the next world tick.
	 *
	 * @param chunk {@link Chunk} Chunk that was loaded
	 */
	public void onChunkLoaded(Chunk chunk) {
		int chunkHash = chunk.getPos().hashCode();
		if (partsAwaitingChunkLoad.containsKey(chunkHash)) {
			synchronized (partsAwaitingChunkLoadMutex) {
				if (partsAwaitingChunkLoad.containsKey(chunkHash)) {
					addAllOrphanedPartsThreadsafe(partsAwaitingChunkLoad.get(chunkHash));
					partsAwaitingChunkLoad.remove(chunkHash);
				}
			}
		}
	}

	/**
	 * Registers a controller as dead. It will be cleaned up at the end of the
	 * next world tick. Note that a controller must shed all of its blocks
	 * before being marked as dead, or the system will complain at you.
	 *
	 * @param deadController {@link MultiblockControllerBase} The controller which is dead.
	 */
	public void addDeadController(MultiblockControllerBase deadController) {
		this.deadControllers.add(deadController);
	}

	/**
	 * Registers a controller as dirty - its list of attached blocks has
	 * changed, and it must be re-checked for assembly and, possibly, for
	 * orphans.
	 *
	 * @param dirtyController {@link MultiblockControllerBase} The dirty controller.
	 */
	public void addDirtyController(MultiblockControllerBase dirtyController) {
		this.dirtyControllers.add(dirtyController);
	}

	/**
	 * Use this only if you know what you're doing. You should rarely need to
	 * iterate over all controllers in a world!
	 *
	 * @return {@link Set} An (unmodifiable) set of {@link MultiblockControllerBase}
	 * controllers which are active in this world.
	 */
	public Set<MultiblockControllerBase> getControllers() {
		return Collections.unmodifiableSet(controllers);
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
