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

package techreborn.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.world.World;
import techreborn.items.DynamicCellItem;

import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecipeUtils {
	public static <T extends Recipe<?>> Optional<Recipe<?>> getMatchingRecipe(World world, RecipeType<T> type, ItemStack input) {
		return getRecipes(world, type).stream()
				.filter(recipe -> matchesSingleInput(recipe, input))
				.findFirst();
	}

	public static boolean matchesSingleInput(Recipe<?> recipe, ItemStack input) {
		return recipe.getPreviewInputs().size() == 1 && recipe.getPreviewInputs().get(0).test(input);
	}

	/**
	 * Used to get the matching output of a recipe type that only has 1 input
	 */
	public static <T extends Recipe<?>> ItemStack getMatchingRecipeOutput(World world, RecipeType<T> type, ItemStack input) {
		return getMatchingRecipe(world, type, input)
				.map(Recipe::getOutput)
				.orElse(ItemStack.EMPTY);
	}

	public static <T extends Recipe<?>> List<Recipe<?>> getRecipes(World world, RecipeType<T> type) {
		return world.getRecipeManager().values().stream().filter(iRecipe -> iRecipe.getType() == type).collect(Collectors.toList());
	}
}
