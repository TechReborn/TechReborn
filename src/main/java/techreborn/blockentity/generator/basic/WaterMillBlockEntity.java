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

package techreborn.blockentity.generator.basic;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

/**
 * Created by modmuss50 on 25/02/2016.
 */
public class WaterMillBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop {

	int waterBlocks = 0;

	public WaterMillBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.WATER_MILL, pos, state);
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world.isClient) {
			return;
		}

		if (world.getTime() % 20 == 0) {
			checkForWater();
		}
		if (waterBlocks > 0) {
			addEnergyProbabilistic(waterBlocks * TechRebornConfig.waterMillEnergyMultiplier);
			world.setBlockState(pos, world.getBlockState(pos).with(BlockMachineBase.ACTIVE, true));
		} else {
			world.setBlockState(pos, world.getBlockState(pos).with(BlockMachineBase.ACTIVE, false));
		}
	}

	public void checkForWater() {
		waterBlocks = 0;
		for (Direction facing : Direction.values()) {
			if (facing.getAxis().isHorizontal() && world.getBlockState(pos.offset(facing)).getBlock() == Blocks.WATER) {
				waterBlocks++;
			}
		}
	}

	@Override
	public long getBaseMaxPower() {
		return TechRebornConfig.waterMillMaxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(@Nullable Direction side) {
		return false;
	}

	@Override
	public long getBaseMaxOutput() {
		return TechRebornConfig.waterMillMaxOutput;
	}

	@Override
	public long getBaseMaxInput() {
		return 0;
	}

	@Override
	public ItemStack getToolDrop(PlayerEntity playerIn) {
		return TRContent.Machine.WATER_MILL.getStack();
	}
}
