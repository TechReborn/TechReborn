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

package techreborn.blocks.machine.tier0;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import techreborn.blockentity.GuiType;
import techreborn.blockentity.machine.iron.IronAlloyFurnaceBlockEntity;
import techreborn.blocks.GenericMachineBlock;

public class IronAlloyFurnaceBlock extends GenericMachineBlock {

	public IronAlloyFurnaceBlock(String name) {
		super(GuiType.ALLOY_FURNACE, IronAlloyFurnaceBlockEntity::new, name);
	}

	// Block
	@Override
	public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
		if (!isActive(stateIn)) {
			return;
		}

		final double x = (double) pos.getX() + 0.5D;
		final double y = (double) pos.getY() + 2.0D / 16.0D + rand.nextDouble() * 5.0D / 16.0D;
		final double z = (double) pos.getZ() + 0.5D;
		if (rand.nextDouble() < 0.1D) {
			worldIn.playLocalSound(x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
		}

		Direction facing = stateIn.getValue(FACING);
		Direction.Axis facing$Axis = facing.getAxis();
		double double_5 = rand.nextDouble() * 0.6D - 0.3D;
		double deltaX = facing$Axis == Direction.Axis.X ? (double) facing.getStepX() * 0.52D : double_5;
		double deltaZ = facing$Axis == Direction.Axis.Z ? (double) facing.getStepZ() * 0.52D : double_5;
		worldIn.addParticle(ParticleTypes.SMOKE, x + deltaX, y, z + deltaZ, 0.0D, 0.0D, 0.0D);
		worldIn.addParticle(ParticleTypes.FLAME, x + deltaX, y, z + deltaZ, 0.0D, 0.0D, 0.0D);
	}
}
