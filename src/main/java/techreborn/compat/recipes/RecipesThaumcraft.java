package techreborn.compat.recipes;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import ic2.api.item.IC2Items;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.IndustrialSawmillRecipe;
import techreborn.compat.ICompatModule;
import thaumcraft.api.blocks.BlocksTC;

public class RecipesThaumcraft implements ICompatModule {
    @Override
    public void preInit(FMLPreInitializationEvent event) {


    }

    @Override
    public void init(FMLInitializationEvent event) {
        ItemStack pulpStack = OreDictionary.getOres("pulpWood").get(0);
        //ItemStack greatWoodStack = BlocksTC.getBlock("blockWoodenDevice", 6);
        ItemStack greatWoodStack = null;
        //SOMEONE WHO KNOWS TC FIX THIS
//        greatWoodStack.stackSize = 6;
        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BlocksTC.log), null, new FluidStack(FluidRegistry.WATER, 1000), greatWoodStack, pulpStack, null, 200, 30, false));
        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BlocksTC.log), IC2Items.getItem("waterCell"), null, greatWoodStack, pulpStack, IC2Items.getItem("cell"), 200, 30, false));
        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BlocksTC.log), new ItemStack(Items.water_bucket), null, greatWoodStack, pulpStack, new ItemStack(Items.bucket), 200, 30, false));

        //ItemStack silverWoodStack = ItemApi.getBlock("blockWoodenDevice", 7);
        ItemStack silverWoodStack = null;
        //silverWoodStack.stackSize = 6;
        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BlocksTC.log, 1), null, new FluidStack(FluidRegistry.WATER, 1000), silverWoodStack, pulpStack, null, 200, 30, false));
        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BlocksTC.log, 1), IC2Items.getItem("waterCell"), null, silverWoodStack, pulpStack, IC2Items.getItem("cell"), 200, 30, false));
        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BlocksTC.log, 1), new ItemStack(Items.water_bucket), null, silverWoodStack, pulpStack, new ItemStack(Items.bucket), 200, 30, false));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }
}