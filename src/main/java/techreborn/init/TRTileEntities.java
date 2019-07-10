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

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.Validate;
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
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class TRTileEntities {
	
	public static List<BlockEntityType<?>> TYPES = new ArrayList<>();

	public static final BlockEntityType<TileThermalGenerator> THERMAL_GEN = register(TileThermalGenerator.class, "thermal_generator", TRContent.Machine.THERMAL_GENERATOR);
	public static final BlockEntityType<TileQuantumTank> QUANTUM_TANK = register(TileQuantumTank.class, "quantum_tank", TRContent.Machine.QUANTUM_TANK);
	public static final BlockEntityType<TileQuantumChest> QUANTUM_CHEST = register(TileQuantumChest.class, "quantum_chest", TRContent.Machine.QUANTUM_CHEST);
	public static final BlockEntityType<TileDigitalChest> DIGITAL_CHEST = register(TileDigitalChest.class, "digital_chest", TRContent.Machine.DIGITAL_CHEST);
	public static final BlockEntityType<TileIndustrialCentrifuge> INDUSTRIAL_CENTRIFUGE = register(TileIndustrialCentrifuge.class, "industrial_centrifuge", TRContent.Machine.INDUSTRIAL_CENTRIFUGE);
	public static final BlockEntityType<TileRollingMachine> ROLLING_MACHINE = register(TileRollingMachine.class, "rolling_machine", TRContent.Machine.ROLLING_MACHINE);
	public static final BlockEntityType<TileIndustrialBlastFurnace> INDUSTRIAL_BLAST_FURNACE = register(TileIndustrialBlastFurnace.class, "industrial_blast_furnace", TRContent.Machine.INDUSTRIAL_BLAST_FURNACE);
	public static final BlockEntityType<TileAlloySmelter> ALLOY_SMELTER = register(TileAlloySmelter.class, "alloy_smelter", TRContent.Machine.ALLOY_SMELTER);
	public static final BlockEntityType<TileIndustrialGrinder> INDUSTRIAL_GRINDER = register(TileIndustrialGrinder.class, "industrial_grinder", TRContent.Machine.INDUSTRIAL_GRINDER);
	public static final BlockEntityType<TileImplosionCompressor> IMPLOSION_COMPRESSOR = register(TileImplosionCompressor.class, "implosion_compressor", TRContent.Machine.IMPLOSION_COMPRESSOR);
	public static final BlockEntityType<TileMatterFabricator> MATTER_FABRICATOR = register(TileMatterFabricator.class, "matter_fabricator", TRContent.Machine.MATTER_FABRICATOR);
	public static final BlockEntityType<TileChunkLoader> CHUNK_LOADER = register(TileChunkLoader.class, "chunk_loader", TRContent.Machine.CHUNK_LOADER);
	public static final BlockEntityType<TileChargeOMat> CHARGE_O_MAT = register(TileChargeOMat.class, "charge_o_mat", TRContent.Machine.CHARGE_O_MAT);
	public static final BlockEntityType<TilePlayerDectector> PLAYER_DETECTOR = register(TilePlayerDectector.class, "player_detector", TRContent.Machine.PLAYER_DETECTOR);
	public static final BlockEntityType<TileCable> CABLE = register(TileCable.class, "cable", TRContent.Cables.values());
	public static final BlockEntityType<TileMachineCasing> MACHINE_CASINGS = register(TileMachineCasing.class, "machine_casing", TRContent.MachineBlocks.getCasings());
	public static final BlockEntityType<TileDragonEggSyphon> DRAGON_EGG_SYPHON = register(TileDragonEggSyphon.class, "dragon_egg_syphon", TRContent.Machine.DRAGON_EGG_SYPHON);
	public static final BlockEntityType<TileAssemblingMachine> ASSEMBLY_MACHINE = register(TileAssemblingMachine.class, "assembly_machine", TRContent.Machine.ASSEMBLY_MACHINE);
	public static final BlockEntityType<TileDieselGenerator> DIESEL_GENERATOR = register(TileDieselGenerator.class, "diesel_generator", TRContent.Machine.DIESEL_GENERATOR);
	public static final BlockEntityType<TileIndustrialElectrolyzer> INDUSTRIAL_ELECTROLYZER = register(TileIndustrialElectrolyzer.class, "industrial_electrolyzer", TRContent.Machine.INDUSTRIAL_ELECTROLYZER);
	public static final BlockEntityType<TileSemiFluidGenerator> SEMI_FLUID_GENERATOR = register(TileSemiFluidGenerator.class, "semi_fluid_generator", TRContent.Machine.SEMI_FLUID_GENERATOR);
	public static final BlockEntityType<TileGasTurbine> GAS_TURBINE = register(TileGasTurbine.class, "gas_turbine", TRContent.Machine.GAS_TURBINE);
	public static final BlockEntityType<TileIronAlloyFurnace> IRON_ALLOY_FURNACE = register(TileIronAlloyFurnace.class, "iron_alloy_furnace", TRContent.Machine.IRON_ALLOY_FURNACE);
	public static final BlockEntityType<TileChemicalReactor> CHEMICAL_REACTOR = register(TileChemicalReactor.class, "chemical_reactor", TRContent.Machine.CHEMICAL_REACTOR);
	public static final BlockEntityType<TileInterdimensionalSU> INTERDIMENSIONAL_SU = register(TileInterdimensionalSU.class, "interdimensional_su", TRContent.Machine.INTERDIMENSIONAL_SU);
	public static final BlockEntityType<TileAdjustableSU> ADJUSTABLE_SU = register(TileAdjustableSU.class, "adjustable_su", TRContent.Machine.ADJUSTABLE_SU);
	public static final BlockEntityType<TileLapotronicSU> LAPOTRONIC_SU = register(TileLapotronicSU.class, "lapotronic_su", TRContent.Machine.LAPOTRONIC_SU);
	public static final BlockEntityType<TileLSUStorage> LSU_STORAGE = register(TileLSUStorage.class, "lsu_storage", TRContent.Machine.LSU_STORAGE);
	public static final BlockEntityType<TileDistillationTower> DISTILLATION_TOWER = register(TileDistillationTower.class, "distillation_tower", TRContent.Machine.DISTILLATION_TOWER);
	public static final BlockEntityType<TileVacuumFreezer> VACUUM_FREEZER = register(TileVacuumFreezer.class, "vacuum_freezer", TRContent.Machine.VACUUM_FREEZER);
	public static final BlockEntityType<TileFusionControlComputer> FUSION_CONTROL_COMPUTER = register(TileFusionControlComputer.class, "fusion_control_computer", TRContent.Machine.FUSION_CONTROL_COMPUTER);
	public static final BlockEntityType<TileLightningRod> LIGHTNING_ROD = register(TileLightningRod.class, "lightning_rod", TRContent.Machine.LIGHTNING_ROD);
	public static final BlockEntityType<TileIndustrialSawmill> INDUSTRIAL_SAWMILL = register(TileIndustrialSawmill.class, "industrial_sawmill", TRContent.Machine.INDUSTRIAL_SAWMILL);
	public static final BlockEntityType<TileGrinder> GRINDER = register(TileGrinder.class, "grinder", TRContent.Machine.GRINDER);
	public static final BlockEntityType<TileSolidFuelGenerator> SOLID_FUEL_GENEREATOR = register(TileSolidFuelGenerator.class, "solid_fuel_generator", TRContent.Machine.SOLID_FUEL_GENERATOR);
	public static final BlockEntityType<TileExtractor> EXTRACTOR = register(TileExtractor.class, "extractor", TRContent.Machine.EXTRACTOR);
	public static final BlockEntityType<TileCompressor> COMPRESSOR = register(TileCompressor.class, "compressor", TRContent.Machine.COMPRESSOR);
	public static final BlockEntityType<TileElectricFurnace> ELECTRIC_FURNACE = register(TileElectricFurnace.class, "electric_furnace", TRContent.Machine.ELECTRIC_FURNACE);
	public static final BlockEntityType<TileSolarPanel> SOLAR_PANEL = register(TileSolarPanel.class, "solar_panel", TRContent.SolarPanels.values());
	public static final BlockEntityType<TileCreativeQuantumTank> CREATIVE_QUANTUM_TANK = register(TileCreativeQuantumTank.class, "creative_quantum_tank", TRContent.Machine.CREATIVE_QUANTUM_TANK);
	public static final BlockEntityType<TileCreativeQuantumChest> CREATIVE_QUANTUM_CHEST = register(TileCreativeQuantumChest.class, "creative_quantum_chest", TRContent.Machine.CREATIVE_QUANTUM_CHEST);
	public static final BlockEntityType<TileWaterMill> WATER_MILL = register(TileWaterMill.class, "water_mill", TRContent.Machine.WATER_MILL);
	public static final BlockEntityType<TileWindMill> WIND_MILL = register(TileWindMill.class, "wind_mill", TRContent.Machine.WIND_MILL);
	public static final BlockEntityType<TileRecycler> RECYCLER = register(TileRecycler.class, "recycler", TRContent.Machine.RECYCLER);
	public static final BlockEntityType<TileLowVoltageSU> LOW_VOLTAGE_SU = register(TileLowVoltageSU.class, "low_voltage_su", TRContent.Machine.LOW_VOLTAGE_SU);
	public static final BlockEntityType<TileMediumVoltageSU> MEDIUM_VOLTAGE_SU = register(TileMediumVoltageSU.class, "medium_voltage_su", TRContent.Machine.MEDIUM_VOLTAGE_SU);
	public static final BlockEntityType<TileHighVoltageSU> HIGH_VOLTAGE_SU = register(TileHighVoltageSU.class, "high_voltage_su", TRContent.Machine.HIGH_VOLTAGE_SU);
	public static final BlockEntityType<TileLVTransformer> LV_TRANSFORMER = register(TileLVTransformer.class, "lv_transformer", TRContent.Machine.LV_TRANSFORMER);
	public static final BlockEntityType<TileMVTransformer> MV_TRANSFORMER = register(TileMVTransformer.class, "mv_transformer", TRContent.Machine.MV_TRANSFORMER);
	public static final BlockEntityType<TileHVTransformer> HV_TRANSFORMER = register(TileHVTransformer.class, "hv_transformer", TRContent.Machine.HV_TRANSFORMER);
	public static final BlockEntityType<TileAutoCraftingTable> AUTO_CRAFTING_TABLE = register(TileAutoCraftingTable.class, "auto_crafting_table", TRContent.Machine.AUTO_CRAFTING_TABLE);
	public static final BlockEntityType<TileIronFurnace> IRON_FURNACE = register(TileIronFurnace.class, "iron_furnace", TRContent.Machine.IRON_FURNACE);
	public static final BlockEntityType<TileScrapboxinator> SCRAPBOXINATOR = register(TileScrapboxinator.class, "scrapboxinator", TRContent.Machine.SCRAPBOXINATOR);
	public static final BlockEntityType<TilePlasmaGenerator> PLASMA_GENERATOR = register(TilePlasmaGenerator.class, "plasma_generator", TRContent.Machine.PLASMA_GENERATOR);
	public static final BlockEntityType<TileLamp> LAMP = register(TileLamp.class, "lamp", TRContent.Machine.LAMP_INCANDESCENT, TRContent.Machine.LAMP_LED);
	public static final BlockEntityType<TileAlarm> ALARM = register(TileAlarm.class, "alarm", TRContent.Machine.ALARM);
	public static final BlockEntityType<TileFluidReplicator> FLUID_REPLICATOR = register(TileFluidReplicator.class, "fluid_replicator", TRContent.Machine.FLUID_REPLICATOR);

	public static <T extends BlockEntity> BlockEntityType<T> register(Class<T> tClass, String name, ItemConvertible... items) {
		return register(tClass, name, Arrays.stream(items).map(itemConvertible -> Block.getBlockFromItem(itemConvertible.asItem())).toArray(Block[]::new));
	}

	public static <T extends BlockEntity> BlockEntityType<T> register(Class<T> tClass, String name, Block... blocks) {
		Validate.isTrue(blocks.length > 0, "no blocks for tile entity type!");
		return register(new Identifier(TechReborn.MOD_ID, name).toString(), BlockEntityType.Builder.create(() -> createBlockEntity(tClass), blocks));
	}

	private static <T extends BlockEntity> T createBlockEntity(Class<T> tClass){
		try {
			return tClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Failed to createBlockEntity tile", e);
		}
	}

	public static <T extends BlockEntity> BlockEntityType<T> register(String id, BlockEntityType.Builder<T> builder) {
		BlockEntityType<T> tileEntityType = builder.build(null);
		Registry.register(Registry.BLOCK_ENTITY, new Identifier(id), tileEntityType);
		TRTileEntities.TYPES.add(tileEntityType);
		return tileEntityType;
	}

}
