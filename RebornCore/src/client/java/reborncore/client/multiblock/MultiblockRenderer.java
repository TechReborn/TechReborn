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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import reborncore.common.blockentity.MachineBaseBlockEntity;

public class MultiblockRenderer<T extends MachineBaseBlockEntity> implements BlockEntityRenderer<T> {

	public MultiblockRenderer(BlockEntityRendererProvider.Context ctx) {
	}

	@Override
	public void render(T blockEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light, int overlay, Vec3 cameraPos) {
		if (blockEntity.renderMultiblock && !blockEntity.isShapeValid()) {
			blockEntity.writeMultiblock(new HologramRenderer(blockEntity.getLevel(), matrixStack, vertexConsumerProvider, 0.4F).rotate(blockEntity.getFacing().getOpposite()));
		}
	}

	@Override
	public boolean shouldRenderOffScreen() {
		return true;
	}
}
