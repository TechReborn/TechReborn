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

package reborncore.common.crafting.ingredient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.registry.entry.RegistryEntry;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class TagIngredient extends RebornIngredient {
	private final TagKey<Item> tag;
	private final Optional<Integer> count;

	public TagIngredient(TagKey<Item> tag, Optional<Integer> count) {
		this.tag = tag;
		this.count = count;
	}

	@Override
	public boolean test(ItemStack itemStack) {
		if (count.isPresent() && count.get() > itemStack.getCount()) {
			return false;
		}

		return itemStack.isIn(tag);
	}

	@Override
	public Ingredient getPreview() {
		return Ingredient.ofStacks(getPreviewStacks().stream());
	}

	@Override
	public List<ItemStack> getPreviewStacks() {
		return streamItems().map(ItemStack::new).peek(itemStack -> itemStack.setCount(count.orElse(1))).collect(Collectors.toList());
	}

	public static RebornIngredient deserialize(JsonObject json) {
		Optional<Integer> count = Optional.empty();
		if (json.has("count")) {
			count = Optional.of(JsonHelper.getInt(json, "count"));
		}

		if (json.has("tag_server_sync")) {
			Identifier tagIdent = new Identifier(JsonHelper.getString(json, "tag_identifier"));

			List<Item> items = new ArrayList<>();
			final JsonArray itemsArray = JsonHelper.getArray(json, "items");
			for (JsonElement jsonElement : itemsArray) {
				Validate.isTrue(jsonElement.isJsonPrimitive());
				Item item = Registries.ITEM.get(jsonElement.getAsInt());
				items.add(item);
			}

			return new Synced(TagKey.of(RegistryKeys.ITEM, tagIdent), count, items);
		}

		Identifier identifier = new Identifier(JsonHelper.getString(json, "tag"));

		TagKey<Item> tagKey = TagKey.of(RegistryKeys.ITEM, identifier);
		return new TagIngredient(tagKey, count);
	}

	@Override
	public JsonObject toJson(boolean networkSync) {
		if (networkSync) {
			return toItemJsonObject();
		}

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("tag", tag.id().toString());
		return jsonObject;
	}

	private JsonObject toItemJsonObject() {
		// Tags are not synced across the server, so we sync all the items
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("tag_server_sync", true);

		Item[] items = streamItems().toArray(Item[]::new);
		JsonArray itemArray = new JsonArray();

		for (Item item : items) {
			int rawId = Registries.ITEM.getRawId(item);
			itemArray.add(rawId);
		}

		jsonObject.add("items", itemArray);

		count.ifPresent(integer -> jsonObject.addProperty("count", integer));
		jsonObject.addProperty("tag_identifier", tag.id().toString());
		return jsonObject;
	}

	protected Stream<Item> streamItems() {
		return StreamSupport.stream(Registries.ITEM.iterateEntries(tag).spliterator(), false)
			.map(RegistryEntry::value);
	}

	private static class Synced extends TagIngredient {
		private final List<Item> items;

		public Synced(TagKey<Item> tag, Optional<Integer> count, List<Item> items) {
			super(tag, count);
			this.items = items;
		}

		@Override
		public boolean test(ItemStack itemStack) {
			return items.contains(itemStack.getItem());
		}

		@Override
		protected Stream<Item> streamItems() {
			return items.stream();
		}
	}

	@Override
	public int getCount() {
		return count.orElse(1);
	}
}
