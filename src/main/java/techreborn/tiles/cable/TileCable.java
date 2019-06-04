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

import net.minecraft.ChatFormat;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;




import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.common.RebornCoreConfig;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.StringUtils;
import techreborn.blocks.cable.BlockCable;
import techreborn.init.TRContent;
import techreborn.init.TRTileEntities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by modmuss50 on 19/05/2017.
 */

public class TileCable extends BlockEntity 
	implements Tickable, IEnergyStorage, IListInfoProvider, IToolDrop {
	
	public int power = 0;
	private int transferRate = 0;
	private TRContent.Cables cableType = null;
	private ArrayList<Direction> sendingFace = new ArrayList<Direction>();
	int ticksSinceLastChange = 0;

	public TileCable() {
		super(TRTileEntities.CABLE);
	}

	private TRContent.Cables getCableType() {
		Block block = world.getBlockState(pos).getBlock();
		if(block instanceof BlockCable){
			return ((BlockCable) block).type;
		}
		//Something has gone wrong if this happens
		return TRContent.Cables.COPPER;
	}
	
	public boolean canReceiveFromFace(Direction face) {
		if (sendingFace.contains(face)) {
			return false;
		}
		return canReceive();
	}
	
	@Override
    public CompoundTag toInitialChunkDataTag() {
        return toTag(new CompoundTag());
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        CompoundTag nbtTag = new CompoundTag();
        toTag(nbtTag);
        return new BlockEntityUpdateS2CPacket(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(ClientConnection net, BlockEntityUpdateS2CPacket packet) {
        fromTag(packet.getCompoundTag());
    }

    @Override
    public void fromTag(CompoundTag compound) {
        super.fromTag(compound);
        if (compound.containsKey("TileCable")) {
            power = compound.getCompound("TileCable").getInt("power");
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag compound) {
        super.toTag(compound);
        if (power > 0) {
        	CompoundTag data = new CompoundTag();
    		data.putInt("power", getEnergyStored());
    		compound.put("TileCable", data);
        }
        return compound;
    }
	
	// ITickable
	@Override
	public void tick() {
		if (world.isClient) {
			return;
		}
		
		if (cableType == null ){
			cableType = getCableType();
			transferRate = cableType.transferRate * RebornCoreConfig.euPerFU; 
		}
		
		ticksSinceLastChange++;
		if (ticksSinceLastChange >= 10) {
			sendingFace.clear();
			ticksSinceLastChange = 0;		
		}
		
		if (!canExtract()) {
			return;
		}

		//TODO needs a full recode to not use a specific power net

//		ArrayList<IEnergyStorage> acceptors = new ArrayList<IEnergyStorage>();
//		for (Direction face : Direction.values()) {
//			BlockEntity tile = world.getBlockEntity(pos.offset(face));
//
//			if (tile == null) {
//				continue;
//			} else if (tile instanceof TileCable) {
//				TileCable cable = (TileCable) tile;
//				if (power > cable.power && cable.canReceiveFromFace(face.getOpposite())) {
//					acceptors.add((IEnergyStorage) tile);
//					if (!sendingFace.contains(face)) {
//						sendingFace.add(face);
//					}
//				}
//			} else if (tile.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).isPresent()) {
//				IEnergyStorage energyTile = tile.getCapability(CapabilityEnergy.ENERGY, face.getOpposite()).orElse(null);
//				if (energyTile != null && energyTile.canReceive()) {
//					acceptors.add(energyTile);
//				}
//			}
//		}
//
//		if (acceptors.size() > 0 ) {
//			for (IEnergyStorage tile : acceptors) {
//				int drain = Math.min(power, transferRate);
//				if (drain > 0 && tile.receiveEnergy(drain, true) > 0) {
//					int move = tile.receiveEnergy(drain, false);
//					extractEnergy(move, false);
//				}
//			}
//		}
	}

	// IEnergyStorage
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		if (!canReceive()) {
			return 0;
		}

		int energyReceived = Math.min(getMaxEnergyStored() - getEnergyStored(), Math.min(transferRate, maxReceive));
		if (!simulate) {
			power += energyReceived;
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		if (!canExtract()) {
			return 0;
		}

		int energyExtracted = Math.min(getEnergyStored(), Math.min(transferRate, maxExtract));
		if (!simulate) {
			power -= energyExtracted;
		}
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
		if (getEnergyStored() == 0 ) {
			return false;
		}
		return true;
	}

	@Override
	public boolean canReceive() {
		if (getMaxEnergyStored() == getEnergyStored()) {
			return false;
		}
		return true;
	}

    // IListInfoProvider
	@Override
	public void addInfo(List<Component> info, boolean isRealTile, boolean hasData) {
		if (isRealTile) {
			info.add(new TextComponent(ChatFormat.GRAY + StringUtils.t("techreborn.tooltip.transferRate") + ": "
				+ ChatFormat.GOLD
				+ PowerSystem.getLocaliszedPowerFormatted(transferRate / RebornCoreConfig.euPerFU) + "/t"));
			info.add(new TextComponent(ChatFormat.GRAY + StringUtils.t("techreborn.tooltip.tier") + ": "
				+ ChatFormat.GOLD + StringUtils.toFirstCapitalAllLowercase(cableType.tier.toString())));
		}
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity playerIn) {
		return new ItemStack(getCableType().block);
	}
}
