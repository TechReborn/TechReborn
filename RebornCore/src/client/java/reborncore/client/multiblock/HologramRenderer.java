/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TeamReborn
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

import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import reborncore.common.blockentity.MultiblockWriter;

import java.util.function.BiPredicate;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

/**
 * Renders a hologram of a multiblock structure.
 */
public record HologramRenderer(BlockModelResolver blockModelResolver, ItemModelResolver itemModelResolver, Level view, MultiblockRenderer.MultiblockRenderState renderState) implements MultiblockWriter {
	private static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();

	@Override
	public MultiblockWriter add(int x, int y, int z, BiPredicate<BlockGetter, BlockPos> predicate, BlockState state) {
		if (state.getBlock() instanceof LiquidBlock) {
			FluidState fluidState = state.getFluidState();
			ItemStackRenderState item = renderState.nextItemStackRenderState();
			itemModelResolver.updateForTopItem(item, new ItemStack(fluidState.getType().getBucket()), ItemDisplayContext.FIXED, view, null, 0);
			renderState.states.add(new HologramRenderState.FluidItem(x, y, z, item));
		} else {
			var blockModelRenderState = renderState.nextBlockModelRenderState();
			blockModelResolver.update(blockModelRenderState, state, BLOCK_DISPLAY_CONTEXT);
			renderState.states.add(new HologramRenderState.Block(x, y, z, blockModelRenderState));
		}
		return this;
	}
}
