package techreborn.config;

import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

import java.io.File;

public class ConfigTechReborn {
	private static ConfigTechReborn instance = null;
	public static String CATEGORY_WORLD = "world";
	public static String CATEGORY_POWER = "power";
	public static String CATEGORY_CRAFTING = "crafting";
	public static String CATEGORY_UU = "uu";
	public static String CATEGORY_EMC = "emc";
	public static String CATEGORY_INTEGRATION = "Integration";

	// WORLDGEN
	public static boolean GalenaOreTrue;
	public static int GalenaOreRare;

	public static boolean IridiumOreTrue;
	public static int IridiumOreRare;

	public static boolean RubyOreTrue;
	public static int RubyOreRare;

	public static boolean SapphireOreTrue;
	public static int SapphireOreRare;

	public static boolean BauxiteOreTrue;
	public static int BauxiteOreRare;

	public static boolean TetrahedriteOreTrue;
	public static int TetrahedriteOreRare;

	public static boolean CassiteriteOreTrue;
	public static int CassiteriteOreRare;

	public static boolean LeadOreTrue;
	public static int LeadOreRare;

	public static boolean SilverOreTrue;
	public static int SilverOreRare;

	public static boolean PyriteOreTrue;
	public static int PyriteOreRare;

	public static boolean CinnabarOreTrue;
	public static int CinnabarOreRare;

	public static boolean SphaleriteOreTrue;
	public static int SphaleriteOreRare;

	public static boolean TungstenOreTrue;
	public static int TungstenOreRare;

	public static boolean SheldoniteOreTrue;
	public static int SheldoniteOreRare;

	public static boolean PeridotOreTrue;
	public static int PeridotOreRare;

	public static boolean SodaliteOreTrue;
	public static int SodaliteOreRare;

	public static double FortuneSecondaryOreMultiplierPerLevel;

	// Power
	public static int ThermalGenertaorOutput;
	public static int CentrifugeInputTick;
	public static int DragoneggsiphonerOutput;
	public static int heatGeneratorOutput;
	public static int aveargeEuOutTickTime;
	public static int extraOutputPerLesuBlock;
	public static int baseLesuOutput;
	public static int lesuStoragePerBlock;
	public static int farmEu;
	public static int aesuMaxOutput;
	public static int aesuMaxStorage;
	// Charge
	public static int AdvancedDrillCharge;
	public static int LapotronPackCharge;
	public static int LithiumBatpackCharge;
	public static int LapotronicOrbMaxCharge;
	public static int OmniToolCharge;
	public static int RockCutterCharge;
	public static int CloakingDeviceCharge;
	public static int CentrifugeCharge;
	public static int ThermalGeneratorCharge;
	// Tier
	public static int AdvancedDrillTier;
	public static int LapotronPackTier;
	public static int LapotronicOrbTier;
	public static int LithiumBatpackTier;
	public static int OmniToolTier;
	public static int RockCutterTier;
	public static int CloakingDeviceTier;
	public static int CentrifugeTier;
	public static int ThermalGeneratorTier;
	// EU/T
	public static int CloakingDeviceEUTick;
	// Crafting
	public static boolean ExpensiveMacerator;
	public static boolean ExpensiveDrill;
	public static boolean ExpensiveDiamondDrill;
	public static boolean ExpensiveSolar;
	public static boolean ExpensiveWatermill;
	public static boolean ExpensiveWindmill;
	// UU
	public static boolean HideUuRecipes;
	public static boolean UUrecipesIridiamOre;
	public static boolean UUrecipesWood;
	public static boolean UUrecipesStone;
	public static boolean UUrecipesSnowBlock;
	public static boolean UUrecipesGrass;
	public static boolean UUrecipesObsidian;
	public static boolean UUrecipesGlass;
	public static boolean UUrecipesWater;
	public static boolean UUrecipesLava;
	public static boolean UUrecipesCocoa;
	public static boolean UUrecipesGunpowder;
	public static boolean UUrecipesGlowstoneBlock;
	public static boolean UUrecipesCactus;
	public static boolean UUrecipesSugarCane;
	public static boolean UUrecipesVine;
	public static boolean UUrecipesSnowBall;
	public static boolean UUrecipeslilypad;
	public static boolean UUrecipesBone;
	public static boolean UUrecipesFeather;
	public static boolean UUrecipesInk;
	public static boolean UUrecipesEnderPearl;
	public static boolean UUrecipesCoal;
	public static boolean UUrecipesIronOre;
	public static boolean UUrecipesIronDust;
	public static boolean UUrecipesGoldOre;
	public static boolean UUrecipesGoldDust;
	public static boolean UUrecipesRedStone;
	public static boolean UUrecipesLapis;
	public static boolean UUrecipesEmeraldOre;
	public static boolean UUrecipesEmerald;
	public static boolean UUrecipesDiamond;
	public static boolean UUrecipesResin;
	public static boolean UUrecipesTinDust;
	public static boolean UUrecipesCopperDust;
	public static boolean UUrecipesLeadDust;
	public static boolean UUrecipesPlatinumDust;
	public static boolean UUrecipesTungstenDust;
	public static boolean UUrecipesTitaniumDust;
	public static boolean UUrecipesAluminumDust;
	// Integration
	public static boolean AllowForestryRecipes;
	public static boolean AllowBOPRecipes;
	public static boolean AllowNaturaRecipes;
	// EMC

	// Client
	public static boolean ShowChargeHud;
	public static boolean useConnectedTextures;
	public static boolean oreUnifer;

	public static Configuration config;

	private ConfigTechReborn(File configFile) {
		config = new Configuration(configFile);
		config.load();

		ConfigTechReborn.Configs();

		config.save();

	}

	public static ConfigTechReborn initialize(File configFile) {

		if (instance == null)
			instance = new ConfigTechReborn(configFile);
		else
			throw new IllegalStateException("Cannot initialize TechReborn Config twice");

		return instance;
	}

	public static ConfigTechReborn instance() {
		if (instance == null) {

			throw new IllegalStateException("Instance of TechReborn Config requested before initialization");
		}
		return instance;
	}

	public static void Configs() {
		GalenaOreTrue = config.get(CATEGORY_WORLD, "Generate Galena Ore", true, "Allow GalenaOre to generate in world")
				.getBoolean(true);

		IridiumOreTrue = config
				.get(CATEGORY_WORLD, "Generate Iridium Ore", true, "Allow Iridium Ore to generate in world")
				.getBoolean(true);

		RubyOreTrue = config.get(CATEGORY_WORLD, "Generate Ruby Ore", true, "Allow Ruby Ore to generate in world")
				.getBoolean(true);

		SapphireOreTrue = config
				.get(CATEGORY_WORLD, "Generate Sapphire Ore", true, "Allow Sapphire Ore to generate in world")
				.getBoolean(true);

		BauxiteOreTrue = config
				.get(CATEGORY_WORLD, "Generate Bauxite Ore", true, "Allow Bauxite Ore to generate in world")
				.getBoolean(true);

		TetrahedriteOreTrue = config
				.get(CATEGORY_WORLD, "Generate Tetrahedrite Ore", true, "Allow Tetrahedrite Ore to generate in world")
				.getBoolean(true);

		CassiteriteOreTrue = config
				.get(CATEGORY_WORLD, "Generate Cassiterite Ore", true, "Allow Cassiterite Ore to generate in world")
				.getBoolean(true);

		LeadOreTrue = config.get(CATEGORY_WORLD, "Generate Lead Ore", true, "Allow Lead Ore to generate in world")
				.getBoolean(true);

		SilverOreTrue = config.get(CATEGORY_WORLD, "Generate Silver Ore", true, "Allow Silver Ore to generate in world")
				.getBoolean(true);

		PyriteOreTrue = config.get(CATEGORY_WORLD, "Generate Pyrite Ore", true, "Allow Pyrite Ore to generate in world")
				.getBoolean(true);

		CinnabarOreTrue = config
				.get(CATEGORY_WORLD, "Generate Cinnabar Ore", true, "Allow Cinnabar Ore to generate in world")
				.getBoolean(true);

		SphaleriteOreTrue = config
				.get(CATEGORY_WORLD, "Generate Sphalerite Ore", true, "Allow Sphalerite Ore to generate in world")
				.getBoolean(true);

		TungstenOreTrue = config
				.get(CATEGORY_WORLD, "Generate Tungsten Ore", true, "Allow Tungsten Ore to generate in world")
				.getBoolean(true);

		SheldoniteOreTrue = config
				.get(CATEGORY_WORLD, "Generate Sheldonite Ore", true, "Allow Sheldonite Ore to generate in world")
				.getBoolean(true);

		PeridotOreTrue = config
				.get(CATEGORY_WORLD, "Generate Peridot Ore", true, "Allow Peridot Ore to generate in world")
				.getBoolean(true);

		SodaliteOreTrue = config
				.get(CATEGORY_WORLD, "Sodalite Peridot Ore", true, "Allow Sodalite Ore to generate in world")
				.getBoolean(true);

		GalenaOreRare = config.get(CATEGORY_WORLD, "Galena Ore vein size", 8, "Set the max vein size for Galena Ore")
				.getInt();

		IridiumOreRare = config.get(CATEGORY_WORLD, "Iridium Ore vein size", 1, "Set the max vein size for Iridium Ore")
				.getInt();

		RubyOreRare = config.get(CATEGORY_WORLD, "Ruby Ore vein size", 6, "Set the max vein size for Ruby Ore")
				.getInt();

		SapphireOreRare = config
				.get(CATEGORY_WORLD, "Sapphire Ore vein size", 6, "Set the max vein size for Sapphire Ore").getInt();

		BauxiteOreRare = config.get(CATEGORY_WORLD, "Bauxite Ore vein size", 6, "Set the max vein size for Bauxite Ore")
				.getInt();

		PyriteOreRare = config.get(CATEGORY_WORLD, "Pyrite Ore vein size", 6, "Set the max vein size for Pyrite Ore")
				.getInt();

		CinnabarOreRare = config
				.get(CATEGORY_WORLD, "Cinnabar Ore vein size", 6, "Set the max vein size for Cinnabar Ore").getInt();

		SphaleriteOreRare = config
				.get(CATEGORY_WORLD, "Sphalerite Ore vein size", 6, "Set the max vein size for Sphalerite Ore")
				.getInt();

		TungstenOreRare = config
				.get(CATEGORY_WORLD, "Tungsten Ore vein size", 6, "Set the max vein size for Tungsten Ore").getInt();

		SheldoniteOreRare = config
				.get(CATEGORY_WORLD, "Sheldonite Ore vein size", 6, "Set the max vein size for Sheldonite Ore")
				.getInt();

		PeridotOreRare = config.get(CATEGORY_WORLD, "Peridot Ore vein size", 6, "Set the max vein size for Peridot Ore")
				.getInt();

		SodaliteOreRare = config
				.get(CATEGORY_WORLD, "Sodalite Ore vein size", 6, "Set the max vein size for Sodalite Ore").getInt();

		TetrahedriteOreRare = config
				.get(CATEGORY_WORLD, "Tetrahedrite Ore vein size", 6, "Set the max vein size for Tetrahedrite Ore")
				.getInt();

		CassiteriteOreRare = config
				.get(CATEGORY_WORLD, "Cassiterite Ore vein size", 6, "Set the max vein size for Cassiterite Ore")
				.getInt();

		LeadOreRare = config.get(CATEGORY_WORLD, "Lead Ore vein size", 6, "Set the max vein size for Lead Ore")
				.getInt();

		SilverOreRare = config.get(CATEGORY_WORLD, "Silver Ore vein size", 6, "Set the max vein size for Silver Ore")
				.getInt();

		FortuneSecondaryOreMultiplierPerLevel = config.get(CATEGORY_WORLD, "FortuneSecondaryOreMultiplierPerLevel", 0.5,
				"FortuneSecondaryOreMultiplierPerLevel").getDouble();

		// Power
		ThermalGenertaorOutput = config
				.get(CATEGORY_POWER, "ThermalGenerator Max Output", 30, "Set the max output for the ThermalGeneratot")
				.getInt();

		DragoneggsiphonerOutput = config
				.get(CATEGORY_POWER, "DragoneggSiphoner Max Output", 30, "Set the max output for the DragoneggSiphoner")
				.getInt();

		CentrifugeInputTick = config
				.get(CATEGORY_POWER, "Centrifuge Max Tick", 30, "Set the max power the Centrifuge uses per tick")
				.getInt();

		heatGeneratorOutput = config
				.get(CATEGORY_POWER, "HeatGenerator Max Output", 30, "Set the max output for the HeatGenerator")
				.getInt();

		aesuMaxOutput = config.get(CATEGORY_POWER, "AESU Max Output", 30, "Set the max output for the AESU").getInt();

		aesuMaxStorage = config.get(CATEGORY_POWER, "AESU Max Storage", 30, "Set the max Storage for the AESU")
				.getInt();


		// Charge
		AdvancedDrillCharge = config
				.get(CATEGORY_POWER, "AdvancedDrill MaxCharge", 60000, "Set the max charge for the advanced drill")
				.getInt();

		LapotronPackCharge = config
				.get(CATEGORY_POWER, "LapotronPack MaxCharge", 100000000, "Set the max charge for the LapotronPack")
				.getInt();

		LapotronicOrbMaxCharge = config
				.get(CATEGORY_POWER, "LapotronicOrb MaxCharge", 10000000, "Set the max charge for the LapotronicOrb")
				.getInt();

		LithiumBatpackCharge = config
				.get(CATEGORY_POWER, "LithiumBatpack MaxCharge", 4000000, "Set the max charge for the LithiumBatpack")
				.getInt();

		OmniToolCharge = config.get(CATEGORY_POWER, "OmniTool MaxCharge", 20000, "Set the max charge for the OmniTool")
				.getInt();

		RockCutterCharge = config
				.get(CATEGORY_POWER, "RockCutter MaxCharge", 10000, "Set the max charge for the RockCutter").getInt();

		CloakingDeviceCharge = config
				.get(CATEGORY_POWER, "CloakingDevice MaxCharge", 10000000, "Set the max charge for the CloakingDevice")
				.getInt();

		CentrifugeCharge = config
				.get(CATEGORY_POWER, "Centrifuge MaxCharge", 1000000, "Set the max charge for the Centrifuge").getInt();

		ThermalGeneratorCharge = config.get(CATEGORY_POWER, "ThermalGenerator MaxCharge", 1000000,
				"Set the max charge for the ThermalGenerator").getInt();

		aveargeEuOutTickTime = config.get(CATEGORY_POWER, "config.techreborn.aveargeEuOutTickTime", 100,
				"config.techreborn.aveargeEuOutTickTime.tooltip").getInt();

		lesuStoragePerBlock = config.get(CATEGORY_POWER, "LESU Storage Block Amount", 1000000,
				"The Amount of energy storage added per Storage block").getInt();

		baseLesuOutput = config.get(CATEGORY_POWER, "LESU base output", 16, "The output of the LESU befor upgrades")
				.getInt();

		extraOutputPerLesuBlock = config.get(CATEGORY_POWER, "Extra output on Storage Blocks", 8, "").getInt();

		farmEu = config.get(CATEGORY_POWER, "farmeu", 32, "").getInt();

		// Teir
		AdvancedDrillTier = config.get(CATEGORY_POWER, "AdvancedDrill Tier", 2, "Set the Tier of the advanced drill")
				.getInt();

		LapotronPackTier = config.get(CATEGORY_POWER, "LapotronPack Tier", 2, "Set the Tier of the LapotronPack")
				.getInt();

		LapotronicOrbTier = config.get(CATEGORY_POWER, "LapotronicOrb Tier", 2, "Set the Tier of the LapotronicOrb")
				.getInt();

		LithiumBatpackTier = config.get(CATEGORY_POWER, "LithiumBatpack Tier", 3, "Set the Tier of the LithiumBatpack")
				.getInt();

		OmniToolTier = config.get(CATEGORY_POWER, "OmniTool Tier", 3, "Set the Tier of the OmniTool").getInt();

		RockCutterTier = config.get(CATEGORY_POWER, "RockCutter Tier", 2, "Set the Tier of the RockCutter").getInt();

		CloakingDeviceTier = config.get(CATEGORY_POWER, "CloakingDevice Tier", 2, "Set the Tier of the CloakingDevice")
				.getInt();

		CloakingDeviceEUTick = config.get(CATEGORY_POWER, "CloakingDevice EUTick", 10000, "CloakingDevice EUTick")
				.getInt();

		CentrifugeTier = config.get(CATEGORY_POWER, "Centrifuge Tier", 1, "Set the Tier of the Centrifuge").getInt();

		ThermalGeneratorTier = config
				.get(CATEGORY_POWER, "ThermalGenerator Tier", 1, "Set the Tier of the ThermalGenerator").getInt();

		// Crafting
		ExpensiveMacerator = config
				.get(CATEGORY_CRAFTING, "Expensive Macerator", true, "Allow TechReborn to change the Macerator recipe")
				.getBoolean(true);

		ExpensiveDrill = config
				.get(CATEGORY_CRAFTING, "Expensive Drill", true, "Allow TechReborn to change the Drill recipe")
				.getBoolean(true);

		ExpensiveDiamondDrill = config.get(CATEGORY_CRAFTING, "Expensive DiamondDrill", true,
				"Allow TechReborn to change the DiamondDrill recipe").getBoolean(true);

		ExpensiveSolar = config.get(CATEGORY_CRAFTING, "Expensive Solarpanels", true,
				"Allow TechReborn to change the Solarpanels recipe").getBoolean(true);

		ExpensiveWatermill = config
				.get(CATEGORY_CRAFTING, "Expensive Watermill", true, "Allow TechReborn to change the Watermill recipe")
				.getBoolean(true);

		ExpensiveWindmill = config
				.get(CATEGORY_CRAFTING, "Expensive Windmill", true, "Allow TechReborn to change the Windmill recipe")
				.getBoolean(true);
		// Uu
		HideUuRecipes = config.get(CATEGORY_UU, "Hide UU Recipes", true, "Hide UU Recipes from JEI/NEI")
				.getBoolean(true);

		UUrecipesIridiamOre = config
				.get(CATEGORY_UU, "UUrecipe IridiamOre", true, "Allow IridiamOre to be crafted with UU")
				.getBoolean(true);

		UUrecipesWood = config.get(CATEGORY_UU, "UUrecipe Wood", true, "Allow Wood to be crafted with UU")
				.getBoolean(true);

		UUrecipesStone = config.get(CATEGORY_UU, "UUrecipe Stone", true, "Allow Stone to be crafted with UU")
				.getBoolean(true);

		UUrecipesSnowBlock = config
				.get(CATEGORY_UU, "UUrecipe SnowBlock", true, "Allow SnowBlock to be crafted with UU").getBoolean(true);

		UUrecipesGrass = config.get(CATEGORY_UU, "UUrecipe Grass", true, "Allow Grass to be crafted with UU")
				.getBoolean(true);

		UUrecipesObsidian = config.get(CATEGORY_UU, "UUrecipe Obsidian", true, "Allow Obsidian to be crafted with UU")
				.getBoolean(true);

		UUrecipesGlass = config.get(CATEGORY_UU, "UUrecipe Glass", true, "Allow Glass to be crafted with UU")
				.getBoolean(true);

		UUrecipesWater = config.get(CATEGORY_UU, "UUrecipe Water", true, "Allow Water to be crafted with UU")
				.getBoolean(true);

		UUrecipesLava = config.get(CATEGORY_UU, "UUrecipe Lava", true, "Allow Lava to be crafted with UU")
				.getBoolean(true);

		UUrecipesCocoa = config.get(CATEGORY_UU, "UUrecipe Cocoa", true, "Allow Cocoa to be crafted with UU")
				.getBoolean(true);

		UUrecipesGlowstoneBlock = config
				.get(CATEGORY_UU, "UUrecipe GlowstoneBlock", true, "Allow GlowstoneBlock to be crafted with UU")
				.getBoolean(true);

		UUrecipesCactus = config.get(CATEGORY_UU, "UUrecipe Cactus", true, "Allow Cactus to be crafted with UU")
				.getBoolean(true);

		UUrecipesSugarCane = config
				.get(CATEGORY_UU, "UUrecipe SugarCane", true, "Allow SugarCane to be crafted with UU").getBoolean(true);

		UUrecipesVine = config.get(CATEGORY_UU, "UUrecipe Vine", true, "Allow Vine to be crafted with UU")
				.getBoolean(true);

		UUrecipesSnowBall = config.get(CATEGORY_UU, "UUrecipe SnowBall", true, "Allow SnowBall to be crafted with UU")
				.getBoolean(true);

		UUrecipeslilypad = config.get(CATEGORY_UU, "UUrecipe lilypad", true, "Allow lilypad to be crafted with UU")
				.getBoolean(true);

		UUrecipesBone = config.get(CATEGORY_UU, "UUrecipe Bone", true, "Allow Bone to be crafted with UU")
				.getBoolean(true);

		UUrecipesFeather = config.get(CATEGORY_UU, "UUrecipe Feather", true, "Allow Feather to be crafted with UU")
				.getBoolean(true);

		UUrecipesInk = config.get(CATEGORY_UU, "UUrecipe Ink", true, "Allow Ink to be crafted with UU")
				.getBoolean(true);

		UUrecipesEnderPearl = config
				.get(CATEGORY_UU, "UUrecipe EnderPearl", true, "Allow EnderPearl to be crafted with UU")
				.getBoolean(true);

		UUrecipesCoal = config.get(CATEGORY_UU, "UUrecipe Coal", true, "Allow Coal to be crafted with UU")
				.getBoolean(true);

		UUrecipesIronOre = config.get(CATEGORY_UU, "UUrecipe IronOre", true, "Allow IronOre to be crafted with UU")
				.getBoolean(true);

		UUrecipesIronDust = config.get(CATEGORY_UU, "UUrecipe IronDust", true, "Allow IronDust to be crafted with UU")
				.getBoolean(true);

		UUrecipesGoldOre = config.get(CATEGORY_UU, "UUrecipe GoldOre", true, "Allow GoldOre to be crafted with UU")
				.getBoolean(true);

		UUrecipesGoldDust = config.get(CATEGORY_UU, "UUrecipe GoldDust", true, "Allow GoldDust to be crafted with UU")
				.getBoolean(true);

		UUrecipesRedStone = config.get(CATEGORY_UU, "UUrecipe RedStone", true, "Allow RedStone to be crafted with UU")
				.getBoolean(true);

		UUrecipesLapis = config.get(CATEGORY_UU, "UUrecipe Lapis", true, "Allow Lapis  to be crafted with UU")
				.getBoolean(true);

		UUrecipesEmeraldOre = config
				.get(CATEGORY_UU, "UUrecipe EmeraldOre", true, "Allow EmeraldOre  to be crafted with UU")
				.getBoolean(true);

		UUrecipesEmerald = config.get(CATEGORY_UU, "UUrecipe Emerald", true, "Allow Emerald  to be crafted with UU")
				.getBoolean(true);

		UUrecipesDiamond = config.get(CATEGORY_UU, "UUrecipe Diamond", true, "Allow Diamond  to be crafted with UU")
				.getBoolean(true);

		UUrecipesResin = config.get(CATEGORY_UU, "UUrecipe Resin", true, "Allow Resin  to be crafted with UU")
				.getBoolean(true);

		UUrecipesTinDust = config.get(CATEGORY_UU, "UUrecipe TinDust", true, "Allow TinDust  to be crafted with UU")
				.getBoolean(true);

		UUrecipesCopperDust = config
				.get(CATEGORY_UU, "UUrecipe CopperDust", true, "Allow CopperDust  to be crafted with UU")
				.getBoolean(true);

		UUrecipesLeadDust = config.get(CATEGORY_UU, "UUrecipe LeadDust", true, "Allow LeadDust  to be crafted with UU")
				.getBoolean(true);

		UUrecipesPlatinumDust = config
				.get(CATEGORY_UU, "UUrecipe PlatinumDust", true, "Allow PlatinumDust  to be crafted with UU")
				.getBoolean(true);

		UUrecipesTungstenDust = config
				.get(CATEGORY_UU, "UUrecipe TungstenDust", true, "Allow TungstenDust  to be crafted with UU")
				.getBoolean(true);

		UUrecipesTitaniumDust = config
				.get(CATEGORY_UU, "UUrecipe TitaniumDust", true, "Allow TitaniumDust  to be crafted with UU")
				.getBoolean(true);

		UUrecipesAluminumDust = config
				.get(CATEGORY_UU, "UUrecipe AluminumDust", true, "Allow AluminumDust  to be crafted with UU")
				.getBoolean(true);

		ShowChargeHud = config.get(CATEGORY_POWER, "Show Charge hud", true, "Show Charge hud (ClientSideOnly)")
				.getBoolean(true);

		useConnectedTextures = config.get(CATEGORY_INTEGRATION, "Render Conected Textures", true,
				"Render Conected Textures (Clinet Side Only)").getBoolean(true);

		oreUnifer = config.get(CATEGORY_INTEGRATION, "OreUnifer", false, "change all ores int TechReborn Ores")
				.getBoolean(false);

		// Integration
		AllowBOPRecipes = config.get(CATEGORY_INTEGRATION, "Allow Bop Recipes", true, "Add BOP suport")
				.getBoolean(true);

		AllowForestryRecipes = config.get(CATEGORY_INTEGRATION, "Allow Forestry Recipes", true, "Add Forestry suport")
				.getBoolean(true);

		AllowNaturaRecipes = config.get(CATEGORY_INTEGRATION, "Allow Natura Recipes", true, "Add Natura suport")
				.getBoolean(true);

		if (config.hasChanged()) {
			config.save();
		}
	}
}
