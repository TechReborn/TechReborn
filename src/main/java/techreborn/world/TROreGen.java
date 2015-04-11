package techreborn.world;

import java.util.Random;

import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import cpw.mods.fml.common.IWorldGenerator;

public class TROreGen implements IWorldGenerator{
	public static ConfigTechReborn config;
	
	WorldGenMinable oreGalena;
	WorldGenMinable oreIridium;
	WorldGenMinable oreRuby;
	WorldGenMinable oreSapphire;
	WorldGenMinable oreBauxite;
	WorldGenMinable orePyrite;
	WorldGenMinable oreCinnabar;
	WorldGenMinable oreSphalerite;
	WorldGenMinable oreTungston;
	WorldGenMinable oreSheldonite;
	WorldGenMinable oreOlivine;
	WorldGenMinable oreSodalite;
	
	public TROreGen()
	{
		//World
		oreGalena = new WorldGenMinable(ModBlocks.ore, 0, 8, Blocks.stone);
		oreIridium = new WorldGenMinable(ModBlocks.ore, 1, 8, Blocks.stone);
		oreRuby = new WorldGenMinable(ModBlocks.ore, 2, 8, Blocks.stone);
		oreSapphire = new WorldGenMinable(ModBlocks.ore, 3, 8, Blocks.stone);
		oreBauxite = new WorldGenMinable(ModBlocks.ore, 4, 8, Blocks.stone);
		//Nether
		orePyrite = new WorldGenMinable(ModBlocks.ore, 5, 8, Blocks.netherrack);
		oreCinnabar = new WorldGenMinable(ModBlocks.ore, 6, 8, Blocks.netherrack);
		oreSphalerite = new WorldGenMinable(ModBlocks.ore, 7, 8, Blocks.netherrack);
		//End
		oreTungston = new WorldGenMinable(ModBlocks.ore, 8, 8, Blocks.end_stone);
		oreSheldonite = new WorldGenMinable(ModBlocks.ore, 9, 8, Blocks.end_stone);
		oreOlivine = new WorldGenMinable(ModBlocks.ore, 10, 8, Blocks.end_stone);
		oreSodalite = new WorldGenMinable(ModBlocks.ore, 11, 8, Blocks.end_stone);
	}
	
	@Override
	public void generate(Random random, int xChunk, int zChunk, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) 
	{
		if(world.provider.isSurfaceWorld())
		{
			generateUndergroundOres(random, xChunk * 16, zChunk * 16, world);
		}
		else if(world.provider.isHellWorld)
		{
			generateHellOres(random, xChunk * 16, zChunk * 16, world);
		}
		else
		{
			generateEndOres(random, xChunk * 16, zChunk * 16, world);
		}
		
	}
	
	void generateUndergroundOres (Random random, int xChunk, int zChunk, World world)
    {
        int xPos, yPos, zPos;
        if (config.GalenaOreTrue)
        {
            for (int i = 0; i <= 16; i++)
            {
                xPos = xChunk + random.nextInt(16);
                yPos = 60 + random.nextInt(60 - 20);
                zPos = zChunk + random.nextInt(16);
                oreGalena.generate(world, random, xPos, yPos, zPos);
            }
        }
        if (config.IridiumOreTrue)
        {
            for (int i = 0; i <= 16; i++)
            {
                xPos = xChunk + random.nextInt(16);
                yPos = 60 + random.nextInt(60 - 20);
                zPos = zChunk + random.nextInt(16);
                oreIridium.generate(world, random, xPos, yPos, zPos);
            }
        }
        if (config.RubyOreTrue)
        {
            for (int i = 0; i <= 16; i++)
            {
                xPos = xChunk + random.nextInt(16);
                yPos = 60 + random.nextInt(60 - 20);
                zPos = zChunk + random.nextInt(16);
                oreRuby.generate(world, random, xPos, yPos, zPos);
            }
        }
        if (config.SapphireOreTrue)
        {
            for (int i = 0; i <= 16; i++)
            {
                xPos = xChunk + random.nextInt(16);
                yPos = 60 + random.nextInt(60 - 20);
                zPos = zChunk + random.nextInt(16);
                oreSapphire.generate(world, random, xPos, yPos, zPos);
            }
        }
        if (config.BauxiteOreTrue)
        {
            for (int i = 0; i <= 16; i++)
            {
                xPos = xChunk + random.nextInt(16);
                yPos = 60 + random.nextInt(60 - 20);
                zPos = zChunk + random.nextInt(16);
                oreBauxite.generate(world, random, xPos, yPos, zPos);
            }
        }
    }
	
	void generateHellOres (Random random, int xChunk, int zChunk, World world)
    {
		int xPos, yPos, zPos;
        if (config.PyriteOreTrue)
        {
            for (int i = 0; i <= 16; i++)
            {
                xPos = xChunk + random.nextInt(16);
                yPos = 60 + random.nextInt(60 - 20);
                zPos = zChunk + random.nextInt(16);
                orePyrite.generate(world, random, xPos, yPos, zPos);
            }
        }
        if (config.CinnabarOreTrue)
        {
            for (int i = 0; i <= 16; i++)
            {
                xPos = xChunk + random.nextInt(16);
                yPos = 60 + random.nextInt(60 - 20);
                zPos = zChunk + random.nextInt(16);
                oreCinnabar.generate(world, random, xPos, yPos, zPos);
            }
        }
        if (config.SphaleriteOreTrue)
        {
            for (int i = 0; i <= 16; i++)
            {
                xPos = xChunk + random.nextInt(16);
                yPos = 60 + random.nextInt(60 - 20);
                zPos = zChunk + random.nextInt(16);
                oreSphalerite.generate(world, random, xPos, yPos, zPos);
            }
        }
    }
	
	void generateEndOres (Random random, int xChunk, int zChunk, World world)
    {
		int xPos, yPos, zPos;
        if (config.TungstonOreTrue)
        {
            for (int i = 0; i <= 16; i++)
            {
                xPos = xChunk + random.nextInt(16);
                yPos = 60 + random.nextInt(60 - 20);
                zPos = zChunk + random.nextInt(16);
                oreTungston.generate(world, random, xPos, yPos, zPos);
            }
        }
        if (config.SheldoniteOreTrue)
        {
            for (int i = 0; i <= 16; i++)
            {
                xPos = xChunk + random.nextInt(16);
                yPos = 60 + random.nextInt(60 - 20);
                zPos = zChunk + random.nextInt(16);
                oreSheldonite.generate(world, random, xPos, yPos, zPos);
            }
        }
        if (config.OlivineOreTrue)
        {
            for (int i = 0; i <= 16; i++)
            {
                xPos = xChunk + random.nextInt(16);
                yPos = 60 + random.nextInt(60 - 20);
                zPos = zChunk + random.nextInt(16);
                oreOlivine.generate(world, random, xPos, yPos, zPos);
            }
        }
        if (config.SodaliteOreTrue)
        {
            for (int i = 0; i <= 16; i++)
            {
                xPos = xChunk + random.nextInt(16);
                yPos = 60 + random.nextInt(60 - 20);
                zPos = zChunk + random.nextInt(16);
                oreSodalite.generate(world, random, xPos, yPos, zPos);
            }
        }
    }
}
