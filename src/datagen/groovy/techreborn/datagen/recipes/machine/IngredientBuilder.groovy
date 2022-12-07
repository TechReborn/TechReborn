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
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import reborncore.common.crafting.ingredient.RebornIngredient
import reborncore.common.crafting.ingredient.StackIngredient
import reborncore.common.crafting.ingredient.TagIngredient

class IngredientBuilder {
	private TagKey<Item> tag
	private List<ItemStack> stacks = []
	private List<Identifier> ids = []

	private IngredientBuilder() {
	}

	static IngredientBuilder create() {
		return new IngredientBuilder()
	}

	RebornIngredient build() {
		if (!ids.isEmpty()) {
			if (!stacks.isEmpty() || tag != null) {
				throw new IllegalStateException("Cannot have id ingredient with tag/stack inputs")
			}

			if (ids.size() != 1) {
				throw new IllegalStateException()
			}

			return new StackIdIngredient(ids.get(0))
		}

		if (tag != null) {
			if (!stacks.isEmpty() || !ids.isEmpty()) {
				throw new IllegalStateException("Cannot have ingredient with tag and stack/id inputs")
			}

			return new TagIngredient(tag, Optional.empty())
		}

		if (stacks.size() == 1) {
			return new StackIngredient(stacks.get(0), Optional.of(stacks.get(0).getCount()), Optional.empty(), false)
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

	def ident(Identifier identifier) {
		ids.add(identifier)
		return this
	}
}
