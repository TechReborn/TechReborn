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
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntMinecartEntityRenderer;
import net.minecraft.client.render.entity.state.TntEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import techreborn.entities.EntityNukePrimed;

/**
 * Created by Mark on 13/03/2016.
 */
public class NukeRenderer extends EntityRenderer<EntityNukePrimed, TntEntityRenderState> {
	private final BlockRenderManager blockRenderManager;

	public NukeRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
		this.shadowRadius = 0.5F;
		this.blockRenderManager = ctx.getBlockRenderManager();
	}

	@Override
	public TntEntityRenderState createRenderState() {
		return new TntEntityRenderState();
	}

	@Override
	public void render(TntEntityRenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		matrixStack.push();
		matrixStack.translate(1D, 0.5D, 0);
		if (state.fuse < 10.0F) {
			float h = 1.0F - state.fuse / 10.0F;
			h = MathHelper.clamp(h, 0.0F, 1.0F);
			h *= h;
			h *= h;
			float j = 1.0F + h * 0.3F;
			matrixStack.scale(j, j, j);
		}

		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0F));
		matrixStack.translate(-0.5D, -0.5D, 0.5D);
		TntMinecartEntityRenderer.renderFlashingBlock(blockRenderManager, state.blockState, matrixStack, vertexConsumerProvider, light, state.fuse / 5 % 2 == 0);
		matrixStack.pop();
		super.render(state, matrixStack, vertexConsumerProvider, light);
	}
}
