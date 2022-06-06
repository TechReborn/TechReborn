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

package techreborn.test.recipe

import com.google.common.truth.Truth
import net.minecraft.test.GameTest
import reborncore.common.crafting.RebornRecipe
import reborncore.common.crafting.RebornRecipeType
import reborncore.common.crafting.RecipeManager
import techreborn.test.TRGameTest
import techreborn.test.TRTestContext

/**
 * A bit of a mess, but checks that we can ser/de all recipes to and from json.
 */
class RecipeSyncTests extends TRGameTest {
	@GameTest(templateName = "fabric-gametest-api-v1:empty", tickLimit = 150)
	def testRecipes(TRTestContext context) {

		def recipeTypes = RecipeManager.getRecipeTypes("techreborn")

		recipeTypes.each { type ->
			def recipes = type.getRecipes(context.world)
			recipes.each { recipe ->
				try {
					testRecipe(type, recipe)
				} catch (Throwable t) {
					throw new TRGameTestException(recipe.id.toString(), t)
				}
			}
		}

		context.complete()
	}

	def <R extends RebornRecipe> void testRecipe(RebornRecipeType<R> type, R recipe) {
		def firstJson = type.toJson(recipe, true).deepCopy()
		def newRecipe = type.read(recipe.id, firstJson)
		def secondJson = type.toJson(newRecipe, true).deepCopy()

		Truth.assertThat(firstJson.toString())
				.isEqualTo(secondJson.toString())

		// And check we can create a data json
		def dataJson = type.toJson(newRecipe, false).deepCopy()
	}
}
