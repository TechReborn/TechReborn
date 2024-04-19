package techreborn.packets.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import techreborn.TechReborn;

public record JumpPayload (BlockPos pos) implements CustomPayload {
	public static final CustomPayload.Id<JumpPayload> ID = new CustomPayload.Id<>(new Identifier(TechReborn.MOD_ID, "jump"));
	public static final PacketCodec<RegistryByteBuf, JumpPayload> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, JumpPayload::pos,
		JumpPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}