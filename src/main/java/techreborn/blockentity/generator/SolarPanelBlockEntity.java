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

package techreborn.blockentity.generator;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import reborncore.api.IToolDrop;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.StringUtils;
import team.reborn.energy.EnergyTier;
import techreborn.blocks.generator.BlockSolarPanel;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.init.TRContent.SolarPanels;

import java.util.List;

public class SolarPanelBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop {

	boolean canSeeSky = false;
	boolean lastState = false;
	SolarPanels panel;

	public SolarPanelBlockEntity() {
		super(TRBlockEntities.SOLAR_PANEL);
	}

	public boolean isSunOut() {
		return canSeeSky && !world.isRaining() && !world.isThundering() && world.isDaylight();
	}

	private void updatePanel() {
		if (world == null) {
			return;
		}
		Block panelBlock = world.getBlockState(pos).getBlock();
		if (panelBlock instanceof BlockSolarPanel) {
			BlockSolarPanel solarPanelBlock = (BlockSolarPanel) panelBlock;
			panel = solarPanelBlock.panelType;
		}
	}

	// PowerAcceptorBlockEntity
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
				world.setBlockState(pos, world.getBlockState(pos).with(BlockMachineBase.ACTIVE, isSunOut()));
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
	public EnergyTier getTier() {
		return panel.powerTier;
	}

	@Override
	public void checkTier() {
		// Nope
	}

	@Override
	public void addInfo(List<Text> info, boolean isReal, boolean hasData) {
		info.add(new LiteralText(Formatting.GRAY + StringUtils.t("reborncore.tooltip.energy.maxEnergy") + ": "
				+ Formatting.GOLD + PowerSystem.getLocaliszedPowerFormatted(getMaxPower())));

		info.add(new LiteralText(Formatting.GRAY + StringUtils.t("techreborn.tooltip.generationRate.day") + ": "
				+ Formatting.GOLD + PowerSystem.getLocaliszedPowerFormatted(panel.generationRateD)));

		info.add(new LiteralText(Formatting.GRAY + StringUtils.t("techreborn.tooltip.generationRate.day") + ": "
				+ Formatting.GOLD + PowerSystem.getLocaliszedPowerFormatted(panel.generationRateN)));

		info.add(new LiteralText(Formatting.GRAY + StringUtils.t("reborncore.tooltip.energy.tier") + ": "
				+ Formatting.GOLD + StringUtils.toFirstCapitalAllLowercase(getTier().toString())));
	}

	@Override
	public void fromTag(CompoundTag tag) {
		if (world == null) {
			// We are in BlockEntity.create method during chunk load.
			this.checkOverfill = false;
		}
		updatePanel();
		super.fromTag(tag);
	}

	// MachineBaseBlockEntity
	@Override
	public void onLoad() {
		super.onLoad();
		updatePanel();
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(final PlayerEntity playerIn) {
		return new ItemStack(getBlockType());
	}
}