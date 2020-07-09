/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.blockentity.machine.iron;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import reborncore.api.IToolDrop;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.SlotConfiguration;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.util.RebornInventory;
import techreborn.config.TechRebornConfig;

public abstract class AbstractIronMachineBlockEntity extends MachineBaseBlockEntity implements InventoryProvider, IToolDrop, SlotConfiguration.SlotFilter {

	public RebornInventory<?> inventory;
	public int burnTime;
	public int totalBurnTime;
	public int progress;
	public int totalCookingTime;
	int fuelSlot;
	Block toolDrop;
	boolean active = false;

	public AbstractIronMachineBlockEntity(BlockEntityType<?> blockEntityTypeIn, int fuelSlot, Block toolDrop) {
		super(blockEntityTypeIn);
		this.fuelSlot = fuelSlot;
		// default value for vanilla smelting recipes is 200
		this.totalCookingTime = (int) (200 / TechRebornConfig.cookingScale);
		this.toolDrop = toolDrop;
	}

	/**
	 * Checks that we have all inputs and can put output into slot
	 *
	 * @return
	 */
	protected abstract boolean canSmelt();

	/**
	 * Turn ingredients into the appropriate smelted
	 * item in the output slot
	 */
	protected abstract void smelt();

	/**
	 * Returns the number of ticks that the supplied fuel item will keep the
	 * furnace burning, or 0 if the item isn't fuel
	 *
	 * @param stack Itemstack of fuel
	 * @return Integer Number of ticks
	 */
	private int getItemBurnTime(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		}
		return (int) (AbstractFurnaceBlockEntity.createFuelTimeMap().getOrDefault(stack.getItem(), 0) * TechRebornConfig.fuelScale);
	}

	/**
	 * Returns remaining fraction of fuel burn time
	 *
	 * @param scale Scale to use for burn time
	 * @return int scaled remaining fuel burn time
	 */
	public int getBurnTimeRemainingScaled(int scale) {
		if (totalBurnTime == 0) {
			return 0;
		}

		return burnTime * scale / totalBurnTime;
	}

	/**
	 * Returns crafting progress
	 *
	 * @param scale Scale to use for crafting progress
	 * @return int Scaled crafting progress
	 */
	public int getProgressScaled(int scale) {
		if (totalCookingTime > 0) {
			return progress * scale / totalCookingTime;
		}
		return 0;
	}

	/**
	 * Returns true if Iron Machine is burning fuel thus can do work
	 *
	 * @return Boolean True if machine is burning
	 */
	public boolean isBurning() {
		return burnTime > 0;
	}

	private void updateState() {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof BlockMachineBase) {
			BlockMachineBase blockMachineBase = (BlockMachineBase) state.getBlock();
			if (state.get(BlockMachineBase.ACTIVE) != burnTime > 0)
				blockMachineBase.setActive(burnTime > 0, world, pos);
		}
	}

	// MachineBaseBlockEntity
	@Override
	public void fromTag(BlockState blockState, CompoundTag compoundTag) {
		super.fromTag(blockState, compoundTag);
		burnTime = compoundTag.getInt("BurnTime");
		totalBurnTime = compoundTag.getInt("TotalBurnTime");
		progress = compoundTag.getInt("Progress");
	}

	@Override
	public CompoundTag toTag(CompoundTag compoundTag) {
		super.toTag(compoundTag);
		compoundTag.putInt("BurnTime", burnTime);
		compoundTag.putInt("TotalBurnTime", totalBurnTime);
		compoundTag.putInt("Progress", progress);
		return compoundTag;
	}

	@Override
	public void tick() {
		super.tick();
		if (world.isClient) {
			return;
		}
		boolean isBurning = isBurning();
		if (isBurning) {
			--burnTime;
		}

		if (!isBurning && canSmelt()) {
			burnTime = totalBurnTime = getItemBurnTime(inventory.getStack(fuelSlot));
			if (burnTime > 0) {
				// Fuel slot
				ItemStack fuelStack = inventory.getStack(fuelSlot);
				if (fuelStack.getItem().hasRecipeRemainder()) {
					inventory.setStack(fuelSlot, new ItemStack(fuelStack.getItem().getRecipeRemainder()));
				} else if (fuelStack.getCount() > 1) {
					inventory.shrinkSlot(fuelSlot, 1);
				} else if (fuelStack.getCount() == 1) {
					inventory.setStack(fuelSlot, ItemStack.EMPTY);
				}
			}
		}

		if (isBurning() && canSmelt()) {
			++progress;
			if (progress == totalCookingTime) {
				progress = 0;
				smelt();
			}
		} else if (!canSmelt()) {
			progress = 0;
		}

		if (isBurning != isBurning()) {
			inventory.setChanged();
			updateState();
		}
		if (inventory.hasChanged()) {
			markDirty();
		}
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	// InventoryProvider
	@Override
	public RebornInventory<?> getInventory() {
		return inventory;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity entityPlayer) {
		return new ItemStack(toolDrop);
	}

	public int getBurnTime() {
		return this.burnTime;
	}

	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
	}

	public int getTotalBurnTime() {
		return this.totalBurnTime;
	}

	public void setTotalBurnTime(int totalBurnTime) {
		this.totalBurnTime = totalBurnTime;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}


}