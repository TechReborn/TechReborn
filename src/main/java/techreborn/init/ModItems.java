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
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.MinecraftForge;
import reborncore.RebornRegistry;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.util.BucketHandler;
import techreborn.Core;
import techreborn.api.Reference;
import techreborn.api.armor.ArmorSlot;
import techreborn.blocks.BlockMachineFrames;
import techreborn.config.ConfigTechReborn;
import techreborn.items.*;
import techreborn.items.armor.ItemCloakingDevice;
import techreborn.items.armor.ItemLapotronPack;
import techreborn.items.armor.ItemLithiumBatpack;
import techreborn.items.armor.ItemTRArmor;
import techreborn.items.armor.modular.ItemModularArmor;
import techreborn.items.battery.*;
import techreborn.items.ingredients.ItemDusts;
import techreborn.items.ingredients.ItemDustsSmall;
import techreborn.items.ingredients.ItemGems;
import techreborn.items.ingredients.ItemIngots;
import techreborn.items.ingredients.ItemNuggets;
import techreborn.items.ingredients.ItemParts;
import techreborn.items.ingredients.ItemPlates;
import techreborn.items.ingredients.ItemUUmatter;
import techreborn.items.tools.*;
import techreborn.lib.ModInfo;

import javax.annotation.Nullable;

public class ModItems {
	public static final String META_PLACEHOLDER = "PLACEHOLDER_ITEM";
	
	// Ingredients
	public static Item DUSTS;
	public static Item GEMS;
	public static Item INGOTS;
	public static Item NUGGETS;
	public static Item PARTS;
	public static Item PLATES;
	public static Item SMALL_DUSTS;
	public static Item UU_MATTER;
	
	// Armor
	public static Item CLOAKING_DEVICE;
	public static Item LAPOTRONIC_ORB_PACK;
	public static Item LITHIUM_BATTERY_PACK;
	
	// Battery
	public static Item ENERGY_CRYSTAL;
	public static Item LAPOTRONIC_CRYSTAL;
	public static Item LAPOTRONIC_ORB;
	public static Item LITHIUM_BATTERY;
	public static Item RE_BATTERY;
	
	// Tools
	public static Item ADVANCED_CHAINSAW;
	public static Item ADVANCED_DRILL;
	public static Item ADVANCED_JACKHAMMER;
	public static Item DEBUG;
	public static Item DIAMOND_CHAINSAW;
	public static Item DIAMOND_DRILL;
	public static Item DIAMOND_JACKHAMMER;
	public static Item ELECTRIC_TREE_TAP;
	public static Item NANOSABER;
	public static Item OMNI_TOOL;
	public static Item ROCK_CUTTER;
	public static Item STEEL_CHAINSAW;
	public static Item STEEL_DRILL;
	public static Item STEEL_JACKHAMMER;
	public static Item TREE_TAP;
	public static Item WRENCH;
	
	// Misc
	public static DynamicCell CELL;
	public static Item FREQUENCY_TRANSMITTER;
	public static Item MANUAL;
	public static Item MISSING_RECIPE_PLACEHOLDER;
	public static Item SCRAP_BOX;
	public static Item UPGRADES;

	public static Item QUANTUMSUIT_HELMET;
	public static Item QUANTUMSUIT_CHEST;
	public static Item QUANTUMSUIT_LEGGS;
	public static Item QUANTUMSUIT_BOOTS;
	
	
	// Gem armor&tools
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
	public static Item SAPPHIRE_CHSTPLATE;
	@Nullable
	public static Item SAPPHIRE_LEGGINGS;
	@Nullable
	public static Item SAPPHIRE_BOOTS;
	@Nullable
	public static Item PERIDOT_SWORD;
	@Nullable
	public static Item PERIDOT_PICKAXE;
	@Nullable
	public static Item PERIDOT_SAPPHIRE;
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

	public static void init() throws InstantiationException, IllegalAccessException {
		// Ingredients
		DUSTS = new ItemDusts();
		registerItem(DUSTS, "dust");
		GEMS = new ItemGems();
		registerItem(GEMS, "gem");
		INGOTS = new ItemIngots();
		registerItem(INGOTS, "ingot");
		NUGGETS = new ItemNuggets();
		registerItem(NUGGETS, "nuggets");
		PARTS = new ItemParts();
		registerItem(PARTS, "part");
		PLATES = new ItemPlates();
		registerItem(PLATES, "plates");		
		SMALL_DUSTS = new ItemDustsSmall();
		registerItem(SMALL_DUSTS, "smallDust");
		UU_MATTER = new ItemUUmatter();
		registerItem(UU_MATTER, "uumatter");

		// Armor
		CLOAKING_DEVICE = new ItemCloakingDevice();
		registerItem(CLOAKING_DEVICE, "cloakingdevice");
		LAPOTRONIC_ORB_PACK = new ItemLapotronPack();
		registerItem(LAPOTRONIC_ORB_PACK, "lapotronPack");
		LITHIUM_BATTERY_PACK = new ItemLithiumBatpack();
		registerItem(LITHIUM_BATTERY_PACK, "lithiumBatpack");
		
		// Battery
		ENERGY_CRYSTAL = new ItemEnergyCrystal();
		registerItem(ENERGY_CRYSTAL, "energycrystal");
		LAPOTRONIC_CRYSTAL = new ItemLapotronCrystal();
		registerItem(LAPOTRONIC_CRYSTAL, "lapotroncrystal");		
		LAPOTRONIC_ORB = new ItemLapotronicOrb();
		registerItem(LAPOTRONIC_ORB, "lapotronicOrb");		
		LITHIUM_BATTERY = new ItemLithiumBattery();
		registerItem(LITHIUM_BATTERY, "lithiumBattery");
		RE_BATTERY = new ItemReBattery();
		registerItem(RE_BATTERY, "rebattery");		
		
		// Tools
		ADVANCED_CHAINSAW = new ItemAdvancedChainsaw();
		registerItem(ADVANCED_CHAINSAW, "advancedchainsaw");		
		ADVANCED_DRILL = new ItemAdvancedDrill();
		registerItem(ADVANCED_DRILL, "advanceddrill");		
		ADVANCED_JACKHAMMER = new ItemAdvancedJackhammer();
		registerItem(ADVANCED_JACKHAMMER, "ironjackhammer");		
		DEBUG = new ItemDebugTool();
		registerItem(DEBUG, "debug");		
		DIAMOND_CHAINSAW = new ItemDiamondChainsaw();
		registerItem(DIAMOND_CHAINSAW, "diamondchainsaw");		
		DIAMOND_DRILL = new ItemDiamondDrill();
		registerItem(DIAMOND_DRILL, "diamonddrill");		
		DIAMOND_JACKHAMMER = new ItemDiamondJackhammer();
		registerItem(DIAMOND_JACKHAMMER, "diamondjackhammer");
		ELECTRIC_TREE_TAP = new ItemElectricTreetap();
		registerItem(ELECTRIC_TREE_TAP, "electricTreetap");	
		NANOSABER = new ItemNanosaber();
		registerItem(NANOSABER, "nanosaber");
		OMNI_TOOL = new ItemOmniTool();
		registerItem(OMNI_TOOL, "omniTool");		
		ROCK_CUTTER = new ItemRockCutter();
		registerItem(ROCK_CUTTER, "rockCutter");
		STEEL_CHAINSAW = new ItemSteelChainsaw();
		registerItem(STEEL_CHAINSAW, "ironchainsaw");		
		STEEL_DRILL = new ItemSteelDrill();
		registerItem(STEEL_DRILL, "irondrill");	
		STEEL_JACKHAMMER = new ItemSteelJackhammer();
		registerItem(STEEL_JACKHAMMER, "steeljackhammer");
		TREE_TAP = new ItemTreeTap();
		registerItem(TREE_TAP, "treetap");		
		WRENCH = new ItemWrench();
		registerItem(WRENCH, "wrench");
		
		// Misc
		CELL = new DynamicCell();
		registerItem(CELL, "dynamicCell");
		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);		
		FREQUENCY_TRANSMITTER = new ItemFrequencyTransmitter();
		registerItem(FREQUENCY_TRANSMITTER, "frequencyTransmitter");
		MANUAL = new ItemTechManual();
		registerItem(MANUAL, "techmanuel");
		MISSING_RECIPE_PLACEHOLDER = new ItemMissingRecipe().setUnlocalizedName("missingRecipe");
		registerItem(MISSING_RECIPE_PLACEHOLDER, "missingRecipe");
		SCRAP_BOX = new ItemScrapBox();
		registerItem(SCRAP_BOX, "scrapbox");
		UPGRADES = new ItemUpgrades();
		registerItem(UPGRADES, "upgrades");

		QUANTUMSUIT_HELMET = new ItemModularArmor(ItemArmor.ArmorMaterial.DIAMOND, ArmorSlot.HEAD);
		QUANTUMSUIT_HELMET.setUnlocalizedName(ModInfo.MOD_ID +".quantumsuit_helmet");
		registerItem(QUANTUMSUIT_HELMET, "quantumsuit_helmet");

		QUANTUMSUIT_CHEST = new ItemModularArmor(ItemArmor.ArmorMaterial.DIAMOND, ArmorSlot.CHEST);
		QUANTUMSUIT_CHEST.setUnlocalizedName(ModInfo.MOD_ID +".quantumsuit_chest");
		registerItem(QUANTUMSUIT_CHEST, "quantumsuit_chestt");

		QUANTUMSUIT_LEGGS = new ItemModularArmor(ItemArmor.ArmorMaterial.DIAMOND, ArmorSlot.LEGS);
		QUANTUMSUIT_LEGGS.setUnlocalizedName(ModInfo.MOD_ID +".quantumsuit_leggs");
		registerItem(QUANTUMSUIT_LEGGS, "quantumsuit_leggs");

		QUANTUMSUIT_BOOTS = new ItemModularArmor(ItemArmor.ArmorMaterial.DIAMOND, ArmorSlot.FEET);
		QUANTUMSUIT_BOOTS.setUnlocalizedName(ModInfo.MOD_ID +".quantumsuit_boots");
		registerItem(QUANTUMSUIT_BOOTS, "quantumsuit_boots");

		ModArmorUpgrades.init();

		if (ConfigTechReborn.enableGemArmorAndTools) {
			BRONZE_SWORD = new ItemTRSword(Reference.BRONZE, "ingotBronze");
			registerItem(BRONZE_SWORD, "bronzeSword");
			BRONZE_PICKAXE = new ItemTRPickaxe(Reference.BRONZE, "ingotBronze");
			registerItem(BRONZE_PICKAXE, "bronzePickaxe");
			BRONZE_SPADE = new ItemTRSpade(Reference.BRONZE, "ingotBronze");
			registerItem(BRONZE_SPADE, "bronzeSpade");
			BRONZE_AXE = new ItemTRAxe(Reference.BRONZE, "ingotBronze");
			registerItem(BRONZE_AXE, "bronzeAxe");
			BRONZE_HOE = new ItemTRHoe(Reference.BRONZE, "ingotBronze");
			registerItem(BRONZE_HOE, "bronzeHoe");

			BRONZE_HELMET = new ItemTRArmor(Reference.BRONZE_ARMOR, EntityEquipmentSlot.HEAD, "ingotBronze");
			registerItem(BRONZE_HELMET, "bronzeHelmet");
			BRONZE_CHESTPLATE = new ItemTRArmor(Reference.BRONZE_ARMOR, EntityEquipmentSlot.CHEST, "ingotBronze");
			registerItem(BRONZE_CHESTPLATE, "bronzeChestplate");
			BRONZE_LEGGINGS = new ItemTRArmor(Reference.BRONZE_ARMOR, EntityEquipmentSlot.LEGS, "ingotBronze");
			registerItem(BRONZE_LEGGINGS, "bronzeLeggings");
			BRONZE_BOOTS = new ItemTRArmor(Reference.BRONZE_ARMOR, EntityEquipmentSlot.FEET, "ingotBronze");
			registerItem(BRONZE_BOOTS, "bronzeBoots");

			RUBY_SWORD = new ItemTRSword(Reference.RUBY, "gemRuby");
			registerItem(RUBY_SWORD, "rubySword");
			RUBY_PICKAXE = new ItemTRPickaxe(Reference.RUBY, "gemRuby");
			registerItem(RUBY_PICKAXE, "rubyPickaxe");
			RUBY_SPADE = new ItemTRSpade(Reference.RUBY, "gemRuby");
			registerItem(RUBY_SPADE, "rubySpade");
			RUBY_AXE = new ItemTRAxe(Reference.RUBY, "gemRuby");
			registerItem(RUBY_AXE, "rubyAxe");
			RUBY_HOE = new ItemTRHoe(Reference.RUBY, "gemRuby");
			registerItem(RUBY_HOE, "rubyHoe");

			RUBY_HELMET = new ItemTRArmor(Reference.RUBY_ARMOR, EntityEquipmentSlot.HEAD, "gemRuby");
			registerItem(RUBY_HELMET, "rubyHelmet");
			RUBY_CHESTPLATE = new ItemTRArmor(Reference.RUBY_ARMOR, EntityEquipmentSlot.CHEST, "gemRuby");
			registerItem(RUBY_CHESTPLATE, "rubyChestplate");
			RUBY_LEGGINGS = new ItemTRArmor(Reference.RUBY_ARMOR, EntityEquipmentSlot.LEGS, "gemRuby");
			registerItem(RUBY_LEGGINGS, "rubyLeggings");
			RUBY_BOOTS = new ItemTRArmor(Reference.RUBY_ARMOR, EntityEquipmentSlot.FEET, "gemRuby");
			registerItem(RUBY_BOOTS, "rubyBoots");

			SAPPHIRE_SWORD = new ItemTRSword(Reference.SAPPHIRE, "gemSapphire");
			registerItem(SAPPHIRE_SWORD, "sapphireSword");
			SAPPHIRE_PICKAXE = new ItemTRPickaxe(Reference.SAPPHIRE, "gemSapphire");
			registerItem(SAPPHIRE_PICKAXE, "sapphirePickaxe");
			SAPPHIRE_SPADE = new ItemTRSpade(Reference.SAPPHIRE, "gemSapphire");
			registerItem(SAPPHIRE_SPADE, "sapphireSpade");
			SAPPHIRE_AXE = new ItemTRAxe(Reference.SAPPHIRE, "gemSapphire");
			registerItem(SAPPHIRE_AXE, "sapphireAxe");
			SAPPHIRE_HOE = new ItemTRHoe(Reference.SAPPHIRE, "gemSapphire");
			registerItem(SAPPHIRE_HOE, "sapphireHoe");

			SAPPHIRE_HELMET = new ItemTRArmor(Reference.SAPPHIRE_ARMOR, EntityEquipmentSlot.HEAD, "gemSapphire");
			registerItem(SAPPHIRE_HELMET, "sapphireHelmet");
			SAPPHIRE_CHSTPLATE = new ItemTRArmor(Reference.SAPPHIRE_ARMOR, EntityEquipmentSlot.CHEST, "gemSapphire");
			registerItem(SAPPHIRE_CHSTPLATE, "sapphireChestplate");
			SAPPHIRE_LEGGINGS = new ItemTRArmor(Reference.SAPPHIRE_ARMOR, EntityEquipmentSlot.LEGS, "gemSapphire");
			registerItem(SAPPHIRE_LEGGINGS, "sapphireLeggings");
			SAPPHIRE_BOOTS = new ItemTRArmor(Reference.SAPPHIRE_ARMOR, EntityEquipmentSlot.FEET, "gemSapphire");
			registerItem(SAPPHIRE_BOOTS, "sapphireBoots");

			PERIDOT_SWORD = new ItemTRSword(Reference.PERIDOT, "gemPeridot");
			registerItem(PERIDOT_SWORD, "peridotSword");
			PERIDOT_PICKAXE = new ItemTRPickaxe(Reference.PERIDOT, "gemPeridot");
			registerItem(PERIDOT_PICKAXE, "peridotPickaxe");
			PERIDOT_SAPPHIRE = new ItemTRSpade(Reference.PERIDOT, "gemPeridot");
			registerItem(PERIDOT_SAPPHIRE, "peridotSpade");
			PERIDOT_AXE = new ItemTRAxe(Reference.PERIDOT, "gemPeridot");
			registerItem(PERIDOT_AXE, "peridotAxe");
			PERIDOT_HOE = new ItemTRHoe(Reference.PERIDOT, "gemPeridot");
			registerItem(PERIDOT_HOE, "peridotHoe");

			PERIDOT_HELMET = new ItemTRArmor(Reference.PERIDOT_ARMOR, EntityEquipmentSlot.HEAD, "gemPeridot");
			registerItem(PERIDOT_HELMET, "peridotHelmet");
			PERIDOT_CHESTPLATE = new ItemTRArmor(Reference.PERIDOT_ARMOR, EntityEquipmentSlot.CHEST, "gemPeridot");
			registerItem(PERIDOT_CHESTPLATE, "peridotChestplate");
			PERIDOT_LEGGINGS = new ItemTRArmor(Reference.PERIDOT_ARMOR, EntityEquipmentSlot.LEGS, "gemPeridot");
			registerItem(PERIDOT_LEGGINGS, "peridotLeggings");
			PERIDOT_BOOTS = new ItemTRArmor(Reference.PERIDOT_ARMOR, EntityEquipmentSlot.FEET, "gemPeridot");
			registerItem(PERIDOT_BOOTS, "peridotBoots");
		}

		Core.logHelper.info("TechReborns Items Loaded");

		BlockMachineBase.advancedFrameStack = BlockMachineFrames.getFrameByName("advanced", 1);
		BlockMachineBase.basicFrameStack = BlockMachineFrames.getFrameByName("basic", 1);
	}

	public static void registerItem(Item item, String name) {
		item.setRegistryName(name);
		RebornRegistry.registerItem(item);
	}
}
