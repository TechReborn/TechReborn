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

package techreborn.compat.jei.generators.fluid;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.generator.FluidGeneratorRecipe;

import javax.annotation.Nonnull;

public class FluidGeneratorRecipeHandler implements IRecipeHandler<FluidGeneratorRecipe> {
	@Nonnull
	private final IJeiHelpers jeiHelpers;

	public FluidGeneratorRecipeHandler(
		@Nonnull
			IJeiHelpers jeiHelpers) {
		this.jeiHelpers = jeiHelpers;
	}

	@Nonnull
	@Override
	public Class<FluidGeneratorRecipe> getRecipeClass() {
		return FluidGeneratorRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(FluidGeneratorRecipe recipe) {
		return recipe.getGeneratorType().getRecipeID();
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(FluidGeneratorRecipe recipe) {
		return new FluidGeneratorRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(FluidGeneratorRecipe recipe) {
		return true;
	}
}
