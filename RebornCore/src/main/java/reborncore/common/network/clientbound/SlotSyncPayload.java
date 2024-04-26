package reborncore.common.network.clientbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import reborncore.common.blockentity.SlotConfiguration;

public record SlotSyncPayload(BlockPos pos, SlotConfiguration.SlotConfig slotConfig) implements CustomPayload {
	public static final Id<SlotSyncPayload> ID = new Id<>(new Identifier("reborncore:slot_sync"));
	public static final PacketCodec<RegistryByteBuf, SlotSyncPayload> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, SlotSyncPayload::pos,
		SlotConfiguration.SlotConfig.PACKET_CODEC, SlotSyncPayload::slotConfig,
		SlotSyncPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
