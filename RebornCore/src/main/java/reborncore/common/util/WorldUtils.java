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

package reborncore.common.util;

import org.jspecify.annotations.Nullable;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Created by Mark on 13/03/2016.
 */
public class WorldUtils {

	public static void updateBlock(Level world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		world.sendBlockUpdated(pos, state, state, 3);
	}

	/**
	 * Checks if chunk is loaded using proper chunk manager
	 *
	 * @param world {@link Level} World object
	 * @param pos   {@link BlockPos} X and Z coordinates to check
	 * @return {@code boolean} True if chunk is loaded
	 */
	public static boolean isChunkLoaded(Level world, BlockPos pos){
		return world.getChunkSource().hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
	}


	public static void dropItem(ItemStack itemStack, Level world, BlockPos pos) {
		RandomSource rand = RandomSource.create();

		float dX = rand.nextFloat() * 0.8F + 0.1F;
		float dY = rand.nextFloat() * 0.8F + 0.1F;
		float dZ = rand.nextFloat() * 0.8F + 0.1F;

		ItemEntity entityItem = new ItemEntity(world, pos.getX() + dX, pos.getY() + dY, pos.getZ() + dZ,
				itemStack.copy());

		float factor = 0.05F;
		entityItem.setDeltaMovement(new Vec3(rand.nextGaussian() * factor, rand.nextGaussian() * factor + 0.2F, rand.nextGaussian() * factor));
		if (!world.isClientSide()) {
			world.addFreshEntity(entityItem);
		}
	}

	public static void dropItem(Item item, Level world, BlockPos pos) {
		dropItem(new ItemStack(item), world, pos);
	}

	public static void dropItems(List<ItemStack> itemStackList, Level world, BlockPos pos) {
		for (final ItemStack itemStack : itemStackList) {
			WorldUtils.dropItem(itemStack, world, pos);
			itemStack.setCount(0);
		}
	}

	public static HolderGetter<Block> getBlockRegistryWrapper(@Nullable Level world) {
		return world != null ? world.holderLookup(Registries.BLOCK) : BuiltInRegistries.BLOCK;
	}
}
