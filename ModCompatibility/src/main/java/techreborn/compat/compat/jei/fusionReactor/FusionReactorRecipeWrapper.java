/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.compat.compat.jei.fusionReactor;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.compat.compat.jei.RecipeUtil;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class FusionReactorRecipeWrapper implements IRecipeWrapper {
	private final FusionReactorRecipe baseRecipe;

	public FusionReactorRecipeWrapper(FusionReactorRecipe baseRecipe) {
		this.baseRecipe = baseRecipe;
	}

	@Override
	public void getIngredients(@Nonnull IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, Arrays.asList(baseRecipe.getTopInput(), baseRecipe.getBottomInput()));
		ingredients.setOutput(ItemStack.class, baseRecipe.getOutput());
	}

	public ItemStack getTopInput() {
		return baseRecipe.getTopInput();
	}

	public ItemStack getBottomInput() {
		return baseRecipe.getBottomInput();
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		RecipeUtil.drawInfo(minecraft, 0, 67, baseRecipe.getStartEU(), baseRecipe.getEuTick(), baseRecipe.getTickTime());
	}
}
