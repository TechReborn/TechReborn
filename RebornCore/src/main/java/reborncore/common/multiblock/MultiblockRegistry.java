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

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import reborncore.RebornCore;

import java.util.HashMap;
import java.util.Set;

/**
 * This is a very static singleton registry class which directs incoming events
 * to sub-objects, which actually manage each individual world's multiblocks.
 *
 * @author Erogenous Beef
 */
public class MultiblockRegistry {
	// World > WorldRegistry map
	private static HashMap<World, MultiblockWorldRegistry> registries = new HashMap<>();

	/**
	 * Called before Tile Entities are ticked in the world. Do bookkeeping here.
	 *
	 * @param world The world being ticked
	 */
	public static void tickStart(World world) {
		if (registries.containsKey(world)) {
			MultiblockWorldRegistry registry = registries.get(world);
			registry.processMultiblockChanges();
			registry.tickStart();
		}
	}

	/**
	 * Called when the world has finished loading a chunk.
	 *
	 * @param world The world which has finished loading a chunk
	 * @param chunk Loaded chunk
	 */
	public static void onChunkLoaded(World world, Chunk chunk) {
		if (registries.containsKey(world)) {
			registries.get(world).onChunkLoaded(chunk);
		}
	}

	/**
	 * Register a new part in the system. The part has been created either
	 * through user action or via a chunk loading.
	 *
	 * @param world The world into which this part is loading.
	 * @param part The part being loaded.
	 */
	public static void onPartAdded(World world, IMultiblockPart part) {
		MultiblockWorldRegistry registry = getOrCreateRegistry(world);
		registry.onPartAdded(part);
	}

	/**
	 * Call to remove a part from world lists.
	 *
	 * @param world The world from which a multiblock part is being removed.
	 * @param part The part being removed.
	 */
	public static void onPartRemovedFromWorld(World world, IMultiblockPart part) {
		if (registries.containsKey(world)) {
			registries.get(world).onPartRemovedFromWorld(part);
		}

	}

	/**
	 * Called whenever a world is unloaded. Unload the relevant registry, if we
	 * have one.
	 *
	 * @param world The world being unloaded.
	 */
	public static void onWorldUnloaded(World world) {
		if (registries.containsKey(world)) {
			registries.get(world).onWorldUnloaded();
			registries.remove(world);
		}
	}

	/**
	 * Call to mark a controller as dirty. Dirty means that parts have been
	 * added or removed this tick.
	 *
	 * @param world The world containing the multiblock
	 * @param controller The dirty controller
	 */
	public static void addDirtyController(World world, MultiblockControllerBase controller) {
		if (registries.containsKey(world)) {
			registries.get(world).addDirtyController(controller);
		} else {
			RebornCore.LOGGER.error("Adding a dirty controller to a world that has no registered controllers! This is most likey not an issue with reborn core, please check the full log file for more infomation!");
		}
	}

	/**
	 * Call to mark a controller as dead. It should only be marked as dead when
	 * it has no connected parts. It will be removed after the next world tick.
	 *
	 * @param world The world formerly containing the multiblock
	 * @param controller The dead controller
	 */
	public static void addDeadController(World world, MultiblockControllerBase controller) {
		if (registries.containsKey(world)) {
			registries.get(world).addDeadController(controller);
		} else {
			RebornCore.LOGGER.warn(String.format(
				"Controller %d in world %s marked as dead, but that world is not tracked! Controller is being ignored.",
				controller.hashCode(), world));
		}
	}

	/**
	 * @param world The world whose controllers you wish to retrieve.
	 * @return An unmodifiable set of controllers active in the given world, or
	 * null if there are none.
	 */
	public static Set<MultiblockControllerBase> getControllersFromWorld(World world) {
		if (registries.containsKey(world)) {
			return registries.get(world).getControllers();
		}
		return null;
	}

	// / *** PRIVATE HELPERS *** ///

	private static MultiblockWorldRegistry getOrCreateRegistry(World world) {
		if (registries.containsKey(world)) {
			return registries.get(world);
		} else {
			MultiblockWorldRegistry newRegistry = new MultiblockWorldRegistry(world);
			registries.put(world, newRegistry);
			return newRegistry;
		}
	}

}
