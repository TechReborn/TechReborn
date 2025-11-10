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

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.entity.state.TntRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import techreborn.entities.EntityNukePrimed;
import techreborn.init.TRContent;

/**
 * Created by Mark on 13/03/2016.
 */
public class NukeRenderer extends EntityRenderer<EntityNukePrimed, TntRenderState> {
	public NukeRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
		this.shadowRadius = 0.5F;
	}

	@Override
	public @NotNull TntRenderState createRenderState() {
		return new TntRenderState();
	}

	@Override
	public void submit(TntRenderState state, PoseStack matrixStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		matrixStack.pushPose();
		matrixStack.translate(1D, 0.5D, 0);
		if (state.fuseRemainingInTicks < 10.0F) {
			float h = 1.0F - state.fuseRemainingInTicks / 10.0F;
			h = Mth.clamp(h, 0.0F, 1.0F);
			h *= h;
			h *= h;
			float j = 1.0F + h * 0.3F;
			matrixStack.scale(j, j, j);
		}

		matrixStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
		matrixStack.translate(-0.5D, -0.5D, 0.5D);
		if (state.blockState != null) {
			TntMinecartRenderer.submitWhiteSolidBlock(state.blockState, matrixStack, submitNodeCollector, state.lightCoords, (int) state.fuseRemainingInTicks / 5 % 2 == 0, state.outlineColor);
		}
		matrixStack.popPose();
		super.submit(state, matrixStack, submitNodeCollector, cameraRenderState);
	}

	@Override
	public void extractRenderState(EntityNukePrimed entity, TntRenderState state, float f) {
		super.extractRenderState(entity, state, f);
		state.fuseRemainingInTicks = (float) entity.getFuse() - f + 1.0F;
		state.blockState = TRContent.NUKE.defaultBlockState();
	}
}
