package techreborn.packets.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import techreborn.TechReborn;

public record SuitNightVisionPayload() implements CustomPayload {
	public static final CustomPayload.Id<SuitNightVisionPayload> ID = new CustomPayload.Id<>(new Identifier(TechReborn.MOD_ID, "suit_night_vision"));
	public static final PacketCodec<RegistryByteBuf, SuitNightVisionPayload> CODEC = PacketCodec.unit(new SuitNightVisionPayload());

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
