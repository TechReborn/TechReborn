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

package techreborn.init.recipes;

import net.minecraft.item.ItemStack;

/**
 * @author drcrazy
 *
 */
public class BlastFurnaceRecipes extends RecipeMethods {

	public static void init() {






		
		

//		register(new ItemStack(Blocks.IRON_ORE), ItemDusts.getDustByName("calcite"), new ItemStack(Items.IRON_INGOT, 3),
//				ItemDusts.getDustByName("dark_ashes"), 140, 1000);
//		register(BlockOre.getOreByName("Pyrite"), ItemDusts.getDustByName("calcite"),
//				new ItemStack(Items.IRON_INGOT, 2), ItemDusts.getDustByName("dark_ashes"), 140, 1000);
		
//		register(new ItemStack(Items.DIAMOND_HELMET), new ItemStack(Blocks.SAND), new ItemStack(Items.DIAMOND, 5),
//				ItemDusts.getDustByName("dark_ashes"), 140, 1000);
//		register(new ItemStack(Items.DIAMOND_CHESTPLATE), new ItemStack(Blocks.SAND), new ItemStack(Items.DIAMOND, 8),
//				ItemDusts.getDustByName("dark_ashes"), 140, 1000);
//		register(new ItemStack(Items.DIAMOND_LEGGINGS), new ItemStack(Blocks.SAND), new ItemStack(Items.DIAMOND, 7),
//				ItemDusts.getDustByName("dark_ashes"), 140, 1000);
//		register(new ItemStack(Items.DIAMOND_BOOTS), new ItemStack(Blocks.SAND), new ItemStack(Items.DIAMOND, 4),
//				ItemDusts.getDustByName("dark_ashes"), 140, 1000);
//		register(new ItemStack(Items.GOLDEN_HELMET), new ItemStack(Blocks.SAND), new ItemStack(Items.GOLD_INGOT, 5),
//				ItemDusts.getDustByName("dark_ashes"), 140, 1000);
//		register(new ItemStack(Items.GOLDEN_CHESTPLATE), new ItemStack(Blocks.SAND), new ItemStack(Items.GOLD_INGOT, 8),
//				ItemDusts.getDustByName("dark_ashes"), 140, 1000);
//		register(new ItemStack(Items.GOLDEN_LEGGINGS), new ItemStack(Blocks.SAND), new ItemStack(Items.GOLD_INGOT, 7),
//				ItemDusts.getDustByName("dark_ashes"), 140, 1000);
//		register(new ItemStack(Items.GOLDEN_BOOTS), new ItemStack(Blocks.SAND), new ItemStack(Items.GOLD_INGOT, 4),
//				ItemDusts.getDustByName("dark_ashes"), 140, 1000);
//		register(new ItemStack(Items.IRON_HELMET), new ItemStack(Blocks.SAND), new ItemStack(Items.IRON_INGOT, 5),
//				ItemDusts.getDustByName("dark_ashes"), 140, 1000);
//		register(new ItemStack(Items.IRON_CHESTPLATE), new ItemStack(Blocks.SAND), new ItemStack(Items.IRON_INGOT, 8),
//				ItemDusts.getDustByName("dark_ashes"), 140, 1000);
//		register(new ItemStack(Items.IRON_LEGGINGS), new ItemStack(Blocks.SAND), new ItemStack(Items.IRON_INGOT, 7),
//				ItemDusts.getDustByName("dark_ashes"), 140, 1000);
//		register(new ItemStack(Items.IRON_BOOTS), new ItemStack(Blocks.SAND), new ItemStack(Items.IRON_INGOT, 4),
//				ItemDusts.getDustByName("dark_ashes"), 140, 1000);
	}

	private static void register(ItemStack in1, ItemStack in2, ItemStack out1, ItemStack out2, int tickTime,
			int euPerTick, int neededHeat) {
	//	RecipeHandler.addRecipe(new BlastFurnaceRecipe(in1, in2, out1, out2, tickTime, euPerTick, neededHeat));
	}

	private static void register(ItemStack in1, ItemStack in2, ItemStack out1, ItemStack out2, int tickTime,
			int neededHeat) {
		register(in1, in2, out1, out2, tickTime, 120, neededHeat);
	}

}