/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"); to deal
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

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import reborncore.common.tile.TileMachineBase;
import techreborn.tiles.*;
import techreborn.tiles.cable.TileCable;
import techreborn.tiles.fusionReactor.TileFusionControlComputer;
import techreborn.tiles.generator.TileLightningRod;
import techreborn.tiles.generator.TilePlasmaGenerator;
import techreborn.tiles.generator.TileSolarPanel;
import techreborn.tiles.generator.advanced.*;
import techreborn.tiles.generator.basic.TileSolidFuelGenerator;
import techreborn.tiles.generator.basic.TileWaterMill;
import techreborn.tiles.generator.basic.TileWindMill;
import techreborn.tiles.lighting.TileLamp;
import techreborn.tiles.machine.iron.TileIronAlloyFurnace;
import techreborn.tiles.machine.iron.TileIronFurnace;
import techreborn.tiles.machine.multiblock.*;
import techreborn.tiles.machine.tier1.*;
import techreborn.tiles.storage.TileAdjustableSU;
import techreborn.tiles.storage.TileHighVoltageSU;
import techreborn.tiles.storage.TileLowVoltageSU;
import techreborn.tiles.storage.TileMediumVoltageSU;
import techreborn.tiles.storage.idsu.TileInterdimensionalSU;
import techreborn.tiles.storage.lesu.TileLSUStorage;
import techreborn.tiles.storage.lesu.TileLapotronicSU;
import techreborn.tiles.transformers.TileHVTransformer;
import techreborn.tiles.transformers.TileLVTransformer;
import techreborn.tiles.transformers.TileMVTransformer;

import java.util.ArrayList;
import java.util.List;

public class TRTileEntities {

	public static final TileEntityType<TileThermalGenerator> THERMAL_GEN = register(TileThermalGenerator.class, "thermal_generator");
	public static final TileEntityType<TileQuantumTank> QUANTUM_TANK = register(TileQuantumTank.class, "quantum_tank");
	public static final TileEntityType<TileQuantumChest> QUANTUM_CHEST = register(TileQuantumChest.class, "quantum_chest");
	public static final TileEntityType<TileDigitalChest> DIGITAL_CHEST = register(TileDigitalChest.class, "digital_chest");
	public static final TileEntityType<TileIndustrialCentrifuge> INDUSTRIAL_CENTRIFUGE = register(TileIndustrialCentrifuge.class, "industrial_centrifuge");
	public static final TileEntityType<TileRollingMachine> ROLLING_MACHINE = register(TileRollingMachine.class, "rolling_machine");
	public static final TileEntityType<TileIndustrialBlastFurnace> INDUSTRIAL_BLAST_FURNACE = register(TileIndustrialBlastFurnace.class, "industrial_blast_furnace");
	public static final TileEntityType<TileAlloySmelter> ALLOY_SMELTER = register(TileAlloySmelter.class, "alloy_smelter");
	public static final TileEntityType<TileIndustrialGrinder> INDUSTRIAL_GRINDER = register(TileIndustrialGrinder.class, "industrial_grinder");
	public static final TileEntityType<TileImplosionCompressor> IMPLOSION_COMPRESSOR = register(TileImplosionCompressor.class, "implosion_compressor");
	public static final TileEntityType<TileMatterFabricator> MATTER_FABRICATOR = register(TileMatterFabricator.class, "matter_fabricator");
	public static final TileEntityType<TileChunkLoader> CHUNK_LOADER = register(TileChunkLoader.class, "chunk_loader");
	public static final TileEntityType<TileChargeOMat> CHARGE_O_MAT = register(TileChargeOMat.class, "charge_o_mat");
	public static final TileEntityType<TilePlayerDectector> PLAYER_DETECTOR = register(TilePlayerDectector.class, "player_detector");
	public static final TileEntityType<TileCable> CABLE = register(TileCable.class, "cable");
	public static final TileEntityType<TileMachineCasing> MACHINE_CASINGS = register(TileMachineCasing.class, "machine_casing");
	public static final TileEntityType<TileDragonEggSyphon> DRAGON_EGG_SYPHON = register(TileDragonEggSyphon.class, "dragon_egg_syphon");
	public static final TileEntityType<TileAssemblingMachine> ASSEMBLY_MACHINE = register(TileAssemblingMachine.class, "assembly_machine");
	public static final TileEntityType<TileDieselGenerator> DIESEL_GENERATOR = register(TileDieselGenerator.class, "diesel_generator");
	public static final TileEntityType<TileIndustrialElectrolyzer> INDUSTRIAL_ELECTROLYZER = register(TileIndustrialElectrolyzer.class, "industrial_electrolyzer");
	public static final TileEntityType<TileSemiFluidGenerator> SEMI_FLUID_GENERATOR = register(TileSemiFluidGenerator.class, "semi_fluid_generator");
	public static final TileEntityType<TileGasTurbine> GAS_TURBINE = register(TileGasTurbine.class, "gas_turbine");
	public static final TileEntityType<TileIronAlloyFurnace> IRON_ALLOY_FURNACE = register(TileIronAlloyFurnace.class, "iron_alloy_furnace");
	public static final TileEntityType<TileChemicalReactor> CHEMICAL_REACTOR = register(TileChemicalReactor.class, "chemical_reactor");
	public static final TileEntityType<TileInterdimensionalSU> INTERDIMENSIONAL_SU = register(TileInterdimensionalSU.class, "interdimensional_su");
	public static final TileEntityType<TileAdjustableSU> ADJUSTABLE_SU = register(TileAdjustableSU.class, "adjustable_su");
	public static final TileEntityType<TileLapotronicSU> LAPOTRONIC_SU = register(TileLapotronicSU.class, "lapotronic_su");
	public static final TileEntityType<TileLSUStorage> LSU_STORAGE = register(TileLSUStorage.class, "lsu_storage");
	public static final TileEntityType<TileDistillationTower> DISTILLATION_TOWER = register(TileDistillationTower.class, "distillation_tower");
	public static final TileEntityType<TileVacuumFreezer> VACUUM_FREEZER = register(TileVacuumFreezer.class, "vacuum_freezer");
	public static final TileEntityType<TileFusionControlComputer> FUSION_CONTROL_COMPUTER = register(TileFusionControlComputer.class, "fusion_control_computer");
	public static final TileEntityType<TileLightningRod> LIGHTNING_ROD = register(TileLightningRod.class, "lightning_rod");
	public static final TileEntityType<TileIndustrialSawmill> INDUSTRIAL_SAWMILL = register(TileIndustrialSawmill.class, "industrial_sawmill");
	public static final TileEntityType<TileGrinder> GRINDER = register(TileGrinder.class, "grinder");
	public static final TileEntityType<TileSolidFuelGenerator> SOLID_FUEL_GENEREATOR = register(TileSolidFuelGenerator.class, "solid_fuel_generator");
	public static final TileEntityType<TileExtractor> EXTRACTOR = register(TileExtractor.class, "extractor");
	public static final TileEntityType<TileCompressor> COMPRESSOR = register(TileCompressor.class, "compressor");
	public static final TileEntityType<TileElectricFurnace> ELECTRIC_FURNACE = register(TileElectricFurnace.class, "electric_furnace");
	public static final TileEntityType<TileSolarPanel> SOLAR_PANEL = register(TileSolarPanel.class, "solar_panel");
	public static final TileEntityType<TileCreativeQuantumTank> CREATIVE_QUANTUM_TANK = register(TileCreativeQuantumTank.class, "creative_quantum_tank");
	public static final TileEntityType<TileCreativeQuantumChest> CREATIVE_QUANTUM_CHEST = register(TileCreativeQuantumChest.class, "creative_quantum_chest");
	public static final TileEntityType<TileWaterMill> WATER_MILL = register(TileWaterMill.class, "water_mill");
	public static final TileEntityType<TileWindMill> WIND_MILL = register(TileWindMill.class, "wind_mill");
	public static final TileEntityType<TileMachineBase> MACHINE_BASE = register(TileMachineBase.class, "machine_base");
	public static final TileEntityType<TileRecycler> RECYCLER = register(TileRecycler.class, "recycler");
	public static final TileEntityType<TileLowVoltageSU> LOW_VOLTAGE_SU = register(TileLowVoltageSU.class, "low_voltage_su");
	public static final TileEntityType<TileMediumVoltageSU> MEDIUM_VOLTAGE_SU = register(TileMediumVoltageSU.class, "medium_voltage_su");
	public static final TileEntityType<TileHighVoltageSU> HIGH_VOLTAGE_SU = register(TileHighVoltageSU.class, "high_voltage_su");
	public static final TileEntityType<TileLVTransformer> LV_TRANSFORMER = register(TileLVTransformer.class, "lv_transformer");
	public static final TileEntityType<TileMVTransformer> MV_TRANSFORMER = register(TileMVTransformer.class, "mv_transformer");
	public static final TileEntityType<TileHVTransformer> HV_TRANSFORMER = register(TileHVTransformer.class, "hv_transformer");
	public static final TileEntityType<TileAutoCraftingTable> AUTO_CRAFTING_TABLE = register(TileAutoCraftingTable.class, "auto_crafting_table");
	public static final TileEntityType<TileIronFurnace> IRON_FURNACE = register(TileIronFurnace.class, "iron_furnace");
	public static final TileEntityType<TileScrapboxinator> SCRAPBOXINATOR = register(TileScrapboxinator.class, "scrapboxinator");
	public static final TileEntityType<TilePlasmaGenerator> PLASMA_GENERATOR = register(TilePlasmaGenerator.class, "plasma_generator");
	public static final TileEntityType<TileLamp> LAMP = register(TileLamp.class, "lamp");
	public static final TileEntityType<TileAlarm> ALARM = register(TileAlarm.class, "alarm");
	public static final TileEntityType<TileFluidReplicator> FLUID_REPLICATOR = register(TileFluidReplicator.class, "fluid_replicator");

	public static List<TileEntityType<?>> TYPES = new ArrayList<>();

	public static <T extends TileEntity> TileEntityType<T> register(Class<T> tClass, String name) {
		return register(new ResourceLocation("techrebon", name).toString(), TileEntityType.Builder.create(() -> {
			//TODO clean this up
			try {
				return tClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException("Failed to create tile", e);
			}
		}));
	}

	public static <T extends TileEntity> TileEntityType<T> register(String id, TileEntityType.Builder<T> builder) {
		TileEntityType<T> tileEntityType = builder.build(null);
		tileEntityType.setRegistryName(new ResourceLocation(id));
		return tileEntityType;
	}

}
