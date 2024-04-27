package techreborn.packets;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import techreborn.packets.clientbound.OpenManualPayload;
import techreborn.packets.serverbound.AESUConfigPayload;
import techreborn.packets.serverbound.AutoCraftingLockPayload;
import techreborn.packets.serverbound.ChunkloaderPayload;
import techreborn.packets.serverbound.DetectorRadiusPayload;
import techreborn.packets.serverbound.ExperiencePayload;
import techreborn.packets.serverbound.FusionControlSizePayload;
import techreborn.packets.serverbound.JumpPayload;
import techreborn.packets.serverbound.LaunchSpeedPayload;
import techreborn.packets.serverbound.PumpDepthPayload;
import techreborn.packets.serverbound.PumpRangePayload;
import techreborn.packets.serverbound.QuantumSuitSprintPayload;
import techreborn.packets.serverbound.RefundPayload;
import techreborn.packets.serverbound.RollingMachineLockPayload;
import techreborn.packets.serverbound.StorageUnitLockPayload;
import techreborn.packets.serverbound.SuitNightVisionPayload;

public class Packets {
	public static void register() {
		clientbound(PayloadTypeRegistry.playS2C());
		serverbound(PayloadTypeRegistry.playC2S());
	}

	private static void clientbound(PayloadTypeRegistry<RegistryByteBuf> registry) {
		registry.register(OpenManualPayload.ID, OpenManualPayload.CODEC);
	}

	private static void serverbound(PayloadTypeRegistry<RegistryByteBuf> registry) {
		registry.register(AESUConfigPayload.ID, AESUConfigPayload.CODEC);
		registry.register(AutoCraftingLockPayload.ID, AutoCraftingLockPayload.CODEC);
		registry.register(ChunkloaderPayload.ID, ChunkloaderPayload.CODEC);
		registry.register(DetectorRadiusPayload.ID, DetectorRadiusPayload.CODEC);
		registry.register(ExperiencePayload.ID, ExperiencePayload.CODEC);
		registry.register(FusionControlSizePayload.ID, FusionControlSizePayload.CODEC);
		registry.register(JumpPayload.ID, JumpPayload.CODEC);
		registry.register(LaunchSpeedPayload.ID, LaunchSpeedPayload.CODEC);
		registry.register(PumpDepthPayload.ID, PumpDepthPayload.CODEC);
		registry.register(PumpRangePayload.ID, PumpRangePayload.CODEC);
		registry.register(QuantumSuitSprintPayload.ID, QuantumSuitSprintPayload.CODEC);
		registry.register(RefundPayload.ID, RefundPayload.CODEC);
		registry.register(RollingMachineLockPayload.ID, RollingMachineLockPayload.CODEC);
		registry.register(StorageUnitLockPayload.ID, StorageUnitLockPayload.CODEC);
		registry.register(SuitNightVisionPayload.ID, SuitNightVisionPayload.CODEC);
	}
}
