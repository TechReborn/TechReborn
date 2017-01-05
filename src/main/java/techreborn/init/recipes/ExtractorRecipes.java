package techreborn.init.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import reborncore.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.ExtractorRecipe;
import techreborn.init.ModBlocks;
import techreborn.items.DynamicCell;

/**
 * Created by Prospector
 */
public class ExtractorRecipes extends RecipeMethods {
	public static void init() {
		register(getStack(ModBlocks.RUBBER_SAPLING), false, getMaterial("rubber", Type.PART));
		register(getStack(ModBlocks.RUBBER_LOG), false, getMaterial("rubber", Type.PART));
		register(getStack(Items.SLIME_BALL), getMaterial("rubber", 2, Type.PART));
		register(getMaterial("sap", Type.PART), getMaterial("rubber", 3, Type.PART));
		register(getStack(Blocks.RED_FLOWER), getStack(Items.DYE, 2, 1));
		register(getStack(Blocks.YELLOW_FLOWER), getStack(Items.DYE, 2, 11));
		register(getStack(Blocks.RED_FLOWER, 1, 1), getStack(Items.DYE, 2, 12));
		register(getStack(Blocks.RED_FLOWER, 1, 2), getStack(Items.DYE, 2, 13));
		register(getStack(Blocks.RED_FLOWER, 1, 3), getStack(Items.DYE, 2, 7));
		register(getStack(Blocks.RED_FLOWER, 1, 4), getStack(Items.DYE, 2, 1));
		register(getStack(Blocks.RED_FLOWER, 1, 5), getStack(Items.DYE, 2, 14));
		register(getStack(Blocks.RED_FLOWER, 1, 6), getStack(Items.DYE, 2, 7));
		register(getStack(Blocks.RED_FLOWER, 1, 7), getStack(Items.DYE, 2, 9));
		register(getStack(Blocks.RED_FLOWER, 1, 8), getStack(Items.DYE, 2, 7));
		register(getStack(Blocks.DOUBLE_PLANT), getStack(Items.DYE, 4, 11));
		register(getStack(Blocks.DOUBLE_PLANT, 1, 1), getStack(Items.DYE, 4, 13));
		register(getStack(Blocks.DOUBLE_PLANT, 1, 4), getStack(Items.DYE, 4, 1));
		register(getStack(Blocks.DOUBLE_PLANT, 1, 5), getStack(Items.DYE, 4, 9));
		register(getStack(Blocks.TALLGRASS, 1, 1), getStack(Items.WHEAT_SEEDS));
		register(getStack(Blocks.TALLGRASS, 1, 2), getStack(Items.WHEAT_SEEDS));
		register(getStack(Blocks.DOUBLE_PLANT, 1, 2), getStack(Items.WHEAT_SEEDS, 2));
		register(getStack(Blocks.DOUBLE_PLANT, 1, 3), getStack(Items.WHEAT_SEEDS, 2));
		register(getStack(Blocks.DEADBUSH, 1, 0), getStack(Items.STICK));
		for (int i = 1; i < 15; i++)
			register(getStack(Blocks.WOOL, 1, i), getStack(Blocks.WOOL, 1, 0));
		for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
			register(DynamicCell.getCellWithFluid(fluid), DynamicCell.getEmptyCell(1));
		}
	}

	static void register(ItemStack input, ItemStack output) {
		register(input, true, output);
	}

	static void register(ItemStack input, boolean oreDict, ItemStack output) {
		RecipeHandler.addRecipe(new ExtractorRecipe(input, output, 400, 2, oreDict));
	}
}
