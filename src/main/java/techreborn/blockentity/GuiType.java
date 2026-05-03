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

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import reborncore.api.blockentity.IMachineGuiHandler;
import reborncore.common.network.BlockPosPayload;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import techreborn.blockentity.generator.PlasmaGeneratorBlockEntity;
import techreborn.blockentity.generator.SolarPanelBlockEntity;
import techreborn.blockentity.generator.advanced.DieselGeneratorBlockEntity;
import techreborn.blockentity.generator.advanced.GasTurbineBlockEntity;
import techreborn.blockentity.generator.nuclear.NuclearReactorBlockEntity;
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
import techreborn.blockentity.machine.tier0.block.BlockBreakerBlockEntity;
import techreborn.blockentity.machine.tier0.block.BlockPlacerBlockEntity;
import techreborn.blockentity.machine.tier1.AlloySmelterBlockEntity;
import techreborn.blockentity.machine.tier1.AssemblingMachineBlockEntity;
import techreborn.blockentity.machine.tier1.AutoCraftingTableBlockEntity;
import techreborn.blockentity.machine.tier1.ChemicalReactorBlockEntity;
import techreborn.blockentity.machine.tier1.CompressorBlockEntity;
import techreborn.blockentity.machine.tier1.ElectricFurnaceBlockEntity;
import techreborn.blockentity.machine.tier1.ElevatorBlockEntity;
import techreborn.blockentity.machine.tier1.ExtractorBlockEntity;
import techreborn.blockentity.machine.tier1.GreenhouseControllerBlockEntity;
import techreborn.blockentity.machine.tier1.GrinderBlockEntity;
import techreborn.blockentity.machine.tier1.IndustrialElectrolyzerBlockEntity;
import techreborn.blockentity.machine.tier1.PlayerDetectorBlockEntity;
import techreborn.blockentity.machine.tier1.RecyclerBlockEntity;
import techreborn.blockentity.machine.tier1.RollingMachineBlockEntity;
import techreborn.blockentity.machine.tier1.ScrapboxinatorBlockEntity;
import techreborn.blockentity.machine.tier1.SolidCanningMachineBlockEntity;
import techreborn.blockentity.machine.tier1.WireMillBlockEntity;
import techreborn.blockentity.machine.tier2.FishingStationBlockEntity;
import techreborn.blockentity.machine.tier2.LaunchpadBlockEntity;
import techreborn.blockentity.machine.tier2.PumpBlockEntity;
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


public record GuiType<T extends BlockEntity>(Identifier identifier, MenuType<BuiltScreenHandler> screenHandlerType) implements IMachineGuiHandler {
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
	public static final GuiType<PumpBlockEntity> PUMP = register("pump");
	public static final GuiType<NuclearReactorBlockEntity> NUCLEAR_REACTOR = register("nuclear_reactor");


	private static <T extends BlockEntity> GuiType<T> register(String path) {
		var id = Identifier.fromNamespaceAndPath("techreborn", path);
		var screenHandlerType = Registry.register(BuiltInRegistries.MENU, id, new ExtendedScreenHandlerType<>(getScreenHandlerFactory(id), ScreenHandlerData.PACKET_CODEC));
		return new GuiType<>(id, screenHandlerType);
	}

	private static ExtendedScreenHandlerType.ExtendedFactory<BuiltScreenHandler, ScreenHandlerData> getScreenHandlerFactory(Identifier identifier) {
		return (syncId, playerInventory, payload) -> {
			if (!payload.isWithinDistance(playerInventory.player, 16)) {
				throw new IllegalStateException("Player cannot use this block entity as its too far away");
			}

			final BlockEntity blockEntity = playerInventory.player.level().getBlockEntity(payload.pos());
			BuiltScreenHandler screenHandler = ((BuiltScreenHandlerProvider) blockEntity).createScreenHandler(syncId, playerInventory.player);

			//noinspection unchecked
			screenHandler.setType((MenuType<BuiltScreenHandler>) BuiltInRegistries.MENU.getValue(identifier));
			return screenHandler;
		};
	}

	public T getBlockEntity(ServerPlayNetworking.Context context, BlockPosPayload posPayload, BlockEntityType<T> blockEntityType) {
		if (!posPayload.canUse(context.player(), screenHandler -> screenHandler.getType() == screenHandlerType)) {
			throw new IllegalStateException("Player cannot use this block entity");
		}

		return posPayload.getBlockEntity(blockEntityType, context.player());
	}

	@Override
	public void open(Player player, BlockPos pos, Level world) {
		if (!world.isClientSide()) {
			//This is awful
			player.openMenu(new ExtendedScreenHandlerFactory<ScreenHandlerData>() {
				@Override
				public ScreenHandlerData getScreenOpeningData(ServerPlayer player) {
					return new ScreenHandlerData(pos);
				}

				@Override
				public Component getDisplayName() {
					return Component.literal("What is this for?");
				}

				@Override
				public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
					final BlockEntity blockEntity = player.level().getBlockEntity(pos);
					BuiltScreenHandler screenHandler = ((BuiltScreenHandlerProvider) blockEntity).createScreenHandler(syncId, player);
					screenHandler.setType(screenHandlerType);
					return screenHandler;
				}
			});
		}
	}

	record ScreenHandlerData(BlockPos pos) implements BlockPosPayload {
		public static final StreamCodec<RegistryFriendlyByteBuf, ScreenHandlerData> PACKET_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC, ScreenHandlerData::pos,
			ScreenHandlerData::new
		);
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public MenuType<BuiltScreenHandler> getScreenHandlerType() {
		return screenHandlerType;
	}
}
