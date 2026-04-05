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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import techreborn.blockentity.cable.CableBlockEntity;
import techreborn.blocks.cable.CableBlock;

import java.util.ArrayList;
import java.util.List;

public class CableCoverRenderer implements BlockEntityRenderer<CableBlockEntity, CableCoverRenderer.CableCoverRenderState> {

	public CableCoverRenderer(BlockEntityRendererProvider.Context ctx) {
	}

	@Override
	public CableCoverRenderState createRenderState() {
		return new CableCoverRenderState();
	}

	@Override
	public void extractRenderState(
		CableBlockEntity blockEntity,
		CableCoverRenderState state,
		float f,
		Vec3 vec3,
		ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay
	) {
		BlockEntityRenderState.extractBase(blockEntity, state, crumblingOverlay);
		if (!blockEntity.getBlockState().getValue(CableBlock.COVERED) || blockEntity.getLevel() == null) {
			return;
		}
		final BlockState renderData = blockEntity.getRenderData();
		state.cover = renderData != null ? renderData : Blocks.OAK_PLANKS.defaultBlockState();
		// TODO 26.1: render type for covers is now baked into model quads; using cutout as default
		state.layer = Sheets.cutoutBlockSheet();
		RandomSource random = RandomSource.create();
		random.setSeed(42L);
		state.parts = new ArrayList<>();
		Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state.cover).collectParts(random, state.parts);
	}

	@Override
	public void submit(
		CableCoverRenderState state,
		PoseStack poseStack,
		SubmitNodeCollector submitNodeCollector,
		CameraRenderState cameraRenderState
	) {
		if (state.layer != null && state.parts != null && !state.parts.isEmpty()) {
			submitNodeCollector.submitBlockModel(poseStack, state.layer, state.parts, new int[0], state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		}
	}

	public static class CableCoverRenderState extends BlockEntityRenderState {
		public RenderType layer;
		public List<BlockStateModelPart> parts;
		public BlockState cover;
	}
}
