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

package techreborn.items.tool.industrial;

import reborncore.common.powerSystem.RcEnergyTier;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRToolMaterials;
import techreborn.items.tool.DrillItem;
import techreborn.utils.TRItemUtils;
import techreborn.utils.ToolsUtil;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class IndustrialDrillItem extends DrillItem {

	public IndustrialDrillItem(String name) {
		super(TRToolMaterials.INDUSTRIAL_DRILL, TechRebornConfig.industrialDrillCharge.get(), RcEnergyTier.INSANE, TechRebornConfig.industrialDrillCost.get(), 20.0F, name);
	}

	private boolean shouldBreak(Player playerIn, Level worldIn, BlockPos originalPos, BlockPos pos) {
		if (originalPos.equals(pos)) {
			return false;
		}
		BlockState blockState = worldIn.getBlockState(pos);
		if (blockState.isAir()) {
			return false;
		}
		if (blockState.liquid()) {
			return false;
		}
		float blockHardness = blockState.getDestroyProgress(playerIn, worldIn, pos);
		if (blockHardness == -1.0F) {
			return false;
		}
		float originalHardness = worldIn.getBlockState(originalPos).getDestroySpeed(worldIn, originalPos);
		return !((originalHardness / blockHardness) > 10.0F);
	}

	// DrillItem
	@Override
	public boolean mineBlock(ItemStack stack, Level worldIn, BlockState stateIn, BlockPos pos, LivingEntity entityLiving) {
		if (!TRItemUtils.isActive(stack)) {
			return super.mineBlock(stack, worldIn, stateIn, pos, entityLiving);
		}
		if (!(entityLiving instanceof Player playerIn)) {
			return super.mineBlock(stack, worldIn, stateIn, pos, entityLiving);
		}
		for (BlockPos additionalPos : ToolsUtil.getAOEMiningBlocks(worldIn, pos, entityLiving, 1)) {
			if (shouldBreak(playerIn, worldIn, pos, additionalPos)) {
				ToolsUtil.breakBlock(stack, worldIn, additionalPos, entityLiving, cost);
			}
		}

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
	public void onUseTick(Level world, LivingEntity entity, ItemStack stack, int i) {
		TRItemUtils.checkActive(stack, cost, entity);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> tooltip, TooltipFlag type) {
		TRItemUtils.buildActiveTooltip(stack, tooltip);
	}
}
