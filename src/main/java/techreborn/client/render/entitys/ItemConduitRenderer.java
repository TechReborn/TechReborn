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
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import techreborn.blockentity.conduit.ConduitMode;
import techreborn.blockentity.conduit.ItemConduitBlockEntity;
import techreborn.blockentity.conduit.ItemTransfer;

import java.util.List;
import java.util.Map;

public class ItemConduitRenderer extends BlockEntityRenderer<ItemConduitBlockEntity> {
	public ItemConduitRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(ItemConduitBlockEntity conduit, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {


//		int light = WorldRenderer.getLightmapCoordinates(conduit.getWorld(), conduit.getPos());

		renderIOFaces(conduit.getFunctionalFaces(), matrices, vertexConsumers, light);
		renderItemMoving(conduit.getStored(), matrices, vertexConsumers, light);

	}

	private void renderIOFaces(Map<Direction, ConduitMode> IOFaces, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light){
		if (IOFaces.isEmpty()) {
			return;
		}

		for (Map.Entry<Direction, ConduitMode> entry : IOFaces.entrySet()) {
			Direction facing = entry.getKey();
			ConduitMode mode = entry.getValue();

			matrices.push();

			matrices.translate(0.5, 0.5, 0.5); // Translate center

			matrices.scale(0.45f, 0.45f, 0.45f); // TEMPscale

			// Rotating
			if(facing.getHorizontal() == -1){
				int offSet = facing.getOffsetY();
				matrices.translate(0, offSet * 0.5,0);
				matrices.multiply(facing.getRotationQuaternion());
			}else{
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-facing.rotateYCounterclockwise().asRotation() + 90)); // Rotate depending on face
				matrices.translate(0, 0, -0.5);

				Quaternion faceQuaternion = facing.getRotationQuaternion();
				matrices.multiply(new Quaternion(-faceQuaternion.getX(), faceQuaternion.getY(), faceQuaternion.getZ(), faceQuaternion.getW()));
			}

			 // Model rendering
			matrices.translate(0,0.25f,0f);
			switch (mode){
				case OUTPUT:
					MinecraftClient.getInstance().getItemRenderer().renderItem(Items.BEACON.getStackForRender(), ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
					break;
				case INPUT:
					matrices.scale(1f,0.7f,4f);
					MinecraftClient.getInstance().getItemRenderer().renderItem(Items.HOPPER.getStackForRender(), ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
					break;
				case BLOCK:
					matrices.scale(1,0.2f,1f);
					matrices.translate(0,-1,0);
					MinecraftClient.getInstance().getItemRenderer().renderItem(Items.BEDROCK.getStackForRender(), ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
					break;
			}

			matrices.pop();
		}
	}

	private void renderItemMoving(ItemTransfer transferItem, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light){
		if (transferItem == null) {
			return;
		}

		float percent = transferItem.getProgressPercent() * 1.2f;

		// Item rendering
		matrices.push();
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((Direction.WEST.getHorizontal() - 2) * 90F));
		matrices.translate(0.5f, 0.5f, -0.5f);
		matrices.scale(0.8F * percent, 0.8F * percent, 0.8F * percent);




		MinecraftClient.getInstance().getItemRenderer().renderItem(transferItem.getItemStack(), ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
		matrices.pop();

	}
}
