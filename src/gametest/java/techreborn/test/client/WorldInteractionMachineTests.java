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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import techreborn.blockentity.machine.tier0.block.BlockBreakerBlockEntity;
import techreborn.blockentity.machine.tier0.block.BlockPlacerBlockEntity;
import techreborn.init.TRContent;

final class WorldInteractionMachineTests {
	private static final BlockPos MACHINE_POS = ClientTestHarness.INTERACTION_POS;

	private WorldInteractionMachineTests() {
	}

	static void run(ClientTestHarness test) {
		testBlockPlacerConsumesInput(test);
		testBlockBreakerCapturesDrops(test);
		testBlockPlacerDoesNotReplaceOccupiedSpace(test);
	}

	private static void testBlockPlacerConsumesInput(ClientTestHarness test) {
		placePoweredMachine(test, TRContent.Machine.BLOCK_PLACER.block);
		BlockPos targetPos = MACHINE_POS.north();
		test.onServer(server -> {
			BlockPlacerBlockEntity placer = ClientTestHarness.requireBlockEntity(server, MACHINE_POS,
				BlockPlacerBlockEntity.class);
			placer.inventory.setItem(BlockPlacerBlockEntity.INPUT_SLOT, new ItemStack(Blocks.GLASS, 2));
			addOverclockers(placer);
			placer.setFacing(Direction.NORTH);
			placer.setChanged();
		});
		test.screenshot(targetPos, "world-block-placer-before-processing");
		test.waitForServer(server -> ClientTestHarness.level(server).getBlockState(targetPos).is(Blocks.GLASS),
			80, "Overclocked block placer did not place its block");
		test.assertServer(server -> ClientTestHarness.requireBlockEntity(server, MACHINE_POS,
			BlockPlacerBlockEntity.class).inventory.getItem(BlockPlacerBlockEntity.INPUT_SLOT).getCount() == 1,
			"Block placer did not consume exactly one input block");
		test.openUi(MACHINE_POS, BlockPlacerBlockEntity.class, "world-block-placer-after-processing");
	}

	private static void testBlockBreakerCapturesDrops(ClientTestHarness test) {
		placePoweredMachine(test, TRContent.Machine.BLOCK_BREAKER.block);
		BlockPos targetPos = MACHINE_POS.north();
		test.setBlock(targetPos, Blocks.GOLD_BLOCK);
		test.onServer(server -> {
			BlockBreakerBlockEntity breaker = ClientTestHarness.requireBlockEntity(server, MACHINE_POS,
				BlockBreakerBlockEntity.class);
			addOverclockers(breaker);
			breaker.setFacing(Direction.NORTH);
			breaker.setChanged();
		});
		test.screenshot(targetPos, "world-block-breaker-before-processing");
		test.waitForServer(server -> ClientTestHarness.level(server).getBlockState(targetPos).isAir(),
			100, "Overclocked block breaker did not break the target block");
		test.assertServer(server -> ClientTestHarness.requireBlockEntity(server, MACHINE_POS,
			BlockBreakerBlockEntity.class).inventory.getItem(BlockBreakerBlockEntity.OUTPUT_SLOT).is(Blocks.GOLD_BLOCK.asItem()),
			"Block breaker did not capture the target block drop");
		test.openUi(MACHINE_POS, BlockBreakerBlockEntity.class, "world-block-breaker-drop-captured");
	}

	private static void testBlockPlacerDoesNotReplaceOccupiedSpace(ClientTestHarness test) {
		placePoweredMachine(test, TRContent.Machine.BLOCK_PLACER.block);
		BlockPos targetPos = MACHINE_POS.north();
		test.setBlock(targetPos, Blocks.OBSIDIAN);
		test.onServer(server -> {
			BlockPlacerBlockEntity placer = ClientTestHarness.requireBlockEntity(server, MACHINE_POS,
				BlockPlacerBlockEntity.class);
			placer.inventory.setItem(BlockPlacerBlockEntity.INPUT_SLOT, new ItemStack(Blocks.GLASS));
			addOverclockers(placer);
			placer.setFacing(Direction.NORTH);
			placer.setChanged();
		});
		test.waitTicks(50);
		test.assertServer(server -> ClientTestHarness.level(server).getBlockState(targetPos).is(Blocks.OBSIDIAN)
			&& ClientTestHarness.requireBlockEntity(server, MACHINE_POS, BlockPlacerBlockEntity.class)
				.inventory.getItem(BlockPlacerBlockEntity.INPUT_SLOT).is(Blocks.GLASS.asItem()),
			"Block placer replaced an occupied target or consumed its input");
		test.openUi(MACHINE_POS, BlockPlacerBlockEntity.class, "world-block-placer-blocked-target");
	}

	private static void placePoweredMachine(ClientTestHarness test, net.minecraft.world.level.block.Block block) {
		test.clearTestArea();
		test.setBlock(MACHINE_POS, block);
		test.setBlock(MACHINE_POS.east(), TRContent.SolarPanels.CREATIVE.block);
		test.waitTicks(3);
	}

	private static void addOverclockers(techreborn.blockentity.machine.GenericMachineBlockEntity machine) {
		for (int slot = 0; slot < machine.getUpgradeInventory().getContainerSize(); slot++) {
			machine.getUpgradeInventory().setItem(slot, new ItemStack(TRContent.Upgrades.OVERCLOCKER));
		}
	}
}
