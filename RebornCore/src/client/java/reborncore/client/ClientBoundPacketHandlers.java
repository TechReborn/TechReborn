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

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.TagValueInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reborncore.RebornCore;
import reborncore.common.blockentity.FluidConfiguration;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.SlotConfiguration;
import reborncore.common.network.clientbound.ChunkSyncPayload;
import reborncore.common.network.clientbound.CustomDescriptionPayload;
import reborncore.common.network.clientbound.FluidConfigSyncPayload;
import reborncore.common.network.clientbound.QueueItemStacksPayload;
import reborncore.common.network.clientbound.ScreenHandlerUpdatePayload;
import reborncore.common.network.clientbound.SlotSyncPayload;
import reborncore.common.screen.BuiltScreenHandler;

public class ClientBoundPacketHandlers {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientBoundPacketHandlers.class);

	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(CustomDescriptionPayload.ID, (payload, context) -> {
			ClientLevel world = Minecraft.getInstance().level;
			if (world.isLoaded(payload.pos())) {
				BlockEntity blockentity = world.getBlockEntity(payload.pos());
				if (blockentity != null && payload.nbt() != null) {
					try (ProblemReporter.ScopedCollector logging = new ProblemReporter.ScopedCollector(blockentity.problemPath(), LOGGER)) {
						blockentity.loadWithComponents(TagValueInput.create(logging, world.registryAccess(), payload.nbt()));
					}
				}
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(FluidConfigSyncPayload.ID, (payload, context) -> {
			FluidConfiguration fluidConfiguration = payload.fluidConfiguration();
			if (!Minecraft.getInstance().level.isLoaded(payload.pos())) {
				return;
			}
			MachineBaseBlockEntity machineBase = (MachineBaseBlockEntity) Minecraft.getInstance().level.getBlockEntity(payload.pos());
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
			if (!Minecraft.getInstance().level.isLoaded(payload.pos())) {
				return;
			}
			MachineBaseBlockEntity machineBase = (MachineBaseBlockEntity) Minecraft.getInstance().level.getBlockEntity(payload.pos());
			if (machineBase == null || machineBase.getSlotConfiguration() == null || slotConfig == null || slotConfig.getSlotDetails() == null) {
				RebornCore.LOGGER.error("Failed to sync slot data to " + payload.pos());
				return;
			}
			Minecraft.getInstance().execute(() -> slotConfig.getSlotDetails().forEach(slotConfigHolder -> machineBase.getSlotConfiguration().updateSlotDetails(slotConfigHolder)));
		});

		ClientPlayNetworking.registerGlobalReceiver(ScreenHandlerUpdatePayload.ID, (payload, context) -> {
			Screen gui = Minecraft.getInstance().screen;
			if (gui instanceof AbstractContainerScreen handledScreen) {
				AbstractContainerMenu screenHandler = handledScreen.getMenu();
				if (screenHandler instanceof BuiltScreenHandler builtScreenHandler) {
					builtScreenHandler.applyScreenHandlerData(payload.data());
				}
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(ChunkSyncPayload.ID, (payload, context) -> ClientChunkManager.setLoadedChunks(payload.chunks()));
		ClientPlayNetworking.registerGlobalReceiver(QueueItemStacksPayload.ID, (payload, context) -> ItemStackRenderManager.RENDER_QUEUE.addAll(payload.stacks()));
	}
}
