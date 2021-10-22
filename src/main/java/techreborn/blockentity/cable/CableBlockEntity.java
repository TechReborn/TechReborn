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
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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
	private BlockState cover = null;
	long lastTick = 0;
	// null means that it needs to be re-queried
	List<CableTarget> targets = null;
	/**
	 * Bitmask to prevent input or output into/from the cable when the cable already transferred in the target direction.
	 * This prevents double transfer rates, and back and forth between two cables.
	 */
	int blockedSides = 0;
	/**
	 * This is only used during the cable tick, whereas blockedOperations is used between ticks.
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

	void appendTargets(List<OfferedEnergyStorage> targetStorages) {
		ServerWorld serverWorld = (ServerWorld) world;
		if (serverWorld == null) { return; }

		// Update our targets if necessary.
		if (targets == null) {
			BlockState newBlockState = getCachedState();

			targets = new ArrayList<>();
			for (Direction direction : Direction.values()) {
				boolean foundSomething = false;

				BlockPos adjPos = getPos().offset(direction);
				BlockEntity adjBe = serverWorld.getBlockEntity(adjPos);

				if (adjBe instanceof CableBlockEntity adjCable) {
					if (adjCable.getCableType().transferRate == getCableType().transferRate) {
						// Make sure cables are not used as regular targets.
						foundSomething = true;
					}
				} else if (EnergyStorage.SIDED.find(serverWorld, adjPos, null, adjBe, direction.getOpposite()) != null) {
					foundSomething = true;
					targets.add(new CableTarget(
							direction,
							BlockApiCache.create(EnergyStorage.SIDED, serverWorld, adjPos)
					));
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
		return writeNbt(new NbtCompound());
	}

	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		NbtCompound nbtTag = new NbtCompound();
		writeNbt(nbtTag);
		return new BlockEntityUpdateS2CPacket(getPos(), 1, nbtTag);
	}

	@Override
	public void readNbt(NbtCompound compound) {
		super.readNbt(compound);
		if (compound.contains("energy")) {
			energyContainer.amount = compound.getLong("energy");
		}
		if (compound.contains("cover")) {
			cover = NbtHelper.toBlockState(compound.getCompound("cover"));
		} else {
			cover = null;
		}
	}

	@Override
	public NbtCompound writeNbt(NbtCompound compound) {
		super.writeNbt(compound);
		compound.putLong("energy", energyContainer.amount);
		if (cover != null) {
			compound.put("cover", NbtHelper.fromBlockState(cover));
		}
		return compound;
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
				new TranslatableText("techreborn.tooltip.transferRate")
						.formatted(Formatting.GRAY)
						.append(": ")
						.append(PowerSystem.getLocalizedPower(getCableType().transferRate))
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

	private record CableTarget(Direction directionTo, BlockApiCache<EnergyStorage, Direction> cache) {

		@Nullable
		EnergyStorage find() {
			return cache.find(directionTo.getOpposite());
		}
	}
}
