package reborncore.common.network.clientbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ScreenHandlerUpdatePayload(byte[] data) implements CustomPayload {
	public static final Id<ScreenHandlerUpdatePayload> ID = new Id<>(Identifier.of("reborncore:screen_handler_update"));
	public static final PacketCodec<RegistryByteBuf, ScreenHandlerUpdatePayload> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.BYTE_ARRAY, ScreenHandlerUpdatePayload::data,
		ScreenHandlerUpdatePayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
