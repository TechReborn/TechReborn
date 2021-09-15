package reborncore.mixin.common;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import reborncore.mixin.ifaces.ServerPlayerEntityScreenHandler;

@Mixin(targets = "net.minecraft.server.network.ServerPlayerEntity$2")
public class MixinServerPlayerEntity implements ServerPlayerEntityScreenHandler {

	@Shadow
	ServerPlayerEntity field_29183;

	@Override
	public ServerPlayerEntity rc_getServerPlayerEntity() {
		return field_29183;
	}
}
