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

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.util.Tank;

/**
 * Created by Gigabit101 on 08/08/2016.
 */
public class RenderUtil {
	public static TextureManager engine() {
		return MinecraftClient.getInstance().getTextureManager();
	}

	public static void bindBlockTexture() {
		RenderSystem.setShaderTexture(0, PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
	}

	public static Sprite getStillTexture(FluidInstance fluid) {
		if (fluid == null || fluid.getFluid() == null) {
			return null;
		}
		return getStillTexture(fluid.getFluid());
	}

	public static Sprite getSprite(Identifier identifier) {
		return MinecraftClient.getInstance().getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).apply(identifier);
	}

	public static Sprite getStillTexture(Fluid fluid) {
		FluidRenderHandler fluidRenderHandler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);
		if (fluidRenderHandler != null) {
			return fluidRenderHandler.getFluidSprites(MinecraftClient.getInstance().world, BlockPos.ORIGIN, fluid.getDefaultState())[0];
		}
		return null;
	}

	public static void renderGuiTank(Tank tank, double x, double y, double zLevel, double width, double height) {
		renderGuiTank(tank.getFluidInstance(), tank.getFluidValueCapacity(), tank.getFluidAmount(), x, y, zLevel, width, height);
	}

	public static void renderGuiTank(FluidInstance fluid, FluidValue capacity, FluidValue amount, double x, double y, double zLevel,
									double width, double height) {
		if (fluid == null || fluid.getFluid() == null || fluid.getAmount().lessThanOrEqual(FluidValue.EMPTY)) {
			return;
		}

		Sprite icon = getStillTexture(fluid);
		if (icon == null) {
			return;
		}

		int renderAmount = (int) Math.max(Math.min(height, amount.getRawValue() * height / capacity.getRawValue()), 1);
		int posY = (int) (y + height - renderAmount);

		bindBlockTexture();
		int color = FluidRenderHandlerRegistry.INSTANCE.get(fluid.getFluid()).getFluidColor(null, null, fluid.getFluid().getDefaultState());
		float r = (float) (color >> 16 & 0xFF) / 255.0F;
		float g = (float) (color >> 8 & 0xFF) / 255.0F;
		float b = (float) (color & 0xFF) / 255.0F;
		RenderSystem.setShaderColor(r, g, b, 1.0F);

		RenderSystem.enableBlend();
		RenderLayer.getTranslucent().startDrawing();
		for (int i = 0; i < width; i += 16) {
			for (int j = 0; j < renderAmount; j += 16) {
				int drawWidth = (int) Math.min(width - i, 16);
				int drawHeight = Math.min(renderAmount - j, 16);

				int drawX = (int) (x + i);
				int drawY = posY + j;

				float minU = icon.getMinU();
				float maxU = icon.getMaxU();
				float minV = icon.getMinV();
				float maxV = icon.getMaxV();

				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder tes = tessellator.getBuffer();
				tes.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
				tes.vertex(drawX, drawY + drawHeight, 0)
					.color(r, g, b, 1.0F)
					.texture(minU, minV + (maxV - minV) * drawHeight / 16F)
					.light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
					.normal(0, 1, 0)
					.next();
				tes.vertex(drawX + drawWidth, drawY + drawHeight, 0)
					.color(r, g, b, 1.0F)
					.texture(minU + (maxU - minU) * drawWidth / 16F, minV + (maxV - minV) * drawHeight / 16F)
					.light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
					.normal(0, 1, 0)
					.next();
				tes.vertex(drawX + drawWidth, drawY, 0)
					.color(r, g, b, 1.0F)
					.texture(minU + (maxU - minU) * drawWidth / 16F, minV)
					.light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
					.normal(0, 1, 0)
					.next();
				tes.vertex(drawX, drawY, 0)
					.color(r, g, b, 1.0F)
					.texture(minU, minV)
					.light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
					.normal(0, 1, 0)
					.next();
				tessellator.draw();
			}
		}
		RenderLayer.getTranslucent().endDrawing();
		RenderSystem.disableBlend();
	}

	public static void drawGradientRect(MatrixStack matrices, int zLevel, int left, int top, int right, int bottom, int startColor, int endColor) {
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

		float f = (startColor >> 24 & 0xFF) / 255.0F;
		float g = (startColor >> 16 & 0xFF) / 255.0F;
		float h = (startColor >> 8 & 0xFF) / 255.0F;
		float i = (startColor & 0xFF) / 255.0F;

		float j = (endColor >> 24 & 0xFF) / 255.0F;
		float k = (endColor >> 16 & 0xFF) / 255.0F;
		float l = (endColor >> 8 & 0xFF) / 255.0F;
		float m = (endColor & 0xFF) / 255.0F;

		bufferBuilder.vertex(matrices.peek().getPositionMatrix(), right, top, zLevel).color(g, h, i, f).next();
		bufferBuilder.vertex(matrices.peek().getPositionMatrix(), left, top, zLevel).color(g, h, i, f).next();
		bufferBuilder.vertex(matrices.peek().getPositionMatrix(), left, bottom, zLevel).color(k, l, m, j).next();
		bufferBuilder.vertex(matrices.peek().getPositionMatrix(), right, bottom, zLevel).color(k, l, m, j).next();

		tessellator.draw();
		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
	}

}
