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

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.WorldUtils;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.List;

public class StorageUnitBaseBlockEntity extends MachineBaseBlockEntity
		implements InventoryProvider, IToolDrop, IListInfoProvider, IContainerProvider {


	// Inventory constants
	private static final int INPUT_SLOT = 0;
	private static final int OUTPUT_SLOT = 1;

	private RebornInventory<StorageUnitBaseBlockEntity> inventory;
	private int maxCapacity;

	private boolean shouldUpdate = false;

	private ItemStack storeItemStack;

	private TRContent.StorageUnit type;

	public StorageUnitBaseBlockEntity() {
		super(TRBlockEntities.STORAGE_UNIT);
	}

	public StorageUnitBaseBlockEntity(TRContent.StorageUnit type) {
		super(TRBlockEntities.STORAGE_UNIT);
		configureEntity(type);
	}

	private void configureEntity(TRContent.StorageUnit type){
		this.maxCapacity = type.capacity;
		storeItemStack = ItemStack.EMPTY;
		inventory = new RebornInventory<>(2, "ItemInventory", 64, this);

		this.type = type;
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

			shouldUpdate = true;
		}

		// Fill output slot with goodies when stored has items and output count is less than max stack size
		if (storeItemStack.getCount() > 0 && inventory.getInvStack(OUTPUT_SLOT).getCount() < getStoredStack().getMaxCount()) {
			populateOutput();

			shouldUpdate = true;
		}

		if(type == TRContent.StorageUnit.CREATIVE){
			if (!isFull() && !isEmpty()) {
				fillToCapacity();
				shouldUpdate = true;
			}
		}


		if (shouldUpdate) {
			inventory.setChanged();
			markDirty();
			syncWithAll();

			shouldUpdate = false;
		}
	}

	private void populateOutput() {
		// Set to storeItemStack to get the stack type
		ItemStack output = storeItemStack.copy();

		int outputSlotCount = inventory.getInvStack(OUTPUT_SLOT).getCount();

		// Set to current outputSlot count
		output.setCount(outputSlotCount);

		// Calculate amount needed to fill stack in output slot
		int amountToFill = getStoredStack().getMaxCount() - outputSlotCount;

		if (storeItemStack.getCount() >= amountToFill) {
			storeItemStack.decrement(amountToFill);

			if (storeItemStack.isEmpty()) {
				storeItemStack = ItemStack.EMPTY;
			}

			output.increment(amountToFill);
		} else {
			output.increment(storeItemStack.getCount());
			storeItemStack = ItemStack.EMPTY;
		}

		inventory.setInvStack(OUTPUT_SLOT, output);
	}

	private void addStoredItemCount(int amount) {
		storeItemStack.increment(amount);
	}

	public ItemStack getStoredStack() {
		return storeItemStack.isEmpty() ? inventory.getInvStack(OUTPUT_SLOT) : storeItemStack;
	}

	public ItemStack processInput(ItemStack inputStack) {

		boolean isSameStack = isSameType(inputStack);

		if (storeItemStack == ItemStack.EMPTY && (isSameStack || getCurrentCapacity() == 0)) {
			// Check if storage is empty, NOT including the output slot

			storeItemStack = inputStack.copy();
			inputStack = ItemStack.EMPTY;
		} else if (isSameStack) {
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
		if (inputStack != ItemStack.EMPTY) {
			return ItemUtils.isItemEqual(getStoredStack(), inputStack, true, true);
		}
		return false;
	}

	// Creative function
	public void fillToCapacity() {
		storeItemStack = getStoredStack();
		storeItemStack.setCount(maxCapacity);

		inventory.setInvStack(OUTPUT_SLOT, ItemStack.EMPTY);
	}

	public boolean isFull() {
		return getCurrentCapacity() == maxCapacity;
	}

	public boolean isEmpty() {
		return getCurrentCapacity() == 0;
	}

	public int getCurrentCapacity() {
		return storeItemStack.getCount() + inventory.getInvStack(OUTPUT_SLOT).getCount();
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	// Other stuff


	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	@Override
	public void fromTag(CompoundTag tagCompound) {
		super.fromTag(tagCompound);

		this.type = TRContent.StorageUnit.valueOf(tagCompound.getString("unitType"));
		configureEntity(type);

		storeItemStack = ItemStack.EMPTY;

		if (tagCompound.contains("storedStack")) {
			storeItemStack = ItemStack.fromTag(tagCompound.getCompound("storedStack"));
		}

		if (!storeItemStack.isEmpty()) {
			storeItemStack.setCount(Math.min(tagCompound.getInt("storedQuantity"), this.maxCapacity));
		}

		inventory.read(tagCompound);
	}

	@Override
	public CompoundTag toTag(CompoundTag tagCompound) {
		super.toTag(tagCompound);

		tagCompound.putString("unitType", this.type.name());

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

		this.toTag(blockEntity);
		dropStack.setTag(new CompoundTag());
		dropStack.getTag().put("blockEntity", blockEntity);

		return dropStack;
	}

	// IListInfoProvider
	@Override
	public void addInfo(final List<Text> info, final boolean isReal, boolean hasData) {
		if (isReal || hasData) {
			if (!this.isEmpty()) {
				info.add(new LiteralText(this.getCurrentCapacity() + " of " + this.getStoredStack().getName().asString()));
			} else {
				info.add(new LiteralText("Empty"));
			}
		}

		info.add(new LiteralText(Formatting.GRAY + "Capacity: " + Formatting.GOLD + this.getMaxCapacity() + " items"));
	}

	@Override
	public void onBreak(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState blockState) {
		super.onBreak(world, playerEntity, blockPos, blockState);

		// No need to drop anything for creative peeps
		if (this instanceof CreativeStorageUnitBlockEntity) {
			this.inventory.clear();
			return;
		}

		// Drop stacks (In one clump, reduce lag)
		if (storeItemStack != ItemStack.EMPTY) {
			WorldUtils.dropItem(storeItemStack, world, pos);
		}

		// Inventory gets dropped automatically
	}

	public BuiltContainer createContainer(int syncID, final PlayerEntity player) {
		return new ContainerBuilder("chest").player(player.inventory).inventory().hotbar().addInventory()
				.blockEntity(this).slot(0, 100, 53).outputSlot(1, 140, 53).addInventory().create(this, syncID);
	}
}
