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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IFixableData;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.registry.GameRegistry;
import reborncore.common.tile.TileLegacyMachineBase;
import techreborn.lib.ModInfo;
import techreborn.tiles.*;
import techreborn.tiles.cable.TileCable;
import techreborn.tiles.cable.TileCableEU;
import techreborn.tiles.fusionReactor.TileFusionControlComputer;
import techreborn.tiles.generator.*;
import techreborn.tiles.idsu.TileInterdimensionalSU;
import techreborn.tiles.lesu.TileLSUStorage;
import techreborn.tiles.lesu.TileLapotronicSU;
import techreborn.tiles.lighting.TileLamp;
import techreborn.tiles.multiblock.*;
import techreborn.tiles.storage.TileAdjustableSU;
import techreborn.tiles.storage.TileHighVoltageSU;
import techreborn.tiles.storage.TileLowVoltageSU;
import techreborn.tiles.storage.TileMediumVoltageSU;
import techreborn.tiles.tier0.TileIronAlloyFurnace;
import techreborn.tiles.tier0.TileIronFurnace;
import techreborn.tiles.tier1.*;
import techreborn.tiles.transformers.TileHVTransformer;
import techreborn.tiles.transformers.TileLVTransformer;
import techreborn.tiles.transformers.TileMVTransformer;

import java.util.Arrays;
import java.util.Optional;

public enum ModTileEntities {

	ADJUSTABLE_SU(TileAdjustableSU.class,  "adjustable_su", "TileAdjustableSUTR"),
	ALARM(TileAlarm.class,  "alarm", "TileAlarmTR"),
	ALLOY_SMELTER(TileAlloySmelter.class,  "alloy_smelter", "TileAlloySmalterTR"),
	ASSEMBLY_MACHINE(TileAssemblingMachine.class,  "assembly_machine", "TileAssemblyMachineTR"),
	AUTO_CRAFTING_TABLE(TileAutoCraftingTable.class,  "auto_crafting_table", "TileAutoCraftingTableTR"),
	CABLE(TileCable.class,  "cable", "TileCableTR"),
	CABLE_EU(TileCableEU.class,  "cable_eu", "TileCableEUTR"),
	CHARGE_O_MAT(TileChargeOMat.class,  "charge_o_mat", "TileChargeOMatTR"),
	CHEMICAL_REACTOR(TileChemicalReactor.class,  "chemical_reactor", "TileChemicalReactorTR"),
	CHUNK_LOADER(TileChunkLoader.class,  "chunk_loader", "TileChunkLoaderTR"),
	COMPRESSOR(TileCompressor.class,  "compressor", "TileCompressorTR"),
	CREATIVE_QUANTUM_CHEST(TileCreativeQuantumChest.class,  "creative_quantum_chest"),
	CREATIVE_QUANTUM_TANK(TileCreativeQuantumTank.class,  "creative_quantum_tank"),
	CREATIVE_SOLAR_PANEL(TileCreativeSolarPanel.class,  "creative_solar_panel", "TileCreativeSolarPanelTR"),
	DIESEL_GENERATOR(TileDieselGenerator.class,  "diesel_generator", "TileDieselGeneratorTR"),
	DIGITAL_CHEST(TileDigitalChest.class,  "digital_chest", "TileDigitalChestTR"),
	DISTILLATION_TOWER(TileDistillationTower.class,  "distillation_tower", "TileDistillationTowerTR"),
	DRAGON_EGG_SYPHON(TileDragonEggSyphon.class,  "dragon_egg_syphon", "TileDragonEggSyphonTR"),
	ELECTRIC_FURNACE(TileElectricFurnace.class,  "electric_furnace", "TileElectricFurnaceTR"),
	EXTRACTOR(TileExtractor.class,  "extractor", "TileExtractorTR"),
	FLUID_REPLICATOR(TileFluidReplicator.class,  "fluid_replicator", "TileFluidReplicatorTR"),
	FUSION_CONTROL_COMPUTER(TileFusionControlComputer.class,  "fusion_control_computer", "TileFusionControlComputerTR"),
	GAS_TURBINE(TileGasTurbine.class,  "gas_turbine", "TileGasTurbineTR"),
	GRINDER(TileGrinder.class,  "grinder", "TileGrinderTR"),
	HIGH_VOLTAGE_SU(TileHighVoltageSU.class,  "high_voltage_su", "TileHighVoltageSUTR"),
	HV_TRANSFORMER(TileHVTransformer.class,  "hv_transformer", "TileHVTransformerTR"),
	IMPLOSION_COMPRESSOR(TileImplosionCompressor.class,  "implosion_compressor", "TileImplosionCompressorTR"),
	INDUSTRIAL_BLAST_FURNACE(TileIndustrialBlastFurnace.class,  "industrial_blast_furnace", "TileIndustrialBlastFurnaceTR"),
	INDUSTRIAL_CENTRIFUGE(TileIndustrialCentrifuge.class,  "industrial_centrifuge", "TileIndustrialCentrifugeTR"),
	INDUSTRIAL_ELECTROLYZER(TileIndustrialElectrolyzer.class,  "industrial_electrolyzer", "TileIndustrialElectrolyzerTR"),
	INDUSTRIAL_GRINDER(TileIndustrialGrinder.class,  "industrial_grinder", "TileIndustrialGrinderTR"),
	INDUSTRIAL_SAWMILL(TileIndustrialSawmill.class,  "industrial_sawmill", "TileIndustrialSawmillTR"),
	INTERDIMENSIONAL_SU(TileInterdimensionalSU.class,  "interdimensional_su", "TileInterdimensionalSUTR"),
	IRON_ALLOY_FURNACE(TileIronAlloyFurnace.class,  "iron_alloy_furnace", "TileIronAlloyFurnaceTR"),
	IRON_FURNACE(TileIronFurnace.class,  "iron_furnace", "TileIronFurnaceTR"),
	LAMP(TileLamp.class,  "lamp", "TileLampTR"),
	LAPOTRONIC_SU(TileLapotronicSU.class,  "lapotronic_su", "TileLapotronicSUTR"),
	LIGHTNING_ROD(TileLightningRod.class,  "lightning_rod", "TileLightningRodTR"),
	LOW_VOLTAGE_SU(TileLowVoltageSU.class,  "low_voltage_su", "TileLowVoltageSUTR"),
	LSU_STORAGE(TileLSUStorage.class,  "lsu_storage", "TileLSUStorageTR"),
	LV_TRANSFORMER(TileLVTransformer.class,  "lv_transformer", "TileLVTransformerTR"),
	MACHINE_BASE(TileLegacyMachineBase.class,  "machine_base", "TileMachineBaseTR"),
	MACHINE_CASINGS(TileMachineCasing.class,  "machine_casing", "TileMachineCasingTR"),
	MATTER_FABRICATOR(TileMatterFabricator.class,  "matter_fabricator", "TileMatterFabricatorTR"),
	MEDIUM_VOLTAGE_SU(TileMediumVoltageSU.class,  "medium_voltage_su", "TileMediumVoltageSUTR"),
	MV_TRANSFORMER(TileMVTransformer.class,  "mv_transformer", "TileMVTransformerTR"),
	PLASMA_GENERATOR(TilePlasmaGenerator.class,  "plasma_generator", "TilePlasmalGeneratorTR"),
	PLAYER_DETECTOR(TilePlayerDectector.class,  "player_detector", "TilePlayerDectectorTR"),
	QUANTUM_CHEST(TileQuantumChest.class,  "quantum_chest", "TileQuantumChestTR"),
	QUANTUM_TANK(TileQuantumTank.class,  "quantum_tank", "TileQuantumTankTR"),
	RECYCLER(TileRecycler.class,  "recycler", "TileRecyclerTR"),
	ROLLING_MACHINE(TileRollingMachine.class,  "rolling_machine", "TileRollingMachineTR"),
	SCRAPBOXINATOR(TileScrapboxinator.class,  "scrapboxinator", "TileScrapboxinatorTR"),
	SEMI_FLUID_GENERATOR(TileSemiFluidGenerator.class,  "semi_fluid_generator", "TileSemiFluidGeneratorTR"),
	SOLAR_PANEL(TileSolarPanel.class,  "solar_panel", "TileSolarPanelTR"),
	SOLID_FUEL_GENEREATOR(TileSolidFuelGenerator.class,  "solid_fuel_generator", "TileSolidFuelGeneratorTR"),
	THERMAL_GEN(TileThermalGenerator.class,  "thermal_generator", "TileThermalGeneratorTR"),
	VACUUM_FREEZER(TileVacuumFreezer.class,  "vacuum_freezer", "TileVacuumFreezerTR"),
	WATER_MILL(TileWaterMill.class,  "water_mill", "TileWaterMillTR"),
	WIND_MILL(TileWindMill.class,  "wind_mill", "TileWindMillTR");

	public static Optional<ModTileEntities> getFromOldName(String name){
		for(ModTileEntities entry : ModTileEntities.values()){
			if(entry.oldName.isPresent() && entry.oldName.get().equalsIgnoreCase(name)){
				return Optional.of(entry);
			}
		}
		return Optional.empty();
	}
	public static void init(){
		Arrays.stream(ModTileEntities.values())
			.forEach(modTileEntities -> GameRegistry.registerTileEntity(modTileEntities.tileClass, modTileEntities.name));
	}
	public static void initDataFixer(ModFixs dataFixes){
		dataFixes.registerFix(FixTypes.BLOCK_ENTITY, new IFixableData() {
			@Override
			public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
				ResourceLocation tileEntityLocation = new ResourceLocation(compound.getString("id"));
				if(tileEntityLocation.getResourceDomain().equalsIgnoreCase("minecraft")){
					Optional<ModTileEntities> tileData = getFromOldName(tileEntityLocation.getResourcePath());
					tileData.ifPresent(modTileEntities -> compound.setString("id", modTileEntities.name.toString()));
				}
				return compound;
			}

			@Override
			public int getFixVersion() {
				return 1;
			}
		});
	}

	public ResourceLocation name;

	public Optional<String> oldName;

	public Class<? extends TileEntity> tileClass;

	ModTileEntities(Class<? extends TileEntity> tileClass, ResourceLocation name) {
		this.tileClass = tileClass;
		this.name = name;
		this.oldName = Optional.empty();
	}

	ModTileEntities(Class<? extends TileEntity> tileClass, ResourceLocation name, String oldName) {
		this.tileClass = tileClass;
		this.name = name;
		this.oldName = Optional.of(oldName);
	}

	ModTileEntities(Class<? extends TileEntity> tileClass, String name) {
		this(tileClass,  new ResourceLocation(ModInfo.MOD_ID, name));
	}

	ModTileEntities(Class<? extends TileEntity> tileClass, String name, String oldName) {
		this(tileClass, new ResourceLocation(ModInfo.MOD_ID, name), oldName);
	}

	public boolean hasUpgradePath(){
		return oldName.isPresent();
	}
}
