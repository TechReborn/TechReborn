/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.common.network;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;
import reborncore.common.blockentity.FluidConfiguration;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.blockentity.SlotConfiguration;
import reborncore.common.chunkloading.ChunkLoaderManager;

public class ServerBoundPackets {

	public static void init() {
		NetworkManager.registerServerBoundHandler(new Identifier("reborncore", "fluid_config_save"), (server, player, handler, packetBuffer, responseSender) -> {
			BlockPos pos = packetBuffer.readBlockPos();
			CompoundTag compoundTag = packetBuffer.readCompoundTag();

			server.execute(() -> {
				FluidConfiguration.FluidConfig fluidConfiguration = new FluidConfiguration.FluidConfig(compoundTag);
				MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) player.world.getBlockEntity(pos);
				legacyMachineBase.fluidConfiguration.updateFluidConfig(fluidConfiguration);
				legacyMachineBase.markDirty();

				IdentifiedPacket packetFluidConfigSync = ClientBoundPackets.createPacketFluidConfigSync(pos, legacyMachineBase.fluidConfiguration);
				NetworkManager.sendToTracking(packetFluidConfigSync, legacyMachineBase);

				//We update the block to allow pipes that are connecting to detctect the update and change their connection status if needed
				World world = legacyMachineBase.getWorld();
				BlockState blockState = world.getBlockState(legacyMachineBase.getPos());
				world.updateNeighborsAlways(legacyMachineBase.getPos(), blockState.getBlock());
			});
		});

		NetworkManager.registerServerBoundHandler(new Identifier("reborncore", "config_save"), (server, player, handler, packetBuffer, responseSender) -> {
			BlockPos pos = packetBuffer.readBlockPos();
			CompoundTag tagCompound = packetBuffer.readCompoundTag();

			server.execute(() -> {
				MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) player.world.getBlockEntity(pos);
				legacyMachineBase.getSlotConfiguration().read(tagCompound);
				legacyMachineBase.markDirty();

				IdentifiedPacket packetSlotSync = ClientBoundPackets.createPacketSlotSync(pos, legacyMachineBase.getSlotConfiguration());
				NetworkManager.sendToWorld(packetSlotSync, (ServerWorld) legacyMachineBase.getWorld());
			});
		});

		NetworkManager.registerServerBoundHandler(new Identifier("reborncore", "fluid_io_save"), (server, player, handler, packetBuffer, responseSender) -> {
			BlockPos pos = packetBuffer.readBlockPos();
			boolean input = packetBuffer.readBoolean();
			boolean output = packetBuffer.readBoolean();

			server.execute(() -> {
				MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) player.world.getBlockEntity(pos);
				FluidConfiguration config = legacyMachineBase.fluidConfiguration;
				if (config == null) {
					return;
				}
				config.setInput(input);
				config.setOutput(output);

				//Syncs back to the client
				IdentifiedPacket packetFluidConfigSync = ClientBoundPackets.createPacketFluidConfigSync(pos, legacyMachineBase.fluidConfiguration);
				NetworkManager.sendToTracking(packetFluidConfigSync, legacyMachineBase);
			});
		});

		NetworkManager.registerServerBoundHandler(new Identifier("reborncore", "io_save"), (server, player, handler, packetBuffer, responseSender) -> {
			BlockPos pos = packetBuffer.readBlockPos();
			int slotID = packetBuffer.readInt();
			boolean input = packetBuffer.readBoolean();
			boolean output = packetBuffer.readBoolean();
			boolean filter = packetBuffer.readBoolean();

			server.execute(() -> {
				MachineBaseBlockEntity machineBase = (MachineBaseBlockEntity) player.world.getBlockEntity(pos);
				Validate.notNull(machineBase, "machine cannot be null");
				SlotConfiguration.SlotConfigHolder holder = machineBase.getSlotConfiguration().getSlotDetails(slotID);
				if (holder == null) {
					return;
				}

				holder.setInput(input);
				holder.setOutput(output);
				holder.setfilter(filter);

				//Syncs back to the client
				IdentifiedPacket packetSlotSync = ClientBoundPackets.createPacketSlotSync(pos, machineBase.getSlotConfiguration());
				NetworkManager.sendToAll(packetSlotSync, player.getServer());
			});
		});

		NetworkManager.registerServerBoundHandler(new Identifier("reborncore", "slot_save"), (server, player, handler, packetBuffer, responseSender) -> {
			BlockPos pos = packetBuffer.readBlockPos();
			CompoundTag compoundTag = packetBuffer.readCompoundTag();

			server.execute(() -> {
				SlotConfiguration.SlotConfig slotConfig = new SlotConfiguration.SlotConfig(compoundTag);
				MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) player.world.getBlockEntity(pos);
				legacyMachineBase.getSlotConfiguration().getSlotDetails(slotConfig.getSlotID()).updateSlotConfig(slotConfig);
				legacyMachineBase.markDirty();

				IdentifiedPacket packetSlotSync = ClientBoundPackets.createPacketSlotSync(pos, legacyMachineBase.getSlotConfiguration());
				NetworkManager.sendToWorld(packetSlotSync, (ServerWorld) legacyMachineBase.getWorld());
			});
		});

		NetworkManager.registerServerBoundHandler(new Identifier("reborncore", "chunk_loader_request"), (server, player, handler, packetBuffer, responseSender) -> {
			BlockPos pos = packetBuffer.readBlockPos();
			server.execute(() -> {
				Validate.isInstanceOf(ServerPlayerEntity.class, player, "something very very bad has happened");
				ChunkLoaderManager chunkLoaderManager = ChunkLoaderManager.get(player.world);
				chunkLoaderManager.syncChunkLoaderToClient((ServerPlayerEntity) player, pos);
			});
		});

		NetworkManager.registerServerBoundHandler(new Identifier("reborncore", "set_redstone_state"), (server, player, handler, packetBuffer, responseSender) -> {
			BlockPos pos = packetBuffer.readBlockPos();
			String elementName = packetBuffer.readString(packetBuffer.readInt());
			int stateId = packetBuffer.readInt();

			RedstoneConfiguration.Element element = RedstoneConfiguration.getElementByName(elementName);
			if (element == null) return;

			if (stateId < 0 || stateId >= RedstoneConfiguration.State.values().length) return;
			RedstoneConfiguration.State state = RedstoneConfiguration.State.values()[stateId];

			server.execute(() -> {
				MachineBaseBlockEntity blockEntity = (MachineBaseBlockEntity) player.world.getBlockEntity(pos);
				if (blockEntity == null) return;

				blockEntity.getRedstoneConfiguration().setState(element, state);
			});
		});
	}


	public static IdentifiedPacket createPacketFluidConfigSave(BlockPos pos, FluidConfiguration.FluidConfig fluidConfiguration) {
		return NetworkManager.createServerBoundPacket(new Identifier("reborncore", "fluid_config_save"), packetBuffer -> {
			packetBuffer.writeBlockPos(pos);
			packetBuffer.writeCompoundTag(fluidConfiguration.write());
		});
	}

	public static IdentifiedPacket createPacketConfigSave(BlockPos pos, SlotConfiguration slotConfig) {
		return NetworkManager.createServerBoundPacket(new Identifier("reborncore", "config_save"), packetBuffer -> {
			packetBuffer.writeBlockPos(pos);
			packetBuffer.writeCompoundTag(slotConfig.write());
		});
	}

	public static IdentifiedPacket createPacketFluidIOSave(BlockPos pos, boolean input, boolean output) {
		return NetworkManager.createServerBoundPacket(new Identifier("reborncore", "fluid_io_save"), packetBuffer -> {
			packetBuffer.writeBlockPos(pos);
			packetBuffer.writeBoolean(input);
			packetBuffer.writeBoolean(output);
		});
	}

	public static IdentifiedPacket createPacketIOSave(BlockPos pos, int slotID, boolean input, boolean output, boolean filter) {
		return NetworkManager.createServerBoundPacket(new Identifier("reborncore", "io_save"), packetBuffer -> {
			packetBuffer.writeBlockPos(pos);
			packetBuffer.writeInt(slotID);
			packetBuffer.writeBoolean(input);
			packetBuffer.writeBoolean(output);
			packetBuffer.writeBoolean(filter);
		});
	}

	public static IdentifiedPacket createPacketSlotSave(BlockPos pos, SlotConfiguration.SlotConfig slotConfig) {
		return NetworkManager.createServerBoundPacket(new Identifier("reborncore", "slot_save"), packetBuffer -> {
			packetBuffer.writeBlockPos(pos);
			packetBuffer.writeCompoundTag(slotConfig.write());
		});
	}

	public static IdentifiedPacket requestChunkloaderChunks(BlockPos pos) {
		return NetworkManager.createServerBoundPacket(new Identifier("reborncore", "chunk_loader_request"), packetBuffer -> {
			packetBuffer.writeBlockPos(pos);
		});
	}

	public static IdentifiedPacket createPacketSetRedstoneSate(BlockPos pos, RedstoneConfiguration.Element element, RedstoneConfiguration.State state) {
		return NetworkManager.createServerBoundPacket(new Identifier("reborncore", "set_redstone_state"), packetBuffer -> {
			packetBuffer.writeBlockPos(pos);
			packetBuffer.writeInt(element.getName().length());
			packetBuffer.writeString(element.getName());
			packetBuffer.writeInt(state.ordinal());
		});
	}
}
