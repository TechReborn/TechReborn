package techreborn.compat.fmp;

import codechicken.microblock.BlockMicroMaterial;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import techreborn.compat.ICompatModule;
import techreborn.init.ModBlocks;

import java.util.ArrayList;
import java.util.List;

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
        if(Item.getItemFromBlock(block).getHasSubtypes()){
            List<ItemStack> list = new ArrayList<ItemStack>();
            block.getSubBlocks(null, null, list);
            for (int i = 0; i < list.size(); i++) {
                BlockMicroMaterial.createAndRegister(block, i);
            }
        } else {
            BlockMicroMaterial.createAndRegister(block, 0);
        }
    }
}
