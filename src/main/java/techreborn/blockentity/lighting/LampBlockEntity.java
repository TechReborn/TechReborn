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

package techreborn.blockentity.lighting;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import techreborn.blocks.lighting.LampBlock;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class LampBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop {

	private static final int capacity = 33;

	public LampBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.LAMP, pos, state);
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick(Level level, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(level, pos, state, blockEntity);
		if (!(level instanceof ServerLevel serverLevel)) {
			return;
		}
		Block b = state.getBlock();
		if (!(b instanceof LampBlock)) {
			return;
		}

		long cost = getEuPerTick(((LampBlock) b).getCost());
		if (getStored() > cost) {
			useEnergy(cost);
			if (!LampBlock.isActive(state)) {
				LampBlock.setActive(true, serverLevel, pos);
			}
		} else if (LampBlock.isActive(state)) {
			LampBlock.setActive(false, serverLevel, pos);
		}
	}

	@Override
	protected boolean canAcceptEnergy(@Nullable Direction side) {
		return side == null || getFacing().getOpposite() == Direction.values()[side.ordinal()];
	}

	@Override
	public boolean canProvideEnergy(@Nullable Direction side) {
		return false;
	}

	@Override
	public long getBaseMaxPower() {
		return capacity;
	}

	@Override
	public long getBaseMaxOutput() {
		return 0;
	}

	@Override
	public long getBaseMaxInput() {
		return 32;
	}

	// MachineBaseBlockEntity
	@Override
	public Direction getFacing(){
		if (level == null){
			return Direction.NORTH;
		}
		return LampBlock.getFacing(level.getBlockState(worldPosition));
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(final Player entityPlayer) {
		// I know it is weird. But world is nullable
		if (level == null) {
			return new ItemStack(TRContent.Machine.LAMP_INCANDESCENT.block);
		}
		return new ItemStack(level.getBlockState(worldPosition).getBlock());
	}
}
