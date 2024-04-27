package techreborn.packets.clientbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import techreborn.TechReborn;

public record OpenManualPayload() implements CustomPayload {
	public static final CustomPayload.Id<OpenManualPayload> ID = new CustomPayload.Id<>(new Identifier(TechReborn.MOD_ID, "open_manual"));
	public static final PacketCodec<RegistryByteBuf, OpenManualPayload> CODEC = PacketCodec.unit(new OpenManualPayload());

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
