/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2023 TeamReborn
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

package reborncore.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import reborncore.client.gui.config.elements.GuiSpriteAtlasHolder;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
	@Shadow
	@Final
	private TextureManager textureManager;
	@Shadow
	@Final
	private ReloadableResourceManagerImpl resourceManager;

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/PaintingManager;<init>(Lnet/minecraft/client/texture/TextureManager;)V"))
	private void init(RunArgs args, CallbackInfo ci) {
		GuiSpriteAtlasHolder.INSTANCE = new GuiSpriteAtlasHolder(this.textureManager);
		this.resourceManager.registerReloader(GuiSpriteAtlasHolder.INSTANCE);
	}

	@Inject(method = "close", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/PaintingManager;close()V"))
	private void close(CallbackInfo ci) {
		GuiSpriteAtlasHolder.INSTANCE.close();
	}
}
