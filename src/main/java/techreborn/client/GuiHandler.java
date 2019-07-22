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

import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import reborncore.client.containerBuilder.IContainerProvider;
import reborncore.client.containerBuilder.builder.BuiltContainer;
import reborncore.mixin.extensions.ContainerExtensions;
import techreborn.client.container.ContainerDestructoPack;
import techreborn.client.gui.*;
import techreborn.tiles.TileChargeOMat;
import techreborn.tiles.TileDigitalChest;
import techreborn.tiles.TileIndustrialCentrifuge;
import techreborn.tiles.fusionReactor.TileFusionControlComputer;
import techreborn.tiles.generator.TilePlasmaGenerator;
import techreborn.tiles.generator.advanced.TileDieselGenerator;
import techreborn.tiles.generator.advanced.TileGasTurbine;
import techreborn.tiles.generator.advanced.TileSemiFluidGenerator;
import techreborn.tiles.generator.advanced.TileThermalGenerator;
import techreborn.tiles.generator.basic.TileSolidFuelGenerator;
import techreborn.tiles.machine.iron.TileIronAlloyFurnace;
import techreborn.tiles.machine.iron.TileIronFurnace;
import techreborn.tiles.machine.multiblock.*;
import techreborn.tiles.machine.tier1.*;
import techreborn.tiles.machine.tier3.TileChunkLoader;
import techreborn.tiles.machine.tier3.TileMatterFabricator;
import techreborn.tiles.machine.tier3.TileQuantumChest;
import techreborn.tiles.machine.tier3.TileQuantumTank;
import techreborn.tiles.storage.TileAdjustableSU;
import techreborn.tiles.storage.TileHighVoltageSU;
import techreborn.tiles.storage.TileLowVoltageSU;
import techreborn.tiles.storage.TileMediumVoltageSU;
import techreborn.tiles.storage.idsu.TileInterdimensionalSU;
import techreborn.tiles.storage.lesu.TileLapotronicSU;

public class GuiHandler {

	public static void register(){

		EGui.stream().forEach(gui -> ContainerProviderRegistry.INSTANCE.registerFactory(gui.getID(), (syncID, identifier, playerEntity, packetByteBuf) -> {
			final BlockEntity tile = playerEntity.world.getBlockEntity(packetByteBuf.readBlockPos());
			return ((IContainerProvider) tile).createContainer(syncID, playerEntity);
		}));

		EGui.stream().forEach(gui -> ScreenProviderRegistry.INSTANCE.registerFactory(gui.getID(), (syncID, identifier, playerEntity, packetByteBuf) -> getClientGuiElement(EGui.byID(identifier), playerEntity, packetByteBuf.readBlockPos(), syncID)));
	}

	private static AbstractContainerScreen getClientGuiElement(final EGui gui, final PlayerEntity player, BlockPos pos, int syncID) {
		final BlockEntity tile = player.world.getBlockEntity(pos);

		switch (gui) {
			case AESU:
				return new GuiAESU(syncID, player, (TileAdjustableSU) tile);
			case ALLOY_FURNACE:
				return new GuiAlloyFurnace(syncID, player, (TileIronAlloyFurnace) tile);
			case ALLOY_SMELTER:
				return new GuiAlloySmelter(syncID, player, (TileAlloySmelter) tile);
			case ASSEMBLING_MACHINE:
				return new GuiAssemblingMachine(syncID, player, (TileAssemblingMachine) tile);
			case LOW_VOLTAGE_SU:
				return new GuiBatbox(syncID, player, (TileLowVoltageSU) tile);
			case BLAST_FURNACE:
				return new GuiBlastFurnace(syncID, player, (TileIndustrialBlastFurnace) tile);
			case CENTRIFUGE:
				return new GuiCentrifuge(syncID, player, (TileIndustrialCentrifuge) tile);
			case CHARGEBENCH:
				return new GuiChargeBench(syncID, player, (TileChargeOMat) tile);
			case CHEMICAL_REACTOR:
				return new GuiChemicalReactor(syncID, player, (TileChemicalReactor) tile);
			case CHUNK_LOADER:
				return new GuiChunkLoader(syncID, player, (TileChunkLoader) tile);
			case COMPRESSOR:
				return new GuiCompressor(syncID, player, (TileCompressor) tile);
			case DESTRUCTOPACK:
				return new GuiDestructoPack(new ContainerDestructoPack(player));
			case DIESEL_GENERATOR:
				return new GuiDieselGenerator(syncID, player, (TileDieselGenerator) tile);
			case DIGITAL_CHEST:
				return new GuiDigitalChest(syncID, player, (TileDigitalChest) tile);
			case ELECTRIC_FURNACE:
				return new GuiElectricFurnace(syncID, player, (TileElectricFurnace) tile);
			case EXTRACTOR:
				return new GuiExtractor(syncID, player, (TileExtractor) tile);
			case FUSION_CONTROLLER:
				return new GuiFusionReactor(syncID, player, (TileFusionControlComputer) tile);
			case GAS_TURBINE:
				return new GuiGasTurbine(syncID, player, (TileGasTurbine) tile);
			case GENERATOR:
				return new GuiGenerator(syncID, player, (TileSolidFuelGenerator) tile);
			case GRINDER:
				return new GuiGrinder(syncID, player, (TileGrinder) tile);
			case IDSU:
				return new GuiIDSU(syncID, player, (TileInterdimensionalSU) tile);
			case IMPLOSION_COMPRESSOR:
				return new GuiImplosionCompressor(syncID, player, (TileImplosionCompressor) tile);
			case INDUSTRIAL_ELECTROLYZER:
				return new GuiIndustrialElectrolyzer(syncID, player, (TileIndustrialElectrolyzer) tile);
			case INDUSTRIAL_GRINDER:
				return new GuiIndustrialGrinder(syncID, player, (TileIndustrialGrinder) tile);
			case IRON_FURNACE:
				return new GuiIronFurnace(syncID, player, (TileIronFurnace) tile);
			case LESU:
				return new GuiLESU(syncID, player, (TileLapotronicSU) tile);
			case MATTER_FABRICATOR:
				return new GuiMatterFabricator(syncID, player, (TileMatterFabricator) tile);
			case MEDIUM_VOLTAGE_SU:
				return new GuiMFE(syncID, player, (TileMediumVoltageSU) tile);
			case HIGH_VOLTAGE_SU:
				return new GuiMFSU(syncID, player, (TileHighVoltageSU) tile);
			case QUANTUM_CHEST:
				return new GuiQuantumChest(syncID, player, (TileQuantumChest) tile);
			case QUANTUM_TANK:
				return new GuiQuantumTank(syncID, player, (TileQuantumTank) tile);
			case RECYCLER:
				return new GuiRecycler(syncID, player, (TileRecycler) tile);
			case ROLLING_MACHINE:
				return new GuiRollingMachine(syncID, player, (TileRollingMachine) tile);
			case SAWMILL:
				return new GuiIndustrialSawmill(syncID, player, (TileIndustrialSawmill) tile);
			case SCRAPBOXINATOR:
				return new GuiScrapboxinator(syncID, player, (TileScrapboxinator) tile);
			case SEMIFLUID_GENERATOR:
				return new GuiSemifluidGenerator(syncID, player, (TileSemiFluidGenerator) tile);
			case THERMAL_GENERATOR:
				return new GuiThermalGenerator(syncID, player, (TileThermalGenerator) tile);
			case VACUUM_FREEZER:
				return new GuiVacuumFreezer(syncID, player, (TileVacuumFreezer) tile);
			case AUTO_CRAFTING_TABLE:
				return new GuiAutoCrafting(syncID, player, (TileAutoCraftingTable) tile);
			case PLASMA_GENERATOR:
				return new GuiPlasmaGenerator(syncID, player, (TilePlasmaGenerator) tile);
			case DISTILLATION_TOWER:
				return new GuiDistillationTower(syncID, player, (TileDistillationTower) tile);
			case FLUID_REPLICATOR:
				return new GuiFluidReplicator(syncID, player, (TileFluidReplicator) tile);
			default:
				break;

		}
		return null;
	}
}
