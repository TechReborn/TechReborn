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

package techreborn.init;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import reborncore.common.fluid.FluidUtils;
import techreborn.items.DynamicCellItem;

public class TRCauldronBehavior {
	public static void init() {
		CauldronInteraction FILL_CELL_WITH_LAVA = (state, world, pos, player, hand, stack) -> {
			if (!FluidUtils.isContainerEmpty(stack)) {
				return InteractionResult.TRY_WITH_EMPTY_HAND;
			}

			return CauldronInteraction.fillBucket(state, world, pos, player, hand, stack,
					DynamicCellItem.getCellWithFluid(Fluids.LAVA), (stateX) -> true, SoundEvents.BUCKET_FILL_LAVA);
		};

		CauldronInteraction FILL_CELL_WITH_WATER = (state, world, pos, player, hand, stack) -> {
			if (!FluidUtils.isContainerEmpty(stack)) {
				return InteractionResult.TRY_WITH_EMPTY_HAND;
			}

			return CauldronInteraction.fillBucket(state, world, pos, player, hand, stack,
					DynamicCellItem.getCellWithFluid(Fluids.WATER), (stateX) -> true, SoundEvents.BUCKET_FILL);
		};

		CauldronInteraction FILL_FROM_CELL = (state, world, pos, player, hand, stack) -> {
			Fluid cellFluid = ((DynamicCellItem) stack.getItem()).getFluid(stack);
			if (cellFluid == Fluids.WATER) {
				return fillCauldronFromCell(world, pos, player, hand, stack,
						Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3),
						SoundEvents.BUCKET_EMPTY);
			} else if (cellFluid == Fluids.LAVA) {
				return fillCauldronFromCell(world, pos, player, hand, stack,
						Blocks.LAVA_CAULDRON.defaultBlockState(),
						SoundEvents.BUCKET_EMPTY_LAVA);
			}

			return InteractionResult.TRY_WITH_EMPTY_HAND;
		};

		CauldronInteraction.LAVA.map().put(TRContent.CELL, FILL_CELL_WITH_LAVA);
		CauldronInteraction.WATER.map().put(TRContent.CELL, FILL_CELL_WITH_WATER);
		CauldronInteraction.EMPTY.map().put(TRContent.CELL, FILL_FROM_CELL);
	}

	static InteractionResult fillCauldronFromCell(Level world, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, BlockState state, SoundEvent soundEvent) {
		if (!world.isClientSide) {
			player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(TRContent.CELL)));
			player.awardStat(Stats.FILL_CAULDRON);
			world.setBlockAndUpdate(pos, state);
			world.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
			world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
		}

		return InteractionResult.SUCCESS;
	}
}
