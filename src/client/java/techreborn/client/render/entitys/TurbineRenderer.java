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
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import techreborn.blockentity.generator.basic.WindMillBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;

public class TurbineRenderer implements BlockEntityRenderer<WindMillBlockEntity, TurbineRenderer.TurbineRenderState> {
	private static final Set<Direction> ALL_DIRECTIONS = EnumSet.allOf(Direction.class);
	private static final TurbineModel MODEL = TurbineModel.create();
	public static final Identifier TEXTURE = Identifier.parse("techreborn:textures/block/machines/generators/wind_mill_turbine.png");

	public TurbineRenderer(BlockEntityRendererProvider.Context ctx) {
	}

	@Override
	public @NotNull TurbineRenderState createRenderState() {
		return new TurbineRenderState();
	}

	@Override
	public void extractRenderState(
		WindMillBlockEntity blockEntity,
		TurbineRenderState state,
		float tickDelta,
		Vec3 vec3,
		@Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay
	) {
		BlockEntityRenderState.extractBase(blockEntity, state, crumblingOverlay);
		Direction facing = blockEntity.getFacing();
		state.layer = RenderType.entitySolid(TEXTURE);
		state.rotate = -facing.getCounterClockWise().toYRot() + 90;
		state.spin = blockEntity.bladeAngle + tickDelta * blockEntity.spinSpeed;
		state.light = LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos().relative(facing));
	}

	@Override
	public void submit(
		TurbineRenderState state,
		PoseStack matrixStack,
		SubmitNodeCollector submitNodeCollector,
		CameraRenderState cameraRenderState
	) {
		matrixStack.pushPose();
		matrixStack.translate(0.5, 0, 0.5);
		matrixStack.mulPose(Axis.YP.rotationDegrees(state.rotate));
		matrixStack.translate(0, -1, -0.56);
		submitNodeCollector.submitModel(MODEL, state.spin, matrixStack, state.layer, state.light, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);
		matrixStack.popPose();
	}

	private static class TurbineModel extends Model<Float> {
		private static TurbineModel create() {
			ModelPart.Cube[] baseCuboids = {
					new ModelPart.Cube(0, 0, -2.0F, -2.0F, -1.0F, 4F, 4F, 2F, 0F, 0F, 0F, false, 64F, 64F, ALL_DIRECTIONS),
					new ModelPart.Cube(0, 6, -1.0F, -1.0F, -2.0F, 2F, 2F, 1F, 0F, 0F, 0F, false, 64F, 64F, ALL_DIRECTIONS)
			};

			ModelPart base = new ModelPart(Arrays.asList(baseCuboids), new HashMap<>() {
				{
					ModelPart.Cube[] blade1Cuboids = {
							new ModelPart.Cube(0, 9, -24.0F, -1.0F, -0.5F, 24F, 2F, 1F, 0F, 0F, 0F, false, 64F, 64F, ALL_DIRECTIONS)
					};
					ModelPart blade1 = new ModelPart(Arrays.asList(blade1Cuboids), Collections.emptyMap());
					blade1.setPos(0.0F, 0.0F, 0.0F);
					setRotation(blade1, -0.5236F, 0.0F, 0.0F);
					put("blade1", blade1);

					ModelPart.Cube[] blade2Cuboids = {
							new ModelPart.Cube(0, 9, -24.0F, -1.0F, -0.5F, 24F, 2F, 1F, 0F, 0F, 0F, false, 64F, 64F, ALL_DIRECTIONS)
					};
					ModelPart blade2 = new ModelPart(Arrays.asList(blade2Cuboids), Collections.emptyMap());
					blade2.setPos(0.0F, 0.0F, 0.0F);
					setRotation(blade2, -0.5236F, 0.0F, 2.0944F);
					put("blade2", blade2);

					ModelPart.Cube[] blade3Cuboids = {
							new ModelPart.Cube(0, 9, -24.0F, -2.0F, -1.075F, 24F, 2F, 1F, 0F, 0F, 0F, false, 64F, 64F, ALL_DIRECTIONS)
					};
					ModelPart blade3 = new ModelPart(Arrays.asList(blade3Cuboids), Collections.emptyMap());
					blade3.setPos(0.0F, 0.0F, 0.0F);
					setRotation(blade3, -0.5236F, 0.0F, -2.0944F);
					put("blade3", blade3);
				}
			});
			base.setPos(0.0F, 24.0F, 0.0F);

			return new TurbineModel(base, RenderType::entityCutoutNoCull);
		}

		private static void setRotation(ModelPart model, float x, float y, float z) {
			model.xRot = x;
			model.yRot = y;
			model.zRot = z;
		}

		public TurbineModel(ModelPart root, Function<Identifier, RenderType> layerFactory) {
			super(root, layerFactory);
		}

		@Override
		public void setupAnim(Float spin) {
			root.zRot = spin;
		}

		@Override
		public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
			root.render(matrices, vertices, light, overlay);
		}
	}

	public static class TurbineRenderState extends BlockEntityRenderState {
		public RenderType layer;
		public float rotate;
		public float spin;
		public int light;
	}
}
