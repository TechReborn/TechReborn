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
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import techreborn.events.TRRecipeHandler;
import techreborn.init.ModItems;
import techreborn.items.ItemTR;

import java.security.InvalidParameterException;

public class ItemIngots extends ItemTR {
	public static final String[] types = new String[] { "aluminum", "brass", "bronze", "chrome", "copper", "electrum",
		"invar", "iridium", "lead", "nickel", "platinum", "silver", "steel", "tin", "titanium", "tungsten",
		"hot_tungstensteel", "tungstensteel", "zinc", "refined_iron", "advanced_alloy", "mixed_metal",
		"iridium_alloy", "thorium", "uranium", "plutonium" };

	public ItemIngots() {
		setHasSubtypes(true);
		setTranslationKey("techreborn.ingot");
		TRRecipeHandler.hideEntry(this);
	}

	public static ItemStack getIngotByName(String name, int count) {
		name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
		for (int i = 0; i < types.length; i++) {
			if (types[i].equalsIgnoreCase(name)) {
				return new ItemStack(ModItems.INGOTS, count, i);
			}
		}
		if (name.equalsIgnoreCase("iron")) {
			return new ItemStack(Items.IRON_INGOT);
		}
		if (name.equalsIgnoreCase("gold")) {
			return new ItemStack(Items.GOLD_INGOT);
		}
		throw new InvalidParameterException("The ingot " + name + " could not be found.");
	}

	public static ItemStack getIngotByName(String name) {
		return getIngotByName(name, 1);
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
