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
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.Lightmap;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface HologramRenderState {
	BlockPos OUT_OF_WORLD_POS = new BlockPos(0, 260, 0); // Bad hack; disables lighting

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

	record Block(
		BlockRenderDispatcher blockRenderManager, Level view, int x, int y, int z, RenderType layer, BlockState state, List<BlockModelPart> parts
	) implements HologramRenderState, SubmitNodeCollector.CustomGeometryRenderer {
		@Override
		public void render(PoseStack.Pose pose, VertexConsumer vertexConsumer) {
			PoseStack matrix = new PoseStack();
			matrix.mulPose(pose.pose());
			blockRenderManager.renderBatched(state, OUT_OF_WORLD_POS, view, matrix, vertexConsumer, false, parts);
		}

		@Override
		public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
			poseStack.translate(-0.5, -0.5, -0.5);
			submitNodeCollector.submitCustomGeometry(poseStack, layer, this);
		}
	}
}
