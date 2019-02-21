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

package techreborn.api;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;


public class RollingMachineRecipe {

	public static final RollingMachineRecipe instance = new RollingMachineRecipe();

	public void addShapedOreRecipe(ResourceLocation resourceLocation, ItemStack outputItemStack, Object... objectInputs) {
		throw new UnsupportedOperationException("Needs moving to json");
	}

	public void addShapelessOreRecipe(ResourceLocation resourceLocation, ItemStack outputItemStack, Object... objectInputs) {
		throw new UnsupportedOperationException("Needs moving to json");
	}

	public ItemStack findMatchingRecipeOutput(InventoryCrafting inv, World world) {
		throw new UnsupportedOperationException("Needs moving to json");
	}

	public IRecipe findMatchingRecipe(InventoryCrafting inv, World world) {
		throw new UnsupportedOperationException("Needs moving to json");
	}

}
