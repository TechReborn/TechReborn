package techreborn.compat.ic2;

import ic2.api.item.IC2Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import reborncore.common.util.CraftingHelper;
import techreborn.compat.ICompatModule;
import techreborn.init.ModBlocks;

/**
 * Created by Mark on 06/06/2016.
 */
public class RecipesIC2 implements ICompatModule {
    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {
        CraftingHelper.addShapelessRecipe(new ItemStack(ModBlocks.machineframe, 0, 1), IC2Items.getItem("resource","machine"));
        CraftingHelper.addShapelessRecipe( IC2Items.getItem("resource","machine"), new ItemStack(ModBlocks.machineframe, 0, 1));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }
}
