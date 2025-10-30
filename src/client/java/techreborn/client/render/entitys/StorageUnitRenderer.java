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

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;

/**
 * Created by drcrazy on 07-Jan-20 for TechReborn-1.15.
 */
public class StorageUnitRenderer implements BlockEntityRenderer<StorageUnitBaseBlockEntity> {

	public StorageUnitRenderer(BlockEntityRendererProvider.Context ctx) {
	}

	@Override
	public void render(StorageUnitBaseBlockEntity storage, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, Vec3 cameraPos) {
		if (storage.getLevel() == null) {
			return;
		}
		ItemStack stack = storage.getDisplayedStack();
		if (stack.isEmpty()) {
			return;
		}

		// Item rendering
		matrices.pushPose();
		Direction direction = storage.getFacing();
		matrices.mulPose(Axis.YP.rotationDegrees((direction.get2DDataValue() - 2) * 90F));
		matrices.scale(0.5F, 0.5F, 0.5F);
		switch (direction) {
			case NORTH, WEST -> matrices.translate(1, 1, 0);
			case SOUTH -> matrices.translate(-1, 1, -2);
			case EAST -> matrices.translate(-1, 1, 2);
		}
		int lightAbove = LevelRenderer.getLightColor(storage.getLevel(), storage.getBlockPos().relative(storage.getFacing()));
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, lightAbove, OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, storage.getLevel(), 0);
		matrices.popPose();

		// Text rendering
		matrices.pushPose();
		Font textRenderer = Minecraft.getInstance().font;
		Direction facing = storage.getFacing();

		// Render item only on horizontal facing #2183
		if (Direction.Plane.HORIZONTAL.test(facing) ){
			matrices.translate(0.5, 0.5, 0.5); // Translate center
			matrices.mulPose(Axis.YP.rotationDegrees(-facing.getCounterClockWise().toYRot() + 90)); // Rotate depending on face
			matrices.translate(0, 0, -0.505); // Translate forward
		}

		matrices.scale(-0.01f, -0.01F, -0.01f);

		float xPosition;

		// Render item count
		String count = String.valueOf(storage.storedAmount);
		xPosition = (float) (-textRenderer.width(count) / 2);
		textRenderer.drawInBatch(count, xPosition, -4f + 40, 0, false, matrices.last().pose(), vertexConsumers, Font.DisplayMode.NORMAL, 0, light);

		// Render name
		String item = stack.getHoverName().getString(18);
		xPosition = (float) (-textRenderer.width(item) / 2);
		textRenderer.drawInBatch(item, xPosition, -4f - 40, 0, false, matrices.last().pose(), vertexConsumers, Font.DisplayMode.NORMAL, 0, light);

		matrices.popPose();
	}
}
