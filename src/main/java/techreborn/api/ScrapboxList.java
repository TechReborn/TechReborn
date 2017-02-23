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

package techreborn.api;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Use this to add items to the scrap box and to check if an item is present
 * there
 */
public class ScrapboxList {

	/**
	 * This full list of items that is registered with this api
	 */
	public static List<ItemStack> stacks = new ArrayList<>();

	/**
	 * Use this to add an item stack to the list
	 *
	 * @param stack the itemstack you want to add
	 */
	public static void addItemStackToList(ItemStack stack) {
		if (!hasItems(stack)) {
			stacks.add(stack);
		}
	}

	/**
	 * @param stack the itemstack you want to test
	 * @return if the scrapbox can output this this item
	 */
	private static boolean hasItems(ItemStack stack) {
		for (ItemStack s : stacks) {
			// TODO why do this!!!!!!!!!
			if (stack.getDisplayName().equals(s.getDisplayName()))
				return true;
		}
		return false;
	}
}
