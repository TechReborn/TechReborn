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

package reborncore.api.items;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public abstract class InventoryBase implements Container {

	private final int size;
	private NonNullList<ItemStack> stacks;

	public InventoryBase(int size) {
		this.size = size;
		stacks = NonNullList.withSize(size, ItemStack.EMPTY);
	}

	public void writeData(ValueOutput view) {
		ContainerHelper.saveAllItems(view, stacks);
	}

	public void readData(ValueInput view) {
		stacks = NonNullList.withSize(size, ItemStack.EMPTY);
		ContainerHelper.loadAllItems(view, stacks);
	}

	@Override
	public int getContainerSize() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return stacks.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getItem(int i) {
		return stacks.get(i);
	}

	@Override
	public ItemStack removeItem(int i, int i1) {
		ItemStack stack = ContainerHelper.removeItem(stacks, i, i1);
		if (!stack.isEmpty()) {
			this.setChanged();
		}
		return stack;
	}

	@Override
	public ItemStack removeItemNoUpdate(int i) {
		return ContainerHelper.takeItem(stacks, i);
	}

	@Override
	public void setItem(int i, ItemStack itemStack) {
		stacks.set(i, itemStack);
		if (itemStack.getCount() > this.getMaxStackSize()) {
			itemStack.setCount(this.getMaxStackSize());
		}

		this.setChanged();
	}

	@Override
	public void setChanged() {
		// Stuff happens in the super methods
	}

	@Override
	public boolean stillValid(Player playerEntity) {
		return true;
	}

	@Override
	public void clearContent() {
		stacks.clear();
	}

	public NonNullList<ItemStack> getStacks() {
		return stacks;
	}
}
