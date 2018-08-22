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
import net.minecraft.util.ResourceLocation;
import techreborn.lib.ModInfo;
import techreborn.items.ItemPlates;


/**
 * @author drcrazy
 *
 */
public enum ModPlates implements IStringSerializable {
	ADVANCED_ALLOY_PLATE("plateAdvancedAlloy"),
	ALUMINUM_PLATE("plateAluminum"),
//	BRASS_PLATE("plateBrass"),
//	BRONZE_PLATE("plateBronze"),
//	CARBON_PLATE("plateCarbon"),
//	COAL_PLATE("plateCoal"),
//	COPPER_PLATE("plateCopper"),
//	DIAMOND_PLATE("plateDiamond"),
//	
//	IRON_PLATE("plateIron"),
//	GOLD_PLATE("plateGold"),
//	
//	WOOD_PLATE("plateWood"),
//	REDSTONE_PLATE("plateRedstone"),
//	
//	EMERALD_PLATE("plateEmerald"),
//	
//	OBSIDIAN_PLATE("plateObsidian"),
	LAZURITE_PLATE("plateLazurite");
	
	public final ResourceLocation name;
	public final Item item;
	
	private ModPlates(String name) {
		this.name = new ResourceLocation(ModInfo.MOD_ID, name);
		this.item = new ItemPlates(this.name);
	}

	@Override
	public String getName() {
		return name.getPath();
	}
}
