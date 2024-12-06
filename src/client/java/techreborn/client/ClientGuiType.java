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

package techreborn.client;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import techreborn.blockentity.GuiType;
import techreborn.blockentity.generator.PlasmaGeneratorBlockEntity;
import techreborn.blockentity.generator.SolarPanelBlockEntity;
import techreborn.blockentity.generator.advanced.DieselGeneratorBlockEntity;
import techreborn.blockentity.generator.advanced.GasTurbineBlockEntity;
import techreborn.blockentity.generator.advanced.SemiFluidGeneratorBlockEntity;
import techreborn.blockentity.generator.advanced.ThermalGeneratorBlockEntity;
import techreborn.blockentity.generator.basic.SolidFuelGeneratorBlockEntity;
import techreborn.blockentity.machine.iron.IronAlloyFurnaceBlockEntity;
import techreborn.blockentity.machine.iron.IronFurnaceBlockEntity;
import techreborn.blockentity.machine.misc.ChargeOMatBlockEntity;
import techreborn.blockentity.machine.multiblock.DistillationTowerBlockEntity;
import techreborn.blockentity.machine.multiblock.FluidReplicatorBlockEntity;
import techreborn.blockentity.machine.multiblock.FusionControlComputerBlockEntity;
import techreborn.blockentity.machine.multiblock.ImplosionCompressorBlockEntity;
import techreborn.blockentity.machine.multiblock.IndustrialBlastFurnaceBlockEntity;
import techreborn.blockentity.machine.multiblock.IndustrialGrinderBlockEntity;
import techreborn.blockentity.machine.multiblock.IndustrialSawmillBlockEntity;
import techreborn.blockentity.machine.multiblock.VacuumFreezerBlockEntity;
import techreborn.blockentity.machine.tier0.block.BlockBreakerBlockEntity;
import techreborn.blockentity.machine.tier0.block.BlockPlacerBlockEntity;
import techreborn.blockentity.machine.tier1.AlloySmelterBlockEntity;
import techreborn.blockentity.machine.tier1.AssemblingMachineBlockEntity;
import techreborn.blockentity.machine.tier1.AutoCraftingTableBlockEntity;
import techreborn.blockentity.machine.tier1.ChemicalReactorBlockEntity;
import techreborn.blockentity.machine.tier1.CompressorBlockEntity;
import techreborn.blockentity.machine.tier1.ElectricFurnaceBlockEntity;
import techreborn.blockentity.machine.tier1.ElevatorBlockEntity;
import techreborn.blockentity.machine.tier1.ExtractorBlockEntity;
import techreborn.blockentity.machine.tier1.GreenhouseControllerBlockEntity;
import techreborn.blockentity.machine.tier1.GrinderBlockEntity;
import techreborn.blockentity.machine.tier1.IndustrialElectrolyzerBlockEntity;
import techreborn.blockentity.machine.tier1.PlayerDetectorBlockEntity;
import techreborn.blockentity.machine.tier1.RecyclerBlockEntity;
import techreborn.blockentity.machine.tier1.RollingMachineBlockEntity;
import techreborn.blockentity.machine.tier1.ScrapboxinatorBlockEntity;
import techreborn.blockentity.machine.tier1.SolidCanningMachineBlockEntity;
import techreborn.blockentity.machine.tier1.WireMillBlockEntity;
import techreborn.blockentity.machine.tier2.FishingStationBlockEntity;
import techreborn.blockentity.machine.tier2.LaunchpadBlockEntity;
import techreborn.blockentity.machine.tier2.MinerBlockEntity;
import techreborn.blockentity.machine.tier2.PumpBlockEntity;
import techreborn.blockentity.machine.tier3.ChunkLoaderBlockEntity;
import techreborn.blockentity.machine.tier3.IndustrialCentrifugeBlockEntity;
import techreborn.blockentity.machine.tier3.MatterFabricatorBlockEntity;
import techreborn.blockentity.storage.energy.AdjustableSUBlockEntity;
import techreborn.blockentity.storage.energy.HighVoltageSUBlockEntity;
import techreborn.blockentity.storage.energy.LowVoltageSUBlockEntity;
import techreborn.blockentity.storage.energy.MediumVoltageSUBlockEntity;
import techreborn.blockentity.storage.energy.idsu.InterdimensionalSUBlockEntity;
import techreborn.blockentity.storage.energy.lesu.LapotronicSUBlockEntity;
import techreborn.blockentity.storage.fluid.TankUnitBaseBlockEntity;
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;
import techreborn.client.gui.GuiAESU;
import techreborn.client.gui.GuiAlloyFurnace;
import techreborn.client.gui.GuiAlloySmelter;
import techreborn.client.gui.GuiAssemblingMachine;
import techreborn.client.gui.GuiAutoCrafting;
import techreborn.client.gui.GuiBatbox;
import techreborn.client.gui.GuiBlastFurnace;
import techreborn.client.gui.GuiBlockBreaker;
import techreborn.client.gui.GuiBlockPlacer;
import techreborn.client.gui.GuiCentrifuge;
import techreborn.client.gui.GuiChargeBench;
import techreborn.client.gui.GuiChemicalReactor;
import techreborn.client.gui.GuiChunkLoader;
import techreborn.client.gui.GuiCompressor;
import techreborn.client.gui.GuiDieselGenerator;
import techreborn.client.gui.GuiDistillationTower;
import techreborn.client.gui.GuiElectricFurnace;
import techreborn.client.gui.GuiElevator;
import techreborn.client.gui.GuiExtractor;
import techreborn.client.gui.GuiFishingStation;
import techreborn.client.gui.GuiFluidReplicator;
import techreborn.client.gui.GuiFusionReactor;
import techreborn.client.gui.GuiGasTurbine;
import techreborn.client.gui.GuiGenerator;
import techreborn.client.gui.GuiGreenhouseController;
import techreborn.client.gui.GuiGrinder;
import techreborn.client.gui.GuiIDSU;
import techreborn.client.gui.GuiImplosionCompressor;
import techreborn.client.gui.GuiIndustrialElectrolyzer;
import techreborn.client.gui.GuiIndustrialGrinder;
import techreborn.client.gui.GuiIndustrialSawmill;
import techreborn.client.gui.GuiIronFurnace;
import techreborn.client.gui.GuiLESU;
import techreborn.client.gui.GuiLaunchpad;
import techreborn.client.gui.GuiMFE;
import techreborn.client.gui.GuiMFSU;
import techreborn.client.gui.GuiMatterFabricator;
import techreborn.client.gui.GuiPlasmaGenerator;
import techreborn.client.gui.GuiPlayerDetector;
import techreborn.client.gui.GuiPump;
import techreborn.client.gui.GuiRecycler;
import techreborn.client.gui.GuiRollingMachine;
import techreborn.client.gui.GuiScrapboxinator;
import techreborn.client.gui.GuiSemifluidGenerator;
import techreborn.client.gui.GuiSolar;
import techreborn.client.gui.GuiSolidCanningMachine;
import techreborn.client.gui.GuiStorageUnit;
import techreborn.client.gui.GuiTankUnit;
import techreborn.client.gui.GuiThermalGenerator;
import techreborn.client.gui.GuiVacuumFreezer;
import techreborn.client.gui.GuiWireMill;
import techreborn.client.gui.GuiMiner;

import java.util.Objects;

@SuppressWarnings("unused")
public record ClientGuiType<T extends BlockEntity>(GuiType<T> guiType, GuiFactory<T> guiFactory) {
	public static final ClientGuiType<AdjustableSUBlockEntity> AESU = register(GuiType.AESU, GuiAESU::new);
	public static final ClientGuiType<IronAlloyFurnaceBlockEntity> ALLOY_FURNACE = register(GuiType.ALLOY_FURNACE, GuiAlloyFurnace::new);
	public static final ClientGuiType<AlloySmelterBlockEntity> ALLOY_SMELTER = register(GuiType.ALLOY_SMELTER, GuiAlloySmelter::new);
	public static final ClientGuiType<AssemblingMachineBlockEntity> ASSEMBLING_MACHINE = register(GuiType.ASSEMBLING_MACHINE, GuiAssemblingMachine::new);
	public static final ClientGuiType<LowVoltageSUBlockEntity> LOW_VOLTAGE_SU = register(GuiType.LOW_VOLTAGE_SU, GuiBatbox::new);
	public static final ClientGuiType<AutoCraftingTableBlockEntity> AUTO_CRAFTING_TABLE = register(GuiType.AUTO_CRAFTING_TABLE, GuiAutoCrafting::new);
	public static final ClientGuiType<IndustrialBlastFurnaceBlockEntity> BLAST_FURNACE = register(GuiType.BLAST_FURNACE, GuiBlastFurnace::new);
	public static final ClientGuiType<IndustrialCentrifugeBlockEntity> CENTRIFUGE = register(GuiType.CENTRIFUGE, GuiCentrifuge::new);
	public static final ClientGuiType<ChargeOMatBlockEntity> CHARGEBENCH = register(GuiType.CHARGEBENCH, GuiChargeBench::new);
	public static final ClientGuiType<ChemicalReactorBlockEntity> CHEMICAL_REACTOR = register(GuiType.CHEMICAL_REACTOR, GuiChemicalReactor::new);
	public static final ClientGuiType<ChunkLoaderBlockEntity> CHUNK_LOADER = register(GuiType.CHUNK_LOADER, GuiChunkLoader::new);
	public static final ClientGuiType<CompressorBlockEntity> COMPRESSOR = register(GuiType.COMPRESSOR, GuiCompressor::new);
	public static final ClientGuiType<DieselGeneratorBlockEntity> DIESEL_GENERATOR = register(GuiType.DIESEL_GENERATOR, GuiDieselGenerator::new);
	public static final ClientGuiType<DistillationTowerBlockEntity> DISTILLATION_TOWER = register(GuiType.DISTILLATION_TOWER, GuiDistillationTower::new);
	public static final ClientGuiType<ElectricFurnaceBlockEntity> ELECTRIC_FURNACE = register(GuiType.ELECTRIC_FURNACE, GuiElectricFurnace::new);
	public static final ClientGuiType<ExtractorBlockEntity> EXTRACTOR = register(GuiType.EXTRACTOR, GuiExtractor::new);
	public static final ClientGuiType<GrinderBlockEntity> GRINDER = register(GuiType.GRINDER, GuiGrinder::new);
	public static final ClientGuiType<FusionControlComputerBlockEntity> FUSION_CONTROLLER = register(GuiType.FUSION_CONTROLLER, GuiFusionReactor::new);
	public static final ClientGuiType<GasTurbineBlockEntity> GAS_TURBINE = register(GuiType.GAS_TURBINE, GuiGasTurbine::new);
	public static final ClientGuiType<SolidFuelGeneratorBlockEntity> GENERATOR = register(GuiType.GENERATOR, GuiGenerator::new);
	public static final ClientGuiType<HighVoltageSUBlockEntity> HIGH_VOLTAGE_SU = register(GuiType.HIGH_VOLTAGE_SU, GuiMFSU::new);
	public static final ClientGuiType<InterdimensionalSUBlockEntity> IDSU = register(GuiType.IDSU, GuiIDSU::new);
	public static final ClientGuiType<ImplosionCompressorBlockEntity> IMPLOSION_COMPRESSOR = register(GuiType.IMPLOSION_COMPRESSOR, GuiImplosionCompressor::new);
	public static final ClientGuiType<IndustrialElectrolyzerBlockEntity> INDUSTRIAL_ELECTROLYZER = register(GuiType.INDUSTRIAL_ELECTROLYZER, GuiIndustrialElectrolyzer::new);
	public static final ClientGuiType<IndustrialGrinderBlockEntity> INDUSTRIAL_GRINDER = register(GuiType.INDUSTRIAL_GRINDER, GuiIndustrialGrinder::new);
	public static final ClientGuiType<LapotronicSUBlockEntity> LESU = register(GuiType.LESU, GuiLESU::new);
	public static final ClientGuiType<MatterFabricatorBlockEntity> MATTER_FABRICATOR = register(GuiType.MATTER_FABRICATOR, GuiMatterFabricator::new);
	public static final ClientGuiType<MediumVoltageSUBlockEntity> MEDIUM_VOLTAGE_SU = register(GuiType.MEDIUM_VOLTAGE_SU, GuiMFE::new);
	public static final ClientGuiType<PlasmaGeneratorBlockEntity> PLASMA_GENERATOR = register(GuiType.PLASMA_GENERATOR, GuiPlasmaGenerator::new);
	public static final ClientGuiType<IronFurnaceBlockEntity> IRON_FURNACE = register(GuiType.IRON_FURNACE, GuiIronFurnace::new);
	public static final ClientGuiType<StorageUnitBaseBlockEntity> STORAGE_UNIT = register(GuiType.STORAGE_UNIT, GuiStorageUnit::new);
	public static final ClientGuiType<TankUnitBaseBlockEntity> TANK_UNIT = register(GuiType.TANK_UNIT, GuiTankUnit::new);
	public static final ClientGuiType<RecyclerBlockEntity> RECYCLER = register(GuiType.RECYCLER, GuiRecycler::new);
	public static final ClientGuiType<RollingMachineBlockEntity> ROLLING_MACHINE = register(GuiType.ROLLING_MACHINE, GuiRollingMachine::new);
	public static final ClientGuiType<IndustrialSawmillBlockEntity> SAWMILL = register(GuiType.SAWMILL, GuiIndustrialSawmill::new);
	public static final ClientGuiType<ScrapboxinatorBlockEntity> SCRAPBOXINATOR = register(GuiType.SCRAPBOXINATOR, GuiScrapboxinator::new);
	public static final ClientGuiType<SolarPanelBlockEntity> SOLAR_PANEL = register(GuiType.SOLAR_PANEL, GuiSolar::new);
	public static final ClientGuiType<SemiFluidGeneratorBlockEntity> SEMIFLUID_GENERATOR = register(GuiType.SEMIFLUID_GENERATOR, GuiSemifluidGenerator::new);
	public static final ClientGuiType<ThermalGeneratorBlockEntity> THERMAL_GENERATOR = register(GuiType.THERMAL_GENERATOR, GuiThermalGenerator::new);
	public static final ClientGuiType<VacuumFreezerBlockEntity> VACUUM_FREEZER = register(GuiType.VACUUM_FREEZER, GuiVacuumFreezer::new);
	public static final ClientGuiType<SolidCanningMachineBlockEntity> SOLID_CANNING_MACHINE = register(GuiType.SOLID_CANNING_MACHINE, GuiSolidCanningMachine::new);
	public static final ClientGuiType<WireMillBlockEntity> WIRE_MILL = register(GuiType.WIRE_MILL, GuiWireMill::new);
	public static final ClientGuiType<GreenhouseControllerBlockEntity> GREENHOUSE_CONTROLLER = register(GuiType.GREENHOUSE_CONTROLLER, GuiGreenhouseController::new);
	public static final ClientGuiType<FluidReplicatorBlockEntity> FLUID_REPLICATOR = register(GuiType.FLUID_REPLICATOR, GuiFluidReplicator::new);
	public static final ClientGuiType<PlayerDetectorBlockEntity> PLAYER_DETECTOR = register(GuiType.PLAYER_DETECTOR, GuiPlayerDetector::new);
	public static final ClientGuiType<BlockBreakerBlockEntity> BLOCK_BREAKER = register(GuiType.BLOCK_BREAKER, GuiBlockBreaker::new);
	public static final ClientGuiType<BlockPlacerBlockEntity> BLOCK_PLACER = register(GuiType.BLOCK_PLACER, GuiBlockPlacer::new);
	public static final ClientGuiType<LaunchpadBlockEntity> LAUNCHPAD = register(GuiType.LAUNCHPAD, GuiLaunchpad::new);
	public static final ClientGuiType<ElevatorBlockEntity> ELEVATOR = register(GuiType.ELEVATOR, GuiElevator::new);
	public static final ClientGuiType<FishingStationBlockEntity> FISHING_STATION = register(GuiType.FISHING_STATION, GuiFishingStation::new);
	public static final ClientGuiType<PumpBlockEntity> PUMP = register(GuiType.PUMP, GuiPump::new);
	public static final ClientGuiType<MinerBlockEntity> MINER = register(GuiType.MINER, GuiMiner::new);

	public static <T extends BlockEntity> ClientGuiType<T> register(GuiType<T> type, GuiFactory<T> factory) {
		return new ClientGuiType<>(type, factory);
	}

	public ClientGuiType(GuiType<T> guiType, GuiFactory<T> guiFactory) {
		this.guiType = Objects.requireNonNull(guiType);
		this.guiFactory = Objects.requireNonNull(guiFactory);

		HandledScreens.register(guiType.getScreenHandlerType(), guiFactory());
	}
}
