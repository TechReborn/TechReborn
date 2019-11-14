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

package techreborn.blockentity.cable;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.StringUtils;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;
import techreborn.blocks.cable.BlockCable;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by modmuss50 on 19/05/2017.
 */

public class CableBlockEntity extends BlockEntity
	implements Tickable, IListInfoProvider, IToolDrop, EnergyStorage {
	
	private double energy = 0;
	private int transferRate = 0;
	private TRContent.Cables cableType = null;
	private ArrayList<Direction> sendingFace = new ArrayList<Direction>();
	int ticksSinceLastChange = 0;

	public CableBlockEntity() {
		super(TRBlockEntities.CABLE);
	}

	private TRContent.Cables getCableType() {
		Block block = world.getBlockState(pos).getBlock();
		if(block instanceof BlockCable){
			return ((BlockCable) block).type;
		}
		//Something has gone wrong if this happens
		return TRContent.Cables.COPPER;
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
    public void fromTag(CompoundTag compound) {
        super.fromTag(compound);
        if (compound.contains("CableBlockEntity")) {
            energy = compound.getCompound("CableBlockEntity").getInt("power");
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag compound) {
        super.toTag(compound);
        if (energy > 0) {
        	CompoundTag data = new CompoundTag();
    		compound.put("CableBlockEntity", data);
        }
        return compound;
    }
	
	// ITickable
	@Override
	public void tick() {
		if (world.isClient) {
			return;
		}

		if (cableType == null) {
			cableType = getCableType();
			transferRate = cableType.transferRate;
		}

		ticksSinceLastChange++;
		if (ticksSinceLastChange >= 20) {
			sendingFace.clear();
			ticksSinceLastChange = 0;
		}

		ArrayList<BlockEntity> acceptors = new ArrayList<BlockEntity>();
		for (Direction face : Direction.values()) {
			BlockEntity blockEntity = world.getBlockEntity(pos.offset(face));

			if (blockEntity == null) {
				continue;
			} else if (Energy.valid(blockEntity)) {
				if (blockEntity instanceof CableBlockEntity && energy <= Energy.of(blockEntity).side(EnergySide.fromMinecraft(face)).getEnergy()) {
					continue;
				}
				acceptors.add(blockEntity);
				if (!sendingFace.contains(face)) {
					sendingFace.add(face);
				}
			}
		}

		if (acceptors.size() == 0) {
			return;
		}
		Collections.shuffle(acceptors);
        acceptors.forEach(blockEntity -> Energy.of(this)
	        .into(Energy.of(blockEntity))
	        .move());
	}

    // IListInfoProvider
	@Override
	public void addInfo(List<Text> info, boolean isReal, boolean hasData) {
		if (isReal) {
			info.add(new LiteralText(Formatting.GRAY + StringUtils.t("techreborn.tooltip.transferRate") + ": "
				+ Formatting.GOLD
				+ PowerSystem.getLocaliszedPowerFormatted(transferRate) + "/t"));
			info.add(new LiteralText(Formatting.GRAY + StringUtils.t("techreborn.tooltip.tier") + ": "
				+ Formatting.GOLD + StringUtils.toFirstCapitalAllLowercase(cableType.tier.toString())));
		}
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity playerIn) {
		return new ItemStack(getCableType().block);
	}


	public double getEnergy() {
		return getStored(EnergySide.UNKNOWN);
	}

	public void setEnergy(double energy) {
		setStored(energy);
	}

	public double useEnergy(double energyOut, boolean simulate) {
		if (energyOut > energy) {
			energyOut = energy;
		}
		if (!simulate) {
			setEnergy(energy - energyOut);
		}
		return energyOut;
	}

	public boolean canAcceptEnergy(Direction direction) {
		if (sendingFace.contains(direction)) {
			return false;
		}
		return getMaxStoredPower() != getEnergy();
	}

	@Override
	public double getMaxInput(EnergySide side) {
		return transferRate;
	}

	@Override
	public double getMaxOutput(EnergySide side) {
		return transferRate;
	}

	@Override
	public double getMaxStoredPower() {
		return transferRate * 4;
	}

	@Override
	public EnergyTier getTier() {
		return PowerAcceptorBlockEntity.getTier(cableType.transferRate);
	}

	@Override
	public double getStored(EnergySide face) {
		return energy;
	}

	@Override
	public void setStored(double amount) {
		this.energy = amount;
	}
}
