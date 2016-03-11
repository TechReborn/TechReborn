package techreborn.world;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by modmuss50 on 11/03/2016.
 */
public class TechRebornWorldGen implements IWorldGenerator {

    WorldGenConfig config;
    public File configFile;

    public void load(){
        if(config == null){
            if(configFile.exists()){

            } else {
                addOres();
                save();
            }
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
            e.printStackTrace();
        }
    }

    private void addOres(){
        config = new WorldGenConfig();
        config.overworldOres = new ArrayList<>();
        config.endOres = new ArrayList<>();
        config.neatherOres = new ArrayList<>();

        config.overworldOres.add(new OreConfig(ModBlocks.ore.getBlockStateFromName("Galena"), 8, 16, 10, 60));
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

    }
}
