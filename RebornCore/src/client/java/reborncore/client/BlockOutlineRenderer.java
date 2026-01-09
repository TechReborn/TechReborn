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

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import reborncore.common.misc.MultiBlockBreakingTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BlockOutlineRenderer implements LevelRenderEvents.BeforeBlockOutline, LevelRenderEvents.AfterBlockOutlineExtraction {
	public static RenderStateDataKey<List<VoxelShape>> KEY = RenderStateDataKey.create(() -> "MultiBlockBreakingTool");
	@Override
	public void afterBlockOutlineExtraction(LevelExtractionContext context, @Nullable HitResult result) {
		if (result instanceof BlockHitResult blockHitResult) {
			LocalPlayer player = Minecraft.getInstance().player;
			if (player == context.camera().entity()) {
				ItemStack stack = player.getMainHandItem();
				if (!stack.isEmpty() && stack.getItem() instanceof MultiBlockBreakingTool tool) {
					BlockOutlineRenderState state = context.levelState().blockOutlineRenderState;
					if (state == null) {
						return;
					}
					BlockPos targetPos = blockHitResult.getBlockPos();
					Level level = player.level();
					Set<BlockPos> blockPosList = tool.getBlocksToBreak(stack, level, targetPos, player);
					List<VoxelShape> shapes = new ArrayList<>();
					for (BlockPos pos : blockPosList) {
						if (pos.equals(targetPos)) {
							continue;
						}
						BlockState blockState = level.getBlockState(pos);
						shapes.add(blockState.getShape(level, pos, CollisionContext.of(player)).move(pos.getX() - targetPos.getX(), pos.getY() - targetPos.getY(), pos.getZ() - targetPos.getZ()));
					}
					if (shapes.isEmpty()) {
						return;
					}
					state.setData(KEY, shapes);
				}
			}
		}
	}

	@Override
	public boolean beforeBlockOutline(LevelRenderContext worldRenderContext, BlockOutlineRenderState context) {
		List<VoxelShape> shapes = context.getData(KEY);
		if (shapes != null) {
			VoxelShape shape = context.shape();

			for (VoxelShape voxelShape : shapes) {
				shape = Shapes.or(shape, voxelShape);
			}

			BlockPos targetPos = context.pos();
			Vec3 camera = worldRenderContext.levelState().cameraRenderState.pos;
			// TODO: Block outline renderer
			// DebugRenderer.renderVoxelShape(worldRenderContext.matrices(), worldRenderContext.consumers().getBuffer(RenderType.lines()), shape, (double)targetPos.getX() - camera.x, (double)targetPos.getY() - camera.y, (double)targetPos.getZ() - camera.z, 0.0F, 0.0F, 0.0F, 0.4F, true);
		}

		return true;
	}
}
