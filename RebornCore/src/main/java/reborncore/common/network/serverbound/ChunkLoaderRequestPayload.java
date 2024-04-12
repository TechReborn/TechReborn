package reborncore.common.network.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record ChunkLoaderRequestPayload(BlockPos pos) implements CustomPayload {
	public static final CustomPayload.Id<ChunkLoaderRequestPayload> ID = new CustomPayload.Id<>(new Identifier("reborncore:chunk_loader_request"));
	public static final PacketCodec<RegistryByteBuf, ChunkLoaderRequestPayload> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, ChunkLoaderRequestPayload::pos,
		ChunkLoaderRequestPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
