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

package techreborn.datagen.recipes.machine.rolling_machine

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapedRecipe
import net.minecraft.world.item.crafting.ShapedRecipePattern
import techreborn.datagen.recipes.TechRebornRecipesProvider
import techreborn.datagen.recipes.crafting.ShapedRecipeFactory
import techreborn.datagen.recipes.machine.IngredientBuilder
import techreborn.datagen.recipes.machine.MachineRecipeJsonFactory
import techreborn.init.ModRecipes
import techreborn.recipe.recipes.RollingMachineRecipe

class RollingMachineRecipeJsonFactory extends MachineRecipeJsonFactory<RollingMachineRecipe> {
	def _ = null

	protected ShapedRecipeFactory shapedRecipeFactory = new ShapedRecipeFactory(provider.generator, provider.itemLookup, 3, 3)

	protected RollingMachineRecipeJsonFactory(TechRebornRecipesProvider provider) {
		super(ModRecipes.ROLLING_MACHINE, provider)
	}

	static RollingMachineRecipeJsonFactory createRollingMachine(TechRebornRecipesProvider provider, @DelegatesTo(value = RollingMachineRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		def factory = new RollingMachineRecipeJsonFactory(provider)
		closure.setDelegate(factory)
		closure.call(factory)
		return factory
	}

	def recipe(@DelegatesTo(value = ShapedRecipeFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		closure.setDelegate(shapedRecipeFactory)
		closure.call(shapedRecipeFactory)
	}

	def pattern(Object... pattern) {
		shapedRecipeFactory.pattern(pattern)
	}

	def result(ItemStackTemplate output) {
		shapedRecipeFactory.output(output)
	}

	def size(int width, int height) {
		shapedRecipeFactory.size(width, height)
	}

	@SuppressWarnings('GroovyAccessibility')
	protected RollingMachineRecipe createRecipe() {
		def builder = shapedRecipeFactory.build()
		ShapedRecipePattern pattern = ShapedRecipePattern.of(builder.key as Map<Character, Ingredient>, builder.rows as List<String>)
		ShapedRecipe shapedRecipe = new ShapedRecipe(
			RecipeBuilder.createCraftingCommonInfo(builder.showNotification),
			RecipeBuilder.createCraftingBookInfo(builder.category, builder.group),
			pattern,
			builder.result
		)
		return new RollingMachineRecipe(ModRecipes.ROLLING_MACHINE, power, time, shapedRecipe)
	}

	@Override
	def getIdentifier() {
		def outputId = BuiltInRegistries.ITEM.getKey(shapedRecipeFactory.output.item().value())
		def recipeId = BuiltInRegistries.RECIPE_TYPE.getKey(type)
		return Identifier.fromNamespaceAndPath("techreborn", "${recipeId.path}/${outputId.path}${getSourceAppendix()}")
	}

	@Override
	def ingredient(@DelegatesTo(value = IngredientBuilder.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		throw new UnsupportedOperationException("This method is not supported for RollingMachineRecipeJsonFactory")
	}

	@Override
	def outputs(Object... objects) {
		throw new UnsupportedOperationException("This method is not supported for RollingMachineRecipeJsonFactory")
	}

	@Override
	protected void validate() {
	}
}
