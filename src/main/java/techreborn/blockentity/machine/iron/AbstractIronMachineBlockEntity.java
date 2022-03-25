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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
	int fuelSlot;
	Block toolDrop;

	public AbstractIronMachineBlockEntity(BlockEntityType<?> blockEntityTypeIn, BlockPos pos, BlockState state, int fuelSlot, Block toolDrop) {
		super(blockEntityTypeIn, pos, state);
		this.fuelSlot = fuelSlot;
		this.toolDrop = toolDrop;
	}

	/**
	 * Checks that we have all inputs and can put output into slot
	 *
	 */
	protected abstract boolean canSmelt();

	/**
	 * Turn ingredients into the appropriate smelted
	 * item in the output slot
	 */
	protected abstract void smelt();

	/**
	 * Get the current recipe's cooking time
	 *
	 */
	protected abstract int cookingTime();

	/**
	 * Returns the number of ticks that the supplied fuel item will keep the
	 * furnace burning, or 0 if the item isn't fuel
	 *
	 * @param stack {@link ItemStack} stack of fuel
	 * @return {@code int} Number of ticks
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
	 * @param scale {@code int} Scale to use for burn time
	 * @return {@code int} scaled remaining fuel burn time
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
	 * @param scale {@code int} Scale to use for crafting progress
	 * @return {@code int} Scaled crafting progress
	 */
	public int getProgressScaled(int scale) {
		if (cookingTime() > 0) {
			return progress * scale / cookingTime();
		}
		return 0;
	}

	/**
	 * Returns true if Iron Machine is burning fuel thus can do work
	 *
	 * @return {@code boolean} True if machine is burning
	 */
	public boolean isBurning() {
		return burnTime > 0;
	}

	private void updateState() {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof BlockMachineBase blockMachineBase) {
			if (state.get(BlockMachineBase.ACTIVE) != burnTime > 0)
				blockMachineBase.setActive(burnTime > 0, world, pos);
		}
	}

	// MachineBaseBlockEntity
	@Override
	public void readNbt(NbtCompound compoundTag) {
		super.readNbt(compoundTag);
		burnTime = compoundTag.getInt("BurnTime");
		totalBurnTime = compoundTag.getInt("TotalBurnTime");
		progress = compoundTag.getInt("Progress");
	}

	@Override
	public void writeNbt(NbtCompound compoundTag) {
		super.writeNbt(compoundTag);
		compoundTag.putInt("BurnTime", burnTime);
		compoundTag.putInt("TotalBurnTime", totalBurnTime);
		compoundTag.putInt("Progress", progress);
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
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
			if (progress == cookingTime()) {
				progress = 0;
				smelt();
			}
		} else if (!canSmelt()) {
			progress = 0;
		}

		if (isBurning != isBurning()) {
			inventory.setHashChanged();
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
