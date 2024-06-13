package techreborn.packets.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import techreborn.TechReborn;

public record PumpRangePayload (BlockPos pos, int buttonAmount) implements CustomPayload {
	public static final CustomPayload.Id<PumpRangePayload> ID = new CustomPayload.Id<>(Identifier.of(TechReborn.MOD_ID, "pump_range"));
	public static final PacketCodec<RegistryByteBuf, PumpRangePayload> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, PumpRangePayload::pos,
		PacketCodecs.INTEGER, PumpRangePayload::buttonAmount,
		PumpRangePayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
