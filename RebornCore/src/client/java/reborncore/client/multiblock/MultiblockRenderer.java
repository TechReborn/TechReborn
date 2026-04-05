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

package reborncore.client.multiblock;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import reborncore.common.blockentity.MachineBaseBlockEntity;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

import java.util.ArrayList;
import java.util.List;

public class MultiblockRenderer<T extends MachineBaseBlockEntity> implements BlockEntityRenderer<T, MultiblockRenderer.MultiblockRenderState> {
	private final BlockModelResolver blockModelResolver;
	private final ItemModelResolver itemModelResolver;

	public MultiblockRenderer(BlockEntityRendererProvider.Context ctx) {
		this.blockModelResolver = ctx.blockModelResolver();
		this.itemModelResolver = ctx.itemModelResolver();
	}

	@Override
	public MultiblockRenderState createRenderState() {
		return new MultiblockRenderState();
	}

	@Override
	public void extractRenderState(
		T blockEntity,
		MultiblockRenderState state,
		float f,
		Vec3 vec3,
		ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay
	) {
		BlockEntityRenderState.extractBase(blockEntity, state, crumblingOverlay);
		state.resetPools();
		if (blockEntity.renderMultiblock && !blockEntity.isShapeValid()) {
			Direction direction = blockEntity.getFacing().getOpposite();
			blockEntity.writeMultiblock(new HologramRenderer(blockModelResolver, itemModelResolver, blockEntity.getLevel(), state).rotate(direction));
		}
	}

	@Override
	public void submit(
		MultiblockRenderState state,
		PoseStack poseStack,
		SubmitNodeCollector submitNodeCollector,
		CameraRenderState cameraRenderState
	) {
		for (HologramRenderState hologram : state.states) {
			hologram.submit(poseStack, submitNodeCollector, 0.4F);
		}
	}

	@Override
	public boolean shouldRenderOffScreen() {
		return true;
	}

	public static class MultiblockRenderState extends BlockEntityRenderState {
		final List<HologramRenderState> states = new ArrayList<>();
		private final List<BlockModelRenderState> blockModelPool = new ArrayList<>();
		private final List<ItemStackRenderState> itemStatePool = new ArrayList<>();
		private int blockPoolIndex;
		private int itemPoolIndex;

		void resetPools() {
			states.clear();
			blockPoolIndex = 0;
			itemPoolIndex = 0;
		}

		BlockModelRenderState nextBlockModelRenderState() {
			if (blockPoolIndex >= blockModelPool.size()) {
				blockModelPool.add(new BlockModelRenderState());
			}
			return blockModelPool.get(blockPoolIndex++);
		}

		ItemStackRenderState nextItemStackRenderState() {
			if (itemPoolIndex >= itemStatePool.size()) {
				itemStatePool.add(new ItemStackRenderState());
			}
			return itemStatePool.get(itemPoolIndex++);
		}
	}
}
