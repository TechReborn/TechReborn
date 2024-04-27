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

import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import reborncore.api.IToolHandler;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.TorchHelper;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;
import techreborn.init.TRToolMaterials;


public class OmniToolItem extends MiningToolItem implements RcEnergyItem, IToolHandler {
	public final int miningLevel;

	// 4M FE max charge with 1k charge rate
	public OmniToolItem() {
		super(TRToolMaterials.OMNI_TOOL, TRContent.BlockTags.OMNI_TOOL_MINEABLE, new Item.Settings()
			.maxDamage(0)
			.attributeModifiers(PickaxeItem.createAttributeModifiers(TRToolMaterials.OMNI_TOOL, 3, 1)
		));
		this.miningLevel = MiningLevels.DIAMOND;
	}

	// MiningToolItem
	@Override
	public boolean isCorrectForDrops(ItemStack stack, BlockState state) {
		return Items.DIAMOND_AXE.isCorrectForDrops(stack, state) || Items.DIAMOND_SWORD.isCorrectForDrops(stack, state)
				|| Items.DIAMOND_PICKAXE.isCorrectForDrops(stack, state) || Items.DIAMOND_SHOVEL.isCorrectForDrops(stack, state)
				|| Items.SHEARS.isCorrectForDrops(stack, state);
	}

	@Override
	public float getMiningSpeed(ItemStack stack, BlockState state) {
		if (getStoredEnergy(stack) >= TechRebornConfig.omniToolCost) {
			return getMaterial().getMiningSpeedMultiplier();
		}
		return super.getMiningSpeed(stack, state);
	}

	@Override
	public boolean postMine(ItemStack stack, World worldIn, BlockState blockIn, BlockPos pos, LivingEntity entityLiving) {
		tryUseEnergy(stack, TechRebornConfig.omniToolCost);
		return true;
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (tryUseEnergy(stack, TechRebornConfig.omniToolHitCost)) {
			target.damage(target.getWorld().getDamageSources().playerAttack((PlayerEntity) attacker), 8F);
		}
		return true;
	}

	// ToolItem
	@Override
	public boolean canRepair(ItemStack itemStack_1, ItemStack itemStack_2) {
		return false;
	}

	// Item
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		ActionResult tryUse = Items.DIAMOND_AXE.useOnBlock(context);
		if (tryUse != ActionResult.PASS) { return tryUse; }

		tryUse = Items.SHEARS.useOnBlock(context);
		if (tryUse != ActionResult.PASS) { return tryUse; }

		tryUse = Items.DIAMOND_SHOVEL.useOnBlock(context);
		if (tryUse != ActionResult.PASS) { return tryUse; }

		return TorchHelper.placeTorch(context);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

	@Override
	public int getItemBarStep(ItemStack stack) {
		return ItemUtils.getPowerForDurabilityBar(stack);
	}

	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public int getItemBarColor(ItemStack stack) {
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
	public boolean handleTool(ItemStack stack, BlockPos pos, World world, PlayerEntity player, Direction side, boolean damage) {
		if (!player.getWorld().isClient && this.getStoredEnergy(stack) >= 5.0) {
			this.tryUseEnergy(stack, 5);
			return true;
		} else {
			return false;
		}
	}

}
