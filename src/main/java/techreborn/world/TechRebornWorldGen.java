package techreborn.world;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import techreborn.Core;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by modmuss50 on 11/03/2016.
 */
public class TechRebornWorldGen implements IWorldGenerator {

    private void init(){
        defaultConfig = new WorldGenConfig();
        defaultConfig.overworldOres = new ArrayList<>();
        defaultConfig.endOres = new ArrayList<>();
        defaultConfig.neatherOres = new ArrayList<>();

        defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Galena"), 8, 16, 10, 60));
        defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Iridium"), 1, 1, 10, 60));
        defaultConfig.overworldOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Ruby"), 6, 3, 10, 60));
        //TODO add the rest of the overworld ones

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

    public void load(){
        init();
        if(configFile.exists()){
            loadFromJson();
        } else {
            config = defaultConfig;
        }
        config.overworldOres.addAll(getMissingOres(config.overworldOres, defaultConfig.overworldOres));
        config.neatherOres.addAll(getMissingOres(config.neatherOres, defaultConfig.neatherOres));
        config.endOres.addAll(getMissingOres(config.endOres, defaultConfig.endOres));
        save();
    }

    private List<OreConfig> getMissingOres(List<OreConfig> config, List<OreConfig> defaultOres){
        List<OreConfig> missingOres = new ArrayList<>();
        for (OreConfig defaultOre : defaultOres){
            boolean hasFoundOre = false;
            for (OreConfig ore : config){
                if(ore.blockName.equals(defaultOre.blockName) && ore.meta == defaultOre.meta){
                    hasFoundOre = true;
                }
            }
            if(!hasFoundOre){
                missingOres.add(defaultOre);
            }
        }
        return missingOres;
    }

    private void loadFromJson(){
        try {
            Gson gson = new Gson();
            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            Type typeOfHashMap = new TypeToken<WorldGenConfig>(){}.getType();
            config = gson.fromJson(reader, typeOfHashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void save(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(config);
        try {
            FileWriter writer = new FileWriter(configFile);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            Core.logHelper.error("The ores.json file was invalid, bad things are about to happen");
            e.printStackTrace();
        }
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

    }
}
