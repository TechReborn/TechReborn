package reborncore.common.network.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import reborncore.common.blockentity.SlotConfiguration;

public record ConfigSavePayload(BlockPos pos, SlotConfiguration slotConfig) implements CustomPayload {
	public static final CustomPayload.Id<ConfigSavePayload> ID = new CustomPayload.Id<>(new Identifier("reborncore:config_save"));
	public static final PacketCodec<RegistryByteBuf, ConfigSavePayload> PACKET_CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, ConfigSavePayload::pos,
		SlotConfiguration.PACKET_CODEC, ConfigSavePayload::slotConfig,
		ConfigSavePayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
