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

package techreborn.blockentity.transformers;

import org.jetbrains.annotations.Nullable;
import reborncore.api.IListInfoProvider;
import reborncore.api.IToolDrop;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.StringUtils;
import techreborn.blocks.transformers.BlockTransformer;
import techreborn.config.TechRebornConfig;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Created by Rushmead
 */
public class TransformerBlockEntity extends PowerAcceptorBlockEntity implements IToolDrop, IListInfoProvider {

	public final String name;
	public final Block wrenchDrop;
	public final RcEnergyTier inputTier;
	public final RcEnergyTier outputTier;
	public final int maxInput;
	public final int maxOutput;
	public final int maxStorage;

	public TransformerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, String name, Block wrenchDrop, RcEnergyTier tier) {
		super(blockEntityType, pos, state);
		this.wrenchDrop = wrenchDrop;
		this.inputTier = tier;
		if (tier != RcEnergyTier.MICRO) {
			outputTier = RcEnergyTier.values()[tier.ordinal() - 1];
		} else {
			outputTier = RcEnergyTier.MICRO;
		}
		this.name = name;
		this.maxInput = tier.getMaxInput();
		this.maxOutput = tier.getMaxOutput();
		this.maxStorage = tier.getMaxInput() * 2;

	}

	// PowerAcceptorBlockEntity
	@Override
	protected boolean canAcceptEnergy(@Nullable Direction side) {
		if (side == null) {
			return true;
		}
		if (TechRebornConfig.IC2TransformersStyle) {
			return getFacing() == side;
		}
		return getFacing() != side;
	}

	@Override
	protected boolean canProvideEnergy(@Nullable Direction side) {
		if (side == null) {
			return true;
		}
		if (TechRebornConfig.IC2TransformersStyle) {
			return getFacing() != side;
		}
		return getFacing() == side;
	}

	@Override
	public long getBaseMaxPower() {
		return maxStorage;
	}

	@Override
	public long getBaseMaxOutput() {
		return outputTier.getMaxOutput();
	}

	@Override
	public long getBaseMaxInput() {
		return inputTier.getMaxInput();
	}

	@Override
	public void checkTier() {
		//Nope
	}

	// MachineBaseBlockEntity
	@Override
	public Direction getFacingEnum() {
		if (level == null) {
			return null;
		}
		Block block = level.getBlockState(worldPosition).getBlock();
		if (block instanceof BlockTransformer) {
			return ((BlockTransformer) block).getFacing(level.getBlockState(worldPosition));
		}
		return null;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}

	// IToolDrop
	@Override
	public ItemStack getToolDrop(Player playerIn) {
		return new ItemStack(wrenchDrop);
	}

	// IListInfoProvider
	@Override
	public void addInfo(List<Component> info, boolean isReal, boolean hasData) {
		info.add(
				Component.translatable("reborncore.tooltip.energy.inputRate")
						.withStyle(ChatFormatting.GRAY)
						.append(": ")
						.append(PowerSystem.getLocalizedPower(getMaxInput(null)))
						.withStyle(ChatFormatting.GOLD)
		);
		info.add(
				Component.translatable("techreborn.tooltip.input_tier")
						.withStyle(ChatFormatting.GRAY)
						.append(": ")
						.append(StringUtils.toFirstCapitalAllLowercase(inputTier.toString()))
						.withStyle(ChatFormatting.GOLD)
		);
		info.add(
				Component.translatable("reborncore.tooltip.energy.outputRate")
						.withStyle(ChatFormatting.GRAY)
						.append(": ")
						.append(PowerSystem.getLocalizedPower(getMaxOutput(null)))
						.withStyle(ChatFormatting.GOLD)
		);
		info.add(
				Component.translatable("techreborn.tooltip.output_tier")
						.withStyle(ChatFormatting.GRAY)
						.append(": ")
						.append(StringUtils.toFirstCapitalAllLowercase(outputTier.toString()))
						.withStyle(ChatFormatting.GOLD)
		);
	}
}
