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

// All moved into one class as it's a lot easier to find the annotations when you know where they all are
public class TechRebornConfig {

	// Generators
	@Config(config = "generators", category = "solarPanelGeneral", key = "internalCapacity", comment = "Multiplier for internal capacity of solar panels (multiplier * day generation rate)")
	public static int solarInternalCapacityMultiplier = 2000;

	@Config(config = "generators", category = "solarPanelBasic", key = "basicDayRate", comment = "Generation rate during day for Basic Solar Panel (Energy per tick)")
	public static int basicGenerationRateD = 3;

	@Config(config = "generators", category = "solarPanelBasic", key = "basicNightRate", comment = "Generation rate during night for Basic Solar Panel (Energy per tick)")
	public static int basicGenerationRateN = 0;

	@Config(config = "generators", category = "solarPanelAdvanced", key = "advancedDayRate", comment = "Generation rate during day for Advanced Solar Panel (Energy per tick)")
	public static int advancedGenerationRateD = 12;

	@Config(config = "generators", category = "solarPanelAdvanced", key = "advancedNightRate", comment = "Generation rate during night for Advanced Solar Panel (Energy per tick)")
	public static int advancedGenerationRateN = 0;

	@Config(config = "generators", category = "solarPanelIndustrial", key = "industrialDayRate", comment = "Generation rate during day for Industrial Solar Panel (Energy per tick)")
	public static int industrialGenerationRateD = 24;

	@Config(config = "generators", category = "solarPanelIndustrial", key = "industrialNightRate", comment = "Generation rate during night for Industrial Solar Panel (Energy per tick)")
	public static int industrialGenerationRateN = 4;

	@Config(config = "generators", category = "solarPanelUltimate", key = "ultimateDayRate", comment = "Generation rate during day for Ultimate Solar Panel (Energy per tick)")
	public static int ultimateGenerationRateD = 48;

	@Config(config = "generators", category = "solarPanelUltimate", key = "ultimateNightRate", comment = "Generation rate during night for Ultimate Solar Panel (Energy per tick)")
	public static int ultimateGenerationRateN = 8;

	@Config(config = "generators", category = "solarPanelQuantum", key = "quantumDayRate", comment = "Generation rate during day for Quantum Solar Panel (Energy per tick)")
	public static int quantumGenerationRateD = 512;

	@Config(config = "generators", category = "solarPanelQuantum", key = "quantumNightRate", comment = "Generation rate during night for Quantum Solar Panel (Energy per tick)")
	public static int quantumGenerationRateN = 32;

	@Config(config = "generators", category = "lightning_rod", key = "LightningRodMaxOutput", comment = "Lightning Rod Max Output (Energy per tick)")
	public static int lightningRodMaxOutput = 2048;

	@Config(config = "generators", category = "lightning_rod", key = "LightningRodMaxEnergy", comment = "Lightning Rod Max Energy")
	public static int lightningRodMaxEnergy = 100_000_000;

	@Config(config = "generators", category = "lightning_rod", key = "LightningRodChanceOfStrike", comment = "Chance of lightning striking a rod (Range: 0-70)")
	public static int lightningRodChanceOfStrike = 24;

	@Config(config = "generators", category = "lightning_rod", key = "LightningRodBaseStrikeEnergy", comment = "Base amount of Energy per strike")
	public static int lightningRodBaseEnergyStrike = 262_144;

	@Config(config = "generators", category = "thermal_generator", key = "ThermalGeneratorMaxOutput", comment = "Thermal Generator Max Output (Energy per tick)")
	public static int thermalGeneratorMaxOutput = 128;

	@Config(config = "generators", category = "thermal_generator", key = "ThermalGeneratorMaxEnergy", comment = "Thermal Generator Max Energy")
	public static int thermalGeneratorMaxEnergy = 1_000_000;

	@Config(config = "generators", category = "thermal_generator", key = "ThermalGeneratorEnergyPerTick", comment = "Thermal Generator Energy Per Tick")
	public static int thermalGeneratorEnergyPerTick = 16;

	@Config(config = "generators", category = "plasma_generator", key = "PlasmaGeneratorMaxOutput", comment = "Plasma Generator Max Output (Energy per tick)")
	public static int plasmaGeneratorMaxOutput = 2048;

	@Config(config = "generators", category = "plasma_generator", key = "PlasmaGeneratorMaxEnergy", comment = "Plasma Generator Max Energy")
	public static long plasmaGeneratorMaxEnergy = 500_000_000;

	@Config(config = "generators", category = "plasma_generator", key = "PlasmaGeneratorEnergyPerTick", comment = "Plasma Generator Energy Per Tick")
	public static int plasmaGeneratorEnergyPerTick = 400;

	@Config(config = "generators", category = "wind_mill", key = "WindMillMaxOutput", comment = "Wind Mill Max Output (Energy per tick)")
	public static int windMillMaxOutput = 128;

	@Config(config = "generators", category = "wind_mill", key = "WindMillMaxEnergy", comment = "Wind Mill Max Energy")
	public static int windMillMaxEnergy = 10_000;

	@Config(config = "generators", category = "wind_mill", key = "WindMillEnergyPerTick", comment = "Wind Mill generation rate (Energy Per Tick)")
	public static int windMillBaseEnergy = 2;

	@Config(config = "generators", category = "wind_mill", key = "WindMillThunderMultiplier", comment = "Wind Mill Thunder Multiplier")
	public static double windMillThunderMultiplier = 1.25;

	@Config(config = "generators", category = "water_mill", key = "WaterMillMaxOutput", comment = "Water Mill Max Output (Energy per tick)")
	public static int waterMillMaxOutput = 32;

	@Config(config = "generators", category = "water_mill", key = "WaterMillMaxEnergy", comment = "Water Mill Max Energy")
	public static int waterMillMaxEnergy = 1000;

	@Config(config = "generators", category = "water_mill", key = "WaterMillEnergyPerTick", comment = "Water Mill generation rate per water block (Energy per tick)")
	public static double waterMillEnergyMultiplier = 0.1;

	@Config(config = "generators", category = "semifluid_generator", key = "SemifluidGeneratorMaxOutput", comment = "Semifluid Generator Max Output (Energy per tick)")
	public static int semiFluidGeneratorMaxOutput = 128;

	@Config(config = "generators", category = "semifluid_generator", key = "SemifluidGeneratorMaxEnergy", comment = "Semifluid Generator Max Energy")
	public static int semiFluidGeneratorMaxEnergy = 1000000;

	@Config(config = "generators", category = "semifluid_generator", key = "SemifluidGeneratorEnergyPerTick", comment = "Semifluid Generator Energy Per Tick")
	public static int semiFluidGeneratorEnergyPerTick = 8;

	@Config(config = "generators", category = "gas_generator", key = "GasGeneratorMaxOutput", comment = "Gas Generator Max Output (Energy per tick)")
	public static int gasTurbineMaxOutput = 128;

	@Config(config = "generators", category = "gas_generator", key = "GasGeneratorMaxEnergy", comment = "Gas Generator Max Energy")
	public static int gasTurbineMaxEnergy = 1000000;

	@Config(config = "generators", category = "gas_generator", key = "GasGeneratorEnergyPerTick", comment = "Gas Generator Energy Per Tick")
	public static int gasTurbineEnergyPerTick = 16;

	@Config(config = "generators", category = "diesel_generator", key = "DieselGeneratorMaxOutput", comment = "Diesel Generator Max Output (Energy per tick)")
	public static int dieselGeneratorMaxOutput = 32;

	@Config(config = "generators", category = "diesel_generator", key = "DieselGeneratorMaxEnergy", comment = "Diesel Generator Max Energy")
	public static int dieselGeneratorMaxEnergy = 10_000;

	@Config(config = "generators", category = "diesel_generator", key = "DieselGeneratorEnergyPerTick", comment = "Diesel Generator Energy Per Tick")
	public static int dieselGeneratorEnergyPerTick = 20;

	@Config(config = "generators", category = "dragon_egg_siphoner", key = "DragonEggSiphonerMaxOutput", comment = "Dragon Egg Siphoner Max Output (Energy per tick)")
	public static int dragonEggSyphonMaxOutput = 128;

	@Config(config = "generators", category = "dragon_egg_siphoner", key = "DragonEggSiphonerMaxEnergy", comment = "Dragon Egg Siphoner Max Energy")
	public static int dragonEggSyphonMaxEnergy = 1000;

	@Config(config = "generators", category = "dragon_egg_siphoner", key = "DragonEggSiphonerEnergyPerTick", comment = "Dragon Egg Siphoner Energy Per Tick")
	public static int dragonEggSyphonEnergyPerTick = 4;

	@Config(config = "generators", category = "generator", key = "GeneratorMaxOutput", comment = "Solid Fuel Generator Max Output (Energy per tick)")
	public static int solidFuelGeneratorMaxOutput = 32;

	@Config(config = "generators", category = "generator", key = "GeneratorMaxEnergy", comment = "Solid Fuel Generator Max Energy")
	public static int solidFuelGeneratorMaxEnergy = 10_000;

	@Config(config = "generators", category = "generator", key = "GeneratorEnergyOutput", comment = "Solid Fuel Generator Energy Per Tick")
	public static int solidFuelGeneratorOutputAmount = 10;

	// Items
	@Config(config = "items", category = "power", key = "nanoSaberCharge", comment = "Energy Capacity for Nano Saber")
	public static int nanosaberCharge = 1_000_000;

	@Config(config = "items", category = "power", key = "nanoSaberCost", comment = "Energy Cost for Nano Saber")
	public static int nanosaberCost = 150;

	@Config(config = "items", category = "power", key = "nanoSaberDamage", comment = "Damage value for the Nano Saber")
	public static int nanosaberDamage = 20;

	@Config(config = "items", category = "power", key = "electricTreetapCharge", comment = "Energy Capacity for Electric Treetap")
	public static int electricTreetapCharge = 10_000;

	@Config(config = "items", category = "power", key = "electricTreetapCost", comment = "Energy Cost for Electric Treetap")
	public static int electricTreetapCost = 20;

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

	@Config(config = "items", category = "power", key = "rockCutterCharge", comment = "Energy Capacity for Rock Cutter")
	public static int rockCutterCharge = 10_000;

	@Config(config = "items", category = "power", key = "rockCutterCost", comment = "Energy Cost for Rock Cutter")
	public static int rockCutterCost = 10;

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

	@Config(config = "items", category = "power", key = "cloakingDeviceCharge", comment = "Energy Capacity for Cloaking Device")
	public static long cloakingDeviceCharge = 40_000_000;

	@Config(config = "items", category = "power", key = "clockingDeviceEnergyUsage", comment = "Cloaking device energy usage")
	public static int cloakingDeviceCost = 10;

	@Config(config = "items", category = "power", key = "quantumSuitCapacity", comment = "Quantum Suit Energy Capacity")
	public static long quantumSuitCapacity = 40_000_000;

	@Config(config = "items", category = "power", key = "quantumSuitFlyingCost", comment = "Quantum Suit Flying Cost")
	public static long quantumSuitFlyingCost = 50;

	@Config(config = "items", category = "power", key = "quantumSuitSwimmingCost", comment = "Quantum Suit Swimming Cost")
	public static long quantumSuitSwimmingCost = 20;

	@Config(config = "items", category = "power", key = "quantumSuitBreathingCost", comment = "Quantum Suit Breathing Cost")
	public static long quantumSuitBreathingCost = 20;

	@Config(config = "items", category = "power", key = "quantumSuitSprintingCost", comment = "Quantum Suit Sprinting Cost")
	public static long quantumSuitSprintingCost = 20;

	@Config(config = "items", category = "power", key = "quantumSuitFireExtinguishCost", comment = "Quantum Suit Cost for Fire Extinguish")
	public static long fireExtinguishCost = 50;

	@Config(config = "items", category = "power", key = "quantumSuitEnableSprint", comment = "Enable Sprint Speed increase for Quantum Legs")
	public static boolean quantumSuitEnableSprint = true;

	@Config(config = "items", category = "power", key = "quantumSuitEnableFlight", comment = "Enable Flight for Quantum Chest")
	public static boolean quantumSuitEnableFlight = true;

	@Config(config = "items", category = "power", key = "quantumSuitDamageAbsorbCost", comment = "Quantum Suit Cost for Damage Absorbed")
	public static double damageAbsorbCost = 10;

	@Config(config = "items", category = "power", key = "nanoSuitCapacity", comment = "Nano Suit Energy Capacity")
	public static long nanoSuitCapacity = 1_000_000;

	@Config(config = "items", category = "power", key = "suitNightVisionCost", comment = "Nano/Quantum Suit Night Vision Cost")
	public static long suitNightVisionCost = 1;

	@Config(config = "items", category = "upgrades", key = "overclocker_speed", comment = "Overclocker behavior speed multiplier")
	public static double overclockerSpeed = 0.25;

	@Config(config = "items", category = "upgrades", key = "overclocker_power", comment = "Overclocker behavior power multiplier")
	public static double overclockerPower = 0.75;

	@Config(config = "items", category = "upgrades", key = "energy_storage", comment = "Energy storage behavior extra power")
	public static double energyStoragePower = 40_000;

	@Config(config = "items", category = "upgrades", key = "super_conductor", comment = "Energy flow power increase")
	public static double superConductorCount = 1;

	// Storages
	@Config(config = "machines", category = "lesu", key = "LesuMaxEnergyPerBlock", comment = "LESU Max Energy Per Block")
	public static int lesuStoragePerBlock = 4_000_000;

	@Config(config = "machines", category = "lesu", key = "LesuExtraIO", comment = "LESU Extra I/O Multiplier")
	public static int lesuExtraIOPerBlock = 64;

	@Config(config = "machines", category = "lesu", key = "LesuBaseOutput", comment = "LESU Base Output")
	public static int lesuBaseOutput = 64;

	@Config(config = "machines", category = "aesu", key = "AesuMaxEnergy", comment = "AESU Max Energy")
	public static int aesuMaxEnergy = 100_000_000;

	@Config(config = "machines", category = "idsu", key = "IdsuMaxEnergy", comment = "IDSU Max Energy")
	public static int idsuMaxEnergy = 1_000_000_000;

	@Config(config = "machines", category = "storage", key = "CrudeStorageUnitMaxStorage", comment = "Maximum amount of items a Crude Storage Unit can store")
	public static int crudeStorageUnitMaxStorage = 1 << 11; // 2^11, around 2,000, holds 2^5=32 64-stacks

	@Config(config = "machines", category = "storage", key = "BasicStorageUnitMaxStorage", comment = "Maximum amount of items a Basic Storage Unit can store")
	public static int basicStorageUnitMaxStorage = 1 << 13; // 2^13, around 8,000, holds 2^7=128 64-stacks

	@Config(config = "machines", category = "storage", key = "BasicTankUnitCapacity", comment = "How much liquid a Basic Tank Unit can take (Value in buckets, 1000 Mb)")
	public static int basicTankUnitCapacity = 1 << 7; // 2^7=128, holds 2^3=8 16-stacks cells (content only)

	@Config(config = "machines", category = "storage", key = "AdvancedStorageMaxStorage", comment = "Maximum amount of items an Advanced Storage Unit can store")
	public static int advancedStorageUnitMaxStorage = 1 << 15; // 2^15, around 32,000, holds 2^9=512 64-stacks

	@Config(config = "machines", category = "storage", key = "AdvancedTankUnitMaxStorage", comment = "How much liquid an Advanced Tank Unit can take (Value in buckets, 1000 Mb)")
	public static int advancedTankUnitMaxStorage = 1 << 9; // 2^9=512, holds 2^5=32 16-stacks cells (content only)

	@Config(config = "machines", category = "storage", key = "IndustrialStorageMaxStorage", comment = "Maximum amount of items an Industrial Storage Unit can store (Compat: >= 32768)")
	public static int industrialStorageUnitMaxStorage = 1 << 16; // 2^16, around 65,000, holds 2^10=1024 64-stacks

	@Config(config = "machines", category = "storage", key = "IndustrialTankUnitCapacity", comment = "How much liquid an Industrial Tank Unit can take (Value in buckets, 1000 Mb)")
	public static int industrialTankUnitCapacity = 1 << 10; // 2^10, around 1,000, holds 2^6=64 16-stacks cells (content only)

	@Config(config = "machines", category = "storage", key = "QuantumStorageUnitMaxStorage", comment = "Maximum amount of items a Quantum Storage Unit can store (Compat: == MAX_VALUE)")
	public static int quantumStorageUnitMaxStorage = Integer.MAX_VALUE;

	@Config(config = "machines", category = "storage", key = "QuantumTankUnitCapacity", comment = "How much liquid a Quantum Tank Unit can take (Value in buckets, 1000 Mb)(Compat: == MAX_VALUE)")
	public static int quantumTankUnitCapacity = Integer.MAX_VALUE / 1000;


	// Machines
	@Config(config = "machines", category = "player_detector", key = "PlayerDetectorMaxInput", comment = "Player Detector Max Input (Energy per tick)")
	public static int playerDetectorMaxInput = 32;

	@Config(config = "machines", category = "player_detector", key = "PlayerDetectorMaxEnergy", comment = "Player Detector Max Energy")
	public static int playerDetectorMaxEnergy = 10000;

	@Config(config = "machines", category = "player_detector", key = "PlayerDetectorEnergyUsage", comment = "Player Detector Energy Consumption per second")
	public static int playerDetectorEuPerTick = 1;

	@Config(config = "machines", category = "player_detector", key = "PlayerDetectorMaxRadius", comment = "Player Detector maximum detection radius")
	public static int playerDetectorMaxRadius = 128;

	@Config(config = "machines", category = "Distillation_tower", key = "DistillationTowerMaxInput", comment = "Distillation Tower Max Input (Energy per tick)")
	public static int distillationTowerMaxInput = 128;

	@Config(config = "machines", category = "Distillation_tower", key = "DistillationTowerMaxEnergy", comment = "Distillation Tower Max Energy")
	public static int distillationTowerMaxEnergy = 10_000;

	@Config(config = "machines", category = "extractor", key = "ExtractorInput", comment = "Extractor Max Input (Energy per tick)")
	public static int extractorMaxInput = 32;

	@Config(config = "machines", category = "extractor", key = "ExtractorMaxEnergy", comment = "Extractor Max Energy")
	public static int extractorMaxEnergy = 1_000;

	@Config(config = "machines", category = "grinder", key = "GrinderInput", comment = "Grinder Max Input (Energy per tick)")
	public static int grinderMaxInput = 32;

	@Config(config = "machines", category = "grinder", key = "GrinderMaxEnergy", comment = "Grinder Max Energy")
	public static int grinderMaxEnergy = 1_000;

	@Config(config = "machines", category = "compressor", key = "CompressorInput", comment = "Compressor Max Input (Energy per tick)")
	public static int compressorMaxInput = 32;

	@Config(config = "machines", category = "compressor", key = "CompressorMaxEnergy", comment = "Compressor Max Energy")
	public static int compressorMaxEnergy = 1000;

	@Config(config = "machines", category = "alloy_smelter", key = "AlloySmelterMaxInput", comment = "Alloy Smelter Max Input (Energy per tick)")
	public static int alloySmelterMaxInput = 32;

	@Config(config = "machines", category = "alloy_smelter", key = "AlloySmelterMaxEnergy", comment = "Alloy Smelter Max Energy")
	public static int alloySmelterMaxEnergy = 1_000;

	@Config(config = "machines", category = "rolling_machine", key = "RollingMachineMaxInput", comment = "Rolling Machine Max Input (Energy per tick)")
	public static int rollingMachineMaxInput = 32;

	@Config(config = "machines", category = "rolling_machine", key = "RollingMachineMaxEnergy", comment = "Rolling Machine Max Energy")
	public static int rollingMachineMaxEnergy = 10000;

	@Config(config = "machines", category = "chunk_loader", key = "ChunkLoaderMaxRadius", comment = "Chunk Loader Max Radius")
	public static int chunkLoaderMaxRadius = 5;

	@Config(config = "machines", category = "assembling_machine", key = "AssemblingMachineMaxInput", comment = "Assembling Machine Max Input (Energy per tick)")
	public static int assemblingMachineMaxInput = 128;

	@Config(config = "machines", category = "assembling_machine", key = "AssemblingMachineMaxEnergy", comment = "Assembling Machine Max Energy")
	public static int assemblingMachineMaxEnergy = 10_000;

	@Config(config = "machines", category = "matter_fabricator", key = "MatterFabricatorMaxInput", comment = "Matter Fabricator Max Input (Energy per tick)")
	public static int matterFabricatorMaxInput = 8192;

	@Config(config = "machines", category = "matter_fabricator", key = "MatterFabricatorMaxEnergy", comment = "Matter Fabricator Max Energy")
	public static int matterFabricatorMaxEnergy = 10_000_000;

	@Config(config = "machines", category = "matter_fabricator", key = "MatterFabricatorFabricationRate", comment = "Matter Fabricator Fabrication Rate, amount of amplifier units per UUM")
	public static int matterFabricatorFabricationRate = 6_000;

	@Config(config = "machines", category = "matter_fabricator", key = "MatterFabricatorEnergyPerAmp", comment = "Matter Fabricator EU per amplifier unit, multiply this with the rate for total Energy")
	public static int matterFabricatorEnergyPerAmp = 5;

	@Config(config = "machines", category = "industrial_grinder", key = "IndustrialGrinderMaxInput", comment = "Industrial Grinder Max Input (Energy per tick)")
	public static int industrialGrinderMaxInput = 128;

	@Config(config = "machines", category = "industrial_grinder", key = "IndustrialGrinderMaxEnergy", comment = "Industrial Grinder Max Energy")
	public static int industrialGrinderMaxEnergy = 10_000;

	@Config(config = "machines", category = "vacuumfreezer", key = "VacuumFreezerInput", comment = "Vacuum Freezer Max Input (Energy per tick)")
	public static int vacuumFreezerMaxInput = 64;

	@Config(config = "machines", category = "vacuumfreezer", key = "VacuumFreezerMaxEnergy", comment = "Vacuum Freezer Max Energy")
	public static int vacuumFreezerMaxEnergy = 64_000;

	@Config(config = "machines", category = "implosion_compressor", key = "ImplosionCompressorMaxInput", comment = "Implosion Compressor Max Input (Energy per tick)")
	public static int implosionCompressorMaxInput = 64;

	@Config(config = "machines", category = "implosion_compressor", key = "ImplosionCompressorMaxEnergy", comment = "Implosion Compressor Max Energy")
	public static int implosionCompressorMaxEnergy = 64_000;

	@Config(config = "machines", category = "industrial_furnace", key = "IndustrialFurnaceMaxInput", comment = "Industrial Blast Furnace Max Input (Energy per tick)")
	public static int industrialBlastFurnaceMaxInput = 128;

	@Config(config = "machines", category = "industrial_furnace", key = "IndustrialFurnaceMaxEnergy", comment = "Industrial Blast Furnace Max Energy")
	public static int industrialBlastFurnaceMaxEnergy = 40_000;

	@Config(config = "machines", category = "industrial_sawmill", key = "IndustrialSawmillMaxInput", comment = "Industrial Sawmill Max Input (Energy per tick)")
	public static int industrialSawmillMaxInput = 128;

	@Config(config = "machines", category = "industrial_sawmill", key = "IndustrialSawmillMaxEnergy", comment = "Industrial Sawmill Max Energy")
	public static int industrialSawmillMaxEnergy = 10_000;

	@Config(config = "machines", category = "autocrafter", key = "AutoCrafterInput", comment = "AutoCrafting Table Max Input (Energy per tick)")
	public static int autoCraftingTableMaxInput = 32;

	@Config(config = "machines", category = "autocrafter", key = "AutoCrafterMaxEnergy", comment = "AutoCrafting Table Max Energy")
	public static int autoCraftingTableMaxEnergy = 10_000;

	@Config(config = "machines", category = "fluidreplicator", key = "FluidReplicatorMaxInput", comment = "Fluid Replicator Max Input (Energy per tick)")
	public static int fluidReplicatorMaxInput = 256;

	@Config(config = "machines", category = "fluidreplicator", key = "FluidReplicatorMaxEnergy", comment = "Fluid Replicator Max Energy")
	public static int fluidReplicatorMaxEnergy = 400_000;

	@Config(config = "machines", category = "electric_furnace", key = "ElectricFurnaceInput", comment = "Electric Furnace Max Input (Energy per tick)")
	public static int electricFurnaceMaxInput = 32;

	@Config(config = "machines", category = "electric_furnace", key = "ElectricFurnaceMaxEnergy", comment = "Electric Furnace Max Energy")
	public static int electricFurnaceMaxEnergy = 1000;

	@Config(config = "machines", category = "charge_bench", key = "ChargeBenchMaxOutput", comment = "Charge Bench Max Output (Energy per tick)")
	public static int chargeOMatBMaxOutput = 512;

	@Config(config = "machines", category = "charge_bench", key = "ChargeBenchMaxInput", comment = "Charge Bench Max Input (Energy per tick)")
	public static int chargeOMatBMaxInput = 512;

	@Config(config = "machines", category = "charge_bench", key = "ChargeBenchMaxEnergy", comment = "Charge Bench Max Energy")
	public static int chargeOMatBMaxEnergy = 100_000_000;

	@Config(config = "machines", category = "industrial_electrolyzer", key = "IndustrialElectrolyzerMaxInput", comment = "Industrial Electrolyzer Max Input (Energy per tick)")
	public static int industrialElectrolyzerMaxInput = 128;

	@Config(config = "machines", category = "industrial_electrolyzer", key = "IndustrialElectrolyzerMaxEnergy", comment = "Industrial Electrolyzer Max Energy")
	public static int industrialElectrolyzerMaxEnergy = 10_000;

	@Config(config = "machines", category = "centrifuge", key = "CentrifugeMaxInput", comment = "Centrifuge Max Input (Energy per tick)")
	public static int industrialCentrifugeMaxInput = 32;

	@Config(config = "machines", category = "centrifuge", key = "CentrifugeMaxEnergy", comment = "Centrifuge Max Energy")
	public static int industrialCentrifugeMaxEnergy = 10_000;

	@Config(config = "machines", category = "chemical_reactor", key = "ChemicalReactorMaxInput", comment = "Chemical Reactor Max Input (Energy per tick)")
	public static int chemicalReactorMaxInput = 128;

	@Config(config = "machines", category = "chemical_reactor", key = "ChemicalReactorMaxEnergy", comment = "Chemical Reactor Max Energy")
	public static int chemicalReactorMaxEnergy = 10_000;

	@Config(config = "machines", category = "fusion_reactor", key = "FusionReactorMaxInput", comment = "Fusion Reactor Max Input (Energy per tick)")
	public static int fusionControlComputerMaxInput = 8192;

	@Config(config = "machines", category = "fusion_reactor", key = "FusionReactorMaxOutput", comment = "Fusion Reactor Max Output (Energy per tick)")
	public static int fusionControlComputerMaxOutput = 1_000_000;

	@Config(config = "machines", category = "fusion_reactor", key = "FusionReactorMaxEnergy", comment = "Fusion Reactor Max Energy")
	public static int fusionControlComputerMaxEnergy = 100_000_000;

	@Config(config = "machines", category = "fusion_reactor", key = "FusionReactorMaxCoilSize", comment = "Fusion Reactor Max Coil size (Radius)")
	public static int fusionControlComputerMaxCoilSize = 50;

	@Config(config = "machines", category = "recycler", key = "RecyclerInput", comment = "Recycler Max Input (Energy per tick)")
	public static int recyclerMaxInput = 32;

	@Config(config = "machines", category = "recycler", key = "RecyclerMaxEnergy", comment = "Recycler Max Energy")
	public static int recyclerMaxEnergy = 1000;

	@Config(config = "machines", category = "recycler", key = "RecyclerChance", comment = "Recycler Chance to produce scrap (1 out of chance)")
	public static int recyclerChance = 6;

	@Config(config = "machines", category = "recycler", key = "RecyclerBlacklist", comment = "Recycler blacklist")
	public static List<String> recyclerBlackList = Arrays.asList("techreborn:scrap_box", "techreborn:scrap");

	@Config(config = "machines", category = "scrapboxinator", key = "ScrapboxinatorMaxInput", comment = "Scrapboxinator Max Input (Energy per tick)")
	public static int scrapboxinatorMaxInput = 32;

	@Config(config = "machines", category = "scrapboxinator", key = "ScrapboxinatorMaxEnergy", comment = "Scrapboxinator Max Energy")
	public static int scrapboxinatorMaxEnergy = 1_000;

	@Config(config = "machines", category = "solid_canning_machine", key = "solidCanningMachineMaxInput", comment = "Solid Canning Machine Max Input (Energy per tick)")
	public static int solidCanningMachineMaxInput = 32;

	@Config(config = "machines", category = "solid_canning_machine", key = "solidCanningMachineMaxEnergy", comment = "Solid Canning Machine Max Energy")
	public static int solidCanningMachineMaxEnergy = 1_000;

	@Config(config = "machines", category = "iron_machine", key = "fuel_scale", comment = "Multiplier for vanilla furnace item burn time")
	public static double fuelScale = 1.25;

	@Config(config = "machines", category = "iron_machine", key = "cooking_scale", comment = "Multiplier for vanilla furnace item cook time")
	public static double cookingScale = 1.25;

	@Config(config = "machines", category = "greenhouse_controller", key = "GreenhouseControllerMaxInput", comment = "Greenhouse Controller Max Input (Energy per tick)")
	public static int greenhouseControllerMaxInput = 32;

	@Config(config = "machines", category = "greenhouse_controller", key = "GreenhouseControllerMaxEnergy", comment = "Greenhouse Controller Max Energy")
	public static int greenhouseControllerMaxEnergy = 1_000;

	@Config(config = "machines", category = "greenhouse_controller", key = "GreenhouseControllerEnergyPerTick", comment = "Greenhouse Controller Energy usage Per Tick")
	public static int greenhouseControllerEnergyPerTick = 2;

	@Config(config = "machines", category = "greenhouse_controller", key = "GreenhouseControllerEnergyPerHarvest", comment = "Greenhouse Controller Energy usage Per Harvest")
	public static int greenhouseControllerEnergyPerHarvest = 100;

	@Config(config = "machines", category = "greenhouse_controller", key = "GreenhouseControllerEnergyPerBonemeal", comment = "Greenhouse Controller Energy usage Per Bonemeal")
	public static int greenhouseControllerEnergyPerBonemeal = 50;

	@Config(config = "machines", category = "drain", key = "TicksUntilNextDrainAttempt", comment = "How many ticks should go between two drain attempts. 0 or negative will disable drain.")
	public static int ticksUntilNextDrainAttempt = 10;

	@Config(config = "machines", category = "block_breaker", key = "BlockBreakerMaxInput", comment = "Block Breaker Max Input (Energy per tick)")
	public static int blockBreakerMaxInput = 32;

	@Config(config = "machines", category = "block_breaker", key = "BlockBreakerMaxEnergy", comment = "Block Breaker Max Energy")
	public static int blockBreakerMaxEnergy = 1_000;

	@Config(config = "machines", category = "block_breaker", key = "BlockBreakerEnergyPerTick", comment = "Block Breaker Energy usage Per Tick")
	public static int blockBreakerEnergyPerTick = 5;

	@Config(config = "machines", category = "block_breaker", key = "BlockBreakerBaseBreakTime", comment = "How many ticks a block of hardness 1 requires to be broken")
	public static int blockBreakerBaseBreakTime = 100;

	@Config(config = "machines", category = "block_placer", key = "BlockPlacerMaxInput", comment = "Block Placer Max Input (Energy per tick)")
	public static int blockPlacerMaxInput = 32;

	@Config(config = "machines", category = "block_placer", key = "BlockPlacerMaxEnergy", comment = "Block Placer Max Energy")
	public static int blockPlacerMaxEnergy = 1_000;

	@Config(config = "machines", category = "block_placer", key = "BlockPlacerEnergyPerTick", comment = "Block Placer Energy usage Per Tick")
	public static int blockPlacerEnergyPerTick = 5;

	@Config(config = "machines", category = "block_placer", key = "BlockPlacerBaseBreakTime", comment = "How many ticks a block of hardness 1 requires to be placed")
	public static int blockPlacerBaseBreakTime = 100;

	@Config(config = "machines", category = "launchpad", key = "LaunchpadMaxInput", comment = "Launchpad Max Input (Energy per tick)")
	public static int launchpadMaxInput = 128;

	@Config(config = "machines", category = "launchpad", key = "LaunchpadMaxEnergy", comment = "Launchpad Max Energy")
	public static int launchpadMaxEnergy = 40_000;

	@Config(config = "machines", category = "launchpad", key = "LaunchpadSpeedLow", comment = "Launchpad Low Speed")
	public static double launchpadSpeedLow = 1d;

	@Config(config = "machines", category = "launchpad", key = "LaunchpadSpeedMedium", comment = "Launchpad Medium Speed")
	public static double launchpadSpeedMedium = 3d;

	@Config(config = "machines", category = "launchpad", key = "LaunchpadSpeedHigh", comment = "Launchpad High Speed")
	public static double launchpadSpeedHigh = 5d;

	@Config(config = "machines", category = "launchpad", key = "LaunchpadSpeedExtreme", comment = "Launchpad Extreme Speed")
	public static double launchpadSpeedExtreme = 10d;

	@Config(config = "machines", category = "launchpad", key = "LaunchpadEnergyLow", comment = "Launchpad Low Energy")
	public static int launchpadEnergyLow = 1_000;

	@Config(config = "machines", category = "launchpad", key = "LaunchpadEnergyMedium", comment = "Launchpad Medium Energy")
	public static int launchpadEnergyMedium = 6_000;

	@Config(config = "machines", category = "launchpad", key = "LaunchpadEnergyHigh", comment = "Launchpad High Energy")
	public static int launchpadEnergyHigh = 10_000;

	@Config(config = "machines", category = "launchpad", key = "LaunchpadEnergyExtreme", comment = "Launchpad Extreme Energy")
	public static int launchpadEnergyExtreme = 20_000;

	@Config(config = "machines", category = "launchpad", key = "LaunchpadDefaultSelection", comment = "Launchpad Default Selection (0-3 for Low-Extreme)")
	public static int launchpadDefaultSelection = 0;

	@Config(config = "machines", category = "launchpad", key = "LaunchpadInterval", comment = "Launchpad Launch Interval in Ticks > 0")
	public static int launchpadInterval = 100; // 5 seconds

	@Config(config = "machines", category = "elevator", key = "ElevatorMaxInput", comment = "Elevator Max Input (Energy per tick)")
	public static int elevatorMaxInput = 32;

	@Config(config = "machines", category = "elevator", key = "ElevatorMaxEnergy", comment = "Elevator Max Energy")
	public static int elevatorMaxEnergy = 1_000;

	@Config(config = "machines", category = "elevator", key = "ElevatorEnergyPerBlock", comment = "Elevator Energy used per vertical block of transportation")
	public static int elevatorEnergyPerBlock = 2;

	@Config(config = "machines", category = "elevator", key = "AllowElevatingThroughBlocks", comment = "Allow elevating through blocks (i.e. non air)")
	public static boolean allowElevatingThroughBlocks = true;

	@Config(config = "machines", category = "fishing_station", key = "FishingStationMaxInput", comment = "Fishing Station Max Input (Energy per tick)")
	public static int fishingStationMaxInput = 128;

	@Config(config = "machines", category = "fishing_station", key = "FishingStationMaxEnergy", comment = "Fishing Station Max Energy")
	public static int fishingStationMaxEnergy = 10_000;

	@Config(config = "machines", category = "fishing_station", key = "FishingStationEnergyPerCatch", comment = "How much energy the Fishing Station uses per catch")
	public static int fishingStationEnergyPerCatch = 500;

	@Config(config = "machines", category = "fishing_station", key = "FishingStationInterval", comment = "Fishing Station Catch Interval in Ticks > 0")
	public static int fishingStationInterval = 400; // 20 seconds

	@Config(config = "machines", category = "pump", key = "PumpTicksToComplete", comment = "How many ticks it takes to pump a source block.")
	public static int pumpTicksToComplete = 100;

	@Config(config = "machines", category = "pump", key = "PumpMaxInput", comment = "Pump Max Input (Energy per tick)")
	public static int pumpMaxInput = 128;

	@Config(config = "machines", category = "pump", key = "PumpMaxEnergy", comment = "Pump Max Energy")
	public static int pumpMaxEnergy = 40_000;

	@Config(config = "machines", category = "pump", key = "PumpEnergyToCollect", comment = "Base amount of Energy to collect a block of fluid")
	public static int pumpEnergyToCollect = 1_000;

	@Config(config = "machines", category = "pump", key = "PumpIterateOutwards", comment = "If true then the pump will collect closest fluid and scan outwards")
	public static boolean pumpIterateOutwards = false;

	// Misc
	@Config(config = "misc", category = "general", key = "IC2TransformersStyle", comment = "Input from dots side, output from other sides, like in IC2.")
	public static boolean IC2TransformersStyle = true;

	@Config(config = "misc", category = "general", key = "MachineSoundVolume", comment = "Machines crafting sound volume (0 - disabled, 1 - max)")
	public static float machineSoundVolume = 1.0F;

	@Config(config = "misc", category = "general", key = "manualRefund", comment = "Allow refunding items used to craft the manual")
	public static boolean allowManualRefund = true;

	@Config(config = "misc", category = "general", key = "vanillaUnlockRecipes", comment = "Enable recipe unlocks only with vanilla mechanic, instead of getting all of them at once")
	public static boolean vanillaUnlockRecipes = true;

	@Config(config = "misc", category = "nuke", key = "fusetime", comment = "Nuke fuse time (ticks)")
	public static int nukeFuseTime = 400;

	@Config(config = "misc", category = "nuke", key = "radius", comment = "Nuke explosion radius")
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

	@Config(config = "world", category = "loot", key = "enableFishingJunkLoot", comment = "When true TechReborn will add items to fishing junk loot.")
	public static boolean enableFishingJunkLoot = true;

	@Config(config = "world", category = "generation", key = "enableOreGeneration", comment = "When enabled ores will generate in the world")
	public static boolean enableOreGeneration = true;

	@Config(config = "world", category = "generation", key = "enableBauxiteOreGeneration", comment = "When enabled and enableOreGeneration isn't disabled, bauxite ores will generate in the world")
	public static boolean enableBauxiteOreGeneration = true;

	@Config(config = "world", category = "generation", key = "enableCinnabarOreGeneration", comment = "When enabled and enableOreGeneration isn't disabled, cinnabar ores will generate in the world")
	public static boolean enableCinnabarOreGeneration = true;

	@Config(config = "world", category = "generation", key = "enableGalenaOreGeneration", comment = "When enabled and enableOreGeneration isn't disabled, galena ores will generate in the world")
	public static boolean enableGalenaOreGeneration = true;

	@Config(config = "world", category = "generation", key = "enableIridiumOreGeneration", comment = "When enabled and enableOreGeneration isn't disabled, iridium ores will generate in the world")
	public static boolean enableIridiumOreGeneration = true;

	@Config(config = "world", category = "generation", key = "enableLeadOreGeneration", comment = "When enabled and enableOreGeneration isn't disabled, lead ores will generate in the world")
	public static boolean enableLeadOreGeneration = true;

	@Config(config = "world", category = "generation", key = "enablePeridotOreGeneration", comment = "When enabled and enableOreGeneration isn't disabled, peridot ores will generate in the world")
	public static boolean enablePeridotOreGeneration = true;

	@Config(config = "world", category = "generation", key = "enablePyriteOreGeneration", comment = "When enabled and enableOreGeneration isn't disabled, pyrite ores will generate in the world")
	public static boolean enablePyriteOreGeneration = true;

	@Config(config = "world", category = "generation", key = "enableRubyOreGeneration", comment = "When enabled and enableOreGeneration isn't disabled, ruby ores will generate in the world")
	public static boolean enableRubyOreGeneration = true;

	@Config(config = "world", category = "generation", key = "enableSapphireOreGeneration", comment = "When enabled and enableOreGeneration isn't disabled, sapphire ores will generate in the world")
	public static boolean enableSapphireOreGeneration = true;

	@Config(config = "world", category = "generation", key = "enableSheldoniteOreGeneration", comment = "When enabled and enableOreGeneration isn't disabled, sheldonite ores will generate in the world")
	public static boolean enableSheldoniteOreGeneration = true;

	@Config(config = "world", category = "generation", key = "enableSilverOreGeneration", comment = "When enabled and enableOreGeneration isn't disabled, silver ores will generate in the world")
	public static boolean enableSilverOreGeneration = true;

	@Config(config = "world", category = "generation", key = "enableSodaliteOreGeneration", comment = "When enabled and enableOreGeneration isn't disabled, sodalite ores will generate in the world")
	public static boolean enableSodaliteOreGeneration = true;

	@Config(config = "world", category = "generation", key = "enableSphaleriteOreGeneration", comment = "When enabled and enableOreGeneration isn't disabled, sphalerite ores will generate in the world")
	public static boolean enableSphaleriteOreGeneration = true;

	@Config(config = "world", category = "generation", key = "enableTinOreGeneration", comment = "When enabled and enableOreGeneration isn't disabled, tin ores will generate in the world")
	public static boolean enableTinOreGeneration = true;

	@Config(config = "world", category = "generation", key = "enableTungstenOreGeneration", comment = "When enabled and enableOreGeneration isn't disabled, tungsten ores will generate in the world")
	public static boolean enableTungstenOreGeneration = true;

	@Config(config = "world", category = "generation", key = "enableRubberTreeGeneration", comment = "When enabled rubber trees will generate in the world")
	public static boolean enableRubberTreeGeneration = true;

	@Config(config = "world", category = "generation", key = "enableOilLakeGeneration", comment = "When enabled oil lakes will generate in the world")
	public static boolean enableOilLakeGeneration = true;

	@Config(config = "world", category = "generation", key = "enableMetallurgistGeneration", comment = "When enabled metallurgist houses can generate in villages")
	public static boolean enableMetallurgistGeneration = true;

	@Config(config = "world", category = "generation", key = "enableElectricianGeneration", comment = "When enabled electrician houses can generate in villages")
	public static boolean enableElectricianGeneration = true;
}
