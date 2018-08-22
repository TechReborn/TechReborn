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

package techreborn.init;

import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import techreborn.lib.ModInfo;
import techreborn.items.ItemPlates;


/**
 * @author drcrazy
 *
 */
public enum ModPlates implements IStringSerializable {
	ADVANCED_ALLOY_PLATE("plateAdvancedAlloy"),
	ALUMINUM_PLATE("plateAluminum"),
	BRASS_PLATE("plateBrass"),
	BRONZE_PLATE("plateBronze"),
	CARBON_PLATE("plateCarbon"),
	COAL_PLATE("plateCoal"),
	COPPER_PLATE("plateCopper"),
	DIAMOND_PLATE("plateDiamond"),
	ELECTRUM_PLATE("plateElectrum"),
	EMERALD_PLATE("plateEmerald"),
	GOLD_PLATE("plateGold"),
	INVAR_PLATE("plateInvar"),
	IRIDIUM_ALLOY_PLATE("plateIridiumAlloy"),
	IRIDIUM_PLATE("plateIridium"),
	IRON_PLATE("plateIron"),
	LAPIS_PLATE("plateLapis"),
	LAZURITE_PLATE("plateLazurite"),
	LEAD_PLATE("plateLead"),
	MAGNALIUM_PLATE("plateMagnalium"),
	NICKEL_PLATE("plateNickel"),
	OBSIDIAN_PLATE("plateObsidian"),
	PERIDOT_PLATE("platePeridot"),
	PLATINUM_PLATE("platePlatinum"),
	RED_GARNET_PLATE("plateRedGarnet"),
	REDSTONE_PLATE("plateRedstone"),
	REFINED_IRON_PLATE("plateRefinedIron"),
	RUBY_PLATE("plateRuby"),
	SAPPHIRE_PLATE("plateSapphire"),
	SILICON_PLATE("plateSilicon"),
	SILVER_PLATE("plateSilver"),
	STEEL_PLATE("plateSteel"),
	TIN_PLATE("plateTin"),
	TITANIUM_PLATE("plateTitanium"),
	TUNGSTEN_PLATE("plateTungsten"),
	TUNGSTENSTEEL_PLATE("plateTungstensteel"),
	WOOD_PLATE("plateWood"),
	YELLOW_GARNET_PLATE("plateYellowGarnet"),
	ZINC_PLATE("plateZinc");
	
	public final String name;
	public final Item item;
	
	private ModPlates(String name) {
		this.name = name;
		this.item = new ItemPlates();
		// ModItems will take care about setRegistryName
		//this.item.setRegistryName(new ResourceLocation(ModInfo.MOD_ID, name));
		this.item.setTranslationKey(ModInfo.MOD_ID + "." + name);
	}

	@Override
	public String getName() {
		return name;
	}
}
