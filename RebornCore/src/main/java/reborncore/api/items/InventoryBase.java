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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.collection.DefaultedList;

public abstract class InventoryBase implements Inventory {

	private final int size;
	private DefaultedList<ItemStack> stacks;

	public InventoryBase(int size) {
		this.size = size;
		stacks = DefaultedList.ofSize(size, ItemStack.EMPTY);
	}

	public NbtElement serializeNBT() {
		NbtCompound tag = new NbtCompound();
		Inventories.writeNbt(tag, stacks);
		return tag;
	}

	public void deserializeNBT(NbtCompound tag) {
		stacks = DefaultedList.ofSize(size, ItemStack.EMPTY);
		Inventories.readNbt(tag, stacks);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return stacks.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getStack(int i) {
		return stacks.get(i);
	}

	@Override
	public ItemStack removeStack(int i, int i1) {
		ItemStack stack = Inventories.splitStack(stacks, i, i1);
		if (!stack.isEmpty()) {
			this.markDirty();
		}
		return stack;
	}

	@Override
	public ItemStack removeStack(int i) {
		return Inventories.removeStack(stacks, i);
	}

	@Override
	public void setStack(int i, ItemStack itemStack) {
		stacks.set(i, itemStack);
		if (itemStack.getCount() > this.getMaxCountPerStack()) {
			itemStack.setCount(this.getMaxCountPerStack());
		}

		this.markDirty();
	}

	@Override
	public void markDirty() {
		//Stuff happens in the super methods
	}

	@Override
	public boolean canPlayerUse(PlayerEntity playerEntity) {
		return true;
	}

	@Override
	public void clear() {
		stacks.clear();
	}

	public DefaultedList<ItemStack> getStacks() {
		return stacks;
	}
}
