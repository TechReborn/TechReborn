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
import net.minecraft.client.gui.DrawContext;
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
	public static Sprite getSprite(Identifier identifier) {
		return MinecraftClient.getInstance().getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).apply(identifier);
	}

	public static void drawGradientRect(DrawContext drawContext, int zLevel, int left, int top, int right, int bottom, int startColor, int endColor) {
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

		bufferBuilder.vertex(drawContext.getMatrices().peek().getPositionMatrix(), right, top, zLevel).color(g, h, i, f).next();
		bufferBuilder.vertex(drawContext.getMatrices().peek().getPositionMatrix(), left, top, zLevel).color(g, h, i, f).next();
		bufferBuilder.vertex(drawContext.getMatrices().peek().getPositionMatrix(), left, bottom, zLevel).color(k, l, m, j).next();
		bufferBuilder.vertex(drawContext.getMatrices().peek().getPositionMatrix(), right, bottom, zLevel).color(k, l, m, j).next();

		tessellator.draw();
		RenderSystem.disableBlend();

	}

}
