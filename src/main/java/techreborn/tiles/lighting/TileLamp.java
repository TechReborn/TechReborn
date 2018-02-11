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

package techreborn.tiles.lighting;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import reborncore.common.powerSystem.TilePowerAcceptor;
import techreborn.blocks.lighting.BlockLamp;

public class TileLamp extends TilePowerAcceptor
	implements ITickable {

	private static int capacity = 33;

	@Override
	public void update() {
		super.update();
		if (world != null && !world.isRemote) {
			IBlockState state = world.getBlockState(pos);
			Block b = state.getBlock();
			if (b instanceof BlockLamp) {
				double cost = getEuPerTick(((BlockLamp)b).getCost());
				if (this.getEnergy() > cost) {
					useEnergy(getEuPerTick(cost));
					if (!BlockLamp.isActive(state))
						BlockLamp.setActive(true, world, pos);
				} else if (BlockLamp.isActive(state)) {
					BlockLamp.setActive(false, world, pos);
				}
			}
		}
	}

	@Override
	public double getBaseMaxPower() {
		return capacity;
	}

	@Override
	public boolean canAcceptEnergy(final EnumFacing direction) {
		EnumFacing me = BlockLamp.getFacing(world.getBlockState(pos)).getOpposite();
		return direction == me;
	}

	@Override
	public boolean canProvideEnergy(final EnumFacing direction) {
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


}
