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

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;
import reborncore.common.util.ItemUtils;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import techreborn.init.TRItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		if (entityItemPickupEvent.getEntityPlayer() instanceof EntityPlayerMP) {
			if (ItemUtils.isInputEqual("logWood", entityItemPickupEvent.getItem().getItem(), false, false, true)) {
				for (IRecipe recipe : CraftingManager.REGISTRY) {
					if (recipe.getRecipeOutput().getItem() == TRItems.TREE_TAP) {
						entityItemPickupEvent.getEntityPlayer().unlockRecipes(Collections.singletonList(recipe));
					}
				}
			}
		}
	}

	public static void unlockTRRecipes(EntityPlayerMP playerMP) {
		List<IRecipe> recipeList = new ArrayList<>();
		for (IRecipe recipe : CraftingManager.REGISTRY) {
			if (isRecipeValid(recipe)) {
				recipeList.add(recipe);
			}
		}
		playerMP.unlockRecipes(recipeList);
	}

	private static boolean isRecipeValid(IRecipe recipe) {
		if (recipe.getRegistryName() == null) {
			return false;
		}
		if (!recipe.getRegistryName().getNamespace().equals(TechReborn.MOD_ID)) {
			return false;
		}
		if (!recipe.getRecipeOutput().getItem().getRegistryName().getNamespace().equals(TechReborn.MOD_ID)) {
			return false;
		}
//		if (hiddenEntrys.contains(recipe.getRecipeOutput().getItem())) {
//			return false;
//		}
		//Hide uu recipes
		for (Ingredient ingredient : recipe.getIngredients()) {
			if (ingredient.apply(TRContent.Parts.UU_MATTER.getStack())) {
				return false;
			}
		}
		return true;
	}

}
