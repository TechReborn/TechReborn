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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;
import net.minecraft.util.JsonHelper;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RecipeUtils;
import reborncore.common.crafting.ingredient.IngredientFactory;
import reborncore.common.crafting.ingredient.RebornIngredient;
import reborncore.common.util.DefaultedListCollector;
import reborncore.common.util.serialization.SerializationUtil;

import java.util.List;

public abstract class AbstractRecipeSerde<R extends RebornRecipe> implements RecipeSerde<R> {
	protected int getPower(JsonObject jsonObject) {
		return JsonHelper.getInt(jsonObject, "power");
	}

	protected int getTime(JsonObject jsonObject) {
		return JsonHelper.getInt(jsonObject, "time");
	}

	protected List<RebornIngredient> getIngredients(JsonObject jsonObject) {
		return SerializationUtil.stream(JsonHelper.getArray(jsonObject, "ingredients"))
				.map(IngredientFactory::deserialize)
				.collect(DefaultedListCollector.toList());
	}

	protected List<ItemStack> getOutputs(JsonObject jsonObject) {
		final JsonArray resultsJson = JsonHelper.getArray(jsonObject, "results");
		return RecipeUtils.deserializeItems(resultsJson);
	}

	protected void writeIngredients(R recipe, JsonObject jsonObject, boolean networkSync) {
		final JsonArray ingredientsArray = new JsonArray();
		recipe.getRebornIngredients().stream().map(ingredient -> ingredient.toJson(networkSync)).forEach(ingredientsArray::add);
		jsonObject.add("ingredients", ingredientsArray);
	}

	protected void writeOutputs(R recipe, JsonObject jsonObject) {
		final JsonArray resultsArray = new JsonArray();

		for (ItemStack stack : recipe.getOutputs()) {
			final JsonObject stackObject = new JsonObject();
			stackObject.addProperty("item", Registries.ITEM.getId(stack.getItem()).toString());

			if (stack.getCount() > 1) {
				stackObject.addProperty("count", stack.getCount());
			}

			if (stack.hasNbt()) {
				stackObject.add("nbt", Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, stack.getNbt()));
			}
			resultsArray.add(stackObject);
		}

		jsonObject.add("results", resultsArray);
	}

	protected void writePower(R recipe, JsonObject jsonObject) {
		jsonObject.addProperty("power", recipe.getPower());
	}

	protected void writeTime(R recipe, JsonObject jsonObject) {
		jsonObject.addProperty("time", recipe.getTime());
	}
}
