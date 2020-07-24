/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.api.generator;

import com.google.common.collect.Sets;
import net.minecraft.fluid.Fluid;
import techreborn.utils.FluidUtils;

import java.util.HashSet;
import java.util.Optional;

public class FluidGeneratorRecipeList {
	private HashSet<FluidGeneratorRecipe> recipes;

	public FluidGeneratorRecipeList(FluidGeneratorRecipe... recipes) {
		this.recipes = Sets.newHashSet(recipes);
	}

	public boolean addRecipe(FluidGeneratorRecipe fluidGeneratorRecipe) {
		if (!this.getRecipeForFluid(fluidGeneratorRecipe.getFluid()).isPresent())
			return this.getRecipes().add(fluidGeneratorRecipe);
		return false;
	}

	public boolean removeRecipe(FluidGeneratorRecipe fluidGeneratorRecipe) {
		return this.getRecipes().remove(fluidGeneratorRecipe);
	}

	public Optional<FluidGeneratorRecipe> getRecipeForFluid(Fluid fluid) {
		return this.recipes.stream().filter(recipe -> FluidUtils.fluidEquals(recipe.getFluid(), fluid)).findAny();
	}

	public HashSet<FluidGeneratorRecipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(HashSet<FluidGeneratorRecipe> recipes) {
		this.recipes = recipes;
	}

	@Override
	public String toString() {
		return "FluidGeneratorRecipeList [recipes=" + recipes + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((recipes == null) ? 0 : recipes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FluidGeneratorRecipeList other = (FluidGeneratorRecipeList) obj;
		if (recipes == null) {
			return other.recipes == null;
		} else return recipes.equals(other.recipes);
	}
}
