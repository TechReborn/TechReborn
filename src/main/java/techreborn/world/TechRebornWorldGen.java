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
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
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
public class TechRebornWorldGen implements IWorldGenerator
{

	public static RubberTreeGenerator treeGenerator = new RubberTreeGenerator();
	public final TechRebornRetroGen retroGen = new TechRebornRetroGen();
	public File configFile;
	public File hConfigFile;
	public boolean jsonInvalid = false;
	WorldGenConfig config;
	WorldGenConfig defaultConfig;

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

	public void load()
	{
		init();
		//Converts the old format to the new one
		if(configFile.exists()){
			if(!hConfigFile.exists()){
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
		if (hConfigFile.exists())
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
			BufferedReader reader = new BufferedReader(new FileReader(hConfigFile));
			String jsonString = JsonValue.readHjson(reader).toString();
			Type typeOfHashMap = new TypeToken<WorldGenConfig>()
			{
			}.getType();
			config = gson.fromJson(jsonString, typeOfHashMap);
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
		String hJson = JsonValue.readHjson(json).toString(Stringify.HJSON);
		try
		{
			FileWriter writer = new FileWriter(hConfigFile);
			writer.write(hJson);
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
		Predicate<IBlockState> predicate = BlockMatcher.forBlock(Blocks.STONE);
		if (world.provider.isSurfaceWorld())
		{
			list.addAll(getAllGenOresFromList(config.overworldOres));
			genTree = true;
		} else if (world.provider.getDimension() == -1)
		{
			list.addAll(getAllGenOresFromList(config.neatherOres));
			predicate = BlockMatcher.forBlock(Blocks.NETHERRACK);
		} else if (world.provider.getDimension() == 1)
		{
			list.addAll(getAllGenOresFromList(config.endOres));
			predicate = BlockMatcher.forBlock(Blocks.END_STONE);
		}

		if (!list.isEmpty() && config.generateOres)
		{
			int xPos, yPos, zPos;
			for (OreConfig ore : list)
			{
				WorldGenMinable worldGenMinable = new WorldGenMinable(ore.state, ore.veinSize, predicate);
				if(ore.state != null){
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
		}
		if (genTree && config.rubberTreeConfig.shouldSpawn)
		{
			int chance = config.rubberTreeConfig.chance;
			boolean isValidSpawn = false;
			Biome biomeGenBase = world.getBiomeGenForCoords(new BlockPos(chunkX * 16, 72, chunkZ * 16));
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
