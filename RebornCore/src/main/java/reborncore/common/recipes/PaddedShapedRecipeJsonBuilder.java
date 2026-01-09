/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TeamReborn
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

package reborncore.common.recipes;

import java.util.Objects;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;

public class PaddedShapedRecipeJsonBuilder extends ShapedRecipeBuilder {

	public PaddedShapedRecipeJsonBuilder(HolderGetter<Item> registryLookup, RecipeCategory category, ItemLike output, int outputCount) {
		super(registryLookup, category, output, outputCount);
	}

	public static PaddedShapedRecipeJsonBuilder shaped(HolderGetter<Item> registryLookup, RecipeCategory category, ItemLike output) {
		return shaped(registryLookup, category, output, 1);
	}

	public static PaddedShapedRecipeJsonBuilder shaped(HolderGetter<Item> registryLookup, RecipeCategory category, ItemLike output, int outputCount) {
		return new PaddedShapedRecipeJsonBuilder(registryLookup, category, output, outputCount);
	}
	@Override
	public void save(RecipeOutput exporter, ResourceKey<Recipe<?>> recipeKey) {
		ShapedRecipePattern raw = toRaw(recipeKey);

		AdvancementHolder advancementEntry = exporter.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeKey))
			.rewards(AdvancementRewards.Builder.recipe(recipeKey))
			.requirements(AdvancementRequirements.Strategy.OR)
			.build(recipeKey.identifier());

		PaddedShapedRecipe shapedRecipe = new PaddedShapedRecipe(
			Objects.requireNonNullElse(this.group, ""),
			RecipeBuilder.determineBookCategory(this.category),
			raw,
			new ItemStack(this.result, this.count),
			this.showNotification
		);

		exporter.accept(recipeKey, shapedRecipe, advancementEntry);
	}

	private ShapedRecipePattern toRaw(ResourceKey<Recipe<?>> recipeKey) {
		if (this.criteria.isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + recipeKey.identifier());
		} else {
			return PaddedShapedRecipe.create(this.key, this.rows);
		}
	}
}
