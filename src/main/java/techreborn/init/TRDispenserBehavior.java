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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.WorldAccess;
import reborncore.common.crafting.RebornRecipe;
import techreborn.config.TechRebornConfig;
import techreborn.items.DynamicCellItem;

import java.util.List;
import java.util.Random;

/**
 * Created by drcrazy on 10-Jan-20 for TechReborn-1.15.
 */
public class TRDispenserBehavior {

	public static void init() {
		if (TechRebornConfig.dispenseScrapboxes) {
			DispenserBlock.registerBehavior(TRContent.SCRAP_BOX, new ItemDispenserBehavior() {
				public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
					List<RebornRecipe> scrapboxRecipeList = ModRecipes.SCRAPBOX.getRecipes(pointer.getWorld());
					int random = new Random().nextInt(scrapboxRecipeList.size());
					ItemStack out = scrapboxRecipeList.get(random).getOutputs().get(0);
					stack.split(1);

					Direction facing = pointer.getBlockState().get(DispenserBlock.FACING);
					Position position = DispenserBlock.getOutputLocation(pointer);
					spawnItem(pointer.getWorld(), out, 6, facing, position);
					return stack;
				}
			});
		}

		DispenserBlock.registerBehavior(TRContent.CELL, new ItemDispenserBehavior() {
			public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
				DynamicCellItem cell = (DynamicCellItem) stack.getItem();
				WorldAccess iWorld = pointer.getWorld();
				BlockPos blockPos = pointer.getBlockPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
				BlockState blockState = iWorld.getBlockState(blockPos);
				Block block = blockState.getBlock();
				if (cell.getFluid(stack) == Fluids.EMPTY) {
					// fill cell
					if (block instanceof FluidDrainable) {
						Fluid fluid = ((FluidDrainable) block).tryDrainFluid(iWorld, blockPos, blockState);
						if (!(fluid instanceof FlowableFluid)) {
							return super.dispenseSilently(pointer, stack);
						} else {
							ItemStack filledCell = DynamicCellItem.getCellWithFluid(fluid, 1);
							if (stack.getCount() == 1) {
								stack = filledCell;
							} else {
								stack.decrement(1);
								if (((DispenserBlockEntity) pointer.getBlockEntity()).addToFirstFreeSlot(filledCell) < 0) {
									this.dispense(pointer, filledCell);
								}
							}
							return stack;
						}
					} else {
						return super.dispenseSilently(pointer, stack);
					}
				} else {
					// drain cell
					if (cell.placeFluid(null, pointer.getWorld(), blockPos, null, stack)) {
						ItemStack emptyCell = cell.getEmpty();
						if (stack.getCount() == 1) {
							stack = emptyCell;
						} else {
							stack.decrement(1);
							if (((DispenserBlockEntity) pointer.getBlockEntity()).addToFirstFreeSlot(emptyCell) < 0) {
								this.dispense(pointer, emptyCell);
							}
						}
					}
					return stack;
				}
			}
		});
	}
}
