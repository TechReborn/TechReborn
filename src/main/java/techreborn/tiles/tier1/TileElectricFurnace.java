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

package techreborn.tiles.tier1;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumFacing;
import reborncore.api.IToolDrop;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.Inventory;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;

public class TileElectricFurnace extends TilePowerAcceptor
		implements IToolDrop, IInventoryProvider, IContainerProvider {

	@ConfigRegistry(config = "machines", category = "electric_furnace", key = "ElectricFurnaceInput", comment = "Electric Furnace Max Input (Value in EU)")
	public static int maxInput = 32;
	@ConfigRegistry(config = "machines", category = "electric_furnace", key = "ElectricFurnaceMaxEnergy", comment = "Electric Furnace Max Energy (Value in EU)")
	public static int maxEnergy = 1000;

	public Inventory inventory = new Inventory(3, "TileElectricFurnace", 64, this);
	public int progress;
	public int fuelScale = 100;
	public int cost = 6;
	int input1 = 0;
	int output = 1;
	boolean wasBurning = false;

	public TileElectricFurnace() {
		super();
	}

	public int gaugeProgressScaled(final int scale) {
		return this.progress * scale / this.fuelScale;
	}

	public void cookItems() {
		if (this.canSmelt()) {
			final ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.getStackInSlot(this.input1));

			if (this.getStackInSlot(this.output) == ItemStack.EMPTY) {
				this.setInventorySlotContents(this.output, itemstack.copy());
			} else if (this.getStackInSlot(this.output).isItemEqual(itemstack)) {
				this.getStackInSlot(this.output).grow(itemstack.getCount());
			}
			if (this.getStackInSlot(this.input1).getCount() > 1) {
				this.decrStackSize(this.input1, 1);
			} else {
				this.setInventorySlotContents(this.input1, ItemStack.EMPTY);
			}
		}
	}

	public boolean canSmelt() {
		if (this.getStackInSlot(this.input1) == ItemStack.EMPTY) {
			return false;
		}
		final ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.getStackInSlot(this.input1));
		if (itemstack.isEmpty())
			return false;
		if (this.getStackInSlot(this.output) == ItemStack.EMPTY)
			return true;
		if (!this.getStackInSlot(this.output).isItemEqual(itemstack))
			return false;
		final int result = this.getStackInSlot(this.output).getCount() + itemstack.getCount();
		return result <= this.getInventoryStackLimit() && result <= itemstack.getMaxStackSize();
	}

	public boolean isBurning() {
		return this.getEnergy() > getEuPerTick(this.cost);
	}

	public ItemStack getResultFor(final ItemStack stack) {
		final ItemStack result = FurnaceRecipes.instance().getSmeltingResult(stack);
		if (!result.isEmpty()) {
			return result.copy();
		}
		return ItemStack.EMPTY;
	}

	public void updateState() {
		if (wasBurning != (this.progress > 0)) {
			// skips updating the block state for 1 tick, to prevent the machine from
			// turning on/off rapidly causing fps drops
			if (wasBurning && this.progress == 0 && canSmelt()) {
				wasBurning = true;
				return;
			}
			final IBlockState BlockStateContainer = this.world.getBlockState(this.pos);
			if (BlockStateContainer.getBlock() instanceof BlockMachineBase) {
				final BlockMachineBase blockMachineBase = (BlockMachineBase) BlockStateContainer.getBlock();
				if (BlockStateContainer.getValue(BlockMachineBase.ACTIVE) != this.progress > 0)
					blockMachineBase.setActive(this.progress > 0, this.world, this.pos);
			}
			wasBurning = (this.progress > 0);
		}

	}

	public int getBurnTime() {
		return this.progress;
	}

	public void setBurnTime(final int burnTime) {
		this.progress = burnTime;
	}

	// TilePowerAcceptor
	@Override
	public void update() {
		if (world.isRemote) {
			return;
		}

		super.update();
		this.charge(2);

		final boolean burning = this.isBurning();
		boolean updateInventory = false;
		if (this.isBurning() && this.canSmelt()) {
			this.updateState();
			if (canUseEnergy(getEuPerTick(this.cost))) {
				this.useEnergy(getEuPerTick(this.cost));
				this.progress++;
				if (this.progress >= Math.max((int) (fuelScale * (1.0 - getSpeedMultiplier())), 2)) {
					this.progress = 0;
					this.cookItems();
					updateInventory = true;
				}
			}
		} else {
			this.progress = 0;
			this.updateState();
		}
		if (burning != this.isBurning()) {
			updateInventory = true;
		}
		if (updateInventory) {
			this.markDirty();
		}

	}

	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(final EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(final EnumFacing direction) {
		return false;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return maxInput;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(final EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.ELECTRIC_FURNACE, 1);
	}

	// IInventoryProvider
	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	// IContainerProvider
	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("electricfurnace").player(player.inventory).inventory().hotbar().addInventory()
				.tile(this).slot(0, 55, 45).outputSlot(1, 101, 45).energySlot(2, 8, 72).syncEnergyValue()
				.syncIntegerValue(this::getBurnTime, this::setBurnTime).addInventory().create(this);
	}
}
