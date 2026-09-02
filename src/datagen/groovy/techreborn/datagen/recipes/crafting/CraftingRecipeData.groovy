/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TechReborn
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

package techreborn.datagen.recipes.crafting

import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluids
import techreborn.init.ModFluids
import techreborn.init.TRContent

/** Declarative definitions for recipes that do not require custom generation logic. */
class CraftingRecipeData {
	static void generate(CraftingRecipeFactory recipes) {
			crafting_table_armor(recipes)
			crafting_table_battery(recipes)
			crafting_table_cable(recipes)
			crafting_table_cell(recipes)
			crafting_table_dust(recipes)
			crafting_table_frequency_transmitter(recipes)
			crafting_table_ingot(recipes)
			crafting_table_machine(recipes)
			crafting_table_machine_block(recipes)
			crafting_table_manual(recipes)
			crafting_table_misc_block(recipes)
			crafting_table_paper(recipes)
			crafting_table_parts(recipes)
			crafting_table_solar_panel(recipes)
			crafting_table_storage_block(recipes)
			crafting_table_tool(recipes)
			crafting_table_unit(recipes)
			crafting_table_upgrade(recipes)
			crafting_table_uu_matter_calcite(recipes)
			crafting_table_uu_matter_misc(recipes)
			crafting_table_uu_matter_ore(recipes)
			crafting_table_uu_matter_sapling(recipes)
			crafting_table_uu_matter_wood(recipes)
			smelting(recipes)
	}

	private static void crafting_table_armor(CraftingRecipeFactory recipes) {
		recipes.shaped(TRContent.CLOAKING_DEVICE, 1, null, ['CIC', 'IOI', 'CIC'], ['C': TRContent.Ingots.CHROME.asTag(), 'I': TRContent.Plates.IRIDIUM_ALLOY, 'O': TRContent.LAPOTRONIC_ORB])
		recipes.shaped(TRContent.LAPOTRONIC_ORBPACK, 1, null, ['FOF', 'SPS', 'FIF'], ['F': TRContent.Parts.ENERGY_FLOW_CHIP, 'O': TRContent.LAPOTRONIC_ORB, 'S': TRContent.Parts.SUPERCONDUCTOR, 'P': TRContent.LITHIUM_ION_BATPACK, 'I': TRContent.Ingots.IRIDIUM.asTag()])
		recipes.shaped(TRContent.LITHIUM_ION_BATPACK, 1, null, ['BCB', 'BPB', 'B B'], ['C': TRContent.Parts.ADVANCED_CIRCUIT, 'P': TRContent.Plates.ALUMINUM.asTag(), 'B': TRContent.LITHIUM_ION_BATTERY])
		recipes.shaped(TRContent.NANO_BOOTS, 1, null, ['E E', 'C C'], ['C': TRContent.Plates.CARBON, 'E': TRContent.ENERGY_CRYSTAL])
		recipes.shaped(TRContent.NANO_CHESTPLATE, 1, null, ['C C', 'CEC', 'CAC'], ['C': TRContent.Plates.CARBON, 'A': TRContent.Parts.ADVANCED_CIRCUIT, 'E': TRContent.ENERGY_CRYSTAL])
		recipes.shaped(TRContent.NANO_HELMET, 1, null, ['CEC', 'C C'], ['C': TRContent.Plates.CARBON, 'E': TRContent.ENERGY_CRYSTAL])
		recipes.shaped(TRContent.NANO_LEGGINGS, 1, null, ['CAC', 'E E', 'C C'], ['C': TRContent.Plates.CARBON, 'A': TRContent.Parts.ADVANCED_CIRCUIT, 'E': TRContent.ENERGY_CRYSTAL])
		recipes.shaped(TRContent.QUANTUM_BOOTS, 1, null, ['L L', 'D D', 'S S'], ['S': TRContent.Parts.SUPERCONDUCTOR, 'L': TRContent.LAPOTRON_CRYSTAL, 'D': TRContent.Parts.DATA_STORAGE_CHIP])
		recipes.shaped(TRContent.QUANTUM_CHESTPLATE, 1, null, ['P P', 'SLS', 'DID'], ['P': TRContent.Plates.TUNGSTENSTEEL, 'S': TRContent.Parts.SUPERCONDUCTOR, 'L': TRContent.LAPOTRON_CRYSTAL, 'D': TRContent.Parts.DATA_STORAGE_CHIP, 'I': TRContent.NuclearReactorComponents.IRIDIUM_NEUTRON_REFLECTOR])
		recipes.shaped(TRContent.QUANTUM_HELMET, 1, null, ['DLD', 'S S'], ['S': TRContent.Parts.SUPERCONDUCTOR, 'L': TRContent.LAPOTRON_CRYSTAL, 'D': TRContent.Parts.DATA_STORAGE_CHIP])
		recipes.shaped(TRContent.QUANTUM_LEGGINGS, 1, null, ['DLD', 'S S', 'P P'], ['P': TRContent.Plates.TUNGSTENSTEEL, 'S': TRContent.Parts.SUPERCONDUCTOR, 'L': TRContent.LAPOTRON_CRYSTAL, 'D': TRContent.Parts.DATA_STORAGE_CHIP])
	}

	private static void crafting_table_battery(CraftingRecipeFactory recipes) {
		recipes.shaped(TRContent.LAPOTRON_CRYSTAL, 1, null, ['LCL', 'LEL', 'LCL'], ['L': TRContent.Plates.LAZURITE.asTag(), 'C': TRContent.Parts.INDUSTRIAL_CIRCUIT, 'E': TRContent.ENERGY_CRYSTAL])
		recipes.shaped(TRContent.LAPOTRONIC_ORB, 1, null, ['LLL', 'LPL', 'LLL'], ['L': TRContent.LAPOTRON_CRYSTAL, 'P': TRContent.Plates.IRIDIUM_ALLOY])
		recipes.shaped(TRContent.RED_CELL_BATTERY, 1, null, [' W ', 'TRT', 'TRT'], ['W': TRContent.Cables.INSULATED_COPPER, 'R': Items.REDSTONE, 'T': TRContent.Ingots.LEAD.asTag()])
	}

	private static void crafting_table_cable(CraftingRecipeFactory recipes) {
		recipes.shaped(TRContent.Cables.COPPER, 6, null, ['III'], ['I': Items.COPPER_INGOT])
		recipes.shaped(TRContent.Cables.GLASSFIBER, 3, 'glassfiber_cable', ['GGG', 'RUR', 'GGG'], ['G': Items.GLASS, 'R': Items.REDSTONE, 'U': TRContent.Dusts.RUBY.asTag()])
		recipes.shaped(TRContent.Cables.GLASSFIBER, 4, 'glassfiber_cable', ['GGG', 'RDR', 'GGG'], ['G': Items.GLASS, 'R': Items.REDSTONE, 'D': TRContent.Dusts.DIAMOND.asTag()])
		recipes.shaped(TRContent.Cables.GLASSFIBER, 6, 'glassfiber_cable', ['GGG', 'SDS', 'GGG'], ['G': Items.GLASS, 'S': TRContent.RawMetals.SILVER, 'D': TRContent.Dusts.DIAMOND.asTag()])
		recipes.shaped(TRContent.Cables.GLASSFIBER, 8, 'glassfiber_cable', ['GGG', 'EDE', 'GGG'], ['G': Items.GLASS, 'E': TRContent.Dusts.ELECTRUM.asTag(), 'D': TRContent.Dusts.DIAMOND.asTag()])
		recipes.shaped(TRContent.Cables.GOLD, 12, null, ['III'], ['I': Items.GOLD_INGOT])
		recipes.shaped(TRContent.Cables.HV, 12, null, ['III'], ['I': TRContent.Ingots.REFINED_IRON])
		recipes.shaped(TRContent.Cables.INSULATED_COPPER, 6, 'insulated_copper_cable', ['RRR', 'III', 'RRR'], ['I': Items.COPPER_INGOT, 'R': TRContent.Parts.RUBBER])
		recipes.shaped(TRContent.Cables.INSULATED_COPPER, 6, 'insulated_copper_cable', ['RIR', 'RIR', 'RIR'], ['I': Items.COPPER_INGOT, 'R': TRContent.Parts.RUBBER])
		recipes.shapeless(TRContent.Cables.INSULATED_COPPER, 1, 'insulated_copper_cable', [TRContent.Parts.RUBBER, TRContent.Cables.COPPER])
		recipes.shaped(TRContent.Cables.INSULATED_GOLD, 4, 'insulated_gold_cable', ['RRR', 'RIR', 'RRR'], ['I': Items.GOLD_INGOT, 'R': TRContent.Parts.RUBBER])
		recipes.shapeless(TRContent.Cables.INSULATED_GOLD, 1, 'insulated_gold_cable', [TRContent.Parts.RUBBER, TRContent.Parts.RUBBER, TRContent.Cables.GOLD])
		recipes.shaped(TRContent.Cables.INSULATED_HV, 4, 'insulated_hv_cable', ['RRR', 'RIR', 'RRR'], ['I': TRContent.Ingots.REFINED_IRON, 'R': TRContent.Parts.RUBBER])
		recipes.shapeless(TRContent.Cables.INSULATED_HV, 1, 'insulated_hv_cable', [TRContent.Parts.RUBBER, TRContent.Parts.RUBBER, TRContent.Cables.HV])
		recipes.shaped(TRContent.Cables.SUPERCONDUCTOR, 1, null, ['MFM', 'SSS', 'MFM'], ['M': TRContent.MachineBlocks.ADVANCED.frame, 'F': TRContent.Parts.ENERGY_FLOW_CHIP, 'S': TRContent.Parts.SUPERCONDUCTOR])
		recipes.shaped(TRContent.Cables.TIN, 9, null, ['III'], ['I': TRContent.Ingots.TIN.asTag()])
	}

	private static void crafting_table_cell(CraftingRecipeFactory recipes) {
		recipes.shaped(TRContent.Cells.EMPTY, 4, null, [' T ', 'T T', ' T '], ['T': TRContent.Ingots.TIN.asTag()])
	}

	private static void crafting_table_dust(CraftingRecipeFactory recipes) {
		recipes.shapeless(TRContent.Dusts.BASALT, 1, null, [TRContent.SmallDusts.COAL, TRContent.SmallDusts.OBSIDIAN, TRContent.SmallDusts.COAL, TRContent.SmallDusts.OBSIDIAN])
		recipes.shapeless(Items.GUNPOWDER, 2, null, [TRContent.Dusts.CHARCOAL, TRContent.Dusts.SULFUR, TRContent.Dusts.SALTPETER, TRContent.Dusts.SALTPETER])
		recipes.shapeless(TRContent.Dusts.MARBLE, 1, null, [TRContent.SmallDusts.OBSIDIAN, TRContent.Dusts.DIORITE, TRContent.Dusts.DIORITE, TRContent.Dusts.DIORITE])
	}

	private static void crafting_table_frequency_transmitter(CraftingRecipeFactory recipes) {
		recipes.shapeless(TRContent.FREQUENCY_TRANSMITTER, 1, null, [TRContent.Parts.ELECTRONIC_CIRCUIT, TRContent.Cables.INSULATED_GOLD])
	}

	private static void crafting_table_ingot(CraftingRecipeFactory recipes) {
		recipes.shaped(TRContent.Ingots.INDUSTRIAL_ALLOY, 1, null, ['SCS', 'IMI', 'SCS'], ['C': TRContent.Ingots.CHROME.asTag(), 'I': TRContent.Ingots.INVAR.asTag(), 'M': TRContent.SmallDusts.MANGANESE.asTag(), 'S': TRContent.Plates.STEEL.asTag()])
		recipes.shaped(TRContent.Ingots.IRIDIUM_ALLOY, 1, null, ['IAI', 'ADA', 'IAI'], ['A': TRContent.Plates.ADVANCED_ALLOY, 'D': TRContent.Dusts.DIAMOND.asTag(), 'I': TRContent.Plates.IRIDIUM.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 2, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.TIN.asTag(), 'T': TRContent.Ingots.REFINED_IRON.asTag(), 'M': TRContent.Ingots.BRONZE.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 2, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.TIN.asTag(), 'T': TRContent.Ingots.REFINED_IRON.asTag(), 'M': TRContent.Ingots.BRASS.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 5, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ALUMINUM.asTag(), 'T': TRContent.Ingots.INVAR.asTag(), 'M': TRContent.Ingots.BRONZE.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 4, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ALUMINUM.asTag(), 'T': TRContent.Ingots.NICKEL.asTag(), 'M': TRContent.Ingots.BRASS.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 4, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ALUMINUM.asTag(), 'T': TRContent.Ingots.NICKEL.asTag(), 'M': TRContent.Ingots.BRONZE.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 6, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ALUMINUM.asTag(), 'T': TRContent.Ingots.TITANIUM.asTag(), 'M': TRContent.Ingots.BRASS.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 6, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ALUMINUM.asTag(), 'T': TRContent.Ingots.TITANIUM.asTag(), 'M': TRContent.Ingots.BRONZE.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 6, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ALUMINUM.asTag(), 'T': TRContent.Ingots.TUNGSTEN.asTag(), 'M': TRContent.Ingots.BRASS.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 6, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ALUMINUM.asTag(), 'T': TRContent.Ingots.TUNGSTEN.asTag(), 'M': TRContent.Ingots.BRONZE.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 9, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ALUMINUM.asTag(), 'T': TRContent.Ingots.TUNGSTENSTEEL.asTag(), 'M': TRContent.Ingots.BRASS.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 9, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ALUMINUM.asTag(), 'T': TRContent.Ingots.TUNGSTENSTEEL.asTag(), 'M': TRContent.Ingots.BRONZE.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 5, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ALUMINUM.asTag(), 'T': TRContent.Ingots.INVAR.asTag(), 'M': TRContent.Ingots.BRASS.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 5, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.TIN.asTag(), 'T': TRContent.Ingots.TITANIUM.asTag(), 'M': TRContent.Ingots.BRONZE.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 4, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ZINC.asTag(), 'T': TRContent.Ingots.INVAR.asTag(), 'M': TRContent.Ingots.BRONZE.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 4, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.TIN.asTag(), 'T': TRContent.Ingots.INVAR.asTag(), 'M': TRContent.Ingots.BRASS.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 4, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.TIN.asTag(), 'T': TRContent.Ingots.INVAR.asTag(), 'M': TRContent.Ingots.BRONZE.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 3, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.TIN.asTag(), 'T': TRContent.Ingots.NICKEL.asTag(), 'M': TRContent.Ingots.BRASS.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 3, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.TIN.asTag(), 'T': TRContent.Ingots.NICKEL.asTag(), 'M': TRContent.Ingots.BRONZE.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 5, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.TIN.asTag(), 'T': TRContent.Ingots.TITANIUM.asTag(), 'M': TRContent.Ingots.BRASS.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 5, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.TIN.asTag(), 'T': TRContent.Ingots.TUNGSTEN.asTag(), 'M': TRContent.Ingots.BRASS.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 5, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.TIN.asTag(), 'T': TRContent.Ingots.TUNGSTEN.asTag(), 'M': TRContent.Ingots.BRONZE.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 8, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.TIN.asTag(), 'T': TRContent.Ingots.TUNGSTENSTEEL.asTag(), 'M': TRContent.Ingots.BRASS.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 8, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.TIN.asTag(), 'T': TRContent.Ingots.TUNGSTENSTEEL.asTag(), 'M': TRContent.Ingots.BRONZE.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 4, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ZINC.asTag(), 'T': TRContent.Ingots.INVAR.asTag(), 'M': TRContent.Ingots.BRASS.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 2, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ZINC.asTag(), 'T': TRContent.Ingots.REFINED_IRON.asTag(), 'M': TRContent.Ingots.BRASS.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 2, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ZINC.asTag(), 'T': TRContent.Ingots.REFINED_IRON.asTag(), 'M': TRContent.Ingots.BRONZE.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 3, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ZINC.asTag(), 'T': TRContent.Ingots.NICKEL.asTag(), 'M': TRContent.Ingots.BRASS.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 3, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ZINC.asTag(), 'T': TRContent.Ingots.NICKEL.asTag(), 'M': TRContent.Ingots.BRONZE.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 5, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ZINC.asTag(), 'T': TRContent.Ingots.TITANIUM.asTag(), 'M': TRContent.Ingots.BRASS.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 5, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ZINC.asTag(), 'T': TRContent.Ingots.TITANIUM.asTag(), 'M': TRContent.Ingots.BRONZE.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 5, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ZINC.asTag(), 'T': TRContent.Ingots.TUNGSTEN.asTag(), 'M': TRContent.Ingots.BRASS.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 5, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ZINC.asTag(), 'T': TRContent.Ingots.TUNGSTEN.asTag(), 'M': TRContent.Ingots.BRONZE.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 8, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ZINC.asTag(), 'T': TRContent.Ingots.TUNGSTENSTEEL.asTag(), 'M': TRContent.Ingots.BRASS.asTag()])
		recipes.shaped(TRContent.Ingots.MIXED_METAL, 8, 'mixed_metal_ingot', ['TTT', 'MMM', 'BBB'], ['B': TRContent.Ingots.ZINC.asTag(), 'T': TRContent.Ingots.TUNGSTENSTEEL.asTag(), 'M': TRContent.Ingots.BRONZE.asTag()])
	}

	private static void crafting_table_machine(CraftingRecipeFactory recipes) {
		recipes.shaped(TRContent.Machine.ADJUSTABLE_SU, 1, null, ['LLL', 'LCL', 'LLL'], ['C': TRContent.ENERGY_CRYSTAL, 'L': TRContent.LAPOTRONIC_ORB])
		recipes.shaped(TRContent.Machine.ALARM, 1, null, ['ICI', 'SRS', 'ICI'], ['R': Items.REDSTONE_BLOCK, 'C': TRContent.Cables.COPPER, 'S': TRContent.Cables.INSULATED_COPPER, 'I': TRContent.Ingots.REFINED_IRON])
		recipes.shaped(TRContent.Machine.ALLOY_SMELTER, 1, null, [' C ', 'FMF', '   '], ['C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'F': TRContent.Machine.ELECTRIC_FURNACE, 'M': TRContent.Machine.IRON_ALLOY_FURNACE])
		recipes.shaped(TRContent.Machine.ASSEMBLY_MACHINE, 1, null, ['EDE', 'CFC', 'EPE'], ['C': TRContent.Parts.ADVANCED_CIRCUIT, 'F': TRContent.MachineBlocks.ADVANCED.frame, 'E': TRContent.Plates.ELECTRUM.asTag(), 'P': Items.PISTON, 'D': TRContent.Parts.DIGITAL_DISPLAY])
		recipes.shaped(TRContent.Machine.AUTO_CRAFTING_TABLE, 1, null, ['MPM', 'PCP', 'MPM'], ['P': TRContent.Plates.IRON.asTag(), 'C': Items.CRAFTER, 'M': TRContent.Parts.ADVANCED_CIRCUIT])
		recipes.shaped(TRContent.Machine.BLOCK_BREAKER, 1, null, ['PCP', 'PHP', 'PXP'], ['P': TRContent.Plates.REFINED_IRON, 'C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'H': Items.HOPPER, 'X': Items.STICKY_PISTON])
		recipes.shaped(TRContent.Machine.BLOCK_PLACER, 1, null, ['PCP', 'PHP', 'PXP'], ['P': TRContent.Plates.REFINED_IRON, 'C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'H': Items.HOPPER, 'X': Items.PISTON])
		recipes.shaped(TRContent.Machine.SOLID_CANNING_MACHINE, 1, null, ['TCT', 'TBT', 'TTT'], ['T': TRContent.Ingots.TIN.asTag(), 'C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'B': TRContent.MachineBlocks.BASIC.frame])
		recipes.shaped(TRContent.Machine.CHARGE_O_MAT, 1, null, ['ETE', 'COC', 'EAE'], ['A': TRContent.MachineBlocks.ADVANCED.frame, 'C': Items.CHEST, 'T': TRContent.ENERGY_CRYSTAL, 'E': TRContent.Parts.ENERGY_FLOW_CHIP, 'O': TRContent.LAPOTRONIC_ORB])
		recipes.shaped(TRContent.Machine.CHEMICAL_REACTOR, 1, null, ['IEI', 'CPC', 'IEI'], ['P': TRContent.Machine.COMPRESSOR, 'C': TRContent.Parts.ADVANCED_CIRCUIT, 'E': TRContent.Machine.EXTRACTOR, 'I': TRContent.Plates.INVAR.asTag()])
		recipes.shaped(TRContent.Machine.CHUNK_LOADER, 1, null, ['BCG', 'LFN', 'OQR'], ['B': TRContent.Plates.BRONZE.asTag(), 'C': TRContent.Plates.COAL.asTag(), 'G': TRContent.Plates.GOLD.asTag(), 'L': TRContent.Plates.LAPIS.asTag(), 'F': TRContent.MachineBlocks.INDUSTRIAL.frame, 'N': TRContent.Plates.NICKEL.asTag(), 'O': TRContent.Plates.OBSIDIAN.asTag(), 'Q': TRContent.Plates.QUARTZ.asTag(), 'R': TRContent.Plates.REDSTONE.asTag()])
		recipes.shaped(TRContent.Machine.COMPRESSOR, 1, null, ['S S', 'SCS', 'SMS'], ['C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'S': Items.STONE, 'M': TRContent.MachineBlocks.BASIC.frame])
		recipes.shaped(TRContent.Machine.DIESEL_GENERATOR, 1, null, ['III', 'I I', 'CGC'], ['C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'G': TRContent.Machine.SOLID_FUEL_GENERATOR, 'I': TRContent.Ingots.REFINED_IRON])
		recipes.shaped(TRContent.Machine.DIESEL_GENERATOR, 1, null, ['III', 'I I', 'CGC'], ['C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'G': TRContent.Machine.SOLID_FUEL_GENERATOR, 'I': TRContent.Plates.ALUMINUM.asTag()])
		recipes.shaped(TRContent.Machine.DISTILLATION_TOWER, 1, null, ['CMC', 'PBP', 'EME'], ['P': TRContent.Machine.EXTRACTOR, 'B': TRContent.MachineBlocks.INDUSTRIAL.frame, 'C': TRContent.Machine.INDUSTRIAL_CENTRIFUGE, 'E': TRContent.Machine.INDUSTRIAL_ELECTROLYZER, 'M': TRContent.Parts.ENERGY_FLOW_CHIP])
		recipes.shaped(TRContent.Machine.DRAGON_EGG_SYPHON, 1, null, ['CTC', 'PSP', 'CBC'], ['P': TRContent.Plates.IRIDIUM_ALLOY, 'B': TRContent.LAPOTRONIC_ORB, 'C': TRContent.Parts.ENERGY_FLOW_CHIP, 'S': TRContent.Parts.SUPERCONDUCTOR, 'T': TRContent.Machine.MEDIUM_VOLTAGE_SU])
		recipes.shaped(TRContent.Machine.DRAIN, 1, null, ['PXP', 'PHP', 'PBP'], ['P': TRContent.Plates.REFINED_IRON, 'B': Items.BUCKET, 'H': Items.HOPPER, 'X': Items.IRON_BARS])
		recipes.shaped(TRContent.Machine.ELECTRIC_FURNACE, 1, null, [' C ', 'RFR', '   '], ['R': Items.REDSTONE, 'C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'F': TRContent.Machine.IRON_FURNACE])
		recipes.shaped(TRContent.Machine.ELEVATOR, 1, null, ['EEE', 'YFY', 'PCP'], ['C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'E': TRContent.Dusts.ENDER_PEARL.asTag(), 'F': TRContent.MachineBlocks.BASIC.frame, 'P': TRContent.Plates.ZINC.asTag(), 'Y': TRContent.Plates.YELLOW_GARNET.asTag()])
		recipes.shaped(TRContent.Machine.EV_TRANSFORMER, 1, null, ['C', 'H', 'C'], ['C': TRContent.Cables.SUPERCONDUCTOR, 'H': TRContent.Machine.HV_TRANSFORMER])
		recipes.shaped(TRContent.Machine.EXTRACTOR, 1, null, ['TMT', 'TCT'], ['C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'T': TRContent.TREE_TAP, 'M': TRContent.MachineBlocks.BASIC.frame])
		recipes.shaped(TRContent.Machine.FISHING_STATION, 1, null, ['SCS', 'TFT', 'PSP'], ['C': TRContent.Parts.ADVANCED_CIRCUIT, 'F': TRContent.MachineBlocks.ADVANCED.frame, 'P': TRContent.Plates.SAPPHIRE.asTag(), 'S': Items.STRING, 'T': Items.STICK])
		recipes.shaped(TRContent.Machine.FLUID_REPLICATOR, 1, null, ['PCP', 'CFC', 'ESR'], ['P': TRContent.Plates.TUNGSTENSTEEL, 'R': TRContent.Machine.CHEMICAL_REACTOR, 'C': TRContent.Parts.ENERGY_FLOW_CHIP, 'S': TRContent.Parts.SUPERCONDUCTOR, 'E': TRContent.Machine.INDUSTRIAL_ELECTROLYZER, 'F': TRContent.MachineBlocks.INDUSTRIAL.frame])
		recipes.shaped(TRContent.Machine.FUSION_COIL, 1, null, ['CSC', 'NAN', 'CRC'], ['A': TRContent.MachineBlocks.ADVANCED.casing, 'R': TRContent.NuclearReactorComponents.IRIDIUM_NEUTRON_REFLECTOR, 'C': TRContent.Parts.ENERGY_FLOW_CHIP, 'S': TRContent.Parts.SUPERCONDUCTOR, 'N': TRContent.Parts.NICHROME_HEATING_COIL])
		recipes.shaped(TRContent.Machine.FUSION_CONTROL_COMPUTER, 1, null, ['CCC', 'PTP', 'CCC'], ['P': TRContent.ENERGY_CRYSTAL, 'C': TRContent.Parts.ENERGY_FLOW_CHIP, 'T': TRContent.Machine.FUSION_COIL])
		recipes.shaped(TRContent.Machine.GAS_TURBINE, 1, 'gas_turbine', ['IAI', 'WGW', 'IAI'], ['A': TRContent.Parts.ADVANCED_CIRCUIT, 'W': TRContent.Machine.WIND_MILL, 'G': TRContent.REINFORCED_GLASS, 'I': TRContent.Plates.INVAR])
		recipes.shaped(TRContent.Machine.GAS_TURBINE, 1, 'gas_turbine', ['IAI', 'WGW', 'IAI'], ['A': TRContent.Parts.ADVANCED_CIRCUIT, 'W': TRContent.Machine.WIND_MILL, 'G': TRContent.REINFORCED_GLASS, 'I': TRContent.Plates.ALUMINUM.asTag()])
		recipes.shaped(TRContent.Machine.GREENHOUSE_CONTROLLER, 1, null, ['PAP', 'HSH', 'ACA'], ['P': TRContent.Ingots.REFINED_IRON, 'A': TRContent.Parts.ADVANCED_CIRCUIT, 'S': TRContent.Parts.DIAMOND_SAW_BLADE, 'C': TRContent.MachineBlocks.ADVANCED.frame, 'H': Items.IRON_HOE])
		recipes.shaped(TRContent.Machine.GRINDER, 1, null, ['FFF', 'SMS', ' C '], ['F': Items.FLINT, 'S': Items.COBBLESTONE, 'C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'M': TRContent.MachineBlocks.BASIC.frame])
		recipes.shaped(TRContent.Machine.HIGH_VOLTAGE_SU, 1, null, ['LAL', 'LML', 'LOL'], ['A': TRContent.Parts.ADVANCED_CIRCUIT, 'L': TRContent.LAPOTRON_CRYSTAL, 'M': TRContent.Machine.MEDIUM_VOLTAGE_SU, 'O': TRContent.MachineBlocks.ADVANCED.frame])
		recipes.shaped(TRContent.Machine.HV_TRANSFORMER, 1, null, ['H', 'M', 'H'], ['H': TRContent.Cables.INSULATED_HV, 'M': TRContent.Machine.MV_TRANSFORMER])
		recipes.shaped(TRContent.Machine.IMPLOSION_COMPRESSOR, 1, null, ['ABA', 'CPC', 'ABA'], ['P': TRContent.Machine.COMPRESSOR, 'A': TRContent.Ingots.ADVANCED_ALLOY, 'B': TRContent.MachineBlocks.ADVANCED.frame, 'C': TRContent.Parts.ADVANCED_CIRCUIT])
		recipes.shaped(TRContent.Machine.INDUSTRIAL_BLAST_FURNACE, 1, null, ['CHC', 'HBH', 'FHF'], ['B': TRContent.MachineBlocks.ADVANCED.frame, 'C': TRContent.Parts.ADVANCED_CIRCUIT, 'F': TRContent.Machine.ELECTRIC_FURNACE, 'H': TRContent.Parts.CUPRONICKEL_HEATING_COIL])
		recipes.shaped(TRContent.Machine.INDUSTRIAL_CENTRIFUGE, 1, 'industrial_centrifuge', ['RCR', 'AEA', 'RCR'], ['A': TRContent.MachineBlocks.ADVANCED.frame, 'R': TRContent.Ingots.REFINED_IRON, 'C': TRContent.Parts.ADVANCED_CIRCUIT, 'E': TRContent.Machine.EXTRACTOR])
		recipes.shaped(TRContent.Machine.INDUSTRIAL_CENTRIFUGE, 1, 'industrial_centrifuge', ['RCR', 'AEA', 'RCR'], ['A': TRContent.MachineBlocks.ADVANCED.frame, 'R': TRContent.Plates.ALUMINUM.asTag(), 'C': TRContent.Parts.ADVANCED_CIRCUIT, 'E': TRContent.Machine.EXTRACTOR])
		recipes.shaped(TRContent.Machine.INDUSTRIAL_ELECTROLYZER, 1, null, ['RER', 'CFC', 'RER'], ['R': TRContent.Plates.IRON.asTag(), 'C': TRContent.Parts.ADVANCED_CIRCUIT, 'E': TRContent.Machine.EXTRACTOR, 'F': TRContent.MachineBlocks.ADVANCED.frame])
		recipes.shaped(TRContent.Machine.INDUSTRIAL_GRINDER, 1, null, ['ECG', 'HHH', 'CBC'], ['B': TRContent.MachineBlocks.ADVANCED.frame, 'C': TRContent.Parts.ADVANCED_CIRCUIT, 'E': TRContent.Machine.INDUSTRIAL_ELECTROLYZER, 'G': TRContent.Machine.GRINDER, 'H': TRContent.Parts.DIAMOND_GRINDING_HEAD])
		recipes.shaped(TRContent.Machine.INDUSTRIAL_SAWMILL, 1, null, ['PAP', 'SSS', 'ACA'], ['P': TRContent.Ingots.REFINED_IRON, 'A': TRContent.Parts.ADVANCED_CIRCUIT, 'S': TRContent.Parts.DIAMOND_SAW_BLADE, 'C': TRContent.MachineBlocks.ADVANCED.frame])
		recipes.shaped(TRContent.Machine.INTERDIMENSIONAL_SU, 1, null, ['PAP', 'ACA', 'PAP'], ['P': TRContent.Plates.IRIDIUM_ALLOY, 'A': TRContent.Machine.ADJUSTABLE_SU, 'C': Items.ENDER_CHEST])
		recipes.shaped(TRContent.Machine.IRON_ALLOY_FURNACE, 1, null, ['III', 'F F', 'III'], ['F': TRContent.Machine.IRON_FURNACE, 'I': TRContent.Ingots.REFINED_IRON])
		recipes.shaped(TRContent.Machine.IRON_FURNACE, 1, 'iron_furnace', ['III', 'I I', 'III'], ['I': Items.IRON_INGOT])
		recipes.shaped(TRContent.Machine.IRON_FURNACE, 1, 'iron_furnace', [' I ', 'I I', 'IFI'], ['F': Items.FURNACE, 'I': Items.IRON_INGOT])
		recipes.shaped(TRContent.Machine.LAMP_INCANDESCENT, 1, null, ['GGG', 'TCT', 'GGG'], ['C': TRContent.Parts.CARBON_FIBER, 'T': TRContent.Cables.COPPER, 'G': Items.GLASS_PANE])
		recipes.shaped(TRContent.Machine.LAMP_LED, 1, null, ['GGG', 'TLT', 'GGG'], ['T': TRContent.Cables.TIN, 'G': Items.GLASS_PANE, 'L': Items.GLOWSTONE_DUST])
		recipes.shaped(TRContent.Machine.LAPOTRONIC_SU, 1, null, [' L ', 'CBC', ' M '], ['B': TRContent.Machine.LSU_STORAGE, 'C': TRContent.Parts.ADVANCED_CIRCUIT, 'L': TRContent.Machine.LV_TRANSFORMER, 'M': TRContent.Machine.MV_TRANSFORMER])
		recipes.shaped(TRContent.Machine.LAUNCHPAD, 1, null, ['MMM', 'CPC', 'ZFZ'], ['C': TRContent.Parts.ADVANCED_CIRCUIT, 'F': TRContent.MachineBlocks.ADVANCED.frame, 'M': TRContent.Plates.MAGNALIUM.asTag(), 'P': Items.PISTON, 'Z': TRContent.Plates.ZINC.asTag()])
		recipes.shaped(TRContent.Machine.LIGHTNING_ROD, 1, null, ['CAC', 'ACA', 'CAC'], ['A': TRContent.MachineBlocks.ADVANCED.casing, 'C': TRContent.Parts.ENERGY_FLOW_CHIP])
		recipes.shaped(TRContent.Machine.LOW_VOLTAGE_SU, 1, null, ['WCW', 'BBB', 'WWW'], ['B': TRContent.RED_CELL_BATTERY, 'C': TRContent.Cables.INSULATED_COPPER, 'W': ItemTags.PLANKS])
		recipes.shaped(TRContent.Machine.LSU_STORAGE, 1, null, ['LSL', 'LCL', 'LSL'], ['C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'L': Items.LAPIS_BLOCK, 'S': TRContent.Plates.SILVER.asTag()])
		recipes.shaped(TRContent.Machine.LV_TRANSFORMER, 1, null, ['PWP', 'CCC', 'PPP'], ['P': ItemTags.PLANKS, 'C': Items.COPPER_INGOT, 'W': TRContent.Cables.INSULATED_COPPER])
		recipes.shaped(TRContent.Machine.MATTER_FABRICATOR, 1, null, ['ETE', 'AOA', 'ETE'], ['A': TRContent.MachineBlocks.INDUSTRIAL.frame, 'T': TRContent.Machine.EXTRACTOR, 'E': TRContent.Parts.ENERGY_FLOW_CHIP, 'O': TRContent.LAPOTRONIC_ORB])
		recipes.shaped(TRContent.Machine.MEDIUM_VOLTAGE_SU, 1, null, ['GEG', 'EME', 'GEG'], ['E': TRContent.ENERGY_CRYSTAL, 'G': TRContent.Cables.INSULATED_GOLD, 'M': TRContent.MachineBlocks.BASIC.frame])
		recipes.shaped(TRContent.Machine.MV_TRANSFORMER, 1, null, ['G', 'M', 'G'], ['G': TRContent.Cables.INSULATED_GOLD, 'M': TRContent.MachineBlocks.BASIC.frame])
		recipes.shaped(TRContent.Machine.NUCLEAR_REACTOR, 1, null, ['PCP', 'RRR', 'PGP'], ['C': TRContent.Parts.ADVANCED_CIRCUIT, 'G': TRContent.Machine.SOLID_FUEL_GENERATOR, 'P': TRContent.Plates.INDUSTRIAL_ALLOY, 'R': TRContent.Machine.REACTOR_CHAMBER])
		recipes.shaped(TRContent.Machine.PLASMA_GENERATOR, 1, null, ['PPP', 'PTP', 'CGC'], ['P': TRContent.Plates.TUNGSTENSTEEL, 'C': TRContent.Parts.ENERGY_FLOW_CHIP, 'T': TRContent.Machine.HV_TRANSFORMER, 'G': TRContent.Machine.SOLID_FUEL_GENERATOR])
		recipes.shaped(TRContent.Machine.PLAYER_DETECTOR, 1, null, [' D ', 'CFC', ' D '], ['C': TRContent.Parts.ADVANCED_CIRCUIT, 'D': TRContent.Parts.DATA_STORAGE_CORE, 'F': TRContent.COMPUTER_CUBE])
		recipes.shaped(TRContent.Machine.PUMP, 1, null, ['PFP', 'PTP', 'DXD'], ['F': TRContent.MachineBlocks.ADVANCED.frame, 'P': TRContent.Plates.REFINED_IRON, 'T': TRContent.TankUnit.BASIC, 'D': TRContent.Machine.DRAIN, 'X': Items.IRON_BARS])
		recipes.shaped(TRContent.Machine.REACTOR_CHAMBER, 1, null, [' P ', 'PFP', ' P '], ['F': TRContent.MachineBlocks.INDUSTRIAL.frame, 'P': TRContent.Plates.INDUSTRIAL_ALLOY])
		recipes.shaped(TRContent.Machine.RECYCLER, 1, null, [' E ', 'DCD', 'GDG'], ['C': TRContent.Machine.COMPRESSOR, 'D': Items.DIRT, 'E': TRContent.Parts.ELECTRONIC_CIRCUIT, 'G': Items.GLOWSTONE_DUST])
		recipes.shaped(TRContent.Machine.RESIN_BASIN, 1, null, ['WTW', 'WDW', 'WBW'], ['T': TRContent.TREE_TAP, 'W': TRContent.RUBBER_PLANKS, 'D': TRContent.Machine.DRAIN, 'B': TRContent.RUBBER_TRAPDOOR])
		recipes.shaped(TRContent.Machine.ROLLING_MACHINE, 1, null, ['PCP', 'MBM', 'PCP'], ['P': Items.PISTON, 'B': TRContent.MachineBlocks.BASIC.frame, 'C': TRContent.Parts.ADVANCED_CIRCUIT, 'M': TRContent.Machine.COMPRESSOR])
		recipes.shaped(TRContent.Machine.SCRAPBOXINATOR, 1, null, ['ICI', 'DSD', 'ICI'], ['S': TRContent.SCRAP_BOX, 'C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'D': Items.DIRT, 'I': TRContent.Plates.IRON.asTag()])
		recipes.shaped(TRContent.Machine.SEMI_FLUID_GENERATOR, 1, 'semi_fluid_generator', ['III', 'IHI', 'CGC'], ['C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'G': TRContent.Machine.SOLID_FUEL_GENERATOR, 'H': TRContent.REINFORCED_GLASS, 'I': TRContent.Plates.IRON.asTag()])
		recipes.shaped(TRContent.Machine.SEMI_FLUID_GENERATOR, 1, 'semi_fluid_generator', ['III', 'IHI', 'CGC'], ['C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'G': TRContent.Machine.SOLID_FUEL_GENERATOR, 'H': TRContent.REINFORCED_GLASS, 'I': TRContent.Plates.ALUMINUM.asTag()])
		recipes.shapeless(TRContent.Machine.SOLID_FUEL_GENERATOR, 1, null, [TRContent.RED_CELL_BATTERY, TRContent.MachineBlocks.BASIC.frame, Items.FURNACE])
		recipes.shaped(TRContent.Machine.THERMAL_GENERATOR, 1, null, ['III', 'IRI', 'CGC'], ['R': TRContent.REINFORCED_GLASS, 'C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'G': TRContent.Machine.SOLID_FUEL_GENERATOR, 'I': TRContent.Plates.INVAR])
		recipes.shaped(TRContent.Machine.VACUUM_FREEZER, 1, null, ['SPS', 'CGC', 'SPS'], ['P': TRContent.Machine.EXTRACTOR, 'S': TRContent.Plates.STEEL.asTag(), 'C': TRContent.Parts.ADVANCED_CIRCUIT, 'G': TRContent.REINFORCED_GLASS])
		recipes.shaped(TRContent.Machine.WATER_MILL, 1, null, ['SWS', 'WGW', 'SWS'], ['S': Items.STICK, 'W': ItemTags.PLANKS, 'G': TRContent.Machine.SOLID_FUEL_GENERATOR])
		recipes.shaped(TRContent.Machine.WIND_MILL, 1, 'wind_mill', ['I', 'G', 'I'], ['G': TRContent.Machine.SOLID_FUEL_GENERATOR, 'I': TRContent.Plates.MAGNALIUM])
		recipes.shaped(TRContent.Machine.WIND_MILL, 1, 'wind_mill', ['IGI'], ['G': TRContent.Machine.SOLID_FUEL_GENERATOR, 'I': TRContent.Plates.MAGNALIUM])
		recipes.shaped(TRContent.Machine.WIRE_MILL, 1, null, ['PEP', 'CMC', 'PGP'], ['P': TRContent.Plates.BRASS.asTag(), 'E': TRContent.Machine.EXTRACTOR, 'C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'M': TRContent.MachineBlocks.BASIC.frame, 'G': Items.PISTON])
	}

	private static void crafting_table_machine_block(CraftingRecipeFactory recipes) {
		recipes.shaped(TRContent.MachineBlocks.ADVANCED.casing, 4, 'advanced_machine_casing', ['RRR', 'CAC', 'RRR'], ['A': TRContent.MachineBlocks.ADVANCED.frame, 'R': TRContent.Plates.STEEL.asTag(), 'C': TRContent.Parts.ADVANCED_CIRCUIT])
		recipes.shaped(TRContent.MachineBlocks.ADVANCED.casing, 1, 'advanced_machine_casing', ['RRR', 'CAC', 'RRR'], ['A': TRContent.MachineBlocks.BASIC.casing, 'R': TRContent.Plates.STEEL.asTag(), 'C': TRContent.Parts.ADVANCED_CIRCUIT])
		recipes.shaped(TRContent.MachineBlocks.ADVANCED.frame, 1, null, [' C ', 'AMA', ' C '], ['A': TRContent.Plates.ADVANCED_ALLOY, 'C': TRContent.Plates.CARBON, 'M': TRContent.MachineBlocks.BASIC.frame])
		recipes.shaped(TRContent.MachineBlocks.BASIC.casing, 4, 'basic_machine_casing', ['RRR', 'CAC', 'RRR'], ['A': TRContent.MachineBlocks.BASIC.frame, 'R': TRContent.Ingots.REFINED_IRON.asTag(), 'C': TRContent.Parts.ELECTRONIC_CIRCUIT])
		recipes.shaped(TRContent.MachineBlocks.BASIC.casing, 4, 'basic_machine_casing', ['RRR', 'CAC', 'RRR'], ['A': TRContent.MachineBlocks.BASIC.frame, 'R': TRContent.Plates.IRON.asTag(), 'C': TRContent.Parts.ELECTRONIC_CIRCUIT])
		recipes.shaped(TRContent.MachineBlocks.BASIC.casing, 4, 'basic_machine_casing', ['RRR', 'CAC', 'RRR'], ['A': TRContent.MachineBlocks.BASIC.frame, 'R': TRContent.Plates.ALUMINUM.asTag(), 'C': TRContent.Parts.ELECTRONIC_CIRCUIT])
		recipes.shaped(TRContent.MachineBlocks.BASIC.frame, 1, 'basic_machine_frame', ['AAA', 'A A', 'AAA'], ['A': TRContent.Ingots.REFINED_IRON.asTag()])
		recipes.shaped(TRContent.MachineBlocks.BASIC.frame, 9, 'basic_machine_frame', ['AAA', 'A A', 'AAA'], ['A': TRContent.StorageBlocks.REFINED_IRON])
		recipes.shaped(TRContent.MachineBlocks.INDUSTRIAL.casing, 4, 'industrial_machine_casing', ['RRR', 'CAC', 'RRR'], ['A': TRContent.MachineBlocks.INDUSTRIAL.frame, 'R': TRContent.Plates.CHROME.asTag(), 'C': TRContent.Parts.DATA_STORAGE_CORE])
		recipes.shaped(TRContent.MachineBlocks.INDUSTRIAL.casing, 1, 'industrial_machine_casing', ['RRR', 'CAC', 'RRR'], ['A': TRContent.MachineBlocks.ADVANCED.casing, 'R': TRContent.Plates.CHROME.asTag(), 'C': TRContent.Parts.DATA_STORAGE_CORE])
		recipes.shaped(TRContent.MachineBlocks.INDUSTRIAL.frame, 1, null, ['CTC', 'TBT', 'CTC'], ['B': TRContent.MachineBlocks.ADVANCED.frame, 'C': TRContent.Plates.CHROME.asTag(), 'T': TRContent.Plates.TITANIUM.asTag()])
	}

	private static void crafting_table_manual(CraftingRecipeFactory recipes) {
		recipes.shapeless(TRContent.MANUAL, 1, null, [Items.BOOK, TRContent.Ingots.REFINED_IRON])
	}

	private static void crafting_table_misc_block(CraftingRecipeFactory recipes) {
		recipes.shaped(TRContent.COMPUTER_CUBE, 1, null, ['OMC', 'MFM', 'CMO'], ['C': TRContent.Parts.ENERGY_FLOW_CHIP, 'F': TRContent.MachineBlocks.ADVANCED.frame, 'M': TRContent.Parts.DIGITAL_DISPLAY, 'O': TRContent.Parts.DATA_STORAGE_CHIP])
		recipes.shapeless(Items.GRAVEL, 4, 'gravel', [TRContent.Dusts.ANDESITE.asTag(), TRContent.Dusts.DIORITE.asTag(), TRContent.Dusts.GRANITE.asTag(), Items.FLINT])
		recipes.shapeless(Items.RED_SAND, 1, 'red_sand', [TRContent.Dusts.NETHERRACK.asTag(), Items.SAND])
		recipes.shaped(TRContent.REFINED_IRON_FENCE, 1, null, ['RRR', 'RRR', ' R '], ['R': TRContent.Ingots.REFINED_IRON])
		recipes.shaped(TRContent.REINFORCED_GLASS, 7, 'reinforced_glass', ['GAG', 'GGG', 'GAG'], ['A': TRContent.Plates.LEAD.asTag(), 'G': Items.GLASS])
		recipes.shaped(TRContent.REINFORCED_GLASS, 7, 'reinforced_glass', ['GGG', 'AGA', 'GGG'], ['A': TRContent.Plates.LEAD.asTag(), 'G': Items.GLASS])
		recipes.shapeless(TRContent.RUBBER_BUTTON, 1, 'wooden_button', [TRContent.RUBBER_PLANKS])
		recipes.shaped(TRContent.RUBBER_DOOR, 3, 'wooden_door', ['##', '##', '##'], ['#': TRContent.RUBBER_PLANKS])
		recipes.shaped(TRContent.RUBBER_FENCE, 3, 'wooden_fence', ['W#W', 'W#W'], ['#': Items.STICK, 'W': TRContent.RUBBER_PLANKS])
		recipes.shaped(TRContent.RUBBER_FENCE_GATE, 1, 'wooden_fence_gate', ['#W#', '#W#'], ['#': Items.STICK, 'W': TRContent.RUBBER_PLANKS])
		recipes.shapeless(TRContent.RUBBER_PLANKS, 4, 'planks', [TRContent.ItemTags.RUBBER_LOGS])
		recipes.shaped(TRContent.RUBBER_PRESSURE_PLATE, 1, 'wooden_pressure_plate', ['##'], ['#': TRContent.RUBBER_PLANKS])
		recipes.shaped(TRContent.RUBBER_SLAB, 6, null, ['WWW'], ['W': TRContent.RUBBER_PLANKS])
		recipes.shaped(TRContent.RUBBER_STAIR, 4, null, ['W  ', 'WW ', 'WWW'], ['W': TRContent.RUBBER_PLANKS])
		recipes.shaped(TRContent.RUBBER_TRAPDOOR, 2, 'wooden_trapdoor', ['###', '###'], ['#': TRContent.RUBBER_PLANKS])
		recipes.shaped(TRContent.RUBBER_WOOD, 3, null, ['##', '##'], ['#': TRContent.RUBBER_LOG])
		recipes.shaped(Items.SPONGE, 1, null, ['###', '###', '###'], ['#': TRContent.Parts.SPONGE_PIECE])
		recipes.shaped(TRContent.STRIPPED_RUBBER_WOOD, 3, null, ['##', '##'], ['#': TRContent.RUBBER_LOG_STRIPPED])
	}

	private static void crafting_table_paper(CraftingRecipeFactory recipes) {
		recipes.shapeless(Items.PAPER, 3, null, [TRContent.Dusts.SAW.asTag(), TRContent.Dusts.SAW.asTag(), TRContent.Dusts.SAW.asTag(), Fluids.WATER])
	}

	private static void crafting_table_parts(CraftingRecipeFactory recipes) {
		recipes.shaped(TRContent.Parts.ADVANCED_CIRCUIT, 1, null, ['RGR', 'LCL', 'RGR'], ['R': Items.REDSTONE, 'C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'G': Items.GLOWSTONE_DUST, 'L': Items.LAPIS_LAZULI])
		recipes.shaped(TRContent.NuclearReactorComponents.ADVANCED_HEAT_EXCHANGER, 1, null, ['DED', 'HCH', 'DED'], ['C': TRContent.Plates.COPPER.asTag(), 'D': TRContent.Plates.DIAMOND.asTag(), 'E': TRContent.Parts.ELECTRONIC_CIRCUIT, 'H': TRContent.NuclearReactorComponents.HEAT_EXCHANGER])
		recipes.shaped(TRContent.NuclearReactorComponents.ADVANCED_HEAT_VENT, 1, null, ['BVB', 'BDB', 'BVB'], ['B': Items.IRON_BARS, 'D': TRContent.Plates.DIAMOND.asTag(), 'V': TRContent.NuclearReactorComponents.HEAT_VENT])
		recipes.shaped(TRContent.Parts.BASIC_DISPLAY, 1, null, ['ADA', 'DCD', 'AGA'], ['A': TRContent.Plates.REFINED_IRON.asTag(), 'D': Items.DYE.black(), 'G': Items.GLASS_PANE, 'C': TRContent.Parts.ELECTRONIC_CIRCUIT])
		recipes.shaped(TRContent.Parts.CARBON_FIBER, 1, 'carbon_fiber', [' C ', 'C C', ' C '], ['C': TRContent.Dusts.COAL.asTag()])
		recipes.shaped(TRContent.Parts.CARBON_FIBER, 1, 'carbon_fiber', ['CCC', 'C C', 'CCC'], ['C': ModFluids.CARBON.fluid])
		recipes.shapeless(TRContent.Parts.CARBON_MESH, 1, null, [TRContent.Parts.CARBON_FIBER, TRContent.Parts.CARBON_FIBER])
		recipes.shaped(TRContent.NuclearReactorComponents.COMPONENT_HEAT_EXCHANGER, 1, null, [' G ', 'GEG', ' G '], ['E': TRContent.NuclearReactorComponents.HEAT_EXCHANGER, 'G': TRContent.Plates.GOLD.asTag()])
		recipes.shaped(TRContent.NuclearReactorComponents.COMPONENT_HEAT_VENT, 1, null, ['BPB', 'PVP', 'BPB'], ['B': Items.IRON_BARS, 'P': TRContent.Plates.TIN.asTag(), 'V': TRContent.NuclearReactorComponents.HEAT_VENT])
		recipes.shapeless(TRContent.NuclearReactorComponents.CONTAINMENT_REACTOR_PLATING, 1, null, [TRContent.NuclearReactorComponents.REACTOR_PLATING, TRContent.Plates.ADVANCED_ALLOY, TRContent.Plates.ADVANCED_ALLOY])
		recipes.shaped(TRContent.Parts.DATA_STORAGE_CHIP, 1, null, ['ADA', 'DID', 'ADA'], ['A': TRContent.Parts.ADVANCED_CIRCUIT, 'D': TRContent.Parts.DATA_STORAGE_CORE, 'I': TRContent.Ingots.IRIDIUM.asTag()])
		recipes.shaped(TRContent.Parts.DATA_STORAGE_CORE, 1, null, ['RGR', 'LCL', 'EEE'], ['R': Items.REDSTONE, 'C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'E': TRContent.Plates.PERIDOT.asTag(), 'G': Items.GLOWSTONE_DUST, 'L': Items.LAPIS_LAZULI])
		recipes.shaped(TRContent.Parts.DIAMOND_GRINDING_HEAD, 3, null, ['DSD', 'SGS', 'DSD'], ['S': TRContent.Ingots.STEEL.asTag(), 'D': TRContent.Dusts.DIAMOND.asTag(), 'G': Items.DIAMOND])
		recipes.shaped(TRContent.Parts.DIAMOND_SAW_BLADE, 4, null, ['DSD', 'S S', 'DSD'], ['S': TRContent.Ingots.STEEL.asTag(), 'D': TRContent.Dusts.DIAMOND.asTag()])
		recipes.shaped(TRContent.Parts.DIGITAL_DISPLAY, 1, null, ['ADA', 'DGD', 'ACA'], ['A': TRContent.Plates.ALUMINUM.asTag(), 'C': TRContent.Parts.ADVANCED_CIRCUIT, 'D': Items.DYE.black(), 'G': Items.GLASS_PANE])
		recipes.shaped(TRContent.NuclearReactorComponents.DUAL_URANIUM_FUEL_ROD, 1, null, ['RAR'], ['A': TRContent.Plates.ADVANCED_ALLOY, 'R': TRContent.NuclearReactorComponents.URANIUM_FUEL_ROD])
		recipes.shaped(TRContent.Parts.ELECTRONIC_CIRCUIT, 1, null, ['WWW', 'SRS', 'WWW'], ['R': TRContent.Ingots.REFINED_IRON, 'W': TRContent.Cables.INSULATED_COPPER, 'S': Items.REDSTONE])
		recipes.shaped(TRContent.Parts.ENERGY_FLOW_CHIP, 4, null, ['ATA', 'LIL', 'ATA'], ['A': TRContent.Parts.ADVANCED_CIRCUIT, 'T': TRContent.Ingots.TUNGSTEN.asTag(), 'I': TRContent.Plates.IRIDIUM_ALLOY, 'L': TRContent.LAPOTRON_CRYSTAL])
		recipes.shaped(TRContent.NuclearReactorComponents.HEAT_CAPACITY_REACTOR_PLATING, 1, null, ['CCC', 'CRC', 'CCC'], ['C': TRContent.Plates.COPPER.asTag(), 'R': TRContent.NuclearReactorComponents.REACTOR_PLATING])
		recipes.shaped(TRContent.NuclearReactorComponents.HEAT_EXCHANGER, 1, null, ['CEC', 'TCT', 'CTC'], ['C': TRContent.Plates.COPPER.asTag(), 'E': TRContent.Parts.ELECTRONIC_CIRCUIT, 'T': TRContent.Plates.TIN.asTag()])
		recipes.shaped(TRContent.NuclearReactorComponents.HEAT_VENT, 1, null, ['BPB', 'PCP', 'BPB'], ['B': Items.IRON_BARS, 'P': TRContent.Plates.STEEL.asTag(), 'C': TRContent.Parts.ELECTRONIC_CIRCUIT])
		recipes.shaped(TRContent.NuclearReactorComponents.HELIUM_COOLANT_CELL_180K, 1, null, ['TTT', 'CCC', 'TTT'], ['C': TRContent.NuclearReactorComponents.HELIUM_COOLANT_CELL_60K, 'T': TRContent.Ingots.TIN.asTag()])
		recipes.shaped(TRContent.NuclearReactorComponents.HELIUM_COOLANT_CELL_360K, 1, null, ['THT', 'TCT', 'THT'], ['C': Items.COPPER_INGOT, 'T': TRContent.Ingots.TIN.asTag(), 'H': TRContent.NuclearReactorComponents.HELIUM_COOLANT_CELL_180K])
		recipes.shaped(TRContent.NuclearReactorComponents.HELIUM_COOLANT_CELL_60K, 1, null, [' T ', 'TCT', ' T '], ['T': TRContent.Ingots.TIN.asTag(), 'C': ModFluids.HELIUM.fluid])
		recipes.shaped(TRContent.NuclearReactorComponents.IRIDIUM_NEUTRON_REFLECTOR, 1, null, ['PPP', 'PIP', 'PPP'], ['P': TRContent.NuclearReactorComponents.THICK_NEUTRON_REFLECTOR, 'I': TRContent.Ingots.IRIDIUM.asTag()])
		recipes.shaped(TRContent.NuclearReactorComponents.NAK_COOLANT_CELL_180K, 1, null, ['TTT', 'CCC', 'TTT'], ['C': TRContent.NuclearReactorComponents.NAK_COOLANT_CELL_60K, 'T': TRContent.Ingots.TIN.asTag()])
		recipes.shaped(TRContent.NuclearReactorComponents.NAK_COOLANT_CELL_360K, 1, null, ['THT', 'TCT', 'THT'], ['C': Items.COPPER_INGOT, 'T': TRContent.Ingots.TIN.asTag(), 'H': TRContent.NuclearReactorComponents.NAK_COOLANT_CELL_180K])
		recipes.shaped(TRContent.NuclearReactorComponents.NAK_COOLANT_CELL_60K, 1, 'nak_coolant_cell_60k', ['TST', 'PCP', 'TST'], ['C': TRContent.NuclearReactorComponents.WATER_COOLANT_CELL_10K, 'T': TRContent.Ingots.TIN.asTag(), 'S': ModFluids.SODIUM.fluid, 'P': ModFluids.POTASSIUM.fluid])
		recipes.shaped(TRContent.NuclearReactorComponents.NAK_COOLANT_CELL_60K, 1, 'nak_coolant_cell_60k', ['TPT', 'SCS', 'TPT'], ['C': TRContent.NuclearReactorComponents.WATER_COOLANT_CELL_10K, 'T': TRContent.Ingots.TIN.asTag(), 'P': ModFluids.POTASSIUM.fluid, 'S': ModFluids.SODIUM.fluid])
		recipes.shaped(TRContent.NuclearReactorComponents.NEUTRON_REFLECTOR, 1, null, ['TCT', 'CPC', 'TCT'], ['P': TRContent.Plates.COPPER.asTag(), 'C': TRContent.Dusts.COAL.asTag(), 'T': TRContent.Plates.TIN.asTag()])
		recipes.shaped(TRContent.NuclearReactorComponents.OVERCLOCKED_HEAT_VENT, 1, null, [' P ', 'PVP', ' P '], ['P': TRContent.Plates.GOLD.asTag(), 'V': TRContent.NuclearReactorComponents.REACTOR_HEAT_VENT])
		recipes.shapeless(TRContent.Parts.PLANTBALL, 1, 'plantball', [itemTag('c:grass_variants'), itemTag('c:grass_variants'), itemTag('c:grass_variants'), itemTag('c:grass_variants'), itemTag('c:grass_variants'), itemTag('c:grass_variants'), itemTag('c:grass_variants'), itemTag('c:grass_variants'), itemTag('c:grass_variants')])
		recipes.shaped(TRContent.Parts.PLANTBALL, 1, 'plantball', [' X ', 'XXX', ' X '], ['X': Items.KELP])
		recipes.shapeless(TRContent.Parts.PLANTBALL, 1, 'plantball', [ItemTags.LEAVES, ItemTags.LEAVES, ItemTags.LEAVES, ItemTags.LEAVES, ItemTags.LEAVES, ItemTags.LEAVES, ItemTags.LEAVES, ItemTags.LEAVES, ItemTags.LEAVES])
		recipes.shapeless(TRContent.Parts.PLANTBALL, 4, 'plantball', [ItemTags.SAPLINGS, ItemTags.SAPLINGS, ItemTags.SAPLINGS, ItemTags.SAPLINGS, ItemTags.SAPLINGS, ItemTags.SAPLINGS, ItemTags.SAPLINGS, ItemTags.SAPLINGS, ItemTags.SAPLINGS])
		recipes.shaped(TRContent.Parts.PLANTBALL, 1, 'plantball', [' X ', 'XXX', ' X '], ['X': Items.SUGAR_CANE])
		recipes.shaped(TRContent.NuclearReactorComponents.QUAD_URANIUM_FUEL_ROD, 1, null, ['RAR', 'SAS', 'RAR'], ['A': TRContent.Plates.ADVANCED_ALLOY, 'R': TRContent.NuclearReactorComponents.URANIUM_FUEL_ROD, 'S': TRContent.Plates.STEEL.asTag()])
		recipes.shaped(TRContent.NuclearReactorComponents.QUAD_URANIUM_FUEL_ROD, 1, null, [' S ', 'RAR', ' S '], ['A': TRContent.Plates.ADVANCED_ALLOY, 'R': TRContent.NuclearReactorComponents.DUAL_URANIUM_FUEL_ROD, 'S': TRContent.Plates.STEEL.asTag()])
		recipes.shaped(TRContent.NuclearReactorComponents.REACTOR_HEAT_EXCHANGER, 1, null, ['CCC', 'CEC', 'CCC'], ['C': TRContent.Plates.COPPER.asTag(), 'E': TRContent.NuclearReactorComponents.HEAT_EXCHANGER])
		recipes.shaped(TRContent.NuclearReactorComponents.REACTOR_HEAT_VENT, 1, null, ['PPP', 'PVP', 'PPP'], ['P': TRContent.Plates.COPPER.asTag(), 'V': TRContent.NuclearReactorComponents.HEAT_VENT])
		recipes.shapeless(TRContent.NuclearReactorComponents.REACTOR_PLATING, 1, null, [TRContent.Plates.INDUSTRIAL_ALLOY, TRContent.Plates.ADVANCED_ALLOY])
		recipes.shaped(Items.SHULKER_SHELL, 1, null, ['BPB', 'BTB', ' E '], ['B': Items.PURPUR_BLOCK, 'E': TRContent.SmallDusts.ENDSTONE.asTag(), 'P': Items.PURPUR_PILLAR, 'T': TRContent.Dusts.ENDER_PEARL.asTag()])
		recipes.shaped(TRContent.Parts.SUPERCONDUCTOR, 4, null, ['CCC', 'TIT', 'EEE'], ['C': TRContent.NuclearReactorComponents.HELIUM_COOLANT_CELL_60K, 'T': TRContent.Ingots.TUNGSTEN.asTag(), 'E': TRContent.Parts.ENERGY_FLOW_CHIP, 'I': TRContent.Plates.IRIDIUM_ALLOY])
		recipes.shaped(TRContent.NuclearReactorComponents.THICK_NEUTRON_REFLECTOR, 1, null, [' P ', 'PCP', ' P '], ['P': TRContent.NuclearReactorComponents.NEUTRON_REFLECTOR, 'C': ModFluids.BERYLLIUM.fluid])
		recipes.shaped(TRContent.Parts.TUNGSTEN_GRINDING_HEAD, 2, null, ['TST', 'SBS', 'TST'], ['B': TRContent.StorageBlocks.STEEL.asTag(), 'S': TRContent.Ingots.STEEL.asTag(), 'T': TRContent.Ingots.TUNGSTEN.asTag()])
		recipes.shaped(TRContent.Dusts.URANIUM, 1, null, ['DDD', 'DSD', 'DDD'], ['D': TRContent.Dusts.URANIUM_238, 'S': TRContent.SmallDusts.URANIUM_235])
		recipes.shaped(TRContent.NuclearReactorComponents.WATER_COOLANT_CELL_10K, 2, null, [' T ', 'TWT', ' T '], ['T': TRContent.Ingots.TIN.asTag(), 'W': Fluids.WATER])
		recipes.shaped(TRContent.NuclearReactorComponents.WATER_COOLANT_CELL_30K, 1, null, ['TTT', 'CCC', 'TTT'], ['C': TRContent.NuclearReactorComponents.WATER_COOLANT_CELL_10K, 'T': TRContent.Ingots.TIN.asTag()])
		recipes.shaped(TRContent.NuclearReactorComponents.WATER_COOLANT_CELL_60K, 1, null, ['TCT', 'TPT', 'TCT'], ['P': TRContent.Plates.COPPER.asTag(), 'C': TRContent.NuclearReactorComponents.WATER_COOLANT_CELL_30K, 'T': TRContent.Ingots.TIN.asTag()])
		recipes.shaped(TRContent.Plates.WOOD, 1, null, [' S ', 'SWS', ' S '], ['W': ItemTags.PLANKS, 'S': Items.STICK])
	}

	private static void crafting_table_solar_panel(CraftingRecipeFactory recipes) {
		recipes.shaped(TRContent.SolarPanels.ADVANCED, 1, null, ['DLD', 'LDL', 'CPC'], ['P': TRContent.SolarPanels.BASIC, 'C': TRContent.Parts.ADVANCED_CIRCUIT, 'D': TRContent.Dusts.COAL.asTag(), 'L': Items.GLASS])
		recipes.shaped(TRContent.SolarPanels.ADVANCED, 1, null, ['DLD', 'LDL', 'CPC'], ['P': TRContent.MachineBlocks.BASIC.frame, 'C': TRContent.Parts.ADVANCED_CIRCUIT, 'D': TRContent.Dusts.COAL.asTag(), 'L': Items.GLASS])
		recipes.shaped(TRContent.SolarPanels.BASIC, 1, null, ['DLD', 'LDL', 'CGC'], ['C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'D': TRContent.Dusts.COAL.asTag(), 'G': TRContent.Machine.SOLID_FUEL_GENERATOR, 'L': Items.GLASS_PANE])
		recipes.shaped(TRContent.SolarPanels.INDUSTRIAL, 1, null, ['DLD', 'LDL', 'CPC'], ['P': TRContent.SolarPanels.ADVANCED, 'C': TRContent.Parts.ADVANCED_CIRCUIT, 'D': TRContent.Dusts.DIAMOND.asTag(), 'L': Items.GLASS])
		recipes.shaped(TRContent.SolarPanels.INDUSTRIAL, 1, null, ['DLD', 'LDL', 'CPC'], ['P': TRContent.MachineBlocks.BASIC.frame, 'C': TRContent.Parts.ADVANCED_CIRCUIT, 'D': TRContent.Dusts.DIAMOND.asTag(), 'L': Items.GLASS])
		recipes.shaped(TRContent.SolarPanels.QUANTUM, 1, null, ['AAA', 'ABA', 'AAA'], ['A': TRContent.SolarPanels.ULTIMATE, 'B': TRContent.Parts.UU_MATTER])
		recipes.shaped(TRContent.SolarPanels.ULTIMATE, 1, null, ['DLD', 'LDL', 'CPC'], ['P': TRContent.SolarPanels.INDUSTRIAL, 'C': TRContent.Parts.ADVANCED_CIRCUIT, 'D': TRContent.Dusts.DIAMOND.asTag(), 'L': TRContent.REINFORCED_GLASS])
		recipes.shaped(TRContent.SolarPanels.ULTIMATE, 1, null, ['DLD', 'LDL', 'CPC'], ['P': TRContent.MachineBlocks.ADVANCED.frame, 'C': TRContent.Parts.ADVANCED_CIRCUIT, 'D': TRContent.Dusts.DIAMOND.asTag(), 'L': TRContent.REINFORCED_GLASS])
	}

	private static void crafting_table_storage_block(CraftingRecipeFactory recipes) {
		recipes.shaped(TRContent.COPPER_WALL, 6, null, ['###', '###'], ['#': Items.COPPER_BLOCK.weathering().unaffected()])
		recipes.stonecutting(TRContent.COPPER_WALL, 1, Items.COPPER_BLOCK.weathering().unaffected())
		recipes.shapeless(TRContent.StorageBlocks.IRIDIUM_REINFORCED_STONE, 1, null, [Items.STONE, TRContent.Plates.IRIDIUM_ALLOY])
		recipes.shapeless(TRContent.StorageBlocks.IRIDIUM_REINFORCED_TUNGSTENSTEEL, 1, 'iridium_reinforced_tungstensteel_storage_block', [TRContent.StorageBlocks.TUNGSTENSTEEL.asTag(), TRContent.Plates.IRIDIUM.asTag()])
		recipes.shapeless(TRContent.StorageBlocks.IRIDIUM_REINFORCED_TUNGSTENSTEEL, 1, 'iridium_reinforced_tungstensteel_storage_block', [TRContent.StorageBlocks.IRIDIUM_REINFORCED_STONE.asTag(), TRContent.Ingots.TUNGSTENSTEEL])
	}

	private static void crafting_table_tool(CraftingRecipeFactory recipes) {
		recipes.shaped(TRContent.ADVANCED_CHAINSAW, 1, null, [' DD', 'TBD', 'ST '], ['D': Items.DIAMOND, 'B': TRContent.LITHIUM_ION_BATTERY, 'T': TRContent.Ingots.TITANIUM.asTag(), 'S': TRContent.BASIC_CHAINSAW])
		recipes.shaped(TRContent.ADVANCED_DRILL, 1, null, [' D ', 'DBD', 'TST'], ['D': Items.DIAMOND, 'B': TRContent.LITHIUM_ION_BATTERY, 'T': TRContent.Ingots.TITANIUM.asTag(), 'S': TRContent.BASIC_DRILL])
		recipes.shaped(TRContent.ADVANCED_JACKHAMMER, 1, null, ['DSD', 'TBT', ' D '], ['D': Items.DIAMOND, 'B': TRContent.LITHIUM_ION_BATTERY, 'T': TRContent.Ingots.TITANIUM.asTag(), 'S': TRContent.BASIC_JACKHAMMER])
		recipes.shaped(TRContent.BASIC_CHAINSAW, 1, null, [' SS', 'SBS', 'CS '], ['S': TRContent.Ingots.REFINED_IRON, 'B': TRContent.RED_CELL_BATTERY, 'C': TRContent.Parts.ELECTRONIC_CIRCUIT])
		recipes.shaped(TRContent.BASIC_DRILL, 1, null, [' S ', 'SBS', 'SCS'], ['S': TRContent.Ingots.REFINED_IRON, 'B': TRContent.RED_CELL_BATTERY, 'C': TRContent.Parts.ELECTRONIC_CIRCUIT])
		recipes.shaped(TRContent.BASIC_JACKHAMMER, 1, null, ['SCS', 'SBS', ' S '], ['S': TRContent.Ingots.REFINED_IRON, 'B': TRContent.RED_CELL_BATTERY, 'C': TRContent.Parts.ELECTRONIC_CIRCUIT])
		recipes.shaped(TRContent.ELECTRIC_TREE_TAP, 1, null, ['TB'], ['T': TRContent.TREE_TAP, 'B': TRContent.RED_CELL_BATTERY])
		recipes.shaped(TRContent.GPS, 1, null, ['B', 'C'], ['B': TRContent.Parts.BASIC_DISPLAY, 'C': Items.COMPASS])
		recipes.shaped(TRContent.INDUSTRIAL_CHAINSAW, 1, null, [' NI', 'OCN', 'DO '], ['I': TRContent.Plates.IRIDIUM_ALLOY, 'N': TRContent.Nuggets.IRIDIUM.asTag(), 'C': TRContent.Parts.ENERGY_FLOW_CHIP, 'O': TRContent.Upgrades.OVERCLOCKER, 'D': TRContent.ADVANCED_CHAINSAW])
		recipes.shaped(TRContent.INDUSTRIAL_DRILL, 1, null, [' I ', 'NCN', 'OAO'], ['I': TRContent.Plates.IRIDIUM_ALLOY, 'N': TRContent.Nuggets.IRIDIUM.asTag(), 'C': TRContent.Parts.ENERGY_FLOW_CHIP, 'O': TRContent.Upgrades.OVERCLOCKER, 'A': TRContent.ADVANCED_DRILL])
		recipes.shaped(TRContent.INDUSTRIAL_JACKHAMMER, 1, null, ['NDN', 'OCO', ' I '], ['I': TRContent.Plates.IRIDIUM_ALLOY, 'N': TRContent.Nuggets.IRIDIUM.asTag(), 'C': TRContent.Parts.ENERGY_FLOW_CHIP, 'O': TRContent.Upgrades.OVERCLOCKER, 'D': TRContent.ADVANCED_JACKHAMMER])
		recipes.shaped(TRContent.NANOSABER, 1, null, ['DC ', 'DC ', 'GLG'], ['D': TRContent.Plates.DIAMOND.asTag(), 'C': TRContent.Plates.CARBON, 'G': TRContent.SmallDusts.GLOWSTONE, 'L': TRContent.LAPOTRON_CRYSTAL])
		recipes.shaped(TRContent.OMNI_TOOL, 1, null, [' WD', ' C ', 'S  '], ['D': TRContent.ADVANCED_DRILL, 'C': TRContent.ADVANCED_CHAINSAW, 'S': Items.DIAMOND_SWORD, 'W': TRContent.WRENCH])
		recipes.shaped(TRContent.PAINTING_TOOL, 1, null, ['BWB', ' B ', ' B '], ['B': TRContent.Ingots.BRONZE.asTag(), 'W': ItemTags.WOOL])
		recipes.shaped(TRContent.ROCK_CUTTER, 1, null, ['DN ', 'DN ', 'DCB'], ['D': TRContent.Dusts.DIAMOND.asTag(), 'N': Items.GOLD_NUGGET, 'C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'B': TRContent.RED_CELL_BATTERY])
		recipes.shaped(TRContent.TREE_TAP, 1, null, [' S ', 'PPP', 'P  '], ['S': Items.STICK, 'P': ItemTags.PLANKS])
		recipes.shaped(TRContent.WRENCH, 1, null, ['B B', ' B ', ' B '], ['B': TRContent.Ingots.BRONZE.asTag()])
	}

	private static void crafting_table_unit(CraftingRecipeFactory recipes) {
		recipes.shaped(TRContent.StorageUnit.ADVANCED, 1, null, ['PPP', 'XBX', 'CDC'], ['P': TRContent.Plates.ELECTRUM.asTag(), 'B': TRContent.MachineBlocks.ADVANCED.frame, 'D': TRContent.Parts.DIGITAL_DISPLAY, 'X': TRContent.StorageUnit.BASIC, 'C': TRContent.Parts.ADVANCED_CIRCUIT])
		recipes.shaped(TRContent.StorageUnit.BASIC, 1, null, ['PPP', 'XBX', 'CDC'], ['X': TRContent.StorageUnit.CRUDE, 'B': TRContent.MachineBlocks.BASIC.frame, 'P': TRContent.Plates.REFINED_IRON.asTag(), 'D': TRContent.Parts.BASIC_DISPLAY, 'C': TRContent.Parts.ELECTRONIC_CIRCUIT])
		recipes.shaped(TRContent.StorageUnit.CRUDE, 1, null, ['WWW', 'WBW', 'WPW'], ['B': itemTag('c:barrels/wooden'), 'P': Items.PAPER, 'W': ItemTags.PLANKS])
		recipes.shaped(TRContent.StorageUnit.INDUSTRIAL, 1, null, ['PPP', 'XBX', 'CDC'], ['P': TRContent.Plates.STEEL.asTag(), 'B': TRContent.MachineBlocks.INDUSTRIAL.frame, 'D': TRContent.Parts.DIGITAL_DISPLAY, 'X': TRContent.StorageUnit.ADVANCED, 'C': TRContent.Parts.INDUSTRIAL_CIRCUIT])
		recipes.shaped(TRContent.StorageUnit.QUANTUM, 1, null, ['PPP', 'XBX', 'CDC'], ['P': TRContent.Plates.TUNGSTEN.asTag(), 'B': TRContent.MachineBlocks.INDUSTRIAL.frame, 'X': TRContent.StorageUnit.INDUSTRIAL, 'D': TRContent.Parts.DIGITAL_DISPLAY, 'C': TRContent.Parts.DATA_STORAGE_CHIP])
		recipes.shaped(TRContent.StorageUnit.BUFFER, 1, null, ['WWW', 'WPW', 'WWW'], ['P': Items.PAPER, 'W': TRContent.Plates.WOOD.asTag()])
		recipes.shaped(TRContent.TankUnit.ADVANCED, 1, 'advanced_tank_unit', [' C ', 'RBR', ' C '], ['B': TRContent.StorageUnit.ADVANCED, 'R': TRContent.Parts.RUBBER, 'C': TRContent.Cells.EMPTY])
		recipes.shaped(TRContent.TankUnit.ADVANCED, 1, 'advanced_tank_unit', ['PPP', 'XBX', 'CDC'], ['P': TRContent.Plates.ELECTRUM.asTag(), 'B': TRContent.MachineBlocks.ADVANCED.frame, 'D': TRContent.Parts.DIGITAL_DISPLAY, 'X': TRContent.TankUnit.BASIC, 'C': TRContent.Parts.ADVANCED_CIRCUIT])
		recipes.shaped(TRContent.TankUnit.BASIC, 1, null, [' C ', 'RBR', ' C '], ['B': TRContent.StorageUnit.BASIC, 'R': TRContent.Parts.RUBBER, 'C': TRContent.Cells.EMPTY])
		recipes.shaped(TRContent.TankUnit.INDUSTRIAL, 1, 'industrial_tank_unit', [' C ', 'RBR', ' C '], ['B': TRContent.StorageUnit.INDUSTRIAL, 'R': TRContent.Parts.RUBBER, 'C': TRContent.Cells.EMPTY])
		recipes.shaped(TRContent.TankUnit.INDUSTRIAL, 1, 'industrial_tank_unit', ['PPP', 'XBX', 'CDC'], ['P': TRContent.Plates.STEEL.asTag(), 'B': TRContent.MachineBlocks.INDUSTRIAL.frame, 'D': TRContent.Parts.DIGITAL_DISPLAY, 'X': TRContent.TankUnit.ADVANCED, 'C': TRContent.Parts.INDUSTRIAL_CIRCUIT])
		recipes.shaped(TRContent.TankUnit.QUANTUM, 1, 'quantum_tank_unit', [' C ', 'RBR', ' C '], ['B': TRContent.StorageUnit.QUANTUM, 'C': TRContent.Cells.EMPTY, 'R': TRContent.Parts.RUBBER])
		recipes.shaped(TRContent.TankUnit.QUANTUM, 1, 'quantum_tank_unit', ['PPP', 'XBX', 'CDC'], ['P': TRContent.Plates.TUNGSTEN.asTag(), 'B': TRContent.MachineBlocks.INDUSTRIAL.frame, 'X': TRContent.TankUnit.INDUSTRIAL, 'D': TRContent.Parts.DIGITAL_DISPLAY, 'C': TRContent.Parts.DATA_STORAGE_CHIP])
		recipes.shaped(TRContent.StorageUnit.ADVANCED.getUpgrader().orElseThrow(), 1, null, ['PPP', 'XB ', 'CDC'], ['P': TRContent.Plates.STEEL.asTag(), 'B': TRContent.MachineBlocks.INDUSTRIAL.frame, 'D': TRContent.Parts.DIGITAL_DISPLAY, 'X': TRContent.StorageUnit.ADVANCED, 'C': TRContent.Parts.INDUSTRIAL_CIRCUIT])
		recipes.shaped(TRContent.StorageUnit.BASIC.getUpgrader().orElseThrow(), 1, null, ['PPP', 'XB ', 'CDC'], ['P': TRContent.Plates.ELECTRUM.asTag(), 'B': TRContent.MachineBlocks.ADVANCED.frame, 'D': TRContent.Parts.DIGITAL_DISPLAY, 'X': TRContent.StorageUnit.BASIC, 'C': TRContent.Parts.ADVANCED_CIRCUIT])
		recipes.shaped(TRContent.StorageUnit.CRUDE.getUpgrader().orElseThrow(), 1, null, ['PPP', 'XB ', 'CDC'], ['X': TRContent.StorageUnit.CRUDE, 'B': TRContent.MachineBlocks.BASIC.frame, 'P': TRContent.Plates.REFINED_IRON.asTag(), 'D': TRContent.Parts.BASIC_DISPLAY, 'C': TRContent.Parts.ELECTRONIC_CIRCUIT])
		recipes.shaped(TRContent.StorageUnit.INDUSTRIAL.getUpgrader().orElseThrow(), 1, null, ['PPP', 'XB ', 'CDC'], ['P': TRContent.Plates.TUNGSTEN.asTag(), 'B': TRContent.MachineBlocks.INDUSTRIAL.frame, 'X': TRContent.StorageUnit.INDUSTRIAL, 'D': TRContent.Parts.DIGITAL_DISPLAY, 'C': TRContent.Parts.DATA_STORAGE_CHIP])
	}

	private static void crafting_table_upgrade(CraftingRecipeFactory recipes) {
		recipes.shaped(TRContent.Upgrades.ENERGY_STORAGE, 1, null, ['PRP', 'WBW', 'PCP'], ['P': ItemTags.PLANKS, 'B': TRContent.RED_CELL_BATTERY, 'C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'W': TRContent.Cables.INSULATED_COPPER, 'R': TRContent.Plates.RED_GARNET.asTag()])
		recipes.shaped(TRContent.Upgrades.MUFFLER, 1, null, [' W ', 'WCW', ' W '], ['C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'W': ItemTags.WOOL])
		recipes.shaped(TRContent.Upgrades.OVERCLOCKER, 1, 'overclocker_upgrade', ['TTT', 'WCW'], ['C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'T': TRContent.NuclearReactorComponents.WATER_COOLANT_CELL_10K, 'W': TRContent.Cables.INSULATED_COPPER])
		recipes.shaped(TRContent.Upgrades.OVERCLOCKER, 2, 'overclocker_upgrade', [' T ', 'WCW'], ['C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'T': TRContent.NuclearReactorComponents.HELIUM_COOLANT_CELL_180K, 'W': TRContent.Cables.INSULATED_COPPER])
		recipes.shaped(TRContent.Upgrades.OVERCLOCKER, 2, 'overclocker_upgrade', [' T ', 'WCW'], ['C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'T': TRContent.NuclearReactorComponents.NAK_COOLANT_CELL_60K, 'W': TRContent.Cables.INSULATED_COPPER])
		recipes.shaped(TRContent.Upgrades.SUPERCONDUCTOR, 1, null, ['SOS', 'CMC', 'SOS'], ['C': TRContent.Cables.SUPERCONDUCTOR, 'S': TRContent.Parts.SUPERCONDUCTOR, 'M': TRContent.MachineBlocks.INDUSTRIAL.frame, 'O': TRContent.Parts.DATA_STORAGE_CHIP])
		recipes.shaped(TRContent.Upgrades.TRANSFORMER, 1, null, ['GGG', 'WTW', 'GCG'], ['C': TRContent.Parts.ELECTRONIC_CIRCUIT, 'T': TRContent.Machine.MV_TRANSFORMER, 'G': Items.GLASS, 'W': TRContent.Cables.INSULATED_GOLD])
	}

	private static void crafting_table_uu_matter_calcite(CraftingRecipeFactory recipes) {
		recipes.shaped(Items.BONE, 1, null, ['   ', 'UC ', '   '], ['C': TRContent.Dusts.CALCITE.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.CALCITE, 1, null, ['   ', ' U ', ' C '], ['C': TRContent.Dusts.CALCITE.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.NAUTILUS_SHELL, 1, null, [' U ', ' CU', '   '], ['C': TRContent.Dusts.CALCITE.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.TURTLE_SCUTE, 1, null, ['UUU', 'UCU', '   '], ['C': TRContent.Dusts.CALCITE.asTag(), 'U': TRContent.Parts.UU_MATTER])
	}

	private static void crafting_table_uu_matter_misc(CraftingRecipeFactory recipes) {
		recipes.shaped(Items.CLAY, 1, null, ['  U', '   ', '   '], ['U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.GLASS, 1, null, ['U  ', '   ', '  U'], ['U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.GLOWSTONE, 1, null, ['UU ', '   ', '   '], ['U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.GRASS_BLOCK, 1, null, ['   ', '   ', 'U  '], ['U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.ENDER_PEARL, 1, null, ['UUU', '   ', '   '], ['U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.FEATHER, 1, null, ['U  ', 'U  ', 'U  '], ['U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.INK_SAC, 1, null, ['UU ', 'U  ', '   '], ['U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.OBSIDIAN, 1, null, ['UUU', ' U ', '   '], ['U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.BIG_DRIPLEAF, 1, null, ['   ', 'UG ', 'U  '], ['G': TRContent.Parts.PLANTBALL, 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.CACTUS, 1, null, ['U  ', ' GU', '   '], ['G': TRContent.Parts.PLANTBALL, 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.COCOA_BEANS, 1, null, ['U  ', ' G ', 'U  '], ['G': TRContent.Parts.PLANTBALL, 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.GLOW_BERRIES, 1, null, [' U ', 'GU ', '   '], ['G': TRContent.Parts.PLANTBALL, 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.GLOW_LICHEN, 1, null, [' U ', ' U ', ' G '], ['G': TRContent.Parts.PLANTBALL, 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.KELP, 1, null, [' U ', ' U ', ' G '], ['G': TRContent.Parts.PLANTBALL, 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.LILY_PAD, 1, null, [' U ', ' GU', '   '], ['G': TRContent.Parts.PLANTBALL, 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.MOSS_BLOCK, 1, null, ['G G', ' U ', 'G G'], ['G': TRContent.Parts.PLANTBALL, 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.SMALL_DRIPLEAF, 1, null, ['   ', ' G ', 'U  '], ['G': TRContent.Parts.PLANTBALL, 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.SUGAR_CANE, 1, null, [' U ', ' G ', 'U  '], ['G': TRContent.Parts.PLANTBALL, 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.TWISTING_VINES, 1, null, [' U ', ' UG', '   '], ['G': TRContent.Parts.PLANTBALL, 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.VINE, 1, null, ['GU ', ' U ', '   '], ['G': TRContent.Parts.PLANTBALL, 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.WEEPING_VINES, 1, null, [' UG', ' U ', '   '], ['G': TRContent.Parts.PLANTBALL, 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.SEA_LANTERN, 1, null, ['UU ', '  U', '   '], ['U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.SNOW_BLOCK, 1, null, ['   ', '   ', ' U '], ['U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.STONE, 1, null, ['   ', ' U ', '   '], ['U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.TNT, 1, null, ['U  ', ' U ', '  U'], ['U': TRContent.Parts.UU_MATTER])
	}

	private static void crafting_table_uu_matter_ore(CraftingRecipeFactory recipes) {
		recipes.shaped(Items.GILDED_BLACKSTONE, 1, null, ['   ', 'UUU', ' S '], ['S': Items.BLACKSTONE, 'U': TRContent.Parts.UU_MATTER])
	}

	private static void crafting_table_uu_matter_sapling(CraftingRecipeFactory recipes) {
		recipes.shaped(Items.ACACIA_SAPLING, 1, null, ['   ', 'UP ', ' S '], ['P': TRContent.Parts.PLANTBALL, 'S': TRContent.Dusts.SAW.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.BAMBOO, 1, null, [' S ', ' P ', 'U  '], ['P': TRContent.Parts.PLANTBALL, 'S': TRContent.Dusts.SAW.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.BIRCH_SAPLING, 1, null, ['  U', ' P ', ' S '], ['P': TRContent.Parts.PLANTBALL, 'S': TRContent.Dusts.SAW.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.CHERRY_SAPLING, 1, null, [' S ', ' P ', ' U '], ['P': TRContent.Parts.PLANTBALL, 'S': TRContent.Dusts.SAW.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.CRIMSON_FUNGUS, 1, null, ['   ', ' U ', ' P '], ['P': Items.NETHER_WART_BLOCK, 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.DARK_OAK_SAPLING, 1, null, ['   ', ' P ', ' SU'], ['P': TRContent.Parts.PLANTBALL, 'S': TRContent.Dusts.SAW.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.JUNGLE_SAPLING, 1, null, ['   ', ' P ', 'US '], ['P': TRContent.Parts.PLANTBALL, 'S': TRContent.Dusts.SAW.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.MANGROVE_PROPAGULE, 1, null, ['U  ', ' P ', ' S '], ['P': TRContent.Parts.PLANTBALL, 'S': TRContent.Dusts.SAW.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.OAK_SAPLING, 1, null, [' U ', ' P ', ' S '], ['P': TRContent.Parts.PLANTBALL, 'S': TRContent.Dusts.SAW.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.PALE_OAK_SAPLING, 1, null, [' S ', 'UP ', '   '], ['P': TRContent.Parts.PLANTBALL, 'S': TRContent.Dusts.SAW.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.SPRUCE_SAPLING, 1, null, ['   ', ' PU', ' S '], ['P': TRContent.Parts.PLANTBALL, 'S': TRContent.Dusts.SAW.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.WARPED_FUNGUS, 1, null, ['   ', ' U ', ' P '], ['P': Items.WARPED_WART_BLOCK, 'U': TRContent.Parts.UU_MATTER])
	}

	private static void crafting_table_uu_matter_wood(CraftingRecipeFactory recipes) {
		recipes.shaped(Items.ACACIA_WOOD, 1, null, ['   ', 'US ', '   '], ['S': TRContent.Dusts.SAW.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.BIRCH_WOOD, 1, null, ['  U', ' S ', '   '], ['S': TRContent.Dusts.SAW.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.CRIMSON_HYPHAE, 1, null, ['   ', '   ', 'US '], ['S': TRContent.Dusts.SAW.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.DARK_OAK_WOOD, 1, null, ['   ', ' S ', '  U'], ['S': TRContent.Dusts.SAW.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.JUNGLE_WOOD, 1, null, ['   ', ' S ', 'U  '], ['S': TRContent.Dusts.SAW.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.MANGROVE_WOOD, 1, null, ['U  ', ' S ', '   '], ['S': TRContent.Dusts.SAW.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.OAK_WOOD, 1, null, [' U ', ' S ', '   '], ['S': TRContent.Dusts.SAW.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.SPRUCE_WOOD, 1, null, ['   ', ' SU', '   '], ['S': TRContent.Dusts.SAW.asTag(), 'U': TRContent.Parts.UU_MATTER])
		recipes.shaped(Items.WARPED_HYPHAE, 1, null, ['   ', '   ', ' SU'], ['S': TRContent.Dusts.SAW.asTag(), 'U': TRContent.Parts.UU_MATTER])
	}

	private static void smelting(CraftingRecipeFactory recipes) {
		recipes.smelting(Items.COPPER_BLOCK.weathering().unaffected(), Items.RAW_COPPER_BLOCK, 4.5f, 1500)
		recipes.blasting(Items.COPPER_BLOCK.weathering().unaffected(), Items.RAW_COPPER_BLOCK, 4.5f, 750)
		recipes.smelting(Items.GOLD_BLOCK, Items.RAW_GOLD_BLOCK, 4.5f, 1500)
		recipes.blasting(Items.GOLD_BLOCK, Items.RAW_GOLD_BLOCK, 4.5f, 750)
		recipes.smelting(Items.IRON_BLOCK, Items.RAW_IRON_BLOCK, 4.5f, 1500)
		recipes.blasting(Items.IRON_BLOCK, Items.RAW_IRON_BLOCK, 4.5f, 750)
		recipes.smelting(TRContent.StorageBlocks.LEAD, TRContent.StorageBlocks.RAW_LEAD, 4.5f, 1500)
		recipes.blasting(TRContent.StorageBlocks.LEAD, TRContent.StorageBlocks.RAW_LEAD, 4.5f, 750)
		recipes.smelting(TRContent.StorageBlocks.REFINED_IRON, Items.IRON_BLOCK, 4.5f, 1500)
		recipes.blasting(TRContent.StorageBlocks.REFINED_IRON, Items.IRON_BLOCK, 4.5f, 750)
		recipes.smelting(TRContent.Parts.RUBBER, TRContent.Parts.SAP, 0.5f, 200)
		recipes.smelting(TRContent.StorageBlocks.SILVER, TRContent.StorageBlocks.RAW_SILVER, 4.5f, 1500)
		recipes.blasting(TRContent.StorageBlocks.SILVER, TRContent.StorageBlocks.RAW_SILVER, 4.5f, 750)
		recipes.smelting(TRContent.StorageBlocks.TIN, TRContent.StorageBlocks.RAW_TIN, 4.5f, 1500)
		recipes.blasting(TRContent.StorageBlocks.TIN, TRContent.StorageBlocks.RAW_TIN, 4.5f, 750)
	}

	private static TagKey<Item> itemTag(String id) {
		return TagKey.create(Registries.ITEM, Identifier.parse(id))
	}
}
