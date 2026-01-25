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


import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider
import net.minecraft.world.level.block.Block
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.model.ItemModelUtils
import net.minecraft.client.data.models.model.ModelLocationUtils
import net.minecraft.client.data.models.model.ModelInstance
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.data.BlockFamilies
import net.minecraft.data.BlockFamily
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.tuple.Pair
import techreborn.client.render.ActiveProperty
import techreborn.client.render.ItemBucketModel
import techreborn.client.render.ItemCellModel
import techreborn.client.render.PowerType
import techreborn.init.ModFluids
import techreborn.init.TRContent
import techreborn.init.TRContent.BlockInfo
import techreborn.init.TRContent.FamilyBlockInfo
import techreborn.init.TRContent.MachineBlockInfo
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
import techreborn.init.TRContent.NuclearReactorComponents

import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer
import java.util.function.Consumer

class ModelProvider extends FabricModelProvider {
	static BlockModelGenerators stateGenerator
	static ItemModelGenerators itemGenerator
	static BiConsumer<ResourceLocation, ModelInstance> modelCollector

	ModelProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output)
	}

	static <T extends Enum<T>> void add(Class<T> type, Closure closure) {
		for (final T value in type.enumConstants) {
			closure.call(value)
		}
	}

	static <T> void add(T target, Consumer<T> consumer) {
		consumer.accept(target)
	}

	static <T> void add(List<T> list, Consumer<T> consumer) {
		for (final T value in list) {
			consumer.accept(value)
		}
	}

	@Override
	void generateBlockStateModels(BlockModelGenerators generator) {
		stateGenerator = generator
		modelCollector = generator.modelOutput

		def toOrientable = { Block block ->
			generator.createTrivialBlock(block, TexturedModel.ORIENTABLE_ONLY_TOP)
		}
		def toCubeAll = { BlockInfo info ->
			generator.createTrivialCube(info.block)
		}
		def toBlockCubeAll = generator::createTrivialCube
		def toCubeColumn = { BlockInfo info ->
			generator.createTrivialBlock(info.block, TexturedModel.COLUMN)
		}
		def toCubeBottomTop = { BlockInfo info ->
			generator.createTrivialBlock(info.block, TexturedModel.CUBE_TOP_BOTTOM)
		}
		def toFamilyBlock = { FamilyBlockInfo info ->
			BlockFamily family = BlockFamilies.familyBuilder(info.block).slab(info.slabBlock).stairs(info.stairsBlock).wall(info.wallBlock).getFamily()
			generator.family(info.block).generateFor(family)
		}
		def toFence = { Block block ->
			generator.new BlockModelGenerators.BlockFamilyProvider(TextureMapping.defaultTexture(block)).fence(block)
		}
		def toWall = { Block block ->
			generator.new BlockModelGenerators.BlockFamilyProvider(TextureMapping.cube(block)).wall(block)
		}
		def toRubberLog = { Block block ->
			ResourceLocation vertical = TexturedModel.COLUMN_ALT.create(block, modelCollector)
			ResourceLocation horizontal = TexturedModel.COLUMN_HORIZONTAL_ALT.create(block, modelCollector)
			ResourceLocation with_sap = TemplateModel.RUBBER_LOG_WITH_SAP.upload(block)
			TemplateState.RUBBER_LOG.apply(vertical, horizontal, with_sap).upload(block)
		}
		def toEnergyOrientable = { BlockInfo info ->
			TemplateModel.ENERGY.upload(info.block)
			TemplateState.SINGLE_NORTH_DEFAULT_FACING.upload(info.block)
		}
		def toSolarPanelCubeBottomTop = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.SOLAR_PANEL.apply(info.block)
			)
			TemplateState.ACTIVE.apply(pair).upload(info.block)
		}
		def toQuantumSolarPanel = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.QUANTUM_SOLAR_PANEL.apply(info.block)
			)
			TemplateState.ACTIVE.apply(pair).upload(info.block)
		}
		def toGeneratorCubeBottomTopSingle = { BlockInfo info ->
			TemplateModel.GENERATOR_CUBE_BOTTOM_TOP.upload(info.block)
			TemplateState.SINGLE.upload(info.block)
		}
		def toGeneratorCubeBottomTop = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.GENERATOR_CUBE_BOTTOM_TOP.apply(info.block)
			)
			TemplateState.ACTIVE_NORTH_DEFAULT_H_FACING.apply(pair).upload(info.block)
		}
		def toPlasmaGenerator = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.CUBE_BOTTOM_TOP_SIDE.apply(info.block)
			)
			TemplateState.ACTIVE_NORTH_DEFAULT_H_FACING.apply(pair).upload(info.block)
		}
		def toGasTurbine = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.GAS_TURBINE.apply(info.block)
			)
			TemplateState.ACTIVE_NORTH_DEFAULT_H_FACING.apply(pair).upload(info.block)
		}
		def toFusionControlComputer = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.FUSION_CONTROL_COMPUTER.apply(info.block)
			)
			TemplateState.ACTIVE_NORTH_DEFAULT_H_FACING.apply(pair).upload(info.block)
		}
		def toSolidFuelGenerator = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.SOLID_FUEL_GENERATOR.apply(info.block)
			)
			TemplateState.ACTIVE_NORTH_DEFAULT_H_FACING.apply(pair).upload(info.block)
		}
		def toWindMill = { BlockInfo info ->
			ResourceLocation off = TemplateModel.WIND_MILL.upload(info.block)
			TemplateState.ACTIVE_NORTH_DEFAULT_H_FACING.apply(Pair.of(off, off)).upload(info.block)
		}
		def toAlarm = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.ALARM_LIGHT.apply(info.block)
			)
			TemplateState.ACTIVE_UP_DEFAULT_FACING.apply(pair).upload(info.block)
		}
		def toLampIncandescent = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.LAMP_INCANDESCENT_LIGHT.apply(info.block)
			)
			TemplateState.ACTIVE_UP_DEFAULT_FACING.apply(pair).upload(info.block)
		}
		def toLampLed = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.LAMP_LED_LIGHT.apply(info.block)
			)
			TemplateState.ACTIVE_UP_DEFAULT_FACING.apply(pair).upload(info.block)
		}
		def toMachineBlock = { MachineBlockInfo info ->
			generator.createTrivialCube(info.frame)
			generator.createTrivialCube(info.casing)
		}
		def toBasicTankUnit = { BlockInfo info ->
			TemplateModel.BASIC_TANK_UNIT.upload(info.block)
			TemplateState.SINGLE_NORTH_DEFAULT_H_FACING.upload(info.block)
		}
		def toBasicStorageUnit = { BlockInfo info ->
			TemplateModel.BASIC_STORAGE_UNIT.upload(info.block)
			TemplateState.SINGLE_NORTH_DEFAULT_H_FACING.upload(info.block)
		}
		def toBasicMachine = { BlockInfo info ->
			ResourceLocation off = TemplateModel.BASIC_MACHINE.upload(info.block)
			TemplateState.ACTIVE_NORTH_DEFAULT_H_FACING.apply(Pair.of(off, off)).upload(info.block)
		}
		def toSideFrontTopBottom = { BlockInfo info ->
			TemplateModel.ORIENTABLE_SIDE_FRONT_TOP_BOTTOM.upload(info.block)
			TemplateState.SINGLE_NORTH_DEFAULT_H_FACING.upload(info.block)
		}
		def toActiveBasicMachine = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.BASIC_MACHINE.apply(info.block)
			)
			TemplateState.ACTIVE_NORTH_DEFAULT_H_FACING.apply(pair).upload(info.block)
		}
		def toResinBasin = { BlockInfo info ->
			ResourceLocation empty = TemplateModel.RESIN_BASIN_EMPTY.upload(info.block)
			ResourceLocation flowing = TemplateModel.RESIN_BASIN_FLOWING.upload(info.block)
			ResourceLocation full = TemplateModel.RESIN_BASIN_FULL.upload(info.block)
			TemplateState.RESIN_BASIN.apply(empty, flowing, full).upload(info.block)
		}
		def toAdvancedTankUnit = { BlockInfo info ->
			TemplateModel.ADVANCED_TANK_UNIT.upload(info.block)
			TemplateState.SINGLE_NORTH_DEFAULT_H_FACING.upload(info.block)
		}
		def toAdvancedStorageUnit = { BlockInfo info ->
			TemplateModel.ADVANCED_STORAGE_UNIT.upload(info.block)
			TemplateState.SINGLE_NORTH_DEFAULT_H_FACING.upload(info.block)
		}
		def toActiveAdvancedMachine = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.ADVANCED_MACHINE.apply(info.block)
			)
			TemplateState.ACTIVE_NORTH_DEFAULT_H_FACING.apply(pair).upload(info.block)
		}
		def toAutoCraftingTable = { BlockInfo info ->
			TemplateModel.ADVANCED_MACHINE_WITH_TOP.upload(info.block)
			TemplateState.SINGLE_NORTH_DEFAULT_H_FACING.upload(info.block)
		}
		def toElevator = { BlockInfo info ->
			TemplateModel.ELEVATOR.upload(info.block)
			TemplateState.SINGLE.upload(info.block)
		}
		def toGreenhouseController = { BlockInfo info ->
			ResourceLocation off = TemplateModel.ADVANCED_MACHINE.upload(info.block)
			TemplateState.ACTIVE_NORTH_DEFAULT_H_FACING.apply(Pair.of(off, off)).upload(info.block)
		}
		def toGrinder = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.ADVANCED_MACHINE_WITH_TOP.apply(info.block)
			)
			TemplateState.ACTIVE_NORTH_DEFAULT_H_FACING.apply(pair).upload(info.block)
		}
		def toPlayerDetector = { BlockInfo info ->
			ResourceLocation all = TemplateModel.CUBE_ALL.upload(info.block)
			ResourceLocation others = TemplateModel.PLAYER_DETECTOR_OTHERS.upload(info.block)
			ResourceLocation you = TemplateModel.PLAYER_DETECTOR_YOU.upload(info.block)
			TemplateState.PLAYER_DETECTOR.apply(all, others, you).upload(info.block)
		}
		def toChargeOMat = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.CHARGE_O_MAT.apply(info.block)
			)
			TemplateState.ACTIVE_NORTH_DEFAULT_H_FACING.apply(pair).upload(info.block)
		}
		def toComputerCube = { Block block ->
			TemplateModel.COMPUTER_CUBE.upload(block)
			TemplateState.SINGLE_NORTH_DEFAULT_H_FACING.upload(block)
		}
		def toActiveFrontMachine = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.ACTIVE_FRONT_MACHINE.apply(info.block)
			)
			TemplateState.ACTIVE_NORTH_DEFAULT_H_FACING.apply(pair).upload(info.block)
		}
		def toFishingStation = { BlockInfo info ->
			TemplateModel.FISHING_STATION.upload(info.block)
			TemplateState.SINGLE_NORTH_DEFAULT_H_FACING.upload(info.block)
		}
		def toActiveSideMachine = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.ACTIVE_SIDE_MACHINE.apply(info.block)
			)
			TemplateState.ACTIVE_NORTH_DEFAULT_H_FACING.apply(pair).upload(info.block)
		}
		def toActiveTopFrontMachine = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.ACTIVE_TOP_FRONT_MACHINE.apply(info.block)
			)
			TemplateState.ACTIVE_NORTH_DEFAULT_H_FACING.apply(pair).upload(info.block)
		}
		def toIndustrialTankUnit = { BlockInfo info ->
			TemplateModel.INDUSTRIAL_TANK_UNIT.upload(info.block)
			TemplateState.SINGLE_NORTH_DEFAULT_H_FACING.upload(info.block)
		}
		def toIndustrialStorageUnit = { BlockInfo info ->
			TemplateModel.INDUSTRIAL_STORAGE_UNIT.upload(info.block)
			TemplateState.SINGLE_NORTH_DEFAULT_H_FACING.upload(info.block)
		}
		def toLaunchpad = { BlockInfo info ->
			TemplateModel.LAUNCHPAD.upload(info.block)
			TemplateState.SINGLE.upload(info.block)
		}
		def toPump = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.PUMP.apply(info.block)
			)
			TemplateState.ACTIVE_NORTH_DEFAULT_H_FACING.apply(pair).upload(info.block)
		}
		def toChunkLoader = { BlockInfo info ->
			TemplateModel.CHUNK_LOADER.upload(info.block)
			TemplateState.SINGLE.upload(info.block)
		}
		def toQuantumTankUnit = { BlockInfo info ->
			TemplateModel.QUANTUM_TANK_UNIT.upload(info.block)
			TemplateState.SINGLE_NORTH_DEFAULT_H_FACING.upload(info.block)
		}
		def toQuantumStorageUnit = { BlockInfo info ->
			TemplateModel.QUANTUM_STORAGE_UNIT.upload(info.block)
			TemplateState.SINGLE_NORTH_DEFAULT_H_FACING.upload(info.block)
		}
		def toFluidReplicator = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.FLUID_REPLICATOR.apply(info.block)
			)
			TemplateState.ACTIVE_NORTH_DEFAULT_H_FACING.apply(pair).upload(info.block)
		}
		def toMatterFabricator = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.CUBE_BOTTOM_TOP_SIDE.apply(info.block)
			)
			TemplateState.ACTIVE.apply(pair).upload(info.block)
		}
		def toSolidCanningMachine = { BlockInfo info ->
			ResourceLocation off = ModelLocationUtils.getModelLocation(Machine.COMPRESSOR.block)
			generator.registerSimpleItemModel(info.asItem(), off)
			ResourceLocation on = off.withSuffix("_on")
			TemplateState.ACTIVE_NORTH_DEFAULT_H_FACING.apply(Pair.of(off, on)).upload(info.block)
		}
		def toFluid = { BlockInfo info ->
			TemplateModel.FLUID.upload(info.block)
			TemplateState.SINGLE.upload(info.block)
		}
		def toCable = { BlockInfo info ->
			ResourceLocation core = TemplateModel.CABLE_CORE.upload(info.block)
			ResourceLocation side = TemplateModel.CABLE_SIDE.upload(info.block)
			TemplateState.CABLE.apply(core, side).upload(info.block)
			generator.registerSimpleFlatItemModel(info.asItem())
		}
		def toThickCable = { BlockInfo info ->
			ResourceLocation core = TemplateModel.CABLE_THICK_CORE.upload(info.block)
			ResourceLocation side = TemplateModel.CABLE_THICK_SIDE.upload(info.block)
			TemplateState.CABLE.apply(core, side).upload(info.block)
			generator.registerSimpleFlatItemModel(info.asItem())
		}
		def toNuclearReactor = { BlockInfo info ->
			Pair<ResourceLocation, ResourceLocation> pair = TemplateModel.ACTIVE.upload(
				TemplateModel.NUCLEAR_REACTOR.apply(info.block)
			)
			TemplateState.ACTIVE_NORTH_DEFAULT_H_FACING.apply(pair).upload(info.block)
		}

		add Ores, toCubeAll
		add StorageBlocks, toFamilyBlock
		add ModFluids, toFluid
		add List.of(
			Machine.ADJUSTABLE_SU,
			Machine.EV_TRANSFORMER,
			Machine.HIGH_VOLTAGE_SU,
			Machine.HV_TRANSFORMER,
			Machine.INTERDIMENSIONAL_SU,
			Machine.LAPOTRONIC_SU,
			Machine.LOW_VOLTAGE_SU,
			Machine.LV_TRANSFORMER,
			Machine.MEDIUM_VOLTAGE_SU,
			Machine.MV_TRANSFORMER,
		), toEnergyOrientable
		add Machine.LSU_STORAGE, toCubeAll
		add Machine.REACTOR_CHAMBER, toCubeAll
		add List.of(
			SolarPanels.BASIC,
			SolarPanels.ADVANCED,
			SolarPanels.INDUSTRIAL,
			SolarPanels.ULTIMATE,
		), toSolarPanelCubeBottomTop
		add SolarPanels.QUANTUM, toQuantumSolarPanel
		add List.of(
			SolarPanels.CREATIVE,
			Machine.LIGHTNING_ROD,
		), toGeneratorCubeBottomTopSingle
		add List.of(
			Machine.DIESEL_GENERATOR,
			Machine.DRAGON_EGG_SYPHON,
			Machine.SEMI_FLUID_GENERATOR,
			Machine.THERMAL_GENERATOR,
			Machine.WATER_MILL,
		), toGeneratorCubeBottomTop
		add Machine.FUSION_CONTROL_COMPUTER, toFusionControlComputer
		add Machine.GAS_TURBINE, toGasTurbine
		add Machine.PLASMA_GENERATOR, toPlasmaGenerator
		add Machine.SOLID_FUEL_GENERATOR, toSolidFuelGenerator
		add Machine.WIND_MILL, toWindMill
		add Machine.ALARM, toAlarm
		add Machine.LAMP_INCANDESCENT, toLampIncandescent
		add Machine.LAMP_LED, toLampLed
		add MachineBlocks, toMachineBlock
		add Machine.FUSION_COIL, toCubeColumn
		add List.of(
			Machine.BLOCK_BREAKER,
			Machine.BLOCK_PLACER,
		), toBasicMachine
		add List.of(
			StorageUnit.BUFFER,
			StorageUnit.CRUDE,
		), toSideFrontTopBottom
		add Machine.DRAIN, toCubeBottomTop
		add List.of(
			Machine.IRON_ALLOY_FURNACE,
			Machine.IRON_FURNACE,
		), toActiveBasicMachine
		add Machine.RESIN_BASIN, toResinBasin
		add TankUnit.BASIC, toBasicTankUnit
		add TankUnit.ADVANCED, toAdvancedTankUnit
		add TankUnit.INDUSTRIAL, toIndustrialTankUnit
		add List.of(
			TankUnit.QUANTUM,
			TankUnit.CREATIVE,
		), toQuantumTankUnit
		add StorageUnit.BASIC, toBasicStorageUnit
		add StorageUnit.ADVANCED, toAdvancedStorageUnit
		add StorageUnit.INDUSTRIAL, toIndustrialStorageUnit
		add List.of(
			StorageUnit.QUANTUM,
			StorageUnit.CREATIVE,
		), toQuantumStorageUnit
		add List.of(
			Machine.ALLOY_SMELTER,
			Machine.ASSEMBLY_MACHINE,
			Machine.CHEMICAL_REACTOR,
			Machine.COMPRESSOR,
			Machine.ELECTRIC_FURNACE,
			Machine.EXTRACTOR,
			Machine.RECYCLER,
			Machine.ROLLING_MACHINE,
			Machine.SCRAPBOXINATOR,
			Machine.WIRE_MILL,
		), toActiveAdvancedMachine
		add Machine.AUTO_CRAFTING_TABLE, toAutoCraftingTable
		add Machine.ELEVATOR, toElevator
		add Machine.GREENHOUSE_CONTROLLER, toGreenhouseController
		add Machine.GRINDER, toGrinder
		add Machine.PLAYER_DETECTOR, toPlayerDetector
		add Machine.CHARGE_O_MAT, toChargeOMat
		add List.of(
			Machine.DISTILLATION_TOWER,
			Machine.INDUSTRIAL_ELECTROLYZER,
			Machine.INDUSTRIAL_SAWMILL,
		), toActiveFrontMachine
		add Machine.FISHING_STATION, toFishingStation
		add List.of(
			Machine.IMPLOSION_COMPRESSOR,
			Machine.INDUSTRIAL_BLAST_FURNACE,
		), toActiveSideMachine
		add List.of(
			Machine.INDUSTRIAL_CENTRIFUGE,
			Machine.INDUSTRIAL_GRINDER,
			Machine.VACUUM_FREEZER,
		), toActiveTopFrontMachine
		add Machine.LAUNCHPAD, toLaunchpad
		add Machine.PUMP, toPump
		add Machine.CHUNK_LOADER, toChunkLoader
		add Machine.FLUID_REPLICATOR, toFluidReplicator
		add Machine.MATTER_FABRICATOR, toMatterFabricator
		add Machine.SOLID_CANNING_MACHINE, toSolidCanningMachine
		add List.of(
			Cables.COPPER,
			Cables.TIN,
			Cables.GOLD,
			Cables.HV,
			Cables.GLASSFIBER,
		), toCable
		add List.of(
			Cables.INSULATED_COPPER,
			Cables.INSULATED_GOLD,
			Cables.INSULATED_HV,
			Cables.SUPERCONDUCTOR,
		), toThickCable
		add TRContent.NUKE, toOrientable
		add TRContent.REINFORCED_GLASS, toBlockCubeAll
		add TRContent.COMPUTER_CUBE, toComputerCube
		add TRContent.RUBBER_LOG, toRubberLog
		add TRContent.REFINED_IRON_FENCE, toFence
		add TRContent.COPPER_WALL, toWall
		add Machine.NUCLEAR_REACTOR, toNuclearReactor

		generator.createPlantWithDefaultItem(TRContent.RUBBER_SAPLING, TRContent.POTTED_RUBBER_SAPLING, BlockModelGenerators.PlantType.NOT_TINTED)
		generator.woodProvider(TRContent.RUBBER_LOG).wood(TRContent.RUBBER_WOOD)
		generator.woodProvider(TRContent.RUBBER_LOG_STRIPPED).logWithHorizontal(TRContent.RUBBER_LOG_STRIPPED).wood(TRContent.STRIPPED_RUBBER_WOOD)
		generator.createTrivialBlock(TRContent.RUBBER_LEAVES, TexturedModel.LEAVES)
		BlockFamily family = BlockFamilies.familyBuilder(TRContent.RUBBER_PLANKS)
			.button(TRContent.RUBBER_BUTTON)
			.fence(TRContent.RUBBER_FENCE)
			.fenceGate(TRContent.RUBBER_FENCE_GATE)
			.pressurePlate(TRContent.RUBBER_PRESSURE_PLATE)
			.slab(TRContent.RUBBER_SLAB)
			.stairs(TRContent.RUBBER_STAIR)
			.door(TRContent.RUBBER_DOOR)
			.trapdoor(TRContent.RUBBER_TRAPDOOR)
			.getFamily()
		generator.family(TRContent.RUBBER_PLANKS).generateFor(family)

		// The BasicItemModel model will be automatically registered through the resolveAndValidate function of the ModelProvider class
		// Just make sure Registries.ITEM contains the id of the block
	}

	@Override
	void generateItemModels(ItemModelGenerators generator) {
		itemGenerator = generator
		modelCollector = generator.modelOutput

		def toGenerated = { ItemLike info ->
			generator.generateFlatItem(info.asItem(), ModelTemplates.FLAT_ITEM)
		}
		def toHandheld = { Item item ->
			generator.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM)
		}
		def toEnergyItem = { Item item ->
			ResourceLocation id = TemplateModel.ENERGY_ITEM.upload(item)
			ResourceLocation active = TemplateModel.ENERGY_ITEM_ACTIVE.upload(item)
			generator.itemModelOutput.accept(item, ItemModelUtils.select(
				new ActiveProperty(),
				ItemModelUtils.plainModel(id),
				ItemModelUtils.when(PowerType.ON, ItemModelUtils.plainModel(active))
			))
		}
		def toHandheldEnergyItem = { Item item ->
			ResourceLocation id = TemplateModel.ENERGY_ITEM_HANDHELD.upload(item)
			ResourceLocation active = TemplateModel.ENERGY_ITEM_HANDHELD_ACTIVE.upload(item)
			generator.itemModelOutput.accept(item, ItemModelUtils.select(
				new ActiveProperty(),
				ItemModelUtils.plainModel(id),
				ItemModelUtils.when(PowerType.ON, ItemModelUtils.plainModel(active))
			))
		}
		def toNanosaber = { Item item ->
			ResourceLocation off = TemplateModel.NANOSABER_OFF.upload(item)
			ResourceLocation on = TemplateModel.NANOSABER_ON.upload(item)
			ResourceLocation low = TemplateModel.NANOSABER_LOW.upload(item)
			generator.itemModelOutput.accept(item, ItemModelUtils.select(
				new ActiveProperty(),
				ItemModelUtils.plainModel(off),
				ItemModelUtils.when(PowerType.ON, ItemModelUtils.plainModel(on)),
				ItemModelUtils.when(PowerType.LOW, ItemModelUtils.plainModel(low)),
			))
		}
		def toCell = { Item item ->
			generator.itemModelOutput.accept(item, new ItemCellModel.Unbaked())
			TemplateModel.CELL_BASE.upload(item)
			TemplateModel.CELL_BACKGROUND.upload(item)
			TemplateModel.CELL_GLASS.upload(item)
		}
		def toFluidBucket = { ModFluids modFluids ->
			generator.itemModelOutput.accept(modFluids.bucket, new ItemBucketModel.Unbaked(modFluids.fluid))
		}
		def toBucket = { ResourceLocation id ->
			TemplateModel.BUCKET_BASE.upload(id)
			TemplateModel.BUCKET_BACKGROUND.upload(id)
		}

		add Dusts, toGenerated
		add SmallDusts, toGenerated
		add Ingots, toGenerated
		add Plates, toGenerated
		add Nuggets, toGenerated
		add Parts, toGenerated
		add RawMetals, toGenerated
		add Gems, toGenerated
		add Upgrades, toGenerated
		add NuclearReactorComponents, toGenerated
		add List.of(
			StorageUnit.CRUDE.upgrader.get(),
			StorageUnit.BASIC.upgrader.get(),
			StorageUnit.ADVANCED.upgrader.get(),
			StorageUnit.INDUSTRIAL.upgrader.get(),
			TRContent.TREE_TAP,
			TRContent.SCRAP_BOX,
			TRContent.WRENCH,
			TRContent.PAINTING_TOOL,
			TRContent.BRONZE_BOOTS,
			TRContent.BRONZE_HELMET,
			TRContent.BRONZE_CHESTPLATE,
			TRContent.BRONZE_LEGGINGS,
			TRContent.RUBY_BOOTS,
			TRContent.RUBY_HELMET,
			TRContent.RUBY_CHESTPLATE,
			TRContent.RUBY_LEGGINGS,
			TRContent.SAPPHIRE_BOOTS,
			TRContent.SAPPHIRE_HELMET,
			TRContent.SAPPHIRE_CHESTPLATE,
			TRContent.SAPPHIRE_LEGGINGS,
			TRContent.PERIDOT_BOOTS,
			TRContent.PERIDOT_HELMET,
			TRContent.PERIDOT_CHESTPLATE,
			TRContent.PERIDOT_LEGGINGS,
			TRContent.SILVER_BOOTS,
			TRContent.SILVER_HELMET,
			TRContent.SILVER_CHESTPLATE,
			TRContent.SILVER_LEGGINGS,
			TRContent.STEEL_BOOTS,
			TRContent.STEEL_HELMET,
			TRContent.STEEL_CHESTPLATE,
			TRContent.STEEL_LEGGINGS,
			TRContent.QUANTUM_BOOTS,
			TRContent.QUANTUM_HELMET,
			TRContent.QUANTUM_CHESTPLATE,
			TRContent.QUANTUM_LEGGINGS,
			TRContent.NANO_BOOTS,
			TRContent.NANO_HELMET,
			TRContent.NANO_CHESTPLATE,
			TRContent.NANO_LEGGINGS,
			TRContent.LAPOTRONIC_ORBPACK,
			TRContent.CLOAKING_DEVICE,
			TRContent.GPS,
			TRContent.MANUAL,
			TRContent.DEBUG_TOOL,
		), toGenerated
		add List.of(
			TRContent.ELECTRIC_TREE_TAP,
			TRContent.BRONZE_SWORD,
			TRContent.BRONZE_PICKAXE,
			TRContent.BRONZE_AXE,
			TRContent.BRONZE_HOE,
			TRContent.BRONZE_SPADE,
			TRContent.RUBY_SWORD,
			TRContent.RUBY_PICKAXE,
			TRContent.RUBY_AXE,
			TRContent.RUBY_HOE,
			TRContent.RUBY_SPADE,
			TRContent.SAPPHIRE_SWORD,
			TRContent.SAPPHIRE_PICKAXE,
			TRContent.SAPPHIRE_AXE,
			TRContent.SAPPHIRE_HOE,
			TRContent.SAPPHIRE_SPADE,
			TRContent.PERIDOT_SWORD,
			TRContent.PERIDOT_PICKAXE,
			TRContent.PERIDOT_AXE,
			TRContent.PERIDOT_HOE,
			TRContent.PERIDOT_SPADE,
			TRContent.BASIC_DRILL,
			TRContent.BASIC_JACKHAMMER,
			TRContent.ADVANCED_DRILL,
			TRContent.ADVANCED_JACKHAMMER,
			TRContent.INDUSTRIAL_DRILL,
			TRContent.INDUSTRIAL_JACKHAMMER,
			TRContent.ROCK_CUTTER,
			TRContent.OMNI_TOOL,
		), toHandheld
		add List.of(
			TRContent.BASIC_CHAINSAW,
			TRContent.ADVANCED_CHAINSAW,
			TRContent.INDUSTRIAL_CHAINSAW,
		), toHandheldEnergyItem
		add List.of(
			TRContent.LITHIUM_ION_BATPACK,
			TRContent.RED_CELL_BATTERY,
			TRContent.LITHIUM_ION_BATTERY,
			TRContent.ENERGY_CRYSTAL,
			TRContent.LAPOTRON_CRYSTAL,
			TRContent.LAPOTRONIC_ORB,
			TRContent.FREQUENCY_TRANSMITTER,
		), toEnergyItem
		add TRContent.NANOSABER, toNanosaber
		add TRContent.CELL, toCell
		add ModFluids, toFluidBucket
		add ItemBucketModel.BUCKET, toBucket
	}
}
