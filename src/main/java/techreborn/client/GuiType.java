package techreborn.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerFactory;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.RebornCore;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.client.containerBuilder.IContainerProvider;
import techreborn.blockentity.data.DataDrivenBEProvider;
import techreborn.blockentity.data.DataDrivenGui;
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
import techreborn.blockentity.machine.tier1.AlloySmelterBlockEntity;
import techreborn.blockentity.machine.tier1.AssemblingMachineBlockEntity;
import techreborn.blockentity.machine.tier1.AutoCraftingTableBlockEntity;
import techreborn.blockentity.machine.tier1.ChemicalReactorBlockEntity;
import techreborn.blockentity.machine.tier1.CompressorBlockEntity;
import techreborn.blockentity.machine.tier1.ElectricFurnaceBlockEntity;
import techreborn.blockentity.machine.tier1.ExtractorBlockEntity;
import techreborn.blockentity.machine.tier1.GreenhouseControllerBlockEntity;
import techreborn.blockentity.machine.tier1.IndustrialElectrolyzerBlockEntity;
import techreborn.blockentity.machine.tier1.RecyclerBlockEntity;
import techreborn.blockentity.machine.tier1.RollingMachineBlockEntity;
import techreborn.blockentity.machine.tier1.ScrapboxinatorBlockEntity;
import techreborn.blockentity.machine.tier1.SoildCanningMachineBlockEntity;
import techreborn.blockentity.machine.tier1.WireMillBlockEntity;
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
import techreborn.client.gui.GuiCentrifuge;
import techreborn.client.gui.GuiChargeBench;
import techreborn.client.gui.GuiChemicalReactor;
import techreborn.client.gui.GuiChunkLoader;
import techreborn.client.gui.GuiCompressor;
import techreborn.client.gui.GuiDieselGenerator;
import techreborn.client.gui.GuiDistillationTower;
import techreborn.client.gui.GuiElectricFurnace;
import techreborn.client.gui.GuiExtractor;
import techreborn.client.gui.GuiFluidReplicator;
import techreborn.client.gui.GuiFusionReactor;
import techreborn.client.gui.GuiGasTurbine;
import techreborn.client.gui.GuiGenerator;
import techreborn.client.gui.GuiGreenhouseController;
import techreborn.client.gui.GuiIDSU;
import techreborn.client.gui.GuiImplosionCompressor;
import techreborn.client.gui.GuiIndustrialElectrolyzer;
import techreborn.client.gui.GuiIndustrialGrinder;
import techreborn.client.gui.GuiIndustrialSawmill;
import techreborn.client.gui.GuiIronFurnace;
import techreborn.client.gui.GuiLESU;
import techreborn.client.gui.GuiMFE;
import techreborn.client.gui.GuiMFSU;
import techreborn.client.gui.GuiMatterFabricator;
import techreborn.client.gui.GuiPlasmaGenerator;
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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

//The meme is here the double suppliers dont cause the client side gui classes to load on the server
public final class GuiType<T extends BlockEntity> implements IMachineGuiHandler {
	private static final Map<Identifier, GuiType<?>> TYPES = new HashMap<>();

	public static final GuiType<AdjustableSUBlockEntity> AESU = register("aesu", () -> () -> GuiAESU::new);
	public static final GuiType<IronAlloyFurnaceBlockEntity> ALLOY_FURNACE = register("alloy_furnace", () -> () -> GuiAlloyFurnace::new);
	public static final GuiType<AlloySmelterBlockEntity> ALLOY_SMELTER = register("alloy_smelter", () -> () -> GuiAlloySmelter::new);
	public static final GuiType<AssemblingMachineBlockEntity> ASSEMBLING_MACHINE = register("assembling_machine", () -> () -> GuiAssemblingMachine::new);
	public static final GuiType<LowVoltageSUBlockEntity> LOW_VOLTAGE_SU = register("low_voltage_su", () -> () -> GuiBatbox::new);
	public static final GuiType<AutoCraftingTableBlockEntity> AUTO_CRAFTING_TABLE = register("auto_crafting_table", () -> () -> GuiAutoCrafting::new);
	public static final GuiType<IndustrialBlastFurnaceBlockEntity> BLAST_FURNACE = register("blast_furnace", () -> () -> GuiBlastFurnace::new);
	public static final GuiType<IndustrialCentrifugeBlockEntity> CENTRIFUGE = register("centrifuge", () -> () -> GuiCentrifuge::new);
	public static final GuiType<ChargeOMatBlockEntity> CHARGEBENCH = register("chargebench", () -> () -> GuiChargeBench::new);
	public static final GuiType<ChemicalReactorBlockEntity> CHEMICAL_REACTOR = register("chemical_reactor", () -> () -> GuiChemicalReactor::new);
	public static final GuiType<ChunkLoaderBlockEntity> CHUNK_LOADER = register("chunk_loader", () -> () -> GuiChunkLoader::new);
	public static final GuiType<CompressorBlockEntity> COMPRESSOR = register("compressor", () -> () -> GuiCompressor::new);
	public static final GuiType<DieselGeneratorBlockEntity> DIESEL_GENERATOR = register("diesel_generator", () -> () -> GuiDieselGenerator::new);
	public static final GuiType<DistillationTowerBlockEntity> DISTILLATION_TOWER = register("distillation_tower", () -> () -> GuiDistillationTower::new);
	public static final GuiType<ElectricFurnaceBlockEntity> ELECTRIC_FURNACE = register("electric_furnace", () -> () -> GuiElectricFurnace::new);
	public static final GuiType<ExtractorBlockEntity> EXTRACTOR = register("extractor", () -> () -> GuiExtractor::new);
	public static final GuiType<FusionControlComputerBlockEntity> FUSION_CONTROLLER = register("fusion_controller", () -> () -> GuiFusionReactor::new);
	public static final GuiType<GasTurbineBlockEntity> GAS_TURBINE = register("gas_turbine", () -> () -> GuiGasTurbine::new);
	public static final GuiType<SolidFuelGeneratorBlockEntity> GENERATOR = register("generator", () -> () -> GuiGenerator::new);
	public static final GuiType<HighVoltageSUBlockEntity> HIGH_VOLTAGE_SU = register("high_voltage_su", () -> () -> GuiMFSU::new);
	public static final GuiType<InterdimensionalSUBlockEntity> IDSU = register("idsu", () -> () -> GuiIDSU::new);
	public static final GuiType<ImplosionCompressorBlockEntity> IMPLOSION_COMPRESSOR = register("implosion_compressor", () -> () -> GuiImplosionCompressor::new);
	public static final GuiType<IndustrialElectrolyzerBlockEntity> INDUSTRIAL_ELECTROLYZER = register("industrial_electrolyzer", () -> () -> GuiIndustrialElectrolyzer::new);
	public static final GuiType<IndustrialGrinderBlockEntity> INDUSTRIAL_GRINDER = register("industrial_grinder", () -> () -> GuiIndustrialGrinder::new);
	public static final GuiType<LapotronicSUBlockEntity> LESU = register("lesu", () -> () -> GuiLESU::new);
	public static final GuiType<MatterFabricatorBlockEntity> MATTER_FABRICATOR = register("matter_fabricator", () -> () -> GuiMatterFabricator::new);
	public static final GuiType<MediumVoltageSUBlockEntity> MEDIUM_VOLTAGE_SU = register("medium_voltage_su", () -> () -> GuiMFE::new);
	public static final GuiType<PlasmaGeneratorBlockEntity> PLASMA_GENERATOR = register("plasma_generator", () -> () -> GuiPlasmaGenerator::new);
	public static final GuiType<IronFurnaceBlockEntity> IRON_FURNACE = register("iron_furnace", () -> () -> GuiIronFurnace::new);
	public static final GuiType<StorageUnitBaseBlockEntity> STORAGE_UNIT = register("storage_unit", () -> () -> GuiStorageUnit::new);
	public static final GuiType<TankUnitBaseBlockEntity> TANK_UNIT = register("tank_unit", () -> () -> GuiTankUnit::new);
	public static final GuiType<RecyclerBlockEntity> RECYCLER = register("recycler", () -> () -> GuiRecycler::new);
	public static final GuiType<RollingMachineBlockEntity> ROLLING_MACHINE = register("rolling_machine", () -> () -> GuiRollingMachine::new);
	public static final GuiType<IndustrialSawmillBlockEntity> SAWMILL = register("sawmill", () -> () -> GuiIndustrialSawmill::new);
	public static final GuiType<ScrapboxinatorBlockEntity> SCRAPBOXINATOR = register("scrapboxinator", () -> () -> GuiScrapboxinator::new);
	public static final GuiType<SolarPanelBlockEntity> SOLAR_PANEL = register("solar_panel", () -> () -> GuiSolar::new);
	public static final GuiType<SemiFluidGeneratorBlockEntity> SEMIFLUID_GENERATOR = register("semifluid_generator", () -> () -> GuiSemifluidGenerator::new);
	public static final GuiType<ThermalGeneratorBlockEntity> THERMAL_GENERATOR = register("thermal_generator", () -> () -> GuiThermalGenerator::new);
	public static final GuiType<VacuumFreezerBlockEntity> VACUUM_FREEZER = register("vacuum_freezer", () -> () -> GuiVacuumFreezer::new);
	public static final GuiType<SoildCanningMachineBlockEntity> SOLID_CANNING_MACHINE = register("solid_canning_machine", () -> () -> GuiSolidCanningMachine::new);
	public static final GuiType<WireMillBlockEntity> WIRE_MILL = register("wire_mill", () -> () -> GuiWireMill::new);
	public static final GuiType<GreenhouseControllerBlockEntity> GREENHOUSE_CONTROLLER = register("greenhouse_controller", () -> () -> GuiGreenhouseController::new);
	public static final GuiType<FluidReplicatorBlockEntity> FLUID_REPLICATOR = register("fluid_replicator", () -> () -> GuiFluidReplicator::new);

	public static final GuiType<DataDrivenBEProvider.DataDrivenBlockEntity> DATA_DRIVEN = register("data_driven", () -> () -> DataDrivenGui::new);

	private static <T extends BlockEntity> GuiType<T> register(String id, Supplier<Supplier<GuiFactory<T>>> factorySupplierMeme) {
		return register(new Identifier("techreborn", id), factorySupplierMeme);
	}

	public static <T extends BlockEntity> GuiType<T> register(Identifier identifier, Supplier<Supplier<GuiFactory<T>>> factorySupplierMeme) {
		if (TYPES.containsKey(identifier)) {
			throw new RuntimeException("Duplicate gui type found");
		}
		GuiType<T> type = new GuiType<>(identifier, factorySupplierMeme);
		TYPES.put(identifier, type);
		return type;
	}

	private final Identifier identifier;
	private final Supplier<Supplier<GuiFactory<T>>> factorySupplierMeme;

	private GuiType(Identifier identifier, Supplier<Supplier<GuiFactory<T>>> factorySupplierMeme) {
		this.identifier = identifier;
		this.factorySupplierMeme = factorySupplierMeme;
		register();
	}

	@Environment(EnvType.CLIENT)
	private GuiFactory<T> getFactory() {
		return factorySupplierMeme.get().get();
	}

	@Override
	public void open(PlayerEntity player, BlockPos pos, World world) {
		if(!world.isClient){
			ContainerProviderRegistry.INSTANCE.openContainer(identifier, player, packetByteBuf -> packetByteBuf.writeBlockPos(pos));
		}
	}

	private void register() {
		ContainerProviderRegistry.INSTANCE.registerFactory(identifier, (syncID, identifier, playerEntity, packetByteBuf) -> {
			final BlockEntity blockEntity = playerEntity.world.getBlockEntity(packetByteBuf.readBlockPos());
			return ((IContainerProvider) blockEntity).createContainer(syncID, playerEntity);
		});
		RebornCore.clientOnly(() -> () -> ScreenProviderRegistry.INSTANCE.registerFactory(identifier, getFactory()));
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	@Environment(EnvType.CLIENT)
	public interface GuiFactory<T extends BlockEntity> extends ContainerFactory<ContainerScreen> {
		ContainerScreen<?> create(int syncId, PlayerEntity playerEntity, T blockEntity);

		@Override
		default ContainerScreen create(int syncId, Identifier identifier, PlayerEntity playerEntity, PacketByteBuf packetByteBuf) {
			//noinspection unchecked
			T blockEntity = (T) playerEntity.world.getBlockEntity(packetByteBuf.readBlockPos());
			return create(syncId, playerEntity, blockEntity);
		}
	}

}
