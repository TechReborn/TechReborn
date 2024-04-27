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

package reborncore.client;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reborncore.RebornCore;
import reborncore.common.blockentity.FluidConfiguration;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.SlotConfiguration;
import reborncore.common.chunkloading.ChunkLoaderManager;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.clientbound.*;
import reborncore.common.screen.BuiltScreenHandler;

public class ClientBoundPacketHandlers {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientBoundPacketHandlers.class);

	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(CustomDescriptionPayload.ID, (payload, context) -> {
			World world = MinecraftClient.getInstance().world;
			if (world.isChunkLoaded(payload.pos())) {
				BlockEntity blockentity = world.getBlockEntity(payload.pos());
				if (blockentity != null && payload.nbt() != null) {
					blockentity.read(payload.nbt(), world.getRegistryManager());
				}
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(FluidConfigSyncPayload.ID, (payload, context) -> {
			FluidConfiguration fluidConfiguration = payload.fluidConfiguration();
			if (!MinecraftClient.getInstance().world.isChunkLoaded(payload.pos())) {
				return;
			}
			MachineBaseBlockEntity machineBase = (MachineBaseBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(payload.pos());
			if (machineBase == null || machineBase.fluidConfiguration == null || fluidConfiguration == null) {
				RebornCore.LOGGER.error("Failed to sync fluid config data to " + payload.pos());
				return;
			}
			fluidConfiguration.getAllSides().forEach(fluidConfig -> machineBase.fluidConfiguration.updateFluidConfig(fluidConfig));
			machineBase.fluidConfiguration.setInput(fluidConfiguration.autoInput());
			machineBase.fluidConfiguration.setOutput(fluidConfiguration.autoOutput());;
		});

		ClientPlayNetworking.registerGlobalReceiver(SlotSyncPayload.ID, (payload, context) -> {
			SlotConfiguration slotConfig = payload.slotConfig();
			if (!MinecraftClient.getInstance().world.isChunkLoaded(payload.pos())) {
				return;
			}
			MachineBaseBlockEntity machineBase = (MachineBaseBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(payload.pos());
			if (machineBase == null || machineBase.getSlotConfiguration() == null || slotConfig == null || slotConfig.getSlotDetails() == null) {
				RebornCore.LOGGER.error("Failed to sync slot data to " + payload.pos());
				return;
			}
			MinecraftClient.getInstance().execute(() -> slotConfig.getSlotDetails().forEach(slotConfigHolder -> machineBase.getSlotConfiguration().updateSlotDetails(slotConfigHolder)));
		});

		ClientPlayNetworking.registerGlobalReceiver(ScreenHandlerUpdatePayload.ID, (payload, context) -> {
			// TODO 1.20.5
//			int size = packetBuffer.readInt();
//			ExtendedPacketBuffer epb = new ExtendedPacketBuffer(packetBuffer);
//			Int2ObjectMap<Object> updatedValues = new Int2ObjectOpenHashMap<>();
//
//			for (int i = 0; i < size; i++) {
//				int id = packetBuffer.readInt();
//				Object value =  epb.readObject();
//				updatedValues.put(id, value);
//			}
//
//			String name = packetBuffer.readString(packetBuffer.readInt());
//
//			client.execute(() -> {
//				Screen gui = MinecraftClient.getInstance().currentScreen;
//				if (gui instanceof HandledScreen handledScreen) {
//					ScreenHandler screenHandler = handledScreen.getScreenHandler();
//					if (screenHandler instanceof BuiltScreenHandler builtScreenHandler) {
//						String shName = screenHandler.getClass().getName();
//						if (!shName.equals(name)) {
//							LOGGER.warn("Received packet for {} but screen handler {} is open!", name, shName);
//							return;
//						}
//
//						builtScreenHandler.handleUpdateValues(updatedValues);
//					}
//				}
//			});
		});

		ClientPlayNetworking.registerGlobalReceiver(ChunkSyncPayload.ID, (payload, context) -> ClientChunkManager.setLoadedChunks(payload.chunks()));
		ClientPlayNetworking.registerGlobalReceiver(QueueItemStacksPayload.ID, (payload, context) -> ItemStackRenderManager.RENDER_QUEUE.addAll(payload.stacks()));
	}
}
