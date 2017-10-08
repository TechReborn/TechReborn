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
import reborncore.api.power.EnumPowerTier;
import reborncore.common.powerSystem.TilePowerAcceptor;
import techreborn.blocks.generator.solarpanel.BlockSolarPanel;
import techreborn.blocks.generator.solarpanel.EnumPanelType;
import techreborn.init.ModBlocks;

public class TileSolarPanel extends TilePowerAcceptor implements IToolDrop {


	boolean canSeeSky = false;
	int powerToAdd;
	EnumPanelType panel = null;

	public TileSolarPanel() {
		super();
	}

	@Override
	public void onLoad() {
		this.panel = getPanelType();
	}

	@Override
	public void update() {
		super.update();
		if (this.world.isRemote) {
			return;
		}
		if (world.getTotalWorldTime() % 20 == 0) {
			canSeeSky = this.world.canBlockSeeSky(this.pos.up());
		}
		if (isSunOut()) {
			this.powerToAdd = panel.generationRateD;
		} else if (canSeeSky) {
			this.powerToAdd = panel.generationRateN;
		} else {
			this.powerToAdd = 0;
		}

		this.addEnergy(this.powerToAdd);
	}

	public boolean isSunOut() {
		return canSeeSky && !this.world.isRaining() && !this.world.isThundering() && this.world.isDaytime();
	}

	private EnumPanelType getPanelType() {
		if (world != null) {
			return world.getBlockState(pos).getValue(BlockSolarPanel.TYPE);
		}
		return EnumPanelType.Basic;
	}

	@Override
	public double getBaseMaxPower() {
		if (this.panel != null) {
			return panel.internalCapacity;
		}
		return 0;
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
		if (this.panel != null) {
			return this.panel.generationRateD;
		}
		return 0;
	}

	@Override
	public double getBaseMaxInput() {
		return 0;
	}

	@Override
	public EnumPowerTier getBaseTier() {
		if (this.panel != null){
			return this.panel.powerTier;
		}
		return EnumPowerTier.MEDIUM;
	}

	@Override
	public ItemStack getToolDrop(final EntityPlayer p0) {
		return new ItemStack(ModBlocks.SOLAR_PANEL);
	}

}