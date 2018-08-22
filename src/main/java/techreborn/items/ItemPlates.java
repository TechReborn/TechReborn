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

package techreborn.items;

import techreborn.events.TRRecipeHandler;

public class ItemPlates extends ItemTR {

	public ItemPlates() {
		TRRecipeHandler.hideEntry(this);
	}

//	public static ItemStack getPlateByName(String name, int count) {
//		name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
//		for (int i = 0; i < types.length; i++) {
//			if (types[i].equalsIgnoreCase(name)) {
//				return new ItemStack(ModItems.PLATES, count, i);
//			}
//		}
//		throw new InvalidParameterException("The plate " + name + " could not be found.");
//	}
//
//	public static ItemStack getPlateByName(String name) {
//		return getPlateByName(name, 1);
//	}
//
//	public static void registerType(String plateType) {
//		for (String type : types) {
//			if (type.equals(plateType))
//				return;
//		}
//		int plateIndex = types.length;
//		String[] newTypes = new String[plateIndex + 1];
//		System.arraycopy(types, 0, newTypes, 0, types.length);
//		types = newTypes;
//		newTypes[plateIndex] = plateType;
//		String oreName = "plate" + CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, plateType);
//		OreUtil.registerOre(oreName, new ItemStack(ModItems.PLATES, 1, plateIndex));
//	}
}
