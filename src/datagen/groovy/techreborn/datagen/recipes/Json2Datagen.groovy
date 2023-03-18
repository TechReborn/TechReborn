/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2023 TechReborn
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

package techreborn.datagen.recipes

import com.google.gson.Gson
import com.google.gson.JsonObject
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import reborncore.common.crafting.RebornRecipe
import reborncore.common.crafting.ingredient.RebornIngredient
import reborncore.common.crafting.ingredient.StackIngredient
import reborncore.common.crafting.ingredient.TagIngredient
import techreborn.init.ModRecipes

class Json2Datagen {
	private static final Gson GSON = new Gson()

	static void processAll() {
		def sj = new StringJoiner("\n")

		new File("../../src/main/resources/data/techreborn/recipes/grinder").eachFile {
			def json = GSON.fromJson(it.text, JsonObject.class)
			def recipe = ModRecipes.GRINDER.read(new Identifier("techreborn:convert"), json)
			sj.add process(recipe)
		}

		println sj
	}

	static String process(RebornRecipe recipe) {
		StringBuilder stringBuilder = new StringBuilder()

		/**
		 * offerAlloySmelterRecipe {
		 * 			power 6
		 * 			time 200
		 * 			ingredients new ItemStack(Items.COPPER_INGOT, 3), TRConventionalTags.ZINC_INGOTS
		 * 			output new ItemStack(TRContent.Ingots.BRASS, 4)
		 *                }
		 */

		stringBuilder.append("offerGrinderRecipe {\n")
		stringBuilder.append("\tpower ").append(recipe.power).append("\n")
		stringBuilder.append("\ttime ").append(recipe.time).append("\n")
		stringBuilder.append("\tingredients ").append(formatIngredients(recipe.getRebornIngredients())).append("\n")
		stringBuilder.append("\toutputs ").append(formatOutputs(recipe.getOutputs())).append("\n")
		stringBuilder.append("}")

		return stringBuilder
	}

	private static String formatOutputs(List<ItemStack> outputs) {
		StringJoiner sj = new StringJoiner(", ")

		for (ItemStack output : outputs) {
			sj.add(formatStack(output))
		}

		return sj.toString()
	}

	private static String formatIngredients(List<RebornIngredient> ingredients) {
		StringJoiner sj = new StringJoiner(", ")
		for (RebornIngredient ingredient : ingredients) {
			if (ingredient instanceof StackIngredient) {
				if (ingredient.getNbt().isPresent()) {
					throw new UnsupportedOperationException()
				}

				ItemStack stack = ingredient.getStack().copy()
				stack.setCount(ingredient.getCount())

				sj.add(formatStack(stack))
			} else if (ingredient instanceof TagIngredient) {
				int count = ingredient.getCount()
				if (count == 1) {
					sj.add("tag(\"%s\")".formatted(ingredient.getTag().id()))
				} else {
					sj.add("tag(\"%s\", \"%s\")".formatted(ingredient.getTag().id(), ingredient.getCount()))
				}
			} else {
				throw new UnsupportedOperationException()
			}
		}

		return sj.toString()
	}

	private static String formatStack(ItemStack stack) {
		String name = Registries.ITEM.getId(stack.getItem()).toString()
		if (stack.getCount() == 1) {
			return "stack(\"%s\")".formatted(name)
		} else {
			return "stack(\"%s\", %s)".formatted(name, stack.getCount())
		}
	}
}
