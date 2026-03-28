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

package techreborn.datagen.recipes.machine

import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.resources.Identifier
import reborncore.common.crafting.SizedIngredient
import techreborn.component.TRDataComponentTypes
import techreborn.init.TRContent

class IngredientBuilder {
	public HolderGetter<Item> itemLookup
	private TagKey<Item> tag
	private Integer tagCount = -1
	private List<ItemStackTemplate> stacks = []

	private IngredientBuilder(HolderGetter<Item> itemLookup) {
		this.itemLookup = itemLookup;
	}

	static IngredientBuilder create(HolderGetter<Item> itemLookup) {
		return new IngredientBuilder(itemLookup)
	}

	SizedIngredient build() {
		checkHasSingleInputType()

		if (tag != null) {
			return new SizedIngredient(tagCount == -1 ? 1 : tagCount, Ingredient.of(itemLookup.getOrThrow(tag)))
		}

		if (!stacks.isEmpty()) {
			if (stacks.size() != 1) {
				throw new IllegalStateException("Must have exactly one stack input")
			}

			def stack = stacks[0]
			def components = stack.components()

			// A bit of a hack to force the component changes to require the specified fluid, especially if empty
			if (stack.item == TRContent.CELL) {
				def builder = DataComponentPatch.builder()
				builder.set(TRDataComponentTypes.FLUID, stack.get(TRDataComponentTypes.FLUID))
				components = builder.build()
			}

			Ingredient ingredient = Ingredient.of(HolderSet.direct(stack.item()))

			if (!components.isEmpty()) {
				ingredient = DefaultCustomIngredients.components(ingredient, components)
			}

			return new SizedIngredient(stack.count(), ingredient)
		}

		throw new IllegalStateException("No input")
	}

	def tag(TagKey<Item> tag, int count = -1) {
		this.tag = tag
		this.tagCount = count
		return this
	}

	def item(ItemLike itemConvertible) {
		return stack(new ItemStackTemplate(itemConvertible.asItem()))
	}

	def stack(ItemStackTemplate itemStack) {
		stacks.add(itemStack)
		return this
	}

	@Deprecated
	def ident(Identifier identifier) {
		return item(BuiltInRegistries.ITEM.getValue(identifier))
	}

	def checkHasSingleInputType() {
		int count = 0

		if (!stacks.isEmpty()) {
			count++
		}

		if (tag != null) {
			count++
		}

		if (count != 1) {
			throw new IllegalStateException("Must have exactly one input type")
		}
	}
}
