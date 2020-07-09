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

package techreborn.blockentity.storage.energy.lesu;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import reborncore.api.IToolDrop;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class LSUStorageBlockEntity extends MachineBaseBlockEntity
		implements IToolDrop {

	public LesuNetwork network;

	public LSUStorageBlockEntity() {
		super(TRBlockEntities.LSU_STORAGE);
	}

	public final void findAndJoinNetwork(World world, BlockPos pos) {
		network = new LesuNetwork();
		network.addElement(this);
		for (Direction direction : Direction.values()) {
			BlockEntity be = world.getBlockEntity(pos.offset(direction));
			if (!(be instanceof LSUStorageBlockEntity)) {
				continue;
			}
			LSUStorageBlockEntity lesuStorage = (LSUStorageBlockEntity) be;
			if (lesuStorage.network != null) {
				lesuStorage.network.merge(network);
			}
		}
	}

	public final void setNetwork(LesuNetwork n) {
		if (n == null) {
			return;
		}
		network = n;
		network.addElement(this);
	}

	public final void resetNetwork() {
		network = null;
	}

	public final void removeFromNetwork() {
		if (network == null) {
			return;
		}
		network.removeElement(this);
	}

	public final void rebuildNetwork() {
		removeFromNetwork();
		resetNetwork();
		findAndJoinNetwork(world, pos);
	}

	// TileMachineBase
	@Override
	public void tick() {
		super.tick();
		if (network == null) {
			findAndJoinNetwork(world, pos);
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
