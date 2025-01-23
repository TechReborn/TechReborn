/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2024 TeamReborn
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

package reborncore.common.misc.world;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChunkPosMultiMap<T> {
	private final Map<World, HashMap<ChunkPos, Set<T>>> storage = new HashMap<>();

	public final void add(World world, ChunkPos chunkPos, T t) {
		storage.computeIfAbsent(world, w -> new HashMap<>()).computeIfAbsent(chunkPos, p -> new HashSet<>()).add(t);
	}

	public final void remove(World world, ChunkPos chunkPos, T t) {
		Map<ChunkPos, Set<T>> chunkPosMap = storage.get(world);
		Set<T> tSet = chunkPosMap.get(chunkPos);

		if (!tSet.remove(t)) {
			throw new RuntimeException("Could not remove element at position " + chunkPos + " as it does not exist.");
		}

		if (tSet.isEmpty()) {
			chunkPosMap.remove(chunkPos);

			if (chunkPosMap.isEmpty()) {
				storage.remove(world);
			}
		}
	}

	@Nullable
	public final Set<T> get(World world, ChunkPos chunkPos) {
		Map<ChunkPos, Set<T>> chunkPosSetMap = storage.get(world);
		if (chunkPosSetMap == null) {
			return null;
		}
		return chunkPosSetMap.get(chunkPos);
	}

	public final int size() {
		return storage.size();
	}
}
