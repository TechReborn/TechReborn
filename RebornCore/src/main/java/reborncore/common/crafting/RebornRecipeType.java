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

import com.google.gson.JsonObject;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import reborncore.RebornCore;
import reborncore.common.crafting.serde.RecipeSerde;
import reborncore.common.util.serialization.SerializationUtil;

import java.util.List;

public record RebornRecipeType<R extends RebornRecipe>(
		RecipeSerde<R> recipeSerde,
		Identifier name) implements RecipeType, RecipeSerializer {

	@Override
	public R read(Identifier recipeId, JsonObject json) {
		Identifier type = new Identifier(JsonHelper.getString(json, "type"));
		if (!type.equals(name)) {
			throw new RuntimeException("RebornRecipe type not supported!");
		}

		try {
			R recipe = newRecipe(json, recipeId);

			if (!ConditionManager.shouldLoadRecipe(json)) {
				return recipeSerde.createDummy(this, recipeId);
			}

			return recipe;
		} catch (Throwable t) {
			RebornCore.LOGGER.error("Failed to read recipe: " + recipeId, t);
			// Make a new recipe - don't reuse the existing recipe object because it might be in an invalid state if an
			// exception was thrown in the middle of its deserialization.
			return recipeSerde.createDummy(this, recipeId);
		}
	}

	public JsonObject toJson(R recipe) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("type", name.toString());

		recipeSerde.toJson(recipe, jsonObject);

		return jsonObject;
	}

	public R fromJson(Identifier recipeType, JsonObject json) {
		return read(recipeType, json);
	}

	private R newRecipe(JsonObject jsonObject, Identifier recipeId) {
		return recipeSerde.fromJson(jsonObject, this, recipeId);
	}

	@Override
	public R read(Identifier recipeId, PacketByteBuf buffer) {
		String input = buffer.readString(buffer.readInt());
		R r = read(recipeId, SerializationUtil.GSON_FLAT.fromJson(input, JsonObject.class));
		r.deserialize(buffer);
		return r;
	}

	@Override
	public void write(PacketByteBuf buffer, Recipe recipe) {
		JsonObject jsonObject = toJson((R) recipe);
		String output = SerializationUtil.GSON_FLAT.toJson(jsonObject);
		buffer.writeInt(output.length());
		buffer.writeString(output);
		((R) recipe).serialize(buffer);
	}

	public List<R> getRecipes(World world) {
		return RecipeUtils.getRecipes(world, this);
	}
}
