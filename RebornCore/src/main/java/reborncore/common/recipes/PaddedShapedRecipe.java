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

package reborncore.common.recipes;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.Map;

public class PaddedShapedRecipe extends ShapedRecipe {
	public static final Identifier ID = new Identifier("reborncore", "padded");
	public static final RecipeSerializer<ShapedRecipe> PADDED = Registry.register(Registries.RECIPE_SERIALIZER, ID, new Serializer());

	public PaddedShapedRecipe(Identifier id, String group, CraftingRecipeCategory category, int width, int height, DefaultedList<Ingredient> input, ItemStack output) {
		super(id, group, category, width, height, input, output);
	}

	private static class Serializer extends ShapedRecipe.Serializer {
		@Override
		public PaddedShapedRecipe read(Identifier identifier, JsonObject jsonObject) {
			String group = JsonHelper.getString(jsonObject, "group", "");
			CraftingRecipeCategory category = CraftingRecipeCategory.CODEC.byId(JsonHelper.getString(jsonObject, "category", null), CraftingRecipeCategory.MISC);
			Map<String, Ingredient> map = readSymbols(JsonHelper.getObject(jsonObject, "key"));
			String[] strings = getPattern(JsonHelper.getArray(jsonObject, "pattern"));

			int width = strings[0].length();
			int height = strings.length;

			DefaultedList<Ingredient> ingredients = createPatternMatrix(strings, map, width, height);
			ItemStack output = outputFromJson(JsonHelper.getObject(jsonObject, "result"));
			return new PaddedShapedRecipe(identifier, group, category, width, height, ingredients, output);
		}
	}

	@Override
	public boolean matches(CraftingInventory craftingInventory, World world) {
		for(int i = 0; i <= craftingInventory.getWidth() - this.getWidth(); ++i) {
			for(int j = 0; j <= craftingInventory.getHeight() - this.getHeight(); ++j) {
				if (this.matchesPattern(craftingInventory, i, j, false)) {
					return true;
				}
			}
		}
		return false;
	}
}
