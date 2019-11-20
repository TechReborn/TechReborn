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

import techreborn.api.recipe.Recipes;
import techreborn.init.IC2Duplicates;
import techreborn.items.ingredients.ItemDusts;
import techreborn.items.ingredients.ItemIngots;
import techreborn.items.ingredients.ItemParts;
import techreborn.items.ingredients.ItemPlates;
import techreborn.lib.ModInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.google.common.collect.ImmutableList;

/**
 * @author estebes
 */
public class CompressorRecipes extends RecipeMethods {
    public static void init() {
        Recipes.compressor = new RecipeHandler("Compressor");

        // Advanced Alloy
        Recipes.compressor.createRecipe()
                .withInput(ItemIngots.getIngotByName("advanced_alloy"))
                .withOutput(ItemPlates.getPlateByName("advanced_alloy"))
                .withEnergyCostPerTick(20)
                .withOperationDuration(400)
                .register();

        // Carbon Mesh
        Recipes.compressor.createRecipe()
                .withInput(IC2Duplicates.CARBON_MESH.getStackBasedOnConfig())
                .withOutput(ItemPlates.getPlateByName("carbon"))
                .withEnergyCostPerTick(2)
                .withOperationDuration(400)
                .register();

        // Gem -> Plate
        ImmutableList<String> gems = Arrays.stream(OreDictionary.getOreNames())
                .filter(name -> name.startsWith("gem"))
                .collect(ImmutableList.toImmutableList());

        gems.forEach(entry -> {
            String equivalent = entry.replaceFirst("gem", "plate");
            if (OreDictionary.doesOreNameExist(equivalent)) {
                List<ItemStack> equivalents = OreDictionary.getOres(equivalent);
                if (!equivalents.isEmpty()) {
                    equivalents.stream()
                            .filter(stack -> Objects.requireNonNull(stack.getItem().getRegistryName())
                                    .getResourceDomain().equalsIgnoreCase(ModInfo.MOD_ID))
                            .findFirst()
                            .ifPresent(stack -> Recipes.compressor.createRecipe()
                                    .withInput(entry)
                                    .withOutput(stack.copy())
                                    .withEnergyCostPerTick(2)
                                    .withOperationDuration(400)
                                    .register());
                }
            }
        });

        // Gem Dust -> Plate
        gems.forEach(entry -> {
            String dustEntry = entry.replaceFirst("gem", "dust");
            String equivalent = entry.replaceFirst("gem", "plate");
            if (OreDictionary.doesOreNameExist(equivalent)) {
                List<ItemStack> equivalents = OreDictionary.getOres(equivalent);
                if (!equivalents.isEmpty()) {
                    equivalents.stream()
                            .filter(stack -> Objects.requireNonNull(stack.getItem().getRegistryName())
                                    .getResourceDomain().equalsIgnoreCase(ModInfo.MOD_ID))
                            .findFirst()
                            .ifPresent(stack -> Recipes.compressor.createRecipe()
                                    .withInput(dustEntry)
                                    .withOutput(stack.copy())
                                    .withEnergyCostPerTick(2)
                                    .withOperationDuration(400)
                                    .register());
                }
            }
        });

        // Wood Plate
        Recipes.compressor.createRecipe()
                .withInput("plankWood")
                .withOutput(ItemPlates.getPlateByName("wood"))
                .withEnergyCostPerTick(2)
                .withOperationDuration(400)
                .register();

//        register(OreUtil.getStackFromName("dustLazurite"), ItemPlates.getPlateByName("lazurite"));
//        register(OreUtil.getStackFromName("dustObsidian"), ItemPlates.getPlateByName("obsidian"));
//        register(OreUtil.getStackFromName("dustYellowGarnet"), ItemPlates.getPlateByName("YellowGarnet"));
//        register(OreUtil.getStackFromName("dustRedGarnet"), ItemPlates.getPlateByName("RedGarnet"));

        // Compressed Plantball >>
        Recipes.compressor.createRecipe()
                .withInput(ItemParts.getPartByName("plantball"))
                .withOutput(ItemParts.getPartByName("compressed_plantball"))
                .withEnergyCostPerTick(2)
                .withOperationDuration(400)
                .register();

        Recipes.compressor.createRecipe()
                .withInput("treeLeaves", 8)
                .withOutput(ItemParts.getPartByName("compressed_plantball"))
                .withEnergyCostPerTick(2)
                .withOperationDuration(400)
                .register();

        Recipes.compressor.createRecipe()
                .withInput("treeSapling", 8)
                .withOutput(ItemParts.getPartByName("compressed_plantball"))
                .withEnergyCostPerTick(2)
                .withOperationDuration(400)
                .register();

        Recipes.compressor.createRecipe()
                .withInput(getStack(Items.REEDS, 8))
                .withOutput(ItemParts.getPartByName("compressed_plantball"))
                .withEnergyCostPerTick(2)
                .withOperationDuration(400)
                .register();

        Recipes.compressor.createRecipe()
                .withInput(getStack(Blocks.CACTUS, 8))
                .withOutput(ItemParts.getPartByName("compressed_plantball"))
                .withEnergyCostPerTick(2)
                .withOperationDuration(400)
                .register();

        Recipes.compressor.createRecipe()
                .withInput(getStack(Items.WHEAT, 8))
                .withOutput(ItemParts.getPartByName("compressed_plantball"))
                .withEnergyCostPerTick(2)
                .withOperationDuration(400)
                .register();

        Recipes.compressor.createRecipe()
                .withInput(getStack(Items.CARROT, 8))
                .withOutput(ItemParts.getPartByName("compressed_plantball"))
                .withEnergyCostPerTick(2)
                .withOperationDuration(400)
                .register();

        Recipes.compressor.createRecipe()
                .withInput(getStack(Items.POTATO, 8))
                .withOutput(ItemParts.getPartByName("compressed_plantball"))
                .withEnergyCostPerTick(2)
                .withOperationDuration(400)
                .register();

        Recipes.compressor.createRecipe()
                .withInput(getStack(Items.APPLE, 8))
                .withOutput(ItemParts.getPartByName("compressed_plantball"))
                .withEnergyCostPerTick(2)
                .withOperationDuration(400)
                .register();

        Recipes.compressor.createRecipe()
                .withInput(getStack(Items.MELON, 64))
                .withOutput(ItemParts.getPartByName("compressed_plantball"))
                .withEnergyCostPerTick(2)
                .withOperationDuration(400)
                .register();

        Recipes.compressor.createRecipe()
                .withInput(getStack(Blocks.MELON_BLOCK, 8))
                .withOutput(ItemParts.getPartByName("compressed_plantball"))
                .withEnergyCostPerTick(2)
                .withOperationDuration(400)
                .register();

        Recipes.compressor.createRecipe()
                .withInput(getStack(Blocks.PUMPKIN, 8))
                .withOutput(ItemParts.getPartByName("compressed_plantball"))
                .withEnergyCostPerTick(2)
                .withOperationDuration(400)
                .register();

        // Nuclear >>
        Recipes.compressor.createRecipe()
                .withInput(ItemDusts.getDustByName("thorium"))
                .withOutput(ItemIngots.getIngotByName("thorium"))
                .withEnergyCostPerTick(2)
                .withOperationDuration(400)
                .register();
        // << Nuclear

//        ItemStack plate;
//        for (String ore : OreUtil.oreNames) {
//            if (ore.equals("iridium")) {
//                continue;
//            }
//            if (!OreUtil.hasPlate(ore)) {
//                continue;
//            }
//
//            try {
//                plate = ItemPlates.getPlateByName(ore, 1);
//            } catch (InvalidParameterException e) {
//                plate = OreUtil.getStackFromName("plate" + OreUtil.capitalizeFirstLetter(ore));
//            }
//            if (plate.isEmpty()) {
//                continue;
//            }
//
//            if (OreUtil.hasGem(ore) && OreUtil.hasDust(ore)) {
//                register(OreUtil.getStackFromName("dust" + OreUtil.capitalizeFirstLetter(ore)), plate);
//            }
//        }
    }
}
