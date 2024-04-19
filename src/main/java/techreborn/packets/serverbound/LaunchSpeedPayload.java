package techreborn.packets.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import techreborn.TechReborn;

public record LaunchSpeedPayload(BlockPos pos, int buttonAmount) implements CustomPayload {
	public static final CustomPayload.Id<LaunchSpeedPayload> ID = new CustomPayload.Id<>(new Identifier(TechReborn.MOD_ID, "launch_speed"));
	public static final PacketCodec<RegistryByteBuf, LaunchSpeedPayload> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, LaunchSpeedPayload::pos,
		PacketCodecs.INTEGER, LaunchSpeedPayload::buttonAmount,
		LaunchSpeedPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
