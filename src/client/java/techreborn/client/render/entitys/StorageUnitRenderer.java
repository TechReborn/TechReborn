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

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;

/**
 * Created by drcrazy on 07-Jan-20 for TechReborn-1.15.
 */
public class StorageUnitRenderer implements BlockEntityRenderer<StorageUnitBaseBlockEntity> {

	public StorageUnitRenderer(BlockEntityRendererFactory.Context ctx) {
	}

	@Override
	public void render(StorageUnitBaseBlockEntity storage, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (storage.getWorld() == null) {
			return;
		}
		ItemStack stack = storage.getDisplayedStack();
		if (stack.isEmpty()) {
			return;
		}

		// Item rendering
		matrices.push();
		Direction direction = storage.getFacing();
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((direction.getHorizontal() - 2) * 90F));
		matrices.scale(0.5F, 0.5F, 0.5F);
		switch (direction) {
			case NORTH, WEST -> matrices.translate(1, 1, 0);
			case SOUTH -> matrices.translate(-1, 1, -2);
			case EAST -> matrices.translate(-1, 1, 2);
		}
		int lightAbove = WorldRenderer.getLightmapCoordinates(storage.getWorld(), storage.getPos().offset(storage.getFacing()));
		MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);
		matrices.pop();

		// Text rendering
		matrices.push();
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		Direction facing = storage.getFacing();

		// Render item only on horizontal facing #2183
		if (Direction.Type.HORIZONTAL.test(facing) ){
			matrices.translate(0.5, 0.5, 0.5); // Translate center
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-facing.rotateYCounterclockwise().asRotation() + 90)); // Rotate depending on face
			matrices.translate(0, 0, -0.505); // Translate forward
		}

		matrices.scale(-0.01f, -0.01F, -0.01f);

		float xPosition;

		// Render item count
		String count = String.valueOf(storage.storedAmount);
		xPosition = (float) (-textRenderer.getWidth(count) / 2);
		textRenderer.draw(count, xPosition, -4f + 40, 0, false, matrices.peek().getPositionMatrix(), vertexConsumers, false, 0, light);

		// Render name
		String item = stack.getName().asTruncatedString(18);
		xPosition = (float) (-textRenderer.getWidth(item) / 2);
		textRenderer.draw(item, xPosition, -4f - 40, 0, false, matrices.peek().getPositionMatrix(), vertexConsumers, false, 0, light);

		matrices.pop();
	}
}
