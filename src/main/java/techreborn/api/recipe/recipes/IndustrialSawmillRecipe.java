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

package techreborn.api.recipe.recipes;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;

import reborncore.common.crafting.Recipe;
import reborncore.common.crafting.RecipeType;
import techreborn.tiles.machine.multiblock.TileIndustrialSawmill;

public class IndustrialSawmillRecipe extends Recipe {

	//TODO 1.14 fluids
	FluidStack fluidStack = null;

	public IndustrialSawmillRecipe(RecipeType<?> type, Identifier name) {
		super(type, name);
	}

	@Override
	public boolean canCraft(BlockEntity tileEntity) {
		TileIndustrialSawmill tile = (TileIndustrialSawmill) tileEntity;
		if (!tile.getMutliBlock()) {
			return false;
		}
		final FluidStack recipeFluid = fluidStack;
		final FluidStack tankFluid = tile.tank.getFluid();
		if (fluidStack == null) {
			return true;
		}
		if (tankFluid == null) {
			return false;
		}
		if (tankFluid.isFluidEqual(recipeFluid)) {
			if (tankFluid.amount >= recipeFluid.amount) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onCraft(BlockEntity tileEntity) {
		TileIndustrialSawmill tile = (TileIndustrialSawmill) tileEntity;
		final FluidStack recipeFluid = fluidStack;
		final FluidStack tankFluid = tile.tank.getFluid();
		if (fluidStack == null) {
			return true;
		}
		if (tankFluid == null) {
			return false;
		}
		if (tankFluid.isFluidEqual(recipeFluid)) {
			if (tankFluid.amount >= recipeFluid.amount) {
				if (tankFluid.amount == recipeFluid.amount) {
					tile.tank.setFluid(null);
				} else {
					tankFluid.amount -= recipeFluid.amount;
				}
				tile.syncWithAll();
				return true;
			}
		}
		return false;
	}
}
