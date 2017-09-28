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
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import reborncore.api.IToolDrop;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.blocks.generator.BlockSolarPanel;
import techreborn.init.ModBlocks;
import techreborn.lib.ModInfo;

import java.util.List;

/**
 * Created by modmuss50 on 25/02/2016.
 */

@RebornRegistry(modID = ModInfo.MOD_ID)
public class TileSolarPanel extends TilePowerAcceptor implements IToolDrop {

	@ConfigRegistry(config = "machines", category = "solar_panel", key = "SolarPanelMaxOutput", comment = "Solar Panel Max Output (Value in EU)")
	public static int maxOutput = 32;
	@ConfigRegistry(config = "machines", category = "solar_panel", key = "SolarPanelMaxEnergy", comment = "Solar Panel Max Energy (Value in EU)")
	public static int maxEnergy = 1000;
	@ConfigRegistry(config = "machines", category = "solar_panel", key = "SolarPanelEnergyPerTick", comment = "Solar Panel Energy Per Tick (Value in EU)")
	public static int energyPerTick = 1;

	boolean shouldMakePower = false;
	boolean lastTickSate = false;

	int powerToAdd;

	public TileSolarPanel() {
		super();
	}

	@Override
	public void update() {
		super.update();
		if (!this.world.isRemote) {
			if (this.world.getTotalWorldTime() % 60 == 0) {
				this.shouldMakePower = this.isSunOut();

			}
			if (this.shouldMakePower) {
				this.powerToAdd = energyPerTick;
				this.addEnergy(this.powerToAdd);
			} else {
				this.powerToAdd = 0;
			}

			this.world.setBlockState(this.getPos(),
				this.world.getBlockState(this.getPos()).withProperty(BlockSolarPanel.ACTIVE, this.isSunOut()));
		}
	}

	@Override
	public void addInfo(final List<String> info, final boolean isRealTile) {
		super.addInfo(info, isRealTile);
		if (isRealTile) {
			// FIXME: 25/02/2016
			// info.add(TextFormatting.LIGHT_PURPLE + "Power gen/tick " +
			// TextFormatting.GREEN + PowerSystem.getLocaliszedPower(
			// powerToAdd)) ;
		}
	}

	public boolean isSunOut() {
		return this.world.canBlockSeeSky(this.pos.up()) && !this.world.isRaining() && !this.world.isThundering()
			&& this.world.isDaytime();
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
		return new ItemStack(ModBlocks.SOLAR_PANEL);
	}
}
