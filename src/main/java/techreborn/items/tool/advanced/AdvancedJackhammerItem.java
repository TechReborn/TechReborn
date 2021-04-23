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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.common.misc.MultiBlockBreakingTool;
import reborncore.common.util.ItemUtils;
import team.reborn.energy.EnergyTier;
import techreborn.config.TechRebornConfig;
import techreborn.items.tool.JackhammerItem;
import techreborn.utils.MessageIDs;
import techreborn.utils.ToolsUtil;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AdvancedJackhammerItem extends JackhammerItem implements MultiBlockBreakingTool {

	public AdvancedJackhammerItem() {
		super(TechRebornConfig.advancedJackhammerCharge, EnergyTier.EXTREME, TechRebornConfig.advancedJackhammerCost);
	}

	private boolean shouldBreak(World worldIn, BlockPos originalPos, BlockPos pos, ItemStack stack) {
		if (originalPos.equals(pos)) {
			return false;
		}
		BlockState blockState = worldIn.getBlockState(pos);
		if (ToolsUtil.JackHammerSkippedBlocks(blockState)){
			return false;
		}
		return (stack.getItem().isEffectiveOn(blockState));
	}

	// JackhammerItem
	@Override
	public boolean postMine(ItemStack stack, World worldIn, BlockState stateIn, BlockPos pos, LivingEntity entityLiving) {
		if (!ItemUtils.isActive(stack) || !stack.getItem().isEffectiveOn(stateIn)) {
			return super.postMine(stack, worldIn, stateIn, pos, entityLiving);
		}
		for (BlockPos additionalPos : ToolsUtil.getAOEMiningBlocks(worldIn, pos, entityLiving, 1)) {
			if (shouldBreak(worldIn, pos, additionalPos, stack)) {
				ToolsUtil.breakBlock(stack, worldIn, additionalPos, entityLiving, cost);
			}
		}

		return super.postMine(stack, worldIn, stateIn, pos, entityLiving);
	}

	// Item
	@Override
	public TypedActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
		final ItemStack stack = player.getStackInHand(hand);
		if (player.isSneaking()) {
			ItemUtils.switchActive(stack, cost, world.isClient, MessageIDs.poweredToolID);
			return new TypedActionResult<>(ActionResult.SUCCESS, stack);
		}
		return new TypedActionResult<>(ActionResult.PASS, stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		ItemUtils.checkActive(stack, cost, entity.world.isClient, MessageIDs.poweredToolID);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
		ItemUtils.buildActiveTooltip(stack, tooltip);
	}

	// MultiBlockBreakingTool
	@Override
	public Set<BlockPos> getBlocksToBreak(ItemStack stack, World worldIn, BlockPos pos, @Nullable LivingEntity entityLiving) {
		if (!stack.getItem().isEffectiveOn(worldIn.getBlockState(pos))) {
			return Collections.emptySet();
		}
		return ToolsUtil.getAOEMiningBlocks(worldIn, pos, entityLiving, 1, false)
				.stream()
				.filter((blockPos -> shouldBreak(worldIn, pos, blockPos, stack)))
				.collect(Collectors.toSet());
	}
}
