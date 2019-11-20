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

import reborncore.api.praescriptum.recipes.RecipeHandler;

import techreborn.api.recipe.Recipes;
import techreborn.items.ingredients.ItemIngots;

/**
 * @author estebes
 */
public class AlloySmelterRecipes extends RecipeMethods {
	public static void init() {
		Recipes.alloySmelter = new RecipeHandler("AlloySmelter");

		// Brass
		Recipes.alloySmelter.createRecipe()
			.withInput("ingotCopper", 3)
			.withInput("ingotZinc", 1)
			.withOutput(ItemIngots.getIngotByName("brass", 4))
			.withEnergyCostPerTick(16)
			.withOperationDuration(200)
			.register();

		Recipes.alloySmelter.createRecipe()
			.withInput("dustCopper", 3)
			.withInput("dustZinc", 1)
			.withOutput(ItemIngots.getIngotByName("brass", 4))
			.withEnergyCostPerTick(16)
			.withOperationDuration(200)
			.register();

		Recipes.alloySmelter.createRecipe()
			.withInput("ingotCopper", 3)
			.withInput("dustZinc", 1)
			.withOutput(ItemIngots.getIngotByName("brass", 4))
			.withEnergyCostPerTick(16)
			.withOperationDuration(200)
			.register();

		Recipes.alloySmelter.createRecipe()
			.withInput("dustCopper", 3)
			.withInput("ingotZinc", 1)
			.withOutput(ItemIngots.getIngotByName("brass", 4))
			.withEnergyCostPerTick(16)
			.withOperationDuration(200)
			.register();

		// Bronze
		Recipes.alloySmelter.createRecipe()
			.withInput("ingotCopper", 3)
			.withInput("ingotTin", 1)
			.withOutput(ItemIngots.getIngotByName("bronze", 4))
			.withEnergyCostPerTick(16)
			.withOperationDuration(100)
			.register();

		Recipes.alloySmelter.createRecipe()
			.withInput("dustCopper", 3)
			.withInput("dustTin", 1)
			.withOutput(ItemIngots.getIngotByName("bronze", 4))
			.withEnergyCostPerTick(16)
			.withOperationDuration(100)
			.register();

		Recipes.alloySmelter.createRecipe()
			.withInput("ingotCopper", 3)
			.withInput("dustTin", 1)
			.withOutput(ItemIngots.getIngotByName("bronze", 4))
			.withEnergyCostPerTick(16)
			.withOperationDuration(100)
			.register();

		Recipes.alloySmelter.createRecipe()
			.withInput("dustCopper", 3)
			.withInput("ingotTin", 1)
			.withOutput(ItemIngots.getIngotByName("bronze", 4))
			.withEnergyCostPerTick(16)
			.withOperationDuration(100)
			.register();

		// Electrum
		Recipes.alloySmelter.createRecipe()
			.withInput("ingotGold", 1)
			.withInput("ingotSilver", 1)
			.withOutput(ItemIngots.getIngotByName("electrum", 2))
			.withEnergyCostPerTick(16)
			.withOperationDuration(100)
			.register();

		Recipes.alloySmelter.createRecipe()
			.withInput("dustGold", 1)
			.withInput("dustSilver", 1)
			.withOutput(ItemIngots.getIngotByName("electrum", 2))
			.withEnergyCostPerTick(16)
			.withOperationDuration(100)
			.register();

		Recipes.alloySmelter.createRecipe()
			.withInput("ingotGold", 1)
			.withInput("dustSilver", 1)
			.withOutput(ItemIngots.getIngotByName("electrum", 2))
			.withEnergyCostPerTick(16)
			.withOperationDuration(100)
			.register();

		Recipes.alloySmelter.createRecipe()
			.withInput("dustGold", 1)
			.withInput("ingotSilver", 1)
			.withOutput(ItemIngots.getIngotByName("electrum", 2))
			.withEnergyCostPerTick(16)
			.withOperationDuration(100)
			.register();

		// Invar
		Recipes.alloySmelter.createRecipe()
			.withInput("ingotIron", 2)
			.withInput("ingotNickel", 1)
			.withOutput(ItemIngots.getIngotByName("invar", 3))
			.withEnergyCostPerTick(16)
			.withOperationDuration(140)
			.register();

		Recipes.alloySmelter.createRecipe()
			.withInput("dustIron", 2)
			.withInput("dustNickel", 1)
			.withOutput(ItemIngots.getIngotByName("invar", 3))
			.withEnergyCostPerTick(16)
			.withOperationDuration(140)
			.register();

		Recipes.alloySmelter.createRecipe()
			.withInput("ingotIron", 2)
			.withInput("dustNickel", 1)
			.withOutput(ItemIngots.getIngotByName("invar", 3))
			.withEnergyCostPerTick(16)
			.withOperationDuration(140)
			.register();

		Recipes.alloySmelter.createRecipe()
			.withInput("dustIron", 2)
			.withInput("ingotNickel", 1)
			.withOutput(ItemIngots.getIngotByName("invar", 3))
			.withEnergyCostPerTick(16)
			.withOperationDuration(140)
			.register();

//
//		// Red Alloy
//		if (OreUtil.doesOreExistAndValid("ingotRedAlloy")) {
//			ItemStack redAlloyStack = getOre("ingotRedAlloy");
//			redAlloyStack.setCount(1);
//			RecipeHandler.addRecipe(
//				new AlloySmelterRecipe(new ItemStack(Items.REDSTONE, 4), ItemIngots.getIngotByName("copper", 1),
//					redAlloyStack, 200, 16));
//			RecipeHandler.addRecipe(
//				new AlloySmelterRecipe(new ItemStack(Items.REDSTONE, 4), new ItemStack(Items.IRON_INGOT, 1),
//					redAlloyStack, 200, 16));
//		}
//
//		// Blue Alloy
//		if (oresExist("ingotBlueAlloy", "dustTeslatite")) {
//			ItemStack blueAlloyStack = getOre("ingotBlueAlloy");
//			blueAlloyStack.setCount(1);
//			RecipeHandler.addRecipe(new AlloySmelterRecipe(getOre("dustTeslatite", 4),
//				ItemIngots.getIngotByName("silver", 1), blueAlloyStack, 200, 16));
//		}
//
//		// Blue Alloy
//		if (OreUtil.doesOreExistAndValid("ingotPurpleAlloy") && OreUtil.doesOreExistAndValid("dustInfusedTeslatite")) {
//			ItemStack purpleAlloyStack = getOre("ingotPurpleAlloy");
//			purpleAlloyStack.setCount(1);
//			ItemStack infusedTeslatiteStack = getOre("ingotPurpleAlloy");
//			infusedTeslatiteStack.setCount(8);
//			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("redAlloy", 1),
//				ItemIngots.getIngotByName("blueAlloy", 1), purpleAlloyStack, 200, 16));
//			RecipeHandler.addRecipe(
//				new AlloySmelterRecipe(new ItemStack(Items.GOLD_INGOT, 1), infusedTeslatiteStack, purpleAlloyStack,
//					200, 16));
//		}
//
//		// Aluminum Brass
//		if (OreUtil.doesOreExistAndValid("ingotAluminumBrass")) {
//			ItemStack aluminumBrassStack = getOre("ingotAluminumBrass");
//			aluminumBrassStack.setCount(4);
//			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3),
//				ItemIngots.getIngotByName("aluminum", 1), aluminumBrassStack, 200, 16));
//			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3),
//				ItemDusts.getDustByName("aluminum", 1), aluminumBrassStack, 200, 16));
//			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3),
//				ItemIngots.getIngotByName("aluminum", 1), aluminumBrassStack, 200, 16));
//			RecipeHandler.addRecipe(
//				new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemDusts.getDustByName("aluminum", 1),
//					aluminumBrassStack, 200, 16));
//		}
//
//		// Manyullyn
//		if (OreUtil.doesOreExistAndValid("ingotManyullyn") && OreUtil.doesOreExistAndValid("ingotCobalt") && OreUtil
//			.doesOreExistAndValid("ingotArdite")) {
//			ItemStack manyullynStack = getOre("ingotManyullyn");
//			manyullynStack.setCount(1);
//			ItemStack cobaltStack = getOre("ingotCobalt");
//			cobaltStack.setCount(1);
//			ItemStack arditeStack = getOre("ingotArdite");
//			arditeStack.setCount(1);
//			RecipeHandler.addRecipe(new AlloySmelterRecipe(cobaltStack, arditeStack, manyullynStack, 200, 16));
//		}
//
//		// Conductive Iron
//		if (OreUtil.doesOreExistAndValid("ingotConductiveIron")) {
//			ItemStack conductiveIronStack = getOre("ingotConductiveIron");
//			conductiveIronStack.setCount(1);
//			RecipeHandler.addRecipe(
//				new AlloySmelterRecipe(new ItemStack(Items.REDSTONE, 1), new ItemStack(Items.IRON_INGOT, 1),
//					conductiveIronStack, 200, 16));
//		}
//
//		// Redstone Alloy
//		if (OreUtil.doesOreExistAndValid("ingotRedstoneAlloy") && OreUtil.doesOreExistAndValid("itemSilicon")) {
//			ItemStack redstoneAlloyStack = getOre("ingotRedstoneAlloy");
//			redstoneAlloyStack.setCount(1);
//			ItemStack siliconStack = getOre("itemSilicon");
//			siliconStack.setCount(1);
//			RecipeHandler.addRecipe(
//				new AlloySmelterRecipe(new ItemStack(Items.REDSTONE, 1), siliconStack, redstoneAlloyStack, 200,
//					16));
//		}
//
//		// Pulsating Iron
//		if (OreUtil.doesOreExistAndValid("ingotPhasedIron")) {
//			ItemStack pulsatingIronStack = getOre("ingotPhasedIron");
//			pulsatingIronStack.setCount(1);
//			RecipeHandler.addRecipe(
//				new AlloySmelterRecipe(new ItemStack(Items.IRON_INGOT, 1), new ItemStack(Items.ENDER_PEARL, 1),
//					pulsatingIronStack, 200, 16));
//			RecipeHandler.addRecipe(
//				new AlloySmelterRecipe(new ItemStack(Items.IRON_INGOT, 1), ItemDusts.getDustByName("ender_pearl", 1),
//					pulsatingIronStack, 200, 16));
//		}
//
//		// Vibrant Alloy
//		if (OreUtil.doesOreExistAndValid("ingotEnergeticAlloy") && OreUtil.doesOreExistAndValid("ingotPhasedGold")) {
//			ItemStack energeticAlloyStack = getOre("ingotEnergeticAlloy");
//			energeticAlloyStack.setCount(1);
//			ItemStack vibrantAlloyStack = getOre("ingotPhasedGold");
//			vibrantAlloyStack.setCount(1);
//			RecipeHandler.addRecipe(
//				new AlloySmelterRecipe(energeticAlloyStack, new ItemStack(Items.ENDER_PEARL, 1), vibrantAlloyStack,
//					200, 16));
//			RecipeHandler.addRecipe(
//				new AlloySmelterRecipe(energeticAlloyStack, ItemDusts.getDustByName("ender_pearl", 1),
//					vibrantAlloyStack, 200, 16));
//		}
//
//		// Soularium
//		if (OreUtil.doesOreExistAndValid("ingotSoularium")) {
//			ItemStack soulariumStack = getOre("ingotSoularium");
//			soulariumStack.setCount(1);
//			RecipeHandler.addRecipe(
//				new AlloySmelterRecipe(new ItemStack(Blocks.SOUL_SAND, 1), new ItemStack(Items.GOLD_INGOT, 1),
//					soulariumStack, 200, 16));
//		}
	}
}
