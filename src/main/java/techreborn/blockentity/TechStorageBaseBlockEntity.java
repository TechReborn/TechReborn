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

package techreborn.blockentity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;

public class TechStorageBaseBlockEntity extends MachineBaseBlockEntity
		implements InventoryProvider, IToolDrop, IListInfoProvider {

	public final int maxCapacity;
	public final RebornInventory<TechStorageBaseBlockEntity> inventory;
	public ItemStack storedItem;

	public TechStorageBaseBlockEntity(BlockEntityType<?> blockEntityTypeIn, String name, int maxCapacity) {
		super(blockEntityTypeIn);
		this.maxCapacity = maxCapacity;
		storedItem = ItemStack.EMPTY;
		inventory = new RebornInventory<>(3, name, maxCapacity, this);
	}

	public void readWithoutCoords(CompoundTag tagCompound) {

		storedItem = ItemStack.EMPTY;

		if (tagCompound.contains("storedStack")) {
			storedItem = ItemStack.fromTag(tagCompound.getCompound("storedStack"));
		}

		if (!storedItem.isEmpty()) {
			storedItem.setCount(Math.min(tagCompound.getInt("storedQuantity"), this.maxCapacity));
		}

		inventory.read(tagCompound);
	}

	public CompoundTag writeWithoutCoords(CompoundTag tagCompound) {
		if (!storedItem.isEmpty()) {
			ItemStack temp = storedItem.copy();
			if (storedItem.getCount() > storedItem.getMaxCount()) {
				temp.setCount(storedItem.getMaxCount());
			}
			tagCompound.put("storedStack", temp.toTag(new CompoundTag()));
			tagCompound.putInt("storedQuantity", Math.min(storedItem.getCount(), maxCapacity));
		} else {
			tagCompound.putInt("storedQuantity", 0);
		}
		inventory.write(tagCompound);
		return tagCompound;
	}

	public ItemStack getDropWithNBT() {
		CompoundTag blockEntity = new CompoundTag();
		ItemStack dropStack = new ItemStack(getBlockType(), 1);
		writeWithoutCoords(blockEntity);
		dropStack.setTag(new CompoundTag());
		dropStack.getTag().put("blockEntity", blockEntity);
		storedItem.setCount(0);
		inventory.setInvStack(1, ItemStack.EMPTY);
		syncWithAll();

		return dropStack;
	}

	public int getStoredCount() {
		return storedItem.getCount();
	}

	public List<ItemStack> getContentDrops() {
		ArrayList<ItemStack> stacks = new ArrayList<>();

		if (!getStoredItemType().isEmpty()) {
			if (!inventory.getInvStack(1).isEmpty()) {
				stacks.add(inventory.getInvStack(1));
			}
			int size = storedItem.getMaxCount();
			for (int i = 0; i < getStoredCount() / size; i++) {
				ItemStack droped = storedItem.copy();
				droped.setCount(size);
				stacks.add(droped);
			}
			if (getStoredCount() % size != 0) {
				ItemStack droped = storedItem.copy();
				droped.setCount(getStoredCount() % size);
				stacks.add(droped);
			}
		}

		return stacks;
	}

	// TileMachineBase
	@Override
	public void tick() {
		super.tick();
		if (!world.isClient) {
			ItemStack outputStack = ItemStack.EMPTY;
			if (!inventory.getInvStack(1).isEmpty()) {
				outputStack = inventory.getInvStack(1);
			}
			if (!inventory.getInvStack(0).isEmpty()
					&& (storedItem.getCount() + outputStack.getCount()) < maxCapacity) {
				ItemStack inputStack = inventory.getInvStack(0);
				if (getStoredItemType().isEmpty()
						|| (storedItem.isEmpty() && ItemUtils.isItemEqual(inputStack, outputStack, true, true))) {

					storedItem = inputStack;
					inventory.setInvStack(0, ItemStack.EMPTY);
				} else if (ItemUtils.isItemEqual(getStoredItemType(), inputStack, true, true)) {
					int reminder = maxCapacity - storedItem.getCount() - outputStack.getCount();
					if (inputStack.getCount() <= reminder) {
						setStoredItemCount(inputStack.getCount());
						inventory.setInvStack(0, ItemStack.EMPTY);
					} else {
						setStoredItemCount(maxCapacity - outputStack.getCount());
						inventory.getInvStack(0).decrement(reminder);
					}
				}
				markDirty();
				syncWithAll();
			}

			if (!storedItem.isEmpty()) {
				if (outputStack.isEmpty()) {

					ItemStack delivered = storedItem.copy();
					delivered.setCount(Math.min(storedItem.getCount(), delivered.getMaxCount()));
					storedItem.decrement(delivered.getCount());

					if (storedItem.isEmpty()) {
						storedItem = ItemStack.EMPTY;
					}

					inventory.setInvStack(1, delivered);
					markDirty();
					syncWithAll();
				} else if (ItemUtils.isItemEqual(storedItem, outputStack, true, true)
						&& outputStack.getCount() < outputStack.getMaxCount()) {

					int wanted = Math.min(storedItem.getCount(),
							outputStack.getMaxCount() - outputStack.getCount());
					outputStack.setCount(outputStack.getCount() + wanted);
					storedItem.decrement(wanted);

					if (storedItem.isEmpty()) {
						storedItem = ItemStack.EMPTY;
					}
					markDirty();
					syncWithAll();
				}
			}
		}
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	@Override
	public void fromTag(CompoundTag tagCompound) {
		super.fromTag(tagCompound);
		readWithoutCoords(tagCompound);
	}

	@Override
	public CompoundTag toTag(CompoundTag tagCompound) {
		super.toTag(tagCompound);
		writeWithoutCoords(tagCompound);
		return tagCompound;
	}

	// ItemHandlerProvider
	@Override
	public RebornInventory<TechStorageBaseBlockEntity> getInventory() {
		return inventory;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity entityPlayer) {
		return getDropWithNBT();
	}

	// IListInfoProvider
	@Override
	public void addInfo(List<Text> info, boolean isReal, boolean hasData) {
		if (isReal || hasData) {
			int size = 0;
			String name = "of nothing";
			if (!storedItem.isEmpty()) {
				name = storedItem.getName().getString();
				size += storedItem.getCount();
			}
			if (!inventory.getInvStack(1).isEmpty()) {
				name = inventory.getInvStack(1).getName().getString();
				size += inventory.getInvStack(1).getCount();
			}
			info.add(new LiteralText(size + " " + name));
		}
	}

	public ItemStack getStoredItemType() {
		return storedItem.isEmpty() ? inventory.getInvStack(1) : storedItem;
	}


	public void setStoredItemCount(int amount) {
		storedItem.increment(amount);
		markDirty();
	}

	public void setStoredItemType(ItemStack type, int amount) {
		storedItem = type;
		storedItem.setCount(amount);
		markDirty();
	}

	public int getMaxStoredCount() {
		return maxCapacity;
	}
}
