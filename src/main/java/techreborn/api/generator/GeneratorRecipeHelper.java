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
	 * This EnumMap store all the energy recipes for the fluid generators. Each
	 * value of the EFluidGenerator enum is linked to an object holding a set of
	 * FluidGeneratorRecipe.
	 */
	public static EnumMap<EFluidGenerator, FluidGeneratorRecipeList> fluidRecipes = new EnumMap<>(
			EFluidGenerator.class);

	/**
	 * Register a Fluid energy recipe.
	 *
	 * @param generatorType A value of the EFluidGenerator type in which the fluid is
	 *                      allowed to be consumed.
	 * @param fluidType
	 * @param energyPerMb   Represent the energy / MILLI_BUCKET the fluid will produce.
	 *                      Some generators use this value to alter their fluid decay
	 *                      speed to match their maximum energy output.
	 */
	public static void registerFluidRecipe(EFluidGenerator generatorType, Fluid fluidType, int energyPerMb) {
		fluidRecipes.putIfAbsent(generatorType, new FluidGeneratorRecipeList());
		fluidRecipes.get(generatorType).addRecipe(new FluidGeneratorRecipe(fluidType, energyPerMb, generatorType));
	}

	/**
	 * @param generatorType A value of the EFluidGenerator type in which the fluid is
	 *                      allowed to be consumed.
	 * @return An object holding a set of availables recipes for this type of
	 * FluidGenerator.
	 */
	public static FluidGeneratorRecipeList getFluidRecipesForGenerator(EFluidGenerator generatorType) {
		return fluidRecipes.get(generatorType);
	}

	/**
	 * Removes recipe
	 *
	 * @param generatorType A value of the EFluidGenerator type for which we should remove recipe
	 * @param fluidType     Fluid to remove from generator recipes
	 */
	public static void removeFluidRecipe(EFluidGenerator generatorType, Fluid fluidType) {
		FluidGeneratorRecipeList recipeList = getFluidRecipesForGenerator(generatorType);
		Optional<FluidGeneratorRecipe> recipe = recipeList.getRecipeForFluid(fluidType);
		recipe.ifPresent(recipeList::removeRecipe);
	}
}
