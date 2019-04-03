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
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import reborncore.api.IToolDrop;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.powerSystem.TilePowerAcceptor;
import techreborn.init.ModBlocks;

import java.util.List;

/**
 * Created by modmuss50 on 25/02/2016.
 */
public class TileCreativeSolarPanel extends TilePowerAcceptor implements IToolDrop {

	boolean shouldMakePower = false;
	boolean lastTickSate = false;

	int powerToAdd;

	@Override
	public void update() {
		if(world.isRemote){
			return;
		}
		for (EnumFacing facing : EnumFacing.VALUES){
			TileEntity tileEntity = world.getTileEntity(getPos().offset(facing));
			if(tileEntity != null ){
				IEnergyStorage energyStorage = tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
				if(energyStorage != null){
					for (int i = 0; i < 10; i++) {
						energyStorage.receiveEnergy(Integer.MAX_VALUE, false);
					}
				}
			}
		}
		setEnergy(getMaxPower());
	}

	@Override
	public void addInfo(final List<String> info, final boolean isRealTile) {
		super.addInfo(info, isRealTile);
	}

	@Override
	public double getBaseMaxPower() {
		return 1_000_000;
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
		return 16_192;
	}

	@Override
	public double getBaseMaxInput() {
		return 0;
	}

	@Override
	public EnumPowerTier getBaseTier() {
		return EnumPowerTier.INFINITE;
	}

	@Override
	public ItemStack getToolDrop(final EntityPlayer p0) {
		return new ItemStack(ModBlocks.SOLAR_PANEL);
	}

	@Override
	public boolean handleTierWithPower() {
		return false;
	}
}
