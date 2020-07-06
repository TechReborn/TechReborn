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
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.common.network.ClientBoundPackets;
import reborncore.common.network.NetworkManager;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.StringUtils;
import team.reborn.energy.Energy;
import techreborn.TechReborn;
import techreborn.blocks.cable.CableBlock;
import techreborn.enet.ElectricNetwork;
import techreborn.enet.ElectricNetworkManager;
import techreborn.enet.PowerAcceptorBlockEntityFace;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by modmuss50 on 19/05/2017.
 */

public class CableBlockEntity extends BlockEntity
		implements Tickable, IListInfoProvider, IToolDrop {

	private ElectricNetwork electricNetwork;
	private TRContent.Cables cableType = null;
	private BlockState cover = null;

	public CableBlockEntity() {
		super(TRBlockEntities.CABLE);
	}

	public CableBlockEntity(TRContent.Cables type) {
		super(TRBlockEntities.CABLE);
		this.cableType = type;
	}

	public TRContent.Cables getCableType() {
		if (cableType != null) {
			return cableType;
		}
		if (world == null) {
			return TRContent.Cables.COPPER;
		}
		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof CableBlock) {
			return ((CableBlock) block).type;
		}
		//Something has gone wrong if this happens
		return TRContent.Cables.COPPER;
	}

	public ElectricNetwork getElectricNetwork() {
		return electricNetwork;
	}

	public boolean isEnergized() {
		if (electricNetwork == null) {
			return false;
		}

		return electricNetwork.isEnergized();
	}

	public void setElectricNetwork(ElectricNetwork value)
	{
		if (electricNetwork != null) {
			electricNetwork.removeBlockEntity(this);
		}

		if (value != null) {
			electricNetwork = value;
			electricNetwork.addBlockEntity(this);
		}
	}

	@Override
	public void markRemoved() {
		if (electricNetwork != null) {
			TechReborn.LOGGER.debug("Removing block {} from ENet {}", this.getPos().asLong(), electricNetwork);
			electricNetwork.removeBlockEntity(this);
			electricNetwork = null;
		}

		super.markRemoved();
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
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);

		if (tag.contains("cover")) {
			cover = NbtHelper.toBlockState(tag.getCompound("cover"));
		} else {
			cover = null;
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag compound) {
		super.toTag(compound);

		if (cover != null) {
			compound.put("cover", NbtHelper.fromBlockState(cover));
		}
		return compound;
	}

	@Override
	public void tick() {
		if (world == null || world.isClient) {
			return;
		}

		if (electricNetwork == null) {
			TechReborn.LOGGER.debug("Block Entity has no network in tick() -- assigning a network");
			this.assignNetwork();
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

	public BlockState getCover() {
		return cover;
	}

	public void setCover(BlockState cover) {
		this.cover = cover;

		if (world != null && !world.isClient) {
			NetworkManager.sendToTracking(ClientBoundPackets.createCustomDescriptionPacket(this), this);
		}
	}

	public boolean canConnectTo(CableBlockEntity other) {
		return other != null && other.getCableType().tier == getCableType().tier;
	}

	public List<PowerAcceptorBlockEntityFace> getConnectedPowerAcceptors() {
		ArrayList<PowerAcceptorBlockEntityFace> result = new ArrayList<>();

		for (Direction face : Direction.values()) {
			BlockEntity blockEntity = world.getBlockEntity(pos.offset(face));

			if (blockEntity != null && Energy.valid(blockEntity) && blockEntity instanceof PowerAcceptorBlockEntity) {
				result.add(new PowerAcceptorBlockEntityFace((PowerAcceptorBlockEntity) blockEntity, face.getOpposite()));
			}
		}

		return result;
	}

	private void assignNetwork() {
		if (!hasWorld()) {
			TechReborn.LOGGER.debug("Attempting to assign an electric network for cable block entity but no World is available");
			return;
		}

		ElectricNetwork eligibleNetwork = null;

		for (Direction face : Direction.values()) {
			BlockEntity blockEntity = world.getBlockEntity(pos.offset(face));

			if (blockEntity instanceof CableBlockEntity && this.canConnectTo((CableBlockEntity) blockEntity)) {
				eligibleNetwork = ((CableBlockEntity) blockEntity).getElectricNetwork();
			}

			if (eligibleNetwork != null) {
				break;
			}
		}

		if (eligibleNetwork == null) {
			TechReborn.LOGGER.debug("Could not find network to join, creating new one");
			eligibleNetwork = ElectricNetworkManager.INSTANCE.newNetwork(getCableType().tier);
		} else {
			TechReborn.LOGGER.debug("Successfully found another network to join: {}", eligibleNetwork);
		}

		this.setElectricNetwork(eligibleNetwork);
	}

}
