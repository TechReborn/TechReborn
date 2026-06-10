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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mojang.serialization.Codec;

import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import reborncore.common.config2.Config;
import reborncore.common.config2.ConfigGroup;
import reborncore.common.config2.ConfigValue;
import reborncore.common.config2.RebornCoreConfigApi;

// All moved into one class as it's a lot easier to find the config values when you know where they all are
public final class TechRebornConfig {
	private static final String MOD_ID = "techreborn";
	private static final Map<String, Config> CONFIGS = new LinkedHashMap<>();
	private static final Map<String, ConfigGroup> CATEGORIES = new LinkedHashMap<>();

	public static final ConfigValue<Integer> solarInternalCapacityMultiplier = intValue("generators", "solarPanelGeneral", "internalCapacity", "Multiplier for internal capacity of solar panels (multiplier * day generation rate)", 2000);

	public static final ConfigValue<Integer> basicGenerationRateD = intValue("generators", "solarPanelBasic", "basicDayRate", "Generation rate during day for Basic Solar Panel (Energy per tick)", 3);

	public static final ConfigValue<Integer> basicGenerationRateN = intValue("generators", "solarPanelBasic", "basicNightRate", "Generation rate during night for Basic Solar Panel (Energy per tick)", 0);

	public static final ConfigValue<Integer> advancedGenerationRateD = intValue("generators", "solarPanelAdvanced", "advancedDayRate", "Generation rate during day for Advanced Solar Panel (Energy per tick)", 12);

	public static final ConfigValue<Integer> advancedGenerationRateN = intValue("generators", "solarPanelAdvanced", "advancedNightRate", "Generation rate during night for Advanced Solar Panel (Energy per tick)", 0);

	public static final ConfigValue<Integer> industrialGenerationRateD = intValue("generators", "solarPanelIndustrial", "industrialDayRate", "Generation rate during day for Industrial Solar Panel (Energy per tick)", 24);

	public static final ConfigValue<Integer> industrialGenerationRateN = intValue("generators", "solarPanelIndustrial", "industrialNightRate", "Generation rate during night for Industrial Solar Panel (Energy per tick)", 4);

	public static final ConfigValue<Integer> ultimateGenerationRateD = intValue("generators", "solarPanelUltimate", "ultimateDayRate", "Generation rate during day for Ultimate Solar Panel (Energy per tick)", 48);

	public static final ConfigValue<Integer> ultimateGenerationRateN = intValue("generators", "solarPanelUltimate", "ultimateNightRate", "Generation rate during night for Ultimate Solar Panel (Energy per tick)", 8);

	public static final ConfigValue<Integer> quantumGenerationRateD = intValue("generators", "solarPanelQuantum", "quantumDayRate", "Generation rate during day for Quantum Solar Panel (Energy per tick)", 512);

	public static final ConfigValue<Integer> quantumGenerationRateN = intValue("generators", "solarPanelQuantum", "quantumNightRate", "Generation rate during night for Quantum Solar Panel (Energy per tick)", 32);

	public static final ConfigValue<Integer> lightningRodMaxOutput = intValue("generators", "lightning_rod", "LightningRodMaxOutput", "Lightning Rod Max Output (Energy per tick)", 2048);

	public static final ConfigValue<Integer> lightningRodMaxEnergy = intValue("generators", "lightning_rod", "LightningRodMaxEnergy", "Lightning Rod Max Energy", 100_000_000);

	public static final ConfigValue<Integer> lightningRodChanceOfStrike = intValue("generators", "lightning_rod", "LightningRodChanceOfStrike", "Chance of lightning striking a rod (Range: 0-70)", 24);

	public static final ConfigValue<Integer> lightningRodBaseEnergyStrike = intValue("generators", "lightning_rod", "LightningRodBaseStrikeEnergy", "Base amount of Energy per strike", 262_144);

	public static final ConfigValue<Integer> thermalGeneratorMaxOutput = intValue("generators", "thermal_generator", "ThermalGeneratorMaxOutput", "Thermal Generator Max Output (Energy per tick)", 128);

	public static final ConfigValue<Integer> thermalGeneratorMaxEnergy = intValue("generators", "thermal_generator", "ThermalGeneratorMaxEnergy", "Thermal Generator Max Energy", 1_000_000);

	public static final ConfigValue<Integer> thermalGeneratorEnergyPerTick = intValue("generators", "thermal_generator", "ThermalGeneratorEnergyPerTick", "Thermal Generator Energy Per Tick", 16);

	public static final ConfigValue<Integer> plasmaGeneratorMaxOutput = intValue("generators", "plasma_generator", "PlasmaGeneratorMaxOutput", "Plasma Generator Max Output (Energy per tick)", 2048);

	public static final ConfigValue<Long> plasmaGeneratorMaxEnergy = longValue("generators", "plasma_generator", "PlasmaGeneratorMaxEnergy", "Plasma Generator Max Energy", 500_000_000);

	public static final ConfigValue<Integer> plasmaGeneratorEnergyPerTick = intValue("generators", "plasma_generator", "PlasmaGeneratorEnergyPerTick", "Plasma Generator Energy Per Tick", 400);

	public static final ConfigValue<Integer> windMillMaxOutput = intValue("generators", "wind_mill", "WindMillMaxOutput", "Wind Mill Max Output (Energy per tick)", 128);

	public static final ConfigValue<Integer> windMillMaxEnergy = intValue("generators", "wind_mill", "WindMillMaxEnergy", "Wind Mill Max Energy", 10_000);

	public static final ConfigValue<Integer> windMillBaseEnergy = intValue("generators", "wind_mill", "WindMillEnergyPerTick", "Wind Mill generation rate (Energy Per Tick)", 2);

	public static final ConfigValue<Double> windMillThunderMultiplier = doubleValue("generators", "wind_mill", "WindMillThunderMultiplier", "Wind Mill Thunder Multiplier", 1.25);

	public static final ConfigValue<Integer> waterMillMaxOutput = intValue("generators", "water_mill", "WaterMillMaxOutput", "Water Mill Max Output (Energy per tick)", 32);

	public static final ConfigValue<Integer> waterMillMaxEnergy = intValue("generators", "water_mill", "WaterMillMaxEnergy", "Water Mill Max Energy", 1000);

	public static final ConfigValue<Double> waterMillEnergyMultiplier = doubleValue("generators", "water_mill", "WaterMillEnergyPerTick", "Water Mill generation rate per water block (Energy per tick)", 0.1);

	public static final ConfigValue<Integer> semiFluidGeneratorMaxOutput = intValue("generators", "semifluid_generator", "SemifluidGeneratorMaxOutput", "Semifluid Generator Max Output (Energy per tick)", 128);

	public static final ConfigValue<Integer> semiFluidGeneratorMaxEnergy = intValue("generators", "semifluid_generator", "SemifluidGeneratorMaxEnergy", "Semifluid Generator Max Energy", 1000000);

	public static final ConfigValue<Integer> semiFluidGeneratorEnergyPerTick = intValue("generators", "semifluid_generator", "SemifluidGeneratorEnergyPerTick", "Semifluid Generator Energy Per Tick", 8);

	public static final ConfigValue<Integer> gasTurbineMaxOutput = intValue("generators", "gas_generator", "GasGeneratorMaxOutput", "Gas Generator Max Output (Energy per tick)", 128);

	public static final ConfigValue<Integer> gasTurbineMaxEnergy = intValue("generators", "gas_generator", "GasGeneratorMaxEnergy", "Gas Generator Max Energy", 1000000);

	public static final ConfigValue<Integer> gasTurbineEnergyPerTick = intValue("generators", "gas_generator", "GasGeneratorEnergyPerTick", "Gas Generator Energy Per Tick", 16);

	public static final ConfigValue<Integer> dieselGeneratorMaxOutput = intValue("generators", "diesel_generator", "DieselGeneratorMaxOutput", "Diesel Generator Max Output (Energy per tick)", 32);

	public static final ConfigValue<Integer> dieselGeneratorMaxEnergy = intValue("generators", "diesel_generator", "DieselGeneratorMaxEnergy", "Diesel Generator Max Energy", 10_000);

	public static final ConfigValue<Integer> dieselGeneratorEnergyPerTick = intValue("generators", "diesel_generator", "DieselGeneratorEnergyPerTick", "Diesel Generator Energy Per Tick", 20);

	public static final ConfigValue<Integer> dragonEggSyphonMaxOutput = intValue("generators", "dragon_egg_siphoner", "DragonEggSiphonerMaxOutput", "Dragon Egg Siphoner Max Output (Energy per tick)", 128);

	public static final ConfigValue<Integer> dragonEggSyphonMaxEnergy = intValue("generators", "dragon_egg_siphoner", "DragonEggSiphonerMaxEnergy", "Dragon Egg Siphoner Max Energy", 1000);

	public static final ConfigValue<Integer> dragonEggSyphonEnergyPerTick = intValue("generators", "dragon_egg_siphoner", "DragonEggSiphonerEnergyPerTick", "Dragon Egg Siphoner Energy Per Tick", 4);

	public static final ConfigValue<Integer> solidFuelGeneratorMaxOutput = intValue("generators", "generator", "GeneratorMaxOutput", "Solid Fuel Generator Max Output (Energy per tick)", 32);

	public static final ConfigValue<Integer> solidFuelGeneratorMaxEnergy = intValue("generators", "generator", "GeneratorMaxEnergy", "Solid Fuel Generator Max Energy", 10_000);

	public static final ConfigValue<Integer> solidFuelGeneratorOutputAmount = intValue("generators", "generator", "GeneratorEnergyOutput", "Solid Fuel Generator Energy Per Tick", 10);

	public static final ConfigValue<Integer> nuclearReactorMaxEnergy = intValue("generators", "nuclear_reactor", "NuclearReactorMaxEnergy", "Nuclear Reactor Max Energy", 100_000_000);

	public static final ConfigValue<Integer> nuclearReactorMaxOutput = intValue("generators", "nuclear_reactor", "NuclearReactorMaxOutput", "Nuclear Reactor Max Output", 8192);

	public static final ConfigValue<Integer> nuclearReactorMaxHeat = intValue("generators", "nuclear_reactor", "NuclearReactorMaxHeat", "Nuclear Reactor Base Max Heat (before plating)", 10_000);

	public static final ConfigValue<Double> nuclearReactorEUMultiplier = doubleValue("generators", "nuclear_reactor", "NuclearReactorEUMultiplier", "Multiplier for EU output from fuel rods", 1.0);

	public static final ConfigValue<Integer> nuclearReactorTickRate = intValue("generators", "nuclear_reactor", "NuclearReactorTickRate", "Ticks between reactor processing cycles (20 = 1 second)", 20);

	public static final ConfigValue<Boolean> nuclearReactorExplosionEnabled = boolValue("generators", "nuclear_reactor", "NuclearReactorExplosionEnabled", "Enable Nuclear Reactor Explosions on Meltdown", false);

	public static final ConfigValue<Float> nuclearReactorExplosionPowerLimit = floatValue("generators", "nuclear_reactor", "NuclearReactorExplosionPowerLimit", "Maximum explosion power for meltdowns", 45.0f);

	public static final ConfigValue<Integer> nanosaberCharge = intValue("items", "power", "nanoSaberCharge", "Energy Capacity for Nano Saber", 1_000_000);

	public static final ConfigValue<Integer> nanosaberCost = intValue("items", "power", "nanoSaberCost", "Energy Cost for Nano Saber", 150);

	public static final ConfigValue<Integer> nanosaberDamage = intValue("items", "power", "nanoSaberDamage", "Damage value for the Nano Saber", 20);

	public static final ConfigValue<Integer> electricTreetapCharge = intValue("items", "power", "electricTreetapCharge", "Energy Capacity for Electric Treetap", 10_000);

	public static final ConfigValue<Integer> electricTreetapCost = intValue("items", "power", "electricTreetapCost", "Energy Cost for Electric Treetap", 20);

	public static final ConfigValue<Integer> basicDrillCharge = intValue("items", "power", "basicDrillCharge", "Energy Capacity for Basic Drill", 10_000);

	public static final ConfigValue<Integer> basicDrillCost = intValue("items", "power", "basicDrillCost", "Energy Cost for Basic Drill", 50);

	public static final ConfigValue<Integer> advancedDrillCharge = intValue("items", "power", "advancedDrillCharge", "Energy Capacity for Advanced Drill", 100_000);

	public static final ConfigValue<Integer> advancedDrillCost = intValue("items", "power", "advancedDrillCost", "Energy Cost for Advanced Drill", 100);

	public static final ConfigValue<Integer> industrialDrillCharge = intValue("items", "power", "industrialDrillCharge", "Energy Capacity for Industrial Drill", 1_000_000);

	public static final ConfigValue<Integer> industrialDrillCost = intValue("items", "power", "industrialDrillCost", "Energy Cost for Industrial Drill", 150);

	public static final ConfigValue<Integer> basicChainsawCharge = intValue("items", "power", "basicChainsawCharge", "Energy Capacity for Basic Chainsaw", 10_000);

	public static final ConfigValue<Integer> basicChainsawCost = intValue("items", "power", "basicChainsawCost", "Energy Cost for Basic Chainsaw", 50);

	public static final ConfigValue<Integer> advancedChainsawCharge = intValue("items", "power", "advancedChainsawCharge", "Energy Capacity for Advanced Chainsaw", 100_000);

	public static final ConfigValue<Integer> advancedChainsawCost = intValue("items", "power", "advancedChainsawCost", "Energy Cost for Advanced Chainsaw", 100);

	public static final ConfigValue<Integer> industrialChainsawCharge = intValue("items", "power", "industrialChainsawCharge", "Energy Capacity for Industrial Chainsaw", 1_000_000);

	public static final ConfigValue<Integer> industrialChainsawCost = intValue("items", "power", "industrialChainsawCost", "Energy Cost for Industrial Chainsaw", 150);

	public static final ConfigValue<Integer> basicJackhammerCharge = intValue("items", "power", "basicJackhammerCharge", "Energy Capacity for Basic Jackhammer", 10_000);

	public static final ConfigValue<Integer> basicJackhammerCost = intValue("items", "power", "basicJackhammerCost", "Energy Cost for Basic Jackhammer", 50);

	public static final ConfigValue<Integer> advancedJackhammerCharge = intValue("items", "power", "advancedJackhammerCharge", "Energy Capacity for Advanced Jackhammer", 100_000);

	public static final ConfigValue<Integer> advancedJackhammerCost = intValue("items", "power", "advancedJackhammerCost", "Energy Cost for Advanced Jackhammer", 100);

	public static final ConfigValue<Integer> industrialJackhammerCharge = intValue("items", "power", "industrialJackhammerCharge", "Energy Capacity for Industrial Jackhammer", 1_000_000);

	public static final ConfigValue<Integer> industrialJackhammerCost = intValue("items", "power", "industrialJackhammerCost", "Energy Cost for Industrial Jackhammer", 150);

	public static final ConfigValue<Integer> omniToolCharge = intValue("items", "power", "omniToolCharge", "Energy Capacity for Omni Tool", 1_000_000);

	public static final ConfigValue<Integer> omniToolCost = intValue("items", "power", "omniToolCost", "Energy Cost for Omni Tool", 100);

	public static final ConfigValue<Integer> omniToolHitCost = intValue("items", "power", "omniToolHitCost", "Hit Energy Cost for Omni Tool", 125);

	public static final ConfigValue<Integer> rockCutterCharge = intValue("items", "power", "rockCutterCharge", "Energy Capacity for Rock Cutter", 10_000);

	public static final ConfigValue<Integer> rockCutterCost = intValue("items", "power", "rockCutterCost", "Energy Cost for Rock Cutter", 10);

	public static final ConfigValue<Integer> lapotronPackCharge = intValue("items", "power", "lapotronPackCharge", "Energy Capacity for Lapotron Pack", 100_000_000);

	public static final ConfigValue<Integer> lithiumBatpackCharge = intValue("items", "power", "LithiumBatpackCharge", "Energy Capacity for Lithium Batpack", 600_000);

	public static final ConfigValue<Integer> redCellBatteryMaxCharge = intValue("items", "power", "redCellBatteryMaxCharge", "Energy Capacity for Red Cell Battery", 10_000);

	public static final ConfigValue<Integer> lithiumIonBatteryMaxCharge = intValue("items", "power", "lithiumIonBatteryMaxCharge", "Energy Capacity for Lithium Ion Battery", 100_000);

	public static final ConfigValue<Integer> energyCrystalMaxCharge = intValue("items", "power", "energyCrystalMaxCharge", "Energy Capacity for Energy Crystal", 1_000_000);

	public static final ConfigValue<Integer> lapotronCrystalMaxCharge = intValue("items", "power", "lapotronCrystalMaxCharge", "Energy Capacity for Lapotron Crystal", 10_000_000);

	public static final ConfigValue<Integer> lapotronicOrbMaxCharge = intValue("items", "power", "lapotronicOrbMaxCharge", "Energy Capacity for Lapotronic Orb", 100_000_000);

	public static final ConfigValue<Long> cloakingDeviceCharge = longValue("items", "power", "cloakingDeviceCharge", "Energy Capacity for Cloaking Device", 40_000_000);

	public static final ConfigValue<Integer> cloakingDeviceCost = intValue("items", "power", "clockingDeviceEnergyUsage", "Cloaking device energy usage", 10);

	public static final ConfigValue<Long> quantumSuitCapacity = longValue("items", "power", "quantumSuitCapacity", "Quantum Suit Energy Capacity", 40_000_000);

	public static final ConfigValue<Long> quantumSuitFlyingCost = longValue("items", "power", "quantumSuitFlyingCost", "Quantum Suit Flying Cost", 50);

	public static final ConfigValue<Long> quantumSuitSwimmingCost = longValue("items", "power", "quantumSuitSwimmingCost", "Quantum Suit Swimming Cost", 20);

	public static final ConfigValue<Long> quantumSuitBreathingCost = longValue("items", "power", "quantumSuitBreathingCost", "Quantum Suit Breathing Cost", 20);

	public static final ConfigValue<Long> quantumSuitSprintingCost = longValue("items", "power", "quantumSuitSprintingCost", "Quantum Suit Sprinting Cost", 20);

	public static final ConfigValue<Long> fireExtinguishCost = longValue("items", "power", "quantumSuitFireExtinguishCost", "Quantum Suit Cost for Fire Extinguish", 50);

	public static final ConfigValue<Boolean> quantumSuitEnableSprint = boolValue("items", "power", "quantumSuitEnableSprint", "Enable Sprint Speed increase for Quantum Legs", true);

	public static final ConfigValue<Boolean> quantumSuitEnableFlight = boolValue("items", "power", "quantumSuitEnableFlight", "Enable Flight for Quantum Chest", true);

	public static final ConfigValue<Double> damageAbsorbCost = doubleValue("items", "power", "quantumSuitDamageAbsorbCost", "Quantum Suit Cost for Damage Absorbed", 10);

	public static final ConfigValue<Long> nanoSuitCapacity = longValue("items", "power", "nanoSuitCapacity", "Nano Suit Energy Capacity", 1_000_000);

	public static final ConfigValue<Long> suitNightVisionCost = longValue("items", "power", "suitNightVisionCost", "Nano/Quantum Suit Night Vision Cost", 1);

	public static final ConfigValue<Long> nanoArmorEnergyCost = longValue("items", "power", "nanoArmorEnergyCost", "Nano Suit Energy Cost", 100);

	public static final ConfigValue<Double> overclockerSpeed = doubleValue("items", "upgrades", "overclocker_speed", "Overclocker behavior speed multiplier", 0.25);

	public static final ConfigValue<Double> overclockerPower = doubleValue("items", "upgrades", "overclocker_power", "Overclocker behavior power multiplier", 0.75);

	public static final ConfigValue<Double> energyStoragePower = doubleValue("items", "upgrades", "energy_storage", "Energy storage behavior extra power", 40_000);

	public static final ConfigValue<Double> superConductorCount = doubleValue("items", "upgrades", "super_conductor", "Energy flow power increase", 1);

	public static final ConfigValue<Integer> lesuStoragePerBlock = intValue("machines", "lesu", "LesuMaxEnergyPerBlock", "LESU Max Energy Per Block", 4_000_000);

	public static final ConfigValue<Integer> lesuExtraIOPerBlock = intValue("machines", "lesu", "LesuExtraIO", "LESU Extra I/O Multiplier", 64);

	public static final ConfigValue<Integer> lesuBaseOutput = intValue("machines", "lesu", "LesuBaseOutput", "LESU Base Output", 64);

	public static final ConfigValue<Integer> aesuMaxEnergy = intValue("machines", "aesu", "AesuMaxEnergy", "AESU Max Energy", 100_000_000);

	public static final ConfigValue<Integer> idsuMaxEnergy = intValue("machines", "idsu", "IdsuMaxEnergy", "IDSU Max Energy", 1_000_000_000);

	public static final ConfigValue<Integer> crudeStorageUnitMaxStorage = intValue("machines", "storage", "CrudeStorageUnitMaxStorage", "Maximum amount of items a Crude Storage Unit can store", 1 << 11);

	public static final ConfigValue<Integer> basicStorageUnitMaxStorage = intValue("machines", "storage", "BasicStorageUnitMaxStorage", "Maximum amount of items a Basic Storage Unit can store", 1 << 13);

	public static final ConfigValue<Integer> basicTankUnitCapacity = intValue("machines", "storage", "BasicTankUnitCapacity", "How much liquid a Basic Tank Unit can take (Value in buckets, 1000 Mb)", 1 << 7);

	public static final ConfigValue<Integer> advancedStorageUnitMaxStorage = intValue("machines", "storage", "AdvancedStorageMaxStorage", "Maximum amount of items an Advanced Storage Unit can store", 1 << 15);

	public static final ConfigValue<Integer> advancedTankUnitMaxStorage = intValue("machines", "storage", "AdvancedTankUnitMaxStorage", "How much liquid an Advanced Tank Unit can take (Value in buckets, 1000 Mb)", 1 << 9);

	public static final ConfigValue<Integer> industrialStorageUnitMaxStorage = intValue("machines", "storage", "IndustrialStorageMaxStorage", "Maximum amount of items an Industrial Storage Unit can store (Compat: >= 32768)", 1 << 16);

	public static final ConfigValue<Integer> industrialTankUnitCapacity = intValue("machines", "storage", "IndustrialTankUnitCapacity", "How much liquid an Industrial Tank Unit can take (Value in buckets, 1000 Mb)", 1 << 10);

	public static final ConfigValue<Integer> quantumStorageUnitMaxStorage = intValue("machines", "storage", "QuantumStorageUnitMaxStorage", "Maximum amount of items a Quantum Storage Unit can store (Compat: == MAX_VALUE)", Integer.MAX_VALUE);

	public static final ConfigValue<Integer> quantumTankUnitCapacity = intValue("machines", "storage", "QuantumTankUnitCapacity", "How much liquid a Quantum Tank Unit can take (Value in buckets, 1000 Mb)(Compat: == MAX_VALUE)", Integer.MAX_VALUE / 1000);

	public static final ConfigValue<Integer> playerDetectorMaxInput = intValue("machines", "player_detector", "PlayerDetectorMaxInput", "Player Detector Max Input (Energy per tick)", 32);

	public static final ConfigValue<Integer> playerDetectorMaxEnergy = intValue("machines", "player_detector", "PlayerDetectorMaxEnergy", "Player Detector Max Energy", 10000);

	public static final ConfigValue<Integer> playerDetectorEuPerTick = intValue("machines", "player_detector", "PlayerDetectorEnergyUsage", "Player Detector Energy Consumption per second", 1);

	public static final ConfigValue<Integer> playerDetectorMaxRadius = intValue("machines", "player_detector", "PlayerDetectorMaxRadius", "Player Detector maximum detection radius", 128);

	public static final ConfigValue<Integer> distillationTowerMaxInput = intValue("machines", "Distillation_tower", "DistillationTowerMaxInput", "Distillation Tower Max Input (Energy per tick)", 128);

	public static final ConfigValue<Integer> distillationTowerMaxEnergy = intValue("machines", "Distillation_tower", "DistillationTowerMaxEnergy", "Distillation Tower Max Energy", 10_000);

	public static final ConfigValue<Integer> extractorMaxInput = intValue("machines", "extractor", "ExtractorInput", "Extractor Max Input (Energy per tick)", 32);

	public static final ConfigValue<Integer> extractorMaxEnergy = intValue("machines", "extractor", "ExtractorMaxEnergy", "Extractor Max Energy", 1_000);

	public static final ConfigValue<Integer> grinderMaxInput = intValue("machines", "grinder", "GrinderInput", "Grinder Max Input (Energy per tick)", 32);

	public static final ConfigValue<Integer> grinderMaxEnergy = intValue("machines", "grinder", "GrinderMaxEnergy", "Grinder Max Energy", 1_000);

	public static final ConfigValue<Integer> compressorMaxInput = intValue("machines", "compressor", "CompressorInput", "Compressor Max Input (Energy per tick)", 32);

	public static final ConfigValue<Integer> compressorMaxEnergy = intValue("machines", "compressor", "CompressorMaxEnergy", "Compressor Max Energy", 1000);

	public static final ConfigValue<Integer> alloySmelterMaxInput = intValue("machines", "alloy_smelter", "AlloySmelterMaxInput", "Alloy Smelter Max Input (Energy per tick)", 32);

	public static final ConfigValue<Integer> alloySmelterMaxEnergy = intValue("machines", "alloy_smelter", "AlloySmelterMaxEnergy", "Alloy Smelter Max Energy", 1_000);

	public static final ConfigValue<Integer> rollingMachineMaxInput = intValue("machines", "rolling_machine", "RollingMachineMaxInput", "Rolling Machine Max Input (Energy per tick)", 32);

	public static final ConfigValue<Integer> rollingMachineMaxEnergy = intValue("machines", "rolling_machine", "RollingMachineMaxEnergy", "Rolling Machine Max Energy", 10000);

	public static final ConfigValue<Integer> chunkLoaderMaxRadius = intValue("machines", "chunk_loader", "ChunkLoaderMaxRadius", "Chunk Loader Max Radius", 5);

	public static final ConfigValue<Integer> assemblingMachineMaxInput = intValue("machines", "assembling_machine", "AssemblingMachineMaxInput", "Assembling Machine Max Input (Energy per tick)", 128);

	public static final ConfigValue<Integer> assemblingMachineMaxEnergy = intValue("machines", "assembling_machine", "AssemblingMachineMaxEnergy", "Assembling Machine Max Energy", 10_000);

	public static final ConfigValue<Integer> matterFabricatorMaxInput = intValue("machines", "matter_fabricator", "MatterFabricatorMaxInput", "Matter Fabricator Max Input (Energy per tick)", 8192);

	public static final ConfigValue<Integer> matterFabricatorMaxEnergy = intValue("machines", "matter_fabricator", "MatterFabricatorMaxEnergy", "Matter Fabricator Max Energy", 10_000_000);

	public static final ConfigValue<Integer> matterFabricatorFabricationRate = intValue("machines", "matter_fabricator", "MatterFabricatorFabricationRate", "Matter Fabricator Fabrication Rate, amount of amplifier units per UUM", 6_000);

	public static final ConfigValue<Integer> matterFabricatorEnergyPerAmp = intValue("machines", "matter_fabricator", "MatterFabricatorEnergyPerAmp", "Matter Fabricator EU per amplifier unit, multiply this with the rate for total Energy", 5);

	public static final ConfigValue<Integer> industrialGrinderMaxInput = intValue("machines", "industrial_grinder", "IndustrialGrinderMaxInput", "Industrial Grinder Max Input (Energy per tick)", 128);

	public static final ConfigValue<Integer> industrialGrinderMaxEnergy = intValue("machines", "industrial_grinder", "IndustrialGrinderMaxEnergy", "Industrial Grinder Max Energy", 10_000);

	public static final ConfigValue<Integer> vacuumFreezerMaxInput = intValue("machines", "vacuumfreezer", "VacuumFreezerInput", "Vacuum Freezer Max Input (Energy per tick)", 64);

	public static final ConfigValue<Integer> vacuumFreezerMaxEnergy = intValue("machines", "vacuumfreezer", "VacuumFreezerMaxEnergy", "Vacuum Freezer Max Energy", 64_000);

	public static final ConfigValue<Integer> implosionCompressorMaxInput = intValue("machines", "implosion_compressor", "ImplosionCompressorMaxInput", "Implosion Compressor Max Input (Energy per tick)", 64);

	public static final ConfigValue<Integer> implosionCompressorMaxEnergy = intValue("machines", "implosion_compressor", "ImplosionCompressorMaxEnergy", "Implosion Compressor Max Energy", 64_000);

	public static final ConfigValue<Integer> industrialBlastFurnaceMaxInput = intValue("machines", "industrial_furnace", "IndustrialFurnaceMaxInput", "Industrial Blast Furnace Max Input (Energy per tick)", 128);

	public static final ConfigValue<Integer> industrialBlastFurnaceMaxEnergy = intValue("machines", "industrial_furnace", "IndustrialFurnaceMaxEnergy", "Industrial Blast Furnace Max Energy", 40_000);

	public static final ConfigValue<Integer> industrialSawmillMaxInput = intValue("machines", "industrial_sawmill", "IndustrialSawmillMaxInput", "Industrial Sawmill Max Input (Energy per tick)", 128);

	public static final ConfigValue<Integer> industrialSawmillMaxEnergy = intValue("machines", "industrial_sawmill", "IndustrialSawmillMaxEnergy", "Industrial Sawmill Max Energy", 10_000);

	public static final ConfigValue<Integer> autoCraftingTableMaxInput = intValue("machines", "autocrafter", "AutoCrafterInput", "AutoCrafting Table Max Input (Energy per tick)", 32);

	public static final ConfigValue<Integer> autoCraftingTableMaxEnergy = intValue("machines", "autocrafter", "AutoCrafterMaxEnergy", "AutoCrafting Table Max Energy", 10_000);

	public static final ConfigValue<Integer> fluidReplicatorMaxInput = intValue("machines", "fluidreplicator", "FluidReplicatorMaxInput", "Fluid Replicator Max Input (Energy per tick)", 256);

	public static final ConfigValue<Integer> fluidReplicatorMaxEnergy = intValue("machines", "fluidreplicator", "FluidReplicatorMaxEnergy", "Fluid Replicator Max Energy", 400_000);

	public static final ConfigValue<Integer> electricFurnaceMaxInput = intValue("machines", "electric_furnace", "ElectricFurnaceInput", "Electric Furnace Max Input (Energy per tick)", 32);

	public static final ConfigValue<Integer> electricFurnaceMaxEnergy = intValue("machines", "electric_furnace", "ElectricFurnaceMaxEnergy", "Electric Furnace Max Energy", 1000);

	public static final ConfigValue<Integer> chargeOMatBMaxOutput = intValue("machines", "charge_bench", "ChargeBenchMaxOutput", "Charge Bench Max Output (Energy per tick)", 512);

	public static final ConfigValue<Integer> chargeOMatBMaxInput = intValue("machines", "charge_bench", "ChargeBenchMaxInput", "Charge Bench Max Input (Energy per tick)", 512);

	public static final ConfigValue<Integer> chargeOMatBMaxEnergy = intValue("machines", "charge_bench", "ChargeBenchMaxEnergy", "Charge Bench Max Energy", 100_000_000);

	public static final ConfigValue<Integer> industrialElectrolyzerMaxInput = intValue("machines", "industrial_electrolyzer", "IndustrialElectrolyzerMaxInput", "Industrial Electrolyzer Max Input (Energy per tick)", 128);

	public static final ConfigValue<Integer> industrialElectrolyzerMaxEnergy = intValue("machines", "industrial_electrolyzer", "IndustrialElectrolyzerMaxEnergy", "Industrial Electrolyzer Max Energy", 10_000);

	public static final ConfigValue<Integer> industrialCentrifugeMaxInput = intValue("machines", "centrifuge", "CentrifugeMaxInput", "Centrifuge Max Input (Energy per tick)", 32);

	public static final ConfigValue<Integer> industrialCentrifugeMaxEnergy = intValue("machines", "centrifuge", "CentrifugeMaxEnergy", "Centrifuge Max Energy", 10_000);

	public static final ConfigValue<Integer> chemicalReactorMaxInput = intValue("machines", "chemical_reactor", "ChemicalReactorMaxInput", "Chemical Reactor Max Input (Energy per tick)", 128);

	public static final ConfigValue<Integer> chemicalReactorMaxEnergy = intValue("machines", "chemical_reactor", "ChemicalReactorMaxEnergy", "Chemical Reactor Max Energy", 10_000);

	public static final ConfigValue<Integer> fusionControlComputerMaxInput = intValue("machines", "fusion_reactor", "FusionReactorMaxInput", "Fusion Reactor Max Input (Energy per tick)", 8192);

	public static final ConfigValue<Integer> fusionControlComputerMaxOutput = intValue("machines", "fusion_reactor", "FusionReactorMaxOutput", "Fusion Reactor Max Output (Energy per tick)", 1_000_000);

	public static final ConfigValue<Integer> fusionControlComputerMaxEnergy = intValue("machines", "fusion_reactor", "FusionReactorMaxEnergy", "Fusion Reactor Max Energy", 100_000_000);

	public static final ConfigValue<Integer> fusionControlComputerMaxCoilSize = intValue("machines", "fusion_reactor", "FusionReactorMaxCoilSize", "Fusion Reactor Max Coil size (Radius)", 50);

	public static final ConfigValue<Integer> recyclerMaxInput = intValue("machines", "recycler", "RecyclerInput", "Recycler Max Input (Energy per tick)", 32);

	public static final ConfigValue<Integer> recyclerMaxEnergy = intValue("machines", "recycler", "RecyclerMaxEnergy", "Recycler Max Energy", 1000);

	public static final ConfigValue<Integer> recyclerChance = intValue("machines", "recycler", "RecyclerChance", "Recycler Chance to produce scrap (1 out of chance)", 6);

	public static final ConfigValue<List<String>> recyclerBlackList = stringListValue("machines", "recycler", "RecyclerBlacklist", "Recycler blacklist", Arrays.asList("techreborn:scrap_box", "techreborn:scrap"));

	public static final ConfigValue<Integer> scrapboxinatorMaxInput = intValue("machines", "scrapboxinator", "ScrapboxinatorMaxInput", "Scrapboxinator Max Input (Energy per tick)", 32);

	public static final ConfigValue<Integer> scrapboxinatorMaxEnergy = intValue("machines", "scrapboxinator", "ScrapboxinatorMaxEnergy", "Scrapboxinator Max Energy", 1_000);

	public static final ConfigValue<Integer> solidCanningMachineMaxInput = intValue("machines", "solid_canning_machine", "solidCanningMachineMaxInput", "Solid Canning Machine Max Input (Energy per tick)", 32);

	public static final ConfigValue<Integer> solidCanningMachineMaxEnergy = intValue("machines", "solid_canning_machine", "solidCanningMachineMaxEnergy", "Solid Canning Machine Max Energy", 1_000);

	public static final ConfigValue<Double> fuelScale = doubleValue("machines", "iron_machine", "fuel_scale", "Multiplier for vanilla furnace item burn time", 1.25);

	public static final ConfigValue<Double> cookingScale = doubleValue("machines", "iron_machine", "cooking_scale", "Multiplier for vanilla furnace item cook time", 1.25);

	public static final ConfigValue<Integer> greenhouseControllerMaxInput = intValue("machines", "greenhouse_controller", "GreenhouseControllerMaxInput", "Greenhouse Controller Max Input (Energy per tick)", 32);

	public static final ConfigValue<Integer> greenhouseControllerMaxEnergy = intValue("machines", "greenhouse_controller", "GreenhouseControllerMaxEnergy", "Greenhouse Controller Max Energy", 1_000);

	public static final ConfigValue<Integer> greenhouseControllerEnergyPerTick = intValue("machines", "greenhouse_controller", "GreenhouseControllerEnergyPerTick", "Greenhouse Controller Energy usage Per Tick", 2);

	public static final ConfigValue<Integer> greenhouseControllerEnergyPerHarvest = intValue("machines", "greenhouse_controller", "GreenhouseControllerEnergyPerHarvest", "Greenhouse Controller Energy usage Per Harvest", 100);

	public static final ConfigValue<Integer> greenhouseControllerEnergyPerBonemeal = intValue("machines", "greenhouse_controller", "GreenhouseControllerEnergyPerBonemeal", "Greenhouse Controller Energy usage Per Bonemeal", 50);

	public static final ConfigValue<Integer> ticksUntilNextDrainAttempt = intValue("machines", "drain", "TicksUntilNextDrainAttempt", "How many ticks should go between two drain attempts. 0 or negative will disable drain.", 10);

	public static final ConfigValue<Integer> blockBreakerMaxInput = intValue("machines", "block_breaker", "BlockBreakerMaxInput", "Block Breaker Max Input (Energy per tick)", 32);

	public static final ConfigValue<Integer> blockBreakerMaxEnergy = intValue("machines", "block_breaker", "BlockBreakerMaxEnergy", "Block Breaker Max Energy", 1_000);

	public static final ConfigValue<Integer> blockBreakerEnergyPerTick = intValue("machines", "block_breaker", "BlockBreakerEnergyPerTick", "Block Breaker Energy usage Per Tick", 5);

	public static final ConfigValue<Integer> blockBreakerBaseBreakTime = intValue("machines", "block_breaker", "BlockBreakerBaseBreakTime", "How many ticks a block of hardness 1 requires to be broken", 100);

	public static final ConfigValue<Integer> blockPlacerMaxInput = intValue("machines", "block_placer", "BlockPlacerMaxInput", "Block Placer Max Input (Energy per tick)", 32);

	public static final ConfigValue<Integer> blockPlacerMaxEnergy = intValue("machines", "block_placer", "BlockPlacerMaxEnergy", "Block Placer Max Energy", 1_000);

	public static final ConfigValue<Integer> blockPlacerEnergyPerTick = intValue("machines", "block_placer", "BlockPlacerEnergyPerTick", "Block Placer Energy usage Per Tick", 5);

	public static final ConfigValue<Integer> blockPlacerBaseBreakTime = intValue("machines", "block_placer", "BlockPlacerBaseBreakTime", "How many ticks a block of hardness 1 requires to be placed", 100);

	public static final ConfigValue<Integer> launchpadMaxInput = intValue("machines", "launchpad", "LaunchpadMaxInput", "Launchpad Max Input (Energy per tick)", 128);

	public static final ConfigValue<Integer> launchpadMaxEnergy = intValue("machines", "launchpad", "LaunchpadMaxEnergy", "Launchpad Max Energy", 40_000);

	public static final ConfigValue<Double> launchpadSpeedLow = doubleValue("machines", "launchpad", "LaunchpadSpeedLow", "Launchpad Low Speed", 1d);

	public static final ConfigValue<Double> launchpadSpeedMedium = doubleValue("machines", "launchpad", "LaunchpadSpeedMedium", "Launchpad Medium Speed", 3d);

	public static final ConfigValue<Double> launchpadSpeedHigh = doubleValue("machines", "launchpad", "LaunchpadSpeedHigh", "Launchpad High Speed", 5d);

	public static final ConfigValue<Double> launchpadSpeedExtreme = doubleValue("machines", "launchpad", "LaunchpadSpeedExtreme", "Launchpad Extreme Speed", 10d);

	public static final ConfigValue<Integer> launchpadEnergyLow = intValue("machines", "launchpad", "LaunchpadEnergyLow", "Launchpad Low Energy", 1_000);

	public static final ConfigValue<Integer> launchpadEnergyMedium = intValue("machines", "launchpad", "LaunchpadEnergyMedium", "Launchpad Medium Energy", 6_000);

	public static final ConfigValue<Integer> launchpadEnergyHigh = intValue("machines", "launchpad", "LaunchpadEnergyHigh", "Launchpad High Energy", 10_000);

	public static final ConfigValue<Integer> launchpadEnergyExtreme = intValue("machines", "launchpad", "LaunchpadEnergyExtreme", "Launchpad Extreme Energy", 20_000);

	public static final ConfigValue<Integer> launchpadDefaultSelection = intValue("machines", "launchpad", "LaunchpadDefaultSelection", "Launchpad Default Selection (0-3 for Low-Extreme)", 0);

	public static final ConfigValue<Integer> launchpadInterval = intValue("machines", "launchpad", "LaunchpadInterval", "Launchpad Launch Interval in Ticks > 0", 100);

	public static final ConfigValue<Integer> elevatorMaxInput = intValue("machines", "elevator", "ElevatorMaxInput", "Elevator Max Input (Energy per tick)", 32);

	public static final ConfigValue<Integer> elevatorMaxEnergy = intValue("machines", "elevator", "ElevatorMaxEnergy", "Elevator Max Energy", 1_000);

	public static final ConfigValue<Integer> elevatorEnergyPerBlock = intValue("machines", "elevator", "ElevatorEnergyPerBlock", "Elevator Energy used per vertical block of transportation", 2);

	public static final ConfigValue<Boolean> allowElevatingThroughBlocks = boolValue("machines", "elevator", "AllowElevatingThroughBlocks", "Allow elevating through blocks (i.e. non air)", true);

	public static final ConfigValue<Integer> fishingStationMaxInput = intValue("machines", "fishing_station", "FishingStationMaxInput", "Fishing Station Max Input (Energy per tick)", 128);

	public static final ConfigValue<Integer> fishingStationMaxEnergy = intValue("machines", "fishing_station", "FishingStationMaxEnergy", "Fishing Station Max Energy", 10_000);

	public static final ConfigValue<Integer> fishingStationEnergyPerCatch = intValue("machines", "fishing_station", "FishingStationEnergyPerCatch", "How much energy the Fishing Station uses per catch", 500);

	public static final ConfigValue<Integer> fishingStationInterval = intValue("machines", "fishing_station", "FishingStationInterval", "Fishing Station Catch Interval in Ticks > 0", 400);

	public static final ConfigValue<Integer> pumpTicksToComplete = intValue("machines", "pump", "PumpTicksToComplete", "How many ticks it takes to pump a source block.", 100);

	public static final ConfigValue<Integer> pumpMaxInput = intValue("machines", "pump", "PumpMaxInput", "Pump Max Input (Energy per tick)", 128);

	public static final ConfigValue<Integer> pumpMaxEnergy = intValue("machines", "pump", "PumpMaxEnergy", "Pump Max Energy", 40_000);

	public static final ConfigValue<Integer> pumpEnergyToCollect = intValue("machines", "pump", "PumpEnergyToCollect", "Base amount of Energy to collect a block of fluid", 1_000);

	public static final ConfigValue<Boolean> pumpIterateOutwards = boolValue("machines", "pump", "PumpIterateOutwards", "If true then the pump will collect closest fluid and scan outwards", false);

	public static final ConfigValue<Boolean> IC2TransformersStyle = boolValue("misc", "general", "IC2TransformersStyle", "Input from dots side, output from other sides, like in IC2.", true);

	public static final ConfigValue<Float> machineSoundVolume = clampedFloatValue("misc", "general", "MachineSoundVolume", "Machines crafting sound volume (0 - disabled, 1 - max)", 1.0F);

	public static final ConfigValue<Boolean> allowManualRefund = boolValue("misc", "general", "manualRefund", "Allow refunding items used to craft the manual", true);

	public static final ConfigValue<Boolean> vanillaUnlockRecipes = boolValue("misc", "general", "vanillaUnlockRecipes", "Enable recipe unlocks only with vanilla mechanic, instead of getting all of them at once", true);

	public static final ConfigValue<Integer> nukeFuseTime = intValue("misc", "nuke", "fusetime", "Nuke fuse time (ticks)", 400);

	public static final ConfigValue<Integer> nukeRadius = intValue("misc", "nuke", "radius", "Nuke explosion radius", 40);

	public static final ConfigValue<Boolean> nukeEnabled = boolValue("misc", "nuke", "enabled", "Should the nuke explode, set to false to prevent block damage", true);

	public static final ConfigValue<Integer> sapTimeTicks = intValue("misc", "resin_basin", "saptime", "How long it takes to harvest one sap (ticks)", 80);

	public static final ConfigValue<Integer> checkForSapTime = intValue("misc", "resin_basin", "SapCheckTime", "How often to check for sap (will check if world time % this number is zero)", 50);

	public static final ConfigValue<Boolean> dispenseScrapboxes = boolValue("misc", "general", "DispenserScrapbox", "Dispensers will open scrapboxes", true);

	public static final ConfigValue<Boolean> uninsulatedElectrocutionDamage = boolValue("misc", "cable", "uninsulatedElectrocutionDamage", "When true an uninsulated cable will cause damage to entities", true);

	public static final ConfigValue<Boolean> uninsulatedElectrocutionSound = boolValue("misc", "cable", "uninsulatedElectrocutionSound", "When true an uninsulated cable will create a spark sound when an entity touches it", true);

	public static final ConfigValue<Boolean> uninsulatedElectrocutionParticles = boolValue("misc", "cable", "uninsulatedElectrocutionParticles", "When true an uninsulated cable will create a spark when an entity touches it", true);

	public static final ConfigValue<Boolean> enableOverworldLoot = boolValue("world", "loot", "enableOverworldLoot", "When true TechReborn will add ingots, machine frames and circuits to OverWorld loot chests.", true);

	public static final ConfigValue<Boolean> enableNetherLoot = boolValue("world", "loot", "enableNetherLoot", "When true TechReborn will add ingots, machine frames and circuits to Nether loot chests.", true);

	public static final ConfigValue<Boolean> enableEndLoot = boolValue("world", "loot", "enableEndLoot", "When true TechReborn will add ingots, machine frames and circuits to The End loot chests.", true);

	public static final ConfigValue<Boolean> enableFishingJunkLoot = boolValue("world", "loot", "enableFishingJunkLoot", "When true TechReborn will add items to fishing junk loot.", true);

	public static final ConfigValue<Boolean> enableOreGeneration = boolValue("world", "generation", "enableOreGeneration", "When enabled ores will generate in the world", true);

	public static final ConfigValue<Boolean> enableBauxiteOreGeneration = boolValue("world", "generation", "enableBauxiteOreGeneration", "When enabled and enableOreGeneration isn't disabled, bauxite ores will generate in the world", true);

	public static final ConfigValue<Boolean> enableCinnabarOreGeneration = boolValue("world", "generation", "enableCinnabarOreGeneration", "When enabled and enableOreGeneration isn't disabled, cinnabar ores will generate in the world", true);

	public static final ConfigValue<Boolean> enableGalenaOreGeneration = boolValue("world", "generation", "enableGalenaOreGeneration", "When enabled and enableOreGeneration isn't disabled, galena ores will generate in the world", true);

	public static final ConfigValue<Boolean> enableIridiumOreGeneration = boolValue("world", "generation", "enableIridiumOreGeneration", "When enabled and enableOreGeneration isn't disabled, iridium ores will generate in the world", true);

	public static final ConfigValue<Boolean> enableLeadOreGeneration = boolValue("world", "generation", "enableLeadOreGeneration", "When enabled and enableOreGeneration isn't disabled, lead ores will generate in the world", true);

	public static final ConfigValue<Boolean> enablePeridotOreGeneration = boolValue("world", "generation", "enablePeridotOreGeneration", "When enabled and enableOreGeneration isn't disabled, peridot ores will generate in the world", true);

	public static final ConfigValue<Boolean> enablePyriteOreGeneration = boolValue("world", "generation", "enablePyriteOreGeneration", "When enabled and enableOreGeneration isn't disabled, pyrite ores will generate in the world", true);

	public static final ConfigValue<Boolean> enableRubyOreGeneration = boolValue("world", "generation", "enableRubyOreGeneration", "When enabled and enableOreGeneration isn't disabled, ruby ores will generate in the world", true);

	public static final ConfigValue<Boolean> enableSapphireOreGeneration = boolValue("world", "generation", "enableSapphireOreGeneration", "When enabled and enableOreGeneration isn't disabled, sapphire ores will generate in the world", true);

	public static final ConfigValue<Boolean> enableSheldoniteOreGeneration = boolValue("world", "generation", "enableSheldoniteOreGeneration", "When enabled and enableOreGeneration isn't disabled, sheldonite ores will generate in the world", true);

	public static final ConfigValue<Boolean> enableSilverOreGeneration = boolValue("world", "generation", "enableSilverOreGeneration", "When enabled and enableOreGeneration isn't disabled, silver ores will generate in the world", true);

	public static final ConfigValue<Boolean> enableSodaliteOreGeneration = boolValue("world", "generation", "enableSodaliteOreGeneration", "When enabled and enableOreGeneration isn't disabled, sodalite ores will generate in the world", true);

	public static final ConfigValue<Boolean> enableSphaleriteOreGeneration = boolValue("world", "generation", "enableSphaleriteOreGeneration", "When enabled and enableOreGeneration isn't disabled, sphalerite ores will generate in the world", true);

	public static final ConfigValue<Boolean> enableTinOreGeneration = boolValue("world", "generation", "enableTinOreGeneration", "When enabled and enableOreGeneration isn't disabled, tin ores will generate in the world", true);

	public static final ConfigValue<Boolean> enableTungstenOreGeneration = boolValue("world", "generation", "enableTungstenOreGeneration", "When enabled and enableOreGeneration isn't disabled, tungsten ores will generate in the world", true);

	public static final ConfigValue<Boolean> enableUraniumOreGeneration = boolValue("world", "generation", "enableUraniumOreGeneration", "When enabled and enableOreGeneration isn't disabled, uranium ores will generate in the world", true);

	public static final ConfigValue<Boolean> enableRubberTreeGeneration = boolValue("world", "generation", "enableRubberTreeGeneration", "When enabled rubber trees will generate in the world", true);

	public static final ConfigValue<Boolean> enableOilLakeGeneration = boolValue("world", "generation", "enableOilLakeGeneration", "When enabled oil lakes will generate in the world", true);

	public static final ConfigValue<Boolean> enableMetallurgistGeneration = boolValue("world", "generation", "enableMetallurgistGeneration", "When enabled metallurgist houses can generate in villages", true);

	public static final ConfigValue<Boolean> enableElectricianGeneration = boolValue("world", "generation", "enableElectricianGeneration", "When enabled electrician houses can generate in villages", true);

	private TechRebornConfig() {
	}

	public static void init() {
		CONFIGS.values().forEach(RebornCoreConfigApi::register);
	}

	private static Config config(String name) {
		return CONFIGS.computeIfAbsent(name, key -> RebornCoreConfigApi.config(Identifier.fromNamespaceAndPath(MOD_ID, key)));
	}

	private static ConfigGroup category(String configName, String categoryName) {
		return CATEGORIES.computeIfAbsent(configName + '\0' + categoryName, key -> config(configName).group(categoryName));
	}

	private static <T> ConfigValue<T> value(String configName, String categoryName, String key, Codec<T> codec, T defaultValue, String comment) {
		return category(configName, categoryName).codec(key, codec, defaultValue).comment(comment);
	}

	private static ConfigValue<Boolean> boolValue(String configName, String categoryName, String key, String comment, boolean defaultValue) {
		return value(configName, categoryName, key, Codec.BOOL, defaultValue, comment);
	}

	private static ConfigValue<String> stringValue(String configName, String categoryName, String key, String comment, String defaultValue) {
		return value(configName, categoryName, key, Codec.STRING, defaultValue, comment);
	}

	private static ConfigValue<List<String>> stringListValue(String configName, String categoryName, String key, String comment, List<String> defaultValue) {
		return value(configName, categoryName, key, Codec.STRING.listOf(), defaultValue, comment);
	}

	private static ConfigValue<Integer> intValue(String configName, String categoryName, String key, String comment, int defaultValue) {
		return value(configName, categoryName, key, Codec.INT, defaultValue, comment);
	}

	private static ConfigValue<Long> longValue(String configName, String categoryName, String key, String comment, long defaultValue) {
		return value(configName, categoryName, key, Codec.LONG, defaultValue, comment);
	}

	private static ConfigValue<Float> floatValue(String configName, String categoryName, String key, String comment, float defaultValue) {
		return value(configName, categoryName, key, Codec.FLOAT, defaultValue, comment);
	}

	private static ConfigValue<Float> clampedFloatValue(String configName, String categoryName, String key, String comment, float defaultValue) {
		return value(configName, categoryName, key, Codec.FLOAT.xmap(value -> Mth.clamp(value, 0.0F, 1.0F), value -> value), defaultValue, comment);
	}

	private static ConfigValue<Double> doubleValue(String configName, String categoryName, String key, String comment, double defaultValue) {
		return value(configName, categoryName, key, Codec.DOUBLE, defaultValue, comment);
	}
}
