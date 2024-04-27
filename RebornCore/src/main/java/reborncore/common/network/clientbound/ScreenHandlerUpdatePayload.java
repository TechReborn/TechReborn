package reborncore.common.network.clientbound;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ScreenHandlerUpdatePayload(Int2ObjectMap<Object> updatedValue) implements CustomPayload {
	public static final Id<ScreenHandlerUpdatePayload> ID = new Id<>(new Identifier("reborncore:screen_handler_update"));

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
