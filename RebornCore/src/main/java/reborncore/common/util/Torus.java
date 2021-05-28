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

package reborncore.common.util;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Torus {
	private static final ExecutorService GEN_EXECUTOR = Executors.newSingleThreadExecutor();
	private static Int2IntMap torusSizeCache;

	public static List<BlockPos> generate(BlockPos orgin, int radius) {
		List<BlockPos> posLists = new ArrayList<>();
		for (int x = -radius; x < radius; x++) {
			for (int y = -radius; y < radius; y++) {
				for (int z = -radius; z < radius; z++) {
					if (Math.pow(radius / 2 - Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)), 2) + Math.pow(z, 2) < Math.pow(radius * 0.05, 2)) {
						posLists.add(orgin.add(x, z, y));
					}
				}
			}
		}
		return posLists;
	}

	public static void genSizeMap(int maxRadius) {
		if (torusSizeCache != null) {
			//Lets not do this again
			return;
		}
		//10 is added as the control computer has a base of around 6 less
		final int sizeToCompute = maxRadius + 10;

		torusSizeCache = new Int2IntOpenHashMap(sizeToCompute);

		for (int i = 0; i < sizeToCompute; i++) {
			final int radius = i;
			GEN_EXECUTOR.submit(() -> {
				int size = 0;
				for (int x = -radius; x < radius; x++) {
					for (int y = -radius; y < radius; y++) {
						for (int z = -radius; z < radius; z++) {
							if (Math.pow(radius / 2 - Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)), 2) + Math.pow(z, 2) < Math.pow(radius * 0.05, 2)) {
								size++;
							}
						}
					}
				}
				torusSizeCache.put(radius, size);
			});
		}

		// Finish running the tasks, and then shutdown the ExecutorService. This call does not stall the main thread
		GEN_EXECUTOR.shutdown();
	}

	public static Int2IntMap getTorusSizeCache() {
		if (!GEN_EXECUTOR.isShutdown()) {
			try {
				GEN_EXECUTOR.awaitTermination(1, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				throw new RuntimeException("Reborn core failed to initialize the torus cache", e);
			}
		}

		return torusSizeCache;
	}
}
