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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import reborncore.common.blockentity.SlotConfiguration;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.init.TRContent;

final class AutomationAndMovementTests {
	private static final BlockPos MACHINE_POS = ClientTestHarness.INTERACTION_POS;

	private AutomationAndMovementTests() {
	}

	static void run(ClientTestHarness test) {
		testHopperInputAndOutput(test);
		testMovementAndItemPickup(test);
	}

	private static void testHopperInputAndOutput(ClientTestHarness test) {
		test.clearTestArea();
		BlockPos inputHopperPos = MACHINE_POS.above();
		BlockPos outputHopperPos = MACHINE_POS.below();
		BlockPos chestPos = outputHopperPos.below();
		test.setBlock(MACHINE_POS, TRContent.Machine.GRINDER.block);
		test.setBlock(MACHINE_POS.east(), TRContent.SolarPanels.CREATIVE.block);
		test.setBlock(inputHopperPos, Blocks.HOPPER);
		test.setBlock(outputHopperPos, Blocks.HOPPER);
		test.setBlock(chestPos, Blocks.CHEST);
		test.waitTicks(2);
		test.onServer(server -> {
			GenericMachineBlockEntity grinder = ClientTestHarness.requireBlockEntity(server, MACHINE_POS,
				GenericMachineBlockEntity.class);
			configureSlot(grinder, 0, Direction.UP, SlotConfiguration.ExtractConfig.INPUT);
			configureSlot(grinder, 1, Direction.DOWN, SlotConfiguration.ExtractConfig.OUTPUT);
			for (int slot = 0; slot < grinder.getUpgradeInventory().getContainerSize(); slot++) {
				grinder.getUpgradeInventory().setItem(slot, new ItemStack(TRContent.Upgrades.OVERCLOCKER));
			}
			ClientTestHarness.requireBlockEntity(server, inputHopperPos, HopperBlockEntity.class)
				.setItem(0, new ItemStack(Items.COAL));
			grinder.setChanged();
		});
		test.waitForServer(server -> {
			ChestBlockEntity chest = ClientTestHarness.requireBlockEntity(server, chestPos, ChestBlockEntity.class);
			return chest.getItem(0).is(TRContent.Dusts.COAL.asItem());
		}, 160, "Hoppers did not insert, process, and extract the grinder recipe");
		test.screenshot(MACHINE_POS, "automation-hopper-machine-round-trip");
	}

	private static void testMovementAndItemPickup(ClientTestHarness test) {
		test.clearTestArea();
		int pathZ = 5;
		for (int x = -5; x <= 5; x++) {
			test.setBlock(new BlockPos(x, ClientTestHarness.TEST_Y - 1, pathZ),
				TRContent.MachineBlocks.values()[Math.floorMod(x, TRContent.MachineBlocks.values().length)].getCasing());
		}
		BlockPos obstaclePos = new BlockPos(2, ClientTestHarness.TEST_Y, pathZ);
		test.setBlock(obstaclePos, TRContent.Machine.RECYCLER.block);
		test.movePlayer(-4.5, ClientTestHarness.TEST_Y, pathZ + 0.5);
		test.screenshot(obstaclePos, "movement-machine-casing-course");
		test.onServer(server -> ClientTestHarness.level(server).addFreshEntity(new ItemEntity(
			ClientTestHarness.level(server), 0.5, ClientTestHarness.TEST_Y + 0.25, pathZ + 0.5,
			new ItemStack(TRContent.Upgrades.ENERGY_STORAGE), 0, 0, 0
		)));
		test.lookAt(new BlockPos(5, ClientTestHarness.TEST_Y, pathZ));
		test.sprintJumpForward(60);
		test.assertServer(server -> {
			var player = server.getPlayerList().getPlayers().getFirst();
			return player.getX() > 2.5 && player.getInventory().contains(new ItemStack(TRContent.Upgrades.ENERGY_STORAGE));
		}, "Real sprint/jump input did not cross the machine course and collect the upgrade item");
		test.screenshot(new BlockPos(0, ClientTestHarness.TEST_Y, pathZ), "movement-completed-with-item-pickup");
		test.movePlayer(ClientTestHarness.PLAYER_POS.getX() + 0.5, ClientTestHarness.TEST_Y,
			ClientTestHarness.PLAYER_POS.getZ() + 0.5);
	}

	private static void configureSlot(GenericMachineBlockEntity machine, int slot, Direction side,
			SlotConfiguration.ExtractConfig mode) {
		SlotConfiguration.SlotConfigHolder holder = machine.getSlotConfiguration().getSlotDetails(slot);
		holder.setFilter(true);
		holder.updateSlotConfig(new SlotConfiguration.SlotConfig(side, new SlotConfiguration.SlotIO(mode), slot));
	}
}
