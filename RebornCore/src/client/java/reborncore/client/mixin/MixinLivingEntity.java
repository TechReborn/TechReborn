package reborncore.client.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import reborncore.client.ClientNetworkManager;
import reborncore.common.network.ServerBoundPackets;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

	public MixinLivingEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(at = @At("HEAD"), method = "jump", cancellable = true)
	private void jump(CallbackInfo info) {
		if (this.getClass().equals(ServerPlayerEntity.class)) {
			ClientNetworkManager.sendToServer(ServerBoundPackets.createPacketJump(getBlockPos()));
		}
	}
}
