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
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.common.network.ClientBoundPackets;
import reborncore.common.network.NetworkManager;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.StringUtils;
import reborncore.common.util.WorldUtils;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleSidedEnergyContainer;
import techreborn.blocks.cable.CableBlock;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.ArrayList;
import java.util.List;

public class CableBlockEntity extends BlockEntity
	implements BlockEntityTicker<CableBlockEntity>, IListInfoProvider, IToolDrop, RenderAttachmentBlockEntity {
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

	private boolean allowTransfer(Direction side) {
		return !ioBlocked && (blockedSides & (1 << side.ordinal())) == 0;
	}

	public EnergyStorage getSideEnergyStorage(@Nullable Direction side) {
		return energyContainer.getSideStorage(side);
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

	public long getEnergy() {
		return energyContainer.amount;
	}

	public void setEnergy(long energy) {
		energyContainer.amount = energy;
	}

	private BlockApiCache<EnergyStorage, Direction> getAdjacentCache(Direction direction) {
		if (adjacentCaches[direction.getId()] == null) {
			adjacentCaches[direction.getId()] = BlockApiCache.create(EnergyStorage.SIDED, (ServerWorld) world, pos.offset(direction));
		}
		return adjacentCaches[direction.getId()];
	}

	@Nullable
	BlockEntity getAdjacentBlockEntity(Direction direction) {
		return getAdjacentCache(direction).getBlockEntity();
	}

	void appendTargets(List<OfferedEnergyStorage> targetStorages) {
		ServerWorld serverWorld = (ServerWorld) world;
		if (serverWorld == null) {
			return;
		}

		// Update our targets if necessary.
		if (targets == null) {
			BlockState newBlockState = getCachedState();

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

				newBlockState = newBlockState.with(CableBlock.PROPERTY_MAP.get(direction), foundSomething);
			}

			serverWorld.setBlockState(getPos(), newBlockState);
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
	public NbtCompound toInitialChunkDataNbt() {
		return createNbt();
	}

	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		NbtCompound nbtTag = new NbtCompound();
		writeNbt(nbtTag);
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public void readNbt(NbtCompound compound) {
		super.readNbt(compound);
		if (compound.contains("energy")) {
			energyContainer.amount = compound.getLong("energy");
		}
		if (compound.contains("cover")) {
			cover = NbtHelper.toBlockState(WorldUtils.getBlockRegistryWrapper(world), compound.getCompound("cover"));
		} else {
			cover = null;
		}
	}

	@Override
	public void writeNbt(NbtCompound compound) {
		super.writeNbt(compound);
		compound.putLong("energy", energyContainer.amount);
		if (cover != null) {
			compound.put("cover", NbtHelper.fromBlockState(cover));
		}
	}

	public void neighborUpdate() {
		targets = null;
	}

	// BlockEntityTicker
	@Override
	public void tick(World world, BlockPos pos, BlockState state, CableBlockEntity blockEntity2) {
		if (world == null || world.isClient) {
			return;
		}

		CableTickManager.handleCableTick(this);
	}

	// IListInfoProvider
	@Override
	public void addInfo(List<Text> info, boolean isReal, boolean hasData) {
		info.add(
			Text.translatable("techreborn.tooltip.transferRate")
				.formatted(Formatting.GRAY)
				.append(": ")
				.append(PowerSystem.getLocalizedPower(getCableType().transferRate))
				.formatted(Formatting.GOLD)
				.append("/t")
		);

		info.add(
			Text.translatable("techreborn.tooltip.tier")
				.formatted(Formatting.GRAY)
				.append(": ")
				.append(
					Text.literal(StringUtils.toFirstCapitalAllLowercase(getCableType().tier.toString()))
						.formatted(Formatting.GOLD)
				)
		);

		if (!getCableType().canKill) {
			info.add(Text.translatable("techreborn.tooltip.cable.can_cover").formatted(Formatting.GRAY));
		}
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity playerIn) {
		return new ItemStack(getCableType().block);
	}

	@Override
	public @Nullable BlockState getRenderAttachmentData() {
		return cover;
	}

	private record CableTarget(Direction directionTo, BlockApiCache<EnergyStorage, Direction> cache) {

		@Nullable
		EnergyStorage find() {
			return cache.find(directionTo.getOpposite());
		}
	}
}
