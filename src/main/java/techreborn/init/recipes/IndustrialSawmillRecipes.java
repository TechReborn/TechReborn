/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

import java.security.InvalidParameterException;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import reborncore.api.recipe.RecipeHandler;
import reborncore.common.util.OreUtil;
import techreborn.api.recipe.machines.IndustrialSawmillRecipe;
import techreborn.items.ItemDusts;

public class IndustrialSawmillRecipes extends RecipeMethods {
	static FluidStack WATER = new FluidStack(FluidRegistry.WATER, 1000);

	public static void init() {
		register(getStack(Blocks.LOG, 1, true), getMaterial("water", Type.CELL), WATER, 100, 128, getStack(Blocks.PLANKS, 8, true), ItemDusts.getDustByName("sawDust", 3), getStack(Items.PAPER,1));
		
	}
	
	static void register(ItemStack input1, ItemStack input2, FluidStack fluid, int ticks, int euPerTick, ItemStack... outputs) {
		if (outputs.length == 3) {
			RecipeHandler.addRecipe(new IndustrialSawmillRecipe(input1, input2,
				fluid, outputs[0], outputs[1], outputs[2], ticks, euPerTick));
		}
		else if (outputs.length == 2) {
			RecipeHandler.addRecipe(new IndustrialSawmillRecipe(input1, input2,
				fluid, outputs[0], outputs[1], null, ticks, euPerTick));
		}
		else if (outputs.length == 1) {
			RecipeHandler.addRecipe(new IndustrialSawmillRecipe(input1, input2,
				fluid, outputs[0], null, null, ticks, euPerTick));
		}
		else {
			throw new InvalidParameterException("Invalid industrial sawmill outputs: " + outputs);
		}
	}

}
