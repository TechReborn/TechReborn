package techreborn.packets.serverbound;

import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import techreborn.TechReborn;

public record QuantumSuitSprintPayload() implements CustomPayload {
	public static final CustomPayload.Id<JumpPayload> ID = new CustomPayload.Id<>(new Identifier(TechReborn.MOD_ID, "quantum_suit_sprint"));

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}