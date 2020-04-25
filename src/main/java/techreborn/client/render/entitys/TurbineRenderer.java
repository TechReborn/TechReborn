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
import techreborn.blockentity.generator.basic.WindMillBlockEntity;

public class TurbineRenderer extends BlockEntityRenderer<WindMillBlockEntity> {
	private static final TurbineModel MODEL = new TurbineModel();
	public static final Identifier TEXTURE = new Identifier("techreborn:textures/block/machines/generators/wind_mill_turbine.png");

	public TurbineRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(WindMillBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		Direction facing = blockEntity.getFacing();
		int renderLight = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().offset(facing));

		final VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(TEXTURE));

		matrixStack.push();
		matrixStack.translate(0.5, 0, 0.5);
		matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-facing.rotateYCounterclockwise().asRotation() + 90));
		matrixStack.translate(0, -1, -0.56);

		float spin = blockEntity.bladeAngle + tickDelta * blockEntity.spinSpeed;
		MODEL.setSpin(spin);
		MODEL.render(matrixStack, vertexConsumer, renderLight, overlay, 1F, 1F, 1F, 1F);

		matrixStack.pop();
	}

	private static class TurbineModel extends Model {

		private final ModelPart base;

		public TurbineModel() {
			super(RenderLayer::getEntityCutoutNoCull);
			textureWidth = 64;
			textureHeight = 64;

			base = new ModelPart(this);

			base.setPivot(0.0F, 24.0F, 0.0F);
			base.addCuboid(null, -2.0F, -2.0F, -1.0F, 4, 4, 2, 0.0F, 0, 0);
			base.addCuboid(null, -1.0F, -1.0F, -2.0F, 2, 2, 1, 0.0F, 0, 6);

			ModelPart blade1 = new ModelPart(this);
			blade1.setPivot(0.0F, 0.0F, 0.0F);
			setRotation(blade1, -0.5236F, 0.0F, 0.0F);
			blade1.addCuboid(null, -24.0F, -1.0F, -0.5F, 24, 2, 1, 0.0F, 0, 9);

			ModelPart blade2 = new ModelPart(this);
			blade2.setPivot(0.0F, 0.0F, 0.0F);
			setRotation(blade2, -0.5236F, 0.0F, 2.0944F);
			blade2.addCuboid(null, -24.0F, -1.0F, -0.5F, 24, 2, 1, 0.0F, 0, 9);

			ModelPart blade3 = new ModelPart(this);
			blade3.setPivot(0.0F, 0.0F, 0.0F);
			setRotation(blade3, -0.5236F, 0.0F, -2.0944F);
			blade3.addCuboid(null, -24.0F, -2.0F, -1.075F, 24, 2, 1, 0.0F, 0, 9);

			base.addChild(blade1);
			base.addChild(blade2);
			base.addChild(blade3);
		}

		private void setRotation(ModelPart model, float x, float y, float z) {
			model.pitch = x;
			model.yaw = y;
			model.roll = z;
		}

		private void setSpin(float z) {
			base.roll = z;
		}

		@Override
		public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
			base.render(matrixStack, vertexConsumer, light, overlay);
		}
	}
}
