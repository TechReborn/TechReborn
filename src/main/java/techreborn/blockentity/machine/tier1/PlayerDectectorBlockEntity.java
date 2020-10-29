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

package techreborn.blockentity.machine.tier1;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import reborncore.api.IToolDrop;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.util.WorldUtils;
import team.reborn.energy.EnergySide;
import techreborn.blocks.machine.tier1.PlayerDetectorBlock;
import techreborn.blocks.machine.tier1.PlayerDetectorBlock.PlayerDetectorType;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;

public class PlayerDectectorBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop {


	public String owenerUdid = "";
	boolean redstone = false;

	public PlayerDectectorBlockEntity() {
		super(TRBlockEntities.PLAYER_DETECTOR);
	}

	public boolean isProvidingPower() {
		return redstone;
	}

	// PowerAcceptorBlockEntity
	@Override
	public void tick() {
		super.tick();

		if (world == null) {
			return;
		}

		if (world.isClient) {
			return;
		}

		if (world.getTime() % 20 != 0) {
			return;
		}

		boolean lastRedstone = redstone;
		redstone = false;
		if (getStored(EnergySide.UNKNOWN) > TechRebornConfig.playerDetectorEuPerTick) {
			for (PlayerEntity player : world.getPlayers()) {
				if (player.distanceTo(player) <= 256.0D) {
					PlayerDetectorType type = world.getBlockState(pos).get(PlayerDetectorBlock.TYPE);
					if (type == PlayerDetectorType.ALL) {// ALL
						redstone = true;
					} else if (type == PlayerDetectorType.OTHERS) {// Others
						if (!owenerUdid.isEmpty() && !owenerUdid.equals(player.getUuid().toString())) {
							redstone = true;
						}
					} else {// You
						if (!owenerUdid.isEmpty() && owenerUdid.equals(player.getUuid().toString())) {
							redstone = true;
						}
					}
				}
			}
			useEnergy(TechRebornConfig.playerDetectorEuPerTick);
		}
		if (lastRedstone != redstone) {
			WorldUtils.updateBlock(world, pos);
			world.updateNeighborsAlways(pos, world.getBlockState(pos).getBlock());
		}
	}

	@Override
	public double getBaseMaxPower() {
		return TechRebornConfig.playerDetectorMaxEnergy;
	}

	@Override
	public boolean canProvideEnergy(EnergySide side) {
		return false;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return TechRebornConfig.playerDetectorMaxInput;
	}

	@Override
	public void fromTag(BlockState blockState, CompoundTag tag) {
		super.fromTag(blockState, tag);
		owenerUdid = tag.getString("ownerID");
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putString("ownerID", owenerUdid);
		return tag;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(PlayerEntity p0) {
		return TRContent.Machine.PLAYER_DETECTOR.getStack();
	}
}
