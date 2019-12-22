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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import org.apache.commons.lang3.tuple.Pair;
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.StringUtils;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;
import techreborn.blocks.cable.CableBlock;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

/**
 * Created by modmuss50 on 19/05/2017.
 */

public class CableBlockEntity extends BlockEntity
	implements Tickable, IListInfoProvider, IToolDrop, EnergyStorage {
	
	private double energy = 0;
	private TRContent.Cables cableType = null;
	private ArrayList<EnergySide> sendingFace = new ArrayList<>();

	public CableBlockEntity() {
		super(TRBlockEntities.CABLE);
	}

	public CableBlockEntity(TRContent.Cables type) {
		super(TRBlockEntities.CABLE);
		this.cableType = type;
	}

	private TRContent.Cables getCableType() {
		if (cableType != null) {
			return cableType;
		}
		if (world == null) {
			return TRContent.Cables.COPPER;
		}
		Block block = world.getBlockState(pos).getBlock();
		if(block instanceof CableBlock){
			return ((CableBlock) block).type;
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
        if (compound.contains("energy")) {
            energy = compound.getDouble("energy");
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag compound) {
        super.toTag(compound);
        compound.putDouble("energy", energy);
        return compound;
    }

	@Override
	public void tick() {
		if (world.isClient) {
			return;
		}

		sendingFace.clear();

		if(getEnergy() == 0){
			return;
		}

		ArrayList<Pair<BlockEntity, Direction>> acceptors = new ArrayList<>();
		for (Direction face : Direction.values()) {
			EnergySide side = EnergySide.fromMinecraft(face);
			BlockEntity blockEntity = world.getBlockEntity(pos.offset(face));

			if (blockEntity != null && Energy.valid(blockEntity)) {
				if (blockEntity instanceof CableBlockEntity && energy <= Energy.of(blockEntity).side(EnergySide.fromMinecraft(face)).getEnergy()) {
					continue;
				}
				if(Energy.of(blockEntity).side(EnergySide.fromMinecraft(face.getOpposite())).getMaxInput() > 0){
					acceptors.add(Pair.of(blockEntity, face));
					if (!sendingFace.contains(side)) {
						sendingFace.add(side);
					}
				}
			}
		}

		if (acceptors.isEmpty()) {
			return;
		}
		Collections.shuffle(acceptors);

        acceptors.forEach(pair -> {
	        Energy.of(this)
		        .into(Energy.of(pair.getLeft()).side(EnergySide.fromMinecraft(pair.getRight().getOpposite())))
		        .move();
        });
	}

    // IListInfoProvider
	@Override
	public void addInfo(List<Text> info, boolean isReal, boolean hasData) {
			info.add(new LiteralText(Formatting.GRAY + StringUtils.t("techreborn.tooltip.transferRate") + ": "
				+ Formatting.GOLD
				+ PowerSystem.getLocaliszedPowerFormatted(getCableType().transferRate) + "/t"));
			info.add(new LiteralText(Formatting.GRAY + StringUtils.t("techreborn.tooltip.tier") + ": "
				+ Formatting.GOLD + StringUtils.toFirstCapitalAllLowercase(getCableType().tier.toString())));
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

	public boolean canAcceptEnergy(EnergySide direction) {
		if (sendingFace.contains(direction)) {
			return false;
		}
		return getMaxStoredPower() != getEnergy();
	}

	@Override
	public double getMaxInput(EnergySide side) {
		if(!canAcceptEnergy(side)) {
			return 0;
		}
		return getCableType().transferRate;
	}

	@Override
	public double getMaxOutput(EnergySide side) {
		return getCableType().transferRate;
	}

	@Override
	public double getMaxStoredPower() {
		return getCableType().transferRate * 4;
	}

	@Override
	public EnergyTier getTier() {
		return PowerAcceptorBlockEntity.getTier(getCableType().transferRate);
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
