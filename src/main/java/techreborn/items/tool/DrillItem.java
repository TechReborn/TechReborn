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
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import techreborn.TechReborn;
import techreborn.utils.InitUtils;

public class DrillItem extends PickaxeItem implements RcEnergyItem, DynamicAttributeTool {

	public final int maxCharge;
	public final int cost;
	public final float poweredSpeed;
	public final float unpoweredSpeed;
	public final int miningLevel;
	public final RcEnergyTier tier;

	public DrillItem(ToolMaterial material, int energyCapacity, RcEnergyTier tier, int cost, float poweredSpeed, float unpoweredSpeed, MiningLevel miningLevel) {
		// combat stats same as for diamond pickaxe. Fix for #2468
		super(material, 1, -2.8F, new Item.Settings().group(TechReborn.ITEMGROUP).maxCount(1).maxDamage(-1));
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

		if (getStoredEnergy(stack) >= cost) {
			if (stack.getItem().isSuitableFor(state)) {
				return poweredSpeed;
			} else {
				return Math.min(unpoweredSpeed * 10f, poweredSpeed); // Still be faster than unpowered when not effective
			}
		}

		return unpoweredSpeed;
	}

	// PickaxeItem
	@Override
	public boolean isSuitableFor(BlockState blockIn) {
		if(blockIn == null){
			return false;
		}

		if (Items.DIAMOND_PICKAXE.isSuitableFor(blockIn)) {
			return true;
		}
		if (Items.DIAMOND_SHOVEL.isSuitableFor(blockIn)) {
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
			tryUseEnergy(stack, cost);
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
	public RcEnergyTier getTier() {
		return tier;
	}

	@Override
	public long getEnergyMaxOutput() {
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
