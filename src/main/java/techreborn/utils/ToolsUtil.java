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

package techreborn.utils;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.common.powerSystem.RcEnergyItem;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author drcrazy
 */
public class ToolsUtil {
	public static void breakBlock(ItemStack tool, World world, BlockPos pos, LivingEntity entityLiving, int cost) {
		if (!(entityLiving instanceof PlayerEntity)) {
			return;
		}
		BlockState blockState = world.getBlockState(pos);
		if (blockState.getHardness(world, pos) == -1.0F) {
			return;
		}

		if (!((RcEnergyItem) tool.getItem()).tryUseEnergy(tool, cost)) {
			return;
		}

		blockState.getBlock().afterBreak(world, (PlayerEntity) entityLiving, pos, blockState, world.getBlockEntity(pos), tool);
		world.setBlockState(pos, Blocks.AIR.getDefaultState());
		world.removeBlockEntity(pos);
	}

	/**
	 * Fills in set of {@link BlockPos} which should be broken by AOE mining
	 *
	 * @param worldIn      {@link World} World reference
	 * @param pos          {@link BlockPos} Position of originally broken block
	 * @param entityLiving {@link LivingEntity} Player who broke block
	 * @param radius       {@code int} Radius of additional blocks to include. E.g. for 3x3 mining radius will be 1
	 * @return {@link Set} Set of {@link BlockPos} to process by tool block break logic
	 */
	public static Set<BlockPos> getAOEMiningBlocks(World worldIn, BlockPos pos, @Nullable LivingEntity entityLiving, int radius) {
		return getAOEMiningBlocks(worldIn, pos, entityLiving, radius, true);
	}

	/**
	 * Fills in set of {@link BlockPos} which should be broken by AOE mining
	 *
	 * @param worldIn          {@link World} World reference
	 * @param pos              {@link BlockPos} Position of originally broken block
	 * @param entityLiving     {@link LivingEntity} Player who broke block
	 * @param radius           {@code int} Radius of additional blocks to include. E.g. for 3x3 mining radius will be 1
	 * @param placeDummyBlocks {@code boolean} Whether to place dummy blocks
	 * @return {@link Set} Set of {@link BlockPos} to process by tool block break logic
	 */
	public static Set<BlockPos> getAOEMiningBlocks(World worldIn, BlockPos pos, @Nullable LivingEntity entityLiving, int radius, boolean placeDummyBlocks) {
		if (!(entityLiving instanceof PlayerEntity playerIn)) {
			return ImmutableSet.of();
		}
		Set<BlockPos> targetBlocks = new HashSet<>();

		if (placeDummyBlocks) {
			//Put a dirt block down to raytrace with to stop it raytracing past the intended block
			worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState());
		}

		HitResult hitResult = playerIn.raycast(20D, 0F, false);

		if (placeDummyBlocks) {
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
		}

		if (!(hitResult instanceof BlockHitResult)) {
			return Collections.emptySet();
		}
		Direction direction = ((BlockHitResult) hitResult).getSide();

		if (direction == Direction.SOUTH || direction == Direction.NORTH) {
			for (int x = -radius; x <= radius; x++) {
				for (int y = -1; y <= 1 + (radius - 1) * 2; y++) {
					targetBlocks.add(pos.add(x, y, 0));
				}
			}
		} else if (direction == Direction.EAST || direction == Direction.WEST) {
			for (int z = -radius; z <= radius; z++) {
				for (int y = -1; y <= 1 + (radius - 1) * 2; y++) {
					targetBlocks.add(pos.add(0, y, z));
				}
			}
		} else if (direction == Direction.DOWN || direction == Direction.UP) {
			Direction playerDirection = playerIn.getHorizontalFacing();
			int minX = 0;
			int maxX = 0;
			int minZ = 0;
			int maxZ = 0;

			switch (playerDirection) {
				case SOUTH -> {
					minZ = -1;
					maxZ = 1 + (radius - 1) * 2;
					minX = -radius;
					maxX = radius;
				}
				case NORTH -> {
					minZ = -1 - (radius - 1) * 2;
					maxZ = 1;
					minX = -radius;
					maxX = radius;
				}
				case WEST -> {
					minZ = -radius;
					maxZ = radius;
					minX = -1 - (radius - 1) * 2;
					maxX = 1;
				}
				case EAST -> {
					minZ = -radius;
					maxZ = radius;
					minX = -1;
					maxX = 1 + (radius - 1) * 2;
				}
			}
			for (int x = minX; x <= maxX; x++) {
				for (int z = minZ; z <= maxZ; z++) {
					targetBlocks.add(pos.add(x, 0, z));
				}
			}
		}
		return targetBlocks;
	}
}
