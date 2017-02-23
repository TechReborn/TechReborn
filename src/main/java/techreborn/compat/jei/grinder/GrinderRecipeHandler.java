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

package techreborn.compat.jei.grinder;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.GrinderRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

import javax.annotation.Nonnull;

public class GrinderRecipeHandler implements IRecipeHandler<GrinderRecipe> {
	@Nonnull
	private final IJeiHelpers jeiHelpers;

	public GrinderRecipeHandler(
		@Nonnull
			IJeiHelpers jeiHelpers) {
		this.jeiHelpers = jeiHelpers;
	}

	@Nonnull
	@Override
	public Class<GrinderRecipe> getRecipeClass() {
		return GrinderRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return RecipeCategoryUids.GRINDER;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(
		@Nonnull
			GrinderRecipe recipe) {
		return RecipeCategoryUids.GRINDER;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(
		@Nonnull
			GrinderRecipe recipe) {
		return new GrinderRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(
		@Nonnull
			GrinderRecipe recipe) {
		return true;
	}
}
