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

import com.google.common.base.CaseFormat;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import reborncore.common.util.OreUtil;
import reborncore.common.util.RebornCraftingHelper;
import reborncore.common.util.StringUtils;
import techreborn.Core;
import techreborn.blocks.BlockStorage;
import techreborn.blocks.BlockStorage2;
import techreborn.blocks.cable.EnumCableType;
import techreborn.compat.CompatManager;
import techreborn.config.ConfigTechReborn;
import techreborn.init.IC2Duplicates;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.items.ItemDynamicCell;
import techreborn.items.ItemUpgrades;
import techreborn.items.ingredients.ItemDustsSmall;
import techreborn.items.ingredients.ItemNuggets;
import techreborn.items.ingredients.ItemParts;
import techreborn.lib.ModInfo;

/**
 * Created by Prospector
 */
@RebornRegistry(modID = ModInfo.MOD_ID)
public class CraftingTableRecipes extends RecipeMethods {

	@ConfigRegistry(config = "recipes", category = "crafting", key = "cellRecipe", comment = "Enables the new thermal expansion compatible cell recipe")
	public static boolean newCellRecipe = true;

	public static void init() {

		registerCompressionRecipes();
		registerMixedMetalIngotRecipes();
		
		// Wood stuff
		registerShapeless(getStack(ModBlocks.RUBBER_PLANKS, 4), IC2Duplicates.RUBBER_WOOD.getStackBasedOnConfig());
		registerShaped(new ItemStack(ModBlocks.RUBBER_LOG_SLAB_HALF, 6),  "WWW", 'W', new ItemStack(ModBlocks.RUBBER_PLANKS));
		registerShaped(new ItemStack(ModBlocks.RUBBER_LOG_STAIR, 4),  "W  ", "WW ", "WWW", 'W', new ItemStack(ModBlocks.RUBBER_PLANKS));
		
		registerShapeless(BlockStorage2.getStorageBlockByName("iridium_reinforced_stone", 1), "stone", "plateIridiumAlloy");
		registerShapeless(BlockStorage2.getStorageBlockByName("iridium_reinforced_tungstensteel", 1), BlockStorage2.getStorageBlockByName("tungstensteel", 1), "plateIridium");
		registerShapeless(BlockStorage2.getStorageBlockByName("iridium_reinforced_tungstensteel", 1), BlockStorage2.getStorageBlockByName("iridium_reinforced_stone", 1), getMaterialObject("tungstensteel", Type.INGOT));
		
		if(newCellRecipe){
			registerShaped(ItemDynamicCell.getEmptyCell(16), " T ", "TGT", " T ", 'T', "ingotTin", 'G', "paneGlass"); // Blame thermal expansion for making gears have the same recipe
		} else {
			registerShaped(ItemDynamicCell.getEmptyCell(16), " T ", "T T", " T ", 'T', "ingotTin");
		}
		
		if (!IC2Duplicates.deduplicate()) {
			registerShaped(getStack(ModBlocks.REFINED_IRON_FENCE), "RRR", "RRR", 'R', "ingotRefinedIron");	
		}

		registerShaped(getStack(ModBlocks.REINFORCED_GLASS, 7), "GAG", "GGG", "GAG", 'A', "plateAdvancedAlloy", 'G', "blockGlass");
		registerShaped(getStack(ModBlocks.REINFORCED_GLASS, 7), "GGG", "AGA", "GGG", 'A', "plateAdvancedAlloy", 'G', "blockGlass");
		
		// Tools and devices		
		registerShaped(getStack(ModItems.WRENCH), "BNB", "NBN", " B ", 'B', "ingotBronze", 'N', "nuggetBronze");	
		registerShaped(getStack(ModItems.TREE_TAP), " S ", "PPP", "P  ", 'S', "stickWood", 'P', "plankWood");
		registerShaped(getStack(ModItems.ELECTRIC_TREE_TAP), "TB", "  ", 'T', getStack(ModItems.TREE_TAP), 'B', "reBattery");
		registerShaped(getStack(ModItems.NANOSABER), "DC ", "DC ", "GLG", 'L', "lapotronCrystal", 'C', "plateCarbon", 'D', "plateDiamond", 'G', getMaterialObject("glowstone", Type.SMALL_DUST));
		ItemStack rockCutter = getStack(ModItems.ROCK_CUTTER);
		rockCutter.addEnchantment(Enchantments.SILK_TOUCH, 1);
		registerShaped(rockCutter, "DT ", "DT ", "DCB", 'D', "dustDiamond", 'T', "ingotTitanium", 'C', "circuitBasic", 'B', "reBattery");
		registerShaped(getStack(ModItems.STEEL_DRILL), " S ", "SCS", "SBS", 'S', "ingotSteel", 'C', "circuitBasic", 'B', "reBattery");
		registerShaped(getStack(ModItems.DIAMOND_DRILL), " D ", "DCD", "TST", 'D', "gemDiamond", 'C', "circuitAdvanced", 'S', getStack(ModItems.STEEL_DRILL, 1, OreDictionary.WILDCARD_VALUE), 'T', "ingotTitanium");
		registerShaped(getStack(ModItems.ADVANCED_DRILL), " I ", "NCN", "OAO", 'I', "plateIridiumAlloy", 'N', "nuggetIridium", 'A', getStack(ModItems.DIAMOND_DRILL, 1, OreDictionary.WILDCARD_VALUE), 'C', "circuitMaster", 'O', getMaterial("overclock", Type.UPGRADE));
		registerShaped(getStack(ModItems.STEEL_CHAINSAW), " SS", "SCS", "BS ", 'S', "ingotSteel", 'C', "circuitBasic", 'B', "reBattery");
		registerShaped(getStack(ModItems.DIAMOND_CHAINSAW), " DD", "TCD", "ST ", 'D', "gemDiamond", 'C', "circuitAdvanced", 'S', getStack(ModItems.STEEL_CHAINSAW, 1, OreDictionary.WILDCARD_VALUE), 'T', "ingotTitanium");
		registerShaped(getStack(ModItems.ADVANCED_CHAINSAW), " NI", "OCN", "DO ", 'I', "plateIridiumAlloy", 'N', "nuggetIridium", 'D', getStack(ModItems.DIAMOND_CHAINSAW, 1, OreDictionary.WILDCARD_VALUE), 'C', "circuitMaster", 'O', getMaterial("overclock", Type.UPGRADE));
		registerShaped(getStack(ModItems.STEEL_JACKHAMMER), "SBS", "SCS", " S ", 'S', "ingotSteel", 'C', "circuitBasic", 'B', "reBattery");
		registerShaped(getStack(ModItems.DIAMOND_JACKHAMMER), "DSD", "TCT", " D ", 'D', "gemDiamond", 'C', "circuitAdvanced", 'S', getStack(ModItems.STEEL_JACKHAMMER, 1, OreDictionary.WILDCARD_VALUE), 'T', "ingotTitanium");
		registerShaped(getStack(ModItems.ADVANCED_JACKHAMMER), "NDN", "OCO", " I ", 'I', "plateIridiumAlloy", 'N', "nuggetIridium", 'D', getStack(ModItems.DIAMOND_JACKHAMMER, 1, OreDictionary.WILDCARD_VALUE), 'C', "circuitMaster", 'O', getMaterial("overclock", Type.UPGRADE));
		
		// Armour
		registerShaped(getStack(ModItems.CLOAKING_DEVICE), "CIC", "IOI", "CIC", 'C', "ingotChrome", 'I', "plateIridiumAlloy", 'O', getStack(ModItems.LAPOTRONIC_ORB));
		registerShaped(getStack(ModItems.LAPOTRONIC_ORB_PACK), "FOF", "SPS", "FIF", 'F', "circuitMaster", 'O', getStack(ModItems.LAPOTRONIC_ORB), 'S', "craftingSuperconductor", 'I', "ingotIridium", 'P', getStack(ModItems.LITHIUM_BATTERY_PACK));
		registerShaped(getStack(ModItems.LITHIUM_BATTERY_PACK),	"BCB", "BPB", "B B", 'B', getStack(ModItems.LITHIUM_BATTERY), 'P', "plateAluminum", 'C', "circuitAdvanced");
		if (ConfigTechReborn.enableGemArmorAndTools) {
			addToolAndArmourRecipes(getStack(ModItems.RUBY_SWORD), getStack(ModItems.RUBY_PICKAXE), getStack(ModItems.RUBY_AXE), getStack(ModItems.RUBY_HOE), getStack(ModItems.RUBY_SPADE), getStack(ModItems.RUBY_HELMET), getStack(ModItems.RUBY_CHESTPLATE), getStack(ModItems.RUBY_LEGGINGS), getStack(ModItems.RUBY_BOOTS), "gemRuby");
			addToolAndArmourRecipes(getStack(ModItems.SAPPHIRE_SWORD), getStack(ModItems.SAPPHIRE_PICKAXE), getStack(ModItems.SAPPHIRE_AXE), getStack(ModItems.SAPPHIRE_HOE), getStack(ModItems.SAPPHIRE_SPADE), getStack(ModItems.SAPPHIRE_HELMET), getStack(ModItems.SAPPHIRE_CHSTPLATE), getStack(ModItems.SAPPHIRE_LEGGINGS), getStack(ModItems.SAPPHIRE_BOOTS), "gemSapphire");
			addToolAndArmourRecipes(getStack(ModItems.PERIDOT_SWORD), getStack(ModItems.PERIDOT_PICKAXE), getStack(ModItems.PERIDOT_AXE), getStack(ModItems.PERIDOT_HOE), getStack(ModItems.PERIDOT_SAPPHIRE), getStack(ModItems.PERIDOT_HELMET), getStack(ModItems.PERIDOT_CHESTPLATE), getStack(ModItems.PERIDOT_LEGGINGS), getStack(ModItems.PERIDOT_BOOTS), "gemPeridot");
			addToolAndArmourRecipes(getStack(ModItems.BRONZE_SWORD), getStack(ModItems.BRONZE_PICKAXE), getStack(ModItems.BRONZE_AXE), getStack(ModItems.BRONZE_HOE), getStack(ModItems.BRONZE_SPADE), getStack(ModItems.BRONZE_HELMET), getStack(ModItems.BRONZE_CHESTPLATE), getStack(ModItems.BRONZE_LEGGINGS), getStack(ModItems.BRONZE_BOOTS), "ingotBronze");
		}

		// Battery
		registerShaped(getStack(ModItems.LAPOTRONIC_ORB), "LLL", "LPL", "LLL", 'L', "lapotronCrystal", 'P', "plateIridiumAlloy");
		registerShaped(getStack(ModItems.LITHIUM_BATTERY), " C ", "PFP", "PFP", 'F', getCell("lithium"), 'P', "plateAluminum", 'C', getStack(IC2Duplicates.CABLE_IGOLD));

		if(!IC2Duplicates.deduplicate()){
			registerShaped(getStack(ModItems.ENERGY_CRYSTAL), "RRR", "RDR", "RRR", 'R', "dustRedstone", 'D', "gemDiamond");
			registerShaped(getStack(ModItems.ENERGY_CRYSTAL), "RRR", "RBR", "RRR", 'R', "dustRedstone", 'B', "gemRuby");
			registerShaped(getStack(ModItems.LAPOTRONIC_CRYSTAL), "LCL", "LEL", "LCL", 'L', getStack(Items.DYE, 1, 4), 'E', "energyCrystal", 'C', "circuitBasic");
			registerShaped(getStack(ModItems.LAPOTRONIC_CRYSTAL), "LCL", "LSL", "LCL", 'L', getStack(Items.DYE, 1, 4), 'S', "gemSapphire", 'C', "circuitBasic");
			registerShaped(getStack(ModItems.RE_BATTERY), " W ", "TRT", "TRT", 'T', "ingotTin", 'R', "dustRedstone", 'W', getStack(IC2Duplicates.CABLE_ICOPPER));
		}

		//Upgrades
		registerShaped(ItemUpgrades.getUpgradeByName("energy_storage"), "PPP", "WBW", "PCP", 'P', "plankWood", 'W', getStack(IC2Duplicates.CABLE_ICOPPER), 'C', "circuitBasic", 'B', "reBattery");
		registerShaped(ItemUpgrades.getUpgradeByName("overclock"), "TTT", "WCW", 'T', getMaterial("coolant_simple", Type.PART), 'W', getStack(IC2Duplicates.CABLE_ICOPPER), 'C', "circuitBasic");
		registerShaped(ItemUpgrades.getUpgradeByName("overclock", 2), " T ", "WCW", 'T', getMaterial("helium_coolant_triple", Type.PART), 'W', getStack(IC2Duplicates.CABLE_ICOPPER), 'C', "circuitBasic");
		registerShaped(ItemUpgrades.getUpgradeByName("overclock", 2),  " T ", "WCW", 'T', getMaterial("nak_coolant_simple", Type.PART), 'W', getStack(IC2Duplicates.CABLE_ICOPPER), 'C', "circuitBasic");
		registerShaped(ItemUpgrades.getUpgradeByName("transformer"), "GGG", "WTW", "GCG", 'G', "blockGlass", 'W', getStack(IC2Duplicates.CABLE_IGOLD), 'C', "circuitBasic", 'T', getStack(IC2Duplicates.MVT));
		registerShaped(ItemUpgrades.getUpgradeByName("superconductor"), "SOS", "CMC", "SOS", 'C', EnumCableType.SUPERCONDUCTOR.getStack(), 'M', getMaterial("highly_advanced_machine", Type.MACHINE_FRAME) , 'O', getMaterial("data_orb", Type.PART), 'S', getMaterial("enhanced_super_conductor", Type.PART));

		//Machines
		registerShaped(getMaterial("standard", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "ingotRefinedIron", 'C', "circuitBasic", 'A', "machineBlockBasic");
		registerShaped(getMaterial("standard", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "plateIron", 'C', "circuitBasic", 'A', "machineBlockBasic");
		registerShaped(getMaterial("standard", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "plateAluminum", 'C', "circuitBasic", 'A', "machineBlockBasic");
		registerShaped(getMaterial("reinforced", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "plateSteel", 'C', "circuitAdvanced", 'A', "machineBlockAdvanced");
		registerShaped(getMaterial("reinforced", 1, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "plateSteel", 'C', "circuitAdvanced", 'A', getMaterial("standard", Type.MACHINE_CASING));
		registerShaped(getMaterial("advanced", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R',  "plateChrome", 'C', "circuitElite", 'A', "machineBlockElite");
		registerShaped(getMaterial("advanced", 1, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R',  "plateChrome", 'C', "circuitElite", 'A', getMaterial("reinforced", Type.MACHINE_CASING));

		registerShaped(getStack(ModBlocks.WIND_MILL), " I ", " G ", " I ", 'I', "plateMagnalium", 'G', getStack(IC2Duplicates.GENERATOR));
		registerShaped(getStack(ModBlocks.WIND_MILL), "IGI", 'I', "plateMagnalium", 'G', getStack(IC2Duplicates.GENERATOR));
		registerShaped(getStack(ModBlocks.LIGHTNING_ROD), "CAC", "ACA", "CAC", 'A', getStack(ModBlocks.MACHINE_CASINGS, 1, 2), 'C', "circuitMaster");
		registerShaped(getStack(ModBlocks.IRON_ALLOY_FURNACE), "III", "F F", "III", 'I', "ingotRefinedIron", 'F', getStack(IC2Duplicates.IRON_FURNACE));
		registerShaped(getStack(ModBlocks.INDUSTRIAL_ELECTROLYZER), "RER", "CFC", "RER", 'R', "plateIron", 'E', getStack(IC2Duplicates.EXTRACTOR), 'C', "circuitAdvanced", 'F', "machineBlockAdvanced");
		registerShaped(getStack(ModBlocks.INDUSTRIAL_CENTRIFUGE), "RCR", "AEA", "RCR", 'R', "ingotRefinedIron", 'E', getStack(IC2Duplicates.EXTRACTOR), 'A', "machineBlockAdvanced", 'C', "circuitAdvanced");
		registerShaped(getStack(ModBlocks.INDUSTRIAL_CENTRIFUGE), "RCR", "AEA", "RCR", 'R', "plateAluminum", 'E', getStack(IC2Duplicates.EXTRACTOR), 'A', "machineBlockAdvanced", 'C', "circuitAdvanced");
		registerShaped(getStack(ModBlocks.INDUSTRIAL_SAWMILL), "PAP", "SSS", "ACA", 'P', "ingotRefinedIron", 'A', "circuitAdvanced", 'S', getMaterial("diamond_saw_blade", Type.PART), 'C', "machineBlockAdvanced");
		registerShaped(getStack(ModBlocks.INDUSTRIAL_BLAST_FURNACE), "CHC", "HBH", "FHF", 'H', getMaterial("cupronickelHeatingCoil", Type.PART), 'C', "circuitAdvanced", 'B', "machineBlockAdvanced", 'F', getStack(IC2Duplicates.ELECTRICAL_FURNACE));
		registerShaped(getStack(ModBlocks.INDUSTRIAL_GRINDER), "ECG", "HHH", "CBC", 'E', getStack(ModBlocks.INDUSTRIAL_ELECTROLYZER), 'H', "craftingDiamondGrinder", 'C', "circuitAdvanced", 'B', "machineBlockAdvanced", 'G', getStack(IC2Duplicates.GRINDER));
		registerShaped(getStack(ModBlocks.IMPLOSION_COMPRESSOR), "ABA", "CPC", "ABA", 'A', getMaterialObject("advancedAlloy", Type.INGOT), 'C', "circuitAdvanced", 'B', "machineBlockAdvanced", 'P', getStack(IC2Duplicates.COMPRESSOR));
		registerShaped(getStack(ModBlocks.VACUUM_FREEZER), "SPS", "CGC", "SPS", 'S', "plateSteel", 'C', "circuitAdvanced", 'G', "glassReinforced", 'P', getStack(IC2Duplicates.EXTRACTOR));
		registerShaped(getStack(ModBlocks.DISTILLATION_TOWER), "CMC", "PBP", "EME", 'E', getStack(ModBlocks.INDUSTRIAL_ELECTROLYZER), 'M', "circuitMaster", 'B', "machineBlockElite", 'C', getStack(ModBlocks.INDUSTRIAL_CENTRIFUGE), 'P', getStack(IC2Duplicates.EXTRACTOR));
		registerShaped(getStack(ModBlocks.PLATE_BENDING_MACHINE), "PCP", "MBM", "PCP", 'P', getStack(Blocks.PISTON), 'C', "circuitBasic", 'M', getStack(IC2Duplicates.COMPRESSOR), 'B', "machineBlockBasic");
		registerShaped(getStack(ModBlocks.PLATE_BENDING_MACHINE), "PCP", "MBM", "PCP", 'P', getStack(Blocks.STICKY_PISTON), 'C', "circuitBasic", 'M', getStack(IC2Duplicates.COMPRESSOR), 'B', "machineBlockBasic");
		registerShaped(getStack(ModBlocks.ASSEMBLING_MACHINE), "CIC", "PBP", "CPC", 'I', getStack(Blocks.PISTON), 'P', "plateRefinedIron", 'C', "circuitBasic", 'B', "machineBlockBasic");
		registerShaped(getStack(ModBlocks.ASSEMBLING_MACHINE), "CIC", "PBP", "CPC", 'I', getStack(Blocks.PISTON), 'P', "plateAluminum", 'C', "circuitBasic", 'B', "machineBlockBasic");
		//
		registerShaped(getStack(ModBlocks.WIRE_MILL), "PEP", "CBC", "PSP", 'P', "plateBrass", 'C', "circuitBasic", 'E', getStack(IC2Duplicates.EXTRACTOR), 'B', "machineBlockBasic", 'S', getStack(Blocks.PISTON));
		registerShaped(getStack(ModBlocks.WIRE_MILL), "PEP", "CBC", "PSP", 'P', "plateBrass", 'C', "circuitBasic", 'E', getStack(IC2Duplicates.EXTRACTOR), 'B', "machineBlockBasic", 'S', getStack(Blocks.STICKY_PISTON));
		//
		registerShaped(getStack(ModBlocks.CHEMICAL_REACTOR), "IMI", "CPC", "IEI", 'I', "plateInvar", 'C', "circuitAdvanced", 'M', getStack(IC2Duplicates.EXTRACTOR), 'P', getStack(IC2Duplicates.COMPRESSOR), 'E', getStack(IC2Duplicates.EXTRACTOR));
		registerShaped(getStack(ModBlocks.ROLLING_MACHINE),  "PCP", "MBM", "PCP", 'P', getStack(Blocks.PISTON), 'C', "circuitAdvanced", 'M', getStack(IC2Duplicates.COMPRESSOR), 'B', "machineBlockBasic");
		registerShaped(getStack(ModBlocks.AUTO_CRAFTING_TABLE), "MPM", "PCP", "MPM", 'M', "circuitAdvanced", 'C', "workbench", 'P', "plateIron");
		registerShaped(getStack(ModBlocks.CHARGE_O_MAT),  "ETE", "COC", "EAE", 'E', "circuitMaster", 'T', "energyCrystal", 'C', "chest", 'O', getStack(ModItems.LAPOTRONIC_ORB), 'A', "machineBlockAdvanced");
		registerShaped(getStack(ModBlocks.ALLOY_SMELTER), " C ", "FMF", "   ", 'C', "circuitBasic", 'F', getStack(IC2Duplicates.ELECTRICAL_FURNACE), 'M', "machineBlockBasic");
		registerShaped(getStack(ModBlocks.INTERDIMENSIONAL_SU), "PAP", "ACA", "PAP", 'P', "plateIridiumAlloy", 'C', "chestEnder", 'A', getStack(ModBlocks.ADJUSTABLE_SU));
		registerShaped(getStack(ModBlocks.ADJUSTABLE_SU), "LLL", "LCL", "LLL", 'L', getStack(ModItems.LAPOTRONIC_ORB), 'C', "energyCrystal");
		registerShaped(getStack(ModBlocks.LAPOTRONIC_SU),  " L ", "CBC", " M ", 'L', getStack(IC2Duplicates.LVT), 'C', "circuitAdvanced", 'M', getStack(IC2Duplicates.MVT), 'B', getStack(ModBlocks.LSU_STORAGE));
		registerShaped(getStack(ModBlocks.LSU_STORAGE), "LLL", "LCL", "LLL", 'L', "blockLapis", 'C', "circuitBasic");
		registerShaped(getStack(ModBlocks.SCRAPBOXINATOR), "ICI", "DSD", "ICI", 'S', getStack(ModItems.SCRAP_BOX), 'C', "circuitBasic", 'I', "plateIron", 'D', "dirt");
		registerShaped(getStack(ModBlocks.FUSION_CONTROL_COMPUTER), "CCC", "PTP", "CCC", 'P', "energyCrystal", 'T', getStack(ModBlocks.FUSION_COIL), 'C', "circuitMaster");
//		registerShaped(getStack(ModBlocks.FUSION_COIL), "CSC", "NAN", "CRC", 'A', getStack(ModBlocks.MACHINE_CASINGS, 1, 2), 'N', getMaterial("nichromeHeatingCoil", Type.PART), 'C', "circuitMaster", 'S', "craftingSuperconductor", 'R', IC2Duplicates.IRIDIUM_NEUTRON_REFLECTOR.getStackBasedOnConfig());
		registerShaped(getStack(ModBlocks.DIGITAL_CHEST), "PPP", "PDP", "PCP", 'P', "plateAluminum",  'D', getMaterial("data_orb", Type.PART), 'C', getMaterial("computer_monitor", Type.PART));
		registerShaped(getStack(ModBlocks.DIGITAL_CHEST), "PPP", "PDP", "PCP", 'P', "plateSteel",  'D', getMaterial("data_orb", Type.PART), 'C', getMaterial("computer_monitor", Type.PART));
		registerShaped(getStack(ModBlocks.MATTER_FABRICATOR), "ETE", "AOA", "ETE", 'E', "circuitMaster", 'T', getStack(IC2Duplicates.EXTRACTOR), 'A', "machineBlockElite", 'O', getStack(ModItems.LAPOTRONIC_ORB));
		registerShaped(getStack(ModBlocks.COMPUTER_CUBE), "OMC", "MFM", "CMO", 'O', getMaterial("data_orb", Type.PART), 'M', getMaterial("computer_monitor", Type.PART), 'C', "circuitMaster", 'F', "machineBlockAdvanced");
		registerShaped(getStack(ModBlocks.PLAYER_DETECTOR, true), " D ", "CFC", " D ", 'D', "circuitStorage", 'C', "circuitAdvanced", 'F',  getStack(ModBlocks.COMPUTER_CUBE));
		registerShaped(getStack(ModBlocks.DRAGON_EGG_SYPHON), "CTC", "PSP", "CBC", 'C', "circuitMaster", 'T', getStack(IC2Duplicates.MFE), 'P', "plateIridiumAlloy", 'S', "craftingSuperconductor", 'B', getStack(ModItems.LAPOTRONIC_ORB));
		registerShaped(getStack(ModBlocks.PLASMA_GENERATOR), "PPP", "PTP", "CGC", 'P', "plateTungstensteel", 'T', getStack(IC2Duplicates.HVT), 'C', "circuitMaster", 'G', getStack(IC2Duplicates.GENERATOR));

		// Transformers >>
		if (!IC2Duplicates.deduplicate()) {
			registerShaped(getStack(IC2Duplicates.LVT), "PWP", "CCC", "PPP", 'P', "plankWood", 'C', "ingotCopper", 'W', getStack(IC2Duplicates.CABLE_TIN));
			registerShaped(getStack(IC2Duplicates.MVT), " G ", " M ", " G ", 'M', "machineBlockBasic", 'G', getStack(IC2Duplicates.CABLE_ICOPPER));
			registerShaped(getStack(IC2Duplicates.HVT), " H ", "CML", " H ", 'M', getStack(IC2Duplicates.MVT), 'H', getStack(IC2Duplicates.CABLE_GOLD), 'C', "circuitBasic", 'L', getStack(ModItems.LITHIUM_BATTERY));
		}
		registerShaped(getStack(ModBlocks.EV_TRANSFORMER), " H ", "CML", " H ", 'M', getStack(IC2Duplicates.HVT), 'H', getStack(IC2Duplicates.CABLE_IHV), 'C', "circuitAdvanced", 'L', getStack(IC2Duplicates.ENERGY_CRYSTAL));
		// << Transformers

		// Solar Panels >>
		registerShaped(getStack(ModBlocks.SOLAR_PANEL, 1, 0), "DLD", "LDL", "CGC", 'D', "dustCoal", 'L', "paneGlass", 'G', getStack(IC2Duplicates.GENERATOR), 'C', "circuitBasic");
		registerShaped(getStack(ModBlocks.SOLAR_PANEL, 1, 1), "SSS", "STS", "SSS", 'S', getStack(ModBlocks.SOLAR_PANEL, 1, 0), 'T', getStack(ModBlocks.LV_TRANSFORMER));
		registerShaped(getStack(ModBlocks.SOLAR_PANEL, 1, 2), "SSS", "STS", "SSS", 'S', getStack(ModBlocks.SOLAR_PANEL, 1, 1), 'T', getStack(ModBlocks.MV_TRANSFORMER));
		registerShaped(getStack(ModBlocks.SOLAR_PANEL, 1, 3), "SSS", "STS", "SSS", 'S', getStack(ModBlocks.SOLAR_PANEL, 1, 2), 'T', getStack(ModBlocks.HV_TRANSFORMER));
		registerShaped(getStack(ModBlocks.SOLAR_PANEL, 1, 4), "SSS", "STS", "SSS", 'S', getStack(ModBlocks.SOLAR_PANEL, 1, 3), 'T', getStack(ModBlocks.EV_TRANSFORMER));
		// << Solar Panels

		// Fluid Generators >>
		registerShaped(getStack(ModBlocks.DIESEL_GENERATOR), "III", "IHI", "CGC", 'I', "plateBronze", 'H', "glassReinforced", 'C', "circuitBasic", 'G', getStack(IC2Duplicates.GENERATOR));
		registerShaped(getStack(ModBlocks.DIESEL_GENERATOR), "III", "IHI", "CGC", 'I', "plateBrass", 'H', "glassReinforced", 'C', "circuitBasic", 'G', getStack(IC2Duplicates.GENERATOR));
		registerShaped(getStack(ModBlocks.SEMI_FLUID_GENERATOR), "III", "IHI", "CGC", 'I', "plateRefinedIron", 'H', "glassReinforced", 'C', "circuitBasic", 'G', getStack(IC2Duplicates.GENERATOR));
		registerShaped(getStack(ModBlocks.SEMI_FLUID_GENERATOR), "III", "IHI", "CGC", 'I', "plateAluminum", 'H', "glassReinforced", 'C', "circuitBasic", 'G', getStack(IC2Duplicates.GENERATOR));

		registerShaped(getStack(ModBlocks.GAS_TURBINE), "IAI", "WGW", "IAI", 'I', "plateInvar", 'A', "circuitAdvanced", 'W', getStack(ModBlocks.WIND_MILL), 'G', "glassReinforced");
		registerShaped(getStack(ModBlocks.GAS_TURBINE), "IAI", "WGW", "IAI", 'I', "plateAluminum", 'A', "circuitAdvanced", 'W', getStack(ModBlocks.WIND_MILL), 'G', "glassReinforced");
		registerShaped(getStack(ModBlocks.GAS_TURBINE), "IAI", "WGW", "IAI", 'I', "plateAluminum", 'A', "circuitAdvanced", 'W', getStack(ModBlocks.WIND_MILL), 'G', "glassReinforced");
		registerShaped(getStack(ModBlocks.THERMAL_GENERATOR), "III", "IHI", "CGC", 'I', "plateInvar", 'H', "glassReinforced", 'G', getStack(IC2Duplicates.GENERATOR), 'C', "circuitBasic");
		// << Fluid Generators

		// Tier 1 Machines >>
		registerShaped(getStack(ModBlocks.SOLID_CANNING_MACHINE), "TCT", "TBT", "TTT", 'T', "ingotTin", 'C', "circuitBasic", 'B', "machineBlockBasic");
		// << Tier 1 Machines

		// Nuclear >>
		if (Loader.isModLoaded("ic2")) {
			// Thorium Fuel Rod (Dual)
			registerShaped(getStack(ModItems.THORIUM_FUEL_ROD_DUAL), "RPR", "   ", "   ", 'R', getStack(ModItems.THORIUM_FUEL_ROD_SINGLE), 'P', "plateCopper");
			registerShaped(getStack(ModItems.THORIUM_FUEL_ROD_DUAL), "RPR", "   ", "   ", 'R', getStack(ModItems.THORIUM_FUEL_ROD_SINGLE), 'P', "plateLead");

			// Thorium Fuel Rod (Quad)
			registerShaped(getStack(ModItems.THORIUM_FUEL_ROD_QUAD), " R ", "PPP", " R ", 'R', getStack(ModItems.THORIUM_FUEL_ROD_DUAL), 'P', "plateCopper");
			registerShaped(getStack(ModItems.THORIUM_FUEL_ROD_QUAD), " R ", "PPP", " R ", 'R', getStack(ModItems.THORIUM_FUEL_ROD_DUAL), 'P', "plateLead");
		}
		// << Nuclear

		registerShaped(getStack(ModBlocks.ALARM, 1, 0), "ICI", "SRS", "ICI", 'I', "ingotIron", 'C', getMaterial("copper", Type.CABLE), 'S', IC2Duplicates.CABLE_ICOPPER.getStackBasedOnConfig(), 'R', "blockRedstone" );
		registerShaped(getStack(ModBlocks.FLUID_REPLICATOR), "PCP", "CFC", "ESR", 'P', "plateTungstensteel", 'F', "machineBlockElite", 'C', "circuitMaster", 'E', getStack(ModBlocks.INDUSTRIAL_ELECTROLYZER), 'S', "craftingSuperconductor",'R', getStack(ModBlocks.CHEMICAL_REACTOR));

		if (!IC2Duplicates.deduplicate()) {
			registerShaped(getStack(IC2Duplicates.BAT_BOX), "WCW", "BBB", "WWW", 'W', "plankWood", 'B', "reBattery", 'C', getStack(IC2Duplicates.CABLE_ICOPPER));
			registerShaped(getStack(IC2Duplicates.MFE), "GEG", "EME", "GEG", 'M', "machineBlockBasic", 'E', "energyCrystal", 'G', getStack(IC2Duplicates.CABLE_IGOLD));
			registerShaped(getStack(IC2Duplicates.MFSU), "LAL", "LML", "LOL", 'A', "circuitAdvanced", 'L', "lapotronCrystal", 'M', getStack(IC2Duplicates.MFE), 'O', "machineBlockAdvanced");
			registerShaped(getStack(IC2Duplicates.COMPRESSOR), "S S", "SCS", "SMS", 'C', "circuitBasic", 'M', "machineBlockBasic", 'S', "stone");
			registerShaped(getStack(IC2Duplicates.ELECTRICAL_FURNACE), " C ", "RFR", "   ", 'C', "circuitBasic", 'F', getStack(IC2Duplicates.IRON_FURNACE), 'R', "dustRedstone");
			registerShaped(getStack(IC2Duplicates.RECYCLER), " E ", "DCD", "GDG", 'D', "dirt", 'C', getStack(IC2Duplicates.COMPRESSOR), 'G', "dustGlowstone", 'E', "circuitBasic");
			registerShaped(getStack(IC2Duplicates.IRON_FURNACE), "III", "I I", "III", 'I', "ingotIron");
			registerShaped(getStack(IC2Duplicates.IRON_FURNACE), " I ", "I I", "IFI", 'I', "ingotIron", 'F', getStack(Blocks.FURNACE));
			registerShaped(getStack(IC2Duplicates.EXTRACTOR), "TMT", "TCT", "   ", 'T', getStack(ModItems.TREE_TAP, true), 'M', "machineBlockBasic", 'C', "circuitBasic");
			registerShaped(getStack(IC2Duplicates.GRINDER), "FFF", "SMS", " C ", 'F', Items.FLINT, 'S', getStack(Blocks.COBBLESTONE), 'M', getMaterial("machine", Type.MACHINE_FRAME), 'C', "circuitBasic");
			registerShapeless(getStack(IC2Duplicates.FREQ_TRANSMITTER), getStack(IC2Duplicates.CABLE_ICOPPER), "circuitBasic");
			registerShapeless(getStack(IC2Duplicates.GENERATOR), "reBattery", "machineBlockBasic", getStack(Blocks.FURNACE));
			registerShaped(getStack(IC2Duplicates.WATER_MILL), "SWS", "WGW", "SWS", 'S', "stickWood", 'W', "plankWood", 'G', getStack(IC2Duplicates.GENERATOR));
		}

		// Cables
		registerShaped(EnumCableType.SUPERCONDUCTOR.getStack(), "MFM", "SSS", "MFM", 'M', getMaterial("advanced_machine", Type.MACHINE_FRAME), 'S', "craftingSuperconductor", 'F', getMaterial("energy_flow_circuit", 1, Type.PART));
		registerShaped(EnumCableType.SUPERCONDUCTOR.getStack(8), "MFM", "SSS", "MFM", 'M', getMaterial("advanced_machine", Type.MACHINE_FRAME), 'S', getMaterial("enhanced_super_conductor", Type.PART), 'F', getMaterial("energy_flow_circuit", 1, Type.PART));
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
		}

		if (!CompatManager.isQuantumStorageLoaded) {
			registerShaped(getStack(ModBlocks.QUANTUM_CHEST), "DCD", "ATA", "DQD", 'D', getMaterial("dataOrb", Type.PART), 'C', getMaterial("computerMonitor", Type.PART), 'A', "machineBlockElite", 'Q', getStack(ModBlocks.DIGITAL_CHEST), 'T', getStack(IC2Duplicates.COMPRESSOR));
			registerShaped(getStack(ModBlocks.QUANTUM_TANK), "EPE", "PCP", "EPE", 'P', "platePlatinum", 'E', "circuitAdvanced", 'C', getStack(ModBlocks.QUANTUM_CHEST));
		}

		//Lighting
		registerShaped(getStack(ModBlocks.LAMP_INCANDESCENT), "GGG", "TCT", "GGG", 'G', "paneGlass", 'T', getStack(IC2Duplicates.CABLE_COPPER), 'C', getMaterial("carbon_fiber", Type.PART));
		registerShaped(getStack(ModBlocks.LAMP_LED), "GGG", "TLT", "GGG", 'G', "paneGlass", 'T', getStack(IC2Duplicates.CABLE_TIN), 'L', "dustGlowstone");

		//Parts
		registerShaped(getMaterial("iridium_alloy", Type.INGOT), "IAI", "ADA", "IAI", 'I', "ingotIridium", 'D', "dustDiamond", 'A', "plateAdvancedAlloy");
		registerShaped(getStack(ModItems.SCRAP_BOX), "SSS", "SSS", "SSS", 'S', getMaterial("scrap", Type.PART));
		registerShaped(getMaterial("machine", Type.MACHINE_FRAME), "AAA", "A A", "AAA", 'A', "ingotRefinedIron");
		registerShaped(getMaterial("advanced_machine", Type.MACHINE_FRAME), " C ", "AMA", " C ", 'A', "plateAdvancedAlloy", 'C', "plateCarbon", 'M', "machineBlockBasic");
		registerShaped(getMaterial("highly_advanced_machine", Type.MACHINE_FRAME), "CTC", "TBT", "CTC", 'C', "plateChrome", 'T', "plateTitanium", 'B', "machineBlockAdvanced");
		registerShaped(getMaterial("data_storage_circuit", Type.PART), "RGR", "LCL", "EEE", 'R', "dustRedstone", 'G', "dustGlowstone", 'L', "gemLapis", 'C', "circuitBasic", 'E', "plateEmerald");
		registerShaped(getMaterial("data_control_circuit", Type.PART), "ADA", "DID", "ADA", 'I', "ingotIridium", 'A', "circuitAdvanced", 'D', "circuitStorage");
		registerShaped(getMaterial("energy_flow_circuit", 4, Type.PART), "ATA", "LIL", "ATA", 'T', "ingotTungsten", 'I', "plateIridiumAlloy", 'A', "circuitAdvanced", 'L', "lapotronCrystal");
		registerShaped(getMaterial("data_orb", Type.PART),  "DDD", "DSD", "DDD", 'D', "circuitStorage", 'S', "circuitElite");
		registerShaped(getMaterial("diamond_saw_blade", 4, Type.PART), "DSD", "S S", "DSD", 'D', "dustDiamond", 'S', "ingotSteel");
		registerShaped(getMaterial("diamond_grinding_head", 2, Type.PART), "DSD", "SGS", "DSD", 'S', "ingotSteel", 'D', "dustDiamond", 'G', "gemDiamond");
		registerShaped(getMaterial("tungsten_grinding_head", 2, Type.PART), "TST", "SBS", "TST", 'S', "ingotSteel", 'T', "ingotTungsten", 'B', "blockSteel");
		registerShaped(getMaterial("computer_monitor", Type.PART), "ADA", "DGD", "ADA", 'D', "dye", 'A', "ingotAluminum", 'G', "paneGlass");
		registerShaped(getMaterial("coolant_simple", 2, Type.PART), " T ", "TWT", " T ", 'T', "ingotTin", 'W', getStack(Items.WATER_BUCKET));
		registerShaped(getMaterial("coolant_simple", 2, Type.PART), " T ", "TWT", " T ", 'T', "ingotTin", 'W', getCell("water"));
		registerShaped(getMaterial("coolant_triple", Type.PART), "TTT", "CCC", "TTT", 'T', "ingotTin", 'C', getMaterial("coolant_simple", Type.PART));
		registerShaped(getMaterial("coolant_six", Type.PART), "TCT", "TPT", "TCT", 'T', "ingotTin", 'C', getMaterial("coolant_triple", Type.PART), 'P', "plateCopper");
		registerShaped(getMaterial("helium_coolant_simple", Type.PART), " T ", "TCT", " T ", 'T', "ingotTin", 'C', getCell("helium"));
		registerShaped(getMaterial("helium_coolant_triple", Type.PART), "TTT", "CCC", "TTT", 'T', "ingotTin", 'C', getMaterial("helium_coolant_simple", Type.PART));
		registerShaped(getMaterial("helium_coolant_six", Type.PART), "THT", "TCT", "THT", 'T', "ingotTin", 'C', "ingotCopper", 'H', getMaterial("helium_coolant_triple", Type.PART));
		registerShaped(getMaterial("nak_coolant_simple", Type.PART), "TST", "PCP", "TST", 'T', "ingotTin", 'C', getMaterial("coolant_simple", Type.PART), 'S', getCell("sodium"), 'P', getCell("potassium"));
		registerShaped(getMaterial("nak_coolant_simple", Type.PART), "TPT", "SCS", "TPT", 'T', "ingotTin", 'C', getMaterial("coolant_simple", Type.PART), 'S', getCell("sodium"), 'P', getCell("potassium"));
		registerShaped(getMaterial("nak_coolant_triple", Type.PART), "TTT", "CCC", "TTT", 'T', "ingotTin", 'C', getMaterial("nak_coolant_simple", Type.PART));
		registerShaped(getMaterial("nak_coolant_six", Type.PART), "THT", "TCT", "THT", 'T', "ingotTin", 'C', "ingotCopper", 'H', getMaterial("nak_coolant_triple", Type.PART));
		registerShaped(getMaterial("super_conductor", 4, Type.PART), "CCC", "TIT", "EEE", 'E', "circuitMaster", 'C', getMaterial("heliumCoolantSimple", Type.PART), 'T', "ingotTungsten", 'I', "plateIridiumAlloy");

		if (!IC2Duplicates.deduplicate()) {
			registerShaped(getMaterial("carbon_fiber", Type.PART), " C ", "C C", " C ", 'C', "dustCoal");
			registerShaped(getMaterial("carbon_fiber", Type.PART), "CCC", "C C", "CCC", 'C', getCell("carbon"));
			registerShapeless(getMaterial("carbon_mesh", Type.PART), getMaterial("carbon_fiber", Type.PART), getMaterial("carbon_fiber", Type.PART));
			registerShaped(getMaterial("electronic_circuit", Type.PART), "WWW", "SRS", "WWW", 'R', "ingotRefinedIron", 'S', Items.REDSTONE, 'W', getStack(IC2Duplicates.CABLE_ICOPPER));
			registerShaped(getMaterial("advanced_circuit", Type.PART), "RGR", "LCL", "RGR", 'R', "dustRedstone", 'G', "dustGlowstone", 'L', "gemLapis", 'C', "circuitBasic");
//			registerShaped(getMaterial("iridium_neutron_reflector", Type.PART), "PPP", "PIP", "PPP", 'P', "reflectorThick", 'I', "ingotIridium");
			registerShaped(getMaterial("thick_neutron_reflector", Type.PART), " P ", "PCP", " P ", 'P', "reflectorBasic", 'C', getCell("Berylium"));
			registerShaped(getMaterial("neutron_reflector", Type.PART), "TCT", "CPC", "TCT", 'T', "dustTin", 'C', "dustCoal", 'P', "plateCopper");
		}

		//UU-Matter
		ItemStack uuStack = new ItemStack(ModItems.UU_MATTER);
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
		registerShaped(getMaterial("tin", 10, Type.DUST), 		"   ", "U U", "  U", 'U', uuStack);
		registerShaped(getMaterial("copper", 10, Type.DUST), 	"  U", "U U", "   ", 'U', uuStack);
		registerShaped(getMaterial("lead", 14, Type.DUST), 		"UUU", "UUU", "U  ", 'U', uuStack);
		registerShaped(getMaterial("platinum", Type.DUST), 		"  U", "UUU", "UUU", 'U', uuStack);
		registerShaped(getMaterial("tungsten", Type.DUST), 		"U  ", "UUU", "UUU", 'U', uuStack);
		registerShaped(getMaterial("titanium", 2, Type.DUST), 	"UUU", " U ", " U ", 'U', uuStack);
		registerShaped(getMaterial("aluminum", 16, Type.DUST), 	" U ", " U ", "UUU", 'U', uuStack);
		registerShaped(getMaterial("iridium", 1, Type.ORE), 	"UUU", " U ", "UUU", 'U', uuStack);

		for (String part : ItemParts.types) {
			if (part.endsWith("Gear")) {
				registerShaped(getMaterial(part, Type.PART), " O ", "OIO", " O ", 'I', getStack(Items.IRON_INGOT), 'O', "ingot" + StringUtils.toFirstCapital(part.replace("Gear", "")));
			}
		}

		Core.logHelper.info("Crafting Table Recipes Added");
	}

	static void registerCompressionRecipes() {
		for (String name : BlockStorage.types) {
			if (OreUtil.hasIngot(name)) {
				registerShaped(BlockStorage.getStorageBlockByName(name), "AAA", "AAA", "AAA", 'A', "ingot" + StringUtils.toFirstCapital(name));
				registerShapeless(getMaterial(name, 9, Type.INGOT), BlockStorage.getStorageBlockByName(name));
			} else if (OreUtil.hasGem(name)) {
				registerShaped(BlockStorage.getStorageBlockByName(name), "AAA", "AAA", "AAA", 'A', "gem" + StringUtils.toFirstCapital(name));
				registerShapeless(getMaterial(name, 9, Type.GEM), BlockStorage.getStorageBlockByName(name));
			}
		}

		for (String block : BlockStorage2.types){
			block = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, block);
			if (OreUtil.hasIngot(block)) {
				registerShaped(BlockStorage2.getStorageBlockByName(block), "AAA", "AAA", "AAA", 'A', "ingot" + StringUtils.toFirstCapital(block));
				registerShapeless(getMaterial(block, 9, Type.INGOT), BlockStorage2.getStorageBlockByName(block));
			} else if (OreUtil.hasGem(block)) {
				registerShaped(BlockStorage2.getStorageBlockByName(block), "AAA", "AAA", "AAA", 'A', "gem" + StringUtils.toFirstCapital(block));
				registerShapeless(getMaterial(block, 9, Type.GEM), BlockStorage2.getStorageBlockByName(block));
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
			registerShaped(getMaterial("mixed_metal", amount, Type.INGOT), "TTT", "MMM", "BBB", 'T', "ingotRefinedIron", 'M', middle, 'B', bottom);
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
