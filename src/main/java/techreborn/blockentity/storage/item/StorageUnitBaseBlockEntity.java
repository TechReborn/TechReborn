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

package techreborn.blockentity.storage.item;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.ItemUtils;

import java.util.List;

public abstract class StorageUnitBaseBlockEntity extends MachineBaseBlockEntity
		implements InventoryProvider, IToolDrop, IListInfoProvider {


	// Inventory of machine 1: Storage
	private final RebornInventory<StorageUnitBaseBlockEntity> inventory;
	// Stack in output slot
	private ItemStack storeItemStack;

	// Inventory constants
	private static final int INPUT_SLOT = 0;
	private static final int OUTPUT_SLOT = 1;

	private final int maxCapacity;

	public StorageUnitBaseBlockEntity(BlockEntityType<?> blockEntityTypeIn, int maxCapacity) {
		super(blockEntityTypeIn);
		this.maxCapacity = maxCapacity;
		storeItemStack = ItemStack.EMPTY;
		inventory = new RebornInventory<>(2, "ItemInventory", maxCapacity, this);
	}

	public void readWithoutCoords(CompoundTag tagCompound) {

		storeItemStack = ItemStack.EMPTY;

		if (tagCompound.contains("storedStack")) {
			storeItemStack = ItemStack.fromTag(tagCompound.getCompound("storedStack"));
		}

		if (!storeItemStack.isEmpty()) {
			storeItemStack.setCount(Math.min(tagCompound.getInt("storedQuantity"), this.maxCapacity));
		}

		inventory.read(tagCompound);
	}

	public CompoundTag writeWithoutCoords(CompoundTag tagCompound) {
		if (!storeItemStack.isEmpty()) {
			ItemStack temp = storeItemStack.copy();
			if (storeItemStack.getCount() > storeItemStack.getMaxCount()) {
				temp.setCount(storeItemStack.getMaxCount());
			}
			tagCompound.put("storedStack", temp.toTag(new CompoundTag()));
			tagCompound.putInt("storedQuantity", Math.min(storeItemStack.getCount(), maxCapacity));
		} else {
			tagCompound.putInt("storedQuantity", 0);
		}
		inventory.write(tagCompound);
		return tagCompound;
	}

	// TileMachineBase
	@Override
	public void tick() {
		super.tick();
		if (world.isClient) {
			return;
		}

		// If there is an item in the input AND stored is less than max capacity
		if (!inventory.getInvStack(INPUT_SLOT).isEmpty() && !isFull()) {
			inventory.setInvStack(INPUT_SLOT, processInput(inventory.getInvStack(INPUT_SLOT)));

			inventory.setChanged();
			markDirty();
			syncWithAll();
		}


		// Fill output slot with goodies when stored has items and output count is less than max stack size
		if (storeItemStack.getCount() > 0 && inventory.getInvStack(OUTPUT_SLOT).getCount() < storeItemStack.getMaxCount()) {
			populateOutput();
			markDirty();
			syncWithAll();
		}
	}

	private void populateOutput() {
		// Set to storeItemStack to get the stack type
		ItemStack output = storeItemStack.copy();

		int outputSlotCount = inventory.getInvStack(OUTPUT_SLOT).getCount();

		// Set to current outputSlot count
		output.setCount(outputSlotCount);

		// Calculate amount needed to fill stack in output slot
		int amountToFill = output.getMaxCount() -  outputSlotCount;

		if(storeItemStack.getCount() >= amountToFill){
			storeItemStack.decrement(amountToFill);

			if (storeItemStack.isEmpty()) {
				storeItemStack = ItemStack.EMPTY;
			}

			output.increment(amountToFill);
		}else{
			output.increment(storeItemStack.getCount());
			storeItemStack = ItemStack.EMPTY;
		}

		inventory.setInvStack(OUTPUT_SLOT, output);

		markDirty();
		syncWithAll();
	}

	public ItemStack processInput(ItemStack inputStack) {

		boolean isSameStack = isSameType(inputStack);

		if (storeItemStack == ItemStack.EMPTY && (isSameStack || getCurrentCapacity() == 0)) {
			// Check if storage is empty, NOT including the output slot

			storeItemStack = inputStack.copy();
			inputStack = ItemStack.EMPTY;
		} else if (isSameStack){
			// Not empty but same type

			// Amount of items that can be added before reaching capacity
			int reminder = maxCapacity - getCurrentCapacity();


			if (inputStack.getCount() <= reminder) {
				// Add full stack
				addStoredItemCount(inputStack.getCount());
				inputStack = ItemStack.EMPTY;
			} else {
				// Add only what is needed to reach max capacity
				addStoredItemCount(reminder);
				inputStack.decrement(reminder);
			}
		}

		return inputStack;
	}

	public boolean isSameType(ItemStack inputStack) {
		if(inputStack != ItemStack.EMPTY){
			return ItemUtils.isItemEqual(getStoredStack(), inputStack, true, true);
		}
		return false;
	}

	private ItemStack getStoredStack() {
		return storeItemStack.isEmpty() ? inventory.getInvStack(OUTPUT_SLOT) : storeItemStack;
	}

	private void addStoredItemCount(int amount) {
		storeItemStack.increment(amount);
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
	public RebornInventory<StorageUnitBaseBlockEntity> getInventory() {
		return inventory;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity entityPlayer) {
		return getDropWithNBT();
	}

	public ItemStack getDropWithNBT() {
		ItemStack dropStack = new ItemStack(getBlockType(), 1);
		final CompoundTag blockEntity = new CompoundTag();
		this.writeWithoutCoords(blockEntity);
		dropStack.setTag(new CompoundTag());
		dropStack.getTag().put("blockEntity", blockEntity);

		return dropStack;
	}

	// IListInfoProvider
	@Override
	public void addInfo(List<Text> info, boolean isReal, boolean hasData) {
		if (isReal || hasData) {
			int size = 0;
			String name = "of nothing";
			if (!storeItemStack.isEmpty()) {
				name = storeItemStack.getName().getString();
				size += storeItemStack.getCount();
			}
			if (!inventory.getInvStack(1).isEmpty()) {
				name = inventory.getInvStack(1).getName().getString();
				size += inventory.getInvStack(1).getCount();
			}
			info.add(new LiteralText(size + " " + name));
		}
	}

	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("chest").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this).slot(0, 80, 24).outputSlot(1, 80, 64).addInventory().create(this, syncID);
	}

	public boolean isFull(){
		return getCurrentCapacity() == maxCapacity;
	}

	public int getCurrentCapacity() {
		return storeItemStack.getCount() + inventory.getInvStack(OUTPUT_SLOT).getCount();
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}
}
