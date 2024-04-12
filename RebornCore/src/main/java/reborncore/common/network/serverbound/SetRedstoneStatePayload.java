package reborncore.common.network.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import reborncore.common.blockentity.RedstoneConfiguration;

public record SetRedstoneStatePayload(BlockPos pos, String elementName, RedstoneConfiguration.State state) implements CustomPayload {
	public static final CustomPayload.Id<SetRedstoneStatePayload> ID = new CustomPayload.Id<>(new Identifier("reborncore:set_redstone_state"));
	public static final PacketCodec<RegistryByteBuf, SetRedstoneStatePayload> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, SetRedstoneStatePayload::pos,
		PacketCodecs.STRING, SetRedstoneStatePayload::elementName,
		RedstoneConfiguration.State.PACKET_CODEC, SetRedstoneStatePayload::state,
		SetRedstoneStatePayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
