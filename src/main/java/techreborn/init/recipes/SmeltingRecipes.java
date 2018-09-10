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

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import reborncore.common.util.RebornCraftingHelper;
import techreborn.blocks.BlockOre;
import techreborn.init.TRIngredients;

/**
 * Created by Prospector
 */
public class SmeltingRecipes extends RecipeMethods {
	public static void init() {

		register(getStack(Items.IRON_INGOT), TRIngredients.Ingots.REFINED_IRON.getStack());
		register(TRIngredients.Parts.SAP.getStack(), TRIngredients.Parts.RUBBER.getStack());
		register(TRIngredients.Ingots.MIXED_METAL.getStack(), TRIngredients.Ingots.ADVANCED_ALLOY.getStack());
//		register(BlockOre.getOreByName("silver"), TRIngredients.Ingots.SILVER.getStack());
//		register(BlockOre2.getOreByName("tin"), TRIngredients.Ingots.TIN.getStack());
				
//		TODO: Fix recipe		
//		register(BlockOre2.getOreByName("copper"), getMaterial("copper", Type.INGOT));
//		register(BlockOre.getOreByName("lead"), getMaterial("lead", Type.INGOT));
//		register(BlockOre.getOreByName("sheldonite"), getMaterial("platinum", Type.INGOT));
		
		// Dust smelting
//		register(getMaterial("iron", Type.DUST), getStack(Items.IRON_INGOT));
//		register(getMaterial("gold", Type.DUST), getStack(Items.GOLD_INGOT));
//		register(getMaterial("copper", Type.DUST), getMaterial("copper", Type.INGOT));
//		register(getMaterial("tin", Type.DUST), getMaterial("tin", Type.INGOT));
//		register(getMaterial("bronze", Type.DUST), getMaterial("bronze", Type.INGOT));
//		register(getMaterial("lead", Type.DUST), getMaterial("lead", Type.INGOT));
//		register(getMaterial("silver", Type.DUST), getMaterial("silver", Type.INGOT));
//		register(getMaterial("nickel", Type.DUST), getMaterial("nickel", Type.INGOT));
//		register(getMaterial("platinum", Type.DUST), getMaterial("platinum", Type.INGOT));
//		register(getMaterial("zinc", Type.DUST), getMaterial("zinc", Type.INGOT));
//		register(getMaterial("brass", Type.DUST), getMaterial("brass", Type.INGOT));
//		register(getMaterial("electrum", Type.DUST), getMaterial("electrum", Type.INGOT));
//		register(getMaterial("invar", Type.DUST), getMaterial("invar", Type.INGOT));
		
		
		
	}

	static void register(ItemStack input, ItemStack output) {
		RebornCraftingHelper.addSmelting(input, output);
	}
}
