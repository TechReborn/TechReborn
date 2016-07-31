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
        if(random.nextInt(2) == 0) {
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

    public static void registerTRVeins() {
        registerOverworldVein(0.83f, 1.5f, 30, 120, primary("copper"));
        registerOverworldVein(0.80f, 1.3f, 30, 100, primary("tin"));

        registerOverworldVein(0.30f, 1.2f, 1, 60, primary("galena"), additional(0.3f, "lead"), additional(0.2f, "silver"));

        registerOverworldVein(0.40f, 2.0f, 1, 35, primary(Blocks.REDSTONE_ORE), additional(0.3f, "ruby"));
        registerOverworldVein(0.30f, 0.7f, 1, 60, primary(Blocks.LAPIS_ORE), additional(0.3f, "sapphire"));
    }

}
