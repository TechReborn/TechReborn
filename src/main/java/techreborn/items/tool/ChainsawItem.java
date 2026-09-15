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

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import techreborn.init.TRItemSettings;

public class ChainsawItem extends Item implements RcEnergyItem {

	public final int maxCharge;
	public final RcEnergyTier tier;
	public final int cost;
	public final float poweredSpeed;
	protected final float unpoweredSpeed = 0.5f;


	public ChainsawItem(ToolMaterial material, int energyCapacity, RcEnergyTier tier, int cost, float poweredSpeed, String name) {
		super(TRItemSettings.unbreakable(name).axe(material, 3f, -2.9f));
		this.maxCharge = energyCapacity;
		this.tier = tier;
		this.cost = cost;
		this.poweredSpeed = poweredSpeed;
	}

	public int getCost() {
		return cost;
	}

	// Item
	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (getStoredEnergy(stack) >= cost && isCorrectToolForDrops(stack, state)) { return poweredSpeed; }
		return unpoweredSpeed;
	}

	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
		if (state.is(BlockTags.LEAVES)){ return true; }
		return super.isCorrectToolForDrops(stack, state);
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level worldIn, BlockState blockIn, BlockPos pos, LivingEntity entityLiving) {
		tryUseEnergy(stack, cost);
		return true;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return ItemUtils.getPowerForDurabilityBar(stack);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) { return true;	}

	@Override
	public int getBarColor(ItemStack stack) {
		return ItemUtils.getColorForDurabilityBar(stack);
	}

	// RcEnergyItem
	@Override
	public long getEnergyCapacity(ItemStack stack) { return maxCharge; }

	@Override
	public RcEnergyTier getTier() {
		return tier;
	}

	@Override
	public long getEnergyMaxOutput(ItemStack stack) {
		return 0;
	}

}
