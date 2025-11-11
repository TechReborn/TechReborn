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
import techreborn.items.tool.ChainsawItem;
import techreborn.utils.TRItemUtils;
import techreborn.utils.ToolsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class IndustrialChainsawItem extends ChainsawItem {

	private static final Direction[] SEARCH_ORDER = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.UP};

	private BlockState lastCheckedBlockState;

	public IndustrialChainsawItem(String name) {
		super(TRToolMaterials.INDUSTRIAL_CHAINSAW, TechRebornConfig.industrialChainsawCharge, RcEnergyTier.INSANE, TechRebornConfig.industrialChainsawCost, 20F, name);
	}

	private boolean isValidLog(BlockState state) {
		return state.is(BlockTags.LOGS);
	}

	private boolean isValidLeaves(BlockState state) {
		return state.is(BlockTags.LEAVES) || state.is(BlockTags.WART_BLOCKS) || state.is(Blocks.SHROOMLIGHT);
	}

	private boolean isValidStartBlock(BlockState state) {
		return isValidLog(state) || isValidLeaves(state);
	}

	private void findWood(Level world, BlockPos pos, List<BlockPos> wood, List<BlockPos> leaves) {
		//Limit the amount of wood to be broken to 64 blocks.
		if (wood.size() >= 64) {
			return;
		}
		//Search 150 leaves for wood
		if (leaves.size() >= 150) {
			return;
		}
		for (Direction facing : SEARCH_ORDER) {
			BlockPos checkPos = pos.relative(facing);
			if (!wood.contains(checkPos) && !leaves.contains(checkPos)) {
				BlockState state = world.getBlockState(checkPos);

				if (isValidLog(state)) {
					wood.add(checkPos);
					findWood(world, checkPos, wood, leaves);
				} else if (isValidLeaves(state)) {
					leaves.add(checkPos);
					findWood(world, checkPos, wood, leaves);
				}
			}
		}
	}

	//ChainsawItem
	@Override
	public boolean mineBlock(ItemStack stack, Level worldIn, BlockState blockIn, BlockPos pos, LivingEntity entityLiving) {
		List<BlockPos> wood = new ArrayList<>();
		List<BlockPos> leaves = new ArrayList<>();
		if (TRItemUtils.isActive(stack) && (lastCheckedBlockState == null || isValidStartBlock(lastCheckedBlockState))) {
			findWood(worldIn, pos, wood, leaves);
			wood.remove(pos);
			wood.stream()
					.filter(p -> tryUseEnergy(stack, cost))
					.forEach(pos1 -> ToolsUtil.breakBlock(stack, worldIn, pos1, entityLiving, cost));
			leaves.remove(pos);
			leaves.forEach(pos1 -> ToolsUtil.breakBlock(stack, worldIn, pos1, entityLiving, 0));
		}
		return super.mineBlock(stack, worldIn, blockIn, pos, entityLiving);
	}

	// Item
	@Override
	public boolean canDestroyBlock(ItemStack stack, BlockState state, Level world, BlockPos pos, LivingEntity miner) {
		lastCheckedBlockState = state;
		return super.canDestroyBlock(stack, state, world, pos, miner);
	}

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
