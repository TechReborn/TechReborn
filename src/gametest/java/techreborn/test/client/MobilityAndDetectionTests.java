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
import techreborn.blockentity.machine.tier1.ElevatorBlockEntity;
import techreborn.blockentity.machine.tier1.PlayerDetectorBlockEntity;
import techreborn.blockentity.machine.tier2.LaunchpadBlockEntity;
import techreborn.init.TRContent;

final class MobilityAndDetectionTests {
	private MobilityAndDetectionTests() {
	}

	static void run(ClientTestHarness test) {
		testLaunchpadMovesPlayer(test);
		testElevatorUsesJumpAndSneakInput(test);
		testPlayerDetectorTracksRange(test);
	}

	private static void testLaunchpadMovesPlayer(ClientTestHarness test) {
		test.clearTestArea();
		BlockPos launchpadPos = ClientTestHarness.INTERACTION_POS;
		test.setBlock(launchpadPos, TRContent.Machine.LAUNCHPAD.block);
		test.setBlock(launchpadPos.east(), TRContent.SolarPanels.CREATIVE.block);
		test.onServer(server -> ClientTestHarness.requireBlockEntity(server, launchpadPos,
			LaunchpadBlockEntity.class).setSelection(LaunchpadBlockEntity.MAX_SELECTION));
		test.waitForServer(server -> {
			LaunchpadBlockEntity launchpad = ClientTestHarness.requireBlockEntity(server, launchpadPos,
				LaunchpadBlockEntity.class);
			return launchpad.getEnergy() == launchpad.getMaxStoredPower();
		}, 20, "Creative solar panel did not fill the launchpad");
		test.setBlock(launchpadPos.east(), net.minecraft.world.level.block.Blocks.AIR);
		test.movePlayer(launchpadPos.getX() + 0.5, launchpadPos.getY() + 1, launchpadPos.getZ() + 0.5);
		test.screenshot(launchpadPos, "movement-launchpad-player-ready");
		test.waitForServer(server -> {
			LaunchpadBlockEntity launchpad = ClientTestHarness.requireBlockEntity(server, launchpadPos,
				LaunchpadBlockEntity.class);
			return launchpad.getEnergy() == launchpad.getMaxStoredPower() - launchpad.selectedEnergyCost();
		}, 110, "Powered launchpad did not activate for the player");
		test.assertServer(server -> {
			var player = server.getPlayerList().getPlayers().getFirst();
			return player.getY() > launchpadPos.getY() + 1 || player.getDeltaMovement().y > 0;
		}, "Launchpad consumed energy without applying upward player movement");
		test.screenshot(launchpadPos, "movement-launchpad-player-airborne");
		restorePlayer(test);
	}

	private static void testElevatorUsesJumpAndSneakInput(ClientTestHarness test) {
		test.clearTestArea();
		BlockPos lowerPos = ClientTestHarness.INTERACTION_POS;
		BlockPos upperPos = lowerPos.above(6);
		test.setBlock(lowerPos, TRContent.Machine.ELEVATOR.block);
		test.setBlock(upperPos, TRContent.Machine.ELEVATOR.block);
		test.setBlock(lowerPos.east(), TRContent.SolarPanels.CREATIVE.block);
		test.setBlock(upperPos.east(), TRContent.SolarPanels.CREATIVE.block);
		test.waitForServer(server -> ClientTestHarness.requireBlockEntity(server, lowerPos,
			ElevatorBlockEntity.class).getEnergy() >= 12
			&& ClientTestHarness.requireBlockEntity(server, upperPos, ElevatorBlockEntity.class).getEnergy() >= 12,
			20, "Creative solar panels did not power both elevators");
		test.movePlayer(lowerPos.getX() + 0.5, lowerPos.getY() + 1, lowerPos.getZ() + 0.5);
		test.screenshot(lowerPos, "movement-elevator-lower-ready");
		test.jumpWithInput();
		test.waitForServer(server -> server.getPlayerList().getPlayers().getFirst().getY() > upperPos.getY(),
			20, "Jump input did not move the player to the upper elevator");
		test.screenshot(upperPos, "movement-elevator-arrived-upper");
		test.sneakWithInput(5);
		test.waitForServer(server -> server.getPlayerList().getPlayers().getFirst().getY() < upperPos.getY() - 2,
			20, "Sneak input did not move the player to the lower elevator");
		test.screenshot(lowerPos, "movement-elevator-returned-lower");
		restorePlayer(test);
	}

	private static void testPlayerDetectorTracksRange(ClientTestHarness test) {
		test.clearTestArea();
		BlockPos detectorPos = ClientTestHarness.INTERACTION_POS;
		test.setBlock(detectorPos, TRContent.Machine.PLAYER_DETECTOR.block);
		test.setBlock(detectorPos.east(), TRContent.SolarPanels.CREATIVE.block);
		test.movePlayer(detectorPos.getX() + 2.5, detectorPos.getY(), detectorPos.getZ() + 0.5);
		test.waitForServer(server -> ClientTestHarness.requireBlockEntity(server, detectorPos,
			PlayerDetectorBlockEntity.class).isProvidingPower(), 45,
			"Powered player detector did not emit redstone for a nearby player");
		test.screenshot(detectorPos, "movement-player-detector-in-range");
		test.movePlayer(30.5, detectorPos.getY(), 0.5);
		test.waitForServer(server -> !ClientTestHarness.requireBlockEntity(server, detectorPos,
			PlayerDetectorBlockEntity.class).isProvidingPower(), 45,
			"Player detector continued emitting redstone after the player left its radius");
		test.screenshot(detectorPos, "movement-player-detector-out-of-range");
		restorePlayer(test);
	}

	private static void restorePlayer(ClientTestHarness test) {
		test.movePlayer(ClientTestHarness.PLAYER_POS.getX() + 0.5, ClientTestHarness.TEST_Y,
			ClientTestHarness.PLAYER_POS.getZ() + 0.5);
	}
}
