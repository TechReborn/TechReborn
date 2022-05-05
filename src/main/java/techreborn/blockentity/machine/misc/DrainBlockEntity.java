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

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;
import reborncore.common.fluid.container.ItemFluidInfo;
import reborncore.common.util.Tank;
import techreborn.TechReborn;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class DrainBlockEntity extends MachineBaseBlockEntity implements IToolDrop {

	protected Tank internalTank = new Tank("tank", FluidValue.BUCKET, this);

	public DrainBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.DRAIN, pos, state);
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || world.isClient) {
			return;
		}

		int ticks = TechRebornConfig.ticksUntilNextDrainAttempt;
		if (ticks > 0 && world.getTime() % ticks == 0) {

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
			ItemStack fluidContainer = ((FluidDrainable) aboveBlock).tryDrainFluid(world, above, aboveBlockState);
			if (fluidContainer.getItem() instanceof ItemFluidInfo) {
				Fluid drainFluid = ((ItemFluidInfo) fluidContainer.getItem()).getFluid(fluidContainer);
				internalTank.setFluidInstance(new FluidInstance(drainFluid, FluidValue.BUCKET));
			} else {
				TechReborn.LOGGER.debug("Could not get Fluid from ItemStack " + fluidContainer.getItem());
			}
		}
		if (aboveBlock instanceof LeveledCauldronBlock && aboveBlockState.isOf(Blocks.WATER_CAULDRON)) { //ensure Water cauldron
			Fluid drainFluid = Fluids.WATER;
			int level;
			if (aboveBlockState.contains(LeveledCauldronBlock.LEVEL)){
				level = aboveBlockState.get(LeveledCauldronBlock.LEVEL);
			}
			else {
				return;
			}
			world.setBlockState(above, Blocks.CAULDRON.getDefaultState());
			internalTank.setFluidInstance(
				new FluidInstance(drainFluid, FluidValue.BUCKET.fraction(3).multiply(level))
			);
		}
		if (aboveBlock instanceof LavaCauldronBlock){
			world.setBlockState(above, Blocks.CAULDRON.getDefaultState());
			internalTank.setFluidInstance(
				new FluidInstance(Fluids.LAVA, FluidValue.BUCKET)
			);
		}
	}

	@Override
	public ItemStack getToolDrop(PlayerEntity p0) {
		return TRContent.Machine.DRAIN.getStack();
	}
}
