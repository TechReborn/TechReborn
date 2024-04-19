package techreborn.packets.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import techreborn.TechReborn;

public record ExperiencePayload (BlockPos pos) implements CustomPayload {
	public static final CustomPayload.Id<ExperiencePayload> ID = new CustomPayload.Id<>(new Identifier(TechReborn.MOD_ID, "experience"));
	public static final PacketCodec<RegistryByteBuf, ExperiencePayload> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, ExperiencePayload::pos,
		ExperiencePayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
