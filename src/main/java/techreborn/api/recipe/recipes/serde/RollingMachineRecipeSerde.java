/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TechReborn
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

package techreborn.api.recipe.recipes.serde;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.ShapedRecipeHelper;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.crafting.serde.RebornRecipeSerde;
import techreborn.api.recipe.recipes.RollingMachineRecipe;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RollingMachineRecipeSerde extends RebornRecipeSerde<RollingMachineRecipe> {
	@Override
	protected RollingMachineRecipe fromJson(JsonObject jsonObject, RebornRecipeType<RollingMachineRecipe> type, Identifier name, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time) {
		final JsonObject shapedRecipeJson = JsonHelper.getObject(jsonObject, "shaped");
		final ShapedRecipe shapedRecipe = RecipeSerializer.SHAPED.read(name, shapedRecipeJson);
		return new RollingMachineRecipe(type, name, ingredients, outputs, power, time, shapedRecipe, shapedRecipeJson);
	}

	@Override
	public void collectJsonData(RollingMachineRecipe recipe, JsonObject jsonObject, boolean networkSync) {
		final JsonObject shapedRecipeJson = networkSync ? ShapedRecipeHelper.rewriteForNetworkSync(recipe.getShapedRecipeJson()) : recipe.getShapedRecipeJson();
		jsonObject.add("shaped", Objects.requireNonNull(shapedRecipeJson));
	}

	@Override
	protected List<RebornIngredient> getIngredients(JsonObject jsonObject) {
		// Inputs are handled by the shaped recipe.
		return Collections.emptyList();
	}

	@Override
	protected List<ItemStack> getOutputs(JsonObject jsonObject) {
		// Outputs are handled by the shaped recipe.
		return Collections.emptyList();
	}

	@Override
	protected void writeIngredients(RollingMachineRecipe recipe, JsonObject jsonObject, boolean networkSync) {
	}

	@Override
	protected void writeOutputs(RollingMachineRecipe recipe, JsonObject jsonObject) {
	}
}
