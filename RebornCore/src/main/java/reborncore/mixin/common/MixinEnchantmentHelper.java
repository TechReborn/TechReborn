package reborncore.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import reborncore.api.items.EnchantmentTargetHandler;

@Mixin(EnchantmentHelper.class)
public class MixinEnchantmentHelper {

	@WrapOperation(method = "getPossibleEntries", at= @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentTarget;isAcceptableItem(Lnet/minecraft/item/Item;)Z"))
	private static boolean modifyAcceptableItem(EnchantmentTarget target, Item item, Operation<Boolean> original){
		if (item instanceof EnchantmentTargetHandler) {
			return ((EnchantmentTargetHandler) item).modifyEnchantmentApplication(target);
		}
		return target.isAcceptableItem(item);
	}
}
