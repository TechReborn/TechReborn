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

package techreborn.blockentity.storage.energy.lesu;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import techreborn.blockentity.storage.energy.EnergyStorageBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

import java.util.HashSet;
import java.util.LinkedList;

public class LapotronicSUBlockEntity extends EnergyStorageBlockEntity implements BuiltScreenHandlerProvider {

	public static final Direction[] DIRECTIONS = Direction.values();
	public static final int DIRECTIONS_LENGTH = DIRECTIONS.length;
	public static final byte[] FLAGS = new byte[DIRECTIONS_LENGTH];
	public static final byte[] OPP_FLAGS = new byte[DIRECTIONS_LENGTH];
	static {
		for (int i = 0; i < DIRECTIONS_LENGTH; i++) {
			FLAGS[i] = (byte) (1 << DIRECTIONS[i].ordinal());
			OPP_FLAGS[i] = (byte) ( 1 << DIRECTIONS[i].getOpposite().ordinal());
		}
	}

	private int connectedBlocks = 0;
	public byte neighbors = 0b000000;

	public LapotronicSUBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.LAPOTRONIC_SU, pos, state, "LESU", 2, TRContent.Machine.LAPOTRONIC_SU.block, RcEnergyTier.LOW, TechRebornConfig.lesuStoragePerBlock);
		checkOverfill = false;
		this.maxOutput = TechRebornConfig.lesuBaseOutput;
	}

	private void setMaxStorage() {
		maxStorage = (connectedBlocks + 1) * TechRebornConfig.lesuStoragePerBlock;
		if (maxStorage < 0) {
			maxStorage = Integer.MAX_VALUE;
		}
	}

	private void setIORate() {
		maxOutput = TechRebornConfig.lesuBaseOutput + (connectedBlocks * TechRebornConfig.lesuExtraIOPerBlock);
		if (connectedBlocks < 32) {
			maxInput = RcEnergyTier.LOW.getMaxInput();
		} else if (connectedBlocks < 128) {
			maxInput = RcEnergyTier.MEDIUM.getMaxInput();
		} else {
			maxInput = RcEnergyTier.HIGH.getMaxInput();
		}
	}

	// EnergyStorageBlockEntity
	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || world.isClient) {
			return;
		}
		if (getEnergy() > getMaxStoredPower()) {
			setEnergy(getMaxStoredPower());
		}
	}

	// MachineBaseBlockEntity
	@Override
	public void onLoad() {
		super.onLoad();
		if (world == null || world.isClient) return;

		// 1. Collect information and change the relationship between surrounding blocks
		byte flagInvalidNeighbors = 0b000000;
		LinkedList<LSUStorageBlockEntity> canConnect = new LinkedList<>();
		HashSet<BlockPos> visited = new HashSet<>();
		for (int i = 0; i < DIRECTIONS_LENGTH; i++) {
			if ((neighbors & FLAGS[i]) != 0) {
				if (world.getBlockEntity(pos.offset(DIRECTIONS[i])) instanceof LSUStorageBlockEntity lsu_storage) {
					if (lsu_storage.master == null) {
						canConnect.add(lsu_storage);
						lsu_storage.addTo(visited);
					}
				} else {
					flagInvalidNeighbors |= FLAGS[i];
				}
			}
		}

		// 2. Compatible with older versions: initialize neighbors
		if (flagInvalidNeighbors != 0b000000) {
			neighbors ^= flagInvalidNeighbors;
			markDirty();
		}

		// 3. Expand outward layer by layer to search for connectable blocks and connect them
		LSUStorageBlockEntity lsu_storage;
		BlockPos linkPos;
		while (!canConnect.isEmpty()) {
			lsu_storage = canConnect.poll();
			lsu_storage.master = this;
			connectedBlocks++;
			for (int i = 0; i < DIRECTIONS_LENGTH; i++) {
				if ((lsu_storage.neighbors & FLAGS[i]) != 0) {
					linkPos = lsu_storage.posOffset(DIRECTIONS[i]);
					if (visited.add(linkPos) && world.getBlockEntity(linkPos) instanceof LSUStorageBlockEntity link_lsu_storage) {
						lsu_storage.links |= FLAGS[i];
						canConnect.add(link_lsu_storage);
					}
				}
			}
		}

		// 4. Update energy configuration
		setMaxStorage();
		setIORate();
	}

	public void disconnectNetwork() {
		if (world == null) return;

		// 1. Collect surrounding connected blocks
		LinkedList<LSUStorageBlockEntity> canDelete = new LinkedList<>();
		LSUStorageBlockEntity lsu_storage;
		for (int i = 0; i < DIRECTIONS_LENGTH; i++) {
			if ((neighbors & FLAGS[i]) != 0) {
				lsu_storage = fastGetLSUS(DIRECTIONS[i]);
				if (lsu_storage.master == this) {
					canDelete.add(lsu_storage);
				}
			}
		}

		// 2. Expand outwards layer by layer, looking for connected blocks and disconnect them
		while (!canDelete.isEmpty()) {
			lsu_storage = canDelete.poll();
			for (int i = 0; i < DIRECTIONS_LENGTH; i++) {
				if ((lsu_storage.links & FLAGS[i]) != 0) {
					canDelete.add(lsu_storage.fastGetLSUS(DIRECTIONS[i]));
				}
			}
			lsu_storage.master = null;
			lsu_storage.links = 0b000000;
		}
	}

	public final void checkNeighbors() {
		if (world == null) return;
		for (int i = 0; i < DIRECTIONS_LENGTH; i++) {
			if (world.getBlockEntity(pos.offset(DIRECTIONS[i])) instanceof LSUStorageBlockEntity) {
				neighbors |= FLAGS[i];
			}
		}
		if (neighbors != 0b000000) {
			markDirty();
		}
	}

	public LSUStorageBlockEntity fastGetLSUS(Direction direction) {
		assert world != null;
		return (LSUStorageBlockEntity) world.getBlockEntity(pos.offset(direction));
	}

	// IContainerProvider
	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final PlayerEntity player) {
		return new ScreenHandlerBuilder("lesu").player(player.getInventory()).inventory().hotbar().armor().complete(8, 18)
				.addArmor().addInventory().blockEntity(this).energySlot(0, 62, 45).energySlot(1, 98, 45).syncEnergyValue()
				.sync(PacketCodecs.INTEGER, this::getConnectedBlocksNum, this::setConnectedBlocksNum).addInventory().create(this, syncID);
	}

	public int getConnectedBlocksNum() {
		return connectedBlocks;
	}

	public void setConnectedBlocksNum(int value) {
		this.connectedBlocks = value;
		setMaxStorage();
		setIORate();
	}

	@Override
	public void writeNbt(NbtCompound tagCompound, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(tagCompound, registryLookup);
		tagCompound.putByte("neighbors", neighbors);
	}

	@Override
	public void readNbt(NbtCompound tagCompound, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(tagCompound, registryLookup);
		if (tagCompound.contains("neighbors")) {
			neighbors = tagCompound.getByte("neighbors");
		} else {
			// Compatible with older versions: judge not initialized
			neighbors = 0b111111;
		}
	}
}
