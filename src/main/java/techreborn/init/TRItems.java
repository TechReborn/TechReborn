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

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import reborncore.RebornRegistry;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.util.BucketHandler;
import techreborn.api.Reference;
import techreborn.config.ConfigTechReborn;
import techreborn.items.DynamicCell;
import techreborn.items.ItemFrequencyTransmitter;
import techreborn.items.ItemManual;
import techreborn.items.ItemScrapBox;
import techreborn.items.armor.ItemCloakingDevice;
import techreborn.items.armor.ItemLapotronicOrbpack;
import techreborn.items.armor.ItemLithiumIonBatpack;
import techreborn.items.armor.ItemTRArmour;
import techreborn.items.battery.*;
import techreborn.items.tool.ItemDebugTool;
import techreborn.items.tool.ItemTreeTap;
import techreborn.items.tool.ItemWrench;
import techreborn.items.tool.advanced.ItemAdvancedChainsaw;
import techreborn.items.tool.advanced.ItemAdvancedDrill;
import techreborn.items.tool.advanced.ItemAdvancedJackhammer;
import techreborn.items.tool.advanced.ItemRockCutter;
import techreborn.items.tool.basic.ItemBasicChainsaw;
import techreborn.items.tool.basic.ItemBasicDrill;
import techreborn.items.tool.basic.ItemBasicJackhammer;
import techreborn.items.tool.basic.ItemElectricTreetap;
import techreborn.items.tool.industrial.*;
import techreborn.items.tool.vanilla.ItemTRAxe;
import techreborn.items.tool.vanilla.ItemTRHoe;
import techreborn.items.tool.vanilla.ItemTRSpade;
import techreborn.items.tool.vanilla.ItemTRSword;
import techreborn.utils.InitUtils;

import javax.annotation.Nullable;

public class TRItems {

	// Armor
	public static Item CLOAKING_DEVICE;
	public static Item LAPOTRONIC_ORBPACK;
	public static Item LITHIUM_ION_BATPACK;

	// Battery
	public static Item ENERGY_CRYSTAL;
	public static Item LAPOTRON_CRYSTAL;
	public static Item LAPOTRONIC_ORB;
	public static Item LITHIUM_ION_BATTERY;
	public static Item RED_CELL_BATTERY;

	// Tools
	public static Item TREE_TAP;
	public static Item WRENCH;

	public static Item BASIC_CHAINSAW;
	public static Item BASIC_DRILL;
	public static Item BASIC_JACKHAMMER;
	public static Item ELECTRIC_TREE_TAP;

	public static Item ADVANCED_CHAINSAW;
	public static Item ADVANCED_DRILL;
	public static Item ADVANCED_JACKHAMMER;
	public static Item ROCK_CUTTER;

	public static Item INDUSTRIAL_CHAINSAW;
	public static Item INDUSTRIAL_DRILL;
	public static Item INDUSTRIAL_JACKHAMMER;
	public static Item NANOSABER;
	public static Item OMNI_TOOL;

	public static Item DEBUG_TOOL;


	// Other
	public static Item FREQUENCY_TRANSMITTER;
	public static Item SCRAP_BOX;
	public static Item MANUAL;
	public static DynamicCell CELL;

	// Gem armor & tools
	@Nullable
	public static Item BRONZE_SWORD;
	@Nullable
	public static Item BRONZE_PICKAXE;
	@Nullable
	public static Item BRONZE_SPADE;
	@Nullable
	public static Item BRONZE_AXE;
	@Nullable
	public static Item BRONZE_HOE;
	@Nullable
	public static Item BRONZE_HELMET;
	@Nullable
	public static Item BRONZE_CHESTPLATE;
	@Nullable
	public static Item BRONZE_LEGGINGS;
	@Nullable
	public static Item BRONZE_BOOTS;
	@Nullable
	public static Item RUBY_SWORD;
	@Nullable
	public static Item RUBY_PICKAXE;
	@Nullable
	public static Item RUBY_SPADE;
	@Nullable
	public static Item RUBY_AXE;
	@Nullable
	public static Item RUBY_HOE;
	@Nullable
	public static Item RUBY_HELMET;
	@Nullable
	public static Item RUBY_CHESTPLATE;
	@Nullable
	public static Item RUBY_LEGGINGS;
	@Nullable
	public static Item RUBY_BOOTS;
	@Nullable
	public static Item SAPPHIRE_SWORD;
	@Nullable
	public static Item SAPPHIRE_PICKAXE;
	@Nullable
	public static Item SAPPHIRE_SPADE;
	@Nullable
	public static Item SAPPHIRE_AXE;
	@Nullable
	public static Item SAPPHIRE_HOE;
	@Nullable
	public static Item SAPPHIRE_HELMET;
	@Nullable
	public static Item SAPPHIRE_CHESTPLATE;
	@Nullable
	public static Item SAPPHIRE_LEGGINGS;
	@Nullable
	public static Item SAPPHIRE_BOOTS;
	@Nullable
	public static Item PERIDOT_SWORD;
	@Nullable
	public static Item PERIDOT_PICKAXE;
	@Nullable
	public static Item PERIDOT_SPADE;
	@Nullable
	public static Item PERIDOT_AXE;
	@Nullable
	public static Item PERIDOT_HOE;
	@Nullable
	public static Item PERIDOT_HELMET;
	@Nullable
	public static Item PERIDOT_CHESTPLATE;
	@Nullable
	public static Item PERIDOT_LEGGINGS;
	@Nullable
	public static Item PERIDOT_BOOTS;

	public static void init() {

		TRContent.registerItems();

		// Gem armor & tools
		if (ConfigTechReborn.enableGemArmorAndTools) {
			//Todo: repair with tags
			registerItem(BRONZE_SWORD = InitUtils.setup(new ItemTRSword(Reference.BRONZE, "ingotBronze"), "bronze_sword"));
			registerItem(BRONZE_PICKAXE = InitUtils.setup(new ItemTRSword(Reference.BRONZE, "ingotBronze"), "bronze_pickaxe"));
			registerItem(BRONZE_SPADE = InitUtils.setup(new ItemTRSpade(Reference.BRONZE, "ingotBronze"), "bronze_spade"));
			registerItem(BRONZE_AXE = InitUtils.setup(new ItemTRAxe(Reference.BRONZE, "ingotBronze"), "bronze_axe"));
			registerItem(BRONZE_HOE = InitUtils.setup(new ItemTRHoe(Reference.BRONZE, "ingotBronze"), "bronze_hoe"));

			registerItem(BRONZE_HELMET = InitUtils.setup(new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.HEAD, "ingotBronze"), "bronze_helmet"));
			registerItem(BRONZE_CHESTPLATE = InitUtils.setup(new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.CHEST, "ingotBronze"), "bronze_chestplate"));
			registerItem(BRONZE_LEGGINGS = InitUtils.setup(new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.LEGS, "ingotBronze"), "bronze_leggings"));
			registerItem(BRONZE_BOOTS = InitUtils.setup(new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.FEET, "ingotBronze"), "bronze_boots"));

			registerItem(RUBY_SWORD = InitUtils.setup(new ItemTRSword(Reference.RUBY, "gemRuby"), "ruby_sword"));
			registerItem(RUBY_PICKAXE = InitUtils.setup(new ItemTRSword(Reference.RUBY, "gemRuby"), "ruby_pickaxe"));
			registerItem(RUBY_SPADE = InitUtils.setup(new ItemTRSpade(Reference.RUBY, "gemRuby"), "ruby_spade"));
			registerItem(RUBY_AXE = InitUtils.setup(new ItemTRAxe(Reference.RUBY, "gemRuby"), "ruby_axe"));
			registerItem(RUBY_HOE = InitUtils.setup(new ItemTRHoe(Reference.RUBY, "gemRuby"), "ruby_hoe"));

			registerItem(RUBY_HELMET = InitUtils.setup(new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.HEAD, "gemRuby"), "ruby_helmet"));
			registerItem(RUBY_CHESTPLATE = InitUtils.setup(new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.CHEST, "gemRuby"), "ruby_chestplate"));
			registerItem(RUBY_LEGGINGS = InitUtils.setup(new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.LEGS, "gemRuby"), "ruby_leggings"));
			registerItem(RUBY_BOOTS = InitUtils.setup(new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.FEET, "gemRuby"), "ruby_boots"));

			registerItem(SAPPHIRE_SWORD = InitUtils.setup(new ItemTRSword(Reference.SAPPHIRE, "gemSapphire"), "sapphire_sword"));
			registerItem(SAPPHIRE_PICKAXE = InitUtils.setup(new ItemTRSword(Reference.SAPPHIRE, "gemSapphire"), "sapphire_pickaxe"));
			registerItem(SAPPHIRE_SPADE = InitUtils.setup(new ItemTRSpade(Reference.SAPPHIRE, "gemSapphire"), "sapphire_spade"));
			registerItem(SAPPHIRE_AXE = InitUtils.setup(new ItemTRAxe(Reference.SAPPHIRE, "gemSapphire"), "sapphire_axe"));
			registerItem(SAPPHIRE_HOE = InitUtils.setup(new ItemTRHoe(Reference.SAPPHIRE, "gemSapphire"), "sapphire_hoe"));

			registerItem(SAPPHIRE_HELMET = InitUtils.setup(new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.HEAD, "gemSapphire"), "sapphire_helmet"));
			registerItem(SAPPHIRE_CHESTPLATE = InitUtils.setup(new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.CHEST, "gemSapphire"), "sapphire_chestplate"));
			registerItem(SAPPHIRE_LEGGINGS = InitUtils.setup(new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.LEGS, "gemSapphire"), "sapphire_leggings"));
			registerItem(SAPPHIRE_BOOTS = InitUtils.setup(new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.FEET, "gemSapphire"), "sapphire_boots"));

			registerItem(PERIDOT_SWORD = InitUtils.setup(new ItemTRSword(Reference.PERIDOT, "gemPeridot"), "peridot_sword"));
			registerItem(PERIDOT_PICKAXE = InitUtils.setup(new ItemTRSword(Reference.PERIDOT, "gemPeridot"), "peridot_pickaxe"));
			registerItem(PERIDOT_SPADE = InitUtils.setup(new ItemTRSpade(Reference.PERIDOT, "gemPeridot"), "peridot_spade"));
			registerItem(PERIDOT_AXE = InitUtils.setup(new ItemTRAxe(Reference.PERIDOT, "gemPeridot"), "peridot_axe"));
			registerItem(PERIDOT_HOE = InitUtils.setup(new ItemTRHoe(Reference.PERIDOT, "gemPeridot"), "peridot_hoe"));

			registerItem(PERIDOT_HELMET = InitUtils.setup(new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.HEAD, "gemPeridot"), "peridot_helmet"));
			registerItem(PERIDOT_CHESTPLATE = InitUtils.setup(new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.CHEST, "gemPeridot"), "peridot_chestplate"));
			registerItem(PERIDOT_LEGGINGS = InitUtils.setup(new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.LEGS, "gemPeridot"), "peridot_leggings"));
			registerItem(PERIDOT_BOOTS = InitUtils.setup(new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.FEET, "gemPeridot"), "peridot_boots"));
		}

		// Battery
		registerItem(RED_CELL_BATTERY = InitUtils.setup(new ItemRedCellBattery(), "red_cell_battery"));
		registerItem(LITHIUM_ION_BATTERY = InitUtils.setup(new ItemLithiumIonBattery(), "lithium_ion_battery"));
		registerItem(LITHIUM_ION_BATPACK = InitUtils.setup(new ItemLithiumIonBatpack(), "lithium_ion_batpack"));
		registerItem(ENERGY_CRYSTAL = InitUtils.setup(new ItemEnergyCrystal(), "energy_crystal"));
		registerItem(LAPOTRON_CRYSTAL = InitUtils.setup(new ItemLapotronCrystal(), "lapotron_crystal"));
		registerItem(LAPOTRONIC_ORB = InitUtils.setup(new ItemLapotronicOrb(), "lapotronic_orb"));
		registerItem(LAPOTRONIC_ORBPACK = InitUtils.setup(new ItemLapotronicOrbpack(), "lapotronic_orbpack"));

		// Tools
		registerItem(TREE_TAP = InitUtils.setup(new ItemTreeTap(), "treetap"));
		registerItem(WRENCH = InitUtils.setup(new ItemWrench(), "wrench"));

		registerItem(BASIC_DRILL = InitUtils.setup(new ItemBasicDrill(), "basic_drill"));
		registerItem(BASIC_CHAINSAW = InitUtils.setup(new ItemBasicChainsaw(), "basic_chainsaw"));
		registerItem(BASIC_JACKHAMMER = InitUtils.setup(new ItemBasicJackhammer(), "basic_jackhammer"));
		registerItem(ELECTRIC_TREE_TAP = InitUtils.setup(new ItemElectricTreetap(), "electric_treetap"));

		registerItem(ADVANCED_DRILL = InitUtils.setup(new ItemAdvancedDrill(), "advanced_drill"));
		registerItem(ADVANCED_CHAINSAW = InitUtils.setup(new ItemAdvancedChainsaw(), "advanced_chainsaw"));
		registerItem(ADVANCED_JACKHAMMER = InitUtils.setup(new ItemAdvancedJackhammer(), "advanced_jackhammer"));
		registerItem(ROCK_CUTTER = InitUtils.setup(new ItemRockCutter(), "rock_cutter"));

		registerItem(INDUSTRIAL_DRILL = InitUtils.setup(new ItemIndustrialDrill(), "industrial_drill"));
		registerItem(INDUSTRIAL_CHAINSAW = InitUtils.setup(new ItemIndustrialChainsaw(), "industrial_chainsaw"));
		registerItem(INDUSTRIAL_JACKHAMMER = InitUtils.setup(new ItemIndustrialJackhammer(), "industrial_jackhammer"));
		registerItem(NANOSABER = InitUtils.setup(new ItemNanosaber(), "nanosaber"));
		registerItem(OMNI_TOOL = InitUtils.setup(new ItemOmniTool(), "omni_tool"));


		// Armor
		registerItem(CLOAKING_DEVICE = InitUtils.setup(new ItemCloakingDevice(), "cloaking_device"));

		// Other
		registerItem(FREQUENCY_TRANSMITTER = InitUtils.setup(new ItemFrequencyTransmitter(), "frequency_transmitter"));
		registerItem(SCRAP_BOX = InitUtils.setup(new ItemScrapBox(), "scrap_box"));
		registerItem(MANUAL = InitUtils.setup(new ItemManual(), "manual"));
		registerItem(DEBUG_TOOL = InitUtils.setup(new ItemDebugTool(), "debug_tool"));
		registerItem(CELL = InitUtils.setup(new DynamicCell(), "cell"));
		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);

		// TODO: do we need this at all?
		BlockMachineBase.advancedFrameStack = new ItemStack(TRContent.MachineBlocks.ADVANCED.getFrame());
		BlockMachineBase.basicFrameStack = new ItemStack(TRContent.MachineBlocks.BASIC.getFrame());
	}

	public static void registerItem(Item item) {
		RebornRegistry.registerItem(item);
	}
}
