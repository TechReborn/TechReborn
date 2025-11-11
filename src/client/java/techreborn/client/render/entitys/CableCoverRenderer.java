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

import techreborn.blockentity.cable.CableBlockEntity;
import techreborn.blocks.cable.CableBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CableCoverRenderer implements BlockEntityRenderer<CableBlockEntity> {

	public CableCoverRenderer(BlockEntityRendererProvider.Context ctx) {
	}

	@Override
	public void render(CableBlockEntity blockEntity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, Vec3 cameraPos) {
		if (!blockEntity.getBlockState().getValue(CableBlock.COVERED) || blockEntity.getLevel() == null) {
			return;
		}

		final BlockRenderDispatcher blockRenderManager = Minecraft.getInstance().getBlockRenderer();
		final BlockState renderData = blockEntity.getRenderData();
		final BlockState coverState = renderData != null ? renderData : Blocks.OAK_PLANKS.defaultBlockState();
		final VertexConsumer consumer = vertexConsumers.getBuffer(ItemBlockRenderTypes.getRenderType(coverState));
		List<BlockModelPart> parts = blockRenderManager.getBlockModel(coverState).collectParts(RandomSource.create());
		blockRenderManager.renderBatched(coverState, blockEntity.getBlockPos(), blockEntity.getLevel(), matrices, consumer, true, parts);
	}

}
