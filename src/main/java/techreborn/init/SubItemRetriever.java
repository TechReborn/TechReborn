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

import net.minecraft.item.ItemStack;
import techreborn.api.ISubItemRetriever;
import techreborn.blocks.BlockOre;
import techreborn.blocks.BlockStorage;
import techreborn.items.*;

/**
 * Created by Mark on 03/04/2016.
 */
public class SubItemRetriever implements ISubItemRetriever {
	@Override
	public ItemStack getCellByName(String name) {
		return ItemCells.getCellByName(name);
	}

	@Override
	public ItemStack getCellByName(String name, int count) {
		return ItemCells.getCellByName(name, count);
	}

	@Override
	public ItemStack getDustByName(String name) {
		return ItemDusts.getDustByName(name);
	}

	@Override
	public ItemStack getDustByName(String name, int count) {
		return ItemDusts.getDustByName(name, count);
	}

	@Override
	public ItemStack getSmallDustByName(String name) {
		return ItemDustsSmall.getSmallDustByName(name);
	}

	@Override
	public ItemStack getSmallDustByName(String name, int count) {
		return ItemDustsSmall.getSmallDustByName(name, count);
	}

	@Override
	public ItemStack getGemByName(String name) {
		// TODO: Fix recipe
		//return ItemGems.getGemByName(name);
		return null;
	}

	@Override
	public ItemStack getGemByName(String name, int count) {
		// TODO: Fix recipe
		//return ItemGems.getGemByName(name, count);
		return null;
	}

	@Override
	public ItemStack getIngotByName(String name) {
		//return ItemIngots.getIngotByName(name);
		return null;
	}

	@Override
	public ItemStack getIngotByName(String name, int count) {
		//return ItemIngots.getIngotByName(name, count);
		return null;
	}


	@Override
	public ItemStack getNuggetByName(String name) {
//		TODO: fix recipe			
//		return ItemNuggets.getNuggetByName(name);
		return null;
	}

	@Override
	public ItemStack getNuggetByName(String name, int count) {
//		TODO: fix recipe			
//		return ItemNuggets.getNuggetByName(name, count);
		return null;
	}

	@Override
	public ItemStack getPartByName(String name) {
		return ItemParts.getPartByName(name);
	}

	@Override
	public ItemStack getPartByName(String name, int count) {
		return ItemParts.getPartByName(name, count);
	}

	@Override
	public ItemStack getPlateByName(String name) {
//		TODO: fix recipe		
//		return ItemPlates.getPlateByName(name);
		return null;
	}

	@Override
	public ItemStack getPlateByName(String name, int count) {
//		TODO: fix recipe			
//		return ItemPlates.getPlateByName(name, count);
		return null;
	}

	@Override
	public ItemStack getUpgradeByName(String name) {
		return ItemUpgrades.getUpgradeByName(name);
	}

	@Override
	public ItemStack getUpgradeByName(String name, int count) {
		return ItemUpgrades.getUpgradeByName(name, count);
	}

	@Override
	public ItemStack getOreByName(String name) {
		return BlockOre.getOreByName(name);
	}

	@Override
	public ItemStack getOreByName(String name, int count) {
		return BlockOre.getOreByName(name, count);
	}

	@Override
	public ItemStack getStorageBlockByName(String name) {
		return BlockStorage.getStorageBlockByName(name);
	}

	@Override
	public ItemStack getStorageBlockByName(String name, int count) {
		return BlockStorage.getStorageBlockByName(name, count);
	}
}
