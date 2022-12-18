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

package techreborn.blockentity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
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
import techreborn.blockentity.machine.tier0.block.BlockBreakerBlockEntity;
import techreborn.blockentity.machine.tier0.block.BlockPlacerBlockEntity;
import techreborn.blockentity.machine.tier1.*;
import techreborn.blockentity.machine.tier2.FishingStationBlockEntity;
import techreborn.blockentity.machine.tier2.LaunchpadBlockEntity;
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

import java.util.HashMap;
import java.util.Map;

public final class GuiType<T extends BlockEntity> implements IMachineGuiHandler {
	public static final Map<Identifier, GuiType<?>> TYPES = new HashMap<>();

	public static final GuiType<AdjustableSUBlockEntity> AESU = register("aesu");
	public static final GuiType<IronAlloyFurnaceBlockEntity> ALLOY_FURNACE = register("alloy_furnace");
	public static final GuiType<AlloySmelterBlockEntity> ALLOY_SMELTER = register("alloy_smelter");
	public static final GuiType<AssemblingMachineBlockEntity> ASSEMBLING_MACHINE = register("assembling_machine");
	public static final GuiType<LowVoltageSUBlockEntity> LOW_VOLTAGE_SU = register("low_voltage_su");
	public static final GuiType<AutoCraftingTableBlockEntity> AUTO_CRAFTING_TABLE = register("auto_crafting_table");
	public static final GuiType<IndustrialBlastFurnaceBlockEntity> BLAST_FURNACE = register("blast_furnace");
	public static final GuiType<IndustrialCentrifugeBlockEntity> CENTRIFUGE = register("centrifuge");
	public static final GuiType<ChargeOMatBlockEntity> CHARGEBENCH = register("chargebench");
	public static final GuiType<ChemicalReactorBlockEntity> CHEMICAL_REACTOR = register("chemical_reactor");
	public static final GuiType<ChunkLoaderBlockEntity> CHUNK_LOADER = register("chunk_loader");
	public static final GuiType<CompressorBlockEntity> COMPRESSOR = register("compressor");
	public static final GuiType<DieselGeneratorBlockEntity> DIESEL_GENERATOR = register("diesel_generator");
	public static final GuiType<DistillationTowerBlockEntity> DISTILLATION_TOWER = register("distillation_tower");
	public static final GuiType<ElectricFurnaceBlockEntity> ELECTRIC_FURNACE = register("electric_furnace");
	public static final GuiType<ExtractorBlockEntity> EXTRACTOR = register("extractor");
	public static final GuiType<FusionControlComputerBlockEntity> FUSION_CONTROLLER = register("fusion_controller");
	public static final GuiType<GasTurbineBlockEntity> GAS_TURBINE = register("gas_turbine");
	public static final GuiType<SolidFuelGeneratorBlockEntity> GENERATOR = register("generator");
	public static final GuiType<HighVoltageSUBlockEntity> HIGH_VOLTAGE_SU = register("high_voltage_su");
	public static final GuiType<InterdimensionalSUBlockEntity> IDSU = register("idsu");
	public static final GuiType<ImplosionCompressorBlockEntity> IMPLOSION_COMPRESSOR = register("implosion_compressor");
	public static final GuiType<IndustrialElectrolyzerBlockEntity> INDUSTRIAL_ELECTROLYZER = register("industrial_electrolyzer");
	public static final GuiType<IndustrialGrinderBlockEntity> INDUSTRIAL_GRINDER = register("industrial_grinder");
	public static final GuiType<LapotronicSUBlockEntity> LESU = register("lesu");
	public static final GuiType<MatterFabricatorBlockEntity> MATTER_FABRICATOR = register("matter_fabricator");
	public static final GuiType<MediumVoltageSUBlockEntity> MEDIUM_VOLTAGE_SU = register("medium_voltage_su");
	public static final GuiType<PlasmaGeneratorBlockEntity> PLASMA_GENERATOR = register("plasma_generator");
	public static final GuiType<IronFurnaceBlockEntity> IRON_FURNACE = register("iron_furnace");
	public static final GuiType<StorageUnitBaseBlockEntity> STORAGE_UNIT = register("storage_unit");
	public static final GuiType<TankUnitBaseBlockEntity> TANK_UNIT = register("tank_unit");
	public static final GuiType<RecyclerBlockEntity> RECYCLER = register("recycler");
	public static final GuiType<RollingMachineBlockEntity> ROLLING_MACHINE = register("rolling_machine");
	public static final GuiType<IndustrialSawmillBlockEntity> SAWMILL = register("sawmill");
	public static final GuiType<GrinderBlockEntity> GRINDER = register("grinder");
	public static final GuiType<ScrapboxinatorBlockEntity> SCRAPBOXINATOR = register("scrapboxinator");
	public static final GuiType<SolarPanelBlockEntity> SOLAR_PANEL = register("solar_panel");
	public static final GuiType<SemiFluidGeneratorBlockEntity> SEMIFLUID_GENERATOR = register("semifluid_generator");
	public static final GuiType<ThermalGeneratorBlockEntity> THERMAL_GENERATOR = register("thermal_generator");
	public static final GuiType<VacuumFreezerBlockEntity> VACUUM_FREEZER = register("vacuum_freezer");
	public static final GuiType<SolidCanningMachineBlockEntity> SOLID_CANNING_MACHINE = register("solid_canning_machine");
	public static final GuiType<WireMillBlockEntity> WIRE_MILL = register("wire_mill");
	public static final GuiType<GreenhouseControllerBlockEntity> GREENHOUSE_CONTROLLER = register("greenhouse_controller");
	public static final GuiType<FluidReplicatorBlockEntity> FLUID_REPLICATOR = register("fluid_replicator");
	public static final GuiType<PlayerDetectorBlockEntity> PLAYER_DETECTOR = register("player_detector");
	public static final GuiType<BlockBreakerBlockEntity> BLOCK_BREAKER = register("block_breaker");
	public static final GuiType<BlockPlacerBlockEntity> BLOCK_PLACER = register("block_placer");
	public static final GuiType<LaunchpadBlockEntity> LAUNCHPAD = register("launchpad");
	public static final GuiType<ElevatorBlockEntity> ELEVATOR = register("elevator");
	public static final GuiType<FishingStationBlockEntity> FISHING_STATION = register("fishing_station");

	private static <T extends BlockEntity> GuiType<T> register(String id) {
		return register(new Identifier("techreborn", id));
	}

	public static <T extends BlockEntity> GuiType<T> register(Identifier identifier) {
		if (TYPES.containsKey(identifier)) {
			throw new RuntimeException("Duplicate gui type found");
		}

		return new GuiType<>(identifier);
	}

	private final Identifier identifier;
	private final ScreenHandlerType<BuiltScreenHandler> screenHandlerType;

	private GuiType(Identifier identifier) {
		this.identifier = identifier;
		this.screenHandlerType = Registry.register(Registries.SCREEN_HANDLER, identifier, new ExtendedScreenHandlerType<>(getScreenHandlerFactory()));

		TYPES.put(identifier, this);
	}

	private ExtendedScreenHandlerType.ExtendedFactory<BuiltScreenHandler> getScreenHandlerFactory() {
		return (syncId, playerInventory, packetByteBuf) -> {

			final BlockEntity blockEntity = playerInventory.player.world.getBlockEntity(packetByteBuf.readBlockPos());
			BuiltScreenHandler screenHandler = ((BuiltScreenHandlerProvider) blockEntity).createScreenHandler(syncId, playerInventory.player);

			//Set the screen handler type, not ideal but works lol
			screenHandler.setType(screenHandlerType);

			return screenHandler;
		};
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
					return Text.literal("What is this for?");
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

	public ScreenHandlerType<BuiltScreenHandler> getScreenHandlerType() {
		return screenHandlerType;
	}
}
