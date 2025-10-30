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

package techreborn.items.tool.advanced;

import org.jetbrains.annotations.Nullable;
import reborncore.common.misc.MultiBlockBreakingTool;
import reborncore.common.powerSystem.RcEnergyTier;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRToolMaterials;
import techreborn.items.tool.JackhammerItem;
import techreborn.utils.TRItemUtils;
import techreborn.utils.ToolsUtil;

import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedJackhammerItem extends JackhammerItem implements MultiBlockBreakingTool {

	public AdvancedJackhammerItem(String name) {
		super(TRToolMaterials.ADVANCED_JACKHAMMER, TechRebornConfig.advancedJackhammerCharge, RcEnergyTier.EXTREME, TechRebornConfig.advancedJackhammerCost, name);
	}

	// JackhammerItem
	@Override
	public boolean mineBlock(ItemStack stack, Level worldIn, BlockState stateIn, BlockPos pos, LivingEntity entityLiving) {
		// No AOE mining turned on OR we've broken a wrong block
		if (!TRItemUtils.isActive(stack) || !isCorrectToolForDrops(stack, stateIn)) {
			return super.mineBlock(stack, worldIn, stateIn, pos, entityLiving);
		}

		// Do AoE mining except original block
		for (BlockPos additionalPos : ToolsUtil.getAOEMiningBlocks(worldIn, pos, entityLiving, 1)) {
			if (shouldBreak(worldIn, pos, additionalPos)) {
				ToolsUtil.breakBlock(stack, worldIn, additionalPos, entityLiving, cost);
			}
		}

		// Do not forget to use energy for original block
		return super.mineBlock(stack, worldIn, stateIn, pos, entityLiving);
	}

	// Item
	@Override
	public InteractionResult use(final Level world, final Player player, final InteractionHand hand) {
		final ItemStack stack = player.getItemInHand(hand);
		if (player.isShiftKeyDown()) {
			TRItemUtils.switchActive(stack, cost, player);
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, @Nullable EquipmentSlot slot) {
		TRItemUtils.checkActive(stack, cost, entity);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> tooltip, TooltipFlag type) {
		TRItemUtils.buildActiveTooltip(stack, tooltip);
	}

	// MultiBlockBreakingTool
	@Override
	public Set<BlockPos> getBlocksToBreak(ItemStack stack, Level worldIn, BlockPos pos, @Nullable LivingEntity entityLiving) {
		if (!isCorrectToolForDrops(stack, worldIn.getBlockState(pos)) || !TRItemUtils.isActive(stack)) {
			return Collections.emptySet();
		}
		return ToolsUtil.getAOEMiningBlocks(worldIn, pos, entityLiving, 1, false)
			.stream()
			.filter((blockPos -> shouldBreak(worldIn, pos, blockPos)))
			.collect(Collectors.toSet());
	}
}
