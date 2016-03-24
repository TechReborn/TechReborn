package techreborn.world;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;
import reborncore.common.misc.ChunkCoord;
import techreborn.Core;
import techreborn.init.ModBlocks;
import techreborn.world.config.OreConfig;
import techreborn.world.config.WorldGenConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Created by modmuss50 on 11/03/2016.
 */
public class TechRebornWorldGen implements IWorldGenerator
{

	private void init()
	{
		defaultConfig = new WorldGenConfig();
		defaultConfig.overworldOres = new ArrayList<>();
		defaultConfig.endOres = new ArrayList<>();
		defaultConfig.neatherOres = new ArrayList<>();

		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Galena"), 8, 16, 10, 60));
		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Iridium"), 1, 1, 10, 60));
		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Ruby"), 6, 3, 10, 60));
		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Sapphire"), 6, 3, 10, 60));
		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Bauxite"), 6, 10, 10, 60));
		defaultConfig.overworldOres
				.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Tetrahedrite"), 6, 16, 10, 60));
		defaultConfig.overworldOres
				.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Cassiterite"), 6, 16, 20, 60));
		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Lead"), 6, 16, 20, 60));
		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Silver"), 6, 16, 20, 60));
		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("copper"), 8, 16, 20, 60));
		defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("tin"), 8, 16, 20, 60));

		defaultConfig.neatherOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Pyrite"), 6, 3, 10, 250));
		defaultConfig.neatherOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Cinnabar"), 6, 3, 10, 250));
		defaultConfig.neatherOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Sphalerite"), 6, 3, 10, 250));

		defaultConfig.endOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Tungston"), 6, 3, 10, 250));
		defaultConfig.endOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Sheldonite"), 6, 3, 10, 250));
		defaultConfig.endOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Peridot"), 6, 3, 10, 250));
		defaultConfig.endOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Sodalite"), 6, 3, 10, 250));
	}

	WorldGenConfig config;
	WorldGenConfig defaultConfig;
	public File configFile;
	public static RubberTreeGenerator treeGenerator = new RubberTreeGenerator();
	public boolean jsonInvalid = false;
	public final TechRebornRetroGen retroGen = new TechRebornRetroGen();

	public void load()
	{
		init();
		if (configFile.exists())
		{
			loadFromJson();
		} else
		{
			config = defaultConfig;
		}
		config.overworldOres.addAll(getMissingOres(config.overworldOres, defaultConfig.overworldOres));
		config.neatherOres.addAll(getMissingOres(config.neatherOres, defaultConfig.neatherOres));
		config.endOres.addAll(getMissingOres(config.endOres, defaultConfig.endOres));
		if (!jsonInvalid)
		{
			save();
		}
	}

	private List<OreConfig> getMissingOres(List<OreConfig> config, List<OreConfig> defaultOres)
	{
		List<OreConfig> missingOres = new ArrayList<>();
		for (OreConfig defaultOre : defaultOres)
		{
			boolean hasFoundOre = false;
			for (OreConfig ore : config)
			{
				if (ore.blockName.equals(defaultOre.blockName) && ore.meta == defaultOre.meta)
				{
					hasFoundOre = true;
					ore.state = defaultOre.state; // Should allow for states to
													// be saved/loaded
				}
			}
			if (!hasFoundOre)
			{
				missingOres.add(defaultOre);
			}
		}
		return missingOres;
	}

	private void loadFromJson()
	{
		try
		{
			Gson gson = new Gson();
			BufferedReader reader = new BufferedReader(new FileReader(configFile));
			Type typeOfHashMap = new TypeToken<WorldGenConfig>()
			{
			}.getType();
			config = gson.fromJson(reader, typeOfHashMap);
		} catch (Exception e)
		{
			Core.logHelper.error(
					"The ores.json file was invalid, bad things are about to happen, I will try and save the world now :");
			config = defaultConfig;
			jsonInvalid = true;
			Core.logHelper.error(
					"The ores.json file was ignored and the default values loaded, you file will NOT be over written");
			e.printStackTrace();
		}
	}

	private void save()
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(config);
		try
		{
			FileWriter writer = new FileWriter(configFile);
			writer.write(json);
			writer.close();
		} catch (IOException e)
		{
			Core.logHelper.error("The ores.json file was invalid, something bad happened");
			e.printStackTrace();
		}
	}

	public List<OreConfig> getAllGenOresFromList(List<OreConfig> configList)
	{
		List<OreConfig> list = new ArrayList<>();
		for (OreConfig config : configList)
		{
			if (config.veinSize != 0 && config.veinsPerChunk != 0 && config.shouldSpawn)
			{
				list.add(config);
			}
		}
		return list;
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider)
	{
		// TODO this could be optimised to not run every chunk gen
		if (!config.generateTechRebornFeatures)
		{
			return;
		}
		boolean genTree = false;
		List<OreConfig> list = new ArrayList<>();
		if (world.provider.isSurfaceWorld())
		{
			list.addAll(getAllGenOresFromList(config.overworldOres));
			genTree = true;
		} else if (world.provider.getDimension() == 0)
		{
			list.addAll(getAllGenOresFromList(config.neatherOres));
		} else if (world.provider.getDimension() == 1)
		{
			list.addAll(getAllGenOresFromList(config.endOres));
		}

		if (!list.isEmpty() && config.generateOres)
		{
			int xPos, yPos, zPos;
			for (OreConfig ore : list)
			{
				WorldGenMinable worldGenMinable = new WorldGenMinable(ore.state, ore.veinSize);
				for (int i = 0; i < ore.veinsPerChunk; i++)
				{
					xPos = chunkX * 16 + random.nextInt(16);
					yPos = 10 + random.nextInt(ore.maxYHeight - ore.minYHeight);
					zPos = chunkZ * 16 + random.nextInt(16);
					BlockPos pos = new BlockPos(xPos, yPos, zPos);
					worldGenMinable.generate(world, random, pos);
				}
			}
		}
		if (genTree && config.rubberTreeConfig.shouldSpawn)
		{
			int chance = config.rubberTreeConfig.chance;
			boolean isValidSpawn = false;
			BiomeGenBase biomeGenBase = world.getBiomeGenForCoords(new BlockPos(chunkX * 16, 72, chunkZ * 16));
			if (BiomeDictionary.isBiomeOfType(biomeGenBase, BiomeDictionary.Type.SWAMP))
			{
				// TODO check the config file for bounds on this, might cause
				// issues
				chance -= random.nextInt(10) + 10;
				isValidSpawn = true;
			}
			if (BiomeDictionary.isBiomeOfType(biomeGenBase, BiomeDictionary.Type.FOREST)
					|| BiomeDictionary.isBiomeOfType(biomeGenBase, BiomeDictionary.Type.JUNGLE))
			{
				chance -= random.nextInt(5) + 3;
				isValidSpawn = true;
			}
			if (!isValidSpawn)
			{
				return;
			}
			if (random.nextInt(chance) == 0)
			{
				int x = (chunkX * 16) + random.nextInt(15);
				int z = (chunkZ * 16) + random.nextInt(15);
				for (int i = 0; i < config.rubberTreeConfig.clusterSize; i++)
				{
					int y = world.getActualHeight() - 1;
					while (world.isAirBlock(new BlockPos(x, y, z)) && y > 0)
					{
						y--;
					}
					treeGenerator.generate(world, random, new BlockPos(x, 72, z));
					x += random.nextInt(16) - 8;
					z += random.nextInt(16) - 8;
				}

			}
			retroGen.markChunk(ChunkCoord.of(chunkX, chunkZ));
		}
	}
}
