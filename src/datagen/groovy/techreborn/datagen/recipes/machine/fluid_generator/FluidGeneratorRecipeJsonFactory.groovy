/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2025 TechReborn
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

import com.google.gson.JsonObject
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import techreborn.api.generator.EFluidGenerator
import techreborn.api.generator.FluidGeneratorRecipe
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.init.ModFluids

import java.util.function.Consumer

class FluidGeneratorRecipeJsonFactory {
	private Fluid fluid = Fluids.EMPTY
	private int power = -1;
	private EFluidGenerator type = null;
	protected final TechRebornRecipesProvider provider

	def fluid(Fluid fluid) {
		this.fluid = fluid
		return this
	}

	def fluid(ModFluids fluids) {
		return fluid(fluids.getFluid())
	}

	protected FluidGeneratorRecipeJsonFactory(EFluidGenerator type, TechRebornRecipesProvider provider) {
		this.type = type
		this.provider = provider
	}

	static FluidGeneratorRecipeJsonFactory create(EFluidGenerator type, TechRebornRecipesProvider provider) {
		return new FluidGeneratorRecipeJsonFactory(type, provider)
	}

	static FluidGeneratorRecipeJsonFactory createFluidGenerator(EFluidGenerator type, TechRebornRecipesProvider provider, @DelegatesTo(value = FluidGeneratorRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		def factory = new FluidGeneratorRecipeJsonFactory(type, provider)
		closure.setDelegate(factory)
		closure.call(factory)
		return factory
	}

	protected FluidGeneratorRecipe createRecipe() {
		return new FluidGeneratorRecipe(fluid, power, type)
	}

	protected void validate() {
		if (fluid == Fluids.EMPTY) {
			throw new IllegalStateException("recipe has no fluid variant")
		}

		if (power < 0) {
			throw new IllegalStateException("recipe has no power value")
		}
	}

	def getIdentifier() {
		def outputId = Registries.FLUID.getId(fluid)
		def recipeId = type.id;
		return Identifier.of("techreborn", "${recipeId.path}/${outputId.path}")
	}

	def power(int power) {
		this.power = power
		return this
	}

	void offerTo(Consumer<RecipeJsonProvider> exporter) {
		validate()
		def recipeId = getIdentifier()

		if (provider.exportedRecipes.contains(recipeId)) {
			int i = 1
			def id
			do {
				i++
				id = new Identifier(recipeId.toString() + "_" + i)
			} while (provider.exportedRecipes.contains(id))

			recipeId = id
		}

		provider.exportedRecipes.add(recipeId)
		exporter.accept(new FluidGeneratorRecipeJsonProvider(recipeId, createRecipe()))
	}

	static class FluidGeneratorRecipeJsonProvider implements RecipeJsonProvider {
		private final FluidGeneratorRecipe recipe
		private final Identifier recipeId;

		FluidGeneratorRecipeJsonProvider(Identifier recipeId, FluidGeneratorRecipe recipe) {
			this.recipe = recipe
			this.recipeId = recipeId
		}

		@Override
		JsonObject toJson() {
			var json = new JsonObject();
			json.addProperty("type", recipe.generatorType().id.toString())
			json.addProperty("fluid", Registries.FLUID.getId(recipe.fluid()).toString())
			json.addProperty("power", recipe.energyPerMb())
			return json
		}

		@Override
		Identifier getRecipeId() {
			return recipeId
		}

		@Override
		void serialize(JsonObject json) {
			throw new UnsupportedOperationException()
		}

		@Override
		RecipeSerializer getSerializer() {
			throw new UnsupportedOperationException()
		}

		@Override
		JsonObject toAdvancementJson() {
			return null
		}

		@Override
		Identifier getAdvancementId() {
			throw new UnsupportedOperationException()
		}
	}
}
