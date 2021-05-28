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

package reborncore.common.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;
import reborncore.api.items.InventoryBase;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.SlotConfiguration;

import org.jetbrains.annotations.NotNull;

public class RebornInventory<T extends MachineBaseBlockEntity> extends InventoryBase {

	private final String name;
	private final int stackLimit;
	private final T blockEntity;
	private boolean hasChanged = false;
	private final IInventoryAccess<T> inventoryAccess;

	public RebornInventory(int size, String invName, int invStackLimit, T blockEntity, IInventoryAccess<T> access) {
		super(size);
		name = invName;
		stackLimit = (invStackLimit == 64 ? Items.AIR.getMaxCount() : invStackLimit); //Blame asie for this
		this.blockEntity = blockEntity;
		this.inventoryAccess = access;
	}

	//If you are using this with a machine, dont forget to set .withConfiguredAccess()
	public RebornInventory(int size, String invName, int invStackLimit, T blockEntity) {
		this(size, invName, invStackLimit, blockEntity, (slotID, stack, facing, direction, be) -> {
			if (facing == null) {
				return true;
			}
			switch (direction) {
				case INSERT:
					return SlotConfiguration.canInsertItem(slotID, stack, facing, be);
				case EXTRACT:
					return SlotConfiguration.canExtractItem(slotID, stack, facing, be);
			}
			return false;
		});
	}

	public String getName() {
		return name;
	}

	@Override
	public void setStack(int slot, @NotNull ItemStack stack) {
		super.setStack(slot, stack);
		setChanged();
	}

	@Override
	public ItemStack removeStack(int i, int i1) {
		ItemStack stack = super.removeStack(i, i1);

		if (!stack.isEmpty()) {
			setChanged();
		}

		return stack;
	}

	@Override
	public int getMaxCountPerStack() {
		return stackLimit;
	}

	public ItemStack shrinkSlot(int slot, int count) {
		ItemStack stack = getStack(slot);
		stack.decrement(count);
		setChanged();
		return stack;
	}


	public RebornInventory getExternal(Direction facing) {
		throw new UnsupportedOperationException("needs fixing");
		//return externalInventory.withFacing(facing);
	}

	public void read(NbtCompound data) {
		read(data, "Items");
	}

	public void read(NbtCompound data, String tag) {
		NbtCompound nbttaglist = data.getCompound(tag);
		deserializeNBT(nbttaglist);
		hasChanged = true;
	}

	public void write(NbtCompound data) {
		write(data, "Items");
	}

	public void write(NbtCompound data, String tag) {
		data.put(tag, serializeNBT());
	}


	public int getContents() {
		int count = 0;
		for (ItemStack stack : getStacks()) {
			if (stack.isEmpty()) {
				continue;
			}
			count += stack.getCount();
		}
		return count;
	}

	public T getBlockEntity() {
		return blockEntity;
	}

	public boolean hasChanged() {
		return hasChanged;
	}

	public void setChanged() {
		this.hasChanged = true;
	}

	public void setChanged(boolean changed) {
		this.hasChanged = changed;
	}

	public void resetChanged() {
		this.hasChanged = false;
	}

	public int getStackLimit() {
		return stackLimit;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		blockEntity.markDirty();
	}

}
