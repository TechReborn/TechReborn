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

import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
public class BlockOutlineRenderer implements LevelRenderEvents.BeforeBlockOutline {
	@Override
	public boolean beforeBlockOutline(LevelRenderContext worldRenderContext, BlockOutlineRenderState context) {
		/*
		 * TODO 26.2-pre-1: Restore the multiblock outline renderer once the new block-outline
		 * extraction/render path is understood well enough to port the previous implementation.
		 *
		 * Previous implementation before the 26.2-pre-1 port:
		 *
		 * List<VoxelShape> shapes = context.getData(KEY);
		 * if (shapes != null) {
		 * 	VoxelShape shape = context.shape();
		 *
		 * 	for (VoxelShape voxelShape : shapes) {
		 * 		shape = Shapes.or(shape, voxelShape);
		 * 	}
		 *
		 * 	BlockPos targetPos = context.pos();
		 * 	Vec3 camera = worldRenderContext.levelState().cameraRenderState.pos;
		 * 	DebugRenderer.renderVoxelShape(
		 * 		worldRenderContext.matrices(),
		 * 		worldRenderContext.consumers().getBuffer(RenderType.lines()),
		 * 		shape,
		 * 		(double) targetPos.getX() - camera.x,
		 * 		(double) targetPos.getY() - camera.y,
		 * 		(double) targetPos.getZ() - camera.z,
		 * 		0.0F,
		 * 		0.0F,
		 * 		0.0F,
		 * 		0.4F,
		 * 		true
		 * 	);
		 * }
		 *
		 * This also previously implemented LevelRenderEvents.AfterBlockOutlineExtraction and used:
		 *
		 * public static RenderStateDataKey<List<VoxelShape>> KEY = RenderStateDataKey.create(() -> "MultiBlockBreakingTool");
		 *
		 * @Override
		 * public void afterBlockOutlineExtraction(LevelExtractionContext context, @Nullable HitResult result) {
		 * 	if (result instanceof BlockHitResult blockHitResult) {
		 * 		LocalPlayer player = Minecraft.getInstance().player;
		 * 		if (player == context.camera().entity()) {
		 * 			ItemStack stack = player.getMainHandItem();
		 * 			if (!stack.isEmpty() && stack.getItem() instanceof MultiBlockBreakingTool tool) {
		 * 				BlockOutlineRenderState state = context.levelState().blockOutlineRenderState;
		 * 				if (state == null) {
		 * 					return;
		 * 				}
		 * 				BlockPos targetPos = blockHitResult.getBlockPos();
		 * 				Level level = player.level();
		 * 				Set<BlockPos> blockPosList = tool.getBlocksToBreak(stack, level, targetPos, player);
		 * 				List<VoxelShape> shapes = new ArrayList<>();
		 * 				for (BlockPos pos : blockPosList) {
		 * 					if (pos.equals(targetPos)) {
		 * 						continue;
		 * 					}
		 * 					BlockState blockState = level.getBlockState(pos);
		 * 					shapes.add(blockState.getShape(level, pos, CollisionContext.of(player)).move(
		 * 						pos.getX() - targetPos.getX(),
		 * 						pos.getY() - targetPos.getY(),
		 * 						pos.getZ() - targetPos.getZ()
		 * 					));
		 * 				}
		 * 				if (shapes.isEmpty()) {
		 * 					return;
		 * 				}
		 * 				state.setData(KEY, shapes);
		 * 			}
		 * 		}
		 * 	}
		 * }
		 */
		return true;
	}
}
