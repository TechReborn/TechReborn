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

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;
import reborncore.common.blockentity.FluidConfiguration;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.blockentity.SlotConfiguration;
import reborncore.common.chunkloading.ChunkLoaderManager;
import reborncore.common.network.clientbound.FluidConfigSyncPayload;
import reborncore.common.network.clientbound.SlotSyncPayload;
import reborncore.common.network.serverbound.*;

public class ServerBoundPackets {

	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(FluidConfigSavePayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			legacyMachineBase.fluidConfiguration.updateFluidConfig(payload.fluidConfiguration());
			legacyMachineBase.markDirty();

			NetworkManager.sendToTracking(new FluidConfigSyncPayload(payload.pos(), legacyMachineBase.fluidConfiguration), legacyMachineBase);

			// We update the block to allow pipes that are connecting to detect the update and change their
			// connection status if needed
			World world = legacyMachineBase.getWorld();
			BlockState blockState = world.getBlockState(legacyMachineBase.getPos());
			world.updateNeighborsAlways(legacyMachineBase.getPos(), blockState.getBlock());
		});

		ServerPlayNetworking.registerGlobalReceiver(ConfigSavePayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			for (SlotConfiguration.SlotConfigHolder slotDetail : payload.slotConfig().getSlotDetails()) {
				legacyMachineBase.getSlotConfiguration().updateSlotDetails(slotDetail);
			}
			legacyMachineBase.markDirty();

			NetworkManager.sendToWorld(new SlotSyncPayload(payload.pos(), legacyMachineBase.getSlotConfiguration()), (ServerWorld) legacyMachineBase.getWorld());
		});

		ServerPlayNetworking.registerGlobalReceiver(FluidIoSavePayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			FluidConfiguration config = legacyMachineBase.fluidConfiguration;
			if (config == null) {
				return;
			}
			config.setInput(payload.input());
			config.setOutput(payload.output());

			// Syncs back to the client
			NetworkManager.sendToTracking(new FluidConfigSyncPayload(payload.pos(), legacyMachineBase.fluidConfiguration), legacyMachineBase);
		});

		ServerPlayNetworking.registerGlobalReceiver(IoSavePayload.ID, (payload, context) -> {
			MachineBaseBlockEntity machineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			Validate.notNull(machineBase, "machine cannot be null");
			SlotConfiguration.SlotConfigHolder holder = machineBase.getSlotConfiguration().getSlotDetails(payload.slotID());
			if (holder == null) {
				return;
			}

			holder.setInput(payload.input());
			holder.setOutput(payload.output());
			holder.setFilter(payload.filter());

			//Syncs back to the client
			NetworkManager.sendToAll(new SlotSyncPayload(payload.pos(), machineBase.getSlotConfiguration()), context.player().getServer());
		});

		ServerPlayNetworking.registerGlobalReceiver(SlotSavePayload.ID, (payload, context) -> {
			MachineBaseBlockEntity legacyMachineBase = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			legacyMachineBase.getSlotConfiguration().getSlotDetails(payload.slotConfig().getSlotID()).updateSlotConfig(payload.slotConfig());
			legacyMachineBase.markDirty();

			NetworkManager.sendToWorld(new SlotSyncPayload(payload.pos(), legacyMachineBase.getSlotConfiguration()), (ServerWorld) legacyMachineBase.getWorld());
		});

		ServerPlayNetworking.registerGlobalReceiver(ChunkLoaderRequestPayload.ID, (payload, context) -> {
			ChunkLoaderManager chunkLoaderManager = ChunkLoaderManager.get(context.player().getWorld());
			chunkLoaderManager.syncChunkLoaderToClient(context.player(), payload.pos());
		});

		ServerPlayNetworking.registerGlobalReceiver(SetRedstoneStatePayload.ID, (payload, context) -> {
			RedstoneConfiguration.Element element = RedstoneConfiguration.getElementByName(payload.elementName());
			MachineBaseBlockEntity blockEntity = (MachineBaseBlockEntity) context.player().getWorld().getBlockEntity(payload.pos());
			if (blockEntity == null) return;
			blockEntity.getRedstoneConfiguration().setState(element, payload.state());
		});
	}
}
