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

import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.tag.Tag;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.ItemDurabilityExtensions;
import reborncore.common.util.ItemUtils;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyTier;
import techreborn.TechReborn;
import techreborn.utils.InitUtils;

public class DrillItem extends PickaxeItem implements EnergyHolder, ItemDurabilityExtensions, DynamicAttributeTool {

	public final int maxCharge;
	public final int cost;
	public final float poweredSpeed;
	public final float unpoweredSpeed;
	public final int miningLevel;
	public final EnergyTier tier;

	public DrillItem(ToolMaterials material, int energyCapacity, EnergyTier tier, int cost, float poweredSpeed, float unpoweredSpeed, MiningLevel miningLevel) {
		super(material, (int) material.getAttackDamage(), unpoweredSpeed, new Item.Settings().group(TechReborn.ITEMGROUP).maxCount(1).maxDamage(-1));
		this.maxCharge = energyCapacity;
		this.cost = cost;
		this.poweredSpeed = poweredSpeed;
		this.unpoweredSpeed = unpoweredSpeed;
		this.miningLevel = miningLevel.intLevel;
		this.tier = tier;
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		// Going to remain to use this ol reliable function, the fabric one is funky

		if (Energy.of(stack).getEnergy() >= cost) {
			if (stack.getItem().isEffectiveOn(state)) {
				return poweredSpeed;
			} else {
				return Math.min(unpoweredSpeed * 10f, poweredSpeed); // Still be faster than unpowered when not effective
			}
		}

		return unpoweredSpeed;
	}

	// PickaxeItem
	@Override
	public boolean isEffectiveOn(BlockState blockIn) {
		if(blockIn == null){
			return false;
		}

		if (Items.DIAMOND_PICKAXE.isEffectiveOn(blockIn)) {
			return true;
		}
		if (Items.DIAMOND_SHOVEL.isEffectiveOn(blockIn)) {
			return true;
		}
		// More checks to fix #2225
		// Pass stack to fix #2348
		if (Items.DIAMOND_SHOVEL.getMiningSpeedMultiplier(new ItemStack(Items.DIAMOND_SHOVEL), blockIn) > 1.0f) {
			return true;
		}
		return Items.DIAMOND_PICKAXE.getMiningSpeedMultiplier(new ItemStack(Items.DIAMOND_SHOVEL), blockIn) > 1.0f;
	}

	// MiningToolItem
	@Override
	public boolean postMine(ItemStack stack, World worldIn, BlockState blockIn, BlockPos pos, LivingEntity entityLiving) {
		if (worldIn.random.nextInt(EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack) + 1) == 0) {
			Energy.of(stack).use(cost);
		}
		return true;
	}

	// ToolItem
	@Override
	public boolean canRepair(ItemStack itemStack_1, ItemStack itemStack_2) {
		return false;
	}

	//Item
	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if (!isIn(group)) {
			return;
		}
		InitUtils.initPoweredItems(this, stacks);
	}

	// ItemDurabilityExtensions
	@Override
	public double getDurability(ItemStack stack) {
		return 1 - ItemUtils.getPowerForDurabilityBar(stack);
	}

	@Override
	public boolean showDurability(ItemStack stack) {
		return true;
	}

	@Override
	public int getDurabilityColor(ItemStack stack) {
		return PowerSystem.getDisplayPower().colour;
	}

	// EnergyHolder
	@Override
	public double getMaxStoredPower() {
		return maxCharge;
	}

	@Override
	public EnergyTier getTier() {
		return tier;
	}

	@Override
	public double getMaxOutput(EnergySide side) {
		return 0;
	}

	// DynamicAttributeTool
	@Override
	public int getMiningLevel(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		if (tag.equals(FabricToolTags.PICKAXES) || tag.equals(FabricToolTags.SHOVELS)) {
			return miningLevel;
		}
		return 0;
	}
}
