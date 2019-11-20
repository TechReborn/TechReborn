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

import net.minecraftforge.oredict.OreDictionary;

import reborncore.api.praescriptum.recipes.RecipeHandler;
import reborncore.common.util.ItemUtils;

import techreborn.api.recipe.Recipes;
import techreborn.items.ingredients.ItemDusts;
import techreborn.items.ingredients.ItemParts;
import techreborn.lib.ModInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.google.common.collect.ImmutableList;

/**
 * @author estebes
 */
public class GrinderRecipes extends RecipeMethods {
    public static void init() {
        Recipes.grinder = new RecipeHandler("Grinder");

        // Ores
        Recipes.grinder.createRecipe()
                .withInput("oreCoal")
                .withOutput(getStack(Items.COAL, 3))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreDiamond")
                .withOutput(getStack(Items.DIAMOND, 2))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreEmerald")
                .withOutput(getStack(Items.EMERALD, 2))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreLapis")
                .withOutput(getStack(Items.DYE, 10, 4))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreRedstone")
                .withOutput(getStack(Items.REDSTONE, 6))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreIron")
                .withOutput(ItemDusts.getDustByName("iron", 2))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreGold")
                .withOutput(ItemDusts.getDustByName("gold", 2))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreGalena")
                .withOutput(ItemDusts.getDustByName("galena", 2))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreRuby")
                .withOutput(ItemDusts.getDustByName("ruby", 2))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreSapphire")
                .withOutput(ItemDusts.getDustByName("sapphire", 2))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreBauxite")
                .withOutput(ItemDusts.getDustByName("bauxite", 4))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("orePyrite")
                .withOutput(ItemDusts.getDustByName("pyrite", 5))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreCinnabar")
                .withOutput(ItemDusts.getDustByName("cinnabar", 3))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreSphalerite")
                .withOutput(ItemDusts.getDustByName("sphalerite", 4))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreTungsten")
                .withOutput(ItemDusts.getDustByName("tungsten", 2))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreSheldonite")
                .withOutput(ItemDusts.getDustByName("platinum", 2))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("orePlatinum")
                .withOutput(ItemDusts.getDustByName("platinum", 2))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("orePeridot")
                .withOutput(ItemDusts.getDustByName("peridot", 2))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreOlivine")
                .withOutput(ItemDusts.getDustByName("peridot", 2))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreSodalite")
                .withOutput(ItemDusts.getDustByName("sodalite", 12))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreCopper")
                .withOutput(ItemDusts.getDustByName("copper", 2))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreTin")
                .withOutput(ItemDusts.getDustByName("tin", 2))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreSilver")
                .withOutput(ItemDusts.getDustByName("silver", 2))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput("oreLead")
                .withOutput(ItemDusts.getDustByName("lead", 2))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        // Ingots -> Dust
        ImmutableList<String> ingots = Arrays.stream(OreDictionary.getOreNames())
                .filter(name -> name.startsWith("ingot"))
                .collect(ImmutableList.toImmutableList());

        ingots.forEach(entry -> {
            String equivalent = entry.replaceFirst("ingot", "dust");
            if (OreDictionary.doesOreNameExist(equivalent)) {
                List<ItemStack> equivalents = OreDictionary.getOres(equivalent);
                if (!equivalents.isEmpty()) {
                    equivalents.stream()
                            .filter(stack -> Objects.requireNonNull(stack.getItem().getRegistryName())
                                    .getResourceDomain().equalsIgnoreCase(ModInfo.MOD_ID))
                            .findFirst()
                            .ifPresent(stack -> Recipes.grinder.createRecipe()
                                    .withInput(entry)
                                    .withOutput(stack.copy())
                                    .withEnergyCostPerTick(2)
                                    .withOperationDuration(300)
                                    .register());
                }
            }
        });

        // Gem -> Dust
        ImmutableList<String> gems = Arrays.stream(OreDictionary.getOreNames())
                .filter(name -> name.startsWith("gem"))
                .collect(ImmutableList.toImmutableList());

        gems.forEach(entry -> {
            String equivalent = entry.replaceFirst("gem", "dust");
            if (OreDictionary.doesOreNameExist(equivalent)) {
                List<ItemStack> equivalents = OreDictionary.getOres(equivalent);
                if (!equivalents.isEmpty()) {
                    equivalents.stream()
                            .filter(stack -> Objects.requireNonNull(stack.getItem().getRegistryName())
                                    .getResourceDomain().equalsIgnoreCase(ModInfo.MOD_ID))
                            .findFirst()
                            .ifPresent(stack -> Recipes.grinder.createRecipe()
                                    .withInput(entry)
                                    .withOutput(stack.copy())
                                    .withEnergyCostPerTick(2)
                                    .withOperationDuration(300)
                                    .register());
                }
            }
        });

        // Plates -> Dust
        ImmutableList<String> plates = Arrays.stream(OreDictionary.getOreNames())
                .filter(name -> name.startsWith("plate"))
                .collect(ImmutableList.toImmutableList());

        plates.forEach(entry -> {
            String equivalent = entry.replaceFirst("plate", "dust");
            if (OreDictionary.doesOreNameExist(equivalent)) {
                List<ItemStack> equivalents = OreDictionary.getOres(equivalent);
                if (!equivalents.isEmpty()) {
                    equivalents.stream()
                            .filter(stack -> Objects.requireNonNull(stack.getItem().getRegistryName())
                                    .getResourceDomain().equalsIgnoreCase(ModInfo.MOD_ID))
                            .findFirst()
                            .ifPresent(stack -> Recipes.grinder.createRecipe()
                                    .withInput(entry)
                                    .withOutput(stack.copy())
                                    .withEnergyCostPerTick(2)
                                    .withOperationDuration(300)
                                    .register());
                }
            }
        });

        // Blocks -> Dust
        ImmutableList<String> blocks = Arrays.stream(OreDictionary.getOreNames())
                .filter(name -> name.startsWith("block"))
                .collect(ImmutableList.toImmutableList());

        blocks.forEach(entry -> {
            String equivalent = entry.replaceFirst("block", "dust");
            if (OreDictionary.doesOreNameExist(equivalent)) {
                List<ItemStack> equivalents = OreDictionary.getOres(equivalent);
                if (!equivalents.isEmpty()) {
                    equivalents.stream()
                            .filter(stack -> Objects.requireNonNull(stack.getItem().getRegistryName())
                                    .getResourceDomain().equalsIgnoreCase(ModInfo.MOD_ID))
                            .findFirst()
                            .ifPresent(stack -> Recipes.grinder.createRecipe()
                                    .withInput(entry)
                                    .withOutput(ItemUtils.copyWithSize(stack, 9))
                                    .withEnergyCostPerTick(2)
                                    .withOperationDuration(300)
                                    .register());
                }
            }
        });

        // Stones -> Dust
        ImmutableList<String> stones = Arrays.stream(OreDictionary.getOreNames())
                .filter(name -> name.startsWith("stone"))
                .collect(ImmutableList.toImmutableList());

        stones.forEach(entry -> {
            String equivalent = entry.replaceFirst("stone", "dust");
            if (OreDictionary.doesOreNameExist(equivalent)) {
                List<ItemStack> equivalents = OreDictionary.getOres(equivalent);
                if (!equivalents.isEmpty()) {
                    equivalents.stream()
                            .filter(stack -> Objects.requireNonNull(stack.getItem().getRegistryName())
                                    .getResourceDomain().equalsIgnoreCase(ModInfo.MOD_ID))
                            .findFirst()
                            .ifPresent(stack -> Recipes.grinder.createRecipe()
                                    .withInput(entry)
                                    .withOutput(stack.copy())
                                    .withEnergyCostPerTick(2)
                                    .withOperationDuration(300)
                                    .register());
                }
            }
        });

        Recipes.grinder.createRecipe()
                .withInput(getStack(Blocks.COBBLESTONE))
                .withOutput(getStack(Blocks.SAND))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput(getStack(Blocks.END_STONE))
                .withOutput(ItemDusts.getDustByName("endstone"))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput(getStack(Blocks.GLOWSTONE))
                .withOutput(ItemDusts.getDustByName("glowstone", 4))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput(getStack(Blocks.NETHERRACK))
                .withOutput(ItemDusts.getDustByName("netherrack"))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput(getStack(Blocks.OBSIDIAN))
                .withOutput(ItemDusts.getDustByName("obsidian", 4))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        // Misc
        Recipes.grinder.createRecipe()
                .withInput(getStack(Items.BLAZE_ROD))
                .withOutput(getStack(Items.BLAZE_POWDER, 4))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput(getStack(Items.BONE))
                .withOutput(getStack(Items.DYE, 6, 15))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput(getStack(Items.CLAY_BALL))
                .withOutput(ItemDusts.getDustByName("clay"))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput(getStack(Items.ENDER_PEARL))
                .withOutput(ItemDusts.getDustByName("ender_pearl", 2))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput(getStack(Items.ENDER_EYE))
                .withOutput(ItemDusts.getDustByName("ender_eye", 2))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput(getStack(Blocks.GRAVEL))
                .withOutput(getStack(Items.FLINT))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput(getStack(Blocks.MAGMA))
                .withOutput(getStack(Items.MAGMA_CREAM, 4))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();

        Recipes.grinder.createRecipe()
                .withInput(ItemParts.getPartByName("plantball"))
                .withOutput(getStack(Blocks.DIRT))
                .withEnergyCostPerTick(2)
                .withOperationDuration(300)
                .register();
    }
}
