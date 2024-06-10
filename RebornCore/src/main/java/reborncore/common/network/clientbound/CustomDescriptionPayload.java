package reborncore.common.network.clientbound;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import reborncore.common.blockentity.SlotConfiguration;

public record CustomDescriptionPayload(BlockPos pos, NbtCompound nbt) implements CustomPayload {
	public static final Id<CustomDescriptionPayload> ID = new Id<>(Identifier.of("reborncore:custom_description"));
	public static final PacketCodec<RegistryByteBuf, CustomDescriptionPayload> PACKET_CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, CustomDescriptionPayload::pos,
		PacketCodecs.NBT_COMPOUND, CustomDescriptionPayload::nbt,
		CustomDescriptionPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
