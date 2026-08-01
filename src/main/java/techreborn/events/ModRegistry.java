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

package techreborn.events;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.WallBlock;
import reborncore.RebornRegistry;
import reborncore.common.powerSystem.RcEnergyTier;
import team.reborn.energy.api.EnergyStorage;
import techreborn.TechReborn;
import techreborn.blockentity.cable.CableBlockEntity;
import techreborn.blockentity.generator.nuclear.ReactorChamberBlockEntity;
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;
import techreborn.blocks.misc.*;
import techreborn.config.TechRebornConfig;
import techreborn.init.*;
import techreborn.init.TRContent.*;
import techreborn.items.*;
import techreborn.items.armor.*;
import techreborn.items.tool.*;
import techreborn.items.tool.advanced.AdvancedJackhammerItem;
import techreborn.items.tool.basic.ElectricTreetapItem;
import techreborn.items.tool.basic.RockCutterItem;
import techreborn.items.tool.industrial.*;
import techreborn.utils.InitUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author drcrazy
 */

public class ModRegistry {

	public static void register() {
		registerBlocks();
		registerItems();
		registerFluids();
		registerSounds();
		registerApis();
		/* TODO: Villager trading
		TRVillager.registerVillagerTrades();
		TRVillager.registerWanderingTraderTrades();
		TRVillager.registerVillagerHouses();
		*/
	}

	private static void registerBlocks() {
		Arrays.stream(Ores.values()).forEach(value -> RebornRegistry.registerBlock(value.block, settings(value.block)));
		StorageBlocks.blockStream().forEach(block -> RebornRegistry.registerBlock(block, settings(block)));
		Arrays.stream(MachineBlocks.values()).forEach(value -> {
			RebornRegistry.registerBlock(value.frame, settings(value.frame));
			RebornRegistry.registerBlock(value.casing, settings(value.casing));
		});
		Arrays.stream(SolarPanels.values()).forEach(value -> RebornRegistry.registerBlock(value.block, settings(value.block)));
		Arrays.stream(StorageUnit.values()).forEach(value -> RebornRegistry.registerBlock(value.block, settings(value.block)));
		Arrays.stream(StorageUnit.values()).map(StorageUnit::getUpgrader).filter(Optional::isPresent).forEach(value -> RebornRegistry.registerItem(value.get()));
		Arrays.stream(TankUnit.values()).forEach(value -> RebornRegistry.registerBlock(value.block, settings(value.block)));
		Arrays.stream(Cables.values()).forEach(value -> RebornRegistry.registerBlock(value.block, settings(value.block)));
		Arrays.stream(Machine.values()).forEach(value -> RebornRegistry.registerBlock(value.block, settings(value.block)));

		// Misc. blocks
		RebornRegistry.registerBlock(TRContent.COMPUTER_CUBE = InitUtils.setup(new BlockComputerCube("computer_cube"), "computer_cube"), settings("computer_cube"));
		RebornRegistry.registerBlock(TRContent.NUKE = InitUtils.setup(new BlockNuke("nuke"), "nuke"), settings("nuke"));
		RebornRegistry.registerBlock(TRContent.REFINED_IRON_FENCE = InitUtils.setup(new BlockRefinedIronFence("refined_iron_fence"), "refined_iron_fence"), settings("refined_iron_fence"));
		RebornRegistry.registerBlock(TRContent.REINFORCED_GLASS = InitUtils.setup(new BlockReinforcedGlass("reinforced_glass"), "reinforced_glass"), settings("reinforced_glass"));
		RebornRegistry.registerBlock(TRContent.RUBBER_LEAVES = InitUtils.setup(new BlockRubberLeaves("rubber_leaves"), "rubber_leaves"), settings("rubber_leaves"));
		RebornRegistry.registerBlock(TRContent.RUBBER_LOG = InitUtils.setup(new BlockRubberLog("rubber_log"), "rubber_log"), settings("rubber_log"));
		RebornRegistry.registerBlock(TRContent.RUBBER_LOG_STRIPPED = InitUtils.setup(new RotatedPillarBlock(TRBlockSettings.rubberLogStripped("rubber_log_stripped")), "rubber_log_stripped"), settings("rubber_log_stripped"));
		RebornRegistry.registerBlock(TRContent.RUBBER_WOOD = InitUtils.setup(new RotatedPillarBlock(TRBlockSettings.rubberWoodStripped("rubber_wood")), "rubber_wood"), settings("rubber_wood"));
		RebornRegistry.registerBlock(TRContent.STRIPPED_RUBBER_WOOD = InitUtils.setup(new RotatedPillarBlock(TRBlockSettings.rubberWoodStripped("stripped_rubber_wood")), "stripped_rubber_wood"), settings("stripped_rubber_wood"));
		RebornRegistry.registerBlock(TRContent.RUBBER_PLANKS = InitUtils.setup(new BlockRubberPlank("rubber_planks"), "rubber_planks"), settings("rubber_planks"));
		RebornRegistry.registerBlock(TRContent.RUBBER_SAPLING = InitUtils.setup(new BlockRubberSapling("rubber_sapling"), "rubber_sapling"), settings("rubber_sapling"));
		RebornRegistry.registerBlock(TRContent.RUBBER_SLAB = InitUtils.setup(new SlabBlock(TRBlockSettings.rubberSlab("rubber_slab")), "rubber_slab"), settings("rubber_slab"));
		RebornRegistry.registerBlock(TRContent.RUBBER_FENCE = InitUtils.setup(new FenceBlock(TRBlockSettings.rubberFence("rubber_fence")), "rubber_fence"), settings("rubber_fence"));
		RebornRegistry.registerBlock(TRContent.RUBBER_FENCE_GATE = InitUtils.setup(new FenceGateBlock(TRContent.RUBBER_WOOD_TYPE, TRBlockSettings.rubberFenceGate("rubber_fence_gate")), "rubber_fence_gate"), settings("rubber_fence_gate"));
		RebornRegistry.registerBlock(TRContent.RUBBER_STAIR = InitUtils.setup(new BlockRubberPlankStair("rubber_stair"), "rubber_stair"), settings("rubber_stair"));
		RebornRegistry.registerBlock(TRContent.RUBBER_TRAPDOOR = InitUtils.setup(new RubberTrapdoorBlock("rubber_trapdoor"), "rubber_trapdoor"), settings("rubber_trapdoor"));
		RebornRegistry.registerBlock(TRContent.RUBBER_BUTTON = InitUtils.setup(new RubberButtonBlock("rubber_button"), "rubber_button"), settings("rubber_button"));
		RebornRegistry.registerBlock(TRContent.RUBBER_PRESSURE_PLATE = InitUtils.setup(new RubberPressurePlateBlock("rubber_pressure_plate"), "rubber_pressure_plate"), settings("rubber_pressure_plate"));
		RebornRegistry.registerBlock(TRContent.RUBBER_DOOR = InitUtils.setup(new RubberDoorBlock("rubber_door"), "rubber_door"), settings("rubber_door"));
		RebornRegistry.registerBlockNoItem(TRContent.POTTED_RUBBER_SAPLING = InitUtils.setup(new FlowerPotBlock(TRContent.RUBBER_SAPLING, TRBlockSettings.pottedRubberSapling("potted_rubber_sapling")), "potted_rubber_sapling"));
		RebornRegistry.registerBlock(TRContent.COPPER_WALL = InitUtils.setup(new WallBlock(TRBlockSettings.copperWall("copper_wall")), "copper_wall"), settings("copper_wall"));

		TechReborn.LOGGER.debug("TechReborn's Blocks Loaded");
	}

	private static Item.Properties settings(Block block) {
		return settings(block.properties().id.identifier().getPath());
	}

	private static Item.Properties settings(String name) {
		return TRItemSettings.item(name)
			.setId(ResourceKey.create(BuiltInRegistries.ITEM.key(), Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, name)))
			.overrideDescription("block.techreborn." + name);
	}

	private static void registerItems() {
		Arrays.stream(Ingots.values()).forEach(value -> RebornRegistry.registerItem(value.asItem()));
		Arrays.stream(Nuggets.values()).forEach(value -> RebornRegistry.registerItem(value.asItem()));
		Arrays.stream(Gems.values()).forEach(value -> RebornRegistry.registerItem(value.asItem()));
		Arrays.stream(Dusts.values()).forEach(value -> RebornRegistry.registerItem(value.asItem()));
		Arrays.stream(RawMetals.values()).forEach(value -> RebornRegistry.registerItem(value.asItem()));
		Arrays.stream(SmallDusts.values()).forEach(value -> RebornRegistry.registerItem(value.asItem()));
		Arrays.stream(Plates.values()).forEach(value -> RebornRegistry.registerItem(value.asItem()));
		Arrays.stream(Parts.values()).forEach(value -> RebornRegistry.registerItem(value.asItem()));
		Arrays.stream(Upgrades.values()).forEach(value -> RebornRegistry.registerItem(value.asItem()));
		Arrays.stream(NuclearReactorComponents.values()).forEach(value -> RebornRegistry.registerItem(value.asItem()));
		Arrays.stream(Cells.values()).forEach(value -> {
			RebornRegistry.registerItem(value.asItem());
			value.getCellItem().registerFluidApi();
		});

		RebornRegistry.registerItem(TRContent.QUANTUM_HELMET = InitUtils.setup(new QuantumSuitItem(TRArmorMaterials.QUANTUM, ArmorType.HELMET, "quantum_helmet"), "quantum_helmet"));
		RebornRegistry.registerItem(TRContent.QUANTUM_CHESTPLATE = InitUtils.setup(new QuantumSuitItem(TRArmorMaterials.QUANTUM, ArmorType.CHESTPLATE, "quantum_chestplate"), "quantum_chestplate"));
		RebornRegistry.registerItem(TRContent.QUANTUM_LEGGINGS = InitUtils.setup(new QuantumSuitItem(TRArmorMaterials.QUANTUM, ArmorType.LEGGINGS, "quantum_leggings"), "quantum_leggings"));
		RebornRegistry.registerItem(TRContent.QUANTUM_BOOTS = InitUtils.setup(new QuantumSuitItem(TRArmorMaterials.QUANTUM, ArmorType.BOOTS, "quantum_boots"), "quantum_boots"));

		RebornRegistry.registerItem(TRContent.NANO_HELMET = InitUtils.setup(new NanoSuitItem(TRArmorMaterials.NANO, ArmorType.HELMET, "nano_helmet"), "nano_helmet"));
		RebornRegistry.registerItem(TRContent.NANO_CHESTPLATE = InitUtils.setup(new NanoSuitItem(TRArmorMaterials.NANO, ArmorType.CHESTPLATE, "nano_chestplate"), "nano_chestplate"));
		RebornRegistry.registerItem(TRContent.NANO_LEGGINGS = InitUtils.setup(new NanoSuitItem(TRArmorMaterials.NANO, ArmorType.LEGGINGS, "nano_leggings"), "nano_leggings"));
		RebornRegistry.registerItem(TRContent.NANO_BOOTS = InitUtils.setup(new NanoSuitItem(TRArmorMaterials.NANO, ArmorType.BOOTS, "nano_boots"), "nano_boots"));

		// Gem armor & tools
		// Todo: repair with tags
		RebornRegistry.registerItem(TRContent.BRONZE_SWORD = InitUtils.setup(new Item(TRItemSettings.item("bronze_sword").sword(TRToolTier.BRONZE, 0f, -2f)), "bronze_sword"));
		RebornRegistry.registerItem(TRContent.BRONZE_PICKAXE = InitUtils.setup(new Item(TRItemSettings.item("bronze_pickaxe").pickaxe(TRToolTier.BRONZE, -2f, -2.8f)), "bronze_pickaxe"));
		RebornRegistry.registerItem(TRContent.BRONZE_SPADE = InitUtils.setup(new ShovelItem(TRToolTier.BRONZE, -2f, -3f, TRItemSettings.item("bronze_spade")), "bronze_spade"));
		RebornRegistry.registerItem(TRContent.BRONZE_AXE = InitUtils.setup(new AxeItem(TRToolTier.BRONZE, 3f, -2.9f, TRItemSettings.item("bronze_axe")), "bronze_axe"));
		RebornRegistry.registerItem(TRContent.BRONZE_HOE = InitUtils.setup(new HoeItem(TRToolTier.BRONZE, -4f, 0f, TRItemSettings.item("bronze_hoe")), "bronze_hoe"));

		RebornRegistry.registerItem(TRContent.BRONZE_HELMET = InitUtils.setup(new Item(TRItemSettings.item("bronze_helmet").humanoidArmor(TRArmorMaterials.BRONZE, ArmorType.HELMET)), "bronze_helmet"));
		RebornRegistry.registerItem(TRContent.BRONZE_CHESTPLATE = InitUtils.setup(new Item(TRItemSettings.item("bronze_chestplate").humanoidArmor(TRArmorMaterials.BRONZE, ArmorType.CHESTPLATE)), "bronze_chestplate"));
		RebornRegistry.registerItem(TRContent.BRONZE_LEGGINGS = InitUtils.setup(new Item(TRItemSettings.item("bronze_leggings").humanoidArmor(TRArmorMaterials.BRONZE, ArmorType.LEGGINGS)), "bronze_leggings"));
		RebornRegistry.registerItem(TRContent.BRONZE_BOOTS = InitUtils.setup(new Item(TRItemSettings.item("bronze_boots").humanoidArmor(TRArmorMaterials.BRONZE, ArmorType.BOOTS)), "bronze_boots"));

		RebornRegistry.registerItem(TRContent.RUBY_SWORD = InitUtils.setup(new Item(TRItemSettings.item("ruby_sword").sword(TRToolTier.RUBY, 0f, -2f)), "ruby_sword"));
		RebornRegistry.registerItem(TRContent.RUBY_PICKAXE = InitUtils.setup(new Item(TRItemSettings.item("ruby_pickaxe").pickaxe(TRToolTier.RUBY, -2f, -2.8f)), "ruby_pickaxe"));
		RebornRegistry.registerItem(TRContent.RUBY_SPADE = InitUtils.setup(new ShovelItem(TRToolTier.RUBY, -2f, -3f, TRItemSettings.item("ruby_spade")), "ruby_spade"));
		RebornRegistry.registerItem(TRContent.RUBY_AXE = InitUtils.setup(new AxeItem(TRToolTier.RUBY, 3f, -2.9f, TRItemSettings.item("ruby_axe")), "ruby_axe"));
		RebornRegistry.registerItem(TRContent.RUBY_HOE = InitUtils.setup(new HoeItem(TRToolTier.RUBY, -4f, 0f, TRItemSettings.item("ruby_hoe")), "ruby_hoe"));

		RebornRegistry.registerItem(TRContent.RUBY_HELMET = InitUtils.setup(new Item(TRItemSettings.item("ruby_helmet").stacksTo(1).humanoidArmor(TRArmorMaterials.RUBY, ArmorType.HELMET)), "ruby_helmet"));
		RebornRegistry.registerItem(TRContent.RUBY_CHESTPLATE = InitUtils.setup(new Item(TRItemSettings.item("ruby_chestplate").stacksTo(1).humanoidArmor(TRArmorMaterials.RUBY, ArmorType.CHESTPLATE)), "ruby_chestplate"));
		RebornRegistry.registerItem(TRContent.RUBY_LEGGINGS = InitUtils.setup(new Item(TRItemSettings.item("ruby_leggings").stacksTo(1).humanoidArmor(TRArmorMaterials.RUBY, ArmorType.LEGGINGS)), "ruby_leggings"));
		RebornRegistry.registerItem(TRContent.RUBY_BOOTS = InitUtils.setup(new Item(TRItemSettings.item("ruby_boots").stacksTo(1).humanoidArmor(TRArmorMaterials.RUBY, ArmorType.BOOTS)), "ruby_boots"));

		RebornRegistry.registerItem(TRContent.SAPPHIRE_SWORD = InitUtils.setup(new Item(TRItemSettings.item("sapphire_sword").sword(TRToolTier.SAPPHIRE, 0f, -2f)), "sapphire_sword"));
		RebornRegistry.registerItem(TRContent.SAPPHIRE_PICKAXE = InitUtils.setup(new Item(TRItemSettings.item("sapphire_pickaxe").pickaxe(TRToolTier.SAPPHIRE, -2f, -2.8f)), "sapphire_pickaxe"));
		RebornRegistry.registerItem(TRContent.SAPPHIRE_SPADE = InitUtils.setup(new ShovelItem(TRToolTier.SAPPHIRE, -2f, -3f, TRItemSettings.item("sapphire_spade")), "sapphire_spade"));
		RebornRegistry.registerItem(TRContent.SAPPHIRE_AXE = InitUtils.setup(new AxeItem(TRToolTier.SAPPHIRE, 3f, -2.9f, TRItemSettings.item("sapphire_axe")), "sapphire_axe"));
		RebornRegistry.registerItem(TRContent.SAPPHIRE_HOE = InitUtils.setup(new HoeItem(TRToolTier.SAPPHIRE, -4f, 0f, TRItemSettings.item("sapphire_hoe")), "sapphire_hoe"));

		RebornRegistry.registerItem(TRContent.SAPPHIRE_HELMET = InitUtils.setup(new Item(TRItemSettings.item("sapphire_helmet").stacksTo(1).humanoidArmor(TRArmorMaterials.SAPPHIRE, ArmorType.HELMET)), "sapphire_helmet"));
		RebornRegistry.registerItem(TRContent.SAPPHIRE_CHESTPLATE = InitUtils.setup(new Item(TRItemSettings.item("sapphire_chestplate").stacksTo(1).humanoidArmor(TRArmorMaterials.SAPPHIRE, ArmorType.CHESTPLATE)), "sapphire_chestplate"));
		RebornRegistry.registerItem(TRContent.SAPPHIRE_LEGGINGS = InitUtils.setup(new Item(TRItemSettings.item("sapphire_leggings").stacksTo(1).humanoidArmor(TRArmorMaterials.SAPPHIRE, ArmorType.LEGGINGS)), "sapphire_leggings"));
		RebornRegistry.registerItem(TRContent.SAPPHIRE_BOOTS = InitUtils.setup(new Item(TRItemSettings.item("sapphire_boots").stacksTo(1).humanoidArmor(TRArmorMaterials.SAPPHIRE, ArmorType.BOOTS)), "sapphire_boots"));

		RebornRegistry.registerItem(TRContent.PERIDOT_SWORD = InitUtils.setup(new Item(TRItemSettings.item("peridot_sword").sword(TRToolTier.PERIDOT, 0f, -2f)), "peridot_sword"));
		RebornRegistry.registerItem(TRContent.PERIDOT_PICKAXE = InitUtils.setup(new Item(TRItemSettings.item("peridot_pickaxe").pickaxe(TRToolTier.PERIDOT, -2f, -2.8f)), "peridot_pickaxe"));
		RebornRegistry.registerItem(TRContent.PERIDOT_SPADE = InitUtils.setup(new ShovelItem(TRToolTier.PERIDOT, -2f, -3f, TRItemSettings.item("peridot_spade")), "peridot_spade"));
		RebornRegistry.registerItem(TRContent.PERIDOT_AXE = InitUtils.setup(new AxeItem(TRToolTier.PERIDOT, 3f, -2.9f, TRItemSettings.item("peridot_axe")), "peridot_axe"));
		RebornRegistry.registerItem(TRContent.PERIDOT_HOE = InitUtils.setup(new HoeItem(TRToolTier.PERIDOT, -4f, 0f, TRItemSettings.item("peridot_hoe")), "peridot_hoe"));

		RebornRegistry.registerItem(TRContent.PERIDOT_HELMET = InitUtils.setup(new Item(TRItemSettings.item("peridot_helmet").stacksTo(1).humanoidArmor(TRArmorMaterials.PERIDOT, ArmorType.HELMET)), "peridot_helmet"));
		RebornRegistry.registerItem(TRContent.PERIDOT_CHESTPLATE = InitUtils.setup(new Item(TRItemSettings.item("peridot_chestplate").stacksTo(1).humanoidArmor(TRArmorMaterials.PERIDOT, ArmorType.CHESTPLATE)), "peridot_chestplate"));
		RebornRegistry.registerItem(TRContent.PERIDOT_LEGGINGS = InitUtils.setup(new Item(TRItemSettings.item("peridot_leggings").stacksTo(1).humanoidArmor(TRArmorMaterials.PERIDOT, ArmorType.LEGGINGS)), "peridot_leggings"));
		RebornRegistry.registerItem(TRContent.PERIDOT_BOOTS = InitUtils.setup(new Item(TRItemSettings.item("peridot_boots").stacksTo(1).humanoidArmor(TRArmorMaterials.PERIDOT, ArmorType.BOOTS)), "peridot_boots"));

		RebornRegistry.registerItem(TRContent.SILVER_HELMET = InitUtils.setup(new Item(TRItemSettings.item("silver_helmet").stacksTo(1).humanoidArmor(TRArmorMaterials.SILVER, ArmorType.HELMET)), "silver_helmet"));
		RebornRegistry.registerItem(TRContent.SILVER_CHESTPLATE = InitUtils.setup(new Item(TRItemSettings.item("silver_chestplate").stacksTo(1).humanoidArmor(TRArmorMaterials.SILVER, ArmorType.CHESTPLATE)), "silver_chestplate"));
		RebornRegistry.registerItem(TRContent.SILVER_LEGGINGS = InitUtils.setup(new Item(TRItemSettings.item("silver_leggings").stacksTo(1).humanoidArmor(TRArmorMaterials.SILVER, ArmorType.LEGGINGS)), "silver_leggings"));
		RebornRegistry.registerItem(TRContent.SILVER_BOOTS = InitUtils.setup(new Item(TRItemSettings.item("silver_boots").stacksTo(1).humanoidArmor(TRArmorMaterials.SILVER, ArmorType.BOOTS)), "silver_boots"));

		RebornRegistry.registerItem(TRContent.STEEL_HELMET = InitUtils.setup(new Item(TRItemSettings.item("steel_helmet").stacksTo(1).humanoidArmor(TRArmorMaterials.STEEL, ArmorType.HELMET)), "steel_helmet"));
		RebornRegistry.registerItem(TRContent.STEEL_CHESTPLATE = InitUtils.setup(new Item(TRItemSettings.item("steel_chestplate").stacksTo(1).humanoidArmor(TRArmorMaterials.STEEL, ArmorType.CHESTPLATE)), "steel_chestplate"));
		RebornRegistry.registerItem(TRContent.STEEL_LEGGINGS = InitUtils.setup(new Item(TRItemSettings.item("steel_leggings").stacksTo(1).humanoidArmor(TRArmorMaterials.STEEL, ArmorType.LEGGINGS)), "steel_leggings"));
		RebornRegistry.registerItem(TRContent.STEEL_BOOTS = InitUtils.setup(new Item(TRItemSettings.item("steel_boots").stacksTo(1).humanoidArmor(TRArmorMaterials.STEEL, ArmorType.BOOTS)), "steel_boots"));

		// Battery
		RebornRegistry.registerItem(TRContent.RED_CELL_BATTERY = InitUtils.setup(new BatteryItem(TechRebornConfig.redCellBatteryMaxCharge, RcEnergyTier.LOW, TechRebornConfig.redCellBatteryIORate, "red_cell_battery"), "red_cell_battery"));
		RebornRegistry.registerItem(TRContent.LITHIUM_ION_BATTERY = InitUtils.setup(new BatteryItem(TechRebornConfig.lithiumIonBatteryMaxCharge, RcEnergyTier.MEDIUM, TechRebornConfig.lithiumIonBatteryIORate, "lithium_ion_battery"), "lithium_ion_battery"));
		RebornRegistry.registerItem(TRContent.LITHIUM_ION_BATPACK = InitUtils.setup(new BatpackItem(TechRebornConfig.lithiumBatpackCharge, TRArmorMaterials.LITHIUM_BATPACK, RcEnergyTier.MEDIUM, "lithium_ion_batpack"), "lithium_ion_batpack"));
		RebornRegistry.registerItem(TRContent.ENERGY_CRYSTAL = InitUtils.setup(new BatteryItem(TechRebornConfig.energyCrystalMaxCharge, RcEnergyTier.HIGH, TechRebornConfig.energyCrystalIORate, "energy_crystal"), "energy_crystal"));
		RebornRegistry.registerItem(TRContent.LAPOTRON_CRYSTAL = InitUtils.setup(new BatteryItem(TechRebornConfig.lapotronCrystalMaxCharge, RcEnergyTier.EXTREME, TechRebornConfig.lapotronCrystalIORate, "lapotron_crystal"), "lapotron_crystal"));
		RebornRegistry.registerItem(TRContent.LAPOTRONIC_ORB = InitUtils.setup(new BatteryItem(TechRebornConfig.lapotronicOrbMaxCharge, RcEnergyTier.INSANE, TechRebornConfig.lapotronicOrbIORate, "lapotronic_orb"), "lapotronic_orb"));
		RebornRegistry.registerItem(TRContent.LAPOTRONIC_ORBPACK = InitUtils.setup(new BatpackItem(TechRebornConfig.lapotronPackCharge, TRArmorMaterials.LAPOTRONIC_ORBPACK, RcEnergyTier.INSANE, "lapotronic_orbpack"), "lapotronic_orbpack"));

		// Tools
		RebornRegistry.registerItem(TRContent.TREE_TAP = InitUtils.setup(new TreeTapItem("treetap"), "treetap"));
		RebornRegistry.registerItem(TRContent.WRENCH = InitUtils.setup(new WrenchItem("wrench"), "wrench"));
		RebornRegistry.registerItem(TRContent.PAINTING_TOOL = InitUtils.setup(new PaintingToolItem("painting_tool"), "painting_tool"));

		RebornRegistry.registerItem(TRContent.BASIC_DRILL = InitUtils.setup(new DrillItem(TRToolMaterials.BASIC_DRILL, TechRebornConfig.basicDrillCharge, RcEnergyTier.MEDIUM, TechRebornConfig.basicDrillCost, 8F, "basic_drill"), "basic_drill"));
		RebornRegistry.registerItem(TRContent.BASIC_CHAINSAW = InitUtils.setup(new ChainsawItem(TRToolMaterials.BASIC_CHAINSAW, TechRebornConfig.basicChainsawCharge, RcEnergyTier.MEDIUM, TechRebornConfig.basicChainsawCost, 8F, "basic_chainsaw"), "basic_chainsaw"));
		RebornRegistry.registerItem(TRContent.BASIC_JACKHAMMER = InitUtils.setup(new JackhammerItem(TRToolMaterials.BASIC_JACKHAMMER, TechRebornConfig.basicJackhammerCharge, RcEnergyTier.MEDIUM, TechRebornConfig.basicJackhammerCost, "basic_jackhammer"), "basic_jackhammer"));
		RebornRegistry.registerItem(TRContent.ELECTRIC_TREE_TAP = InitUtils.setup(new ElectricTreetapItem("electric_treetap"), "electric_treetap"));

		RebornRegistry.registerItem(TRContent.ADVANCED_DRILL = InitUtils.setup(new DrillItem(TRToolMaterials.ADVANCED_DRILL, TechRebornConfig.advancedDrillCharge, RcEnergyTier.EXTREME, TechRebornConfig.advancedDrillCost, 12F, "advanced_drill"), "advanced_drill"));
		RebornRegistry.registerItem(TRContent.ADVANCED_CHAINSAW = InitUtils.setup(new ChainsawItem(TRToolMaterials.ADVANCED_CHAINSAW, TechRebornConfig.advancedChainsawCharge, RcEnergyTier.EXTREME, TechRebornConfig.advancedChainsawCost, 12F, "advanced_chainsaw"), "advanced_chainsaw"));
		RebornRegistry.registerItem(TRContent.ADVANCED_JACKHAMMER = InitUtils.setup(new AdvancedJackhammerItem("advanced_jackhammer"), "advanced_jackhammer"));
		RebornRegistry.registerItem(TRContent.ROCK_CUTTER = InitUtils.setup(new RockCutterItem("rock_cutter"), "rock_cutter"));

		RebornRegistry.registerItem(TRContent.INDUSTRIAL_DRILL = InitUtils.setup(new IndustrialDrillItem("industrial_drill"), "industrial_drill"));
		RebornRegistry.registerItem(TRContent.INDUSTRIAL_CHAINSAW = InitUtils.setup(new IndustrialChainsawItem("industrial_chainsaw"), "industrial_chainsaw"));
		RebornRegistry.registerItem(TRContent.INDUSTRIAL_JACKHAMMER = InitUtils.setup(new IndustrialJackhammerItem("industrial_jackhammer"), "industrial_jackhammer"));
		RebornRegistry.registerItem(TRContent.NANOSABER = InitUtils.setup(new NanosaberItem("nanosaber"), "nanosaber"));
		RebornRegistry.registerItem(TRContent.OMNI_TOOL = InitUtils.setup(new OmniToolItem("omni_tool"), "omni_tool"));

		// Armor
		RebornRegistry.registerItem(TRContent.CLOAKING_DEVICE = InitUtils.setup(new CloakingDeviceItem("cloaking_device"), "cloaking_device"));

		// Other
		RebornRegistry.registerItem(TRContent.GPS = InitUtils.setup(new GpsItem("gps"), "gps"));
		RebornRegistry.registerItem(TRContent.FREQUENCY_TRANSMITTER = InitUtils.setup(new FrequencyTransmitterItem("frequency_transmitter"), "frequency_transmitter"));
		RebornRegistry.registerItem(TRContent.SCRAP_BOX = InitUtils.setup(new ScrapBoxItem("scrap_box"), "scrap_box"));
		RebornRegistry.registerItem(TRContent.MANUAL = InitUtils.setup(new ManualItem("manual"), "manual"));
		RebornRegistry.registerItem(TRContent.DEBUG_TOOL = InitUtils.setup(new DebugToolItem("debug_tool"), "debug_tool"));

		TechReborn.LOGGER.debug("TechReborn's Items Loaded");
	}

	private static void registerFluids() {
		Arrays.stream(ModFluids.values()).forEach(ModFluids::register);
	}

	private static void registerSounds() {
		ModSounds.ALARM = InitUtils.setup("alarm");
		ModSounds.ALARM_2 = InitUtils.setup("alarm_2");
		ModSounds.ALARM_3 = InitUtils.setup("alarm_3");
		ModSounds.AUTO_CRAFTING = InitUtils.setup("auto_crafting");
		ModSounds.BLOCK_DISMANTLE = InitUtils.setup("block_dismantle");
		ModSounds.CABLE_SHOCK = InitUtils.setup("cable_shock");
		ModSounds.MACHINE_RUN = InitUtils.setup("machine_run");
		ModSounds.MACHINE_START = InitUtils.setup("machine_start");
		ModSounds.SAP_EXTRACT = InitUtils.setup("sap_extract");
	}

	private static void registerApis() {
		EnergyStorage.SIDED.registerForBlockEntity(CableBlockEntity::getSideEnergyStorage, TRBlockEntities.CABLE);
		EnergyStorage.SIDED.registerForBlockEntity(ReactorChamberBlockEntity::getSideEnergyStorage, TRBlockEntities.REACTOR_CHAMBER);
		ItemStorage.SIDED.registerForBlockEntity(StorageUnitBaseBlockEntity::getExposedStorage, TRBlockEntities.STORAGE_UNIT);
	}
}
