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
import techreborn.api.recipe.machines.GrinderRecipe;
import techreborn.api.recipe.machines.VacuumFreezerRecipe;
import techreborn.compat.CompatManager;
import techreborn.config.ConfigTechReborn;
import techreborn.init.recipes.AlloySmelterRecipes;
import techreborn.init.recipes.AssemblingMachineRecipes;
import techreborn.init.recipes.BlastFurnaceRecipes;
import techreborn.init.recipes.ChemicalReactorRecipes;
import techreborn.init.recipes.CompressorRecipes;
import techreborn.init.recipes.CraftingTableRecipes;
import techreborn.init.recipes.DistillationTowerRecipes;
import techreborn.init.recipes.ExtractorRecipes;
import techreborn.init.recipes.FluidGeneratorRecipes;
import techreborn.init.recipes.FluidReplicatorRecipes;
import techreborn.init.recipes.FusionReactorRecipes;
import techreborn.init.recipes.ImplosionCompressorRecipes;
import techreborn.init.recipes.IndustrialCentrifugeRecipes;
import techreborn.init.recipes.IndustrialElectrolyzerRecipes;
import techreborn.init.recipes.IndustrialGrinderRecipes;
import techreborn.init.recipes.IndustrialSawmillRecipes;
import techreborn.init.recipes.PlateBendingMachineRecipes;
import techreborn.init.recipes.RollingMachineRecipes;
import techreborn.init.recipes.ScrapboxRecipes;
import techreborn.init.recipes.SmeltingRecipes;
import techreborn.init.recipes.SolidCanningMachineRecipes;
import techreborn.init.recipes.WireMillRecipes;
import techreborn.items.ItemCells;
import techreborn.items.ingredients.ItemDusts;
import techreborn.items.ingredients.ItemIngots;
import techreborn.items.ingredients.ItemParts;
import techreborn.lib.ModInfo;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import static techreborn.utils.OreDictUtils.getDictData;
import static techreborn.utils.OreDictUtils.getDictOreOrEmpty;
import static techreborn.utils.OreDictUtils.isDictPrefixed;
import static techreborn.utils.OreDictUtils.joinDictName;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class ModRecipes {

	public static void init() {
		//Gonna rescan to make sure we have an uptodate list
		OreUtil.scanForOres();
		//Done again incase we loaded before QuantumStorage
		CompatManager.isQuantumStorageLoaded = Loader.isModLoaded("quantumstorage");

		CraftingTableRecipes.init();
		SmeltingRecipes.init();
		ExtractorRecipes.init();
		RollingMachineRecipes.init();
		FluidGeneratorRecipes.init();
		IndustrialGrinderRecipes.init();
		IndustrialCentrifugeRecipes.init();
		IndustrialElectrolyzerRecipes.init();
		ImplosionCompressorRecipes.init();
		ScrapboxRecipes.init();
		ChemicalReactorRecipes.init();
		FusionReactorRecipes.init();
		DistillationTowerRecipes.init();
		AlloySmelterRecipes.init();
		FluidReplicatorRecipes.init();
		BlastFurnaceRecipes.init();
		CompressorRecipes.init();
		PlateBendingMachineRecipes.init();

		// Using Praescriptum >>
		AssemblingMachineRecipes.init();
		SolidCanningMachineRecipes.init();
		WireMillRecipes.init();
		// << Using Praescriptum

		addVacuumFreezerRecipes();
		addIc2Recipes();
		addGrinderRecipes();
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
	}

	static void addGrinderRecipes() {

		// Vanilla
		// int eutick = 2;
		// int ticktime = 300;

		RecipeHandler.addRecipe(new GrinderRecipe(
			ItemParts.getPartByName("plantball"),
			new ItemStack(Blocks.DIRT),
			300, 20));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Items.BONE),
			new ItemStack(Items.DYE, 6, 15),
			300, 20));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Blocks.COBBLESTONE),
			new ItemStack(Blocks.SAND),
			300, 20));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Blocks.GRAVEL),
			new ItemStack(Items.FLINT),
			300, 20));

		RecipeHandler.addRecipe(new GrinderRecipe(
				new ItemStack(Items.COAL),
				ItemDusts.getDustByName("coal"),
			300, 20));
		
		RecipeHandler.addRecipe(new GrinderRecipe(
				new ItemStack(Items.COAL, 1, 1),
				ItemDusts.getDustByName("charcoal"),
			300, 20));

		RecipeHandler.addRecipe(new GrinderRecipe(
				new ItemStack(net.minecraft.init.Items.CLAY_BALL),
				ItemDusts.getDustByName("clay"),
			300, 20));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Blocks.GLOWSTONE),
			ItemDusts.getDustByName("glowstone", 4),
			300, 20));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Blocks.NETHERRACK),
			ItemDusts.getDustByName("netherrack"),
			300, 20));
		
		RecipeHandler.addRecipe(new GrinderRecipe(
				new ItemStack(Blocks.END_STONE),
				ItemDusts.getDustByName("endstone"),
			300, 20));
		
		RecipeHandler.addRecipe(new GrinderRecipe(
				new ItemStack(Items.ENDER_EYE),
				ItemDusts.getDustByName("ender_eye", 2),
			300, 20));
		
		RecipeHandler.addRecipe(new GrinderRecipe(
				new ItemStack(Items.ENDER_PEARL),
				ItemDusts.getDustByName("ender_pearl", 2),
			300, 20));
		
		RecipeHandler.addRecipe(new GrinderRecipe(
				new ItemStack(Blocks.LAPIS_ORE),
				new ItemStack(Items.DYE, 10, 4),
			300, 20));
		
		RecipeHandler.addRecipe(new GrinderRecipe(
				new ItemStack(Blocks.OBSIDIAN),
				ItemDusts.getDustByName("obsidian", 4),
			300, 20));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Items.BLAZE_ROD),
			new ItemStack(Items.BLAZE_POWDER, 4),
			300, 20));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Blocks.MAGMA),
			new ItemStack(Items.MAGMA_CREAM, 4),
			300, 20));

		if (OreUtil.doesOreExistAndValid("stoneMarble")) {
			ItemStack marbleStack = getOre("stoneMarble");
			marbleStack.setCount(1);
			RecipeHandler.addRecipe(new GrinderRecipe(
					marbleStack, ItemDusts.getDustByName("marble"),
				300, 20));
		}
		if (OreUtil.doesOreExistAndValid("stoneBasalt")) {
			ItemStack marbleStack = getOre("stoneBasalt");
			marbleStack.setCount(1);
			RecipeHandler.addRecipe(new GrinderRecipe(
					marbleStack, ItemDusts.getDustByName("basalt"),
				300, 20));
		}

		//See comments bellow, this allows the ore to go to the product when it sometimes goes straight to dust.
		RecipeHandler.addRecipe(new GrinderRecipe(
			"oreCoal", new ItemStack(Items.COAL, 2),
			300, 20));

		RecipeHandler.addRecipe(new GrinderRecipe(
			"oreDiamond", new ItemStack(Items.DIAMOND, 1),
			300, 20));

		RecipeHandler.addRecipe(new GrinderRecipe(
			"oreEmerald", new ItemStack(Items.EMERALD, 1),
			300, 20));

		RecipeHandler.addRecipe(new GrinderRecipe(
			"oreRedstone", new ItemStack(Items.REDSTONE, 8),
			300, 20));

		for (String oreDictionaryName : OreDictionary.getOreNames()) {
			if (isDictPrefixed(oreDictionaryName, "ore", "gem", "ingot")) {
				ItemStack oreStack = getDictOreOrEmpty(oreDictionaryName, 1);
				String[] data = getDictData(oreDictionaryName);

				//High-level ores shouldn't grind here
				if (data[0].equals("ore") && (
					data[1].equals("tungsten") ||
						data[1].equals("titanium") ||
						data[1].equals("aluminium") ||
						data[1].equals("iridium") ||
						data[1].equals("saltpeter")||
						data[1].equals("coal") || //Done here to skip going to dust so it can go to the output
						data[1].equals("diamond") || //For example diamond ore should go to diamonds not the diamond dust
						data[1].equals("emerald") || //TODO possibly remove this and make it a bit more dyamic? (Check for furnace recipes? and then the block drop?)
						data[1].equals("redstone")
						) ||
					oreStack.isEmpty())
					continue;

				if (data[0].equals("ore") && (data[1].equals("quartz") || data[1].equals("certuzQuartz"))) {
					continue;
				}

				boolean ore = data[0].equals("ore");
				Core.logHelper.debug("Ore: " + data[1]);
				ItemStack dust = getDictOreOrEmpty(joinDictName("dust", data[1]), ore ? 2 : 1);
				if (dust.isEmpty() || dust.getItem() == null) {
					continue;
				}
				dust = dust.copy();
				if (ore) {
					dust.setCount(2);
				}
				boolean useOreDict = true;
				//Disables the ore dict for lapis, this is becuase it is oredict with dye. This may cause some other lapis ores to not be grindable, but we can fix that when that arrises.
				if(data[1].equalsIgnoreCase("lapis")){
					useOreDict = false;
				}
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, dust, 300, 20, useOreDict));
			}
		}
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
