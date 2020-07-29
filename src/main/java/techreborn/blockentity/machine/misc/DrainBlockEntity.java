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

package techreborn.blockentity.machine.misc;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.util.Tank;
import techreborn.init.TRBlockEntities;

import org.jetbrains.annotations.Nullable;

public class DrainBlockEntity extends MachineBaseBlockEntity {

	protected Tank internalTank = new Tank("tank", FluidValue.BUCKET, this);

	public DrainBlockEntity() {
		this(TRBlockEntities.DRAIN);
	}

	public DrainBlockEntity(BlockEntityType<?> blockEntityTypeIn) {
		super(blockEntityTypeIn);
	}

	@Override
	public void tick() {
		super.tick();
		if (world.isClient) {
			return;
		}

		if (world.getTime() % 10 == 0) {

			if (internalTank.isEmpty()) {
				tryDrain();
			}
		}
	}

	@Nullable
	@Override
	public Tank getTank() {
		return internalTank;
	}

	private void tryDrain() {
		// Position above drain
		BlockPos above = this.getPos().up();

		// Block and state above drain
		BlockState aboveBlockState = world.getBlockState(above);
		Block aboveBlock = aboveBlockState.getBlock();

		if (aboveBlock instanceof FluidDrainable) {

			Fluid drainFluid = ((FluidDrainable) aboveBlock).tryDrainFluid(world, above, aboveBlockState);

			if (drainFluid != Fluids.EMPTY) {
				internalTank.setFluidInstance(new FluidInstance(drainFluid, FluidValue.BUCKET));
			}
		}
	}
}
