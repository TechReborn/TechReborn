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
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import reborncore.api.systems.conduit.functionalfaces.ConduitFunction;
import reborncore.api.systems.conduit.functionalfaces.ConduitFunctionalFace;
import reborncore.api.systems.functionalface.FunctionalFaceStorage;
import reborncore.api.systems.conduit.IConduitTransfer;
import techreborn.blockentity.conduit.item.ItemConduitBlockEntity;

import java.util.Map;

public class ItemConduitRenderer extends BlockEntityRenderer<ItemConduitBlockEntity> {
	private static final ExportModel EXPORT_MODEL = new ExportModel();
	public static final Identifier EXPORT_TEXTURE = new Identifier("techreborn:textures/block/conduit/exporter.png");

	private static final ImportModel IMPORT_MODEL = new ImportModel();
	public static final Identifier IMPORT_TEXTURE = new Identifier("techreborn:textures/block/conduit/importer.png");

	private static final OneWayModel ONE_WAY_MODEL = new OneWayModel();
	public static final Identifier ONE_WAY_TEXTURE = new Identifier("techreborn:textures/block/conduit/one-way.png");

	private static final BlockingModel BLOCKING_MODEL = new BlockingModel();
	public static final Identifier BLOCKING_TEXTURE = new Identifier("techreborn:textures/block/conduit/blocking.png");

	private static final RestrictiveModel RESTRICTIVE_MODEL = new RestrictiveModel();
	public static final Identifier RESTRICTIVE_TEXTURE_ALLOWING = new Identifier("techreborn:textures/block/conduit/restrictive_allowing.png");
	public static final Identifier RESTRICTIVE_TEXTURE_BLOCKING = new Identifier("techreborn:textures/block/conduit/restrictive_blocking.png");


	public ItemConduitRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(ItemConduitBlockEntity conduit, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {


//		int light = WorldRenderer.getLightmapCoordinates(conduit.getWorld(), conduit.getPos());

		renderIOFaces(conduit.getFunctionalFaces(), matrices, vertexConsumers, light, overlay);
		renderItemMoving(conduit.getStored(), matrices, vertexConsumers, light);

	}

	private void renderIOFaces(FunctionalFaceStorage<ConduitFunctionalFace> functionalFaceStorage, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay){
		if (functionalFaceStorage.isEmpty()) {
			return;
		}

		for (Map.Entry<Direction, ConduitFunctionalFace> entry : functionalFaceStorage.getEntrySet()) {
			Direction facing = entry.getKey();
			ConduitFunction mode = entry.getValue().conduitFunction;

			matrices.push();

			matrices.translate(0.5f, 0.5f, 0.5f); // Translate center

//			// Rotating
			if(facing.getHorizontal() == -1){
				// UP/DOWN

				int offSet = facing.getOffsetY();
				matrices.translate(0, offSet * 0.5,0);
				matrices.multiply(facing.getRotationQuaternion());
			}else{
				// NORTH, SOUTH, WEST, EAST
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-facing.rotateYCounterclockwise().asRotation() + 90)); // Rotate depending on face

				matrices.translate(0, 0, -0.5);
				Quaternion faceQuaternion = facing.getRotationQuaternion();
				matrices.multiply(new Quaternion(-faceQuaternion.getX(), faceQuaternion.getY(), faceQuaternion.getZ(), faceQuaternion.getW()));
			}

			// Janky translating due to unfound issues
			matrices.translate(0,0.031f, 0);

			 // Model rendering
			switch (mode){
				case EXPORT:
					EXPORT_MODEL.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(EXPORT_TEXTURE)), light, overlay, 1F, 1F, 1F, 1F);
					break;
				case IMPORT:
					IMPORT_MODEL.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(IMPORT_TEXTURE)), light, overlay, 1F, 1F, 1F, 1F);
					break;
				case ONE_WAY:
					matrices.scale(1.15f, 1.15f,  1.15f);
					ONE_WAY_MODEL.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(ONE_WAY_TEXTURE)), light, overlay, 1F, 1F, 1F, 1F);
					break;
				case BLOCK:
					matrices.translate(0, -0.0622, 0); // Put it 1 block inside the block
					new BlockingModel().render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(BLOCKING_TEXTURE)), light, overlay, 1F, 1F, 1F, 1F);
					break;
			}

			matrices.pop();
		}
	}

	private void renderItemMoving(IConduitTransfer<ItemStack> transferItem, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light){
		if (transferItem == null) {
			return;
		}

		float percent = transferItem.getProgressPercent() * 1.2f;

		// Item rendering
		matrices.push();
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion((Direction.WEST.getHorizontal() - 2) * 90F));
		matrices.translate(0.5f, 0.5f, -0.5f);

		ItemStack itemStack = transferItem.getStored();

		float multiplier = 0.3f;

		if(itemStack.getItem() instanceof BlockItem){
			multiplier = 0.5f;
		}

		matrices.scale(multiplier * percent, multiplier * percent, multiplier * percent);

		MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
		matrices.pop();

	}


	// Sided function models

	private static class ExportModel extends Model {

		private final ModelPart base;

		public ExportModel() {
			super(RenderLayer::getEntityCutoutNoCull);
			textureWidth = 48;
			textureHeight = 32;

			base = new ModelPart(this);

			base.setTextureOffset(0, 0).addCuboid(-4.5F, -4.0F, -4.5F, 9.0F, 2.0F, 9.0F, 0.0F, false);
			base.setTextureOffset(0, 11).addCuboid(-3.5F, -2.0F, -3.5F, 7.0F, 2.0F, 7.0F, 0.0F, false);
		}

		@Override
		public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
			base.render(matrixStack, vertexConsumer, light, overlay);
		}
	}

	private static class ImportModel extends Model {

		private final ModelPart base;

		public ImportModel() {
			super(RenderLayer::getEntityCutoutNoCull);
			textureWidth = 48;
			textureHeight = 32;

			base = new ModelPart(this);

			base.setTextureOffset(0, 9).addCuboid(-4.5F, -2.0F, -4.5F, 9.0F, 2.0F, 9.0F, 0.0F, false);
			base.setTextureOffset(0, 0).addCuboid(-3.5F, -4.0F, -3.5F, 7.0F, 2.0F, 7.0F, 0.0F, false);
		}

		@Override
		public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
			base.render(matrixStack, vertexConsumer, light, overlay);
		}
	}

	private static class OneWayModel extends Model {

		private final ModelPart base;

		public OneWayModel() {
			super(RenderLayer::getEntityCutoutNoCull);
			textureWidth = 32;
			textureHeight = 32;

			base = new ModelPart(this);

			base.setTextureOffset(0, 0).addCuboid(-3.5f, -4F, -3.5f, 7.0F, 4.0F, 7.0F, 0.0F, false);
		}

		@Override
		public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
			base.render(matrixStack, vertexConsumer, light, overlay);
		}
	}

	private static class BlockingModel extends Model {

		private final ModelPart base;

		public BlockingModel() {
			super(RenderLayer::getEntityCutoutNoCull);
			textureWidth = 48;
			textureHeight = 32;

			base = new ModelPart(this);

			base.setTextureOffset(0, 4).addCuboid(-4.5F, -2.0F, -4.5F, 9.0F, 1.0F, 9.0F, 0.0F, false);
			base.setTextureOffset(0, 0).addCuboid(-3.5F, -4.0F, 1.5F, 7.0F, 2.0F, 2.0F, 0.0F, false);
			base.setTextureOffset(0, 0).addCuboid(-3.5F, -4.0F, -3.5F, 7.0F, 2.0F, 2.0F, 0.0F, false);
		}

		@Override
		public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
			base.render(matrixStack, vertexConsumer, light, overlay);
		}
	}

	private static class RestrictiveModel extends Model {

		private final ModelPart base;

		public RestrictiveModel() {
			super(RenderLayer::getEntityCutoutNoCull);
			textureWidth = 48;
			textureHeight = 32;

			base = new ModelPart(this);

			base.setPivot(0, 0, 0);
			base.setTextureOffset(0, 0).addCuboid(-7.5F, 0.0F, 4.4F, 7.0F, 4.0F, 7.0F, 0.0F, false);
		}

		@Override
		public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
			base.render(matrixStack, vertexConsumer, light, overlay);
		}
	}
}
