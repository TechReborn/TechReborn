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

/**
 * Created by Prospector
 */
public class CraftingTableRecipes extends RecipeMethods {
	/* TODO 1.13 :D

	public static void init() {

		registerCompressionRecipes();
		registerMixedMetalIngotRecipes();
			
//		registerShapeless(BlockStorage2.getStorageBlockByName("iridium_reinforced_stone", 1), "stone", "plateIridiumAlloy");
//		registerShapeless(BlockStorage2.getStorageBlockByName("iridium_reinforced_tungstensteel", 1), BlockStorage2.getStorageBlockByName("tungstensteel", 1), "plateIridium");
//		registerShapeless(BlockStorage2.getStorageBlockByName("iridium_reinforced_tungstensteel", 1), BlockStorage2.getStorageBlockByName("iridium_reinforced_stone", 1), "ingotTungstensteel");
		registerShapeless(getStack(TRContent.RUBBER_PLANKS, 4), getStack(TRContent.RUBBER_LOG));
		registerShaped(DynamicCell.getEmptyCell(16), " T ", "T T", " T ", 'T', "ingotTin");
		registerShaped(getStack(TRContent.REFINED_IRON_FENCE), "RRR", "RRR", 'R', "ingotRefinedIron");
		registerShaped(getStack(TRContent.REINFORCED_GLASS, 7), "GAG", "GGG", "GAG", 'A', "plateAdvancedAlloy", 'G', "blockGlass");
		registerShaped(getStack(TRContent.REINFORCED_GLASS, 7), "GGG", "AGA", "GGG", 'A', "plateAdvancedAlloy", 'G', "blockGlass");
//		registerShaped(getMaterial("iridium_alloy", Type.INGOT), "IAI", "ADA", "IAI", 'I', "ingotIridium", 'D', "dustDiamond", 'A', "plateAdvancedAlloy");

		// Tools and devices		
		registerShaped(getStack(TRContent.WRENCH), "BNB", "NBN", " B ", 'B', "ingotBronze", 'N', "nuggetBronze");
		registerShaped(getStack(TRContent.TREE_TAP), " S ", "PPP", "P  ", 'S', "stickWood", 'P', "plankWood");
		registerShaped(getStack(TRContent.ELECTRIC_TREE_TAP), "TB", "  ", 'T', getStack(TRContent.TREE_TAP), 'B', "reBattery");
		registerShaped(getStack(TRContent.NANOSABER), "DC ", "DC ", "GLG", 'L', "lapotronCrystal", 'C', "plateCarbon", 'D', "plateDiamond", 'G', "dustsmallGlowstone");
		ItemStack rockCutter = getStack(TRContent.ROCK_CUTTER);
		rockCutter.addEnchantment(Enchantments.SILK_TOUCH, 1);
		registerShaped(rockCutter, "DT ", "DT ", "DCB", 'D', "dustDiamond", 'T', "ingotTitanium", 'C', "circuitBasic", 'B', "reBattery");
		registerShaped(getStack(TRContent.BASIC_DRILL), " S ", "SCS", "SBS", 'S', "ingotSteel", 'C', "circuitBasic", 'B', "reBattery");
		registerShaped(getStack(TRContent.ADVANCED_DRILL), " D ", "DCD", "TST", 'D', "gemDiamond", 'C', "circuitAdvanced", 'S', getStack(TRContent.BASIC_DRILL, 1, OreDictionary.WILDCARD_VALUE), 'T', "ingotTitanium");
		registerShaped(getStack(TRContent.INDUSTRIAL_DRILL), " I ", "NCN", "OAO", 'I', "plateIridiumAlloy", 'N', "nuggetIridium", 'A', getStack(TRContent.ADVANCED_DRILL, 1, OreDictionary.WILDCARD_VALUE), 'C', "circuitMaster", 'O', getMaterial("overclock", Type.UPGRADE));
		registerShaped(getStack(TRContent.BASIC_CHAINSAW), " SS", "SCS", "BS ", 'S', "ingotSteel", 'C', "circuitBasic", 'B', "reBattery");
		registerShaped(getStack(TRContent.ADVANCED_CHAINSAW), " DD", "TCD", "ST ", 'D', "gemDiamond", 'C', "circuitAdvanced", 'S', getStack(TRContent.BASIC_CHAINSAW, 1, OreDictionary.WILDCARD_VALUE), 'T', "ingotTitanium");
		registerShaped(getStack(TRContent.INDUSTRIAL_CHAINSAW), " NI", "OCN", "DO ", 'I', "plateIridiumAlloy", 'N', "nuggetIridium", 'D', getStack(TRContent.ADVANCED_CHAINSAW, 1, OreDictionary.WILDCARD_VALUE), 'C', "circuitMaster", 'O', getMaterial("overclock", Type.UPGRADE));
		registerShaped(getStack(TRContent.BASIC_JACKHAMMER), "SBS", "SCS", " S ", 'S', "ingotSteel", 'C', "circuitBasic", 'B', "reBattery");
		registerShaped(getStack(TRContent.ADVANCED_JACKHAMMER), "DSD", "TCT", " D ", 'D', "gemDiamond", 'C', "circuitAdvanced", 'S', getStack(TRContent.BASIC_JACKHAMMER, 1, OreDictionary.WILDCARD_VALUE), 'T', "ingotTitanium");
		registerShaped(getStack(TRContent.INDUSTRIAL_JACKHAMMER), "NDN", "OCO", " I ", 'I', "plateIridiumAlloy", 'N', "nuggetIridium", 'D', getStack(TRContent.ADVANCED_JACKHAMMER, 1, OreDictionary.WILDCARD_VALUE), 'C', "circuitMaster", 'O', getMaterial("overclock", Type.UPGRADE));
		registerShaped(getStack(TRContent.CLOAKING_DEVICE), "CIC", "IOI", "CIC", 'C', "ingotChrome", 'I', "plateIridiumAlloy", 'O', getStack(TRContent.LAPOTRONIC_ORB));
		registerShaped(getStack(TRContent.LAPOTRONIC_ORBPACK), "FOF", "SPS", "FIF", 'F', "circuitMaster", 'O', getStack(TRContent.LAPOTRONIC_ORB), 'S', "craftingSuperconductor", 'I', "ingotIridium", 'P', getStack(TRContent.LITHIUM_ION_BATPACK));
//		registerShaped(getStack(TRContent.RED_CELL_BATTERY), " W ", "TRT", "TRT", 'T', "ingotTin", 'R', "dustRedstone", 'W', EnumCableType.ICOPPER.getStack());
//		registerShaped(getStack(TRContent.LITHIUM_ION_BATTERY), " C ", "PFP", "PFP", 'F', getCell("lithium"), 'P', "plateAluminum", 'C', EnumCableType.IGOLD.getStack());
		registerShaped(getStack(TRContent.LITHIUM_ION_BATPACK),	"BCB", "BPB", "B B", 'B', getStack(TRContent.LITHIUM_ION_BATTERY), 'P', "plateAluminum", 'C', "circuitAdvanced");
		registerShaped(getStack(TRContent.ENERGY_CRYSTAL), "RRR", "RDR", "RRR", 'R', "dustRedstone", 'D', "gemDiamond");
		registerShaped(getStack(TRContent.LAPOTRON_CRYSTAL), "LCL", "LEL", "LCL", 'L', "dyeBlue", 'E', "energyCrystal", 'C', "circuitBasic");
		registerShaped(getStack(TRContent.LAPOTRONIC_ORB), "LLL", "LPL", "LLL", 'L', "lapotronCrystal", 'P', "plateIridiumAlloy");
		registerShaped(getStack(TRContent.SCRAP_BOX), "SSS", "SSS", "SSS", 'S', TRContent.Parts.SCRAP.getStack());
		//registerShapeless(getStack(TRContent.FREQUENCY_TRANSMITTER), EnumCableType.ICOPPER.getStack(), "circuitBasic");

		if (ConfigTechReborn.enableGemArmorAndTools) {
			addToolAndArmourRecipes(getStack(TRContent.RUBY_SWORD), getStack(TRContent.RUBY_PICKAXE), getStack(TRContent.RUBY_AXE), getStack(TRContent.RUBY_HOE), getStack(TRContent.RUBY_SPADE), getStack(TRContent.RUBY_HELMET), getStack(TRContent.RUBY_CHESTPLATE), getStack(TRContent.RUBY_LEGGINGS), getStack(TRContent.RUBY_BOOTS), "gemRuby");
			addToolAndArmourRecipes(getStack(TRContent.SAPPHIRE_SWORD), getStack(TRContent.SAPPHIRE_PICKAXE), getStack(TRContent.SAPPHIRE_AXE), getStack(TRContent.SAPPHIRE_HOE), getStack(TRContent.SAPPHIRE_SPADE), getStack(TRContent.SAPPHIRE_HELMET), getStack(TRContent.SAPPHIRE_CHESTPLATE), getStack(TRContent.SAPPHIRE_LEGGINGS), getStack(TRContent.SAPPHIRE_BOOTS), "gemSapphire");
			addToolAndArmourRecipes(getStack(TRContent.PERIDOT_SWORD), getStack(TRContent.PERIDOT_PICKAXE), getStack(TRContent.PERIDOT_AXE), getStack(TRContent.PERIDOT_HOE), getStack(TRContent.PERIDOT_SPADE), getStack(TRContent.PERIDOT_HELMET), getStack(TRContent.PERIDOT_CHESTPLATE), getStack(TRContent.PERIDOT_LEGGINGS), getStack(TRContent.PERIDOT_BOOTS), "gemPeridot");
			addToolAndArmourRecipes(getStack(TRContent.BRONZE_SWORD), getStack(TRContent.BRONZE_PICKAXE), getStack(TRContent.BRONZE_AXE), getStack(TRContent.BRONZE_HOE), getStack(TRContent.BRONZE_SPADE), getStack(TRContent.BRONZE_HELMET), getStack(TRContent.BRONZE_CHESTPLATE), getStack(TRContent.BRONZE_LEGGINGS), getStack(TRContent.BRONZE_BOOTS), "ingotBronze");
		}
		
		//Upgrades
//		registerShaped(ItemUpgrades.getUpgradeByName("energy_storage"), "PPP", "WBW", "PCP", 'P', "plankWood", 'W', EnumCableType.ICOPPER.getStack(), 'C', "circuitBasic", 'B', "reBattery");
//		registerShaped(ItemUpgrades.getUpgradeByName("overclock"), "TTT", "WCW", 'T', TRIngredients.Parts.COOLANT_SIMPLE.getStack(), 'W', EnumCableType.ICOPPER.getStack(), 'C', "circuitBasic");
//		registerShaped(ItemUpgrades.getUpgradeByName("overclock", 2), " T ", "WCW", 'T', TRIngredients.Parts.HELIUM_COOLANT_TRIPLE.getStack(), 'W', EnumCableType.ICOPPER.getStack(), 'C', "circuitBasic");
//		registerShaped(ItemUpgrades.getUpgradeByName("overclock", 2),  " T ", "WCW", 'T', TRIngredients.Parts.NAK_COOLANT_SIMPLE.getStack(), 'W', EnumCableType.ICOPPER.getStack(), 'C', "circuitBasic");
//		registerShaped(ItemUpgrades.getUpgradeByName("transformer"), "GGG", "WTW", "GCG", 'G', "blockGlass", 'W', EnumCableType.IGOLD.getStack(), 'C', "circuitBasic", 'T', getStack(ModBlocks.MV_TRANSFORMER));

		//Machines
//		registerShapeless(getStack(TRContent.SOLID_FUEL_GENEREATOR), "reBattery", "machineBlockBasic", getStack(Blocks.FURNACE));
//		registerShaped(getStack(TRContent.SEMI_FLUID_GENERATOR), "III", "IHI", "CGC", 'I', "plateIron", 'H', "glassReinforced", 'C', "circuitBasic", 'G', getStack(TRContent.SOLID_FUEL_GENEREATOR));
//		registerShaped(getStack(TRContent.SEMI_FLUID_GENERATOR), "III", "IHI", "CGC", 'I', "plateAluminum", 'H', "glassReinforced", 'C', "circuitBasic", 'G', getStack(TRContent.SOLID_FUEL_GENEREATOR));
//		registerShaped(getStack(TRContent.DIESEL_GENERATOR), "III", "I I", "CGC", 'I', "ingotRefinedIron", 'C', "circuitBasic", 'G', getStack(TRContent.SOLID_FUEL_GENEREATOR));
//		registerShaped(getStack(TRContent.DIESEL_GENERATOR), "III", "I I", "CGC", 'I', "plateAluminum", 'C', "circuitBasic", 'G', getStack(TRContent.SOLID_FUEL_GENEREATOR));
//		registerShaped(getStack(TRContent.GAS_TURBINE), "IAI", "WGW", "IAI", 'I', "plateInvar", 'A', "circuitAdvanced", 'W', getStack(TRContent.WIND_MILL), 'G', "glassReinforced");
//		registerShaped(getStack(TRContent.GAS_TURBINE), "IAI", "WGW", "IAI", 'I', "plateAluminum", 'A', "circuitAdvanced", 'W', getStack(TRContent.WIND_MILL), 'G', "glassReinforced");
//		registerShaped(getStack(TRContent.THERMAL_GENERATOR), "III", "IRI", "CGC", 'I', "plateInvar", 'R', "glassReinforced", 'G', getStack(TRContent.SOLID_FUEL_GENEREATOR), 'C', "circuitBasic");
//		registerShaped(getStack(TRContent.WIND_MILL), " I ", " G ", " I ", 'I', "plateMagnalium", 'G', getStack(TRContent.SOLID_FUEL_GENEREATOR));
//		registerShaped(getStack(TRContent.WIND_MILL), "IGI", 'I', "plateMagnalium", 'G', getStack(TRContent.SOLID_FUEL_GENEREATOR));
//		registerShaped(getStack(TRContent.WATER_MILL), "SWS", "WGW", "SWS", 'S', "stickWood", 'W', "plankWood", 'G', getStack(TRContent.SOLID_FUEL_GENEREATOR));
//		registerShaped(getStack(ModBlocks.LIGHTNING_ROD), "CAC", "ACA", "CAC", 'A', getStack(ModBlocks.MACHINE_CASINGS, 1, 2), 'C', "circuitMaster");
		registerShaped(TRContent.Machine.IRON_ALLOY_FURNACE.getStack(), "III", "F F", "III", 'I', "ingotRefinedIron", 'F', TRContent.Machine.IRON_FURNACE.getStack());
		registerShaped(TRContent.Machine.INDUSTRIAL_ELECTROLYZER.getStack(), "RER", "CFC", "RER", 'R', "plateIron", 'E', TRContent.Machine.EXTRACTOR.getStack(), 'C', "circuitAdvanced", 'F', "machineBlockAdvanced");
//		registerShaped(getStack(TRContent.INDUSTRIAL_CENTRIFUGE), "RCR", "AEA", "RCR", 'R', "ingotRefinedIron", 'E', getStack(TRContent.EXTRACTOR), 'A', "machineBlockAdvanced", 'C', "circuitAdvanced");
//		registerShaped(getStack(TRContent.INDUSTRIAL_CENTRIFUGE), "RCR", "AEA", "RCR", 'R', "plateAluminum", 'E', getStack(TRContent.EXTRACTOR), 'A', "machineBlockAdvanced", 'C', "circuitAdvanced");
//		registerShaped(getStack(TRContent.INDUSTRIAL_SAWMILL), "PAP", "SSS", "ACA", 'P', "ingotRefinedIron", 'A', "circuitAdvanced", 'S', TRContent.Parts.DIAMOND_SAW_BLADE.getStack(), 'C', "machineBlockAdvanced");
//		registerShaped(getStack(TRContent.INDUSTRIAL_BLAST_FURNACE), "CHC", "HBH", "FHF", 'H', getMaterial("cupronickelHeatingCoil", Type.PART), 'C', "circuitAdvanced", 'B', "machineBlockAdvanced", 'F', getStack(TRContent.ELECTRIC_FURNACE));
//		registerShaped(getStack(TRContent.INDUSTRIAL_GRINDER), "ECG", "HHH", "CBC", 'E', getStack(TRContent.INDUSTRIAL_ELECTROLYZER), 'H', "craftingDiamondGrinder", 'C', "circuitAdvanced", 'B', "machineBlockAdvanced", 'G', getStack(TRContent.GRINDER));
//		registerShaped(getStack(ModBlocks.IMPLOSION_COMPRESSOR), "ABA", "CPC", "ABA", 'A', getMaterialObject("advancedAlloy", Type.INGOT), 'C', "circuitAdvanced", 'B', "machineBlockAdvanced", 'P', getStack(ModBlocks.COMPRESSOR));
//		registerShaped(getStack(TRContent.VACUUM_FREEZER), "SPS", "CGC", "SPS", 'S', "plateSteel", 'C', "circuitAdvanced", 'G', "glassReinforced", 'P', getStack(TRContent.EXTRACTOR));
//		registerShaped(getStack(TRContent.EnvTypeILLATION_TOWER), "CMC", "PBP", "EME", 'E', getStack(TRContent.INDUSTRIAL_ELECTROLYZER), 'M', "circuitMaster", 'B', "machineBlockElite", 'C', getStack(TRContent.INDUSTRIAL_CENTRIFUGE), 'P', getStack(TRContent.EXTRACTOR));
//		registerShaped(getStack(TRContent.CHEMICAL_REACTOR), "IMI", "CPC", "IEI", 'I', "plateInvar", 'C', "circuitAdvanced", 'M', getStack(TRContent.EXTRACTOR), 'P', getStack(TRContent.COMPRESSOR), 'E', getStack(TRContent.EXTRACTOR));
//		registerShaped(getStack(TRContent.ROLLING_MACHINE),  "PCP", "MBM", "PCP", 'P', getStack(Blocks.PISTON), 'C', "circuitAdvanced", 'M', getStack(TRContent.COMPRESSOR), 'B', "machineBlockBasic");
//		registerShaped(getStack(TRContent.AUTO_CRAFTING_TABLE), "MPM", "PCP", "MPM", 'M', "circuitAdvanced", 'C', "workbench", 'P', "plateIron");
		registerShaped(TRContent.Machine.CHARGE_O_MAT.getStack(),  "ETE", "COC", "EAE", 'E', "circuitMaster", 'T', "energyCrystal", 'C', "chest", 'O', getStack(TRContent.LAPOTRONIC_ORB), 'A', "machineBlockAdvanced");
//		registerShaped(getStack(TRContent.ALLOY_SMELTER), " C ", "FMF", "   ", 'C', "circuitBasic", 'F', getStack(TRContent.ELECTRIC_FURNACE), 'M', "machineBlockBasic");
//		registerShaped(getStack(TRContent.INTERDIMENSIONAL_SU), "PAP", "ACA", "PAP", 'P', "plateIridiumAlloy", 'C', "chestEnder", 'A', getStack(TRContent.ADJUSTABLE_SU));
//		registerShaped(getStack(TRContent.ADJUSTABLE_SU), "LLL", "LCL", "LLL", 'L', getStack(TRContent.LAPOTRONIC_ORB), 'C', "energyCrystal");
//		registerShaped(getStack(TRContent.LAPOTRONIC_SU),  " L ", "CBC", " M ", 'L', getStack(TRContent.LV_TRANSFORMER), 'C', "circuitAdvanced", 'M', getStack(TRContent.MV_TRANSFORMER), 'B', getStack(TRContent.LSU_STORAGE));
		registerShaped(TRContent.Machine.LSU_STORAGE.getStack(), "LLL", "LCL", "LLL", 'L', "blockLapis", 'C', "circuitBasic");
//		registerShaped(getStack(TRContent.SCRAPBOXINATOR), "ICI", "DSD", "ICI", 'S', getStack(TRContent.SCRAP_BOX), 'C', "circuitBasic", 'I', "plateIron", 'D', "dirt");
//		registerShaped(getStack(TRContent.FUSION_CONTROL_COMPUTER), "CCC", "PTP", "CCC", 'P', "energyCrystal", 'T', getStack(TRContent.FUSION_COIL), 'C', "circuitMaster");
//		registerShaped(getStack(ModBlocks.FUSION_COIL), "CSC", "NAN", "CRC", 'A', getStack(ModBlocks.MACHINE_CASINGS, 1, 2), 'N', getMaterial("nichromeHeatingCoil", Type.PART), 'C', "circuitMaster", 'S', "craftingSuperconductor", 'R', getMaterial("iridiumNeutronReflector", Type.PART));
//		registerShaped(getStack(TRContent.DIGITAL_CHEST), "PPP", "PDP", "PCP", 'P', "plateAluminum",  'D', TRContent.Parts.DATA_STORAGE_CORE.getStack(), 'C', TRContent.Parts.DIGITAL_DISPLAY.getStack());
//		registerShaped(getStack(TRContent.DIGITAL_CHEST), "PPP", "PDP", "PCP", 'P', "plateSteel",  'D', TRContent.Parts.DATA_STORAGE_CORE.getStack(), 'C', TRContent.Parts.DIGITAL_DISPLAY.getStack());
//		registerShaped(getStack(TRContent.MATTER_FABRICATOR), "ETE", "AOA", "ETE", 'E', "circuitMaster", 'T', getStack(TRContent.EXTRACTOR), 'A', "machineBlockElite", 'O', getStack(TRContent.LAPOTRONIC_ORB));
		registerShaped(getStack(TRContent.COMPUTER_CUBE), "OMC", "MFM", "CMO", 'O', TRContent.Parts.DATA_STORAGE_CORE.getStack(), 'M', TRContent.Parts.DIGITAL_DISPLAY.getStack(), 'C', "circuitMaster", 'F', "machineBlockAdvanced");
//		registerShaped(getStack(TRContent.PLAYER_DETECTOR, true), " D ", "CFC", " D ", 'D', "circuitStorage", 'C', "circuitAdvanced", 'F',  getStack(TRContent.COMPUTER_CUBE));
//		registerShaped(getStack(TRContent.DRAGON_EGG_SYPHON), "CTC", "PSP", "CBC", 'C', "circuitMaster", 'T', getStack(TRContent.MEDIUM_VOLTAGE_SU), 'P', "plateIridiumAlloy", 'S', "craftingSuperconductor", 'B', getStack(TRContent.LAPOTRONIC_ORB));
//		registerShaped(getStack(TRContent.PLASMA_GENERATOR), "PPP", "PTP", "CGC", 'P', "plateTungstensteel", 'T', getStack(TRContent.HV_TRANSFORMER), 'C', "circuitMaster", 'G', getStack(TRContent.SOLID_FUEL_GENEREATOR));
//		registerShaped(getStack(ModBlocks.SOLAR_PANEL, 1, 0), "DLD", "LDL", "CGC", 'D', "dustCoal", 'L', "paneGlass", 'G', getStack(ModBlocks.SOLID_FUEL_GENEREATOR), 'C', "circuitBasic");
//		registerShaped(getStack(ModBlocks.SOLAR_PANEL, 1, 1), "DLD", "LDL", "CPC", 'D', "dustCoal", 'L', "blockGlass", 'C', "circuitAdvanced", 'P', getStack(ModBlocks.SOLAR_PANEL, 1, 0));
//		registerShaped(getStack(ModBlocks.SOLAR_PANEL, 1, 1), "DLD", "LDL", "CPC", 'D', "dustCoal", 'L', "blockGlass", 'C', "circuitAdvanced", 'P', "machineBlockBasic");
//		registerShaped(getStack(ModBlocks.SOLAR_PANEL, 1, 2), "DLD", "LDL", "CPC", 'D', "dustDiamond", 'L', "blockGlass", 'C', "circuitAdvanced", 'P', getStack(ModBlocks.SOLAR_PANEL, 1, 1));
//		registerShaped(getStack(ModBlocks.SOLAR_PANEL, 1, 2), "DLD", "LDL", "CPC", 'D', "dustDiamond", 'L', "blockGlass", 'C', "circuitAdvanced", 'P', "machineBlockBasic");
//		registerShaped(getStack(ModBlocks.SOLAR_PANEL, 1, 3), "DLD", "LDL", "CPC", 'D', "dustDiamond", 'L', "glassReinforced", 'C', "circuitAdvanced", 'P', getStack(ModBlocks.SOLAR_PANEL, 1, 2));
//		registerShaped(getStack(ModBlocks.SOLAR_PANEL, 1, 3), "DLD", "LDL", "CPC", 'D', "dustDiamond", 'L', "glassReinforced", 'C', "circuitAdvanced", 'P', "machineBlockAdvanced");
//		registerShaped(getStack(ModBlocks.SOLAR_PANEL, 1, 4), "DLD", "LDL", "CPC", 'D', "dustDiamond", 'L', "glassReinforced", 'C', "circuitMaster", 'P', getStack(ModBlocks.SOLAR_PANEL, 1, 3));
//		registerShaped(getStack(ModBlocks.SOLAR_PANEL, 1, 4), "DLD", "LDL", "CPC", 'D', "dustDiamond", 'L', "glassReinforced", 'C', "circuitMaster", 'P', "machineBlockElite");
//		registerShaped(getStack(ModBlocks.ALARM, 1, 0), "ICI", "SRS", "ICI", 'I', "ingotIron", 'C', getMaterial("copper", Type.CABLE), 'S', getMaterial("insulatedcopper", Type.CABLE), 'R', "blockRedstone" );
//		registerShaped(getStack(TRContent.FLUID_REPLICATOR), "PCP", "CFC", "ESR", 'P', "plateTungstensteel", 'F', "machineBlockElite", 'C', "circuitMaster", 'E', getStack(TRContent.INDUSTRIAL_ELECTROLYZER), 'S', "craftingSuperconductor",'R', getStack(TRContent.CHEMICAL_REACTOR));
//		registerShaped(getStack(ModBlocks.HV_TRANSFORMER), " H ", " M ", " H ", 'M', getStack(ModBlocks.MV_TRANSFORMER), 'H', EnumCableType.IHV.getStack());
//		registerShaped(getStack(ModBlocks.MV_TRANSFORMER), " G ", " M ", " G ", 'M', "machineBlockBasic", 'G', EnumCableType.IGOLD.getStack());
//		registerShaped(getStack(ModBlocks.LV_TRANSFORMER), "PWP", "CCC", "PPP", 'P', "plankWood", 'C', "ingotCopper", 'W', EnumCableType.ICOPPER.getStack());
//		registerShaped(getStack(ModBlocks.LOW_VOLTAGE_SU), "WCW", "BBB", "WWW", 'W', "plankWood", 'B', "reBattery", 'C', EnumCableType.ICOPPER.getStack());
//		registerShaped(getStack(ModBlocks.MEDIUM_VOLTAGE_SU), "GEG", "EME", "GEG", 'M', "machineBlockBasic", 'E', "energyCrystal", 'G', EnumCableType.IGOLD.getStack());
//		registerShaped(getStack(ModBlocks.HIGH_VOLTAGE_SU), "LAL", "LML", "LOL", 'A', "circuitAdvanced", 'L', "lapotronCrystal", 'M', getStack(ModBlocks.MEDIUM_VOLTAGE_SU), 'O', "machineBlockAdvanced");
//		registerShaped(getStack(TRContent.COMPRESSOR), "S S", "SCS", "SMS", 'C', "circuitBasic", 'M', "machineBlockBasic", 'S', "stone");
//		registerShaped(getStack(TRContent.ELECTRIC_FURNACE), " C ", "RFR", "   ", 'C', "circuitBasic", 'F', getStack(TRContent.IRON_FURNACE), 'R', "dustRedstone");
//		registerShaped(getStack(TRContent.RECYCLER), " E ", "DCD", "GDG", 'D', "dirt", 'C', getStack(TRContent.COMPRESSOR), 'G', "dustGlowstone", 'E', "circuitBasic");
		registerShaped(TRContent.Machine.IRON_FURNACE.getStack(), "III", "I I", "III", 'I', "ingotIron");
//		registerShaped(getStack(TRContent.IRON_FURNACE), " I ", "I I", "IFI", 'I', "ingotIron", 'F', getStack(Blocks.FURNACE));
//		registerShaped(getStack(TRContent.EXTRACTOR), "TMT", "TCT", "   ", 'T', getStack(TRContent.TREE_TAP, true), 'M', "machineBlockBasic", 'C', "circuitBasic");
//		registerShaped(getStack(TRContent.GRINDER), "FFF", "SMS", " C ", 'F', Items.FLINT, 'S', getStack(Blocks.COBBLESTONE), 'M', "machineBlockBasic", 'C', "circuitBasic");
//		registerShaped(getStack(TRContent.QUANTUM_CHEST), "DCD", "ATA", "DQD", 'D', TRContent.Parts.DATA_STORAGE_CORE.getStack(), 'C', TRContent.Parts.DIGITAL_DISPLAY.getStack(), 'A', "machineBlockElite", 'Q', getStack(TRContent.DIGITAL_CHEST), 'T', getStack(TRContent.COMPRESSOR));
//		registerShaped(getStack(TRContent.QUANTUM_TANK), "EPE", "PCP", "EPE", 'P', "platePlatinum", 'E', "circuitAdvanced", 'C', getStack(TRContent.QUANTUM_CHEST));
//		registerShaped(getStack(TRContent.LAMP_INCANDESCENT), "GGG", "TCT", "GGG", 'G', "paneGlass", 'T', getMaterial("copper", Type.CABLE), 'C', TRContent.Parts.CARBON_FIBER.getStack());
		registerShaped(TRContent.Machine.LAMP_LED.getStack(), "GGG", "TLT", "GGG", 'G', "paneGlass", 'T', TRContent.Cables.TIN.getStack(), 'L', "dustGlowstone");


		// Machine Frames
		registerShaped(getMaterial("machine", Type.MACHINE_FRAME), "AAA", "A A", "AAA", 'A', "ingotRefinedIron");
		registerShaped(getMaterial("advanced_machine", Type.MACHINE_FRAME), " C ", "AMA", " C ", 'A', "plateAdvancedAlloy", 'C', "plateCarbon", 'M', "machineBlockBasic");
		registerShaped(getMaterial("highly_advanced_machine", Type.MACHINE_FRAME), "CTC", "TBT", "CTC", 'C', "plateChrome", 'T', "plateTitanium", 'B', "machineBlockAdvanced");

		// Multiblock casings
		registerShaped(getMaterial("standard", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "ingotRefinedIron", 'C', "circuitBasic", 'A', "machineBlockBasic");
		registerShaped(getMaterial("standard", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "plateIron", 'C', "circuitBasic", 'A', "machineBlockBasic");
		registerShaped(getMaterial("standard", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "plateAluminum", 'C', "circuitBasic", 'A', "machineBlockBasic");
		registerShaped(getMaterial("reinforced", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "plateSteel", 'C', "circuitAdvanced", 'A', "machineBlockAdvanced");
		registerShaped(getMaterial("reinforced", 1, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "plateSteel", 'C', "circuitAdvanced", 'A', getMaterial("standard", Type.MACHINE_CASING));
		registerShaped(getMaterial("advanced", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R',  "plateChrome", 'C', "circuitElite", 'A', "machineBlockElite");
		registerShaped(getMaterial("advanced", 1, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R',  "plateChrome", 'C', "circuitElite", 'A', getMaterial("reinforced", Type.MACHINE_CASING));
		
		// Parts
//		registerShaped(getMaterial("data_storage_circuit", Type.PART), "RGR", "LCL", "EEE", 'R', "dustRedstone", 'G', "dustGlowstone", 'L', "gemLapis", 'C', "circuitBasic", 'E', "plateEmerald");
//		registerShaped(getMaterial("data_control_circuit", Type.PART), "ADA", "DID", "ADA", 'I', "ingotIridium", 'A', "circuitAdvanced", 'D', "circuitStorage");
//		registerShaped(getMaterial("energy_flow_circuit", 4, Type.PART), "ATA", "LIL", "ATA", 'T', "ingotTungsten", 'I', "plateIridiumAlloy", 'A', "circuitAdvanced", 'L', "lapotronCrystal");
//		registerShaped(getMaterial("data_orb", Type.PART),  "DDD", "DSD", "DDD", 'D', "circuitStorage", 'S', "circuitElite");
//		registerShaped(getMaterial("diamond_saw_blade", 4, Type.PART), "DSD", "S S", "DSD", 'D', "dustDiamond", 'S', "ingotSteel");
//		registerShaped(getMaterial("diamond_grinding_head", 2, Type.PART), "DSD", "SGS", "DSD", 'S', "ingotSteel", 'D', "dustDiamond", 'G', "gemDiamond");
//		registerShaped(getMaterial("tungsten_grinding_head", 2, Type.PART), "TST", "SBS", "TST", 'S', "ingotSteel", 'T', "ingotTungsten", 'B', "blockSteel");
//		registerShaped(getMaterial("computer_monitor", Type.PART), "ADA", "DGD", "ADA", 'D', "dye", 'A', "ingotAluminum", 'G', "paneGlass");
//		registerShaped(getMaterial("coolant_simple", 2, Type.PART), " T ", "TWT", " T ", 'T', "ingotTin", 'W', getStack(Items.WATER_BUCKET));
//		registerShaped(getMaterial("coolant_triple", Type.PART), "TTT", "CCC", "TTT", 'T', "ingotTin", 'C', getMaterial("coolant_simple", Type.PART));
//		registerShaped(getMaterial("coolant_six", Type.PART), "TCT", "TPT", "TCT", 'T', "ingotTin", 'C', getMaterial("coolant_triple", Type.PART), 'P', "plateCopper");
//		registerShaped(getMaterial("helium_coolant_simple", Type.PART), " T ", "TCT", " T ", 'T', "ingotTin", 'C', getCell("helium"));
//		registerShaped(getMaterial("helium_coolant_triple", Type.PART), "TTT", "CCC", "TTT", 'T', "ingotTin", 'C', getMaterial("helium_coolant_simple", Type.PART));
//		registerShaped(getMaterial("helium_coolant_six", Type.PART), "THT", "TCT", "THT", 'T', "ingotTin", 'C', "ingotCopper", 'H', getMaterial("helium_coolant_triple", Type.PART));
//		registerShaped(getMaterial("nak_coolant_simple", Type.PART), "TST", "PCP", "TST", 'T', "ingotTin", 'C', getMaterial("coolant_simple", Type.PART), 'S', getCell("sodium"), 'P', getCell("potassium"));
//		registerShaped(getMaterial("nak_coolant_simple", Type.PART), "TPT", "SCS", "TPT", 'T', "ingotTin", 'C', getMaterial("coolant_simple", Type.PART), 'S', getCell("sodium"), 'P', getCell("potassium"));
//		registerShaped(getMaterial("nak_coolant_triple", Type.PART), "TTT", "CCC", "TTT", 'T', "ingotTin", 'C', getMaterial("nak_coolant_simple", Type.PART));
//		registerShaped(getMaterial("nak_coolant_six", Type.PART), "THT", "TCT", "THT", 'T', "ingotTin", 'C', "ingotCopper", 'H', getMaterial("nak_coolant_triple", Type.PART));
//		registerShaped(getMaterial("iridium_neutron_reflector", Type.PART), "PPP", "PIP", "PPP", 'P', getMaterial("thick_neutron_reflector", Type.PART), 'I', "ingotIridium");
//		registerShaped(getMaterial("thick_neutron_reflector", Type.PART), " P ", "PCP", " P ", 'P', getMaterial("neutron_reflector", Type.PART), 'C', getCell("Berylium"));
//		registerShaped(getMaterial("neutron_reflector", Type.PART), "TCT", "CPC", "TCT", 'T', "dustTin", 'C', "dustCoal", 'P', "plateCopper");
//		registerShaped(getMaterial("super_conductor", 4, Type.PART), "CCC", "TIT", "EEE", 'E', "circuitMaster", 'C', getMaterial("heliumCoolantSimple", Type.PART), 'T', "ingotTungsten", 'I', "plateIridiumAlloy");
//		registerShaped(getMaterial("carbon_fiber", Type.PART), " C ", "C C", " C ", 'C', "dustCoal");
//		registerShaped(getMaterial("carbon_fiber", Type.PART), "CCC", "C C", "CCC", 'C', getCell("carbon"));
//		registerShapeless(getMaterial("carbon_mesh", Type.PART), getMaterial("carbon_fiber", Type.PART), getMaterial("carbon_fiber", Type.PART));
//		registerShaped(getMaterial("electronic_circuit", Type.PART), "WWW", "SRS", "WWW", 'R', "ingotRefinedIron", 'S', Items.REDSTONE, 'W', EnumCableType.ICOPPER.getStack());
//		registerShaped(getMaterial("advanced_circuit", Type.PART), "RGR", "LCL", "RGR", 'R', "dustRedstone", 'G', "dustGlowstone", 'L', "gemLapis", 'C', "circuitBasic");

		// Cables
		registerShaped(getMaterial("copper", 6, Type.CABLE), "CCC", 'C', "ingotCopper");
		registerShaped(getMaterial("tin", 9, Type.CABLE), "TTT", 'T', "ingotTin");
		registerShaped(getMaterial("gold", 12, Type.CABLE), "GGG", 'G', "ingotGold");
		registerShaped(getMaterial("hv", 12, Type.CABLE), "RRR", 'R', "ingotRefinedIron");
		registerShaped(getMaterial("insulatedcopper", 6, Type.CABLE), "RRR", "CCC", "RRR", 'R', "itemRubber", 'C', "ingotCopper");
		registerShaped(getMaterial("insulatedcopper", 6, Type.CABLE), "RCR", "RCR", "RCR", 'R', "itemRubber", 'C', "ingotCopper");
		registerShapeless(getMaterial("insulatedcopper", Type.CABLE), "itemRubber", getMaterial("copper", Type.CABLE));
		registerShaped(getMaterial("insulatedgold", 4, Type.CABLE), "RRR", "RGR", "RRR", 'R', "itemRubber", 'G', "ingotGold");
		registerShapeless(getMaterial("insulatedgold", Type.CABLE), "itemRubber", "itemRubber", getMaterial("gold", Type.CABLE));
		registerShaped(getMaterial("insulatedhv", 4, Type.CABLE), "RRR", "RIR", "RRR", 'R', "itemRubber", 'I', "ingotRefinedIron");
		registerShapeless(getMaterial("insulatedhv", Type.CABLE), "itemRubber", "itemRubber", getMaterial("hv", Type.CABLE));
		registerShaped(getMaterial("glassfiber", 4, Type.CABLE), "GGG", "RDR", "GGG", 'R', "dustRedstone", 'D', "gemDiamond", 'G', "blockGlass");
		registerShaped(getMaterial("glassfiber", 4, Type.CABLE), "GGG", "RDR", "GGG", 'R', "dustRedstone", 'D', "dustDiamond", 'G', "blockGlass");
		registerShaped(getMaterial("glassfiber", 3, Type.CABLE), "GGG", "RDR", "GGG", 'R', "dustRedstone", 'D', "gemRuby", 'G', "blockGlass");
		registerShaped(getMaterial("glassfiber", 3, Type.CABLE), "GGG", "RDR", "GGG", 'R', "dustRedstone", 'D', "dustRuby", 'G', "blockGlass");
		registerShaped(getMaterial("glassfiber", 6, Type.CABLE), "GGG", "RDR", "GGG", 'R', "ingotSilver", 'D', "gemDiamond", 'G', "blockGlass");
		registerShaped(getMaterial("glassfiber", 6, Type.CABLE), "GGG", "RDR", "GGG", 'R', "ingotSilver", 'D', "dustDiamond", 'G', "blockGlass");
		registerShaped(getMaterial("glassfiber", 8, Type.CABLE), "GGG", "RDR", "GGG", 'R', "ingotElectrum", 'D', "gemDiamond", 'G', "blockGlass");
		registerShaped(getMaterial("glassfiber", 8, Type.CABLE), "GGG", "RDR", "GGG", 'R', "ingotElectrum", 'D', "dustDiamond", 'G', "blockGlass");



		//UU-Matter
		ItemStack uuStack = TRContent.Parts.UU_MATTER.getStack();
		registerShaped(getStack(Blocks.LOG, 8), 				" U ", "   ", "   ", 'U', uuStack);
		registerShaped(getStack(Blocks.STONE, 16), 				"   ", " U ", "   ", 'U', uuStack);
		registerShaped(getStack(Blocks.SNOW, 16), 				"U U", "   ", "   ", 'U', uuStack);
		registerShaped(getStack(Blocks.GRASS, 16), 				"   ", "U  ", "U  ", 'U', uuStack);
		registerShaped(getStack(Blocks.OBSIDIAN, 12), 			"U U", "U U", "   ", 'U', uuStack);
		registerShaped(getStack(Blocks.GLASS, 32), 				" U ", "U U", " U ", 'U', uuStack);
		registerShaped(getStack(Items.DYE, 32, 3), 				"UU ", "  U", "UU ", 'U', uuStack);
		registerShaped(getStack(Blocks.GLOWSTONE, 8), 			" U ", "U U", "UUU", 'U', uuStack);
		registerShaped(getStack(Blocks.CACTUS, 48), 			" U ", "UUU", "U U", 'U', uuStack);
		registerShaped(getStack(Items.REEDS, 48), 				"U U", "U U", "U U", 'U', uuStack);
		registerShaped(getStack(Blocks.VINE, 24), 				"U  ", "U  ", "U  ", 'U', uuStack);
		registerShaped(getStack(Items.SNOWBALL, 16), 			"   ", "   ", "UUU", 'U', uuStack);
		registerShaped(getStack(Items.CLAY_BALL, 48), 			"UU ", "U  ", "UU ", 'U', uuStack);
		registerShaped(getStack(Blocks.WATERLILY, 64), 			"U U", " U ", " U ", 'U', uuStack);
		registerShaped(getStack(Items.GUNPOWDER, 15), 			"UUU", "U  ", "UUU", 'U', uuStack);
		registerShaped(getStack(Items.BONE, 32), 				"U  ", "UU ", "U  ", 'U', uuStack);
		registerShaped(getStack(Items.FEATHER, 32), 			" U ", " U ", "U U", 'U', uuStack);
		registerShaped(getStack(Items.DYE, 48), 				" UU", " UU", " U ", 'U', uuStack);
		registerShaped(getStack(Items.ENDER_PEARL, 1), 			"UUU", "U U", " U ", 'U', uuStack);
		registerShaped(getStack(Items.COAL, 5), 				"  U", "U  ", "  U", 'U', uuStack);
		registerShaped(getStack(Blocks.IRON_ORE, 2), 			"U U", " U ", "U U", 'U', uuStack);
		registerShaped(getStack(Blocks.GOLD_ORE, 2), 			" U ", "UUU", " U ", 'U', uuStack);
		registerShaped(getStack(Items.REDSTONE, 24), 			"   ", " U ", "UUU", 'U', uuStack);
		registerShaped(getStack(Items.DYE, 9, 4), 				" U ", " U ", " UU", 'U', uuStack);
		registerShaped(getStack(Blocks.EMERALD_ORE, 1), 		"UU ", "U U", " UU", 'U', uuStack);
		registerShaped(getStack(Items.EMERALD, 2), 				"UUU", "UUU", " U ", 'U', uuStack);
		registerShaped(getStack(Items.DIAMOND, 1), 				"UUU", "UUU", "UUU", 'U', uuStack);
//		registerShaped(getMaterial("tin", 10, Type.DUST), 		"   ", "U U", "  U", 'U', uuStack);
//		registerShaped(getMaterial("copper", 10, Type.DUST), 	"  U", "U U", "   ", 'U', uuStack);
//		registerShaped(getMaterial("lead", 14, Type.DUST), 		"UUU", "UUU", "U  ", 'U', uuStack);
//		registerShaped(getMaterial("platinum", Type.DUST), 		"  U", "UUU", "UUU", 'U', uuStack);
//		registerShaped(getMaterial("tungsten", Type.DUST), 		"U  ", "UUU", "UUU", 'U', uuStack);
//		registerShaped(getMaterial("titanium", 2, Type.DUST), 	"UUU", " U ", " U ", 'U', uuStack);
//		registerShaped(getMaterial("aluminum", 16, Type.DUST), 	" U ", " U ", "UUU", 'U', uuStack);
//		registerShaped(getMaterial("iridium", 1, Type.ORE), 	"UUU", " U ", "UUU", 'U', uuStack);
		
//		for (String part : ItemParts.types) {
//			if (part.endsWith("Gear")) {
//				registerShaped(getMaterial(part, Type.PART), " O ", "OIO", " O ", 'I', getStack(Items.IRON_INGOT), 'O', "ingot" + StringUtils.toFirstCapital(part.replace("Gear", "")));
//			}
//		}

		registerShaped(new ItemStack(TRContent.RUBBER_LOG_SLAB_HALF),  "WWW", 'W', new ItemStack(TRContent.RUBBER_PLANKS));
		registerShaped(new ItemStack(TRContent.RUBBER_LOG_STAIR),  "W  ", "WW ", "WWW", 'W', new ItemStack(TRContent.RUBBER_PLANKS));

		RebornCraftingHelper.addShapelessOreRecipe(new ItemStack(TRContent.MANUAL), "ingotRefinedIron",
			Items.BOOK);


		TechReborn.LOGGER.info("Crafting Table Recipes Added");
	}

	static void registerCompressionRecipes() {
//		for (String name : BlockStorage.types) {
//			if (OreUtil.hasIngot(name)) {
//				registerShaped(BlockStorage.getStorageBlockByName(name), "AAA", "AAA", "AAA", 'A', "ingot" + StringUtils.toFirstCapital(name));
//				registerShapeless(getMaterial(name, 9, Type.INGOT), BlockStorage.getStorageBlockByName(name));
//			} else if (OreUtil.hasGem(name)) {
//				registerShaped(BlockStorage.getStorageBlockByName(name), "AAA", "AAA", "AAA", 'A', "gem" + StringUtils.toFirstCapital(name));
//				registerShapeless(getMaterial(name, 9, Type.GEM), BlockStorage.getStorageBlockByName(name));
//			}
//		}
		
//		for (String block : BlockStorage2.types){
//			block = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, block);
//			if (OreUtil.hasIngot(block)) {
//				registerShaped(BlockStorage2.getStorageBlockByName(block), "AAA", "AAA", "AAA", 'A', "ingot" + StringUtils.toFirstCapital(block));
//				registerShapeless(getMaterial(block, 9, Type.INGOT), BlockStorage2.getStorageBlockByName(block));
//			} else if (OreUtil.hasGem(block)) {
//				registerShaped(BlockStorage2.getStorageBlockByName(block), "AAA", "AAA", "AAA", 'A', "gem" + StringUtils.toFirstCapital(block));
//				registerShapeless(getMaterial(block, 9, Type.GEM), BlockStorage2.getStorageBlockByName(block));
//			}
//		}

//		for (String name : ItemDustsSmall.types) {
//			registerShapeless(getMaterial(name, 4, Type.SMALL_DUST), getMaterialObject(name, Type.DUST));
//			registerShapeless(getMaterial(name, Type.DUST), getMaterialObject(name, Type.SMALL_DUST), getMaterialObject(name, Type.SMALL_DUST), getMaterialObject(name, Type.SMALL_DUST), getMaterialObject(name, Type.SMALL_DUST));
//		}

// TODO: fix recipe
//		for (String nuggets : ItemNuggets.types) {
//			if (nuggets.equalsIgnoreCase("diamond"))
//				continue;
//			registerShapeless(getMaterial(nuggets, 9, Type.NUGGET), CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "ingot_" + nuggets));
//			registerShaped(getMaterial(nuggets, Type.INGOT), "NNN", "NNN", "NNN", 'N', CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "nugget_" + nuggets));
//		}
//
//		registerShapeless(getMaterial("diamond", 9, Type.NUGGET), "gemDiamond");
//		registerShaped(getStack(Items.DIAMOND), "NNN", "NNN", "NNN", 'N', "nuggetDiamond");
	}

	static void registerMixedMetalIngotRecipes() {
		registerMixedMetal("ingotRefinedIron", "ingotBronze", "ingotTin", 2);
		registerMixedMetal("ingotRefinedIron", "ingotBronze", "ingotZinc", 2);

		registerMixedMetal("ingotNickel", "ingotBronze", "ingotTin", 3);
		registerMixedMetal("ingotNickel", "ingotBronze", "ingotZinc", 3);

		registerMixedMetal("ingotNickel", "ingotBronze", "ingotAluminum", 4);
		registerMixedMetal("ingotInvar", "ingotBronze", "ingotTin", 4);
		registerMixedMetal("ingotInvar", "ingotBronze", "ingotZinc", 4);

		registerMixedMetal("ingotInvar", "ingotBronze", "ingotAluminum", 5);
		registerMixedMetal("ingotTitanium", "ingotBronze", "ingotTin", 5);
		registerMixedMetal("ingotTitanium", "ingotBronze", "ingotZinc", 5);
		registerMixedMetal("ingotTungsten", "ingotBronze", "ingotTin", 5);
		registerMixedMetal("ingotTungsten", "ingotBronze", "ingotZinc", 5);

		registerMixedMetal("ingotTitanium", "ingotBronze", "ingotAluminum", 6);
		registerMixedMetal("ingotTungsten", "ingotBronze", "ingotAluminum", 6);

		registerMixedMetal("ingotTungstensteel", "ingotBronze", "ingotTin", 8);
		registerMixedMetal("ingotTungstensteel", "ingotBronze", "ingotZinc", 8);

		registerMixedMetal("ingotTungstensteel", "ingotBronze", "ingotAluminum", 9);
	}

	static void registerMixedMetal(String top, String middle, String bottom, int amount) {
		if (!OreDictionary.doesOreNameExist(top)) {
			return;
		}
		if (!OreDictionary.doesOreNameExist(middle)) {
			return;
		}
		if (!OreDictionary.doesOreNameExist(bottom)) {
			return;
		}
//		if (top.equals("ingotRefinedIron")) {
//			registerShaped(getMaterial("mixed_metal", amount, Type.INGOT), "TTT", "MMM", "BBB", 'T', "ingotRefinedIron", 'M', middle, 'B', bottom);
//		} else {
//			registerShaped(getMaterial("mixed_metal", amount, Type.INGOT), "TTT", "MMM", "BBB", 'T', top, 'M', middle, 'B', bottom);
//		}
		if (middle.equals("ingotBronze")) {
			registerMixedMetal(top, "ingotBrass", bottom, amount);
		}
		if (bottom.equals("ingotAluminum")) {
			registerMixedMetal(top, middle, "ingotAluminium", amount);
		}
	}

	static void registerShaped(ItemStack output, Object... inputs) {
		RebornCraftingHelper.addShapedOreRecipe(output, inputs);
	}

	static void registerShapeless(ItemStack output, Object... inputs) {
		RebornCraftingHelper.addShapelessOreRecipe(output, inputs);
	}

	static void addToolAndArmourRecipes(ItemStack sword,
	                                    ItemStack pickaxe,
	                                    ItemStack axe,
	                                    ItemStack hoe,
	                                    ItemStack spade,
	                                    ItemStack helmet,
	                                    ItemStack chestplate,
	                                    ItemStack leggings,
	                                    ItemStack boots,
	                                    String material) {
		registerShaped(sword, "G", "G", "S", 'S', Items.STICK, 'G', material);
		registerShaped(pickaxe, "GGG", " S ", " S ", 'S', Items.STICK, 'G', material);
		registerShaped(axe, "GG", "GS", " S", 'S', Items.STICK, 'G', material);
		registerShaped(hoe, "GG", " S", " S", 'S', Items.STICK, 'G', material);
		registerShaped(spade, "G", "S", "S", 'S', Items.STICK, 'G', material);
		registerShaped(helmet, "GGG", "G G", 'G', material);
		registerShaped(chestplate, "G G", "GGG", "GGG", 'G', material);
		registerShaped(leggings, "GGG", "G G", "G G", 'G', material);
		registerShaped(boots, "G G", "G G", 'G', material);
	}

	*/
}
