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
import techreborn.api.reactor.FusionReactorRecipe;
import techreborn.api.reactor.FusionReactorRecipeHelper;
import techreborn.api.recipe.machines.*;
import techreborn.blocks.BlockOre;
import techreborn.compat.CompatManager;
import techreborn.config.ConfigTechReborn;
import techreborn.init.recipes.*;
import techreborn.items.*;
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

		addAlloySmelterRecipes();
		addChemicalReactorRecipes();
		addBlastFurnaceRecipes();
		addVacuumFreezerRecipes();
		addReactorRecipes();
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
		RecipeHandler.addRecipe(
			new CompressorRecipe(IC2Duplicates.CARBON_MESH.getStackBasedOnConfig(), ItemPlates.getPlateByName("carbon"), 400,
				2));

		for (String ore : OreUtil.oreNames) {
			if (ore.equals("iridium")) {
				continue;
			}
			if (OreUtil.doesOreExistAndValid("plate" + OreUtil.capitalizeFirstLetter(ore)) && OreUtil.doesOreExistAndValid("ingot" + OreUtil.capitalizeFirstLetter(ore))) {

				RecipeHandler.addRecipe(
					new CompressorRecipe(OreUtil.getStackFromName("ingot" + OreUtil.capitalizeFirstLetter(ore), 1), OreUtil.getStackFromName("plate" + OreUtil.capitalizeFirstLetter(ore), 1), 300,
						4));
			}
			if (OreUtil.doesOreExistAndValid("plate" + OreUtil.capitalizeFirstLetter(ore)) && OreUtil.doesOreExistAndValid("gem" + OreUtil.capitalizeFirstLetter(ore))) {

				RecipeHandler.addRecipe(
					new CompressorRecipe(OreUtil.getStackFromName("gem" + OreUtil.capitalizeFirstLetter(ore), 9), OreUtil.getStackFromName("plate" + OreUtil.capitalizeFirstLetter(ore), 1), 300,
						4));
			}

			if (OreUtil.hasPlate(ore) && OreUtil.hasBlock(ore)) {
				RecipeHandler.addRecipe(
					new CompressorRecipe(OreUtil.getStackFromName("block" + OreUtil.capitalizeFirstLetter(ore), 1), OreUtil.getStackFromName("plate" + OreUtil.capitalizeFirstLetter(ore), 9), 300,
						4));
			}
		}
		RecipeHandler.addRecipe(
			new CompressorRecipe(OreUtil.getStackFromName("plankWood", 1), OreUtil.getStackFromName("plateWood", 1), 300,
				4));
		RecipeHandler.addRecipe(
			new CompressorRecipe(OreUtil.getStackFromName("dustLazurite", 8), OreUtil.getStackFromName("plateLazurite", 1), 300,
				4));
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
			new ItemStack(Blocks.GLOWSTONE),
			ItemDusts.getDustByName("glowstone", 4), 220, 21));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Blocks.NETHERRACK),
			ItemDusts.getDustByName("netherrack"),
			300, 27));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Items.COAL),
			ItemDusts.getDustByName("coal"),
			300, 27));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(net.minecraft.init.Items.CLAY_BALL),
			ItemDusts.getDustByName("clay"),
			200, 18));

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
				RecipeHandler.addRecipe(new GrinderRecipe(oreStack, dust, ore ? 270 : 200, ore ? 31 : 22));
			}
		}

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Items.COAL),
			ItemDusts.getDustByName("coal"),
			120, 10));

		RecipeHandler.addRecipe(new GrinderRecipe(
			new ItemStack(Blocks.END_STONE),
			ItemDusts.getDustByName("endstone"),
			300, 16));
		
	}

	static void addReactorRecipes() {
		FusionReactorRecipeHelper.registerRecipe(
			new FusionReactorRecipe(ItemCells.getCellByName("helium3"), ItemCells.getCellByName("deuterium"),
				ItemCells.getCellByName("heliumplasma"), 40000000, 32768, 1024));
		FusionReactorRecipeHelper.registerRecipe(
			new FusionReactorRecipe(ItemCells.getCellByName("tritium"), ItemCells.getCellByName("deuterium"),
				ItemCells.getCellByName("helium3"), 60000000, 32768, 2048));
		FusionReactorRecipeHelper.registerRecipe(
			new FusionReactorRecipe(ItemCells.getCellByName("wolframium"), ItemCells.getCellByName("Berylium"),
				ItemDusts.getDustByName("platinum"), 80000000, -2048, 1024));
		FusionReactorRecipeHelper.registerRecipe(
			new FusionReactorRecipe(ItemCells.getCellByName("wolframium"), ItemCells.getCellByName("lithium"),
				BlockOre.getOreByName("iridium"), 90000000, -2048, 1024));
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

	static void addAlloySmelterRecipes() {

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
				null, 18000, 120, 2500));
		RecipeHandler.addRecipe(new BlastFurnaceRecipe(ItemDustsSmall.getSmallDustByName("tungsten", 4), null,
			ItemIngots.getIngotByName("tungsten"), null, 18000, 120, 2500));
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
				500, 3000));
		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(new ItemStack(Blocks.IRON_ORE), ItemDusts.getDustByName("calcite"),
				new ItemStack(Items.IRON_INGOT, 3), ItemDusts.getDustByName("dark_ashes"), 140, 120, 1000));
		RecipeHandler.addRecipe(
			new BlastFurnaceRecipe(BlockOre.getOreByName("Pyrite"), ItemDusts.getDustByName("calcite"),
				new ItemStack(Items.IRON_INGOT, 2), ItemDusts.getDustByName("dark_ashes"), 140, 120, 1000));
	}

	static void addChemicalReactorRecipes() {
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(ItemCells.getCellByName("calcium", 1), ItemCells.getCellByName("carbon", 1),
				ItemCells.getCellByName("calciumCarbonate", 2), 240, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(new ItemStack(Items.GOLD_NUGGET, 8), new ItemStack(Items.MELON, 1),
				new ItemStack(Items.SPECKLED_MELON, 1), 40, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(ItemCells.getCellByName("nitrogen", 1), ItemCells.getCellByName("carbon", 1),
				ItemCells.getCellByName("nitrocarbon", 2), 1500, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(ItemCells.getCellByName("carbon", 1), ItemCells.getCellByName("hydrogen", 4),
				ItemCells.getCellByName("methane", 5), 3500, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(ItemCells.getCellByName("sulfur", 1), ItemCells.getCellByName("sodium", 1),
				ItemCells.getCellByName("sodiumSulfide", 2), 100, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(new ItemStack(Items.BLAZE_POWDER, 1), new ItemStack(Items.ENDER_PEARL, 1),
				new ItemStack(Items.ENDER_EYE, 1), 40, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(new ItemStack(Items.GOLD_NUGGET, 8), new ItemStack(Items.CARROT, 1),
				new ItemStack(Items.GOLDEN_CARROT, 1), 40, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(ItemCells.getCellByName("glyceryl", 1), ItemCells.getCellByName("diesel", 4),
				ItemCells.getCellByName("nitroDiesel", 5), 1000, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(new ItemStack(Items.GOLD_INGOT, 8), new ItemStack(Items.APPLE, 1),
				new ItemStack(Items.GOLDEN_APPLE, 1), 40, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(new ItemStack(Blocks.GOLD_BLOCK, 8), new ItemStack(Items.APPLE, 1),
				new ItemStack(Items.GOLDEN_APPLE, 1, 1), 40, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(new ItemStack(Items.BLAZE_POWDER, 1), new ItemStack(Items.SLIME_BALL, 1),
				new ItemStack(Items.MAGMA_CREAM, 1), 40, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(ItemDusts.getDustByName("calcite", 1), null,
				new ItemStack(getOre("fertilizer").getItem(), 1), 100, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(ItemDusts.getDustByName("calcite", 1),
				ItemDusts.getDustByName("phosphorous", 1),
				new ItemStack(getOre("fertilizer").getItem(), 3), 100, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(ItemCells.getCellByName("sodiumSulfide", 1),
				ItemCells.getCellByName("empty"), ItemCells.getCellByName("sodiumPersulfate", 2), 2000,
				30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(ItemCells.getCellByName("nitrocarbon", 1),
				ItemCells.getCellByName("water"), ItemCells.getCellByName("glyceryl", 2), 580, 30));
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(ItemDusts.getDustByName("calcite", 1), ItemDusts.getDustByName("sulfur", 1),
					new ItemStack(getOre("fertilizer").getItem(), 2), 100, 30));

		ItemStack waterCells = ItemCells.getCellByName("water").copy();
		waterCells.setCount(2);
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(ItemCells.getCellByName("sulfur", 1), waterCells,
				ItemCells.getCellByName("sulfuricAcid", 3), 1140, 30));

		ItemStack waterCells2 = ItemCells.getCellByName("water").copy();
		waterCells2.setCount(5);
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(ItemCells.getCellByName("hydrogen", 4),
				ItemCells.getCellByName("empty"), waterCells2, 10, 30));
		
		RecipeHandler.addRecipe(
			new ChemicalReactorRecipe(ItemCells.getCellByName("nitrogen", 1),
				ItemCells.getCellByName("empty"), ItemCells.getCellByName("nitrogenDioxide", 2), 1240,
				30));
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

		//		RebornCraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.dragonEggEnergySiphoner), "CTC", "ISI", "CBC", 'I',
		//			"plateIridium", 'C', "circuitBasic",
		//			'B', ModItems.lithiumBattery, 'S', ModBlocks.Supercondensator, 'T', ModBlocks.extractor);

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

		//		RebornCraftingHelper.addShapedOreRecipe(new ItemStack(ModBlocks.PlasmaGenerator), "PPP", "PTP", "CGC", 'P',
		//			ItemPlates.getPlateByName("tungstensteel"), 'T', IC2Duplicates.HVT.getStackBasedOnConfig(),
		//			'G', IC2Duplicates.GENERATOR.getStackBasedOnConfig(), 'C',
		//			"circuitMaster");

		// Smetling
		RebornCraftingHelper
			.addSmelting(ItemDusts.getDustByName("copper", 1), getOre("ingotCopper"),
				1F);
		RebornCraftingHelper
			.addSmelting(ItemDusts.getDustByName("tin", 1), ItemIngots.getIngotByName("tin"), 1F);
		RebornCraftingHelper
			.addSmelting(ItemDusts.getDustByName("bronze", 1), ItemIngots.getIngotByName("bronze"),
				1F);
		RebornCraftingHelper
			.addSmelting(ItemDusts.getDustByName("lead", 1), ItemIngots.getIngotByName("lead"), 1F);
		RebornCraftingHelper
			.addSmelting(ItemDusts.getDustByName("silver", 1), ItemIngots.getIngotByName("silver"),
				1F);
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
