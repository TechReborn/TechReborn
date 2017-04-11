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

package techreborn.tiles.storage;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.blocks.storage.BlockEnergyStorage;

/**
 * Created by Rushmead
 */
public class TileEnergyStorage extends TilePowerAcceptor implements IWrenchable, ITickable, IInventoryProvider {

	public Inventory inventory;
	public String name;
	public Block wrenchDrop;
	public EnumPowerTier tier;
	public int maxInput;
	public int maxOutput;
	public int maxStorage;

	public TileEnergyStorage(String name, int invSize, Block wrenchDrop, EnumPowerTier tier, int maxInput, int maxOuput, int maxStorage) {
		super(1);
		inventory = new Inventory(invSize, "Tile" + name, 64, this);
		this.wrenchDrop = wrenchDrop;
		this.tier = tier;
		this.name = name;
		this.maxInput = maxInput;
		this.maxOutput = maxOuput;
		this.maxStorage = maxStorage;
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, EnumFacing side) {
		return true;
	}

	@Override
	public EnumFacing getFacing() {
		return getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return entityPlayer.isSneaking();
	}

	@Override
	public void updateEntity() {
		if (inventory.getStackInSlot(0) != ItemStack.EMPTY) {
			ItemStack stack = inventory.getStackInSlot(0);
			if (!(stack.getItem() instanceof IEnergyItemInfo)) {
				return;
			}
			IEnergyItemInfo item = (IEnergyItemInfo) inventory.getStackInSlot(0).getItem();
			if (PoweredItem.getEnergy(stack) != PoweredItem.getMaxPower(stack)) {
				if (canUseEnergy(item.getMaxTransfer(stack))) {
					useEnergy(item.getMaxTransfer(stack));
					PoweredItem.setEnergy(PoweredItem.getEnergy(stack) + item.getMaxTransfer(stack), stack);
				}
			}
		}
		if (inventory.getStackInSlot(1) != ItemStack.EMPTY) {
			ItemStack stack = inventory.getStackInSlot(1);
			if (!(stack.getItem() instanceof IEnergyItemInfo)) {
				return;
			}
			IEnergyItemInfo item = (IEnergyItemInfo) stack.getItem();
			if (item.canProvideEnergy(stack)) {
				if (getEnergy() != getMaxPower() && PoweredItem.getEnergy(stack) > 0) {
					addEnergy(item.getMaxTransfer(stack));
					PoweredItem.setEnergy(PoweredItem.getEnergy(stack) - item.getMaxTransfer(stack), stack);
				}
			}
		}
	}

	@Override
	public void setFacing(EnumFacing enumFacing) {
		world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockEnergyStorage.FACING, enumFacing));
	}

	@Override
	public float getWrenchDropRate() {
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(wrenchDrop);
	}

	@Override
	public double getBaseMaxPower() {
		return maxStorage;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction) {
		return getFacing() != direction;
	}

	@Override
	public EnumFacing getFacingEnum() {
		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof BlockEnergyStorage) {
			return ((BlockEnergyStorage) block).getFacing(world.getBlockState(pos));
		}
		return null;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction) {
		return getFacing() == direction;
	}

	@Override
	public double getBaseMaxOutput() {
		return maxOutput;
	}

	@Override
	public double getBaseMaxInput() {
		return maxInput;
	}

	@Override
	public EnumPowerTier getBaseTier() {
		return tier;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] { 0, 1 };
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return true;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return true;
	}

}
