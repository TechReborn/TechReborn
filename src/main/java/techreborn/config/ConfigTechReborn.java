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

package techreborn.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigTechReborn {
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
	public static boolean disableRailcraftSteelNuggetRecipe;
	// Power

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
	public static boolean REMOVE_DUPLICATES;
	public static boolean ExpensiveQuarry;
	// Client
	public static boolean ShowChargeHud;

	public static boolean enableGemArmorAndTools;

	public static Configuration config;
	private static ConfigTechReborn instance = null;

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

		SteelJackhammerCharge = config.get(CATEGORY_POWER, "SteelJackhammer MaxCharge", 10000,
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

		REMOVE_DUPLICATES = config
			.get(CATEGORY_CRAFTING, "Remove Duplicates when IC2 is installed", false, "This attempts to fully integrate TR with ic2 recipes (Beta)")
			.getBoolean(false);

		ExpensiveQuarry = config
			.get(CATEGORY_CRAFTING, "Expensive Buildcraft quarry", true, "Change the buildcraft quarry recipe to require the diamond drill")
			.getBoolean(true);

		disableRailcraftSteelNuggetRecipe = config.get(CATEGORY_CRAFTING, "Disable Railcraft's Steel nugget recipe", false, "When true TechReborn will remove Railcrafts Iron Nugget to steel nuggert recipe.").getBoolean(false);

		ShowChargeHud = config.get(CATEGORY_POWER, "Show Charge hud", true, "Show Charge hud (ClientSideOnly)")
			.getBoolean(true);

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

		if (config.hasChanged()) {
			config.save();
		}
	}
}
