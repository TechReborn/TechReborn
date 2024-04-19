package techreborn.packets.serverbound;

import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import techreborn.TechReborn;

public record SuitNightVisionPayload() implements CustomPayload {
	public static final CustomPayload.Id<JumpPayload> ID = new CustomPayload.Id<>(new Identifier(TechReborn.MOD_ID, "suit_night_vision"));

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
