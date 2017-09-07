/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.events.TRRecipeHandler;

/**
 * Created by modmuss50 on 26/02/2016.
 */
public class ItemTRArmour extends ItemArmor {

	public ItemTRArmour(ArmorMaterial material, EntityEquipmentSlot slot) {
		super(material, material.getDamageReductionAmount(slot), slot);
		if (slot == EntityEquipmentSlot.HEAD)
			setUnlocalizedName(material.name().toLowerCase() + "Helmet");
		if (slot == EntityEquipmentSlot.CHEST)
			setUnlocalizedName(material.name().toLowerCase() + "Chestplate");
		if (slot == EntityEquipmentSlot.LEGS)
			setUnlocalizedName(material.name().toLowerCase() + "Leggings");
		if (slot == EntityEquipmentSlot.FEET)
			setUnlocalizedName(material.name().toLowerCase() + "Boots");
		setCreativeTab(TechRebornCreativeTabMisc.instance);
		this.material = material;
		this.slot = slot;
		TRRecipeHandler.hideEntry(this);
	}
}
