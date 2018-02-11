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

package techreborn.client.container.builder.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class FilteredSlot extends Slot {

	private Predicate<ItemStack> filter;
	private int stackLimit = 64;

	public FilteredSlot(final IInventory inventory, final int index, final int xPosition, final int yPosition) {
		super(inventory, index, xPosition, yPosition);
	}

	public FilteredSlot(final IInventory inventory, final int index, final int xPosition, final int yPosition, int stackLimit) {
		super(inventory, index, xPosition, yPosition);
		this.stackLimit = stackLimit;
	}

	public FilteredSlot setFilter(final Predicate<ItemStack> filter) {
		this.filter = filter;
		return this;
	}

	@Override
	public boolean isItemValid(final ItemStack stack) {
		try {
			return this.filter.test(stack);
		} catch (NullPointerException e) {
			return true;
		}
	}

	@Override
	public int getSlotStackLimit() {
		return stackLimit;
	}
}
