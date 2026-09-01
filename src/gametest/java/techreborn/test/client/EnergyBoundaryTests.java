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
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import techreborn.blockentity.storage.energy.EnergyStorageBlockEntity;
import techreborn.init.TRContent;

final class EnergyBoundaryTests {
	private static final BlockPos STORAGE_POS = ClientTestHarness.INTERACTION_POS;

	private EnergyBoundaryTests() {
	}

	static void run(ClientTestHarness test) {
		testStoredEnergyClampsToValidRange(test);
		testStorageOutputsOnlyThroughFacingSide(test);
		testRedstoneCanGatePowerOutput(test);
	}

	private static void testStoredEnergyClampsToValidRange(ClientTestHarness test) {
		test.clearTestArea();
		test.setBlock(STORAGE_POS, TRContent.Machine.LOW_VOLTAGE_SU.block);
		test.onServer(server -> storage(server).setEnergy(Long.MAX_VALUE));
		test.assertServer(server -> storage(server).getEnergy() == storage(server).getMaxStoredPower(),
			"Energy storage accepted more than its maximum capacity");
		test.onServer(server -> storage(server).setEnergy(-1));
		test.assertServer(server -> storage(server).getEnergy() == 0,
			"Energy storage accepted a negative energy value");
		test.screenshot(STORAGE_POS, "energy-storage-capacity-clamped");
	}

	private static void testStorageOutputsOnlyThroughFacingSide(ClientTestHarness test) {
		test.clearTestArea();
		BlockPos frontReceiver = STORAGE_POS.east();
		BlockPos backReceiver = STORAGE_POS.west();
		test.setBlock(STORAGE_POS, TRContent.Machine.LOW_VOLTAGE_SU.block);
		test.setBlock(frontReceiver, TRContent.Machine.ELECTRIC_FURNACE.block);
		test.setBlock(backReceiver, TRContent.Machine.GRINDER.block);
		test.onServer(server -> {
			EnergyStorageBlockEntity storage = storage(server);
			storage.setFacing(Direction.EAST);
			storage.setEnergy(5_000);
		});
		test.waitForServer(server -> energy(server, frontReceiver) > 0, 20,
			"Energy storage did not output through its facing side");
		test.assertServer(server -> energy(server, backReceiver) == 0,
			"Energy storage output through its non-facing side");
		test.screenshot(STORAGE_POS, "energy-storage-directional-output");
	}

	private static void testRedstoneCanGatePowerOutput(ClientTestHarness test) {
		test.clearTestArea();
		BlockPos receiverPos = STORAGE_POS.east();
		test.setBlock(STORAGE_POS, TRContent.Machine.LOW_VOLTAGE_SU.block);
		test.setBlock(receiverPos, TRContent.Machine.ELECTRIC_FURNACE.block);
		test.onServer(server -> {
			EnergyStorageBlockEntity storage = storage(server);
			storage.setFacing(Direction.EAST);
			storage.setEnergy(5_000);
			storage.setRedstoneConfiguration(storage.getRedstoneConfiguration().withState(
				RedstoneConfiguration.Element.POWER_IO, RedstoneConfiguration.State.ENABLED_ON));
		});
		test.waitTicks(10);
		test.assertServer(server -> energy(server, receiverPos) == 0,
			"Redstone-gated storage emitted energy without a signal");
		test.screenshot(STORAGE_POS, "energy-redstone-output-gated-off");
		test.setBlock(STORAGE_POS.north(), Blocks.REDSTONE_BLOCK);
		test.waitForServer(server -> energy(server, receiverPos) > 0, 20,
			"Redstone-gated storage did not emit energy when signalled");
		test.screenshot(STORAGE_POS, "energy-redstone-output-gated-on");
	}

	private static EnergyStorageBlockEntity storage(net.minecraft.server.MinecraftServer server) {
		return ClientTestHarness.requireBlockEntity(server, STORAGE_POS, EnergyStorageBlockEntity.class);
	}

	private static long energy(net.minecraft.server.MinecraftServer server, BlockPos pos) {
		return ClientTestHarness.requireBlockEntity(server, pos, PowerAcceptorBlockEntity.class).getEnergy();
	}
}
