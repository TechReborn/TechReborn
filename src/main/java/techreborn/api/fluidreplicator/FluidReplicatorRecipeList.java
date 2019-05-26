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

package techreborn.api.fluidreplicator;


import org.apache.commons.lang3.Validate;
import reborncore.common.util.FluidUtils;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author drcrazy
 *
 */
public class FluidReplicatorRecipeList {
	
	/**
	 * This is the list of all the recipes
	 */
	public static ArrayList<FluidReplicatorRecipe> recipes = new ArrayList<>();

	/**
	 * Register your Fluid Replicator recipe 
	 *
	 * @param recipe FluidReplicatorRecipe The recipe you want to add
	 */
	public static void addRecipe(FluidReplicatorRecipe recipe) {
		Validate.notNull(recipe);
		Validate.isTrue(!recipes.contains(recipe));
		Validate.validState(recipe.getInput() >= 1);
		Validate.validState(!getRecipeForFluid(recipe.getFluid()).isPresent());

		recipes.add(recipe);
	}
	
	/**
	 * Fluid Replicator recipe you want to remove
	 * 
	 * @param recipe FluidReplicatorRecipe Recipe to remove
	 * @return boolean true if recipe was removed from list of recipes
	 */
	public static boolean removeRecipe(FluidReplicatorRecipe recipe) {
		return recipes.remove(recipe);
	}
	
	/**
	 * Check if there is a Fluid Replicator recipe for that fluid
	 * 
	 * @param fluid Fluid Fluid to search for
	 * @return FluidReplicatorRecipe Recipe for fluid provided
	 */
	public static Optional<FluidReplicatorRecipe> getRecipeForFluid(Fluid fluid) {
		return recipes.stream().filter(recipe -> FluidUtils.fluidEquals(recipe.getFluid(), fluid)).findAny();
	}
}