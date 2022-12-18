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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.JsonHelper;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ShapedRecipeHelper {
	public static JsonObject rewriteForNetworkSync(JsonObject shapedRecipeJson) {
		final Map<String, Ingredient> symbols = ShapedRecipe.readSymbols(JsonHelper.getObject(shapedRecipeJson, "key"));
		final JsonObject keys = new JsonObject();

		for (Map.Entry<String, Ingredient> entry : symbols.entrySet()) {
			if (entry.getKey().equals(" ")) continue;

			JsonArray entries = new JsonArray();

			for (Ingredient.Entry ingredientEntry : entry.getValue().entries) {
				if (ingredientEntry instanceof Ingredient.StackEntry stackEntry) {
					entries.add(stackEntry.toJson());
				} else if (ingredientEntry instanceof Ingredient.TagEntry tagEntry) {
					final TagKey<Item> tag = tagEntry.tag;
					final Item[] items = streamItemsFromTag(tag).toArray(Item[]::new);

					if (items.length == 0) {
						throw new IllegalStateException("No items in %s tag".formatted(tag.id()));
					}

					for (Item item : items) {
						JsonObject jsonObject = new JsonObject();
						jsonObject.addProperty("item", Registries.ITEM.getId(item).toString());
						entries.add(jsonObject);
					}
				} else {
					throw new UnsupportedOperationException("Cannot write " + ingredientEntry.getClass());
				}
			}

			if (entries.size() == 0) {
				throw new IllegalStateException("Cannot write no entries");
			}

			keys.add(entry.getKey(), entries);
		}

		shapedRecipeJson.remove("key");
		shapedRecipeJson.add("key", keys);
		return shapedRecipeJson;
	}

	private static Stream<Item> streamItemsFromTag(TagKey<Item> tag) {
		return StreamSupport.stream(Registries.ITEM.iterateEntries(tag).spliterator(), false)
			.map(RegistryEntry::value);
	}
}
