/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TeamReborn
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

package reborncore.common.crafting.serde;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RebornRecipeType;
import reborncore.common.crafting.ingredient.RebornIngredient;

import java.util.List;

public abstract class RebornRecipeSerde<R extends RebornRecipe> extends AbstractRecipeSerde<R> {
	public static final RebornRecipeSerde<RebornRecipe> BASIC = create(RebornRecipe::new);

	protected abstract R fromJson(JsonObject jsonObject, RebornRecipeType<R> type, Identifier name, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time);

	@Override
	public final R fromJson(JsonObject jsonObject, RebornRecipeType<R> type, Identifier name) {
		final int power = getPower(jsonObject);
		final int time = getTime(jsonObject);
		final List<RebornIngredient> ingredients = getIngredients(jsonObject);
		final List<ItemStack> outputs = getOutputs(jsonObject);

		return fromJson(jsonObject, type, name, ingredients, outputs, power, time);
	}

	protected abstract void collectJsonData(R recipe, JsonObject jsonObject, boolean networkSync);

	@Override
	public final void toJson(R recipe, JsonObject jsonObject, boolean networkSync) {
		writePower(recipe, jsonObject);
		writeTime(recipe, jsonObject);
		writeIngredients(recipe, jsonObject, networkSync);
		writeOutputs(recipe, jsonObject);

		collectJsonData(recipe, jsonObject, networkSync);
	}

	public static <R extends RebornRecipe> RebornRecipeSerde<R> create(SimpleRecipeFactory<R> factory) {
		return new RebornRecipeSerde<R>() {
			@Override
			protected R fromJson(JsonObject jsonObject, RebornRecipeType<R> type, Identifier name, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time) {
				return factory.create(type, name, ingredients, outputs, power, time);
			}

			@Override
			protected void collectJsonData(R recipe, JsonObject jsonObject, boolean networkSync) {
			}
		};
	}

	public interface SimpleRecipeFactory<R extends RebornRecipe> {
		R create(RebornRecipeType<R> type, Identifier name, List<RebornIngredient> ingredients, List<ItemStack> outputs, int power, int time);
	}
}
