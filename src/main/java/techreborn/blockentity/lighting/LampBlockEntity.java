/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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
import techreborn.blocks.lighting.BlockLamp;
import techreborn.init.TRBlockEntities;

public class LampBlockEntity extends PowerAcceptorBlockEntity
	implements IToolDrop {

	private static int capacity = 33;

	public LampBlockEntity() {
		super(TRBlockEntities.LAMP);
	}

	// TilePowerAcceptor
	@Override
	public void tick() {
		super.tick();
		if (world == null || world.isClient) {
			return;
		}
		BlockState state = world.getBlockState(pos);
		Block b = state.getBlock();
		if (b instanceof BlockLamp) {
			double cost = getEuPerTick(((BlockLamp) b).getCost());
			if (getEnergy() > cost) {
				useEnergy(getEuPerTick(cost));
				if (!BlockLamp.isActive(state))
					BlockLamp.setActive(true, world, pos);
			} else if (BlockLamp.isActive(state)) {
				BlockLamp.setActive(false, world, pos);
			}
		}	
	}

	@Override
	public double getBaseMaxPower() {
		return capacity;
	}

	@Override
	public boolean canAcceptEnergy(final Direction direction) {
		if (world == null) {
			// Blame tooltip for this
			return true;
		}
		Direction me = BlockLamp.getFacing(world.getBlockState(pos)).getOpposite();
		return direction == me;
	}

	@Override
	public boolean canProvideEnergy(final Direction direction) {
		return false;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return 32;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(final PlayerEntity entityPlayer) {
		return new ItemStack(world.getBlockState(pos).getBlock());
	}


}
