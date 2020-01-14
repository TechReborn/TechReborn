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

package techreborn.items.ingredients;

import com.google.common.base.CaseFormat;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import reborncore.common.util.OreUtil;
import techreborn.utils.TechRebornCreativeTab;
import techreborn.events.TRRecipeHandler;
import techreborn.init.ModItems;
import techreborn.items.ItemTR;

import java.security.InvalidParameterException;

public class ItemPlates extends ItemTR {

	// Vanilla plates or plates not from ingots or gems
	public static String[] types = new String[] {
		"iron", "gold", "carbon", "wood", "redstone", "diamond", "emerald", ModItems.META_PLACEHOLDER, "coal", "obsidian", "lazurite", "silicon"
	};

	public ItemPlates() {
		setTranslationKey("techreborn.plate");
		setHasSubtypes(true);
		setCreativeTab(TechRebornCreativeTab.instance);
		TRRecipeHandler.hideEntry(this);
	}

	public static ItemStack getPlateByName(String name, int count) {
		name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
		for (int i = 0; i < types.length; i++) {
			if (types[i].equalsIgnoreCase(name)) {
				if (types[i].equals(ModItems.META_PLACEHOLDER)) {
					throw new InvalidParameterException("The plate " + name + " could not be found.");
				}
				return new ItemStack(ModItems.PLATES, count, i);
			}
		}
		throw new InvalidParameterException("The plate " + name + " could not be found.");
	}

	public static ItemStack getPlateByName(String name) {
		return getPlateByName(name, 1);
	}

	public static void registerType(String plateType) {
		for (String type : types) {
			if (type.equals(plateType))
				return;
		}
		int plateIndex = types.length;
		String[] newTypes = new String[plateIndex + 1];
		System.arraycopy(types, 0, newTypes, 0, types.length);
		types = newTypes;
		newTypes[plateIndex] = plateType;
		String oreName = "plate" + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, plateType);
		OreUtil.registerOre(oreName, new ItemStack(ModItems.PLATES, 1, plateIndex));
	}

	@Override
	// gets Unlocalized Name depending on meta data
	public String getTranslationKey(ItemStack itemStack) {
		int meta = itemStack.getItemDamage();
		if (meta < 0 || meta >= types.length) {
			meta = 0;
		}

		return super.getTranslationKey() + "." + types[meta];
	}

	// Adds Dusts SubItems To Creative Tab
	@Override
	public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
		if (!isInCreativeTab(creativeTabs)) {
			return;
		}
		for (int meta = 0; meta < types.length; ++meta) {
			if (!types[meta].equals(ModItems.META_PLACEHOLDER)) {
				list.add(new ItemStack(this, 1, meta));
			}
		}
	}
}
