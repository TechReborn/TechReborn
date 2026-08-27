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
import net.minecraft.world.level.block.Blocks;
import reborncore.common.util.Torus;
import techreborn.blockentity.machine.multiblock.FusionControlComputerBlockEntity;
import techreborn.blockentity.machine.multiblock.IndustrialBlastFurnaceBlockEntity;
import techreborn.init.TRContent;

final class MultiblockMachineTests {
	private static final BlockPos CONTROLLER_POS = ClientTestHarness.INTERACTION_POS;

	private MultiblockMachineTests() {
	}

	static void run(ClientTestHarness test) {
		testBlastFurnaceProcessing(test);
		testBlastFurnaceInsufficientHeat(test);
		testBlastFurnaceMissingCasing(test);
		testFusionReactorFormation(test);
		testFusionReactorIgnition(test);
		testFusionReactorMissingCoil(test);
	}

	private static void testBlastFurnaceProcessing(ClientTestHarness test) {
		placeBlastFurnace(test);
		test.waitForServer(server -> {
			IndustrialBlastFurnaceBlockEntity furnace = blastFurnace(server);
			furnace.rematch();
			return furnace.isShapeValid() && furnace.getHeat() >= 1500;
		}, 80, "Blast furnace casing did not assemble with enough heat");
		test.movePlayer(CONTROLLER_POS.getX() - 4.5, ClientTestHarness.TEST_Y + 3, CONTROLLER_POS.getZ() - 4.5);
		test.screenshot(CONTROLLER_POS.above(2), "multiblock-blast-furnace-assembled");
		resetPlayerPosition(test);

		test.onServer(server -> {
			IndustrialBlastFurnaceBlockEntity furnace = blastFurnace(server);
			furnace.inventory.setItem(0, TRContent.Dusts.GALENA.getStack(2));
			for (int slot = 0; slot < 3; slot++) {
				furnace.getUpgradeInventory().setItem(slot, new ItemStack(TRContent.Upgrades.OVERCLOCKER));
			}
			furnace.setChanged();
		});
		test.waitForServer(server -> {
			IndustrialBlastFurnaceBlockEntity furnace = blastFurnace(server);
			return furnace.inventory.getItem(2).is(TRContent.Ingots.LEAD.asItem())
				&& furnace.inventory.getItem(3).is(TRContent.Ingots.SILVER.asItem());
		}, 100, "Overclocked blast furnace did not separate galena into both outputs");
		test.openUi(CONTROLLER_POS, IndustrialBlastFurnaceBlockEntity.class, "multiblock-blast-furnace-processed");
	}

	private static void testBlastFurnaceInsufficientHeat(ClientTestHarness test) {
		placeBlastFurnace(test);
		test.onServer(server -> {
			IndustrialBlastFurnaceBlockEntity furnace = blastFurnace(server);
			BlockPos center = CONTROLLER_POS.relative(furnace.getFacing().getOpposite(), 2);
			ClientTestHarness.level(server).setBlockAndUpdate(center.above(), Blocks.AIR.defaultBlockState());
			ClientTestHarness.level(server).setBlockAndUpdate(center.above(2), Blocks.AIR.defaultBlockState());
			furnace.rematch();
			furnace.inventory.setItem(0, TRContent.Dusts.GALENA.getStack(2));
			for (int slot = 0; slot < 3; slot++) {
				furnace.getUpgradeInventory().setItem(slot, new ItemStack(TRContent.Upgrades.OVERCLOCKER));
			}
			furnace.setChanged();
		});
		test.waitForServer(server -> {
			IndustrialBlastFurnaceBlockEntity furnace = blastFurnace(server);
			return furnace.isShapeValid() && furnace.getHeat() == 1360;
		}, 80, "Blast furnace without lava did not assemble at its casing-only heat");
		test.waitTicks(30);
		test.assertServer(server -> {
			IndustrialBlastFurnaceBlockEntity furnace = blastFurnace(server);
			return furnace.inventory.getItem(0).getCount() == 2
				&& furnace.inventory.getItem(2).isEmpty()
				&& furnace.inventory.getItem(3).isEmpty();
		}, "Blast furnace processed a recipe above its available heat");
		test.openUi(CONTROLLER_POS, IndustrialBlastFurnaceBlockEntity.class, "odd-blast-furnace-insufficient-heat");
	}

	private static void testBlastFurnaceMissingCasing(ClientTestHarness test) {
		test.onServer(server -> {
			IndustrialBlastFurnaceBlockEntity furnace = blastFurnace(server);
			BlockPos topCenter = CONTROLLER_POS.relative(furnace.getFacing().getOpposite(), 2).above(3);
			ClientTestHarness.level(server).setBlockAndUpdate(topCenter, Blocks.AIR.defaultBlockState());
			furnace.rematch();
		});
		test.assertServer(server -> {
			IndustrialBlastFurnaceBlockEntity furnace = blastFurnace(server);
			return !furnace.isShapeValid() && furnace.getHeat() == 0;
		}, "Blast furnace remained valid after removing a required casing");
		test.movePlayer(CONTROLLER_POS.getX() - 4.5, ClientTestHarness.TEST_Y + 6, CONTROLLER_POS.getZ() - 4.5);
		test.screenshot(CONTROLLER_POS.above(3), "odd-blast-furnace-missing-casing");
		resetPlayerPosition(test);
		test.openUi(CONTROLLER_POS, IndustrialBlastFurnaceBlockEntity.class, "odd-blast-furnace-invalid-ui");
	}

	private static void testFusionReactorFormation(ClientTestHarness test) {
		test.clearTestArea();
		test.placeWithInput(CONTROLLER_POS, TRContent.Machine.FUSION_CONTROL_COMPUTER.block);
		test.useBlockWithItem(CONTROLLER_POS, new ItemStack(TRContent.Machine.FUSION_COIL));
		test.waitForServer(server -> {
			FusionControlComputerBlockEntity reactor = fusionReactor(server);
			reactor.rematch();
			return reactor.isShapeValid();
		}, 40, "Fusion reactor was not formed by using a fusion coil on its controller");
		test.movePlayer(CONTROLLER_POS.getX() + 0.5, ClientTestHarness.TEST_Y + 3, CONTROLLER_POS.getZ() - 8.5);
		test.screenshot(CONTROLLER_POS, "multiblock-fusion-reactor-formed-with-input");
		resetPlayerPosition(test);
		test.openUi(CONTROLLER_POS, FusionControlComputerBlockEntity.class, "multiblock-fusion-reactor-valid-ui");
	}

	private static void testFusionReactorIgnition(ClientTestHarness test) {
		BlockPos solarPos = CONTROLLER_POS.south();
		test.setBlock(solarPos, TRContent.SolarPanels.CREATIVE.block);
		test.onServer(server -> {
			FusionControlComputerBlockEntity reactor = fusionReactor(server);
			reactor.inventory.setItem(0, TRContent.Cells.TRITIUM.getStack());
			reactor.inventory.setItem(1, TRContent.Cells.DEUTERIUM.getStack());
			reactor.setChanged();
		});
		test.waitForServer(server -> {
			FusionControlComputerBlockEntity reactor = fusionReactor(server);
			return reactor.craftingTickTime > 0
				&& reactor.inventory.getItem(0).isEmpty()
				&& reactor.inventory.getItem(1).isEmpty();
		}, 80, "Creative-solar-powered fusion reactor did not charge and ignite");
		test.openUi(CONTROLLER_POS, FusionControlComputerBlockEntity.class, "multiblock-fusion-reactor-ignited");
	}

	private static void testFusionReactorMissingCoil(ClientTestHarness test) {
		BlockPos missingCoil = Torus.generate(CONTROLLER_POS, 6).getFirst();
		test.setBlock(missingCoil, Blocks.AIR);
		test.waitForServer(server -> {
			FusionControlComputerBlockEntity reactor = fusionReactor(server);
			reactor.rematch();
			return !reactor.isShapeValid() && reactor.craftingTickTime == 0;
		}, 30, "Fusion reactor continued processing with a missing coil");
		test.movePlayer(CONTROLLER_POS.getX() + 0.5, ClientTestHarness.TEST_Y + 3, CONTROLLER_POS.getZ() - 8.5);
		test.screenshot(missingCoil, "odd-fusion-reactor-missing-coil");
		resetPlayerPosition(test);
		test.openUi(CONTROLLER_POS, FusionControlComputerBlockEntity.class, "odd-fusion-reactor-invalid-ui");
	}

	private static void placeBlastFurnace(ClientTestHarness test) {
		test.clearTestArea();
		test.placeWithInput(CONTROLLER_POS, TRContent.Machine.INDUSTRIAL_BLAST_FURNACE.block);
		test.formMultiblock(CONTROLLER_POS, IndustrialBlastFurnaceBlockEntity.class);
		test.setBlock(CONTROLLER_POS.above(), TRContent.SolarPanels.CREATIVE.block);
	}

	private static IndustrialBlastFurnaceBlockEntity blastFurnace(net.minecraft.server.MinecraftServer server) {
		return ClientTestHarness.requireBlockEntity(server, CONTROLLER_POS, IndustrialBlastFurnaceBlockEntity.class);
	}

	private static FusionControlComputerBlockEntity fusionReactor(net.minecraft.server.MinecraftServer server) {
		return ClientTestHarness.requireBlockEntity(server, CONTROLLER_POS, FusionControlComputerBlockEntity.class);
	}

	private static void resetPlayerPosition(ClientTestHarness test) {
		test.movePlayer(ClientTestHarness.PLAYER_POS.getX() + 0.5, ClientTestHarness.TEST_Y,
			ClientTestHarness.PLAYER_POS.getZ() + 0.5);
	}
}
