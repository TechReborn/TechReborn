package techreborn.world;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import techreborn.Core;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

import java.util.Random;

public class TROreGen implements IWorldGenerator {
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
    WorldGenMinable orePeridot;
    WorldGenMinable oreSodalite;
    WorldGenMinable oreTetrahedrite;
    WorldGenMinable oreCassiterite;
    WorldGenMinable oreLead;
    WorldGenMinable oreSilver;
    WorldGenMinable oreCopper;
    WorldGenMinable oreTin;

    public TROreGen() {
        // World
        oreGalena = new WorldGenMinable(ModBlocks.ore.getBlockStateFromName("Galena"), ConfigTechReborn.GalenaOreRare);
        oreIridium = new WorldGenMinable(ModBlocks.ore.getBlockStateFromName("Iridium"), ConfigTechReborn.IridiumOreRare);
        oreRuby = new WorldGenMinable(ModBlocks.ore.getBlockStateFromName("Ruby"), ConfigTechReborn.RubyOreRare);
        oreSapphire = new WorldGenMinable(ModBlocks.ore.getBlockStateFromName("Sapphire"), ConfigTechReborn.SapphireOreRare);
        oreBauxite = new WorldGenMinable(ModBlocks.ore.getBlockStateFromName("Bauxite"), ConfigTechReborn.BauxiteOreRare);
        oreTetrahedrite = new WorldGenMinable(ModBlocks.ore.getBlockStateFromName("Tetrahedrite"), ConfigTechReborn.TetrahedriteOreRare);
        oreCassiterite = new WorldGenMinable(ModBlocks.ore.getBlockStateFromName("Cassiterite"), ConfigTechReborn.CassiteriteOreRare);
        oreLead = new WorldGenMinable(ModBlocks.ore.getBlockStateFromName("Lead"), ConfigTechReborn.LeadOreRare);
        oreSilver = new WorldGenMinable(ModBlocks.ore.getBlockStateFromName("Silver"), ConfigTechReborn.SilverOreRare);
        oreCopper = new WorldGenMinable(ModBlocks.ore2.getBlockStateFromName("copper"), ConfigTechReborn.CopperOreRare);
        oreTin = new WorldGenMinable(ModBlocks.ore2.getBlockStateFromName("tin"), ConfigTechReborn.TinOreRare);

        // Nether
        orePyrite = new WorldGenMinable(ModBlocks.ore.getBlockStateFromName("Pyrite"), ConfigTechReborn.PyriteOreRare);
        oreCinnabar = new WorldGenMinable(ModBlocks.ore.getBlockStateFromName("Cinnabar"), ConfigTechReborn.CinnabarOreRare);
        oreSphalerite = new WorldGenMinable(ModBlocks.ore.getBlockStateFromName("Sphalerite"), ConfigTechReborn.SphaleriteOreRare);

        // End
        oreTungston = new WorldGenMinable(ModBlocks.ore.getBlockStateFromName("Tungston"), ConfigTechReborn.TungstenOreRare);
        oreSheldonite = new WorldGenMinable(ModBlocks.ore.getBlockStateFromName("Sheldonite"), ConfigTechReborn.SheldoniteOreRare);
        orePeridot = new WorldGenMinable(ModBlocks.ore.getBlockStateFromName("Peridot"), ConfigTechReborn.PeridotOreRare);
        oreSodalite = new WorldGenMinable(ModBlocks.ore.getBlockStateFromName("Sodalite"), ConfigTechReborn.SodaliteOreRare);

        Core.logHelper.info("WorldGen Loaded");
    }

    public void retroGen(Random random, int chunkX, int chunkZ, World world) {
        //TODO
        generateUndergroundOres(random, chunkX, chunkZ, world);
        generateHellOres(random, chunkX, chunkZ, world);
        generateEndOres(random, chunkX, chunkZ, world);
        world.getChunkFromChunkCoords(chunkX, chunkZ).setChunkModified();
    }

    @Override
    public void generate(Random random, int xChunk, int zChunk, World world,
                         IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

        if (world.provider.isSurfaceWorld()) {
            generateUndergroundOres(random, xChunk * 16, zChunk * 16, world);
        } else if (world.provider.getDimensionId() == 0) {
            generateHellOres(random, xChunk * 16, zChunk * 16, world);
        } else if (world.provider.getDimensionId() == 1) {
            generateEndOres(random, xChunk * 16, zChunk * 16, world);
        }

    }

    void generateUndergroundOres(Random random, int xChunk, int zChunk, World world) {
        int xPos, yPos, zPos;
        if (config.GalenaOreTrue) {
            for (int i = 0; i <= 16; i++) {
                xPos = xChunk + random.nextInt(16);
                yPos = 10 + random.nextInt(60 - 10);
                zPos = zChunk + random.nextInt(16);
                BlockPos pos = new BlockPos(xPos, yPos, zPos);
                oreGalena.generate(world, random, pos);
            }
        }
        if (config.IridiumOreTrue) {
            for (int i = 0; i <= 1; i++) {
                xPos = xChunk + random.nextInt(16);
                yPos = 10 + random.nextInt(60 - 10);
                zPos = zChunk + random.nextInt(1);
                BlockPos pos = new BlockPos(xPos, yPos, zPos);
                oreIridium.generate(world, random, pos);
            }
        }
        if (config.RubyOreTrue) {
            for (int i = 0; i <= 3; i++) {
                xPos = xChunk + random.nextInt(16);
                yPos = 10 + random.nextInt(60 - 10);
                zPos = zChunk + random.nextInt(16);
                BlockPos pos = new BlockPos(xPos, yPos, zPos);
                oreRuby.generate(world, random, pos);
            }
        }
        if (config.SapphireOreTrue) {
            for (int i = 0; i <= 3; i++) {
                xPos = xChunk + random.nextInt(16);
                yPos = 10 + random.nextInt(60 - 10);
                zPos = zChunk + random.nextInt(16);
                BlockPos pos = new BlockPos(xPos, yPos, zPos);
                oreSapphire.generate(world, random, pos);
            }
        }
        if (config.BauxiteOreTrue) {
            for (int i = 0; i <= 10; i++) {
                xPos = xChunk + random.nextInt(16);
                yPos = 10 + random.nextInt(60 - 10);
                zPos = zChunk + random.nextInt(16);
                BlockPos pos = new BlockPos(xPos, yPos, zPos);
                oreBauxite.generate(world, random, pos);
            }
        }
        if (config.TetrahedriteOreTrue) {
            for (int i = 0; i <= 16; i++) {
                xPos = xChunk + random.nextInt(16);
                yPos = 10 + random.nextInt(60 - 10);
                zPos = zChunk + random.nextInt(16);
                BlockPos pos = new BlockPos(xPos, yPos, zPos);
                oreTetrahedrite.generate(world, random, pos);
            }
        }
        if (config.CassiteriteOreTrue) {
            for (int i = 0; i <= 16; i++) {
                xPos = xChunk + random.nextInt(16);
                yPos = 10 + random.nextInt(60 - 20);
                zPos = zChunk + random.nextInt(16);
                BlockPos pos = new BlockPos(xPos, yPos, zPos);
                oreCassiterite.generate(world, random, pos);
            }
        }
        if (config.LeadOreTrue) {
            for (int i = 0; i <= 16; i++) {
                xPos = xChunk + random.nextInt(16);
                yPos = 10 + random.nextInt(60 - 20);
                zPos = zChunk + random.nextInt(16);
                BlockPos pos = new BlockPos(xPos, yPos, zPos);
                oreLead.generate(world, random, pos);
            }
        }
        if (config.SilverOreTrue) {
            for (int i = 0; i <= 16; i++) {
                xPos = xChunk + random.nextInt(16);
                yPos = 10 + random.nextInt(60 - 20);
                zPos = zChunk + random.nextInt(16);
                BlockPos pos = new BlockPos(xPos, yPos, zPos);
                oreSilver.generate(world, random, pos);
            }
        }
        if (config.CopperOreTrue) {
            for (int i = 0; i <= 16; i++) {
                xPos = xChunk + random.nextInt(16);
                yPos = 10 + random.nextInt(60 - 20);
                zPos = zChunk + random.nextInt(16);
                BlockPos pos = new BlockPos(xPos, yPos, zPos);
                oreCopper.generate(world, random, pos);
            }
        }
        if (config.TinOreTrue) {
            for (int i = 0; i <= 16; i++) {
                xPos = xChunk + random.nextInt(16);
                yPos = 10 + random.nextInt(60 - 20);
                zPos = zChunk + random.nextInt(16);
                BlockPos pos = new BlockPos(xPos, yPos, zPos);
                oreTin.generate(world, random, pos);
            }
        }
    }

    void generateHellOres(Random random, int xChunk, int zChunk, World world) {
        int xPos, yPos, zPos;
        if (config.PyriteOreTrue) {
            for (int i = 0; i <= 16; i++) {
                xPos = xChunk + random.nextInt(16);
                yPos = 10 + random.nextInt(60 - 10);
                zPos = zChunk + random.nextInt(16);
                BlockPos pos = new BlockPos(xPos, yPos, zPos);
                orePyrite.generate(world, random, pos);
            }
        }
        if (config.CinnabarOreTrue) {
            for (int i = 0; i <= 16; i++) {
                xPos = xChunk + random.nextInt(16);
                yPos = 10 + random.nextInt(60 - 10);
                zPos = zChunk + random.nextInt(16);
                BlockPos pos = new BlockPos(xPos, yPos, zPos);
                oreCinnabar.generate(world, random, pos);
            }
        }
        if (config.SphaleriteOreTrue) {
            for (int i = 0; i <= 16; i++) {
                xPos = xChunk + random.nextInt(16);
                yPos = 10 + random.nextInt(60 - 10);
                zPos = zChunk + random.nextInt(16);
                BlockPos pos = new BlockPos(xPos, yPos, zPos);
                oreSphalerite.generate(world, random, pos);
            }
        }
    }

    void generateEndOres(Random random, int xChunk, int zChunk, World world) {
        int xPos, yPos, zPos;
        if (config.TungstenOreTrue) {
            for (int i = 0; i <= 2; i++) {
                xPos = xChunk + random.nextInt(16);
                yPos = 10 + random.nextInt(60 - 10);
                zPos = zChunk + random.nextInt(16);
                BlockPos pos = new BlockPos(xPos, yPos, zPos);
                oreTungston.generate(world, random, pos);
            }
        }
        if (config.SheldoniteOreTrue) {
            for (int i = 0; i <= 3; i++) {
                xPos = xChunk + random.nextInt(16);
                yPos = 10 + random.nextInt(60 - 10);
                zPos = zChunk + random.nextInt(16);
                BlockPos pos = new BlockPos(xPos, yPos, zPos);
                oreSheldonite.generate(world, random, pos);
            }
        }
        if (config.PeridotOreTrue) {
            for (int i = 0; i <= 3; i++) {
                xPos = xChunk + random.nextInt(16);
                yPos = 10 + random.nextInt(60 - 10);
                zPos = zChunk + random.nextInt(16);
                BlockPos pos = new BlockPos(xPos, yPos, zPos);
                orePeridot.generate(world, random, pos);
            }
        }
        if (config.SodaliteOreTrue) {
            for (int i = 0; i <= 3; i++) {
                xPos = xChunk + random.nextInt(16);
                yPos = 10 + random.nextInt(60 - 10);
                zPos = zChunk + random.nextInt(16);
                BlockPos pos = new BlockPos(xPos, yPos, zPos);
                oreSodalite.generate(world, random, pos);
            }
        }
    }
}
