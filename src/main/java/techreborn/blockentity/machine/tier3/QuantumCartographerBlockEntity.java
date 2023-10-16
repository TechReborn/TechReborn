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

package techreborn.blockentity.machine.tier3;

import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import techreborn.init.TRBlockEntities;

public class QuantumCartographerBlockEntity extends PowerAcceptorBlockEntity implements InventoryProvider, BuiltScreenHandlerProvider {
	public static final int MAP_SLOT = 0;
	public static final int SCAN_AREA = 32;
	public static final int SCAN_TICKS = 15;

	private final RebornInventory<QuantumCartographerBlockEntity> inventory = new RebornInventory<>(1, "QuantumCartographerBlockEntity", 1, this);
	private int scanOffsetX = 0;
	private int scanOffsetZ = 0;

	public QuantumCartographerBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.QUANTUM_CARTOGRAPHER, pos, state);
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);

		if (world.isClient) {
			return;
		}

		ItemStack mapStack = inventory.getStack(MAP_SLOT);

		if (!isValidMapStack(mapStack)) {
			scanOffsetZ = 0;
			scanOffsetX = 0;
			return;
		}

		MapState mapState = FilledMapItem.getMapState(mapStack, world);

		if (mapState == null) {
			return;
		}

		MinecraftServer server = ((ServerWorld) world).getServer();
		ServerWorld serverWorld = server.getWorld(mapState.dimension);
		FilledMapItem mapItem = (FilledMapItem) mapStack.getItem();

		// Find all the players who have the GUI open, and sync the changes to them.
		for (ServerPlayerEntity serverPlayerEntity : PlayerLookup.tracking(this)) {
			if (serverPlayerEntity.currentScreenHandler instanceof BuiltScreenHandler builtScreenHandler
			&& builtScreenHandler.getBlockEntity() == this) {
				mapState.update(serverPlayerEntity, mapStack);

				Packet<?> packet = mapItem.createSyncPacket(mapStack, this.getWorld(), serverPlayerEntity);
				if (packet != null) {
					serverPlayerEntity.networkHandler.sendPacket(packet);
				}
			}
		}

		if (!tryUseExact(powerUsage(mapState))) {
			return;
		}

		final FakePlayer fakePlayer = FakePlayer.get(serverWorld);

		int mapSizeBlocks = (1 << mapState.scale) * 128;
		BlockPos mapOrigin = new BlockPos(mapState.centerX - (mapSizeBlocks / 2), 0, mapState.centerZ  - (mapSizeBlocks / 2));
		BlockPos scanBlockPos = mapOrigin.add(scanOffsetX, 0, scanOffsetZ);
		fakePlayer.setPos(scanBlockPos.getX(), scanBlockPos.getY(), scanBlockPos.getZ());

		mapState.update(fakePlayer, mapStack);

		final MapState.PlayerUpdateTracker playerUpdateTracker = mapState.getPlayerSyncData(fakePlayer);
		playerUpdateTracker.field_131 = (int) world.getTime() % SCAN_TICKS;

		mapItem.updateColors(serverWorld, fakePlayer, mapState);

		if (world.getTime() % SCAN_TICKS != 0) {
			return;
		}

		scanOffsetX += SCAN_AREA;

		if (scanOffsetX > mapSizeBlocks) {
			scanOffsetX = 0;
			scanOffsetZ += SCAN_AREA;
		}

		if (scanOffsetZ > mapSizeBlocks) {
			scanOffsetZ = 0;
		}
	}

	@Override
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		tag.putInt("scanOffsetX", scanOffsetX);
		tag.putInt("scanOffsetZ", scanOffsetZ);
	}

	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		scanOffsetX = tag.getInt("scanOffsetX");
		scanOffsetZ = tag.getInt("scanOffsetZ");
	}

	@Override
	public long getBaseMaxPower() {
		return 10_000_000;
	}

	@Override
	public long getBaseMaxOutput() {
		return 0;
	}

	@Override
	public long getBaseMaxInput() {
		return 8192;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	public static int powerUsage(MapState mapState) {
		int size = (1 << mapState.scale) * 128;
		return size * 2;
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
		return new ScreenHandlerBuilder("quantumcartographer")
			.player(player.getInventory()).inventory().hotbar().addInventory()
			.blockEntity(this).slot(MAP_SLOT, 8, 72, QuantumCartographerBlockEntity::isValidMapStack)
			.syncEnergyValue()
			.sync(this::getScanOffsetX, value -> scanOffsetX = value)
			.sync(this::getScanOffsetZ, value -> scanOffsetZ = value)
			.addInventory()
			.create(this, syncID);
	}

	public static boolean isValidMapStack(ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}

		if (stack.getItem() instanceof FilledMapItem) {
			return true;
		}

		return false;
	}

	public int getScanOffsetX() {
		return scanOffsetX;
	}

	public int getScanOffsetZ() {
		return scanOffsetZ;
	}
}
