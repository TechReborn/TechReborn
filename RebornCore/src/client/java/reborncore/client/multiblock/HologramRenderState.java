/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2025 TeamReborn
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

package reborncore.client.multiblock;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.LightCoordsUtil;

public interface HologramRenderState {
	void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector);

	default void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, float scale) {
		poseStack.pushPose();
		poseStack.translate(x(), y(), z());
		poseStack.translate(0.5, 0.5, 0.5);
		poseStack.scale(scale, scale, scale);
		submit(poseStack, submitNodeCollector);
		poseStack.popPose();
	}

	int x();

	int y();

	int z();

	record FluidItem(int x, int y, int z, ItemStackRenderState state) implements HologramRenderState {
		@Override
		public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
			state.submit(poseStack, submitNodeCollector, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
		}
	}

	record Block(int x, int y, int z, BlockModelRenderState blockModelRenderState) implements HologramRenderState {
		@Override
		public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
			poseStack.translate(-0.5, -0.5, -0.5);
			blockModelRenderState.submit(poseStack, submitNodeCollector, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
		}
	}
}
