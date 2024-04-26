package reborncore.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import reborncore.common.entity.RebornCoreEntityAttributes;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity {
	private MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@ModifyExpressionValue(method = {"tickMovement", "canSprint"}, at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerAbilities;allowFlying:Z"))
	public boolean allowFlying(boolean allowFlying) {
		return allowFlying || getAttributeValue(RebornCoreEntityAttributes.CREATIVE_FLIGHT) > 0;
	}
}
