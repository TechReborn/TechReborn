package techreborn.world;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;
import techreborn.config.ConfigTechReborn;

import java.util.Random;

/**
 * Created by Mark on 19/02/2016.
 */
public class TreeGenerator implements IWorldGenerator {

	public static RubberTreeGenerator treeGenerator = new RubberTreeGenerator();

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (ConfigTechReborn.RubberTreeGen) {
			int chance = 75;
			boolean isValidSpawn = false;
			BiomeGenBase biomeGenBase = world.getBiomeGenForCoords(new BlockPos(chunkX * 16, 72, chunkZ * 16));
			if (BiomeDictionary.isBiomeOfType(biomeGenBase, BiomeDictionary.Type.SWAMP)) {
				chance -= random.nextInt(10) + 10;
				isValidSpawn = true;
			}
			if (BiomeDictionary.isBiomeOfType(biomeGenBase, BiomeDictionary.Type.FOREST) || BiomeDictionary.isBiomeOfType(biomeGenBase, BiomeDictionary.Type.JUNGLE)) {
				chance -= random.nextInt(5) + 3;
				isValidSpawn = true;
			}
			if (!isValidSpawn) {
				return;
			}
			if (world.provider.isSurfaceWorld()) {
				if (random.nextInt(chance) == 0) {
					int x = (chunkX * 16) + random.nextInt(15);
					int z = (chunkZ * 16) + random.nextInt(15);
					for (int i = 0; i < 7; i++) {
						int y = world.getActualHeight() - 1;
						while (world.isAirBlock(new BlockPos(x, y, z)) && y > 0) {
							y--;
						}
						treeGenerator.generate(world, random, new BlockPos(x, 72, z));
						x += random.nextInt(16) - 8;
						z += random.nextInt(16) - 8;
					}

				}

			}
		}
	}
}
