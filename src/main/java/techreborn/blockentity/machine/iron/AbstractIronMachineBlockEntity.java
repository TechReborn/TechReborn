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

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
	final int fuelSlot;
	final Block toolDrop;

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
		if (stack.isEmpty() || level == null) {
			return 0;
		}
		return (int) (level.fuelValues().burnDuration(stack) * TechRebornConfig.fuelScale);
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
		BlockState state = level.getBlockState(worldPosition);
		if (state.getBlock() instanceof BlockMachineBase blockMachineBase) {
			if (state.getValue(BlockMachineBase.ACTIVE) != burnTime > 0)
				blockMachineBase.setActive(burnTime > 0, level, worldPosition);
		}
	}

	// MachineBaseBlockEntity
	@Override
	public void loadAdditional(ValueInput view) {
		super.loadAdditional(view);
		burnTime = view.getIntOr("BurnTime", 0);
		totalBurnTime = view.getIntOr("TotalBurnTime", 0);
		progress = view.getIntOr("Progress", 0);
	}

	@Override
	public void saveAdditional(ValueOutput view) {
		super.saveAdditional(view);
		view.putInt("BurnTime", burnTime);
		view.putInt("TotalBurnTime", totalBurnTime);
		view.putInt("Progress", progress);
	}

	@Override
	public void tick(Level world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world.isClientSide) {
			return;
		}
		boolean isBurning = isBurning();
		if (isBurning) {
			--burnTime;
		}

		boolean canSmelt = canSmelt();
		if (!isBurning && canSmelt) {
			burnTime = totalBurnTime = getItemBurnTime(inventory.getItem(fuelSlot));
			if (burnTime > 0) {
				// Fuel slot
				ItemStack fuelStack = inventory.getItem(fuelSlot);
				ItemStack remainderStack = fuelStack.getItem().getCraftingRemainder();
				if (!remainderStack.isEmpty()) {
					inventory.setItem(fuelSlot, remainderStack);
				} else if (fuelStack.getCount() > 1) {
					inventory.shrinkSlot(fuelSlot, 1);
				} else if (fuelStack.getCount() == 1) {
					inventory.setItem(fuelSlot, ItemStack.EMPTY);
				}
			}
		}

		if (isBurning() && canSmelt) {
			++progress;
			if (progress == cookingTime()) {
				progress = 0;
				smelt();
			}
		} else if (!canSmelt) {
			progress = 0;
		}

		if (isBurning != isBurning()) {
			inventory.setHashChanged();
			updateState();
		}
		if (inventory.hasChanged()) {
			setChanged();
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
	public ItemStack getToolDrop(Player entityPlayer) {
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
