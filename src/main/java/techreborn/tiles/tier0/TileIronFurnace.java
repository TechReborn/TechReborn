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

package techreborn.tiles.tier0;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.tile.RebornMachineTile;
import reborncore.common.util.Inventory;
import reborncore.common.util.ItemUtils;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.client.containerBuilder.builder.ContainerBuilder;

public class TileIronFurnace extends RebornMachineTile
		implements IInventoryProvider, IContainerProvider {

	public int tickTime;
	public Inventory inventory = new Inventory(3, "TileIronFurnace", 64, this);
	public int fuel;
	public int fuelGague;
	public int progress;
	public int fuelScale = 160;
	int input1 = 0;
	int output = 1;
	int fuelslot = 2;
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
	public void update() {
		super.update();
		if(world.isRemote){
			return;
		}
		final boolean burning = this.isBurning();
		boolean updateInventory = false;
		if (this.fuel > 0) {
			this.fuel--;
			this.updateState();
		}
		if (this.fuel <= 0 && this.canSmelt()) {
			this.fuel = this.fuelGague = (int) (TileEntityFurnace.getItemBurnTime(this.getStackInSlot(this.fuelslot)) * 1.25);
			if (this.fuel > 0) {
				// Fuel slot
				ItemStack fuelStack = this.getStackInSlot(this.fuelslot);
				if (fuelStack.getItem().hasContainerItem(fuelStack)) {
					this.setInventorySlotContents(this.fuelslot, new ItemStack(fuelStack.getItem().getContainerItem()));
				} else if (fuelStack.getCount() > 1) {
					this.decrStackSize(this.fuelslot, 1);
				} else if (fuelStack.getCount() == 1) {
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

			if (this.getStackInSlot(this.output).isEmpty()) {
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
		if (this.getStackInSlot(this.input1).isEmpty())
			return false;
		final ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.getStackInSlot(this.input1));
		if (itemstack.isEmpty())
			return false;
		if (this.getStackInSlot(this.output).isEmpty())
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
		if (!result.isEmpty()) {
			return result.copy();
		}
		return ItemStack.EMPTY;
	}

	public void updateState() {
		setActive(this.fuel > 0);
	}

	@Override
	public Inventory getInventory() {
		return this.inventory;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		boolean isFuel = TileEntityFurnace.isItemFuel(stack);
		if(isFuel){
			ItemStack fuelSlotStack = getStackInSlot(fuelslot);
			if(fuelSlotStack.isEmpty() || ItemUtils.isItemEqual(stack, fuelSlotStack, true, true) && fuelSlotStack.getMaxStackSize() != fuelSlotStack.getCount()){
				return index == fuelslot;
			}
		}
		return index != output;
	}
	
	@Override
	public boolean canBeUpgraded() {
		return false;
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

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	@Override
	public BuiltContainer createContainer(final EntityPlayer player) {
		return new ContainerBuilder("ironfurnace").player(player.inventory).inventory(8, 84).hotbar(8, 142)
				.addInventory().tile(this).fuelSlot(2, 56, 53).slot(0, 56, 17).outputSlot(1, 116, 35)
				.syncIntegerValue(this::getBurnTime, this::setBurnTime)
				.syncIntegerValue(this::getProgress, this::setProgress)
				.syncIntegerValue(this::getTotalBurnTime, this::setTotalBurnTime).addInventory().create(this);
	}
}
