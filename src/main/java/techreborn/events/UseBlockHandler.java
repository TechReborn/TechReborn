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

package techreborn.events;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import techreborn.init.TRContent;

public class UseBlockHandler implements UseBlockCallback{

	public static void init() {
		UseBlockCallback.EVENT.register(new UseBlockHandler());
	}

	@Override
	public InteractionResult interact(Player playerEntity, Level world, InteractionHand hand, BlockHitResult blockHitResult) {
		ItemStack stack = playerEntity.getItemInHand(hand);

		if (stack.getItem() instanceof AxeItem) {
			BlockPos pos = blockHitResult.getBlockPos();
			BlockState hitState = world.getBlockState(pos);
			Block hitBlock = hitState.getBlock();

			Block strippedBlock = null;
			if (hitBlock == TRContent.RUBBER_LOG) {
				strippedBlock = TRContent.RUBBER_LOG_STRIPPED;
			} else if (hitBlock == TRContent.RUBBER_WOOD) {
				strippedBlock = TRContent.STRIPPED_RUBBER_WOOD;
			}

			if (strippedBlock != null) {
				// Play stripping sound
				world.playSound(playerEntity, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
				if (world.isClientSide) {
					return InteractionResult.SUCCESS;
				}

				world.setBlock(pos, strippedBlock.defaultBlockState().setValue(RotatedPillarBlock.AXIS, hitState.getValue(RotatedPillarBlock.AXIS)), 11);

				// Damage axe
				stack.hurtAndBreak(1, playerEntity, EquipmentSlot.MAINHAND);
				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.PASS;
	}
}
