package techreborn.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigTechReborn
{
	public static String CATEGORY_WORLD = "world";
	public static String CATEGORY_POWER = "power";
	public static String CATEGORY_CRAFTING = "crafting";
	public static String CATEGORY_UU = "uu";
	public static String CATEGORY_EMC = "emc";
	public static String CATEGORY_INTEGRATION = "Integration";
	public static String CATEGORY_FEATURES = "Features";

	public static double FortuneSecondaryOreMultiplierPerLevel;
	public static boolean RubberSaplingLoot;
	public static boolean TinIngotsLoot;
	public static boolean CopperIngotsLoot;
	public static boolean SteelIngotsLoot;
	public static boolean UninsulatedElectocutionSound;
	public static boolean UninsulatedElectocutionParticle;
	public static boolean UninsulatedElectocutionDamage;
	public static boolean ScrapboxDispenser;
	// Power
	public static int ThermalGeneratorOutput;
	public static int CentrifugeInputTick;
	public static int DragonEggSiphonerOutput;
	public static int AverageEuOutTickTime;
	public static int ExtraOutputPerLesuBlock;
	public static int BaseLesuOutput;
	public static int LesuStoragePerBlock;
	public static int FarmEu;
	public static int AesuMaxOutput;
	public static int AesuMaxStorage;
	public static int pumpExtractEU;

	public static int LVTransformerMaxInput;
	public static int LVTransformerMaxOutput;
	public static int MVTransformerMaxInput;
	public static int MVTransformerMaxOutput;
	public static int HVTransformerMaxInput;
	public static int HVTransformerMaxOutput;
	// Charge
	public static int IronDrillCharge;
	public static int DiamondDrillCharge;
	public static int AdvancedDrillCharge;
	public static int IronChainsawCharge;
	public static int DiamondChainsawCharge;
	public static int AdvancedChainsawCharge;
	public static int SteelJackhammerCharge;
	public static int DiamondJackhammerCharge;
	public static int AdvancedJackhammerCharge;
	public static int LapotronPackCharge;
	public static int LithiumBatpackCharge;
	public static int LapotronicOrbMaxCharge;
	public static int OmniToolCharge;
	public static int RockCutterCharge;
	public static int CloakingDeviceCharge;
	public static int CentrifugeCharge;
	public static int ThermalGeneratorCharge;
	// Tier
	public static int IronDrillTier;
	public static int DiamondDrillTier;
	public static int AdvancedDrillTier;
	public static int IronChainsawTier;
	public static int DiamondChainsawTier;
	public static int AdvancedChainsawTier;
	public static int SteelJackhammerTier;
	public static int DiamondJackhammerTier;
	public static int AdvancedJackhammerTier;
	public static int LapotronPackTier;
	public static int LapotronicOrbTier;
	public static int LithiumBatpackTier;
	public static int OmniToolTier;
	public static int RockCutterTier;
	public static int CloakingDeviceTier;
	public static int CentrifugeTier;
	public static int ThermalGeneratorTier;

	public static boolean FreqTransmitterChat;
	public static boolean FreqTransmitterTooltip;
	public static boolean NanosaberChat;
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
	// Client
	public static boolean ShowChargeHud;
	// EMC
	public static boolean UseConnectedTextures;
	public static boolean OreUnifer;

	public static boolean enableGemArmorAndTools;

	public static Configuration config;
	private static ConfigTechReborn instance = null;

	private ConfigTechReborn(File configFile)
	{
		config = new Configuration(configFile);
		config.load();

		ConfigTechReborn.Configs();

		config.save();

	}

	public static ConfigTechReborn initialize(File configFile)
	{

		if (instance == null)
			instance = new ConfigTechReborn(configFile);
		else
			throw new IllegalStateException("Cannot initialize TechReborn Config twice");

		return instance;
	}

	public static ConfigTechReborn instance()
	{
		if (instance == null)
		{

			throw new IllegalStateException("Instance of TechReborn Config requested before initialization");
		}
		return instance;
	}

	public static void Configs()
	{
		RubberSaplingLoot = config
				.get(CATEGORY_WORLD, "Rubber Sapling Loot", true, "Allow Rubber Saplings to generate in loot chests")
				.getBoolean(true);

		CopperIngotsLoot = config
				.get(CATEGORY_WORLD, "Copper Ingots Loot", true, "Allow Copper Ingots to generate in loot chests")
				.getBoolean(true);

		TinIngotsLoot = config
				.get(CATEGORY_WORLD, "Tin Ingots Loot", true, "Allow Tin Ingots to generate in loot chests")
				.getBoolean(true);

		SteelIngotsLoot = config
				.get(CATEGORY_WORLD, "Steel Ingots Loot", true, "Allow Steel Ingots to generate in loot chests")
				.getBoolean(true);

		FortuneSecondaryOreMultiplierPerLevel = config.get(CATEGORY_WORLD, "FortuneSecondaryOreMultiplierPerLevel", 0.5,
				"FortuneSecondaryOreMultiplierPerLevel").getDouble();

		// Power
		ThermalGeneratorOutput = config
				.get(CATEGORY_POWER, "ThermalGenerator Max Output", 60, "Set the max output for the ThermalGenerator")
				.getInt();

		DragonEggSiphonerOutput = config
				.get(CATEGORY_POWER, "DragoneggSiphoner Max Output", 30, "Set the max output for the DragonEggSiphoner")
				.getInt();

		CentrifugeInputTick = config
				.get(CATEGORY_POWER, "Centrifuge Max Tick", 30, "Set the max power the Centrifuge uses per tick")
				.getInt();

		AesuMaxOutput = config.get(CATEGORY_POWER, "AESU Max Output", 30, "Set the max output for the AESU").getInt();

		AesuMaxStorage = config.get(CATEGORY_POWER, "AESU Max Storage", 30, "Set the max Storage for the AESU")
				.getInt();

		pumpExtractEU = config.get(CATEGORY_POWER, "Pump extract eu", 20, "How mutch eu should the pump use to extract the fluid")
				.getInt();

		//Transformers
		LVTransformerMaxInput = config.get(CATEGORY_POWER, "LV Transformer Max Input", 128, "Set the max input for the LV Trasnformer")
				.getInt();
		LVTransformerMaxOutput = config.get(CATEGORY_POWER, "LV Transformer Max Output", 32, "Set the max output for the LV Trasnformer")
				.getInt();
		MVTransformerMaxInput = config.get(CATEGORY_POWER, "MV Transformer Max Input", 512, "Set the max input for the MV Trasnformer")
				.getInt();
		MVTransformerMaxOutput = config.get(CATEGORY_POWER, "MV Transformer Max Output", 128, "Set the max output for the MV Trasnformer")
				.getInt();
		HVTransformerMaxInput = config.get(CATEGORY_POWER, "HV Transformer Max Input", 2048, "Set the max input for the HV Trasnformer")
				.getInt();
		HVTransformerMaxOutput = config.get(CATEGORY_POWER, "HV Transformer Max Output", 512, "Set the max output for the HV Trasnformer")
				.getInt();
		
		// Charge
		IronDrillCharge = config
				.get(CATEGORY_POWER, "IronDrill MaxCharge", 10000, "Set the max charge for the iron drill").getInt();

		DiamondDrillCharge = config
				.get(CATEGORY_POWER, "DiamondDrill MaxCharge", 100000, "Set the max charge for the diamond drill")
				.getInt();

		AdvancedDrillCharge = config
				.get(CATEGORY_POWER, "AdvancedDrill MaxCharge", 1000000, "Set the max charge for the advanced drill")
				.getInt();

		IronChainsawCharge = config
				.get(CATEGORY_POWER, "IronChainsaw MaxCharge", 10000, "Set the max charge for the iron chainsaw")
				.getInt();

		DiamondChainsawCharge = config
				.get(CATEGORY_POWER, "DiamondChainsaw MaxCharge", 100000, "Set the max charge for the diamond chainsaw")
				.getInt();

		AdvancedChainsawCharge = config.get(CATEGORY_POWER, "AdvancedChainsaw MaxCharge", 1000000,
				"Set the max charge for the advanced chainsaw").getInt();

		AdvancedJackhammerCharge = config
				.get(CATEGORY_POWER, "AdvancedJackhammer MaxCharge", 100000, "Set the max charge for the advanced jackhammer")
				.getInt();

		SteelJackhammerCharge = config.get(CATEGORY_POWER, "AdvancedJackhammer MaxCharge", 10000,
				"Set the max charge for the steel jackhammer").getInt();

		DiamondJackhammerCharge = config.get(CATEGORY_POWER, "DiamondJackhammer MaxCharge", 20000,
				"Set the max charge for the diamond jackhammer").getInt();

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

		AverageEuOutTickTime = config.get(CATEGORY_POWER, "config.techreborn.AverageEuOutTickTime", 100,
				"config.techreborn.AverageEuOutTickTime.tooltip").getInt();

		LesuStoragePerBlock = config.get(CATEGORY_POWER, "LESU Storage Block Amount", 1000000,
				"The Amount of energy storage added per Storage block").getInt();

		BaseLesuOutput = config.get(CATEGORY_POWER, "LESU base output", 16, "The output of the LESU befor upgrades")
				.getInt();

		ExtraOutputPerLesuBlock = config.get(CATEGORY_POWER, "Extra output on Storage Blocks", 8, "").getInt();

		FarmEu = config.get(CATEGORY_POWER, "farmeu", 32, "").getInt();

		// Tier
		IronDrillTier = config.get(CATEGORY_POWER, "IronDrill Tier", 2, "Set the Tier of the iron drill").getInt();

		DiamondDrillTier = config.get(CATEGORY_POWER, "DiamondDrill Tier", 2, "Set the Tier of the diamond drill")
				.getInt();

		AdvancedDrillTier = config.get(CATEGORY_POWER, "AdvancedDrill Tier", 2, "Set the Tier of the advanced drill")
				.getInt();

		IronChainsawTier = config.get(CATEGORY_POWER, "IronChainsaw Tier", 2, "Set the Tier of the iron chainsaw")
				.getInt();

		DiamondChainsawTier = config
				.get(CATEGORY_POWER, "DiamondChainsaw Tier", 2, "Set the Tier of the diamond chainsaw").getInt();

		AdvancedChainsawTier = config
				.get(CATEGORY_POWER, "AdvancedChainsaw Tier", 2, "Set the Tier of the advanced chainsaw").getInt();

		AdvancedJackhammerTier = config.get(CATEGORY_POWER, "AdvancedJackhammer Tier", 2, "Set the Tier of the advanced jackhammer")
				.getInt();

		DiamondJackhammerTier = config
				.get(CATEGORY_POWER, "DiamondJackhammer Tier", 2, "Set the Tier of the diamond jackhammer").getInt();

		SteelJackhammerTier = config
				.get(CATEGORY_POWER, "AdvancedJackhammer Tier", 2, "Set the Tier of the advanced jackhammer").getInt();

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

		//Features
		FreqTransmitterChat = config
				.get(CATEGORY_FEATURES, "Frequency Transmitter Chat messages", true, "Allow Frequency Transmitter chat messages")
				.getBoolean(true);

		FreqTransmitterTooltip = config
				.get(CATEGORY_FEATURES, "Frequency Transmitter tooltips", true, "Allow Frequency Transmitter to display tooltip info")
				.getBoolean(true);

		NanosaberChat = config
				.get(CATEGORY_FEATURES, "Nanosaber Chat messages", true, "Allow Nanosaber chat messages")
				.getBoolean(true);

		enableGemArmorAndTools = config.get(CATEGORY_FEATURES, "Gem tools and armor", true, "Should the gem tools and armor be added to the game").getBoolean(true);

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
				.get(CATEGORY_UU, "UUrecipe IridiamOre", true, "Allow Iridium Ore to be crafted with UU")
				.getBoolean(true);

		UUrecipesWood = config.get(CATEGORY_UU, "UUrecipe Wood", true, "Allow Wood to be crafted with UU")
				.getBoolean(true);

		UUrecipesStone = config.get(CATEGORY_UU, "UUrecipe Stone", true, "Allow Stone to be crafted with UU")
				.getBoolean(true);

		UUrecipesSnowBlock = config
				.get(CATEGORY_UU, "UUrecipe SnowBlock", true, "Allow Snow Blocks to be crafted with UU").getBoolean(true);

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
				.get(CATEGORY_UU, "UUrecipe GlowstoneBlock", true, "Allow Glowstone Blocks to be crafted with UU")
				.getBoolean(true);

		UUrecipesCactus = config.get(CATEGORY_UU, "UUrecipe Cactus", true, "Allow Cactus to be crafted with UU")
				.getBoolean(true);

		UUrecipesSugarCane = config
				.get(CATEGORY_UU, "UUrecipe SugarCane", true, "Allow SugarCane to be crafted with UU").getBoolean(true);

		UUrecipesVine = config.get(CATEGORY_UU, "UUrecipe Vine", true, "Allow Vines to be crafted with UU")
				.getBoolean(true);

		UUrecipesSnowBall = config.get(CATEGORY_UU, "UUrecipe SnowBall", true, "Allow SnowBall to be crafted with UU")
				.getBoolean(true);

		UUrecipeslilypad = config.get(CATEGORY_UU, "UUrecipe lilypad", true, "Allow lilypads to be crafted with UU")
				.getBoolean(true);

		UUrecipesBone = config.get(CATEGORY_UU, "UUrecipe Bone", true, "Allow Bones to be crafted with UU")
				.getBoolean(true);

		UUrecipesFeather = config.get(CATEGORY_UU, "UUrecipe Feather", true, "Allow Feathers to be crafted with UU")
				.getBoolean(true);

		UUrecipesInk = config.get(CATEGORY_UU, "UUrecipe Ink", true, "Allow Ink to be crafted with UU")
				.getBoolean(true);

		UUrecipesEnderPearl = config
				.get(CATEGORY_UU, "UUrecipe EnderPearl", true, "Allow EnderPearls to be crafted with UU")
				.getBoolean(true);

		UUrecipesCoal = config.get(CATEGORY_UU, "UUrecipe Coal", true, "Allow Coal to be crafted with UU")
				.getBoolean(true);

		UUrecipesIronOre = config.get(CATEGORY_UU, "UUrecipe IronOre", true, "Allow Iron Ore to be crafted with UU")
				.getBoolean(true);

		UUrecipesIronDust = config.get(CATEGORY_UU, "UUrecipe IronDust", true, "Allow Iron Dust to be crafted with UU")
				.getBoolean(true);

		UUrecipesGoldOre = config.get(CATEGORY_UU, "UUrecipe GoldOre", true, "Allow Gold Ore to be crafted with UU")
				.getBoolean(true);

		UUrecipesGoldDust = config.get(CATEGORY_UU, "UUrecipe GoldDust", true, "Allow Gold Dust to be crafted with UU")
				.getBoolean(true);

		UUrecipesRedStone = config.get(CATEGORY_UU, "UUrecipe RedStone", true, "Allow Redstone to be crafted with UU")
				.getBoolean(true);

		UUrecipesLapis = config.get(CATEGORY_UU, "UUrecipe Lapis", true, "Allow Lapis to be crafted with UU")
				.getBoolean(true);

		UUrecipesEmeraldOre = config
				.get(CATEGORY_UU, "UUrecipe EmeraldOre", true, "Allow EmeraldOre to be crafted with UU")
				.getBoolean(true);

		UUrecipesEmerald = config.get(CATEGORY_UU, "UUrecipe Emerald", true, "Allow Emerald to be crafted with UU")
				.getBoolean(true);

		UUrecipesDiamond = config.get(CATEGORY_UU, "UUrecipe Diamond", true, "Allow Diamond to be crafted with UU")
				.getBoolean(true);

		UUrecipesResin = config.get(CATEGORY_UU, "UUrecipe Resin", true, "Allow Resin to be crafted with UU")
				.getBoolean(true);

		UUrecipesTinDust = config.get(CATEGORY_UU, "UUrecipe TinDust", true, "Allow TinDust to be crafted with UU")
				.getBoolean(true);

		UUrecipesCopperDust = config
				.get(CATEGORY_UU, "UUrecipe CopperDust", true, "Allow CopperDust to be crafted with UU")
				.getBoolean(true);

		UUrecipesLeadDust = config.get(CATEGORY_UU, "UUrecipe LeadDust", true, "Allow LeadDust to be crafted with UU")
				.getBoolean(true);

		UUrecipesPlatinumDust = config
				.get(CATEGORY_UU, "UUrecipe PlatinumDust", true, "Allow PlatinumDust to be crafted with UU")
				.getBoolean(true);

		UUrecipesTungstenDust = config
				.get(CATEGORY_UU, "UUrecipe TungstenDust", true, "Allow TungstenDust to be crafted with UU")
				.getBoolean(true);

		UUrecipesTitaniumDust = config
				.get(CATEGORY_UU, "UUrecipe TitaniumDust", true, "Allow TitaniumDust to be crafted with UU")
				.getBoolean(true);

		UUrecipesAluminumDust = config
				.get(CATEGORY_UU, "UUrecipe AluminumDust", true, "Allow AluminumDust to be crafted with UU")
				.getBoolean(true);

		ShowChargeHud = config.get(CATEGORY_POWER, "Show Charge hud", true, "Show Charge hud (ClientSideOnly)")
				.getBoolean(true);

		UseConnectedTextures = config.get(CATEGORY_INTEGRATION, "Render Conected Textures", true,
				"Render Conected Textures (Clinet Side Only)").getBoolean(true);

		OreUnifer = config.get(CATEGORY_INTEGRATION, "OreUnifer", false, "Change all ores into TechReborn Ores")
				.getBoolean(false);

		UninsulatedElectocutionDamage = config.get(CATEGORY_WORLD, "Uninsulated Electrocution Damage", true,
				"Damage entities on contact with uninsulated cables").getBoolean(true);
		UninsulatedElectocutionSound = config.get(CATEGORY_WORLD, "Uninsulated Electrocution Sound", true,
				"Play sound on contact with uninsulated cables").getBoolean(true);
		UninsulatedElectocutionParticle = config.get(CATEGORY_WORLD, "Uninsulated Electrocution Particle", true,
				"Spawn particles on contact with uninsulated cables").getBoolean(true);

		ScrapboxDispenser = config
				.get(CATEGORY_WORLD, "Scrapboxes in Dispenser", true, "Allow scrapboxes to be opened via dispenser")
				.getBoolean(true);

		ExpensiveWatermill = config
				.get(CATEGORY_CRAFTING, "Expensive Watermill", true, "Allow TechReborn to change the Watermill recipe")
				.getBoolean(true);

		// Integration
		AllowBOPRecipes = config.get(CATEGORY_INTEGRATION, "Allow Bop Recipes", true, "Add BOP suport")
				.getBoolean(true);

		AllowForestryRecipes = config.get(CATEGORY_INTEGRATION, "Allow Forestry Recipes", true, "Add Forestry suport")
				.getBoolean(true);

		AllowNaturaRecipes = config.get(CATEGORY_INTEGRATION, "Allow Natura Recipes", true, "Add Natura suport")
				.getBoolean(true);

		if (config.hasChanged())
		{
			config.save();
		}
	}
}