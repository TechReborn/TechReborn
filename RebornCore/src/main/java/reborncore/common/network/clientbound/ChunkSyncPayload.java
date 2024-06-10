package reborncore.common.network.clientbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import reborncore.common.chunkloading.ChunkLoaderManager;

import java.util.List;

public record ChunkSyncPayload(List<ChunkLoaderManager.LoadedChunk> chunks) implements CustomPayload {
	public static final Id<ChunkSyncPayload> ID = new Id<>(Identifier.of("reborncore:sync_chunks"));
	public static final PacketCodec<RegistryByteBuf, ChunkSyncPayload> PACKET_CODEC = PacketCodec.tuple(
		ChunkLoaderManager.LoadedChunk.PACKET_CODEC.collect(PacketCodecs.toList()), ChunkSyncPayload::chunks,
		ChunkSyncPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
