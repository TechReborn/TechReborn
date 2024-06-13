package techreborn.packets.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import techreborn.TechReborn;

public record QuantumSuitSprintPayload() implements CustomPayload {
	public static final CustomPayload.Id<QuantumSuitSprintPayload> ID = new CustomPayload.Id<>(Identifier.of(TechReborn.MOD_ID, "quantum_suit_sprint"));
	public static final PacketCodec<RegistryByteBuf, QuantumSuitSprintPayload> CODEC = PacketCodec.unit(new QuantumSuitSprintPayload());

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}