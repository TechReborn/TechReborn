package techreborn.init;

import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PoweredItem;
import reborncore.common.util.BucketHandler;
import techreborn.Core;
import techreborn.api.Reference;
import techreborn.blocks.BlockMachineFrame;
import techreborn.config.ConfigTechReborn;
import techreborn.events.OreUnifier;
import techreborn.items.*;
import techreborn.items.armor.ItemLapotronPack;
import techreborn.items.armor.ItemLithiumBatpack;
import techreborn.items.armor.ItemTRArmour;
import techreborn.items.battery.*;
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

		rockCutter = PoweredItem.createItem(ItemRockCutter.class);
		registerItem(rockCutter, "rockCutter");
		lithiumBatpack = PoweredItem.createItem(ItemLithiumBatpack.class);
		registerItem(lithiumBatpack, "lithiumBatpack");
		lapotronpack = PoweredItem.createItem(ItemLapotronPack.class);
		registerItem(lapotronpack, "lapotronPack");
		lithiumBattery = PoweredItem.createItem(ItemLithiumBattery.class);
		registerItem(lithiumBattery, "lithiumBattery");
		lapotronicOrb = PoweredItem.createItem(ItemLapotronOrb.class);
		registerItem(lapotronicOrb, "lapotronicOrb");
		omniTool = PoweredItem.createItem(ItemOmniTool.class);
		registerItem(omniTool, "omniTool");
		energyCrystal = PoweredItem.createItem(ItemEnergyCrystal.class);
		registerItem(energyCrystal, "energycrystal");
		lapotronCrystal = PoweredItem.createItem(ItemLapotronCrystal.class);
		registerItem(lapotronCrystal, "lapotroncrystal");

		manual = new ItemTechManual();
		registerItem(manual, "techmanuel");
		uuMatter = new ItemUUmatter();
		registerItem(uuMatter, "uumatter");
		reBattery = PoweredItem.createItem(ItemReBattery.class);
		registerItem(reBattery, "rebattery");
		treeTap = new ItemTreeTap();
		registerItem(treeTap, "treetap");

		ironDrill = PoweredItem.createItem(ItemSteelDrill.class);
		registerItem(ironDrill, "irondrill");
		diamondDrill = PoweredItem.createItem(ItemDiamondDrill.class);
		registerItem(diamondDrill, "diamonddrill");
		advancedDrill = PoweredItem.createItem(ItemAdvancedDrill.class);
		registerItem(advancedDrill, "advanceddrill");

		ironChainsaw = PoweredItem.createItem(ItemSteelChainsaw.class);
		registerItem(ironChainsaw, "ironchainsaw");
		diamondChainsaw = PoweredItem.createItem(ItemDiamondChainsaw.class);
		registerItem(diamondChainsaw, "diamondchainsaw");
		advancedChainsaw = PoweredItem.createItem(ItemAdvancedChainsaw.class);
		registerItem(advancedChainsaw, "advancedchainsaw");

		steelJackhammer = PoweredItem.createItem(ItemSteelJackhammer.class);
		registerItem(steelJackhammer, "steeljackhammer");
		diamondJackhammer = PoweredItem.createItem(ItemDiamondJackhammer.class);
		registerItem(diamondJackhammer, "diamondjackhammer");
		advancedJackhammer = PoweredItem.createItem(ItemAdvancedJackhammer.class);
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

		nanosaber = PoweredItem.createItem(ItemNanosaber.class);
		registerItem(nanosaber, "nanosaber");

		scrapBox = new ItemScrapBox();
		registerItem(scrapBox, "scrapbox");
		
		frequencyTransmitter = new ItemFrequencyTransmitter();
		registerItem(frequencyTransmitter, "frequencyTransmitter");

		upgrades = new ItemUpgrades();
		registerItem(upgrades, "upgrades");

		cloakingDevice = PoweredItem.createItem(ItemCloakingDevice.class);
		registerItem(cloakingDevice, "cloakingdevice");

		missingRecipe = new ItemMissingRecipe().setUnlocalizedName("missingRecipe");
		registerItem(missingRecipe, "mssingRecipe");

		debug = new ItemDebugTool();
		registerItem(debug, "debug");

		emptyCell = new EmptyCell();
		registerItem(emptyCell, "emptyCell");

		dynamicCell = new DynamicCell();
		registerItem(dynamicCell, "dynamicCell");

		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);

		Core.logHelper.info("TechReborns Items Loaded");

		registerOreDict();

		BlockMachineBase.advancedMachineStack = BlockMachineFrame.getFrameByName("advancedMachine", 1);
		BlockMachineBase.machineStack = BlockMachineFrame.getFrameByName("machine", 1);
	}

	public static void registerItem(Item item, String name){
		item.setRegistryName(name);
		GameRegistry.register(item);
	}

	public static void registerOreDict()
	{

		OreUnifier.registerOre("gemRuby", ItemGems.getGemByName("ruby"));
		OreUnifier.registerOre("gemSapphire", ItemGems.getGemByName("sapphire"));
		OreUnifier.registerOre("gemPeridot", ItemGems.getGemByName("peridot"));
		OreUnifier.registerOre("gemRedGarnet", ItemGems.getGemByName("redGarnet"));
		OreUnifier.registerOre("gemYellowGarnet", ItemGems.getGemByName("yellowGarnet"));

		// Dusts
		OreUnifier.registerOre("dustAlmandine", ItemDusts.getDustByName("almandine"));
		OreUnifier.registerOre("dustAluminum", ItemDusts.getDustByName("aluminum"));
		OreUnifier.registerOre("dustAluminium", ItemDusts.getDustByName("aluminum"));
		OreUnifier.registerOre("dustAndesite", ItemDusts.getDustByName("andesite"));
		OreUnifier.registerOre("dustAndradite", ItemDusts.getDustByName("andradite"));
		OreUnifier.registerOre("dustAsh", ItemDusts.getDustByName("ashes"));
		OreUnifier.registerOre("dustBasalt", ItemDusts.getDustByName("basalt"));
		OreUnifier.registerOre("dustBauxite", ItemDusts.getDustByName("bauxite"));
		OreUnifier.registerOre("dustBrass", ItemDusts.getDustByName("brass"));
		OreUnifier.registerOre("dustBronze", ItemDusts.getDustByName("bronze"));
		OreUnifier.registerOre("dustCalcite", ItemDusts.getDustByName("calcite"));
		OreUnifier.registerOre("dustCharcoal", ItemDusts.getDustByName("charcoal"));
		OreUnifier.registerOre("dustChrome", ItemDusts.getDustByName("chrome"));
		OreUnifier.registerOre("dustCinnabar", ItemDusts.getDustByName("cinnabar"));
		OreUnifier.registerOre("dustClay", ItemDusts.getDustByName("clay"));
		OreUnifier.registerOre("dustCoal", ItemDusts.getDustByName("coal"));
		OreUnifier.registerOre("dustCopper", ItemDusts.getDustByName("copper"));
		OreUnifier.registerOre("dustDarkAsh", ItemDusts.getDustByName("darkAshes"));
		OreUnifier.registerOre("dustDiamond", ItemDusts.getDustByName("diamond"));
		OreUnifier.registerOre("dustDiorite", ItemDusts.getDustByName("diorite"));
		OreUnifier.registerOre("dustElectrum", ItemDusts.getDustByName("electrum"));
		OreUnifier.registerOre("dustEmerald", ItemDusts.getDustByName("emerald"));
		OreUnifier.registerOre("dustEnderEye", ItemDusts.getDustByName("enderEye"));
		OreUnifier.registerOre("dustEnderPearl", ItemDusts.getDustByName("enderPearl"));
		OreUnifier.registerOre("dustEndstone", ItemDusts.getDustByName("endstone"));
		OreUnifier.registerOre("dustFlint", ItemDusts.getDustByName("flint"));
		OreUnifier.registerOre("dustGalena", ItemDusts.getDustByName("galena"));
		OreUnifier.registerOre("dustGold", ItemDusts.getDustByName("gold"));
		OreUnifier.registerOre("dustGranite", ItemDusts.getDustByName("granite"));
		OreUnifier.registerOre("dustGrossular", ItemDusts.getDustByName("grossular"));
		OreUnifier.registerOre("dustInvar", ItemDusts.getDustByName("invar"));
		OreUnifier.registerOre("dustIron", ItemDusts.getDustByName("iron"));
		OreUnifier.registerOre("dustLapis", ItemDusts.getDustByName("lazurite"));
		OreUnifier.registerOre("dustLazurite", ItemDusts.getDustByName("lazurite"));
		OreUnifier.registerOre("dustLead", ItemDusts.getDustByName("lead"));
		OreUnifier.registerOre("dustMagnesium", ItemDusts.getDustByName("magnesium"));
		OreUnifier.registerOre("dustManganese", ItemDusts.getDustByName("manganese"));
		OreUnifier.registerOre("dustMarble", ItemDusts.getDustByName("marble"));
		OreUnifier.registerOre("dustNetherrack", ItemDusts.getDustByName("netherrack"));
		OreUnifier.registerOre("dustNickel", ItemDusts.getDustByName("nickel"));
		OreUnifier.registerOre("dustObsidian", ItemDusts.getDustByName("obsidian"));
		OreUnifier.registerOre("dustPeridot", ItemDusts.getDustByName("peridot"));
		OreUnifier.registerOre("dustPhosphorous", ItemDusts.getDustByName("phosphorous"));
		OreUnifier.registerOre("dustPlatinum", ItemDusts.getDustByName("platinum"));
		OreUnifier.registerOre("dustPyrite", ItemDusts.getDustByName("pyrite"));
		OreUnifier.registerOre("dustPyrope", ItemDusts.getDustByName("pyrope"));
		OreUnifier.registerOre("dustRedGarnet", ItemDusts.getDustByName("redGarnet"));
		OreUnifier.registerOre("dustRuby", ItemDusts.getDustByName("ruby"));
		OreUnifier.registerOre("dustSaltpeter", ItemDusts.getDustByName("saltpeter"));
		OreUnifier.registerOre("dustSapphire", ItemDusts.getDustByName("sapphire"));
		OreUnifier.registerOre("dustSilver", ItemDusts.getDustByName("silver"));
		OreUnifier.registerOre("dustSodalite", ItemDusts.getDustByName("sodalite"));
		OreUnifier.registerOre("dustSpessartine", ItemDusts.getDustByName("spessartine"));
		OreUnifier.registerOre("dustSphalerite", ItemDusts.getDustByName("sphalerite"));
		OreUnifier.registerOre("dustSteel", ItemDusts.getDustByName("steel"));
		OreUnifier.registerOre("dustSulfur", ItemDusts.getDustByName("sulfur"));
		OreUnifier.registerOre("dustCopper", ItemDusts.getDustByName("copper"));
		OreUnifier.registerOre("dustTin", ItemDusts.getDustByName("tin"));
		OreUnifier.registerOre("dustTitanium", ItemDusts.getDustByName("titanium"));
		OreUnifier.registerOre("dustTungsten", ItemDusts.getDustByName("tungsten"));
		OreUnifier.registerOre("dustUvarovite", ItemDusts.getDustByName("uvarovite"));
		OreUnifier.registerOre("dustYellowGarnet", ItemDusts.getDustByName("yellowGarnet"));
		OreUnifier.registerOre("dustZinc", ItemDusts.getDustByName("zinc"));
		OreUnifier.registerOre("dustOlivine", ItemDusts.getDustByName("olivine"));
		OreUnifier.registerOre("dustWood", ItemDusts.getDustByName("sawDust"));
		OreUnifier.registerOre("pulpWood", ItemDusts.getDustByName("sawDust"));

		// Small Dusts
		OreUnifier.registerOre("dustAlmandine", ItemDusts.getDustByName("almandine"));
		OreUnifier.registerOre("dustAluminum", ItemDusts.getDustByName("aluminum"));
		OreUnifier.registerOre("dustAluminium", ItemDusts.getDustByName("aluminum"));
		OreUnifier.registerOre("dustAndesite", ItemDusts.getDustByName("andesite"));
		OreUnifier.registerOre("dustAndradite", ItemDusts.getDustByName("andradite"));
		OreUnifier.registerOre("dustAsh", ItemDusts.getDustByName("ashes"));
		OreUnifier.registerOre("dustBasalt", ItemDusts.getDustByName("basalt"));
		OreUnifier.registerOre("dustBauxite", ItemDusts.getDustByName("bauxite"));
		OreUnifier.registerOre("dustBrass", ItemDusts.getDustByName("brass"));
		OreUnifier.registerOre("dustBronze", ItemDusts.getDustByName("bronze"));
		OreUnifier.registerOre("dustCalcite", ItemDusts.getDustByName("calcite"));
		OreUnifier.registerOre("dustCharcoal", ItemDusts.getDustByName("charcoal"));
		OreUnifier.registerOre("dustChrome", ItemDusts.getDustByName("chrome"));
		OreUnifier.registerOre("dustCinnabar", ItemDusts.getDustByName("cinnabar"));
		OreUnifier.registerOre("dustClay", ItemDusts.getDustByName("clay"));
		OreUnifier.registerOre("dustCoal", ItemDusts.getDustByName("coal"));
		OreUnifier.registerOre("dustCopper", ItemDusts.getDustByName("copper"));
		OreUnifier.registerOre("dustDarkAsh", ItemDusts.getDustByName("darkAshes"));
		OreUnifier.registerOre("dustDiamond", ItemDusts.getDustByName("diamond"));
		OreUnifier.registerOre("dustDiorite", ItemDusts.getDustByName("diorite"));
		OreUnifier.registerOre("dustElectrum", ItemDusts.getDustByName("electrum"));
		OreUnifier.registerOre("dustEmerald", ItemDusts.getDustByName("emerald"));
		OreUnifier.registerOre("dustEnderEye", ItemDusts.getDustByName("enderEye"));
		OreUnifier.registerOre("dustEnderPearl", ItemDusts.getDustByName("enderPearl"));
		OreUnifier.registerOre("dustEndstone", ItemDusts.getDustByName("endstone"));
		OreUnifier.registerOre("dustFlint", ItemDusts.getDustByName("flint"));
		OreUnifier.registerOre("dustGalena", ItemDusts.getDustByName("galena"));
		OreUnifier.registerOre("dustGold", ItemDusts.getDustByName("gold"));
		OreUnifier.registerOre("dustGranite", ItemDusts.getDustByName("granite"));
		OreUnifier.registerOre("dustGrossular", ItemDusts.getDustByName("grossular"));
		OreUnifier.registerOre("dustInvar", ItemDusts.getDustByName("invar"));
		OreUnifier.registerOre("dustIron", ItemDusts.getDustByName("iron"));
		OreUnifier.registerOre("dustLapis", ItemDusts.getDustByName("lazurite"));
		OreUnifier.registerOre("dustLazurite", ItemDusts.getDustByName("lazurite"));
		OreUnifier.registerOre("dustLead", ItemDusts.getDustByName("lead"));
		OreUnifier.registerOre("dustMagnesium", ItemDusts.getDustByName("magnesium"));
		OreUnifier.registerOre("dustManganese", ItemDusts.getDustByName("manganese"));
		OreUnifier.registerOre("dustMarble", ItemDusts.getDustByName("marble"));
		OreUnifier.registerOre("dustNetherrack", ItemDusts.getDustByName("netherrack"));
		OreUnifier.registerOre("dustNickel", ItemDusts.getDustByName("nickel"));
		OreUnifier.registerOre("dustObsidian", ItemDusts.getDustByName("obsidian"));
		OreUnifier.registerOre("dustPeridot", ItemDusts.getDustByName("peridot"));
		OreUnifier.registerOre("dustPhosphorous", ItemDusts.getDustByName("phosphorous"));
		OreUnifier.registerOre("dustPlatinum", ItemDusts.getDustByName("platinum"));
		OreUnifier.registerOre("dustPyrite", ItemDusts.getDustByName("pyrite"));
		OreUnifier.registerOre("dustPyrope", ItemDusts.getDustByName("pyrope"));
		OreUnifier.registerOre("dustRedGarnet", ItemDusts.getDustByName("redGarnet"));
		OreUnifier.registerOre("dustRuby", ItemDusts.getDustByName("ruby"));
		OreUnifier.registerOre("dustSaltpeter", ItemDusts.getDustByName("saltpeter"));
		OreUnifier.registerOre("dustSapphire", ItemDusts.getDustByName("sapphire"));
		OreUnifier.registerOre("dustSilver", ItemDusts.getDustByName("silver"));
		OreUnifier.registerOre("dustSodalite", ItemDusts.getDustByName("sodalite"));
		OreUnifier.registerOre("dustSpessartine", ItemDusts.getDustByName("spessartine"));
		OreUnifier.registerOre("dustSphalerite", ItemDusts.getDustByName("sphalerite"));
		OreUnifier.registerOre("dustSteel", ItemDusts.getDustByName("steel"));
		OreUnifier.registerOre("dustSulfur", ItemDusts.getDustByName("sulfur"));
		OreUnifier.registerOre("dustCopper", ItemDusts.getDustByName("copper"));
		OreUnifier.registerOre("dustTin", ItemDusts.getDustByName("tin"));
		OreUnifier.registerOre("dustTitanium", ItemDusts.getDustByName("titanium"));
		OreUnifier.registerOre("dustTungsten", ItemDusts.getDustByName("tungsten"));
		OreUnifier.registerOre("dustUvarovite", ItemDusts.getDustByName("uvarovite"));
		OreUnifier.registerOre("dustYellowGarnet", ItemDusts.getDustByName("yellowGarnet"));
		OreUnifier.registerOre("dustZinc", ItemDusts.getDustByName("zinc"));
		OreUnifier.registerOre("dustOlivine", ItemDusts.getDustByName("olivine"));
		OreUnifier.registerOre("pulpWood", ItemDusts.getDustByName("sawDust"));

		// Ingots
		OreUnifier.registerOre("ingotAluminum", ItemIngots.getIngotByName("aluminum"));
		OreUnifier.registerOre("ingotAluminium", ItemIngots.getIngotByName("aluminum"));
		OreUnifier.registerOre("ingotBrass", ItemIngots.getIngotByName("brass"));
		OreUnifier.registerOre("ingotBronze", ItemIngots.getIngotByName("bronze"));
		OreUnifier.registerOre("ingotChrome", ItemIngots.getIngotByName("chrome"));
		OreUnifier.registerOre("ingotCopper", ItemIngots.getIngotByName("copper"));
		OreUnifier.registerOre("ingotElectrum", ItemIngots.getIngotByName("electrum"));
		OreUnifier.registerOre("ingotInvar", ItemIngots.getIngotByName("invar"));
		OreUnifier.registerOre("ingotIridium", ItemIngots.getIngotByName("iridium"));
		OreUnifier.registerOre("ingotLead", ItemIngots.getIngotByName("lead"));
		OreUnifier.registerOre("ingotNickel", ItemIngots.getIngotByName("nickel"));
		OreUnifier.registerOre("ingotPlatinum", ItemIngots.getIngotByName("platinum"));
		OreUnifier.registerOre("ingotSilver", ItemIngots.getIngotByName("silver"));
		OreUnifier.registerOre("ingotSteel", ItemIngots.getIngotByName("steel"));
		OreUnifier.registerOre("ingotTin", ItemIngots.getIngotByName("tin"));
		OreUnifier.registerOre("ingotTitanium", ItemIngots.getIngotByName("titanium"));
		OreUnifier.registerOre("ingotTungsten", ItemIngots.getIngotByName("tungsten"));
		OreUnifier.registerOre("ingotTungstensteel", ItemIngots.getIngotByName("tungstensteel"));
		OreUnifier.registerOre("ingotHotTungstenSteel", ItemIngots.getIngotByName("hotTungstensteel"));
		OreUnifier.registerOre("ingotZinc", ItemIngots.getIngotByName("zinc"));
		OreUnifier.registerOre("ingotRefinedIron", ItemIngots.getIngotByName("refinedIron"));
		OreUnifier.registerOre("ingotCopper", ItemIngots.getIngotByName("copper"));


		// Nuggets
		OreUnifier.registerOre("nuggetAluminum", ItemNuggets.getNuggetByName("aluminum"));
		OreUnifier.registerOre("nuggetAluminium", ItemNuggets.getNuggetByName("aluminum"));
		OreUnifier.registerOre("nuggetBrass", ItemNuggets.getNuggetByName("brass"));
		OreUnifier.registerOre("nuggetBronze", ItemNuggets.getNuggetByName("bronze"));
		OreUnifier.registerOre("nuggetChrome", ItemNuggets.getNuggetByName("chrome"));
		OreUnifier.registerOre("nuggetCopper", ItemNuggets.getNuggetByName("copper"));
		OreUnifier.registerOre("nuggetElectrum", ItemNuggets.getNuggetByName("electrum"));
		OreUnifier.registerOre("nuggetInvar", ItemNuggets.getNuggetByName("invar"));
		OreUnifier.registerOre("nuggetIridium", ItemNuggets.getNuggetByName("iridium"));
		OreUnifier.registerOre("nuggetLead", ItemNuggets.getNuggetByName("lead"));
		OreUnifier.registerOre("nuggetNickel", ItemNuggets.getNuggetByName("nickel"));
		OreUnifier.registerOre("nuggetPlatinum", ItemNuggets.getNuggetByName("platinum"));
		OreUnifier.registerOre("nuggetSilver", ItemNuggets.getNuggetByName("silver"));
		OreUnifier.registerOre("nuggetSteel", ItemNuggets.getNuggetByName("steel"));
		OreUnifier.registerOre("nuggetTin", ItemNuggets.getNuggetByName("tin"));
		OreUnifier.registerOre("nuggetTitanium", ItemNuggets.getNuggetByName("titanium"));
		OreUnifier.registerOre("nuggetTungsten", ItemNuggets.getNuggetByName("tungsten"));
		OreUnifier.registerOre("nuggetTungstensteel", ItemNuggets.getNuggetByName("tungstensteel"));
		OreUnifier.registerOre("nuggetHotTungstenSteel", ItemNuggets.getNuggetByName("hotTungstensteel"));
		OreUnifier.registerOre("nuggetZinc", ItemNuggets.getNuggetByName("zinc"));
		OreUnifier.registerOre("nuggetRefinedIron", ItemNuggets.getNuggetByName("refinedIron"));
		OreUnifier.registerOre("nuggetCopper", ItemNuggets.getNuggetByName("copper"));
		OreUnifier.registerOre("nuggetIron", ItemNuggets.getNuggetByName("iron"));

		// Plates
		OreUnifier.registerOre("plateAluminum", ItemPlates.getPlateByName("aluminum"));
		OreUnifier.registerOre("plateAluminium", ItemPlates.getPlateByName("aluminum"));
		OreUnifier.registerOre("plateBrass", ItemPlates.getPlateByName("brass"));
		OreUnifier.registerOre("plateBronze", ItemPlates.getPlateByName("bronze"));
		OreUnifier.registerOre("plateChrome", ItemPlates.getPlateByName("chrome"));
		OreUnifier.registerOre("plateCopper", ItemPlates.getPlateByName("copper"));
		OreUnifier.registerOre("plateDiamond", ItemPlates.getPlateByName("diamond"));
		OreUnifier.registerOre("plateElectrum", ItemPlates.getPlateByName("electrum"));
		OreUnifier.registerOre("plateEmerald", ItemPlates.getPlateByName("emerald"));
		OreUnifier.registerOre("plateGold", ItemPlates.getPlateByName("gold"));
		OreUnifier.registerOre("plateInvar", ItemPlates.getPlateByName("invar"));
		OreUnifier.registerOre("plateIridium", ItemPlates.getPlateByName("iridium"));
		OreUnifier.registerOre("plateIron", ItemPlates.getPlateByName("iron"));
		OreUnifier.registerOre("plateLapis", ItemPlates.getPlateByName("lapis"));
		OreUnifier.registerOre("plateLead", ItemPlates.getPlateByName("lead"));
		OreUnifier.registerOre("plateMagnalium", ItemPlates.getPlateByName("magnalium"));
		OreUnifier.registerOre("plateNickel", ItemPlates.getPlateByName("nickel"));
		OreUnifier.registerOre("plateObsidian", ItemPlates.getPlateByName("obsidian"));
		OreUnifier.registerOre("platePeridot", ItemPlates.getPlateByName("peridot"));
		OreUnifier.registerOre("platePlatinum", ItemPlates.getPlateByName("platinum"));
		OreUnifier.registerOre("plateRedGarnet", ItemPlates.getPlateByName("redGarnet"));
		OreUnifier.registerOre("plateRedstone", ItemPlates.getPlateByName("redstone"));
		OreUnifier.registerOre("plateRuby", ItemPlates.getPlateByName("ruby"));
		OreUnifier.registerOre("plateSapphire", ItemPlates.getPlateByName("sapphire"));
		OreUnifier.registerOre("plateSilicon", ItemPlates.getPlateByName("silicon"));
		OreUnifier.registerOre("plateSilver", ItemPlates.getPlateByName("silver"));
		OreUnifier.registerOre("plateSteel", ItemPlates.getPlateByName("steel"));
		OreUnifier.registerOre("plateTin", ItemPlates.getPlateByName("tin"));
		OreUnifier.registerOre("plateTitanium", ItemPlates.getPlateByName("titanium"));
		OreUnifier.registerOre("plateTungsten", ItemPlates.getPlateByName("tungsten"));
		OreUnifier.registerOre("plateTungstensteel", ItemPlates.getPlateByName("tungstensteel"));
		OreUnifier.registerOre("plateYellowGarnet", ItemPlates.getPlateByName("yellowGarnet"));
		OreUnifier.registerOre("plateZinc", ItemPlates.getPlateByName("zinc"));
		OreUnifier.registerOre("blockLapis", ItemPlates.getPlateByName("lazurite"));

		OreUnifier.registerOre("diamondTR", ItemDusts.getDustByName("Diamond"));
		OreUnifier.registerOre("diamondTR", Items.DIAMOND);

		OreUnifier.registerOre("craftingGrinder", ItemParts.getPartByName("diamondGrindingHead"));
		OreUnifier.registerOre("craftingGrinder", ItemParts.getPartByName("tungstenGrindingHead"));
		OreUnifier.registerOre("circuitElite", ItemParts.getPartByName("dataControlCircuit"));
		OreUnifier.registerOre("circuitData", ItemParts.getPartByName("dataStorageCircuit"));
		OreUnifier.registerOre("craftingSuperconductor", ItemParts.getPartByName("superconductor"));
		OreUnifier.registerOre("batteryUltimate", ItemParts.getPartByName("diamondGrindingHead"));

		OreUnifier.registerOre("containerWater", ItemCells.getCellByName("water"));
		OreUnifier.registerOre("containerWater", Items.WATER_BUCKET);

		OreUnifier.registerOre("materialResin", ItemParts.getPartByName("rubberSap"));
		OreUnifier.registerOre("materialRubber", ItemParts.getPartByName("rubber"));
		OreUnifier.registerOre("circuitBasic", ItemParts.getPartByName("electronicCircuit"));
		OreUnifier.registerOre("circuitAdvanced", ItemParts.getPartByName("advancedCircuit"));

	}

}
