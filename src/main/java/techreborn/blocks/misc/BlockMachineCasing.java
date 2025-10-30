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

package techreborn.blocks.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;
import reborncore.common.multiblock.BlockMultiblockBase;
import techreborn.blockentity.machine.multiblock.casing.MachineCasingBlockEntity;
import techreborn.init.TRBlockSettings;
import techreborn.utils.DirectionUtils;

public class BlockMachineCasing extends BlockMultiblockBase {

	public final int heatCapacity;

	public BlockMachineCasing(int heatCapacity, String name) {
		super(TRBlockSettings.machineCasing(name));
		registerDefaultState(defaultBlockState().setValue(DirectionUtils.HORIZONTAL_NEIGHBORS, 0));
		this.heatCapacity = heatCapacity;
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(DirectionUtils.HORIZONTAL_NEIGHBORS);
	}

	@Override
	public void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
		super.affectNeighborsAfterRemoval(state, world, pos, moved);
		DirectionUtils.removeHorizontalNeighbor(world, pos, state, block -> block instanceof BlockMachineCasing);
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.setPlacedBy(world, pos, state, placer, itemStack);
		DirectionUtils.addHorizontalNeighbor(world, pos, state, block -> block instanceof BlockMachineCasing);
	}

	public static int getHeatFromState(BlockState state) {
		Block block = state.getBlock();
		if (block instanceof BlockMachineCasing casing) {
			return casing.heatCapacity;
		}

		return 0;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MachineCasingBlockEntity(pos, state);
	}
}
