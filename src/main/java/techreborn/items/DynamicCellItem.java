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

package techreborn.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.WordUtils;
import reborncore.common.fluid.FluidUtil;
import reborncore.common.fluid.container.ItemFluidInfo;
import reborncore.common.util.ItemNBTHelper;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import techreborn.utils.FluidUtils;

import org.jetbrains.annotations.Nullable;

/**
 * Created by modmuss50 on 17/05/2016.
 */
public class DynamicCellItem extends Item implements ItemFluidInfo {

	public DynamicCellItem() {
		super(new Item.Settings().group(TechReborn.ITEMGROUP).maxCount(16));
	}

	// Thanks vanilla :)
	private void playEmptyingSound(@Nullable PlayerEntity playerEntity, WorldAccess world, BlockPos blockPos, Fluid fluid) {
		SoundEvent soundEvent = fluid.isIn(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY;
		world.playSound(playerEntity, blockPos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}

	public static ItemStack getCellWithFluid(Fluid fluid, int stackSize) {
		Validate.notNull(fluid);
		ItemStack stack = new ItemStack(TRContent.CELL);
		ItemNBTHelper.getNBT(stack).putString("fluid", Registry.FLUID.getId(fluid).toString());
		stack.setCount(stackSize);
		return stack;
	}

	public static ItemStack getCellWithFluid(Fluid fluid) {
		return getCellWithFluid(fluid, 1);
	}

	public static ItemStack getEmptyCell(int amount) {
		return new ItemStack(TRContent.CELL, amount);
	}

	private void insertOrDropStack(PlayerEntity playerEntity, ItemStack stack) {
		if (!playerEntity.inventory.insertStack(stack)) {
			playerEntity.dropStack(stack);
		}
	}

	public boolean placeFluid(@Nullable PlayerEntity player, World world, BlockPos pos, @Nullable BlockHitResult hitResult, ItemStack filledCell) {
		Fluid fluid = getFluid(filledCell);
		if (fluid == Fluids.EMPTY) {
			return false;
		}

		BlockState blockState = world.getBlockState(pos);
		Material material = blockState.getMaterial();
		boolean canPlace = blockState.canBucketPlace(fluid);

		if (!blockState.isAir() && !canPlace && (!(blockState.getBlock() instanceof FluidFillable) || !((FluidFillable) blockState.getBlock()).canFillWithFluid(world, pos, blockState, fluid))) {
			return hitResult != null && this.placeFluid(player, world, hitResult.getBlockPos().offset(hitResult.getSide()), null, filledCell);
		} else {
			if (world.getDimension().isUltrawarm() && fluid.isIn(FluidTags.WATER)) {
				int i = pos.getX();
				int j = pos.getY();
				int k = pos.getZ();
				world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

				for (int l = 0; l < 8; ++l) {
					world.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
				}
			} else if (blockState.getBlock() instanceof FluidFillable && fluid == Fluids.WATER) {
				if (((FluidFillable) blockState.getBlock()).tryFillWithFluid(world, pos, blockState, ((FlowableFluid) fluid).getStill(false))) {
					this.playEmptyingSound(player, world, pos, fluid);
				}
			} else {
				if (!world.isClient && canPlace && !material.isLiquid()) {
					world.breakBlock(pos, true);
				}

				this.playEmptyingSound(player, world, pos, fluid);
				world.setBlockState(pos, fluid.getDefaultState().getBlockState(), 11);
			}
			return true;
		}
	}

	@Override
	public void appendStacks(ItemGroup tab, DefaultedList<ItemStack> subItems) {
		if (!isIn(tab)) {
			return;
		}
		subItems.add(getEmptyCell(1));
		for (Fluid fluid : FluidUtils.getAllFluids()) {
			if (fluid.isStill(fluid.getDefaultState())) {
				subItems.add(getCellWithFluid(fluid));
			}
		}
	}

	@Override
	public Text getName(ItemStack itemStack) {
		Fluid fluid = getFluid(itemStack);
		if (fluid != Fluids.EMPTY) {
			//TODO use translation keys for fluid and the cell https://fabric.asie.pl/wiki/tutorial:lang?s[]=translation might be useful
			return new LiteralText(WordUtils.capitalizeFully(FluidUtil.getFluidName(fluid).replaceAll("_", " ")) + " Cell");
		}
		return super.getName(itemStack);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		Fluid containedFluid = getFluid(stack);

		BlockHitResult hitResult = raycast(world, player, containedFluid == Fluids.EMPTY ? RaycastContext.FluidHandling.SOURCE_ONLY : RaycastContext.FluidHandling.NONE);
		if (hitResult.getType() == HitResult.Type.MISS) {
			return TypedActionResult.pass(stack);
		} else if (hitResult.getType() != HitResult.Type.BLOCK) {
			return TypedActionResult.pass(stack);
		} else {
			BlockPos hitPos = hitResult.getBlockPos();
			BlockState hitState = world.getBlockState(hitPos);

			Direction side = hitResult.getSide();
			BlockPos placePos = hitPos.offset(side);

			if (world.canPlayerModifyAt(player, hitPos) && player.canPlaceOn(placePos, side, stack)) {
				if (containedFluid == Fluids.EMPTY) {
					if (hitState.getBlock() instanceof FluidDrainable) {
						Fluid drainFluid = ((FluidDrainable) hitState.getBlock()).tryDrainFluid(world, hitPos, hitState);
						if (drainFluid != Fluids.EMPTY) {
							if (stack.getCount() == 1) {
								stack = getCellWithFluid(drainFluid, 1);
							} else {
								stack.decrement(1);
								insertOrDropStack(player, getCellWithFluid(drainFluid, 1));
							}

							playEmptyingSound(player, world, hitPos, drainFluid);
							return TypedActionResult.success(stack);
						}
					}

				} else {
					BlockState placeState = world.getBlockState(placePos);
					if (placeState.canBucketPlace(containedFluid)) {
						placeFluid(player, world, placePos, hitResult, stack);

						if (stack.getCount() == 1) {
							stack = getEmpty();
						} else {
							stack.decrement(1);
							insertOrDropStack(player, getEmpty());
						}

						return TypedActionResult.success(stack);
					}
				}
			}
		}
		return TypedActionResult.fail(stack);
	}

	// ItemFluidInfo
	@Override
	public ItemStack getEmpty() {
		return new ItemStack(this);
	}

	@Override
	public ItemStack getFull(Fluid fluid) {
		return getCellWithFluid(fluid);
	}

	@Override
	public Fluid getFluid(ItemStack itemStack) {
		CompoundTag tag = itemStack.getTag();
		if (tag != null && tag.contains("fluid")) {
			return Registry.FLUID.get(new Identifier(tag.getString("fluid")));
		}
		return Fluids.EMPTY;
	}
}
