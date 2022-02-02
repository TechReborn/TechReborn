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

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TagIngredient extends RebornIngredient {
	private final Tag.Identified<Item> tag;
	private final Optional<Integer> count;

	public TagIngredient(Tag.Identified<Item> tag, Optional<Integer> count) {
		this.tag = tag;
		this.count = count;
	}

	public TagIngredient(Tag.Identified<Item> tag, int count) {
		this(tag, count > 1 ? Optional.of(count) : Optional.empty());
	}

	@Override
	public boolean test(ItemStack itemStack) {
		if (count.isPresent() && count.get() > itemStack.getCount()) {
			return false;
		}
		return tag.contains(itemStack.getItem());
	}

	@Override
	public Ingredient getPreview() {
		return Ingredient.ofStacks(getPreviewStacks().stream());
	}

	@Override
	public List<ItemStack> getPreviewStacks() {
		return tag.values().stream().map(ItemStack::new).peek(itemStack -> itemStack.setCount(count.orElse(1))).collect(Collectors.toList());
	}

	public static RebornIngredient deserialize(JsonObject json) {
		Optional<Integer> count = Optional.empty();
		if (json.has("count")) {
			count = Optional.of(JsonHelper.getInt(json, "count"));
		}

		if (json.has("tag_server_sync")) {
			Identifier tagIdent = new Identifier(JsonHelper.getString(json, "tag_identifier"));
			List<Item> items = new ArrayList<>();
			for (int i = 0; i < JsonHelper.getInt(json, "items"); i++) {
				Identifier identifier = new Identifier(JsonHelper.getString(json, "item_" + i));
				Item item = Registry.ITEM.get(identifier);
				Validate.isTrue(item != Items.AIR, "item cannot be air");
				items.add(item);
			}
			return new TagIngredient(new SimpleTag<>(items, tagIdent), count);
		}

		Identifier identifier = new Identifier(JsonHelper.getString(json, "tag"));

		Tag.Identified<Item> tag = TagFactory.of(() -> ServerTagManagerHolder.getTagManager().getOrCreateTagGroup(Registry.ITEM_KEY)).create(identifier);
		if (tag == null) {
			throw new JsonSyntaxException("Unknown item tag '" + identifier + "'");
		}

		return new TagIngredient(tag, count);
	}

	@Override
	public JsonObject toJson(boolean networkSync) {
		if (networkSync) {
			return toItemJsonObject();
		}

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("tag", tag.getId().toString());
		return jsonObject;
	}

	private JsonObject toItemJsonObject() {
		// Tags are not synced across the server, so we sync all the items
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("tag_server_sync", true);

		Item[] items = tag.values().toArray(new Item[0]);
		jsonObject.addProperty("items", items.length);
		for (int i = 0; i < items.length; i++) {
			jsonObject.addProperty("item_" + i, Registry.ITEM.getId(items[i]).toString());
		}

		count.ifPresent(integer -> jsonObject.addProperty("count", integer));
		jsonObject.addProperty("tag_identifier", tag.getId().toString());
		return jsonObject;
	}

	@Override
	public int getCount() {
		return count.orElse(1);
	}
}
