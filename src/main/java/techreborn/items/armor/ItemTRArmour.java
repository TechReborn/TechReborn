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

package techreborn.items.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import reborncore.common.util.ItemUtils;
import techreborn.events.TRRecipeHandler;
import techreborn.utils.TechRebornCreativeTab;

/**
 * Created by modmuss50 on 26/02/2016.
 */
public class ItemTRArmour extends ItemArmor {

	String repairOreDict = "";

	public ItemTRArmour(ArmorMaterial material, EntityEquipmentSlot slot) {
		this(material, slot, "");
	}

	public ItemTRArmour(ArmorMaterial material, EntityEquipmentSlot slot, String repairOreDict) {
		super(material, material.getDamageReductionAmount(slot), slot);
		this.repairOreDict = repairOreDict;
		if (slot == EntityEquipmentSlot.HEAD)
			setUnlocalizedName(material.name().toLowerCase() + "Helmet");
		if (slot == EntityEquipmentSlot.CHEST)
			setUnlocalizedName(material.name().toLowerCase() + "Chestplate");
		if (slot == EntityEquipmentSlot.LEGS)
			setUnlocalizedName(material.name().toLowerCase() + "Leggings");
		if (slot == EntityEquipmentSlot.FEET)
			setUnlocalizedName(material.name().toLowerCase() + "Boots");
		setCreativeTab(TechRebornCreativeTab.instance);
		TRRecipeHandler.hideEntry(this);
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		if (toRepair.getItem() == this && !repairOreDict.isEmpty()) {
			return ItemUtils.isInputEqual(repairOreDict, repair, false, false, true);
		}
		return super.getIsRepairable(toRepair, repair);
	}
}
