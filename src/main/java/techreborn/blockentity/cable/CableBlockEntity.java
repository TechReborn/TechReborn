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

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiCache;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.common.network.NetworkManager;
import reborncore.common.network.clientbound.CustomDescriptionPayload;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.StringUtils;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleSidedEnergyContainer;
import techreborn.blocks.cable.CableBlock;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.ArrayList;
import java.util.List;

public class CableBlockEntity extends BlockEntity
	implements BlockEntityTicker<CableBlockEntity>, IListInfoProvider, IToolDrop {
	// Can't use SimpleEnergyStorage because the cable type is not available when the BE is constructed.
	final SimpleSidedEnergyContainer energyContainer = new SimpleSidedEnergyContainer() {
		@Override
		public long getCapacity() {
			return getCableType().transferRate * 4L;
		}

		@Override
		public long getMaxInsert(Direction side) {
			if (allowTransfer(side)) return getCableType().transferRate;
			else return 0;
		}

		@Override
		public long getMaxExtract(Direction side) {
			if (allowTransfer(side)) return getCableType().transferRate;
			else return 0;
		}
	};
	private TRContent.Cables cableType = null;
	@Nullable
	private BlockState cover = null;
	long lastTick = 0;
	// null means that it needs to be re-queried
	List<CableTarget> targets = null;
	/**
	 * Adjacent caches, used to quickly query adjacent cable block entities.
	 */
	@SuppressWarnings("unchecked")
	private final BlockApiCache<EnergyStorage, Direction>[] adjacentCaches = new BlockApiCache[6];
	/**
	 * Bitmask to prevent input or output into/from the cable when the cable already transferred in the target direction.
	 * This prevents double transfer rates, and back and forth between two cables.
	 */
	int blockedSides = 0;

	/**
	 * This is only used during the cable tick, whereas {@link #blockedSides} is used between ticks.
	 */
	boolean ioBlocked = false;

	public CableBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.CABLE, pos, state);
	}

	public CableBlockEntity(BlockPos pos, BlockState state, TRContent.Cables type) {
		super(TRBlockEntities.CABLE, pos, state);
		this.cableType = type;
	}

	TRContent.Cables getCableType() {
		if (cableType != null) {
			return cableType;
		}
		if (level == null) {
			return TRContent.Cables.COPPER;
		}
		Block block = level.getBlockState(worldPosition).getBlock();
		if (block instanceof CableBlock) {
			return ((CableBlock) block).type;
		}
		//Something has gone wrong if this happens
		return TRContent.Cables.COPPER;
	}

	private boolean allowTransfer(Direction side) {
		if (side == null) {
			return true;
		}

		return !ioBlocked && (blockedSides & (1 << side.ordinal())) == 0;
	}

	public EnergyStorage getSideEnergyStorage(@Nullable Direction side) {
		return energyContainer.getSideStorage(side);
	}

	public @Nullable BlockState getCover() {
		return cover;
	}

	public void setCover(BlockState cover) {
		this.cover = cover;
		if (level != null && !level.isClientSide) {
			NetworkManager.sendToTracking(new CustomDescriptionPayload(getBlockPos(), this.saveWithoutMetadata(level.registryAccess())), this);
		}
	}

	public long getEnergy() {
		return energyContainer.amount;
	}

	public void setEnergy(long energy) {
		energyContainer.amount = energy;
	}

	private BlockApiCache<EnergyStorage, Direction> getAdjacentCache(Direction direction) {
		if (adjacentCaches[direction.get3DDataValue()] == null) {
			adjacentCaches[direction.get3DDataValue()] = BlockApiCache.create(EnergyStorage.SIDED, (ServerLevel) level, worldPosition.relative(direction));
		}
		return adjacentCaches[direction.get3DDataValue()];
	}

	@Nullable
	BlockEntity getAdjacentBlockEntity(Direction direction) {
		return getAdjacentCache(direction).getBlockEntity();
	}

	void appendTargets(List<OfferedEnergyStorage> targetStorages) {
		ServerLevel serverWorld = (ServerLevel) level;
		if (serverWorld == null) {
			return;
		}

		// Update our targets if necessary.
		if (targets == null) {
			BlockState newBlockState = getBlockState();

			targets = new ArrayList<>();
			for (Direction direction : Direction.values()) {
				boolean foundSomething = false;

				BlockApiCache<EnergyStorage, Direction> adjCache = getAdjacentCache(direction);

				if (adjCache.getBlockEntity() instanceof CableBlockEntity adjCable) {
					if (adjCable.getCableType().transferRate == getCableType().transferRate) {
						// Make sure cables are not used as regular targets.
						foundSomething = true;
					}
				} else if (adjCache.find(direction.getOpposite()) != null) {
					foundSomething = true;
					targets.add(new CableTarget(direction, adjCache));
				}

				newBlockState = newBlockState.setValue(CableBlock.PROPERTY_MAP.get(direction), foundSomething);
			}

			serverWorld.setBlockAndUpdate(getBlockPos(), newBlockState);
		}

		// Fill the list.
		for (CableTarget target : targets) {
			EnergyStorage storage = target.find();

			if (storage == null) {
				// Schedule a rebuild next tick.
				// This is just a reference change, the iterator remains valid.
				targets = null;
			} else {
				targetStorages.add(new OfferedEnergyStorage(this, target.directionTo, storage));
			}
		}

		// Reset blocked sides.
		blockedSides = 0;
	}

	// BlockEntity
	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
		return saveWithoutMetadata(registryLookup);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		CompoundTag nbtTag = new CompoundTag();
		// writeNbt(nbtTag);
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void loadAdditional(ValueInput view) {
		super.loadAdditional(view);
		energyContainer.amount = view.getLongOr("energy", 0);
		cover = view.read("cover", BlockState.CODEC).orElse(null);
	}

	@Override
	public void saveAdditional(ValueOutput view) {
		super.saveAdditional(view);
		view.putLong("energy", energyContainer.amount);
		if (cover != null) {
			view.store("cover", BlockState.CODEC, cover);
		}
	}

	public void neighborUpdate() {
		targets = null;
	}

	// BlockEntityTicker
	@Override
	public void tick(Level world, BlockPos pos, BlockState state, CableBlockEntity blockEntity2) {
		if (world == null || world.isClientSide) {
			return;
		}

		CableTickManager.handleCableTick(this);
	}

	// IListInfoProvider
	@Override
	public void addInfo(List<Component> info, boolean isReal, boolean hasData) {
		info.add(
			Component.translatable("techreborn.tooltip.transferRate")
				.withStyle(ChatFormatting.GRAY)
				.append(": ")
				.append(PowerSystem.getLocalizedPower(getCableType().transferRate))
				.withStyle(ChatFormatting.GOLD)
				.append("/t")
		);

		info.add(
			Component.translatable("techreborn.tooltip.tier")
				.withStyle(ChatFormatting.GRAY)
				.append(": ")
				.append(
					Component.literal(StringUtils.toFirstCapitalAllLowercase(getCableType().tier.toString()))
						.withStyle(ChatFormatting.GOLD)
				)
		);

		if (!getCableType().canKill) {
			info.add(Component.translatable("techreborn.tooltip.cable.can_cover").withStyle(ChatFormatting.GRAY));
		}
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(Player playerIn) {
		return new ItemStack(getCableType().block);
	}

	@Override
	public @Nullable BlockState getRenderData() {
		return cover;
	}

	private record CableTarget(Direction directionTo, BlockApiCache<EnergyStorage, Direction> cache) {

		@Nullable
		EnergyStorage find() {
			return cache.find(directionTo.getOpposite());
		}
	}
}
