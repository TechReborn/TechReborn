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

import reborncore.api.praescriptum.recipes.RecipeHandler;

import techreborn.api.recipe.Recipes;
import techreborn.items.ItemCells;
import techreborn.items.ItemDynamicCell;
import techreborn.items.ingredients.*;

/**
 * @author estebes
 */
public class CentrifugeRecipes extends RecipeMethods {
    public static void init() {
        Recipes.centrifuge = new RecipeHandler("Centrifuge");

        // Lava
        Recipes.centrifuge.createRecipe()
                .withInput(ItemCells.getCellByName("lava", 16))
                .withOutput(ItemIngots.getIngotByName("tin", 18))
                .withOutput(ItemIngots.getIngotByName("copper", 4))
                .withOutput(ItemIngots.getIngotByName("electrum", 1))
                .withOutput(ItemDustsSmall.getSmallDustByName("tungsten", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(15000)
                .register();

        // Magma cream
        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.MAGMA_CREAM, 1))
                .withOutput(getStack(Items.SLIME_BALL, 1))
                .withOutput(getStack(Items.BLAZE_POWDER, 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(500)
                .register();

        // Sap
        Recipes.centrifuge.createRecipe()
                .withInput(ItemParts.getPartByName("sap", 4))
                .withOutput(ItemParts.getPartByName("rubber", 14))
                .withOutput(ItemParts.getPartByName("plantball", 1))
                .withOutput(ItemParts.getPartByName("compressed_plantball", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(1300)
                .register();

        // Rubber wood
        Recipes.centrifuge.createRecipe()
                .withInput("logRubber", 16)
                .withInput(ItemDynamicCell.getEmptyCell(5))
                .withOutput(ItemParts.getPartByName("sap", 8))
                .withOutput(ItemParts.getPartByName("plantball", 6))
                .withOutput(ItemCells.getCellByName("carbon", 4))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        // Dirt
        Recipes.centrifuge.createRecipe()
                .withInput("dirt", 16)
                .withOutput(getStack(Blocks.SAND, 8))
                .withOutput(getStack(Items.CLAY_BALL, 1))
                .withOutput(ItemParts.getPartByName("plantball", 1))
                .withOutput(ItemParts.getPartByName("compressed_plantball", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(2500)
                .register();

        // Grass
        Recipes.centrifuge.createRecipe()
                .withInput("grass", 16)
                .withOutput(getStack(Blocks.SAND, 8))
                .withOutput(getStack(Items.CLAY_BALL, 1))
                .withOutput(ItemParts.getPartByName("plantball", 2))
                .withOutput(ItemParts.getPartByName("compressed_plantball", 2))
                .withEnergyCostPerTick(5)
                .withOperationDuration(2500)
                .register();

        // Soul sand
        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Blocks.SOUL_SAND, 16))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(getStack(Blocks.SAND, 10))
                .withOutput(ItemCells.getCellByName("oil", 1))
                .withOutput(ItemDusts.getDustByName("saltpeter", 4))
                .withOutput(ItemDusts.getDustByName("coal", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(2500)
                .register();

        // Mycelium
        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Blocks.MYCELIUM, 8))
                .withOutput(getStack(Blocks.SAND, 4))
                .withOutput(getStack(Blocks.BROWN_MUSHROOM, 2))
                .withOutput(getStack(Blocks.RED_MUSHROOM, 2))
                .withOutput(getStack(Items.CLAY_BALL, 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(1640)
                .register();

        // Dusts
        Recipes.centrifuge.createRecipe()
                .withInput("dustRedstone", 10)
                .withInput(ItemDynamicCell.getEmptyCell(4))
                .withOutput(ItemDusts.getDustByName("pyrite", 5))
                .withOutput(ItemDusts.getDustByName("ruby", 1))
                .withOutput(ItemCells.getCellByName("mercury", 3))
                .withOutput(ItemCells.getCellByName("silicon", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(7000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustGlowstone", 16)
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(getStack(Items.REDSTONE, 8))
                .withOutput(ItemDusts.getDustByName("gold", 8))
                .withOutput(ItemCells.getCellByName("helium", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(25000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("gemLapis", 4)
                .withOutput(ItemDusts.getDustByName("lazurite", 3))
                .withOutput(ItemDustsSmall.getSmallDustByName("sodalite", 2))
                .withOutput(ItemDustsSmall.getSmallDustByName("pyrite", 1))
                .withOutput(ItemDustsSmall.getSmallDustByName("calcite", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(1500)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustBasalt", 16)
                .withOutput(ItemDusts.getDustByName("flint", 8))
                .withOutput(ItemDusts.getDustByName("dark_ashes", 4))
                .withOutput(ItemDusts.getDustByName("calcite", 3))
                .withOutput(ItemDusts.getDustByName("peridot", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(2040)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustBrass", 1)
                .withOutput(ItemDustsSmall.getSmallDustByName("copper", 3))
                .withOutput(ItemDustsSmall.getSmallDustByName("zinc", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(1500)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustBronze", 1)
                .withOutput(ItemDustsSmall.getSmallDustByName("copper", 6))
                .withOutput(ItemDustsSmall.getSmallDustByName("tin", 2))
                .withEnergyCostPerTick(5)
                .withOperationDuration(1500)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustCopper", 3)
                .withOutput(ItemDustsSmall.getSmallDustByName("gold", 1))
                .withOutput(ItemDustsSmall.getSmallDustByName("nickel", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(2400)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustDarkAshes", 2)
                .withOutput(ItemDusts.getDustByName("ashes", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(240)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustElectrum", 1)
                .withOutput(ItemDustsSmall.getSmallDustByName("gold", 2))
                .withOutput(ItemDustsSmall.getSmallDustByName("silver", 2))
                .withEnergyCostPerTick(5)
                .withOperationDuration(960)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustEnderEye", 2)
                .withOutput(getStack(Items.BLAZE_POWDER, 1))
                .withOutput(ItemDusts.getDustByName("ender_pearl", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(1840)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustEndstone", 16)
                .withInput(ItemDynamicCell.getEmptyCell(2))
                .withOutput(getStack(Blocks.SAND, 12))
                .withOutput(ItemDustsSmall.getSmallDustByName("tungsten", 1))
                .withOutput(ItemCells.getCellByName("helium", 1))
                .withOutput(ItemCells.getCellByName("helium3", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(4800)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustGold", 3)
                .withOutput(ItemDustsSmall.getSmallDustByName("copper", 1))
                .withOutput(ItemDustsSmall.getSmallDustByName("nickel", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(2400)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustInvar", 3)
                .withOutput(ItemDusts.getDustByName("iron", 2))
                .withOutput(ItemDusts.getDustByName("nickel", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(1000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustIron", 2)
                .withOutput(ItemDustsSmall.getSmallDustByName("nickel", 1))
                .withOutput(ItemDustsSmall.getSmallDustByName("tin", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(1500)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustLead", 2)
                .withOutput(ItemDustsSmall.getSmallDustByName("silver", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(2400)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustMarble", 2)
                .withOutput(ItemDusts.getDustByName("calcite", 7))
                .withOutput(ItemDusts.getDustByName("magnesium", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(1040)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustNetherrack", 16)
                .withOutput(ItemDusts.getDustByName("sulfur", 4))
                .withOutput(getStack(Items.REDSTONE, 1))
                .withOutput(ItemDusts.getDustByName("coal", 1))
                .withOutput(getStack(Items.GOLD_NUGGET, 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(2400)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustNickel", 3)
                .withOutput(ItemDustsSmall.getSmallDustByName("copper", 1))
                .withOutput(ItemDustsSmall.getSmallDustByName("gold", 1))
                .withOutput(ItemDustsSmall.getSmallDustByName("iron", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(3440)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustPlatinum", 1)
                .withOutput(ItemNuggets.getNuggetByName("iridium", 1))
                .withOutput(ItemDustsSmall.getSmallDustByName("nickel", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(3000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustRedGarnet", 16)
                .withOutput(ItemDusts.getDustByName("spessartine", 8))
                .withOutput(ItemDusts.getDustByName("almandine", 5))
                .withOutput(ItemDusts.getDustByName("pyrope", 3))
                .withEnergyCostPerTick(5)
                .withOperationDuration(3000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustSilver", 2)
                .withOutput(ItemDustsSmall.getSmallDustByName("lead", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(2400)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustTin", 2)
                .withOutput(ItemDustsSmall.getSmallDustByName("iron", 1))
                .withOutput(ItemDustsSmall.getSmallDustByName("zinc", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(2100)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustYellowGarnet", 16)
                .withOutput(ItemDusts.getDustByName("grossular", 8))
                .withOutput(ItemDusts.getDustByName("andradite", 5))
                .withOutput(ItemDusts.getDustByName("uvarovite", 3))
                .withEnergyCostPerTick(5)
                .withOperationDuration(3500)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput("dustZinc", 1)
                .withOutput(ItemDustsSmall.getSmallDustByName("tin", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(1040)
                .register();

        // Cells
        Recipes.centrifuge.createRecipe()
                .withInput(ItemCells.getCellByName("hydrogen", 4))
                .withOutput(ItemCells.getCellByName("deuterium", 1))
                .withOutput(ItemDynamicCell.getEmptyCell(3))
                .withEnergyCostPerTick(5)
                .withOperationDuration(3000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(ItemCells.getCellByName("deuterium", 4))
                .withOutput(ItemCells.getCellByName("tritium", 1))
                .withOutput(ItemDynamicCell.getEmptyCell(3))
                .withEnergyCostPerTick(5)
                .withOperationDuration(3000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(ItemCells.getCellByName("helium", 16))
                .withOutput(ItemCells.getCellByName("helium3", 1))
                .withOutput(ItemDynamicCell.getEmptyCell(15))
                .withEnergyCostPerTick(5)
                .withOperationDuration(10000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(ItemCells.getCellByName("calciumcarbonate", 1))
                .withOutput(ItemDusts.getDustByName("calcite", 1))
                .withOutput(ItemDynamicCell.getEmptyCell(1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(40)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(ItemCells.getCellByName("sulfur", 1))
                .withOutput(ItemDusts.getDustByName("sulfur", 1))
                .withOutput(ItemDynamicCell.getEmptyCell(1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(40)
                .register();

        // Methane
        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.GOLDEN_APPLE, 1, 1))
                .withInput(ItemDynamicCell.getEmptyCell(2))
                .withOutput(ItemCells.getCellByName("methane", 2))
                .withOutput(getStack(Items.GOLD_INGOT, 64))
                .withEnergyCostPerTick(5)
                .withOperationDuration(10000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.GOLDEN_APPLE, 1))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withOutput(getStack(Items.GOLD_NUGGET, 6))
                .withEnergyCostPerTick(5)
                .withOperationDuration(10000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.GOLDEN_CARROT, 1))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withOutput(getStack(Items.GOLD_NUGGET, 6))
                .withEnergyCostPerTick(5)
                .withOperationDuration(10000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.SPECKLED_MELON, 1))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withOutput(getStack(Items.GOLD_NUGGET, 6))
                .withEnergyCostPerTick(5)
                .withOperationDuration(10000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.APPLE, 32))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.MUSHROOM_STEW, 16))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.BREAD, 64))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.PORKCHOP, 12))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.COOKED_PORKCHOP, 16))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.BEEF, 12))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.COOKED_BEEF, 16))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.CHICKEN, 12))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.COOKED_CHICKEN, 16))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.MUTTON, 12))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.COOKED_MUTTON, 16))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.RABBIT, 12))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.COOKED_RABBIT, 16))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.FISH, 12))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.COOKED_FISH, 16))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.MELON, 64))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Blocks.PUMPKIN, 16))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.ROTTEN_FLESH, 16))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.SPIDER_EYE, 32))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.CARROT, 16))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.POTATO, 16))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.POISONOUS_POTATO, 12))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.BAKED_POTATO, 24))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.BEETROOT, 12))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.BEETROOT_SOUP, 16))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.COOKIE, 64))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.CAKE, 8))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Blocks.BROWN_MUSHROOM_BLOCK, 12))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Blocks.RED_MUSHROOM_BLOCK, 12))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Blocks.BROWN_MUSHROOM, 12))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Blocks.RED_MUSHROOM, 12))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();

        Recipes.centrifuge.createRecipe()
                .withInput(getStack(Items.NETHER_WART, 32))
                .withInput(ItemDynamicCell.getEmptyCell(1))
                .withOutput(ItemCells.getCellByName("methane", 1))
                .withEnergyCostPerTick(5)
                .withOperationDuration(5000)
                .register();
    }
}
