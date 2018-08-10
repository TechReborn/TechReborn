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

package techreborn.config;

import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;
import techreborn.lib.ModInfo;

@RebornRegistry(modID = ModInfo.MOD_ID)
public class ConfigTechReborn {
	@ConfigRegistry(config = "recipes", category = "railcraft", key = "disableRailcraftNugget", comment = "When true TechReborn will remove Railcraft's Iron Nugget to steel nugget recipe.")
	public static boolean disableRailcraftSteelNuggetRecipe = false;

	@ConfigRegistry(config = "recipes", category = "ic2", key = "deduplicate", comment = "Changes a lot of recipes and hides blocks to integrate TechReborn into IC2")
	public static boolean REMOVE_DUPLICATES = false;

	@ConfigRegistry(config = "misc", category = "general", key = "enableGemTools", comment = "Enable Gem armor and tools")
	public static boolean enableGemArmorAndTools = true;

	@ConfigRegistry(config = "items", category = "power", key = "nanoSaberCharge", comment = "Energy Capacity for Nano Saber (FE)")
	public static int nanoSaberCharge = 640_000;
	
	@ConfigRegistry(config = "items", category = "power", key = "ironDrillCharge", comment = "Energy Capacity for Iron Drill (FE)")
	public static int IronDrillCharge = 40_000;

	@ConfigRegistry(config = "items", category = "power", key = "diamondDrillCharge", comment = "Energy Capacity for Diamond Drill (FE)")
	public static int DiamondDrillCharge = 400_000;

	@ConfigRegistry(config = "items", category = "power", key = "advancedDrillCharge", comment = "Energy Capacity for Advanced Drill (FE)")
	public static int AdvancedDrillCharge = 4_000_000;

	@ConfigRegistry(config = "items", category = "power", key = "ironChainsawCharge", comment = "Energy Capacity for Iron Chainsaw (FE)")
	public static int IronChainsawCharge = 40_000;

	@ConfigRegistry(config = "items", category = "power", key = "diamondChainsawCharge", comment = "Energy Capacity for Diamond Chainsaw (FE)")
	public static int DiamondChainsawCharge = 400_000;

	@ConfigRegistry(config = "items", category = "power", key = "advancedChainsawCharge", comment = "Energy Capacity for Advanced Chainsaw (FE)")
	public static int AdvancedChainsawCharge = 4_000_000;

	@ConfigRegistry(config = "items", category = "power", key = "steelJackhammerCharge", comment = "Energy Capacity for Steel Jackhammer (FE)")
	public static int SteelJackhammerCharge = 40_000;

	@ConfigRegistry(config = "items", category = "power", key = "diamondJackhammerCharge", comment = "Energy Capacity for Diamond Jackhammer (FE)")
	public static int DiamondJackhammerCharge = 400_000;

	@ConfigRegistry(config = "items", category = "power", key = "advancedJackhammerCharge", comment = "Energy Capacity for Advanced Jachammer (FE)")
	public static int AdvancedJackhammerCharge = 4_000_000;

	@ConfigRegistry(config = "items", category = "power", key = "omniToolCharge", comment = "Energy Capacity for Omni Tool (FE)")
	public static int OmniToolCharge = 20_000;

	@ConfigRegistry(config = "items", category = "power", key = "rockCutterCharge", comment = "Energy Capacity for Rock Cutter (FE)")
	public static int RockCutterCharge = 400_000;

	@ConfigRegistry(config = "items", category = "power", key = "lapotronPackCharge", comment = "Energy Capacity for Lapotron Pack (FE)")
	public static int LapotronPackCharge = 400_000_000;

	@ConfigRegistry(config = "items", category = "power", key = "LithiumBatpackCharge", comment = "Energy Capacity for Lithium Batpack (FE)")
	public static int LithiumBatpackCharge = 16_000_000;

	@ConfigRegistry(config = "items", category = "power", key = "energyCrystalMaxCharge", comment = "Energy Capacity for Energy Crystal (FE)")
	public static int EnergyCrystalMaxCharge = 4_000_000;
	
	@ConfigRegistry(config = "items", category = "power", key = "lapotronCrystalMaxCharge", comment = "Energy Capacity for Lapotron Crystal (FE)")
	public static int LapotronCrystalMaxCharge = 40_000_000;

	@ConfigRegistry(config = "items", category = "power", key = "lapotronicOrbMaxCharge", comment = "Energy Capacity for Lapotronic Orb (FE)")
	public static int LapotronicOrbMaxCharge = 400_000_000;

	@ConfigRegistry(config = "items", category = "power", key = "CloakingDeviceCharge", comment = "Energy Capacity for Clocking Device (FE)")
	public static int CloakingDeviceCharge = 40_000_000;
	
	@ConfigRegistry(config = "generators", category = "solarPanelBasic", key = "basicDayRate", comment = "Generation rate during day for Basic Solar Panel (Value in EU)")
	public static int basicGenerationRateD = 1;
	
	@ConfigRegistry(config = "generators", category = "solarPanelBasic", key = "basicNightRate", comment = "Generation rate during night for Basic Solar Panel (Value in EU)")
	public static int basicGenerationRateN = 0;
	
	@ConfigRegistry(config = "generators", category = "solarPanelHybrid", key = "hybridDayRate", comment = "Generation rate during day for Hybrid Solar Panel (Value in EU)")
	public static int hybridGenerationRateD = 16;
	
	@ConfigRegistry(config = "generators", category = "solarPanelHybrid", key = "hybridNightRate", comment = "Generation rate during night for Hybrid Solar Panel (Value in EU)")
	public static int hybridGenerationRateN = 0;	
	
	@ConfigRegistry(config = "generators", category = "solarPanelAdvanced", key = "advancedDayRate", comment = "Generation rate during day for Advanced Solar Panel (Value in EU)")
	public static int advancedGenerationRateD = 64;
	
	@ConfigRegistry(config = "generators", category = "solarPanelAdvanced", key = "advancedNightRate", comment = "Generation rate during night for Advanced Solar Panel (Value in EU)")
	public static int advancedGenerationRateN = 2;
	
	@ConfigRegistry(config = "generators", category = "solarPanelUltimate", key = "ultimateDayRate", comment = "Generation rate during day for Ultimate Solar Panel (Value in EU)")
	public static int ultimateGenerationRateD = 256;
	
	@ConfigRegistry(config = "generators", category = "solarPanelUltimate", key = "ultimateNightRate", comment = "Generation rate during night for Ultimate Solar Panel (Value in EU)")
	public static int ultimateGenerationRateN = 16;	
	
	@ConfigRegistry(config = "generators", category = "solarPanelQuantum", key = "quantumDayRate", comment = "Generation rate during day for Quantum Solar Panel (Value in EU)")
	public static int quantumGenerationRateD = 1024;
	
	@ConfigRegistry(config = "generators", category = "solarPanelQuantum", key = "quantumNightRate", comment = "Generation rate during night for Quantum Solar Panel (Value in EU)")
	public static int quantumGenerationRateN = 64;	

	@ConfigRegistry(config = "worlds", category = "loot", key = "enableOverworldLoot", comment = "When true TechReborn will add ingots, machine frames and circuits to OverWorld loot chests.")
	public static boolean enableOverworldLoot = true;

	@ConfigRegistry(config = "worlds", category = "loot", key = "enableNetherLoot", comment = "When true TechReborn will add ingots, machine frames and circuits to Nether loot chests.")
	public static boolean enableNetherLoot = true;
	
	@ConfigRegistry(config = "worlds", category = "loot", key = "enableEndLoot", comment = "When true TechReborn will add ingots, machine frames and circuits to The End loot chests.")
	public static boolean enableEndLoot = true;
}
