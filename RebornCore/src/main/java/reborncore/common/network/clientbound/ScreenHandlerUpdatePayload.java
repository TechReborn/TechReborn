package reborncore.common.network.clientbound;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ScreenHandlerUpdatePayload(Int2ObjectMap<Object> updatedValue) implements CustomPayload {
	public static final Id<ScreenHandlerUpdatePayload> ID = new Id<>(new Identifier("reborncore:screen_handler_update"));
	public static final PacketCodec<RegistryByteBuf, ScreenHandlerUpdatePayload> PACKET_CODEC = PacketCodec.unit(new ScreenHandlerUpdatePayload(new Int2ObjectOpenHashMap<>()));

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
