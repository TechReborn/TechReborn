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

import com.google.gson.JsonObject
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.util.Identifier
import reborncore.common.crafting.ingredient.RebornIngredient

// Dummy ingredient to create recipes for mods that do not exist at datagen runtime.
class StackIdIngredient extends RebornIngredient {
	final Identifier itemId
	final int count

	StackIdIngredient(Identifier itemId, int count = -1) {
		this.itemId = itemId
		this.count = count
	}

	@Override
	JsonObject toJson(boolean networkSync) {
		assert !networkSync

		def json = new JsonObject()
		json.addProperty("item", itemId.toString())

		if (count > 0) {
			json.addProperty("count", count)
		}

		return json
	}

	@Override
	boolean test(ItemStack itemStack) {
		throw new UnsupportedOperationException()
	}

	@Override
	Ingredient getPreview() {
		throw new UnsupportedOperationException()
	}

	@Override
	List<ItemStack> getPreviewStacks() {
		throw new UnsupportedOperationException()
	}

	@Override
	int getCount() {
		throw new UnsupportedOperationException()
	}
}
