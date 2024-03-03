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

import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import reborncore.common.crafting.ingredient.FluidIngredient
import reborncore.common.crafting.ingredient.RebornIngredient
import reborncore.common.crafting.ingredient.StackIngredient
import reborncore.common.crafting.ingredient.TagIngredient
import techreborn.init.ModFluids
import techreborn.init.TRContent

class IngredientBuilder {
	private TagKey<Item> tag
	private Integer tagCount = -1
	private List<ItemStack> stacks = []
	private List<Identifier> ids = []
	private List<FluidIngredient> fluids = []

	private IngredientBuilder() {
	}

	static IngredientBuilder create() {
		return new IngredientBuilder()
	}

	RebornIngredient build() {
		checkHasSingleInputType()

		if (!ids.isEmpty()) {
			if (ids.size() != 1) {
				throw new IllegalStateException()
			}

			return new StackIdIngredient(ids.get(0))
		}

		if (tag != null) {
			return new TagIngredient(tag, tagCount == -1 ? Optional.empty() : Optional.of(tagCount))
		}

		if (!stacks.isEmpty()) {
			if (stacks.size() != 1) {
				throw new IllegalStateException("Must have exactly one stack input")
			}

			def stack = stacks[0]
			def count = stack.getCount() > 1 ? Optional.of(stack.getCount()) : Optional.empty()
			return new StackIngredient(stack, count, Optional.empty(), false)
		}

		if (!fluids.isEmpty()) {
			if (fluids.size() != 1) {
				throw new IllegalStateException("Must have exactly one fluid input")
			}

			return fluids.get(0)
		}

		throw new IllegalStateException("No input")
	}

	def tag(TagKey<Item> tag, int count = -1) {
		this.tag = tag
		this.tagCount = count
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

	def fluid(Fluid fluid, Item holder = TRContent.CELL, int count = -1) {
		fluids.add(new FluidIngredient(fluid, Optional.of([holder]), Optional.ofNullable(count == -1 ? null : count)))
		return this
	}

	def fluid(ModFluids modFluid, Item holder = TRContent.CELL, int count = -1) {
		return fluid(modFluid.fluid, holder, count)
	}

	def checkHasSingleInputType() {
		int count = 0

		if (!stacks.isEmpty()) {
			count++
		}

		if (!ids.isEmpty()) {
			count++
		}

		if (tag != null) {
			count++
		}

		if (!fluids.isEmpty()) {
			count++
		}

		if (count != 1) {
			throw new IllegalStateException("Must have exactly one input type")
		}
	}
}
