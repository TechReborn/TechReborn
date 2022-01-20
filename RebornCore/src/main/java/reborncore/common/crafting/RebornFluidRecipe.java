/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.common.crafting;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.util.Tank;

import java.util.List;

public abstract class RebornFluidRecipe extends RebornRecipe {
	@NotNull
	private final FluidInstance fluidInstance;

	public RebornFluidRecipe(RebornRecipeType<?> type, Identifier name, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time, @NotNull FluidInstance fluidInstance) {
		super(type, name, ingredients, outputs, power, time);
		this.fluidInstance = fluidInstance;
	}

	public abstract Tank getTank(BlockEntity be);

	@Override
	public boolean canCraft(BlockEntity be) {
		final FluidInstance recipeFluid = fluidInstance;
		final FluidInstance tankFluid = getTank(be).getFluidInstance();
		if (fluidInstance.isEmpty()) {
			return true;
		}
		if (tankFluid.isEmpty()) {
			return false;
		}
		if (tankFluid.getFluid().equals(recipeFluid.getFluid())) {
			if (tankFluid.getAmount().equalOrMoreThan(recipeFluid.getAmount())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onCraft(BlockEntity be) {
		final FluidInstance recipeFluid = fluidInstance;
		final FluidInstance tankFluid = getTank(be).getFluidInstance();
		if (fluidInstance.isEmpty()) {
			return true;
		}
		if (tankFluid.isEmpty()) {
			return false;
		}
		if (tankFluid.getFluid().equals(recipeFluid.getFluid())) {
			if (tankFluid.getAmount().equalOrMoreThan(recipeFluid.getAmount())) {
				tankFluid.subtractAmount(recipeFluid.getAmount());
				return true;
			}
		}
		return false;
	}

	@NotNull
	public FluidInstance getFluidInstance() {
		return fluidInstance;
	}
}
