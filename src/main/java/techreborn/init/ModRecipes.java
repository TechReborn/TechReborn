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

package techreborn.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

import reborncore.api.recipe.RecipeHandler;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.util.ItemUtils;
import reborncore.common.util.OreUtil;
import reborncore.common.util.RebornCraftingHelper;

import techreborn.Core;
import techreborn.api.recipe.machines.VacuumFreezerRecipe;
import techreborn.compat.CompatManager;
import techreborn.config.ConfigTechReborn;
import techreborn.init.fuels.*;
import techreborn.init.recipes.*;
import techreborn.items.ItemCells;
import techreborn.items.ingredients.ItemIngots;
import techreborn.lib.ModInfo;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class ModRecipes {

    public static void init() {
        //Gonna rescan to make sure we have an uptodate list
        OreUtil.scanForOres();
        //Done again incase we loaded before QuantumStorage
        CompatManager.isQuantumStorageLoaded = Loader.isModLoaded("quantumstorage");

        CraftingTableRecipes.init();
        SmeltingRecipes.init();
        RollingMachineRecipes.init();
        FluidGeneratorRecipes.init();
        IndustrialGrinderRecipes.init();
        IndustrialCentrifugeRecipes.init();
        IndustrialElectrolyzerRecipes.init();
        ImplosionCompressorRecipes.init();
        ScrapboxRecipes.init();
        FusionReactorRecipes.init();
        DistillationTowerRecipes.init();
        FluidReplicatorRecipes.init();
        BlastFurnaceRecipes.init();

        // Using Praescriptum >>
        // Recipes
        AlloySmelterRecipes.init();
        AssemblingMachineRecipes.init();
        ChemicalReactorRecipes.init();
        CompressorRecipes.init();
        ExtractorRecipes.init();
        GrinderRecipes.init();
        PlateBendingMachineRecipes.init();
        SolidCanningMachineRecipes.init();
        WireMillRecipes.init();

        // Fuels
        DieselGeneratorFuels.init();
        GasTurbineFuels.init();
        PlasmaGeneratorFuels.init();
        SemiFluidGeneratorFuels.init();
        ThermalGeneratorFuels.init();
        // << Using Praescriptum

        addVacuumFreezerRecipes();
        addIc2Recipes();
    }

    public static void postInit() {
        if (ConfigTechReborn.disableRailcraftSteelNuggetRecipe) {
            Iterator<Entry<ItemStack, ItemStack>> iterator = FurnaceRecipes.instance().getSmeltingList().entrySet().iterator();
            Map.Entry<ItemStack, ItemStack> entry;
            while (iterator.hasNext()) {
                entry = iterator.next();
                if (entry.getValue() instanceof ItemStack && entry.getKey() instanceof ItemStack) {
                    ItemStack input = (ItemStack) entry.getKey();
                    ItemStack output = (ItemStack) entry.getValue();
                    if (ItemUtils.isInputEqual("nuggetSteel", output, true, true, false) && ItemUtils.isInputEqual("nuggetIron", input, true, true, false)) {
                        Core.logHelper.info("Removing a steelnugget smelting recipe");
                        iterator.remove();
                    }
                }
            }
        }

        IndustrialSawmillRecipes.init();

        DieselGeneratorFuels.postInit();
        GasTurbineFuels.postInit();
        PlasmaGeneratorFuels.postInit();
        SemiFluidGeneratorFuels.postInit();
        ThermalGeneratorFuels.postInit();
    }

    static void addVacuumFreezerRecipes() {
        RecipeHandler.addRecipe(new VacuumFreezerRecipe(
                new ItemStack(Blocks.ICE, 2),
                new ItemStack(Blocks.PACKED_ICE),
                60, 64
        ));

        RecipeHandler.addRecipe(new VacuumFreezerRecipe(
                ItemIngots.getIngotByName("hot_tungstensteel"),
                ItemIngots.getIngotByName("tungstensteel"),
                440, 64));

        RecipeHandler.addRecipe(new VacuumFreezerRecipe(
                ItemCells.getCellByName("heliumplasma"),
                ItemCells.getCellByName("helium"),
                440, 64));

        RecipeHandler.addRecipe(
                new VacuumFreezerRecipe(
                        ItemCells.getCellByName("water"),
                        ItemCells.getCellByName("cell"),
                        60, 64));

    }

    static void addIc2Recipes() {
        RebornCraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.MANUAL), "ingotRefinedIron",
                Items.BOOK);

//		RebornCraftingHelper
//			.addShapedOreRecipe(ItemParts.getPartByName("machineParts", 16), "CSC", "SCS", "CSC", 'S', "ingotSteel",
//				'C', "circuitBasic");
//
        // RebornCraftingHelper.addShapedOreRecipe(new
        // ItemStack(ModBlocks.magicalAbsorber),
        // "CSC", "IBI", "CAC",
        // 'C', "circuitMaster",
        // 'S', "craftingSuperconductor",
        // 'B', Blocks.beacon,
        // 'A', ModBlocks.magicEnergeyConverter,
        // 'I', "plateIridium");
        //
        // RebornCraftingHelper.addShapedOreRecipe(new
        // ItemStack(ModBlocks.magicEnergeyConverter),
        // "CTC", "PBP", "CLC",
        // 'C', "circuitAdvanced",
        // 'P', "platePlatinum",
        // 'B', Blocks.beacon,
        // 'L', "lapotronCrystal",
        // 'T', TechRebornAPI.recipeCompact.getItem("teleporter"));

        // RebornCraftingHelper.addShapedOreRecipe(new
        // ItemStack(ModBlocks.chunkLoader),
        // "SCS", "CMC", "SCS",
        // 'S', "plateSteel",
        // 'C', "circuitMaster",
        // 'M', new ItemStack(ModItems.parts, 1, 39));


    }

    public static ItemStack getBucketWithFluid(Fluid fluid) {
        return FluidUtil.getFilledBucket(new FluidStack(fluid, Fluid.BUCKET_VOLUME));
    }

    public static ItemStack getOre(String name) {
        if (OreDictionary.getOres(name).isEmpty()) {
            return new ItemStack(ModItems.MISSING_RECIPE_PLACEHOLDER);
        }
        return OreDictionary.getOres(name).get(0).copy();
    }
}
