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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import reborncore.common.screen.BuiltScreenHandler;

@Environment(EnvType.CLIENT)
public class ClientBoundPacketHandlers {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientBoundPacketHandlers.class);

	public static void init() {
		ClientNetworkManager.registerClientBoundHandler(new Identifier("reborncore", "custom_description"), (client, handler, packetBuffer, responseSender) -> {
			BlockPos pos = packetBuffer.readBlockPos();
			NbtCompound tagCompound = packetBuffer.readNbt();
			client.execute(() -> {
				World world = MinecraftClient.getInstance().world;
				if (world.isChunkLoaded(pos)) {
					BlockEntity blockentity = world.getBlockEntity(pos);
					if (blockentity != null && tagCompound != null) {
						blockentity.readNbt(tagCompound);
					}
				}
			});
		});

		ClientNetworkManager.registerClientBoundHandler(new Identifier("reborncore", "fluid_config_sync"), (client, handler, packetBuffer, responseSender) -> {
			BlockPos pos = packetBuffer.readBlockPos();
			NbtCompound compoundTag = packetBuffer.readNbt();

			client.execute(() -> {
				FluidConfiguration fluidConfiguration = new FluidConfiguration(compoundTag);
				if (!MinecraftClient.getInstance().world.isChunkLoaded(pos)) {
					return;
				}
				MachineBaseBlockEntity machineBase = (MachineBaseBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(pos);
				if (machineBase == null || machineBase.fluidConfiguration == null || fluidConfiguration == null) {
					RebornCore.LOGGER.error("Failed to sync fluid config data to " + pos);
					return;
				}
				fluidConfiguration.getAllSides().forEach(fluidConfig -> machineBase.fluidConfiguration.updateFluidConfig(fluidConfig));
				machineBase.fluidConfiguration.setInput(fluidConfiguration.autoInput());
				machineBase.fluidConfiguration.setOutput(fluidConfiguration.autoOutput());

			});
		});

		ClientNetworkManager.registerClientBoundHandler(new Identifier("reborncore", "slot_sync"), (client, handler, packetBuffer, responseSender) -> {
			BlockPos pos = packetBuffer.readBlockPos();
			NbtCompound compoundTag = packetBuffer.readNbt();

			client.execute(() -> {
				SlotConfiguration slotConfig = new SlotConfiguration(compoundTag);
				if (!MinecraftClient.getInstance().world.isChunkLoaded(pos)) {
					return;
				}
				MachineBaseBlockEntity machineBase = (MachineBaseBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(pos);
				if (machineBase == null || machineBase.getSlotConfiguration() == null || slotConfig == null || slotConfig.getSlotDetails() == null) {
					RebornCore.LOGGER.error("Failed to sync slot data to " + pos);
					return;
				}
				MinecraftClient.getInstance().execute(() -> slotConfig.getSlotDetails().forEach(slotConfigHolder -> machineBase.getSlotConfiguration().updateSlotDetails(slotConfigHolder)));
			});
		});

		ClientNetworkManager.registerClientBoundHandler(new Identifier("reborncore", "send_object"), (client, handler, packetBuffer, responseSender) -> {
			int size = packetBuffer.readInt();
			ExtendedPacketBuffer epb = new ExtendedPacketBuffer(packetBuffer);
			Int2ObjectMap<Object> updatedValues = new Int2ObjectOpenHashMap<>();

			for (int i = 0; i < size; i++) {
				int id = packetBuffer.readInt();
				Object value =  epb.readObject();
				updatedValues.put(id, value);
			}

			String name = packetBuffer.readString(packetBuffer.readInt());

			client.execute(() -> {
				Screen gui = MinecraftClient.getInstance().currentScreen;
				if (gui instanceof HandledScreen handledScreen) {
					ScreenHandler screenHandler = handledScreen.getScreenHandler();
					if (screenHandler instanceof BuiltScreenHandler builtScreenHandler) {
						String shName = screenHandler.getClass().getName();
						if (!shName.equals(name)) {
							LOGGER.warn("Received packet for {} but screen handler {} is open!", name, shName);
							return;
						}

						builtScreenHandler.handleUpdateValues(updatedValues);
					}
				}
			});
		});

		ClientNetworkManager.registerClientBoundHandler(new Identifier("reborncore", "sync_chunks"), ChunkLoaderManager.CODEC, ClientChunkManager::setLoadedChunks);
		ClientNetworkManager.registerClientBoundHandler(new Identifier("reborncore", "stacks_to_render"), Codec.list(ItemStack.CODEC), ItemStackRenderManager.RENDER_QUEUE::addAll);
	}
}
