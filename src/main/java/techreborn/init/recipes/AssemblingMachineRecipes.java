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

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import reborncore.api.recipe.RecipeHandler;
import reborncore.common.util.OreUtil;
import techreborn.api.recipe.machines.AssemblingMachineRecipe;
import techreborn.init.IC2Duplicates;
import techreborn.items.ingredients.ItemParts;

public class AssemblingMachineRecipes {
	public static void init() {
		// Basic Circuit Board
		register(OreUtil.getStackFromName("plateRefinedIron"), OreUtil.getStackFromName("plateElectrum", 2), ItemParts.getPartByName("basic_circuit_board", 2), 800, 1);
		register(OreUtil.getStackFromName("plateAluminum"), OreUtil.getStackFromName("plateElectrum", 2), ItemParts.getPartByName("basic_circuit_board", 2), 800, 1);

		// Basic Circuit
		register(ItemParts.getPartByName("basic_circuit_board"), RecipeMethods.getStack(IC2Duplicates.CABLE_ICOPPER, 3), ItemParts.getPartByName("electronic_circuit"), 1600, 1);

		// Advanced Circuit Board
		register(OreUtil.getStackFromName("circuitBasic"), OreUtil.getStackFromName("plateElectrum", 2), ItemParts.getPartByName("advanced_circuit_board"), 1600, 2);
		register(OreUtil.getStackFromName("plateSilicon"), OreUtil.getStackFromName("plateElectrum", 4), ItemParts.getPartByName("advanced_circuit_board", 2), 1600, 2);

		// Advanced Circuit Parts
		register(new ItemStack(Items.DYE, 1, 4), OreUtil.getStackFromName("dustGlowstone"), ItemParts.getPartByName("advanced_circuit_parts", 2), 800, 2);
		register(OreUtil.getStackFromName("dustLazurite"), OreUtil.getStackFromName("dustGlowstone"), ItemParts.getPartByName("advanced_circuit_parts",2 ), 800, 2);

		// Advanced Circuit
		register(ItemParts.getPartByName("advanced_circuit_board"), ItemParts.getPartByName("advanced_circuit_parts", 2), ItemParts.getPartByName("advanced_circuit"), 160, 2);

		// Processor Circuit Board
		register(OreUtil.getStackFromName("platePlatinum"), OreUtil.getStackFromName("circuitAdvanced"), ItemParts.getPartByName("processor_circuit_board"), 3200, 4);

		// Data Storage Circuit
		register(OreUtil.getStackFromName("gemEmerald", 8), OreUtil.getStackFromName("circuitAdvanced"), ItemParts.getPartByName("data_storage_circuit", 4), 3200, 4);
		register(OreUtil.getStackFromName("gemPeridot", 8), OreUtil.getStackFromName("circuitAdvanced"), ItemParts.getPartByName("data_storage_circuit", 4), 3200, 8);
		register(OreUtil.getStackFromName("dustEmerald", 8), OreUtil.getStackFromName("circuitAdvanced"), ItemParts.getPartByName("data_storage_circuit", 4), 3200, 8);
		register(OreUtil.getStackFromName("dustPeridot", 8), OreUtil.getStackFromName("circuitAdvanced"), ItemParts.getPartByName("data_storage_circuit", 4), 3200, 8);

		// Data Control Circuit
		register(ItemParts.getPartByName("processor_circuit_board"), ItemParts.getPartByName("data_storage_circuit"), ItemParts.getPartByName("data_control_circuit"), 3200, 4);

		// Energy Flow Circuit
		register(ItemParts.getPartByName("processor_circuit_board"), RecipeMethods.getStack(IC2Duplicates.LAPATRON_CRYSTAL), ItemParts.getPartByName("energy_flow_circuit"), 3200, 4);

		// Data Orb
		register(ItemParts.getPartByName("data_control_circuit"), ItemParts.getPartByName("data_storage_circuit", 8), ItemParts.getPartByName("data_orb"), 12800, 16);
	}

	static void register(Object inputA, Object inputB, ItemStack output, int tickTime, int euPerTick) {
		RecipeHandler.addRecipe(new AssemblingMachineRecipe(inputA, inputB, output, tickTime, euPerTick));
	}
}
