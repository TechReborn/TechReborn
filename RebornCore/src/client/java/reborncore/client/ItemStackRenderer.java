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

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.joml.Matrix4fStack;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Initially taken from https://github.com/JamiesWhiteShirt/developer-mode/tree/experimental-item-render
 * and then ported to 1.15
 * Thanks 2xsaiko for fixing the lighting + odd issues above
 */
public class ItemStackRenderer implements HudRenderCallback {
	private static ProjectionMatrix2 guiProjectionMatrix;
	private static final int SIZE = 512;

	@Override
	public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
		if (!ItemStackRenderManager.RENDER_QUEUE.isEmpty()) {
			if (guiProjectionMatrix == null) {
				guiProjectionMatrix = new ProjectionMatrix2("gui", 1000.0F, 11000.0F, true);
			}
			ItemStack itemStack = ItemStackRenderManager.RENDER_QUEUE.remove();
			export(drawContext, itemStack, ItemStackRenderManager.RENDER_QUEUE.size());
		}
	}

	private void export(DrawContext drawContext, ItemStack stack, int queue) {
		MinecraftClient client = MinecraftClient.getInstance();
		Framebuffer framebuffer = client.getFramebuffer();
		GpuTexture gpuTexture = framebuffer.getColorAttachment();
		if (gpuTexture == null) {
			return;
		}
		// clear background
		RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(gpuTexture, 0, framebuffer.getDepthAttachment(), 1);

		// draw info
		Window window = client.getWindow();
		float scaleFactor = window.getScaleFactor();
		final int drawSize = Math.min(framebuffer.textureHeight, SIZE);
		int left = (int) (drawSize / scaleFactor) + 5;
		Identifier identifier = Registries.ITEM.getId(stack.getItem());
		drawContext.drawText(client.textRenderer, "Rendering " + identifier, left, 5, -1, false);
		drawContext.drawText(client.textRenderer, queue + " items left", left, 15, -1, false);

		// draw item stack
		RenderSystem.backupProjectionMatrix();
		RenderSystem.setProjectionMatrix(
			guiProjectionMatrix.set(window.getFramebufferWidth() / scaleFactor, window.getFramebufferHeight() / scaleFactor),
			ProjectionType.ORTHOGRAPHIC
		);
		Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
		matrix4fStack.pushMatrix();
		matrix4fStack.translate(0, 0, -11000);
		VertexConsumerProvider.Immediate vertexConsumers = client.getBufferBuilders().getEntityVertexConsumers();
		MatrixStack matrices = new MatrixStack();
		matrices.push();
		float drawScale = drawSize / (16 * scaleFactor);
		matrices.scale(drawScale, drawScale, drawScale);
		ItemRenderState itemRenderState = new ItemRenderState();
		client.getItemModelManager().clearAndUpdate(itemRenderState, stack, ItemDisplayContext.GUI, client.world, client.player, 0);
		matrices.translate(8, 8, 150);
		matrices.scale(16.0F, -16.0F, 16.0F);
		boolean bl = !itemRenderState.isSideLit();
		DiffuseLighting diffuseLighting = MinecraftClient.getInstance().gameRenderer.getDiffuseLighting();
		if (bl) {
			diffuseLighting.setShaderLights(DiffuseLighting.Type.ITEMS_FLAT);
		} else {
			diffuseLighting.setShaderLights(DiffuseLighting.Type.ITEMS_3D);
		}
		itemRenderState.render(matrices, vertexConsumers, 15728880, OverlayTexture.DEFAULT_UV);
		vertexConsumers.draw();
		matrix4fStack.popMatrix();
		RenderSystem.restoreProjectionMatrix();

		// export image
		int pixelSize = gpuTexture.getFormat().pixelSize();
		final GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(
			() -> "Export buffer",
			9,
			framebuffer.textureWidth * framebuffer.textureHeight * pixelSize
		);
		final CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
		commandEncoder.copyTextureToBuffer(
			gpuTexture,
			gpuBuffer,
			0,
			() -> {
				try (GpuBuffer.MappedView readView = commandEncoder.mapBuffer(gpuBuffer, true, false)) {
					ByteBuffer imageData = readView.data();
					NativeImage nativeImage = null;
					try {
						int scale = drawSize < SIZE ? 2 : 1;
						int imageSize = drawSize * scale;
						nativeImage = new NativeImage(imageSize, imageSize, false);
						for (int rowIndex = 0, maxRowIndex = imageSize - 1, scaledRowIndex; rowIndex < drawSize; rowIndex++) {
							scaledRowIndex = rowIndex * scale;
							for (int colIndex = 0, scaledColIndex; colIndex < drawSize; colIndex++) {
								scaledColIndex = colIndex * scale;
								int color = imageData.getInt((colIndex + rowIndex * drawSize) * pixelSize);
								for (int x = 0; x < scale; x++) {
									for (int y = 0; y < scale; y++) {
										nativeImage.setColor(scaledColIndex + x, maxRowIndex - scaledRowIndex - y, color);
									}
								}
							}
						}
						if (drawSize < SIZE) {
							NativeImage destroy = null;
							try {
								NativeImage resizedImage = new NativeImage(SIZE, SIZE, false);
								destroy = resizedImage;
								nativeImage.resizeSubRectTo(0, 0, imageSize, imageSize, resizedImage);
								destroy = nativeImage;
								nativeImage = resizedImage;
							} catch(Exception ignored) {}
							finally {
								if (destroy != null) {
									try {
										destroy.close();
									} catch(Exception ignored) {}
								}
							}
						}
						Path path = FabricLoader.getInstance().getGameDir().resolve("item_renderer")
							.resolve(identifier.getNamespace()).resolve(identifier.getPath() + ".png");
						Files.createDirectories(path.getParent());
						nativeImage.writeTo(path);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (nativeImage != null) {
							try {
								nativeImage.close();
							} catch(Exception ignored) {}
						}
					}
				}
				gpuBuffer.close();
			},
			0,
			0,
			framebuffer.textureHeight - drawSize,
			drawSize,
			drawSize
		);
	}
}
