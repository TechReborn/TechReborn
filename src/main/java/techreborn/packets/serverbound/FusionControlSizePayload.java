package techreborn.packets.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import techreborn.TechReborn;

public record FusionControlSizePayload(BlockPos pos, int sizeDelta) implements CustomPayload {
	public static final CustomPayload.Id<FusionControlSizePayload> ID = new CustomPayload.Id<>(Identifier.of(TechReborn.MOD_ID, "fusion_control_size"));
	public static final PacketCodec<RegistryByteBuf, FusionControlSizePayload> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, FusionControlSizePayload::pos,
		PacketCodecs.INTEGER, FusionControlSizePayload::sizeDelta,
		FusionControlSizePayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
