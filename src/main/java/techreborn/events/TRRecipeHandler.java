/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.events;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;






import reborncore.common.util.ItemUtils;
import techreborn.TechReborn;
import techreborn.init.TRContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TRRecipeHandler {

	@SuppressWarnings("rawtypes")
	private static List<IForgeRegistryEntry> hiddenEntrys = new ArrayList<>();

	public static void hideEntry(IForgeRegistryEntry<?> entry) {
		hiddenEntrys.add(entry);
	}

	@SubscribeEvent
	public void pickupEvent(EntityItemPickupEvent entityItemPickupEvent) {
		if(entityItemPickupEvent.getEntityPlayer() instanceof FakePlayer){
			return;
		}
		World world = entityItemPickupEvent.getItem().world;
		if (entityItemPickupEvent.getEntityPlayer() instanceof ServerPlayerEntity) {
			if (ItemUtils.isInputEqual("logWood", entityItemPickupEvent.getItem().getItem(), false, true)) {
				for (Recipe recipe : world.getRecipeManager().getRecipes()) {
					if (recipe.getOutput().getItem() == TRContent.TREE_TAP) {
						entityItemPickupEvent.getEntityPlayer().unlockRecipes(Collections.singletonList(recipe));
					}
				}
			}
		}
	}

	public static void unlockTRRecipes(ServerPlayerEntity playerMP) {
		List<Recipe> recipeList = new ArrayList<>();
		for (Recipe recipe : getRecipes(playerMP.world, VanillaRecipeTypes.CRAFTING)) {
			if (isRecipeValid(recipe)) {
				recipeList.add(recipe);
			}
		}
		playerMP.unlockRecipes(recipeList);
	}

	public static <T extends Recipe> List<Recipe> getRecipes(World world, RecipeType<T> type){
		return world.getRecipeManager().getRecipes().stream().filter(iRecipe -> iRecipe.getType() == type).collect(Collectors.toList());
	}

	/**
	 *
	 * Used to get the matching output of a recipe type that only has 1 input
	 *
	 */
	public static <T extends Recipe> ItemStack getMatchingRecipes(World world, RecipeType<T> type, ItemStack input){
		return getRecipes(world, type).stream()
			.filter(iRecipe -> iRecipe.getIngredients().size() == 1 && iRecipe.getIngredients().get(0).test(input))
			.map(Recipe::getRecipeOutput)
			.findFirst()
			.orElse(ItemStack.EMPTY);
	}

	private static boolean isRecipeValid(Recipe recipe) {
		if (recipe.getId() == null) {
			return false;
		}
		if (!recipe.getId().getNamespace().equals(TechReborn.MOD_ID)) {
			return false;
		}
		if (!recipe.getOutput().getItem().getRegistryName().getNamespace().equals(TechReborn.MOD_ID)) {
			return false;
		}
//		if (hiddenEntrys.contains(recipe.getRecipeOutput().getItem())) {
//			return false;
//		}
		//Hide uu recipes
		for (Ingredient ingredient : recipe.getPreviewInputs()) {
			if (ingredient.method_8093(TRContent.Parts.UU_MATTER.getStack())) {
				return false;
			}
		}
		return true;
	}

}
