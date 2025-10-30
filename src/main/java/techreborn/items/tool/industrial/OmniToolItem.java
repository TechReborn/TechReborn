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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import reborncore.api.IToolHandler;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.TorchHelper;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;
import techreborn.init.TRItemSettings;
import techreborn.init.TRToolMaterials;


public class OmniToolItem extends Item implements RcEnergyItem, IToolHandler {
	// 4M FE max charge with 1k charge rate
	public OmniToolItem(String name) {
		super(TRItemSettings.unbreakable(name).tool(TRToolMaterials.OMNI_TOOL, TRContent.BlockTags.OMNI_TOOL_MINEABLE, 3f, 1f, 0.0F));
	}

	// MiningToolItem
	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
		return Items.DIAMOND_AXE.isCorrectToolForDrops(stack, state) || Items.DIAMOND_SWORD.isCorrectToolForDrops(stack, state)
				|| Items.DIAMOND_PICKAXE.isCorrectToolForDrops(stack, state) || Items.DIAMOND_SHOVEL.isCorrectToolForDrops(stack, state)
				|| Items.SHEARS.isCorrectToolForDrops(stack, state);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (getStoredEnergy(stack) >= TechRebornConfig.omniToolCost) {
			return TRToolMaterials.OMNI_TOOL.speed();
		}
		Tool toolComponent = stack.get(DataComponents.TOOL);
		return toolComponent != null ? toolComponent.defaultMiningSpeed() : 1.0F;
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level worldIn, BlockState blockIn, BlockPos pos, LivingEntity entityLiving) {
		tryUseEnergy(stack, TechRebornConfig.omniToolCost);
		return true;
	}

	@Override
	public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (tryUseEnergy(stack, TechRebornConfig.omniToolHitCost) && target.level() instanceof ServerLevel serverWorld) {
			target.hurtServer(serverWorld, serverWorld.damageSources().playerAttack((Player) attacker), 8F);
		}
	}

	// Item
	@Override
	public InteractionResult useOn(UseOnContext context) {
		InteractionResult tryUse = Items.DIAMOND_AXE.useOn(context);
		if (tryUse != InteractionResult.PASS) { return tryUse; }

		tryUse = Items.SHEARS.useOn(context);
		if (tryUse != InteractionResult.PASS) { return tryUse; }

		tryUse = Items.DIAMOND_SHOVEL.useOn(context);
		if (tryUse != InteractionResult.PASS) { return tryUse; }

		return TorchHelper.placeTorch(context);
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return ItemUtils.getPowerForDurabilityBar(stack);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return ItemUtils.getColorForDurabilityBar(stack);
	}

	// RcEnergyItem
	@Override
	public long getEnergyCapacity(ItemStack stack) {
		return TechRebornConfig.omniToolCharge;
	}

	@Override
	public long getEnergyMaxOutput(ItemStack stack) {
		return 0;
	}

	@Override
	public RcEnergyTier getTier() {
		return RcEnergyTier.EXTREME;
	}

	// IToolHandler
	@Override
	public boolean handleTool(ItemStack stack, BlockPos pos, Level world, Player player, Direction side, boolean damage) {
		if (!player.level().isClientSide && this.getStoredEnergy(stack) >= 5.0) {
			this.tryUseEnergy(stack, 5);
			return true;
		} else {
			return false;
		}
	}

}
