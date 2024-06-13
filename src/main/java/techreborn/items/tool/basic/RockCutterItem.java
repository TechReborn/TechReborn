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

package techreborn.items.tool.basic;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRToolMaterials;

public class RockCutterItem extends PickaxeItem implements RcEnergyItem {
	// 10k Energy with 128 E\t charge rate
	public RockCutterItem() {
		// combat stats same as for diamond pickaxe. Fix for #2468
		super(TRToolMaterials.ROCK_CUTTER, new Item.Settings()
			.maxDamage(0)
			.attributeModifiers(PickaxeItem.createAttributeModifiers(TRToolMaterials.ROCK_CUTTER, 1.0f, -2.8f)
		));
	}

	// PickaxeItem
	@Override
	public boolean isCorrectForDrops(ItemStack stack, BlockState state) {
		return Items.DIAMOND_PICKAXE.isCorrectForDrops(stack, state);
	}

	@Override
	public float getMiningSpeed(ItemStack stack, BlockState state) {
		if (getStoredEnergy(stack) < TechRebornConfig.rockCutterCost) {
			return 1.0f;
		} else {
			return Items.DIAMOND_PICKAXE.getMiningSpeed(stack, state);
		}
	}

	// MiningToolItem
	@Override
	public boolean postMine(ItemStack stack, World worldIn, BlockState blockIn, BlockPos pos, LivingEntity entityLiving) {
		tryUseEnergy(stack, TechRebornConfig.rockCutterCost);
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
	public void onCraft(ItemStack stack, World world) {
		if (!stack.hasEnchantments()) {
			RegistryWrapper.Impl<Enchantment> registry = world.getRegistryManager().getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
			stack.addEnchantment(registry.getOrThrow(Enchantments.SILK_TOUCH), 1);
		}

		super.onCraft(stack, world);
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
		return TechRebornConfig.rockCutterCharge;
	}

	@Override
	public RcEnergyTier getTier() {
		return RcEnergyTier.MEDIUM;
	}

	@Override
	public long getEnergyMaxOutput(ItemStack stack) {
		return 0;
	}

}
