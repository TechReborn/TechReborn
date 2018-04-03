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
import techreborn.api.recipe.machines.BlastFurnaceRecipe;
import techreborn.api.recipe.machines.CompressorRecipe;
import techreborn.api.recipe.machines.GrinderRecipe;
import techreborn.api.recipe.machines.VacuumFreezerRecipe;
import techreborn.blocks.BlockOre;
import techreborn.compat.CompatManager;
import techreborn.config.ConfigTechReborn;
import techreborn.init.recipes.*;
import techreborn.items.*;
import techreborn.lib.ModInfo;

import java.security.InvalidParameterException;
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

		addBlastFurnaceRecipes();
		addVacuumFreezerRecipes();
		addIc2Recipes();
		addGrinderRecipes();
		addCompressorRecipes();
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

		//Let it be in postInit to be sure that oredict already there
		if (OreUtil.doesOreExistAndValid("stoneMarble")) {
			ItemStack marbleStack = getOre("stoneMarble");
			marbleStack.setCount(1);
			RecipeHandler.addRecipe(new GrinderRecipe(
					marbleStack, ItemDusts.getDustByName("marble"), 
					120, 10));
		}
		if (OreUtil.doesOreExistAndValid("stoneBasalt")) {
			ItemStack marbleStack = getOre("stoneBasalt");
			marbleStack.setCount(1);
			RecipeHandler.addRecipe(new GrinderRecipe(
					marbleStack, ItemDusts.getDustByName("basalt"), 
					120, 10));
		}
	}

	private static void addCompressorRecipes() {
		RecipeHandler.addRecipe(new CompressorRecipe(ItemIngots.getIngotByName("advanced_alloy"),
				ItemPlates.getPlateByName("advanced_alloy"), 400, 20));
		RecipeHandler.addRecipe(new CompressorRecipe(IC2Duplicates.CARBON_MESH.getStackBasedOnConfig(),
				ItemPlates.getPlateByName("carbon"), 400, 2));
		RecipeHandler.addRecipe(new CompressorRecipe(OreUtil.getStackFromName("plankWood", 1),
				OreUtil.getStackFromName("plateWood", 1), 300, 4));
		RecipeHandler.addRecipe(new CompressorRecipe(OreUtil.getStackFromName("dustLazurite", 1),
				ItemPlates.getPlateByName("lazurite", 1), 300, 4));

		ItemStack plate;
		for (String ore : OreUtil.oreNames) {
			if (ore.equals("iridium")) {
				continue;
			}
			if (OreUtil.hasPlate(ore)) {
				try {
					plate = ItemPlates.getPlateByName(ore, 1);
				} catch (InvalidParameterException e) {
					plate = OreUtil.getStackFromName("plate" + OreUtil.capitalizeFirstLetter(ore), 1);
				}
				if (plate.isEmpty()) {
					continue;				
				}
				if (OreUtil.hasIngot(ore)) {
					RecipeHandler.addRecipe(new CompressorRecipe(
							OreUtil.getStackFromName("ingot" + OreUtil.capitalizeFirstLetter(ore), 1), plate, 300, 4));
				}
				if (OreUtil.hasGem(ore) && OreUtil.hasDust(ore)) {
					RecipeHandler.addRecipe(new CompressorRecipe(
							OreUtil.getStackFromName("dust" + OreUtil.capitalizeFirstLetter(ore), 1), plate, 300, 4));
				}
				if (OreUtil.hasBlock(ore)) {
					ItemStack morePlates = plate.copy();
					morePlates.setCount(9);
					RecipeHandler.addRecipe(new CompressorRecipe(
							OreUtil.getStackFromName("block" + OreUtil.capitalizeFirstLetter(ore), 1), morePlates, 300, 4));
				}
			}
		}
	}

	static void addGrinderRecipes() {

		// Vanilla
		// int eutick = 2;
		// int ticktime = 300;

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Items.BONE),
			new ItemStack(Items.DYE, 6, 15),
			170, 19));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Blocks.COBBLESTONE),
			new ItemStack(Blocks.SAND),
			230, 23));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Blocks.GRAVEL),
			new ItemStack(Items.FLINT),
			200, 20));

		RecipeHandler.addRecipe(new GrinderRecipe(
				new ItemStack(Items.COAL),
				ItemDusts.getDustByName("coal"),
				230, 27));
		
		RecipeHandler.addRecipe(new GrinderRecipe(
				new ItemStack(Items.COAL, 1, 1),
				ItemDusts.getDustByName("charcoal"),
				230, 27));

		RecipeHandler.addRecipe(new GrinderRecipe(
				new ItemStack(net.minecraft.init.Items.CLAY_BALL),
				ItemDusts.getDustByName("clay"),
				200, 18));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Blocks.GLOWSTONE),
			ItemDusts.getDustByName("glowstone", 4), 220, 21));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Blocks.NETHERRACK),
			ItemDusts.getDustByName("netherrack"),
			300, 27));
		
		RecipeHandler.addRecipe(new GrinderRecipe(
				new ItemStack(Blocks.END_STONE),
				ItemDusts.getDustByName("endstone"),
				300, 16));
		
		RecipeHandler.addRecipe(new GrinderRecipe(
				new ItemStack(Items.ENDER_EYE),
				ItemDusts.getDustByName("ender_eye", 2),
				200, 22));
		
		RecipeHandler.addRecipe(new GrinderRecipe(
				new ItemStack(Items.ENDER_PEARL),
				ItemDusts.getDustByName("ender_pearl", 2),
				200, 22));
		
		RecipeHandler.addRecipe(new GrinderRecipe(
				new ItemStack(Blocks.LAPIS_ORE),
				new ItemStack(Items.DYE, 10, 4),
				170, 19));
		
		RecipeHandler.addRecipe(new GrinderRecipe(
				new ItemStack(Blocks.OBSIDIAN),
				ItemDusts.getDustByName("obsidian", 4),
				170, 19));

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
						data[1].equals("saltpeter")) ||
					oreStack.isEmpty())
					continue;

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
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, dust, ore ? 270 : 200, ore ? 31 : 22, useOreDict));
			}
		}
	}

	static void addVacuumFreezerRecipes() {
		RecipeHandler.addRecipe(new VacuumFreezerRecipe(
			new ItemStack(Blocks.ICE, 2),
			new ItemStack(Blocks.PACKED_ICE),
			60, 100
		));

		RecipeHandler.addRecipe(new VacuumFreezerRecipe(
			ItemIngots.getIngotByName("hot_tungstensteel"),
			ItemIngots.getIngotByName("tungstensteel"),
			440, 120));

		RecipeHandler.addRecipe(new VacuumFreezerRecipe(
			ItemCells.getCellByName("heliumplasma"),
			ItemCells.getCellByName("helium"),
			440, 128));

		RecipeHandler.addRecipe(
			new VacuumFreezerRecipe(
				ItemCells.getCellByName("water"),
				ItemCells.getCellByName("cell"),
				60, 87));
			
	}

	static void addBlastFurnaceRecipes() {
		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(ItemDusts.getDustByName("titanium"), null, ItemIngots.getIngotByName("titanium"),
				null, 3600, 120, 1500));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("titanium", 4), null,
			ItemIngots.getIngotByName("titanium"), null, 3600, 120, 1500));
		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(ItemDusts.getDustByName("aluminum"), null, ItemIngots.getIngotByName("aluminum"),
				null, 2200, 120, 1700));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("aluminum", 4), null,
			ItemIngots.getIngotByName("aluminum"), null, 2200, 120, 1700));
		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(ItemDusts.getDustByName("tungsten"), null, ItemIngots.getIngotByName("tungsten"),
				null, 1800, 120, 2500));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("tungsten", 4), null,
			ItemIngots.getIngotByName("tungsten"), null, 1800, 120, 2500));
		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(ItemDusts.getDustByName("chrome"), null, ItemIngots.getIngotByName("chrome"),
				null, 4420, 120, 1700));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("chrome", 4), null,
			ItemIngots.getIngotByName("chrome"), null, 4420, 120, 1700));
		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(ItemDusts.getDustByName("steel"), null, ItemIngots.getIngotByName("steel"), null,
				2800, 120, 1000));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("steel", 4), null,
			ItemIngots.getIngotByName("steel"), null, 2800, 120, 1000));
		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(ItemDusts.getDustByName("galena", 2), null, ItemIngots.getIngotByName("silver"),
				ItemIngots.getIngotByName("lead"), 80, 120, 1500));
		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(new ItemStack(Items.IRON_INGOT), ItemDusts.getDustByName("coal", 2),
				ItemIngots.getIngotByName("steel"), ItemDusts.getDustByName("dark_ashes", 2), 500, 120, 1000));
		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(ItemIngots.getIngotByName("tungsten"), ItemIngots.getIngotByName("steel"),
				ItemIngots.getIngotByName("hot_tungstensteel"), ItemDusts.getDustByName("dark_ashes", 4), 500,
				128, 3000));
		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(new ItemStack(Blocks.IRON_ORE), ItemDusts.getDustByName("calcite"),
				new ItemStack(Items.IRON_INGOT, 3), ItemDusts.getDustByName("dark_ashes"), 140, 120, 1000));
		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(BlockOre.getOreByName("Pyrite"), ItemDusts.getDustByName("calcite"),
				new ItemStack(Items.IRON_INGOT, 2), ItemDusts.getDustByName("dark_ashes"), 140, 120, 1000));
	}

	static void addIc2Recipes() {
		RebornCraftingHelper.addShapelessOreRecipe(new ItemStack(ModItems.MANUAL), IC2Duplicates.REFINED_IRON.getStackBasedOnConfig(),
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
		// ItemStack(ModBlocks.electricCraftingTable),
		// "ITI", "IBI", "ICI",
		// 'I', "plateIron",
		// 'C', "circuitAdvanced",
		// 'T', "crafterWood",
		// 'B', "machineBlockBasic");

		// RebornCraftingHelper.addShapedOreRecipe(new
		// ItemStack(ModBlocks.electricCraftingTable),
		// "ATA", "ABA", "ACA",
		// 'A', "plateAluminum",
		// 'C', "circuitAdvanced",
		// 'T', "crafterWood",
		// 'B', "machineBlockBasic");

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
