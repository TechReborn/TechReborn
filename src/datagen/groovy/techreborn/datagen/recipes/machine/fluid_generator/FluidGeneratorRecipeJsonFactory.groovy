/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TechReborn
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

package techreborn.datagen.recipes.machine.fluid_generator

import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.datagen.recipes.machine.MachineRecipeJsonFactory
import techreborn.init.ModFluids
import techreborn.recipe.recipes.FluidGeneratorRecipe

class FluidGeneratorRecipeJsonFactory extends MachineRecipeJsonFactory<FluidGeneratorRecipe> {
	private Fluid fluid = Fluids.EMPTY

	def fluid(Fluid fluid) {
		this.fluid = fluid
		return this
	}

	def fluid(ModFluids fluids) {
		return fluid(fluids.getFluid())
	}

	protected FluidGeneratorRecipeJsonFactory(RecipeType<FluidGeneratorRecipe> type, TechRebornRecipesProvider provider) {
		super(type, provider)
	}

	static FluidGeneratorRecipeJsonFactory create(RecipeType<FluidGeneratorRecipe> type, TechRebornRecipesProvider provider) {
		return new FluidGeneratorRecipeJsonFactory(type, provider)
	}

	static FluidGeneratorRecipeJsonFactory createFluidGenerator(RecipeType<FluidGeneratorRecipe> type, TechRebornRecipesProvider provider, @DelegatesTo(value = FluidGeneratorRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		def factory = new FluidGeneratorRecipeJsonFactory(type, provider)
		closure.setDelegate(factory)
		closure.call(factory)
		return factory
	}

	@Override
	protected FluidGeneratorRecipe createRecipe() {
		return new FluidGeneratorRecipe(type, power, fluid)
	}

	@Override
	protected void validate() {
		if (fluid == Fluids.EMPTY) {
			throw new IllegalStateException("recipe has no fluid variant")
		}

		if (power < 0) {
			throw new IllegalStateException("recipe has no power value")
		}
	}

	@Override
	def getIdentifier() {
		def outputId = Registries.FLUID.getId(fluid)
		def recipeId = Registries.RECIPE_TYPE.getId(type)
		return Identifier.of("techreborn", "${recipeId.path}/${outputId.path}${getSourceAppendix()}")
	}
}
