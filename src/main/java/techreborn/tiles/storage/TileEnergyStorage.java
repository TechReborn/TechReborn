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
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import reborncore.api.IToolDrop;
import reborncore.api.power.EnumPowerTier;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.api.tile.IInventoryProvider;
import reborncore.common.RebornCoreConfig;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.Inventory;
import techreborn.blocks.storage.BlockEnergyStorage;
import techreborn.compat.CompatManager;
import techreborn.utils.IC2ItemCharger;

/**
 * Created by Rushmead
 */
public class TileEnergyStorage extends TilePowerAcceptor 
		implements IToolDrop, IInventoryProvider {

	public Inventory inventory;
	public String name;
	public Block wrenchDrop;
	public EnumPowerTier tier;
	public int maxInput;
	public int maxOutput;
	public int maxStorage;

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
	public void update() {
		super.update();
		if (!inventory.getStackInSlot(0).isEmpty()) {
			ItemStack stack = inventory.getStackInSlot(0);
			if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
				IEnergyStorage powerItem = stack.getCapability(CapabilityEnergy.ENERGY, null);
                int maxTransfer;
				if(stack.getItem() instanceof IEnergyItemInfo){
				    maxTransfer = (int) ((IEnergyItemInfo) stack.getItem()).getMaxTransfer(stack);
                } else {
                    maxTransfer = maxOutput;
                }
                int maxReceive = Math.min((powerItem.getMaxEnergyStored() - powerItem.getEnergyStored()), maxTransfer);
				double maxUse = Math.min((double) (maxReceive / RebornCoreConfig.euPerFU), getMaxOutput());
				if (getEnergy() >= 0.0 && maxReceive > 0) {
					powerItem.receiveEnergy((int) useEnergy(maxUse) * RebornCoreConfig.euPerFU, false);
				}
			} else if (CompatManager.isIC2Loaded) {
				IC2ItemCharger.chargeIc2Item(this, stack);
			}
		}
		if (!inventory.getStackInSlot(1).isEmpty()) {
			charge(1);
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

	// TileLegacyMachineBase
	@Override
	public void setFacing(EnumFacing enumFacing) {
		world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockEnergyStorage.FACING, enumFacing));
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
	public boolean canBeUpgraded() {
		return false;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(EntityPlayer entityPlayer) {
		return new ItemStack(wrenchDrop);
	}
	
	// IInventoryProvider
	@Override
	public Inventory getInventory() {
		return inventory;
	}
}
