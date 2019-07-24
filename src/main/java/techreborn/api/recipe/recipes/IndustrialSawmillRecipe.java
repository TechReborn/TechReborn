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

import io.github.prospector.silk.fluid.FluidInstance;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import techreborn.blockentity.machine.multiblock.IndustrialSawmillBlockEntity;

public class IndustrialSawmillRecipe extends RebornRecipe {

	//TODO 1.14 fluids
	FluidInstance fluidStack = null;

	public IndustrialSawmillRecipe(RebornRecipeType<?> type, Identifier name) {
		super(type, name);
	}

	@Override
	public boolean canCraft(BlockEntity be) {
		IndustrialSawmillBlockEntity blockEntity = (IndustrialSawmillBlockEntity) be;
		if (!blockEntity.getMutliBlock()) {
			return false;
		}
		final FluidInstance recipeFluid = fluidStack;
		final FluidInstance tankFluid = blockEntity.tank.getFluidInstance();
		if (fluidStack == null) {
			return true;
		}
		if (tankFluid == null) {
			return false;
		}
		if (tankFluid.getFluid().equals(recipeFluid.getFluid())) {
			if (tankFluid.getAmount() >= recipeFluid.getAmount()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onCraft(BlockEntity be) {
		IndustrialSawmillBlockEntity blockEntity = (IndustrialSawmillBlockEntity) be;
		final FluidInstance recipeFluid = fluidStack;
		final FluidInstance tankFluid = blockEntity.tank.getFluidInstance();
		if (fluidStack == null) {
			return true;
		}
		if (tankFluid == null) {
			return false;
		}
		if (tankFluid.getFluid().equals(recipeFluid.getFluid())) {
			if (tankFluid.getAmount() >= recipeFluid.getAmount()) {
				if (tankFluid.getAmount() == recipeFluid.getAmount()) {
					blockEntity.tank.setFluid(null);
				} else {
					tankFluid.subtractAmount(recipeFluid.getAmount());
				}
				blockEntity.syncWithAll();
				return true;
			}
		}
		return false;
	}
}
