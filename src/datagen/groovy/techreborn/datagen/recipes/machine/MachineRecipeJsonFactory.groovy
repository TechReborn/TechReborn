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
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper
import net.minecraft.advancement.Advancement.Builder
import net.minecraft.advancement.criterion.CriterionConditions
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.TagKey
import net.minecraft.resource.featuretoggle.FeatureFlag
import net.minecraft.util.Identifier
import org.jetbrains.annotations.NotNull
import reborncore.common.crafting.RebornRecipe
import reborncore.common.crafting.RebornRecipeType
import reborncore.common.crafting.RecipeUtils
import reborncore.common.crafting.ingredient.RebornIngredient

import java.util.function.Consumer

class MachineRecipeJsonFactory<R extends RebornRecipe> {
	protected final RebornRecipeType<R> type
	protected final Builder builder = Builder.create()

	protected final List<RebornIngredient> ingredients = new ArrayList<>()
	protected final List<ItemStack> outputs = new ArrayList<>()
	protected int power = -1
	protected int time = -1
	protected Identifier customId = null
	protected String source = null
	protected List<ConditionJsonProvider> conditions = []

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
			} else if (object instanceof TagKey) {
				ingredient {
					tag object
				}
			} else if (object instanceof ItemStack) {
				ingredient {
					stack object
				}
			} else if (object instanceof Identifier) {
				ingredient {
					ident object
				}
			} else if (object instanceof String) {
				ingredient {
					ident(new Identifier(object))
				}
			} else {
				throw new IllegalArgumentException()
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

	def source(String s) {
		this.source = s
		return this
	}

	def condition(ConditionJsonProvider conditionJsonProvider) {
		this.conditions.add(conditionJsonProvider)
		return this
	}

	@NotNull String getSourceAppendix() {
		if (source == null)
			return ""
		return "_from_" + source
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
			throw new IllegalStateException("recipe has no ingredients")
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

	MachineRecipeJsonFactory<R> criterion(String string, CriterionConditions criterionConditions) {
		builder.criterion(string, criterionConditions)
		return this
	}

	void offerTo(Consumer<RecipeJsonProvider> exporter) {
		validate()
		Identifier recipeId = getIdentifier()
		Identifier advancementId = new Identifier(recipeId.getNamespace(), "recipes/" + recipeId.getPath())
		RecipeUtils.addToastDefaults(builder, recipeId)
		exporter.accept(new MachineRecipeJsonProvider<R>(type, createRecipe(recipeId), advancementId, builder, conditions))
	}

	def getIdentifier() {
		if (customId) {
			return customId
		}

		if (outputs.size() < 1) {
			throw new IllegalStateException("Recipe has no outputs")
		}

		def outputId = Registries.ITEM.getId(outputs[0].item)
		return new Identifier("techreborn", "${type.name().path}/${outputId.path}${getSourceAppendix()}")
	}

	def feature(FeatureFlag flag) {
		condition(DefaultResourceConditions.featuresEnabled(flag))
	}

	static class MachineRecipeJsonProvider<R extends RebornRecipe> implements RecipeJsonProvider {
		private final RebornRecipeType<R> type
		private final R recipe
		private final Identifier advancementId
		private final Builder builder
		private final List<ConditionJsonProvider> conditions

		MachineRecipeJsonProvider(RebornRecipeType<R> type, R recipe, Identifier advancementId, Builder builder, List<ConditionJsonProvider> conditions) {
			this.type = type
			this.recipe = recipe
			this.advancementId = advancementId
			this.builder = builder
			this.conditions = conditions
		}

		@Override
		JsonObject toJson() {
			if (!conditions.isEmpty()) {
				FabricDataGenHelper.addConditions(this, conditions.toArray() as ConditionJsonProvider[])
			}

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
			if (builder == null)
				return null
			return builder.toJson()
		}

		@Override
		Identifier getAdvancementId() {
			return advancementId
		}
	}
}
