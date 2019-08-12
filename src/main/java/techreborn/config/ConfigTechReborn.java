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

import reborncore.common.registration.RebornRegister;
import reborncore.common.registration.config.ConfigRegistry;
import techreborn.TechReborn;

@RebornRegister(TechReborn.MOD_ID)
public class ConfigTechReborn {

	@ConfigRegistry(config = "items", category = "general", key = "enableGemTools", comment = "Enable Gem armor and tools")
	public static boolean enableGemArmorAndTools = true;

	@ConfigRegistry(config = "items", category = "power", key = "nanoSaberCharge", comment = "Energy Capacity for Nano Saber (FE)")
	public static int nanoSaberCharge = 4_000_000;
	
	@ConfigRegistry(config = "items", category = "power", key = "basicDrillCharge", comment = "Energy Capacity for Basic Drill (FE)")
	public static int BasicDrillCharge = 40_000;

	@ConfigRegistry(config = "items", category = "power", key = "advancedDrillCharge", comment = "Energy Capacity for Advanced Drill (FE)")
	public static int AdvancedDrillCharge = 400_000;

	@ConfigRegistry(config = "items", category = "power", key = "industrialDrillCharge", comment = "Energy Capacity for Industrial Drill (FE)")
	public static int IndustrialDrillCharge = 4_000_000;

	@ConfigRegistry(config = "items", category = "power", key = "basicChainsawCharge", comment = "Energy Capacity for Basic Chainsaw (FE)")
	public static int BasicChainsawCharge = 40_000;

	@ConfigRegistry(config = "items", category = "power", key = "advancedChainsawCharge", comment = "Energy Capacity for Advanced Chainsaw (FE)")
	public static int AdvancedChainsawCharge = 400_000;

	@ConfigRegistry(config = "items", category = "power", key = "industrialChainsawCharge", comment = "Energy Capacity for Industrial Chainsaw (FE)")
	public static int IndustrialChainsawCharge = 4_000_000;

	@ConfigRegistry(config = "items", category = "power", key = "basicJackhammerCharge", comment = "Energy Capacity for Basic Jackhammer (FE)")
	public static int BasicJackhammerCharge = 40_000;

	@ConfigRegistry(config = "items", category = "power", key = "advancedJackhammerCharge", comment = "Energy Capacity for Advanced Jackhammer (FE)")
	public static int AdvancedJackhammerCharge = 400_000;

	@ConfigRegistry(config = "items", category = "power", key = "industrialJackhammerCharge", comment = "Energy Capacity for Industrial Jachammer (FE)")
	public static int IndustrialJackhammerCharge = 4_000_000;

	@ConfigRegistry(config = "items", category = "power", key = "omniToolCharge", comment = "Energy Capacity for Omni Tool (FE)")
	public static int OmniToolCharge = 4_000_000;

	@ConfigRegistry(config = "items", category = "power", key = "rockCutterCharge", comment = "Energy Capacity for Rock Cutter (FE)")
	public static int RockCutterCharge = 400_000;

	@ConfigRegistry(config = "items", category = "power", key = "lapotronPackCharge", comment = "Energy Capacity for Lapotron Pack (FE)")
	public static int LapotronPackCharge = 400_000_000;

	@ConfigRegistry(config = "items", category = "power", key = "LithiumBatpackCharge", comment = "Energy Capacity for Lithium Batpack (FE)")
	public static int LithiumBatpackCharge = 8_000_000;

	@ConfigRegistry(config = "items", category = "power", key = "energyCrystalMaxCharge", comment = "Energy Capacity for Energy Crystal (FE)")
	public static int EnergyCrystalMaxCharge = 4_000_000;
	
	@ConfigRegistry(config = "items", category = "power", key = "lapotronCrystalMaxCharge", comment = "Energy Capacity for Lapotron Crystal (FE)")
	public static int LapotronCrystalMaxCharge = 40_000_000;

	@ConfigRegistry(config = "items", category = "power", key = "lapotronicOrbMaxCharge", comment = "Energy Capacity for Lapotronic Orb (FE)")
	public static int LapotronicOrbMaxCharge = 400_000_000;

	@ConfigRegistry(config = "items", category = "power", key = "cloakingDeviceCharge", comment = "Energy Capacity for Clocking Device (FE)")
	public static int CloakingDeviceCharge = 40_000_000;
	
	@ConfigRegistry(config = "items", category = "power", key = "clockingDeviceEnergyUsage", comment = "Cloacking device energy usesage (FE)")
	public static int CloackingDeviceUsage = 10;

	@ConfigRegistry(config = "generators", category = "solarPanelBasic", key = "basicDayRate", comment = "Generation rate during day for Basic Solar Panel (Value in FE)")
	public static int basicGenerationRateD = 1;
	
	@ConfigRegistry(config = "generators", category = "solarPanelBasic", key = "basicNightRate", comment = "Generation rate during night for Basic Solar Panel (Value in FE)")
	public static int basicGenerationRateN = 0;
	
	@ConfigRegistry(config = "generators", category = "solarPanelAdvanced", key = "advancedDayRate", comment = "Generation rate during day for Advanced Solar Panel (Value in FE)")
	public static int advancedGenerationRateD = 16;
	
	@ConfigRegistry(config = "generators", category = "solarPanelAdvanced", key = "advancedNightRate", comment = "Generation rate during night for Advanced Solar Panel (Value in FE)")
	public static int advancedGenerationRateN = 0;
	
	@ConfigRegistry(config = "generators", category = "solarPanelIndustrial", key = "industrialDayRate", comment = "Generation rate during day for Industrial Solar Panel (Value in FE)")
	public static int industrialGenerationRateD = 64;
	
	@ConfigRegistry(config = "generators", category = "solarPanelIndustrial", key = "industrialNightRate", comment = "Generation rate during night for Industrial Solar Panel (Value in FE)")
	public static int industrialGenerationRateN = 2;
	
	@ConfigRegistry(config = "generators", category = "solarPanelUltimate", key = "ultimateDayRate", comment = "Generation rate during day for Ultimate Solar Panel (Value in FE)")
	public static int ultimateGenerationRateD = 256;
	
	@ConfigRegistry(config = "generators", category = "solarPanelUltimate", key = "ultimateNightRate", comment = "Generation rate during night for Ultimate Solar Panel (Value in FE)")
	public static int ultimateGenerationRateN = 16;	
	
	@ConfigRegistry(config = "generators", category = "solarPanelQuantum", key = "quantumDayRate", comment = "Generation rate during day for Quantum Solar Panel (Value in FE)")
	public static int quantumGenerationRateD = 1024;
	
	@ConfigRegistry(config = "generators", category = "solarPanelQuantum", key = "quantumNightRate", comment = "Generation rate during night for Quantum Solar Panel (Value in FE)")
	public static int quantumGenerationRateN = 64;	

	@ConfigRegistry(config = "world", category = "loot", key = "enableOverworldLoot", comment = "When true TechReborn will add ingots, machine frames and circuits to OverWorld loot chests.")
	public static boolean enableOverworldLoot = true;

	@ConfigRegistry(config = "world", category = "loot", key = "enableNetherLoot", comment = "When true TechReborn will add ingots, machine frames and circuits to Nether loot chests.")
	public static boolean enableNetherLoot = true;
	
	@ConfigRegistry(config = "world", category = "loot", key = "enableEndLoot", comment = "When true TechReborn will add ingots, machine frames and circuits to The End loot chests.")
	public static boolean enableEndLoot = true;

}
