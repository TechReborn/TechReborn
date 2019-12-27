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

package techreborn.blockentity.storage.energy.lesu;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import reborncore.api.IToolDrop;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import techreborn.init.TRContent;
import techreborn.init.TRBlockEntities;

public class LSUStorageBlockEntity extends MachineBaseBlockEntity
	implements IToolDrop {

	public LesuNetwork network;

	public LSUStorageBlockEntity() {
		super(TRBlockEntities.LSU_STORAGE);
	}

	public final void findAndJoinNetwork(World world, int x, int y, int z) {
		network = new LesuNetwork();
		network.addElement(this);
		for (Direction direction : Direction.values()) {
			if (world.getBlockEntity(new BlockPos(x + direction.getOffsetX(), y + direction.getOffsetY(),
					z + direction.getOffsetZ())) instanceof LSUStorageBlockEntity) {
				LSUStorageBlockEntity lesu = (LSUStorageBlockEntity) world.getBlockEntity(new BlockPos(x + direction.getOffsetX(),
				                                                                                       y + direction.getOffsetY(), z + direction.getOffsetZ()));
				if (lesu.network != null) {
					lesu.network.merge(network);
				}
			}
		}
	}

	public final void setNetwork(LesuNetwork n) {
		if (n == null) {
		} else {
			network = n;
			network.addElement(this);
		}
	}

	public final void resetNetwork() {
		network = null;
	}

	public final void removeFromNetwork() {
		if (network == null) {
		} else
			network.removeElement(this);
	}

	public final void rebuildNetwork() {
		removeFromNetwork();
		resetNetwork();
		findAndJoinNetwork(world, pos.getX(), pos.getY(), pos.getZ());
	}

	// TileMachineBase
	@Override
	public void tick() {
		super.tick();
		if (network == null) {
			findAndJoinNetwork(world, pos.getX(), pos.getY(), pos.getZ());
		} else {
			if (network.master != null
					&& network.master.getWorld().getBlockEntity(new BlockPos(network.master.getPos().getX(),
							network.master.getPos().getY(), network.master.getPos().getZ())) != network.master) {
				network.master = null;
			}
		}
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity entityPlayer) {
		return TRContent.Machine.LSU_STORAGE.getStack();
	}
}
