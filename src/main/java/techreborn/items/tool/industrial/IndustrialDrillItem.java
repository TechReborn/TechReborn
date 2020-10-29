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
import net.minecraft.block.Material;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.common.util.ItemUtils;
import team.reborn.energy.EnergyTier;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;
import techreborn.items.tool.DrillItem;
import techreborn.items.tool.MiningLevel;
import techreborn.utils.InitUtils;
import techreborn.utils.MessageIDs;
import techreborn.utils.ToolsUtil;

import java.util.List;

public class IndustrialDrillItem extends DrillItem {

	public IndustrialDrillItem() {
		super(ToolMaterials.DIAMOND, TechRebornConfig.industrialDrillCharge, EnergyTier.INSANE, TechRebornConfig.industrialDrillCost, 20.0F, 1.0F, MiningLevel.DIAMOND);
	}

	private boolean shouldBreak(PlayerEntity playerIn, World worldIn, BlockPos originalPos, BlockPos pos) {
		if (originalPos.equals(pos)) {
			return false;
		}
		BlockState blockState = worldIn.getBlockState(pos);
		if (blockState.getMaterial() == Material.AIR) {
			return false;
		}
		if (blockState.getMaterial().isLiquid()) {
			return false;
		}
		float blockHardness = blockState.calcBlockBreakingDelta(playerIn, worldIn, pos);
		if (blockHardness == -1.0F) {
			return false;
		}
		float originalHardness = worldIn.getBlockState(originalPos).getHardness(worldIn, originalPos);
		return !((originalHardness / blockHardness) > 10.0F);
	}

	// DrillItem
	@Override
	public boolean postMine(ItemStack stack, World worldIn, BlockState stateIn, BlockPos pos, LivingEntity entityLiving) {
		if (!ItemUtils.isActive(stack)) {
			return super.postMine(stack, worldIn, stateIn, pos, entityLiving);
		}
		if (!(entityLiving instanceof PlayerEntity)) {
			return super.postMine(stack, worldIn, stateIn, pos, entityLiving);
		}
		PlayerEntity playerIn = (PlayerEntity) entityLiving;
		for (BlockPos additionalPos : ToolsUtil.getAOEMiningBlocks(worldIn, pos, entityLiving, 1)) {
			if (shouldBreak(playerIn, worldIn, pos, additionalPos)) {
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
	public void usageTick(World world, LivingEntity entity, ItemStack stack, int i) {
		ItemUtils.checkActive(stack, cost, entity.world.isClient, MessageIDs.poweredToolID);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World worldIn, List<Text> tooltip, TooltipContext flagIn) {
		ItemUtils.buildActiveTooltip(stack, tooltip);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendStacks(ItemGroup par2ItemGroup, DefaultedList<ItemStack> itemList) {
		if (!isIn(par2ItemGroup)) {
			return;
		}
		InitUtils.initPoweredItems(TRContent.INDUSTRIAL_DRILL, itemList);
	}
}
