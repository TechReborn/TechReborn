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

import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.tag.TagKey
import reborncore.common.crafting.ingredient.RebornIngredient
import reborncore.common.crafting.ingredient.StackIngredient
import reborncore.common.crafting.ingredient.TagIngredient

class IngredientBuilder {
	private TagKey<Item> tag
	private List<ItemStack> stacks = new ArrayList<>()

	private IngredientBuilder() {
	}

	static IngredientBuilder create() {
		return new IngredientBuilder()
	}

	RebornIngredient build() {
		if (tag != null) {
			if (!stacks.isEmpty()) {
				throw new IllegalStateException("Cannot have ingredient with tag and stack inputs")
			}

			return new TagIngredient(tag, getCount())
		}

		if (stacks.size() == 1) {
			return new StackIngredient(stacks.get(0), getCount(), Optional.empty(), false)
		}

		throw new IllegalStateException()
	}

	def tag(TagKey<Item> tag) {
		this.tag = tag
		return this
	}

	def item(ItemConvertible itemConvertible) {
		return stack(new ItemStack(itemConvertible.asItem()))
	}

	def stack(ItemStack itemStack) {
		stacks.add(itemStack)
		return this
	}


	private Optional<Integer> getCount() {
		return Optional.empty()
	}
}
