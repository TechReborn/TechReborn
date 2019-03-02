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

package techreborn.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import reborncore.api.tile.IMachineGuiHandler;
import reborncore.client.containerBuilder.IContainerProvider;
import techreborn.client.container.ContainerDestructoPack;
import techreborn.tiles.*;
import techreborn.tiles.fusionReactor.TileFusionControlComputer;
import techreborn.tiles.generator.*;
import techreborn.tiles.generator.advanced.TileDieselGenerator;
import techreborn.tiles.generator.advanced.TileGasTurbine;
import techreborn.tiles.generator.advanced.TileSemiFluidGenerator;
import techreborn.tiles.generator.advanced.TileThermalGenerator;
import techreborn.tiles.generator.basic.TileSolidFuelGenerator;
import techreborn.tiles.machine.iron.TileIronAlloyFurnace;
import techreborn.tiles.machine.iron.TileIronFurnace;
import techreborn.tiles.machine.multiblock.*;
import techreborn.tiles.machine.tier1.*;
import techreborn.tiles.storage.TileAdjustableSU;
import techreborn.tiles.storage.TileHighVoltageSU;
import techreborn.tiles.storage.TileLowVoltageSU;
import techreborn.tiles.storage.TileMediumVoltageSU;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Consumer;
import techreborn.client.gui.*;
import techreborn.tiles.storage.idsu.TileInterdimensionalSU;
import techreborn.tiles.storage.lesu.TileLapotronicSU;

public enum EGui implements IMachineGuiHandler {

	AESU(true),
	ALLOY_FURNACE(true),
	ALLOY_SMELTER(true),
	ASSEMBLING_MACHINE(true),
	AUTO_CRAFTING_TABLE(true),
	BLAST_FURNACE(true),
	CENTRIFUGE(true),
	CHARGEBENCH(true),
	CHEMICAL_REACTOR(true),
	CHUNK_LOADER(true),
	COMPRESSOR(true),
	DESTRUCTOPACK(false),
	DIESEL_GENERATOR(true),
	DIGITAL_CHEST(true),
	DISTILLATION_TOWER(true),
	ELECTRIC_FURNACE(true),
	EXTRACTOR(true),
	FUSION_CONTROLLER(true),
	GAS_TURBINE(true),
	GENERATOR(true),
	GRINDER(true),
	HIGH_VOLTAGE_SU(true),
	IDSU(true),
	IMPLOSION_COMPRESSOR(true),
	INDUSTRIAL_ELECTROLYZER(true),
	INDUSTRIAL_GRINDER(true),
	IRON_FURNACE(true),
	LESU(true),
	LOW_VOLTAGE_SU(true),
	MANUAL(false),
	MATTER_FABRICATOR(true),
	MEDIUM_VOLTAGE_SU(true),
	PLASMA_GENERATOR(true),
	QUANTUM_CHEST(true),
	QUANTUM_TANK(true),
	RECYCLER(true),
	ROLLING_MACHINE(true),
	SAWMILL(true),
	SCRAPBOXINATOR(true),
	SEMIFLUID_GENERATOR(true),
	THERMAL_GENERATOR(true),
	VACUUM_FREEZER(true),
	FLUID_REPLICATOR(true);

	private final boolean containerBuilder;

	private EGui(final boolean containerBuilder) {
		this.containerBuilder = containerBuilder;
	}

	static {
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, ()-> EGui::getClientGuiElement);
	}

	public static GuiContainer getClientGuiElement(FMLPlayMessages.OpenContainer msg) {
		EGui gui = byID(msg.getId());
		EntityPlayer player = Minecraft.getInstance().player;
		BlockPos pos = msg.getAdditionalData().readBlockPos();
		TileEntity tile = player.world.getTileEntity(pos);

		switch (gui) {
			case AESU:
				return new GuiAESU(player, (TileAdjustableSU) tile);
			case ALLOY_FURNACE:
				return new GuiAlloyFurnace(player, (TileIronAlloyFurnace) tile);
			case ALLOY_SMELTER:
				return new GuiAlloySmelter(player, (TileAlloySmelter) tile);
			case ASSEMBLING_MACHINE:
				return new GuiAssemblingMachine(player, (TileAssemblingMachine) tile);
			case LOW_VOLTAGE_SU:
				return new GuiBatbox(player, (TileLowVoltageSU) tile);
			case BLAST_FURNACE:
				return new GuiBlastFurnace(player, (TileIndustrialBlastFurnace) tile);
			case CENTRIFUGE:
				return new GuiCentrifuge(player, (TileIndustrialCentrifuge) tile);
			case CHARGEBENCH:
				return new GuiChargeBench(player, (TileChargeOMat) tile);
			case CHEMICAL_REACTOR:
				return new GuiChemicalReactor(player, (TileChemicalReactor) tile);
			case CHUNK_LOADER:
				return new GuiChunkLoader(player, (TileChunkLoader) tile);
			case COMPRESSOR:
				return new GuiCompressor(player, (TileCompressor) tile);
			case DESTRUCTOPACK:
				return new GuiDestructoPack(new ContainerDestructoPack(player));
			case DIESEL_GENERATOR:
				return new GuiDieselGenerator(player, (TileDieselGenerator) tile);
			case DIGITAL_CHEST:
				return new GuiDigitalChest(player, (TileDigitalChest) tile);
			case ELECTRIC_FURNACE:
				return new GuiElectricFurnace(player, (TileElectricFurnace) tile);
			case EXTRACTOR:
				return new GuiExtractor(player, (TileExtractor) tile);
			case FUSION_CONTROLLER:
				return new GuiFusionReactor(player, (TileFusionControlComputer) tile);
			case GAS_TURBINE:
				return new GuiGasTurbine(player, (TileGasTurbine) tile);
			case GENERATOR:
				return new GuiGenerator(player, (TileSolidFuelGenerator) tile);
			case GRINDER:
				return new GuiGrinder(player, (TileGrinder) tile);
			case IDSU:
				return new GuiIDSU(player, (TileInterdimensionalSU) tile);
			case IMPLOSION_COMPRESSOR:
				return new GuiImplosionCompressor(player, (TileImplosionCompressor) tile);
			case INDUSTRIAL_ELECTROLYZER:
				return new GuiIndustrialElectrolyzer(player, (TileIndustrialElectrolyzer) tile);
			case INDUSTRIAL_GRINDER:
				return new GuiIndustrialGrinder(player, (TileIndustrialGrinder) tile);
			case IRON_FURNACE:
				return new GuiIronFurnace(player, (TileIronFurnace) tile);
			case LESU:
				return new GuiLESU(player, (TileLapotronicSU) tile);
			case MATTER_FABRICATOR:
				return new GuiMatterFabricator(player, (TileMatterFabricator) tile);
			case MEDIUM_VOLTAGE_SU:
				return new GuiMFE(player, (TileMediumVoltageSU) tile);
			case HIGH_VOLTAGE_SU:
				return new GuiMFSU(player, (TileHighVoltageSU) tile);
			case QUANTUM_CHEST:
				return new GuiQuantumChest(player, (TileQuantumChest) tile);
			case QUANTUM_TANK:
				return new GuiQuantumTank(player, (TileQuantumTank) tile);
			case RECYCLER:
				return new GuiRecycler(player, (TileRecycler) tile);
			case ROLLING_MACHINE:
				return new GuiRollingMachine(player, (TileRollingMachine) tile);
			case SAWMILL:
				return new GuiIndustrialSawmill(player, (TileIndustrialSawmill) tile);
			case SCRAPBOXINATOR:
				return new GuiScrapboxinator(player, (TileScrapboxinator) tile);
			case SEMIFLUID_GENERATOR:
				return new GuiSemifluidGenerator(player, (TileSemiFluidGenerator) tile);
			case THERMAL_GENERATOR:
				return new GuiThermalGenerator(player, (TileThermalGenerator) tile);
			case VACUUM_FREEZER:
				return new GuiVacuumFreezer(player, (TileVacuumFreezer) tile);
			case AUTO_CRAFTING_TABLE:
				return new GuiAutoCrafting(player, (TileAutoCraftingTable) tile);
			case PLASMA_GENERATOR:
				return new GuiPlasmaGenerator(player, (TilePlasmaGenerator) tile);
			case DISTILLATION_TOWER:
				return new GuiDistillationTower(player, (TileDistillationTower) tile);
			case FLUID_REPLICATOR:
				return new GuiFluidReplicator(player, (TileFluidReplicator) tile);
			default:
				throw new RuntimeException("No gui found for " + msg.getId());

		}
	}

	public static EGui byID(ResourceLocation resourceLocation){
		return Arrays.stream(values())
			.filter(eGui -> eGui.name().toLowerCase().equals(resourceLocation.getPath()))
			.findFirst()
			.orElseThrow(() -> new RuntimeException("Failed to find gui for " + resourceLocation));
	}

	@Override
	public void open(EntityPlayer player, BlockPos pos, World world) {
		if(!world.isRemote){
			EGui gui = this;
			NetworkHooks.openGui((EntityPlayerMP) player, new IInteractionObject() {
				@Override
				public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
					TileEntity tileEntity = playerIn.world.getTileEntity(pos);
					if (tileEntity instanceof IContainerProvider) {
						return ((IContainerProvider) tileEntity).createContainer(player);
					}
					return null;
				}

				@Override
				public String getGuiID() {
					return "techreborn:" + gui.name().toLowerCase();
				}

				@Override
				public ITextComponent getName() {
					return new TextComponentString(getGuiID());
				}

				@Override
				public boolean hasCustomName() {
					return false;
				}

				@Nullable
				@Override
				public ITextComponent getCustomName() {
					return null;
				}
			}, packetBuffer -> packetBuffer.writeBlockPos(pos));
		}
	}

	public boolean useContainerBuilder() {
		return this.containerBuilder;
	}
}
