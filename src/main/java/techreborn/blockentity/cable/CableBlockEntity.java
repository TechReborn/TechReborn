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
import reborncore.api.power.EnergyBlockEntity;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.StringUtils;
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
	implements Tickable, IListInfoProvider, IToolDrop, EnergyBlockEntity {
	
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
        if (compound.containsKey("CableBlockEntity")) {
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

		ArrayList<EnergyBlockEntity> acceptors = new ArrayList<EnergyBlockEntity>();
		for (Direction face : Direction.values()) {
			BlockEntity blockEntity = world.getBlockEntity(pos.offset(face));

			if (blockEntity == null) {
				continue;
			} else if (blockEntity instanceof EnergyBlockEntity) {
				EnergyBlockEntity acceptor = (EnergyBlockEntity) blockEntity;
				if (blockEntity instanceof CableBlockEntity && energy <= acceptor.getEnergy()) {
					continue;
				}
				if (!acceptor.canAcceptEnergy(face.getOpposite())) {
					continue;
				}
				acceptors.add(acceptor);
				if (!sendingFace.contains(face)) {
					sendingFace.add(face);
				}
			}
		}

		if (acceptors.size() == 0) {
			return;
		}
		Collections.shuffle(acceptors);
        acceptors.forEach(blockEntity -> {
            double drain = Math.min(energy, transferRate);
            if (drain > 0 && blockEntity.addEnergy(drain, true) > 0) {
                double move = blockEntity.addEnergy(drain, false);
                useEnergy(move, false);
            }
        });
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

	@Override
	public double getEnergy() {
		return energy;
	}

	@Override
	public void setEnergy(double energy) {
		this.energy = energy;
	}

	@Override
	public double getMaxPower() {
		return transferRate * 4;
	}

	@Override
	public boolean canAddEnergy(double energyIn) {
		return getEnergy() + energyIn <= getMaxPower();
	}

	@Override
	public double addEnergy(double energy) {
		return addEnergy(energy, false);
	}

	@Override
	public double addEnergy(double energyIn, boolean simulate) {
		double energyReceived = Math.min(getMaxPower(), Math.min(getMaxPower() - getEnergy(), energyIn));

		if (!simulate) {
			setEnergy(getEnergy() + energyReceived);
		}
		return energyReceived;
	}

	@Override
	public boolean canUseEnergy(double energy) {
		return this.energy >= energy;
	}

	@Override
	public double useEnergy(double energy) {
		return useEnergy(energy, false);
	}

	@Override
	public double useEnergy(double energyOut, boolean simulate) {
		if (energyOut > energy) {
			energyOut = energy;
		}
		if (!simulate) {
			setEnergy(energy - energyOut);
		}
		return energyOut;
	}

	@Override
	public boolean canAcceptEnergy(Direction direction) {
		if (sendingFace.contains(direction)) {
			return false;
		}
		return getMaxPower() != getEnergy();
	}

	@Override
	public boolean canProvideEnergy(Direction direction) {
		return true;
	}

	@Override
	public double getMaxOutput() {
		return transferRate;
	}

	@Override
	public double getMaxInput() {
		return transferRate;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.getTier(cableType.transferRate);
	}
}
