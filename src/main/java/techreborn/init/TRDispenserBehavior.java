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

import reborncore.common.crafting.RecipeUtils;
import reborncore.common.fluid.RebornBucketItem;
import reborncore.common.fluid.container.ItemFluidInfo;
import techreborn.TechReborn;
import techreborn.config.TechRebornConfig;
import techreborn.items.DynamicCellItem;
import techreborn.recipe.recipes.ScrapBoxRecipe;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

/**
 * Created by drcrazy on 10-Jan-20 for TechReborn-1.15.
 */
public class TRDispenserBehavior {

	public static void init() {
		if (TechRebornConfig.dispenseScrapboxes) {
			DispenserBlock.registerBehavior(TRContent.SCRAP_BOX, new DefaultDispenseItemBehavior() {
				public ItemStack execute(BlockSource pointer, ItemStack stack) {
					List<ScrapBoxRecipe> scrapboxRecipeList = RecipeUtils.getRecipes(pointer.level(), ModRecipes.SCRAPBOX);
					int random = RandomSource.create().nextInt(scrapboxRecipeList.size());
					ItemStack out = scrapboxRecipeList.get(random).outputs().getFirst().create();
					stack.split(1);

					Direction facing = pointer.state().getValue(DispenserBlock.FACING);
					Position position = DispenserBlock.getDispensePosition(pointer);
					spawnItem(pointer.level(), out, 6, facing, position);
					return stack;
				}
			});
		}

		DispenserBlock.registerBehavior(TRContent.CELL, new DefaultDispenseItemBehavior() {
			public ItemStack execute(BlockSource pointer, ItemStack stack) {
				DynamicCellItem cell = (DynamicCellItem) stack.getItem();
				LevelAccessor iWorld = pointer.level();
				BlockPos blockPos = pointer.pos().relative(pointer.state().getValue(DispenserBlock.FACING));
				BlockState blockState = iWorld.getBlockState(blockPos);
				Block block = blockState.getBlock();
				if (cell.getFluid(stack) == Fluids.EMPTY) {
					// fill cell
					if (block instanceof BucketPickup) {
						ItemStack fluidContainer = ((BucketPickup) block).pickupBlock(null, iWorld, blockPos, blockState);
						Fluid fluid = null;
						if (fluidContainer.getItem() instanceof ItemFluidInfo) {
							fluid = ((ItemFluidInfo) fluidContainer.getItem()).getFluid(fluidContainer);
						} else {
							TechReborn.LOGGER.debug("Could not get Fluid from ItemStack " + fluidContainer.getItem());
						}
						if (!(fluid instanceof FlowingFluid)) {
							return super.execute(pointer, stack);
						} else {
							ItemStack filledCell = DynamicCellItem.getCellWithFluid(fluid, 1);
							if (stack.getCount() == 1) {
								stack = filledCell;
							} else {
								stack.shrink(1);
								if (pointer.blockEntity().insertItem(filledCell).getCount() < 0) {
									this.dispense(pointer, filledCell);
								}
							}
							return stack;
						}
					} else {
						return super.execute(pointer, stack);
					}
				} else {
					// drain cell
					if (cell.placeFluid(null, pointer.level(), blockPos, null, stack)) {
						ItemStack emptyCell = cell.getEmpty();
						if (stack.getCount() == 1) {
							stack = emptyCell;
						} else {
							stack.shrink(1);
							if (pointer.blockEntity().insertItem(emptyCell).getCount() < 0) {
								this.dispense(pointer, emptyCell);
							}
						}
					}
					return stack;
				}
			}
		});

		for (ModFluids fluid : ModFluids.values()) {
			DispenserBlock.registerBehavior(fluid, new DefaultDispenseItemBehavior() {
				public ItemStack execute(BlockSource pointer, ItemStack stack) {
					RebornBucketItem bucket = (RebornBucketItem) stack.getItem();
					BlockPos blockPos = pointer.pos().relative(pointer.state().getValue(DispenserBlock.FACING));

					if (bucket.emptyContents(null, pointer.level(), blockPos, null)) {
						ItemStack emptyBucket = new ItemStack(Items.BUCKET);
						if (stack.getCount() == 1) {
							stack = emptyBucket;
						} else {
							stack.shrink(1);
							if (pointer.blockEntity().insertItem(emptyBucket).getCount() < 0) {
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
