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

package techreborn.blocks.storage.energy;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import techreborn.blockentity.storage.energy.idsu.InterdimensionalSUBlockEntity;
import techreborn.client.GuiType;

public class InterdimensionalSUBlock extends EnergyStorageBlock {

	public InterdimensionalSUBlock() {
		super("IDSU", GuiType.IDSU);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView worldIn) {
		return new InterdimensionalSUBlockEntity();
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		final BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
		if (blockEntity instanceof InterdimensionalSUBlockEntity) {
			((InterdimensionalSUBlockEntity) blockEntity).ownerUdid = context.getPlayer().getUuid().toString();
		}
		return this.getDefaultState();
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onPlaced(world, pos, state, placer, stack);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof InterdimensionalSUBlockEntity) {
			((InterdimensionalSUBlockEntity) blockEntity).ownerUdid = placer.getUuid().toString();
		}
	}

}
