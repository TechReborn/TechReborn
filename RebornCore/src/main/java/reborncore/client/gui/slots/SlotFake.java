/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.client.gui.slots;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class SlotFake extends BaseSlot {

	public boolean mCanInsertItem;
	public boolean mCanStackItem;
	public int mMaxStacksize = 127;

	public SlotFake(Inventory itemHandler, int par2, int par3, int par4, boolean aCanInsertItem,
					boolean aCanStackItem, int aMaxStacksize) {
		super(itemHandler, par2, par3, par4);
		this.mCanInsertItem = aCanInsertItem;
		this.mCanStackItem = aCanStackItem;
		this.mMaxStacksize = aMaxStacksize;
	}

	@Override
	public boolean canInsert(ItemStack par1ItemStack) {
		return this.mCanInsertItem;
	}

	@Override
	public int getMaxItemCount() {
		return this.mMaxStacksize;
	}

	@Override
	public boolean hasStack() {
		return false;
	}

	@Override
	public ItemStack takeStack(int par1) {
		return !this.mCanStackItem ? ItemStack.EMPTY : super.takeStack(par1);
	}

	@Override
	public boolean canWorldBlockRemove() {
		return false;
	}
}
