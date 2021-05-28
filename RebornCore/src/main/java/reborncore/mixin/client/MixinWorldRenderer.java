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

package reborncore.mixin.client;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import reborncore.common.misc.MultiBlockBreakingTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

	@Shadow
	@Final
	private MinecraftClient client;
	@Shadow
	private ClientWorld world;

	@Shadow
	private static void drawShapeOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double d, double e, double f, float g, float h, float i, float j) {
		throw new AssertionError();
	}

	@Inject(method = "drawBlockOutline", at = @At("HEAD"), cancellable = true)
	private void drawBlockOutline(MatrixStack matrixStack, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos targetPos, BlockState targetBlockState, CallbackInfo info) {
		List<VoxelShape> shapes = new ArrayList<>();

		if (entity == client.player) {
			ClientPlayerEntity clientPlayerEntity = client.player;
			ItemStack stack = clientPlayerEntity.getMainHandStack();
			if (stack.isEmpty()) {
				return;
			}
			if (stack.getItem() instanceof MultiBlockBreakingTool) {
				Set<BlockPos> blockPosList = ((MultiBlockBreakingTool) stack.getItem()).getBlocksToBreak(stack, clientPlayerEntity.world, targetPos, clientPlayerEntity);

				for (BlockPos pos : blockPosList) {
					if (pos.equals(targetPos)) {
						continue;
					}

					BlockState blockState = world.getBlockState(pos);
					shapes.add(blockState.getOutlineShape(world, pos, ShapeContext.of(entity)).offset(pos.getX() - targetPos.getX(), pos.getY() - targetPos.getY(), pos.getZ() - targetPos.getZ()));

				}
			}
		}

		if (!shapes.isEmpty()) {
			VoxelShape shape = targetBlockState.getOutlineShape(world, targetPos, ShapeContext.of(entity));

			for (VoxelShape voxelShape : shapes) {
				shape = VoxelShapes.union(shape, voxelShape);
			}

			drawShapeOutline(matrixStack, vertexConsumer, shape, (double)targetPos.getX() - d, (double)targetPos.getY() - e, (double)targetPos.getZ() - f, 0.0F, 0.0F, 0.0F, 0.4F);
			//info.cancel(); // Enable to render a single bounding box around the whole thing
		}
	}

}
