package reborncore.common.network.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record FluidIoSavePayload(BlockPos pos, boolean input, boolean output) implements CustomPayload {
	public static final Id<FluidIoSavePayload> ID = new Id<>(new Identifier("reborncore:fluid_io_save"));
	public static final PacketCodec<RegistryByteBuf, FluidIoSavePayload> PACKET_CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, FluidIoSavePayload::pos,
		PacketCodecs.BOOL, FluidIoSavePayload::input,
		PacketCodecs.BOOL, FluidIoSavePayload::output,
		FluidIoSavePayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
