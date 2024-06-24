/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2024 TechReborn
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

package techreborn.datagen.recipes.machine.fusion_reactor

import techreborn.recipe.recipes.FusionReactorRecipe
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.datagen.recipes.machine.MachineRecipeJsonFactory
import techreborn.init.ModRecipes

class FusionReactorRecipeJsonFactory extends MachineRecipeJsonFactory<FusionReactorRecipe> {
	protected int startEnergy = -1
	protected int minimumSize = -1

	protected FusionReactorRecipeJsonFactory(TechRebornRecipesProvider provider) {
		super(ModRecipes.FUSION_REACTOR, provider)
	}

	static FusionReactorRecipeJsonFactory createFusionReactor(TechRebornRecipesProvider provider, @DelegatesTo(value = FusionReactorRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		def factory = new FusionReactorRecipeJsonFactory(provider)
		closure.setDelegate(factory)
		closure.call(factory)
		return factory
	}

	def startEnergy(int startEnergy) {
		this.startEnergy = startEnergy
	}

	def minimumSize(int minimumSize) {
		this.minimumSize = minimumSize
	}

	@Override
	protected void validate() {
		super.validate()

		if (startEnergy == -1) {
			throw new IllegalArgumentException("startEnergy must be set")
		}

		if (minimumSize == -1) {
			throw new IllegalArgumentException("minimumSize must be set")
		}
	}

	@Override
	protected FusionReactorRecipe createRecipe() {
		return new FusionReactorRecipe(ModRecipes.FUSION_REACTOR, ingredients, outputs, power, time, startEnergy, minimumSize)
	}
}
