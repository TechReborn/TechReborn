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
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.common.misc.MultiBlockBreakingTool;
import reborncore.common.util.ChatUtils;
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

public class IndustrialJackhammerItem extends JackhammerItem implements MultiBlockBreakingTool {

	public IndustrialJackhammerItem() {
		super(TechRebornConfig.industrialJackhammerCharge, EnergyTier.INSANE, TechRebornConfig.industrialJackhammerCost);
	}

	// Cycle Inactive, Active 3*3 and Active 5*5
	private void switchAOE(ItemStack stack, int cost, boolean isClient, int messageId) {
		ItemUtils.checkActive(stack, cost, isClient, messageId);
		if (!ItemUtils.isActive(stack)) {
			ItemUtils.switchActive(stack, cost, isClient, messageId);
			stack.getOrCreateTag().putBoolean("AOE5", false);
			if (isClient) {
				ChatUtils.sendNoSpamMessages(messageId, new TranslatableText("techreborn.message.setTo").formatted(Formatting.GRAY).append(" ").append(new LiteralText("3*3").formatted(Formatting.GOLD)));
			}
		} else {
			if (isAOE5(stack)) {
				ItemUtils.switchActive(stack, cost, isClient, messageId);
				stack.getOrCreateTag().putBoolean("AOE5", false);
			} else {
				stack.getOrCreateTag().putBoolean("AOE5", true);
				if (isClient) {
					ChatUtils.sendNoSpamMessages(messageId, new TranslatableText("techreborn.message.setTo").formatted(Formatting.GRAY).append(" ").append(new LiteralText("5*5").formatted(Formatting.GOLD)));
				}
			}
		}
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

	private boolean isAOE5(ItemStack stack) {
		return !stack.isEmpty() && stack.getTag() != null && stack.getTag().getBoolean("AOE5");
	}

	// JackhammerItem
	@Override
	public boolean postMine(ItemStack stack, World worldIn, BlockState stateIn, BlockPos pos, LivingEntity entityLiving) {
		if (!ItemUtils.isActive(stack) || !stack.getItem().isEffectiveOn(stateIn)) {
			return super.postMine(stack, worldIn, stateIn, pos, entityLiving);
		}
		int radius = isAOE5(stack) ? 2 : 1;
		for (BlockPos additionalPos : ToolsUtil.getAOEMiningBlocks(worldIn, pos, entityLiving, radius)) {
			if (shouldBreak(worldIn, pos, additionalPos, stack)) {
				ToolsUtil.breakBlock(stack, worldIn, additionalPos, entityLiving, cost);
			}
		}

		return super.postMine(stack, worldIn, stateIn, pos, entityLiving);
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		float speed = super.getMiningSpeedMultiplier(stack, state);

		if (speed > unpoweredSpeed) {
			return miningSpeed * 4;
		}

		return speed;
	}

	// Item
	@Override
	public TypedActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
		final ItemStack stack = player.getStackInHand(hand);
		if (player.isSneaking()) {
			switchAOE(stack, cost, world.isClient, MessageIDs.poweredToolID);
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
		if (ItemUtils.isActive(stack)) {
			if (isAOE5(stack)) {
				tooltip.add(new LiteralText("5*5").formatted(Formatting.RED));
			} else {
				tooltip.add(new LiteralText("3*3").formatted(Formatting.RED));
			}
		}
	}

	// MultiBlockBreakingTool
	@Override
	public Set<BlockPos> getBlocksToBreak(ItemStack stack, World worldIn, BlockPos pos, @Nullable LivingEntity entityLiving) {
		if (!stack.getItem().isEffectiveOn(worldIn.getBlockState(pos))) {
			return Collections.emptySet();
		}
		int radius = isAOE5(stack) ? 2 : 1;
		return ToolsUtil.getAOEMiningBlocks(worldIn, pos, entityLiving, radius, false)
				.stream()
				.filter((blockPos -> shouldBreak(worldIn, pos, blockPos, stack)))
				.collect(Collectors.toSet());
	}
}
