/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.compat.jei.rollingMachine;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapedOreRecipeWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapedRecipesWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapelessOreRecipeWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapelessRecipesWrapper;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RollingMachineRecipeWrapper extends BlankRecipeWrapper implements ICraftingRecipeWrapper {
	private final ICraftingRecipeWrapper baseRecipe;

	public RollingMachineRecipeWrapper(ICraftingRecipeWrapper baseRecipe) {
		this.baseRecipe = baseRecipe;
	}

	@Nullable
	public static RollingMachineRecipeWrapper create(
		@Nonnull
			IJeiHelpers jeiHelpers, IRecipe baseRecipe) {
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		ICraftingRecipeWrapper recipeWrapper;
		if (baseRecipe instanceof ShapelessRecipes) {
			recipeWrapper = new ShapelessRecipesWrapper(guiHelper, (ShapelessRecipes) baseRecipe);
		} else if (baseRecipe instanceof ShapedRecipes) {
			recipeWrapper = new ShapedRecipesWrapper((ShapedRecipes) baseRecipe);
		} else if (baseRecipe instanceof ShapedOreRecipe) {
			recipeWrapper = new ShapedOreRecipeWrapper(jeiHelpers, (ShapedOreRecipe) baseRecipe);
		} else if (baseRecipe instanceof ShapelessOreRecipe) {
			recipeWrapper = new ShapelessOreRecipeWrapper(jeiHelpers, (ShapelessOreRecipe) baseRecipe);
		} else {
			return null;
		}

		return new RollingMachineRecipeWrapper(recipeWrapper);
	}

	@Override
	public void getIngredients(
		@Nonnull
			IIngredients ingredients) {
		baseRecipe.getIngredients(ingredients);
	}

}
