/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

import reborncore.common.config.Config;

import java.util.Arrays;
import java.util.List;

//All moved into one class as its a lot easier to find the annotations when you know where they all are
public class TechRebornConfig {

	// Generators
	@Config(config = "generators", category = "solarPanelGeneral", key = "internalCapacity", comment = "Multiplier for internal capacity of solar panels (multiplier * day generation rate)")
	public static int solarInternalCapacityMultiplier = 2000;

	@Config(config = "generators", category = "solarPanelBasic", key = "basicDayRate", comment = "Generation rate during day for Basic Solar Panel (Value in FE)")
	public static int basicGenerationRateD = 1;

	@Config(config = "generators", category = "solarPanelBasic", key = "basicNightRate", comment = "Generation rate during night for Basic Solar Panel (Value in FE)")
	public static int basicGenerationRateN = 0;

	@Config(config = "generators", category = "solarPanelAdvanced", key = "advancedDayRate", comment = "Generation rate during day for Advanced Solar Panel (Value in FE)")
	public static int advancedGenerationRateD = 16;

	@Config(config = "generators", category = "solarPanelAdvanced", key = "advancedNightRate", comment = "Generation rate during night for Advanced Solar Panel (Value in FE)")
	public static int advancedGenerationRateN = 0;

	@Config(config = "generators", category = "solarPanelIndustrial", key = "industrialDayRate", comment = "Generation rate during day for Industrial Solar Panel (Value in FE)")
	public static int industrialGenerationRateD = 64;

	@Config(config = "generators", category = "solarPanelIndustrial", key = "industrialNightRate", comment = "Generation rate during night for Industrial Solar Panel (Value in FE)")
	public static int industrialGenerationRateN = 2;

	@Config(config = "generators", category = "solarPanelUltimate", key = "ultimateDayRate", comment = "Generation rate during day for Ultimate Solar Panel (Value in FE)")
	public static int ultimateGenerationRateD = 256;

	@Config(config = "generators", category = "solarPanelUltimate", key = "ultimateNightRate", comment = "Generation rate during night for Ultimate Solar Panel (Value in FE)")
	public static int ultimateGenerationRateN = 16;

	@Config(config = "generators", category = "solarPanelQuantum", key = "quantumDayRate", comment = "Generation rate during day for Quantum Solar Panel (Value in FE)")
	public static int quantumGenerationRateD = 2048;

	@Config(config = "generators", category = "solarPanelQuantum", key = "quantumNightRate", comment = "Generation rate during night for Quantum Solar Panel (Value in FE)")
	public static int quantumGenerationRateN = 128;

	@Config(config = "generators", category = "lightning_rod", key = "LightningRodMaxOutput", comment = "Lightning Rod Max Output (Value in EU)")
	public static int lightningRodMaxOutput = 2048;

	@Config(config = "generators", category = "lightning_rod", key = "LightningRodMaxEnergy", comment = "Lightning Rod Max Energy (Value in EU)")
	public static int lightningRodMaxEnergy = 100_000_000;

	@Config(config = "generators", category = "lightning_rod", key = "LightningRodChanceOfStrike", comment = "Chance of lightning striking a rod (Range: 0-70)")
	public static int lightningRodChanceOfStrike = 24;

	@Config(config = "generators", category = "lightning_rod", key = "LightningRodBaseStrikeEnergy", comment = "Base amount of energy per strike (Value in EU)")
	public static int lightningRodBaseEnergyStrike = 262_144;

	@Config(config = "generators", category = "thermal_generator", key = "ThermalGeneratorMaxOutput", comment = "Thermal Generator Max Output (Value in EU)")
	public static int thermalGeneratorMaxOutput = 128;

	@Config(config = "generators", category = "thermal_generator", key = "ThermalGeneratorMaxEnergy", comment = "Thermal Generator Max Energy (Value in EU)")
	public static int thermalGeneratorMaxEnergy = 1_000_000;

	@Config(config = "generators", category = "thermal_generator", key = "ThermalGeneratorEnergyPerTick", comment = "Thermal Generator Energy Per Tick (Value in EU)")
	public static int thermalGeneratorEnergyPerTick = 16;

	@Config(config = "generators", category = "plasma_generator", key = "PlasmaGeneratorMaxOutput", comment = "Plasma Generator Max Output (Value in EU)")
	public static int plasmaGeneratorMaxOutput = 2048;

	@Config(config = "generators", category = "plasma_generator", key = "PlasmaGeneratorMaxEnergy", comment = "Plasma Generator Max Energy (Value in EU)")
	public static double plasmaGeneratorMaxEnergy = 500_000_000;

	@Config(config = "generators", category = "plasma_generator", key = "PlasmaGeneratorEnergyPerTick", comment = "Plasma Generator Energy Per Tick (Value in EU)")
	public static int plasmaGeneratorEnergyPerTick = 400;

	@Config(config = "generators", category = "wind_mill", key = "WindMillMaxOutput", comment = "Wind Mill Max Output (Value in EU)")
	public static int windMillMaxOutput = 128;

	@Config(config = "generators", category = "wind_mill", key = "WindMillMaxEnergy", comment = "Wind Mill Max Energy (Value in EU)")
	public static int windMillMaxEnergy = 10_000;

	@Config(config = "generators", category = "wind_mill", key = "WindMillEnergyPerTick", comment = "Wind Mill Energy Per Tick (Value in EU)")
	public static int windMillBaseEnergy = 2;

	@Config(config = "generators", category = "wind_mill", key = "WindMillThunderMultiplier", comment = "Wind Mill Thunder Multiplier")
	public static double windMillThunderMultiplier = 1.25;

	@Config(config = "generators", category = "water_mill", key = "WaterMillMaxOutput", comment = "Water Mill Max Output (Value in EU)")
	public static int waterMillMaxOutput = 32;

	@Config(config = "generators", category = "water_mill", key = "WaterMillMaxEnergy", comment = "Water Mill Max Energy (Value in EU)")
	public static int waterMillMaxEnergy = 1000;

	@Config(config = "generators", category = "water_mill", key = "WaterMillEnergyPerTick", comment = "Water Mill Energy Multiplier")
	public static double waterMillEnergyMultiplier = 0.1;

	@Config(config = "generators", category = "semifluid_generator", key = "SemifluidGeneratorMaxOutput", comment = "Semifluid Generator Max Output (Value in EU)")
	public static int semiFluidGeneratorMaxOutput = 128;

	@Config(config = "generators", category = "semifluid_generator", key = "SemifluidGeneratorMaxEnergy", comment = "Semifluid Generator Max Energy (Value in EU)")
	public static int semiFluidGeneratorMaxEnergy = 1000000;

	@Config(config = "generators", category = "semifluid_generator", key = "SemifluidGeneratorEnergyPerTick", comment = "Semifluid Generator Energy Per Tick (Value in EU)")
	public static int semiFluidGeneratorEnergyPerTick = 8;

	@Config(config = "generators", category = "gas_generator", key = "GasGeneratorMaxOutput", comment = "Gas Generator Max Output (Value in EU)")
	public static int gasTurbineMaxOutput = 128;

	@Config(config = "generators", category = "gas_generator", key = "GasGeneratorMaxEnergy", comment = "Gas Generator Max Energy (Value in EU)")
	public static int gasTurbineMaxEnergy = 1000000;

	@Config(config = "generators", category = "gas_generator", key = "GasGeneratorEnergyPerTick", comment = "Gas Generator Energy Per Tick (Value in EU)")
	public static int gasTurbineEnergyPerTick = 16;

	@Config(config = "generators", category = "diesel_generator", key = "DieselGeneratorMaxOutput", comment = "Diesel Generator Max Output (Value in EU)")
	public static int dieselGeneratorMaxOutput = 32;

	@Config(config = "generators", category = "diesel_generator", key = "DieselGeneratorMaxEnergy", comment = "Diesel Generator Max Energy (Value in EU)")
	public static int dieselGeneratorMaxEnergy = 10_000;

	@Config(config = "generators", category = "diesel_generator", key = "DieselGeneratorEnergyPerTick", comment = "Diesel Generator Energy Per Tick (Value in EU)")
	public static int dieselGeneratorEnergyPerTick = 20;

	@Config(config = "generators", category = "dragon_egg_siphoner", key = "DragonEggSiphonerMaxOutput", comment = "Dragon Egg Siphoner Max Output (Value in EU)")
	public static int dragonEggSyphonMaxOutput = 128;

	@Config(config = "generators", category = "dragon_egg_siphoner", key = "DragonEggSiphonerMaxEnergy", comment = "Dragon Egg Siphoner Max Energy (Value in EU)")
	public static int dragonEggSyphonMaxEnergy = 1000;

	@Config(config = "generators", category = "dragon_egg_siphoner", key = "DragonEggSiphonerEnergyPerTick", comment = "Dragon Egg Siphoner Energy Per Tick (Value in EU)")
	public static int dragonEggSyphonEnergyPerTick = 4;

	@Config(config = "generators", category = "generator", key = "GeneratorMaxOutput", comment = "Solid Fuel Generator Max Output (Value in EU)")
	public static int solidFuelGeneratorMaxOutput = 32;

	@Config(config = "generators", category = "generator", key = "GeneratorMaxEnergy", comment = "Solid Fuel Generator Max Energy (Value in EU)")
	public static int solidFuelGeneratorMaxEnergy = 10_000;

	@Config(config = "generators", category = "generator", key = "GeneratorEnergyOutput", comment = "Solid Fuel Generator Energy Output Amount (Value in EU)")
	public static int solidFuelGeneratorOutputAmount = 10;

	// Items
	@Config(config = "items", category = "general", key = "enableGemTools", comment = "Enable Gem armor and tools")
	public static boolean enableGemArmorAndTools = true;

	@Config(config = "items", category = "power", key = "nanoSaberCharge", comment = "Energy Capacity for Nano Saber")
	public static int nanosaberCharge = 1_000_000;

	@Config(config = "items", category = "power", key = "nanoSaberCost", comment = "Energy Cost for Nano Saber")
	public static int nanosaberCost = 150;

	@Config(config = "items", category = "power", key = "nanoSaberDamage", comment = "Damage value for the Nano Saber")
	public static int nanosaberDamage = 20;

	@Config(config = "items", category = "power", key = "electricTreetapCharge", comment = "Energy Capacity for Electric Treetap")
	public static int electricTreetapCharge = 10_000;

	@Config(config = "items", category = "power", key = "electricTreetapCost", comment = "Energy Cost for Electric Treetap")
	public static int electricTreetapCost = 50;

	@Config(config = "items", category = "power", key = "basicDrillCharge", comment = "Energy Capacity for Basic Drill")
	public static int basicDrillCharge = 10_000;

	@Config(config = "items", category = "power", key = "basicDrillCost", comment = "Energy Cost for Basic Drill")
	public static int basicDrillCost = 50;

	@Config(config = "items", category = "power", key = "advancedDrillCharge", comment = "Energy Capacity for Advanced Drill")
	public static int advancedDrillCharge = 100_000;

	@Config(config = "items", category = "power", key = "advancedDrillCost", comment = "Energy Cost for Advanced Drill")
	public static int advancedDrillCost = 100;

	@Config(config = "items", category = "power", key = "industrialDrillCharge", comment = "Energy Capacity for Industrial Drill")
	public static int industrialDrillCharge = 1_000_000;

	@Config(config = "items", category = "power", key = "industrialDrillCost", comment = "Energy Cost for Industrial Drill")
	public static int industrialDrillCost = 150;

	@Config(config = "items", category = "power", key = "basicChainsawCharge", comment = "Energy Capacity for Basic Chainsaw")
	public static int basicChainsawCharge = 10_000;

	@Config(config = "items", category = "power", key = "basicChainsawCost", comment = "Energy Cost for Basic Chainsaw")
	public static int basicChainsawCost = 50;

	@Config(config = "items", category = "power", key = "advancedChainsawCharge", comment = "Energy Capacity for Advanced Chainsaw")
	public static int advancedChainsawCharge = 100_000;

	@Config(config = "items", category = "power", key = "advancedChainsawCost", comment = "Energy Cost for Advanced Chainsaw")
	public static int advancedChainsawCost = 100;

	@Config(config = "items", category = "power", key = "industrialChainsawCharge", comment = "Energy Capacity for Industrial Chainsaw")
	public static int industrialChainsawCharge = 1_000_000;

	@Config(config = "items", category = "power", key = "industrialChainsawCost", comment = "Energy Cost for Industrial Chainsaw")
	public static int industrialChainsawCost = 150;

	@Config(config = "items", category = "power", key = "basicJackhammerCharge", comment = "Energy Capacity for Basic Jackhammer")
	public static int basicJackhammerCharge = 10_000;

	@Config(config = "items", category = "power", key = "basicJackhammerCost", comment = "Energy Cost for Basic Jackhammer")
	public static int basicJackhammerCost = 50;

	@Config(config = "items", category = "power", key = "advancedJackhammerCharge", comment = "Energy Capacity for Advanced Jackhammer")
	public static int advancedJackhammerCharge = 100_000;

	@Config(config = "items", category = "power", key = "advancedJackhammerCost", comment = "Energy Cost for Advanced Jackhammer")
	public static int advancedJackhammerCost = 100;

	@Config(config = "items", category = "power", key = "industrialJackhammerCharge", comment = "Energy Capacity for Industrial Jackhammer")
	public static int industrialJackhammerCharge = 1_000_000;

	@Config(config = "items", category = "power", key = "industrialJackhammerCost", comment = "Energy Cost for Industrial Jackhammer")
	public static int industrialJackhammerCost = 150;

	@Config(config = "items", category = "power", key = "omniToolCharge", comment = "Energy Capacity for Omni Tool")
	public static int omniToolCharge = 1_000_000;

	@Config(config = "items", category = "power", key = "omniToolCost", comment = "Energy Cost for Omni Tool")
	public static int omniToolCost = 100;

	@Config(config = "items", category = "power", key = "omniToolHitCost", comment = "Hit Energy Cost for Omni Tool")
	public static int omniToolHitCost = 125;

	@Config(config = "items", category = "power", key = "rockCutterCharge", comment = "Energy Capacity for Rock Cutter (FE)")
	public static int rockCutterCharge = 400_000;

	@Config(config = "items", category = "power", key = "rockCutterCost", comment = "Energy Cost for Rock Cutter (FE)")
	public static int rockCutterCost = 500;

	@Config(config = "items", category = "power", key = "lapotronPackCharge", comment = "Energy Capacity for Lapotron Pack")
	public static int lapotronPackCharge = 100_000_000;

	@Config(config = "items", category = "power", key = "LithiumBatpackCharge", comment = "Energy Capacity for Lithium Batpack")
	public static int lithiumBatpackCharge = 600_000;

	@Config(config = "items", category = "power", key = "redCellBatteryMaxCharge", comment = "Energy Capacity for Red Cell Battery")
	public static int redCellBatteryMaxCharge = 10_000;

	@Config(config = "items", category = "power", key = "lithiumIonBatteryMaxCharge", comment = "Energy Capacity for Lithium Ion Battery")
	public static int lithiumIonBatteryMaxCharge = 100_000;

	@Config(config = "items", category = "power", key = "energyCrystalMaxCharge", comment = "Energy Capacity for Energy Crystal")
	public static int energyCrystalMaxCharge = 1_000_000;

	@Config(config = "items", category = "power", key = "lapotronCrystalMaxCharge", comment = "Energy Capacity for Lapotron Crystal")
	public static int lapotronCrystalMaxCharge = 10_000_000;

	@Config(config = "items", category = "power", key = "lapotronicOrbMaxCharge", comment = "Energy Capacity for Lapotronic Orb")
	public static int lapotronicOrbMaxCharge = 100_000_000;

	@Config(config = "items", category = "power", key = "cloakingDeviceCharge", comment = "Energy Capacity for Clocking Device (FE)")
	public static int cloakingDeviceCharge = 40_000_000;

	@Config(config = "items", category = "power", key = "clockingDeviceEnergyUsage", comment = "Cloacking device energy usesage (FE)")
	public static int cloackingDeviceCost = 10;

	@Config(config = "items", category = "power", key = "quantumSuitCapacity", comment = "Quantum Suit Energy Capacity")
	public static double quantumSuitCapacity = 40_000_000;

	@Config(config = "items", category = "power", key = "quantumSuitFlyingCost", comment = "Quantum Suit Flying Cost")
	public static double quantumSuitFlyingCost = 50;

	@Config(config = "items", category = "power", key = "quantumSuitSwimmingCost", comment = "Quantum Suit Swimming Cost")
	public static double quantumSuitSwimmingCost = 20;

	@Config(config = "items", category = "power", key = "quantumSuitBreathingCost", comment = "Quantum Suit Breathing Cost")
	public static double quantumSuitBreathingCost = 20;

	@Config(config = "items", category = "power", key = "quantumSuitSprintingCost", comment = "Quantum Suit Sprinting Cost")
	public static double quantumSuitSprintingCost = 20;

	@Config(config = "items", category = "power", key = "quantumSuitFireExtinguishCost", comment = "Quantum Suit Cost for Fire Extinguish")
	public static double fireExtinguishCost = 50;

	@Config(config = "items", category = "upgrades", key = "overclcoker_speed", comment = "Overclocker behavior speed multipiler")
	public static double overclockerSpeed = 0.25;

	@Config(config = "items", category = "upgrades", key = "overclcoker_power", comment = "Overclocker behavior power multipiler")
	public static double overclockerPower = 0.75;

	@Config(config = "items", category = "upgrades", key = "energy_storage", comment = "Energy storage behavior extra power")
	public static double energyStoragePower = 40_000;

	@Config(config = "items", category = "upgrades", key = "super_conductor", comment = "Energy flow power increase")
	public static double superConductorCount = 1;


	// Machines
	@Config(config = "machines", category = "lesu", key = "LesuMaxEnergyPerBlock", comment = "LESU Max Energy Per Block")
	public static int lesuStoragePerBlock = 1_000_000;

	@Config(config = "machines", category = "lesu", key = "LesuExtraIO", comment = "LESU Extra I/O Multiplier")
	public static int lesuExtraIOPerBlock = 1;

	@Config(config = "machines", category = "lesu", key = "LesuBaseOutput", comment = "LESU Base Output")
	public static int lesuBaseOutput = 5;

	@Config(config = "machines", category = "aesu", key = "AesuMaxEnergy", comment = "AESU Max Energy")
	public static int aesuMaxEnergy = 100_000_000;

	@Config(config = "machines", category = "player_detector", key = "PlayerDetectorMaxInput", comment = "Player Detector Max Input (Value in EU)")
	public static int playerDetectorMaxInput = 32;

	@Config(config = "machines", category = "player_detector", key = "PlayerDetectorMaxEnergy", comment = "Player Detector Max Energy (Value in EU)")
	public static int playerDetectorMaxEnergy = 10000;

	@Config(config = "machines", category = "player_detector", key = "PlayerDetectorEUPerSecond", comment = "Player Detector Energy Consumption per second (Value in EU)")
	public static int playerDetectorEuPerTick = 10;

	@Config(config = "machines", category = "Distillation_tower", key = "DistillationTowerMaxInput", comment = "Distillation Tower Max Input (Value in EU)")
	public static int distillationTowerMaxInput = 128;

	@Config(config = "machines", category = "Distillation_tower", key = "DistillationTowerMaxEnergy", comment = "Distillation Tower Max Energy (Value in EU)")
	public static int distillationTowerMaxEnergy = 10_000;

	@Config(config = "machines", category = "extractor", key = "ExtractorInput", comment = "Extractor Max Input (Value in EU)")
	public static int extractorMaxInput = 32;

	@Config(config = "machines", category = "extractor", key = "ExtractorMaxEnergy", comment = "Extractor Max Energy (Value in EU)")
	public static int extractorMaxEnergy = 1_000;

	@Config(config = "machines", category = "compressor", key = "CompressorInput", comment = "Compressor Max Input (Value in EU)")
	public static int compressorMaxInput = 32;

	@Config(config = "machines", category = "compressor", key = "CompressorMaxEnergy", comment = "Compressor Max Energy (Value in EU)")
	public static int compressorMaxEnergy = 1000;

	@Config(config = "machines", category = "alloy_smelter", key = "AlloySmelterMaxInput", comment = "Alloy Smelter Max Input (Value in EU)")
	public static int alloySmelterMaxInput = 32;

	@Config(config = "machines", category = "alloy_smelter", key = "AlloySmelterMaxEnergy", comment = "Alloy Smelter Max Energy (Value in EU)")
	public static int alloySmelterMaxEnergy = 1_000;

	@Config(config = "machines", category = "rolling_machine", key = "RollingMachineMaxInput", comment = "Rolling Machine Max Input (Value in EU)")
	public static int rollingMachineMaxInput = 32;

	@Config(config = "machines", category = "rolling_machine", key = "RollingMachineEnergyPerTick", comment = "Rolling Machine Energy Per Tick (Value in EU)")
	public static int rollingMachineEnergyPerTick = 5;

	@Config(config = "machines", category = "rolling_machine", key = "RollingMachineEnergyRunTime", comment = "Rolling Machine Run Time")
	public static int rollingMachineRunTime = 250;

	@Config(config = "machines", category = "rolling_machine", key = "RollingMachineMaxEnergy", comment = "Rolling Machine Max Energy (Value in EU)")
	public static int rollingMachineMaxEnergy = 10000;

	@Config(config = "machines", category = "chunk_loader", key = "ChunkLoaderMaxRadius", comment = "Chunk Loader Max Radius")
	public static int chunkLoaderMaxRadius = 5;

	@Config(config = "machines", category = "assembling_machine", key = "AssemblingMachineMaxInput", comment = "Assembling Machine Max Input (Value in EU)")
	public static int assemblingMachineMaxInput = 128;

	@Config(config = "machines", category = "assembling_machine", key = "AssemblingMachineMaxEnergy", comment = "Assembling Machine Max Energy (Value in EU)")
	public static int assemblingMachineMaxEnergy = 10_000;

	@Config(config = "machines", category = "matter_fabricator", key = "MatterFabricatorMaxInput", comment = "Matter Fabricator Max Input (Value in EU)")
	public static int matterFabricatorMaxInput = 8192;

	@Config(config = "machines", category = "matter_fabricator", key = "MatterFabricatorMaxEnergy", comment = "Matter Fabricator Max Energy (Value in EU)")
	public static int matterFabricatorMaxEnergy = 10_000_000;

	@Config(config = "machines", category = "matter_fabricator", key = "MatterFabricatorFabricationRate", comment = "Matter Fabricator Fabrication Rate, amount of amplifier units per UUM")
	public static int matterFabricatorFabricationRate = 6_000;

	@Config(config = "machines", category = "matter_fabricator", key = "MatterFabricatorEnergyPerAmp", comment = "Matter Fabricator EU per amplifier unit, multiply this with the rate for total EU")
	public static int matterFabricatorEnergyPerAmp = 5;

	@Config(config = "machines", category = "industrial_grinder", key = "IndustrialGrinderMaxInput", comment = "Industrial Grinder Max Input (Value in EU)")
	public static int industrialGrinderMaxInput = 128;

	@Config(config = "machines", category = "industrial_grinder", key = "IndustrialGrinderMaxEnergy", comment = "Industrial Grinder Max Energy (Value in EU)")
	public static int industrialGrinderMaxEnergy = 10_000;

	@Config(config = "machines", category = "vacuumfreezer", key = "VacuumFreezerInput", comment = "Vacuum Freezer Max Input (Value in EU)")
	public static int vacuumFreezerMaxInput = 64;

	@Config(config = "machines", category = "vacuumfreezer", key = "VacuumFreezerMaxEnergy", comment = "Vacuum Freezer Max Energy (Value in EU)")
	public static int vacuumFreezerMaxEnergy = 64_000;

	@Config(config = "machines", category = "implosion_compressor", key = "ImplosionCompressorMaxInput", comment = "Implosion Compressor Max Input (Value in EU)")
	public static int implosionCompressorMaxInput = 64;

	@Config(config = "machines", category = "implosion_compressor", key = "ImplosionCompressorMaxEnergy", comment = "Implosion Compressor Max Energy (Value in EU)")
	public static int implosionCompressorMaxEnergy = 64_000;

	@Config(config = "machines", category = "industrial_furnace", key = "IndustrialFurnaceMaxInput", comment = "Industrial Blast Furnace Max Input (Value in EU)")
	public static int industrialBlastFurnaceMaxInput = 128;

	@Config(config = "machines", category = "industrial_furnace", key = "IndustrialFurnaceMaxEnergy", comment = "Industrial Blast Furnace Max Energy (Value in EU)")
	public static int industrialBlastFurnaceMaxEnergy = 40_000;

	@Config(config = "machines", category = "industrial_sawmill", key = "IndustrialSawmillMaxInput", comment = "Industrial Sawmill Max Input (Value in EU)")
	public static int industrialSawmillMaxInput = 128;

	@Config(config = "machines", category = "industrial_sawmill", key = "IndustrialSawmillMaxEnergy", comment = "Industrial Sawmill Max Energy (Value in EU)")
	public static int industrialSawmillMaxEnergy = 10_000;

	@Config(config = "machines", category = "autocrafter", key = "AutoCrafterInput", comment = "AutoCrafting Table Max Input (Value in EU)")
	public static int autoCraftingTableMaxInput = 32;

	@Config(config = "machines", category = "autocrafter", key = "AutoCrafterMaxEnergy", comment = "AutoCrafting Table Max Energy (Value in EU)")
	public static int autoCraftingTableMaxEnergy = 10_000;

	@Config(config = "machines", category = "fluidreplicator", key = "FluidReplicatorMaxInput", comment = "Fluid Replicator Max Input (Value in EU)")
	public static int fluidReplicatorMaxInput = 256;

	@Config(config = "machines", category = "fluidreplicator", key = "FluidReplicatorMaxEnergy", comment = "Fluid Replicator Max Energy (Value in EU)")
	public static int fluidReplicatorMaxEnergy = 400_000;

	@Config(config = "machines", category = "electric_furnace", key = "ElectricFurnaceInput", comment = "Electric Furnace Max Input (Value in EU)")
	public static int electricFurnaceMaxInput = 32;

	@Config(config = "machines", category = "electric_furnace", key = "ElectricFurnaceMaxEnergy", comment = "Electric Furnace Max Energy (Value in EU)")
	public static int electricFurnaceMaxEnergy = 1000;

	@Config(config = "machines", category = "storage", key = "CrudeStorageUnitMaxStorage", comment = "Maximum amount of items a Crude Storage Unit can store")
	public static int crudeStorageUnitMaxStorage = 2048;

	@Config(config = "machines", category = "storage", key = "BasicStorageUnitMaxStorage", comment = "Maximum amount of items a Basic Storage Unit can store")
	public static int basicStorageUnitMaxStorage = 8192;

	@Config(config = "machines", category = "storage", key = "BasicTankUnitCapacity", comment = "How much liquid a Basic Tank Unit can take (Value in buckets, 1000 Mb)")
	public static int basicTankUnitCapacity = 35;

	@Config(config = "machines", category = "storage", key = "AdvancedStorageMaxStorage", comment = "Maximum amount of items an Advanced Storage Unit can store")
	public static int advancedStorageUnitMaxStorage = 32768;

	@Config(config = "machines", category = "storage", key = "AdvancedTankUnitMaxStorage", comment = "How much liquid an Advanced Tank Unit can take (Value in buckets, 1000 Mb)")
	public static int advancedTankUnitMaxStorage = 200;

	@Config(config = "machines", category = "storage", key = "IndustrialStorageMaxStorage", comment = "Maximum amount of items an Industrial Storage Unit can store (Compat: >= 32768)")
	public static int industrialStorageUnitMaxStorage = 65536;

	@Config(config = "machines", category = "storage", key = "IndustrialTankUnitCapacity", comment = "How much liquid an Industrial Tank Unit can take (Value in buckets, 1000 Mb)")
	public static int industrialTankUnitCapacity = 500;

	@Config(config = "machines", category = "storage", key = "QuantumStorageUnitMaxStorage", comment = "Maximum amount of items a Quantum Storage Unit can store (Compat: == MAX_VALUE)")
	public static int quantumStorageUnitMaxStorage = Integer.MAX_VALUE;

	@Config(config = "machines", category = "storage", key = "QuantumTankUnitCapacity", comment = "How much liquid a Quantum Tank Unit can take (Value in buckets, 1000 Mb)(Compat: == MAX_VALUE)")
	public static int quantumTankUnitCapacity = Integer.MAX_VALUE / 1000;

	@Config(config = "machines", category = "charge_bench", key = "ChargeBenchMaxOutput", comment = "Charge Bench Max Output (Value in EU)")
	public static int chargeOMatBMaxOutput = 512;

	@Config(config = "machines", category = "charge_bench", key = "ChargeBenchMaxInput", comment = "Charge Bench Max Input (Value in EU)")
	public static int chargeOMatBMaxInput = 512;

	@Config(config = "machines", category = "charge_bench", key = "ChargeBenchMaxEnergy", comment = "Charge Bench Max Energy (Value in EU)")
	public static int chargeOMatBMaxEnergy = 100_000_000;

	@Config(config = "machines", category = "industrial_electrolyzer", key = "IndustrialElectrolyzerMaxInput", comment = "Industrial Electrolyzer Max Input (Value in EU)")
	public static int industrialElectrolyzerMaxInput = 128;

	@Config(config = "machines", category = "industrial_electrolyzer", key = "IndustrialElectrolyzerMaxEnergy", comment = "Industrial Electrolyzer Max Energy (Value in EU)")
	public static int industrialElectrolyzerMaxEnergy = 10_000;

	@Config(config = "machines", category = "centrifuge", key = "CentrifugeMaxInput", comment = "Centrifuge Max Input (Value in EU)")
	public static int industrialCentrifugeMaxInput = 32;

	@Config(config = "machines", category = "centrifuge", key = "CentrifugeMaxEnergy", comment = "Centrifuge Max Energy (Value in EU)")
	public static int industrialCentrifugeMaxEnergy = 10_000;

	@Config(config = "machines", category = "chemical_reactor", key = "ChemicalReactorMaxInput", comment = "Chemical Reactor Max Input (Value in EU)")
	public static int chemicalReactorMaxInput = 128;

	@Config(config = "machines", category = "chemical_reactor", key = "ChemicalReactorMaxEnergy", comment = "Chemical Reactor Max Energy (Value in EU)")
	public static int chemicalReactorMaxEnergy = 10_000;

	@Config(config = "machines", category = "idsu", key = "IdsuMaxEnergy", comment = "IDSU Max Energy")
	public static int idsuMaxEnergy = 1_000_000_000;

	@Config(config = "machines", category = "fusion_reactor", key = "FusionReactorMaxInput", comment = "Fusion Reactor Max Input (Value in EU)")
	public static int fusionControlComputerMaxInput = 8192;

	@Config(config = "machines", category = "fusion_reactor", key = "FusionReactorMaxOutput", comment = "Fusion Reactor Max Output (Value in EU)")
	public static int fusionControlComputerMaxOutput = 1_000_000;

	@Config(config = "machines", category = "fusion_reactor", key = "FusionReactorMaxEnergy", comment = "Fusion Reactor Max Energy (Value in EU)")
	public static int fusionControlComputerMaxEnergy = 100_000_000;

	@Config(config = "machines", category = "fusion_reactor", key = "FusionReactorMaxCoilSize", comment = "Fusion Reactor Max Coil size (Radius)")
	public static int fusionControlComputerMaxCoilSize = 50;

	@Config(config = "machines", category = "recycler", key = "RecyclerInput", comment = "Recycler Max Input (Value in EU)")
	public static int recyclerMaxInput = 32;

	@Config(config = "machines", category = "recycler", key = "RecyclerMaxEnergy", comment = "Recycler Max Energy (Value in EU)")
	public static int recyclerMaxEnergy = 1000;

	@Config(config = "machines", category = "recycler", key = "RecyclerBlacklist", comment = "Recycler blacklist")
	public static List<String> recyclerBlackList = Arrays.asList("techreborn:scrap_box", "techreborn:scrap");

	@Config(config = "machines", category = "scrapboxinator", key = "ScrapboxinatorMaxInput", comment = "Scrapboxinator Max Input (Value in EU)")
	public static int scrapboxinatorMaxInput = 32;

	@Config(config = "machines", category = "scrapboxinator", key = "ScrapboxinatorMaxEnergy", comment = "Scrapboxinator Max Energy (Value in EU)")
	public static int scrapboxinatorMaxEnergy = 1_000;

	@Config(config = "machines", category = "solid_canning_machine", key = "solidCanningMachineMaxInput", comment = "Solid Canning Machine Max Input (Value in EU)")
	public static int solidCanningMachineMaxInput = 32;

	@Config(config = "machines", category = "solid_canning_machine", key = "solidCanningMachineMaxEnergy", comment = "Solid Canning Machine Max Energy (Value in EU)")
	public static int solidCanningMachineMaxEnergy = 1_000;

	@Config(config = "machines", category = "iron_machine", key = "fuel_scale", comment = "Multiplier for vanilla furnace item burn time")
	public static double fuelScale = 1.25;

	@Config(config = "machines", category = "iron_machine", key = "cooking_scale", comment = "Multiplier for vanilla furnace item cook time")
	public static double cookingScale = 1.25;

	@Config(config = "machines", category = "greenhouse_controller", key = "GreenhouseControllerMaxInput", comment = "Greenhouse Controller Max Input")
	public static int greenhouseControllerMaxInput = 32;

	@Config(config = "machines", category = "greenhouse_controller", key = "GreenhouseControllerMaxEnergy", comment = "Greenhouse Controller Max Energy")
	public static int greenhouseControllerMaxEnergy = 1_000;

	@Config(config = "machines", category = "greenhouse_controller", key = "GreenhouseControllerEnergyPerTick", comment = "Greenhouse Controller Energy Per Tick")
	public static int greenhouseControllerEnergyPerTick = 2;

	@Config(config = "machines", category = "greenhouse_controller", key = "GreenhouseControllerEnergyPerHarvest", comment = "Greenhouse Controller Energy Per Harvest")
	public static int greenhouseControllerEnergyPerHarvest = 100;

	@Config(config = "machines", category = "greenhouse_controller", key = "GreenhouseControllerEnergyPerBonemeal", comment = "Greenhouse Controller Energy Per Bonemeal")
	public static int greenhouseControllerEnergyPerBonemeal = 50;

	// MINING RIG

	@Config(config = "machines", category = "mining_rig", key = "MiningRigMaxInput", comment = "Mining Rig Max Input")
	public static int miningRigMaxInput = 512;

	@Config(config = "machines", category = "mining_rig", key = "MiningRigMaxEnergy", comment = "Mining Rig Max Energy")
	public static int miningRigMaxEnergy = 10_000;

	@Config(config = "machines", category = "mining_rig", key = "MiningRigDrillOffset", comment = "Ming Rig How Far Down Before Mining (0 - No buffer, 2 - Two blocks (No world holes)")
	public static int miningRigDrillOffset = 2;

	@Config(config = "machines", category = "mining_rig", key = "MiningRigTankSize", comment = "Mining Rig Internal Tank Storage (Buckets)")
	public static int miningRigTankSize = 15;


	// Misc
	@Config(config = "misc", category = "general", key = "IC2TransformersStyle", comment = "Input from dots side, output from other sides, like in IC2.")
	public static boolean IC2TransformersStyle = true;

	@Config(config = "misc", category = "general", key = "MachineSoundVolume", comment = "Machines crafting sound volume (0 - disabled, 1 - max)")
	public static float machineSoundVolume = 1.0F;

	@Config(config = "misc", category = "general", key = "manualRefund", comment = "Allow refunding items used to craft the manual")
	public static boolean allowManualRefund = true;

	@Config(config = "misc", category = "nuke", key = "fusetime", comment = "Nuke fuse time (ticks)")
	public static int nukeFuseTime = 400;

	@Config(config = "misc", category = "nuke", key = "radius", comment = "Nuke explision radius")
	public static int nukeRadius = 40;

	@Config(config = "misc", category = "nuke", key = "enabled", comment = "Should the nuke explode, set to false to prevent block damage")
	public static boolean nukeEnabled = true;

	@Config(config = "misc", category = "resin_basin", key = "saptime", comment = "How long it takes to harvest one sap (ticks)")
	public static int sapTimeTicks = 80;

	@Config(config = "misc", category = "resin_basin", key = "SapCheckTime", comment = "How often to check for sap (will check if world time % this number is zero)")
	public static int checkForSapTime = 50;

	@Config(config = "misc", category = "general", key = "DispenserScrapbox", comment = "Dispensers will open scrapboxes")
	public static boolean dispenseScrapboxes = true;

	@Config(config = "misc", category = "cable", key = "uninsulatedElectrocutionDamage", comment = "When true an uninsulated cable will cause damage to entities")
	public static boolean uninsulatedElectrocutionDamage = true;

	@Config(config = "misc", category = "cable", key = "uninsulatedElectrocutionSound", comment = "When true an uninsulated cable will create a spark sound when an entity touches it")
	public static boolean uninsulatedElectrocutionSound = true;

	@Config(config = "misc", category = "cable", key = "uninsulatedElectrocutionParticles", comment = "When true an uninsulated cable will create a spark when an entity touches it")
	public static boolean uninsulatedElectrocutionParticles = true;

	// World
	@Config(config = "world", category = "loot", key = "enableOverworldLoot", comment = "When true TechReborn will add ingots, machine frames and circuits to OverWorld loot chests.")
	public static boolean enableOverworldLoot = true;

	@Config(config = "world", category = "loot", key = "enableNetherLoot", comment = "When true TechReborn will add ingots, machine frames and circuits to Nether loot chests.")
	public static boolean enableNetherLoot = true;

	@Config(config = "world", category = "loot", key = "enableEndLoot", comment = "When true TechReborn will add ingots, machine frames and circuits to The End loot chests.")
	public static boolean enableEndLoot = true;

	@Config(config = "world", category = "ore", key = "enableBauxiteOre", comment = "Generate Bauxite Ore in The Overworld.")
	public static boolean enableBauxiteOre = true;

	@Config(config = "world", category = "ore", key = "bauxiteVeinCount", comment = "Count of Bauxite Ore veins per chunk.")
	public static int bauxiteVeinCount = 10;

	@Config(config = "world", category = "ore", key = "bauxiteVeinSize", comment = "Amount of Bauxite Ores per vein.")
	public static int bauxiteVeinSize = 6;

	@Config(config = "world", category = "ore", key = "enableCopperOre", comment = "Generate Copper Ore in The Overworld.")
	public static boolean enableCopperOre = true;

	@Config(config = "world", category = "ore", key = "copperVeinCount", comment = "Count of Copper Ore veins per chunk.")
	public static int copperVeinCount = 16;

	@Config(config = "world", category = "ore", key = "copperVeinSize", comment = "Amount of Copper Ores per vein.")
	public static int copperVeinSize = 8;

	@Config(config = "world", category = "ore", key = "enableGalenaOre", comment = "Generate Galena Ore in The Overworld.")
	public static boolean enableGalenaOre = true;

	@Config(config = "world", category = "ore", key = "galenaVeinCount", comment = "Count of Galena Ore veins per chunk.")
	public static int galenaVeinCount = 16;

	@Config(config = "world", category = "ore", key = "galenaVeinSize", comment = "Amount of Galena Ores per vein.")
	public static int galenaVeinSize = 8;

	@Config(config = "world", category = "ore", key = "enableIridiumOre", comment = "Generate Iridium Ore in The Overworld.")
	public static boolean enableIridiumOre = true;

	@Config(config = "world", category = "ore", key = "iridiumVeinCount", comment = "Count of Iridium Ore veins per chunk.")
	public static int iridiumVeinCount = 3;

	@Config(config = "world", category = "ore", key = "iridiumVeinSize", comment = "Amount of Iridium Ores per vein.")
	public static int iridiumVeinSize = 3;

	@Config(config = "world", category = "ore", key = "enableLeadOre", comment = "Generate Lead Ore in The Overworld.")
	public static boolean enableLeadOre = true;

	@Config(config = "world", category = "ore", key = "leadVeinCount", comment = "Count of Lead Ore veins per chunk.")
	public static int leadVeinCount = 16;

	@Config(config = "world", category = "ore", key = "leadVeinSize", comment = "Amount of Lead Ores per vein.")
	public static int leadVeinSize = 6;

	@Config(config = "world", category = "ore", key = "enableRubyOre", comment = "Generate Ruby Ore in The Overworld.")
	public static boolean enableRubyOre = true;

	@Config(config = "world", category = "ore", key = "rubyVeinCount", comment = "Count of Ruby Ore veins per chunk.")
	public static int rubyVeinCount = 3;

	@Config(config = "world", category = "ore", key = "rubyVeinSize", comment = "Amount of Ruby Ores per vein.")
	public static int rubyVeinSize = 6;

	@Config(config = "world", category = "ore", key = "enableSapphireOre", comment = "Generate Sapphire Ore in The Overworld.")
	public static boolean enableSapphireOre = true;

	@Config(config = "world", category = "ore", key = "sapphireVeinCount", comment = "Count of Sapphire Ore veins per chunk.")
	public static int sapphireVeinCount = 3;

	@Config(config = "world", category = "ore", key = "sapphireVeinSize", comment = "Amount of Sapphire Ores per vein.")
	public static int sapphireVeinSize = 6;

	@Config(config = "world", category = "ore", key = "enableSilverOre", comment = "Generate Silver Ore in The Overworld.")
	public static boolean enableSilverOre = true;

	@Config(config = "world", category = "ore", key = "silverVeinCount", comment = "Count of Silver Ore veins per chunk.")
	public static int silverVeinCount = 16;

	@Config(config = "world", category = "ore", key = "silverVeinSize", comment = "Amount of Silver Ores per vein.")
	public static int silverVeinSize = 6;

	@Config(config = "world", category = "ore", key = "enableTinOre", comment = "Generate Tin Ore in The Overworld.")
	public static boolean enableTinOre = true;

	@Config(config = "world", category = "ore", key = "tinVeinCount", comment = "Count of Tin Ore veins per chunk.")
	public static int tinVeinCount = 16;

	@Config(config = "world", category = "ore", key = "tinVeinSize", comment = "Amount of Tin Ores per vein.")
	public static int tinVeinSize = 8;

	@Config(config = "world", category = "ore", key = "enableCinnabarOre", comment = "Generate Cinnabar Ore in The Nether.")
	public static boolean enableCinnabarOre = true;

	@Config(config = "world", category = "ore", key = "cinnabarVeinCount", comment = "Count of Cinnabar Ore veins per chunk.")
	public static int cinnabarVeinCount = 3;

	@Config(config = "world", category = "ore", key = "cinnabarVeinSize", comment = "Amount of Cinnabar Ores per vein.")
	public static int cinnabarVeinSize = 6;

	@Config(config = "world", category = "ore", key = "enablePyriteOre", comment = "Generate Pyrite Ore in The Nether.")
	public static boolean enablePyriteOre = true;

	@Config(config = "world", category = "ore", key = "pyriteVeinCount", comment = "Count of Pyrite Ore veins per chunk.")
	public static int pyriteVeinCount = 3;

	@Config(config = "world", category = "ore", key = "pyriteVeinSize", comment = "Amount of Pyrite Ores per vein.")
	public static int pyriteVeinSize = 6;

	@Config(config = "world", category = "ore", key = "enableSphaleriteOre", comment = "Generate Sphalerite Ore in The Nether.")
	public static boolean enableSphaleriteOre = true;

	@Config(config = "world", category = "ore", key = "sphaleriteVeinCount", comment = "Count of Sphalerite Ore veins per chunk.")
	public static int sphaleriteVeinCount = 3;

	@Config(config = "world", category = "ore", key = "sphaleriteVeinSize", comment = "Amount of Sphalerite Ores per vein.")
	public static int sphaleriteVeinSize = 6;

	@Config(config = "world", category = "ore", key = "enablePeridotOre", comment = "Generate Peridot Ore in The End.")
	public static boolean enablePeridotOre = true;

	@Config(config = "world", category = "ore", key = "peridotVeinCount", comment = "Count of Peridot Ore veins per chunk.")
	public static int peridotVeinCount = 3;

	@Config(config = "world", category = "ore", key = "peridotVeinSize", comment = "Amount of Peridot Ores per vein.")
	public static int peridotVeinSize = 6;

	@Config(config = "world", category = "ore", key = "enableSheldoniteOre", comment = "Generate Sheldonite Ore in The End.")
	public static boolean enableSheldoniteOre = true;

	@Config(config = "world", category = "ore", key = "sheldoniteVeinCount", comment = "Count of Sheldonite Ore veins per chunk.")
	public static int sheldoniteVeinCount = 3;

	@Config(config = "world", category = "ore", key = "sheldoniteVeinSize", comment = "Amount of Sheldonite Ores per vein.")
	public static int sheldoniteVeinSize = 6;

	@Config(config = "world", category = "ore", key = "enableSodaliteOre", comment = "Generate Sodalite Ore in The End.")
	public static boolean enableSodaliteOre = true;

	@Config(config = "world", category = "ore", key = "sodaliteVeinCount", comment = "Count of Sodalite Ore veins per chunk.")
	public static int sodaliteVeinCount = 3;

	@Config(config = "world", category = "ore", key = "sodaliteVeinSize", comment = "Amount of Sodalite Ores per vein.")
	public static int sodaliteVeinSize = 6;

	@Config(config = "world", category = "ore", key = "enableTungstenOre", comment = "Generate Tungsten Ore in The End.")
	public static boolean enableTungstenOre = true;

	@Config(config = "world", category = "ore", key = "tungstenVeinCount", comment = "Count of Tungsten Ore veins per chunk.")
	public static int tungstenVeinCount = 3;

	@Config(config = "world", category = "ore", key = "tungstenVeinSize", comment = "Amount of Tungsten Ores per vein.")
	public static int tungstenVeinSize = 6;

	@Config(config = "world", category = "rubber_tree", key = "rubberTreeChance", comment = "Chance to spawn rubber tree")
	public static int rubberTreeChance = 50;

	@Config(config = "world", category = "rubber_tree", key = "rubberTreeBaseHeight", comment = "Basic height for not-spire part of rubber tree")
	public static int rubberTreeBaseHeight = 6;

	@Config(config = "world", category = "rubber_tree", key = "rubberTreeSpireHeight", comment = "Height of spire of rubber tree")
	public static int rubberTreeSpireHeight = 3;
}
