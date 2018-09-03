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

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import techreborn.api.recipe.IRecipeCompact;

public final class TechRebornAPI {

	/**
	 * Use this to get the instance of IRecipeCompat
	 */
	public static IRecipeCompact recipeCompact;

	public static ISubItemRetriever subItemRetriever;

	public static void addRollingOreMachinceRecipe(ResourceLocation resourceLocation, ItemStack output, Object... components) {
		RollingMachineRecipe.instance.addShapedOreRecipe(resourceLocation, output, components);
	}

	public static void addShapelessOreRollingMachinceRecipe(ResourceLocation resourceLocation, ItemStack output, Object... components) {
		RollingMachineRecipe.instance.addShapelessOreRecipe(resourceLocation, output, components);
	}

	public static void addRollingMachinceRecipe(ResourceLocation resourceLocation, ItemStack output, Object... components) {
		RollingMachineRecipe.instance.addRecipe(resourceLocation, output, components);
	}

	public static void addShapelessRollingMachinceRecipe(ResourceLocation resourceLocation, ItemStack output, Object... components) {
		RollingMachineRecipe.instance.addShapelessRecipe(resourceLocation, output, components);
	}

	/**
	 * Use this to get an item from Tech Reborn, @see <a href=
	 * "https://github.com/TechReborn/TechReborn/blob/1.9/src/main/java/techreborn/init/ModItems.java">
	 * ModItems.java</a> for the full list
	 *
	 * @param name
	 * @return
	 */
	public static Item getItem(String name) {
		try {
			Object e = Class.forName("techreborn.init.TRItems").getField(name).get(null);
			return e instanceof Item ? (Item) e : null;
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Use this to get an ingredient item from Tech Reborn, @see <a href=
	 * "https://github.com/TechReborn/TechReborn/blob/1.9/src/main/java/techreborn/init/ModItems.java">
	 * ModItems.java</a> for the full list
	 *
	 * @param name
	 * @return
	 */
	public static Item getIngredient(String name) {
		try {
			Object e = Class.forName("techreborn.init.TRIngredient").getField(name).get(null);
			return e instanceof Item ? (Item) e : null;
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Use this to get an block from techrebonrn, @see <a href=
	 * "https://github.com/TechReborn/TechReborn/blob/1.9/src/main/java/techreborn/init/ModBlocks.java">
	 * ModBlocks.java</a> for the full list
	 *
	 * @param name
	 * @return
	 */
	public static Block getBlock(String name) {
		try {
			Object e = Class.forName("techreborn.init.ModBlocks").getField(name).get(null);
			return e instanceof Block ? (Block) e : null;
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

}
