/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.client.render.entitys;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import techreborn.blocks.BlockNuke;
import techreborn.entities.EntityNukePrimed;
import techreborn.init.TRContent;

/**
 * Created by Mark on 13/03/2016.
 */
public class RenderNukePrimed extends EntityRenderer<EntityNukePrimed> {

	public RenderNukePrimed(EntityRenderDispatcher renderManager) {
		super(renderManager);
		this.field_4673 = 0.5F;
	}

	@Override
	public void doRender(EntityNukePrimed entity, double x, double y, double z, float entityYaw, float partialTicks) {
		BlockRenderManager blockrendererdispatcher = MinecraftClient.getInstance().getBlockRenderManager();
		GlStateManager.pushMatrix();
		GlStateManager.translatef((float) x, (float) y + 0.5F, (float) z);
		if ((float) entity.getFuseTimer() - partialTicks + 1.0F < 10.0F) {
			float f = 1.0F - ((float) entity.getFuseTimer() - partialTicks + 1.0F) / 10.0F;
			f = MathHelper.clamp(f, 0.0F, 1.0F);
			f = f * f;
			f = f * f;
			float f1 = 1.0F + f * 0.3F;
			GlStateManager.scalef(f1, f1, f1);
		}
		this.bindEntityTexture(entity);
		GlStateManager.translatef(-0.5F, -0.5F, 0.5F);
		blockrendererdispatcher.renderDynamic(TRContent.NUKE.getDefaultState(),
			entity.getBrightnessAtEyes());
		GlStateManager.translatef(0.0F, 0.0F, 1.0F);
		if (entity.getFuseTimer() / 5 % 2 == 0) {
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 772);
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1F);
			GlStateManager.polygonOffset(-3.0F, -3.0F);
			GlStateManager.enablePolygonOffset();
			blockrendererdispatcher.renderDynamic(
				TRContent.NUKE.getDefaultState().with(BlockNuke.OVERLAY, true), 1.0F);
			GlStateManager.polygonOffset(0.0F, 0.0F);
			GlStateManager.disablePolygonOffset();
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.disableBlend();
			GlStateManager.enableLighting();
		}
		GlStateManager.popMatrix();
		super.render(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected Identifier getEntityTexture(EntityNukePrimed entity) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEX;
	}
}
