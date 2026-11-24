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

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public class RebornFluid extends FlowingFluid {

	private final FluidSettings fluidSettings;
	private final Supplier<RebornFluidBlock> fluidBlockSupplier;

	public RebornFluid(FluidSettings fluidSettings, Supplier<RebornFluidBlock> fluidBlockSupplier, ResourceKey<Item> bucketId) {
		super(Fluid.Properties.of().bucketItem(bucketId).soundEvent(SoundEvents.BUCKET_FILL).explosionResistance(100F));
		this.fluidSettings = fluidSettings;
		this.fluidBlockSupplier = fluidBlockSupplier;
	}

	public FluidSettings getFluidSettings() {
		return fluidSettings;
	}

	@Override
	protected boolean canConvertToSource(ServerLevel world) {
		return false;
	}

	@Override
	protected void beforeDestroyingBlock(LevelAccessor world, BlockPos pos, BlockState state) {
		// Should behave same as water
		BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
		Block.dropResources(state, world, pos, blockEntity);
	}

	@Override
	protected int getSlopeFindDistance(LevelReader world) {
		return 4;
	}

	@Override
	protected int getDropOff(LevelReader world) {
		return 1;
	}

	@Override
	protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockView, BlockPos blockPos, FluidState other, Direction direction) {
		// Same as vanilla water
		return direction == Direction.DOWN && !other.is(this);
	}

	@Override
	public int getTickDelay(LevelReader world) {
		return 10;
	}

	@Override
	protected BlockState createLegacyBlock(FluidState fluidState) {
		return fluidBlockSupplier.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(fluidState));
	}
}
