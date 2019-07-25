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

package techreborn.blockentity.generator;

import net.minecraft.block.Block;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import reborncore.api.IToolDrop;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.config.ConfigRegistry;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import techreborn.init.TRBlockEntities;

@RebornRegister(TechReborn.MOD_ID)
public class LightningRodBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop {

	@ConfigRegistry(config = "generators", category = "lightning_rod", key = "LightningRodMaxOutput", comment = "Lightning Rod Max Output (Value in EU)")
	public static int maxOutput = 2048;
	@ConfigRegistry(config = "generators", category = "lightning_rod", key = "LightningRodMaxEnergy", comment = "Lightning Rod Max Energy (Value in EU)")
	public static int maxEnergy = 100_000_000;
	@ConfigRegistry(config = "generators", category = "lightning_rod", key = "LightningRodChanceOfStrike", comment = "Chance of lightning striking a rod (Range: 0-70)")
	public static int chanceOfStrike = 24;
	@ConfigRegistry(config = "generators", category = "lightning_rod", key = "LightningRodBaseStrikeEnergy", comment = "Base amount of energy per strike (Value in EU)")
	public static int baseEnergyStrike = 262_144;

	private int onStatusHoldTicks = -1;

	public LightningRodBlockEntity() {
		super(TRBlockEntities.LIGHTNING_ROD);
	}

	@Override
	public void tick() {
		super.tick();

		if (onStatusHoldTicks > 0) { --onStatusHoldTicks; }

		Block BEBlock = getCachedState().getBlock();
		if (! (BEBlock instanceof BlockMachineBase)) {
			return;
		}
		
		BlockMachineBase machineBaseBlock = (BlockMachineBase) BEBlock;
		
		if (onStatusHoldTicks == 0 || getEnergy() <= 0) {
			machineBaseBlock.setActive(false, world, pos);
			onStatusHoldTicks = -1;
		}

		final float weatherStrength = world.getThunderGradient(1.0F);
		if (weatherStrength > 0.2F) {
			//lightStrikeChance = (MAX - (CHANCE * WEATHER_STRENGTH)
			final float lightStrikeChance = (100F - chanceOfStrike) * 20F;
			final float totalChance = lightStrikeChance * getLightningStrikeMultiplier() * (1.1F - weatherStrength);
			if (world.random.nextInt((int) Math.floor(totalChance)) == 0) {
				if (!isValidIronFence(pos.up().getY())) {
					onStatusHoldTicks = 400;
					return;
				}
				final LightningEntity lightningBolt = new LightningEntity(world,
					pos.getX() + 0.5F, world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, getPos()).getY(),
					pos.getZ() + 0.5F, false);
				
				if (!world.isClient) {
					((ServerWorld) world).addLightning(lightningBolt);
				}
				addEnergy(baseEnergyStrike * (0.3F + weatherStrength));
				machineBaseBlock.setActive(true, world, pos);
				onStatusHoldTicks = 400;
			}
		}

	}

	public float getLightningStrikeMultiplier() {
		final float actualHeight = 256;
		final float groundLevel = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, getPos()).getY();
		for (int i = pos.getY() + 1; i < actualHeight; i++) {
			if (!isValidIronFence(i)) {
				if (groundLevel >= i)
					return 4.3F;
				final float max = actualHeight - groundLevel;
				final float got = i - groundLevel;
				return 1.2F - got / max;
			}
		}
		return 4F;
	}

	public boolean isValidIronFence(int y) {
		Block block = this.world.getBlockState(new BlockPos(pos.getX(), y, pos.getZ())).getBlock();
		if(block == TRContent.REFINED_IRON_FENCE){
			return true;
		}
		return false;
	}

	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(final Direction direction) {
		return false;
	}

	@Override
	public boolean canProvideEnergy(final Direction direction) {
		return true;
	}

	@Override
	public double getBaseMaxOutput() {
		return maxOutput;
	}

	@Override
	public double getBaseMaxInput() {
		return 0;
	}

	@Override
	public ItemStack getToolDrop(PlayerEntity playerIn) {
		return TRContent.Machine.LIGHTNING_ROD.getStack();
	}
}
