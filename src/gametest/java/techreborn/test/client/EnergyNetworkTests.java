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
import net.minecraft.world.level.block.Blocks;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import techreborn.blockentity.generator.basic.SolidFuelGeneratorBlockEntity;
import techreborn.init.TRContent;

final class EnergyNetworkTests {
	private EnergyNetworkTests() {
	}

	static void run(ClientTestHarness test) {
		testCableChain(test);
		testCreativeSolarWithMultipleReceivers(test);
		testDisconnectedMachine(test);
	}

	private static void testCableChain(ClientTestHarness test) {
		BlockPos generatorPos = new BlockPos(-5, ClientTestHarness.TEST_Y, 3);
		BlockPos cablePos = generatorPos.east();
		BlockPos secondCablePos = cablePos.east();
		BlockPos storagePos = secondCablePos.east();
		placeFueledNetwork(test, generatorPos, storagePos, cablePos, secondCablePos);
		test.waitTicks(80);
		assertPowered(test, storagePos, "Generator did not transfer energy through a two-cable chain");
		test.screenshot(secondCablePos, "energy-two-cable-chain");
	}

	private static void testCreativeSolarWithMultipleReceivers(ClientTestHarness test) {
		BlockPos solarPos = new BlockPos(4, ClientTestHarness.TEST_Y, 3);
		BlockPos electricFurnacePos = solarPos.east();
		BlockPos grinderPos = solarPos.south();
		test.setBlock(solarPos, TRContent.SolarPanels.CREATIVE.block);
		test.setBlock(electricFurnacePos, TRContent.Machine.ELECTRIC_FURNACE.block);
		test.setBlock(grinderPos, TRContent.Machine.GRINDER.block);
		test.waitForServer(server -> {
			PowerAcceptorBlockEntity electricFurnace = ClientTestHarness.requireBlockEntity(server, electricFurnacePos, PowerAcceptorBlockEntity.class);
			PowerAcceptorBlockEntity grinder = ClientTestHarness.requireBlockEntity(server, grinderPos, PowerAcceptorBlockEntity.class);
			return electricFurnace.getEnergy() == electricFurnace.getMaxStoredPower()
				&& grinder.getEnergy() == grinder.getMaxStoredPower();
		}, 20, "Creative solar panel did not fill both adjacent machines");
		test.screenshot(solarPos, "energy-creative-solar-multiple-receivers");
	}

	private static void testDisconnectedMachine(ClientTestHarness test) {
		BlockPos generatorPos = new BlockPos(-4, ClientTestHarness.TEST_Y, 6);
		BlockPos gapPos = generatorPos.east();
		BlockPos storagePos = gapPos.east();
		placeFueledNetwork(test, generatorPos, storagePos);
		test.setBlock(gapPos, Blocks.AIR);
		test.waitTicks(80);
		test.assertServer(server -> ClientTestHarness.requireBlockEntity(server, storagePos, PowerAcceptorBlockEntity.class).getEnergy() == 0,
			"Disconnected energy storage unexpectedly received energy across an air gap");
		test.screenshot(gapPos, "odd-disconnected-energy-network");
	}

	private static void placeFueledNetwork(ClientTestHarness test, BlockPos generatorPos, BlockPos storagePos, BlockPos... cablePositions) {
		test.setBlock(generatorPos, TRContent.Machine.SOLID_FUEL_GENERATOR.block);
		for (BlockPos cablePos : cablePositions) {
			test.setBlock(cablePos, TRContent.Cables.TIN.block);
		}
		test.setBlock(storagePos, TRContent.Machine.LOW_VOLTAGE_SU.block);
		test.onServer(server -> {
			SolidFuelGeneratorBlockEntity generator = ClientTestHarness.requireBlockEntity(server, generatorPos, SolidFuelGeneratorBlockEntity.class);
			generator.inventory.setItem(generator.fuelSlot, new ItemStack(Items.COAL));
			generator.setChanged();
		});
	}

	private static void assertPowered(ClientTestHarness test, BlockPos pos, String message) {
		test.assertServer(server -> ClientTestHarness.requireBlockEntity(server, pos, PowerAcceptorBlockEntity.class).getEnergy() > 0, message);
	}
}
