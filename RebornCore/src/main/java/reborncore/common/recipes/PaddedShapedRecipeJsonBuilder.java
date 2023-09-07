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

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class PaddedShapedRecipeJsonBuilder extends ShapedRecipeJsonBuilder {

	public PaddedShapedRecipeJsonBuilder(RecipeCategory category, ItemConvertible output, int outputCount) {
		super(category, output, outputCount);
	}

	public static PaddedShapedRecipeJsonBuilder create(RecipeCategory category, ItemConvertible output) {
		return create(category, output, 1);
	}

	public static PaddedShapedRecipeJsonBuilder create(RecipeCategory category, ItemConvertible output, int outputCount) {
		return new PaddedShapedRecipeJsonBuilder(category, output, outputCount);
	}
	@Override
	public void offerTo(RecipeExporter exporter, Identifier recipeId) {
		validate(recipeId);
		AdvancementEntry advancementEntry = exporter.getAdvancementBuilder().parent(ROOT).criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).criteriaMerger(AdvancementRequirements.CriterionMerger.OR).build(recipeId);

		String group = this.group == null ? "" : this.group;
		exporter.accept(new PaddedShapedRecipeJsonProvider(recipeId, output, count, group, getCraftingCategory(category), pattern, inputs, advancementEntry, false));

		// ,
		//			new Identifier(recipeId.getNamespace(), "recipes/" + category.getName() + "/" + recipeId.getPath())
	}

	static class PaddedShapedRecipeJsonProvider extends PaddedShapedRecipeJsonBuilder.ShapedRecipeJsonProvider {

		public PaddedShapedRecipeJsonProvider(Identifier id, Item output, int resultCount, String group, CraftingRecipeCategory craftingCategory, List<String> pattern, Map<Character, Ingredient> inputs, AdvancementEntry advancement, boolean showNotification) {
			super(id, output, resultCount, group, craftingCategory, pattern, inputs, advancement, showNotification);
		}
	}

}
