package reborncore.common.network.clientbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import reborncore.common.blockentity.FluidConfiguration;

public record FluidConfigSyncPayload(BlockPos pos, FluidConfiguration fluidConfiguration) implements CustomPayload {
	public static final Id<FluidConfigSyncPayload> ID = new Id<>(new Identifier("reborncore:fluid_config_sync"));
	public static final PacketCodec<RegistryByteBuf, FluidConfigSyncPayload> PACKET_CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, FluidConfigSyncPayload::pos,
		FluidConfiguration.PACKET_CODEC, FluidConfigSyncPayload::fluidConfiguration,
		FluidConfigSyncPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
