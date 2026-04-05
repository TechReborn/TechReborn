/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

import org.jspecify.annotations.Nullable;
import reborncore.api.IToolDrop;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.StringUtils;
import techreborn.blocks.generator.BlockSolarPanel;
import techreborn.init.TRBlockEntities;
import techreborn.init.TRContent;
import techreborn.init.TRContent.SolarPanels;

import java.util.List;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;

public class SolarPanelBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop, BuiltScreenHandlerProvider {

	private boolean generating = false;

	// Range of panel between day/night production; we calculate this only when panel is updated
	private int dayNightRange = 0;

	private SolarPanels panel;

	public SolarPanelBlockEntity(BlockPos pos, BlockState state) {
		super(TRBlockEntities.SOLAR_PANEL, pos, state);
	}

	public SolarPanelBlockEntity(BlockPos pos, BlockState state, SolarPanels panel) {
		super(TRBlockEntities.SOLAR_PANEL, pos, state);
		this.panel = panel;
	}

	private void updatePanel() {
		Objects.requireNonNull(level, "World may not be null.");

		Block panelBlock = level.getBlockState(worldPosition).getBlock();
		if (panelBlock instanceof BlockSolarPanel solarPanelBlock) {
			panel = solarPanelBlock.panelType;
		}

		dayNightRange = getPanel().generationRateD - getPanel().generationRateN;
	}

	// Setters/getters that provide boolean interface to underlying generating int; something about
	// screen auto-sync REQUIRES an integer value (booleans don't get transmitted?!), so resorted to
	// this ugly approach
	public boolean isGenerating() { return generating; }
	private void setIsGenerating(boolean isGenerating) {
		Objects.requireNonNull(level, "World may not be null.");

		if (isGenerating != isGenerating()) {
			// Update block state if necessary
			level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(BlockMachineBase.ACTIVE, isGenerating));
		}
		this.generating = isGenerating;
	}

	SolarPanels getPanel() {
		if (panel == null) {
			updatePanel();
		}
		return panel;
	}

	private void updateState() {
		Objects.requireNonNull(level, "World may not be null.");

		// Generation is only possible if sky is visible above us
		setIsGenerating(level.canSeeSky(worldPosition.above()));
	}

	public int getGenerationRate() {
		Objects.requireNonNull(level, "World may not be null.");

		if (!isGenerating()) {
			return 0;
		}

		float skyAngle = (level.getOverworldClockTime() % 24000) / 24000f;

		// Ok, we are actively generating power, but check for a few conditions that would restrict
		// the generation to minimal production...
		if (!level.dimensionType().hasSkyLight() || // No light source in dimension (e.g. nether or end)
			(skyAngle > 0.25 && skyAngle < 0.75) || // Light source is below horizon
			(level.isRaining() || level.isThundering())) { // Weather is present
			return getPanel().generationRateN;
		}

		// At this point, we know a light source is present, and it's clear weather. We need to determine
		// the level of generation based on % of time through the day, with peak production at noon and
		// a smooth transition to night production as sun rises/sets
		float multiplier;
		if (skyAngle > 0.75) {
			// Morning to noon
			multiplier = (0.25f - (1 - skyAngle)) / 0.25f;
		} else {
			// Noon to sunset
			multiplier = (0.25f - skyAngle) / 0.25f;
		}

		return (int)Math.ceil(getPanel().generationRateN + (dayNightRange * multiplier));
	}


	// Overrides

	@Override
	public void tick(Level world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
		super.tick(world, pos, state, blockEntity);
		if (world == null || world.isClientSide()) {
			return;
		}

		if (getPanel() == TRContent.SolarPanels.CREATIVE) {
			checkOverfill = false;
			setEnergy(Integer.MAX_VALUE);
			for (Direction side : Direction.values()) {
				BlockEntity to = world.getBlockEntity(pos.relative(side));
				if (to instanceof PowerAcceptorBlockEntity receiver) {
					if (receiver.getMaxInput(side.getOpposite()) > 0){
						receiver.setStored(receiver.getMaxStoredPower());
					}
				}
			}
			return;
		}

		// State checking and updating
		if (world.getGameTime() % 20 == 0) {
			checkOverfill = true;
			updateState();
		}

		// Power generation calculations
		addEnergy(getGenerationRate());
	}

	@Override
	public long getBaseMaxPower() {
		return getPanel().internalCapacity;
	}

	@Override
	protected boolean canAcceptEnergy(@Nullable Direction side) { return false; }

	@Override
	public long getBaseMaxOutput() {
		if (getPanel() == TRContent.SolarPanels.CREATIVE) {
			return RcEnergyTier.INSANE.getMaxOutput();
		}
		// Solar panel output will only be limited by the cables the users use
		return RcEnergyTier.EXTREME.getMaxOutput();
	}

	@Override
	public long getBaseMaxInput() {
		return 0;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	@Override
	public boolean hasSlotConfig() {
		return false;
	}

	@Override
	public RcEnergyTier getTier() {
		return getPanel().powerTier;
	}

	@Override
	public void checkTier() {
		// Nope
	}

	@Override
	public void addInfo(List<Component> info, boolean isReal, boolean hasData) {
		if (panel == SolarPanels.CREATIVE) {
			return;
		}

		info.add(
				Component.translatable("reborncore.tooltip.energy.maxEnergy")
						.withStyle(ChatFormatting.GRAY)
						.append(": ")
						.append(
								Component.literal(PowerSystem.getLocalizedPower(getMaxStoredPower()))
										.withStyle(ChatFormatting.GOLD)
						)
		);

		info.add(
				Component.translatable("techreborn.tooltip.generationRate.day")
						.withStyle(ChatFormatting.GRAY)
						.append(": ")
						.append(
								Component.literal(PowerSystem.getLocalizedPower(panel.generationRateD))
										.withStyle(ChatFormatting.GOLD)
						)
		);

		info.add(
				Component.translatable("techreborn.tooltip.generationRate.night")
						.withStyle(ChatFormatting.GRAY)
						.append(": ")
						.append(
								Component.literal(PowerSystem.getLocalizedPower(panel.generationRateN))
										.withStyle(ChatFormatting.GOLD)
						)
		);

		info.add(
				Component.translatable("reborncore.tooltip.energy.tier")
						.withStyle(ChatFormatting.GRAY)
						.append(": ")
						.append(
								Component.literal(StringUtils.toFirstCapitalAllLowercase(getTier().toString()))
										.withStyle(ChatFormatting.GOLD)
						)
		);
	}

	@Override
	public void loadAdditional(ValueInput view) {
		if (level == null) {
			// We are in BlockEntity.create method during chunk load.
			this.checkOverfill = false;
			return;
		}
		updatePanel();
		super.loadAdditional(view);
	}

	// MachineBaseBlockEntity
	@Override
	public void onLoad() {
		super.onLoad();
		updatePanel();
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(final Player playerIn) {
		return new ItemStack(getBlockType());
	}

	@Override
	public BuiltScreenHandler createScreenHandler(int syncID, final Player player) {
		return new ScreenHandlerBuilder("solar_panel").player(player.getInventory()).inventory().hotbar().addInventory()
				.blockEntity(this).syncEnergyValue()
				.sync(ByteBufCodecs.BOOL, this::isGenerating, this::setIsGenerating)
				.addInventory().create(this, syncID);
	}
}
