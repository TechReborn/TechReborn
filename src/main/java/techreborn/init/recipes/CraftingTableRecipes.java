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

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.common.util.RebornCraftingHelper;
import techreborn.TechReborn;
import techreborn.init.TRContent;
import techreborn.items.ItemCells;
import techreborn.utils.RecipeDump;

/**
 * Created by Prospector
 */
public class CraftingTableRecipes extends RecipeMethods {
	public static void init() {

		registerCompressionRecipes();
		registerMixedMetalIngotRecipes();
			

		// Tools and devices		
		//registerShaped(getStack(TRContent.ELECTRIC_TREE_TAP), "TB", "  ", 'T', getStack(TRContent.TREE_TAP), 'B', "reBattery");
		//registerShaped(getStack(TRContent.NANOSABER), "DC ", "DC ", "GLG", 'L', "lapotronCrystal", 'C', "plateCarbon", 'D', "plateDiamond", 'G', "dustsmallGlowstone");
		//ItemStack rockCutter = getStack(TRContent.ROCK_CUTTER);
		//rockCutter.addEnchantment(Enchantments.SILK_TOUCH, 1);
		//registerShaped(rockCutter, "DT ", "DT ", "DCB", 'D', "dustDiamond", 'T', "ingotTitanium", 'C', "circuitBasic", 'B', "reBattery");
//		registerShaped(getStack(TRContent.BASIC_DRILL), " S ", "SCS", "SBS", 'S', "ingotSteel", 'C', "circuitBasic", 'B', "reBattery");
//		registerShaped(getStack(TRContent.ADVANCED_DRILL), " D ", "DCD", "TST", 'D', "gemDiamond", 'C', "circuitAdvanced", 'S', getStack(TRContent.BASIC_DRILL, 1, OreDictionary.WILDCARD_VALUE), 'T', "ingotTitanium");
//		registerShaped(getStack(TRContent.INDUSTRIAL_DRILL), " I ", "NCN", "OAO", 'I', "plateIridiumAlloy", 'N', "nuggetIridium", 'A', getStack(TRContent.ADVANCED_DRILL, 1, OreDictionary.WILDCARD_VALUE), 'C', "circuitMaster", 'O', getMaterial("overclock", Type.UPGRADE));
//		registerShaped(getStack(TRContent.BASIC_CHAINSAW), " SS", "SCS", "BS ", 'S', "ingotSteel", 'C', "circuitBasic", 'B', "reBattery");
//		registerShaped(getStack(TRContent.ADVANCED_CHAINSAW), " DD", "TCD", "ST ", 'D', "gemDiamond", 'C', "circuitAdvanced", 'S', getStack(TRContent.BASIC_CHAINSAW, 1, OreDictionary.WILDCARD_VALUE), 'T', "ingotTitanium");
//		registerShaped(getStack(TRContent.INDUSTRIAL_CHAINSAW), " NI", "OCN", "DO ", 'I', "plateIridiumAlloy", 'N', "nuggetIridium", 'D', getStack(TRContent.ADVANCED_CHAINSAW, 1, OreDictionary.WILDCARD_VALUE), 'C', "circuitMaster", 'O', getMaterial("overclock", Type.UPGRADE));
//		registerShaped(getStack(TRContent.BASIC_JACKHAMMER), "SBS", "SCS", " S ", 'S', "ingotSteel", 'C', "circuitBasic", 'B', "reBattery");
//		registerShaped(getStack(TRContent.ADVANCED_JACKHAMMER), "DSD", "TCT", " D ", 'D', "gemDiamond", 'C', "circuitAdvanced", 'S', getStack(TRContent.BASIC_JACKHAMMER, 1, OreDictionary.WILDCARD_VALUE), 'T', "ingotTitanium");
//		registerShaped(getStack(TRContent.INDUSTRIAL_JACKHAMMER), "NDN", "OCO", " I ", 'I', "plateIridiumAlloy", 'N', "nuggetIridium", 'D', getStack(TRContent.ADVANCED_JACKHAMMER, 1, OreDictionary.WILDCARD_VALUE), 'C', "circuitMaster", 'O', getMaterial("overclock", Type.UPGRADE));
//		registerShaped(getStack(TRContent.CLOAKING_DEVICE), "CIC", "IOI", "CIC", 'C', "ingotChrome", 'I', "plateIridiumAlloy", 'O', getStack(TRContent.LAPOTRONIC_ORB));
//		registerShaped(getStack(TRContent.LAPOTRONIC_ORBPACK), "FOF", "SPS", "FIF", 'F', "circuitMaster", 'O', getStack(TRContent.LAPOTRONIC_ORB), 'S', "craftingSuperconductor", 'I', "ingotIridium", 'P', getStack(TRContent.LITHIUM_ION_BATPACK));
//		registerShaped(getStack(TRContent.RED_CELL_BATTERY), " W ", "TRT", "TRT", 'T', "ingotTin", 'R', "dustRedstone", 'W', EnumCableType.ICOPPER.getStack());
//		registerShaped(getStack(TRContent.LITHIUM_ION_BATTERY), " C ", "PFP", "PFP", 'F', ItemCells.getCellByName("lithium"), 'P', "plateAluminum", 'C', EnumCableType.IGOLD.getStack());
//		registerShaped(getStack(TRContent.LITHIUM_ION_BATPACK),	"BCB", "BPB", "B B", 'B', getStack(TRContent.LITHIUM_ION_BATTERY), 'P', "plateAluminum", 'C', "circuitAdvanced");
//		registerShaped(getStack(TRContent.ENERGY_CRYSTAL), "RRR", "RDR", "RRR", 'R', "dustRedstone", 'D', "gemDiamond");
//		registerShaped(getStack(TRContent.LAPOTRON_CRYSTAL), "LCL", "LEL", "LCL", 'L', "dyeBlue", 'E', "energyCrystal", 'C', "circuitBasic");
//		registerShaped(getStack(TRContent.LAPOTRONIC_ORB), "LLL", "LPL", "LLL", 'L', "lapotronCrystal", 'P', "plateIridiumAlloy");
		
		//registerShapeless(getStack(TRContent.FREQUENCY_TRANSMITTER), EnumCableType.ICOPPER.getStack(), "circuitBasic");

		//Upgrades
//		registerShaped(ItemUpgrades.getUpgradeByName("energy_storage"), "PPP", "WBW", "PCP", 'P', "plankWood", 'W', EnumCableType.ICOPPER.getStack(), 'C', "circuitBasic", 'B', "reBattery");
//		registerShaped(ItemUpgrades.getUpgradeByName("overclock"), "TTT", "WCW", 'T', TRIngredients.Parts.COOLANT_SIMPLE.getStack(), 'W', EnumCableType.ICOPPER.getStack(), 'C', "circuitBasic");
//		registerShaped(ItemUpgrades.getUpgradeByName("overclock", 2), " T ", "WCW", 'T', TRIngredients.Parts.HELIUM_COOLANT_TRIPLE.getStack(), 'W', EnumCableType.ICOPPER.getStack(), 'C', "circuitBasic");
//		registerShaped(ItemUpgrades.getUpgradeByName("overclock", 2),  " T ", "WCW", 'T', TRIngredients.Parts.NAK_COOLANT_SIMPLE.getStack(), 'W', EnumCableType.ICOPPER.getStack(), 'C', "circuitBasic");
//		registerShaped(ItemUpgrades.getUpgradeByName("transformer"), "GGG", "WTW", "GCG", 'G', "blockGlass", 'W', EnumCableType.IGOLD.getStack(), 'C', "circuitBasic", 'T', getStack(ModBlocks.MV_TRANSFORMER));

		//Machines
		
		
		
//		registerShaped(TRContent.Machine.CHARGE_O_MAT.getStack(),  "ETE", "COC", "EAE", 'E', "circuitMaster", 'T', "energyCrystal", 'C', "chest", 'O', getStack(TRContent.LAPOTRONIC_ORB), 'A', "machineBlockAdvanced");
//		registerShaped(TRContent.Machine.ADJUSTABLE_SU.getStack(), "LLL", "LCL", "LLL", 'L', getStack(TRContent.LAPOTRONIC_ORB), 'C', "energyCrystal");
//		registerShaped(TRContent.Machine.MATTER_FABRICATOR.getStack(), "ETE", "AOA", "ETE", 'E', "circuitMaster", 'T', TRContent.Machine.EXTRACTOR.getStack(), 'A', "machineBlockElite", 'O', getStack(TRContent.LAPOTRONIC_ORB));

//		registerShaped(getStack(TRContent.PLAYER_DETECTOR, true), " D ", "CFC", " D ", 'D', "circuitStorage", 'C', "circuitAdvanced", 'F',  getStack(TRContent.COMPUTER_CUBE));
//		registerShaped(getStack(TRContent.DRAGON_EGG_SYPHON), "CTC", "PSP", "CBC", 'C', "circuitMaster", 'T', getStack(TRContent.MEDIUM_VOLTAGE_SU), 'P', "plateIridiumAlloy", 'S', "craftingSuperconductor", 'B', getStack(TRContent.LAPOTRONIC_ORB));
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
//		registerShaped(getStack(ModBlocks.HV_TRANSFORMER), " H ", " M ", " H ", 'M', getStack(ModBlocks.MV_TRANSFORMER), 'H', EnumCableType.IHV.getStack());
//		registerShaped(getStack(ModBlocks.MV_TRANSFORMER), " G ", " M ", " G ", 'M', "machineBlockBasic", 'G', EnumCableType.IGOLD.getStack());
//		registerShaped(getStack(ModBlocks.LV_TRANSFORMER), "PWP", "CCC", "PPP", 'P', "plankWood", 'C', "ingotCopper", 'W', EnumCableType.ICOPPER.getStack());
//		registerShaped(getStack(ModBlocks.LOW_VOLTAGE_SU), "WCW", "BBB", "WWW", 'W', "plankWood", 'B', "reBattery", 'C', EnumCableType.ICOPPER.getStack());
//		registerShaped(getStack(ModBlocks.MEDIUM_VOLTAGE_SU), "GEG", "EME", "GEG", 'M', "machineBlockBasic", 'E', "energyCrystal", 'G', EnumCableType.IGOLD.getStack());



		// Machine Frames
		//registerShaped(TRContent.MachineBlocks.BASIC.getFrame(), "AAA", "A A", "AAA", 'A', "ingotRefinedIron");
		
//		registerShaped(getMaterial("machine", Type.MACHINE_FRAME), "AAA", "A A", "AAA", 'A', "ingotRefinedIron");
//		registerShaped(getMaterial("advanced_machine", Type.MACHINE_FRAME), " C ", "AMA", " C ", 'A', "plateAdvancedAlloy", 'C', "plateCarbon", 'M', "machineBlockBasic");
//		registerShaped(getMaterial("highly_advanced_machine", Type.MACHINE_FRAME), "CTC", "TBT", "CTC", 'C', "plateChrome", 'T', "plateTitanium", 'B', "machineBlockAdvanced");

		// Multiblock casings
//		registerShaped(getMaterial("standard", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "ingotRefinedIron", 'C', "circuitBasic", 'A', "machineBlockBasic");
//		registerShaped(getMaterial("standard", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "plateIron", 'C', "circuitBasic", 'A', "machineBlockBasic");
//		registerShaped(getMaterial("standard", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "plateAluminum", 'C', "circuitBasic", 'A', "machineBlockBasic");
//		registerShaped(getMaterial("reinforced", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "plateSteel", 'C', "circuitAdvanced", 'A', "machineBlockAdvanced");
//		registerShaped(getMaterial("reinforced", 1, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "plateSteel", 'C', "circuitAdvanced", 'A', getMaterial("standard", Type.MACHINE_CASING));
//		registerShaped(getMaterial("advanced", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R',  "plateChrome", 'C', "circuitElite", 'A', "machineBlockElite");
//		registerShaped(getMaterial("advanced", 1, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R',  "plateChrome", 'C', "circuitElite", 'A', getMaterial("reinforced", Type.MACHINE_CASING));
		
		// Parts
		//registerShaped(TRContent.Parts.DATA_STORAGE_CHIP.getStack(), "RGR", "LCL", "EEE", 'R', "dustRedstone", 'G', "dustGlowstone", 'L', "gemLapis", 'C', "circuitBasic", 'E', "plateEmerald");
//		registerShaped(getMaterial("data_storage_circuit", Type.PART), "RGR", "LCL", "EEE", 'R', "dustRedstone", 'G', "dustGlowstone", 'L', "gemLapis", 'C', "circuitBasic", 'E', "plateEmerald");
//		registerShaped(getMaterial("data_control_circuit", Type.PART), "ADA", "DID", "ADA", 'I', "ingotIridium", 'A', "circuitAdvanced", 'D', "circuitStorage");
//		registerShaped(getMaterial("energy_flow_circuit", 4, Type.PART), "ATA", "LIL", "ATA", 'T', "ingotTungsten", 'I', "plateIridiumAlloy", 'A', "circuitAdvanced", 'L', "lapotronCrystal");
//		registerShaped(getMaterial("data_orb", Type.PART),  "DDD", "DSD", "DDD", 'D', "circuitStorage", 'S', "circuitElite");
//		registerShaped(TRContent.Parts.INDUSTRIAL_CIRCUIT.getStack() );

		
//		registerShaped(getMaterial("computer_monitor", Type.PART), "ADA", "DGD", "ADA", 'D', "dye", 'A', "ingotAluminum", 'G', "paneGlass");


//		registerShaped(getMaterial("super_conductor", 4, Type.PART), "CCC", "TIT", "EEE", 'E', "circuitMaster", 'C', getMaterial("heliumCoolantSimple", Type.PART), 'T', "ingotTungsten", 'I', "plateIridiumAlloy");




		//UU-Matter
		ItemStack uuStack = TRContent.Parts.UU_MATTER.getStack();
//		registerShaped(getStack(Blocks.LOG, 8), 				" U ", "   ", "   ", 'U', uuStack);
//		registerShaped(getStack(Blocks.STONE, 16), 				"   ", " U ", "   ", 'U', uuStack);
//		registerShaped(getStack(Blocks.GRASS, 16), 				"   ", "U  ", "U  ", 'U', uuStack);
//		registerShaped(getStack(Items.DYE, 32, 3), 				"UU ", "  U", "UU ", 'U', uuStack);
//		registerShaped(getStack(Items.REEDS, 48), 				"U U", "U U", "U U", 'U', uuStack);
//		registerShaped(getStack(Items.DYE, 48), 				" UU", " UU", " U ", 'U', uuStack);
//		registerShaped(getStack(Items.COAL, 5), 				"  U", "U  ", "  U", 'U', uuStack);
//		registerShaped(getStack(Items.DYE, 9, 4), 				" U ", " U ", " UU", 'U', uuStack);


		
//		for (String part : ItemParts.types) {
//			if (part.endsWith("Gear")) {
//				registerShaped(getMaterial(part, Type.PART), " O ", "OIO", " O ", 'I', getStack(Items.IRON_INGOT), 'O', "ingot" + StringUtils.toFirstCapital(part.replace("Gear", "")));
//			}
//		}

//		registerShaped(new ItemStack(TRContent.RUBBER_LOG_SLAB_HALF),  "WWW", 'W', new ItemStack(TRContent.RUBBER_PLANKS));



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
		RecipeDump.addShapedRecipe(output, inputs);
		RebornCraftingHelper.addShapedOreRecipe(output, inputs);
	}

	static void registerShapeless(ItemStack output, Object... inputs) {
		RecipeDump.addShapelessRecipe(output, inputs);
		RebornCraftingHelper.addShapelessOreRecipe(output, inputs);
	}

}
