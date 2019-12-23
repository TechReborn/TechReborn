/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.items.tool.advanced;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.DefaultedList;
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
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;

import java.util.Random;

public class RockCutterItem extends PickaxeItem implements EnergyHolder, ItemDurabilityExtensions {

	public static final int maxCharge = TechRebornConfig.rockCutterCharge;
	public int transferLimit = 1_000;
	public int cost = 500;

	// 400k FE with 1k FE\t charge rate
	public RockCutterItem() {
		super(ToolMaterials.DIAMOND, 1, 1, new Item.Settings().group(TechReborn.ITEMGROUP).maxCount(1));
	}

	// PickaxeItem
	@Override
	public boolean isEffectiveOn(BlockState state) {
		if (Items.DIAMOND_PICKAXE.isEffectiveOn(state)) {
			return true;
		}
		return false;
	}

	@Override
	public float getMiningSpeed(ItemStack stack, BlockState state) {
		if (Energy.of(stack).getEnergy() < cost) {
			return 2F;
		} else {
			return Items.DIAMOND_PICKAXE.getMiningSpeed(stack, state);
		}
	}

	// MiningToolItem
	@Override
	public boolean postMine(ItemStack stack, World worldIn, BlockState blockIn, BlockPos pos, LivingEntity entityLiving) {
		Random rand = new Random();
		if (rand.nextInt(EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack) + 1) == 0) {
			Energy.of(stack).use(cost);
		}
		return true;
	}

	@Override
	public boolean postHit(ItemStack itemstack, LivingEntity entityliving, LivingEntity entityliving1) {
		return true;
	}

	// ToolItem
	@Override
	public boolean canRepair(ItemStack stack, ItemStack stack2) {
		return false;
	}

	// Item
	@Override
	public void onCraft(ItemStack stack, World worldIn, PlayerEntity playerIn) {
		if (!stack.hasEnchantments()) {
			stack.addEnchantment(Enchantments.SILK_TOUCH, 1);
		}
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void appendStacks(ItemGroup par2ItemGroup, DefaultedList<ItemStack> itemList) {
		if (!isIn(par2ItemGroup)) {
			return;
		}
		ItemStack uncharged = new ItemStack(this);
		uncharged.addEnchantment(Enchantments.SILK_TOUCH, 1);
		ItemStack charged = new ItemStack(TRContent.ROCK_CUTTER);
		charged.addEnchantment(Enchantments.SILK_TOUCH, 1);
		Energy.of(charged).set(Energy.of(charged).getMaxStored());

		itemList.add(uncharged);
		itemList.add(charged);
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
		return EnergyTier.HIGH;
	}

	@Override
	public double getMaxInput(EnergySide side) {
		return transferLimit;
	}

	@Override
	public double getMaxOutput(EnergySide side) {
		return 0;
	}
}
