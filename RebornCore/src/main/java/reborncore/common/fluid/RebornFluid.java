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

package reborncore.common.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.function.Supplier;

public abstract class RebornFluid extends FlowableFluid {

	private final boolean still;

	private final FluidSettings fluidSettings;
	private final Supplier<RebornFluidBlock> fluidBlockSupplier;
	private final Supplier<RebornBucketItem> bucketItemSuppler;
	private final Supplier<RebornFluid> flowingSuppler;
	private final Supplier<RebornFluid> stillSuppler;

	public RebornFluid(boolean still, FluidSettings fluidSettings, Supplier<RebornFluidBlock> fluidBlockSupplier, Supplier<RebornBucketItem> bucketItemSuppler, Supplier<RebornFluid> flowingSuppler, Supplier<RebornFluid> stillSuppler) {
		this.still = still;
		this.fluidSettings = fluidSettings;
		this.fluidBlockSupplier = fluidBlockSupplier;
		this.bucketItemSuppler = bucketItemSuppler;
		this.flowingSuppler = flowingSuppler;
		this.stillSuppler = stillSuppler;
	}

	public FluidSettings getFluidSettings() {
		return fluidSettings;
	}

	@Override
	public RebornFluid getFlowing() {
		return flowingSuppler.get();
	}

	@Override
	public RebornFluid getStill() {
		return stillSuppler.get();
	}

	@Override
	protected boolean isInfinite() {
		return false;
	}

	@Override
	public boolean isStill(FluidState fluidState) {
		return still;
	}

	@Override
	protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {

	}

	@Override
	protected int getFlowSpeed(WorldView world) {
		return 4;
	}

	@Override
	protected int getLevelDecreasePerBlock(WorldView world) {
		return 1;
	}

	@Override
	public Item getBucketItem() {
		return bucketItemSuppler.get();
	}

	@Override
	protected boolean canBeReplacedWith(FluidState fluidState, BlockView blockView, BlockPos blockPos, Fluid fluid, Direction direction) {
		return false;
	}

	@Override
	public boolean matchesType(Fluid fluid) {
		return getFlowing() == fluid || getStill() == fluid;
	}

	@Override
	public int getTickRate(WorldView world) {
		return 10;
	}

	@Override
	protected float getBlastResistance() {
		return 100F;
	}

	@Override
	protected BlockState toBlockState(FluidState fluidState) {
		return fluidBlockSupplier.get().getDefaultState().with(FluidBlock.LEVEL, getBlockStateLevel(fluidState));
	}

	@Override
	public int getLevel(FluidState fluidState) {
		return still ? 8 : fluidState.get(LEVEL);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Fluid, FluidState> stateBuilder) {
		super.appendProperties(stateBuilder);
		if (!still) {
			stateBuilder.add(LEVEL);
		}
	}

}
