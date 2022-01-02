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
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.RebornCore;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.client.screen.BuiltScreenHandlerProvider;
import reborncore.client.screen.builder.BuiltScreenHandler;
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
import techreborn.blockentity.machine.multiblock.*;
import techreborn.blockentity.machine.tier1.*;
import techreborn.blockentity.machine.tier3.ChunkLoaderBlockEntity;
import techreborn.blockentity.machine.tier3.IndustrialCentrifugeBlockEntity;
import techreborn.blockentity.machine.tier3.MatterFabricatorBlockEntity;
import techreborn.blockentity.storage.energy.AdjustableSUBlockEntity;
import techreborn.blockentity.storage.energy.HighVoltageSUBlockEntity;
import techreborn.blockentity.storage.energy.LowVoltageSUBlockEntity;
import techreborn.blockentity.storage.energy.MediumVoltageSUBlockEntity;
import techreborn.blockentity.storage.energy.msb.MoltenSaltBatteryBlockEntity;
import techreborn.blockentity.storage.energy.idsu.InterdimensionalSUBlockEntity;
import techreborn.blockentity.storage.energy.lesu.LapotronicSUBlockEntity;
import techreborn.blockentity.storage.fluid.TankUnitBaseBlockEntity;
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;
import techreborn.client.gui.*;

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
	public static final GuiType<PlayerDetectorBlockEntity> PLAYER_DETECTOR = register("player_detector", () -> () -> GuiPlayerDetector::new);
	public static final GuiType<MoltenSaltBatteryBlockEntity> MOLTEN_SALT_BATTERY = register("molten_salt_battery", () -> () -> GuiMoltenSaltBattery::new);

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
	private final Supplier<Supplier<GuiFactory<T>>> guiFactory;
	private final ScreenHandlerType<BuiltScreenHandler> screenHandlerType;

	private GuiType(Identifier identifier, Supplier<Supplier<GuiFactory<T>>> factorySupplierMeme) {
		this.identifier = identifier;
		this.guiFactory = factorySupplierMeme;
		this.screenHandlerType = ScreenHandlerRegistry.registerExtended(identifier, getScreenHandlerFactory());
		RebornCore.clientOnly(() -> () -> ScreenRegistry.register(screenHandlerType, getGuiFactory()));
	}

	private ScreenHandlerRegistry.ExtendedClientHandlerFactory<BuiltScreenHandler> getScreenHandlerFactory() {
		return (syncId, playerInventory, packetByteBuf) -> {
			final BlockEntity blockEntity = playerInventory.player.world.getBlockEntity(packetByteBuf.readBlockPos());
			BuiltScreenHandler screenHandler = ((BuiltScreenHandlerProvider) blockEntity).createScreenHandler(syncId, playerInventory.player);

			//Set the screen handler type, not ideal but works lol
			screenHandler.setType(screenHandlerType);

			return screenHandler;
		};
	}

	@Environment(EnvType.CLIENT)
	private GuiFactory<T> getGuiFactory() {
		return guiFactory.get().get();
	}

	@Override
	public void open(PlayerEntity player, BlockPos pos, World world) {
		if (!world.isClient) {
			//This is awful
			player.openHandledScreen(new ExtendedScreenHandlerFactory() {
				@Override
				public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf) {
					packetByteBuf.writeBlockPos(pos);
				}

				@Override
				public Text getDisplayName() {
					return new LiteralText("What is this for?");
				}

				@Nullable
				@Override
				public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
					final BlockEntity blockEntity = player.world.getBlockEntity(pos);
					BuiltScreenHandler screenHandler = ((BuiltScreenHandlerProvider) blockEntity).createScreenHandler(syncId, player);
					screenHandler.setType(screenHandlerType);
					return screenHandler;
				}
			});
		}
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	@Environment(EnvType.CLIENT)
	public interface GuiFactory<T extends BlockEntity> extends ScreenRegistry.Factory<BuiltScreenHandler, HandledScreen<BuiltScreenHandler>> {
		HandledScreen<?> create(int syncId, PlayerEntity playerEntity, T blockEntity);

		@Override
		default HandledScreen create(BuiltScreenHandler builtScreenHandler, PlayerInventory playerInventory, Text text) {
			PlayerEntity playerEntity = playerInventory.player;
			//noinspection unchecked
			T blockEntity = (T) builtScreenHandler.getBlockEntity();
			return create(builtScreenHandler.syncId, playerEntity, blockEntity);
		}
	}

}
