/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2024 TeamReborn
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

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import reborncore.common.network.clientbound.ChunkSyncPayload;
import reborncore.common.network.clientbound.CustomDescriptionPayload;
import reborncore.common.network.clientbound.FluidConfigSyncPayload;
import reborncore.common.network.clientbound.QueueItemStacksPayload;
import reborncore.common.network.clientbound.ScreenHandlerUpdatePayload;
import reborncore.common.network.clientbound.SlotSyncPayload;
import reborncore.common.network.serverbound.ChunkLoaderRequestPayload;
import reborncore.common.network.serverbound.FluidConfigSavePayload;
import reborncore.common.network.serverbound.FluidIoSavePayload;
import reborncore.common.network.serverbound.IoSavePayload;
import reborncore.common.network.serverbound.SetRedstoneStatePayload;
import reborncore.common.network.serverbound.SlotConfigSavePayload;
import reborncore.common.network.serverbound.SlotSavePayload;

public class Packets {
	public static void register() {
		clientbound(PayloadTypeRegistry.playS2C());
		serverbound(PayloadTypeRegistry.playC2S());
	}

	private static void clientbound(PayloadTypeRegistry<RegistryByteBuf> registry) {
		registry.register(ChunkSyncPayload.ID, ChunkSyncPayload.PACKET_CODEC);
		registry.register(CustomDescriptionPayload.ID, CustomDescriptionPayload.PACKET_CODEC);
		registry.register(FluidConfigSyncPayload.ID, FluidConfigSyncPayload.PACKET_CODEC);
		registry.register(QueueItemStacksPayload.ID, QueueItemStacksPayload.PACKET_CODEC);
		registry.register(ScreenHandlerUpdatePayload.ID, ScreenHandlerUpdatePayload.PACKET_CODEC);
		registry.register(SlotSyncPayload.ID, SlotSyncPayload.PACKET_CODEC);
	}

	private static void serverbound(PayloadTypeRegistry<RegistryByteBuf> registry) {
		registry.register(ChunkLoaderRequestPayload.ID, ChunkLoaderRequestPayload.PACKET_CODEC);
		registry.register(FluidConfigSavePayload.ID, FluidConfigSavePayload.PACKET_CODEC);
		registry.register(FluidIoSavePayload.ID, FluidIoSavePayload.PACKET_CODEC);
		registry.register(IoSavePayload.ID, IoSavePayload.PACKET_CODEC);
		registry.register(SetRedstoneStatePayload.ID, SetRedstoneStatePayload.CODEC);
		registry.register(SlotConfigSavePayload.ID, SlotConfigSavePayload.PACKET_CODEC);
		registry.register(SlotSavePayload.ID, SlotSavePayload.PACKET_CODEC);
	}
}
