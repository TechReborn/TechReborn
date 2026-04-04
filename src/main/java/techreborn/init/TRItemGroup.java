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

package techreborn.init;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import reborncore.common.powerSystem.RcEnergyItem;
import techreborn.TechReborn;
import techreborn.component.TRDataComponentTypes;
import techreborn.init.TRContent.Cells;
import techreborn.items.armor.NanoSuitItem;
import techreborn.items.armor.QuantumSuitItem;
import techreborn.items.tool.basic.RockCutterItem;
import techreborn.items.tool.industrial.NanosaberItem;
import techreborn.utils.MaterialComparator;
import techreborn.utils.MaterialTypeComparator;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TRItemGroup {
	private static final ResourceKey<CreativeModeTab> ITEM_GROUP = ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(TechReborn.MOD_ID, "item_group"));

	public static void register() {
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ITEM_GROUP, FabricCreativeModeTab.builder()
			.title(Component.translatable("itemGroup.techreborn.item_group"))
			.icon(() -> new ItemStack(TRContent.NUKE))
			.build());

		CreativeModeTabEvents.modifyOutputEvent(ITEM_GROUP).register(TRItemGroup::entries);
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(TRItemGroup::addBuildingBlocks);
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COLORED_BLOCKS).register(TRItemGroup::addColoredBlocks);
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(TRItemGroup::addNaturalBlocks);
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(TRItemGroup::addFunctionalBlocks);
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(TRItemGroup::addRedstoneBlocks);
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(TRItemGroup::addTools);
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(TRItemGroup::addCombat);
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(TRItemGroup::addIngredients);
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.OP_BLOCKS).register(TRItemGroup::addOperator);
	}

	private static final ItemLike[] rubberOrderSmall = new ItemLike[]{
		TRContent.RUBBER_LOG,
		TRContent.RUBBER_LOG_STRIPPED,
		TRContent.RUBBER_WOOD,
		TRContent.STRIPPED_RUBBER_WOOD,
		TRContent.RUBBER_PLANKS,
		TRContent.RUBBER_STAIR,
		TRContent.RUBBER_SLAB,
		TRContent.RUBBER_FENCE,
		TRContent.RUBBER_FENCE_GATE,
		TRContent.RUBBER_DOOR,
		TRContent.RUBBER_TRAPDOOR,
		TRContent.RUBBER_PRESSURE_PLATE,
		TRContent.RUBBER_BUTTON
	};

	private static void entries(FabricCreativeModeTabOutput entries) {
		// rubber tree and related stuff
		entries.accept(TRContent.RUBBER_SAPLING);
		entries.accept(TRContent.RUBBER_LEAVES);
		addContent(rubberOrderSmall, entries);
		entries.accept(TRContent.TREE_TAP);
		addPoweredItem(TRContent.ELECTRIC_TREE_TAP, entries, null, true);
		entries.accept(TRContent.Machine.RESIN_BASIN);
		entries.accept(TRContent.Parts.SAP);
		entries.accept(TRContent.Parts.RUBBER);

		// resources
		List<Enum<? extends ItemLike>> stuff = new LinkedList<>();
		stuff.addAll(Arrays.stream(TRContent.Ores.values()).filter(ore -> !ore.isDeepslate()).toList());
		stuff.addAll(Arrays.stream(TRContent.Dusts.values()).toList());
		stuff.addAll(Arrays.stream(TRContent.RawMetals.values()).toList());
		stuff.addAll(Arrays.stream(TRContent.SmallDusts.values()).toList());
		stuff.addAll(Arrays.stream(TRContent.Gems.values()).toList());
		stuff.addAll(Arrays.stream(TRContent.Ingots.values()).toList());
		stuff.addAll(Arrays.stream(TRContent.Nuggets.values()).toList());
		stuff.addAll(Arrays.stream(TRContent.Plates.values()).toList());
		stuff.addAll(Arrays.stream(TRContent.StorageBlocks.values()).filter(block -> !block.name().startsWith("RAW")).toList());
		stuff.addAll(Arrays.stream(TRContent.NuclearReactorComponents.values()).toList());
		stuff.sort(new MaterialComparator().thenComparing(new MaterialTypeComparator()));
		for (Object item : stuff) {
			entries.accept((ItemLike)item);
		}
		entries.insertAfter(TRContent.Plates.COPPER, TRContent.COPPER_WALL);
		entries.insertAfter(TRContent.Plates.IRON, TRContent.REFINED_IRON_FENCE);
		entries.insertBefore(TRContent.Plates.CARBON,
			TRContent.Parts.CARBON_FIBER,
			TRContent.Parts.CARBON_MESH);
		entries.insertAfter(TRContent.RawMetals.TIN, TRContent.StorageBlocks.RAW_TIN);
		entries.insertAfter(TRContent.RawMetals.LEAD, TRContent.StorageBlocks.RAW_LEAD);
		entries.insertAfter(TRContent.RawMetals.SILVER, TRContent.StorageBlocks.RAW_SILVER);
		entries.insertAfter(TRContent.RawMetals.IRIDIUM, TRContent.StorageBlocks.RAW_IRIDIUM);
		entries.insertAfter(TRContent.RawMetals.TUNGSTEN, TRContent.StorageBlocks.RAW_TUNGSTEN);
		entries.insertAfter(TRContent.RawMetals.URANIUM, TRContent.StorageBlocks.RAW_URANIUM);
		for (TRContent.StorageBlocks block : TRContent.StorageBlocks.values()) {
			entries.insertAfter(block,
				block.getStairsBlock(),
				block.getSlabBlock(),
				block.getWallBlock()
				);
		}
		for (TRContent.Ores ore : TRContent.Ores.values()) {
			if (!ore.isDeepslate()) {
				continue;
			}
			entries.insertAfter(ore.getUnDeepslate(), ore);
		}

		// fluids
		addContent(ModFluids.values(), entries);
		addCells(entries);

		// parts
		addContent(TRContent.Parts.values(), entries);
		entries.accept(TRContent.FREQUENCY_TRANSMITTER);
		entries.accept(TRContent.REINFORCED_GLASS);
		entries.insertAfter(TRContent.Parts.SCRAP, TRContent.SCRAP_BOX);

		// machines
		entries.accept(TRContent.WRENCH);
		entries.accept(TRContent.PAINTING_TOOL);
		for (TRContent.MachineBlocks machineBlock : TRContent.MachineBlocks.values()) {
			entries.accept(machineBlock.frame);
			entries.accept(machineBlock.casing);
		}
		addContent(TRContent.Cables.values(), entries);
		addContent(TRContent.Machine.values(), entries);
		addContent(TRContent.SolarPanels.values(), entries);
		entries.accept(TRContent.COMPUTER_CUBE);
		entries.accept(TRContent.NUKE);
		addContent(TRContent.Upgrades.values(), entries);
		addContent(TRContent.StorageUnit.values(), entries);
		addContent(TRContent.TankUnit.values(), entries);
		for (TRContent.StorageUnit storageUnit : TRContent.StorageUnit.values()) {
			if (storageUnit.upgrader != null) {
				entries.accept(storageUnit.upgrader);
			}
		}

		// armor and traditional tools
		entries.accept(TRContent.BRONZE_HELMET);
		entries.accept(TRContent.BRONZE_CHESTPLATE);
		entries.accept(TRContent.BRONZE_LEGGINGS);
		entries.accept(TRContent.BRONZE_BOOTS);
		entries.accept(TRContent.BRONZE_SWORD);
		entries.accept(TRContent.BRONZE_PICKAXE);
		entries.accept(TRContent.BRONZE_AXE);
		entries.accept(TRContent.BRONZE_HOE);
		entries.accept(TRContent.BRONZE_SPADE);

		entries.accept(TRContent.RUBY_HELMET);
		entries.accept(TRContent.RUBY_CHESTPLATE);
		entries.accept(TRContent.RUBY_LEGGINGS);
		entries.accept(TRContent.RUBY_BOOTS);
		entries.accept(TRContent.RUBY_SWORD);
		entries.accept(TRContent.RUBY_PICKAXE);
		entries.accept(TRContent.RUBY_AXE);
		entries.accept(TRContent.RUBY_HOE);
		entries.accept(TRContent.RUBY_SPADE);

		entries.accept(TRContent.SAPPHIRE_HELMET);
		entries.accept(TRContent.SAPPHIRE_CHESTPLATE);
		entries.accept(TRContent.SAPPHIRE_LEGGINGS);
		entries.accept(TRContent.SAPPHIRE_BOOTS);
		entries.accept(TRContent.SAPPHIRE_SWORD);
		entries.accept(TRContent.SAPPHIRE_PICKAXE);
		entries.accept(TRContent.SAPPHIRE_AXE);
		entries.accept(TRContent.SAPPHIRE_HOE);
		entries.accept(TRContent.SAPPHIRE_SPADE);

		entries.accept(TRContent.PERIDOT_HELMET);
		entries.accept(TRContent.PERIDOT_CHESTPLATE);
		entries.accept(TRContent.PERIDOT_LEGGINGS);
		entries.accept(TRContent.PERIDOT_BOOTS);
		entries.accept(TRContent.PERIDOT_SWORD);
		entries.accept(TRContent.PERIDOT_PICKAXE);
		entries.accept(TRContent.PERIDOT_AXE);
		entries.accept(TRContent.PERIDOT_HOE);
		entries.accept(TRContent.PERIDOT_SPADE);

		entries.accept(TRContent.SILVER_HELMET);
		entries.accept(TRContent.SILVER_CHESTPLATE);
		entries.accept(TRContent.SILVER_LEGGINGS);
		entries.accept(TRContent.SILVER_BOOTS);

		entries.accept(TRContent.STEEL_HELMET);
		entries.accept(TRContent.STEEL_CHESTPLATE);
		entries.accept(TRContent.STEEL_LEGGINGS);
		entries.accept(TRContent.STEEL_BOOTS);

		// powered tools
		addPoweredItem(TRContent.BASIC_CHAINSAW, entries, null, true);
		addPoweredItem(TRContent.BASIC_JACKHAMMER, entries, null, true);
		addPoweredItem(TRContent.BASIC_DRILL, entries, null, true);

		addPoweredItem(TRContent.ADVANCED_CHAINSAW, entries, null, true);
		addPoweredItem(TRContent.ADVANCED_JACKHAMMER, entries, null, true);
		addPoweredItem(TRContent.ADVANCED_DRILL, entries, null, true);

		addPoweredItem(TRContent.INDUSTRIAL_CHAINSAW, entries, null, true);
		addPoweredItem(TRContent.INDUSTRIAL_JACKHAMMER, entries, null, true);
		addPoweredItem(TRContent.INDUSTRIAL_DRILL, entries, null, true);

		addRockCutter(entries, null, true);
		addPoweredItem(TRContent.OMNI_TOOL, entries, null, true);

		addPoweredItem(TRContent.QUANTUM_HELMET, entries, null, true);
		addPoweredItem(TRContent.QUANTUM_CHESTPLATE, entries, null, true);
		addPoweredItem(TRContent.QUANTUM_LEGGINGS, entries, null, true);
		addPoweredItem(TRContent.QUANTUM_BOOTS, entries, null, true);

		addPoweredItem(TRContent.NANO_HELMET, entries, null, true);
		addPoweredItem(TRContent.NANO_CHESTPLATE, entries, null, true);
		addPoweredItem(TRContent.NANO_LEGGINGS, entries, null, true);
		addPoweredItem(TRContent.NANO_BOOTS, entries, null, true);

		addNanosaber(entries, null, false);

		addPoweredItem(TRContent.LITHIUM_ION_BATPACK, entries, null, true);
		addPoweredItem(TRContent.LAPOTRONIC_ORBPACK, entries, null, true);
		addPoweredItem(TRContent.CLOAKING_DEVICE, entries, null, true);

		addPoweredItem(TRContent.RED_CELL_BATTERY, entries, null, true);
		addPoweredItem(TRContent.LITHIUM_ION_BATTERY, entries, null, true);
		addPoweredItem(TRContent.ENERGY_CRYSTAL, entries, null, true);
		addPoweredItem(TRContent.LAPOTRON_CRYSTAL, entries, null, true);
		addPoweredItem(TRContent.LAPOTRONIC_ORB, entries, null, true);

		entries.accept(TRContent.GPS);

		entries.accept(TRContent.MANUAL);
		entries.accept(TRContent.DEBUG_TOOL);
	}

	private static void addBuildingBlocks(FabricCreativeModeTabOutput entries) {
		entries.insertAfter(Items.MANGROVE_BUTTON, rubberOrderSmall);
		entries.insertAfter(Items.AMETHYST_BLOCK,
			TRContent.MachineBlocks.BASIC.getFrame(),
			TRContent.MachineBlocks.ADVANCED.getFrame(),
			TRContent.MachineBlocks.INDUSTRIAL.getFrame());
		entries.insertAfter(Items.IRON_CHAIN, TRContent.REFINED_IRON_FENCE);
		entries.insertBefore(Items.COPPER_BLOCK,
			TRContent.StorageBlocks.RAW_TIN,
			TRContent.StorageBlocks.RAW_TIN.getStairsBlock(),
			TRContent.StorageBlocks.RAW_TIN.getSlabBlock(),
			TRContent.StorageBlocks.RAW_TIN.getWallBlock(),
			TRContent.StorageBlocks.TIN,
			TRContent.StorageBlocks.TIN.getStairsBlock(),
			TRContent.StorageBlocks.TIN.getSlabBlock(),
			TRContent.StorageBlocks.TIN.getWallBlock(),
			TRContent.StorageBlocks.ZINC,
			TRContent.StorageBlocks.ZINC.getStairsBlock(),
			TRContent.StorageBlocks.ZINC.getSlabBlock(),
			TRContent.StorageBlocks.ZINC.getWallBlock(),
			TRContent.StorageBlocks.REFINED_IRON,
			TRContent.StorageBlocks.REFINED_IRON.getStairsBlock(),
			TRContent.StorageBlocks.REFINED_IRON.getSlabBlock(),
			TRContent.StorageBlocks.REFINED_IRON.getWallBlock(),
			TRContent.StorageBlocks.STEEL,
			TRContent.StorageBlocks.STEEL.getStairsBlock(),
			TRContent.StorageBlocks.STEEL.getSlabBlock(),
			TRContent.StorageBlocks.STEEL.getWallBlock());
		entries.insertAfter(Items.CUT_COPPER_SLAB, TRContent.COPPER_WALL);
		entries.insertAfter(Items.WAXED_OXIDIZED_CUT_COPPER_SLAB,
			TRContent.StorageBlocks.RAW_LEAD,
			TRContent.StorageBlocks.RAW_LEAD.getStairsBlock(),
			TRContent.StorageBlocks.RAW_LEAD.getSlabBlock(),
			TRContent.StorageBlocks.RAW_LEAD.getWallBlock(),
			TRContent.StorageBlocks.LEAD,
			TRContent.StorageBlocks.LEAD.getStairsBlock(),
			TRContent.StorageBlocks.LEAD.getSlabBlock(),
			TRContent.StorageBlocks.LEAD.getWallBlock(),
			TRContent.StorageBlocks.NICKEL,
			TRContent.StorageBlocks.NICKEL.getStairsBlock(),
			TRContent.StorageBlocks.NICKEL.getSlabBlock(),
			TRContent.StorageBlocks.NICKEL.getWallBlock(),
			TRContent.StorageBlocks.BRONZE,
			TRContent.StorageBlocks.BRONZE.getStairsBlock(),
			TRContent.StorageBlocks.BRONZE.getSlabBlock(),
			TRContent.StorageBlocks.BRONZE.getWallBlock(),
			TRContent.StorageBlocks.BRASS,
			TRContent.StorageBlocks.BRASS.getStairsBlock(),
			TRContent.StorageBlocks.BRASS.getSlabBlock(),
			TRContent.StorageBlocks.BRASS.getWallBlock(),
			TRContent.StorageBlocks.ADVANCED_ALLOY,
			TRContent.StorageBlocks.ADVANCED_ALLOY.getStairsBlock(),
			TRContent.StorageBlocks.ADVANCED_ALLOY.getSlabBlock(),
			TRContent.StorageBlocks.ADVANCED_ALLOY.getWallBlock(),
			TRContent.StorageBlocks.INVAR,
			TRContent.StorageBlocks.INVAR.getStairsBlock(),
			TRContent.StorageBlocks.INVAR.getSlabBlock(),
			TRContent.StorageBlocks.INVAR.getWallBlock(),
			TRContent.StorageBlocks.RAW_SILVER,
			TRContent.StorageBlocks.RAW_SILVER.getStairsBlock(),
			TRContent.StorageBlocks.RAW_SILVER.getSlabBlock(),
			TRContent.StorageBlocks.RAW_SILVER.getWallBlock(),
			TRContent.StorageBlocks.SILVER,
			TRContent.StorageBlocks.SILVER.getStairsBlock(),
			TRContent.StorageBlocks.SILVER.getSlabBlock(),
			TRContent.StorageBlocks.SILVER.getWallBlock(),
			TRContent.StorageBlocks.ELECTRUM,
			TRContent.StorageBlocks.ELECTRUM.getStairsBlock(),
			TRContent.StorageBlocks.ELECTRUM.getSlabBlock(),
			TRContent.StorageBlocks.ELECTRUM.getWallBlock(),
			TRContent.StorageBlocks.ALUMINUM,
			TRContent.StorageBlocks.ALUMINUM.getStairsBlock(),
			TRContent.StorageBlocks.ALUMINUM.getSlabBlock(),
			TRContent.StorageBlocks.ALUMINUM.getWallBlock(),
			TRContent.StorageBlocks.TITANIUM,
			TRContent.StorageBlocks.TITANIUM.getStairsBlock(),
			TRContent.StorageBlocks.TITANIUM.getSlabBlock(),
			TRContent.StorageBlocks.TITANIUM.getWallBlock(),
			TRContent.StorageBlocks.CHROME,
			TRContent.StorageBlocks.CHROME.getStairsBlock(),
			TRContent.StorageBlocks.CHROME.getSlabBlock(),
			TRContent.StorageBlocks.CHROME.getWallBlock(),
			TRContent.StorageBlocks.RUBY,
			TRContent.StorageBlocks.RUBY.getStairsBlock(),
			TRContent.StorageBlocks.RUBY.getSlabBlock(),
			TRContent.StorageBlocks.RUBY.getWallBlock(),
			TRContent.StorageBlocks.SAPPHIRE,
			TRContent.StorageBlocks.SAPPHIRE.getStairsBlock(),
			TRContent.StorageBlocks.SAPPHIRE.getSlabBlock(),
			TRContent.StorageBlocks.SAPPHIRE.getWallBlock(),
			TRContent.StorageBlocks.PERIDOT,
			TRContent.StorageBlocks.PERIDOT.getStairsBlock(),
			TRContent.StorageBlocks.PERIDOT.getSlabBlock(),
			TRContent.StorageBlocks.PERIDOT.getWallBlock(),
			TRContent.StorageBlocks.RED_GARNET,
			TRContent.StorageBlocks.RED_GARNET.getStairsBlock(),
			TRContent.StorageBlocks.RED_GARNET.getSlabBlock(),
			TRContent.StorageBlocks.RED_GARNET.getWallBlock(),
			TRContent.StorageBlocks.YELLOW_GARNET,
			TRContent.StorageBlocks.YELLOW_GARNET.getStairsBlock(),
			TRContent.StorageBlocks.YELLOW_GARNET.getSlabBlock(),
			TRContent.StorageBlocks.YELLOW_GARNET.getWallBlock(),
			TRContent.StorageBlocks.RAW_IRIDIUM,
			TRContent.StorageBlocks.RAW_IRIDIUM.getStairsBlock(),
			TRContent.StorageBlocks.RAW_IRIDIUM.getSlabBlock(),
			TRContent.StorageBlocks.RAW_IRIDIUM.getWallBlock(),
			TRContent.StorageBlocks.IRIDIUM,
			TRContent.StorageBlocks.IRIDIUM.getStairsBlock(),
			TRContent.StorageBlocks.IRIDIUM.getSlabBlock(),
			TRContent.StorageBlocks.IRIDIUM.getWallBlock(),
			TRContent.StorageBlocks.RAW_TUNGSTEN,
			TRContent.StorageBlocks.RAW_TUNGSTEN.getStairsBlock(),
			TRContent.StorageBlocks.RAW_TUNGSTEN.getSlabBlock(),
			TRContent.StorageBlocks.RAW_TUNGSTEN.getWallBlock(),
			TRContent.StorageBlocks.TUNGSTEN,
			TRContent.StorageBlocks.TUNGSTEN.getStairsBlock(),
			TRContent.StorageBlocks.TUNGSTEN.getSlabBlock(),
			TRContent.StorageBlocks.TUNGSTEN.getWallBlock(),
			TRContent.StorageBlocks.TUNGSTENSTEEL,
			TRContent.StorageBlocks.TUNGSTENSTEEL.getStairsBlock(),
			TRContent.StorageBlocks.TUNGSTENSTEEL.getSlabBlock(),
			TRContent.StorageBlocks.TUNGSTENSTEEL.getWallBlock(),
			TRContent.StorageBlocks.HOT_TUNGSTENSTEEL,
			TRContent.StorageBlocks.HOT_TUNGSTENSTEEL.getStairsBlock(),
			TRContent.StorageBlocks.HOT_TUNGSTENSTEEL.getSlabBlock(),
			TRContent.StorageBlocks.HOT_TUNGSTENSTEEL.getWallBlock(),
			TRContent.StorageBlocks.IRIDIUM_REINFORCED_STONE,
			TRContent.StorageBlocks.IRIDIUM_REINFORCED_STONE.getStairsBlock(),
			TRContent.StorageBlocks.IRIDIUM_REINFORCED_STONE.getSlabBlock(),
			TRContent.StorageBlocks.IRIDIUM_REINFORCED_STONE.getWallBlock(),
			TRContent.StorageBlocks.IRIDIUM_REINFORCED_TUNGSTENSTEEL,
			TRContent.StorageBlocks.IRIDIUM_REINFORCED_TUNGSTENSTEEL.getStairsBlock(),
			TRContent.StorageBlocks.IRIDIUM_REINFORCED_TUNGSTENSTEEL.getSlabBlock(),
			TRContent.StorageBlocks.IRIDIUM_REINFORCED_TUNGSTENSTEEL.getWallBlock());
	}

	private static void addColoredBlocks(FabricCreativeModeTabOutput entries) {
		entries.insertBefore(Items.TINTED_GLASS, TRContent.REINFORCED_GLASS);
	}

	private static void addNaturalBlocks(FabricCreativeModeTabOutput entries) {
		entries.insertBefore(Items.IRON_ORE, TRContent.Ores.TIN, TRContent.Ores.DEEPSLATE_TIN);
		entries.insertAfter(Items.DEEPSLATE_COPPER_ORE,
			TRContent.Ores.LEAD, TRContent.Ores.DEEPSLATE_LEAD,
			TRContent.Ores.SILVER, TRContent.Ores.DEEPSLATE_SILVER);
		entries.insertAfter(Items.DEEPSLATE_GOLD_ORE,
			TRContent.Ores.GALENA, TRContent.Ores.DEEPSLATE_GALENA,
			TRContent.Ores.BAUXITE, TRContent.Ores.DEEPSLATE_BAUXITE);
		entries.insertAfter(Items.DEEPSLATE_REDSTONE_ORE,
			TRContent.Ores.RUBY, TRContent.Ores.DEEPSLATE_RUBY,
			TRContent.Ores.SAPPHIRE, TRContent.Ores.DEEPSLATE_SAPPHIRE);
		entries.insertAfter(Items.DEEPSLATE_DIAMOND_ORE, TRContent.Ores.IRIDIUM, TRContent.Ores.DEEPSLATE_IRIDIUM, TRContent.Ores.URANIUM, TRContent.Ores.DEEPSLATE_URANIUM);
		entries.insertAfter(Items.NETHER_GOLD_ORE,
			TRContent.Ores.CINNABAR,
			TRContent.Ores.PYRITE,
			TRContent.Ores.SPHALERITE);
		entries.insertAfter(Items.ANCIENT_DEBRIS,
			TRContent.Ores.PERIDOT,
			TRContent.Ores.SHELDONITE,
			TRContent.Ores.SODALITE,
			TRContent.Ores.TUNGSTEN);
		entries.insertBefore(Items.RAW_IRON_BLOCK, TRContent.StorageBlocks.RAW_TIN);
		entries.insertAfter(Items.RAW_COPPER_BLOCK,
			TRContent.StorageBlocks.RAW_LEAD,
			TRContent.StorageBlocks.RAW_SILVER);
		entries.insertAfter(Items.RAW_GOLD_BLOCK,
			TRContent.StorageBlocks.RAW_IRIDIUM,
			TRContent.StorageBlocks.RAW_TUNGSTEN,
			TRContent.StorageBlocks.RAW_URANIUM);
		entries.insertAfter(Items.MANGROVE_LOG, TRContent.RUBBER_LOG);
		entries.insertAfter(Items.MUDDY_MANGROVE_ROOTS, TRContent.RUBBER_LEAVES);
		entries.insertAfter(Items.MANGROVE_PROPAGULE, TRContent.RUBBER_SAPLING);
	}

	private static void addFunctionalBlocks(FabricCreativeModeTabOutput entries) {
		entries.insertAfter(Items.END_ROD,
			TRContent.Machine.LAMP_INCANDESCENT,
			TRContent.Machine.LAMP_LED);
		entries.insertBefore(Items.CRYING_OBSIDIAN, TRContent.StorageBlocks.HOT_TUNGSTENSTEEL);
		entries.insertAfter(Items.CRAFTING_TABLE,
			TRContent.Machine.AUTO_CRAFTING_TABLE,
			TRContent.Machine.BLOCK_BREAKER,
			TRContent.Machine.BLOCK_PLACER);
		entries.insertAfter(Items.STONECUTTER, TRContent.Machine.RESIN_BASIN);
		entries.insertAfter(Items.SMITHING_TABLE, TRContent.Machine.ASSEMBLY_MACHINE);
		entries.insertAfter(Items.FURNACE,
			TRContent.Machine.IRON_FURNACE,
			TRContent.Machine.ELECTRIC_FURNACE);
		entries.insertAfter(Items.BLAST_FURNACE,
			TRContent.Machine.INDUSTRIAL_BLAST_FURNACE,
			TRContent.Machine.IRON_ALLOY_FURNACE,
			TRContent.Machine.ALLOY_SMELTER,
			TRContent.Machine.GRINDER,
			TRContent.Machine.INDUSTRIAL_GRINDER,
			TRContent.Machine.INDUSTRIAL_SAWMILL,
			TRContent.Machine.ROLLING_MACHINE,
			TRContent.Machine.VACUUM_FREEZER);
		entries.insertAfter(Items.SCAFFOLDING,
			TRContent.Machine.ELEVATOR,
			TRContent.Machine.LAUNCHPAD);
		entries.insertAfter(Items.COMPOSTER,
			TRContent.Machine.COMPRESSOR,
			TRContent.Machine.SOLID_CANNING_MACHINE,
			TRContent.Machine.RECYCLER,
			TRContent.Machine.SCRAPBOXINATOR,
			TRContent.Machine.MATTER_FABRICATOR);
		entries.insertBefore(Items.BREWING_STAND, TRContent.Machine.IMPLOSION_COMPRESSOR);
		entries.insertBefore(Items.CAULDRON,
			TRContent.Machine.EXTRACTOR,
			TRContent.Machine.CHEMICAL_REACTOR,
			TRContent.Machine.INDUSTRIAL_CENTRIFUGE,
			TRContent.Machine.INDUSTRIAL_ELECTROLYZER,
			TRContent.Machine.DISTILLATION_TOWER);
		entries.insertAfter(Items.CAULDRON,
			TRContent.Machine.DRAIN,
			TRContent.Machine.PUMP,
			TRContent.Machine.FLUID_REPLICATOR,
			TRContent.Machine.FISHING_STATION);
		entries.insertAfter(Items.LODESTONE, TRContent.Machine.CHUNK_LOADER);
		entries.insertAfter(Items.BEEHIVE, TRContent.Machine.GREENHOUSE_CONTROLLER);
		entries.insertAfter(Items.LIGHTNING_ROD, TRContent.Machine.LIGHTNING_ROD);
		// inventory stuff
		entries.insertAfter(Items.ENDER_CHEST,
			TRContent.StorageUnit.BUFFER,
			TRContent.StorageUnit.CRUDE,
			TRContent.StorageUnit.BASIC,
			TRContent.StorageUnit.ADVANCED,
			TRContent.StorageUnit.INDUSTRIAL,
			TRContent.StorageUnit.QUANTUM,
			TRContent.StorageUnit.CREATIVE,
			TRContent.TankUnit.BASIC,
			TRContent.TankUnit.ADVANCED,
			TRContent.TankUnit.INDUSTRIAL,
			TRContent.TankUnit.QUANTUM,
			TRContent.TankUnit.CREATIVE);
		entries.insertBefore(Items.SKELETON_SKULL,
			// blocks
			TRContent.MachineBlocks.BASIC.getCasing(),
			TRContent.MachineBlocks.ADVANCED.getCasing(),
			TRContent.MachineBlocks.INDUSTRIAL.getCasing(),
			TRContent.Machine.FUSION_COIL,
			// generators
			TRContent.Machine.SOLID_FUEL_GENERATOR,
			TRContent.Machine.SEMI_FLUID_GENERATOR,
			TRContent.Machine.DIESEL_GENERATOR,
			TRContent.Machine.GAS_TURBINE,
			TRContent.Machine.PLASMA_GENERATOR,
			TRContent.Machine.THERMAL_GENERATOR,
			TRContent.Machine.WATER_MILL,
			TRContent.Machine.WIND_MILL,
			TRContent.Machine.DRAGON_EGG_SYPHON,
			TRContent.Machine.FUSION_CONTROL_COMPUTER,
			TRContent.SolarPanels.BASIC,
			TRContent.SolarPanels.ADVANCED,
			TRContent.SolarPanels.INDUSTRIAL,
			TRContent.SolarPanels.ULTIMATE,
			TRContent.SolarPanels.QUANTUM,
			TRContent.SolarPanels.CREATIVE,
			// batteries & transformers
			TRContent.Machine.LOW_VOLTAGE_SU,
			TRContent.Machine.LV_TRANSFORMER,
			TRContent.Machine.MEDIUM_VOLTAGE_SU,
			TRContent.Machine.MV_TRANSFORMER,
			TRContent.Machine.HIGH_VOLTAGE_SU,
			TRContent.Machine.HV_TRANSFORMER,
			TRContent.Machine.CHARGE_O_MAT,
			TRContent.Machine.LAPOTRONIC_SU,
			TRContent.Machine.LSU_STORAGE,
			TRContent.Machine.ADJUSTABLE_SU,
			TRContent.Machine.EV_TRANSFORMER,
			TRContent.Machine.INTERDIMENSIONAL_SU,
			// cables
			TRContent.Cables.TIN,
			TRContent.Cables.COPPER,
			TRContent.Cables.INSULATED_COPPER,
			TRContent.Cables.GOLD,
			TRContent.Cables.INSULATED_GOLD,
			TRContent.Cables.HV,
			TRContent.Cables.INSULATED_HV,
			TRContent.Cables.GLASSFIBER,
			TRContent.Cables.SUPERCONDUCTOR,
			TRContent.Machine.WIRE_MILL);
	}

	private static void addRedstoneBlocks(FabricCreativeModeTabOutput entries) {
		entries.insertBefore(Items.SCULK_SENSOR, TRContent.Machine.ALARM);
		entries.insertAfter(Items.WHITE_WOOL, TRContent.Machine.PLAYER_DETECTOR);
	}

	private static void addTools(FabricCreativeModeTabOutput entries) {
		entries.insertBefore(Items.GOLDEN_SHOVEL,
			TRContent.BRONZE_SPADE,
			TRContent.BRONZE_PICKAXE,
			TRContent.BRONZE_AXE,
			TRContent.BRONZE_HOE);
		entries.insertBefore(Items.DIAMOND_SHOVEL,
			TRContent.RUBY_SPADE,
			TRContent.RUBY_PICKAXE,
			TRContent.RUBY_AXE,
			TRContent.RUBY_HOE,
			TRContent.SAPPHIRE_SPADE,
			TRContent.SAPPHIRE_PICKAXE,
			TRContent.SAPPHIRE_AXE,
			TRContent.SAPPHIRE_HOE,
			TRContent.PERIDOT_SPADE,
			TRContent.PERIDOT_PICKAXE,
			TRContent.PERIDOT_AXE,
			TRContent.PERIDOT_HOE);
		// order very important here
		entries.insertBefore(Items.BUCKET, TRContent.TREE_TAP);
		addPoweredItem(TRContent.ELECTRIC_TREE_TAP, entries, Items.BUCKET, false);
		addPoweredItem(TRContent.ROCK_CUTTER, entries, Items.BUCKET, false);
		addPoweredItem(TRContent.BASIC_DRILL, entries, Items.BUCKET, false);
		addPoweredItem(TRContent.ADVANCED_DRILL, entries, Items.BUCKET, false);
		addPoweredItem(TRContent.INDUSTRIAL_DRILL, entries, Items.BUCKET, false);
		addPoweredItem(TRContent.BASIC_JACKHAMMER, entries, Items.BUCKET, false);
		addPoweredItem(TRContent.ADVANCED_JACKHAMMER, entries, Items.BUCKET, false);
		addPoweredItem(TRContent.INDUSTRIAL_JACKHAMMER, entries, Items.BUCKET, false);
		addPoweredItem(TRContent.BASIC_CHAINSAW, entries, Items.BUCKET, false);
		addPoweredItem(TRContent.ADVANCED_CHAINSAW, entries, Items.BUCKET, false);
		addPoweredItem(TRContent.INDUSTRIAL_CHAINSAW, entries, Items.BUCKET, false);
		addPoweredItem(TRContent.OMNI_TOOL, entries, Items.BUCKET, false);
		entries.insertBefore(Items.BUCKET, TRContent.Cells.EMPTY);
		addPoweredItem(TRContent.RED_CELL_BATTERY, entries, Items.OAK_BOAT, false);
		addPoweredItem(TRContent.LITHIUM_ION_BATTERY, entries, Items.OAK_BOAT, false);
		addPoweredItem(TRContent.LITHIUM_ION_BATPACK, entries, Items.OAK_BOAT, false);
		addPoweredItem(TRContent.ENERGY_CRYSTAL, entries, Items.OAK_BOAT, false);
		addPoweredItem(TRContent.LAPOTRON_CRYSTAL, entries, Items.OAK_BOAT, false);
		addPoweredItem(TRContent.LAPOTRONIC_ORB, entries, Items.OAK_BOAT, false);
		addPoweredItem(TRContent.LAPOTRONIC_ORBPACK, entries, Items.OAK_BOAT, false);
		// now order not important again
		entries.insertBefore(Items.SHEARS,
			TRContent.SCRAP_BOX,
			TRContent.Plates.WOOD,
			TRContent.PAINTING_TOOL);
		entries.insertAfter(Items.RECOVERY_COMPASS, TRContent.GPS);
		entries.insertAfter(Items.SPYGLASS, TRContent.WRENCH);
	}

	private static void addCombat(FabricCreativeModeTabOutput entries) {
		addNanosaber(entries, Items.WOODEN_AXE, true);
		entries.insertAfter(Items.IRON_BOOTS,
			TRContent.STEEL_HELMET,
			TRContent.STEEL_CHESTPLATE,
			TRContent.STEEL_LEGGINGS,
			TRContent.STEEL_BOOTS,
			TRContent.BRONZE_HELMET,
			TRContent.BRONZE_CHESTPLATE,
			TRContent.BRONZE_LEGGINGS,
			TRContent.BRONZE_BOOTS);
		entries.insertBefore(Items.GOLDEN_HELMET,
			TRContent.SILVER_HELMET,
			TRContent.SILVER_CHESTPLATE,
			TRContent.SILVER_LEGGINGS,
			TRContent.SILVER_BOOTS);
		entries.insertBefore(Items.DIAMOND_HELMET,
			TRContent.RUBY_HELMET,
			TRContent.RUBY_CHESTPLATE,
			TRContent.RUBY_LEGGINGS,
			TRContent.RUBY_BOOTS,
			TRContent.SAPPHIRE_HELMET,
			TRContent.SAPPHIRE_CHESTPLATE,
			TRContent.SAPPHIRE_LEGGINGS,
			TRContent.SAPPHIRE_BOOTS,
			TRContent.PERIDOT_HELMET,
			TRContent.PERIDOT_CHESTPLATE,
			TRContent.PERIDOT_LEGGINGS,
			TRContent.PERIDOT_BOOTS);
		addPoweredItem(TRContent.QUANTUM_HELMET, entries, Items.TURTLE_HELMET, false);
		addPoweredItem(TRContent.QUANTUM_CHESTPLATE, entries, Items.TURTLE_HELMET, false);
		addPoweredItem(TRContent.QUANTUM_LEGGINGS, entries, Items.TURTLE_HELMET, false);
		addPoweredItem(TRContent.QUANTUM_BOOTS, entries, Items.TURTLE_HELMET, false);
		addPoweredItem(TRContent.NANO_HELMET, entries, Items.TURTLE_HELMET, false);
		addPoweredItem(TRContent.NANO_CHESTPLATE, entries, Items.TURTLE_HELMET, false);
		addPoweredItem(TRContent.NANO_LEGGINGS, entries, Items.TURTLE_HELMET, false);
		addPoweredItem(TRContent.NANO_BOOTS, entries, Items.TURTLE_HELMET, false);
		addPoweredItem(TRContent.CLOAKING_DEVICE, entries, Items.LEATHER_HORSE_ARMOR, false);
		entries.insertAfter(Items.END_CRYSTAL, TRContent.NUKE);
	}

	private static void addIngredients(FabricCreativeModeTabOutput entries) {
		// raw / gem
		entries.insertBefore(Items.RAW_IRON, TRContent.RawMetals.TIN);
		entries.insertAfter(Items.RAW_COPPER,
			TRContent.RawMetals.LEAD,
			TRContent.RawMetals.SILVER);
		entries.insertBefore(Items.EMERALD,
			TRContent.Gems.RUBY,
			TRContent.Gems.SAPPHIRE,
			TRContent.Gems.PERIDOT,
			TRContent.Gems.RED_GARNET,
			TRContent.Gems.YELLOW_GARNET);
		entries.insertAfter(Items.DIAMOND,
			TRContent.RawMetals.IRIDIUM,
			TRContent.RawMetals.TUNGSTEN);
		// nuggets
		entries.insertBefore(Items.IRON_NUGGET,
			TRContent.Nuggets.TIN,
			TRContent.Nuggets.ZINC);
		entries.insertAfter(Items.IRON_NUGGET,
			TRContent.Nuggets.REFINED_IRON,
			TRContent.Nuggets.STEEL,
			TRContent.Nuggets.COPPER,
			TRContent.Nuggets.LEAD,
			TRContent.Nuggets.NICKEL,
			TRContent.Nuggets.BRONZE,
			TRContent.Nuggets.BRASS,
			TRContent.Nuggets.INVAR);
		entries.insertBefore(Items.GOLD_NUGGET, TRContent.Nuggets.SILVER);
		entries.insertAfter(Items.GOLD_NUGGET,
			TRContent.Nuggets.ELECTRUM,
			TRContent.Nuggets.ALUMINUM,
			TRContent.Nuggets.TITANIUM,
			TRContent.Nuggets.CHROME,
			TRContent.Nuggets.EMERALD,
			TRContent.Nuggets.DIAMOND,
			TRContent.Nuggets.IRIDIUM,
			TRContent.Nuggets.TUNGSTEN,
			TRContent.Nuggets.NETHERITE);
		// ingots
		entries.insertBefore(Items.IRON_INGOT,
			TRContent.Ingots.TIN,
			TRContent.Ingots.ZINC);
		entries.insertAfter(Items.IRON_INGOT,
			TRContent.Ingots.REFINED_IRON,
			TRContent.Ingots.STEEL);
		entries.insertAfter(Items.COPPER_INGOT,
			TRContent.Ingots.LEAD,
			TRContent.Ingots.NICKEL,
			TRContent.Ingots.BRONZE,
			TRContent.Ingots.BRASS,
			TRContent.Ingots.MIXED_METAL,
			TRContent.Ingots.ADVANCED_ALLOY,
			TRContent.Ingots.INVAR);
		entries.insertBefore(Items.GOLD_INGOT, TRContent.Ingots.SILVER);
		entries.insertAfter(Items.GOLD_INGOT,
			TRContent.Ingots.ELECTRUM,
			TRContent.Ingots.ALUMINUM,
			TRContent.Ingots.TITANIUM,
			TRContent.Ingots.CHROME,
			TRContent.Ingots.INDUSTRIAL_ALLOY,
			TRContent.Ingots.IRIDIUM,
			TRContent.Ingots.IRIDIUM_ALLOY,
			TRContent.Ingots.TUNGSTEN,
			TRContent.Ingots.TUNGSTENSTEEL,
			TRContent.Ingots.HOT_TUNGSTENSTEEL);
		// dusts and small dusts
		// misc
		entries.insertAfter(Items.STICK,
			TRContent.Parts.PLANTBALL,
			TRContent.Parts.COMPRESSED_PLANTBALL);
		entries.insertAfter(Items.HONEYCOMB,
			TRContent.Parts.SAP,
			TRContent.Parts.RUBBER,
			TRContent.Parts.SCRAP);
		entries.insertBefore(Items.PRISMARINE_SHARD, TRContent.Parts.SPONGE_PIECE);
		entries.insertBefore(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, TRContent.Parts.TEMPLATE_TEMPLATE);
		entries.insertBefore(Items.BRICK,
			TRContent.Parts.CARBON_FIBER,
			TRContent.Parts.CARBON_MESH);
		entries.insertBefore(Items.NETHER_STAR, TRContent.Parts.SYNTHETIC_REDSTONE_CRYSTAL);
		entries.insertAfter(Items.NETHERITE_INGOT, TRContent.Parts.UU_MATTER);
		// machine parts
		entries.insertAfter(Items.NETHER_BRICK,
			TRContent.Parts.ELECTRONIC_CIRCUIT,
			TRContent.Parts.ADVANCED_CIRCUIT,
			TRContent.Parts.INDUSTRIAL_CIRCUIT,
			TRContent.Parts.DATA_STORAGE_CORE,
			TRContent.Parts.DATA_STORAGE_CHIP,
			TRContent.Parts.ENERGY_FLOW_CHIP,
			TRContent.Parts.SUPERCONDUCTOR,
			TRContent.Parts.CUPRONICKEL_HEATING_COIL,
			TRContent.Parts.NICHROME_HEATING_COIL,
			TRContent.Parts.KANTHAL_HEATING_COIL,
			TRContent.Parts.DIAMOND_SAW_BLADE,
			TRContent.Parts.DIAMOND_GRINDING_HEAD,
			TRContent.Parts.TUNGSTEN_GRINDING_HEAD);
		entries.insertBefore(Items.FIREWORK_STAR,
			TRContent.Parts.BASIC_DISPLAY,
			TRContent.Parts.DIGITAL_DISPLAY);
		// nuclear reactor parts
		entries.insertAfter(Items.PHANTOM_MEMBRANE,
			TRContent.NuclearReactorComponents.EMPTY_FUEL_ROD,
			TRContent.NuclearReactorComponents.URANIUM_FUEL_ROD,
			TRContent.NuclearReactorComponents.DUAL_URANIUM_FUEL_ROD,
			TRContent.NuclearReactorComponents.QUAD_URANIUM_FUEL_ROD,
			TRContent.NuclearReactorComponents.DEPLETED_URANIUM_FUEL_ROD,
			TRContent.NuclearReactorComponents.DUAL_DEPLETED_URANIUM_FUEL_ROD,
			TRContent.NuclearReactorComponents.QUAD_DEPLETED_URANIUM_FUEL_ROD,
			TRContent.NuclearReactorComponents.WATER_COOLANT_CELL_10K,
			TRContent.NuclearReactorComponents.WATER_COOLANT_CELL_30K,
			TRContent.NuclearReactorComponents.WATER_COOLANT_CELL_60K,
			TRContent.NuclearReactorComponents.NAK_COOLANT_CELL_60K,
			TRContent.NuclearReactorComponents.NAK_COOLANT_CELL_180K,
			TRContent.NuclearReactorComponents.NAK_COOLANT_CELL_360K,
			TRContent.NuclearReactorComponents.HELIUM_COOLANT_CELL_60K,
			TRContent.NuclearReactorComponents.HELIUM_COOLANT_CELL_180K,
			TRContent.NuclearReactorComponents.HELIUM_COOLANT_CELL_360K,
			TRContent.NuclearReactorComponents.NEUTRON_REFLECTOR,
			TRContent.NuclearReactorComponents.THICK_NEUTRON_REFLECTOR,
			TRContent.NuclearReactorComponents.IRIDIUM_NEUTRON_REFLECTOR,
			TRContent.NuclearReactorComponents.HEAT_VENT,
			TRContent.NuclearReactorComponents.ADVANCED_HEAT_VENT,
			TRContent.NuclearReactorComponents.REACTOR_HEAT_VENT,
			TRContent.NuclearReactorComponents.OVERCLOCKED_HEAT_VENT,
			TRContent.NuclearReactorComponents.COMPONENT_HEAT_VENT,
			TRContent.NuclearReactorComponents.HEAT_EXCHANGER,
			TRContent.NuclearReactorComponents.ADVANCED_HEAT_EXCHANGER,
			TRContent.NuclearReactorComponents.REACTOR_HEAT_EXCHANGER,
			TRContent.NuclearReactorComponents.COMPONENT_HEAT_EXCHANGER,
			TRContent.NuclearReactorComponents.REACTOR_PLATING,
			TRContent.NuclearReactorComponents.HEAT_CAPACITY_REACTOR_PLATING,
			TRContent.NuclearReactorComponents.CONTAINMENT_REACTOR_PLATING);
	}

	private static void addOperator(FabricCreativeModeTabOutput entries) {
		if (entries.shouldShowOpRestrictedItems()) {
			entries.insertAfter(Items.DEBUG_STICK, TRContent.DEBUG_TOOL);
		}
	}

	private static void addContent(ItemLike[] items, FabricCreativeModeTabOutput entries) {
		for (ItemLike item : items) {
			entries.accept(item);
		}
	}

	private static void addCells(FabricCreativeModeTabOutput entries) {
		for (Cells cell : Cells.values()) {
			entries.accept(cell.asItem());
		}
	}

	private static void addPoweredItem(Item item, FabricCreativeModeTabOutput entries, ItemLike before, boolean includeUncharged) {
		ItemStack uncharged = new ItemStack(item);
		ItemStack charged = new ItemStack(item);
		RcEnergyItem energyItem = (RcEnergyItem) item;

		energyItem.setStoredEnergy(charged, energyItem.getEnergyCapacity(null));
		if (item instanceof NanoSuitItem nanoSuitItem) {
			nanoSuitItem.applyModifier(uncharged, false);
			nanoSuitItem.applyModifier(charged, false);
		} else if (item instanceof QuantumSuitItem quantumSuitItem) {
			quantumSuitItem.applyModifier(uncharged, false, false);
			quantumSuitItem.applyModifier(charged, false, false);
		}

		if (before == null) {
			if (includeUncharged) {
				entries.accept(uncharged);
			}
			entries.accept(charged);
		}
		else {
			if (includeUncharged) {
				entries.insertBefore(before, uncharged, charged);
			}
			else {
				entries.insertBefore(before, charged);
			}
		}
	}

	private static void addRockCutter(FabricCreativeModeTabOutput entries, ItemLike before, boolean includeUncharged) {
		HolderLookup.RegistryLookup<Enchantment> enchantmentRegistry = entries.getContext().holders().lookupOrThrow(Registries.ENCHANTMENT);
		RockCutterItem rockCutter = (RockCutterItem) TRContent.ROCK_CUTTER;

		ItemStack uncharged = new ItemStack(rockCutter);
		uncharged.enchant(enchantmentRegistry.getOrThrow(Enchantments.SILK_TOUCH), 1);
		ItemStack charged = new ItemStack(rockCutter);
		charged.enchant(enchantmentRegistry.getOrThrow(Enchantments.SILK_TOUCH), 1);
		rockCutter.setStoredEnergy(charged, rockCutter.getEnergyCapacity(charged));

		if (before == null) {
			if (includeUncharged) {
				entries.accept(uncharged);
			}
			entries.accept(charged);
		}
		else {
			if (includeUncharged) {
				entries.insertBefore(before, uncharged, charged);
			}
			else {
				entries.insertBefore(before, charged);
			}
		}
	}

	private static void addNanosaber(FabricCreativeModeTabOutput entries, ItemLike before, boolean onlyPoweredAndActive) {
		NanosaberItem nanosaber = (NanosaberItem) TRContent.NANOSABER;

		ItemStack inactiveUncharged = new ItemStack(nanosaber);
		inactiveUncharged.set(TRDataComponentTypes.IS_ACTIVE, false);

		ItemStack inactiveCharged = new ItemStack(TRContent.NANOSABER);
		nanosaber.setStoredEnergy(inactiveCharged, nanosaber.getEnergyCapacity(inactiveCharged));
		inactiveCharged.set(TRDataComponentTypes.IS_ACTIVE, false);

		ItemStack activeCharged = new ItemStack(TRContent.NANOSABER);
		nanosaber.setStoredEnergy(activeCharged, nanosaber.getEnergyCapacity(activeCharged));
		activeCharged.set(TRDataComponentTypes.IS_ACTIVE, false);

		if (before == null) {
			if (!onlyPoweredAndActive) {
				entries.accept(inactiveUncharged);
				entries.accept(inactiveCharged);
			}
			entries.accept(activeCharged);
		}
		else {
			if (!onlyPoweredAndActive) {
				entries.insertBefore(before, inactiveUncharged, inactiveCharged, activeCharged);
			}
			else {
				entries.insertBefore(before, activeCharged);
			}
		}
	}
}
