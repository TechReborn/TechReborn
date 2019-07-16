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
import techreborn.api.recipe.machines.ExtractorRecipe;
import techreborn.init.ModBlocks;
import techreborn.items.ItemDynamicCell;

/**
 * Created by Prospector
 */
public class ExtractorRecipes extends RecipeMethods {
	public static void init() {
		register(getStack(ModBlocks.RUBBER_SAPLING), getMaterial("rubber", Type.PART), false);
		register(getStack(ModBlocks.RUBBER_LOG),  getMaterial("rubber", Type.PART), false);
		register(getStack(Items.SLIME_BALL), getMaterial("rubber", 2, Type.PART));
		register(getMaterial("sap", Type.PART), getMaterial("rubber", 3, Type.PART));
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
			register(ItemDynamicCell.getCellWithFluid(fluid), ItemDynamicCell.getEmptyCell(1), false);
		}
	}

	static void register(ItemStack input, ItemStack output) {
		register(input,  output, true);
	}

	static void register(ItemStack input, ItemStack output, boolean oreDict) {
		RecipeHandler.addRecipe(new ExtractorRecipe(input, output, 400, 2, oreDict));
	}
}
