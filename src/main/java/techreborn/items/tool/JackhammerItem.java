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
import net.minecraft.block.Material;
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
import techreborn.utils.ToolsUtil;

import java.util.Random;

public class JackhammerItem extends PickaxeItem implements EnergyHolder, ItemDurabilityExtensions, DynamicAttributeTool {

	public final int maxCharge;
	public final EnergyTier tier;
	public final int cost;
	protected final float unpoweredSpeed = 0.5F;

	public JackhammerItem(int energyCapacity, EnergyTier tier, int cost) {
		super(ToolMaterials.DIAMOND, (int) ToolMaterials.DIAMOND.getAttackDamage(), 1F, new Item.Settings().group(TechReborn.ITEMGROUP).maxCount(1).maxDamage(-1));
		this.maxCharge = energyCapacity;
		this.tier = tier;
		this.cost = cost;
	}

	// PickaxeItem
	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		if (Energy.of(stack).getEnergy() < cost) return unpoweredSpeed;
		if (ToolsUtil.JackHammerSkippedBlocks(state)) return unpoweredSpeed;

		if (state.getMaterial() == Material.STONE) {
			return miningSpeed;
		} else {
			return unpoweredSpeed;
		}
	}

/*
	Fabric API doesn't allow to have mining speed less then the one from vanilla ToolMaterials
	@Override
	public float getMiningSpeedMultiplier(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		if (tag.equals(FabricToolTags.PICKAXES) && stack.getItem().isEffectiveOn(state)) {
			if (Energy.of(stack).getEnergy() >= cost) {
				return miningSpeed;
			}
		}
		return 0.5F;
	}*/


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
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if (!isIn(group)) {
			return;
		}
		InitUtils.initPoweredItems(this, stacks);
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

	// DynamicAttributeTool
	@Override
	public int getMiningLevel(Tag<Item> tag, BlockState state, ItemStack stack, LivingEntity user) {
		if (tag.equals(FabricToolTags.PICKAXES)) {
			return MiningLevel.IRON.intLevel;
		}
		return 0;
	}
}
