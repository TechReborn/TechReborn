/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2023 TechReborn
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

package techreborn.datagen.advancement

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.minecraft.advancement.Advancement
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.advancement.AdvancementFrame
import net.minecraft.advancement.criterion.CriterionConditions
import net.minecraft.advancement.criterion.InventoryChangedCriterion
import net.minecraft.advancement.criterion.ItemCriterion
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.predicate.item.ItemPredicate
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import techreborn.init.TRContent

import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

class TRAdvancementProvider extends FabricAdvancementProvider {
	private Consumer<AdvancementEntry> consumer

	public TRAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture)
	}

	@Override
	void generateAdvancement(RegistryWrapper.WrapperLookup registryLookup, Consumer<AdvancementEntry> consumer) {
		this.consumer = consumer

		def root = create {
			name "root"
			icon TRContent.MANUAL
			background Identifier.of("techreborn:textures/block/storage/steel_storage_block.png")
			condition inventoryChanged(TRContent.ItemTags.ORES)
			condition inventoryChanged(TRContent.ItemTags.RAW_METALS)
			condition inventoryChanged(TRContent.ItemTags.GEMS)
		}

		refinedIronTree(root)
		treeTapTree(root)
	}

	private void refinedIronTree(AdvancementEntry root) {
		def refinediron = create {
			parent root
			name "refinediron"
			icon TRContent.Ingots.REFINED_IRON
			condition inventoryChanged(TRContent.Ingots.REFINED_IRON)
		}

		def alarm = create {
			parent refinediron
			name "alarm"
			icon TRContent.Machine.ALARM
			condition inventoryChanged(TRContent.Machine.ALARM)
		}

		def machineBlock = create {
			parent refinediron
			name "machineblock"
			icon TRContent.MachineBlocks.BASIC.frame
			condition inventoryChanged(TRContent.MachineBlocks.BASIC.frame)
		}

		machineTree(machineBlock)

		def ironAlloyFurnace = create {
			parent machineBlock
			name "ironalloyfurnace"
			icon TRContent.Machine.IRON_ALLOY_FURNACE
			condition inventoryChanged(TRContent.Machine.IRON_ALLOY_FURNACE.block)
		}

		def alloySmelter = create {
			parent ironAlloyFurnace
			name "alloysmelter"
			icon TRContent.Machine.ALLOY_SMELTER
			condition inventoryChanged(TRContent.Machine.ALLOY_SMELTER.block)
		}

		def generator = create {
			parent machineBlock
			name "generator"
			icon TRContent.Machine.SOLID_FUEL_GENERATOR
			condition placedBlock(TRContent.Machine.SOLID_FUEL_GENERATOR.block)
		}

		def windmill = create {
			parent generator
			name "windmill"
			frame AdvancementFrame.GOAL
			icon TRContent.Machine.WIND_MILL
			condition placedBlock(TRContent.Machine.WIND_MILL.block)
		}

		def watermill = create {
			parent windmill
			name "watermill"
			frame AdvancementFrame.GOAL
			icon TRContent.Machine.WATER_MILL
			condition placedBlock(TRContent.Machine.WATER_MILL.block)
		}

		def thermalGenerator = create {
			parent watermill
			name "thermalgenerator"
			frame AdvancementFrame.GOAL
			icon TRContent.Machine.THERMAL_GENERATOR
			condition placedBlock(TRContent.Machine.THERMAL_GENERATOR.block)
		}

		solarTree(machineBlock)
	}

	private void solarTree(AdvancementEntry root) {
		def basicSolar = create {
			parent root
			name "basicsolar"
			icon TRContent.SolarPanels.BASIC
			condition placedBlock(TRContent.SolarPanels.BASIC.block)
		}

		def advancedSolar = create {
			parent basicSolar
			name "advancedsolar"
			icon TRContent.SolarPanels.ADVANCED
			condition placedBlock(TRContent.SolarPanels.ADVANCED.block)
		}

		def industrialSolar = create {
			parent advancedSolar
			name "industrialsolar"
			icon TRContent.SolarPanels.INDUSTRIAL
			frame AdvancementFrame.GOAL
			condition placedBlock(TRContent.SolarPanels.INDUSTRIAL.block)
		}

		def ultimateSolar = create {
			parent industrialSolar
			name "ultimatesolar"
			icon TRContent.SolarPanels.ULTIMATE
			frame AdvancementFrame.CHALLENGE
			condition placedBlock(TRContent.SolarPanels.ULTIMATE.block)
		}

		def quantumSolar = create {
			parent industrialSolar
			name "quantumsolar"
			icon TRContent.SolarPanels.QUANTUM
			frame AdvancementFrame.CHALLENGE
			condition placedBlock(TRContent.SolarPanels.QUANTUM.block)
		}
	}

	private void treeTapTree(AdvancementEntry root) {
		def treeTap = create {
			parent root
			name "treetap"
			icon TRContent.TREE_TAP
			condition inventoryChanged(TRContent.TREE_TAP)
		}

		def sap = create {
			parent treeTap
			name "sap"
			icon TRContent.Parts.SAP
			condition inventoryChanged(TRContent.Parts.SAP)
		}

		def rubber = create {
			parent sap
			name "rubber"
			icon TRContent.Parts.RUBBER
			condition inventoryChanged(TRContent.Parts.RUBBER)
		}

		def copperCable = create {
			parent rubber
			name "coppercable"
			icon TRContent.Cables.COPPER
			condition inventoryChanged(TRContent.Cables.COPPER)
		}

		def upgrade = create {
			parent copperCable
			name "upgrade"
			icon TRContent.Upgrades.OVERCLOCKER

			for (def upgrade in TRContent.Upgrades.values()) {
				condition inventoryChanged(upgrade)
			}
		}

		def cellBattery = create {
			parent copperCable
			name "cellbattery"
			icon TRContent.RED_CELL_BATTERY
			condition inventoryChanged(TRContent.RED_CELL_BATTERY)
		}

		def batBox = create {
			parent cellBattery
			name "batbox"
			icon TRContent.Machine.LOW_VOLTAGE_SU
			condition placedBlock(TRContent.Machine.LOW_VOLTAGE_SU.block)
		}

		def mfe = create {
			parent batBox
			name "mfe"
			icon TRContent.Machine.MEDIUM_VOLTAGE_SU
			frame AdvancementFrame.GOAL
			condition placedBlock(TRContent.Machine.MEDIUM_VOLTAGE_SU.block)
		}

		def lvTransformer = create {
			parent batBox
			name "lvtransformer"
			icon TRContent.Machine.LV_TRANSFORMER
			condition placedBlock(TRContent.Machine.LV_TRANSFORMER.block)
		}

		def futureTransformer = create {
			parent lvTransformer
			name "futuretransformer"
			icon TRContent.Machine.MV_TRANSFORMER
			condition placedBlock(TRContent.Machine.MV_TRANSFORMER.block)
			condition placedBlock(TRContent.Machine.HV_TRANSFORMER.block)
			condition placedBlock(TRContent.Machine.EV_TRANSFORMER.block)
		}

		def mfsu = create {
			parent mfe
			name "mfsu"
			icon TRContent.Machine.HIGH_VOLTAGE_SU
			frame AdvancementFrame.CHALLENGE
			condition placedBlock(TRContent.Machine.HIGH_VOLTAGE_SU.block)
		}

		def idsu = create {
			parent mfsu
			name "interdimensionalsu"
			icon TRContent.Machine.INTERDIMENSIONAL_SU
			frame AdvancementFrame.CHALLENGE
			condition placedBlock(TRContent.Machine.INTERDIMENSIONAL_SU.block)
		}
	}

	private void machineTree(AdvancementEntry root) {
		def ironFurnace = create {
			parent root
			name "ironfurnace"
			icon TRContent.Machine.IRON_FURNACE
			condition placedBlock(TRContent.Machine.IRON_FURNACE.block)
		}

		def electricFurnace = create {
			parent ironFurnace
			name "electricfurnace"
			icon TRContent.Machine.ELECTRIC_FURNACE
			condition placedBlock(TRContent.Machine.ELECTRIC_FURNACE.block)
		}

		def grinder = create {
			parent electricFurnace
			name "grinder"
			icon TRContent.Machine.GRINDER
			condition placedBlock(TRContent.Machine.GRINDER.block)
		}

		def extractor = create {
			parent grinder
			name "extractor"
			icon TRContent.Machine.EXTRACTOR
			condition placedBlock(TRContent.Machine.EXTRACTOR.block)
		}

		def compressor = create {
			parent grinder
			name "compressor"
			icon TRContent.Machine.COMPRESSOR
			condition placedBlock(TRContent.Machine.COMPRESSOR.block)
		}

		def recycler = create {
			parent compressor
			name "recycler"
			icon TRContent.Machine.RECYCLER
			condition placedBlock(TRContent.Machine.RECYCLER.block)
		}

		def scrapboxinator = create {
			parent recycler
			name "scrapboxinator"
			icon TRContent.Machine.SCRAPBOXINATOR
			condition placedBlock(TRContent.Machine.SCRAPBOXINATOR.block)
		}

		def canningMachine = create {
			parent electricFurnace
			name "canningmachine"
			icon TRContent.Machine.SOLID_CANNING_MACHINE
			condition placedBlock(TRContent.Machine.SOLID_CANNING_MACHINE.block)
		}

		def rollingMachine = create {
			parent electricFurnace
			name "rollingmachine"
			icon TRContent.Machine.ROLLING_MACHINE
			condition placedBlock(TRContent.Machine.ROLLING_MACHINE.block)
		}

		def wireMill = create {
			parent electricFurnace
			name "wiremill"
			icon TRContent.Machine.WIRE_MILL
			condition placedBlock(TRContent.Machine.WIRE_MILL.block)
		}

		advancedMachineTreeTree(ironFurnace)
	}

	private void advancedMachineTreeTree(AdvancementEntry root) {
		def advancedMachineBlock = create {
			parent root
			name "advancedmachineblock"
			icon TRContent.MachineBlocks.ADVANCED.frame
			condition placedBlock(TRContent.MachineBlocks.ADVANCED.frame)
		}

		def lightningRod = create {
			parent advancedMachineBlock
			name "lightningrod"
			frame AdvancementFrame.GOAL
			icon TRContent.Machine.LIGHTNING_ROD
			condition placedBlock(TRContent.Machine.LIGHTNING_ROD.block)
		}

		def fusionCoil = create {
			parent advancedMachineBlock
			name "fusioncoil"
			icon TRContent.Machine.FUSION_COIL
			condition placedBlock(TRContent.Machine.FUSION_COIL.block)
		}

		def fusionComputer = create {
			parent fusionCoil
			name "fusioncomputer"
			icon TRContent.Machine.FUSION_CONTROL_COMPUTER
			frame AdvancementFrame.CHALLENGE
			condition placedBlock(TRContent.Machine.FUSION_CONTROL_COMPUTER.block)
		}

		def nuke = create {
			parent fusionCoil
			name "nuke"
			icon TRContent.NUKE
			condition placedBlock(TRContent.NUKE)
			hidden true
		}

		def industrialCentrifuge = create {
			parent advancedMachineBlock
			name "industrialcentrifuge"
			icon TRContent.Machine.INDUSTRIAL_CENTRIFUGE
			frame AdvancementFrame.GOAL
			condition placedBlock(TRContent.Machine.INDUSTRIAL_CENTRIFUGE.block)
		}

		def nickelNugget = create {
			parent industrialCentrifuge
			name "nickelnugget"
			icon TRContent.Nuggets.NICKEL
			condition inventoryChanged(TRContent.Nuggets.NICKEL)
		}

		def blastFurnace = create {
			parent nickelNugget
			name "blastfurnace"
			icon TRContent.Machine.INDUSTRIAL_BLAST_FURNACE
			frame AdvancementFrame.GOAL
			condition placedBlock(TRContent.Machine.INDUSTRIAL_BLAST_FURNACE.block)
		}

		def industrialGrinder = create {
			parent blastFurnace
			name "industrialgrinder"
			icon TRContent.Machine.INDUSTRIAL_GRINDER
			frame AdvancementFrame.GOAL
			condition placedBlock(TRContent.Machine.INDUSTRIAL_GRINDER.block)
		}

		def implosion = create {
			parent industrialGrinder
			name "implosion"
			icon TRContent.Machine.IMPLOSION_COMPRESSOR
			frame AdvancementFrame.GOAL
			condition placedBlock(TRContent.Machine.IMPLOSION_COMPRESSOR.block)
		}

		def quantumArmor = create {
			parent implosion
			name "quantumarmor"
			icon TRContent.QUANTUM_CHESTPLATE
			frame AdvancementFrame.GOAL
			condition inventoryChanged(TRContent.QUANTUM_HELMET)
			condition inventoryChanged(TRContent.QUANTUM_CHESTPLATE)
			condition inventoryChanged(TRContent.QUANTUM_LEGGINGS)
			condition inventoryChanged(TRContent.QUANTUM_BOOTS)
		}

		def industrialMachineBlock = create {
			parent advancedMachineBlock
			name "industrialmachineblock"
			icon TRContent.MachineBlocks.INDUSTRIAL.frame
			condition placedBlock(TRContent.MachineBlocks.INDUSTRIAL.frame)
		}

		def matterFabricator = create {
			parent industrialMachineBlock
			name "matterfabricator"
			icon TRContent.Machine.MATTER_FABRICATOR
			frame AdvancementFrame.CHALLENGE
			condition placedBlock(TRContent.Machine.MATTER_FABRICATOR.block)
		}

		def quantumTank = create {
			parent industrialMachineBlock
			name "quantumtank"
			icon TRContent.TankUnit.QUANTUM
			frame AdvancementFrame.CHALLENGE
			condition placedBlock(TRContent.TankUnit.QUANTUM.block)
		}
	}

	private static AdvancementCriterion<ItemCriterion.Conditions> placedBlock(Block block) {
		return ItemCriterion.Conditions.createPlacedBlock(block)
	}

	private static AdvancementCriterion<InventoryChangedCriterion.Conditions> inventoryChanged(ItemConvertible... items) {
		return InventoryChangedCriterion.Conditions.items(items)
	}

	private static AdvancementCriterion<InventoryChangedCriterion.Conditions> inventoryChanged(TagKey<Item> tag) {
		return InventoryChangedCriterion.Conditions.items(ItemPredicate.Builder.create().tag(tag))
	}

	private AdvancementEntry create(@DelegatesTo(value = AdvancementFactory.class) Closure closure) {
		def factory = new AdvancementFactory()
		closure.setDelegate(factory)
		closure.call(factory)
		def advancement = factory.build()
		consumer.accept(advancement)
		return advancement
	}
}
