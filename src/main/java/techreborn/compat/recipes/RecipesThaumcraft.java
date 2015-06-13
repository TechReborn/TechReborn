package techreborn.compat.recipes;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import ic2.api.item.IC2Items;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.IndustrialSawmillRecipe;
import techreborn.compat.ICompatModule;
import thaumcraft.api.ItemApi;

public class RecipesThaumcraft implements ICompatModule {
    @Override
    public void preInit(FMLPreInitializationEvent event) {


    }

    @Override
    public void init(FMLInitializationEvent event) {
        ItemStack pulpStack = OreDictionary.getOres("pulpWood").get(0);
        ItemStack greatWoodStack = ItemApi.getBlock("blockWoodenDevice", 6);
        greatWoodStack.stackSize = 6;
        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ItemApi.getBlock("blockMagicalLog", 0), null, new FluidStack(FluidRegistry.WATER, 1000), greatWoodStack, pulpStack, null, 200, 30, false));
        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ItemApi.getBlock("blockMagicalLog", 0), IC2Items.getItem("waterCell"), null, greatWoodStack, pulpStack, IC2Items.getItem("cell"), 200, 30, false));
        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ItemApi.getBlock("blockMagicalLog", 0), new ItemStack(Items.water_bucket), null, greatWoodStack, pulpStack, new ItemStack(Items.bucket), 200, 30, false));

        ItemStack silverWooodStack = ItemApi.getBlock("blockWoodenDevice", 7);
        silverWooodStack.stackSize = 6;
        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ItemApi.getBlock("blockMagicalLog", 1), null, new FluidStack(FluidRegistry.WATER, 1000), silverWooodStack, pulpStack, null, 200, 30, false));
        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ItemApi.getBlock("blockMagicalLog", 1), IC2Items.getItem("waterCell"), null, silverWooodStack, pulpStack, IC2Items.getItem("cell"), 200, 30, false));
        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ItemApi.getBlock("blockMagicalLog", 1), new ItemStack(Items.water_bucket), null, silverWooodStack, pulpStack, new ItemStack(Items.bucket), 200, 30, false));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }
}