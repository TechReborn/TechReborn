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

package techreborn.items.tool;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.api.power.ItemPowerManager;
import reborncore.common.powerSystem.ExternalPowerSystems;
import reborncore.common.powerSystem.PowerSystem;
import reborncore.common.util.ItemDurabilityExtensions;
import reborncore.common.util.ItemUtils;
import team.reborn.energy.EnergyHolder;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyTier;
import techreborn.TechReborn;

import java.util.Random;

public class ItemDrill extends PickaxeItem implements EnergyHolder, ItemDurabilityExtensions {

	public int maxCharge = 1;
	public int cost = 250;
	public float unpoweredSpeed = 2.0F;
	public int transferLimit = 100;

	public ItemDrill(ToolMaterial material, int energyCapacity, float unpoweredSpeed, float efficiencyOnProperMaterial) {
		super(material, (int) material.getAttackDamage(), unpoweredSpeed, new Item.Settings().group(TechReborn.ITEMGROUP).maxCount(1));
		this.maxCharge = energyCapacity;
		this.unpoweredSpeed = unpoweredSpeed;
	}

	// ItemPickaxe
	@Override
	public float getMiningSpeed(ItemStack stack, BlockState state) {
		if (new ItemPowerManager(stack).getEnergyStored() < cost) {
			return unpoweredSpeed;
		}
		if (Items.WOODEN_PICKAXE.getMiningSpeed(stack, state) > 1.0F
			|| Items.WOODEN_SHOVEL.getMiningSpeed(stack, state) > 1.0F) {
			return miningSpeed;
		} else {
			return super.getMiningSpeed(stack, state);
		}
	}

	// ItemTool
	@Override
	public boolean postMine(ItemStack stack, World worldIn, BlockState blockIn, BlockPos pos, LivingEntity entityLiving) {
		Random rand = new Random();
		if (rand.nextInt(EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack) + 1) == 0) {
			ItemPowerManager capEnergy = new ItemPowerManager(stack);

			capEnergy.useEnergy(cost, false);
			ExternalPowerSystems.requestEnergyFromArmor(capEnergy, entityLiving);
		}
		return true;
	}

	@Override
	public boolean postHit(ItemStack itemstack, LivingEntity entityliving, LivingEntity entityliving1) {
		return true;
	}

	//Item
	@Override
	public boolean canRepair(ItemStack itemStack_1, ItemStack itemStack_2) {
		return false;
	}

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

	// IEnergyItemInfo
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
