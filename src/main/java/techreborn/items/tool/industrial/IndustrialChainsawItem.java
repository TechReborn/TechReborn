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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterials;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import reborncore.common.util.ItemUtils;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyTier;
import techreborn.config.TechRebornConfig;
import techreborn.items.tool.ChainsawItem;
import techreborn.utils.MessageIDs;
import techreborn.utils.TagUtils;
import techreborn.utils.ToolsUtil;

import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public class IndustrialChainsawItem extends ChainsawItem {

	private static final Direction[] SEARCH_ORDER = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.UP};

	public IndustrialChainsawItem() {
		super(ToolMaterials.DIAMOND, TechRebornConfig.industrialChainsawCharge, EnergyTier.EXTREME, TechRebornConfig.industrialChainsawCost, 20F, 1.0F, Items.DIAMOND_AXE);
	}

	private void findWood(World world, BlockPos pos, List<BlockPos> wood, List<BlockPos> leaves) {
		//Limit the amount of wood to be broken to 64 blocks.
		if (wood.size() >= 64) {
			return;
		}
		//Search 150 leaves for wood
		if (leaves.size() >= 150) {
			return;
		}
		for (Direction facing : SEARCH_ORDER) {
			BlockPos checkPos = pos.offset(facing);
			if (!wood.contains(checkPos) && !leaves.contains(checkPos)) {
				BlockState state = world.getBlockState(checkPos);
				if (TagUtils.hasTag(state.getBlock(), BlockTags.LOGS)) {
					wood.add(checkPos);
					findWood(world, checkPos, wood, leaves);
				} else if (TagUtils.hasTag(state.getBlock(), BlockTags.LEAVES)) {
					leaves.add(checkPos);
					findWood(world, checkPos, wood, leaves);
				}
			}
		}
	}

	//ChainsawItem
	@Override
	public boolean postMine(ItemStack stack, World worldIn, BlockState blockIn, BlockPos pos, LivingEntity entityLiving) {
		List<BlockPos> wood = new ArrayList<>();
		List<BlockPos> leaves = new ArrayList<>();
		if (ItemUtils.isActive(stack)) {
			findWood(worldIn, pos, wood, leaves);
			wood.remove(pos);
			wood.stream()
					.filter(p -> Energy.of(stack).simulate().use(cost))
					.forEach(pos1 -> ToolsUtil.breakBlock(stack, worldIn, pos1, entityLiving, cost));
			leaves.remove(pos);
			leaves.forEach(pos1 -> ToolsUtil.breakBlock(stack, worldIn, pos1, entityLiving, 0));
		}
		return super.postMine(stack, worldIn, blockIn, pos, entityLiving);
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
	public void usageTick(World world, LivingEntity entity, ItemStack stack, int i) {
		ItemUtils.checkActive(stack, cost, entity.world.isClient, MessageIDs.poweredToolID);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
		ItemUtils.buildActiveTooltip(stack, tooltip);
	}
}
