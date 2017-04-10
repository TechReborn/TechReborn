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

package techreborn.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import reborncore.api.tile.IUpgrade;
import reborncore.common.recipes.RecipeCrafter;
import reborncore.common.tile.TileLegacyMachineBase;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.init.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.security.InvalidParameterException;
import java.util.List;

public class ItemUpgrades extends ItemTRNoDestroy implements IUpgrade {

	public static final String[] types = new String[] { "overclock", "transformer", "energy_storage", "range" };

	public ItemUpgrades() {
		setUnlocalizedName("techreborn.upgrade");
		setHasSubtypes(true);
		setCreativeTab(TechRebornCreativeTabMisc.instance);
	}

	public static ItemStack getUpgradeByName(String name, int count) {
		for (int i = 0; i < types.length; i++) {
			if (types[i].equalsIgnoreCase(name)) {
				return new ItemStack(ModItems.UPGRADES, count, i);
			}
		}
		throw new InvalidParameterException("The upgrade " + name + " could not be found.");
	}

	public static ItemStack getUpgradeByName(String name) {
		return getUpgradeByName(name, 1);
	}

	@Override
	// gets Unlocalized Name depending on meta data
	public String getUnlocalizedName(ItemStack itemStack) {
		int meta = itemStack.getItemDamage();
		if (meta < 0 || meta >= types.length) {
			meta = 0;
		}

		return super.getUnlocalizedName() + "." + types[meta];
	}

	// Adds Dusts SubItems To Creative Tab
	@Override
	public void getSubItems(Item item, CreativeTabs creativeTabs, NonNullList list) {
		for (int meta = 0; meta < types.length; ++meta) {
			list.add(new ItemStack(item, 1, meta));
		}
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(TextFormatting.RED + I18n.translateToLocal("tooltip.wip"));
		tooltip.add(TextFormatting.RED + I18n.translateToLocal("tooltip.upBroken"));
		tooltip.add(TextFormatting.RED + I18n.translateToLocal("tooltip.ingredient"));
	}

	@Override
	public void process(
		@Nonnull
			TileLegacyMachineBase machineBase,
		@Nullable
			RecipeCrafter crafter,
		@Nonnull
			ItemStack stack) {
		if(crafter != null){
			if (stack.getItemDamage() == 0) {
				crafter.addSpeedMulti(0.2);
				crafter.addPowerMulti(0.5);
			}
		}
	}
}