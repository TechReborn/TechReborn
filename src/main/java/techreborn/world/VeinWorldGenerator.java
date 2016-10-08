package techreborn.world;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import techreborn.init.ModBlocks;
import techreborn.world.veins.VeinGenerator;

import java.util.Random;

public enum VeinWorldGenerator implements IWorldGenerator {

	INSTANCE;

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (random.nextInt(2) == 0) {
			VeinGenerator.generateRandomVein(random, chunkX, chunkZ, world);
		}
	}

	private static Pair<Float, IBlockState> additional(float chance, String name) {
		return ImmutablePair.of(chance, ModBlocks.ore.getBlockStateFromName(name));
	}

	private static Pair<Float, IBlockState> primary(String name) {
		return ImmutablePair.of(1.00f, ModBlocks.ore.getBlockStateFromName(name));
	}

	private static Pair<Float, IBlockState> primary(Block block) {
		return ImmutablePair.of(1.00f, block.getDefaultState());
	}

	private static void registerOverworldVein(float chance, float avrSize, int minHeight, int maxHeight, Pair<Float, IBlockState>... varargs) {
		VeinGenerator.registerVein(0, chance, avrSize, minHeight, maxHeight, varargs);
	}

	private static void registerNetherVein(float chance, float avrSize, Pair<Float, IBlockState>... varargs) {
		VeinGenerator.registerVein(-1, chance, avrSize, 1, 128, varargs);
	}

	private static void registerEndVein(float chance, float avrSize, Pair<Float, IBlockState>... varargs) {
		VeinGenerator.registerVein(1, chance, avrSize, 1, 64, varargs);
	}

	public static void registerTRVeins() {
		//Overworld veins
		registerOverworldVein(0.05f, 0.4f, 1, 60,
			primary("iridium"));

		registerOverworldVein(0.34f, 0.7f, 1, 80,
			primary("bauxite"));

		registerOverworldVein(0.67f, 1.4f, 30, 120,
			primary("copper"));

		registerOverworldVein(0.64f, 1.3f, 30, 120,
			primary("tin"));

		registerOverworldVein(0.32f, 0.7f, 1, 40,
			primary("galena"),
			additional(0.3f, "lead"),
			additional(0.2f, "silver"));

		registerOverworldVein(0.37f, 1.2f, 1, 30,
			primary(Blocks.REDSTONE_ORE),
			additional(0.3f, "ruby"));

		registerOverworldVein(0.20f, 0.74f, 1, 40,
			primary(Blocks.LAPIS_ORE),
			additional(0.37f, "sapphire"));

		//Nether veins
		registerNetherVein(0.73f, 0.92f,
			primary("pyrite"));

		registerNetherVein(0.47f, 0.84f,
			primary("sphalerite"),
			additional(0.2f, "cinnabar"));

		//End veins
		registerEndVein(0.26f, 0.6f,
			primary("sheldonite"),
			additional(0.3f, "tungsten"));

		registerEndVein(0.67f, 0.84f,
			primary("sodalite"),
			additional(0.4f, "peridot"));

	}

}
