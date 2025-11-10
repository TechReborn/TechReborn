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

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import techreborn.blockentity.cable.CableBlockEntity;
import techreborn.blocks.cable.CableBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CableCoverRenderer implements BlockEntityRenderer<CableBlockEntity, CableCoverRenderer.CableCoverRenderState> {
	private final BlockRenderDispatcher blockRenderDispatcher;

	public CableCoverRenderer(BlockEntityRendererProvider.Context ctx) {
		this.blockRenderDispatcher = ctx.blockRenderDispatcher();
	}

	@Override
	public @NotNull CableCoverRenderState createRenderState() {
		return new CableCoverRenderState();
	}

	@Override
	public void extractRenderState(
		CableBlockEntity blockEntity,
		CableCoverRenderState state,
		float f,
		Vec3 vec3,
		@Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay
	) {
		BlockEntityRenderState.extractBase(blockEntity, state, crumblingOverlay);
		if (!blockEntity.getBlockState().getValue(CableBlock.COVERED) || blockEntity.getLevel() == null) {
			return;
		}
		final BlockState renderData = blockEntity.getRenderData();
		state.cover = renderData != null ? renderData : Blocks.OAK_PLANKS.defaultBlockState();
		state.layer = ItemBlockRenderTypes.getMovingBlockRenderType(state.cover);
		RandomSource random = RandomSource.create();
		random.setSeed(42L);
		state.parts = blockRenderDispatcher.getBlockModel(state.cover).collectParts(random);
		state.level = blockEntity.getLevel();
		state.blockRenderDispatcher = blockRenderDispatcher;
	}

	@Override
	public void submit(
		CableCoverRenderState state,
		PoseStack poseStack,
		SubmitNodeCollector submitNodeCollector,
		CameraRenderState cameraRenderState
	) {
		if (state.layer != null) {
			submitNodeCollector.submitCustomGeometry(poseStack, state.layer, state);
		}
	}

	public static class CableCoverRenderState extends BlockEntityRenderState implements SubmitNodeCollector.CustomGeometryRenderer {
		public RenderType layer;
		public List<BlockModelPart> parts;
		public Level level;
		public BlockRenderDispatcher blockRenderDispatcher;
		public BlockState cover;

		@Override
		public void render(PoseStack.Pose pose, VertexConsumer consumer) {
			PoseStack matrices = new PoseStack();
			matrices.last().set(pose);
			blockRenderDispatcher.renderBatched(cover, blockPos, level, matrices, consumer, true, parts);
		}
	}
}
