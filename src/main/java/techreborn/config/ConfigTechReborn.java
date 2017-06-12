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
import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.lib.ModInfo;

import java.io.File;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class ConfigTechReborn {
	public static String CATEGORY_WORLD = "world";
	public static String CATEGORY_POWER = "power";
	public static String CATEGORY_CRAFTING = "crafting";
	public static String CATEGORY_UU = "uu";
	public static String CATEGORY_EMC = "emc";
	public static String CATEGORY_INTEGRATION = "Integration";
	public static String CATEGORY_FEATURES = "Features";

	@ConfigRegistry(config = "recipes", category = "railcraft", key = "disableRailcraftNugget", comment = "When true TechReborn will remove Railcrafts Iron Nugget to steel nuggert recipe.")
	public static boolean disableRailcraftSteelNuggetRecipe = false;
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

	// Crafting
	@ConfigRegistry(config = "recipes", category = "ic2", key = "deduplicate", comment = "Changes a lot of reipes and hides blocks to intrgrade TechReborn into IC2")
	public static boolean REMOVE_DUPLICATES = false;
	// Client
	@ConfigRegistry(config = "client", category = "hud", key = "showChargeHud", comment = "Show the charge hud")
	public static boolean ShowChargeHud = true;

	@ConfigRegistry(config = "misc", category = "general", key = "enableGemTools", comment = "Enable Gem armor and tools")
	public static boolean enableGemArmorAndTools = true;

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


		if (config.hasChanged()) {
			config.save();
		}
	}
}
