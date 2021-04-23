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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import reborncore.api.IToolDrop;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import team.reborn.energy.EnergySide;
import techreborn.blocks.lighting.LampBlock;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class LampBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop {

	private static final int capacity = 33;

	public LampBlockEntity() {
		super(TRBlockEntities.LAMP);
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick() {
		super.tick();
		if (world == null || world.isClient) {
			return;
		}
		BlockState state = world.getBlockState(pos);
		Block b = state.getBlock();
		if (!(b instanceof LampBlock)) {
			return;
		}

		double cost = getEuPerTick(((LampBlock) b).getCost());
		if (getStored(EnergySide.UNKNOWN) > cost) {
			useEnergy(cost);
			if (!LampBlock.isActive(state)) {
				LampBlock.setActive(true, world, pos);
			}
		} else if (LampBlock.isActive(state)) {
			LampBlock.setActive(false, world, pos);
		}
	}

	@Override
	protected boolean canAcceptEnergy(EnergySide side) {
		return side == EnergySide.UNKNOWN || getFacing().getOpposite() == Direction.values()[side.ordinal()];
	}

	@Override
	public boolean canProvideEnergy(EnergySide side) {
		return false;
	}

	@Override
	public double getBaseMaxPower() {
		return capacity;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return 32;
	}

	//MachineBaseBlockEntity
	@Override
	public Direction getFacing(){
		if (world == null){
			return Direction.NORTH;
		}
		return LampBlock.getFacing(world.getBlockState(pos));
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(final PlayerEntity entityPlayer) {
		// I know it is weird. But world is nullable
		if (world == null) {
			return new ItemStack(TRContent.Machine.LAMP_INCANDESCENT.block);
		}
		return new ItemStack(world.getBlockState(pos).getBlock());
	}
}
