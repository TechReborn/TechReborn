/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TechReborn
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

package techreborn.test.client;

import java.util.List;
import java.util.Optional;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import techreborn.TechReborn;
import techreborn.init.TRContent;

final class UuMatterCraftingTests {
	static void run(ClientTestHarness test) {
		test.onServer(server -> {
			var recipes = server.getRecipeManager();
			var level = ClientTestHarness.level(server);
			ItemStack uuMatter = TRContent.Parts.UU_MATTER.getStack();
			int checkedRecipes = 0;
			for (var entry : recipes.getRecipes()) {
				if (!entry.id().identifier().getNamespace().equals(TechReborn.MOD_ID)
					|| !(entry.value() instanceof ShapedRecipe recipe)
					|| recipe.getIngredients().stream().flatMap(Optional::stream).noneMatch(ingredient -> ingredient.test(uuMatter))) {
					continue;
				}

				List<ItemStack> items = recipe.getIngredients().stream()
					.map(ingredient -> ingredient.map(UuMatterCraftingTests::exampleStack).orElse(ItemStack.EMPTY))
					.toList();
				CraftingInput input = CraftingInput.of(recipe.getWidth(), recipe.getHeight(), items);
				var match = recipes.getRecipeFor(RecipeType.CRAFTING, input, level).map(RecipeHolder::id);
				if (match.isEmpty() || !match.get().equals(entry.id())) {
					throw new AssertionError("Expected UU-Matter recipe " + entry.id() + ", got " + match);
				}
				checkedRecipes++;
			}
			if (checkedRecipes == 0) {
				throw new AssertionError("No UU-Matter crafting recipes loaded");
			}
		});
	}

	private static ItemStack exampleStack(Ingredient ingredient) {
		return new ItemStack(ingredient.items().findFirst().orElseThrow().value());
	}
}
