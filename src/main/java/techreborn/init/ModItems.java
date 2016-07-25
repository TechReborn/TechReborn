package techreborn.init;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.util.BucketHandler;
import techreborn.Core;
import techreborn.api.Reference;
import techreborn.blocks.BlockMachineFrame;
import techreborn.config.ConfigTechReborn;
import techreborn.items.*;
import techreborn.items.armor.ItemLapotronPack;
import techreborn.items.armor.ItemLithiumBatpack;
import techreborn.items.armor.ItemTRArmour;
import techreborn.items.battery.ItemEnergyCrystal;
import techreborn.items.battery.ItemLapotronCrystal;
import techreborn.items.battery.ItemLithiumBattery;
import techreborn.items.battery.ItemReBattery;
import techreborn.items.tools.*;

public class ModItems
{

	public static Item gems;
	public static Item ingots;
	public static Item nuggets;
	public static Item dusts;
	public static Item smallDusts;
	public static Item parts;
	public static Item rockCutter;
	public static Item lithiumBatpack;
	public static Item lapotronpack;
	public static Item lithiumBattery;
	public static Item omniTool;
	public static Item lapotronicOrb;
	public static Item manual;
	public static Item uuMatter;
	public static Item plate;
	public static Item cloakingDevice;

	public static Item reBattery;
	public static Item treeTap;

	public static Item ironDrill;
	public static Item diamondDrill;
	public static Item advancedDrill;

	public static Item ironChainsaw;
	public static Item diamondChainsaw;
	public static Item advancedChainsaw;

	public static Item steelJackhammer;
	public static Item diamondJackhammer;
	public static Item advancedJackhammer;

	public static Item nanosaber;
	public static Item wrench;
	public static Item lapotronCrystal;
	public static Item energyCrystal;
	public static Item scrapBox;
	public static Item frequencyTransmitter;

	public static Item bronzeSword;
	public static Item bronzePickaxe;
	public static Item bronzeSpade;
	public static Item bronzeAxe;
	public static Item bronzeHoe;

	public static Item bronzeHelmet;
	public static Item bronzeChestplate;
	public static Item bronzeLeggings;
	public static Item bronzeBoots;

	public static Item rubySword;
	public static Item rubyPickaxe;
	public static Item rubySpade;
	public static Item rubyAxe;
	public static Item rubyHoe;

	public static Item rubyHelmet;
	public static Item rubyChestplate;
	public static Item rubyLeggings;
	public static Item rubyBoots;

	public static Item sapphireSword;
	public static Item sapphirePickaxe;
	public static Item sapphireSpade;
	public static Item sapphireAxe;
	public static Item sapphireHoe;

	public static Item sapphireHelmet;
	public static Item sapphireChestplate;
	public static Item sapphireLeggings;
	public static Item sapphireBoots;

	public static Item peridotSword;
	public static Item peridotPickaxe;
	public static Item peridotSpade;
	public static Item peridotAxe;
	public static Item peridotHoe;

	public static Item peridotHelmet;
	public static Item peridotChestplate;
	public static Item peridotLeggings;
	public static Item peridotBoots;

	public static Item upgrades;

	public static Item missingRecipe;
	public static Item debug;

	public static Item emptyCell;
	public static DynamicCell dynamicCell;

	public static final String META_PLACEHOLDER = "PLACEHOLDER_ITEM";

	public static void init() throws InstantiationException, IllegalAccessException
	{
		gems = new ItemGems();
		registerItem(gems, "gem");
		ingots = new ItemIngots();
		registerItem(ingots, "ingot");
		dusts = new ItemDusts();
		registerItem(dusts, "dust");
		smallDusts = new ItemDustsSmall();
		registerItem(smallDusts, "smallDust");
		plate = new ItemPlates();
		registerItem(plate, "plates");
		nuggets = new ItemNuggets();
		registerItem(nuggets, "nuggets");
		// purifiedCrushedOre = new ItemPurifiedCrushedOre();
		// registerItem(purifiedCrushedOre, "purifiedCrushedOre");
		parts = new ItemParts();
		registerItem(parts, "part");

		rockCutter = new ItemRockCutter();
		registerItem(rockCutter, "rockCutter");
		lithiumBatpack = new ItemLithiumBatpack();
		registerItem(lithiumBatpack, "lithiumBatpack");
		lapotronpack = new ItemLapotronPack();
		registerItem(lapotronpack, "lapotronPack");
		lithiumBattery = new ItemLithiumBattery();
		registerItem(lithiumBattery, "lithiumBattery");
		lapotronicOrb = new ItemLapotronicOrb();
		registerItem(lapotronicOrb, "lapotronicOrb");
		omniTool = new ItemOmniTool();
		registerItem(omniTool, "omniTool");
		energyCrystal = new ItemEnergyCrystal();
		registerItem(energyCrystal, "energycrystal");
		lapotronCrystal = new ItemLapotronCrystal();
		registerItem(lapotronCrystal, "lapotroncrystal");

		manual = new ItemTechManual();
		registerItem(manual, "techmanuel");
		uuMatter = new ItemUUmatter();
		registerItem(uuMatter, "uumatter");
		reBattery = new ItemReBattery();
		registerItem(reBattery, "rebattery");
		treeTap = new ItemTreeTap();
		registerItem(treeTap, "treetap");

		ironDrill = new ItemSteelDrill();
		registerItem(ironDrill, "irondrill");
		diamondDrill = new ItemDiamondDrill();
		registerItem(diamondDrill, "diamonddrill");
		advancedDrill = new ItemAdvancedDrill();
		registerItem(advancedDrill, "advanceddrill");

		ironChainsaw = new ItemSteelChainsaw();
		registerItem(ironChainsaw, "ironchainsaw");
		diamondChainsaw = new ItemDiamondChainsaw();
		registerItem(diamondChainsaw, "diamondchainsaw");
		advancedChainsaw = new ItemAdvancedChainsaw();
		registerItem(advancedChainsaw, "advancedchainsaw");

		steelJackhammer = new ItemSteelJackhammer();
		registerItem(steelJackhammer, "steeljackhammer");
		diamondJackhammer = new ItemDiamondJackhammer();
		registerItem(diamondJackhammer, "diamondjackhammer");
		advancedJackhammer = new ItemAdvancedJackhammer();
		registerItem(advancedJackhammer, "ironjackhammer");

		if(ConfigTechReborn.enableGemArmorAndTools){
			bronzeSword = new ItemTRSword(Reference.BRONZE);
			registerItem(bronzeSword, "bronzeSword");
			bronzePickaxe = new ItemTRPickaxe(Reference.BRONZE);
			registerItem(bronzePickaxe, "bronzePickaxe");
			bronzeSpade = new ItemTRSpade(Reference.BRONZE);
			registerItem(bronzeSpade, "bronzeSpade");
			bronzeAxe = new ItemTRAxe(Reference.BRONZE);
			registerItem(bronzeAxe, "bronzeAxe");
			bronzeHoe = new ItemTRHoe(Reference.BRONZE);
			registerItem(bronzeHoe, "bronzeHoe");

			bronzeHelmet = new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.HEAD);
			registerItem(bronzeHelmet, "bronzeHelmet");
			bronzeChestplate = new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.CHEST);
			registerItem(bronzeChestplate, "bronzeChestplate");
			bronzeLeggings = new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.LEGS);
			registerItem(bronzeLeggings, "bronzeLeggings");
			bronzeBoots = new ItemTRArmour(Reference.BRONZE_ARMOUR, EntityEquipmentSlot.FEET);
			registerItem(bronzeBoots, "bronzeBoots");

			rubySword = new ItemTRSword(Reference.RUBY);
			registerItem(rubySword, "rubySword");
			rubyPickaxe = new ItemTRPickaxe(Reference.RUBY);
			registerItem(rubyPickaxe, "rubyPickaxe");
			rubySpade = new ItemTRSpade(Reference.RUBY);
			registerItem(rubySpade, "rubySpade");
			rubyAxe = new ItemTRAxe(Reference.RUBY);
			registerItem(rubyAxe, "rubyAxe");
			rubyHoe = new ItemTRHoe(Reference.RUBY);
			registerItem(rubyHoe, "rubyHoe");

			rubyHelmet = new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.HEAD);
			registerItem(rubyHelmet, "rubyHelmet");
			rubyChestplate = new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.CHEST);
			registerItem(rubyChestplate, "rubyChestplate");
			rubyLeggings = new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.LEGS);
			registerItem(rubyLeggings, "rubyLeggings");
			rubyBoots = new ItemTRArmour(Reference.RUBY_ARMOUR, EntityEquipmentSlot.FEET);
			registerItem(rubyBoots, "rubyBoots");

			sapphireSword = new ItemTRSword(Reference.SAPPHIRE);
			registerItem(sapphireSword, "sapphireSword");
			sapphirePickaxe = new ItemTRPickaxe(Reference.SAPPHIRE);
			registerItem(sapphirePickaxe, "sapphirePickaxe");
			sapphireSpade = new ItemTRSpade(Reference.SAPPHIRE);
			registerItem(sapphireSpade, "sapphireSpade");
			sapphireAxe = new ItemTRAxe(Reference.SAPPHIRE);
			registerItem(sapphireAxe, "sapphireAxe");
			sapphireHoe = new ItemTRHoe(Reference.SAPPHIRE);
			registerItem(sapphireHoe, "sapphireHoe");

			sapphireHelmet = new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.HEAD);
			registerItem(sapphireHelmet, "sapphireHelmet");
			sapphireChestplate = new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.CHEST);
			registerItem(sapphireChestplate, "sapphireChestplate");
			sapphireLeggings = new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.LEGS);
			registerItem(sapphireLeggings, "sapphireLeggings");
			sapphireBoots = new ItemTRArmour(Reference.SAPPHIRE_ARMOUR, EntityEquipmentSlot.FEET);
			registerItem(sapphireBoots, "sapphireBoots");

			peridotSword = new ItemTRSword(Reference.PERIDOT);
			registerItem(peridotSword, "peridotSword");
			peridotPickaxe = new ItemTRPickaxe(Reference.PERIDOT);
			registerItem(peridotPickaxe, "peridotPickaxe");
			peridotSpade = new ItemTRSpade(Reference.PERIDOT);
			registerItem(peridotSpade, "peridotSpade");
			peridotAxe = new ItemTRAxe(Reference.PERIDOT);
			registerItem(peridotAxe, "peridotAxe");
			peridotHoe = new ItemTRHoe(Reference.PERIDOT);
			registerItem(peridotHoe, "peridotHoe");

			peridotHelmet = new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.HEAD);
			registerItem(peridotHelmet, "peridotHelmet");
			peridotChestplate = new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.CHEST);
			registerItem(peridotChestplate, "peridotChestplate");
			peridotLeggings = new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.LEGS);
			registerItem(peridotLeggings, "peridotLeggings");
			peridotBoots = new ItemTRArmour(Reference.PERIDOT_ARMOUR, EntityEquipmentSlot.FEET);
			registerItem(peridotBoots, "peridotBoots");
		}


		wrench = new ItemWrench();
		registerItem(wrench, "wrench");

		nanosaber = new ItemNanosaber();
		registerItem(nanosaber, "nanosaber");

		scrapBox = new ItemScrapBox();
		registerItem(scrapBox, "scrapbox");
		
		frequencyTransmitter = new ItemFrequencyTransmitter();
		registerItem(frequencyTransmitter, "frequencyTransmitter");

		upgrades = new ItemUpgrades();
		registerItem(upgrades, "upgrades");

		cloakingDevice = new ItemCloakingDevice();
		registerItem(cloakingDevice, "cloakingdevice");

		missingRecipe = new ItemMissingRecipe().setUnlocalizedName("missingRecipe");
		registerItem(missingRecipe, "mssingRecipe");

		debug = new ItemDebugTool();
		registerItem(debug, "debug");

		dynamicCell = new DynamicCell();
		registerItem(dynamicCell, "dynamicCell");

		emptyCell = dynamicCell;
		Item cell = new EmptyCell();
		registerItem(cell, "emptyCell");
		GameRegistry.addShapelessRecipe(new ItemStack(dynamicCell), cell);

		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);

		Core.logHelper.info("TechReborns Items Loaded");

		BlockMachineBase.advancedMachineStack = BlockMachineFrame.getFrameByName("advancedMachine", 1);
		BlockMachineBase.machineStack = BlockMachineFrame.getFrameByName("machine", 1);
	}

	public static void registerItem(Item item, String name){
		item.setRegistryName(name);
		GameRegistry.register(item);
	}

}
