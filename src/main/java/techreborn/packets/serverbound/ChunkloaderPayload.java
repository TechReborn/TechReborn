package techreborn.packets.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import techreborn.TechReborn;

public record ChunkloaderPayload(BlockPos pos, int buttonID, boolean sync) implements CustomPayload {
	public static final CustomPayload.Id<ChunkloaderPayload> ID = new CustomPayload.Id<>(Identifier.of(TechReborn.MOD_ID, "chunkloader"));
	public static final PacketCodec<RegistryByteBuf, ChunkloaderPayload> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, ChunkloaderPayload::pos,
		PacketCodecs.INTEGER, ChunkloaderPayload::buttonID,
		PacketCodecs.BOOL, ChunkloaderPayload::sync,
		ChunkloaderPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
