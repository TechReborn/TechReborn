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

package techreborn.items.armor;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;
import org.jspecify.annotations.Nullable;
import reborncore.common.powerSystem.RcEnergyItem;
import reborncore.common.powerSystem.RcEnergyTier;
import reborncore.common.util.ItemUtils;
import techreborn.init.TRItemSettings;

public abstract class TREnergyArmourItem extends Item implements RcEnergyItem {
	public final long maxCharge;
	private final RcEnergyTier energyTier;

	public TREnergyArmourItem(ArmorMaterial material, ArmorType slot, long maxCharge, RcEnergyTier energyTier, String name) {
		super(TRItemSettings.unbreakable(name).stacksTo(1).humanoidArmor(material, slot));
		this.maxCharge = maxCharge;
		this.energyTier = energyTier;
	}

	// Item
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
		return maxCharge;
	}

	@Override
	public RcEnergyTier getTier() {
		return energyTier;
	}

	@Nullable
	public EquipmentSlot getSlotType() {
		Equippable equippableComponent = this.components().get(DataComponents.EQUIPPABLE);
		return equippableComponent != null ? equippableComponent.slot() : null;
	}
}
