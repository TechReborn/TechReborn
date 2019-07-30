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

package techreborn.init.recipes;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import reborncore.common.util.StringUtils;
import techreborn.items.ItemDynamicCell;

/**
 * Created by Prospector
 */
public abstract class RecipeMethods {
	public static ItemStack getMaterial(String name, int count, Type type) {
		name = name.toLowerCase();
		if(type == Type.CELL){

			Fluid fluid = Registry.FLUID.get(new Identifier("techreborn", name.toLowerCase()));
			if(name.equals("water")){
				fluid = Fluids.WATER;
			}
			if(name.equals("lava")){
				fluid = Fluids.LAVA;
			}

			if(fluid == Fluids.EMPTY){
				throw new RuntimeException("could not find fluid " + name);
			}
			return ItemDynamicCell.getCellWithFluid(fluid, count);
		}

		if(type == Type.INGOT){
			return find(name + "_ingot", count);
		}

		if(type == Type.DUST){
			return find(name + "_dust", count);
		}

		if(type == Type.PLATE){
			return find(name + "_plate", count);
		}

		if(type == Type.NUGGET){
			return find(name + "_nugget", count);
		}

		if(type == Type.SMALL_DUST){
			return find(name + "_small_dust", count);
		}

		if(type == Type.ORE){
			return find(name + "_ore", count);
		}

		if(type == Type.PART){
			return find(name, count);
		}

		throw new UnsupportedOperationException(type.name());
	}

	private static ItemStack find(String path, int count){
		Identifier identifier = new Identifier(path);
		Item item = Registry.ITEM.get(identifier);
		if(item != Items.AIR){
			return new ItemStack(item, count);
		}

		identifier = new Identifier("techreborn", path);
		item = Registry.ITEM.get(identifier);
		if(item != Items.AIR){
			return new ItemStack(item, count);
		}

		throw new UnsupportedOperationException("Could not find" + path);
	}

	static Object getMaterialObjectFromType(String name, Type type) {
		Object object = null;
		if (type == Type.DUST) {
			object = "dust" + StringUtils.toFirstCapital(name);
		} else if (type == Type.SMALL_DUST) {
			object = "smallDust" + StringUtils.toFirstCapital(name);
		} else if (type == Type.INGOT) {
			object = "ingot" + StringUtils.toFirstCapital(name);
		} else if (type == Type.GEM) {
			object = "gem" + StringUtils.toFirstCapital(name);
		} else if (type == Type.PLATE) {
			object = "plate" + StringUtils.toFirstCapital(name);
		} else if (type == Type.NUGGET) {
			object = "nugget" + StringUtils.toFirstCapital(name);
		}else if (type == Type.ORE) {
			object = "ore" + StringUtils.toFirstCapital(name);
		}
		if (object != null) {
			if (object instanceof String) {
				throw new UnsupportedOperationException("Use tags");
			} else {
				return object;
			}
		}
		return getMaterial(name, type);
	}

	public static ItemStack getMaterial(String name, Type type) {
		return getMaterial(name, 1, type);
	}

	@Deprecated
	public static ItemStack getOre(String name, int count) {
		if(name.equals("dustRedstone")){
			return new ItemStack(Items.REDSTONE, count);
		}
		if(name.startsWith("dust")){
			return getMaterial(name.replace("dust", ""), count, Type.DUST);
		}
		return getMaterial(name, count, Type.ORE);
	}

	@Deprecated
	public static ItemStack getOre(String name) {
		return getOre(name, 1);
	}

	@Deprecated
	public static boolean oresExist(String... names) {
		return false;
	}

	public static ItemStack getStack(Item item) {
		return getStack(item, 1);
	}

	public static ItemStack getStack(Item item, int count) {
		return new ItemStack(item, count);
	}


	public static ItemStack getStack(Item item, boolean wildcard) {
		return getStack(item, 1, wildcard);
	}

	@Deprecated
	public static ItemStack getStack(Item item, int count, boolean wildcard) {
		return new ItemStack(item, count);
	}

	public static ItemStack getStack(Block block) {
		return getStack(block, 1);
	}

	public static ItemStack getStack(Block block, int count) {
		return new ItemStack(block, count);
	}

	public static ItemStack getStack(Block block, boolean wildcard) {
		return getStack(block, 1, true);
	}

	public static ItemStack getStack(Block block, int count, boolean wildcard) {
		throw new UnsupportedOperationException("Use tags");
	}


	public static Ingredient getCell(String name, int count){
		throw new UnsupportedOperationException("fix me");
		//return new IngredientCell(ItemCells.getCellByName(name, count));
	}

	public static Ingredient getCell(String name){
		return getCell(name, 1);
	}

	public enum Type {
		DUST, SMALL_DUST, INGOT, NUGGET, PLATE, GEM, CELL, PART, CABLE, MACHINE_FRAME, MACHINE_CASING, UPGRADE, ORE
	}
}
