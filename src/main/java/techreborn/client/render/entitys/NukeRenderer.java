/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.TntMinecartEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import techreborn.entities.EntityNukePrimed;
import techreborn.init.TRContent;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Mark on 13/03/2016.
 */
public class NukeRenderer extends EntityRenderer<EntityNukePrimed> {

	public NukeRenderer(EntityRenderDispatcher renderManager) {
		super(renderManager);
		this.shadowRadius = 0.5F;
	}

	@Nullable
	@Override
	public Identifier getTexture(EntityNukePrimed entityNukePrimed) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
	}

	@Override
	public void render(EntityNukePrimed entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		matrixStack.push();
		matrixStack.translate(0.0D, 0.5D, 0.0D);
		if ((float) entity.getFuseTimer() - g + 1.0F < 10.0F) {
			float h = 1.0F - ((float) entity.getFuseTimer() - g + 1.0F) / 10.0F;
			h = MathHelper.clamp(h, 0.0F, 1.0F);
			h *= h;
			h *= h;
			float j = 1.0F + h * 0.3F;
			matrixStack.scale(j, j, j);
		}

		matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90.0F));
		matrixStack.translate(-0.5D, -0.5D, 0.5D);
		TntMinecartEntityRenderer.renderFlashingBlock(TRContent.NUKE.getDefaultState(), matrixStack, vertexConsumerProvider, i, entity.getFuseTimer() / 5 % 2 == 0);
		matrixStack.pop();
		super.render(entity, f, g, matrixStack, vertexConsumerProvider, i);
	}
}
