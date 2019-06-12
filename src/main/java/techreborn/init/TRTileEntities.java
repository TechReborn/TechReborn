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

package techreborn.init;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import reborncore.common.tile.TileMachineBase;
import techreborn.TechReborn;
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
import techreborn.tiles.machine.tier3.*;
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
	
	public static List<BlockEntityType<?>> TYPES = new ArrayList<>();

	public static final BlockEntityType<TileThermalGenerator> THERMAL_GEN = register(TileThermalGenerator.class, "thermal_generator");
	public static final BlockEntityType<TileQuantumTank> QUANTUM_TANK = register(TileQuantumTank.class, "quantum_tank");
	public static final BlockEntityType<TileQuantumChest> QUANTUM_CHEST = register(TileQuantumChest.class, "quantum_chest");
	public static final BlockEntityType<TileDigitalChest> DIGITAL_CHEST = register(TileDigitalChest.class, "digital_chest");
	public static final BlockEntityType<TileIndustrialCentrifuge> INDUSTRIAL_CENTRIFUGE = register(TileIndustrialCentrifuge.class, "industrial_centrifuge");
	public static final BlockEntityType<TileRollingMachine> ROLLING_MACHINE = register(TileRollingMachine.class, "rolling_machine");
	public static final BlockEntityType<TileIndustrialBlastFurnace> INDUSTRIAL_BLAST_FURNACE = register(TileIndustrialBlastFurnace.class, "industrial_blast_furnace");
	public static final BlockEntityType<TileAlloySmelter> ALLOY_SMELTER = register(TileAlloySmelter.class, "alloy_smelter");
	public static final BlockEntityType<TileIndustrialGrinder> INDUSTRIAL_GRINDER = register(TileIndustrialGrinder.class, "industrial_grinder");
	public static final BlockEntityType<TileImplosionCompressor> IMPLOSION_COMPRESSOR = register(TileImplosionCompressor.class, "implosion_compressor");
	public static final BlockEntityType<TileMatterFabricator> MATTER_FABRICATOR = register(TileMatterFabricator.class, "matter_fabricator");
	public static final BlockEntityType<TileChunkLoader> CHUNK_LOADER = register(TileChunkLoader.class, "chunk_loader");
	public static final BlockEntityType<TileChargeOMat> CHARGE_O_MAT = register(TileChargeOMat.class, "charge_o_mat");
	public static final BlockEntityType<TilePlayerDectector> PLAYER_DETECTOR = register(TilePlayerDectector.class, "player_detector");
	public static final BlockEntityType<TileCable> CABLE = register(TileCable.class, "cable");
	public static final BlockEntityType<TileMachineCasing> MACHINE_CASINGS = register(TileMachineCasing.class, "machine_casing");
	public static final BlockEntityType<TileDragonEggSyphon> DRAGON_EGG_SYPHON = register(TileDragonEggSyphon.class, "dragon_egg_syphon");
	public static final BlockEntityType<TileAssemblingMachine> ASSEMBLY_MACHINE = register(TileAssemblingMachine.class, "assembly_machine");
	public static final BlockEntityType<TileDieselGenerator> DIESEL_GENERATOR = register(TileDieselGenerator.class, "diesel_generator");
	public static final BlockEntityType<TileIndustrialElectrolyzer> INDUSTRIAL_ELECTROLYZER = register(TileIndustrialElectrolyzer.class, "industrial_electrolyzer");
	public static final BlockEntityType<TileSemiFluidGenerator> SEMI_FLUID_GENERATOR = register(TileSemiFluidGenerator.class, "semi_fluid_generator");
	public static final BlockEntityType<TileGasTurbine> GAS_TURBINE = register(TileGasTurbine.class, "gas_turbine");
	public static final BlockEntityType<TileIronAlloyFurnace> IRON_ALLOY_FURNACE = register(TileIronAlloyFurnace.class, "iron_alloy_furnace");
	public static final BlockEntityType<TileChemicalReactor> CHEMICAL_REACTOR = register(TileChemicalReactor.class, "chemical_reactor");
	public static final BlockEntityType<TileInterdimensionalSU> INTERDIMENSIONAL_SU = register(TileInterdimensionalSU.class, "interdimensional_su");
	public static final BlockEntityType<TileAdjustableSU> ADJUSTABLE_SU = register(TileAdjustableSU.class, "adjustable_su");
	public static final BlockEntityType<TileLapotronicSU> LAPOTRONIC_SU = register(TileLapotronicSU.class, "lapotronic_su");
	public static final BlockEntityType<TileLSUStorage> LSU_STORAGE = register(TileLSUStorage.class, "lsu_storage");
	public static final BlockEntityType<TileDistillationTower> EnvTypeILLATION_TOWER = register(TileDistillationTower.class, "distillation_tower");
	public static final BlockEntityType<TileVacuumFreezer> VACUUM_FREEZER = register(TileVacuumFreezer.class, "vacuum_freezer");
	public static final BlockEntityType<TileFusionControlComputer> FUSION_CONTROL_COMPUTER = register(TileFusionControlComputer.class, "fusion_control_computer");
	public static final BlockEntityType<TileLightningRod> LIGHTNING_ROD = register(TileLightningRod.class, "lightning_rod");
	public static final BlockEntityType<TileIndustrialSawmill> INDUSTRIAL_SAWMILL = register(TileIndustrialSawmill.class, "industrial_sawmill");
	public static final BlockEntityType<TileGrinder> GRINDER = register(TileGrinder.class, "grinder");
	public static final BlockEntityType<TileSolidFuelGenerator> SOLID_FUEL_GENEREATOR = register(TileSolidFuelGenerator.class, "solid_fuel_generator");
	public static final BlockEntityType<TileExtractor> EXTRACTOR = register(TileExtractor.class, "extractor");
	public static final BlockEntityType<TileCompressor> COMPRESSOR = register(TileCompressor.class, "compressor");
	public static final BlockEntityType<TileElectricFurnace> ELECTRIC_FURNACE = register(TileElectricFurnace.class, "electric_furnace");
	public static final BlockEntityType<TileSolarPanel> SOLAR_PANEL = register(TileSolarPanel.class, "solar_panel");
	public static final BlockEntityType<TileCreativeQuantumTank> CREATIVE_QUANTUM_TANK = register(TileCreativeQuantumTank.class, "creative_quantum_tank");
	public static final BlockEntityType<TileCreativeQuantumChest> CREATIVE_QUANTUM_CHEST = register(TileCreativeQuantumChest.class, "creative_quantum_chest");
	public static final BlockEntityType<TileWaterMill> WATER_MILL = register(TileWaterMill.class, "water_mill");
	public static final BlockEntityType<TileWindMill> WIND_MILL = register(TileWindMill.class, "wind_mill");
	public static final BlockEntityType<TileMachineBase> MACHINE_BASE = register(TileMachineBase.class, "machine_base");
	public static final BlockEntityType<TileRecycler> RECYCLER = register(TileRecycler.class, "recycler");
	public static final BlockEntityType<TileLowVoltageSU> LOW_VOLTAGE_SU = register(TileLowVoltageSU.class, "low_voltage_su");
	public static final BlockEntityType<TileMediumVoltageSU> MEDIUM_VOLTAGE_SU = register(TileMediumVoltageSU.class, "medium_voltage_su");
	public static final BlockEntityType<TileHighVoltageSU> HIGH_VOLTAGE_SU = register(TileHighVoltageSU.class, "high_voltage_su");
	public static final BlockEntityType<TileLVTransformer> LV_TRANSFORMER = register(TileLVTransformer.class, "lv_transformer");
	public static final BlockEntityType<TileMVTransformer> MV_TRANSFORMER = register(TileMVTransformer.class, "mv_transformer");
	public static final BlockEntityType<TileHVTransformer> HV_TRANSFORMER = register(TileHVTransformer.class, "hv_transformer");
	public static final BlockEntityType<TileAutoCraftingTable> AUTO_CRAFTING_TABLE = register(TileAutoCraftingTable.class, "auto_crafting_table");
	public static final BlockEntityType<TileIronFurnace> IRON_FURNACE = register(TileIronFurnace.class, "iron_furnace");
	public static final BlockEntityType<TileScrapboxinator> SCRAPBOXINATOR = register(TileScrapboxinator.class, "scrapboxinator");
	public static final BlockEntityType<TilePlasmaGenerator> PLASMA_GENERATOR = register(TilePlasmaGenerator.class, "plasma_generator");
	public static final BlockEntityType<TileLamp> LAMP = register(TileLamp.class, "lamp");
	public static final BlockEntityType<TileAlarm> ALARM = register(TileAlarm.class, "alarm");
	public static final BlockEntityType<TileFluidReplicator> FLUID_REPLICATOR = register(TileFluidReplicator.class, "fluid_replicator");

	

	public static <T extends BlockEntity> BlockEntityType<T> register(Class<T> tClass, String name) {
		return register(new Identifier(TechReborn.MOD_ID, name).toString(), BlockEntityType.Builder.create(() -> {
			//TODO clean this up
			try {
				return tClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException("Failed to create tile", e);
			}
		}));
	}

	public static <T extends BlockEntity> BlockEntityType<T> register(String id, BlockEntityType.Builder<T> builder) {
		BlockEntityType<T> tileEntityType = builder.build(null);
		Registry.register(Registry.BLOCK_ENTITY, new Identifier(id), tileEntityType);
		TRTileEntities.TYPES.add(tileEntityType);
		return tileEntityType;
	}

}
