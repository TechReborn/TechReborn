package techreborn.packets.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import techreborn.TechReborn;

public record DetectorRadiusPayload(BlockPos pos, int buttonAmount) implements CustomPayload {
	public static final CustomPayload.Id<DetectorRadiusPayload> ID = new CustomPayload.Id<>(Identifier.of(TechReborn.MOD_ID, "detector_radius"));
	public static final PacketCodec<RegistryByteBuf, DetectorRadiusPayload> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, DetectorRadiusPayload::pos,
		PacketCodecs.INTEGER, DetectorRadiusPayload::buttonAmount,
		DetectorRadiusPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
