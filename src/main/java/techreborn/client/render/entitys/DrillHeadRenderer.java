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

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import techreborn.blockentity.machine.multiblock.structure.DrillHeadBlockEntity;

public class DrillHeadRenderer extends BlockEntityRenderer<DrillHeadBlockEntity> {
	public static final DrillModel MODEL = new DrillModel();
	public static final Identifier TEXTURE = new Identifier("techreborn:textures/block/machines/structure/drill_heads/iron.png");

	public DrillHeadRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(DrillHeadBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		Direction facing = Direction.NORTH;
		int renderLight = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().offset(facing));

		final VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(TEXTURE));

		matrixStack.push();
		matrixStack.translate(0.5, 0.37, 0.5);
		matrixStack.multiply(new Quaternion(0,0,1f,0));

		MODEL.setSpin(blockEntity.drillAngle + tickDelta * blockEntity.spinSpeed);

		MODEL.render(matrixStack, vertexConsumer, renderLight, overlay, 1F, 1F, 1F, 1F);

		matrixStack.pop();
	}

	private static class DrillModel extends Model {

		private final ModelPart base;

		public DrillModel() {
			super(RenderLayer::getEntityCutoutNoCull);
			textureWidth = 64;
			textureHeight = 64;

			base = new ModelPart(this);

			base.setPivot(0.0F, 0.0f, 0.0F);
			base.addCuboid(null, -5.5F, -6.0F, -5.5F, 11, 2, 11, 0.0F, 0, 16);
			base.addCuboid(null, -4.5F, -4.0F, -4.5F, 9, 2, 9, 0.0F, 0, 29);
			base.addCuboid(null, -3.5F, -2.0F, -3.5F, 7, 2, 7, 0.0F, 0, 40);
			base.addCuboid(null, -2.5F, 0.0F, -2.5F, 5, 2, 5, 0.0F, 0, 49);
			base.addCuboid(null, -0.5F, 4.0F, -0.5F, 1, 2, 1, 0.0F, 0, 61);
			base.addCuboid(null, -1.5F, 2.0F, -1.5F, 3, 2, 3, 0.0F, 0, 56);
		}

		private void setSpin(float z) {
			base.yaw = z;
		}

		@Override
		public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
			base.render(matrixStack, vertexConsumer, light, overlay);
		}
	}
}
