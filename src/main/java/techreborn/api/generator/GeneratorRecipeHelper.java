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


import net.minecraft.fluid.Fluid;

import java.util.EnumMap;
import java.util.Optional;

public class GeneratorRecipeHelper {

	/**
	 * This {@link EnumMap} store all the energy recipes for the fluid generators. Each
	 * value of the {@link EFluidGenerator} enum is linked to an object holding a set of
	 * {@link FluidGeneratorRecipe}.
	 */
	public static EnumMap<EFluidGenerator, FluidGeneratorRecipeList> fluidRecipes = new EnumMap<>(
			EFluidGenerator.class);

	/**
	 * Register a {@link Fluid} energy recipe.
	 *
	 * @param generatorType {@link EFluidGenerator} A value of the {@link EFluidGenerator} type in
	 *                      which the fluid is allowed to be consumed.
	 * @param fluidType     {@link Fluid} The fluid type that will be registered.
	 * @param energyPerMb   {@code int} Represent the energy / MILLI_BUCKET the fluid will produce.
	 *                      <p>
	 *                       Some generators use this value to alter their fluid decay
	 *                       speed to match their maximum energy output.
	 *                      </p>
	 */
	public static void registerFluidRecipe(EFluidGenerator generatorType, Fluid fluidType, int energyPerMb) {
		fluidRecipes.putIfAbsent(generatorType, new FluidGeneratorRecipeList());
		fluidRecipes.get(generatorType).addRecipe(new FluidGeneratorRecipe(fluidType, energyPerMb, generatorType));
	}

	/**
	 * @param generatorType {@link EFluidGenerator} A value of the {@link EFluidGenerator} type in
	 *                      which the fluid is allowed to be consumed.
	 * @return {@link FluidGeneratorRecipeList} An object holding a set of available recipes
	 * for this type of {@link EFluidGenerator}.
	 */
	public static FluidGeneratorRecipeList getFluidRecipesForGenerator(EFluidGenerator generatorType) {
		return fluidRecipes.get(generatorType);
	}

	/**
	 * Removes recipe
	 *
	 * @param generatorType {@link EFluidGenerator} A value of the {@link EFluidGenerator} type for
	 *                      which we should remove recipe
	 * @param fluidType     {@link Fluid} Fluid to remove from generator recipes
	 */
	public static void removeFluidRecipe(EFluidGenerator generatorType, Fluid fluidType) {
		FluidGeneratorRecipeList recipeList = getFluidRecipesForGenerator(generatorType);
		Optional<FluidGeneratorRecipe> recipe = recipeList.getRecipeForFluid(fluidType);
		recipe.ifPresent(recipeList::removeRecipe);
	}
}
