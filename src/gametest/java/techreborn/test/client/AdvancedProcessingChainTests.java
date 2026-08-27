/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2026 TechReborn
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

package techreborn.test.client;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.blockentity.machine.multiblock.IndustrialSawmillBlockEntity;
import techreborn.blockentity.machine.multiblock.VacuumFreezerBlockEntity;
import techreborn.blockentity.machine.tier1.ChemicalReactorBlockEntity;
import techreborn.blockentity.machine.tier1.IndustrialElectrolyzerBlockEntity;
import techreborn.blockentity.machine.tier1.SolidCanningMachineBlockEntity;
import techreborn.init.TRContent;

final class AdvancedProcessingChainTests {
	private static final BlockPos MACHINE_POS = ClientTestHarness.INTERACTION_POS;

	private AdvancedProcessingChainTests() {
	}

	static void run(ClientTestHarness test) {
		testElectrolysisToReactorCoolantChain(test);
		testVacuumFreezerPackedIceRecipe(test);
		testIndustrialSawmillFluidRecipe(test);
	}

	private static void testElectrolysisToReactorCoolantChain(ClientTestHarness test) {
		placePoweredMachine(test, TRContent.Machine.INDUSTRIAL_ELECTROLYZER.block);
		test.onServer(server -> {
			IndustrialElectrolyzerBlockEntity electrolyzer = ClientTestHarness.requireBlockEntity(server, MACHINE_POS,
				IndustrialElectrolyzerBlockEntity.class);
			electrolyzer.inventory.setItem(0, TRContent.Cells.WATER.getStack(5));
			installOverclockers(electrolyzer);
			electrolyzer.setChanged();
		});
		test.waitForServer(server -> machine(server).inventory.getItem(2).is(TRContent.Cells.ELECTROLYZED_WATER.asItem())
			&& machine(server).inventory.getItem(2).getCount() == 5, 220,
			"Industrial electrolyzer did not convert five water cells");
		test.openUi(MACHINE_POS, IndustrialElectrolyzerBlockEntity.class, "processing-chain-electrolyzed-water");

		test.onServer(server -> {
			GenericMachineBlockEntity electrolyzer = machine(server);
			ItemStack electrolyzedWater = electrolyzer.inventory.getItem(2).copy();
			for (int slot = 0; slot <= 5; slot++) {
				electrolyzer.inventory.setItem(slot, ItemStack.EMPTY);
			}
			electrolyzer.inventory.setItem(0, electrolyzedWater);
			electrolyzer.setChanged();
		});
		test.waitForServer(server -> machine(server).inventory.getItem(2).is(TRContent.Cells.HYDROGEN.asItem())
			&& machine(server).inventory.getItem(2).getCount() == 4
			&& machine(server).inventory.getItem(3).is(TRContent.Cells.COMPRESSED_AIR.asItem()), 220,
			"Industrial electrolyzer did not split electrolyzed water into hydrogen and compressed air");
		test.openUi(MACHINE_POS, IndustrialElectrolyzerBlockEntity.class, "processing-chain-hydrogen-compressed-air");
		test.onServer(server -> {
			GenericMachineBlockEntity electrolyzer = machine(server);
			server.getPlayerList().getPlayers().getFirst().getInventory().setItem(7,
				electrolyzer.inventory.getItem(2).copy());
			server.getPlayerList().getPlayers().getFirst().getInventory().setItem(8,
				electrolyzer.inventory.getItem(3).copy());
		});

		placePoweredMachine(test, TRContent.Machine.CHEMICAL_REACTOR.block);
		test.onServer(server -> {
			ChemicalReactorBlockEntity reactor = ClientTestHarness.requireBlockEntity(server, MACHINE_POS,
				ChemicalReactorBlockEntity.class);
			var inventory = server.getPlayerList().getPlayers().getFirst().getInventory();
			reactor.inventory.setItem(0, inventory.removeItemNoUpdate(8));
			reactor.inventory.setItem(1, inventory.removeItemNoUpdate(7));
			installOverclockers(reactor);
			reactor.setChanged();
		});
		test.waitForServer(server -> machine(server).inventory.getItem(2).is(TRContent.Cells.WATER.asItem())
			&& machine(server).inventory.getItem(2).getCount() == 2, 140,
			"Chemical reactor did not recombine chain products into water cells");
		test.openUi(MACHINE_POS, ChemicalReactorBlockEntity.class, "processing-chain-chemical-water");
		test.onServer(server -> server.getPlayerList().getPlayers().getFirst().getInventory().setItem(8,
			machine(server).inventory.removeItem(2, 1)));

		placePoweredMachine(test, TRContent.Machine.SOLID_CANNING_MACHINE.block);
		test.onServer(server -> {
			SolidCanningMachineBlockEntity canner = ClientTestHarness.requireBlockEntity(server, MACHINE_POS,
				SolidCanningMachineBlockEntity.class);
			canner.inventory.setItem(0, server.getPlayerList().getPlayers().getFirst().getInventory().removeItemNoUpdate(8));
			canner.inventory.setItem(1, TRContent.Ingots.TIN.getStack(2));
			installOverclockers(canner);
			canner.setChanged();
		});
		test.waitForServer(server -> machine(server).inventory.getItem(2)
			.is(TRContent.NuclearReactorComponents.WATER_COOLANT_CELL_10K.asItem())
			&& machine(server).inventory.getItem(2).getCount() == 2, 100,
			"Solid canning machine did not turn chained water into reactor coolant cells");
		test.openUi(MACHINE_POS, SolidCanningMachineBlockEntity.class, "processing-chain-reactor-coolant-cells");
	}

	private static void testVacuumFreezerPackedIceRecipe(ClientTestHarness test) {
		placePoweredMultiblock(test, TRContent.Machine.VACUUM_FREEZER.block, VacuumFreezerBlockEntity.class);
		test.onServer(server -> {
			VacuumFreezerBlockEntity freezer = ClientTestHarness.requireBlockEntity(server, MACHINE_POS,
				VacuumFreezerBlockEntity.class);
			freezer.inventory.setItem(0, new ItemStack(Items.PACKED_ICE, 4));
			installOverclockers(freezer);
			freezer.setChanged();
		});
		test.waitForServer(server -> machine(server).inventory.getItem(1).is(Items.BLUE_ICE), 100,
			"Vacuum freezer did not convert packed ice into blue ice");
		test.openUi(MACHINE_POS, VacuumFreezerBlockEntity.class, "multiblock-vacuum-freezer-blue-ice");
	}

	private static void testIndustrialSawmillFluidRecipe(ClientTestHarness test) {
		placePoweredMultiblock(test, TRContent.Machine.INDUSTRIAL_SAWMILL.block, IndustrialSawmillBlockEntity.class);
		test.onServer(server -> {
			IndustrialSawmillBlockEntity sawmill = ClientTestHarness.requireBlockEntity(server, MACHINE_POS,
				IndustrialSawmillBlockEntity.class);
			sawmill.tank.setFluidInstance(new FluidInstance(Fluids.WATER, FluidValue.BUCKET));
			sawmill.inventory.setItem(0, new ItemStack(Items.OAK_LOG));
			installOverclockers(sawmill);
			sawmill.setChanged();
		});
		test.waitForServer(server -> machine(server).inventory.getItem(2).is(Items.OAK_PLANKS)
			&& machine(server).inventory.getItem(2).getCount() == 4
			&& machine(server).inventory.getItem(3).is(TRContent.Dusts.SAW.asItem()), 120,
			"Industrial sawmill did not produce planks and sawdust using water");
		test.openUi(MACHINE_POS, IndustrialSawmillBlockEntity.class, "multiblock-industrial-sawmill-water-products");
	}

	private static void placePoweredMachine(ClientTestHarness test, net.minecraft.world.level.block.Block block) {
		test.clearTestArea();
		test.setBlock(MACHINE_POS, block);
		test.setBlock(MACHINE_POS.above(), TRContent.SolarPanels.CREATIVE.block);
		test.waitTicks(3);
	}

	private static void placePoweredMultiblock(ClientTestHarness test, net.minecraft.world.level.block.Block block,
			Class<? extends MachineBaseBlockEntity> type) {
		test.clearTestArea();
		test.setBlock(MACHINE_POS, block);
		test.formMultiblock(MACHINE_POS, type);
		test.setBlock(MACHINE_POS.above(), TRContent.SolarPanels.CREATIVE.block);
		test.waitForServer(server -> ClientTestHarness.requireBlockEntity(server, MACHINE_POS, type).isShapeValid(),
			30, type.getSimpleName() + " did not form");
		test.screenshot(MACHINE_POS.above(2), "multiblock-" + type.getSimpleName().replace("BlockEntity", "").toLowerCase());
	}

	private static void installOverclockers(MachineBaseBlockEntity machine) {
		for (int slot = 0; slot < machine.getUpgradeInventory().getContainerSize(); slot++) {
			machine.getUpgradeInventory().setItem(slot, new ItemStack(TRContent.Upgrades.OVERCLOCKER));
		}
	}

	private static GenericMachineBlockEntity machine(net.minecraft.server.MinecraftServer server) {
		return ClientTestHarness.requireBlockEntity(server, MACHINE_POS, GenericMachineBlockEntity.class);
	}
}
