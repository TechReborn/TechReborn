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

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.api.power.IEnergyInterfaceItem;
import reborncore.api.power.IEnergyItemInfo;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.util.BucketHandler;
import techreborn.Core;
import techreborn.api.Reference;
import techreborn.blocks.BlockMachineFrame;
import techreborn.config.ConfigTechReborn;
import techreborn.items.*;
import techreborn.items.armor.ItemLapotronPack;
import techreborn.items.armor.ItemLithiumBatpack;
import techreborn.items.armor.ItemTRArmour;
import techreborn.items.battery.*;
import techreborn.items.tools.*;

public class ModItems {

	public static final String META_PLACEHOLDER = "PLACEHOLDER_ITEM";
	public static Item GEMS;
	public static Item INGOTS;
	public static Item NUGGETS;
	public static Item DUSTS;
	public static Item SMALL_DUSTS;
	public static Item PARTS;
	public static Item ROCK_CUTTER;
	public static Item LITHIUM_BATTERY_PACK;
	public static Item LAPOTRONIC_ORB_PACK;
	public static Item LITHIUM_BATTERY;
	public static Item OMNI_TOOL;
	public static Item LAPOTRONIC_ORB;
	public static Item MANUAL;
	public static Item UU_MATTER;
	public static Item PLATES;
	public static Item CLOAKING_DEVICE;
	public static Item RE_BATTERY;
	public static Item TREE_TAP;
	public static Item ELECTRIC_TREE_TAP;
	public static Item STEEL_DRILL;
	public static Item DIAMOND_DRILL;
	public static Item ADVANCED_DRILL;
	public static Item STEEL_CHAINSAW;
	public static Item DIAMOND_CHAINSAW;
	public static Item ADVANCED_CHAINSAW;
	public static Item STEEL_JACKHAMMER;
	public static Item DIAMOND_JACKHAMMER;
	public static Item ADVANCED_JACKHAMMER;
	public static Item NANOSABER;
	public static Item WRENCH;
	public static Item LAPOTRONIC_CRYSTAL;
	public static Item ENERGY_CRYSTAL;
	public static Item SCRAP_BOX;
	public static Item FREQUENCY_TRANSMITTER;
	public static Item BRONZE_SWORD;
	public static Item BRONZE_PICKAXE;
	public static Item BRONZE_SPADE;
	public static Item BRONZE_AXE;
	public static Item BRONZE_HOE;
	public static Item BRONZE_HELMET;
	public static Item BRONZE_CHESTPLATE;
	public static Item BRONZE_LEGGINGS;
	public static Item BRONZE_BOOTS;
	public static Item RUBY_SWORD;
	public static Item RUBY_PICKAXE;
	public static Item RUBY_SPADE;
	public static Item RUBY_AXE;
	public static Item RUBY_HOE;
	public static Item RUBY_HELMET;
	public static Item RUBY_CHESTPLATE;
	public static Item RUBY_LEGGINGS;
	public static Item RUBY_BOOTS;
	public static Item SAPPHIRE_SWORD;
	public static Item SAPPHIRE_PICKAXE;
	public static Item SAPPHIRE_SPADE;
	public static Item SAPPHIRE_AXE;
	public static Item SAPPHIRE_HOE;
	public static Item SAPPHIRE_HELMET;
	public static Item SAPPHIRE_CHSTPLATE;
	public static Item SAPPHIRE_LEGGINGS;
	public static Item SAPPHIRE_BOOTS;
	public static Item PERIDOT_SWORD;
	public static Item PERIDOT_PICKAXE;
	public static Item PERIDOT_SAPPHIRE;
	public static Item PERIDOT_AXE;
	public static Item PERIDOT_HOE;
	public static Item PERIDOT_HELMET;
	public static Item PERIDOT_CHESTPLATE;
	public static Item PERIDOT_LEGGINGS;
	public static Item PERIDOT_BOOTS;
	public static Item UPGRADES;
	public static Item MISSING_RECIPE_PLACEHOLDER;
	public static Item DEBUG;
	public static DynamicCell CELL;

	public static void init() throws InstantiationException, IllegalAccessException {
		GEMS = new ItemGems();
		registerItem(GEMS, "gem");
		INGOTS = new ItemIngots();
		registerItem(INGOTS, "ingot");
		DUSTS = new ItemDusts();
		registerItem(DUSTS, "dust");
		SMALL_DUSTS = new ItemDustsSmall();
		registerItem(SMALL_DUSTS, "smallDust");
		PLATES = new ItemPlates();
		registerItem(PLATES, "plates");
		NUGGETS = new ItemNuggets();
		registerItem(NUGGETS, "nuggets");
		// purifiedCrushedOre = new ItemPurifiedCrushedOre();
		// registerItem(purifiedCrushedOre, "purifiedCrushedOre");
		PARTS = new ItemParts();
		registerItem(PARTS, "part");

		ROCK_CUTTER = new ItemRockCutter();
		registerItem(ROCK_CUTTER, "rockCutter");
		LITHIUM_BATTERY_PACK = new ItemLithiumBatpack();
		registerItem(LITHIUM_BATTERY_PACK, "lithiumBatpack");
		LAPOTRONIC_ORB_PACK = new ItemLapotronPack();
		registerItem(LAPOTRONIC_ORB_PACK, "lapotronPack");
		LITHIUM_BATTERY = new ItemLithiumBattery();
		registerItem(LITHIUM_BATTERY, "lithiumBattery");
		LAPOTRONIC_ORB = new ItemLapotronicOrb();
		registerItem(LAPOTRONIC_ORB, "lapotronicOrb");
		OMNI_TOOL = new ItemOmniTool();
		registerItem(OMNI_TOOL, "omniTool");
		ENERGY_CRYSTAL = new ItemEnergyCrystal();
		registerItem(ENERGY_CRYSTAL, "energycrystal");
		LAPOTRONIC_CRYSTAL = new ItemLapotronCrystal();
		registerItem(LAPOTRONIC_CRYSTAL, "lapotroncrystal");

		MANUAL = new ItemTechManual();
		registerItem(MANUAL, "techmanuel");
		UU_MATTER = new ItemUUmatter();
		registerItem(UU_MATTER, "uumatter");
		RE_BATTERY = new ItemReBattery();
		registerItem(RE_BATTERY, "rebattery");
		TREE_TAP = new ItemTreeTap();
		registerItem(TREE_TAP, "treetap");

		ELECTRIC_TREE_TAP = new ItemElectricTreetap();
		registerItem(ELECTRIC_TREE_TAP, "electricTreetap");

		STEEL_DRILL = new ItemSteelDrill();
		registerItem(STEEL_DRILL, "irondrill");
		DIAMOND_DRILL = new ItemDiamondDrill();
		registerItem(DIAMOND_DRILL, "diamonddrill");
		ADVANCED_DRILL = new ItemAdvancedDrill();
		registerItem(ADVANCED_DRILL, "advanceddrill");

		STEEL_CHAINSAW = new ItemSteelChainsaw();
		registerItem(STEEL_CHAINSAW, "ironchainsaw");
		DIAMOND_CHAINSAW = new ItemDiamondChainsaw();
		registerItem(DIAMOND_CHAINSAW, "diamondchainsaw");
		ADVANCED_CHAINSAW = new ItemAdvancedChainsaw();
		registerItem(ADVANCED_CHAINSAW, "advancedchainsaw");

		STEEL_JACKHAMMER = new ItemSteelJackhammer();
		registerItem(STEEL_JACKHAMMER, "steeljackhammer");
		DIAMOND_JACKHAMMER = new ItemDiamondJackhammer();
		registerItem(DIAMOND_JACKHAMMER, "diamondjackhammer");
		ADVANCED_JACKHAMMER = new ItemAdvancedJackhammer();
		registerItem(ADVANCED_JACKHAMMER, "ironjackhammer");

		if (ConfigTechReborn.enableGemArmorAndTools) {
			BRONZE_SWORD = new ItemTRSword(Reference.BRONZE);
			registerItem(BRONZE_SWORD, "bronzeSword");
			BRONZE_PICKAXE = new ItemTRPickaxe(Reference.BRONZE);
			registerItem(BRONZE_PICKAXE, "bronzePickaxe");
			BRONZE_SPADE = new ItemTRSpade(Reference.BRONZE);
			registerItem(BRONZE_SPADE, "bronzeSpade");
			BRONZE_AXE = new ItemTRAxe(Reference.BRONZE);
			registerItem(BRONZE_AXE, "bronzeAxe");
			BRONZE_HOE = new ItemTRHoe(Reference.BRONZE);
			registerItem(BRONZE_HOE, "bronzeHoe");

			BRONZE_HELMET = new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.HEAD);
			registerItem(BRONZE_HELMET, "bronzeHelmet");
			BRONZE_CHESTPLATE = new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.CHEST);
			registerItem(BRONZE_CHESTPLATE, "bronzeChestplate");
			BRONZE_LEGGINGS = new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.LEGS);
			registerItem(BRONZE_LEGGINGS, "bronzeLeggings");
			BRONZE_BOOTS = new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.FEET);
			registerItem(BRONZE_BOOTS, "bronzeBoots");

			RUBY_SWORD = new ItemTRSword(Reference.RUBY);
			registerItem(RUBY_SWORD, "rubySword");
			RUBY_PICKAXE = new ItemTRPickaxe(Reference.RUBY);
			registerItem(RUBY_PICKAXE, "rubyPickaxe");
			RUBY_SPADE = new ItemTRSpade(Reference.RUBY);
			registerItem(RUBY_SPADE, "rubySpade");
			RUBY_AXE = new ItemTRAxe(Reference.RUBY);
			registerItem(RUBY_AXE, "rubyAxe");
			RUBY_HOE = new ItemTRHoe(Reference.RUBY);
			registerItem(RUBY_HOE, "rubyHoe");

			RUBY_HELMET = new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.HEAD);
			registerItem(RUBY_HELMET, "rubyHelmet");
			RUBY_CHESTPLATE = new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.CHEST);
			registerItem(RUBY_CHESTPLATE, "rubyChestplate");
			RUBY_LEGGINGS = new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.LEGS);
			registerItem(RUBY_LEGGINGS, "rubyLeggings");
			RUBY_BOOTS = new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.FEET);
			registerItem(RUBY_BOOTS, "rubyBoots");

			SAPPHIRE_SWORD = new ItemTRSword(Reference.SAPPHIRE);
			registerItem(SAPPHIRE_SWORD, "sapphireSword");
			SAPPHIRE_PICKAXE = new ItemTRPickaxe(Reference.SAPPHIRE);
			registerItem(SAPPHIRE_PICKAXE, "sapphirePickaxe");
			SAPPHIRE_SPADE = new ItemTRSpade(Reference.SAPPHIRE);
			registerItem(SAPPHIRE_SPADE, "sapphireSpade");
			SAPPHIRE_AXE = new ItemTRAxe(Reference.SAPPHIRE);
			registerItem(SAPPHIRE_AXE, "sapphireAxe");
			SAPPHIRE_HOE = new ItemTRHoe(Reference.SAPPHIRE);
			registerItem(SAPPHIRE_HOE, "sapphireHoe");

			SAPPHIRE_HELMET = new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.HEAD);
			registerItem(SAPPHIRE_HELMET, "sapphireHelmet");
			SAPPHIRE_CHSTPLATE = new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.CHEST);
			registerItem(SAPPHIRE_CHSTPLATE, "sapphireChestplate");
			SAPPHIRE_LEGGINGS = new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.LEGS);
			registerItem(SAPPHIRE_LEGGINGS, "sapphireLeggings");
			SAPPHIRE_BOOTS = new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.FEET);
			registerItem(SAPPHIRE_BOOTS, "sapphireBoots");

			PERIDOT_SWORD = new ItemTRSword(Reference.PERIDOT);
			registerItem(PERIDOT_SWORD, "peridotSword");
			PERIDOT_PICKAXE = new ItemTRPickaxe(Reference.PERIDOT);
			registerItem(PERIDOT_PICKAXE, "peridotPickaxe");
			PERIDOT_SAPPHIRE = new ItemTRSpade(Reference.PERIDOT);
			registerItem(PERIDOT_SAPPHIRE, "peridotSpade");
			PERIDOT_AXE = new ItemTRAxe(Reference.PERIDOT);
			registerItem(PERIDOT_AXE, "peridotAxe");
			PERIDOT_HOE = new ItemTRHoe(Reference.PERIDOT);
			registerItem(PERIDOT_HOE, "peridotHoe");

			PERIDOT_HELMET = new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.HEAD);
			registerItem(PERIDOT_HELMET, "peridotHelmet");
			PERIDOT_CHESTPLATE = new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.CHEST);
			registerItem(PERIDOT_CHESTPLATE, "peridotChestplate");
			PERIDOT_LEGGINGS = new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.LEGS);
			registerItem(PERIDOT_LEGGINGS, "peridotLeggings");
			PERIDOT_BOOTS = new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.FEET);
			registerItem(PERIDOT_BOOTS, "peridotBoots");
		}

		WRENCH = new ItemWrench();
		registerItem(WRENCH, "wrench");

		NANOSABER = new ItemNanosaber();
		registerItem(NANOSABER, "nanosaber");

		SCRAP_BOX = new ItemScrapBox();
		registerItem(SCRAP_BOX, "scrapbox");

		FREQUENCY_TRANSMITTER = new ItemFrequencyTransmitter();
		registerItem(FREQUENCY_TRANSMITTER, "frequencyTransmitter");

		UPGRADES = new ItemUpgrades();
		registerItem(UPGRADES, "upgrades");

		CLOAKING_DEVICE = new ItemCloakingDevice();
		registerItem(CLOAKING_DEVICE, "cloakingdevice");

		MISSING_RECIPE_PLACEHOLDER = new ItemMissingRecipe().setUnlocalizedName("missingRecipe");
		registerItem(MISSING_RECIPE_PLACEHOLDER, "mssingRecipe");

		DEBUG = new ItemDebugTool();
		registerItem(DEBUG, "debug");

		CELL = new DynamicCell();
		registerItem(CELL, "dynamicCell");

		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);

		Core.logHelper.info("TechReborns Items Loaded");

		BlockMachineBase.advancedMachineStack = BlockMachineFrame.getFrameByName("advancedMachine", 1);
		BlockMachineBase.machineStack = BlockMachineFrame.getFrameByName("machine", 1);

		OreDictionary.registerOre("itemRubber", ItemParts.getPartByName("rubber"));
	}

	public static void registerItem(Item item, String name) {
		item.setRegistryName(name);
		GameRegistry.register(item);
		if(item.getClass().isInstance(IEnergyItemInfo.class)){
			if(!item.getClass().isInstance(IEnergyInterfaceItem.class)){
				Core.logHelper.error(name + " was not patched with the power mixin. This is a error, the item may not work as intended.");
				Core.logHelper.error("Please check that the reborn core loading plugin has been registerd.");
			}
		}
	}

}
