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

package techreborn.tiles.cable;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import reborncore.common.RebornCoreConfig;
import techreborn.blocks.cable.BlockCable;
import techreborn.blocks.cable.EnumCableType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by modmuss50 on 19/05/2017.
 */
public class TileCable extends TileEntity implements ITickable, IEnergyStorage {
	public int power = 0;

	List<TileCable> sentTo = new ArrayList<>();

	@Override
	public void update() {
		if (world.isRemote) {
			return;
		}
		sentTo.clear();
		for (EnumFacing face : EnumFacing.VALUES) {
			BlockPos offPos = getPos().offset(face);
			TileEntity tile = getWorld().getTileEntity(offPos);
			if (tile == null) {
				continue;
			}

			if (tile.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite())) {
				if(tile instanceof TileCable){
					if (((TileCable) tile).sentTo.contains(this)){
						continue;
					}
				}
				IEnergyStorage energy = tile.getCapability(CapabilityEnergy.ENERGY, face.getOpposite());
				if (energy.canReceive()) {
					int move = energy.receiveEnergy(Math.min(getCableType().transferRate, power), false);
					if (move != 0 && power >= move) {
						power -= move;
						if(tile instanceof TileCable){
							sentTo.add((TileCable) tile);
						}
					}
				}
			}
		}
	}

	private EnumCableType getCableType() {
		//Todo cache this
		return world.getBlockState(pos).getValue(BlockCable.TYPE);
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		if (!canReceive()) {
			return 0;
		}

		int energyReceived = Math.min(getMaxEnergyStored() - power, Math.min(getCableType().transferRate * RebornCoreConfig.euPerFU, maxReceive));
		if (!simulate)
			power += energyReceived;
		return energyReceived;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		if (!canExtract()) {
			return 0;
		}

		int energyExtracted = Math.min(power, Math.min(getCableType().transferRate * RebornCoreConfig.euPerFU, maxExtract));
		if (!simulate)
			power -= energyExtracted;
		return energyExtracted;
	}

	@Override
	public int getEnergyStored() {
		return power;
	}

	@Override
	public int getMaxEnergyStored() {
		return getCableType().transferRate;
	}

	@Override
	public boolean canExtract() {
		return true;
	}

	@Override
	public boolean canReceive() {
		return true;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return (T) this;
		}
		return super.getCapability(capability, facing);
	}
}
