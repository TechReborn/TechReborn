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

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRItemSettings;
import techreborn.init.TRToolMaterials;

public class RockCutterItem extends Item implements RcEnergyItem {
	// 10k Energy with 128 E\t charge rate
	public RockCutterItem(String name) {
		// combat stats same as for diamond pickaxe. Fix for #2468
		super(TRItemSettings.unbreakable(name).pickaxe(TRToolMaterials.ROCK_CUTTER,  1f, -2.8f));
	}

	// PickaxeItem
	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
		return Items.DIAMOND_PICKAXE.isCorrectToolForDrops(stack, state);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (getStoredEnergy(stack) < TechRebornConfig.rockCutterCost) {
			return 1.0f;
		} else {
			return Items.DIAMOND_PICKAXE.getDestroySpeed(stack, state);
		}
	}

	// MiningToolItem
	@Override
	public boolean mineBlock(ItemStack stack, Level worldIn, BlockState blockIn, BlockPos pos, LivingEntity entityLiving) {
		tryUseEnergy(stack, TechRebornConfig.rockCutterCost);
		return true;
	}

	// Item
	@Override
	public void onCraftedPostProcess(ItemStack stack, Level world) {
		if (!stack.isEnchanted()) {
			HolderLookup.RegistryLookup<Enchantment> registry = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
			stack.enchant(registry.getOrThrow(Enchantments.SILK_TOUCH), 1);
		}

		super.onCraftedPostProcess(stack, world);
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
