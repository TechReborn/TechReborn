package techreborn.packets.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import techreborn.TechReborn;

public record RefundPayload() implements CustomPayload {
	public static final CustomPayload.Id<RefundPayload> ID = new CustomPayload.Id<>(Identifier.of(TechReborn.MOD_ID, "refund"));
	public static final PacketCodec<RegistryByteBuf, RefundPayload> CODEC = PacketCodec.unit(new RefundPayload());

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
