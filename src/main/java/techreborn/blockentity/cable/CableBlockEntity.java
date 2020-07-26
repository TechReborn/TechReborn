/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.tuple.Pair;
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.common.network.ClientBoundPackets;
import reborncore.common.network.NetworkManager;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by modmuss50 on 19/05/2017.
 */

public class CableBlockEntity extends BlockEntity
	implements Tickable, IListInfoProvider, IToolDrop, EnergyStorage {
	
	private double energy = 0;
	private TRContent.Cables cableType = null;
	private ArrayList<Direction> sendingFace = new ArrayList<>();
	private BlockState cover = null;

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
    public void fromTag(BlockState blockState, CompoundTag compound) {
		super.fromTag(blockState, compound);
		if (compound.contains("energy")) {
			energy = compound.getDouble("energy");
		}
		if (compound.contains("cover")) {
			cover = NbtHelper.toBlockState(compound.getCompound("cover"));
		} else {
			cover = null;
		}
	}

    @Override
    public CompoundTag toTag(CompoundTag compound) {
		super.toTag(compound);
		compound.putDouble("energy", energy);
		if (cover != null) {
			compound.put("cover", NbtHelper.fromBlockState(cover));
		}
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
		ArrayList<CableBlockEntity> cables = new ArrayList<>();

		for (Direction face : Direction.values()) {
			BlockEntity blockEntity = world.getBlockEntity(pos.offset(face));

			if (blockEntity != null && Energy.valid(blockEntity)) {
				if (blockEntity instanceof CableBlockEntity ) {
					CableBlockEntity cableBlockEntity = (CableBlockEntity)blockEntity;

					if(cableBlockEntity.getTier() == this.getTier()) {
						// Only need ones with energy stores less than ours
						if (cableBlockEntity.getEnergy() < this.getEnergy()) {
							cables.add(cableBlockEntity);
						}

						continue;
					}else if(energy <= Energy.of(blockEntity).side(face).getEnergy()){
						continue;
					}
				}

				if(Energy.of(blockEntity).side(face.getOpposite()).getMaxInput() > 0){
					acceptors.add(Pair.of(blockEntity, face));
					if (!sendingFace.contains(face)) {
						sendingFace.add(face);
					}
				}
			}
		}

		if (!acceptors.isEmpty()) {
			Collections.shuffle(acceptors);

			acceptors.forEach(pair ->
				Energy.of(this)
						.into(Energy.of(pair.getLeft()).side(pair.getRight().getOpposite()))
						.move()
			);
		}


        // Distribute energy between cables (TODO DIRTY FIX, until we game network cables)

		if(!cables.isEmpty()) {

			cables.add(this);
			double energyTotal = cables.stream().mapToDouble(CableBlockEntity::getEnergy).sum();
			double energyPer = energyTotal / cables.size();

			cables.forEach(cableBlockEntity -> cableBlockEntity.setEnergy(energyPer));
		}
	}

    // IListInfoProvider
	@Override
	public void addInfo(List<Text> info, boolean isReal, boolean hasData) {
		info.add(
				new TranslatableText("techreborn.tooltip.transferRate")
				.formatted(Formatting.GRAY)
				.append(": ")
				.append(PowerSystem.getLocaliszedPowerFormatted(getCableType().transferRate))
				.formatted(Formatting.GOLD)
				.append("/t")
		);

		info.add(
			new TranslatableText("techreborn.tooltip.tier")
			.formatted(Formatting.GRAY)
			.append(": ")
			.append(
					new LiteralText(StringUtils.toFirstCapitalAllLowercase(getCableType().tier.toString()))
					.formatted(Formatting.GOLD)
			)
		);

		if (!getCableType().canKill) {
			info.add(new TranslatableText("techreborn.tooltip.cable.can_cover").formatted(Formatting.GRAY));
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

	public double addEnergy(double energyIn) {
		if(this.isFull()){
			return energyIn;
		}

		double maxStore = this.getMaxStoredPower();

		if (energyIn + energy <= maxStore) {
			setEnergy(energyIn + energy);
			return 0;
		}

		// Can't fit energy fully, add part of it
		double amountCanAdd = (maxStore - energy);
		setEnergy(amountCanAdd + energy);

		return energyIn - amountCanAdd;
	}

	public boolean canAcceptEnergy(EnergySide direction) {
		if (sendingFace.contains(direction)) {
			return false;
		}
		return getMaxStoredPower() != getEnergy();
	}

	public boolean isFull(){
		return getMaxStoredPower() == getEnergy();
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

	public BlockState getCover() {
		return cover;
	}

	public void setCover(BlockState cover) {
		this.cover = cover;
		if (!world.isClient) {
			NetworkManager.sendToTracking(ClientBoundPackets.createCustomDescriptionPacket(this), this);
		}
	}

}
