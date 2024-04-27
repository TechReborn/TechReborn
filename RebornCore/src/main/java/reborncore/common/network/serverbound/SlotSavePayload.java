package reborncore.common.network.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import reborncore.common.blockentity.SlotConfiguration;

public record SlotSavePayload(BlockPos pos, SlotConfiguration.SlotConfig slotConfig) implements CustomPayload {
	public static final CustomPayload.Id<SlotSavePayload> ID = new CustomPayload.Id<>(new Identifier("reborncore:slot_save"));
	public static final PacketCodec<RegistryByteBuf, SlotSavePayload> PACKET_CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, SlotSavePayload::pos,
		SlotConfiguration.SlotConfig.PACKET_CODEC, SlotSavePayload::slotConfig,
		SlotSavePayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
