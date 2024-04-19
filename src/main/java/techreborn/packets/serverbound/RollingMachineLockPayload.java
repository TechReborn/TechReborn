package techreborn.packets.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import techreborn.TechReborn;

public record RollingMachineLockPayload(BlockPos pos, boolean locked) implements CustomPayload {
	public static final CustomPayload.Id<RollingMachineLockPayload> ID = new CustomPayload.Id<>(new Identifier(TechReborn.MOD_ID, "rolling_machine_lock"));
	public static final PacketCodec<RegistryByteBuf, RollingMachineLockPayload> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, RollingMachineLockPayload::pos,
		PacketCodecs.BOOL, RollingMachineLockPayload::locked,
		RollingMachineLockPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
