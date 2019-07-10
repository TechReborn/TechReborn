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

import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import reborncore.api.IToolDrop;
import reborncore.api.power.EnumPowerTier;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.TilePowerAcceptor;
import reborncore.common.util.StringUtils;
import techreborn.blocks.generator.BlockSolarPanel;
import techreborn.init.TRContent;
import techreborn.init.TRContent.SolarPanels;
import techreborn.init.TRTileEntities;

import java.util.List;

public class TileSolarPanel extends TilePowerAcceptor implements IToolDrop {

	boolean canSeeSky = false;
	boolean lastState = false;
	SolarPanels panel;

	public TileSolarPanel() {
		super(TRTileEntities.SOLAR_PANEL);
	}

	public boolean isSunOut() {
		return canSeeSky && !world.isRaining() && !world.isThundering() && world.isDaylight();
	}

	// TilePowerAcceptor
	@Override
	public void tick() {
		super.tick();
		if (world.isClient) {
			return;
		}
		if (panel == TRContent.SolarPanels.CREATIVE) {
			checkOverfill = false;
			setEnergy(Integer.MAX_VALUE);
			return;
		}
		if (world.getTime() % 20 == 0) {
			canSeeSky = world.method_8626(pos.up());
			if (lastState != isSunOut()) {
				world.setBlockState(pos, world.getBlockState(pos).with(BlockSolarPanel.ACTIVE, isSunOut()));
				lastState = isSunOut();
			}
		}
		int powerToAdd;
		if (isSunOut()) {
			powerToAdd = panel.generationRateD;
		} else if (canSeeSky) {
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
	public boolean canAcceptEnergy(final Direction direction) {
		return false;
	}

	@Override
	public boolean canProvideEnergy(final Direction direction) {
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
	public void checkTier() {
		// Nope
	}

	// TODO: Translate
	@Override
	public void addInfo(List<Text> info, boolean isRealTile, boolean hasData) {
		info.add(new LiteralText(Formatting.GRAY + "Internal Energy Storage: " + Formatting.GOLD
				+ PowerSystem.getLocaliszedPowerFormatted((int) getMaxPower())));

		info.add(new LiteralText(Formatting.GRAY + "Generation Rate Day: " + Formatting.GOLD
				+ PowerSystem.getLocaliszedPowerFormatted(panel.generationRateD)));

		info.add(new LiteralText(Formatting.GRAY + "Generation Rate Night: " + Formatting.GOLD
				+ PowerSystem.getLocaliszedPowerFormatted(panel.generationRateN)));

		info.add(new LiteralText(Formatting.GRAY + "Tier: " + Formatting.GOLD
				+ StringUtils.toFirstCapitalAllLowercase(getTier().toString())));
	}
	
	@Override
	public void fromTag(CompoundTag tag) {
		if (world == null) {
			// We are in TileEntity.create method during chunk load.
			this.checkOverfill = false;
		}
		super.fromTag(tag);
	}

	// TileMachineBase
	@Override
	public void onLoad() {
		super.onLoad();
		Block panelBlock = world.getBlockState(pos).getBlock();
		if (panelBlock instanceof BlockSolarPanel) {
			BlockSolarPanel solarPanelBlock = (BlockSolarPanel) panelBlock;
			panel = solarPanelBlock.panelType;
		}
	}
	
	// IToolDrop
	@Override
	public ItemStack getToolDrop(final PlayerEntity playerIn) {
		return new ItemStack(getBlockType());
	}
}