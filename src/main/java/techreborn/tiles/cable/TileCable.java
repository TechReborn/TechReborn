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

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.common.RebornCoreConfig;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.StringUtils;
import techreborn.blocks.cable.BlockCable;
import techreborn.blocks.cable.EnumCableType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by modmuss50 on 19/05/2017.
 */

public class TileCable extends TileEntity implements ITickable, IEnergyStorage, IListInfoProvider, IToolDrop, ICable {
	// Fields >>
	public int power = 0;
	private int transferRate = 0;
	private EnumCableType cableType = null;
	private ArrayList<EnumFacing> sendingFace = new ArrayList<EnumFacing>();
	int ticksSinceLastChange = 0;
	protected boolean checkedConnections = false;
	private byte connectivity = 0;
	// << Fields

	//MC calls this during world load. Keep it, please.
	public TileCable() {
		super();
	}
	
	public TileCable(EnumCableType cableType) {
		this.cableType = cableType;
		this.transferRate = cableType.transferRate * RebornCoreConfig.euPerFU;
	}

	private void updateCableType() {
		if (cableType == null) {
			cableType = world.getBlockState(pos).getValue(BlockCable.TYPE);
			transferRate = cableType.transferRate * RebornCoreConfig.euPerFU;
		}
		return;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		connectivity = compound.getByte("connectivity");
		if (compound.hasKey("TileCable")) {
			power = compound.getCompoundTag("TileCable").getInteger("power");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setByte("connectivity", connectivity);
		if (power > 0) {
			NBTTagCompound data = new NBTTagCompound();
			data.setInteger("power", getEnergyStored());
			compound.setTag("TileCable", data);
		}
		return compound;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		notifyBlockUpdate();
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}
	
	public boolean canReceiveFromFace(EnumFacing face) {
		if (sendingFace.contains(face)) {
			return false;
		}
		return canReceive();
	}
		
	// Capabilities >>
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
			return CapabilityEnergy.ENERGY.cast(this);
		}
		return super.getCapability(capability, facing);
	}
	// << Capabilities

	// Networking >>
	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		writeToNBT(nbtTag);
		return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
		notifyBlockUpdate();
	}

	private void notifyBlockUpdate() {
		final IBlockState state = world.getBlockState(getPos());
		world.notifyBlockUpdate(getPos(), state, state, 3);
	}
	// << Networking

	// ICable >>
	@Override
	public void onNeighborChanged(Block neighbor, BlockPos neighborPos) {
		if (!world.isRemote) updateConnectivity();
	}

	@Override
	public void updateConnectivity() {
		World world = getWorld();
		byte newConnectivity = 0;
		int mask = 1;

		for (EnumFacing facing : EnumFacing.values()) {
			TileEntity tileEntity = world.getTileEntity(pos.offset(facing));
			if (tileEntity != null) {
				if (tileEntity.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) newConnectivity |= mask;
			}

			mask *= 2;
		}

		if (connectivity != newConnectivity) {
			connectivity = newConnectivity;
			markDirty();
		}
	}

	@Override
	public byte getConnectivity() {
		return connectivity;
	}
	// << ICable


	
	// ITickable >>
	@Override
	public void update() {
		if (world.isRemote) return;

		if (cableType == null) updateCableType();

		if (!checkedConnections) {
			updateConnectivity();
			checkedConnections = true;
		}
		
		ticksSinceLastChange++;
		if (ticksSinceLastChange >= 10) {
			sendingFace.clear();
			ticksSinceLastChange = 0;		
		}
		
		if (!canExtract()) return;

		ArrayList<IEnergyStorage> acceptors = new ArrayList<>();
		for (EnumFacing face : EnumFacing.VALUES) {
			TileEntity tile = world.getTileEntity(pos.offset(face));

			if (tile == null) {
				continue;
			} else if (tile instanceof TileCable) {
				TileCable cable = (TileCable) tile;
				if (power > cable.power && cable.canReceiveFromFace(face.getOpposite())) {
					acceptors.add((IEnergyStorage) tile);
					if (!sendingFace.contains(face)) sendingFace.add(face);
				}
			} else if (tile.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite())) {
				IEnergyStorage energyTile = tile.getCapability(CapabilityEnergy.ENERGY, face.getOpposite());
				if (energyTile != null && energyTile.canReceive()) acceptors.add(energyTile);
			}
		}

		Collections.shuffle(acceptors);
		if (!acceptors.isEmpty()) {
			for (IEnergyStorage tile : acceptors) {
				int drain = Math.min(power, transferRate);
				if (drain > 0 && tile.receiveEnergy(drain, true) > 0) {
					int move = tile.receiveEnergy(drain, false);
					extractEnergy(move, false);
				}
			}
		}
	}
	// << ITickable

	// IEnergyStorage >>
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		if (!canReceive()) return 0;

		int energyReceived = Math.min(getMaxEnergyStored() - getEnergyStored(), Math.min(transferRate, maxReceive));
		if (!simulate) power += energyReceived;

		return energyReceived;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		if (!canExtract()) return 0;

		int energyExtracted = Math.min(getEnergyStored(), Math.min(transferRate, maxExtract));
		if (!simulate) power -= energyExtracted;

		return energyExtracted;
	}

	@Override
	public int getEnergyStored() {
		return power;
	}

	@Override
	public int getMaxEnergyStored() {
		return transferRate * 5;
	}

	@Override
	public boolean canExtract() {
		return getEnergyStored() != 0;
	}

	@Override
	public boolean canReceive() {
		return getMaxEnergyStored() != getEnergyStored();
	}
	// << IEnergyStorage

    // IListInfoProvider >>
	@Override
	public void addInfo(List<String> info, boolean isRealTile) {
			info.add(TextFormatting.GRAY + StringUtils.t("techreborn.tooltip.transferRate") + ": "
					+ TextFormatting.GOLD
					+ PowerSystem.getLocaliszedPowerFormatted(transferRate / RebornCoreConfig.euPerFU) + "/t");
			info.add(TextFormatting.GRAY + StringUtils.t("techreborn.tooltip.tier") + ": "
					+ TextFormatting.GOLD + StringUtils.toFirstCapitalAllLowercase(cableType.tier.toString()));
	}
	// << IListInfoProvider

	// IToolDrop >>
	@Override
	public ItemStack getToolDrop(EntityPlayer playerIn) {
		return cableType.getStack();
	}
	// << IToolDrop
}
