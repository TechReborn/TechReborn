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
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import reborncore.common.crafting.RebornRecipe;
import reborncore.common.crafting.RecipeUtils;
import reborncore.common.fluid.RebornBucketItem;
import reborncore.common.fluid.container.ItemFluidInfo;
import techreborn.TechReborn;
import techreborn.config.TechRebornConfig;
import techreborn.items.DynamicCellItem;

import java.util.List;

/**
 * Created by drcrazy on 10-Jan-20 for TechReborn-1.15.
 */
public class TRDispenserBehavior {

	public static void init() {
		if (TechRebornConfig.dispenseScrapboxes) {
			DispenserBlock.registerBehavior(TRContent.SCRAP_BOX, new ItemDispenserBehavior() {
				public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
					List<RebornRecipe> scrapboxRecipeList = RecipeUtils.getRecipes(pointer.world(), ModRecipes.SCRAPBOX);
					int random = Random.create().nextInt(scrapboxRecipeList.size());
					ItemStack out = scrapboxRecipeList.get(random).outputs().getFirst().copy();
					stack.split(1);

					Direction facing = pointer.state().get(DispenserBlock.FACING);
					Position position = DispenserBlock.getOutputLocation(pointer);
					spawnItem(pointer.world(), out, 6, facing, position);
					return stack;
				}
			});
		}

		DispenserBlock.registerBehavior(TRContent.CELL, new ItemDispenserBehavior() {
			public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
				DynamicCellItem cell = (DynamicCellItem) stack.getItem();
				WorldAccess iWorld = pointer.world();
				BlockPos blockPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));
				BlockState blockState = iWorld.getBlockState(blockPos);
				Block block = blockState.getBlock();
				if (cell.getFluid(stack) == Fluids.EMPTY) {
					// fill cell
					if (block instanceof FluidDrainable) {
						ItemStack fluidContainer = ((FluidDrainable) block).tryDrainFluid(null, iWorld, blockPos, blockState);
						Fluid fluid = null;
						if (fluidContainer.getItem() instanceof ItemFluidInfo) {
							fluid = ((ItemFluidInfo) fluidContainer.getItem()).getFluid(fluidContainer);
						} else {
							TechReborn.LOGGER.debug("Could not get Fluid from ItemStack " + fluidContainer.getItem());
						}
						if (!(fluid instanceof FlowableFluid)) {
							return super.dispenseSilently(pointer, stack);
						} else {
							ItemStack filledCell = DynamicCellItem.getCellWithFluid(fluid, 1);
							if (stack.getCount() == 1) {
								stack = filledCell;
							} else {
								stack.decrement(1);
								if (pointer.blockEntity().addToFirstFreeSlot(filledCell).getCount() < 0) {
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
					if (cell.placeFluid(null, pointer.world(), blockPos, null, stack)) {
						ItemStack emptyCell = cell.getEmpty();
						if (stack.getCount() == 1) {
							stack = emptyCell;
						} else {
							stack.decrement(1);
							if (pointer.blockEntity().addToFirstFreeSlot(emptyCell).getCount() < 0) {
								this.dispense(pointer, emptyCell);
							}
						}
					}
					return stack;
				}
			}
		});

		for (ModFluids fluid : ModFluids.values()) {
			DispenserBlock.registerBehavior(fluid, new ItemDispenserBehavior() {
				public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
					RebornBucketItem bucket = (RebornBucketItem) stack.getItem();
					BlockPos blockPos = pointer.pos().offset(pointer.state().get(DispenserBlock.FACING));

					if (bucket.placeFluid(null, pointer.world(), blockPos, null)) {
						ItemStack emptyBucket = new ItemStack(Items.BUCKET);
						if (stack.getCount() == 1) {
							stack = emptyBucket;
						} else {
							stack.decrement(1);
							if (pointer.blockEntity().addToFirstFreeSlot(emptyBucket).getCount() < 0) {
								this.dispense(pointer, emptyBucket);
							}
						}
					}
					return stack;
				}
			});
		}
	}
}
