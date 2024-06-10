package techreborn.packets.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import techreborn.TechReborn;

public record PumpDepthPayload(BlockPos pos, int buttonAmount) implements CustomPayload {
	public static final CustomPayload.Id<PumpDepthPayload> ID = new CustomPayload.Id<>(Identifier.of(TechReborn.MOD_ID, "pump_depth"));
	public static final PacketCodec<RegistryByteBuf, PumpDepthPayload> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, PumpDepthPayload::pos,
		PacketCodecs.INTEGER, PumpDepthPayload::buttonAmount,
		PumpDepthPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
