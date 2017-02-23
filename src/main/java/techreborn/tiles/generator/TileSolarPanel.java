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

import reborncore.api.power.EnumPowerTier;
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.TilePowerAcceptor;

import techreborn.blocks.generator.BlockSolarPanel;
import techreborn.init.ModBlocks;

import java.util.List;

/**
 * Created by modmuss50 on 25/02/2016.
 */
public class TileSolarPanel extends TilePowerAcceptor implements IWrenchable {

	boolean shouldMakePower = false;
	boolean lastTickSate = false;

	int powerToAdd;

	public TileSolarPanel() {
		super(1);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (!this.world.isRemote) {
			if (this.world.getTotalWorldTime() % 60 == 0) {
				this.shouldMakePower = this.isSunOut();

			}
			if (this.shouldMakePower) {
				this.powerToAdd = 10;
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
	public double getMaxPower() {
		return 1000;
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
	public double getMaxOutput() {
		return 32;
	}

	@Override
	public double getMaxInput() {
		return 0;
	}

	@Override
	public EnumPowerTier getTier() {
		return EnumPowerTier.LOW;
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
		return new ItemStack(ModBlocks.SOLAR_PANEL);
	}
}
