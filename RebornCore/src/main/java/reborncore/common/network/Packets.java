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
