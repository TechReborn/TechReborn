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

package techreborn.tiles.machine.tier1;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import reborncore.api.IToolDrop;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.config.ConfigRegistry;
import reborncore.common.util.WorldUtils;
import techreborn.TechReborn;
import techreborn.blocks.tier1.BlockPlayerDetector;
import techreborn.init.TRContent;
import techreborn.init.TRTileEntities;

@RebornRegister(TechReborn.MOD_ID)
public class TilePlayerDectector extends TilePowerAcceptor implements IToolDrop {

	@ConfigRegistry(config = "machines", category = "player_detector", key = "PlayerDetectorMaxInput", comment = "Player Detector Max Input (Value in EU)")
	public static int maxInput = 32;
	@ConfigRegistry(config = "machines", category = "player_detector", key = "PlayerDetectorMaxEnergy", comment = "Player Detector Max Energy (Value in EU)")
	public static int maxEnergy = 10000;
	@ConfigRegistry(config = "machines", category = "player_detector", key = "PlayerDetectorEUPerSecond", comment = "Player Detector Energy Consumption per second (Value in EU)")
	public static int euPerTick = 10;

	public String owenerUdid = "";
	boolean redstone = false;

	public TilePlayerDectector() {
		super(TRTileEntities.PLAYER_DETECTOR);
	}
	
	public boolean isProvidingPower() {
		return redstone;
	}

	// TilePowerAcceptor
	@Override
	public void tick() {
		super.tick();
		if (!world.isClient && world.getTime() % 20 == 0) {
			boolean lastRedstone = redstone;
			redstone = false;
			if (canUseEnergy(euPerTick)) {
				for(PlayerEntity player : world.getPlayers()){
					if (player.distanceTo(player) <= 256.0D) {
						String type = world.getBlockState(pos).get(BlockPlayerDetector.TYPE);
						if (type.equals("all")) {// ALL
							redstone = true;
						} else if (type.equals("others")) {// Others
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
				useEnergy(euPerTick);
			}
			if (lastRedstone != redstone) {
				WorldUtils.updateBlock(world, pos);
				world.updateNeighborsAlways(pos, world.getBlockState(pos).getBlock());
			}
		}
	}
	
	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(Direction direction) {
		return true;
	}

	@Override
	public boolean canProvideEnergy(Direction direction) {
		return false;
	}

	@Override
	public double getBaseMaxOutput() {
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return maxInput;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
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
