/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
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

package reborncore.client;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import reborncore.common.misc.MultiBlockBreakingTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BlockOutlineRenderer implements WorldRenderEvents.BlockOutline {

	@Override
	public boolean onBlockOutline(WorldRenderContext worldRenderContext, WorldRenderContext.BlockOutlineContext context) {
		List<VoxelShape> shapes = new ArrayList<>();

		World world = context.entity().world;
		BlockPos targetPos = context.blockPos();

		if (context.entity() == MinecraftClient.getInstance().player) {
			ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;

			ItemStack stack = clientPlayerEntity.getMainHandStack();
			if (stack.isEmpty()) {
				return true;
			}

			if (stack.getItem() instanceof MultiBlockBreakingTool) {
				Set<BlockPos> blockPosList = ((MultiBlockBreakingTool) stack.getItem()).getBlocksToBreak(stack, clientPlayerEntity.world, targetPos, clientPlayerEntity);

				for (BlockPos pos : blockPosList) {
					if (pos.equals(targetPos)) {
						continue;
					}

					BlockState blockState = world.getBlockState(pos);
					shapes.add(blockState.getOutlineShape(world, pos, ShapeContext.of(clientPlayerEntity)).offset(pos.getX() - targetPos.getX(), pos.getY() - targetPos.getY(), pos.getZ() - targetPos.getZ()));

				}
			}
		}

		if (!shapes.isEmpty()) {
			VoxelShape shape = context.blockState().getOutlineShape(world, targetPos, ShapeContext.of(context.entity()));

			for (VoxelShape voxelShape : shapes) {
				shape = VoxelShapes.union(shape, voxelShape);
			}

			WorldRenderer.drawShapeOutline(worldRenderContext.matrixStack(), worldRenderContext.consumers().getBuffer(RenderLayer.getLines()), shape, (double)targetPos.getX() - context.cameraX(), (double)targetPos.getY() - context.cameraY(), (double)targetPos.getZ() - context.cameraZ(), 0.0F, 0.0F, 0.0F, 0.4F);
		}

		return true;
	}
}
