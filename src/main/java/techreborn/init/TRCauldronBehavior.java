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

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import reborncore.common.fluid.FluidUtils;
import techreborn.items.DynamicCellItem;

public class TRCauldronBehavior {
	public static void init() {
		CauldronBehavior FILL_CELL_WITH_LAVA = (state, world, pos, player, hand, stack) -> {
			if (!FluidUtils.isContainerEmpty(stack)) {
				return ActionResult.PASS;
			}

			return CauldronBehavior.emptyCauldron(state, world, pos, player, hand, stack,
					DynamicCellItem.getCellWithFluid(Fluids.LAVA), (stateX) -> true, SoundEvents.ITEM_BUCKET_FILL);
		};

		CauldronBehavior FILL_CELL_WITH_WATER = (state, world, pos, player, hand, stack) -> {
			if (!FluidUtils.isContainerEmpty(stack)) {
				return ActionResult.PASS;
			}

			return CauldronBehavior.emptyCauldron(state, world, pos, player, hand, stack,
					DynamicCellItem.getCellWithFluid(Fluids.WATER), (stateX) -> true, SoundEvents.ITEM_BUCKET_FILL_LAVA);
		};

		CauldronBehavior FILL_FROM_CELL = (state, world, pos, player, hand, stack) -> {
			Fluid cellFluid = ((DynamicCellItem) stack.getItem()).getFluid(stack);
			if (cellFluid == Fluids.WATER) {
				return fillCauldronFromCell(world, pos, player, hand, stack,
						Blocks.WATER_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, 3),
						SoundEvents.ITEM_BUCKET_EMPTY);
			} else if (cellFluid == Fluids.LAVA) {
				return fillCauldronFromCell(world, pos, player, hand, stack,
						Blocks.LAVA_CAULDRON.getDefaultState(),
						SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
			}

			return ActionResult.PASS;
		};

		CauldronBehavior.LAVA_CAULDRON_BEHAVIOR.put(TRContent.CELL, FILL_CELL_WITH_LAVA);
		CauldronBehavior.WATER_CAULDRON_BEHAVIOR.put(TRContent.CELL, FILL_CELL_WITH_WATER);
		CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.put(TRContent.CELL, FILL_FROM_CELL);
	}

	static ActionResult fillCauldronFromCell(World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack, BlockState state, SoundEvent soundEvent) {
		if (!world.isClient) {
			player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(TRContent.CELL)));
			player.incrementStat(Stats.FILL_CAULDRON);
			world.setBlockState(pos, state);
			world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
			world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
		}

		return ActionResult.success(world.isClient);
	}
}
