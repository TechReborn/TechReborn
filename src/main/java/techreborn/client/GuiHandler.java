/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import reborncore.RebornCore;
import reborncore.client.containerBuilder.IContainerProvider;
import techreborn.blockentity.machine.misc.ChargeOMatBlockEntity;
import techreborn.blockentity.machine.tier3.IndustrialCentrifugeBlockEntity;
import techreborn.blockentity.storage.fluid.TankUnitBaseBlockEntity;
import techreborn.blockentity.data.DataDrivenBEProvider;
import techreborn.blockentity.data.DataDrivenGui;
import techreborn.blockentity.machine.multiblock.FusionControlComputerBlockEntity;
import techreborn.blockentity.generator.PlasmaGeneratorBlockEntity;
import techreborn.blockentity.generator.SolarPanelBlockEntity;
import techreborn.blockentity.generator.advanced.DieselGeneratorBlockEntity;
import techreborn.blockentity.generator.advanced.GasTurbineBlockEntity;
import techreborn.blockentity.generator.advanced.SemiFluidGeneratorBlockEntity;
import techreborn.blockentity.generator.advanced.ThermalGeneratorBlockEntity;
import techreborn.blockentity.generator.basic.SolidFuelGeneratorBlockEntity;
import techreborn.blockentity.machine.iron.IronAlloyFurnaceBlockEntity;
import techreborn.blockentity.machine.iron.IronFurnaceBlockEntity;
import techreborn.blockentity.machine.multiblock.*;
import techreborn.blockentity.machine.tier1.*;
import techreborn.blockentity.machine.tier3.ChunkLoaderBlockEntity;
import techreborn.blockentity.machine.tier3.MatterFabricatorBlockEntity;
import techreborn.blockentity.storage.energy.AdjustableSUBlockEntity;
import techreborn.blockentity.storage.energy.HighVoltageSUBlockEntity;
import techreborn.blockentity.storage.energy.LowVoltageSUBlockEntity;
import techreborn.blockentity.storage.energy.MediumVoltageSUBlockEntity;
import techreborn.blockentity.storage.energy.idsu.InterdimensionalSUBlockEntity;
import techreborn.blockentity.storage.energy.lesu.LapotronicSUBlockEntity;
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;
import techreborn.client.gui.*;

public class GuiHandler {

	public static void register(){

		EGui.stream().forEach(gui -> ContainerProviderRegistry.INSTANCE.registerFactory(gui.getID(), (syncID, identifier, playerEntity, packetByteBuf) -> {
			final BlockEntity blockEntity = playerEntity.world.getBlockEntity(packetByteBuf.readBlockPos());
			return ((IContainerProvider) blockEntity).createContainer(syncID, playerEntity);
		}));

		RebornCore.clientOnly(() -> () -> EGui.stream().forEach(gui -> ScreenProviderRegistry.INSTANCE.registerFactory(gui.getID(), (syncID, identifier, playerEntity, packetByteBuf) -> getClientGuiElement(EGui.byID(identifier), playerEntity, packetByteBuf.readBlockPos(), syncID))));
	}

	@Environment(EnvType.CLIENT)
	private static AbstractContainerScreen<?> getClientGuiElement(final EGui gui, final PlayerEntity player, BlockPos pos, int syncID) {
		final BlockEntity blockEntity = player.world.getBlockEntity(pos);

		if(blockEntity instanceof DataDrivenBEProvider.DataDrivenBlockEntity){
			return new DataDrivenGui(syncID, player, (DataDrivenBEProvider.DataDrivenBlockEntity) blockEntity);
		}

		switch (gui) {
			case AESU:
				return new GuiAESU(syncID, player, (AdjustableSUBlockEntity) blockEntity);
			case ALLOY_FURNACE:
				return new GuiAlloyFurnace(syncID, player, (IronAlloyFurnaceBlockEntity) blockEntity);
			case ALLOY_SMELTER:
				return new GuiAlloySmelter(syncID, player, (AlloySmelterBlockEntity) blockEntity);
			case ASSEMBLING_MACHINE:
				return new GuiAssemblingMachine(syncID, player, (AssemblingMachineBlockEntity) blockEntity);
			case LOW_VOLTAGE_SU:
				return new GuiBatbox(syncID, player, (LowVoltageSUBlockEntity) blockEntity);
			case BLAST_FURNACE:
				return new GuiBlastFurnace(syncID, player, (IndustrialBlastFurnaceBlockEntity) blockEntity);
			case CENTRIFUGE:
				return new GuiCentrifuge(syncID, player, (IndustrialCentrifugeBlockEntity) blockEntity);
			case CHARGEBENCH:
				return new GuiChargeBench(syncID, player, (ChargeOMatBlockEntity) blockEntity);
			case CHEMICAL_REACTOR:
				return new GuiChemicalReactor(syncID, player, (ChemicalReactorBlockEntity) blockEntity);
			case CHUNK_LOADER:
				return new GuiChunkLoader(syncID, player, (ChunkLoaderBlockEntity) blockEntity);
			case COMPRESSOR:
				return new GuiCompressor(syncID, player, (CompressorBlockEntity) blockEntity);
			case DIESEL_GENERATOR:
				return new GuiDieselGenerator(syncID, player, (DieselGeneratorBlockEntity) blockEntity);
			case ELECTRIC_FURNACE:
				return new GuiElectricFurnace(syncID, player, (ElectricFurnaceBlockEntity) blockEntity);
			case EXTRACTOR:
				return new GuiExtractor(syncID, player, (ExtractorBlockEntity) blockEntity);
			case FUSION_CONTROLLER:
				return new GuiFusionReactor(syncID, player, (FusionControlComputerBlockEntity) blockEntity);
			case GAS_TURBINE:
				return new GuiGasTurbine(syncID, player, (GasTurbineBlockEntity) blockEntity);
			case GENERATOR:
				return new GuiGenerator(syncID, player, (SolidFuelGeneratorBlockEntity) blockEntity);
			case IDSU:
				return new GuiIDSU(syncID, player, (InterdimensionalSUBlockEntity) blockEntity);
			case IMPLOSION_COMPRESSOR:
				return new GuiImplosionCompressor(syncID, player, (ImplosionCompressorBlockEntity) blockEntity);
			case INDUSTRIAL_ELECTROLYZER:
				return new GuiIndustrialElectrolyzer(syncID, player, (IndustrialElectrolyzerBlockEntity) blockEntity);
			case INDUSTRIAL_GRINDER:
				return new GuiIndustrialGrinder(syncID, player, (IndustrialGrinderBlockEntity) blockEntity);
			case IRON_FURNACE:
				return new GuiIronFurnace(syncID, player, (IronFurnaceBlockEntity) blockEntity);
			case LESU:
				return new GuiLESU(syncID, player, (LapotronicSUBlockEntity) blockEntity);
			case MATTER_FABRICATOR:
				return new GuiMatterFabricator(syncID, player, (MatterFabricatorBlockEntity) blockEntity);
			case MEDIUM_VOLTAGE_SU:
				return new GuiMFE(syncID, player, (MediumVoltageSUBlockEntity) blockEntity);
			case HIGH_VOLTAGE_SU:
				return new GuiMFSU(syncID, player, (HighVoltageSUBlockEntity) blockEntity);
			case STORAGE_UNIT:
				return new GuiStorageUnit(syncID, player, (StorageUnitBaseBlockEntity) blockEntity);
			case TANK_UNIT:
				return new GuiTankUnit(syncID, player, (TankUnitBaseBlockEntity) blockEntity);
			case RECYCLER:
				return new GuiRecycler(syncID, player, (RecyclerBlockEntity) blockEntity);
			case ROLLING_MACHINE:
				return new GuiRollingMachine(syncID, player, (RollingMachineBlockEntity) blockEntity);
			case SAWMILL:
				return new GuiIndustrialSawmill(syncID, player, (IndustrialSawmillBlockEntity) blockEntity);
			case SCRAPBOXINATOR:
				return new GuiScrapboxinator(syncID, player, (ScrapboxinatorBlockEntity) blockEntity);
			case SOLAR_PANEL:
				return new GuiSolar(syncID, player, (SolarPanelBlockEntity) blockEntity);
			case SEMIFLUID_GENERATOR:
				return new GuiSemifluidGenerator(syncID, player, (SemiFluidGeneratorBlockEntity) blockEntity);
			case THERMAL_GENERATOR:
				return new GuiThermalGenerator(syncID, player, (ThermalGeneratorBlockEntity) blockEntity);
			case VACUUM_FREEZER:
				return new GuiVacuumFreezer(syncID, player, (VacuumFreezerBlockEntity) blockEntity);
			case AUTO_CRAFTING_TABLE:
				return new GuiAutoCrafting(syncID, player, (AutoCraftingTableBlockEntity) blockEntity);
			case PLASMA_GENERATOR:
				return new GuiPlasmaGenerator(syncID, player, (PlasmaGeneratorBlockEntity) blockEntity);
			case DISTILLATION_TOWER:
				return new GuiDistillationTower(syncID, player, (DistillationTowerBlockEntity) blockEntity);
			case FLUID_REPLICATOR:
				return new GuiFluidReplicator(syncID, player, (FluidReplicatorBlockEntity) blockEntity);
			case SOLID_CANNING_MACHINE:
				return new GuiSolidCanningMachine(syncID, player, (SoildCanningMachineBlockEntity) blockEntity);
			case WIRE_MILL:
				return new GuiWireMill(syncID, player, (WireMillBlockEntity) blockEntity);
			case GREENHOUSE_CONTROLLER:
				return new GuiGreenhouseController(syncID, player, (GreenhouseControllerBlockEntity) blockEntity);
			default:
				break;

		}
		return null;
	}
}
