package techreborn.compat.recipes;


import biomesoplenty.api.content.BOPCBlocks;
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

public class RecipesBiomesOPlenty implements ICompatModule {
	@Override
	public void preInit(FMLPreInitializationEvent event) {

	}

	@Override
	public void init(FMLInitializationEvent event) {
		ItemStack pulpStack = OreDictionary.getOres("pulpWood").get(0);
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs1, 1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPCBlocks.planks, 6, 0), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs1, 1, 0), IC2Items.getItem("waterCell"), null, new ItemStack(BOPCBlocks.planks, 6, 0), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs1, 1, 0), new ItemStack(Items.water_bucket), null, new ItemStack(BOPCBlocks.planks, 6, 0), pulpStack, new ItemStack(Items.bucket), 200, 30, false));

		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs1, 1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPCBlocks.planks, 6, 1), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs1, 1, 1), IC2Items.getItem("waterCell"), null, new ItemStack(BOPCBlocks.planks, 6, 1), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs1, 1, 1), new ItemStack(Items.water_bucket), null, new ItemStack(BOPCBlocks.planks, 6, 1), pulpStack, new ItemStack(Items.bucket), 200, 30, false));

		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs1, 1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPCBlocks.planks, 6, 2), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs1, 1, 2), IC2Items.getItem("waterCell"), null, new ItemStack(BOPCBlocks.planks, 6, 2), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs1, 1, 2), new ItemStack(Items.water_bucket), null, new ItemStack(BOPCBlocks.planks, 6, 2), pulpStack, new ItemStack(Items.bucket), 200, 30, false));

		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs1, 1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPCBlocks.planks, 6, 3), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs1, 1, 3), IC2Items.getItem("waterCell"), null, new ItemStack(BOPCBlocks.planks, 6, 3), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs1, 1, 3), new ItemStack(Items.water_bucket), null, new ItemStack(BOPCBlocks.planks, 6, 3), pulpStack, new ItemStack(Items.bucket), 200, 30, false));

		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs2, 1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPCBlocks.planks, 6, 4), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs2, 1, 0), IC2Items.getItem("waterCell"), null, new ItemStack(BOPCBlocks.planks, 6, 4), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs2, 1, 0), new ItemStack(Items.water_bucket), null, new ItemStack(BOPCBlocks.planks, 6, 4), pulpStack, new ItemStack(Items.bucket), 200, 30, false));

		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs2, 1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPCBlocks.planks, 6, 5), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs2, 1, 1), IC2Items.getItem("waterCell"), null, new ItemStack(BOPCBlocks.planks, 6, 5), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs2, 1, 1), new ItemStack(Items.water_bucket), null, new ItemStack(BOPCBlocks.planks, 6, 5), pulpStack, new ItemStack(Items.bucket), 200, 30, false));

		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs2, 1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPCBlocks.planks, 6, 6), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs2, 1, 2), IC2Items.getItem("waterCell"), null, new ItemStack(BOPCBlocks.planks, 6, 6), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs2, 1, 2), new ItemStack(Items.water_bucket), null, new ItemStack(BOPCBlocks.planks, 6, 6), pulpStack, new ItemStack(Items.bucket), 200, 30, false));

		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs2, 1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPCBlocks.planks, 6, 7), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs2, 1, 3), IC2Items.getItem("waterCell"), null, new ItemStack(BOPCBlocks.planks, 6, 7), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs2, 1, 3), new ItemStack(Items.water_bucket), null, new ItemStack(BOPCBlocks.planks, 6, 7), pulpStack, new ItemStack(Items.bucket), 200, 30, false));

		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs3, 1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPCBlocks.planks, 6, 8), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs3, 1, 0), IC2Items.getItem("waterCell"), null, new ItemStack(BOPCBlocks.planks, 6, 8), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs3, 1, 0), new ItemStack(Items.water_bucket), null, new ItemStack(BOPCBlocks.planks, 6, 8), pulpStack, new ItemStack(Items.bucket), 200, 30, false));

		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs3, 1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPCBlocks.planks, 6, 9), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs3, 1, 1), IC2Items.getItem("waterCell"), null, new ItemStack(BOPCBlocks.planks, 6, 9), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs3, 1, 1), new ItemStack(Items.water_bucket), null, new ItemStack(BOPCBlocks.planks, 6, 9), pulpStack, new ItemStack(Items.bucket), 200, 30, false));

		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs4, 1, 0), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPCBlocks.planks, 6, 11), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs4, 1, 0), IC2Items.getItem("waterCell"), null, new ItemStack(BOPCBlocks.planks, 6, 11), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs4, 1, 0), new ItemStack(Items.water_bucket), null, new ItemStack(BOPCBlocks.planks, 6, 11), pulpStack, new ItemStack(Items.bucket), 200, 30, false));

		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs4, 1, 1), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPCBlocks.planks, 6, 12), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs4, 1, 1), IC2Items.getItem("waterCell"), null, new ItemStack(BOPCBlocks.planks, 6, 12), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs4, 1, 1), new ItemStack(Items.water_bucket), null, new ItemStack(BOPCBlocks.planks, 6, 12), pulpStack, new ItemStack(Items.bucket), 200, 30, false));

		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs4, 1, 2), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPCBlocks.planks, 6, 13), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs4, 1, 2), IC2Items.getItem("waterCell"), null, new ItemStack(BOPCBlocks.planks, 6, 13), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs4, 1, 2), new ItemStack(Items.water_bucket), null, new ItemStack(BOPCBlocks.planks, 6, 13), pulpStack, new ItemStack(Items.bucket), 200, 30, false));

		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs4, 1, 3), null, new FluidStack(FluidRegistry.WATER, 1000), new ItemStack(BOPCBlocks.planks, 6, 14), pulpStack, null, 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs4, 1, 3), IC2Items.getItem("waterCell"), null, new ItemStack(BOPCBlocks.planks, 6, 14), pulpStack, IC2Items.getItem("cell"), 200, 30, false));
		RecipeHandler.addRecipe(new IndustrialSawmillRecipe(new ItemStack(BOPCBlocks.logs4, 1, 3), new ItemStack(Items.water_bucket), null, new ItemStack(BOPCBlocks.planks, 6, 14), pulpStack, new ItemStack(Items.bucket), 200, 30, false));
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}

	@Override
	public void serverStarting(FMLServerStartingEvent event) {

	}
}
