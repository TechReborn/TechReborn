package techreborn.datagen.mixin

import net.minecraft.block.Block
import net.minecraft.data.client.TextureMap
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import techreborn.datagen.models.TexturePaths

// Do as I say, not as I do. Only write mixins in Java!
@Mixin(TextureMap.class)
class MixinTextureMap {
	@Inject(method = "getId(Lnet/minecraft/item/Item;)Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
	private static void getId(Item item, CallbackInfoReturnable<Identifier> cir) {
		TexturePaths.blockPaths.each {
			if (it.key.asItem() == item) {
				cir.setReturnValue(new Identifier("techreborn", "item/${it.value}"))
			}
		}

		def itemPath = TexturePaths.itemPaths.get(item)

		if (itemPath) {
			cir.setReturnValue(new Identifier("techreborn", "item/${itemPath}"))
		}
	}

	@Inject(method = "getId(Lnet/minecraft/block/Block;)Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
	private static void getId(Block block, CallbackInfoReturnable<Identifier> cir) {
		TexturePaths.blockPaths.each {
			if (it.key == block) {
				cir.setReturnValue(new Identifier("techreborn", "block/${it.value}"))
			}
		}
	}
}
