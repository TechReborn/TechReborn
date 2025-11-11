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
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import reborncore.common.misc.MultiBlockBreakingTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BlockOutlineRenderer implements WorldRenderEvents.BlockOutline {

	@Override
	public boolean onBlockOutline(WorldRenderContext worldRenderContext, WorldRenderContext.BlockOutlineContext context) {
		List<VoxelShape> shapes = new ArrayList<>();

		Level world = context.entity().level();
		BlockPos targetPos = context.blockPos();

		if (context.entity() == Minecraft.getInstance().player) {
			LocalPlayer clientPlayerEntity = Minecraft.getInstance().player;

			ItemStack stack = clientPlayerEntity.getMainHandItem();
			if (stack.isEmpty()) {
				return true;
			}

			if (stack.getItem() instanceof MultiBlockBreakingTool) {
				Set<BlockPos> blockPosList = ((MultiBlockBreakingTool) stack.getItem()).getBlocksToBreak(stack, clientPlayerEntity.level(), targetPos, clientPlayerEntity);

				for (BlockPos pos : blockPosList) {
					if (pos.equals(targetPos)) {
						continue;
					}

					BlockState blockState = world.getBlockState(pos);
					shapes.add(blockState.getShape(world, pos, CollisionContext.of(clientPlayerEntity)).move(pos.getX() - targetPos.getX(), pos.getY() - targetPos.getY(), pos.getZ() - targetPos.getZ()));

				}
			}
		}

		if (!shapes.isEmpty()) {
			VoxelShape shape = context.blockState().getShape(world, targetPos, CollisionContext.of(context.entity()));

			for (VoxelShape voxelShape : shapes) {
				shape = Shapes.or(shape, voxelShape);
			}

			DebugRenderer.renderVoxelShape(worldRenderContext.matrixStack(), worldRenderContext.consumers().getBuffer(RenderType.lines()), shape, (double)targetPos.getX() - context.cameraX(), (double)targetPos.getY() - context.cameraY(), (double)targetPos.getZ() - context.cameraZ(), 0.0F, 0.0F, 0.0F, 0.4F, true);
		}

		return true;
	}
}
