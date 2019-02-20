/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.init.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import reborncore.api.recipe.RecipeHandler;
import techreborn.api.Reference;
import techreborn.api.recipe.machines.ExtractorRecipe;
import techreborn.init.TRContent;
import techreborn.items.DynamicCell;

/**
 * Created by Prospector
 */
public class ExtractorRecipes extends RecipeMethods {
	public static void init() {
		register(getStack(TRContent.RUBBER_SAPLING), TRContent.Parts.RUBBER.getStack(), false);
		register(getStack(TRContent.RUBBER_LOG), TRContent.Parts.RUBBER.getStack(), false);
		register(getStack(Items.SLIME_BALL), TRContent.Parts.RUBBER.getStack(2), false);
		register(TRContent.Parts.SAP.getStack(), TRContent.Parts.RUBBER.getStack(3), false);
		register(getStack(Blocks.RED_TULIP), getStack(Items.ROSE_RED, 2), false);
		register(getStack(Blocks.POPPY), getStack(Items.ROSE_RED, 2), false);
		register(getStack(Blocks.ROSE_BUSH), getStack(Items.ROSE_RED, 4), false);

		
		register(getStack(Blocks.RED_FLOWER), getStack(Items.DYE, 2, 1), false);
		register(getStack(Blocks.YELLOW_FLOWER), getStack(Items.DYE, 2, 11), false);
		register(getStack(Blocks.RED_FLOWER, 1, 1), getStack(Items.DYE, 2, 12), false);
		register(getStack(Blocks.RED_FLOWER, 1, 2), getStack(Items.DYE, 2, 13), false);
		register(getStack(Blocks.RED_FLOWER, 1, 3), getStack(Items.DYE, 2, 7), false);
		register(getStack(Blocks.RED_FLOWER, 1, 4), getStack(Items.DYE, 2, 1), false);
		register(getStack(Blocks.RED_FLOWER, 1, 5), getStack(Items.DYE, 2, 14), false);
		register(getStack(Blocks.RED_FLOWER, 1, 6), getStack(Items.DYE, 2, 7), false);
		register(getStack(Blocks.RED_FLOWER, 1, 7), getStack(Items.DYE, 2, 9), false);
		register(getStack(Blocks.RED_FLOWER, 1, 8), getStack(Items.DYE, 2, 7), false);
		register(getStack(Blocks.DOUBLE_PLANT), getStack(Items.DYE, 4, 11), false);
		register(getStack(Blocks.DOUBLE_PLANT, 1, 1), getStack(Items.DYE, 4, 13), false);
		register(getStack(Blocks.DOUBLE_PLANT, 1, 4), getStack(Items.DYE, 4, 1), false);
		register(getStack(Blocks.DOUBLE_PLANT, 1, 5), getStack(Items.DYE, 4, 9), false);
		
		
		register(getStack(Blocks.TALLGRASS, 1, 1), getStack(Items.WHEAT_SEEDS), false);
		register(getStack(Blocks.TALLGRASS, 1, 2), getStack(Items.WHEAT_SEEDS), false);
		register(getStack(Blocks.DOUBLE_PLANT, 1, 2), getStack(Items.WHEAT_SEEDS, 2), false);
		register(getStack(Blocks.DOUBLE_PLANT, 1, 3), getStack(Items.WHEAT_SEEDS, 2), false);
		register(getStack(Blocks.DEADBUSH, 1, 0), getStack(Items.STICK));
		for (int i = 1; i < 15; i++)
			register(getStack(Blocks.WOOL, 1, i), getStack(Blocks.WOOL, 1, 0), false);
		for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
			register(DynamicCell.getCellWithFluid(fluid), DynamicCell.getEmptyCell(1), false);
		}
	}

	static void register(ItemStack input, ItemStack output) {
		register(input,  output, true);
	}

	static void register(ItemStack input, ItemStack output, boolean oreDict) {
		RecipeHandler.addRecipe(Reference.EXTRACTOR_RECIPE, new ExtractorRecipe(input, output, 400, 2, oreDict));
	}
}
