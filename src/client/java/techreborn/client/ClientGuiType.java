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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.util.Identifier;
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
import techreborn.blockentity.machine.multiblock.*;
import techreborn.blockentity.machine.tier0.block.BlockBreakerBlockEntity;
import techreborn.blockentity.machine.tier0.block.BlockPlacerBlockEntity;
import techreborn.blockentity.machine.tier1.*;
import techreborn.blockentity.machine.tier2.FishingStationBlockEntity;
import techreborn.blockentity.machine.tier2.LaunchpadBlockEntity;
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
import techreborn.client.gui.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ClientGuiType<T extends BlockEntity> {
	public static final Map<Identifier, ClientGuiType<?>> TYPES = new HashMap<>();

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

	private static <T extends BlockEntity> ClientGuiType<T> register(GuiType<T> type, GuiFactory<T> factory) {
		return new ClientGuiType<>(type, factory);
	}

	public static void validate() {
		// Ensure all gui types also have a client version.
		for (Identifier identifier : GuiType.TYPES.keySet()) {
			Objects.requireNonNull(TYPES.get(identifier), "No ClientGuiType for " + identifier);
		}
	}

	private final GuiType<T> guiType;
	private final GuiFactory<T> guiFactory;

	public ClientGuiType(GuiType<T> guiType, GuiFactory<T> guiFactory) {
		this.guiType = Objects.requireNonNull(guiType);
		this.guiFactory = Objects.requireNonNull(guiFactory);

		HandledScreens.register(guiType.getScreenHandlerType(), getGuiFactory());

		TYPES.put(guiType.getIdentifier(), this);
	}

	public GuiType<T> getGuiType() {
		return guiType;
	}

	public GuiFactory<T> getGuiFactory() {
		return guiFactory;
	}
}
