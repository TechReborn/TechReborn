package techreborn.compat.recipes;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import ic2.api.item.IC2Items;
import mods.natura.common.NContent;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.IndustrialSawmillRecipe;
import techreborn.compat.ICompatModule;
import techreborn.config.ConfigTechReborn;

public class RecipesNatura implements ICompatModule {
    @Override
    public void preInit(FMLPreInitializationEvent event) {


    }

    @Override
    public void init(FMLInitializationEvent event) {
    	if (ConfigTechReborn.AllowNaturaRecipes) {
	        ItemStack pulpStack = OreDictionary.getOres("pulpWood").get(0);
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.tree, 1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(NContent.planks, 6, 0), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.tree, 1, 0), IC2Items.getItem("waterCell"), null, new ItemStack(NContent.planks, 6, 0), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.tree, 1, 0), new ItemStack(Items.water_bucket), null, new ItemStack(NContent.planks, 6, 0), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.tree, 1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(NContent.planks, 6, 1), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.tree, 1, 1), IC2Items.getItem("waterCell"), null, new ItemStack(NContent.planks, 6, 1), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.tree, 1, 1), new ItemStack(Items.water_bucket), null, new ItemStack(NContent.planks, 6, 1), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.tree, 1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(NContent.planks, 6, 2), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.tree, 1, 2), IC2Items.getItem("waterCell"), null, new ItemStack(NContent.planks, 6, 2), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.tree, 1, 2), new ItemStack(Items.water_bucket), null, new ItemStack(NContent.planks, 6, 2), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.tree, 1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(NContent.planks, 6, 5), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.tree, 1, 3), IC2Items.getItem("waterCell"), null, new ItemStack(NContent.planks, 6, 5), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.tree, 1, 3), new ItemStack(Items.water_bucket), null, new ItemStack(NContent.planks, 6, 5), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.redwood, 1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(NContent.planks, 6, 3), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.redwood, 1, 1), IC2Items.getItem("waterCell"), null, new ItemStack(NContent.planks, 6, 3), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.redwood, 1, 1), new ItemStack(Items.water_bucket), null, new ItemStack(NContent.planks, 6, 3), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.willow, 1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(NContent.planks, 6, 10), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.willow, 1, 0), IC2Items.getItem("waterCell"), null, new ItemStack(NContent.planks, 6, 10), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.willow, 1, 0), new ItemStack(Items.water_bucket), null, new ItemStack(NContent.planks, 6, 10), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.rareTree, 1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(NContent.planks, 6, 6), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.rareTree, 1, 0), IC2Items.getItem("waterCell"), null, new ItemStack(NContent.planks, 6, 6), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.rareTree, 1, 0), new ItemStack(Items.water_bucket), null, new ItemStack(NContent.planks, 6, 6), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.rareTree, 1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(NContent.planks, 6, 7), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.rareTree, 1, 1), IC2Items.getItem("waterCell"), null, new ItemStack(NContent.planks, 6, 7), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.rareTree, 1, 1), new ItemStack(Items.water_bucket), null, new ItemStack(NContent.planks, 6, 7), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.rareTree, 1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(NContent.planks, 6, 8), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.rareTree, 1, 2), IC2Items.getItem("waterCell"), null, new ItemStack(NContent.planks, 6, 8), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.rareTree, 1, 2), new ItemStack(Items.water_bucket), null, new ItemStack(NContent.planks, 6, 8), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.rareTree, 1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(NContent.planks, 6, 9), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.rareTree, 1, 3), IC2Items.getItem("waterCell"), null, new ItemStack(NContent.planks, 6, 9), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.rareTree, 1, 3), new ItemStack(Items.water_bucket), null, new ItemStack(NContent.planks, 6, 9), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.darkTree, 1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(NContent.planks, 6, 11), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.darkTree, 1, 0), IC2Items.getItem("waterCell"), null, new ItemStack(NContent.planks, 6, 11), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.darkTree, 1, 0), new ItemStack(Items.water_bucket), null, new ItemStack(NContent.planks, 6, 11), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.darkTree, 1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(NContent.planks, 6, 12), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.darkTree, 1, 1), IC2Items.getItem("waterCell"), null, new ItemStack(NContent.planks, 6, 12), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(NContent.darkTree, 1, 1), new ItemStack(Items.water_bucket), null, new ItemStack(NContent.planks, 6, 12), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
    	}
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }
}
