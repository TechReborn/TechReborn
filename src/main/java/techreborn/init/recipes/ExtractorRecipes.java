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
		register(new ItemStack(ModBlocks.RUBBER_SAPLING), false, getMaterial("rubber", Type.PART));
		register(new ItemStack(ModBlocks.RUBBER_LOG), false, getMaterial("rubber", Type.PART));
		register(new ItemStack(Items.SLIME_BALL), getMaterial("rubber", 2, Type.PART));
		register(getMaterial("sap", Type.PART), getMaterial("rubber", 3, Type.PART));
		register(new ItemStack(Blocks.RED_FLOWER), new ItemStack(Items.DYE, 2, 1));
		register(new ItemStack(Blocks.YELLOW_FLOWER), new ItemStack(Items.DYE, 2, 11));
		register(new ItemStack(Blocks.RED_FLOWER, 1, 1), new ItemStack(Items.DYE, 2, 12));
		register(new ItemStack(Blocks.RED_FLOWER, 1, 2), new ItemStack(Items.DYE, 2, 13));
		register(new ItemStack(Blocks.RED_FLOWER, 1, 3), new ItemStack(Items.DYE, 2, 7));
		register(new ItemStack(Blocks.RED_FLOWER, 1, 4), new ItemStack(Items.DYE, 2, 1));
		register(new ItemStack(Blocks.RED_FLOWER, 1, 5), new ItemStack(Items.DYE, 2, 14));
		register(new ItemStack(Blocks.RED_FLOWER, 1, 6), new ItemStack(Items.DYE, 2, 7));
		register(new ItemStack(Blocks.RED_FLOWER, 1, 7), new ItemStack(Items.DYE, 2, 9));
		register(new ItemStack(Blocks.RED_FLOWER, 1, 8), new ItemStack(Items.DYE, 2, 7));
		register(new ItemStack(Blocks.DOUBLE_PLANT), new ItemStack(Items.DYE, 4, 11));
		register(new ItemStack(Blocks.DOUBLE_PLANT, 1, 1), new ItemStack(Items.DYE, 4, 13));
		register(new ItemStack(Blocks.DOUBLE_PLANT, 1, 4), new ItemStack(Items.DYE, 4, 1));
		register(new ItemStack(Blocks.DOUBLE_PLANT, 1, 5), new ItemStack(Items.DYE, 4, 9));
		register(new ItemStack(Blocks.TALLGRASS, 1, 1), new ItemStack(Items.WHEAT_SEEDS));
		register(new ItemStack(Blocks.TALLGRASS, 1, 2), new ItemStack(Items.WHEAT_SEEDS));
		register(new ItemStack(Blocks.DOUBLE_PLANT, 1, 2), new ItemStack(Items.WHEAT_SEEDS, 2));
		register(new ItemStack(Blocks.DOUBLE_PLANT, 1, 3), new ItemStack(Items.WHEAT_SEEDS, 2));
		register(new ItemStack(Blocks.DEADBUSH, 1, 0), new ItemStack(Items.STICK));
		for (int i = 1; i < 15; i++)
			register(new ItemStack(Blocks.WOOL, 1, i), new ItemStack(Blocks.WOOL, 1, 0));
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
