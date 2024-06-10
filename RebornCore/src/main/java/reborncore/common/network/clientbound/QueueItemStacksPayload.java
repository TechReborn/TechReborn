package reborncore.common.network.clientbound;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.List;

public record QueueItemStacksPayload(List<ItemStack> stacks) implements CustomPayload {
	public static final Id<QueueItemStacksPayload> ID = new Id<>(Identifier.of("reborncore:stacks_to_render"));
	public static final PacketCodec<RegistryByteBuf, QueueItemStacksPayload> PACKET_CODEC = PacketCodec.tuple(
		ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()), QueueItemStacksPayload::stacks,
		QueueItemStacksPayload::new
	);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
