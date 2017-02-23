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

package techreborn.api;

import net.minecraft.item.ItemStack;

public interface ISubItemRetriever {

	ItemStack getCellByName(String name);

	ItemStack getCellByName(String name, int count);

	ItemStack getCellByName(String name, int count, boolean lookForIC2);

	ItemStack getDustByName(String name);

	ItemStack getDustByName(String name, int count);

	ItemStack getSmallDustByName(String name);

	ItemStack getSmallDustByName(String name, int count);

	ItemStack getGemByName(String name);

	ItemStack getGemByName(String name, int count);

	ItemStack getIngotByName(String name);

	ItemStack getIngotByName(String name, int count);

	ItemStack getNuggetByName(String name);

	ItemStack getNuggetByName(String name, int count);

	ItemStack getPartByName(String name);

	ItemStack getPartByName(String name, int count);

	ItemStack getPlateByName(String name);

	ItemStack getPlateByName(String name, int count);

	ItemStack getUpgradeByName(String name);

	ItemStack getUpgradeByName(String name, int count);

	ItemStack getOreByName(String name);

	ItemStack getOreByName(String name, int count);

	ItemStack getStorageBlockByName(String name);

	ItemStack getStorageBlockByName(String name, int count);

}
