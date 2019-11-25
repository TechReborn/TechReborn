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

package techreborn.tiles.generator;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.api.IToolDrop;
import reborncore.common.blocks.RebornMachineBlock;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileLightningRod extends TilePowerAcceptor implements IToolDrop {

	@ConfigRegistry(config = "generators", category = "lightning_rod", key = "LightningRodMaxOutput", comment = "Lightning Rod Max Output (Value in EU)")
	public static int maxOutput = 2048;
	@ConfigRegistry(config = "generators", category = "lightning_rod", key = "LightningRodMaxEnergy", comment = "Lightning Rod Max Energy (Value in EU)")
	public static int maxEnergy = 100_000_000;
	@ConfigRegistry(config = "generators", category = "lightning_rod", key = "LightningRodChanceOfStrike", comment = "Chance of lightning striking a rod (Range: 0-70)")
	public static int chanceOfStrike = 24;
	@ConfigRegistry(config = "generators", category = "lightning_rod", key = "LightningRodBaseStrikeEnergy", comment = "Base amount of energy per strike (Value in EU)")
	public static int baseEnergyStrike = 262_144;

	private int onStatusHoldTicks = -1;

	public TileLightningRod() {
		super();
	}

	@Override
	public void update() {
		super.update();

		if (onStatusHoldTicks > 0)
			--onStatusHoldTicks;

		if (onStatusHoldTicks == 0 || getEnergy() <= 0) {
			setActive(false);
			onStatusHoldTicks = -1;
		}

		final float weatherStrength = world.getThunderStrength(1.0F);
		if (weatherStrength > 0.2F) {
			//lightStrikeChance = (MAX - (CHANCE * WEATHER_STRENGTH)
			final float lightStrikeChance = (100F - chanceOfStrike) * 20F;
			final float totalChance = lightStrikeChance * getLightningStrikeMultiplier() * (1.1F - weatherStrength);
			if (world.rand.nextInt((int) Math.floor(totalChance)) == 0) {
				if (!isValidIronFence(pos.up().getY())) {
					onStatusHoldTicks = 400;
					return;
				}
				final EntityLightningBolt lightningBolt = new EntityLightningBolt(world,
					pos.getX() + 0.5F,
					world.provider.getAverageGroundLevel(),
					pos.getZ() + 0.5F, false);
				world.addWeatherEffect(lightningBolt);
				world.spawnEntity(lightningBolt);
				addEnergy(baseEnergyStrike * (0.3F + weatherStrength));
				setActive(true);
				onStatusHoldTicks = 400;
			}
		}

	}

	public float getLightningStrikeMultiplier() {
		final float actualHeight = world.provider.getActualHeight();
		final float groundLevel = world.provider.getAverageGroundLevel();
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
		final Item itemBlock = Item.getItemFromBlock(this.world.getBlockState(new BlockPos(pos.getX(), y, pos.getZ())).getBlock());
		for (final ItemStack fence : OreDictionary.getOres("fenceIron")) {
			if (fence.getItem() == itemBlock)
				return true;
		}
		return false;
	}

	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(final EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canProvideEnergy(final EnumFacing direction) {
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
	public ItemStack getToolDrop(EntityPlayer playerIn) {
		return new ItemStack(ModBlocks.LIGHTNING_ROD);
	}
}
