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

package techreborn.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import powercrystals.minefactoryreloaded.api.IDeepStorageUnit;
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.tile.RebornMachineTile;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class TileTechStorageBase extends RebornMachineTile
		implements IInventoryProvider, IToolDrop, IListInfoProvider, IDeepStorageUnit {

	public final int maxCapacity;
	public final Inventory inventory;
	public InvWrapper invWrapper;
	public ItemStack storedItem;

	public TileTechStorageBase(String name, int maxCapacity) {
		this.maxCapacity = maxCapacity;
		storedItem = ItemStack.EMPTY;
		inventory = new Inventory(3, name, maxCapacity, this);
	}

	public void readFromNBTWithoutCoords(NBTTagCompound tagCompound) {

		storedItem = ItemStack.EMPTY;

		if (tagCompound.hasKey("storedStack")) {
			storedItem = new ItemStack((NBTTagCompound) tagCompound.getTag("storedStack"));
		}

		if (!storedItem.isEmpty()) {
			storedItem.setCount(Math.min(tagCompound.getInteger("storedQuantity"), this.maxCapacity));
		}

		inventory.readFromNBT(tagCompound);
	}

	public NBTTagCompound writeToNBTWithoutCoords(NBTTagCompound tagCompound) {
		if (!storedItem.isEmpty()) {
			ItemStack temp = storedItem.copy();
			if (storedItem.getCount() > storedItem.getMaxStackSize()) {
				temp.setCount(storedItem.getMaxStackSize());
			}
			tagCompound.setTag("storedStack", temp.writeToNBT(new NBTTagCompound()));
			tagCompound.setInteger("storedQuantity", Math.min(storedItem.getCount(), maxCapacity));
		} else {
			tagCompound.setInteger("storedQuantity", 0);
		}
		inventory.writeToNBT(tagCompound);
		return tagCompound;
	}

	public ItemStack getDropWithNBT() {
		NBTTagCompound tileEntity = new NBTTagCompound();
		ItemStack dropStack = new ItemStack(getBlockType(), 1);
		writeToNBTWithoutCoords(tileEntity);
		dropStack.setTagCompound(new NBTTagCompound());
		dropStack.getTagCompound().setTag("tileEntity", tileEntity);
		storedItem.setCount(0);
		setInventorySlotContents(1, ItemStack.EMPTY);
		syncWithAll();

		return dropStack;
	}

	public InvWrapper getInvWrapper() {
		if (invWrapper == null)
			invWrapper = new InvWrapper(this);
		return invWrapper;
	}

	public int getStoredCount() {
		return storedItem.getCount();
	}

	public List<ItemStack> getContentDrops() {
		ArrayList<ItemStack> stacks = new ArrayList<>();

		if (!getStoredItemType().isEmpty()) {
			if (!getStackInSlot(1).isEmpty()) {
				stacks.add(getStackInSlot(1));
			}
			int size = storedItem.getMaxStackSize();
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

	// RebornMachineTile
	@Override
	public void update() {
		super.update();
		if (!world.isRemote) {
			ItemStack outputStack = ItemStack.EMPTY;
			if (!getStackInSlot(1).isEmpty()) {
				outputStack = getStackInSlot(1);
			}
			if (!getStackInSlot(0).isEmpty()
					&& (storedItem.getCount() + outputStack.getCount()) < maxCapacity) {
				ItemStack inputStack = getStackInSlot(0);
				if (getStoredItemType().isEmpty()
						|| (storedItem.isEmpty() && ItemUtils.isItemEqual(inputStack, outputStack, true, true))) {

					storedItem = inputStack;
					setInventorySlotContents(0, ItemStack.EMPTY);
				} else if (ItemUtils.isItemEqual(getStoredItemType(), inputStack, true, true)) {
					int reminder = maxCapacity - storedItem.getCount() - outputStack.getCount();
					if (inputStack.getCount() <= reminder) {
						setStoredItemCount(inputStack.getCount());
						setInventorySlotContents(0, ItemStack.EMPTY);
					} else {
						setStoredItemCount(maxCapacity - outputStack.getCount());
						getStackInSlot(0).shrink(reminder);
					}
				}
				markDirty();
				syncWithAll();
			}

			if (!storedItem.isEmpty()) {
				if (outputStack.isEmpty()) {

					ItemStack delivered = storedItem.copy();
					delivered.setCount(Math.min(storedItem.getCount(), delivered.getMaxStackSize()));
					storedItem.shrink(delivered.getCount());

					if (storedItem.isEmpty()) {
						storedItem = ItemStack.EMPTY;
					}

					setInventorySlotContents(1, delivered);
					markDirty();
					syncWithAll();
				} else if (ItemUtils.isItemEqual(storedItem, outputStack, true, true)
						&& outputStack.getCount() < outputStack.getMaxStackSize()) {

					int wanted = Math.min(storedItem.getCount(),
							outputStack.getMaxStackSize() - outputStack.getCount());
					outputStack.setCount(outputStack.getCount() + wanted);
					storedItem.shrink(wanted);

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
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		world.markBlockRangeForRenderUpdate(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
		readFromNBT(packet.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		readFromNBTWithoutCoords(tagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		writeToNBTWithoutCoords(tagCompound);
		return tagCompound;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getInvWrapper());
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	// IInventoryProvider
	@Override
	public Inventory getInventory() {
		return inventory;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(EntityPlayer entityPlayer) {
		return getDropWithNBT();
	}

	// IListInfoProvider
	@Override
	public void addInfo(List<String> info, boolean isRealTile) {
		if (isRealTile) {
			int size = 0;
			String name = "of nothing";
			if (!storedItem.isEmpty()) {
				name = storedItem.getDisplayName();
				size += storedItem.getCount();
			}
			if (!getStackInSlot(1).isEmpty()) {
				name = getStackInSlot(1).getDisplayName();
				size += getStackInSlot(1).getCount();
			}
			info.add(size + " " + name);
		}
	}

	// IDeepStorageUnit
	@Override
	public ItemStack getStoredItemType() {
		return storedItem.isEmpty() ? getStackInSlot(1) : storedItem;
	}

	@Override
	public void setStoredItemCount(int amount) {
		storedItem.grow(amount);
		markDirty();
	}

	@Override
	public void setStoredItemType(ItemStack type, int amount) {
		storedItem = type;
		storedItem.setCount(amount);
		markDirty();
	}

	@Override
	public int getMaxStoredCount() {
		return maxCapacity;
	}
}
