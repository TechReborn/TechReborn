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

import org.jetbrains.annotations.Nullable;
import reborncore.common.misc.MultiBlockBreakingTool;
import reborncore.common.powerSystem.RcEnergyTier;
import techreborn.component.TRDataComponentTypes;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRToolMaterials;
import techreborn.items.tool.JackhammerItem;
import techreborn.utils.TRItemUtils;
import techreborn.utils.ToolsUtil;

import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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

public class IndustrialJackhammerItem extends JackhammerItem implements MultiBlockBreakingTool {

	public IndustrialJackhammerItem(String name) {
		super(TRToolMaterials.INDUSTRIAL_JACKHAMMER, TechRebornConfig.industrialJackhammerCharge, RcEnergyTier.INSANE, TechRebornConfig.industrialJackhammerCost, name);
	}

	// Cycle Inactive, Active 3*3 and Active 5*5
	private void switchAOE(ItemStack stack, int cost, Entity entity) {
		TRItemUtils.checkActive(stack, cost, entity);
		if (!TRItemUtils.isActive(stack)) {
			TRItemUtils.switchActive(stack, cost, entity);
			stack.set(TRDataComponentTypes.AOE5, false);
			if (entity instanceof ServerPlayer serverPlayerEntity) {
				serverPlayerEntity.displayClientMessage(Component.translatable("techreborn.message.setTo").withStyle(ChatFormatting.GRAY).append(" ").append(Component.literal("3*3").withStyle(ChatFormatting.GOLD)), true);
			}
		} else {
			if (isAOE5(stack)) {
				TRItemUtils.switchActive(stack, cost, entity);
				stack.set(TRDataComponentTypes.AOE5, false);
			} else {
				stack.set(TRDataComponentTypes.AOE5, true);
				if (entity instanceof ServerPlayer serverPlayerEntity) {
					serverPlayerEntity.displayClientMessage(Component.translatable("techreborn.message.setTo").withStyle(ChatFormatting.GRAY).append(" ").append(Component.literal("5*5").withStyle(ChatFormatting.GOLD)), true);
				}
			}
		}
	}

	private boolean isAOE5(ItemStack stack) {
		return !stack.isEmpty() && Boolean.TRUE.equals(stack.get(TRDataComponentTypes.AOE5));
	}

	// JackhammerItem
	@Override
	public boolean mineBlock(ItemStack stack, Level worldIn, BlockState stateIn, BlockPos pos, LivingEntity entityLiving) {
		// No AOE mining turned on OR we've broken a wrong block
		if (!TRItemUtils.isActive(stack) || !isCorrectToolForDrops(stack, stateIn)) {
			return super.mineBlock(stack, worldIn, stateIn, pos, entityLiving);
		}

		// Do AoE mining except original block
		int radius = isAOE5(stack) ? 2 : 1;
		for (BlockPos additionalPos : ToolsUtil.getAOEMiningBlocks(worldIn, pos, entityLiving, radius)) {
			if (shouldBreak(worldIn, pos, additionalPos)) {
				ToolsUtil.breakBlock(stack, worldIn, additionalPos, entityLiving, cost);
			}
		}

		// Do not forget to use energy for original block
		return super.mineBlock(stack, worldIn, stateIn, pos, entityLiving);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		float speed = super.getDestroySpeed(stack, state);

		if (speed > unpoweredSpeed) {
			return speed * 4;
		}

		return speed;
	}

	// Item
	@Override
	public InteractionResult use(final Level world, final Player player, final InteractionHand hand) {
		final ItemStack stack = player.getItemInHand(hand);
		if (player.isShiftKeyDown()) {
			switchAOE(stack, cost, player);
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
		if (TRItemUtils.isActive(stack)) {
			if (isAOE5(stack)) {
				tooltip.accept(Component.literal("5*5").withStyle(ChatFormatting.RED));
			} else {
				tooltip.accept(Component.literal("3*3").withStyle(ChatFormatting.RED));
			}
		}
	}

	// MultiBlockBreakingTool
	@Override
	public Set<BlockPos> getBlocksToBreak(ItemStack stack, Level worldIn, BlockPos pos, @Nullable LivingEntity entityLiving) {
		if (!isCorrectToolForDrops(stack, worldIn.getBlockState(pos)) || !TRItemUtils.isActive(stack)) {
			return Collections.emptySet();
		}
		int radius = isAOE5(stack) ? 2 : 1;
		return ToolsUtil.getAOEMiningBlocks(worldIn, pos, entityLiving, radius, false)
			.stream()
			.filter((blockPos -> shouldBreak(worldIn, pos, blockPos)))
			.collect(Collectors.toSet());
	}
}
