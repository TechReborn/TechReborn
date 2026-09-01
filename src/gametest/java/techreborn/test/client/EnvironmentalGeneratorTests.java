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
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import techreborn.init.TRContent;

final class EnvironmentalGeneratorTests {
	private EnvironmentalGeneratorTests() {
	}

	static void run(ClientTestHarness test) {
		testWaterMillRequiresAdjacentWater(test);
		testWindMillRequiresAltitude(test);
		testDragonEggSyphonRequiresEgg(test);
	}

	private static void testWaterMillRequiresAdjacentWater(ClientTestHarness test) {
		test.clearTestArea();
		BlockPos millPos = ClientTestHarness.INTERACTION_POS;
		test.setBlock(millPos, TRContent.Machine.WATER_MILL.block);
		test.waitTicks(25);
		test.assertServer(server -> ClientTestHarness.requireBlockEntity(server, millPos,
			PowerAcceptorBlockEntity.class).getEnergy() == 0, "Dry water mill unexpectedly generated energy");
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			test.setBlock(millPos.relative(direction), Blocks.WATER);
		}
		test.waitForServer(server -> ClientTestHarness.requireBlockEntity(server, millPos,
			PowerAcceptorBlockEntity.class).getEnergy() > 0, 160,
			"Water mill did not generate with four adjacent water sources");
		test.assertServer(server -> ClientTestHarness.level(server).getBlockState(millPos)
			.getValue(BlockMachineBase.ACTIVE), "Generating water mill did not become active");
		test.screenshot(millPos, "generator-water-mill-four-sources");
		test.onServer(server -> {
			for (BlockPos pos : BlockPos.betweenClosed(millPos.offset(-3, 0, -3), millPos.offset(3, 0, 3))) {
				if (ClientTestHarness.level(server).getBlockState(pos).is(Blocks.WATER)) {
					ClientTestHarness.level(server).setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
				}
			}
		});
		test.waitForServer(server -> !ClientTestHarness.level(server).getBlockState(millPos)
			.getValue(BlockMachineBase.ACTIVE), 45, "Water mill stayed active after all water was removed");
		test.screenshot(millPos, "generator-water-mill-dry-stopped");
	}

	private static void testWindMillRequiresAltitude(ClientTestHarness test) {
		test.clearTestArea();
		BlockPos highPos = ClientTestHarness.INTERACTION_POS;
		BlockPos lowPos = new BlockPos(-4, 60, 0);
		test.setBlock(highPos, TRContent.Machine.WIND_MILL.block);
		test.setBlock(lowPos, TRContent.Machine.WIND_MILL.block);
		test.waitTicks(10);
		test.assertServer(server -> ClientTestHarness.requireBlockEntity(server, highPos,
			PowerAcceptorBlockEntity.class).getEnergy() > 0, "Wind mill above Y=64 did not generate energy");
		test.assertServer(server -> ClientTestHarness.requireBlockEntity(server, lowPos,
			PowerAcceptorBlockEntity.class).getEnergy() == 0, "Wind mill below Y=64 unexpectedly generated energy");
		test.screenshot(highPos, "generator-wind-mill-high-altitude");
		test.setBlock(lowPos, Blocks.AIR);
	}

	private static void testDragonEggSyphonRequiresEgg(ClientTestHarness test) {
		test.clearTestArea();
		BlockPos syphonPos = ClientTestHarness.INTERACTION_POS;
		test.setBlock(syphonPos, TRContent.Machine.DRAGON_EGG_SYPHON.block);
		test.waitTicks(5);
		test.assertServer(server -> ClientTestHarness.requireBlockEntity(server, syphonPos,
			PowerAcceptorBlockEntity.class).getEnergy() == 0, "Dragon egg syphon generated without an egg");
		test.setBlock(syphonPos.above(), Blocks.DRAGON_EGG);
		test.waitForServer(server -> ClientTestHarness.requireBlockEntity(server, syphonPos,
			PowerAcceptorBlockEntity.class).getEnergy() > 0, 20, "Dragon egg syphon did not generate beneath an egg");
		test.screenshot(syphonPos, "generator-dragon-egg-syphon-active");
		test.setBlock(syphonPos.above(), Blocks.AIR);
		test.waitForServer(server -> !ClientTestHarness.level(server).getBlockState(syphonPos)
			.getValue(BlockMachineBase.ACTIVE), 40, "Dragon egg syphon stayed active after its egg was removed");
		test.screenshot(syphonPos, "generator-dragon-egg-syphon-stopped");
	}
}
