/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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
import reborncore.common.IWrenchable;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileLightningRod extends TilePowerAcceptor implements IWrenchable {

	@ConfigRegistry(config = "machines", category = "lightning_rod", key = "LightningRodMaxOutput", comment = "Lightning Rod Max Output (Value in EU)")
	public static int maxOutput = 2048;
	@ConfigRegistry(config = "machines", category = "lightning_rod", key = "LightningRodMaxEnergy", comment = "Lightning Rod Max Energy (Value in EU)")
	public static int maxEnergy = 1000000;
	@ConfigRegistry(config = "machines", category = "lightning_rod", key = "LightningRodChanceOfStrike", comment = "Chance of lightning striking a rod (Range: 0-70)")
	public static int chanceOfStrike = 24;
	@ConfigRegistry(config = "machines", category = "lightning_rod", key = "LightningRodBaseStrikeEnergy", comment = "Base amount of energy per strike (Value in EU)")
	public static int baseEnergyStrike = 32768;

	private int onStatusHoldTicks = -1;

	public TileLightningRod() {
		super(2);
	}

	@Override
	public void update() {
		super.update();

		if (this.onStatusHoldTicks > 0)
			--this.onStatusHoldTicks;

		if (this.onStatusHoldTicks == 0 || this.getEnergy() <= 0) {
			if (this.getBlockType() instanceof BlockMachineBase)
				((BlockMachineBase) this.getBlockType()).setActive(false, this.world, this.pos);
			this.onStatusHoldTicks = -1;
		}

		final float weatherStrength = this.world.getThunderStrength(1.0F);
		if (weatherStrength > 0.2F) {
			//lightStrikeChance = (MAX - (CHANCE * WEATHER_STRENGTH)
			final float lightStrikeChance = (100F - chanceOfStrike) * 20F;
			final float totalChance = lightStrikeChance * this.getLightningStrikeMultiplier() * (1.1F - weatherStrength);
			if (this.world.rand.nextInt((int) Math.floor(totalChance)) == 0) {
				final EntityLightningBolt lightningBolt = new EntityLightningBolt(this.world,
					this.pos.getX() + 0.5F,
					this.world.provider.getAverageGroundLevel(),
					this.pos.getZ() + 0.5F, false);
				this.world.addWeatherEffect(lightningBolt);
				this.world.spawnEntity(lightningBolt);
				this.addEnergy(baseEnergyStrike * (0.3F + weatherStrength));
				((BlockMachineBase) this.getBlockType()).setActive(true, this.world, this.pos);
				this.onStatusHoldTicks = 400;
			}
		}

	}

	public float getLightningStrikeMultiplier() {
		final float actualHeight = this.world.provider.getActualHeight();
		final float groundLevel = this.world.provider.getAverageGroundLevel();
		for (int i = this.pos.getY() + 1; i < actualHeight; i++) {
			if (!this.isValidIronFence(i)) {
				if (groundLevel >= i)
					return 4.3F;
				final float max = actualHeight - groundLevel;
				final float got = i - groundLevel;
				return 1.2F - got / max;
			}
		}
		return 4F;
	}

	public boolean isValidIronFence(final int y) {
		final Item itemBlock = Item.getItemFromBlock(this.world.getBlockState(new BlockPos(this.pos.getX(), y, this.pos.getZ())).getBlock());
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
	public boolean wrenchCanSetFacing(final EntityPlayer entityPlayer, final EnumFacing side) {
		return false;
	}

	@Override
	public EnumFacing getFacing() {
		return this.getFacingEnum();
	}

	@Override
	public boolean wrenchCanRemove(final EntityPlayer entityPlayer) {
		return entityPlayer.isSneaking();
	}

	@Override
	public float getWrenchDropRate() {
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(final EntityPlayer p0) {
		return new ItemStack(ModBlocks.LIGHTNING_ROD);
	}
}
