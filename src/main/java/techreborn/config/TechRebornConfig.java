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
import java.util.List;

import com.mojang.serialization.Codec;

import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import reborncore.common.config.LegacyConfigCleanup;
import reborncore.common.config.Config;
import reborncore.common.config.ConfigGroup;
import reborncore.common.config.ConfigValue;
import reborncore.common.config.RebornCoreConfigApi;

// All moved into one class as it's a lot easier to find the config values when you know where they all are
public final class TechRebornConfig {
	private static final String MOD_ID = "techreborn";
	private static final Codec<Float> CLAMPED_FLOAT_CODEC = Codec.FLOAT.xmap(value -> Mth.clamp(value, 0.0F, 1.0F), value -> value);
	private static final Identifier GENERATORS_ID = Identifier.fromNamespaceAndPath(MOD_ID, "generators");
	private static final Identifier ITEMS_ID = Identifier.fromNamespaceAndPath(MOD_ID, "items");
	private static final Identifier MACHINES_ID = Identifier.fromNamespaceAndPath(MOD_ID, "machines");
	private static final Identifier MISC_ID = Identifier.fromNamespaceAndPath(MOD_ID, "misc");
	private static final Identifier WORLD_ID = Identifier.fromNamespaceAndPath(MOD_ID, "world");
	private static final Config GENERATORS = RebornCoreConfigApi.config(GENERATORS_ID);
	private static final Config ITEMS = RebornCoreConfigApi.config(ITEMS_ID);
	private static final Config MACHINES = RebornCoreConfigApi.config(MACHINES_ID);
	private static final Config MISC = RebornCoreConfigApi.config(MISC_ID);
	private static final Config WORLD = RebornCoreConfigApi.config(WORLD_ID);

	private static final ConfigGroup GENERATORS_SOLAR_PANEL_GENERAL = GENERATORS.group("solarPanelGeneral");
	public static final ConfigValue<Integer> solarInternalCapacityMultiplier = GENERATORS_SOLAR_PANEL_GENERAL.codec("internalCapacity", Codec.INT, 2000).comment("Multiplier for internal capacity of solar panels (multiplier * day generation rate)");

	private static final ConfigGroup GENERATORS_SOLAR_PANEL_BASIC = GENERATORS.group("solarPanelBasic");
	public static final ConfigValue<Integer> basicGenerationRateD = GENERATORS_SOLAR_PANEL_BASIC.codec("basicDayRate", Codec.INT, 3).comment("Generation rate during day for Basic Solar Panel (Energy per tick)");
	public static final ConfigValue<Integer> basicGenerationRateN = GENERATORS_SOLAR_PANEL_BASIC.codec("basicNightRate", Codec.INT, 0).comment("Generation rate during night for Basic Solar Panel (Energy per tick)");

	private static final ConfigGroup GENERATORS_SOLAR_PANEL_ADVANCED = GENERATORS.group("solarPanelAdvanced");
	public static final ConfigValue<Integer> advancedGenerationRateD = GENERATORS_SOLAR_PANEL_ADVANCED.codec("advancedDayRate", Codec.INT, 12).comment("Generation rate during day for Advanced Solar Panel (Energy per tick)");
	public static final ConfigValue<Integer> advancedGenerationRateN = GENERATORS_SOLAR_PANEL_ADVANCED.codec("advancedNightRate", Codec.INT, 0).comment("Generation rate during night for Advanced Solar Panel (Energy per tick)");

	private static final ConfigGroup GENERATORS_SOLAR_PANEL_INDUSTRIAL = GENERATORS.group("solarPanelIndustrial");
	public static final ConfigValue<Integer> industrialGenerationRateD = GENERATORS_SOLAR_PANEL_INDUSTRIAL.codec("industrialDayRate", Codec.INT, 24).comment("Generation rate during day for Industrial Solar Panel (Energy per tick)");
	public static final ConfigValue<Integer> industrialGenerationRateN = GENERATORS_SOLAR_PANEL_INDUSTRIAL.codec("industrialNightRate", Codec.INT, 4).comment("Generation rate during night for Industrial Solar Panel (Energy per tick)");

	private static final ConfigGroup GENERATORS_SOLAR_PANEL_ULTIMATE = GENERATORS.group("solarPanelUltimate");
	public static final ConfigValue<Integer> ultimateGenerationRateD = GENERATORS_SOLAR_PANEL_ULTIMATE.codec("ultimateDayRate", Codec.INT, 48).comment("Generation rate during day for Ultimate Solar Panel (Energy per tick)");
	public static final ConfigValue<Integer> ultimateGenerationRateN = GENERATORS_SOLAR_PANEL_ULTIMATE.codec("ultimateNightRate", Codec.INT, 8).comment("Generation rate during night for Ultimate Solar Panel (Energy per tick)");

	private static final ConfigGroup GENERATORS_SOLAR_PANEL_QUANTUM = GENERATORS.group("solarPanelQuantum");
	public static final ConfigValue<Integer> quantumGenerationRateD = GENERATORS_SOLAR_PANEL_QUANTUM.codec("quantumDayRate", Codec.INT, 512).comment("Generation rate during day for Quantum Solar Panel (Energy per tick)");
	public static final ConfigValue<Integer> quantumGenerationRateN = GENERATORS_SOLAR_PANEL_QUANTUM.codec("quantumNightRate", Codec.INT, 32).comment("Generation rate during night for Quantum Solar Panel (Energy per tick)");

	private static final ConfigGroup GENERATORS_LIGHTNING_ROD = GENERATORS.group("lightning_rod");
	public static final ConfigValue<Integer> lightningRodMaxOutput = GENERATORS_LIGHTNING_ROD.codec("LightningRodMaxOutput", Codec.INT, 2048).comment("Lightning Rod Max Output (Energy per tick)");
	public static final ConfigValue<Integer> lightningRodMaxEnergy = GENERATORS_LIGHTNING_ROD.codec("LightningRodMaxEnergy", Codec.INT, 100_000_000).comment("Lightning Rod Max Energy");
	public static final ConfigValue<Integer> lightningRodChanceOfStrike = GENERATORS_LIGHTNING_ROD.codec("LightningRodChanceOfStrike", Codec.INT, 24).comment("Chance of lightning striking a rod (Range: 0-70)");
	public static final ConfigValue<Integer> lightningRodBaseEnergyStrike = GENERATORS_LIGHTNING_ROD.codec("LightningRodBaseStrikeEnergy", Codec.INT, 262_144).comment("Base amount of Energy per strike");

	private static final ConfigGroup GENERATORS_THERMAL_GENERATOR = GENERATORS.group("thermal_generator");
	public static final ConfigValue<Integer> thermalGeneratorMaxOutput = GENERATORS_THERMAL_GENERATOR.codec("ThermalGeneratorMaxOutput", Codec.INT, 128).comment("Thermal Generator Max Output (Energy per tick)");
	public static final ConfigValue<Integer> thermalGeneratorMaxEnergy = GENERATORS_THERMAL_GENERATOR.codec("ThermalGeneratorMaxEnergy", Codec.INT, 1_000_000).comment("Thermal Generator Max Energy");
	public static final ConfigValue<Integer> thermalGeneratorEnergyPerTick = GENERATORS_THERMAL_GENERATOR.codec("ThermalGeneratorEnergyPerTick", Codec.INT, 16).comment("Thermal Generator Energy Per Tick");

	private static final ConfigGroup GENERATORS_PLASMA_GENERATOR = GENERATORS.group("plasma_generator");
	public static final ConfigValue<Integer> plasmaGeneratorMaxOutput = GENERATORS_PLASMA_GENERATOR.codec("PlasmaGeneratorMaxOutput", Codec.INT, 2048).comment("Plasma Generator Max Output (Energy per tick)");
	public static final ConfigValue<Long> plasmaGeneratorMaxEnergy = GENERATORS_PLASMA_GENERATOR.codec("PlasmaGeneratorMaxEnergy", Codec.LONG, 500_000_000L).comment("Plasma Generator Max Energy");
	public static final ConfigValue<Integer> plasmaGeneratorEnergyPerTick = GENERATORS_PLASMA_GENERATOR.codec("PlasmaGeneratorEnergyPerTick", Codec.INT, 400).comment("Plasma Generator Energy Per Tick");

	private static final ConfigGroup GENERATORS_WIND_MILL = GENERATORS.group("wind_mill");
	public static final ConfigValue<Integer> windMillMaxOutput = GENERATORS_WIND_MILL.codec("WindMillMaxOutput", Codec.INT, 128).comment("Wind Mill Max Output (Energy per tick)");
	public static final ConfigValue<Integer> windMillMaxEnergy = GENERATORS_WIND_MILL.codec("WindMillMaxEnergy", Codec.INT, 10_000).comment("Wind Mill Max Energy");
	public static final ConfigValue<Integer> windMillBaseEnergy = GENERATORS_WIND_MILL.codec("WindMillEnergyPerTick", Codec.INT, 2).comment("Wind Mill generation rate (Energy Per Tick)");
	public static final ConfigValue<Double> windMillThunderMultiplier = GENERATORS_WIND_MILL.codec("WindMillThunderMultiplier", Codec.DOUBLE, 1.25).comment("Wind Mill Thunder Multiplier");

	private static final ConfigGroup GENERATORS_WATER_MILL = GENERATORS.group("water_mill");
	public static final ConfigValue<Integer> waterMillMaxOutput = GENERATORS_WATER_MILL.codec("WaterMillMaxOutput", Codec.INT, 32).comment("Water Mill Max Output (Energy per tick)");
	public static final ConfigValue<Integer> waterMillMaxEnergy = GENERATORS_WATER_MILL.codec("WaterMillMaxEnergy", Codec.INT, 1000).comment("Water Mill Max Energy");
	public static final ConfigValue<Double> waterMillEnergyMultiplier = GENERATORS_WATER_MILL.codec("WaterMillEnergyPerTick", Codec.DOUBLE, 0.1).comment("Water Mill generation rate per water block (Energy per tick)");

	private static final ConfigGroup GENERATORS_SEMIFLUID_GENERATOR = GENERATORS.group("semifluid_generator");
	public static final ConfigValue<Integer> semiFluidGeneratorMaxOutput = GENERATORS_SEMIFLUID_GENERATOR.codec("SemifluidGeneratorMaxOutput", Codec.INT, 128).comment("Semifluid Generator Max Output (Energy per tick)");
	public static final ConfigValue<Integer> semiFluidGeneratorMaxEnergy = GENERATORS_SEMIFLUID_GENERATOR.codec("SemifluidGeneratorMaxEnergy", Codec.INT, 1000000).comment("Semifluid Generator Max Energy");
	public static final ConfigValue<Integer> semiFluidGeneratorEnergyPerTick = GENERATORS_SEMIFLUID_GENERATOR.codec("SemifluidGeneratorEnergyPerTick", Codec.INT, 8).comment("Semifluid Generator Energy Per Tick");

	private static final ConfigGroup GENERATORS_GAS_GENERATOR = GENERATORS.group("gas_generator");
	public static final ConfigValue<Integer> gasTurbineMaxOutput = GENERATORS_GAS_GENERATOR.codec("GasGeneratorMaxOutput", Codec.INT, 128).comment("Gas Generator Max Output (Energy per tick)");
	public static final ConfigValue<Integer> gasTurbineMaxEnergy = GENERATORS_GAS_GENERATOR.codec("GasGeneratorMaxEnergy", Codec.INT, 1000000).comment("Gas Generator Max Energy");
	public static final ConfigValue<Integer> gasTurbineEnergyPerTick = GENERATORS_GAS_GENERATOR.codec("GasGeneratorEnergyPerTick", Codec.INT, 16).comment("Gas Generator Energy Per Tick");

	private static final ConfigGroup GENERATORS_DIESEL_GENERATOR = GENERATORS.group("diesel_generator");
	public static final ConfigValue<Integer> dieselGeneratorMaxOutput = GENERATORS_DIESEL_GENERATOR.codec("DieselGeneratorMaxOutput", Codec.INT, 32).comment("Diesel Generator Max Output (Energy per tick)");
	public static final ConfigValue<Integer> dieselGeneratorMaxEnergy = GENERATORS_DIESEL_GENERATOR.codec("DieselGeneratorMaxEnergy", Codec.INT, 10_000).comment("Diesel Generator Max Energy");
	public static final ConfigValue<Integer> dieselGeneratorEnergyPerTick = GENERATORS_DIESEL_GENERATOR.codec("DieselGeneratorEnergyPerTick", Codec.INT, 20).comment("Diesel Generator Energy Per Tick");

	private static final ConfigGroup GENERATORS_DRAGON_EGG_SIPHONER = GENERATORS.group("dragon_egg_siphoner");
	public static final ConfigValue<Integer> dragonEggSyphonMaxOutput = GENERATORS_DRAGON_EGG_SIPHONER.codec("DragonEggSiphonerMaxOutput", Codec.INT, 128).comment("Dragon Egg Siphoner Max Output (Energy per tick)");
	public static final ConfigValue<Integer> dragonEggSyphonMaxEnergy = GENERATORS_DRAGON_EGG_SIPHONER.codec("DragonEggSiphonerMaxEnergy", Codec.INT, 1000).comment("Dragon Egg Siphoner Max Energy");
	public static final ConfigValue<Integer> dragonEggSyphonEnergyPerTick = GENERATORS_DRAGON_EGG_SIPHONER.codec("DragonEggSiphonerEnergyPerTick", Codec.INT, 4).comment("Dragon Egg Siphoner Energy Per Tick");

	private static final ConfigGroup GENERATORS_GENERATOR = GENERATORS.group("generator");
	public static final ConfigValue<Integer> solidFuelGeneratorMaxOutput = GENERATORS_GENERATOR.codec("GeneratorMaxOutput", Codec.INT, 32).comment("Solid Fuel Generator Max Output (Energy per tick)");
	public static final ConfigValue<Integer> solidFuelGeneratorMaxEnergy = GENERATORS_GENERATOR.codec("GeneratorMaxEnergy", Codec.INT, 10_000).comment("Solid Fuel Generator Max Energy");
	public static final ConfigValue<Integer> solidFuelGeneratorOutputAmount = GENERATORS_GENERATOR.codec("GeneratorEnergyOutput", Codec.INT, 10).comment("Solid Fuel Generator Energy Per Tick");

	private static final ConfigGroup GENERATORS_NUCLEAR_REACTOR = GENERATORS.group("nuclear_reactor");
	public static final ConfigValue<Integer> nuclearReactorMaxEnergy = GENERATORS_NUCLEAR_REACTOR.codec("NuclearReactorMaxEnergy", Codec.INT, 100_000_000).comment("Nuclear Reactor Max Energy");
	public static final ConfigValue<Integer> nuclearReactorMaxOutput = GENERATORS_NUCLEAR_REACTOR.codec("NuclearReactorMaxOutput", Codec.INT, 8192).comment("Nuclear Reactor Max Output");
	public static final ConfigValue<Integer> nuclearReactorMaxHeat = GENERATORS_NUCLEAR_REACTOR.codec("NuclearReactorMaxHeat", Codec.INT, 10_000).comment("Nuclear Reactor Base Max Heat (before plating)");
	public static final ConfigValue<Double> nuclearReactorEUMultiplier = GENERATORS_NUCLEAR_REACTOR.codec("NuclearReactorEUMultiplier", Codec.DOUBLE, 1.0).comment("Multiplier for EU output from fuel rods");
	public static final ConfigValue<Integer> nuclearReactorTickRate = GENERATORS_NUCLEAR_REACTOR.codec("NuclearReactorTickRate", Codec.INT, 20).comment("Ticks between reactor processing cycles (20 = 1 second)");
	public static final ConfigValue<Boolean> nuclearReactorExplosionEnabled = GENERATORS_NUCLEAR_REACTOR.codec("NuclearReactorExplosionEnabled", Codec.BOOL, false).comment("Enable Nuclear Reactor Explosions on Meltdown");
	public static final ConfigValue<Float> nuclearReactorExplosionPowerLimit = GENERATORS_NUCLEAR_REACTOR.codec("NuclearReactorExplosionPowerLimit", Codec.FLOAT, 45.0f).comment("Maximum explosion power for meltdowns");

	private static final ConfigGroup ITEMS_POWER = ITEMS.group("power");
	public static final ConfigValue<Integer> nanosaberCharge = ITEMS_POWER.codec("nanoSaberCharge", Codec.INT, 1_000_000).comment("Energy Capacity for Nano Saber");
	public static final ConfigValue<Integer> nanosaberCost = ITEMS_POWER.codec("nanoSaberCost", Codec.INT, 150).comment("Energy Cost for Nano Saber");
	public static final ConfigValue<Integer> nanosaberDamage = ITEMS_POWER.codec("nanoSaberDamage", Codec.INT, 20).comment("Damage value for the Nano Saber");
	public static final ConfigValue<Integer> electricTreetapCharge = ITEMS_POWER.codec("electricTreetapCharge", Codec.INT, 10_000).comment("Energy Capacity for Electric Treetap");
	public static final ConfigValue<Integer> electricTreetapCost = ITEMS_POWER.codec("electricTreetapCost", Codec.INT, 20).comment("Energy Cost for Electric Treetap");
	public static final ConfigValue<Integer> basicDrillCharge = ITEMS_POWER.codec("basicDrillCharge", Codec.INT, 10_000).comment("Energy Capacity for Basic Drill");
	public static final ConfigValue<Integer> basicDrillCost = ITEMS_POWER.codec("basicDrillCost", Codec.INT, 50).comment("Energy Cost for Basic Drill");
	public static final ConfigValue<Integer> advancedDrillCharge = ITEMS_POWER.codec("advancedDrillCharge", Codec.INT, 100_000).comment("Energy Capacity for Advanced Drill");
	public static final ConfigValue<Integer> advancedDrillCost = ITEMS_POWER.codec("advancedDrillCost", Codec.INT, 100).comment("Energy Cost for Advanced Drill");
	public static final ConfigValue<Integer> industrialDrillCharge = ITEMS_POWER.codec("industrialDrillCharge", Codec.INT, 1_000_000).comment("Energy Capacity for Industrial Drill");
	public static final ConfigValue<Integer> industrialDrillCost = ITEMS_POWER.codec("industrialDrillCost", Codec.INT, 150).comment("Energy Cost for Industrial Drill");
	public static final ConfigValue<Integer> basicChainsawCharge = ITEMS_POWER.codec("basicChainsawCharge", Codec.INT, 10_000).comment("Energy Capacity for Basic Chainsaw");
	public static final ConfigValue<Integer> basicChainsawCost = ITEMS_POWER.codec("basicChainsawCost", Codec.INT, 50).comment("Energy Cost for Basic Chainsaw");
	public static final ConfigValue<Integer> advancedChainsawCharge = ITEMS_POWER.codec("advancedChainsawCharge", Codec.INT, 100_000).comment("Energy Capacity for Advanced Chainsaw");
	public static final ConfigValue<Integer> advancedChainsawCost = ITEMS_POWER.codec("advancedChainsawCost", Codec.INT, 100).comment("Energy Cost for Advanced Chainsaw");
	public static final ConfigValue<Integer> industrialChainsawCharge = ITEMS_POWER.codec("industrialChainsawCharge", Codec.INT, 1_000_000).comment("Energy Capacity for Industrial Chainsaw");
	public static final ConfigValue<Integer> industrialChainsawCost = ITEMS_POWER.codec("industrialChainsawCost", Codec.INT, 150).comment("Energy Cost for Industrial Chainsaw");
	public static final ConfigValue<Integer> basicJackhammerCharge = ITEMS_POWER.codec("basicJackhammerCharge", Codec.INT, 10_000).comment("Energy Capacity for Basic Jackhammer");
	public static final ConfigValue<Integer> basicJackhammerCost = ITEMS_POWER.codec("basicJackhammerCost", Codec.INT, 50).comment("Energy Cost for Basic Jackhammer");
	public static final ConfigValue<Integer> advancedJackhammerCharge = ITEMS_POWER.codec("advancedJackhammerCharge", Codec.INT, 100_000).comment("Energy Capacity for Advanced Jackhammer");
	public static final ConfigValue<Integer> advancedJackhammerCost = ITEMS_POWER.codec("advancedJackhammerCost", Codec.INT, 100).comment("Energy Cost for Advanced Jackhammer");
	public static final ConfigValue<Integer> industrialJackhammerCharge = ITEMS_POWER.codec("industrialJackhammerCharge", Codec.INT, 1_000_000).comment("Energy Capacity for Industrial Jackhammer");
	public static final ConfigValue<Integer> industrialJackhammerCost = ITEMS_POWER.codec("industrialJackhammerCost", Codec.INT, 150).comment("Energy Cost for Industrial Jackhammer");
	public static final ConfigValue<Integer> omniToolCharge = ITEMS_POWER.codec("omniToolCharge", Codec.INT, 1_000_000).comment("Energy Capacity for Omni Tool");
	public static final ConfigValue<Integer> omniToolCost = ITEMS_POWER.codec("omniToolCost", Codec.INT, 100).comment("Energy Cost for Omni Tool");
	public static final ConfigValue<Integer> omniToolHitCost = ITEMS_POWER.codec("omniToolHitCost", Codec.INT, 125).comment("Hit Energy Cost for Omni Tool");
	public static final ConfigValue<Integer> rockCutterCharge = ITEMS_POWER.codec("rockCutterCharge", Codec.INT, 10_000).comment("Energy Capacity for Rock Cutter");
	public static final ConfigValue<Integer> rockCutterCost = ITEMS_POWER.codec("rockCutterCost", Codec.INT, 10).comment("Energy Cost for Rock Cutter");
	public static final ConfigValue<Integer> lapotronPackCharge = ITEMS_POWER.codec("lapotronPackCharge", Codec.INT, 100_000_000).comment("Energy Capacity for Lapotron Pack");
	public static final ConfigValue<Integer> lithiumBatpackCharge = ITEMS_POWER.codec("LithiumBatpackCharge", Codec.INT, 600_000).comment("Energy Capacity for Lithium Batpack");
	public static final ConfigValue<Integer> redCellBatteryMaxCharge = ITEMS_POWER.codec("redCellBatteryMaxCharge", Codec.INT, 10_000).comment("Energy Capacity for Red Cell Battery");
	public static final ConfigValue<Integer> lithiumIonBatteryMaxCharge = ITEMS_POWER.codec("lithiumIonBatteryMaxCharge", Codec.INT, 100_000).comment("Energy Capacity for Lithium Ion Battery");
	public static final ConfigValue<Integer> energyCrystalMaxCharge = ITEMS_POWER.codec("energyCrystalMaxCharge", Codec.INT, 1_000_000).comment("Energy Capacity for Energy Crystal");
	public static final ConfigValue<Integer> lapotronCrystalMaxCharge = ITEMS_POWER.codec("lapotronCrystalMaxCharge", Codec.INT, 10_000_000).comment("Energy Capacity for Lapotron Crystal");
	public static final ConfigValue<Integer> lapotronicOrbMaxCharge = ITEMS_POWER.codec("lapotronicOrbMaxCharge", Codec.INT, 100_000_000).comment("Energy Capacity for Lapotronic Orb");
	public static final ConfigValue<Long> cloakingDeviceCharge = ITEMS_POWER.codec("cloakingDeviceCharge", Codec.LONG, 40_000_000L).comment("Energy Capacity for Cloaking Device");
	public static final ConfigValue<Integer> cloakingDeviceCost = ITEMS_POWER.codec("clockingDeviceEnergyUsage", Codec.INT, 10).comment("Cloaking device energy usage");
	public static final ConfigValue<Long> quantumSuitCapacity = ITEMS_POWER.codec("quantumSuitCapacity", Codec.LONG, 40_000_000L).comment("Quantum Suit Energy Capacity");
	public static final ConfigValue<Long> quantumSuitFlyingCost = ITEMS_POWER.codec("quantumSuitFlyingCost", Codec.LONG, 50L).comment("Quantum Suit Flying Cost");
	public static final ConfigValue<Long> quantumSuitSwimmingCost = ITEMS_POWER.codec("quantumSuitSwimmingCost", Codec.LONG, 20L).comment("Quantum Suit Swimming Cost");
	public static final ConfigValue<Long> quantumSuitBreathingCost = ITEMS_POWER.codec("quantumSuitBreathingCost", Codec.LONG, 20L).comment("Quantum Suit Breathing Cost");
	public static final ConfigValue<Long> quantumSuitSprintingCost = ITEMS_POWER.codec("quantumSuitSprintingCost", Codec.LONG, 20L).comment("Quantum Suit Sprinting Cost");
	public static final ConfigValue<Long> fireExtinguishCost = ITEMS_POWER.codec("quantumSuitFireExtinguishCost", Codec.LONG, 50L).comment("Quantum Suit Cost for Fire Extinguish");
	public static final ConfigValue<Boolean> quantumSuitEnableSprint = ITEMS_POWER.codec("quantumSuitEnableSprint", Codec.BOOL, true).comment("Enable Sprint Speed increase for Quantum Legs");
	public static final ConfigValue<Boolean> quantumSuitEnableFlight = ITEMS_POWER.codec("quantumSuitEnableFlight", Codec.BOOL, true).comment("Enable Flight for Quantum Chest");
	public static final ConfigValue<Double> damageAbsorbCost = ITEMS_POWER.codec("quantumSuitDamageAbsorbCost", Codec.DOUBLE, 10.0).comment("Quantum Suit Cost for Damage Absorbed");
	public static final ConfigValue<Long> nanoSuitCapacity = ITEMS_POWER.codec("nanoSuitCapacity", Codec.LONG, 1_000_000L).comment("Nano Suit Energy Capacity");
	public static final ConfigValue<Long> suitNightVisionCost = ITEMS_POWER.codec("suitNightVisionCost", Codec.LONG, 1L).comment("Nano/Quantum Suit Night Vision Cost");
	public static final ConfigValue<Long> nanoArmorEnergyCost = ITEMS_POWER.codec("nanoArmorEnergyCost", Codec.LONG, 100L).comment("Nano Suit Energy Cost");

	private static final ConfigGroup ITEMS_UPGRADES = ITEMS.group("upgrades");
	public static final ConfigValue<Double> overclockerSpeed = ITEMS_UPGRADES.codec("overclocker_speed", Codec.DOUBLE, 0.25).comment("Overclocker behavior speed multiplier");
	public static final ConfigValue<Double> overclockerPower = ITEMS_UPGRADES.codec("overclocker_power", Codec.DOUBLE, 0.75).comment("Overclocker behavior power multiplier");
	public static final ConfigValue<Double> energyStoragePower = ITEMS_UPGRADES.codec("energy_storage", Codec.DOUBLE, 40_000.0).comment("Energy storage behavior extra power");
	public static final ConfigValue<Double> superConductorCount = ITEMS_UPGRADES.codec("super_conductor", Codec.DOUBLE, 1.0).comment("Energy flow power increase");

	private static final ConfigGroup MACHINES_LESU = MACHINES.group("lesu");
	public static final ConfigValue<Integer> lesuStoragePerBlock = MACHINES_LESU.codec("LesuMaxEnergyPerBlock", Codec.INT, 4_000_000).comment("LESU Max Energy Per Block");
	public static final ConfigValue<Integer> lesuExtraIOPerBlock = MACHINES_LESU.codec("LesuExtraIO", Codec.INT, 64).comment("LESU Extra I/O Multiplier");
	public static final ConfigValue<Integer> lesuBaseOutput = MACHINES_LESU.codec("LesuBaseOutput", Codec.INT, 64).comment("LESU Base Output");

	private static final ConfigGroup MACHINES_AESU = MACHINES.group("aesu");
	public static final ConfigValue<Integer> aesuMaxEnergy = MACHINES_AESU.codec("AesuMaxEnergy", Codec.INT, 100_000_000).comment("AESU Max Energy");

	private static final ConfigGroup MACHINES_IDSU = MACHINES.group("idsu");
	public static final ConfigValue<Integer> idsuMaxEnergy = MACHINES_IDSU.codec("IdsuMaxEnergy", Codec.INT, 1_000_000_000).comment("IDSU Max Energy");

	private static final ConfigGroup MACHINES_STORAGE = MACHINES.group("storage");
	public static final ConfigValue<Integer> crudeStorageUnitMaxStorage = MACHINES_STORAGE.codec("CrudeStorageUnitMaxStorage", Codec.INT, 1 << 11).comment("Maximum amount of items a Crude Storage Unit can store");
	public static final ConfigValue<Integer> basicStorageUnitMaxStorage = MACHINES_STORAGE.codec("BasicStorageUnitMaxStorage", Codec.INT, 1 << 13).comment("Maximum amount of items a Basic Storage Unit can store");
	public static final ConfigValue<Integer> basicTankUnitCapacity = MACHINES_STORAGE.codec("BasicTankUnitCapacity", Codec.INT, 1 << 7).comment("How much liquid a Basic Tank Unit can take (Value in buckets, 1000 Mb)");
	public static final ConfigValue<Integer> advancedStorageUnitMaxStorage = MACHINES_STORAGE.codec("AdvancedStorageMaxStorage", Codec.INT, 1 << 15).comment("Maximum amount of items an Advanced Storage Unit can store");
	public static final ConfigValue<Integer> advancedTankUnitMaxStorage = MACHINES_STORAGE.codec("AdvancedTankUnitMaxStorage", Codec.INT, 1 << 9).comment("How much liquid an Advanced Tank Unit can take (Value in buckets, 1000 Mb)");
	public static final ConfigValue<Integer> industrialStorageUnitMaxStorage = MACHINES_STORAGE.codec("IndustrialStorageMaxStorage", Codec.INT, 1 << 16).comment("Maximum amount of items an Industrial Storage Unit can store (Compat: >= 32768)");
	public static final ConfigValue<Integer> industrialTankUnitCapacity = MACHINES_STORAGE.codec("IndustrialTankUnitCapacity", Codec.INT, 1 << 10).comment("How much liquid an Industrial Tank Unit can take (Value in buckets, 1000 Mb)");
	public static final ConfigValue<Integer> quantumStorageUnitMaxStorage = MACHINES_STORAGE.codec("QuantumStorageUnitMaxStorage", Codec.INT, Integer.MAX_VALUE).comment("Maximum amount of items a Quantum Storage Unit can store (Compat: == MAX_VALUE)");
	public static final ConfigValue<Integer> quantumTankUnitCapacity = MACHINES_STORAGE.codec("QuantumTankUnitCapacity", Codec.INT, Integer.MAX_VALUE / 1000).comment("How much liquid a Quantum Tank Unit can take (Value in buckets, 1000 Mb)(Compat: == MAX_VALUE)");

	private static final ConfigGroup MACHINES_PLAYER_DETECTOR = MACHINES.group("player_detector");
	public static final ConfigValue<Integer> playerDetectorMaxInput = MACHINES_PLAYER_DETECTOR.codec("PlayerDetectorMaxInput", Codec.INT, 32).comment("Player Detector Max Input (Energy per tick)");
	public static final ConfigValue<Integer> playerDetectorMaxEnergy = MACHINES_PLAYER_DETECTOR.codec("PlayerDetectorMaxEnergy", Codec.INT, 10000).comment("Player Detector Max Energy");
	public static final ConfigValue<Integer> playerDetectorEuPerTick = MACHINES_PLAYER_DETECTOR.codec("PlayerDetectorEnergyUsage", Codec.INT, 1).comment("Player Detector Energy Consumption per second");
	public static final ConfigValue<Integer> playerDetectorMaxRadius = MACHINES_PLAYER_DETECTOR.codec("PlayerDetectorMaxRadius", Codec.INT, 128).comment("Player Detector maximum detection radius");

	private static final ConfigGroup MACHINES_DISTILLATION_TOWER = MACHINES.group("Distillation_tower");
	public static final ConfigValue<Integer> distillationTowerMaxInput = MACHINES_DISTILLATION_TOWER.codec("DistillationTowerMaxInput", Codec.INT, 128).comment("Distillation Tower Max Input (Energy per tick)");
	public static final ConfigValue<Integer> distillationTowerMaxEnergy = MACHINES_DISTILLATION_TOWER.codec("DistillationTowerMaxEnergy", Codec.INT, 10_000).comment("Distillation Tower Max Energy");

	private static final ConfigGroup MACHINES_EXTRACTOR = MACHINES.group("extractor");
	public static final ConfigValue<Integer> extractorMaxInput = MACHINES_EXTRACTOR.codec("ExtractorInput", Codec.INT, 32).comment("Extractor Max Input (Energy per tick)");
	public static final ConfigValue<Integer> extractorMaxEnergy = MACHINES_EXTRACTOR.codec("ExtractorMaxEnergy", Codec.INT, 1_000).comment("Extractor Max Energy");

	private static final ConfigGroup MACHINES_GRINDER = MACHINES.group("grinder");
	public static final ConfigValue<Integer> grinderMaxInput = MACHINES_GRINDER.codec("GrinderInput", Codec.INT, 32).comment("Grinder Max Input (Energy per tick)");
	public static final ConfigValue<Integer> grinderMaxEnergy = MACHINES_GRINDER.codec("GrinderMaxEnergy", Codec.INT, 1_000).comment("Grinder Max Energy");

	private static final ConfigGroup MACHINES_COMPRESSOR = MACHINES.group("compressor");
	public static final ConfigValue<Integer> compressorMaxInput = MACHINES_COMPRESSOR.codec("CompressorInput", Codec.INT, 32).comment("Compressor Max Input (Energy per tick)");
	public static final ConfigValue<Integer> compressorMaxEnergy = MACHINES_COMPRESSOR.codec("CompressorMaxEnergy", Codec.INT, 1000).comment("Compressor Max Energy");

	private static final ConfigGroup MACHINES_ALLOY_SMELTER = MACHINES.group("alloy_smelter");
	public static final ConfigValue<Integer> alloySmelterMaxInput = MACHINES_ALLOY_SMELTER.codec("AlloySmelterMaxInput", Codec.INT, 32).comment("Alloy Smelter Max Input (Energy per tick)");
	public static final ConfigValue<Integer> alloySmelterMaxEnergy = MACHINES_ALLOY_SMELTER.codec("AlloySmelterMaxEnergy", Codec.INT, 1_000).comment("Alloy Smelter Max Energy");

	private static final ConfigGroup MACHINES_ROLLING_MACHINE = MACHINES.group("rolling_machine");
	public static final ConfigValue<Integer> rollingMachineMaxInput = MACHINES_ROLLING_MACHINE.codec("RollingMachineMaxInput", Codec.INT, 32).comment("Rolling Machine Max Input (Energy per tick)");
	public static final ConfigValue<Integer> rollingMachineMaxEnergy = MACHINES_ROLLING_MACHINE.codec("RollingMachineMaxEnergy", Codec.INT, 10000).comment("Rolling Machine Max Energy");

	private static final ConfigGroup MACHINES_CHUNK_LOADER = MACHINES.group("chunk_loader");
	public static final ConfigValue<Integer> chunkLoaderMaxRadius = MACHINES_CHUNK_LOADER.codec("ChunkLoaderMaxRadius", Codec.INT, 5).comment("Chunk Loader Max Radius");

	private static final ConfigGroup MACHINES_ASSEMBLING_MACHINE = MACHINES.group("assembling_machine");
	public static final ConfigValue<Integer> assemblingMachineMaxInput = MACHINES_ASSEMBLING_MACHINE.codec("AssemblingMachineMaxInput", Codec.INT, 128).comment("Assembling Machine Max Input (Energy per tick)");
	public static final ConfigValue<Integer> assemblingMachineMaxEnergy = MACHINES_ASSEMBLING_MACHINE.codec("AssemblingMachineMaxEnergy", Codec.INT, 10_000).comment("Assembling Machine Max Energy");

	private static final ConfigGroup MACHINES_MATTER_FABRICATOR = MACHINES.group("matter_fabricator");
	public static final ConfigValue<Integer> matterFabricatorMaxInput = MACHINES_MATTER_FABRICATOR.codec("MatterFabricatorMaxInput", Codec.INT, 8192).comment("Matter Fabricator Max Input (Energy per tick)");
	public static final ConfigValue<Integer> matterFabricatorMaxEnergy = MACHINES_MATTER_FABRICATOR.codec("MatterFabricatorMaxEnergy", Codec.INT, 10_000_000).comment("Matter Fabricator Max Energy");
	public static final ConfigValue<Integer> matterFabricatorFabricationRate = MACHINES_MATTER_FABRICATOR.codec("MatterFabricatorFabricationRate", Codec.INT, 6_000).comment("Matter Fabricator Fabrication Rate, amount of amplifier units per UUM");
	public static final ConfigValue<Integer> matterFabricatorEnergyPerAmp = MACHINES_MATTER_FABRICATOR.codec("MatterFabricatorEnergyPerAmp", Codec.INT, 5).comment("Matter Fabricator EU per amplifier unit, multiply this with the rate for total Energy");

	private static final ConfigGroup MACHINES_INDUSTRIAL_GRINDER = MACHINES.group("industrial_grinder");
	public static final ConfigValue<Integer> industrialGrinderMaxInput = MACHINES_INDUSTRIAL_GRINDER.codec("IndustrialGrinderMaxInput", Codec.INT, 128).comment("Industrial Grinder Max Input (Energy per tick)");
	public static final ConfigValue<Integer> industrialGrinderMaxEnergy = MACHINES_INDUSTRIAL_GRINDER.codec("IndustrialGrinderMaxEnergy", Codec.INT, 10_000).comment("Industrial Grinder Max Energy");

	private static final ConfigGroup MACHINES_VACUUMFREEZER = MACHINES.group("vacuumfreezer");
	public static final ConfigValue<Integer> vacuumFreezerMaxInput = MACHINES_VACUUMFREEZER.codec("VacuumFreezerInput", Codec.INT, 64).comment("Vacuum Freezer Max Input (Energy per tick)");
	public static final ConfigValue<Integer> vacuumFreezerMaxEnergy = MACHINES_VACUUMFREEZER.codec("VacuumFreezerMaxEnergy", Codec.INT, 64_000).comment("Vacuum Freezer Max Energy");

	private static final ConfigGroup MACHINES_IMPLOSION_COMPRESSOR = MACHINES.group("implosion_compressor");
	public static final ConfigValue<Integer> implosionCompressorMaxInput = MACHINES_IMPLOSION_COMPRESSOR.codec("ImplosionCompressorMaxInput", Codec.INT, 64).comment("Implosion Compressor Max Input (Energy per tick)");
	public static final ConfigValue<Integer> implosionCompressorMaxEnergy = MACHINES_IMPLOSION_COMPRESSOR.codec("ImplosionCompressorMaxEnergy", Codec.INT, 64_000).comment("Implosion Compressor Max Energy");

	private static final ConfigGroup MACHINES_INDUSTRIAL_FURNACE = MACHINES.group("industrial_furnace");
	public static final ConfigValue<Integer> industrialBlastFurnaceMaxInput = MACHINES_INDUSTRIAL_FURNACE.codec("IndustrialFurnaceMaxInput", Codec.INT, 128).comment("Industrial Blast Furnace Max Input (Energy per tick)");
	public static final ConfigValue<Integer> industrialBlastFurnaceMaxEnergy = MACHINES_INDUSTRIAL_FURNACE.codec("IndustrialFurnaceMaxEnergy", Codec.INT, 40_000).comment("Industrial Blast Furnace Max Energy");

	private static final ConfigGroup MACHINES_INDUSTRIAL_SAWMILL = MACHINES.group("industrial_sawmill");
	public static final ConfigValue<Integer> industrialSawmillMaxInput = MACHINES_INDUSTRIAL_SAWMILL.codec("IndustrialSawmillMaxInput", Codec.INT, 128).comment("Industrial Sawmill Max Input (Energy per tick)");
	public static final ConfigValue<Integer> industrialSawmillMaxEnergy = MACHINES_INDUSTRIAL_SAWMILL.codec("IndustrialSawmillMaxEnergy", Codec.INT, 10_000).comment("Industrial Sawmill Max Energy");

	private static final ConfigGroup MACHINES_AUTOCRAFTER = MACHINES.group("autocrafter");
	public static final ConfigValue<Integer> autoCraftingTableMaxInput = MACHINES_AUTOCRAFTER.codec("AutoCrafterInput", Codec.INT, 32).comment("AutoCrafting Table Max Input (Energy per tick)");
	public static final ConfigValue<Integer> autoCraftingTableMaxEnergy = MACHINES_AUTOCRAFTER.codec("AutoCrafterMaxEnergy", Codec.INT, 10_000).comment("AutoCrafting Table Max Energy");

	private static final ConfigGroup MACHINES_FLUIDREPLICATOR = MACHINES.group("fluidreplicator");
	public static final ConfigValue<Integer> fluidReplicatorMaxInput = MACHINES_FLUIDREPLICATOR.codec("FluidReplicatorMaxInput", Codec.INT, 256).comment("Fluid Replicator Max Input (Energy per tick)");
	public static final ConfigValue<Integer> fluidReplicatorMaxEnergy = MACHINES_FLUIDREPLICATOR.codec("FluidReplicatorMaxEnergy", Codec.INT, 400_000).comment("Fluid Replicator Max Energy");

	private static final ConfigGroup MACHINES_ELECTRIC_FURNACE = MACHINES.group("electric_furnace");
	public static final ConfigValue<Integer> electricFurnaceMaxInput = MACHINES_ELECTRIC_FURNACE.codec("ElectricFurnaceInput", Codec.INT, 32).comment("Electric Furnace Max Input (Energy per tick)");
	public static final ConfigValue<Integer> electricFurnaceMaxEnergy = MACHINES_ELECTRIC_FURNACE.codec("ElectricFurnaceMaxEnergy", Codec.INT, 1000).comment("Electric Furnace Max Energy");

	private static final ConfigGroup MACHINES_CHARGE_BENCH = MACHINES.group("charge_bench");
	public static final ConfigValue<Integer> chargeOMatBMaxOutput = MACHINES_CHARGE_BENCH.codec("ChargeBenchMaxOutput", Codec.INT, 512).comment("Charge Bench Max Output (Energy per tick)");
	public static final ConfigValue<Integer> chargeOMatBMaxInput = MACHINES_CHARGE_BENCH.codec("ChargeBenchMaxInput", Codec.INT, 512).comment("Charge Bench Max Input (Energy per tick)");
	public static final ConfigValue<Integer> chargeOMatBMaxEnergy = MACHINES_CHARGE_BENCH.codec("ChargeBenchMaxEnergy", Codec.INT, 100_000_000).comment("Charge Bench Max Energy");

	private static final ConfigGroup MACHINES_INDUSTRIAL_ELECTROLYZER = MACHINES.group("industrial_electrolyzer");
	public static final ConfigValue<Integer> industrialElectrolyzerMaxInput = MACHINES_INDUSTRIAL_ELECTROLYZER.codec("IndustrialElectrolyzerMaxInput", Codec.INT, 128).comment("Industrial Electrolyzer Max Input (Energy per tick)");
	public static final ConfigValue<Integer> industrialElectrolyzerMaxEnergy = MACHINES_INDUSTRIAL_ELECTROLYZER.codec("IndustrialElectrolyzerMaxEnergy", Codec.INT, 10_000).comment("Industrial Electrolyzer Max Energy");

	private static final ConfigGroup MACHINES_CENTRIFUGE = MACHINES.group("centrifuge");
	public static final ConfigValue<Integer> industrialCentrifugeMaxInput = MACHINES_CENTRIFUGE.codec("CentrifugeMaxInput", Codec.INT, 32).comment("Centrifuge Max Input (Energy per tick)");
	public static final ConfigValue<Integer> industrialCentrifugeMaxEnergy = MACHINES_CENTRIFUGE.codec("CentrifugeMaxEnergy", Codec.INT, 10_000).comment("Centrifuge Max Energy");

	private static final ConfigGroup MACHINES_CHEMICAL_REACTOR = MACHINES.group("chemical_reactor");
	public static final ConfigValue<Integer> chemicalReactorMaxInput = MACHINES_CHEMICAL_REACTOR.codec("ChemicalReactorMaxInput", Codec.INT, 128).comment("Chemical Reactor Max Input (Energy per tick)");
	public static final ConfigValue<Integer> chemicalReactorMaxEnergy = MACHINES_CHEMICAL_REACTOR.codec("ChemicalReactorMaxEnergy", Codec.INT, 10_000).comment("Chemical Reactor Max Energy");

	private static final ConfigGroup MACHINES_FUSION_REACTOR = MACHINES.group("fusion_reactor");
	public static final ConfigValue<Integer> fusionControlComputerMaxInput = MACHINES_FUSION_REACTOR.codec("FusionReactorMaxInput", Codec.INT, 8192).comment("Fusion Reactor Max Input (Energy per tick)");
	public static final ConfigValue<Integer> fusionControlComputerMaxOutput = MACHINES_FUSION_REACTOR.codec("FusionReactorMaxOutput", Codec.INT, 1_000_000).comment("Fusion Reactor Max Output (Energy per tick)");
	public static final ConfigValue<Integer> fusionControlComputerMaxEnergy = MACHINES_FUSION_REACTOR.codec("FusionReactorMaxEnergy", Codec.INT, 100_000_000).comment("Fusion Reactor Max Energy");
	public static final ConfigValue<Integer> fusionControlComputerMaxCoilSize = MACHINES_FUSION_REACTOR.codec("FusionReactorMaxCoilSize", Codec.INT, 50).comment("Fusion Reactor Max Coil size (Radius)");

	private static final ConfigGroup MACHINES_RECYCLER = MACHINES.group("recycler");
	public static final ConfigValue<Integer> recyclerMaxInput = MACHINES_RECYCLER.codec("RecyclerInput", Codec.INT, 32).comment("Recycler Max Input (Energy per tick)");
	public static final ConfigValue<Integer> recyclerMaxEnergy = MACHINES_RECYCLER.codec("RecyclerMaxEnergy", Codec.INT, 1000).comment("Recycler Max Energy");
	public static final ConfigValue<Integer> recyclerChance = MACHINES_RECYCLER.codec("RecyclerChance", Codec.INT, 6).comment("Recycler Chance to produce scrap (1 out of chance)");
	public static final ConfigValue<List<String>> recyclerBlackList = MACHINES_RECYCLER.codec("RecyclerBlacklist", Codec.STRING.listOf(), Arrays.asList("techreborn:scrap_box", "techreborn:scrap")).comment("Recycler blacklist");

	private static final ConfigGroup MACHINES_SCRAPBOXINATOR = MACHINES.group("scrapboxinator");
	public static final ConfigValue<Integer> scrapboxinatorMaxInput = MACHINES_SCRAPBOXINATOR.codec("ScrapboxinatorMaxInput", Codec.INT, 32).comment("Scrapboxinator Max Input (Energy per tick)");
	public static final ConfigValue<Integer> scrapboxinatorMaxEnergy = MACHINES_SCRAPBOXINATOR.codec("ScrapboxinatorMaxEnergy", Codec.INT, 1_000).comment("Scrapboxinator Max Energy");

	private static final ConfigGroup MACHINES_SOLID_CANNING_MACHINE = MACHINES.group("solid_canning_machine");
	public static final ConfigValue<Integer> solidCanningMachineMaxInput = MACHINES_SOLID_CANNING_MACHINE.codec("solidCanningMachineMaxInput", Codec.INT, 32).comment("Solid Canning Machine Max Input (Energy per tick)");
	public static final ConfigValue<Integer> solidCanningMachineMaxEnergy = MACHINES_SOLID_CANNING_MACHINE.codec("solidCanningMachineMaxEnergy", Codec.INT, 1_000).comment("Solid Canning Machine Max Energy");

	private static final ConfigGroup MACHINES_IRON_MACHINE = MACHINES.group("iron_machine");
	public static final ConfigValue<Double> fuelScale = MACHINES_IRON_MACHINE.codec("fuel_scale", Codec.DOUBLE, 1.25).comment("Multiplier for vanilla furnace item burn time");
	public static final ConfigValue<Double> cookingScale = MACHINES_IRON_MACHINE.codec("cooking_scale", Codec.DOUBLE, 1.25).comment("Multiplier for vanilla furnace item cook time");

	private static final ConfigGroup MACHINES_GREENHOUSE_CONTROLLER = MACHINES.group("greenhouse_controller");
	public static final ConfigValue<Integer> greenhouseControllerMaxInput = MACHINES_GREENHOUSE_CONTROLLER.codec("GreenhouseControllerMaxInput", Codec.INT, 32).comment("Greenhouse Controller Max Input (Energy per tick)");
	public static final ConfigValue<Integer> greenhouseControllerMaxEnergy = MACHINES_GREENHOUSE_CONTROLLER.codec("GreenhouseControllerMaxEnergy", Codec.INT, 1_000).comment("Greenhouse Controller Max Energy");
	public static final ConfigValue<Integer> greenhouseControllerEnergyPerTick = MACHINES_GREENHOUSE_CONTROLLER.codec("GreenhouseControllerEnergyPerTick", Codec.INT, 2).comment("Greenhouse Controller Energy usage Per Tick");
	public static final ConfigValue<Integer> greenhouseControllerEnergyPerHarvest = MACHINES_GREENHOUSE_CONTROLLER.codec("GreenhouseControllerEnergyPerHarvest", Codec.INT, 100).comment("Greenhouse Controller Energy usage Per Harvest");
	public static final ConfigValue<Integer> greenhouseControllerEnergyPerBonemeal = MACHINES_GREENHOUSE_CONTROLLER.codec("GreenhouseControllerEnergyPerBonemeal", Codec.INT, 50).comment("Greenhouse Controller Energy usage Per Bonemeal");

	private static final ConfigGroup MACHINES_DRAIN = MACHINES.group("drain");
	public static final ConfigValue<Integer> ticksUntilNextDrainAttempt = MACHINES_DRAIN.codec("TicksUntilNextDrainAttempt", Codec.INT, 10).comment("How many ticks should go between two drain attempts. 0 or negative will disable drain.");

	private static final ConfigGroup MACHINES_BLOCK_BREAKER = MACHINES.group("block_breaker");
	public static final ConfigValue<Integer> blockBreakerMaxInput = MACHINES_BLOCK_BREAKER.codec("BlockBreakerMaxInput", Codec.INT, 32).comment("Block Breaker Max Input (Energy per tick)");
	public static final ConfigValue<Integer> blockBreakerMaxEnergy = MACHINES_BLOCK_BREAKER.codec("BlockBreakerMaxEnergy", Codec.INT, 1_000).comment("Block Breaker Max Energy");
	public static final ConfigValue<Integer> blockBreakerEnergyPerTick = MACHINES_BLOCK_BREAKER.codec("BlockBreakerEnergyPerTick", Codec.INT, 5).comment("Block Breaker Energy usage Per Tick");
	public static final ConfigValue<Integer> blockBreakerBaseBreakTime = MACHINES_BLOCK_BREAKER.codec("BlockBreakerBaseBreakTime", Codec.INT, 100).comment("How many ticks a block of hardness 1 requires to be broken");

	private static final ConfigGroup MACHINES_BLOCK_PLACER = MACHINES.group("block_placer");
	public static final ConfigValue<Integer> blockPlacerMaxInput = MACHINES_BLOCK_PLACER.codec("BlockPlacerMaxInput", Codec.INT, 32).comment("Block Placer Max Input (Energy per tick)");
	public static final ConfigValue<Integer> blockPlacerMaxEnergy = MACHINES_BLOCK_PLACER.codec("BlockPlacerMaxEnergy", Codec.INT, 1_000).comment("Block Placer Max Energy");
	public static final ConfigValue<Integer> blockPlacerEnergyPerTick = MACHINES_BLOCK_PLACER.codec("BlockPlacerEnergyPerTick", Codec.INT, 5).comment("Block Placer Energy usage Per Tick");
	public static final ConfigValue<Integer> blockPlacerBaseBreakTime = MACHINES_BLOCK_PLACER.codec("BlockPlacerBaseBreakTime", Codec.INT, 100).comment("How many ticks a block of hardness 1 requires to be placed");

	private static final ConfigGroup MACHINES_LAUNCHPAD = MACHINES.group("launchpad");
	public static final ConfigValue<Integer> launchpadMaxInput = MACHINES_LAUNCHPAD.codec("LaunchpadMaxInput", Codec.INT, 128).comment("Launchpad Max Input (Energy per tick)");
	public static final ConfigValue<Integer> launchpadMaxEnergy = MACHINES_LAUNCHPAD.codec("LaunchpadMaxEnergy", Codec.INT, 40_000).comment("Launchpad Max Energy");
	public static final ConfigValue<Double> launchpadSpeedLow = MACHINES_LAUNCHPAD.codec("LaunchpadSpeedLow", Codec.DOUBLE, 1d).comment("Launchpad Low Speed");
	public static final ConfigValue<Double> launchpadSpeedMedium = MACHINES_LAUNCHPAD.codec("LaunchpadSpeedMedium", Codec.DOUBLE, 3d).comment("Launchpad Medium Speed");
	public static final ConfigValue<Double> launchpadSpeedHigh = MACHINES_LAUNCHPAD.codec("LaunchpadSpeedHigh", Codec.DOUBLE, 5d).comment("Launchpad High Speed");
	public static final ConfigValue<Double> launchpadSpeedExtreme = MACHINES_LAUNCHPAD.codec("LaunchpadSpeedExtreme", Codec.DOUBLE, 10d).comment("Launchpad Extreme Speed");
	public static final ConfigValue<Integer> launchpadEnergyLow = MACHINES_LAUNCHPAD.codec("LaunchpadEnergyLow", Codec.INT, 1_000).comment("Launchpad Low Energy");
	public static final ConfigValue<Integer> launchpadEnergyMedium = MACHINES_LAUNCHPAD.codec("LaunchpadEnergyMedium", Codec.INT, 6_000).comment("Launchpad Medium Energy");
	public static final ConfigValue<Integer> launchpadEnergyHigh = MACHINES_LAUNCHPAD.codec("LaunchpadEnergyHigh", Codec.INT, 10_000).comment("Launchpad High Energy");
	public static final ConfigValue<Integer> launchpadEnergyExtreme = MACHINES_LAUNCHPAD.codec("LaunchpadEnergyExtreme", Codec.INT, 20_000).comment("Launchpad Extreme Energy");
	public static final ConfigValue<Integer> launchpadDefaultSelection = MACHINES_LAUNCHPAD.codec("LaunchpadDefaultSelection", Codec.INT, 0).comment("Launchpad Default Selection (0-3 for Low-Extreme)");
	public static final ConfigValue<Integer> launchpadInterval = MACHINES_LAUNCHPAD.codec("LaunchpadInterval", Codec.INT, 100).comment("Launchpad Launch Interval in Ticks > 0");

	private static final ConfigGroup MACHINES_ELEVATOR = MACHINES.group("elevator");
	public static final ConfigValue<Integer> elevatorMaxInput = MACHINES_ELEVATOR.codec("ElevatorMaxInput", Codec.INT, 32).comment("Elevator Max Input (Energy per tick)");
	public static final ConfigValue<Integer> elevatorMaxEnergy = MACHINES_ELEVATOR.codec("ElevatorMaxEnergy", Codec.INT, 1_000).comment("Elevator Max Energy");
	public static final ConfigValue<Integer> elevatorEnergyPerBlock = MACHINES_ELEVATOR.codec("ElevatorEnergyPerBlock", Codec.INT, 2).comment("Elevator Energy used per vertical block of transportation");
	public static final ConfigValue<Boolean> allowElevatingThroughBlocks = MACHINES_ELEVATOR.codec("AllowElevatingThroughBlocks", Codec.BOOL, true).comment("Allow elevating through blocks (i.e. non air)");

	private static final ConfigGroup MACHINES_FISHING_STATION = MACHINES.group("fishing_station");
	public static final ConfigValue<Integer> fishingStationMaxInput = MACHINES_FISHING_STATION.codec("FishingStationMaxInput", Codec.INT, 128).comment("Fishing Station Max Input (Energy per tick)");
	public static final ConfigValue<Integer> fishingStationMaxEnergy = MACHINES_FISHING_STATION.codec("FishingStationMaxEnergy", Codec.INT, 10_000).comment("Fishing Station Max Energy");
	public static final ConfigValue<Integer> fishingStationEnergyPerCatch = MACHINES_FISHING_STATION.codec("FishingStationEnergyPerCatch", Codec.INT, 500).comment("How much energy the Fishing Station uses per catch");
	public static final ConfigValue<Integer> fishingStationInterval = MACHINES_FISHING_STATION.codec("FishingStationInterval", Codec.INT, 400).comment("Fishing Station Catch Interval in Ticks > 0");

	private static final ConfigGroup MACHINES_PUMP = MACHINES.group("pump");
	public static final ConfigValue<Integer> pumpTicksToComplete = MACHINES_PUMP.codec("PumpTicksToComplete", Codec.INT, 100).comment("How many ticks it takes to pump a source block.");
	public static final ConfigValue<Integer> pumpMaxInput = MACHINES_PUMP.codec("PumpMaxInput", Codec.INT, 128).comment("Pump Max Input (Energy per tick)");
	public static final ConfigValue<Integer> pumpMaxEnergy = MACHINES_PUMP.codec("PumpMaxEnergy", Codec.INT, 40_000).comment("Pump Max Energy");
	public static final ConfigValue<Integer> pumpEnergyToCollect = MACHINES_PUMP.codec("PumpEnergyToCollect", Codec.INT, 1_000).comment("Base amount of Energy to collect a block of fluid");
	public static final ConfigValue<Boolean> pumpIterateOutwards = MACHINES_PUMP.codec("PumpIterateOutwards", Codec.BOOL, false).comment("If true then the pump will collect closest fluid and scan outwards");

	private static final ConfigGroup MISC_GENERAL = MISC.group("general");
	public static final ConfigValue<Boolean> IC2TransformersStyle = MISC_GENERAL.codec("IC2TransformersStyle", Codec.BOOL, true).comment("Input from dots side, output from other sides, like in IC2.");
	public static final ConfigValue<Float> machineSoundVolume = MISC_GENERAL.codec("MachineSoundVolume", CLAMPED_FLOAT_CODEC, 1.0F).comment("Machines crafting sound volume (0 - disabled, 1 - max)");
	public static final ConfigValue<Boolean> allowManualRefund = MISC_GENERAL.codec("manualRefund", Codec.BOOL, true).comment("Allow refunding items used to craft the manual");
	public static final ConfigValue<Boolean> vanillaUnlockRecipes = MISC_GENERAL.codec("vanillaUnlockRecipes", Codec.BOOL, true).comment("Enable recipe unlocks only with vanilla mechanic, instead of getting all of them at once");
	public static final ConfigValue<Boolean> dispenseScrapboxes = MISC_GENERAL.codec("DispenserScrapbox", Codec.BOOL, true).comment("Dispensers will open scrapboxes");

	private static final ConfigGroup MISC_NUKE = MISC.group("nuke");
	public static final ConfigValue<Integer> nukeFuseTime = MISC_NUKE.codec("fusetime", Codec.INT, 400).comment("Nuke fuse time (ticks)");
	public static final ConfigValue<Integer> nukeRadius = MISC_NUKE.codec("radius", Codec.INT, 40).comment("Nuke explosion radius");
	public static final ConfigValue<Boolean> nukeEnabled = MISC_NUKE.codec("enabled", Codec.BOOL, true).comment("Should the nuke explode, set to false to prevent block damage");

	private static final ConfigGroup MISC_RESIN_BASIN = MISC.group("resin_basin");
	public static final ConfigValue<Integer> sapTimeTicks = MISC_RESIN_BASIN.codec("saptime", Codec.INT, 80).comment("How long it takes to harvest one sap (ticks)");
	public static final ConfigValue<Integer> checkForSapTime = MISC_RESIN_BASIN.codec("SapCheckTime", Codec.INT, 50).comment("How often to check for sap (will check if world time % this number is zero)");

	private static final ConfigGroup MISC_CABLE = MISC.group("cable");
	public static final ConfigValue<Boolean> uninsulatedElectrocutionDamage = MISC_CABLE.codec("uninsulatedElectrocutionDamage", Codec.BOOL, true).comment("When true an uninsulated cable will cause damage to entities");
	public static final ConfigValue<Boolean> uninsulatedElectrocutionSound = MISC_CABLE.codec("uninsulatedElectrocutionSound", Codec.BOOL, true).comment("When true an uninsulated cable will create a spark sound when an entity touches it");
	public static final ConfigValue<Boolean> uninsulatedElectrocutionParticles = MISC_CABLE.codec("uninsulatedElectrocutionParticles", Codec.BOOL, true).comment("When true an uninsulated cable will create a spark when an entity touches it");

	private static final ConfigGroup WORLD_LOOT = WORLD.group("loot");
	public static final ConfigValue<Boolean> enableOverworldLoot = WORLD_LOOT.codec("enableOverworldLoot", Codec.BOOL, true).comment("When true TechReborn will add ingots, machine frames and circuits to OverWorld loot chests.");
	public static final ConfigValue<Boolean> enableNetherLoot = WORLD_LOOT.codec("enableNetherLoot", Codec.BOOL, true).comment("When true TechReborn will add ingots, machine frames and circuits to Nether loot chests.");
	public static final ConfigValue<Boolean> enableEndLoot = WORLD_LOOT.codec("enableEndLoot", Codec.BOOL, true).comment("When true TechReborn will add ingots, machine frames and circuits to The End loot chests.");
	public static final ConfigValue<Boolean> enableFishingJunkLoot = WORLD_LOOT.codec("enableFishingJunkLoot", Codec.BOOL, true).comment("When true TechReborn will add items to fishing junk loot.");

	private static final ConfigGroup WORLD_GENERATION = WORLD.group("generation");
	public static final ConfigValue<Boolean> enableOreGeneration = WORLD_GENERATION.codec("enableOreGeneration", Codec.BOOL, true).comment("When enabled ores will generate in the world");
	public static final ConfigValue<Boolean> enableBauxiteOreGeneration = WORLD_GENERATION.codec("enableBauxiteOreGeneration", Codec.BOOL, true).comment("When enabled and enableOreGeneration isn't disabled, bauxite ores will generate in the world");
	public static final ConfigValue<Boolean> enableCinnabarOreGeneration = WORLD_GENERATION.codec("enableCinnabarOreGeneration", Codec.BOOL, true).comment("When enabled and enableOreGeneration isn't disabled, cinnabar ores will generate in the world");
	public static final ConfigValue<Boolean> enableGalenaOreGeneration = WORLD_GENERATION.codec("enableGalenaOreGeneration", Codec.BOOL, true).comment("When enabled and enableOreGeneration isn't disabled, galena ores will generate in the world");
	public static final ConfigValue<Boolean> enableIridiumOreGeneration = WORLD_GENERATION.codec("enableIridiumOreGeneration", Codec.BOOL, true).comment("When enabled and enableOreGeneration isn't disabled, iridium ores will generate in the world");
	public static final ConfigValue<Boolean> enableLeadOreGeneration = WORLD_GENERATION.codec("enableLeadOreGeneration", Codec.BOOL, true).comment("When enabled and enableOreGeneration isn't disabled, lead ores will generate in the world");
	public static final ConfigValue<Boolean> enablePeridotOreGeneration = WORLD_GENERATION.codec("enablePeridotOreGeneration", Codec.BOOL, true).comment("When enabled and enableOreGeneration isn't disabled, peridot ores will generate in the world");
	public static final ConfigValue<Boolean> enablePyriteOreGeneration = WORLD_GENERATION.codec("enablePyriteOreGeneration", Codec.BOOL, true).comment("When enabled and enableOreGeneration isn't disabled, pyrite ores will generate in the world");
	public static final ConfigValue<Boolean> enableRubyOreGeneration = WORLD_GENERATION.codec("enableRubyOreGeneration", Codec.BOOL, true).comment("When enabled and enableOreGeneration isn't disabled, ruby ores will generate in the world");
	public static final ConfigValue<Boolean> enableSapphireOreGeneration = WORLD_GENERATION.codec("enableSapphireOreGeneration", Codec.BOOL, true).comment("When enabled and enableOreGeneration isn't disabled, sapphire ores will generate in the world");
	public static final ConfigValue<Boolean> enableSheldoniteOreGeneration = WORLD_GENERATION.codec("enableSheldoniteOreGeneration", Codec.BOOL, true).comment("When enabled and enableOreGeneration isn't disabled, sheldonite ores will generate in the world");
	public static final ConfigValue<Boolean> enableSilverOreGeneration = WORLD_GENERATION.codec("enableSilverOreGeneration", Codec.BOOL, true).comment("When enabled and enableOreGeneration isn't disabled, silver ores will generate in the world");
	public static final ConfigValue<Boolean> enableSodaliteOreGeneration = WORLD_GENERATION.codec("enableSodaliteOreGeneration", Codec.BOOL, true).comment("When enabled and enableOreGeneration isn't disabled, sodalite ores will generate in the world");
	public static final ConfigValue<Boolean> enableSphaleriteOreGeneration = WORLD_GENERATION.codec("enableSphaleriteOreGeneration", Codec.BOOL, true).comment("When enabled and enableOreGeneration isn't disabled, sphalerite ores will generate in the world");
	public static final ConfigValue<Boolean> enableTinOreGeneration = WORLD_GENERATION.codec("enableTinOreGeneration", Codec.BOOL, true).comment("When enabled and enableOreGeneration isn't disabled, tin ores will generate in the world");
	public static final ConfigValue<Boolean> enableTungstenOreGeneration = WORLD_GENERATION.codec("enableTungstenOreGeneration", Codec.BOOL, true).comment("When enabled and enableOreGeneration isn't disabled, tungsten ores will generate in the world");
	public static final ConfigValue<Boolean> enableUraniumOreGeneration = WORLD_GENERATION.codec("enableUraniumOreGeneration", Codec.BOOL, true).comment("When enabled and enableOreGeneration isn't disabled, uranium ores will generate in the world");
	public static final ConfigValue<Boolean> enableRubberTreeGeneration = WORLD_GENERATION.codec("enableRubberTreeGeneration", Codec.BOOL, true).comment("When enabled rubber trees will generate in the world");
	public static final ConfigValue<Boolean> enableOilLakeGeneration = WORLD_GENERATION.codec("enableOilLakeGeneration", Codec.BOOL, true).comment("When enabled oil lakes will generate in the world");
	public static final ConfigValue<Boolean> enableMetallurgistGeneration = WORLD_GENERATION.codec("enableMetallurgistGeneration", Codec.BOOL, true).comment("When enabled metallurgist houses can generate in villages");
	public static final ConfigValue<Boolean> enableElectricianGeneration = WORLD_GENERATION.codec("enableElectricianGeneration", Codec.BOOL, true).comment("When enabled electrician houses can generate in villages");

	private TechRebornConfig() {
	}

	public static void init() {
		LegacyConfigCleanup.delete(GENERATORS_ID, ITEMS_ID, MACHINES_ID, MISC_ID, WORLD_ID);
		RebornCoreConfigApi.register(GENERATORS);
		RebornCoreConfigApi.register(ITEMS);
		RebornCoreConfigApi.register(MACHINES);
		RebornCoreConfigApi.register(MISC);
		RebornCoreConfigApi.register(WORLD);
	}
}
