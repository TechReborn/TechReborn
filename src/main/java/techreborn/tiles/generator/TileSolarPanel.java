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

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.text.TextFormatting;
import reborncore.api.IToolDrop;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.StringUtils;
import techreborn.Core;
import techreborn.blocks.generator.solarpanel.BlockSolarPanel;
import techreborn.blocks.generator.solarpanel.EnumPanelType;
import techreborn.init.ModBlocks;

import java.util.List;

public class TileSolarPanel extends TilePowerAcceptor implements IToolDrop {

	boolean canSeeSky = false;
	boolean lastSate = false;
	int powerToAdd;
	EnumPanelType panel;

	//This is left here to allow the world to create a new instance of it when loading, do not remove this.
	public TileSolarPanel() {
		super();
	}

	public TileSolarPanel(EnumPanelType panel) {
		this.panel = panel;
	}
	
	private boolean isSunnyDay() {
		return canSeeSky && !world.isRaining() && !world.isThundering() && world.isDaytime();
	}

	private boolean isRainyDay() {
		return canSeeSky && world.isRaining() && !world.isThundering() && world.isDaytime();
	}

	private boolean isStormyDay() {
		return canSeeSky && world.isRaining() && world.isThundering() && world.isDaytime();
	}

	private boolean isClearNight() {
		return canSeeSky && !world.isRaining() && !world.isThundering() && !world.isDaytime();
	}

	// TilePowerAcceptor
	@Override
	public void update() {
		super.update();
		if (world.isRemote) return;

		if (world.getTotalWorldTime() % 20 == 0) {
			canSeeSky = world.canBlockSeeSky(pos.up());
			if (lastSate != isSunnyDay()){
				world.setBlockState(pos,
					world.getBlockState(pos).withProperty(BlockSolarPanel.ACTIVE, isSunnyDay()));
				lastSate = isSunnyDay();
			}

		}

		if (isSunnyDay()) {
			powerToAdd = panel.generationRateD;
		} else if (isRainyDay()) {
			powerToAdd = panel.generationRateN;
		} else if (isStormyDay()) {
			powerToAdd = 0;
		} else if (isClearNight()) {
			powerToAdd = panel.generationRateN;
		} else {
			powerToAdd = 0;
		}

		addEnergy(powerToAdd);
	}

	@Override
	public double getBaseMaxPower() {
		return panel.internalCapacity;
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
		return panel.generationRateD;
	}

	@Override
	public double getBaseMaxInput() {
		return 0;
	}

	@Override
	public EnumPowerTier getTier() {
		return panel.powerTier;
	}

	@Override
	public EnumPowerTier getBaseTier() {
		return getTier();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		if(tag.hasKey("panelType")){
			panel = EnumPanelType.values()[tag.getInteger("panelType")];
		} else {
			Core.logHelper.warn("A solar panel has failed to load from NBT, it will not work correctly. Please break and replace it to fix the issue. BlockPos:" + pos.toString());
			panel = EnumPanelType.Basic;
		}
		super.readFromNBT(tag);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag.setInteger("panelType", panel.ordinal());
		return super.writeToNBT(tag);
	}

	@Override
	public void checkTeir() {
		//Nope
	}

	@Override
	public void addInfo(List<String> info, boolean isRealTile) {
		info.add(TextFormatting.GRAY + "Internal Energy Storage: " + TextFormatting.GOLD
			+ PowerSystem.getLocaliszedPowerFormatted((int) getMaxPower()));

		info.add(TextFormatting.GRAY + "Generation Rate Day: " + TextFormatting.GOLD
				+ PowerSystem.getLocaliszedPowerFormatted(panel.generationRateD));

		info.add(TextFormatting.GRAY + "Generation Rate Night: " + TextFormatting.GOLD
			+ PowerSystem.getLocaliszedPowerFormatted(panel.generationRateN));

		info.add(TextFormatting.GRAY + "Generation Rate Rain: " + TextFormatting.GOLD
			+ PowerSystem.getLocaliszedPowerFormatted(panel.generationRateN));

		info.add(TextFormatting.GRAY + "Generation Rate Storm: " + TextFormatting.GOLD
			+ PowerSystem.getLocaliszedPowerFormatted(0));

		info.add(TextFormatting.GRAY + "Tier: " + TextFormatting.GOLD + StringUtils.toFirstCapitalAllLowercase(getTier().toString()));
	}
	
	// RebornMachineTile
    public IBlockState getBlockState() {
        return world.getBlockState(pos).getBlock().getDefaultState()
                .withProperty(BlockSolarPanel.ACTIVE, isActive())
                .withProperty(BlockSolarPanel.TYPE, panel);
    }

	@Override
	public void rotate(Rotation rotationIn) {
		return;
	}
	
	// IToolDrop
	@Override
	public ItemStack getToolDrop(final EntityPlayer playerIn) {
		return new ItemStack(ModBlocks.SOLAR_PANEL, 1, panel.ordinal());
	}
}