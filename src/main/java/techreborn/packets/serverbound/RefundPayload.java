package techreborn.packets.serverbound;

import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import techreborn.TechReborn;

public record RefundPayload() implements CustomPayload {
	public static final CustomPayload.Id<RefundPayload> ID = new CustomPayload.Id<>(new Identifier(TechReborn.MOD_ID, "refund"));

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
