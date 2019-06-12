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

import net.minecraft.item.ItemStack;


/**
 * @author drcrazy
 *
 */
public class CompressorRecipes extends RecipeMethods {
	
	public static void init() {
//		register(ItemIngots.getIngotByName("advanced_alloy"), ItemPlates.getPlateByName("advanced_alloy"), 400, 20);
//		register(IC2Duplicates.CARBON_MESH.getStackBasedOnConfig(), ItemPlates.getPlateByName("carbon"), 400, 2);
//		register(OreUtil.getStackFromName("plankWood"), OreUtil.getStackFromName("plateWood"));
//		register(OreUtil.getStackFromName("dustLazurite"), ItemPlates.getPlateByName("lazurite"));
//		register(OreUtil.getStackFromName("obsidian"), ItemPlates.getPlateByName("obsidian", 9));
//		register(OreUtil.getStackFromName("dustObsidian"), ItemPlates.getPlateByName("obsidian"));
//		register(OreUtil.getStackFromName("dustYellowGarnet"), ItemPlates.getPlateByName("YellowGarnet"));
//		register(OreUtil.getStackFromName("blockYellowGarnet"), ItemPlates.getPlateByName("YellowGarnet", 9));
//		register(OreUtil.getStackFromName("dustRedGarnet"), ItemPlates.getPlateByName("RedGarnet"));
//		register(OreUtil.getStackFromName("blockRedGarnet"), ItemPlates.getPlateByName("RedGarnet", 9));
//		register("ingotRefinedIron", ItemPlates.getPlateByName("RefinedIron"));
//
//		ItemStack plate;
//		for (String ore : OreUtil.oreNames) {
//			if (ore.equals("iridium")) {
//				continue;
//			}
//			if (!OreUtil.hasPlate(ore)) {
//				continue;
//			}
//
//			try {
//				plate = ItemPlates.getPlateByName(ore, 1);
//			} catch (InvalidParameterException e) {
//				plate = OreUtil.getStackFromName("plate" + OreUtil.capitalizeFirstLetter(ore));
//			}
//			if (plate.isEmpty()) {
//				continue;
//			}
//			if (OreUtil.hasIngot(ore)) {
//				register(OreUtil.getStackFromName("ingot" + OreUtil.capitalizeFirstLetter(ore)), plate);
//			}
//			if (OreUtil.hasGem(ore) && OreUtil.hasDust(ore)) {
//				register(OreUtil.getStackFromName("dust" + OreUtil.capitalizeFirstLetter(ore)), plate);
//			}
//			if (OreUtil.hasBlock(ore)) {
//				ItemStack morePlates = plate.copy();
//				morePlates.setCount(9);
//				register(OreUtil.getStackFromName("block" + OreUtil.capitalizeFirstLetter(ore)), morePlates);
//			}
//		}
	}
	
	static void register(Object input, ItemStack output) {
		register(input,  output, 300, 4);
	}

	static void register(Object input, ItemStack output, int tickTime, int euPerTick) {
		//RecipeHandler.addRecipe(new CompressorRecipe(input, output, tickTime, euPerTick));
	}
}
