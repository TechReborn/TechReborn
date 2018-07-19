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

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import reborncore.api.recipe.RecipeHandler;
import techreborn.api.recipe.machines.ChemicalReactorRecipe;

/**
 * @author drcrazy
 *
 */
public class ChemicalReactorRecipes extends RecipeMethods {
	public static void init(){
		register(getStack(Items.GOLD_NUGGET, 8), getStack(Items.MELON), getStack(Items.SPECKLED_MELON), 40);
		register(getStack(Items.GOLD_NUGGET, 8), getStack(Items.CARROT), getStack(Items.GOLDEN_CARROT), 40);
		register(getStack(Items.GOLD_INGOT, 8), getStack(Items.APPLE), getStack(Items.GOLDEN_APPLE), 40);
		register(getStack(Blocks.GOLD_BLOCK, 8), getStack(Items.APPLE, 1), getStack(Items.GOLDEN_APPLE, 1, 1), 100);
		register(getStack(Items.BLAZE_POWDER), getStack(Items.SLIME_BALL), getStack(Items.MAGMA_CREAM), 40);
		register(getStack(Items.BLAZE_POWDER), getStack(Items.ENDER_PEARL), getStack(Items.ENDER_EYE), 40);
		
		//Cells recipes. One cell should be cooked in 20 seconds
		register(getMaterial("carbon", Type.CELL), getMaterial("calcium", Type.CELL), getMaterial("calciumCarbonate", 2, Type.CELL), 800);
		register(getMaterial("carbon", Type.CELL), getMaterial("hydrogen", 4, Type.CELL), getMaterial("methane", 5, Type.CELL), 2000);
		register(getMaterial("carbon", Type.CELL), getMaterial("nitrogen", Type.CELL), getMaterial("nitrocarbon", 2, Type.CELL), 800);
		register(getMaterial("nitrocarbon", Type.CELL), getMaterial("water", Type.CELL), getMaterial("glyceryl", 2, Type.CELL), 600);
		register(getMaterial("glyceryl", Type.CELL), getMaterial("diesel", 4, Type.CELL), getMaterial("nitroDiesel", 5, Type.CELL), 250);
		register(getMaterial("glyceryl", Type.CELL), getMaterial("carbon", 4, Type.CELL), getMaterial("nitrocoalFuel", 5, Type.CELL), 250);
		register(getMaterial("sulfur", Type.CELL), getMaterial("water", 2, Type.CELL), getMaterial("sulfuricAcid", 3, Type.CELL), 1200);
		register(getMaterial("sulfur", Type.CELL), getMaterial("sodium", Type.CELL), getMaterial("sodiumSulfide", 2, Type.CELL), 800);
		register(getMaterial("sodiumSulfide", Type.CELL), getMaterial("compressedair", Type.CELL), getMaterial("sodiumPersulfate", 2, Type.CELL), 800);
		register(getMaterial("compressedair", Type.CELL), getMaterial("hydrogen", 2, Type.CELL), getMaterial("water", Type.CELL), 400);
		register(getMaterial("compressedair", 2, Type.CELL), getMaterial("nitrogen", Type.CELL), getMaterial("nitrogenDioxide", Type.CELL), 400);
		register(getMaterial("oil", Type.CELL), getMaterial("nitrogen", Type.CELL), getMaterial("nitrofuel", 2, Type.CELL), 800);
	}
	
	static void register(ItemStack in1, ItemStack in2,  ItemStack out, int tickTime, int euPerTick){
		RecipeHandler.addRecipe(new ChemicalReactorRecipe(in1, in2, out, tickTime, euPerTick));
	}
	
	static void register (ItemStack in1, ItemStack in2,  ItemStack out, int tickTime){
		register(in1, in2, out, tickTime, 30);
	}

}