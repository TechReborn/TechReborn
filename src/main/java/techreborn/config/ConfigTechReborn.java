package techreborn.config;

import java.io.File;

import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;

public class ConfigTechReborn {
	private static ConfigTechReborn instance = null;
	public static String CATEGORY_WORLD = "world";
	public static String CATEGORY_POWER = "power";
	public static String CATEGORY_CRAFTING = "crafting";
	public static String CATEGORY_UU = "uu";

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

	// Power
	public static int ThermalGenertaorOutput;
	public static int CentrifugeInputTick;
	public static int DragoneggsiphonerOutput;
	public static int heatGeneratorOutput;
	// Charge
	public static int AdvancedDrillCharge;
	public static int LapotronPackCharge;
	public static int LithiumBatpackCharge;
	public static int OmniToolCharge;
	public static int RockCutterCharge;
	public static int GravityCharge;
	public static int CentrifugeCharge;
	public static int ThermalGeneratorCharge;
	// Tier
	public static int AdvancedDrillTier;
	public static int LapotronPackTier;
	public static int LithiumBatpackTier;
	public static int OmniToolTier;
	public static int RockCutterTier;
	public static int GravityTier;
	public static int CentrifugeTier;
	public static int ThermalGeneratorTier;
	// Crafting
	public static boolean ExpensiveMacerator;
	public static boolean ExpensiveDrill;
	public static boolean ExpensiveDiamondDrill;
	public static boolean ExpensiveSolar;

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
	// TODO
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

	public static Configuration config;

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
			throw new IllegalStateException(
					"Cannot initialize TechReborn Config twice");

		return instance;
	}

	public static ConfigTechReborn instance()
	{
		if (instance == null)
		{

			throw new IllegalStateException(
					"Instance of TechReborn Config requested before initialization");
		}
		return instance;
	}

	public static void Configs()
	{
		GalenaOreTrue = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.allow.galenaOre"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.galenaOre.tooltip"))
				.getBoolean(true);
		IridiumOreTrue = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.allow.iridiumOre"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.iridiumOre.tooltip"))
				.getBoolean(true);
		RubyOreTrue = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.allow.rubyOre"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.rubyOre.tooltip"))
				.getBoolean(true);
		SapphireOreTrue = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.allow.sapphireOre"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.sapphireOre.tooltip"))
				.getBoolean(true);
		BauxiteOreTrue = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.allow.bauxiteOre"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.bauxiteOre.tooltip"))
				.getBoolean(true);
		TetrahedriteOreTrue = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.allow.copperOre"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.copperOre.tooltip"))
				.getBoolean(true);
		CassiteriteOreTrue = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.allow.tinOre"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.tinOre.tooltip"))
				.getBoolean(true);
		LeadOreTrue = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.allow.leadOre"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.leadOre.tooltip"))
				.getBoolean(true);
		SilverOreTrue = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.allow.silverOre"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.silverOre.tooltip"))
				.getBoolean(true);
		PyriteOreTrue = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.allow.pyriteOre"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.pyriteOre.tooltip"))
				.getBoolean(true);
		CinnabarOreTrue = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.allow.cinnabarOre"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.cinnabarOre.tooltip"))
				.getBoolean(true);
		SphaleriteOreTrue = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.allow.sphaleriteOre"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.sphaleriteOre.tooltip"))
				.getBoolean(true);
		TungstenOreTrue = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.allow.tungstonOre"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.tungstonOre.tooltip"))
				.getBoolean(true);
		SheldoniteOreTrue = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.allow.sheldoniteOre"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.sheldoniteOre.tooltip"))
				.getBoolean(true);
		PeridotOreTrue = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.allow.olivineOre"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.olivineOre.tooltip"))
				.getBoolean(true);
		SodaliteOreTrue = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.allow.sodaliteOre"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.sodaliteOre.tooltip"))
				.getBoolean(true);
		GalenaOreRare = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.galenaOre.rare"),
						8,
						StatCollector
								.translateToLocal("config.techreborn.galenaOre.rare.tooltip"))
				.getInt();
		IridiumOreRare = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.iridiumOre.rare"),
						1,
						StatCollector
								.translateToLocal("config.techreborn.iridiumOre.rare.tooltip"))
				.getInt();

		RubyOreRare = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.rubyOre.rare"),
						6,
						StatCollector
								.translateToLocal("config.techreborn.rubyOre.rare.tooltip"))
				.getInt();

		SapphireOreRare = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.sapphireOre.rare"),
						6,
						StatCollector
								.translateToLocal("config.techreborn.sapphireOre.rare.tooltip"))
				.getInt();

		BauxiteOreRare = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.bauxiteOre.rare"),
						6,
						StatCollector
								.translateToLocal("config.techreborn.bauxiteOre.rare.tooltip"))
				.getInt();

		PyriteOreRare = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.pyriteOre.rare"),
						6,
						StatCollector
								.translateToLocal("config.techreborn.pyriteOre.rare.tooltip"))
				.getInt();

		CinnabarOreRare = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.cinnabarOre.rare"),
						6,
						StatCollector
								.translateToLocal("config.techreborn.cinnabarOre.rare.tooltip"))
				.getInt();

		SphaleriteOreRare = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.sphaleriteOre.rare"),
						6,
						StatCollector
								.translateToLocal("config.techreborn.sphaleriteOre.rare.tooltip"))
				.getInt();

		TungstenOreRare = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.tungstenOre.rare"),
						6,
						StatCollector
								.translateToLocal("config.techreborn.tungstenOre.rare.tooltip"))
				.getInt();

		SheldoniteOreRare = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.sheldoniteOre.rare"),
						6,
						StatCollector
								.translateToLocal("config.techreborn.sheldoniteOre.rare.tooltip"))
				.getInt();

		PeridotOreRare = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.olivineOre.rare"),
						6,
						StatCollector
								.translateToLocal("config.techreborn.olivineOre.rare.tooltip"))
				.getInt();

		SodaliteOreRare = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.sodaliteOre.rare"),
						6,
						StatCollector
								.translateToLocal("config.techreborn.sodaliteOre.rare.tooltip"))
				.getInt();

		TetrahedriteOreRare = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.copperOre.rare"),
						6,
						StatCollector
								.translateToLocal("config.techreborn.copperOre.rare.tooltip"))
				.getInt();

		CassiteriteOreRare = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.tinOre.rare"),
						6,
						StatCollector
								.translateToLocal("config.techreborn.tinOre.rare.tooltip"))
				.getInt();

		LeadOreRare = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.leadOre.rare"),
						6,
						StatCollector
								.translateToLocal("config.techreborn.leadOre.rare.tooltip"))
				.getInt();

		SilverOreRare = config
				.get(CATEGORY_WORLD,
						StatCollector
								.translateToLocal("config.techreborn.silverOre.rare"),
						6,
						StatCollector
								.translateToLocal("config.techreborn.silverOre.rare.tooltip"))
				.getInt();

		// Power
		ThermalGenertaorOutput = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.thermalGeneratorPower"),
						30,
						StatCollector
								.translateToLocal("config.techreborn.thermalGeneratorPower.tooltip"))
								
				.getInt();
		DragoneggsiphonerOutput = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.dragoneggsiphonerPower"),
						30,
						StatCollector
								.translateToLocal("config.techreborn.dragoneggsiphonerPower.tooltip"))
								
				.getInt();
		CentrifugeInputTick = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.centrifugePowerUsage"),
						5,
						StatCollector
								.translateToLocal("config.techreborn.centrifugePowerUsage.tooltip"))
				.getInt();
		heatGeneratorOutput = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.heatGeneratorOutput"),
						1,
						StatCollector
								.translateToLocal("config.techreborn.heatGeneratorOutput.tooltip"))
								
				.getInt();

		// Charge
		AdvancedDrillCharge = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.advancedDrillMaxCharge"),
						60000,
						StatCollector
								.translateToLocal("config.techreborn.advancedDrillMaxCharge.tooltip"))
				.getInt();
		LapotronPackCharge = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.lapotronPackMaxCharge"),
						100000000,
						StatCollector
								.translateToLocal("config.techreborn.lapotronPackMaxCharge.tooltop"))
				.getInt();
		LithiumBatpackCharge = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.lithiumBatpackMaxCharge"),
						4000000,
						StatCollector
								.translateToLocal("config.techreborn.lithiumBatpackMaxCharge.tooltip"))
				.getInt();
		OmniToolCharge = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.omniToolMaxCharge"),
						20000,
						StatCollector
								.translateToLocal("config.techreborn.omniToolMaxCharge.tooltip"))
				.getInt();
		RockCutterCharge = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.rockCutterMaxCharge"),
						10000,
						StatCollector
								.translateToLocal("config.techreborn.rockCutterMaxCharge.tooltip"))
				.getInt();
		GravityCharge = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.gravityChestplateMaxCharge"),
						100000,
						StatCollector
								.translateToLocal("config.techreborn.gravityChestplateMaxCharge.tooltip"))
				.getInt();
		CentrifugeCharge = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.centrifugeMaxCharge"),
						1000000,
						StatCollector
								.translateToLocal("config.techreborn.centrifugeMaxCharge.tooltip"))
				.getInt();
		ThermalGeneratorCharge = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.thermalGeneratorMaxCharge"),
						1000000,
						StatCollector
								.translateToLocal("config.techreborn.thermalGeneratorMaxCharge.tooltip"))
				.getInt();

		// Teir
		AdvancedDrillTier = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.advancedDrillTier"),
						2,
						StatCollector
								.translateToLocal("config.techreborn.advancedDrillTier.tooltip"))
				.getInt();
		LapotronPackTier = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.lapotronPackTier"),
						2,
						StatCollector
								.translateToLocal("config.techreborn.lapotronPackTier.tooltip"))
				.getInt();
		LithiumBatpackTier = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.lithiumBatpackTier"),
						3,
						StatCollector
								.translateToLocal("config.techreborn.lithiumBatpackTier.tooltip"))
				.getInt();
		OmniToolTier = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.omniToolTier"),
						3,
						StatCollector
								.translateToLocal("config.techreborn.omniToolTier.tooltip"))
				.getInt();
		RockCutterTier = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.rockCutterTier"),
						3,
						StatCollector
								.translateToLocal("config.techreborn.rockCutterTier.tooltip"))
				.getInt();
		GravityTier = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.gravityChestplateTier"),
						3,
						StatCollector
								.translateToLocal("config.techreborn.gravityChestplateTier.tooltip"))
				.getInt();
		CentrifugeTier = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.centrifugeTier"),
						1,
						StatCollector
								.translateToLocal("config.techreborn.centrifugeTier.tooltip"))
				.getInt();
		ThermalGeneratorTier = config
				.get(CATEGORY_POWER,
						StatCollector
								.translateToLocal("config.techreborn.thermalGeneratorTier"),
						1,
						StatCollector
								.translateToLocal("config.techreborn.thermalGeneratorTier.tooltip"))
				.getInt();

		// Crafting
		ExpensiveMacerator = config
				.get(CATEGORY_CRAFTING,
						StatCollector
								.translateToLocal("config.techreborn.allowExpensiveMacerator"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allowExpensiveMacerator.tooltip"))
				.getBoolean(true);
		ExpensiveDrill = config
				.get(CATEGORY_CRAFTING,
						StatCollector
								.translateToLocal("config.techreborn.allowExpensiveDrill"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allowExpensiveDrill.tooltip"))
				.getBoolean(true);
		ExpensiveDiamondDrill = config
				.get(CATEGORY_CRAFTING,
						StatCollector
								.translateToLocal("config.techreborn.allowExpensiveDiamondDrill"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allowExpensiveDiamondDrill.tooltip"))
				.getBoolean(true);
		ExpensiveSolar = config
				.get(CATEGORY_CRAFTING,
						StatCollector
								.translateToLocal("config.techreborn.allowExpensiveSolarPanels"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allowExpensiveSolarPanels.tooltip"))
				.getBoolean(true);
		// Uu
		HideUuRecipes = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.hiderecipes"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.hiderecipes.tooltip"))
				.getBoolean(true);
		UUrecipesIridiamOre = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesIridiamOre"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesIridiamOre.tooltip"))
				.getBoolean(true);
		UUrecipesWood = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesWood"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesWood.tooltip"))
				.getBoolean(true);

		UUrecipesStone = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesStone"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesStone.tooltip"))
				.getBoolean(true);

		UUrecipesSnowBlock = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesSnowBlock"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesSnowBlock.tooltip"))
				.getBoolean(true);

		UUrecipesGrass = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesGrass"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesGrass.tooltip"))
				.getBoolean(true);

		UUrecipesObsidian = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesObsidian"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesObsidian.tooltip"))
				.getBoolean(true);

		UUrecipesGlass = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesGlass"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesGlass.tooltip"))
				.getBoolean(true);

		UUrecipesWater = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesWater"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesWater.tooltip"))
				.getBoolean(true);

		UUrecipesLava = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesLava"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesLava.tooltip"))
				.getBoolean(true);

		UUrecipesCocoa = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesCocoa"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesCocoa.tooltip"))
				.getBoolean(true);

		UUrecipesGlowstoneBlock = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesGlowstoneBlock"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesGlowstoneBlock.tooltip"))
				.getBoolean(true);

		UUrecipesCactus = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesCactus"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesCactus.tooltip"))
				.getBoolean(true);
		UUrecipesSugarCane = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesSugarCane"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesSugarCane.tooltip"))
				.getBoolean(true);

		UUrecipesVine = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesVine"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesVine.tooltip"))
				.getBoolean(true);

		UUrecipesSnowBall = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesSnowBall"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesSnowBall.tooltip"))
				.getBoolean(true);

		UUrecipeslilypad = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesLilypad"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesLilypad.tooltip"))
				.getBoolean(true);

		UUrecipesBone = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesBone"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesBone.tooltip"))
				.getBoolean(true);

		UUrecipesFeather = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesFeather"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesFeather.tooltip"))
				.getBoolean(true);

		UUrecipesInk = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesInk"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesInk.tooltip"))
				.getBoolean(true);

		UUrecipesEnderPearl = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesEnderPearl"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesEnderPearl.tooltip"))
				.getBoolean(true);

		UUrecipesGunpowder = config
				.get(CATEGORY_UU,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesGunPowder"),
						true,
						StatCollector
								.translateToLocal("config.techreborn.allow.uurecipesGunPowder.tooltip"))
				.getBoolean(true);

		if (config.hasChanged())
			config.save();
	}

}
