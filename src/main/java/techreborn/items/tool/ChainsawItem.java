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

package techreborn.items.tool;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.api.items.EnchantmentTargetHandler;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;

public class ChainsawItem extends AxeItem implements RcEnergyItem, EnchantmentTargetHandler {

	public final int maxCharge;
	public final int cost;
	public final float poweredSpeed;
	private final float unpoweredSpeed;
	public final Item referenceTool;
	public final RcEnergyTier tier;

	public ChainsawItem(ToolMaterial material, int energyCapacity, RcEnergyTier tier, int cost, float poweredSpeed, float unpoweredSpeed, Item referenceTool) {
		// combat stats same as for diamond axe. Fix for #2468
		super(material, 5.0F, -3.0F, new Item.Settings().maxCount(1).maxDamage(-1));
		this.maxCharge = energyCapacity;
		this.tier = tier;
		this.cost = cost;
		this.poweredSpeed = poweredSpeed;
		this.unpoweredSpeed = unpoweredSpeed;
		this.referenceTool = referenceTool;
	}

	public int getCost() {
		return cost;
	}

	// AxeItem
	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		if (getStoredEnergy(stack) >= cost && isSuitableFor(state)) {
			return poweredSpeed;
		}
		return unpoweredSpeed;
	}

	// MiningToolItem
	@Override
	public boolean isSuitableFor(BlockState state) {
		if (state.isIn(BlockTags.LEAVES)){
			return true;
		}

		return referenceTool.isSuitableFor(state);
	}
	@Override
	public boolean postMine(ItemStack stack, World worldIn, BlockState blockIn, BlockPos pos, LivingEntity entityLiving) {
		tryUseEnergy(stack, cost);
		return true;
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		return true;
	}

	// ToolItem
	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return false;
	}

	// Item
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

	// RcEnergyItem
	@Override
	public long getEnergyCapacity(ItemStack stack) {
		return maxCharge;
	}

	@Override
	public RcEnergyTier getTier() {
		return tier;
	}

	@Override
	public long getEnergyMaxOutput(ItemStack stack) {
		return 0;
	}

	// EnchantmentTargetHandler
	@Override
	public boolean modifyEnchantmentApplication(EnchantmentTarget target) {
		return target == EnchantmentTarget.BREAKABLE;
	}
}
