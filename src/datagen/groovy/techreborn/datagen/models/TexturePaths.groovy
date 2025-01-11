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

package techreborn.datagen.models

import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.client.data.TextureMap
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import techreborn.TechReborn
import techreborn.init.ModFluids
import techreborn.init.TRContent
import techreborn.init.TRContent.FamilyBlockInfo
import techreborn.init.TRContent.MachineBlockInfo
import techreborn.init.TRContent.BlockInfo
import techreborn.init.TRContent.ItemInfo
import techreborn.init.TRContent.Ores
import techreborn.init.TRContent.StorageBlocks
import techreborn.init.TRContent.Dusts
import techreborn.init.TRContent.SmallDusts
import techreborn.init.TRContent.Ingots
import techreborn.init.TRContent.Plates
import techreborn.init.TRContent.Nuggets
import techreborn.init.TRContent.Machine
import techreborn.init.TRContent.SolarPanels
import techreborn.init.TRContent.MachineBlocks
import techreborn.init.TRContent.StorageUnit
import techreborn.init.TRContent.TankUnit
import techreborn.init.TRContent.Parts
import techreborn.init.TRContent.RawMetals
import techreborn.init.TRContent.Gems
import techreborn.init.TRContent.Upgrades
import techreborn.init.TRContent.Cables

import java.util.function.Consumer
import java.util.function.Function

class TexturePaths {
	public static Map<Block, Identifier> blockPaths = [:]
	public static Map<Item, Identifier> itemPaths = [:]
	public static Map<Object, Identifier> aliasPaths = [:]

	static <T> void ifPresent(Map<T, Identifier> map, T target, Consumer<Identifier> callback) {
		def id = map.get(target)
		if (id) callback.accept(id)
	}

	static <T> void ifPresent(Map<T, Identifier> map, T target, String suffix, Consumer<Identifier> callback) {
		def id = map.get(target)
		if (id) callback.accept(id.withSuffixedPath(suffix))
	}

	static void ifPresent(Item item, Consumer<Identifier> callback) {
		ifPresent(itemPaths, item, callback)
	}

	static void ifPresent(Item item, String suffix, Consumer<Identifier> callback) {
		ifPresent(itemPaths, item, suffix, callback)
	}

	static void ifPresent(Block block, Consumer<Identifier> callback) {
		ifPresent(blockPaths, block, callback)
	}

	static void ifPresentOrAlias(Block block, Consumer<Identifier> callback) {
		def id = Optional.ofNullable(aliasPaths.get(block)).orElseGet(() -> blockPaths.get(block))
		if (id) callback.accept(id)
	}

	static void ifPresent(Block block, String suffix, Consumer<Identifier> callback) {
		ifPresent(blockPaths, block, suffix, callback)
	}

	static void add(Block block, String path) {
		blockPaths.put(block, Identifier.of(TechReborn.MOD_ID, "block/" + path))
		itemPaths.put(block.asItem(), Identifier.of(TechReborn.MOD_ID, "item/" + path))
	}

	static void add(Item item, String path) {
		itemPaths.put(item, Identifier.of(TechReborn.MOD_ID, "item/" + path))
	}

	static void add(def info, String path) {
		if (info instanceof FamilyBlockInfo) {
			add(info.block, path)
			add(info.slabBlock, path + "_slab")
			add(info.stairsBlock, path + "_stairs")
			add(info.wallBlock, path + "_wall")
		} else if (info instanceof BlockInfo) {
			add(info.block, path)
		} else if (info instanceof MachineBlockInfo) {
			add(info.frame, path + "_frame")
			add(info.casing, path + "_casing")
		} else if (info instanceof ItemInfo) {
			add(info.asItem(), path)
		}
	}

	static <T extends Enum<T>> void add(T type, Function<T, String> with) {
		add(type, with.apply(type))
	}

	static <T extends Enum<T>> void add(Class<T> type, Function<T, String> with) {
		for (final T value in type.enumConstants) {
			add(value, with.apply(value))
		}
	}

	static <T> void add(List<T> type, Function<T, String> with) {
		for (final T value in type) {
			add(value, with.apply(value))
		}
	}

	static void alias(Block block, String path) {
		aliasPaths.put(block, Identifier.of(TechReborn.MOD_ID, "block/" + path))
	}

	static void alias(Block block, Identifier id) {
		aliasPaths.put(block, id)
	}

	static void alias(BlockInfo info, String path) {
		alias(info.block, path)
	}

	static {
		add Ores, { "ore/${ it.isDeepslate() ? "deepslate/" : "" }${it.name}_ore" }
		add StorageBlocks, {"storage/${it.name}_storage_block" }
		add Dusts, { "dust/${it.name}_dust" }
		add SmallDusts, { "smalldust/${it.name}_small_dust" }
		add Ingots, { "ingot/${it.name}_ingot" }
		add Plates, { "plate/${it.name}_plate" }
		add Nuggets, { "nugget/${it.name}_nugget" }
		add Parts, { "part/$it.name" }
		add RawMetals, { "rawmetal/raw_$it.name" }
		add Gems, { "gem/${it.name}_gem" }
		add Upgrades, { "upgrade/${it.name}_upgrade" }
		add SolarPanels, { "machines/generators/${it.name}_solar_panel" }
		add MachineBlocks, { "machines/structure/${it.name}_machine" }
		add ModFluids, { "fluids/${it.name}" }
		add Cables, { "cables/${it.name}_cable" }
		add List.of(
			Machine.ADJUSTABLE_SU,
			Machine.EV_TRANSFORMER,
			Machine.HIGH_VOLTAGE_SU,
			Machine.HV_TRANSFORMER,
			Machine.INTERDIMENSIONAL_SU,
			Machine.LAPOTRONIC_SU,
			Machine.LOW_VOLTAGE_SU,
			Machine.LSU_STORAGE,
			Machine.LV_TRANSFORMER,
			Machine.MEDIUM_VOLTAGE_SU,
			Machine.MV_TRANSFORMER,
		), { "machines/energy/$it.name" }
		alias Machine.LSU_STORAGE, "machines/energy/" + Machine.LAPOTRONIC_SU.name + "_side"
		add List.of(
			Machine.DIESEL_GENERATOR,
			Machine.DRAGON_EGG_SYPHON,
			Machine.FUSION_CONTROL_COMPUTER,
			Machine.GAS_TURBINE,
			Machine.LIGHTNING_ROD,
			Machine.PLASMA_GENERATOR,
			Machine.SEMI_FLUID_GENERATOR,
			Machine.SOLID_FUEL_GENERATOR,
			Machine.THERMAL_GENERATOR,
			Machine.WATER_MILL,
			Machine.WIND_MILL,
		), { "machines/generators/$it.name" }
		add List.of(
			Machine.ALARM,
			Machine.LAMP_INCANDESCENT,
			Machine.LAMP_LED,
		), { "machines/lighting/$it.name" }
		add Machine.FUSION_COIL, { "machines/structure/$it.name" }
		add List.of(
			Machine.BLOCK_BREAKER,
			Machine.BLOCK_PLACER,
			Machine.DRAIN,
			Machine.IRON_ALLOY_FURNACE,
			Machine.IRON_FURNACE,
		), { "machines/tier0_machines/$it.name" }
		add List.of(
			Machine.ALLOY_SMELTER,
			Machine.ASSEMBLY_MACHINE,
			Machine.AUTO_CRAFTING_TABLE,
			Machine.CHEMICAL_REACTOR,
			Machine.COMPRESSOR,
			Machine.ELECTRIC_FURNACE,
			Machine.ELEVATOR,
			Machine.EXTRACTOR,
			Machine.GREENHOUSE_CONTROLLER,
			Machine.GRINDER,
			Machine.PLAYER_DETECTOR,
			Machine.RECYCLER,
			Machine.ROLLING_MACHINE,
			Machine.SCRAPBOXINATOR,
			Machine.SOLID_CANNING_MACHINE,
			Machine.WIRE_MILL,
		), { "machines/tier1_machines/$it.name" }
		add List.of(
			Machine.CHARGE_O_MAT,
			Machine.DISTILLATION_TOWER,
			Machine.FISHING_STATION,
			Machine.IMPLOSION_COMPRESSOR,
			Machine.INDUSTRIAL_BLAST_FURNACE,
			Machine.INDUSTRIAL_CENTRIFUGE,
			Machine.INDUSTRIAL_ELECTROLYZER,
			Machine.INDUSTRIAL_GRINDER,
			Machine.INDUSTRIAL_SAWMILL,
			Machine.LAUNCHPAD,
			Machine.PUMP,
			Machine.VACUUM_FREEZER,
		), { "machines/tier2_machines/$it.name" }
		add List.of(
			Machine.CHUNK_LOADER,
			Machine.FLUID_REPLICATOR,
			Machine.MATTER_FABRICATOR,
		), { "machines/tier3_machines/$it.name" }
		alias Machine.LAMP_INCANDESCENT, "machines/lighting/lamp"
		alias Machine.LAMP_LED, "machines/lighting/lamp"
		add List.of(
			StorageUnit.BASIC,
			StorageUnit.CRUDE,
		), { "machines/tier0_machines/${it.name}_storage_unit" }
		add StorageUnit.ADVANCED, { "machines/tier1_machines/${it.name}_storage_unit" }
		add StorageUnit.INDUSTRIAL, { "machines/tier2_machines/${it.name}_storage_unit" }
		add List.of(
			StorageUnit.QUANTUM,
			StorageUnit.CREATIVE,
		), { "machines/tier3_machines/${it.name}_storage_unit" }
		add StorageUnit.BUFFER.block, "machines/tier0_machines/storage_buffer"
		add StorageUnit.CRUDE.upgrader.get(), "part/" + StorageUnit.CRUDE.name + "_unit_upgrader"
		add StorageUnit.BASIC.upgrader.get(), "part/" + StorageUnit.BASIC.name + "_unit_upgrader"
		add StorageUnit.ADVANCED.upgrader.get(), "part/" + StorageUnit.ADVANCED.name + "_unit_upgrader"
		add StorageUnit.INDUSTRIAL.upgrader.get(), "part/" + StorageUnit.INDUSTRIAL.name + "_unit_upgrader"
		add TankUnit.BASIC, { "machines/tier0_machines/${it.name}_tank_unit" }
		add TankUnit.ADVANCED, { "machines/tier1_machines/${it.name}_tank_unit" }
		add TankUnit.INDUSTRIAL, { "machines/tier2_machines/${it.name}_tank_unit" }
		add List.of(
			TankUnit.QUANTUM,
			TankUnit.CREATIVE,
		), { "machines/tier3_machines/${it.name}_tank_unit" }
		add Machine.RESIN_BASIN, { "machines/tier0_machines/resin_basin/$it.name" }
		add TRContent.COMPUTER_CUBE, "machines/tier2_machines/computer_cube"
		add TRContent.REFINED_IRON_FENCE, "misc/refined_iron_fence"
		alias TRContent.REFINED_IRON_FENCE, TextureMap.getId(Blocks.IRON_BLOCK)
		add TRContent.COPPER_WALL, "misc/copper_wall"
		alias TRContent.COPPER_WALL, "storage/copper_storage_block"
		add TRContent.NUKE, "misc/nuke"
		add TRContent.REINFORCED_GLASS, "misc/reinforced_glass"
		add TRContent.SCRAP_BOX, "misc/scrapbox"
		add TRContent.MANUAL, "misc/manual"
		add TRContent.RUBBER_SAPLING, "misc/rubber_sapling"
		add TRContent.POTTED_RUBBER_SAPLING, "misc/potted_rubber_sapling"
		add TRContent.RUBBER_LOG, "misc/rubber_log"
		add TRContent.RUBBER_WOOD, "misc/rubber_wood"
		add TRContent.RUBBER_LOG_STRIPPED, "misc/rubber_log_stripped"
		add TRContent.STRIPPED_RUBBER_WOOD, "misc/stripped_rubber_wood"
		add TRContent.RUBBER_LEAVES, "misc/rubber_leaves"
		add TRContent.RUBBER_PLANKS, "misc/rubber_planks"
		add TRContent.RUBBER_BUTTON, "misc/rubber_button"
		add TRContent.RUBBER_FENCE, "misc/rubber_fence"
		add TRContent.RUBBER_FENCE_GATE, "misc/rubber_fence_gate"
		add TRContent.RUBBER_PRESSURE_PLATE, "misc/rubber_pressure_plate"
		add TRContent.RUBBER_SLAB, "misc/rubber_slab"
		add TRContent.RUBBER_STAIR, "misc/rubber_stair"
		add TRContent.RUBBER_DOOR, "misc/rubber_door"
		add TRContent.RUBBER_TRAPDOOR, "misc/rubber_trapdoor"
		add TRContent.TREE_TAP, "tool/treetap"
		add TRContent.FREQUENCY_TRANSMITTER, "tool/frequency_transmitter"
		add TRContent.WRENCH, "tool/wrench"
		add TRContent.PAINTING_TOOL, "tool/painting_tool"
		add TRContent.ELECTRIC_TREE_TAP, "tool/electric_treetap"
		add TRContent.BRONZE_SWORD, "tool/bronze_sword"
		add TRContent.BRONZE_PICKAXE, "tool/bronze_pickaxe"
		add TRContent.BRONZE_AXE, "tool/bronze_axe"
		add TRContent.BRONZE_HOE, "tool/bronze_hoe"
		add TRContent.BRONZE_SPADE, "tool/bronze_spade"
		add TRContent.RUBY_SWORD, "tool/ruby_sword"
		add TRContent.RUBY_PICKAXE, "tool/ruby_pickaxe"
		add TRContent.RUBY_AXE, "tool/ruby_axe"
		add TRContent.RUBY_HOE, "tool/ruby_hoe"
		add TRContent.RUBY_SPADE, "tool/ruby_spade"
		add TRContent.SAPPHIRE_SWORD, "tool/sapphire_sword"
		add TRContent.SAPPHIRE_PICKAXE, "tool/sapphire_pickaxe"
		add TRContent.SAPPHIRE_AXE, "tool/sapphire_axe"
		add TRContent.SAPPHIRE_HOE, "tool/sapphire_hoe"
		add TRContent.SAPPHIRE_SPADE, "tool/sapphire_spade"
		add TRContent.PERIDOT_SWORD, "tool/peridot_sword"
		add TRContent.PERIDOT_PICKAXE, "tool/peridot_pickaxe"
		add TRContent.PERIDOT_AXE, "tool/peridot_axe"
		add TRContent.PERIDOT_HOE, "tool/peridot_hoe"
		add TRContent.PERIDOT_SPADE, "tool/peridot_spade"
		add TRContent.BASIC_CHAINSAW, "tool/basic_chainsaw"
		add TRContent.BASIC_DRILL, "tool/basic_drill"
		add TRContent.BASIC_JACKHAMMER, "tool/basic_jackhammer"
		add TRContent.ADVANCED_CHAINSAW, "tool/advanced_chainsaw"
		add TRContent.ADVANCED_DRILL, "tool/advanced_drill"
		add TRContent.ADVANCED_JACKHAMMER, "tool/advanced_jackhammer"
		add TRContent.INDUSTRIAL_CHAINSAW, "tool/industrial_chainsaw"
		add TRContent.INDUSTRIAL_DRILL, "tool/industrial_drill"
		add TRContent.INDUSTRIAL_JACKHAMMER, "tool/industrial_jackhammer"
		add TRContent.ROCK_CUTTER, "tool/rock_cutter"
		add TRContent.OMNI_TOOL, "tool/omni_tool"
		add TRContent.GPS, "tool/gps"
		add TRContent.DEBUG_TOOL, "tool/debug_tool"
		add TRContent.NANOSABER, "tool/nanosaber"
		add TRContent.BRONZE_BOOTS, "armor/bronze_boots"
		add TRContent.BRONZE_HELMET, "armor/bronze_helmet"
		add TRContent.BRONZE_CHESTPLATE, "armor/bronze_chestplate"
		add TRContent.BRONZE_LEGGINGS, "armor/bronze_leggings"
		add TRContent.RUBY_BOOTS, "armor/ruby_boots"
		add TRContent.RUBY_HELMET, "armor/ruby_helmet"
		add TRContent.RUBY_CHESTPLATE, "armor/ruby_chestplate"
		add TRContent.RUBY_LEGGINGS, "armor/ruby_leggings"
		add TRContent.SAPPHIRE_BOOTS, "armor/sapphire_boots"
		add TRContent.SAPPHIRE_HELMET, "armor/sapphire_helmet"
		add TRContent.SAPPHIRE_CHESTPLATE, "armor/sapphire_chestplate"
		add TRContent.SAPPHIRE_LEGGINGS, "armor/sapphire_leggings"
		add TRContent.PERIDOT_BOOTS, "armor/peridot_boots"
		add TRContent.PERIDOT_HELMET, "armor/peridot_helmet"
		add TRContent.PERIDOT_CHESTPLATE, "armor/peridot_chestplate"
		add TRContent.PERIDOT_LEGGINGS, "armor/peridot_leggings"
		add TRContent.SILVER_BOOTS, "armor/silver_boots"
		add TRContent.SILVER_HELMET, "armor/silver_helmet"
		add TRContent.SILVER_CHESTPLATE, "armor/silver_chestplate"
		add TRContent.SILVER_LEGGINGS, "armor/silver_leggings"
		add TRContent.STEEL_BOOTS, "armor/steel_boots"
		add TRContent.STEEL_HELMET, "armor/steel_helmet"
		add TRContent.STEEL_CHESTPLATE, "armor/steel_chestplate"
		add TRContent.STEEL_LEGGINGS, "armor/steel_leggings"
		add TRContent.QUANTUM_BOOTS, "armor/quantum_boots"
		add TRContent.QUANTUM_HELMET, "armor/quantum_helmet"
		add TRContent.QUANTUM_CHESTPLATE, "armor/quantum_chestplate"
		add TRContent.QUANTUM_LEGGINGS, "armor/quantum_leggings"
		add TRContent.NANO_BOOTS, "armor/nano_boots"
		add TRContent.NANO_HELMET, "armor/nano_helmet"
		add TRContent.NANO_CHESTPLATE, "armor/nano_chestplate"
		add TRContent.NANO_LEGGINGS, "armor/nano_leggings"
		add TRContent.CLOAKING_DEVICE, "armor/cloaking_device"
		add TRContent.LAPOTRONIC_ORBPACK, "armor/lapotronic_orbpack"
		add TRContent.LITHIUM_ION_BATPACK, "armor/lithium_batpack"
		add TRContent.RED_CELL_BATTERY, "battery/red_cell_battery"
		add TRContent.LITHIUM_ION_BATTERY, "battery/lithium_battery"
		add TRContent.ENERGY_CRYSTAL, "battery/energy_crystal"
		add TRContent.LAPOTRON_CRYSTAL, "battery/lapotron_crystal"
		add TRContent.LAPOTRONIC_ORB, "battery/lapotronic_orb"
	}

	public static Identifier generatorDir = Identifier.of(TechReborn.MOD_ID, "block/machines/generators/")
	public static Identifier machineTier0Dir = Identifier.of(TechReborn.MOD_ID, "block/machines/tier0_machines/")
	public static Identifier machineTier1Dir = Identifier.of(TechReborn.MOD_ID, "block/machines/tier1_machines/")
	public static Identifier machineTier2Dir = Identifier.of(TechReborn.MOD_ID, "block/machines/tier2_machines/")
	public static Identifier machineTier3Dir = Identifier.of(TechReborn.MOD_ID, "block/machines/tier3_machines/")
	public static Identifier machineTier1Bottom = machineTier1Dir.withSuffixedPath("machine_bottom")
	public static Identifier machineTier1Side = machineTier1Dir.withSuffixedPath("machine_side")
	public static Identifier machineTier2Top = machineTier2Dir.withSuffixedPath("machine_top")
	public static Identifier machineTier2Bottom = machineTier2Dir.withSuffixedPath("machine_bottom")
	public static Identifier machineTier2West = machineTier2Dir.withSuffixedPath("machine_west")
	public static Identifier machineTier2East = machineTier2Dir.withSuffixedPath("machine_east")
	public static Identifier machineTier2Back = machineTier2Dir.withSuffixedPath("machine_back")
	public static Identifier machineTier3Top = machineTier3Dir.withSuffixedPath("machine_top")
	public static Identifier machineTier3Bottom = machineTier3Dir.withSuffixedPath("machine_bottom")
	public static Identifier machineTier3Back = machineTier3Dir.withSuffixedPath("machine_back")
	public static Identifier generatorBottom = generatorDir.withSuffixedPath("generator_bottom")
	public static Identifier generatorSide = generatorDir.withSuffixedPath("generator_side")
	public static Identifier generatorTop = generatorDir.withSuffixedPath("generator_top")
	public static Identifier quantumTop = machineTier3Dir.withSuffixedPath("quantum_top")
	public static Identifier quantumBottom = machineTier3Dir.withSuffixedPath("quantum_bottom")
	public static Identifier quantumSolarPanelSide = generatorDir.withSuffixedPath("quantum_solar_panel_side")
	public static Identifier solarPanelSide = generatorDir.withSuffixedPath("solar_panel_side")
	public static Identifier basicUnitBottom = machineTier0Dir.withSuffixedPath("basic_unit_bottom")
	public static Identifier advancedUnitBottom = machineTier1Dir.withSuffixedPath("advanced_unit_bottom")
	public static Identifier industrialUnitBottom = machineTier2Dir.withSuffixedPath("storage_bottom")
}
