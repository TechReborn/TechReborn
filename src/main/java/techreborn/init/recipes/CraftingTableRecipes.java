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

import com.google.common.base.CaseFormat;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import reborncore.common.util.OreUtil;
import reborncore.common.util.RebornCraftingHelper;
import reborncore.common.util.StringUtils;
import techreborn.Core;
import techreborn.blocks.BlockMachineFrames;
import techreborn.blocks.BlockStorage;
import techreborn.blocks.BlockStorage2;
import techreborn.compat.CompatManager;
import techreborn.config.ConfigTechReborn;
import techreborn.init.IC2Duplicates;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.items.DynamicCell;
import techreborn.items.ItemCells;
import techreborn.items.ItemDusts;
import techreborn.items.ItemDustsSmall;
import techreborn.items.ItemIngots;
import techreborn.items.ItemNuggets;
import techreborn.items.ItemParts;
import techreborn.items.ItemUpgrades;

/**
 * Created by Prospector
 */
public class CraftingTableRecipes extends RecipeMethods {
	public static void init() {
		ItemStack refined_iron  = getStack(IC2Duplicates.REFINED_IRON);
		
		registerCompressionRecipes();
		registerMixedMetalIngotRecipes();
			
		registerShapeless(BlockStorage2.getStorageBlockByName("iridium_reinforced_stone", 1), getStack(Blocks.STONE), "ingotIridium");
		registerShapeless(BlockStorage2.getStorageBlockByName("iridium_reinforced_tungstensteel", 1), BlockStorage2.getStorageBlockByName("tungstensteel", 1), "ingotIridium");
		registerShapeless(BlockStorage2.getStorageBlockByName("iridium_reinforced_tungstensteel", 1), BlockStorage2.getStorageBlockByName("iridium_reinforced_stone", 1), getMaterialObject("tungstensteel", Type.INGOT));
		registerShapeless(getStack(ModBlocks.RUBBER_PLANKS, 4), getStack(ModBlocks.RUBBER_LOG));
		registerShaped(DynamicCell.getEmptyCell(16), " T ", "T T", " T ", 'T', "ingotTin");
		registerShaped(getStack(ModBlocks.REFINED_IRON_FENCE), "RRR", "RRR", 'R', refined_iron);
		registerShaped(getStack(ModBlocks.REINFORCED_GLASS, 7), "GAG", "GGG", "GAG", 'A', "plateAdvancedAlloy", 'G', "blockGlass");
		registerShaped(getStack(ModBlocks.REINFORCED_GLASS, 7), "GGG", "AGA", "GGG", 'A', "plateAdvancedAlloy", 'G', "blockGlass");
		
		// Tools and devices		
		registerShaped(getStack(ModItems.WRENCH), "BNB", "NBN", " B ", 'B', "ingotBronze", 'N', "nuggetBronze");	
		registerShaped(getStack(ModItems.TREE_TAP), " S ", "PPP", "P  ", 'S', "stickWood", 'P', "plankWood");
		registerShaped(getStack(ModItems.ELECTRIC_TREE_TAP), "TB", "  ", 'T', getStack(ModItems.TREE_TAP), 'B', getStack(ModItems.RE_BATTERY));
		registerShaped(getStack(ModItems.NANOSABER), "DC ", "DC ", "GLG", 'L', getStack(ModItems.LAPOTRONIC_CRYSTAL), 'C', "plateCarbon", 'D', "plateDiamond", 'G', getMaterialObject("glowstone", Type.SMALL_DUST));
		registerShaped(getStack(ModItems.ROCK_CUTTER), "DT ", "DT ", "DCB", 'D', "dustDiamond", 'T', "ingotTitanium", 'C', "circuitBasic", 'B', getStack(ModItems.RE_BATTERY));
		registerShaped(getStack(ModItems.STEEL_DRILL), " S ", "SCS", "SBS", 'S', "ingotSteel", 'C', "circuitBasic", 'B', "reBattery");
		registerShaped(getStack(ModItems.DIAMOND_DRILL), " D ", "DCD", "TST", 'D', "gemDiamond", 'C', "circuitAdvanced", 'S', getStack(ModItems.STEEL_DRILL, 1, OreDictionary.WILDCARD_VALUE), 'T', "ingotTitanium");
		registerShaped(getStack(ModItems.ADVANCED_DRILL), " I ", "NCN", "OAO", 'I', "ingotIridium", 'N', "nuggetIridium", 'A', getStack(ModItems.DIAMOND_DRILL, 1, OreDictionary.WILDCARD_VALUE), 'C', "circuitMaster", 'O', getMaterial("overclock", Type.UPGRADE));
		registerShaped(getStack(ModItems.STEEL_CHAINSAW), " SS", "SCS", "BS ", 'S', "ingotSteel", 'C', "circuitBasic", 'B', "reBattery");
		registerShaped(getStack(ModItems.DIAMOND_CHAINSAW), " DD", "TCD", "ST ", 'D', "gemDiamond", 'C', "circuitAdvanced", 'S', getStack(ModItems.STEEL_CHAINSAW, 1, OreDictionary.WILDCARD_VALUE), 'T', "ingotTitanium");
		registerShaped(getStack(ModItems.ADVANCED_CHAINSAW), " NI", "OCN", "DO ", 'I', "ingotIridium", 'N', "nuggetIridium", 'D', getStack(ModItems.DIAMOND_CHAINSAW, 1, OreDictionary.WILDCARD_VALUE), 'C', "circuitMaster", 'O', getMaterial("overclock", Type.UPGRADE));
		registerShaped(getStack(ModItems.STEEL_JACKHAMMER), "SBS", "SCS", " S ", 'S', "ingotSteel", 'C', "circuitBasic", 'B', "reBattery");
		registerShaped(getStack(ModItems.DIAMOND_JACKHAMMER), "DSD", "TCT", " D ", 'D', "gemDiamond", 'C', "circuitAdvanced", 'S', getStack(ModItems.STEEL_JACKHAMMER, 1, OreDictionary.WILDCARD_VALUE), 'T', "ingotTitanium");
		registerShaped(getStack(ModItems.ADVANCED_JACKHAMMER), "NDN", "OCO", " I ", 'I', "ingotIridium", 'N', "nuggetIridium", 'D', getStack(ModItems.DIAMOND_DRILL, 1, OreDictionary.WILDCARD_VALUE), 'C', "circuitMaster", 'O', getMaterial("overclock", Type.UPGRADE));
		registerShaped(getStack(ModItems.CLOAKING_DEVICE), "CIC", "IOI", "CIC", 'C', "ingotChrome", 'I', "plateIridium", 'O', getStack(ModItems.LAPOTRONIC_ORB));
		registerShaped(getStack(ModItems.LAPOTRONIC_ORB_PACK), "FOF", "SPS", "FIF", 'F', "circuitMaster", 'O', getStack(ModItems.LAPOTRONIC_ORB), 'S', getMaterial("super_conductor", Type.PART), 'I', "ingotIridium", 'P', getStack(ModItems.LITHIUM_BATTERY_PACK));

		if (ConfigTechReborn.enableGemArmorAndTools) {
			addToolAndArmourRecipes(getStack(ModItems.RUBY_SWORD), getStack(ModItems.RUBY_PICKAXE), getStack(ModItems.RUBY_AXE), getStack(ModItems.RUBY_HOE), getStack(ModItems.RUBY_SPADE), getStack(ModItems.RUBY_HELMET), getStack(ModItems.RUBY_CHESTPLATE), getStack(ModItems.RUBY_LEGGINGS), getStack(ModItems.RUBY_BOOTS), "gemRuby");
			addToolAndArmourRecipes(getStack(ModItems.SAPPHIRE_SWORD), getStack(ModItems.SAPPHIRE_PICKAXE), getStack(ModItems.SAPPHIRE_AXE), getStack(ModItems.SAPPHIRE_HOE), getStack(ModItems.SAPPHIRE_SPADE), getStack(ModItems.SAPPHIRE_HELMET), getStack(ModItems.SAPPHIRE_CHSTPLATE), getStack(ModItems.SAPPHIRE_LEGGINGS), getStack(ModItems.SAPPHIRE_BOOTS), "gemSapphire");
			addToolAndArmourRecipes(getStack(ModItems.PERIDOT_SWORD), getStack(ModItems.PERIDOT_PICKAXE), getStack(ModItems.PERIDOT_AXE), getStack(ModItems.PERIDOT_HOE), getStack(ModItems.PERIDOT_SAPPHIRE), getStack(ModItems.PERIDOT_HELMET), getStack(ModItems.PERIDOT_CHESTPLATE), getStack(ModItems.PERIDOT_LEGGINGS), getStack(ModItems.PERIDOT_BOOTS), "gemPeridot");
			addToolAndArmourRecipes(getStack(ModItems.BRONZE_SWORD), getStack(ModItems.BRONZE_PICKAXE), getStack(ModItems.BRONZE_AXE), getStack(ModItems.BRONZE_HOE), getStack(ModItems.BRONZE_SPADE), getStack(ModItems.BRONZE_HELMET), getStack(ModItems.BRONZE_CHESTPLATE), getStack(ModItems.BRONZE_LEGGINGS), getStack(ModItems.BRONZE_BOOTS), "ingotBronze");
		}
		
		//Upgrades
		registerShaped(ItemUpgrades.getUpgradeByName("injection"), "CHC", "PSP", "PPP", 'S', "chestWood", 'C', "circuitBasic", 'P', "plateIron", 'H', getStack(Blocks.HOPPER));
		registerShaped(ItemUpgrades.getUpgradeByName("ejection"), "CSC", "PHP", "PPP", 'S', "chestWood", 'C', "circuitBasic", 'P', "plateIron", 'H', getStack(Blocks.HOPPER));
		registerShaped(ItemUpgrades.getUpgradeByName("energy_storage"), "PPP", "WBW", "PCP", 'P', "plankWood", 'W', getStack(IC2Duplicates.CABLE_ICOPPER), 'C', "circuitBasic", 'B', getStack(ModItems.RE_BATTERY));
		
		if (!IC2Duplicates.deduplicate()) {
			registerShaped(ItemUpgrades.getUpgradeByName("Overclock"), "TTT", "WCW", 'T', getMaterial("coolant_simple", Type.PART), 'W', getStack(IC2Duplicates.CABLE_ICOPPER), 'C', "circuitBasic");
			registerShaped(ItemUpgrades.getUpgradeByName("Overclock", 2), " T ", "WCW", 'T', getMaterial("helium_coolant_triple", Type.PART), 'W', getStack(IC2Duplicates.CABLE_ICOPPER), 'C', "circuitBasic");
			registerShaped(ItemUpgrades.getUpgradeByName("Overclock", 2),  " T ", "WCW", 'T', getMaterial("nak_coolant_simple", Type.PART), 'W', getStack(IC2Duplicates.CABLE_ICOPPER), 'C', "circuitBasic");
			registerShaped(ItemUpgrades.getUpgradeByName("transformer"), "GGG", "WTW", "GCG", 'G', "blockGlass", 'W', getStack(IC2Duplicates.CABLE_IGOLD), 'C', "circuitBasic", 'T', getStack(IC2Duplicates.MVT));
		}
		
		//Machines
		registerShaped(getMaterial("standard", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', refined_iron, 'C', "circuitBasic", 'A', "machineBlockBasic");
		registerShaped(getMaterial("standard", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "plateIron", 'C', "circuitBasic", 'A', "machineBlockBasic");
		registerShaped(getMaterial("standard", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "plateAluminum", 'C', "circuitBasic", 'A', "machineBlockBasic");		
		registerShaped(getMaterial("reinforced", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "plateSteel", 'C', "circuitAdvanced", 'A', "machineBlockAdvanced");
		registerShaped(getMaterial("reinforced", 1, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "plateSteel", 'C', "circuitAdvanced", 'A', getMaterial("standard", Type.MACHINE_CASING));
		registerShaped(getMaterial("advanced", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R',  "plateChrome", 'C', "circuitElite", 'A', getMaterial("highly_advanced_machine", Type.MACHINE_FRAME));
		registerShaped(getMaterial("advanced", 1, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R',  "plateChrome", 'C', "circuitElite", 'A', getMaterial("reinforced", Type.MACHINE_CASING));		
		registerShapeless(getStack(IC2Duplicates.GENERATOR), getStack(ModItems.RE_BATTERY), "machineBlockBasic", getStack(Blocks.FURNACE));
		registerShaped(getStack(ModBlocks.SEMI_FLUID_GENERATOR), "III", "IHI", "CGC", 'I', "plateIron", 'H', getStack(ModBlocks.REINFORCED_GLASS), 'C', "circuitBasic", 'G', getStack(IC2Duplicates.GENERATOR));
		registerShaped(getStack(ModBlocks.SEMI_FLUID_GENERATOR), "III", "IHI", "CGC", 'I', "plateAluminum", 'H', getStack(ModBlocks.REINFORCED_GLASS), 'C', "circuitBasic", 'G', getStack(IC2Duplicates.GENERATOR));
		registerShaped(getStack(ModBlocks.DIESEL_GENERATOR), "III", "I I", "CGC", 'I', refined_iron, 'C', "circuitBasic", 'G', getStack(IC2Duplicates.GENERATOR));
		registerShaped(getStack(ModBlocks.DIESEL_GENERATOR), "III", "I I", "CGC", 'I', "plateAluminum", 'C', "circuitBasic", 'G', getStack(IC2Duplicates.GENERATOR));
		registerShaped(getStack(ModBlocks.GAS_TURBINE), "IAI", "WGW", "IAI", 'I', "plateInvar", 'A', "circuitAdvanced", 'W', getStack(ModBlocks.WIND_MILL), 'G', getStack(ModBlocks.REINFORCED_GLASS));
		registerShaped(getStack(ModBlocks.GAS_TURBINE), "IAI", "WGW", "IAI", 'I', "plateAluminum", 'A', "circuitAdvanced", 'W', getStack(ModBlocks.WIND_MILL), 'G', getStack(ModBlocks.REINFORCED_GLASS));
		registerShaped(getStack(ModBlocks.THERMAL_GENERATOR), "III", "IRI", "CGC", 'I', "plateInvar", 'R', getStack(ModBlocks.REINFORCED_GLASS), 'G', getStack(IC2Duplicates.GENERATOR), 'C', "circuitBasic");
		registerShaped(getStack(ModBlocks.WIND_MILL), "I I", " G ", "I I", 'I', "plateIron", 'G', getStack(IC2Duplicates.GENERATOR));
		registerShaped(getStack(ModBlocks.WATER_MILL), "SWS", "WGW", "SWS", 'S', "stickWood", 'W', "plankWood", 'G', getStack(IC2Duplicates.GENERATOR));
		registerShaped(getStack(ModBlocks.LIGHTNING_ROD), "CAC", "ACA", "CAC", 'A', getStack(ModBlocks.MACHINE_CASINGS, 1, 2), 'C', "circuitMaster");
		registerShaped(getStack(ModBlocks.IRON_ALLOY_FURNACE), "III", "F F", "III", 'I', refined_iron, 'F', getStack(IC2Duplicates.IRON_FURNACE));
		registerShaped(getStack(ModBlocks.INDUSTRIAL_ELECTROLYZER), "RER", "CEC", "RER", 'R', "plateIron", 'E', getStack(IC2Duplicates.EXTRACTOR), 'C', "circuitAdvanced");
		registerShaped(getStack(ModBlocks.INDUSTRIAL_CENTRIFUGE), "RCR", "AEA", "RCR", 'R', refined_iron, 'E', getStack(IC2Duplicates.EXTRACTOR), 'A', "machineBlockAdvanced", 'C', "circuitAdvanced");
		registerShaped(getStack(ModBlocks.INDUSTRIAL_CENTRIFUGE), "RCR", "AEA", "RCR", 'R', "plateAluminum", 'E', getStack(IC2Duplicates.EXTRACTOR), 'A', "machineBlockAdvanced", 'C', "circuitAdvanced");
		registerShaped(getStack(ModBlocks.INDUSTRIAL_SAWMILL), "PAP", "SSS", "ACA", 'P', refined_iron, 'A', "circuitAdvanced", 'S', getMaterial("diamond_saw_blade", Type.PART), 'C', "machineBlockAdvanced");
		registerShaped(getStack(ModBlocks.INDUSTRIAL_BLAST_FURNACE), "CHC", "HBH", "FHF", 'H', getMaterial("cupronickelHeatingCoil", Type.PART), 'C', "circuitAdvanced", 'B', "machineBlockAdvanced", 'F', getStack(IC2Duplicates.ELECTRICAL_FURNACE));
		registerShaped(getStack(ModBlocks.INDUSTRIAL_GRINDER), "ECG", "HHH", "CBC", 'E', getStack(ModBlocks.INDUSTRIAL_ELECTROLYZER), 'H', "craftingDiamondGrinder", 'C', "circuitAdvanced", 'B', "machineBlockAdvanced", 'G', getStack(IC2Duplicates.GRINDER));
		registerShaped(getStack(ModBlocks.IMPLOSION_COMPRESSOR), "ABA", "CPC", "ABA", 'A', getMaterialObject("advancedAlloy", Type.INGOT), 'C', "circuitAdvanced", 'B', "machineBlockAdvanced", 'P', getStack(IC2Duplicates.COMPRESSOR));
		registerShaped(getStack(ModBlocks.VACUUM_FREEZER), "SPS", "CGC", "SPS", 'S', "plateSteel", 'C', "circuitAdvanced", 'G', getStack(ModBlocks.REINFORCED_GLASS), 'P', getStack(IC2Duplicates.EXTRACTOR));		
		registerShaped(getStack(ModBlocks.DISTILLATION_TOWER), "CMC", "PBP", "EME", 'E', getStack(ModBlocks.INDUSTRIAL_ELECTROLYZER), 'M', "circuitMaster", 'B', "machineBlockAdvanced", 'C', getStack(ModBlocks.INDUSTRIAL_CENTRIFUGE), 'P', getStack(IC2Duplicates.EXTRACTOR));
		registerShaped(getStack(ModBlocks.CHEMICAL_REACTOR), "IMI", "CPC", "IEI", 'I', "plateInvar", 'C', "circuitAdvanced", 'M', getStack(IC2Duplicates.EXTRACTOR), 'P', getStack(IC2Duplicates.COMPRESSOR), 'E', getStack(IC2Duplicates.EXTRACTOR));
		registerShaped(getStack(ModBlocks.ROLLING_MACHINE),  "PCP", "MBM", "PCP", 'P', getStack(Blocks.PISTON), 'C', "circuitAdvanced", 'M', getStack(IC2Duplicates.COMPRESSOR), 'B', "machineBlockBasic");
		registerShaped(getStack(ModBlocks.AUTO_CRAFTING_TABLE), "MPM", "PCP", "MPM", 'M', "circuitAdvanced", 'C', "workbench", 'P', "plateIron");
		registerShaped(getStack(ModBlocks.CHARGE_O_MAT),  "ETE", "COC", "EAE", 'E', "circuitMaster", 'T', getStack(ModItems.ENERGY_CRYSTAL), 'C', getStack(Blocks.CHEST), 'O', getStack(ModItems.LAPOTRONIC_ORB), 'A', "machineBlockAdvanced");
		registerShaped(getStack(ModBlocks.ALLOY_SMELTER), " C ", "FMF", "   ", 'C', "circuitBasic", 'F', getStack(IC2Duplicates.ELECTRICAL_FURNACE), 'M', "machineBlockBasic");
		registerShaped(getStack(ModBlocks.INTERDIMENSIONAL_SU), "PAP", "ACA", "PAP", 'P', "plateIridium", 'C', getStack(Blocks.ENDER_CHEST), 'A', getStack(ModBlocks.ADJUSTABLE_SU));
		registerShaped(getStack(ModBlocks.ADJUSTABLE_SU), "LLL", "LCL", "LLL", 'L', getStack(ModItems.LAPOTRONIC_ORB), 'C', getStack(ModItems.ENERGY_CRYSTAL));
		registerShaped(getStack(ModBlocks.LAPOTRONIC_SU),  " L ", "CBC", " M ", 'L', getStack(IC2Duplicates.LVT), 'C', "circuitAdvanced", 'M', getStack(IC2Duplicates.MVT), 'B', getStack(ModBlocks.LSU_STORAGE));
		registerShaped(getStack(ModBlocks.LSU_STORAGE), "LLL", "LCL", "LLL", 'L', "blockLapis", 'C', "circuitBasic");
		registerShaped(getStack(ModBlocks.FUSION_CONTROL_COMPUTER), "CCC", "PTP", "CCC", 'P', getStack(ModItems.ENERGY_CRYSTAL), 'T', getStack(ModBlocks.FUSION_COIL), 'C', "circuitMaster");
		registerShaped(getStack(ModBlocks.FUSION_COIL), "CSC", "NAN", "CRC", 'A', getStack(ModBlocks.MACHINE_CASINGS, 1, 2), 'N', getMaterial("nichromeHeatingCoil", Type.PART), 'C', "circuitMaster", 'S', getMaterial("superConductor", Type.PART), 'R', getMaterial("iridiumNeutronReflector", Type.PART));
		registerShaped(getStack(ModBlocks.DIGITAL_CHEST), "PPP", "PDP", "PCP", 'P', "plateAluminum",  'D', getMaterial("data_orb", Type.PART), 'C', getMaterial("computer_monitor", Type.PART));
		registerShaped(getStack(ModBlocks.DIGITAL_CHEST), "PPP", "PDP", "PCP", 'P', "plateSteel",  'D', getMaterial("data_orb", Type.PART), 'C', getMaterial("computer_monitor", Type.PART));
		registerShaped(getStack(ModBlocks.MATTER_FABRICATOR),  "ETE", "AOA", "ETE", 'E', "circuitMaster", 'T', getStack(IC2Duplicates.EXTRACTOR), 'A', getMaterial("highly_advanced_machine", Type.MACHINE_FRAME), 'O', getStack(ModItems.LAPOTRONIC_ORB));

		if (!IC2Duplicates.deduplicate()) {
			registerShaped(getStack(IC2Duplicates.HVT), " H ", " M ", " H ", 'M', getStack(IC2Duplicates.MVT), 'H', getStack(IC2Duplicates.CABLE_IHV));
			registerShaped(getStack(IC2Duplicates.MVT), " G ", " M ", " G ", 'M', "machineBlockBasic", 'G', getStack(IC2Duplicates.CABLE_IGOLD));
			registerShaped(getStack(IC2Duplicates.LVT), "PWP", "CCC", "PPP", 'P', "plankWood", 'C', "ingotCopper", 'W', getStack(IC2Duplicates.CABLE_ICOPPER));
			registerShaped(getStack(IC2Duplicates.BAT_BOX), "WCW", "BBB", "WWW", 'W', "plankWood", 'B', getStack(ModItems.RE_BATTERY), 'C', getStack(IC2Duplicates.CABLE_ICOPPER));
			registerShaped(getStack(IC2Duplicates.MFE), "GEG", "EME", "GEG", 'M', "machineBlockBasic", 'E', getStack(ModItems.ENERGY_CRYSTAL), 'G', getStack(IC2Duplicates.CABLE_IGOLD));
			registerShaped(getStack(IC2Duplicates.MFSU), "LAL", "LML", "LOL", 'A', "circuitAdvanced", 'L', getStack(ModItems.LAPOTRONIC_CRYSTAL), 'M', getStack(IC2Duplicates.MFE), 'O', "machineBlockAdvanced");
			registerShaped(getStack(IC2Duplicates.COMPRESSOR), "S S", "SCS", "SMS", 'C', "circuitBasic", 'M', "machineBlockBasic", 'S', getStack(Blocks.STONE));
			registerShaped(getStack(IC2Duplicates.ELECTRICAL_FURNACE), " C ", "RFR", "   ", 'C', "circuitBasic", 'F', getStack(IC2Duplicates.IRON_FURNACE), 'R', "dustRedstone");
			registerShaped(getStack(IC2Duplicates.RECYCLER), " E ", "DCD", "GDG", 'D', getStack(Blocks.DIRT), 'C', getStack(IC2Duplicates.COMPRESSOR), 'G', getMaterialObject("glowstone", Type.DUST), 'E', "circuitBasic");
			registerShaped(getStack(IC2Duplicates.IRON_FURNACE), "III", "I I", "III", 'I', "ingotIron");
			registerShaped(getStack(IC2Duplicates.IRON_FURNACE), " I ", "I I", "IFI", 'I', "ingotIron", 'F', getStack(Blocks.FURNACE));
			registerShaped(getStack(IC2Duplicates.EXTRACTOR), "TMT", "TCT", "   ", 'T', getStack(ModItems.TREE_TAP, true), 'M', "machineBlockBasic", 'C', "circuitBasic");
			registerShaped(getStack(IC2Duplicates.GRINDER), "FFF", "SMS", " C ", 'F', Items.FLINT, 'S', getStack(Blocks.COBBLESTONE), 'M', "machineBlockBasic", 'C', "circuitBasic");
			registerShaped(getStack(IC2Duplicates.SOLAR_PANEL), "DLD", "LDL", "CGC", 'D', "dustCoal", 'L', getStack(Blocks.GLASS), 'G', getStack(IC2Duplicates.GENERATOR), 'C', "circuitBasic");		
			registerShapeless(getStack(IC2Duplicates.FREQ_TRANSMITTER), getStack(IC2Duplicates.CABLE_ICOPPER), "circuitBasic");
		}
		
		if (!CompatManager.isQuantumStorageLoaded) {
			registerShaped(getStack(ModBlocks.QUANTUM_CHEST), "DCD", "ATA", "DQD", 'D', getMaterial("dataOrb", Type.PART), 'C', getMaterial("computerMonitor", Type.PART), 'A', getMaterial("highly_advanced_machine", Type.MACHINE_FRAME), 'Q', getStack(ModBlocks.DIGITAL_CHEST), 'T', getStack(IC2Duplicates.COMPRESSOR));
			registerShaped(getStack(ModBlocks.QUANTUM_TANK), "EPE", "PCP", "EPE", 'P', "platePlatinum", 'E', "circuitAdvanced", 'C', getStack(ModBlocks.QUANTUM_CHEST));
		}	

		//Parts
		registerShaped(getMaterial("iridium_alloy", Type.INGOT), "IAI", "ADA", "IAI", 'I', "ingotIridium", 'D', "dustDiamond", 'A', "plateAdvancedAlloy");
		registerShaped(getStack(ModItems.RE_BATTERY), " W ", "TRT", "TRT", 'T', "ingotTin", 'R', "dustRedstone", 'W', getStack(IC2Duplicates.CABLE_ICOPPER));
		registerShaped(getStack(ModItems.LITHIUM_BATTERY), " C ", "PFP", "PFP", 'F', getMaterial("lithium", Type.CELL), 'P', "plateAluminum", 'C', getStack(IC2Duplicates.CABLE_IGOLD));
		registerShaped(getStack(ModItems.LITHIUM_BATTERY_PACK),	"BCB", "BPB", "B B", 'B', getStack(ModItems.LITHIUM_BATTERY), 'P', "plateAluminum", 'C', "circuitAdvanced");	
		registerShaped(getStack(ModItems.ENERGY_CRYSTAL), "RRR", "RDR", "RRR", 'R', "dustRedstone", 'D', "gemDiamond");
		registerShaped(getStack(ModItems.LAPOTRONIC_CRYSTAL), "LCL", "LEL", "LCL", 'L', "dyeBlue", 'E', "energyCrystal", 'C', "circuitBasic");
		registerShaped(getStack(ModItems.LAPOTRONIC_ORB), "LLL", "LPL", "LLL", 'L', "lapotronCrystal", 'P', getMaterialObject("iridium", Type.PLATE));
		registerShaped(getStack(ModItems.SCRAP_BOX), "SSS", "SSS", "SSS", 'S', getMaterial("scrap", Type.PART));
		registerShaped(getMaterial("machine", Type.MACHINE_FRAME), "AAA", "A A", "AAA", 'A', refined_iron);
		registerShaped(getMaterial("advanced_machine", Type.MACHINE_FRAME), " C ", "AMA", " C ", 'A', "plateAdvancedAlloy", 'C', "plateCarbon", 'M', "machineBlockBasic");
		registerShaped(getMaterial("highly_advanced_machine", Type.MACHINE_FRAME), "CTC", "TBT", "CTC", 'C', "plateChrome", 'T', "plateTitanium", 'B', "machineBlockAdvanced");
		registerShaped(getMaterial("data_storage_circuit", Type.PART), "EEE", "ECE", "EEE", 'E', "gemEmerald", 'C', "circuitBasic");
		registerShaped(getMaterial("data_control_circuit", Type.PART), "ADA", "DID", "ADA", 'I', "ingotIridium", 'A', "circuitAdvanced", 'D', getMaterial("data_storage_circuit", Type.PART));
		registerShaped(getMaterial("energy_flow_circuit", 4, Type.PART), "ATA", "LIL", "ATA", 'T', "ingotTungsten", 'I', "plateIridium", 'A', "circuitAdvanced", 'L', "lapotronCrystal");
		registerShaped(getMaterial("data_orb", Type.PART),  "DDD", "DSD", "DDD", 'D', getMaterial("data_storage_circuit", Type.PART), 'S', getMaterial("data_control_circuit", Type.PART));
		registerShaped(getMaterial("diamond_saw_blade", 4, Type.PART), "DSD", "S S", "DSD", 'D', "dustDiamond", 'S', "ingotSteel");
		registerShaped(getMaterial("diamond_grinding_head", 2, Type.PART), "DSD", "SGS", "DSD", 'S', "ingotSteel", 'D', "dustDiamond", 'G', "gemDiamond");	
		registerShaped(getMaterial("tungsten_grinding_head", 2, Type.PART), "TST", "SBS", "TST", 'S', "ingotSteel", 'T', "ingotTungsten", 'B', "blockSteel");
		registerShaped(getMaterial("computer_monitor", Type.PART), "ADA", "DGD", "ADA", 'D', "dye", 'A', "ingotAluminum", 'G', "paneGlass");
		registerShaped(getMaterial("coolant_simple", 2, Type.PART), " T ", "TWT", " T ", 'T', "ingotTin", 'W', getStack(Items.WATER_BUCKET));
		registerShaped(getMaterial("coolant_triple", Type.PART), "TTT", "CCC", "TTT", 'T', "ingotTin", 'C', getMaterial("coolant_simple", Type.PART));
		registerShaped(getMaterial("coolant_six", Type.PART), "TCT", "TPT", "TCT", 'T', "ingotTin", 'C', getMaterial("coolant_triple", Type.PART), 'P', "plateCopper");
		registerShaped(getMaterial("helium_coolant_simple", Type.PART), " T ", "TCT", " T ", 'T', "ingotTin", 'C', getMaterial("helium", Type.CELL));
		registerShaped(getMaterial("helium_coolant_triple", Type.PART), "TTT", "CCC", "TTT", 'T', "ingotTin", 'C', getMaterial("helium_coolant_simple", Type.PART));
		registerShaped(getMaterial("helium_coolant_six", Type.PART), "THT", "TCT", "THT", 'T', "ingotTin", 'C', "ingotCopper", 'H', getMaterial("helium_coolant_triple", Type.PART));
		registerShaped(getMaterial("nak_coolant_simple", Type.PART), "TST", "PCP", "TST", 'T', "ingotTin", 'C', getMaterial("coolant_simple", Type.PART), 'S', getMaterial("sodium", Type.CELL), 'P', getMaterial("potassium", Type.CELL));
		registerShaped(getMaterial("nak_coolant_simple", Type.PART), "TPT", "SCS", "TPT", 'T', "ingotTin", 'C', getMaterial("coolant_simple", Type.PART), 'S', getMaterial("sodium", Type.CELL), 'P', getMaterial("potassium", Type.CELL));
		registerShaped(getMaterial("nak_coolant_triple", Type.PART), "TTT", "CCC", "TTT", 'T', "ingotTin", 'C', getMaterial("nak_coolant_simple", Type.PART));
		registerShaped(getMaterial("nak_coolant_six", Type.PART), "THT", "TCT", "THT", 'T', "ingotTin", 'C', "ingotCopper", 'H', getMaterial("nak_coolant_triple", Type.PART));
		registerShaped(getMaterial("iridium_neutron_reflector", Type.PART), "PPP", "PIP", "PPP", 'P', getMaterial("thick_neutron_reflector", Type.PART), 'I', "ingotIridium");
		registerShaped(getMaterial("thick_neutron_reflector", Type.PART), " P ", "PCP", " P ", 'P', getMaterial("neutron_reflector", Type.PART), 'C', getMaterial("Berylium", Type.CELL));
		registerShaped(getMaterial("neutron_reflector", Type.PART), "TCT", "CPC", "TCT", 'T', "dustTin", 'C', "dustCoal", 'P', "plateCopper");
		registerShaped(getMaterial("super_conductor", 4, Type.PART), "CCC", "TIT", "EEE", 'E', "circuitMaster", 'C', getMaterial("heliumCoolantSimple", Type.PART), 'T', "ingotTungsten", 'I', "plateIridium");
		
		if (!IC2Duplicates.deduplicate()) {
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
			registerShapeless(getMaterial("carbon_fiber", Type.PART), "dustCoal", "dustCoal", "dustCoal", "dustCoal");
			registerShapeless(getMaterial("carbon_fiber", Type.PART), getMaterial("carbon", Type.CELL), getMaterial("carbon", Type.CELL), getMaterial("carbon", Type.CELL), getMaterial("carbon", Type.CELL), getMaterial("carbon", Type.CELL), getMaterial("carbon", Type.CELL), getMaterial("carbon", Type.CELL), getMaterial("carbon", Type.CELL), getMaterial("carbon", Type.CELL));
			registerShapeless(getMaterial("carbon_mesh", Type.PART), getMaterial("carbon_fiber", Type.PART), getMaterial("carbon_fiber", Type.PART));
			registerShaped(getMaterial("electronic_circuit", Type.PART), "WWW", "SRS", "WWW", 'R', refined_iron, 'S', Items.REDSTONE, 'W', getStack(IC2Duplicates.CABLE_ICOPPER));
			registerShaped(getMaterial("advanced_circuit", Type.PART), "RGR", "LCL", "RGR", 'R', "dustRedstone", 'G', "dustGlowstone", 'L', "gemLapis", 'C', "circuitBasic");
		}
		
		//UU-Matter
		registerShaped(getStack(Blocks.LOG, 8), 				" U ", "   ", "   ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.STONE, 16), 				"   ", " U ", "   ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.SNOW, 16), 				"U U", "   ", "   ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.GRASS, 16), 				"   ", "U  ", "U  ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.OBSIDIAN, 12), 			"U U", "U U", "   ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.GLASS, 32), 				" U ", "U U", " U ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.DYE, 32, 3), 				"UU ", "  U", "UU ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.GLOWSTONE, 8), 			" U ", "U U", "UUU", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.CACTUS, 48), 			" U ", "UUU", "U U", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.REEDS, 48), 				"U U", "U U", "U U", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.VINE, 24), 				"U  ", "U  ", "U  ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.SNOWBALL, 16), 			"   ", "   ", "UUU", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.CLAY_BALL, 48), 			"UU ", "U  ", "UU ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.WATERLILY, 64), 			"U U", " U ", " U ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.GUNPOWDER, 15), 			"UUU", "U  ", "UUU", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.BONE, 32), 				"U  ", "UU ", "U  ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.FEATHER, 32), 			" U ", " U ", "U U", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.DYE, 48), 				" UU", " UU", " U ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.ENDER_PEARL, 1), 			"UUU", "U U", " U ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.COAL, 5), 				"  U", "U  ", "  U", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.IRON_ORE, 2), 			"U U", " U ", "U U", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.GOLD_ORE, 2), 			" U ", "UUU", " U ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.REDSTONE, 24), 			"   ", " U ", "UUU", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.DYE, 9, 4), 				" U ", " U ", " UU", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.EMERALD_ORE, 1), 		"UU ", "U U", " UU", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.EMERALD, 2), 				"UUU", "UUU", " U ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.DIAMOND, 1), 				"UUU", "UUU", "UUU", 'U', ModItems.UU_MATTER);
		registerShaped(getMaterial("tin", 10, Type.DUST), 		"   ", "U U", "  U", 'U', ModItems.UU_MATTER);
		registerShaped(getMaterial("copper", 10, Type.DUST), 	"  U", "U U", "   ", 'U', ModItems.UU_MATTER);
		registerShaped(getMaterial("lead", 14, Type.DUST), 		"UUU", "UUU", "U  ", 'U', ModItems.UU_MATTER);
		registerShaped(getMaterial("platinum", Type.DUST), 		"  U", "UUU", "UUU", 'U', ModItems.UU_MATTER);
		registerShaped(getMaterial("tungsten", Type.DUST), 		"U  ", "UUU", "UUU", 'U', ModItems.UU_MATTER);
		registerShaped(getMaterial("titanium", 2, Type.DUST), 	"UUU", " U ", " U ", 'U', ModItems.UU_MATTER);
		registerShaped(getMaterial("aluminum", 16, Type.DUST), 	" U ", " U ", "UUU", 'U', ModItems.UU_MATTER);
		registerShaped(getMaterial("iridium", 1, Type.ORE), 	"UUU", " U ", "UUU", 'U', ModItems.UU_MATTER);
		
		for (String part : ItemParts.types) {
			if (part.endsWith("Gear")) {
				registerShaped(getMaterial(part, Type.PART), " O ", "OIO", " O ", 'I', getStack(Items.IRON_INGOT), 'O', "ingot" + StringUtils.toFirstCapital(part.replace("Gear", "")));
			}
		}
		
		Core.logHelper.info("Crafting Table Recipes Added");
	}

	static void registerCompressionRecipes() {
		for (String name : ArrayUtils.addAll(BlockStorage.types, BlockStorage2.types)) {
			if (OreUtil.hasIngot(name)) {
				registerShaped(BlockStorage.getStorageBlockByName(name), "AAA", "AAA", "AAA", 'A',
					"ingot" + StringUtils.toFirstCapital(name));
			} else if (OreUtil.hasGem(name)) {
				registerShaped(BlockStorage.getStorageBlockByName(name), "AAA", "AAA", "AAA", 'A',
					"gem" + StringUtils.toFirstCapital(name));
			}
		}

		for (String name : ItemDustsSmall.types) {
			if (name.equals(ModItems.META_PLACEHOLDER)) {
				continue;
			}
			registerShapeless(getMaterial(name, 4, Type.SMALL_DUST), getMaterialObject(name, Type.DUST));
			registerShapeless(getMaterial(name, Type.DUST), getMaterialObject(name, Type.SMALL_DUST), getMaterialObject(name, Type.SMALL_DUST), getMaterialObject(name, Type.SMALL_DUST), getMaterialObject(name, Type.SMALL_DUST));
		}

		for (String nuggets : ItemNuggets.types) {
			if (nuggets.equals(ModItems.META_PLACEHOLDER) || nuggets.equalsIgnoreCase("diamond"))
				continue;
			registerShapeless(getMaterial(nuggets, 9, Type.NUGGET), CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "ingot_" + nuggets));
			registerShaped(getMaterial(nuggets, Type.INGOT), "NNN", "NNN", "NNN", 'N', CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "nugget_" + nuggets));
		}

		registerShapeless(getMaterial("diamond", 9, Type.NUGGET), "gemDiamond");
		registerShaped(getStack(Items.DIAMOND), "NNN", "NNN", "NNN", 'N', "nuggetDiamond");
	}

	static void registerMixedMetalIngotRecipes() {
		if (!IC2Duplicates.deduplicate()) {
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
		if (top.equals("ingotRefinedIron") && IC2Duplicates.deduplicate()) {
			registerShaped(getMaterial("mixed_metal", amount, Type.INGOT), "TTT", "MMM", "BBB", 'T', getStack(IC2Duplicates.REFINED_IRON), 'M', middle, 'B', bottom);
		} else {
			registerShaped(getMaterial("mixed_metal", amount, Type.INGOT), "TTT", "MMM", "BBB", 'T', top, 'M', middle, 'B', bottom);
		}
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
}
