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
import techreborn.blocks.BlockStorage;
import techreborn.blocks.BlockStorage2;
import techreborn.config.ConfigTechReborn;
import techreborn.init.IC2Duplicates;
import techreborn.init.ModBlocks;
import techreborn.init.ModItems;
import techreborn.items.DynamicCell;
import techreborn.items.ItemDustsSmall;
import techreborn.items.ItemNuggets;
import techreborn.items.ItemUpgrades;

/**
 * Created by Prospector
 */
public class CraftingTableRecipes extends RecipeMethods {
	public static void init() {
		registerCompressionRecipes();
		registerMixedMetalIngotRecipes();
		registerShapeless(BlockStorage2.getStorageBlockByName("iridium_reinforced_stone", 1), getStack(Blocks.STONE), getMaterialObject("iridium", Type.INGOT));
		registerShapeless(BlockStorage2.getStorageBlockByName("iridium_reinforced_tungstensteel", 1), BlockStorage2.getStorageBlockByName("tungstensteel", 1), getMaterialObject("iridium", Type.INGOT));
		registerShapeless(BlockStorage2.getStorageBlockByName("iridium_reinforced_tungstensteel", 1), BlockStorage2.getStorageBlockByName("iridium_reinforced_stone", 1), getMaterialObject("tungstensteel", Type.INGOT));

		registerShapeless(getStack(ModBlocks.RUBBER_PLANKS, 4), getStack(ModBlocks.RUBBER_LOG));

		if (!IC2Duplicates.deduplicate()) {
			registerShapeless(getStack(ModItems.FREQUENCY_TRANSMITTER), getStack(IC2Duplicates.CABLE_ICOPPER), "circuitBasic");
		}

		registerShaped(DynamicCell.getEmptyCell(16), " T ", "T T", " T ", 'T', "ingotTin");
		registerShaped(getStack(ModBlocks.REFINED_IRON_FENCE), "RRR", "RRR", 'R', getStack(IC2Duplicates.REFINED_IRON));

		registerShaped(getStack(ModItems.STEEL_DRILL), " S ", "SCS", "SBS", 'S', "ingotSteel", 'C', "circuitBasic", 'B', "reBattery");
		registerShaped(getStack(ModItems.DIAMOND_DRILL), " D ", "DCD", "TST", 'D', "gemDiamond", 'C', "circuitAdvanced", 'S', getStack(ModItems.STEEL_DRILL, 1, OreDictionary.WILDCARD_VALUE), 'T', "ingotTitanium");
		registerShaped(getStack(ModItems.ADVANCED_DRILL), " I ", "NCN", "OAO", 'I', "ingotIridium", 'N', "nuggetIridium", 'A', getStack(ModItems.DIAMOND_DRILL, 1, OreDictionary.WILDCARD_VALUE), 'C', "circuitMaster", 'O', getMaterial("overclock", Type.UPGRADE));

		registerShaped(getStack(ModItems.STEEL_CHAINSAW), " SS", "SCS", "BS ", 'S', "ingotSteel", 'C', "circuitBasic", 'B', "reBattery");
		registerShaped(getStack(ModItems.DIAMOND_CHAINSAW), " DD", "TCD", "ST ", 'D', "gemDiamond", 'C', "circuitAdvanced", 'S', getStack(ModItems.STEEL_CHAINSAW, 1, OreDictionary.WILDCARD_VALUE), 'T', "ingotTitanium");
		registerShaped(getStack(ModItems.ADVANCED_CHAINSAW), " NI", "OCN", "DO ", 'I', "ingotIridium", 'N', "nuggetIridium", 'D', getStack(ModItems.DIAMOND_CHAINSAW, 1, OreDictionary.WILDCARD_VALUE), 'C', "circuitMaster", 'O', getMaterial("overclock", Type.UPGRADE));

		registerShaped(getStack(ModItems.STEEL_JACKHAMMER), "SBS", "SCS", " S ", 'S', "ingotSteel", 'C', "circuitBasic", 'B', "reBattery");
		registerShaped(getStack(ModItems.DIAMOND_JACKHAMMER), "DSD", "TCT", " D ", 'D', "gemDiamond", 'C', "circuitAdvanced", 'S', getStack(ModItems.STEEL_JACKHAMMER, 1, OreDictionary.WILDCARD_VALUE), 'T', "ingotTitanium");
		registerShaped(getStack(ModItems.ADVANCED_JACKHAMMER), "NDN", "OCO", " I ", 'I', "ingotIridium", 'N', "nuggetIridium", 'D', getStack(ModItems.DIAMOND_DRILL, 1, OreDictionary.WILDCARD_VALUE), 'C', "circuitMaster", 'O', getMaterial("overclock", Type.UPGRADE));

		registerShaped(ItemUpgrades.getUpgradeByName("injection"), "CHC", "PSP", "PPP", 'S', "chestWood", 'C', "circuitBasic", 'P', "plateIron", 'H', new ItemStack(Blocks.HOPPER));
		registerShaped(ItemUpgrades.getUpgradeByName("ejection"), "CSC", "PHP", "PPP", 'S', "chestWood", 'C', "circuitBasic", 'P', "plateIron", 'H', new ItemStack(Blocks.HOPPER));

		registerShaped(new ItemStack(ModBlocks.AUTO_CRAFTING_TABLE), "MPM", "PCP", "MPM", 'M', "circuitAdvanced", 'C', "workbench", 'P', "plateIron");

		if (ConfigTechReborn.enableGemArmorAndTools) {
			addToolAndArmourRecipes(getStack(ModItems.RUBY_SWORD), getStack(ModItems.RUBY_PICKAXE), getStack(ModItems.RUBY_AXE), getStack(ModItems.RUBY_HOE), getStack(ModItems.RUBY_SPADE), getStack(ModItems.RUBY_HELMET), getStack(ModItems.RUBY_CHESTPLATE), getStack(ModItems.RUBY_LEGGINGS), getStack(ModItems.RUBY_BOOTS), "gemRuby");
			addToolAndArmourRecipes(getStack(ModItems.SAPPHIRE_SWORD), getStack(ModItems.SAPPHIRE_PICKAXE), getStack(ModItems.SAPPHIRE_AXE), getStack(ModItems.SAPPHIRE_HOE), getStack(ModItems.SAPPHIRE_SPADE), getStack(ModItems.SAPPHIRE_HELMET), getStack(ModItems.SAPPHIRE_CHSTPLATE), getStack(ModItems.SAPPHIRE_LEGGINGS), getStack(ModItems.SAPPHIRE_BOOTS), "gemSapphire");
			addToolAndArmourRecipes(getStack(ModItems.PERIDOT_SWORD), getStack(ModItems.PERIDOT_PICKAXE), getStack(ModItems.PERIDOT_AXE), getStack(ModItems.PERIDOT_HOE), getStack(ModItems.PERIDOT_SAPPHIRE), getStack(ModItems.PERIDOT_HELMET), getStack(ModItems.PERIDOT_CHESTPLATE), getStack(ModItems.PERIDOT_LEGGINGS), getStack(ModItems.PERIDOT_BOOTS), "gemPeridot");
			addToolAndArmourRecipes(getStack(ModItems.BRONZE_SWORD), getStack(ModItems.BRONZE_PICKAXE), getStack(ModItems.BRONZE_AXE), getStack(ModItems.BRONZE_HOE), getStack(ModItems.BRONZE_SPADE), getStack(ModItems.BRONZE_HELMET), getStack(ModItems.BRONZE_CHESTPLATE), getStack(ModItems.BRONZE_LEGGINGS), getStack(ModItems.BRONZE_BOOTS), "ingotBronze");
		}

		if (!IC2Duplicates.deduplicate()) {
			registerShaped(getMaterial("copper", 6, Type.CABLE), "CCC", 'C', "ingotCopper");
			registerShaped(getMaterial("tin", 9, Type.CABLE), "TTT", 'T', "ingotTin");
			registerShaped(getMaterial("gold", 12, Type.CABLE), "GGG", 'G', "ingotGold");
			registerShaped(getMaterial("hv", 12, Type.CABLE), "RRR", 'R', "ingotRefinedIron");

			registerShaped(getMaterial("insulatedcopper", 6, Type.CABLE), "RRR", "CCC", "RRR", 'R', "itemRubber", 'C', "ingotCopper");
			registerShaped(getMaterial("insulatedcopper", 6, Type.CABLE), "RCR", "RCR", "RCR", 'R', "itemRubber", 'C', "ingotCopper");
			registerShapeless(getMaterial("insulatedcopper", Type.CABLE), "itemRubber", getMaterialObject("copper", Type.CABLE));

			registerShaped(getMaterial("insulatedgold", 4, Type.CABLE), "RRR", "RGR", "RRR", 'R', "itemRubber", 'G', "ingotGold");
			registerShapeless(getMaterial("insulatedgold", Type.CABLE), "itemRubber", "itemRubber", getMaterialObject("gold", Type.CABLE));

			registerShaped(getMaterial("insulatedhv", 4, Type.CABLE), "RRR", "RIR", "RRR", 'R', "itemRubber", 'I', "ingotRefinedIron");
			registerShapeless(getMaterial("insulatedhv", Type.CABLE), "itemRubber", "itemRubber", getMaterialObject("hv", Type.CABLE));

			registerShaped(getMaterial("glassfiber", 4, Type.CABLE), "GGG", "RDR", "GGG", 'R', "dustRedstone", 'D', "gemDiamond", 'G', "blockGlass");
			registerShaped(getMaterial("glassfiber", 4, Type.CABLE), "GGG", "RDR", "GGG", 'R', "dustRedstone", 'D', "dustDiamond", 'G', "blockGlass");

			registerShaped(getMaterial("glassfiber", 3, Type.CABLE), "GGG", "RDR", "GGG", 'R', "dustRedstone", 'D', "gemRuby", 'G', "blockGlass");
			registerShaped(getMaterial("glassfiber", 3, Type.CABLE), "GGG", "RDR", "GGG", 'R', "dustRedstone", 'D', "dustRuby", 'G', "blockGlass");

			registerShaped(getMaterial("glassfiber", 6, Type.CABLE), "GGG", "RDR", "GGG", 'R', "ingotSilver", 'D', "gemDiamond", 'G', "blockGlass");
			registerShaped(getMaterial("glassfiber", 6, Type.CABLE), "GGG", "RDR", "GGG", 'R', "ingotSilver", 'D', "dustDiamond", 'G', "blockGlass");

			registerShaped(getMaterial("glassfiber", 8, Type.CABLE), "GGG", "RDR", "GGG", 'R', "ingotElectrum", 'D', "gemDiamond", 'G', "blockGlass");
			registerShaped(getMaterial("glassfiber", 8, Type.CABLE), "GGG", "RDR", "GGG", 'R', "ingotElectrum", 'D', "dustDiamond", 'G', "blockGlass");
		}

		if (!IC2Duplicates.deduplicate()) {
			registerShapeless(getMaterial("carbon_fiber", Type.PART), "dustCoal", "dustCoal", "dustCoal", "dustCoal");
			registerShapeless(getMaterial("carbon_fiber", Type.PART), getMaterialObject("carbon", Type.CELL), getMaterialObject("carbon", Type.CELL), getMaterialObject("carbon", Type.CELL), getMaterialObject("carbon", Type.CELL), getMaterialObject("carbon", Type.CELL), getMaterialObject("carbon", Type.CELL), getMaterialObject("carbon", Type.CELL), getMaterialObject("carbon", Type.CELL), getMaterialObject("carbon", Type.CELL));
			registerShapeless(getMaterial("carbon_mesh", Type.PART), getMaterialObject("carbon_fiber", Type.PART), getMaterialObject("carbon_fiber", Type.PART));
		}

		registerShaped(getMaterial("computer_monitor", Type.PART), "ADA", "DGD", "ADA", 'D', "dye", 'A', "ingotAluminum", 'G', "paneGlass");
		registerShaped(getStack(ModBlocks.REINFORCED_GLASS, 7), "GAG", "GGG", "GAG", 'A', "plateAdvancedAlloy", 'G', "blockGlass");
		registerShaped(getStack(ModBlocks.REINFORCED_GLASS, 7), "GGG", "AGA", "GGG", 'A', "plateAdvancedAlloy", 'G', "blockGlass");
		registerShaped(getStack(ModBlocks.WIND_MILL), "I I", " G ", "I I", 'I', "plateIron", 'G', getStack(IC2Duplicates.GENERATOR));
		registerShaped(getStack(ModBlocks.WATER_MILL), "SWS", "WGW", "SWS", 'S', "stickWood", 'W', "plankWood", 'G', getStack(IC2Duplicates.GENERATOR));

		if (!IC2Duplicates.deduplicate()) {
		}

		registerShaped(getMaterial("standard", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', getStack(IC2Duplicates.REFINED_IRON), 'C', "circuitBasic", 'A', getMaterialObject("machine", Type.MACHINE_FRAME));
		registerShaped(getMaterial("reinforced", 4, Type.MACHINE_CASING), "RRR", "CAC", "RRR", 'R', "ingotSteel", 'C', "circuitAdvanced", 'A', getMaterialObject("advanced_machine", Type.MACHINE_FRAME));

		registerShaped(getMaterial("dataOrb", Type.PART), "DDD", "DID", "DDD", 'D', "circuitData", 'I', "circuitElite");

		registerShaped(getMaterial("dataControlCircuit", 4, Type.PART), "CDC", "DID", "CDC", 'I', getMaterialObject("iridium", Type.PLATE), 'D', "circuitData", 'C', "circuitAdvanced");

		registerShaped(getStack(ModBlocks.THERMAL_GENERATOR), "III", "IRI", "CGC", 'I', "ingotInvar", 'R', ModBlocks.REINFORCED_GLASS, 'G', getStack(IC2Duplicates.GENERATOR), 'C', "circuitBasic");

		if (!IC2Duplicates.deduplicate()) {
			registerShaped(getStack(IC2Duplicates.HVT), " H ", " M ", " H ", 'M', getStack(IC2Duplicates.MVT), 'H', getStack(IC2Duplicates.CABLE_IHV));
			registerShaped(getStack(IC2Duplicates.MVT), " G ", " M ", " G ", 'M', getMaterialObject("machine", Type.MACHINE_FRAME), 'G', getStack(IC2Duplicates.CABLE_IGOLD));
			registerShaped(getStack(IC2Duplicates.LVT), "PWP", "CCC", "PPP", 'P', "plankWood", 'C', "ingotCopper", 'W', getStack(IC2Duplicates.CABLE_ICOPPER));
			registerShaped(getStack(IC2Duplicates.BAT_BOX), "WCW", "BBB", "WWW", 'W', "plankWood", 'B', getStack(ModItems.RE_BATTERY), 'C', getStack(IC2Duplicates.CABLE_ICOPPER));
			registerShaped(getStack(IC2Duplicates.MFE), "GEG", "EME", "GEG", 'M', getMaterialObject("machine", Type.MACHINE_FRAME), 'E', getStack(ModItems.ENERGY_CRYSTAL), 'G', getStack(IC2Duplicates.CABLE_IGOLD));
			registerShaped(getStack(IC2Duplicates.MFSU), "LAL", "LML", "LOL", 'A', "circuitAdvanced", 'L', getStack(ModItems.LAPOTRONIC_CRYSTAL), 'M', getStack(IC2Duplicates.MFE), 'O', getMaterialObject("advanced_machine", Type.MACHINE_FRAME));
			registerShaped(getStack(IC2Duplicates.COMPRESSOR), "S S", "SCS", "SMS", 'C', "circuitBasic", 'M', getMaterialObject("machine", Type.MACHINE_FRAME), 'S', Blocks.STONE);
			registerShaped(getStack(IC2Duplicates.ELECTRICAL_FURNACE), " C ", "RFR", "   ", 'C', "circuitBasic", 'F', getStack(IC2Duplicates.IRON_FURNACE), 'R', "dustRedstone");
			registerShaped(getStack(IC2Duplicates.RECYCLER), " E ", "DCD", "GDG", 'D', Blocks.DIRT, 'C', getStack(IC2Duplicates.COMPRESSOR), 'G', Items.GLOWSTONE_DUST, 'E', "circuitBasic");
			registerShaped(getStack(IC2Duplicates.IRON_FURNACE), "III", "I I", "III", 'I', "ingotIron");
			registerShaped(getStack(IC2Duplicates.IRON_FURNACE), " I ", "I I", "IFI", 'I', "ingotIron", 'F', Blocks.FURNACE);
			registerShaped(getMaterial("electronic_circuit", Type.PART), "WWW", "SRS", "WWW", 'R', getStack(IC2Duplicates.REFINED_IRON), 'S', Items.REDSTONE, 'W', getStack(IC2Duplicates.CABLE_ICOPPER));
			registerShaped(getMaterial("advanced_circuit", Type.PART), "RGR", "LCL", "RGR", 'R', "dustRedstone", 'G', "dustGlowstone", 'L', "gemLapis", 'C', "circuitBasic");
			registerShaped(getStack(IC2Duplicates.EXTRACTOR), "TMT", "TCT", "   ", 'T', getStack(ModItems.TREE_TAP, true), 'M', getMaterialObject("machine", Type.MACHINE_FRAME), 'C', "circuitBasic");
		}
		registerShaped(getStack(ModBlocks.INDUSTRIAL_ELECTROLYZER), "RER", "CEC", "RER", 'R', "plateIron", 'E', getStack(IC2Duplicates.EXTRACTOR), 'C', "circuitAdvanced");

		registerShaped(getStack(ModItems.WRENCH), "BNB", "NBN", " B ", 'B', "ingotBronze", 'N', "nuggetBronze");

		registerShaped(getStack(ModItems.RE_BATTERY), " W ", "TRT", "TRT", 'T', "ingotTin", 'R', "dustRedstone", 'W', getStack(IC2Duplicates.CABLE_ICOPPER));

		registerShaped(getStack(ModBlocks.INDUSTRIAL_CENTRIFUGE), "RCR", "AEA", "RCR", 'R', getStack(IC2Duplicates.REFINED_IRON), 'E', getStack(IC2Duplicates.EXTRACTOR), 'A', "machineBlockAdvanced", 'C', "circuitAdvanced");
		registerShaped(getStack(ModBlocks.INDUSTRIAL_CENTRIFUGE), "RCR", "AEA", "RCR", 'R', "ingotAluminum", 'E', getStack(IC2Duplicates.EXTRACTOR), 'A', "machineBlockAdvanced", 'C', "circuitAdvanced");
		registerShaped(getStack(ModItems.ENERGY_CRYSTAL), "RRR", "RDR", "RRR", 'R', "dustRedstone", 'D', "gemDiamond");
		registerShaped(getStack(ModItems.LAPOTRONIC_CRYSTAL), "LCL", "LEL", "LCL", 'L', "dyeBlue", 'E', "energyCrystal", 'C', "circuitBasic");
		registerShapeless(getStack(IC2Duplicates.GENERATOR), getStack(ModItems.RE_BATTERY), getMaterialObject("machine", Type.MACHINE_FRAME), Blocks.FURNACE);

		registerShaped(getMaterial("machine", Type.MACHINE_FRAME), "AAA", "A A", "AAA", 'A', getStack(IC2Duplicates.REFINED_IRON));
		registerShaped(getMaterial("advanced_machine", Type.MACHINE_FRAME), " C ", "AMA", " C ", 'A', "plateAdvancedAlloy", 'C', "plateCarbon", 'M', getMaterialObject("machine", Type.MACHINE_FRAME));

		registerShaped(getMaterial("data_storage_circuit", Type.PART), "EEE", "ECE", "EEE", 'E', "gemEmerald", 'C', "circuitBasic");

		registerShaped(getMaterial("diamond_saw_blade", 4, Type.PART), "DSD", "S S", "DSD", 'D', "dustDiamond", 'S', "ingotSteel");
		registerShaped(getMaterial("diamond_grinding_head", 2, Type.PART), "DSD", "SGS", "DSD", 'S', "ingotSteel", 'D', "dustDiamond", 'G', "gemDiamond");
		registerShaped(getMaterial("tungsten_grinding_head", 2, Type.PART), "TST", "SBS", "TST", 'S', "ingotSteel", 'T', "ingotTungsten", 'B', "blockSteel");

		registerShaped(getStack(Blocks.LOG, 8), " U ", "   ", "   ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.STONE, 16), "   ", " U ", "   ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.SNOW, 16), "U U", "   ", "   ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.GRASS, 16), "   ", "U  ", "U  ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.OBSIDIAN, 12), "U U", "U U", "   ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.GLASS, 32), " U ", "U U", " U ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.DYE, 32, 3), "UU ", "  U", "UU ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.GLOWSTONE, 8), " U ", "U U", "UUU", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.CACTUS, 48), " U ", "UUU", "U U", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.REEDS, 48), "U U", "U U", "U U", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.VINE, 24), "U  ", "U  ", "U  ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.SNOWBALL, 16), "   ", "   ", "UUU", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.CLAY_BALL, 48), "UU ", "U  ", "UU ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.WATERLILY, 64), "U U", " U ", " U ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.GUNPOWDER, 15), "UUU", "U  ", "UUU", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.BONE, 32), "U  ", "UU ", "U  ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.FEATHER, 32), " U ", " U ", "U U", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.DYE, 48), " UU", " UU", " U ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.ENDER_PEARL, 1), "UUU", "U U", " U ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.COAL, 5), "  U", "U  ", "  U", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.IRON_ORE, 2), "U U", " U ", "U U", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.GOLD_ORE, 2), " U ", "UUU", " U ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.REDSTONE, 24), "   ", " U ", "UUU", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.DYE, 9, 4), " U ", " U ", " UU", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Blocks.EMERALD_ORE, 1), "UU ", "U U", " UU", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.EMERALD, 2), "UUU", "UUU", " U ", 'U', ModItems.UU_MATTER);
		registerShaped(getStack(Items.DIAMOND, 1), "UUU", "UUU", "UUU", 'U', ModItems.UU_MATTER);
		registerShaped(getMaterial("tin", 10, Type.DUST), "   ", "U U", "  U", 'U', ModItems.UU_MATTER);
		registerShaped(getMaterial("copper", 10, Type.DUST), "  U", "U U", "   ", 'U', ModItems.UU_MATTER);
		registerShaped(getMaterial("lead", 14, Type.DUST), "UUU", "UUU", "U  ", 'U', ModItems.UU_MATTER);
		registerShaped(getMaterial("platinum", Type.DUST), "  U", "UUU", "UUU", 'U', ModItems.UU_MATTER);
		registerShaped(getMaterial("tungsten", Type.DUST), "U  ", "UUU", "UUU", 'U', ModItems.UU_MATTER);
		registerShaped(getMaterial("titanium", 2, Type.DUST), "UUU", " U ", " U ", 'U', ModItems.UU_MATTER);
		registerShaped(getMaterial("aluminum", 16, Type.DUST), " U ", " U ", "UUU", 'U', ModItems.UU_MATTER);
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
