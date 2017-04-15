/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;

import reborncore.api.tile.IInventoryProvider;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.tile.TileLegacyMachineBase;
import reborncore.common.util.Inventory;

import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;

public class TileIronFurnace extends TileLegacyMachineBase
		implements IInventoryProvider, ISidedInventory, IContainerProvider {

	private static final int[] SLOTS_TOP = new int[] { 0 };
	private static final int[] SLOTS_BOTTOM = new int[] { 2, 1 };
	private static final int[] SLOTS_SIDES = new int[] { 1 };

	public int tickTime;
	public Inventory inventory = new Inventory(3, "TileIronFurnace", 64, this);
	public int fuel;
	public int fuelGague;
	public int progress;
	public int fuelScale = 160;
	int input1 = 0;
	int fuelslot = 1;
	int output = 2;

	boolean active = false;

	public int gaugeProgressScaled(final int scale) {
		return this.progress * scale / this.fuelScale;
	}

	public int gaugeFuelScaled(final int scale) {
		if (this.fuelGague == 0) {
			this.fuelGague = this.fuel;
			if (this.fuelGague == 0) {
				this.fuelGague = this.fuelScale;
			}
		}
		return this.fuel * scale / this.fuelGague;
	}

	@Override
	public void updateEntity() {
		final boolean burning = this.isBurning();
		boolean updateInventory = false;
		if (this.fuel > 0) {
			this.fuel--;
			this.updateState();
		}
		if (this.fuel <= 0 && this.canSmelt()) {
			this.fuel = this.fuelGague = (int) (TileEntityFurnace.getItemBurnTime(this.getStackInSlot(this.fuelslot)) * 1.25);
			if (this.fuel > 0) {
				if (this.getStackInSlot(this.fuelslot).getItem().hasContainerItem()) // Fuel
					// slot
				{
					this.setInventorySlotContents(this.fuelslot, new ItemStack(this.getStackInSlot(this.fuelslot).getItem().getContainerItem()));
				} else if (this.getStackInSlot(this.fuelslot).getCount() > 1) {
					this.decrStackSize(this.fuelslot, 1);
				} else if (this.getStackInSlot(this.fuelslot).getCount() == 1) {
					this.setInventorySlotContents(this.fuelslot, ItemStack.EMPTY);
				}
				updateInventory = true;
			}
		}
		if (this.isBurning() && this.canSmelt()) {
			this.progress++;
			if (this.progress >= this.fuelScale) {
				this.progress = 0;
				this.cookItems();
				updateInventory = true;
			}
		} else {
			this.progress = 0;
		}
		if (burning != this.isBurning()) {
			updateInventory = true;
		}
		if (updateInventory) {
			this.markDirty();
		}
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
		if (this.getStackInSlot(this.input1) == ItemStack.EMPTY)
			return false;
		final ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.getStackInSlot(this.input1));
		if (itemstack == ItemStack.EMPTY)
			return false;
		if (this.getStackInSlot(this.output) == ItemStack.EMPTY)
			return true;
		if (!this.getStackInSlot(this.output).isItemEqual(itemstack))
			return false;
		final int result = this.getStackInSlot(this.output).getCount() + itemstack.getCount();
		return result <= this.getInventoryStackLimit() && result <= itemstack.getMaxStackSize();
	}

	public boolean isBurning() {
		return this.fuel > 0;
	}

	public ItemStack getResultFor(final ItemStack stack) {
		final ItemStack result = FurnaceRecipes.instance().getSmeltingResult(stack);
		if (result != ItemStack.EMPTY) {
			return result.copy();
		}
		return null;
	}

	public void updateState() {
		final IBlockState BlockStateContainer = this.world.getBlockState(this.pos);
		if (BlockStateContainer.getBlock() instanceof BlockMachineBase) {
			final BlockMachineBase blockMachineBase = (BlockMachineBase) BlockStateContainer.getBlock();
			if (BlockStateContainer.getValue(BlockMachineBase.ACTIVE) != this.fuel > 0)
				blockMachineBase.setActive(this.fuel > 0, this.world, this.pos);
		}
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	@Override
	public int[] getSlotsForFace(final EnumFacing side) {
		return side == EnumFacing.DOWN ? TileIronFurnace.SLOTS_BOTTOM : side == EnumFacing.UP ? TileIronFurnace.SLOTS_TOP : TileIronFurnace.SLOTS_SIDES;
	}

	@Override
	public boolean canInsertItem(final int index, final ItemStack itemStackIn, final EnumFacing direction) {
		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(final int index, final ItemStack stack, final EnumFacing direction) {
		if (direction == EnumFacing.DOWN && index == 1) {
			final Item item = stack.getItem();

			if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
				return false;
			}
		}

		return true;
	}

	public int getBurnTime() {
		return this.fuel;
	}

	public void setBurnTime(final int burnTime) {
		this.fuel = burnTime;
	}

	public int getTotalBurnTime() {
		return this.fuelGague;
	}

	public void setTotalBurnTime(final int totalBurnTime) {
		this.fuelGague = totalBurnTime;
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("ironfurnace").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().tile(this).slot(0, 56, 17).outputSlot(2, 116, 34).fuelSlot(1, 56, 53)
				.syncIntegerValue(this::getBurnTime, this::setBurnTime)
				.syncIntegerValue(this::getTotalBurnTime, this::setTotalBurnTime).addInventory().create();
	}
}
