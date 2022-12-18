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

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.MiningToolItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.TorchHelper;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;
import techreborn.init.TRToolMaterials;
import techreborn.items.tool.MiningLevel;

public class OmniToolItem extends MiningToolItem implements RcEnergyItem {
	public final int maxCharge = TechRebornConfig.omniToolCharge;
	public int cost = TechRebornConfig.omniToolCost;
	public int hitCost = TechRebornConfig.omniToolHitCost;
	public final int miningLevel;

	// 4M FE max charge with 1k charge rate
	public OmniToolItem() {
		super(3, 1, TRToolMaterials.OMNI_TOOL, TRContent.BlockTags.OMNI_TOOL_MINEABLE, new Item.Settings().maxCount(1).maxDamage(-1));
		this.miningLevel = MiningLevel.DIAMOND.intLevel;
	}

	// PickaxeItem
	@Override
	public boolean isSuitableFor(BlockState state) {
		return Items.DIAMOND_AXE.isSuitableFor(state) || Items.DIAMOND_SWORD.isSuitableFor(state)
				|| Items.DIAMOND_PICKAXE.isSuitableFor(state) || Items.DIAMOND_SHOVEL.isSuitableFor(state)
				|| Items.SHEARS.isSuitableFor(state);
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		if (getStoredEnergy(stack) >= cost) {
			return getMaterial().getMiningSpeedMultiplier();
		}
		return super.getMiningSpeedMultiplier(stack, state);
	}

	// MiningToolItem
	@Override
	public boolean postMine(ItemStack stack, World worldIn, BlockState blockIn, BlockPos pos, LivingEntity entityLiving) {
		tryUseEnergy(stack, cost);
		return true;
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (tryUseEnergy(stack, hitCost)) {
			target.damage(DamageSource.player((PlayerEntity) attacker), 8F);
		}
		return false;
	}

	// ToolItem
	@Override
	public boolean canRepair(ItemStack itemStack_1, ItemStack itemStack_2) {
		return false;
	}

	// Item
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		return TorchHelper.placeTorch(context);
	}

	@Override
	public boolean isDamageable() {
		return false;
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

	// EnergyHolder
	@Override
	public long getEnergyCapacity() {
		return maxCharge;
	}

	@Override
	public long getEnergyMaxOutput() {
		return 0;
	}

	@Override
	public RcEnergyTier getTier() {
		return RcEnergyTier.EXTREME;
	}
}
