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

package techreborn.api;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;

public class RollingMachineRecipe {

	public static final RollingMachineRecipe instance = new RollingMachineRecipe();
	private static final HashMap<ResourceLocation, IRecipe> recipes = new HashMap<>();

	public void addShapedOreRecipe(ResourceLocation resourceLocation, ItemStack outputItemStack, Object... objectInputs) {
				Validate.notNull(outputItemStack);
				Validate.notNull(outputItemStack.getItem());
				if (objectInputs.length == 0) {
					Validate.notNull(null); //Quick way to crash
				}
				recipes.put(resourceLocation, new ShapedOreRecipe(resourceLocation, outputItemStack, objectInputs));
	}

	public void addShapelessOreRecipe(ResourceLocation resourceLocation, ItemStack outputItemStack, Object... objectInputs) {
				Validate.notNull(outputItemStack);
				Validate.notNull(outputItemStack.getItem());
				if (objectInputs.length == 0) {
					Validate.notNull(null); //Quick way to crash
				}
				recipes.put(resourceLocation, new ShapelessOreRecipe(resourceLocation, outputItemStack, objectInputs));
	}

	public static ResourceLocation getNameForRecipe(ItemStack output) {
		ModContainer activeContainer = Loader.instance().activeModContainer();
		ResourceLocation baseLoc = new ResourceLocation(activeContainer.getModId(), output.getItem().getRegistryName().getPath());
		ResourceLocation recipeLoc = baseLoc;
		int index = 0;
		while (recipes.containsKey(recipeLoc)) {
			index++;
			recipeLoc = new ResourceLocation(activeContainer.getModId(), baseLoc.getPath() + "_" + index);
		}
		return recipeLoc;
	}

	public void addRecipe(ResourceLocation resourceLocation, ItemStack output, Object... components) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		if (components[i] instanceof String[]) {
			String as[] = (String[]) components[i++];
			for (int l = 0; l < as.length; l++) {
				String s2 = as[l];
				k++;
				j = s2.length();
				s = (new StringBuilder()).append(s).append(s2).toString();
			}
		} else {
			while (components[i] instanceof String) {
				String s1 = (String) components[i++];
				k++;
				j = s1.length();
				s = (new StringBuilder()).append(s).append(s1).toString();
			}
		}
		HashMap<Character, ItemStack> hashmap = new HashMap<Character, ItemStack>();
		for (; i < components.length; i += 2) {
			Character character = (Character) components[i];
			ItemStack itemstack1 = null;
			if (components[i + 1] instanceof Item) {
				itemstack1 = new ItemStack((Item) components[i + 1]);
			} else if (components[i + 1] instanceof Block) {
				itemstack1 = new ItemStack((Block) components[i + 1], 1, -1);
			} else if (components[i + 1] instanceof ItemStack) {
				itemstack1 = (ItemStack) components[i + 1];
			}
			hashmap.put(character, itemstack1);
		}

		NonNullList<Ingredient> recipeArray = NonNullList.create();
		for (int i1 = 0; i1 < j * k; i1++) {
			char c = s.charAt(i1);
			if (hashmap.containsKey(c)) {
				recipeArray.set(i1, CraftingHelper.getIngredient(((ItemStack) hashmap.get(c)).copy()));
			} else {
				recipeArray.set(i1, CraftingHelper.getIngredient(ItemStack.EMPTY));
			}
		}

		recipes.put(resourceLocation, new ShapedRecipes("", j, k, recipeArray, output));
	}

	public void addShapelessRecipe(ResourceLocation resourceLocation, ItemStack output, Object... components) {
		NonNullList<Ingredient> ingredients = NonNullList.create();
		for (int j = 0; j < components.length; j++) {
			ingredients.add(CraftingHelper.getIngredient(components[j]));
		}
		recipes.put(resourceLocation, new ShapelessRecipes("", output, ingredients));
	}

	public ItemStack findMatchingRecipeOutput(InventoryCrafting inv, World world) {
		for (IRecipe irecipe : recipes.values()) {
			if (irecipe.matches(inv, world)) {
				return irecipe.getCraftingResult(inv);
			}
		}
		return ItemStack.EMPTY;
	}

	public IRecipe findMatchingRecipe(InventoryCrafting inv, World world) {
		for (IRecipe irecipe : recipes.values()) {
			if (irecipe.matches(inv, world)) {
				return irecipe;
			}
		}
		return null;
	}


	public HashMap<ResourceLocation, IRecipe> getRecipeList() {
		return recipes;
	}

}
