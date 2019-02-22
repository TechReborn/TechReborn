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
	/* TODO 1.13 :D
	public static void init() {
		register(getStack(TRContent.RUBBER_SAPLING), TRContent.Parts.RUBBER.getStack(), false);
		register(getStack(TRContent.RUBBER_LOG), TRContent.Parts.RUBBER.getStack(), false);
		register(getStack(Items.SLIME_BALL), TRContent.Parts.RUBBER.getStack(2), false);
		register(TRContent.Parts.SAP.getStack(), TRContent.Parts.RUBBER.getStack(3), false);
		register(getStack(Blocks.RED_TULIP), getStack(Items.ROSE_RED, 2), false);
		register(getStack(Blocks.POPPY), getStack(Items.ROSE_RED, 2), false);
		register(getStack(Blocks.ROSE_BUSH), getStack(Items.ROSE_RED, 4), false);
		register(getStack(Blocks.BLUE_ORCHID), getStack(Items.LIGHT_BLUE_DYE, 2), false);
		register(getStack(Blocks.AZURE_BLUET), getStack(Items.LIGHT_GRAY_DYE, 2), false);
		register(getStack(Blocks.OXEYE_DAISY), getStack(Items.LIGHT_GRAY_DYE, 2), false);
		register(getStack(Blocks.WHITE_TULIP), getStack(Items.LIGHT_GRAY_DYE, 2), false);
		register(getStack(Blocks.ALLIUM), getStack(Items.MAGENTA_DYE, 2), false);
		register(getStack(Blocks.LILAC), getStack(Items.MAGENTA_DYE, 4), false);
		register(getStack(Blocks.ORANGE_TULIP), getStack(Items.ORANGE_DYE, 2), false);
		register(getStack(Blocks.PINK_TULIP), getStack(Items.PINK_DYE, 2), false);
		register(getStack(Blocks.PEONY), getStack(Items.PINK_DYE, 4), false);
		register(getStack(Blocks.DANDELION), getStack(Items.DANDELION_YELLOW, 2), false);
		register(getStack(Blocks.SUNFLOWER), getStack(Items.DANDELION_YELLOW, 2), false);
		register(getStack(Blocks.TALL_GRASS), getStack(Items.WHEAT_SEEDS), false);
		register(getStack(Blocks.LARGE_FERN), getStack(Items.WHEAT_SEEDS), false);
		register(getStack(Blocks.DEAD_BUSH), getStack(Items.STICK));
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

	*/
}
