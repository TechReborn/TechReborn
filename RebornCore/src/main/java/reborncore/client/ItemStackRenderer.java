/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.registry.Registry;
import org.lwjgl.opengl.GL12;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Initially take from https://github.com/JamiesWhiteShirt/developer-mode/tree/experimental-item-render and then ported to 1.15
 * Thanks 2xsaiko for fixing the lighting + odd issues above
 */
public class ItemStackRenderer implements HudRenderCallback {

	private static final int SIZE = 512;

	@Override
	public void onHudRender(MatrixStack matrixStack, float v) {
		if (!ItemStackRenderManager.RENDER_QUEUE.isEmpty()) {
			ItemStack itemStack = ItemStackRenderManager.RENDER_QUEUE.remove();
			Identifier id = Registry.ITEM.getId(itemStack.getItem());
			MinecraftClient.getInstance().textRenderer.draw(matrixStack, "Rendering " + id + ", " + ItemStackRenderManager.RENDER_QUEUE.size() + " items left", 5, 5, -1);
			export(id, itemStack);
		}
	}

	private void export(Identifier identifier, ItemStack item) {
		RenderSystem.setProjectionMatrix(Matrix4f.projectionMatrix(0, 16, 0, 16, 1000, 3000));
		MatrixStack stack = RenderSystem.getModelViewStack();
		stack.loadIdentity();
		stack.translate(0, 0, -2000);
		DiffuseLighting.enableGuiDepthLighting();
		RenderSystem.applyModelViewMatrix();

		Framebuffer framebuffer = new SimpleFramebuffer(SIZE, SIZE, true, MinecraftClient.IS_SYSTEM_MAC);

		try (NativeImage nativeImage = new NativeImage(SIZE, SIZE, true)) {
			framebuffer.setClearColor(0, 0, 0, 0);
			framebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);

			framebuffer.beginWrite(true);
			GlStateManager._clear(GL12.GL_COLOR_BUFFER_BIT | GL12.GL_DEPTH_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);
			MinecraftClient.getInstance().getItemRenderer().renderInGui(item, 0, 0);
			framebuffer.endWrite();

			// framebuffer.beginRead
			framebuffer.method_35610();
			nativeImage.loadFromTextureImage(0, false);
			nativeImage.mirrorVertically();
			framebuffer.endRead();

			try {
				Path path = FabricLoader.getInstance().getGameDir().resolve("item_renderer").resolve(identifier.getNamespace()).resolve(identifier.getPath() + ".png");
				Files.createDirectories(path.getParent());
				nativeImage.writeTo(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		framebuffer.delete();
	}
}
