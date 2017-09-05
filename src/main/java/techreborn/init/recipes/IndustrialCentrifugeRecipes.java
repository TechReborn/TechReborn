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

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import reborncore.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.CentrifugeRecipe;
import techreborn.init.ModBlocks;
import techreborn.items.DynamicCell;

import java.security.InvalidParameterException;

/**
 * Created by Prospector
 */
public class IndustrialCentrifugeRecipes extends RecipeMethods {
	public static void init() {
		register(getStack(Items.MAGMA_CREAM), 500, getStack(Items.BLAZE_POWDER), getStack(Items.SLIME_BALL));
		register(getStack(Blocks.DIRT, 16), 2500, getStack(Blocks.SAND, 8), getStack(Items.CLAY_BALL), getStack(Blocks.GRAVEL, 2));
		register(getStack(Blocks.DIRT, 16), 2500, getStack(Blocks.SAND, 8), getStack(Items.CLAY_BALL), getStack(Blocks.GRAVEL, 2), getStack(Items.WHEAT_SEEDS, 4));
		register(getStack(Blocks.MYCELIUM, 8), 1640, getStack(Blocks.SAND, 4), getStack(Items.CLAY_BALL), getStack(Blocks.BROWN_MUSHROOM, 2), getStack(Blocks.RED_MUSHROOM, 2));
		register(getStack(Items.GOLDEN_APPLE), 10000, getStack(Items.GOLD_INGOT, 6), getMaterial("methane", Type.CELL));
		register(getStack(Items.GOLDEN_APPLE, 1, 1), 10000, getStack(Items.GOLD_INGOT, 64), getMaterial("methane", Type.CELL));
		register(getStack(Items.GOLDEN_CARROT), 10000, getStack(Items.GOLD_NUGGET, 6), getMaterial("methane", Type.CELL));
		register(getStack(Items.SPECKLED_MELON, 8), 10000, getStack(Items.GOLD_NUGGET, 6), getMaterial("methane", Type.CELL));
		register(getStack(Items.APPLE, 32), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.MUSHROOM_STEW, 16), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.BREAD, 64), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.PORKCHOP, 12), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.COOKED_PORKCHOP, 16), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.BEEF, 12), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.COOKED_BEEF, 16), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.CHICKEN, 12), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.COOKED_CHICKEN, 16), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.MUTTON, 12), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.COOKED_MUTTON, 16), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.RABBIT, 12), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.COOKED_RABBIT, 16), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.FISH, 12), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.COOKED_FISH, 16), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.MELON, 64), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Blocks.PUMPKIN, 16), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.ROTTEN_FLESH, 16), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.SPIDER_EYE, 32), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.CARROT, 16), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.POTATO, 16), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.POISONOUS_POTATO, 12), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.BAKED_POTATO, 24), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.BEETROOT, 12), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.COOKIE, 64), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Blocks.BROWN_MUSHROOM_BLOCK, 12), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Blocks.RED_MUSHROOM_BLOCK, 12), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Blocks.BROWN_MUSHROOM, 32), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Blocks.RED_MUSHROOM, 32), 5000, getMaterial("methane", Type.CELL));
		register(getStack(Items.NETHER_WART, 32), 5000, getMaterial("methane", Type.CELL));
		register(getMaterial("sap", 4, Type.PART), 1300, getMaterial("rubber", 14, Type.PART));
		register(getStack(Blocks.SOUL_SAND, 16), 2500, getStack(Blocks.SAND, 10), getMaterial("saltpeter", 4, Type.DUST), getMaterial("coal", Type.DUST), getMaterial("oil", Type.CELL));
		register(getMaterial("lava", 16, Type.CELL), 15000, getMaterial("tin", 18, Type.INGOT), getMaterial("copper", 4, Type.INGOT), getMaterial("electrum", Type.INGOT), getMaterial("tungsten", Type.SMALL_DUST));
		register(getMaterial("bronze", Type.DUST), 1500, getMaterial("copper", 6, Type.SMALL_DUST), getMaterial("tin", 2, Type.SMALL_DUST));
		register(getMaterial("iron", 2, Type.DUST), 1500, getMaterial("tin", Type.SMALL_DUST), getMaterial("nickel", Type.SMALL_DUST));
		register(getMaterial("silver", 2, Type.DUST), 2400, getMaterial("lead", Type.SMALL_DUST));
		register(getMaterial("lead", 2, Type.DUST), 2400, getMaterial("silver", Type.SMALL_DUST));
		register(getMaterial("tin", 2, Type.DUST), 210, getMaterial("zinc", Type.SMALL_DUST), getMaterial("iron", Type.SMALL_DUST));
		register(getMaterial("electrum", Type.DUST), 960, getMaterial("gold", 2, Type.SMALL_DUST), getMaterial("silver", 2, Type.SMALL_DUST));
		register(getMaterial("zinc", Type.DUST), 1040, getMaterial("tin", Type.SMALL_DUST));
		register(getMaterial("brass", Type.DUST), 1500, getMaterial("copper", 3, Type.SMALL_DUST), getMaterial("zinc", Type.SMALL_DUST));
		register(getMaterial("platinum", 2, Type.DUST), 3000, getMaterial("iridium", 2, Type.NUGGET), getMaterial("nickel", Type.SMALL_DUST));
		register(getMaterial("nickel", 3, Type.DUST), 3440, getMaterial("iron", Type.SMALL_DUST), getMaterial("gold", Type.SMALL_DUST), getMaterial("copper", Type.SMALL_DUST));
		register(getMaterial("gold", 3, Type.DUST), 2400, getMaterial("copper", Type.SMALL_DUST), getMaterial("nickel", Type.SMALL_DUST));
		register(getMaterial("copper", 3, Type.DUST), 2400, getMaterial("gold", Type.SMALL_DUST), getMaterial("nickel", Type.SMALL_DUST));
		register(getStack(ModBlocks.RUBBER_LOG, 16), 5000, false, getMaterial("sap", 8, Type.PART), getMaterial("methane", Type.CELL), getMaterial("carbon", 4, Type.CELL));
		register(getStack(Items.REDSTONE, 32), 22000, getMaterial("silicon", 3, Type.CELL), getMaterial("pyrite", 16, Type.DUST), getMaterial("ruby", 3, Type.DUST), getMaterial("mercury", 10, Type.CELL));
		register(getStack(Items.GLOWSTONE_DUST, 16), 25000, getStack(Items.REDSTONE, 8), getMaterial("sulfur", Type.CELL), getMaterial("helium", Type.CELL));
		register(getStack(Items.DYE, 4, 4), 1500, false, getMaterial("lazurite", 3, Type.DUST), getMaterial("pyrite", Type.SMALL_DUST), getMaterial("calcite", Type.SMALL_DUST), getMaterial("sodalite", 2, Type.SMALL_DUST));
		register(getMaterial("ender_eye", 2, Type.DUST), 1840, getMaterial("ender_pearl", Type.DUST), getStack(Items.BLAZE_POWDER));
		register(getMaterial("netherrack", 16, Type.DUST), 2400, getStack(Items.REDSTONE), getMaterial("sulfur", Type.CELL), getMaterial("coal", Type.DUST), getStack(Items.GOLD_NUGGET));
		register(getMaterial("endstone", 16, Type.DUST), 4800, getMaterial("helium3", Type.CELL), getMaterial("helium", Type.CELL), getMaterial("tungsten", Type.SMALL_DUST), getStack(Blocks.SAND, 12));
		register(getMaterial("red_garnet", 16, Type.DUST), 3000, getMaterial("pyrope", 3, Type.DUST), getMaterial("almandine", 5, Type.DUST), getMaterial("spessartine", 8, Type.DUST));
		register(getMaterial("yellow_garnet", 16, Type.DUST), 3500, getMaterial("andradite", 5, Type.DUST), getMaterial("grossular", 8, Type.DUST), getMaterial("uvarovite", 3, Type.DUST));
		register(getMaterial("dark_ashes", 2, Type.DUST), 240, getMaterial("ashes", 2, Type.DUST));
		register("dustMarble", 1040, getMaterial("magnesium", Type.DUST), getMaterial("calcite", 7, Type.DUST));
		register(getMaterial("basalt", 16, Type.DUST), 2040, getMaterial("peridot", Type.DUST), getMaterial("calcite", 3, Type.DUST), getMaterial("flint", 8, Type.DUST), getMaterial("dark_ashes", 4, Type.DUST));
		register(getMaterial("hydrogen", 4, Type.CELL), 3000, getMaterial("deuterium", Type.CELL));
		register(getMaterial("deuterium", 4, Type.CELL), 3000, getMaterial("tritium", Type.CELL));
		register(getMaterial("helium", 16, Type.CELL), 10000, getMaterial("helium3", Type.CELL));
		register(getMaterial("calciumcarbonate", Type.CELL), 40, getMaterial("calcite", Type.DUST));
		register(getMaterial("sulfur", Type.CELL), 40, getMaterial("sulfur", Type.DUST));
		//TO-DO Implement Distillation tower
		register(getMaterial("oil", 4, Type.CELL), 800, getMaterial("diesel", Type.CELL));
	}

	static void register(Object input, int ticks, boolean oreDict, ItemStack... outputs) {
		ItemStack output1;
		ItemStack output2 = null;
		ItemStack output3 = null;
		ItemStack output4 = null;

		if (outputs.length == 3) {
			output1 = outputs[0];
			output2 = outputs[1];
			output3 = outputs[2];
		} else if (outputs.length == 2) {
			output1 = outputs[0];
			output2 = outputs[1];
		} else if (outputs.length == 1) {
			output1 = outputs[0];
		} else if (outputs.length == 4) {
			output1 = outputs[0];
			output2 = outputs[1];
			output3 = outputs[2];
			output4 = outputs[3];
		} else {
			throw new InvalidParameterException("Invalid industrial centrifuge outputs: " + outputs);
		}

		int cellCount = 0;
		for (ItemStack stack : outputs) {
			if (stack.getItem() instanceof DynamicCell) {
				cellCount += stack.getCount();
			}

		}

		if (input instanceof ItemStack) {
			if (((ItemStack) input).getItem() instanceof DynamicCell) {
				int inputCount = ((ItemStack) input).getCount();
				if (cellCount < inputCount) {
					if (output2 == null) {
						output2 = DynamicCell.getEmptyCell(inputCount - cellCount);
					} else if (output3 == null) {
						output3 = DynamicCell.getEmptyCell(inputCount - cellCount);
					} else if (output4 == null) {
						output4 = DynamicCell.getEmptyCell(inputCount - cellCount);
					}
				}
				cellCount -= inputCount;

			}
		}

		if (cellCount < 0) {
			cellCount = 0;
		}
		ItemStack cells = null;
		if (cellCount > 0) {
			if (cellCount > 64) {
				throw new InvalidParameterException("Invalid industrial centrifuge outputs: " + outputs + "(Recipe requires > 64 cells)");
			}
			cells = DynamicCell.getEmptyCell(cellCount);
		}
		RecipeHandler.addRecipe(new CentrifugeRecipe(input, cells, output1, output2, output3, output4, ticks, 5, oreDict));
	}

	static void register(Object input, int ticks, ItemStack... outputs) {
		register(input, ticks, true, outputs);
	}
}
