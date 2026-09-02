/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TechReborn
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

package techreborn.datagen.recipes.crafting

import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder
import net.minecraft.data.recipes.SingleItemRecipeBuilder
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.CookingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import reborncore.common.fluid.container.FluidContainerIngredient
import techreborn.TechReborn
import techreborn.datagen.recipes.TechRebornRecipesProvider

/** A compact DSL for recipes whose layouts do not benefit from bespoke generator logic. */
class CraftingRecipeFactory {
	private final TechRebornRecipesProvider provider
	private final HolderGetter<Item> itemLookup
	private final RecipeOutput exporter
	private final Set<String> recipeNames = []

	CraftingRecipeFactory(TechRebornRecipesProvider provider, HolderGetter<Item> itemLookup, RecipeOutput exporter) {
		this.provider = provider
		this.itemLookup = itemLookup
		this.exporter = exporter
	}

	void shaped(ItemLike result, int count, String group, List<String> pattern, Map<String, ?> key) {
		String name = recipeName('crafting_table', result, key.values(), pattern)
		def builder = ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, result, count)
		pattern.each(builder::pattern)
		key.each { symbol, value -> builder.define(symbol.charAt(0), ingredient(value)) }
		if (group != null) builder.group(group)
		unlock(builder, key.values())
		builder.save(exporter, recipeKey(name))
	}

	void shapeless(ItemLike result, int count, String group, List<?> ingredients) {
		String name = recipeName('crafting_table', result, ingredients)
		def builder = ShapelessRecipeBuilder.shapeless(itemLookup, RecipeCategory.MISC, new ItemStackTemplate(result.asItem(), count))
		ingredients.each { builder.requires(ingredient(it)) }
		if (group != null) builder.group(group)
		unlock(builder, ingredients)
		builder.save(exporter, recipeKey(name))
	}

	void smelting(ItemLike result, def input, float experience, int cookingTime) {
		String name = recipeName('smelting', result, [input])
		def builder = SimpleCookingRecipeBuilder.smelting(ingredient(input), RecipeCategory.MISC, CookingBookCategory.MISC,
			result, experience, cookingTime)
		unlock(builder, [input])
		builder.save(exporter, recipeKey(name))
	}

	void blasting(ItemLike result, def input, float experience, int cookingTime) {
		String name = recipeName('blasting', result, [input])
		def builder = SimpleCookingRecipeBuilder.blasting(ingredient(input), RecipeCategory.MISC, CookingBookCategory.MISC,
			result, experience, cookingTime)
		unlock(builder, [input])
		builder.save(exporter, recipeKey(name))
	}

	void stonecutting(ItemLike result, int count, def input) {
		String name = recipeName('stonecutting', result, [input])
		def builder = SingleItemRecipeBuilder.stonecutting(ingredient(input), RecipeCategory.MISC, result, count)
		unlock(builder, [input])
		builder.save(exporter, recipeKey(name))
	}

	private void unlock(def builder, Collection<?> ingredients) {
		def input = ingredients.first()
		if (input instanceof Fluid) {
			builder.unlockedBy("has_${BuiltInRegistries.FLUID.getKey(input).toDebugFileName()}", provider.getCriterionConditions(provider.getCellItemPredicate(input)))
		} else {
			builder.unlockedBy(TechRebornRecipesProvider.getCriterionName(input), provider.getCriterionConditions(input))
		}
	}

	private Ingredient ingredient(def value) {
		if (value instanceof Fluid) {
			return new FluidContainerIngredient(value.builtInRegistryHolder(), 1000).toVanilla()
		}
		return provider.createIngredient(value)
	}

	private String recipeName(String type, ItemLike result, Collection<?> ingredients, List<String> pattern = null) {
		String inputs = ingredients.collect { pathName(it) }.countBy { it }.sort().collect { input, count ->
			count == 1 ? input : "${count}x_${input}"
		}.join('_and_')
		String base = "${type}/${pathName(result)}_from_${inputs}"
		if (recipeNames.add(base)) return base

		String layout = pattern?.collect { it.replace(' ', 'x').toLowerCase(Locale.ROOT) }?.join('_') ?: 'alternate'
		String variant = "${base}_layout_${layout}"
		if (!recipeNames.add(variant)) {
			throw new IllegalStateException("Recipes have the same automatically generated name: ${variant}")
		}
		return variant
	}

	private static String pathName(def value) {
		Identifier id
		if (value instanceof TagKey) {
			id = value.location()
		} else if (value instanceof Fluid) {
			id = BuiltInRegistries.FLUID.getKey(value)
		} else {
			id = BuiltInRegistries.ITEM.getKey(((ItemLike) value).asItem())
		}
		String path = id.getPath().replace('/', '_')
		return id.getNamespace() in ['minecraft', TechReborn.MOD_ID, 'c'] ? path : "${id.getNamespace()}_${path}"
	}

	private static ResourceKey<Recipe<?>> recipeKey(String name) {
		return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, name))
	}
}
