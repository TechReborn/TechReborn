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
import techreborn.TechReborn;
import techreborn.blockentity.*;
import techreborn.blockentity.cable.CableBlockEntity;
import techreborn.blockentity.fusionReactor.FusionControlComputerBlockEntity;
import techreborn.blockentity.generator.LightningRodBlockEntity;
import techreborn.blockentity.generator.PlasmaGeneratorBlockEntity;
import techreborn.blockentity.generator.SolarPanelBlockEntity;
import techreborn.blockentity.generator.advanced.*;
import techreborn.blockentity.generator.basic.SolidFuelGeneratorBlockEntity;
import techreborn.blockentity.generator.basic.WaterMillBlockEntity;
import techreborn.blockentity.generator.basic.WindMillBlockEntity;
import techreborn.blockentity.lighting.LampBlockEntity;
import techreborn.blockentity.machine.iron.IronAlloyFurnaceBlockEntity;
import techreborn.blockentity.machine.iron.IronFurnaceBlockEntity;
import techreborn.blockentity.machine.multiblock.*;
import techreborn.blockentity.machine.tier1.*;
import techreborn.blockentity.machine.tier3.*;
import techreborn.blockentity.storage.AdjustableSUBlockEntity;
import techreborn.blockentity.storage.HighVoltageSUBlockEntity;
import techreborn.blockentity.storage.LowVoltageSUBlockEntity;
import techreborn.blockentity.storage.MediumVoltageSUBlockEntity;
import techreborn.blockentity.storage.idsu.InterdimensionalSUBlockEntity;
import techreborn.blockentity.storage.lesu.LSUStorageBlockEntity;
import techreborn.blockentity.storage.lesu.LapotronicSUBlockEntity;
import techreborn.blockentity.transformers.EVTransformerBlockEntity;
import techreborn.blockentity.transformers.HVTransformerBlockEntity;
import techreborn.blockentity.transformers.LVTransformerBlockEntity;
import techreborn.blockentity.transformers.MVTransformerBlockEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TRBlockEntities {
	
	private static List<BlockEntityType<?>> TYPES = new ArrayList<>();

	public static final BlockEntityType<ThermalGeneratorBlockEntity> THERMAL_GEN = register(ThermalGeneratorBlockEntity.class, "thermal_generator", TRContent.Machine.THERMAL_GENERATOR);
	public static final BlockEntityType<QuantumTankBlockEntity> QUANTUM_TANK = register(QuantumTankBlockEntity.class, "quantum_tank", TRContent.Machine.QUANTUM_TANK);
	public static final BlockEntityType<QuantumChestBlockEntity> QUANTUM_CHEST = register(QuantumChestBlockEntity.class, "quantum_chest", TRContent.Machine.QUANTUM_CHEST);
	public static final BlockEntityType<DigitalChestBlockEntity> DIGITAL_CHEST = register(DigitalChestBlockEntity.class, "digital_chest", TRContent.Machine.DIGITAL_CHEST);
	public static final BlockEntityType<IndustrialCentrifugeBlockEntity> INDUSTRIAL_CENTRIFUGE = register(IndustrialCentrifugeBlockEntity.class, "industrial_centrifuge", TRContent.Machine.INDUSTRIAL_CENTRIFUGE);
	public static final BlockEntityType<RollingMachineBlockEntity> ROLLING_MACHINE = register(RollingMachineBlockEntity.class, "rolling_machine", TRContent.Machine.ROLLING_MACHINE);
	public static final BlockEntityType<IndustrialBlastFurnaceBlockEntity> INDUSTRIAL_BLAST_FURNACE = register(IndustrialBlastFurnaceBlockEntity.class, "industrial_blast_furnace", TRContent.Machine.INDUSTRIAL_BLAST_FURNACE);
	public static final BlockEntityType<AlloySmelterBlockEntity> ALLOY_SMELTER = register(AlloySmelterBlockEntity.class, "alloy_smelter", TRContent.Machine.ALLOY_SMELTER);
	public static final BlockEntityType<IndustrialGrinderBlockEntity> INDUSTRIAL_GRINDER = register(IndustrialGrinderBlockEntity.class, "industrial_grinder", TRContent.Machine.INDUSTRIAL_GRINDER);
	public static final BlockEntityType<ImplosionCompressorBlockEntity> IMPLOSION_COMPRESSOR = register(ImplosionCompressorBlockEntity.class, "implosion_compressor", TRContent.Machine.IMPLOSION_COMPRESSOR);
	public static final BlockEntityType<MatterFabricatorBlockEntity> MATTER_FABRICATOR = register(MatterFabricatorBlockEntity.class, "matter_fabricator", TRContent.Machine.MATTER_FABRICATOR);
	public static final BlockEntityType<ChunkLoaderBlockEntity> CHUNK_LOADER = register(ChunkLoaderBlockEntity.class, "chunk_loader", TRContent.Machine.CHUNK_LOADER);
	public static final BlockEntityType<ChargeOMatBlockEntity> CHARGE_O_MAT = register(ChargeOMatBlockEntity.class, "charge_o_mat", TRContent.Machine.CHARGE_O_MAT);
	public static final BlockEntityType<PlayerDectectorBlockEntity> PLAYER_DETECTOR = register(PlayerDectectorBlockEntity.class, "player_detector", TRContent.Machine.PLAYER_DETECTOR);
	public static final BlockEntityType<CableBlockEntity> CABLE = register(CableBlockEntity.class, "cable", TRContent.Cables.values());
	public static final BlockEntityType<MachineCasingBlockEntity> MACHINE_CASINGS = register(MachineCasingBlockEntity.class, "machine_casing", TRContent.MachineBlocks.getCasings());
	public static final BlockEntityType<DragonEggSyphonBlockEntity> DRAGON_EGG_SYPHON = register(DragonEggSyphonBlockEntity.class, "dragon_egg_syphon", TRContent.Machine.DRAGON_EGG_SYPHON);
	public static final BlockEntityType<AssemblingMachineBlockEntity> ASSEMBLY_MACHINE = register(AssemblingMachineBlockEntity.class, "assembly_machine", TRContent.Machine.ASSEMBLY_MACHINE);
	public static final BlockEntityType<DieselGeneratorBlockEntity> DIESEL_GENERATOR = register(DieselGeneratorBlockEntity.class, "diesel_generator", TRContent.Machine.DIESEL_GENERATOR);
	public static final BlockEntityType<IndustrialElectrolyzerBlockEntity> INDUSTRIAL_ELECTROLYZER = register(IndustrialElectrolyzerBlockEntity.class, "industrial_electrolyzer", TRContent.Machine.INDUSTRIAL_ELECTROLYZER);
	public static final BlockEntityType<SemiFluidGeneratorBlockEntity> SEMI_FLUID_GENERATOR = register(SemiFluidGeneratorBlockEntity.class, "semi_fluid_generator", TRContent.Machine.SEMI_FLUID_GENERATOR);
	public static final BlockEntityType<GasTurbineBlockEntity> GAS_TURBINE = register(GasTurbineBlockEntity.class, "gas_turbine", TRContent.Machine.GAS_TURBINE);
	public static final BlockEntityType<IronAlloyFurnaceBlockEntity> IRON_ALLOY_FURNACE = register(IronAlloyFurnaceBlockEntity.class, "iron_alloy_furnace", TRContent.Machine.IRON_ALLOY_FURNACE);
	public static final BlockEntityType<ChemicalReactorBlockEntity> CHEMICAL_REACTOR = register(ChemicalReactorBlockEntity.class, "chemical_reactor", TRContent.Machine.CHEMICAL_REACTOR);
	public static final BlockEntityType<InterdimensionalSUBlockEntity> INTERDIMENSIONAL_SU = register(InterdimensionalSUBlockEntity.class, "interdimensional_su", TRContent.Machine.INTERDIMENSIONAL_SU);
	public static final BlockEntityType<AdjustableSUBlockEntity> ADJUSTABLE_SU = register(AdjustableSUBlockEntity.class, "adjustable_su", TRContent.Machine.ADJUSTABLE_SU);
	public static final BlockEntityType<LapotronicSUBlockEntity> LAPOTRONIC_SU = register(LapotronicSUBlockEntity.class, "lapotronic_su", TRContent.Machine.LAPOTRONIC_SU);
	public static final BlockEntityType<LSUStorageBlockEntity> LSU_STORAGE = register(LSUStorageBlockEntity.class, "lsu_storage", TRContent.Machine.LSU_STORAGE);
	public static final BlockEntityType<DistillationTowerBlockEntity> DISTILLATION_TOWER = register(DistillationTowerBlockEntity.class, "distillation_tower", TRContent.Machine.DISTILLATION_TOWER);
	public static final BlockEntityType<VacuumFreezerBlockEntity> VACUUM_FREEZER = register(VacuumFreezerBlockEntity.class, "vacuum_freezer", TRContent.Machine.VACUUM_FREEZER);
	public static final BlockEntityType<FusionControlComputerBlockEntity> FUSION_CONTROL_COMPUTER = register(FusionControlComputerBlockEntity.class, "fusion_control_computer", TRContent.Machine.FUSION_CONTROL_COMPUTER);
	public static final BlockEntityType<LightningRodBlockEntity> LIGHTNING_ROD = register(LightningRodBlockEntity.class, "lightning_rod", TRContent.Machine.LIGHTNING_ROD);
	public static final BlockEntityType<IndustrialSawmillBlockEntity> INDUSTRIAL_SAWMILL = register(IndustrialSawmillBlockEntity.class, "industrial_sawmill", TRContent.Machine.INDUSTRIAL_SAWMILL);
	public static final BlockEntityType<GrinderBlockEntity> GRINDER = register(GrinderBlockEntity.class, "grinder", TRContent.Machine.GRINDER);
	public static final BlockEntityType<SolidFuelGeneratorBlockEntity> SOLID_FUEL_GENEREATOR = register(SolidFuelGeneratorBlockEntity.class, "solid_fuel_generator", TRContent.Machine.SOLID_FUEL_GENERATOR);
	public static final BlockEntityType<ExtractorBlockEntity> EXTRACTOR = register(ExtractorBlockEntity.class, "extractor", TRContent.Machine.EXTRACTOR);
	public static final BlockEntityType<CompressorBlockEntity> COMPRESSOR = register(CompressorBlockEntity.class, "compressor", TRContent.Machine.COMPRESSOR);
	public static final BlockEntityType<ElectricFurnaceBlockEntity> ELECTRIC_FURNACE = register(ElectricFurnaceBlockEntity.class, "electric_furnace", TRContent.Machine.ELECTRIC_FURNACE);
	public static final BlockEntityType<SolarPanelBlockEntity> SOLAR_PANEL = register(SolarPanelBlockEntity.class, "solar_panel", TRContent.SolarPanels.values());
	public static final BlockEntityType<CreativeQuantumTankBlockEntity> CREATIVE_QUANTUM_TANK = register(CreativeQuantumTankBlockEntity.class, "creative_quantum_tank", TRContent.Machine.CREATIVE_QUANTUM_TANK);
	public static final BlockEntityType<CreativeQuantumChestBlockEntity> CREATIVE_QUANTUM_CHEST = register(CreativeQuantumChestBlockEntity.class, "creative_quantum_chest", TRContent.Machine.CREATIVE_QUANTUM_CHEST);
	public static final BlockEntityType<WaterMillBlockEntity> WATER_MILL = register(WaterMillBlockEntity.class, "water_mill", TRContent.Machine.WATER_MILL);
	public static final BlockEntityType<WindMillBlockEntity> WIND_MILL = register(WindMillBlockEntity.class, "wind_mill", TRContent.Machine.WIND_MILL);
	public static final BlockEntityType<RecyclerBlockEntity> RECYCLER = register(RecyclerBlockEntity.class, "recycler", TRContent.Machine.RECYCLER);
	public static final BlockEntityType<LowVoltageSUBlockEntity> LOW_VOLTAGE_SU = register(LowVoltageSUBlockEntity.class, "low_voltage_su", TRContent.Machine.LOW_VOLTAGE_SU);
	public static final BlockEntityType<MediumVoltageSUBlockEntity> MEDIUM_VOLTAGE_SU = register(MediumVoltageSUBlockEntity.class, "medium_voltage_su", TRContent.Machine.MEDIUM_VOLTAGE_SU);
	public static final BlockEntityType<HighVoltageSUBlockEntity> HIGH_VOLTAGE_SU = register(HighVoltageSUBlockEntity.class, "high_voltage_su", TRContent.Machine.HIGH_VOLTAGE_SU);
	public static final BlockEntityType<LVTransformerBlockEntity> LV_TRANSFORMER = register(LVTransformerBlockEntity.class, "lv_transformer", TRContent.Machine.LV_TRANSFORMER);
	public static final BlockEntityType<MVTransformerBlockEntity> MV_TRANSFORMER = register(MVTransformerBlockEntity.class, "mv_transformer", TRContent.Machine.MV_TRANSFORMER);
	public static final BlockEntityType<HVTransformerBlockEntity> HV_TRANSFORMER = register(HVTransformerBlockEntity.class, "hv_transformer", TRContent.Machine.HV_TRANSFORMER);
	public static final BlockEntityType<EVTransformerBlockEntity> EV_TRANSFORMER = register(EVTransformerBlockEntity.class, "ev_transformer", TRContent.Machine.EV_TRANSFORMER);
	public static final BlockEntityType<AutoCraftingTableBlockEntity> AUTO_CRAFTING_TABLE = register(AutoCraftingTableBlockEntity.class, "auto_crafting_table", TRContent.Machine.AUTO_CRAFTING_TABLE);
	public static final BlockEntityType<IronFurnaceBlockEntity> IRON_FURNACE = register(IronFurnaceBlockEntity.class, "iron_furnace", TRContent.Machine.IRON_FURNACE);
	public static final BlockEntityType<ScrapboxinatorBlockEntity> SCRAPBOXINATOR = register(ScrapboxinatorBlockEntity.class, "scrapboxinator", TRContent.Machine.SCRAPBOXINATOR);
	public static final BlockEntityType<PlasmaGeneratorBlockEntity> PLASMA_GENERATOR = register(PlasmaGeneratorBlockEntity.class, "plasma_generator", TRContent.Machine.PLASMA_GENERATOR);
	public static final BlockEntityType<LampBlockEntity> LAMP = register(LampBlockEntity.class, "lamp", TRContent.Machine.LAMP_INCANDESCENT, TRContent.Machine.LAMP_LED);
	public static final BlockEntityType<AlarmBlockEntity> ALARM = register(AlarmBlockEntity.class, "alarm", TRContent.Machine.ALARM);
	public static final BlockEntityType<FluidReplicatorBlockEntity> FLUID_REPLICATOR = register(FluidReplicatorBlockEntity.class, "fluid_replicator", TRContent.Machine.FLUID_REPLICATOR);
	public static final BlockEntityType<SoildCanningMachineBlockEntity> SOLID_CANNING_MACHINE = register(SoildCanningMachineBlockEntity.class, "solid_canning_machine", TRContent.Machine.SOLID_CANNING_MACHINE);

	public static <T extends BlockEntity> BlockEntityType<T> register(Class<T> tClass, String name, ItemConvertible... items) {
		return register(tClass, name, Arrays.stream(items).map(itemConvertible -> Block.getBlockFromItem(itemConvertible.asItem())).toArray(Block[]::new));
	}

	public static <T extends BlockEntity> BlockEntityType<T> register(Class<T> tClass, String name, Block... blocks) {
		Validate.isTrue(blocks.length > 0, "no blocks for blockEntity entity type!");
		return register(new Identifier(TechReborn.MOD_ID, name).toString(), BlockEntityType.Builder.create(() -> createBlockEntity(tClass), blocks));
	}

	private static <T extends BlockEntity> T createBlockEntity(Class<T> tClass){
		try {
			return tClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Failed to createBlockEntity blockEntity", e);
		}
	}

	public static <T extends BlockEntity> BlockEntityType<T> register(String id, BlockEntityType.Builder<T> builder) {
		BlockEntityType<T> blockEntityType = builder.build(null);
		Registry.register(Registry.BLOCK_ENTITY, new Identifier(id), blockEntityType);
		TRBlockEntities.TYPES.add(blockEntityType);
		return blockEntityType;
	}

}
