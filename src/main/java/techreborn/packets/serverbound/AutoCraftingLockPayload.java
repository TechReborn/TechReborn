package techreborn.packets.serverbound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import techreborn.TechReborn;

public record AutoCraftingLockPayload(BlockPos pos, boolean locked) implements CustomPayload {
	public static final CustomPayload.Id<AutoCraftingLockPayload> ID = new CustomPayload.Id<>(Identifier.of(TechReborn.MOD_ID, "auto_crafting_lock"));
	public static final PacketCodec<RegistryByteBuf, AutoCraftingLockPayload> CODEC = PacketCodec.tuple(
		BlockPos.PACKET_CODEC, AutoCraftingLockPayload::pos,
		PacketCodecs.BOOL, AutoCraftingLockPayload::locked,
		AutoCraftingLockPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
