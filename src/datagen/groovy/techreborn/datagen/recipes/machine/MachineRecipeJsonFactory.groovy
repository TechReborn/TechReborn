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
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.tag.Tag
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import reborncore.common.crafting.RebornRecipe
import reborncore.common.crafting.RebornRecipeType
import reborncore.common.crafting.ingredient.RebornIngredient

import java.util.function.Consumer

class MachineRecipeJsonFactory<R extends RebornRecipe> {
	private final RebornRecipeType<R> type

	private final List<RebornIngredient> ingredients = new ArrayList<>()
	private final List<ItemStack> outputs = new ArrayList<>()
	private int power = -1
	private int time = -1
	private Identifier customId = null

	protected MachineRecipeJsonFactory(RebornRecipeType<R> type) {
		this.type = type
	}

	static <R extends RebornRecipe> MachineRecipeJsonFactory<R> create(RebornRecipeType<R> type) {
		return new MachineRecipeJsonFactory<R>(type)
	}

	static <R extends RebornRecipe> MachineRecipeJsonFactory<R> create(RebornRecipeType<R> type, @DelegatesTo(value = MachineRecipeJsonFactory.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		def factory = new MachineRecipeJsonFactory<R>(type)
		closure.setDelegate(factory)
		closure.call(factory)
		return factory
	}

	def ingredients(Object... objects) {
		for (object in objects) {
			if (object instanceof ItemConvertible) {
				ingredient {
					item object
				}
			} else if (object instanceof Tag.Identified) {
				ingredient {
					tag object
				}
			} else {
				throw new UnsupportedOperationException()
			}
		}

		return this
	}

	def ingredient(@DelegatesTo(value = IngredientBuilder.class, strategy = Closure.DELEGATE_FIRST) Closure closure) {
		def builder = IngredientBuilder.create()
		closure.setDelegate(builder)
		closure.call(builder)
		ingredients.add(builder.build())
		return this
	}

	def outputs(Object... objects) {
		for (object in objects) {
			if (object instanceof ItemStack) {
				output(object)
			} else if (object instanceof ItemConvertible) {
				output(new ItemStack(object.asItem()))
			}
		}

		return this
	}

	def output(ItemStack stack) {
		outputs.add(stack)
		return this
	}

	def power(int power) {
		this.power = power
		return this
	}

	def time(int time) {
		this.time = time
		return this
	}

	def id(Identifier identifier) {
		this.customId = identifier
		return this
	}

	MachineRecipeJsonFactory id(String path) {
		return id(new Identifier("techreborn", path))
	}

	/**
	 * Override this method to support custom recipe types.
	 */
	protected R createRecipe(Identifier identifier) {
		return new RebornRecipe(type, identifier, ingredients, outputs, power, time) as R
	}

	protected void validate() {
		if (ingredients.isEmpty()) {
			throw new IllegalStateException("recipe has no ingrendients")
		}

		if (outputs.isEmpty()) {
			throw new IllegalStateException("recipe has no outputs")
		}

		if (power < 0) {
			throw new IllegalStateException("recipe has no power value")
		}

		if (time < 0) {
			throw new IllegalStateException("recipe has no time value")
		}
	}

	void offerTo(Consumer<RecipeJsonProvider> exporter) {
		validate()
		exporter.accept(new MachineRecipeJsonProvider<R>(type, createRecipe(getIdentifier())))
	}

	def getIdentifier() {
		if (customId) {
			return customId
		}

		if (outputs.size() < 1) {
			throw new IllegalStateException("Recipe has no outputs")
		}

		if (outputs.size() > 1) {
			throw new IllegalStateException("Cannot compute default identifier for a recipe with more than one output. TODO might want to improve this?")
		}

		def outputId = Registry.ITEM.getId(outputs[0].item)
		return new Identifier("techreborn", "${type.name().path}/${outputId.path}")
	}

	static class MachineRecipeJsonProvider<R extends RebornRecipe> implements RecipeJsonProvider {
		private final RebornRecipeType<R> type
		private final R recipe

		MachineRecipeJsonProvider(RebornRecipeType<R> type, R recipe) {
			this.type = type
			this.recipe = recipe
		}

		@Override
		JsonObject toJson() {
			return type.toJson(recipe, false)
		}

		@Override
		Identifier getRecipeId() {
			return recipe.id
		}

		@Override
		void serialize(JsonObject json) {
			throw new UnsupportedOperationException()
		}

		@Override
		RecipeSerializer<?> getSerializer() {
			throw new UnsupportedOperationException()
		}

		@Override
		JsonObject toAdvancementJson() {
			return null
		}

		@Override
		Identifier getAdvancementId() {
			return null
		}
	}
}
