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

package reborncore.common.blocks;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import reborncore.api.ToolManager;

import java.util.ArrayList;
import java.util.List;

public class BlockWrenchEventHandler {

	public static List<Block> wrenableBlocks = new ArrayList<>();


	public static void setup() {
		UseBlockCallback.EVENT.register((playerEntity, world, hand, blockHitResult) -> {
			if (hand == Hand.OFF_HAND) {
				// Wrench should be in main hand
				return ActionResult.PASS;
			}
			if (ToolManager.INSTANCE.canHandleTool(playerEntity.getStackInHand(Hand.MAIN_HAND))) {
				BlockState state = world.getBlockState(blockHitResult.getBlockPos());
				if (wrenableBlocks.contains(state.getBlock())) {
					Block block = state.getBlock();
					block.onUse(state, world, blockHitResult.getBlockPos(), playerEntity, hand, blockHitResult);
					return ActionResult.SUCCESS;
				}
			}
			return ActionResult.PASS;
		});
	}


}
