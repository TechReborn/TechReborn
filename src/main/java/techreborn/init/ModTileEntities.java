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

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import reborncore.common.tile.TileLegacyMachineBase;
import techreborn.TechReborn;
import techreborn.tiles.*;
import techreborn.tiles.cable.TileCable;
import techreborn.tiles.fusionReactor.TileFusionControlComputer;
import techreborn.tiles.generator.*;
import techreborn.tiles.generator.advanced.*;
import techreborn.tiles.generator.basic.TileSolidFuelGenerator;
import techreborn.tiles.generator.basic.TileWaterMill;
import techreborn.tiles.generator.basic.TileWindMill;
import techreborn.tiles.storage.idsu.TileInterdimensionalSU;
import techreborn.tiles.storage.lesu.TileLSUStorage;
import techreborn.tiles.storage.lesu.TileLapotronicSU;
import techreborn.tiles.lighting.TileLamp;
import techreborn.tiles.machine.multiblock.*;
import techreborn.tiles.storage.TileAdjustableSU;
import techreborn.tiles.storage.TileHighVoltageSU;
import techreborn.tiles.storage.TileLowVoltageSU;
import techreborn.tiles.storage.TileMediumVoltageSU;
import techreborn.tiles.machine.iron.TileIronAlloyFurnace;
import techreborn.tiles.machine.iron.TileIronFurnace;
import techreborn.tiles.machine.tier1.*;
import techreborn.tiles.transformers.TileHVTransformer;
import techreborn.tiles.transformers.TileLVTransformer;
import techreborn.tiles.transformers.TileMVTransformer;

import java.util.Arrays;

public enum ModTileEntities {

	THERMAL_GEN(TileThermalGenerator.class,  "thermal_generator"),
	QUANTUM_TANK(TileQuantumTank.class,  "quantum_tank"),
	QUANTUM_CHEST(TileQuantumChest.class,  "quantum_chest"),
	DIGITAL_CHEST(TileDigitalChest.class,  "digital_chest"),
	INDUSTRIAL_CENTRIFUGE(TileIndustrialCentrifuge.class,  "industrial_centrifuge"),
	ROLLING_MACHINE(TileRollingMachine.class,  "rolling_machine"),
	INDUSTRIAL_BLAST_FURNACE(TileIndustrialBlastFurnace.class,  "industrial_blast_furnace"),
	ALLOY_SMELTER(TileAlloySmelter.class,  "alloy_smelter"),
	INDUSTRIAL_GRINDER(TileIndustrialGrinder.class,  "industrial_grinder"),
	IMPLOSION_COMPRESSOR(TileImplosionCompressor.class,  "implosion_compressor"),
	MATTER_FABRICATOR(TileMatterFabricator.class,  "matter_fabricator"),
	CHUNK_LOADER(TileChunkLoader.class,  "chunk_loader"),
	CHARGE_O_MAT(TileChargeOMat.class,  "charge_o_mat"),
	PLAYER_DETECTOR(TilePlayerDectector.class,  "player_detector"),
	CABLE(TileCable.class,  "cable"),
	MACHINE_CASINGS(TileMachineCasing.class,  "machine_casing"),
	DRAGON_EGG_SYPHON(TileDragonEggSyphon.class,  "dragon_egg_syphon"),
	ASSEMBLY_MACHINE(TileAssemblingMachine.class,  "assembly_machine"),
	DIESEL_GENERATOR(TileDieselGenerator.class,  "diesel_generator"),
	INDUSTRIAL_ELECTROLYZER(TileIndustrialElectrolyzer.class,  "industrial_electrolyzer"),
	SEMI_FLUID_GENERATOR(TileSemiFluidGenerator.class,  "semi_fluid_generator"),
	GAS_TURBINE(TileGasTurbine.class,  "gas_turbine"),
	IRON_ALLOY_FURNACE(TileIronAlloyFurnace.class,  "iron_alloy_furnace"),
	CHEMICAL_REACTOR(TileChemicalReactor.class,  "chemical_reactor"),
	INTERDIMENSIONAL_SU(TileInterdimensionalSU.class,  "interdimensional_su"),
	ADJUSTABLE_SU(TileAdjustableSU.class,  "adjustable_su"),
	LAPOTRONIC_SU(TileLapotronicSU.class,  "lapotronic_su"),
	LSU_STORAGE(TileLSUStorage.class,  "lsu_storage"),
	DISTILLATION_TOWER(TileDistillationTower.class,  "distillation_tower"),
	VACUUM_FREEZER(TileVacuumFreezer.class,  "vacuum_freezer"),
	FUSION_CONTROL_COMPUTER(TileFusionControlComputer.class,  "fusion_control_computer"),
	LIGHTNING_ROD(TileLightningRod.class,  "lightning_rod"),
	INDUSTRIAL_SAWMILL(TileIndustrialSawmill.class,  "industrial_sawmill"),
	GRINDER(TileGrinder.class,  "grinder"),
	SOLID_FUEL_GENEREATOR(TileSolidFuelGenerator.class,  "solid_fuel_generator"),
	EXTRACTOR(TileExtractor.class,  "extractor"),
	COMPRESSOR(TileCompressor.class,  "compressor"),
	ELECTRIC_FURNACE(TileElectricFurnace.class,  "electric_furnace"),
	SOLAR_PANEL(TileSolarPanel.class,  "solar_panel"),
	CREATIVE_SOLAR_PANEL(TileCreativeSolarPanel.class,  "creative_solar_panel"),
	CREATIVE_QUANTUM_TANK(TileCreativeQuantumTank.class,  "creative_quantum_tank"),
	CREATIVE_QUANTUM_CHEST(TileCreativeQuantumChest.class,  "creative_quantum_chest"),
	WATER_MILL(TileWaterMill.class,  "water_mill"),
	WIND_MILL(TileWindMill.class,  "wind_mill"),
	MACHINE_BASE(TileLegacyMachineBase.class,  "machine_base"),
	RECYCLER(TileRecycler.class,  "recycler"),
	LOW_VOLTAGE_SU(TileLowVoltageSU.class,  "low_voltage_su"),
	MEDIUM_VOLTAGE_SU(TileMediumVoltageSU.class,  "medium_voltage_su"),
	HIGH_VOLTAGE_SU(TileHighVoltageSU.class,  "high_voltage_su"),
	LV_TRANSFORMER(TileLVTransformer.class,  "lv_transformer"),
	MV_TRANSFORMER(TileMVTransformer.class,  "mv_transformer"),
	HV_TRANSFORMER(TileHVTransformer.class,  "hv_transformer"),
	AUTO_CRAFTING_TABLE(TileAutoCraftingTable.class,  "auto_crafting_table"),
	IRON_FURNACE(TileIronFurnace.class,  "iron_furnace"),
	SCRAPBOXINATOR(TileScrapboxinator.class,  "scrapboxinator"),
	PLASMA_GENERATOR(TilePlasmaGenerator.class,  "plasma_generator"),
	LAMP(TileLamp.class,  "lamp"),
	ALARM(TileAlarm.class,  "alarm"),
	FLUID_REPLICATOR(TileFluidReplicator.class,  "fluid_replicator");

	public Class<? extends TileEntity> tileClass;
	public ResourceLocation name;

	ModTileEntities(Class<? extends TileEntity> tileClass, ResourceLocation name) {
		this.tileClass = tileClass;
		this.name = name;
	}
	ModTileEntities(Class<? extends TileEntity> tileClass, String name) {
		this(tileClass,  new ResourceLocation(TechReborn.MOD_ID, name));
	}


	public static void init(){
		Arrays.stream(ModTileEntities.values())
			.forEach(modTileEntities -> GameRegistry.registerTileEntity(modTileEntities.tileClass, modTileEntities.name));
	}

}
