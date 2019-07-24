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
import techreborn.items.ItemDynamicCell;
import techreborn.items.ItemCells;

import java.security.InvalidParameterException;

/**
 * @author drcrazy
 *
 */
public class DistillationTowerRecipes extends RecipeMethods {

	public static void init() {
		register(ItemCells.getCellByName("oil", 16), 1400, 13, getMaterial("diesel", 16, Type.CELL), getMaterial("sulfuricAcid", 16, Type.CELL), getMaterial("glyceryl", Type.CELL));
	}
	
	static void register(ItemStack input, int ticks, int euPerTick, boolean oreDict, ItemStack... outputs) {
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
			throw new InvalidParameterException("Invalid number of Distillation tower outputs: " + outputs);
		}

		int cellCount = 0;
		for (ItemStack stack : outputs) {
			if (stack.getItem() instanceof ItemDynamicCell) {
				cellCount += stack.getCount();
			}
		}

		if (input.getItem() instanceof ItemDynamicCell) {
			int inputCount = input.getCount();
			if (cellCount < inputCount) {
				if (output2 == null) {
					output2 = ItemDynamicCell.getEmptyCell(inputCount - cellCount);
				} else if (output3 == null) {
					output3 = ItemDynamicCell.getEmptyCell(inputCount - cellCount);
				} else if (output4 == null) {
					output4 = ItemDynamicCell.getEmptyCell(inputCount - cellCount);
				}
			}
			cellCount -= inputCount;
		}

		if (cellCount < 0) { cellCount = 0; }
		
		ItemStack cells = null;
		if (cellCount > 0) {
			if (cellCount > 64) {
				throw new InvalidParameterException("Invalid Distillation tower outputs: " + outputs + "(Recipe requires > 64 cells)");
			}
			cells = ItemDynamicCell.getEmptyCell(cellCount);
		}
	//	RecipeHandler.addRecipe(Reference.DistiLLATION_TOWER_RECIPE, new DistillationTowerRecipe(input, cells, output1, output2, output3, output4, ticks, euPerTick, oreDict));
	}

	static void register(ItemStack input, int ticks, int euPerTick, ItemStack... outputs) {
		register(input, ticks, euPerTick, true, outputs);
	}

}
