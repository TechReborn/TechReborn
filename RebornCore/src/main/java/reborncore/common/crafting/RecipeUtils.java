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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import reborncore.common.util.DefaultedListCollector;
import reborncore.common.util.serialization.SerializationUtil;
import reborncore.mixin.common.AccessorRecipeManager;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RecipeUtils {
	@SuppressWarnings("unchecked")
	public static <T extends RebornRecipe> List<T> getRecipes(World world, RebornRecipeType<T> type) {
		final AccessorRecipeManager accessorRecipeManager = (AccessorRecipeManager) world.getRecipeManager();
		final Collection<Recipe<Inventory>> recipes = accessorRecipeManager.getAll(type).values();
		//noinspection unchecked
		return Collections.unmodifiableList((List<T>) (Object) recipes);
	}

	public static DefaultedList<ItemStack> deserializeItems(JsonElement jsonObject) {
		if (jsonObject.isJsonArray()) {
			return SerializationUtil.stream(jsonObject.getAsJsonArray()).map(entry -> deserializeItem(entry.getAsJsonObject())).collect(DefaultedListCollector.toList());
		} else {
			return DefaultedList.copyOf(deserializeItem(jsonObject.getAsJsonObject()));
		}
	}

	private static ItemStack deserializeItem(JsonObject jsonObject) {
		Identifier resourceLocation = new Identifier(JsonHelper.getString(jsonObject, "item"));
		Item item = Registry.ITEM.get(resourceLocation);
		if (item == Items.AIR) {
			throw new IllegalStateException(resourceLocation + " did not exist");
		}
		int count = 1;
		if (jsonObject.has("count")) {
			count = JsonHelper.getInt(jsonObject, "count");
		}
		ItemStack stack = new ItemStack(item, count);
		if (jsonObject.has("nbt")) {
			NbtCompound tag = (NbtCompound) Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, jsonObject.get("nbt"));
			stack.setNbt(tag);
		}
		return stack;
	}

}
