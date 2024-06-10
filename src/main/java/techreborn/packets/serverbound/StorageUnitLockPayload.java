package techreborn.packets.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import techreborn.TechReborn;

public record StorageUnitLockPayload(BlockPos pos, boolean locked) implements CustomPayload {
	public static final CustomPayload.Id<StorageUnitLockPayload> ID = new CustomPayload.Id<>(Identifier.of(TechReborn.MOD_ID, "storage_unit_lock"));
	public static final PacketCodec<RegistryByteBuf, StorageUnitLockPayload> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, StorageUnitLockPayload::pos,
		PacketCodecs.BOOL, StorageUnitLockPayload::locked,
		StorageUnitLockPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
