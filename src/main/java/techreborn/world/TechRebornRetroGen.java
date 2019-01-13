/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.world;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import reborncore.common.misc.ChunkCoord;
import techreborn.TechReborn;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.Set;

/**
 * Based off https://github.com/SteamNSteel/SteamNSteel
 */
public class TechRebornRetroGen {
	private static final String RETROGEN_TAG = "techrebonr:retogen";
	private static final Set<ChunkCoord> completedChunks = Sets.newHashSet();
	private final Deque<ChunkCoord> chunksToRetroGen = new ArrayDeque<>(64);

	private boolean isChunkEligibleForRetroGen(ChunkDataEvent.Load event) {
		return TechReborn.worldGen.config.retroGenOres && event.getWorld().provider.getDimension() == 0
			&& event.getData().getString(RETROGEN_TAG).isEmpty();
	}

	public void markChunk(ChunkCoord coord) {
		completedChunks.add(coord);
	}

	private boolean isTickEligibleForRetroGen(TickEvent.WorldTickEvent event) {
		return event.phase == TickEvent.Phase.END || event.side == Side.SERVER;
	}

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		if (isTickEligibleForRetroGen(event)) {
			if (!chunksToRetroGen.isEmpty()) {
				final ChunkCoord coord = chunksToRetroGen.pollFirst();
				TechReborn.LOGGER.info("Regenerating ore in " + coord + '.');
				final World world = event.world;
				if (world.getChunkProvider().getLoadedChunk(coord.getX(), coord.getZ()) != null) {
					final long seed = world.getSeed();
					final Random rng = new Random(seed);
					final long xSeed = rng.nextLong() >> 2 + 1L;
					final long zSeed = rng.nextLong() >> 2 + 1L;
					final long chunkSeed = (xSeed * coord.getX() + zSeed * coord.getZ()) * seed;
					rng.setSeed(chunkSeed);
					TechReborn.worldGen.generate(rng, coord.getX(), coord.getZ(), world, null, null);
				}
			}
		}
	}

	@SubscribeEvent
	public void onChunkLoad(ChunkDataEvent.Load event) {
		if (isChunkEligibleForRetroGen(event)) {
			final ChunkCoord coord = ChunkCoord.of(event);
			TechReborn.LOGGER.info("Queueing retro ore gen for " + coord + '.');
			chunksToRetroGen.addLast(coord);
		}
	}

	@SubscribeEvent
	public void onChunkSave(ChunkDataEvent.Save event) {
		final ChunkCoord coord = ChunkCoord.of(event);
		if (completedChunks.contains(coord)) {
			event.getData().setString(RETROGEN_TAG, "X");
			completedChunks.remove(coord);
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("chunksToRetroGen", chunksToRetroGen).toString();
	}
}
