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

import net.fabricmc.fabric.api.client.rendereregistry.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import reborncore.common.RebornCoreConfig;
import reborncore.common.util.CalenderUtils;

/**
 * Created by Mark on 27/11/2016.
 */
public class HolidayRenderManager {

	public static void setupClient() {
		if (CalenderUtils.christmas && RebornCoreConfig.easterEggs) {
			LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper) -> {
				if (entityRenderer.getModel() instanceof PlayerEntityModel) {
					registrationHelper.register(new LayerRender(entityRenderer));
				}
			});
		}
	}

	private static final ModelSantaHat santaHat = new ModelSantaHat();
	private static final Identifier TEXTURE = new Identifier("reborncore", "textures/models/santa_hat.png");

	public static class LayerRender <T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {

		public LayerRender(FeatureRendererContext<T, M> context) {
			super(context);
		}

		@Override
		public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
			MinecraftClient.getInstance().getTextureManager().bindTexture(TEXTURE);
			VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(TEXTURE));
			matrixStack.push();

			float yaw = player.prevYaw + (player.yaw - player.prevYaw) * tickDelta - (player.prevBodyYaw + (player.bodyYaw - player.prevBodyYaw) * tickDelta);
			float pitch = player.prevPitch + (player.pitch - player.prevPitch) * tickDelta;

			matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(yaw));
			matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(pitch));
			santaHat.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(player, 0.0F), 1F, 1F, 1F, 1F);
			matrixStack.pop();
		}
	}

}
