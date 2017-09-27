/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.compat.crafttweaker;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import reborncore.common.util.ItemUtils;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.RollingMachineRecipe;
import techreborn.api.TechRebornAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ZenClass("mods.techreborn.rollingMachine")
public class CTRollingMachine {

	@ZenMethod
	public static void addShaped(IItemStack output, IIngredient[][] ingredients) {
		TechRebornAPI.addRollingOreMachinceRecipe(RollingMachineRecipe.getNameForRecipe(CraftTweakerCompat.toStack(output)), CraftTweakerCompat.toStack(output), toShapedObjects(ingredients));
	}

	@ZenMethod
	public static void addShapeless(IItemStack output, IIngredient[] ingredients) {
		TechRebornAPI.addShapelessOreRollingMachinceRecipe(RollingMachineRecipe.getNameForRecipe(CraftTweakerCompat.toStack(output)), CraftTweakerCompat.toStack(output), toObjects(ingredients));
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		List<ResourceLocation> toRemove = new ArrayList<>();
		for (Map.Entry<ResourceLocation, IRecipe> recipe : RollingMachineRecipe.instance.getRecipeList().entrySet()) {
			if (ItemUtils.isItemEqual(recipe.getValue().getRecipeOutput(), CraftTweakerCompat.toStack(output), true, false)) {
				toRemove.add(recipe.getKey());
			}
		}
		for (ResourceLocation resourceLocation : toRemove) {
			RollingMachineRecipe.instance.getRecipeList().remove(resourceLocation);
		}
	}

	public static Object[] toShapedObjects(IIngredient[][] ingredients) {
		if (ingredients == null)
			return null;
		else {
			ArrayList<Object> prep = new ArrayList<Object>();
			prep.add("abc");
			prep.add("def");
			prep.add("ghi");
			char[][] map = new char[][] { { 'a', 'b', 'c' }, { 'd', 'e', 'f' }, { 'g', 'h', 'i' } };
			for (int x = 0; x < ingredients.length; x++) {
				if (ingredients[x] != null) {
					for (int y = 0; y < ingredients[x].length; y++) {
						if (ingredients[x][y] != null && x < map.length && y < map[x].length) {
							prep.add(map[x][y]);
							prep.add(CraftTweakerCompat.toObject(ingredients[x][y]));
						}
					}
				}
			}
			return prep.toArray();
		}
	}

	public static Object[] toObjects(IIngredient[] ingredient) {
		if (ingredient == null)
			return null;
		else {
			Object[] output = new Object[ingredient.length];
			for (int i = 0; i < ingredient.length; i++) {
				if (ingredient[i] != null) {
					output[i] = CraftTweakerCompat.toObject(ingredient[i]);
				} else
					output[i] = "";
			}

			return output;
		}
	}
}
