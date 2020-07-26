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

package techreborn.events;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.network.ServerPlayerEntity;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import techreborn.utils.RecipeUtils;

import java.util.ArrayList;
import java.util.List;

public class TRRecipeHandler {


	public static void unlockTRRecipes(ServerPlayerEntity playerMP) {
		List<Recipe<?>> recipeList = new ArrayList<>();
		for (Recipe<?> recipe : RecipeUtils.getRecipes(playerMP.world, RecipeType.CRAFTING)) {
			if (isRecipeValid(recipe)) {
				recipeList.add(recipe);
			}
		}
		playerMP.unlockRecipes(recipeList);
	}

	private static boolean isRecipeValid(Recipe<?> recipe) {
		if (recipe.getId() == null) {
			return false;
		}
		if (!recipe.getId().getNamespace().equals(TechReborn.MOD_ID)) {
			return false;
		}
		return recipe.getPreviewInputs().stream().noneMatch(ingredient -> ingredient.test(TRContent.Parts.UU_MATTER.getStack()));
	}

}
