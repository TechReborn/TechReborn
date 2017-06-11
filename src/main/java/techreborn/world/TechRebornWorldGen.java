/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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
import com.google.gson.reflect.TypeToken;
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
import org.hjson.JsonValue;
import org.hjson.Stringify;
import reborncore.common.misc.ChunkCoord;
import techreborn.Core;
import techreborn.init.ModBlocks;
import techreborn.world.config.OreConfig;
import techreborn.world.config.WorldGenConfig;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by modmuss50 on 11/03/2016.
 */
public class TechRebornWorldGen implements IWorldGenerator {

	public static RubberTreeGenerator treeGenerator = new RubberTreeGenerator();
	public final TechRebornRetroGen retroGen = new TechRebornRetroGen();
	public File configFile;
	public File hConfigFile;
	public boolean jsonInvalid = false;
	public WorldGenConfig config;
	WorldGenConfig defaultConfig;

	private void init() {
		defaultConfig = new WorldGenConfig();
		defaultConfig.overworldOres = new ArrayList<>();
		defaultConfig.endOres = new ArrayList<>();
		defaultConfig.neatherOres = new ArrayList<>();

		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Galena"), 8, 16, 10, 60));
		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Iridium"), 3, 3, 5, 60));
		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Ruby"), 6, 3, 10, 60));
		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Sapphire"), 6, 3, 10, 60));
		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Bauxite"), 6, 10, 10, 60));
		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Lead"), 6, 16, 20, 60));
		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Silver"), 6, 16, 20, 60));
		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("copper"), 8, 16, 20, 60));
		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("tin"), 8, 16, 20, 60));

		defaultConfig.neatherOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Pyrite"), 6, 3, 10, 126));
		defaultConfig.neatherOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Cinnabar"), 6, 3, 10, 126));
		defaultConfig.neatherOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Sphalerite"), 6, 3, 10, 126));

		defaultConfig.endOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Tungsten"), 6, 3, 10, 250));
		defaultConfig.endOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Sheldonite"), 6, 3, 10, 250));
		defaultConfig.endOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Peridot"), 6, 3, 10, 250));
		defaultConfig.endOres.add(new OreConfig(ModBlocks.ORE.getBlockStateFromName("Sodalite"), 6, 3, 10, 250));
	}

	public void load() {
		init();
		//Converts the old format to the new one
		if (configFile.exists()) {
			if (!hConfigFile.exists()) {
				try {
					//Reads json
					BufferedReader reader = new BufferedReader(new FileReader(configFile));
					//Converts to hjson
					String hJson = JsonValue.readHjson(reader).toString(Stringify.HJSON);
					//Saves as the new HJson file
					FileWriter writer = new FileWriter(hConfigFile);
					writer.write(hJson);
					writer.close();
					reader.close();
					//Delete old json file
					configFile.delete();

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (hConfigFile.exists()) {
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
				if (ore.blockName.equals(defaultOre.blockName) && ore.meta == defaultOre.meta) {
					hasFoundOre = true;
					ore.state = defaultOre.state; // Should allow for states to
					// be saved/loaded
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
			BufferedReader reader = new BufferedReader(new FileReader(hConfigFile));
			String jsonString = JsonValue.readHjson(reader).toString();
			Type typeOfHashMap = new TypeToken<WorldGenConfig>() {
			}.getType();
			config = gson.fromJson(jsonString, typeOfHashMap);
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
			Core.logHelper.error(
				"The ores.json file was invalid, bad things are about to happen, I will try and save the world now :");
			config = defaultConfig;
			jsonInvalid = true;
			Core.logHelper.error(
				"The ores.json file was ignored and the default values loaded, you file will NOT be over written");
			e.printStackTrace();
		}
	}

	public void printError(String string) {
		Core.logHelper.error("###############-ERROR-####################");
		Core.logHelper.error("");
		Core.logHelper.error(string);
		Core.logHelper.error("");
		Core.logHelper.error("###############-ERROR-####################");
	}

	private void save() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(config);
		String hJson = JsonValue.readHjson(json).toString(Stringify.HJSON);
		try {
			FileWriter writer = new FileWriter(hConfigFile);
			writer.write(hJson);
			writer.close();
		} catch (IOException e) {
			Core.logHelper.error("The ores.json file was invalid, something bad happened");
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
		if (world.provider.isSurfaceWorld()) {
			list.addAll(getAllGenOresFromList(config.overworldOres));
			genTree = true;
		} else if (world.provider.getDimension() == -1) {
			list.addAll(getAllGenOresFromList(config.neatherOres));
			predicate = BlockMatcher.forBlock(Blocks.NETHERRACK);
		} else if (world.provider.getDimension() == 1) {
			list.addAll(getAllGenOresFromList(config.endOres));

			predicate = BlockMatcher.forBlock(Blocks.END_STONE);
		}

		if (!list.isEmpty() && config.generateOres) {
			int xPos, yPos, zPos;
			for (OreConfig ore : list) {
				WorldGenMinable worldGenMinable = new WorldGenMinable(ore.state, ore.veinSize, predicate);
				if (ore.state != null) {
					for (int i = 0; i < ore.veinsPerChunk; i++) {
						xPos = chunkX * 16 + random.nextInt(16);
						if (ore.maxYHeight == -1 || ore.minYHeight == -1) {
							continue;
						}
						yPos = ore.minYHeight + random.nextInt(ore.maxYHeight - ore.minYHeight);
						zPos = chunkZ * 16 + random.nextInt(16);
						BlockPos pos = new BlockPos(xPos, yPos, zPos);
						if(ore.blockNiceName.equalsIgnoreCase("iridium")){ //Work around for iridium
							BlockPos iridiumPos = pos.add(8, 0, 8); // standard worldgen offset is added here like in WorldGenMinable#generate
							IBlockState blockState = world.getBlockState(iridiumPos);
							if(blockState.getBlock().isReplaceableOreGen(blockState, world, iridiumPos, predicate)){
								world.setBlockState(iridiumPos, ore.state, 2);
							}

						} else {
							try {
								worldGenMinable.generate(world, random, pos);
							} catch (ArrayIndexOutOfBoundsException e) {
								Core.logHelper.error("Something bad is happening during world gen the ore " + ore.blockNiceName + " caused a crash when generating. Report this to the TechReborn devs with a log");
							}
						}


					}
				}
			}
		}
		if (genTree && config.rubberTreeConfig.shouldSpawn) {
			int chance = config.rubberTreeConfig.chance;
			boolean isValidSpawn = false;
			Biome biomeGenBase = world.getBiomeForCoordsBody(new BlockPos(chunkX * 16, 72, chunkZ * 16));
			if (BiomeDictionary.hasType(biomeGenBase, BiomeDictionary.Type.SWAMP)) {
				// TODO check the config file for bounds on this, might cause
				// issues
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
			retroGen.markChunk(ChunkCoord.of(chunkX, chunkZ));
		}
	}
}
