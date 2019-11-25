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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import reborncore.api.IToolDrop;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.blocks.generator.BlockWindMill;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

/**
 * Created by modmuss50 on 25/02/2016.
 */

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileWaterMill extends TilePowerAcceptor implements IToolDrop {

	@ConfigRegistry(config = "generators", category = "water_mill", key = "WaterMillMaxOutput", comment = "Water Mill Max Output (Value in EU)")
	public static int maxOutput = 32;
	@ConfigRegistry(config = "generators", category = "water_mill", key = "WaterMillMaxEnergy", comment = "Water Mill Max Energy (Value in EU)")
	public static int maxEnergy = 1000;
	@ConfigRegistry(config = "generators", category = "water_mill", key = "WaterMillEnergyPerTick", comment = "Water Mill Energy Multiplier")
	public static double energyMultiplier = 0.1;

	int waterblocks = 0;

	public TileWaterMill() {
		super();
	}

	@Override
	public void update() {
		super.update();
		if (world.getTotalWorldTime() % 20 == 0) {
			checkForWater();
		}
		if (waterblocks > 0) {
			addEnergy(waterblocks * energyMultiplier);
			world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockWindMill.activeProperty, true));
		}
		else {
			world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockWindMill.activeProperty, false));
		}
	}

	public void checkForWater() {
		waterblocks = 0;
		for (EnumFacing facing : EnumFacing.HORIZONTALS) {
			if (world.getBlockState(pos.offset(facing)).getBlock() == Blocks.WATER) {
				waterblocks++;
			}
		}
	}

	@Override
	public double getBaseMaxPower() {
		return maxEnergy;
	}

	@Override
	public boolean canAcceptEnergy(EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canProvideEnergy(EnumFacing direction) {
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
		return new ItemStack(ModBlocks.WATER_MILL);
	}
}
