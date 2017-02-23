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

package techreborn.compat.jei.vacuumFreezer;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import techreborn.api.recipe.machines.VacuumFreezerRecipe;
import techreborn.compat.jei.RecipeCategoryUids;

import javax.annotation.Nonnull;

public class VacuumFreezerRecipeHandler implements IRecipeHandler<VacuumFreezerRecipe> {
	@Nonnull
	private final IJeiHelpers jeiHelpers;

	public VacuumFreezerRecipeHandler(
		@Nonnull
			IJeiHelpers jeiHelpers) {
		this.jeiHelpers = jeiHelpers;
	}

	@Nonnull
	@Override
	public Class<VacuumFreezerRecipe> getRecipeClass() {
		return VacuumFreezerRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return RecipeCategoryUids.VACUUM_FREEZER;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(
		@Nonnull
			VacuumFreezerRecipe recipe) {
		return RecipeCategoryUids.VACUUM_FREEZER;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(
		@Nonnull
			VacuumFreezerRecipe recipe) {
		return new VacuumFreezerRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(
		@Nonnull
			VacuumFreezerRecipe recipe) {
		return true;
	}
}
