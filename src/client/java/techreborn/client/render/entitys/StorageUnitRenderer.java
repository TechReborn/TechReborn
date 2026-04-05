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
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;

/**
 * Created by drcrazy on 07-Jan-20 for TechReborn-1.15.
 */
public class StorageUnitRenderer implements BlockEntityRenderer<StorageUnitBaseBlockEntity, StorageUnitRenderer.StorageUnitRenderState> {
	private final ItemModelResolver itemModelResolver;
	private final Font font;

	public StorageUnitRenderer(BlockEntityRendererProvider.Context ctx) {
		this.itemModelResolver = ctx.itemModelResolver();
		this.font = ctx.font();
	}

	@Override
	public StorageUnitRenderState createRenderState() {
		return new StorageUnitRenderState();
	}

	@Override
	public void extractRenderState(
		StorageUnitBaseBlockEntity storage,
		StorageUnitRenderState state,
		float f,
		Vec3 vec3,
		ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay
	) {
		BlockEntityRenderState.extractBase(storage, state, crumblingOverlay);
		if (storage.getLevel() == null) {
			return;
		}
		ItemStack stack = storage.getDisplayedStack();
		if (stack.isEmpty()) {
			return;
		}
		// Item rendering
		state.direction = storage.getFacing();
		state.rotate = (state.direction.get2DDataValue() - 2) * 90F;
		state.lightAbove = LevelRenderer.getLightCoords(storage.getLevel(), storage.getBlockPos().relative(storage.getFacing()));
		state.item = new ItemStackRenderState();
		itemModelResolver.updateForTopItem(state.item, stack, ItemDisplayContext.FIXED, storage.getLevel(), null, 0);
		// Text rendering
		state.count = Component.literal(String.valueOf(storage.storedAmount)).getVisualOrderText();
		state.countX = (float) (-font.width(state.count) / 2);
		state.countY = -4f + 40;
		state.name = Component.literal(stack.getHoverName().getString(18)).getVisualOrderText();
		state.nameX = (float) (-font.width(state.name) / 2);
		state.nameY = -4f - 40;
	}

	@Override
	public void submit(
		StorageUnitRenderState state,
		PoseStack matrices,
		SubmitNodeCollector submitNodeCollector,
		CameraRenderState cameraRenderState
	) {
		if (state.direction == null) {
			return;
		}
		// Item rendering
		matrices.pushPose();
		matrices.mulPose(Axis.YP.rotationDegrees(state.rotate));
		matrices.scale(0.5F, 0.5F, 0.5F);
		switch (state.direction) {
			case NORTH, WEST -> matrices.translate(1, 1, 0);
			case SOUTH -> matrices.translate(-1, 1, -2);
			case EAST -> matrices.translate(-1, 1, 2);
		}
		state.item.submit(matrices, submitNodeCollector, state.lightAbove, OverlayTexture.NO_OVERLAY, 0);
		matrices.popPose();
		// Text rendering
		matrices.pushPose();
		// Render item only on horizontal facing #2183
		if (Direction.Plane.HORIZONTAL.test(state.direction)) {
			matrices.translate(0.5, 0.5, 0.5); // Translate center
			matrices.mulPose(Axis.YP.rotationDegrees(-state.direction.getCounterClockWise().toYRot() + 90)); // Rotate depending on face
			matrices.translate(0, 0, -0.505); // Translate forward
		}
		matrices.scale(-0.01f, -0.01F, -0.01f);
		// Render item count
		submitNodeCollector.submitText(matrices, state.countX, state.countY, state.count, false, Font.DisplayMode.NORMAL, state.lightCoords, 0xFF000000, 0, 0);
		// Render name
		submitNodeCollector.submitText(matrices, state.nameX, state.nameY, state.name, false, Font.DisplayMode.NORMAL, state.lightCoords, 0xFF000000, 0, 0);
		matrices.popPose();
	}

	public static class StorageUnitRenderState extends BlockEntityRenderState {
		public Direction direction;
		public float rotate;
		public int lightAbove;
		public ItemStackRenderState item;
		public FormattedCharSequence count;
		public float countX;
		public float countY;
		public FormattedCharSequence name;
		public float nameX;
		public float nameY;
	}
}
