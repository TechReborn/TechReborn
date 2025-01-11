/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package techreborn.datagen.mixin

import net.minecraft.block.Block
import net.minecraft.client.data.ModelIds
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import techreborn.datagen.models.TexturePaths

@Mixin(ModelIds.class)
class MixinModelIds {
	@Inject(method = "getItemModelId(Lnet/minecraft/item/Item;)Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
	private static void getItemModelId(Item item, CallbackInfoReturnable<Identifier> cir) {
		TexturePaths.ifPresent(item, cir::setReturnValue)
	}

	@Inject(method = "getItemSubModelId(Lnet/minecraft/item/Item;Ljava/lang/String;)Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
	private static void getItemSubModelId(Item item, String suffix, CallbackInfoReturnable<Identifier> cir) {
		TexturePaths.ifPresent(item, suffix, cir::setReturnValue)
	}

	@Inject(method = "getBlockModelId(Lnet/minecraft/block/Block;)Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
	private static void getBlockModelId(Block block, CallbackInfoReturnable<Identifier> cir) {
		TexturePaths.ifPresent(block, cir::setReturnValue)
	}

	@Inject(method = "getBlockSubModelId(Lnet/minecraft/block/Block;Ljava/lang/String;)Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
	private static void getBlockSubModelId(Block block, String suffix, CallbackInfoReturnable<Identifier> cir) {
		TexturePaths.ifPresent(block, suffix, cir::setReturnValue)
	}
}
