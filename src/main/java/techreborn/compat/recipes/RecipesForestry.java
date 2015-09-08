package techreborn.compat.recipes;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import forestry.api.fuels.FuelManager;
import forestry.api.fuels.GeneratorFuel;
import forestry.core.config.ForestryBlock;
import ic2.api.item.IC2Items;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import techreborn.api.fuel.FluidPowerManager;
import techreborn.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.IndustrialSawmillRecipe;
import techreborn.compat.ICompatModule;
import techreborn.config.ConfigTechReborn;

import java.rmi.UnexpectedException;

public class RecipesForestry implements ICompatModule {
    @Override
    public void preInit(FMLPreInitializationEvent event) {


    }

    @Override
    public void init(FMLInitializationEvent event) {
    	if (ConfigTechReborn.AllowForestryRecipes) {
	        ItemStack pulpStack = OreDictionary.getOres("pulpWood").get(0);
	
	        //Regular wood
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log1.getItemStack(1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks1.getItemStack(6, 0), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log1.getItemStack(1, 0), IC2Items.getItem("waterCell"), null, ForestryBlock.planks1.getItemStack(6, 0), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log1.getItemStack(1, 0), new ItemStack(Items.water_bucket), null, ForestryBlock.planks1.getItemStack(6, 0), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log1.getItemStack(1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks1.getItemStack(6, 1), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log1.getItemStack(1, 1), IC2Items.getItem("waterCell"), null, ForestryBlock.planks1.getItemStack(6, 1), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log1.getItemStack(1, 1), new ItemStack(Items.water_bucket), null, ForestryBlock.planks1.getItemStack(6, 1), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log1.getItemStack(1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks1.getItemStack(6, 2), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log1.getItemStack(1, 2), IC2Items.getItem("waterCell"), null, ForestryBlock.planks1.getItemStack(6, 2), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log1.getItemStack(1, 2), new ItemStack(Items.water_bucket), null, ForestryBlock.planks1.getItemStack(6, 2), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log1.getItemStack(1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks1.getItemStack(6, 3), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log1.getItemStack(1, 3), IC2Items.getItem("waterCell"), null, ForestryBlock.planks1.getItemStack(6, 3), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log1.getItemStack(1, 3), new ItemStack(Items.water_bucket), null, ForestryBlock.planks1.getItemStack(6, 3), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log2.getItemStack(1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks1.getItemStack(6, 4), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log2.getItemStack(1, 0), IC2Items.getItem("waterCell"), null, ForestryBlock.planks1.getItemStack(6, 4), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log2.getItemStack(1, 0), new ItemStack(Items.water_bucket), null, ForestryBlock.planks1.getItemStack(6, 4), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log2.getItemStack(1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks1.getItemStack(6, 5), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log2.getItemStack(1, 1), IC2Items.getItem("waterCell"), null, ForestryBlock.planks1.getItemStack(6, 5), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log2.getItemStack(1, 1), new ItemStack(Items.water_bucket), null, ForestryBlock.planks1.getItemStack(6, 5), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log2.getItemStack(1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks1.getItemStack(6, 6), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log2.getItemStack(1, 2), IC2Items.getItem("waterCell"), null, ForestryBlock.planks1.getItemStack(6, 6), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log2.getItemStack(1, 2), new ItemStack(Items.water_bucket), null, ForestryBlock.planks1.getItemStack(6, 6), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log2.getItemStack(1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks1.getItemStack(6, 7), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log2.getItemStack(1, 3), IC2Items.getItem("waterCell"), null, ForestryBlock.planks1.getItemStack(6, 7), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log2.getItemStack(1, 3), new ItemStack(Items.water_bucket), null, ForestryBlock.planks1.getItemStack(6, 7), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log3.getItemStack(1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks1.getItemStack(6, 8), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log3.getItemStack(1, 0), IC2Items.getItem("waterCell"), null, ForestryBlock.planks1.getItemStack(6, 8), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log3.getItemStack(1, 0), new ItemStack(Items.water_bucket), null, ForestryBlock.planks1.getItemStack(6, 8), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log3.getItemStack(1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks1.getItemStack(6, 9), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log3.getItemStack(1, 1), IC2Items.getItem("waterCell"), null, ForestryBlock.planks1.getItemStack(6, 9), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log3.getItemStack(1, 1), new ItemStack(Items.water_bucket), null, ForestryBlock.planks1.getItemStack(6, 9), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log3.getItemStack(1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks1.getItemStack(6, 10), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log3.getItemStack(1, 2), IC2Items.getItem("waterCell"), null, ForestryBlock.planks1.getItemStack(6, 10), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log3.getItemStack(1, 2), new ItemStack(Items.water_bucket), null, ForestryBlock.planks1.getItemStack(6, 10), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log3.getItemStack(1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks1.getItemStack(6, 11), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log3.getItemStack(1, 3), IC2Items.getItem("waterCell"), null, ForestryBlock.planks1.getItemStack(6, 11), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log3.getItemStack(1, 3), new ItemStack(Items.water_bucket), null, ForestryBlock.planks1.getItemStack(6, 11), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log4.getItemStack(1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks1.getItemStack(6, 12), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log4.getItemStack(1, 0), IC2Items.getItem("waterCell"), null, ForestryBlock.planks1.getItemStack(6, 12), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log4.getItemStack(1, 0), new ItemStack(Items.water_bucket), null, ForestryBlock.planks1.getItemStack(6, 12), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log4.getItemStack(1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks1.getItemStack(6, 13), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log4.getItemStack(1, 1), IC2Items.getItem("waterCell"), null, ForestryBlock.planks1.getItemStack(6, 13), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log4.getItemStack(1, 1), new ItemStack(Items.water_bucket), null, ForestryBlock.planks1.getItemStack(6, 13), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log4.getItemStack(1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks1.getItemStack(6, 14), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log4.getItemStack(1, 2), IC2Items.getItem("waterCell"), null, ForestryBlock.planks1.getItemStack(6, 14), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log4.getItemStack(1, 2), new ItemStack(Items.water_bucket), null, ForestryBlock.planks1.getItemStack(6, 14), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log4.getItemStack(1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks1.getItemStack(6, 15), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log4.getItemStack(1, 3), IC2Items.getItem("waterCell"), null, ForestryBlock.planks1.getItemStack(6, 15), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log4.getItemStack(1, 3), new ItemStack(Items.water_bucket), null, ForestryBlock.planks1.getItemStack(6, 15), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log5.getItemStack(1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks2.getItemStack(6, 0), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log5.getItemStack(1, 0), IC2Items.getItem("waterCell"), null, ForestryBlock.planks2.getItemStack(6, 0), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log5.getItemStack(1, 0), new ItemStack(Items.water_bucket), null, ForestryBlock.planks2.getItemStack(6, 0), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log5.getItemStack(1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks2.getItemStack(6, 1), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log5.getItemStack(1, 1), IC2Items.getItem("waterCell"), null, ForestryBlock.planks2.getItemStack(6, 1), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log5.getItemStack(1, 1), new ItemStack(Items.water_bucket), null, ForestryBlock.planks2.getItemStack(6, 1), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log5.getItemStack(1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks2.getItemStack(6, 2), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log5.getItemStack(1, 2), IC2Items.getItem("waterCell"), null, ForestryBlock.planks2.getItemStack(6, 2), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log5.getItemStack(1, 2), new ItemStack(Items.water_bucket), null, ForestryBlock.planks2.getItemStack(6, 2), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log5.getItemStack(1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks2.getItemStack(6, 3), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log5.getItemStack(1, 3), IC2Items.getItem("waterCell"), null, ForestryBlock.planks2.getItemStack(6, 3), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log5.getItemStack(1, 3), new ItemStack(Items.water_bucket), null, ForestryBlock.planks2.getItemStack(6, 3), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log6.getItemStack(1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks2.getItemStack(6, 4), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log6.getItemStack(1, 0), IC2Items.getItem("waterCell"), null, ForestryBlock.planks2.getItemStack(6, 4), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log6.getItemStack(1, 0), new ItemStack(Items.water_bucket), null, ForestryBlock.planks2.getItemStack(6, 4), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log6.getItemStack(1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks2.getItemStack(6, 5), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log6.getItemStack(1, 1), IC2Items.getItem("waterCell"), null, ForestryBlock.planks2.getItemStack(6, 5), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log6.getItemStack(1, 1), new ItemStack(Items.water_bucket), null, ForestryBlock.planks2.getItemStack(6, 5), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log6.getItemStack(1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks2.getItemStack(6, 6), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log6.getItemStack(1, 2), IC2Items.getItem("waterCell"), null, ForestryBlock.planks2.getItemStack(6, 6), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log6.getItemStack(1, 2), new ItemStack(Items.water_bucket), null, ForestryBlock.planks2.getItemStack(6, 6), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log6.getItemStack(1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks2.getItemStack(6, 7), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log6.getItemStack(1, 3), IC2Items.getItem("waterCell"), null, ForestryBlock.planks2.getItemStack(6, 7), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log6.getItemStack(1, 3), new ItemStack(Items.water_bucket), null, ForestryBlock.planks2.getItemStack(6, 7), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log7.getItemStack(1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks2.getItemStack(6, 8), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log7.getItemStack(1, 0), IC2Items.getItem("waterCell"), null, ForestryBlock.planks2.getItemStack(6, 8), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log7.getItemStack(1, 0), new ItemStack(Items.water_bucket), null, ForestryBlock.planks2.getItemStack(6, 8), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log7.getItemStack(1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks2.getItemStack(6, 9), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log7.getItemStack(1, 1), IC2Items.getItem("waterCell"), null, ForestryBlock.planks2.getItemStack(6, 9), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log7.getItemStack(1, 1), new ItemStack(Items.water_bucket), null, ForestryBlock.planks2.getItemStack(6, 9), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log7.getItemStack(1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks2.getItemStack(6, 10), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log7.getItemStack(1, 2), IC2Items.getItem("waterCell"), null, ForestryBlock.planks2.getItemStack(6, 10), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log7.getItemStack(1, 2), new ItemStack(Items.water_bucket), null, ForestryBlock.planks2.getItemStack(6, 10), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log7.getItemStack(1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks2.getItemStack(6, 11), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log7.getItemStack(1, 3), IC2Items.getItem("waterCell"), null, ForestryBlock.planks2.getItemStack(6, 11), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log7.getItemStack(1, 3), new ItemStack(Items.water_bucket), null, ForestryBlock.planks2.getItemStack(6, 11), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log8.getItemStack(1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.planks2.getItemStack(6, 12), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log8.getItemStack(1, 0), IC2Items.getItem("waterCell"), null, ForestryBlock.planks2.getItemStack(6, 12), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.log8.getItemStack(1, 0), new ItemStack(Items.water_bucket), null, ForestryBlock.planks2.getItemStack(6, 12), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        //Fireproof wood
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog1.getItemStack(1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks1.getItemStack(6, 0), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog1.getItemStack(1, 0), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 0), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog1.getItemStack(1, 0), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 0), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog1.getItemStack(1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks1.getItemStack(6, 1), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog1.getItemStack(1, 1), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 1), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog1.getItemStack(1, 1), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 1), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog1.getItemStack(1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks1.getItemStack(6, 2), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog1.getItemStack(1, 2), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 2), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog1.getItemStack(1, 2), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 2), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog1.getItemStack(1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks1.getItemStack(6, 3), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog1.getItemStack(1, 3), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 3), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog1.getItemStack(1, 3), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 3), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog2.getItemStack(1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks1.getItemStack(6, 4), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog2.getItemStack(1, 0), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 4), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog2.getItemStack(1, 0), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 4), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog2.getItemStack(1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks1.getItemStack(6, 5), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog2.getItemStack(1, 1), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 5), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog2.getItemStack(1, 1), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 5), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog2.getItemStack(1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks1.getItemStack(6, 6), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog2.getItemStack(1, 2), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 6), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog2.getItemStack(1, 2), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 6), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog2.getItemStack(1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks1.getItemStack(6, 7), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog2.getItemStack(1, 3), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 7), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog2.getItemStack(1, 3), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 7), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog3.getItemStack(1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks1.getItemStack(6, 8), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog3.getItemStack(1, 0), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 8), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog3.getItemStack(1, 0), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 8), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog3.getItemStack(1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks1.getItemStack(6, 9), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog3.getItemStack(1, 1), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 9), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog3.getItemStack(1, 1), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 9), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog3.getItemStack(1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks1.getItemStack(6, 10), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog3.getItemStack(1, 2), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 10), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog3.getItemStack(1, 2), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 10), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog3.getItemStack(1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks1.getItemStack(6, 11), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog3.getItemStack(1, 3), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 11), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog3.getItemStack(1, 3), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 11), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog4.getItemStack(1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks1.getItemStack(6, 12), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog4.getItemStack(1, 0), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 12), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog4.getItemStack(1, 0), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 12), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog4.getItemStack(1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks1.getItemStack(6, 13), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog4.getItemStack(1, 1), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 13), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog4.getItemStack(1, 1), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 13), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog4.getItemStack(1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks1.getItemStack(6, 14), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog4.getItemStack(1, 2), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 14), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog4.getItemStack(1, 2), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 14), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog4.getItemStack(1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks1.getItemStack(6, 15), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog4.getItemStack(1, 3), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 15), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog4.getItemStack(1, 3), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks1.getItemStack(6, 15), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog5.getItemStack(1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks2.getItemStack(6, 0), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog5.getItemStack(1, 0), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 0), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog5.getItemStack(1, 0), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 0), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog5.getItemStack(1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks2.getItemStack(6, 1), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog5.getItemStack(1, 1), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 1), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog5.getItemStack(1, 1), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 1), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog5.getItemStack(1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks2.getItemStack(6, 2), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog5.getItemStack(1, 2), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 2), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog5.getItemStack(1, 2), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 2), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog5.getItemStack(1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks2.getItemStack(6, 3), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog5.getItemStack(1, 3), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 3), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog5.getItemStack(1, 3), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 3), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog6.getItemStack(1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks2.getItemStack(6, 4), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog6.getItemStack(1, 0), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 4), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog6.getItemStack(1, 0), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 4), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog6.getItemStack(1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks2.getItemStack(6, 5), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog6.getItemStack(1, 1), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 5), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog6.getItemStack(1, 1), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 5), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog6.getItemStack(1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks2.getItemStack(6, 6), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog6.getItemStack(1, 2), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 6), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog6.getItemStack(1, 2), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 6), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog6.getItemStack(1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks2.getItemStack(6, 7), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog6.getItemStack(1, 3), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 7), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog6.getItemStack(1, 3), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 7), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog7.getItemStack(1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks2.getItemStack(6, 8), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog7.getItemStack(1, 0), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 8), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog7.getItemStack(1, 0), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 8), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog7.getItemStack(1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks2.getItemStack(6, 9), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog7.getItemStack(1, 1), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 9), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog7.getItemStack(1, 1), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 9), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog7.getItemStack(1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks2.getItemStack(6, 10), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog7.getItemStack(1, 2), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 10), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog7.getItemStack(1, 2), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 10), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog7.getItemStack(1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks2.getItemStack(6, 11), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog7.getItemStack(1, 3), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 11), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog7.getItemStack(1, 3), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 11), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog8.getItemStack(1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), ForestryBlock.fireproofPlanks2.getItemStack(6, 12), pulpStack, null, 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog8.getItemStack(1, 0), IC2Items.getItem("waterCell"), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 12), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
	        RecipeHandler.addRecipe(new IndustrialSawmillRecipe(ForestryBlock.fireproofLog8.getItemStack(1, 0), new ItemStack(Items.water_bucket), null, ForestryBlock.fireproofPlanks2.getItemStack(6, 12), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
    	}
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
		for(Object fuel : FuelManager.generatorFuel.entrySet().toArray()){
			if(fuel instanceof GeneratorFuel){
				GeneratorFuel generatorFuel = (GeneratorFuel) fuel;
				FluidPowerManager.fluidPowerValues.put(generatorFuel.fuelConsumed.getFluid(), (double) (generatorFuel.eu / generatorFuel.rate));
			} else {
				try {
					throw new UnexpectedException(fuel + " should be an instace of GeneratorFuel");
				} catch (UnexpectedException e) {
					e.printStackTrace();
				}
			}
		}
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }
}

