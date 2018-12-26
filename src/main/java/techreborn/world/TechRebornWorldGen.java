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

import com.google.common.base.Predicate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.apache.commons.io.FileUtils;
import reborncore.common.misc.ChunkCoord;
import techreborn.TechReborn;
import techreborn.world.config.OreConfig;
import techreborn.world.config.WorldGenConfig;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by modmuss50 on 11/03/2016.
 */
public class TechRebornWorldGen implements IWorldGenerator {

	public static RubberTreeGenerator treeGenerator = new RubberTreeGenerator();
	public final TechRebornRetroGen retroGen = new TechRebornRetroGen();
	public File configFile;
	public boolean jsonInvalid = false;
	public WorldGenConfig config;
	WorldGenConfig defaultConfig;

	private void init() {
		defaultConfig = new WorldGenConfig();
		defaultConfig.overworldOres = new ArrayList<>();
		defaultConfig.endOres = new ArrayList<>();
		defaultConfig.neatherOres = new ArrayList<>();
// TODO: Fix block
//		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Galena"), 8, 16, 10, 60));
//		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Iridium"), 3, 3, 5, 60));
//		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Ruby"), 6, 3, 10, 60));
//		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Sapphire"), 6, 3, 10, 60));
//		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Bauxite"), 6, 10, 10, 60));
//		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Lead"), 6, 16, 20, 60));
//		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Silver"), 6, 16, 20, 60));
//		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("copper"), 8, 16, 20, 60));
//		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("tin"), 8, 16, 20, 60));
//
//		defaultConfig.neatherOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Pyrite"), 6, 3, 10, 126));
//		defaultConfig.neatherOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Cinnabar"), 6, 3, 10, 126));
//		defaultConfig.neatherOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Sphalerite"), 6, 3, 10, 126));
//
//		defaultConfig.endOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Tungsten"), 6, 3, 10, 250));
//		defaultConfig.endOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Sheldonite"), 6, 3, 10, 250));
//		defaultConfig.endOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Peridot"), 6, 3, 10, 250));
//		defaultConfig.endOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Sodalite"), 6, 3, 10, 250));
	}

	public void load() {
		init();
		if (configFile.exists()) {
			loadFromJson();
		} else {
			config = defaultConfig;
			jsonInvalid = true;
		}
		config.overworldOres.addAll(getMissingOres(config.overworldOres, defaultConfig.overworldOres));
		config.neatherOres.addAll(getMissingOres(config.neatherOres, defaultConfig.neatherOres));
		config.endOres.addAll(getMissingOres(config.endOres, defaultConfig.endOres));
		if (jsonInvalid) {
			save();
		}
	}

	private List<OreConfig> getMissingOres(List<OreConfig> config, List<OreConfig> defaultOres) {
		List<OreConfig> missingOres = new ArrayList<>();
		for (OreConfig defaultOre : defaultOres) {
			boolean hasFoundOre = false;
			for (OreConfig ore : config) {
				if (ore.blockName.replaceAll(":", ".").equals(defaultOre.blockName.replaceAll(":", ".")) && ore.meta == defaultOre.meta) {
					hasFoundOre = true;
					// Should allow for states to be saved/loaded
					ore.state = defaultOre.state;
					break;
				}
			}
			if (!hasFoundOre) {
				missingOres.add(defaultOre);
			}
		}
		return missingOres;
	}

	private void loadFromJson() {
		try {
			Gson gson = new Gson();
			String jsonString = FileUtils.readFileToString(configFile, Charset.defaultCharset());
			config = gson.fromJson(jsonString, WorldGenConfig.class);
			//			ArrayUtils.addAll(config.endOres, config.neatherOres, config.overworldOres).stream().forEach(oreConfig -> {
			//				if (oreConfig.minYHeight > oreConfig.maxYHeight) {
			//					printError(oreConfig.blockName + " ore generation value is invalid, the min y height is bigger than the max y height, this ore value will be disabled in code");
			//
			//					oreConfig.minYHeight = -1;
			//					oreConfig.maxYHeight = -1;
			//				}
			//
			//				if (oreConfig.minYHeight < 0 || oreConfig.maxYHeight < 0) {
			//					printError(oreConfig.blockName + " ore generation value is invalid, the min y height or the max y height is less than 0, this ore value will be disabled in code");
			//					oreConfig.minYHeight = -1;
			//					oreConfig.maxYHeight = -1;
			//				}
			//
			//			});
		} catch (Exception e) {
			TechReborn.LOGGER.error(
				"The ores.json file was invalid, bad things are about to happen, I will try and save the world now :");
			config = defaultConfig;
			jsonInvalid = true;
			TechReborn.LOGGER.error(
				"The ores.json file was ignored and the default values loaded, you file will NOT be over written");
			e.printStackTrace();
		}
	}

	public void printError(String string) {
		TechReborn.LOGGER.error("###############-ERROR-####################");
		TechReborn.LOGGER.error("");
		TechReborn.LOGGER.error(string);
		TechReborn.LOGGER.error("");
		TechReborn.LOGGER.error("###############-ERROR-####################");
	}

	private void save() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(config);
		try {
			FileUtils.writeStringToFile(configFile, json, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<OreConfig> getAllGenOresFromList(List<OreConfig> configList) {
		List<OreConfig> list = new ArrayList<>();
		for (OreConfig config : configList) {
			if (config.veinSize != 0 && config.veinsPerChunk != 0 && config.shouldSpawn) {
				list.add(config);
			}
		}
		return list;
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
	                     IChunkProvider chunkProvider) {
		// TODO this could be optimised to not run every chunk gen
		if (!config.generateTechRebornFeatures) {
			return;
		}
		boolean genTree = false;
		List<OreConfig> list = new ArrayList<>();
		Predicate<IBlockState> predicate = BlockMatcher.forBlock(Blocks.STONE);
		if (world.provider.getDimension() == -1) {
			list.addAll(getAllGenOresFromList(config.neatherOres));
			predicate = BlockMatcher.forBlock(Blocks.NETHERRACK);
		} else if (world.provider.getDimension() == 1) {
			list.addAll(getAllGenOresFromList(config.endOres));
			predicate = BlockMatcher.forBlock(Blocks.END_STONE);
		}
		else if (config.overworldOresInModdedDims || world.provider.getDimension() == 0) {
			list.addAll(getAllGenOresFromList(config.overworldOres));
			genTree = true;				
		}
		
		if (!list.isEmpty() && config.generateOres) {
			int xPos, yPos, zPos;
			for (OreConfig ore : list) {
				WorldGenMinable worldGenMinable = new WorldGenMinable(ore.state, ore.veinSize, predicate);
				if (ore.state == null) {
					continue;
				}
				for (int i = 0; i < ore.veinsPerChunk; i++) {
					xPos = chunkX * 16 + random.nextInt(16);
					if (ore.maxYHeight == -1 || ore.minYHeight == -1) {
						continue;
					}
					yPos = ore.minYHeight + random.nextInt(ore.maxYHeight - ore.minYHeight);
					zPos = chunkZ * 16 + random.nextInt(16);
					BlockPos pos = new BlockPos(xPos, yPos, zPos);

					if (ore.veinSize < 4) {
						// Workaround for small veins
						for (int j = 1; j < ore.veinSize; j++) {
							// standard worldgen offset is added here like in WorldGenMinable#generate
							BlockPos smallVeinPos = pos.add(8, 0, 8);
							smallVeinPos.add(random.nextInt(2), random.nextInt(2), random.nextInt(2));
							IBlockState blockState = world.getBlockState(smallVeinPos);
							if (blockState.getBlock().isReplaceableOreGen(blockState, world, smallVeinPos, predicate)) {
								world.setBlockState(smallVeinPos, ore.state, 2);
							}
						}
					} else {
						try {
							worldGenMinable.generate(world, random, pos);
						} catch (ArrayIndexOutOfBoundsException e) {
							TechReborn.LOGGER.error("Something bad is happening during world gen the ore "
									+ ore.blockNiceName
									+ " caused a crash when generating. Report this to the TechReborn devs with a log");
						}
					}
				}
			}
		}
		if (genTree && config.rubberTreeConfig.shouldSpawn) {
			int chance = config.rubberTreeConfig.chance;
			boolean isValidSpawn = false;
			Biome biomeGenBase = world.getBiomeForCoordsBody(new BlockPos(chunkX * 16, 72, chunkZ * 16));
			if(biomeGenBase.getRegistryName() != null && Arrays.asList(config.rubberTreeConfig.rubberTreeBiomeBlacklist).contains(biomeGenBase.getRegistryName().toString())){
				return;
			}
			if (BiomeDictionary.hasType(biomeGenBase, BiomeDictionary.Type.SWAMP)) {
				// TODO check the config file for bounds on this, might cause issues
				chance -= random.nextInt(10) + 10;
				isValidSpawn = true;
			}
			if (BiomeDictionary.hasType(biomeGenBase, BiomeDictionary.Type.FOREST)
				|| BiomeDictionary.hasType(biomeGenBase, BiomeDictionary.Type.JUNGLE)) {
				chance -= random.nextInt(5) + 3;
				isValidSpawn = true;
			}
			if (!isValidSpawn) {
				return;
			}
			if (chance <= 0) {
				chance = 1;
			}
			if (random.nextInt(chance) == 0) {
				int x = chunkX * 16;
				int z = chunkZ * 16;
				for (int i = 0; i < config.rubberTreeConfig.clusterSize; i++) {
					treeGenerator.generate(world, random, new BlockPos(x, 72, z));
				}
			}
		}
		retroGen.markChunk(ChunkCoord.of(chunkX, chunkZ));
	}
}
