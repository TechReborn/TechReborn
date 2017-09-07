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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import reborncore.api.IToolDrop;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

/**
 * Created by modmuss50 on 25/02/2016.
 */

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileWaterMill extends TilePowerAcceptor implements IToolDrop {

	@ConfigRegistry(config = "machines", category = "water_mill", key = "WaterMillMaxOutput", comment = "Water Mill Max Output (Value in EU)")
	public static int maxOutput = 32;
	@ConfigRegistry(config = "machines", category = "water_mill", key = "WaterMillMaxEnergy", comment = "Water Mill Max Energy (Value in EU)")
	public static int maxEnergy = 1000;
	@ConfigRegistry(config = "machines", category = "water_mill", key = "WaterMillEnergyPerTick", comment = "Water Mill Energy Multiplier")
	public static double energyMultiplier = 0.1;

	int waterblocks = 0;

	public TileWaterMill() {
		super();
	}

	@Override
	public void update() {
		super.update();
		if (this.world.getTotalWorldTime() % 20 == 0) {
			this.checkForWater();
		}
		if (this.waterblocks > 0) {
			this.addEnergy(this.waterblocks * energyMultiplier);
		}
	}

	public void checkForWater() {
		this.waterblocks = 0;
		for (final EnumFacing facing : EnumFacing.HORIZONTALS) {
			if (this.world.getBlockState(this.getPos().offset(facing)).getBlock() == Blocks.WATER) {
				this.waterblocks++;
			}
		}
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
	public ItemStack getToolDrop(final EntityPlayer p0) {
		return new ItemStack(ModBlocks.WATER_MILL);
	}
}
