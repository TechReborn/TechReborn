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

package techreborn.tiles.teir1;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumFacing;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.tile.IMachineSlotProvider;
import reborncore.common.util.Inventory;
import techreborn.client.container.IContainerProvider;
import techreborn.client.container.builder.BuiltContainer;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.init.ModBlocks;

public class TileElectricFurnace extends TilePowerAcceptor
	implements IWrenchable, IInventoryProvider, IContainerProvider, IMachineSlotProvider {

	public Inventory inventory = new Inventory(6, "TileElectricFurnace", 64, this);
	public int capacity = 1000;
	public int progress;
	public int fuelScale = 100;
	public int cost = 8;
	int input1 = 0;
	int output = 1;

	public TileElectricFurnace() {
		super(1);
	}

	public int gaugeProgressScaled(final int scale) {
		return this.progress * scale / this.fuelScale;
	}

	@Override
	public void update() {
		super.update();
		if (!this.world.isRemote) {
			final boolean burning = this.isBurning();
			boolean updateInventory = false;
			if (this.isBurning() && this.canSmelt()) {
				this.updateState();

				this.progress++;
				if (this.progress % 10 == 0) {
					this.useEnergy(this.cost);
				}
				if (this.progress >= this.fuelScale) {
					this.progress = 0;
					this.cookItems();
					updateInventory = true;
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
		return this.getEnergy() > this.cost;
	}

	public ItemStack getResultFor(final ItemStack stack) {
		final ItemStack result = FurnaceRecipes.instance().getSmeltingResult(stack);
		if (result != ItemStack.EMPTY) {
			return result.copy();
		}
		return ItemStack.EMPTY;
	}

	public void updateState() {
		final IBlockState BlockStateContainer = this.world.getBlockState(this.pos);
		if (BlockStateContainer.getBlock() instanceof BlockMachineBase) {
			final BlockMachineBase blockMachineBase = (BlockMachineBase) BlockStateContainer.getBlock();
			if (BlockStateContainer.getValue(BlockMachineBase.ACTIVE) != this.progress > 0)
				blockMachineBase.setActive(this.progress > 0, this.world, this.pos);
		}
	}

	@Override
	public boolean wrenchCanSetFacing(final EntityPlayer entityPlayer, final EnumFacing side) {
		return false;
	}

	@Override
	public EnumFacing getFacing() {
		return this.getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(final EntityPlayer entityPlayer) {
		return entityPlayer.isSneaking();
	}

	@Override
	public float getWrenchDropRate() {
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(final EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.ELECTRIC_FURNACE, 1);
	}

	public boolean isComplete() {
		return false;
	}

	// ISidedInventory
	@Override
	public int[] getSlotsForFace(final EnumFacing side) {
		if (side.equals(EnumFacing.UP))
			return new int[] { 0 };
		else if (side.equals(EnumFacing.DOWN))
			return new int[] { 1 };
		return new int[0];
	}

	@Override
	public boolean canInsertItem(final int slotIndex, final ItemStack itemStack, final EnumFacing side) {
		if (slotIndex == 2)
			return false;
		return this.isItemValidForSlot(slotIndex, itemStack);
	}

	@Override
	public boolean canExtractItem(final int slotIndex, final ItemStack itemStack, final EnumFacing side) {
		return slotIndex == 1;
	}

	@Override
	public double getBaseMaxPower() {
		return this.capacity;
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
		return 32;
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	public int getBurnTime() {
		return this.progress;
	}

	public void setBurnTime(final int burnTime) {
		this.progress = burnTime;
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("electricfurnace").player(player.inventory).inventory().hotbar().addInventory()
			.tile(this).slot(0, 55, 45).outputSlot(1, 101, 45).syncEnergyValue()
			.syncIntegerValue(this::getBurnTime, this::setBurnTime).addInventory().create();
	}

	@Override
	public int[] getInputSlots() {
		return new int[] { input1 };
	}

	@Override
	public int[] getOuputSlots() {
		return new int[] { output };
	}

	@Override
	public IInventory getMachineInv() {
		return inventory;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}
}
