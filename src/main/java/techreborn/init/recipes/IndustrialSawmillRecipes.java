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

package techreborn.init.recipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.api.recipe.RecipeHandler;
import reborncore.common.util.ItemUtils;
import techreborn.api.recipe.machines.IndustrialSawmillRecipe;
import techreborn.items.ItemDusts;

import javax.annotation.Nonnull;
import java.security.InvalidParameterException;

public class IndustrialSawmillRecipes extends RecipeMethods {
	static FluidStack WATER = new FluidStack(FluidRegistry.WATER, 1000);

	public static void init() {
		InventoryCrafting inventoryCrafting = new InventoryCrafting(new Container() {
			@Override
			public boolean canInteractWith(EntityPlayer playerIn) {
				return false;
			}
		}, 3, 3);

		for (int i = 0; i < 9; i++) {
			inventoryCrafting.setInventorySlotContents(i, ItemStack.EMPTY);
		}

		NonNullList<ItemStack> logs = OreDictionary.getOres("logWood");
		for (ItemStack logStack : logs) {
			if (logStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
				for (int i = 0; i < 16; i++) {
					ItemStack newStack = logStack.copy();
					newStack.setItemDamage(i);
					inventoryCrafting.setInventorySlotContents(0, newStack);
					ItemStack output = findMatchingRecipe(inventoryCrafting);
					if (!output.isEmpty()) {
						if (ItemUtils.isInputEqual("plankWood", output, false, false, false)) {
							addRecipe(newStack.copy(), output.copy());
						}
					}
				}
			} else {
				inventoryCrafting.setInventorySlotContents(0, logStack.copy());
				ItemStack output = findMatchingRecipe(inventoryCrafting);
				if (!output.isEmpty()) {
					if (ItemUtils.isInputEqual("plankWood", output, false, false, false)) {
						addRecipe(logStack.copy(), output.copy());
					}
				}
			}
		}

	}

	@Nonnull
	public static ItemStack findMatchingRecipe(InventoryCrafting inv) {
		for (IRecipe recipe : CraftingManager.field_193380_a) {
			if (recipe.matches(inv, null)) {
				return recipe.getCraftingResult(inv);
			}
		}
		return ItemStack.EMPTY;

	}

	public static void addRecipe(ItemStack log, ItemStack plank) {
		register(log, getMaterial("water", Type.CELL), WATER, 100, 128, plank, ItemDusts.getDustByName("sawDust", 3), getStack(Items.PAPER, 1));
		register(log, new ItemStack(Items.WATER_BUCKET), WATER, 100, 128, plank, ItemDusts.getDustByName("sawDust", 3), getStack(Items.PAPER, 1));
	}

	static void register(ItemStack input1, ItemStack input2, FluidStack fluid, int ticks, int euPerTick, ItemStack... outputs) {
		if (outputs.length == 3) {
			RecipeHandler.addRecipe(new IndustrialSawmillRecipe(input1, input2,
				fluid, outputs[0], outputs[1], outputs[2], ticks, euPerTick, false));
		} else if (outputs.length == 2) {
			RecipeHandler.addRecipe(new IndustrialSawmillRecipe(input1, input2,
				fluid, outputs[0], outputs[1], null, ticks, euPerTick, false));
		} else if (outputs.length == 1) {
			RecipeHandler.addRecipe(new IndustrialSawmillRecipe(input1, input2,
				fluid, outputs[0], null, null, ticks, euPerTick, false));
		} else {
			throw new InvalidParameterException("Invalid industrial sawmill outputs: " + outputs);
		}
	}

}
