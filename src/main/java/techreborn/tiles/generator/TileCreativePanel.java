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
import net.minecraftforge.fml.common.Optional;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.IWrenchable;
import reborncore.common.powerSystem.TilePowerAcceptor;
import techreborn.init.ModBlocks;

import java.util.List;

/**
 * Created by modmuss50 on 25/02/2016.
 */
public class TileCreativePanel extends TilePowerAcceptor implements IWrenchable {

	boolean shouldMakePower = false;
	boolean lastTickSate = false;

	int powerToAdd;

	public TileCreativePanel() {
		super(1);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		this.setEnergy(getMaxPower());
	}

	@Override
	public void addInfo(final List<String> info, final boolean isRealTile) {
		super.addInfo(info, isRealTile);
	}

	@Override
	public double getBaseMaxPower() {
		return 1000000;
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
		return 16192;
	}

	@Override
	public double getBaseMaxInput() {
		return 0;
	}

	@Override
	public EnumPowerTier getBaseTier() {
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

	//Done to prevent ic2 from exploding the adjacent blocks
	@Override
	@Optional.Method(modid = "ic2")
	public int getSourceTier() {
		return 0;
	}
}
