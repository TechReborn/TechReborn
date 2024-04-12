package reborncore.common.network.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record IoSavePayload(BlockPos pos, int slotID, boolean input, boolean output, boolean filter) implements CustomPayload {
	public static final CustomPayload.Id<IoSavePayload> ID = new CustomPayload.Id<>(new Identifier("reborncore:io_save"));
	public static final PacketCodec<RegistryByteBuf, IoSavePayload> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, IoSavePayload::pos,
		PacketCodecs.INTEGER, IoSavePayload::slotID,
		PacketCodecs.BOOL, IoSavePayload::input,
		PacketCodecs.BOOL, IoSavePayload::output,
		PacketCodecs.BOOL, IoSavePayload::filter,
		IoSavePayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
