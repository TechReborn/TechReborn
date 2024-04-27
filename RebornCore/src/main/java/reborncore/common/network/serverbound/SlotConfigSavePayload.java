package reborncore.common.network.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import reborncore.common.blockentity.SlotConfiguration;

public record SlotConfigSavePayload(BlockPos pos, SlotConfiguration slotConfig) implements CustomPayload {
	public static final CustomPayload.Id<SlotConfigSavePayload> ID = new CustomPayload.Id<>(new Identifier("reborncore:slot_config_save"));
	public static final PacketCodec<RegistryByteBuf, SlotConfigSavePayload> PACKET_CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, SlotConfigSavePayload::pos,
		SlotConfiguration.PACKET_CODEC, SlotConfigSavePayload::slotConfig,
		SlotConfigSavePayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
