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

package techreborn.compat.minetweaker;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import techreborn.api.Reference;
import techreborn.api.recipe.machines.AlloySmelterRecipe;

/**
 * mods.techreborn.alloySmelter.addRecipe(<minecraft:gold_ingot>, <minecraft:iron_ingot>, <minecraft:diamond>, 20, 100);
 */

@ZenClass("mods.techreborn.alloySmelter")
public class MTAlloySmelter extends MTGeneric {

	@ZenMethod
	public static void addRecipe(IItemStack output, IIngredient input1, IIngredient input2, int ticktime, int euTick) {
		ItemStack oInput1 = (ItemStack) MinetweakerCompat.toObject(input1);
		ItemStack oInput2 = (ItemStack) MinetweakerCompat.toObject(input2);

		AlloySmelterRecipe r = new AlloySmelterRecipe(oInput1, oInput2, MinetweakerCompat.toStack(output), ticktime, euTick);

		addRecipe(r);
	}

	@ZenMethod
	public static void removeInputRecipe(IIngredient iIngredient) {
		MineTweakerAPI.apply(new RemoveInput(iIngredient, getMachineName()));
	}

	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		MineTweakerAPI.apply(new Remove(MinetweakerCompat.toStack(output), getMachineName()));
	}

	public static String getMachineName() {
		return Reference.alloySmelteRecipe;
	}
}
