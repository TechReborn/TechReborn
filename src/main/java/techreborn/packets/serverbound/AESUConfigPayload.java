package techreborn.packets.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import techreborn.TechReborn;


public record AESUConfigPayload (BlockPos pos, int buttonID, boolean shift, boolean ctrl) implements CustomPayload {
	public static final CustomPayload.Id<AESUConfigPayload> ID = new CustomPayload.Id<>(Identifier.of(TechReborn.MOD_ID, "aesu"));
	public static final PacketCodec<RegistryByteBuf, AESUConfigPayload> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, AESUConfigPayload::pos,
		PacketCodecs.INTEGER, AESUConfigPayload::buttonID,
		PacketCodecs.BOOL, AESUConfigPayload::shift,
		PacketCodecs.BOOL, AESUConfigPayload::ctrl,
		AESUConfigPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
