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

package reborncore.common.util.serialization;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Type;

//Based from ee3's code
public class ItemStackSerializer implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

	private static final String NAME = "name";
	private static final String STACK_SIZE = "stackSize";
	private static final String TAG_COMPOUND = "tagCompound";

	@Override
	public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

		if (json.isJsonObject()) {

			JsonObject jsonObject = json.getAsJsonObject();

			String name = null;
			int stackSize = 1;
			CompoundTag tagCompound = null;

			if (jsonObject.has(NAME) && jsonObject.get(NAME).isJsonPrimitive()) {
				name = jsonObject.getAsJsonPrimitive(NAME).getAsString();
			}

			if (jsonObject.has(STACK_SIZE) && jsonObject.get(STACK_SIZE).isJsonPrimitive()) {
				stackSize = jsonObject.getAsJsonPrimitive(STACK_SIZE).getAsInt();
			}

			if (jsonObject.has(TAG_COMPOUND) && jsonObject.get(TAG_COMPOUND).isJsonPrimitive()) {
				try {
					tagCompound = StringNbtReader.parse(jsonObject.getAsJsonPrimitive(TAG_COMPOUND).getAsString());
				} catch (CommandSyntaxException e) {

				}
			}

			if (name != null && Registry.ITEM.get(new Identifier(name)) != null) {
				ItemStack itemStack = new ItemStack(Registry.ITEM.get(new Identifier(name)), stackSize);
				itemStack.setTag(tagCompound);
				return itemStack;
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {

		if (src != null && src.getItem() != null) {
			JsonObject jsonObject = new JsonObject();

			if (Registry.ITEM.getId(src.getItem()) != null) {
				jsonObject.addProperty(NAME, Registry.ITEM.getId(src.getItem()).toString());
			} else {
				return JsonNull.INSTANCE;
			}

			jsonObject.addProperty(STACK_SIZE, src.getCount());

			if (src.getTag() != null) {
				jsonObject.addProperty(TAG_COMPOUND, src.getTag().toString());
			}

			return jsonObject;
		}

		return JsonNull.INSTANCE;
	}
}
