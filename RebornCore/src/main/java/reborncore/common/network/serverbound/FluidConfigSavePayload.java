package reborncore.common.network.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import reborncore.common.blockentity.FluidConfiguration;
import reborncore.common.blockentity.SlotConfiguration;

public record FluidConfigSavePayload(BlockPos pos, FluidConfiguration.FluidConfig fluidConfiguration) implements CustomPayload {
	public static final Id<FluidConfigSavePayload> ID = new Id<>(Identifier.of("reborncore:config_save"));
	public static final PacketCodec<RegistryByteBuf, FluidConfigSavePayload> PACKET_CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, FluidConfigSavePayload::pos,
		FluidConfiguration.FluidConfig.PACKET_CODEC, FluidConfigSavePayload::fluidConfiguration,
		FluidConfigSavePayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
