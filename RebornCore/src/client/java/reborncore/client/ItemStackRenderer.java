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

import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.Projection;
import net.minecraft.client.renderer.ProjectionMatrixBuffer;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4fStack;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ItemStackRenderer implements HudElement {
	private static final int SIZE = 512;

	private static ProjectionMatrixBuffer guiProjectionMatrix;
	private static Projection guiProjection;

	@Override
	public void extractRenderState(GuiGraphicsExtractor drawContext, DeltaTracker tickCounter) {
		ItemStack itemStack = ItemStackRenderManager.RENDER_QUEUE.poll();
		if (itemStack != null) {
			if (guiProjectionMatrix == null) {
				guiProjectionMatrix = new ProjectionMatrixBuffer("reborncore_item_export");
				guiProjection = new Projection();
			}

			export(drawContext, itemStack, ItemStackRenderManager.RENDER_QUEUE.size());
		}
	}

	private void export(GuiGraphicsExtractor drawContext, ItemStack stack, int queue) {
		Minecraft client = Minecraft.getInstance();
		RenderTarget framebuffer = client.gameRenderer.mainRenderTarget();
		GpuTexture gpuTexture = framebuffer.getColorTexture();
		GpuTexture depthTexture = framebuffer.getDepthTexture();
		if (gpuTexture == null || depthTexture == null) {
			return;
		}

		RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(gpuTexture, GuiRenderer.CLEAR_COLOR, depthTexture, 0.0);

		Window window = client.getWindow();
		float scaleFactor = window.getGuiScale();
		int drawSize = Math.min(framebuffer.height, SIZE);
		int left = (int) (drawSize / scaleFactor) + 5;
		Identifier identifier = BuiltInRegistries.ITEM.getKey(stack.getItem());
		drawContext.text(client.font, "Rendering " + identifier, left, 5, -1, false);
		drawContext.text(client.font, queue + " items left", left, 15, -1, false);

		RenderSystem.backupProjectionMatrix();
		Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
		modelViewStack.pushMatrix();

		try {
			guiProjection.setupOrtho(1000.0F, 11000.0F, window.getWidth() / scaleFactor, window.getHeight() / scaleFactor, true);
			RenderSystem.setProjectionMatrix(guiProjectionMatrix.getBuffer(guiProjection), ProjectionType.ORTHOGRAPHIC);
			modelViewStack.translate(0, 0, -11000);
			RenderSystem.outputColorTextureOverride = framebuffer.getColorTextureView();
			RenderSystem.outputDepthTextureOverride = framebuffer.getDepthTextureView();
			RenderSystem.enableScissorForRenderTypeDraws(0, framebuffer.height - drawSize, drawSize, drawSize);

			PoseStack poseStack = new PoseStack();
			poseStack.pushPose();
			float drawScale = drawSize / (16 * scaleFactor);
			poseStack.scale(drawScale, drawScale, drawScale);
			poseStack.translate(8, 8, 150);
			poseStack.scale(16.0F, -16.0F, 16.0F);

			ItemStackRenderState itemRenderState = new ItemStackRenderState();
			client.getItemModelResolver().updateForTopItem(itemRenderState, stack, ItemDisplayContext.GUI, client.level, client.player, 0);
			Lighting.Entry lighting = itemRenderState.usesBlockLight() ? Lighting.Entry.ITEMS_3D : Lighting.Entry.ITEMS_FLAT;
			client.gameRenderer.lighting().setupFor(lighting);

			SubmitNodeStorage submitNodeStorage = new SubmitNodeStorage();
			FeatureRenderDispatcher renderDispatcher = client.gameRenderer.featureRenderDispatcher();
			itemRenderState.submit(poseStack, submitNodeStorage, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
			renderDispatcher.renderAllFeatures(submitNodeStorage);
			poseStack.popPose();
		} finally {
			RenderSystem.disableScissorForRenderTypeDraws();
			RenderSystem.outputColorTextureOverride = null;
			RenderSystem.outputDepthTextureOverride = null;
			modelViewStack.popMatrix();
			RenderSystem.restoreProjectionMatrix();
		}

		copyExportToFile(framebuffer, gpuTexture, drawSize, identifier);
	}

	private static void copyExportToFile(RenderTarget framebuffer, GpuTexture gpuTexture, int drawSize, Identifier identifier) {
		int pixelSize = gpuTexture.getFormat().blockSize();
		GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(
			() -> "RebornCore item export buffer",
			GpuBuffer.USAGE_MAP_READ | GpuBuffer.USAGE_COPY_DST,
			(long) drawSize * drawSize * pixelSize
		);
		CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
		commandEncoder.copyTextureToBuffer(
			gpuTexture,
			gpuBuffer,
			0,
			() -> {
				try (GpuBufferSlice.MappedView readView = gpuBuffer.map(true, false)) {
					writeImage(readView.data(), pixelSize, drawSize, identifier);
				} finally {
					gpuBuffer.close();
				}
			},
			0,
			0,
			framebuffer.height - drawSize,
			drawSize,
			drawSize
		);
	}

	private static void writeImage(ByteBuffer imageData, int pixelSize, int drawSize, Identifier identifier) {
		NativeImage nativeImage = null;

		try {
			int scale = drawSize < SIZE ? 2 : 1;
			int imageSize = drawSize * scale;
			nativeImage = new NativeImage(imageSize, imageSize, false);

			for (int rowIndex = 0, maxRowIndex = imageSize - 1; rowIndex < drawSize; rowIndex++) {
				int scaledRowIndex = rowIndex * scale;

				for (int colIndex = 0; colIndex < drawSize; colIndex++) {
					int scaledColIndex = colIndex * scale;
					int color = imageData.getInt((colIndex + rowIndex * drawSize) * pixelSize);

					for (int x = 0; x < scale; x++) {
						for (int y = 0; y < scale; y++) {
							nativeImage.setPixelABGR(scaledColIndex + x, maxRowIndex - scaledRowIndex - y, color);
						}
					}
				}
			}

			if (drawSize < SIZE) {
				NativeImage resizedImage = new NativeImage(SIZE, SIZE, false);
				nativeImage.resizeSubRectTo(0, 0, imageSize, imageSize, resizedImage);
				nativeImage.close();
				nativeImage = resizedImage;
			}

			Path path = FabricLoader.getInstance().getGameDir().resolve("item_renderer")
				.resolve(identifier.getNamespace()).resolve(identifier.getPath() + ".png");
			Files.createDirectories(path.getParent());
			nativeImage.writeToFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (nativeImage != null) {
				nativeImage.close();
			}
		}
	}
}
