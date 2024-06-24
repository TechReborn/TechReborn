/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.common.crafting;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class RecipeUtils {
	public static <T extends RebornRecipe> List<T> getRecipes(World world, RecipeType<T> type) {
		return streamRecipeEntries(world, type).map(RecipeEntry::value).toList();
	}

	public static <T extends RebornRecipe> List<RecipeEntry<T>> getRecipeEntries(World world, RecipeType<T> type) {
		return streamRecipeEntries(world, type).toList();
	}

	private static <T extends RebornRecipe> Stream<RecipeEntry<T>> streamRecipeEntries(World world, RecipeType<T> type) {
		return world.getRecipeManager().getAllOfType(type).stream();
	}

	/**
	 * Adds the following toast/recipe defaults to an advancement builder:
	 * <ul>
	 *     <li>parent as "recipes/root"</li>
	 *     <li>criterion "has_the_recipe" via OR</li>
	 *     <li>reward: the specified recipe</li>
	 * </ul>
	 * @param builder the advancement task builder to expand
	 * @param recipeId the ID of the recipe
	 * @throws NullPointerException If any parameter refers to <code>null</code>.
	 */
	public static void addToastDefaults(@NotNull Advancement.Builder builder, @NotNull Identifier recipeId) {
		Objects.requireNonNull(builder);
		Objects.requireNonNull(recipeId);
		builder
			.criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
			.rewards(AdvancementRewards.Builder.recipe(recipeId))
			.criteriaMerger(AdvancementRequirements.CriterionMerger.OR);
	}

}
