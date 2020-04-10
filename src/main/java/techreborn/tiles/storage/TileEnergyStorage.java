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

package techreborn.tiles.storage;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import reborncore.api.IToolDrop;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.powerSystem.ExternalPowerSystems;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.blocks.storage.BlockEnergyStorage;
import techreborn.tiles.IRedstoneHandler;

/**
 * @author Rushmead, estebes
 */
public abstract class TileEnergyStorage extends TilePowerAcceptor implements IToolDrop, IInventoryProvider, IRedstoneHandler {
	// Fields >>
	public Inventory inventory;
	public String name;
	public Block wrenchDrop;
	public EnumPowerTier tier;
	public int maxInput;
	public int maxOutput;
	public int maxStorage;

	// redstone related
	public byte redstoneMode = 0; // current redstone mode
	public static byte redstoneModes = 5; // number of redstone modes
	public int redstoneSignal = 0;
	// << Fields

	public TileEnergyStorage(String name, int invSize, Block wrenchDrop, EnumPowerTier tier, int maxInput, int maxOuput, int maxStorage) {
		super();
		inventory = new Inventory(invSize, "Tile" + name, 64, this);
		this.wrenchDrop = wrenchDrop;
		this.tier = tier;
		this.name = name;
		this.maxInput = maxInput;
		this.maxOutput = maxOuput;
		this.maxStorage = maxStorage;
	}

	// TilePowerAcceptor
	@Override
	public void readFromNBT(final NBTTagCompound tag) {
		super.readFromNBT(tag);

		this.redstoneMode = tag.getByte("redstoneMode");
		this.redstoneSignal = tag.getInteger("redstoneSignal");
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound tag) {
		super.writeToNBT(tag);

		tag.setByte("redstoneMode", this.redstoneMode);
		tag.setInteger("redstoneSignal", this.redstoneSignal);

		return tag;
	}

	@Override
	public void update() {
		super.update();

		if (world.isRemote) return;

		if (!inventory.getStackInSlot(0).isEmpty()) {
			ItemStack stack = inventory.getStackInSlot(0);

			if (ExternalPowerSystems.isPoweredItem(stack)) ExternalPowerSystems.chargeItem(this, stack);
		}

		if (!inventory.getStackInSlot(1).isEmpty()) charge(1);

		// handle redstone updates
		int newRedstoneSignal = shouldEmitRedstoneSignal() ? 15 : 0;
		if (redstoneSignal != newRedstoneSignal) {
			redstoneSignal = newRedstoneSignal;
			world.notifyNeighborsOfStateChange(pos, blockType, false);
		}
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

	// RebornMachineTile	
	@Override
	public boolean canBeUpgraded() {
		return false;
	}
	
	@Override
	public EnumFacing getFacing() {
		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof BlockEnergyStorage) {
			return ((BlockEnergyStorage) block).getFacing(world, pos);
		}
		return null;
	}
	
	// IToolDrop >>
	@Override
	public ItemStack getToolDrop(EntityPlayer entityPlayer) {
		return new ItemStack(wrenchDrop);
	}
	// << IToolDrop
	
	// IInventoryProvider
	@Override
	public Inventory getInventory() {
		return inventory;
	}

	// Redstone >>
	@Override
	public int getRedstoneLevel() {
		return redstoneSignal;
	}

	@Override
	public int getComparatorValue()  {
		return Math.min((int) (getEnergy() * 15 / getBaseMaxPower()), 15);
	}

	protected boolean shouldEmitRedstoneSignal() {
		switch (redstoneMode) {
			case 1: // emit if full
				return getEnergy() >= (getBaseMaxPower() - (maxOutput * 20));
			case 2: // emit if partially full
				return getEnergy() > maxOutput && getEnergy() < getBaseMaxPower() - maxOutput;
			case 3: // emit if partially full or empty
				return getEnergy() < getBaseMaxPower() - maxOutput;
			case 4: // emit if empty
				return getEnergy() < maxOutput;
			default: // do nothing
				return false;
		}
	}

	@Override
	public boolean canSetFacing(EnumFacing facing) {
		return true;
	}

	@Override
	public boolean setFacing(EnumFacing facing) {
		world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockEnergyStorage.FACING, facing));
		return true;
	}

	public int getRedstoneModeInt() {
		return redstoneMode & 0xff; // TODO: Add byteSuppliers to core
	}

	public void setRedstoneModeInt(int redstoneModeInt) {
		redstoneMode = (byte) redstoneModeInt;
	}
	// << Redstone
}
