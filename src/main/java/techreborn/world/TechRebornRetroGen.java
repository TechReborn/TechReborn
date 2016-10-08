package techreborn.world;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import reborncore.common.misc.ChunkCoord;
import techreborn.Core;

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
		return Core.worldGen.config.retroGenOres && event.getWorld().provider.getDimension() == 0
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
				Core.logHelper.info("Regenerating ore in " + coord + '.');
				final World world = event.world;
				if (world.getChunkProvider().getLoadedChunk(coord.getX(), coord.getZ()) != null) {
					final long seed = world.getSeed();
					final Random rng = new Random(seed);
					final long xSeed = rng.nextLong() >> 2 + 1L;
					final long zSeed = rng.nextLong() >> 2 + 1L;
					final long chunkSeed = (xSeed * coord.getX() + zSeed * coord.getZ()) * seed;
					rng.setSeed(chunkSeed);
					Core.worldGen.generate(rng, coord.getX() << 4, coord.getZ() << 4, world, null, null);
				}
			}
		}
	}

	@SubscribeEvent
	public void onChunkLoad(ChunkDataEvent.Load event) {
		if (isChunkEligibleForRetroGen(event)) {
			final ChunkCoord coord = ChunkCoord.of(event);
			Core.logHelper.info("Queueing retro ore gen for " + coord + '.');
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
		return Objects.toStringHelper(this).add("chunksToRetroGen", chunksToRetroGen).toString();
	}
}
