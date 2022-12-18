/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TeamReborn
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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import reborncore.common.blockentity.MultiblockWriter;

import java.util.function.BiPredicate;

/**
 * Renders a hologram
 */
@Environment(EnvType.CLIENT)
public
record HologramRenderer(BlockRenderView view, MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider,
						float scale) implements MultiblockWriter {
	private static final BlockPos OUT_OF_WORLD_POS = new BlockPos(0, 260, 0); // Bad hack; disables lighting

	@Override
	public MultiblockWriter add(int x, int y, int z, BiPredicate<BlockView, BlockPos> predicate, BlockState state) {
		final BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
		matrix.push();
		matrix.translate(x, y, z);
		matrix.translate(0.5, 0.5, 0.5);
		matrix.scale(scale, scale, scale);


		if (state.getBlock() instanceof FluidBlock) {
			FluidState fluidState = ((FluidBlock) state.getBlock()).getFluidState(state);
			MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(fluidState.getFluid().getBucketItem()), ModelTransformation.Mode.FIXED, 15728880, OverlayTexture.DEFAULT_UV, matrix, vertexConsumerProvider, 0);
		} else {
			matrix.translate(-0.5, -0.5, -0.5);
			VertexConsumer consumer = vertexConsumerProvider.getBuffer(RenderLayers.getBlockLayer(state));
			blockRenderManager.renderBlock(state, OUT_OF_WORLD_POS, view, matrix, consumer, false, Random.create());
		}

		matrix.pop();
		return this;
	}
}
