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
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;
import techreborn.init.TRContent;

final class StorageUnitTests {
	private static final BlockPos STORAGE_POS = ClientTestHarness.INTERACTION_POS;

	private StorageUnitTests() {
	}

	static void run(ClientTestHarness test) {
		testRealBulkDepositAndSingleWithdrawal(test);
		testLockedEmptyUnitKeepsItemFilter(test);
		testUpgraderPreservesContents(test);
		testCreativeStorageFillsToCapacity(test);
	}

	private static void testRealBulkDepositAndSingleWithdrawal(ClientTestHarness test) {
		placeUnit(test, TRContent.StorageUnit.CRUDE);
		test.useBlockWithItem(STORAGE_POS, new ItemStack(Items.DIAMOND, 64));
		test.waitForServer(server -> unit(server).getCurrentCapacity() == 64, 20,
			"Real right-click did not bulk-deposit the held diamond stack");
		test.openUi(STORAGE_POS, StorageUnitBaseBlockEntity.class, "storage-real-bulk-deposit");

		test.setHotbarItem(ItemStack.EMPTY);
		test.runCommand("gamemode survival @a");
		test.attackBlockWithInput(STORAGE_POS);
		test.waitForServer(server -> unit(server).getCurrentCapacity() == 63, 20,
			"Real left-click did not withdraw one item from storage");
		test.assertServer(server -> server.getPlayerList().getPlayers().getFirst().getInventory()
			.contains(new ItemStack(Items.DIAMOND)), "Withdrawn storage item was not collected by the player");
		test.screenshot(STORAGE_POS, "storage-real-single-withdrawal");
		test.runCommand("gamemode creative @a");
		test.waitTicks(2);
	}

	private static void testLockedEmptyUnitKeepsItemFilter(ClientTestHarness test) {
		placeUnit(test, TRContent.StorageUnit.BASIC);
		test.onServer(server -> unit(server).processInput(new ItemStack(Items.IRON_INGOT, 8)));
		test.waitForServer(server -> unit(server).getInventory()
			.getItem(StorageUnitBaseBlockEntity.OUTPUT_SLOT).getCount() == 8, 10,
			"Storage unit did not expose its stored items for extraction");
		test.onServer(server -> {
			StorageUnitBaseBlockEntity unit = unit(server);
			unit.setLocked(true);
			unit.getInventory().setItem(StorageUnitBaseBlockEntity.OUTPUT_SLOT, ItemStack.EMPTY);
		});
		test.waitTicks(2);
		test.assertServer(server -> {
			StorageUnitBaseBlockEntity unit = unit(server);
			return unit.isLocked() && unit.isEmpty()
				&& unit.canPlaceItem(StorageUnitBaseBlockEntity.INPUT_SLOT, new ItemStack(Items.IRON_INGOT))
				&& !unit.canPlaceItem(StorageUnitBaseBlockEntity.INPUT_SLOT, new ItemStack(Items.GOLD_INGOT));
		}, "Locked empty storage unit did not retain its item filter");
		test.openUi(STORAGE_POS, StorageUnitBaseBlockEntity.class, "storage-locked-empty-filter-retained");
	}

	private static void testUpgraderPreservesContents(ClientTestHarness test) {
		placeUnit(test, TRContent.StorageUnit.CRUDE);
		test.onServer(server -> unit(server).processInput(new ItemStack(Items.EMERALD, 32)));
		test.screenshot(STORAGE_POS, "storage-crude-before-upgrade");
		test.setHotbarItem(new ItemStack(TRContent.StorageUnit.CRUDE.upgrader));
		test.assertServer(server -> server.getPlayerList().getPlayers().getFirst().getMainHandItem()
			.is(TRContent.StorageUnit.CRUDE.upgrader), "Crude storage upgrader was not synchronized to the held slot");
		test.runCommand("gamemode survival @a");
		test.useBlockWithHeldItem(STORAGE_POS);
		test.waitForServer(server -> ClientTestHarness.level(server).getBlockState(STORAGE_POS)
			.is(TRContent.StorageUnit.BASIC.block), 20, "Crude storage upgrader did not create a basic storage unit");
		test.assertServer(server -> unit(server).getCurrentCapacity() == 32
			&& unit(server).getStoredStack().is(Items.EMERALD), "Storage upgrade did not preserve contents");
		test.runCommand("gamemode creative @a");
		test.waitTicks(2);
		test.openUi(STORAGE_POS, StorageUnitBaseBlockEntity.class, "storage-basic-after-content-preserving-upgrade");
	}

	private static void testCreativeStorageFillsToCapacity(ClientTestHarness test) {
		placeUnit(test, TRContent.StorageUnit.CREATIVE);
		test.onServer(server -> {
			StorageUnitBaseBlockEntity unit = unit(server);
			unit.getInventory().setItem(StorageUnitBaseBlockEntity.INPUT_SLOT, new ItemStack(Items.REDSTONE));
			unit.setChanged();
		});
		test.waitForServer(server -> unit(server).isFull(), 20,
			"Creative storage unit did not expand its first item to full capacity");
		test.assertServer(server -> unit(server).getStoredStack().is(Items.REDSTONE),
			"Creative storage unit lost its configured item type");
		test.openUi(STORAGE_POS, StorageUnitBaseBlockEntity.class, "storage-creative-full-capacity");
	}

	private static void placeUnit(ClientTestHarness test, TRContent.StorageUnit type) {
		test.clearTestArea();
		test.setBlock(STORAGE_POS, type.block);
		test.waitTicks(2);
	}

	private static StorageUnitBaseBlockEntity unit(net.minecraft.server.MinecraftServer server) {
		return ClientTestHarness.requireBlockEntity(server, STORAGE_POS, StorageUnitBaseBlockEntity.class);
	}
}
