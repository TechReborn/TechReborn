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

package techreborn.tiles.cable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import reborncore.api.IListInfoProvider;
import reborncore.common.RebornCoreConfig;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.StringUtils;
import techreborn.blocks.cable.BlockCable;
import techreborn.blocks.cable.EnumCableType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by modmuss50 on 19/05/2017.
 */
@SuppressWarnings("deprecation")
public class TileCable extends TileEntity implements ITickable, IEnergyStorage, IListInfoProvider {
	public int power = 0;
	private int transferRate = 0;
	private EnumCableType cableType = null;
	private ArrayList<EnumFacing> sendingFace = new ArrayList<EnumFacing>();
	
	private EnumCableType getCableType() {
		return world.getBlockState(pos).getValue(BlockCable.TYPE);
	}
	
	public boolean canReceiveFromFace(EnumFacing face) {
		if (sendingFace.contains(face)) {
			return false;
		}
		return true;
	}
	
	// TileEntity
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return (T) this;
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
    public NBTTagCompound getUpdateTag() {
        // getUpdateTag() is called whenever the chunkdata is sent to the
        // client. In contrast getUpdatePacket() is called when the tile entity
        // itself wants to sync to the client. In many cases you want to send
        // over the same information in getUpdateTag() as in getUpdatePacket().
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        // Prepare a packet for syncing our TE to the client. Since we only have to sync the stack
        // and that's all we have we just write our entire NBT here. If you have a complex
        // tile entity that doesn't need to have all information on the client you can write
        // a more optimal NBT here.
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        // Here we get the packet from the server and read it into our client side tile entity
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("TileCable")) {
            power = compound.getCompoundTag("TileCable").getInteger("power");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (power > 0) {
        	NBTTagCompound data = new NBTTagCompound();
    		data.setInteger("power", getEnergyStored());
    		compound.setTag("TileCable", data);
        }
        return compound;
    }
	
	// ITickable
	@Override
	public void update() {
		if (world.isRemote) {
			return;
		}
		
		if (this.cableType == null ){
			this.cableType = getCableType();
			this.transferRate = this.cableType.transferRate * RebornCoreConfig.euPerFU; 
		}
		
		ArrayList<IEnergyStorage> acceptors = new ArrayList<IEnergyStorage>();
		ArrayList<TileCable> cables = new ArrayList<TileCable>();
		
		for (EnumFacing face : EnumFacing.VALUES) {
			TileEntity tile = world.getTileEntity(pos.offset(face));

			if (tile == null) {
				continue;
			} else if (tile instanceof TileCable) {
				TileCable cable = (TileCable) tile;
				if (power > cable.power && cable.canReceiveFromFace(face.getOpposite())) {
					cables.add(cable);
					sendingFace.add(face);
				}
			} else if (tile.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite())) {
				IEnergyStorage energyTile = tile.getCapability(CapabilityEnergy.ENERGY, face.getOpposite());
				if (energyTile != null && energyTile.canReceive()){
					acceptors.add(energyTile);
				}
			}
		}
		
		if (cables.size() > 0){
			int drain  = Math.min(power, transferRate);
			int energyShare = drain / cables.size();
			int remainingEnergy = drain;
			
			if (energyShare > 0) {
				for (TileCable cable : cables) {
					// Push energy to connected cables
					int move = cable.receiveEnergy(Math.min(energyShare, remainingEnergy), false);
					if (move > 0) {
						remainingEnergy -= move;
					}
				}
				extractEnergy(drain - remainingEnergy, false);
			}
		}
		
		if (acceptors.size() > 0){
			int drain = Math.min(power, transferRate);
			int energyShare = drain / acceptors.size();
			int remainingEnergy = drain;
			
			if (energyShare > 0) {
				for (IEnergyStorage tile : acceptors){
					// Push energy to connected tile acceptors
					int move = tile.receiveEnergy(Math.min(energyShare, remainingEnergy), false);
					if (move > 0) {
						remainingEnergy -= move;
					}
				}
				extractEnergy(drain - remainingEnergy, false);
			}
		}
	}

	// IEnergyStorage
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		if (!canReceive()) {
			return 0;
		}

		int energyReceived = Math.min(getMaxEnergyStored() - getEnergyStored(), Math.min(transferRate, maxReceive));
		if (!simulate)
			power += energyReceived;
		return energyReceived;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		if (!canExtract()) {
			return 0;
		}

		int energyExtracted = Math.min(getEnergyStored(), Math.min(transferRate, maxExtract));
		if (!simulate)
			power -= energyExtracted;
		return energyExtracted;
	}

	@Override
	public int getEnergyStored() {
		return this.power;
	}

	@Override
	public int getMaxEnergyStored() {
		return this.transferRate * 6;
	}

	@Override
	public boolean canExtract() {
		return true;
	}

	@Override
	public boolean canReceive() {
		return true;
	}

    // IListInfoProvider
	@Override
	public void addInfo(List<String> info, boolean isRealTile) {
		if (isRealTile) {
			info.add(TextFormatting.GRAY + I18n.translateToLocal("techreborn.tooltip.transferRate") + ": "
					+ TextFormatting.GOLD
					+ PowerSystem.getLocaliszedPowerFormatted(this.transferRate / RebornCoreConfig.euPerFU) + "/t");
			info.add(TextFormatting.GRAY + I18n.translateToLocal("techreborn.tooltip.tier") + ": "
					+ TextFormatting.GOLD + StringUtils.toFirstCapitalAllLowercase(this.cableType.tier.toString()));
		}
	}
}
