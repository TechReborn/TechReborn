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

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LavaCauldronBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
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

	protected final Tank internalTank = new Tank("tank", FluidValue.BUCKET);

	public DrainBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.DRAIN, pos, state);
	}

	@Override
	public void tick(Level world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || world.isClientSide()) {
			return;
		}

		int ticks = TechRebornConfig.ticksUntilNextDrainAttempt;
		if (ticks > 0 && world.getGameTime() % ticks == 0) {

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
		BlockPos above = this.getBlockPos().above();

		// Block and state above drain
		BlockState aboveBlockState = level.getBlockState(above);
		Block aboveBlock = aboveBlockState.getBlock();

		if (aboveBlock instanceof BucketPickup) {
			ItemStack fluidContainer = ((BucketPickup) aboveBlock).pickupBlock(null, level, above, aboveBlockState);
			if (fluidContainer.getItem() instanceof ItemFluidInfo) {
				Fluid drainFluid = ((ItemFluidInfo) fluidContainer.getItem()).getFluid(fluidContainer);
				internalTank.setFluidInstance(new FluidInstance(drainFluid, FluidValue.BUCKET));
			} else {
				TechReborn.LOGGER.debug("Could not get Fluid from ItemStack " + fluidContainer.getItem());
			}
		}
		if (aboveBlock instanceof LayeredCauldronBlock && aboveBlockState.is(Blocks.WATER_CAULDRON)) { //ensure Water cauldron
			Fluid drainFluid = Fluids.WATER;
			int fluidLevel;
			if (aboveBlockState.hasProperty(LayeredCauldronBlock.LEVEL)){
				fluidLevel = aboveBlockState.getValue(LayeredCauldronBlock.LEVEL);
			}
			else {
				return;
			}
			level.setBlockAndUpdate(above, Blocks.CAULDRON.defaultBlockState());
			internalTank.setFluidInstance(
				new FluidInstance(drainFluid, FluidValue.BUCKET.fraction(3).multiply(fluidLevel))
			);
		}
		if (aboveBlock instanceof LavaCauldronBlock){
			level.setBlockAndUpdate(above, Blocks.CAULDRON.defaultBlockState());
			internalTank.setFluidInstance(
				new FluidInstance(Fluids.LAVA, FluidValue.BUCKET)
			);
		}
	}

	@Override
	public ItemStack getToolDrop(Player p0) {
		return TRContent.Machine.DRAIN.getStack();
	}

	@Override
	public void saveAdditional(ValueOutput view) {
		super.saveAdditional(view);
		internalTank.write(view);
	}

	@Override
	public void loadAdditional(ValueInput view) {
		super.loadAdditional(view);
		internalTank.read(view);
	}
}
