package techreborn.compat.fmp;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.block.Block;
import techreborn.compat.ICompatModule;
import techreborn.init.ModBlocks;


/**
 * Created by modmuss50 on 02/01/2016 for TechReborn-1.7.
 */
public class ForgeMultipartCompat implements ICompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {
        for(Block block : ModBlocks.blocksToCut){
            cuttableBlock(block);
        }
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }

    public void cuttableBlock(Block block){

    }
}
