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

package techreborn.datagen.recipes.machine.scrapbox

import techreborn.recipe.recipes.ScrapBoxRecipe
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.datagen.recipes.machine.MachineRecipeJsonFactory
import techreborn.init.ModRecipes

class ScrapboxRecipeJsonFactory extends MachineRecipeJsonFactory<ScrapBoxRecipe> {
	protected ScrapboxRecipeJsonFactory(TechRebornRecipesProvider provider) {
		super(ModRecipes.SCRAPBOX, provider)
	}

	static ScrapboxRecipeJsonFactory create(TechRebornRecipesProvider provider) {
		return new ScrapboxRecipeJsonFactory(provider)
	}

	static ScrapboxRecipeJsonFactory createScrapBox(TechRebornRecipesProvider provider, @DelegatesTo(value = ScrapboxRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		def factory = new ScrapboxRecipeJsonFactory(provider)
		closure.setDelegate(factory)
		closure.call(factory)
		return factory
	}

	@Override
	protected ScrapBoxRecipe createRecipe() {
		return new ScrapBoxRecipe(type, ingredients, outputs, power, time)
	}
}
