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

package techreborn.init.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import reborncore.api.recipe.RecipeHandler;
import reborncore.common.util.OreUtil;
import techreborn.api.recipe.machines.AlloySmelterRecipe;
import techreborn.items.ItemDusts;
import techreborn.items.ItemIngots;

/**
 * @author drcrazy
 *
 */
public class AlloySmelterRecipes extends RecipeMethods {

	public static void init() {
		// Bronze
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3), ItemIngots.getIngotByName("tin", 1),
				ItemIngots.getIngotByName("bronze", 4), 200, 16));
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3), ItemDusts.getDustByName("tin", 1),
				ItemIngots.getIngotByName("bronze", 4), 200, 16));
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemIngots.getIngotByName("tin", 1),
				ItemIngots.getIngotByName("bronze", 4), 200, 16));
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemDusts.getDustByName("tin", 1),
				ItemIngots.getIngotByName("bronze", 4), 200, 16));

		// Electrum
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(new ItemStack(Items.GOLD_INGOT, 1), ItemIngots.getIngotByName("silver", 1),
				ItemIngots.getIngotByName("electrum", 2), 200, 16));
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(new ItemStack(Items.GOLD_INGOT, 1), ItemDusts.getDustByName("silver", 1),
				ItemIngots.getIngotByName("electrum", 2), 200, 16));
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(ItemDusts.getDustByName("gold", 1), ItemIngots.getIngotByName("silver", 1),
				ItemIngots.getIngotByName("electrum", 2), 200, 16));
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(ItemDusts.getDustByName("gold", 1), ItemDusts.getDustByName("silver", 1),
				ItemIngots.getIngotByName("electrum", 2), 200, 16));

		// Invar
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(new ItemStack(Items.IRON_INGOT, 2), ItemIngots.getIngotByName("nickel", 1),
				ItemIngots.getIngotByName("invar", 3), 200, 16));
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(new ItemStack(Items.IRON_INGOT, 2), ItemDusts.getDustByName("nickel", 1),
				ItemIngots.getIngotByName("invar", 3), 200, 16));
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(ItemDusts.getDustByName("iron", 2), ItemIngots.getIngotByName("nickel", 1),
				ItemIngots.getIngotByName("invar", 3), 200, 16));
		RecipeHandler.addRecipe(
			new AlloySmelterRecipe(ItemDusts.getDustByName("iron", 2), ItemDusts.getDustByName("nickel", 1),
				ItemIngots.getIngotByName("invar", 3), 200, 16));

		// Brass
		if (OreUtil.doesOreExistAndValid("ingotBrass")) {
			ItemStack brassStack = getOre("ingotBrass");
			brassStack.setCount(4);
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3), ItemIngots.getIngotByName("zinc", 1),
					brassStack, 200, 16));
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3), ItemDusts.getDustByName("zinc", 1),
					brassStack, 200, 16));
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemIngots.getIngotByName("zinc", 1),
					brassStack, 200, 16));
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemDusts.getDustByName("zinc", 1),
					brassStack, 200, 16));
		}

		// Red Alloy
		if (OreUtil.doesOreExistAndValid("ingotRedAlloy")) {
			ItemStack redAlloyStack = getOre("ingotRedAlloy");
			redAlloyStack.setCount(1);
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(new ItemStack(Items.REDSTONE, 4), ItemIngots.getIngotByName("copper", 1),
					redAlloyStack, 200, 16));
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(new ItemStack(Items.REDSTONE, 4), new ItemStack(Items.IRON_INGOT, 1),
					redAlloyStack, 200, 16));
		}

		// Blue Alloy
		if (OreUtil.doesOreExistAndValid("ingotBlueAlloy")) {
			ItemStack blueAlloyStack = getOre("ingotBlueAlloy");
			blueAlloyStack.setCount(1);
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("teslatite", 4),
				ItemIngots.getIngotByName("silver", 1), blueAlloyStack, 200, 16));
		}

		// Blue Alloy
		if (OreUtil.doesOreExistAndValid("ingotPurpleAlloy") && OreUtil.doesOreExistAndValid("dustInfusedTeslatite")) {
			ItemStack purpleAlloyStack = getOre("ingotPurpleAlloy");
			purpleAlloyStack.setCount(1);
			ItemStack infusedTeslatiteStack = getOre("ingotPurpleAlloy");
			infusedTeslatiteStack.setCount(8);
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("redAlloy", 1),
				ItemIngots.getIngotByName("blueAlloy", 1), purpleAlloyStack, 200, 16));
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(new ItemStack(Items.GOLD_INGOT, 1), infusedTeslatiteStack, purpleAlloyStack,
					200, 16));
		}

		// Aluminum Brass
		if (OreUtil.doesOreExistAndValid("ingotAluminumBrass")) {
			ItemStack aluminumBrassStack = getOre("ingotAluminumBrass");
			aluminumBrassStack.setCount(4);
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3),
				ItemIngots.getIngotByName("aluminum", 1), aluminumBrassStack, 200, 16));
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemIngots.getIngotByName("copper", 3),
				ItemDusts.getDustByName("aluminum", 1), aluminumBrassStack, 200, 16));
			RecipeHandler.addRecipe(new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3),
				ItemIngots.getIngotByName("aluminum", 1), aluminumBrassStack, 200, 16));
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(ItemDusts.getDustByName("copper", 3), ItemDusts.getDustByName("aluminum", 1),
					aluminumBrassStack, 200, 16));
		}

		// Manyullyn
		if (OreUtil.doesOreExistAndValid("ingotManyullyn") && OreUtil.doesOreExistAndValid("ingotCobalt") && OreUtil
			.doesOreExistAndValid("ingotArdite")) {
			ItemStack manyullynStack = getOre("ingotManyullyn");
			manyullynStack.setCount(1);
			ItemStack cobaltStack = getOre("ingotCobalt");
			cobaltStack.setCount(1);
			ItemStack arditeStack = getOre("ingotArdite");
			arditeStack.setCount(1);
			RecipeHandler.addRecipe(new AlloySmelterRecipe(cobaltStack, arditeStack, manyullynStack, 200, 16));
		}

		// Conductive Iron
		if (OreUtil.doesOreExistAndValid("ingotConductiveIron")) {
			ItemStack conductiveIronStack = getOre("ingotConductiveIron");
			conductiveIronStack.setCount(1);
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(new ItemStack(Items.REDSTONE, 1), new ItemStack(Items.IRON_INGOT, 1),
					conductiveIronStack, 200, 16));
		}

		// Redstone Alloy
		if (OreUtil.doesOreExistAndValid("ingotRedstoneAlloy") && OreUtil.doesOreExistAndValid("itemSilicon")) {
			ItemStack redstoneAlloyStack = getOre("ingotRedstoneAlloy");
			redstoneAlloyStack.setCount(1);
			ItemStack siliconStack = getOre("itemSilicon");
			siliconStack.setCount(1);
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(new ItemStack(Items.REDSTONE, 1), siliconStack, redstoneAlloyStack, 200,
					16));
		}

		// Pulsating Iron
		if (OreUtil.doesOreExistAndValid("ingotPhasedIron")) {
			ItemStack pulsatingIronStack = getOre("ingotPhasedIron");
			pulsatingIronStack.setCount(1);
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(new ItemStack(Items.IRON_INGOT, 1), new ItemStack(Items.ENDER_PEARL, 1),
					pulsatingIronStack, 200, 16));
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(new ItemStack(Items.IRON_INGOT, 1), ItemDusts.getDustByName("ender_pearl", 1),
					pulsatingIronStack, 200, 16));
		}

		// Vibrant Alloy
		if (OreUtil.doesOreExistAndValid("ingotEnergeticAlloy") && OreUtil.doesOreExistAndValid("ingotPhasedGold")) {
			ItemStack energeticAlloyStack = getOre("ingotEnergeticAlloy");
			energeticAlloyStack.setCount(1);
			ItemStack vibrantAlloyStack = getOre("ingotPhasedGold");
			vibrantAlloyStack.setCount(1);
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(energeticAlloyStack, new ItemStack(Items.ENDER_PEARL, 1), vibrantAlloyStack,
					200, 16));
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(energeticAlloyStack, ItemDusts.getDustByName("ender_pearl", 1),
					vibrantAlloyStack, 200, 16));
		}

		// Soularium
		if (OreUtil.doesOreExistAndValid("ingotSoularium")) {
			ItemStack soulariumStack = getOre("ingotSoularium");
			soulariumStack.setCount(1);
			RecipeHandler.addRecipe(
				new AlloySmelterRecipe(new ItemStack(Blocks.SOUL_SAND, 1), new ItemStack(Items.GOLD_INGOT, 1),
					soulariumStack, 200, 16));
		}
	}

}
