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
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeEntry;
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
		Identifier name) implements RecipeType<R>, RecipeSerializer<R> {

	@Override
	public Codec<R> codec() {
		// TODO: could look into using codecs directly
		return JsonMapCodec.INSTANCE
			.flatXmap(json -> {
				try {
					return DataResult.success(read(json));
				} catch (JsonParseException e) {
					return DataResult.error(e::getMessage);
				}
			}, recipe -> DataResult.success(toJson(recipe, false)))
			.codec();
	}

	private R read(JsonObject json) {
		Identifier type = new Identifier(JsonHelper.getString(json, "type"));
		if (!type.equals(name)) {
			throw new RuntimeException("RebornRecipe type not supported!");
		}

		return recipeSerde.fromJson(json, this);
	}

	public JsonObject toJson(R recipe, boolean networkSync) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("type", name.toString());

		recipeSerde.toJson(recipe, jsonObject, networkSync);

		return jsonObject;
	}

	@Override
	public R read(PacketByteBuf buffer) {
		String jsonSize = buffer.readString(buffer.readInt());
		return read(SerializationUtil.GSON_FLAT.fromJson(jsonSize, JsonObject.class));
	}

	@Override
	public void write(PacketByteBuf buffer, R recipe) {
		String output = SerializationUtil.GSON_FLAT.toJson(toJson(recipe, true));
		// Add more info to debug weird mods
		if (output.length() > PacketByteBuf.DEFAULT_MAX_STRING_LENGTH) {
			RebornCore.LOGGER.error("Recipe output string is too big. This breaks recipe books!");
			RebornCore.LOGGER.error("Output is:" + output);
		}
		buffer.writeInt(output.length());
		buffer.writeString(output);
	}

	public List<R> getRecipes(World world) {
		return RecipeUtils.getRecipes(world, this);
	}

	public List<RecipeEntry<R>> getRecipeEntries(World world) {
		return RecipeUtils.getRecipeEntries(world, this);
	}
}
