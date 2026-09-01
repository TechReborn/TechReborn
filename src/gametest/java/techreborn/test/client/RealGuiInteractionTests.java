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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.blockentity.SlotConfiguration;
import reborncore.common.screen.slot.BaseSlot;
import reborncore.common.screen.slot.SlotOutput;
import reborncore.common.screen.slot.UpgradeSlot;
import techreborn.blockentity.machine.tier1.ElectricFurnaceBlockEntity;
import techreborn.init.TRContent;

final class RealGuiInteractionTests {
	private static final BlockPos MACHINE_POS = ClientTestHarness.INTERACTION_POS;

	private RealGuiInteractionTests() {
	}

	static void run(ClientTestHarness test) {
		testGuiInventoryAndUpgradeTransactions(test);
		testGuiRedstoneProcessingControl(test);
	}

	private static void testGuiInventoryAndUpgradeTransactions(ClientTestHarness test) {
		placePoweredFurnace(test);
		test.openUi(MACHINE_POS, ElectricFurnaceBlockEntity.class);
		test.setHotbarItem(new ItemStack(TRContent.Upgrades.OVERCLOCKER, 4));
		test.clickPlayerSlot(0);
		test.clickMachineSlot(UpgradeSlot.class, 0);
		test.clickMachineSlot(UpgradeSlot.class, 1);
		test.clickMachineSlot(UpgradeSlot.class, 2);
		test.clickMachineSlot(UpgradeSlot.class, 3);
		test.screenshotUi("real-gui-overclocker-insertion");
		test.closeUi();

		test.openUi(MACHINE_POS, ElectricFurnaceBlockEntity.class);
		test.setHotbarItem(new ItemStack(Items.RAW_GOLD));
		test.clickPlayerSlot(0);
		test.clickMachineSlot(BaseSlot.class, 0);
		test.screenshotUi("real-gui-machine-input");
		test.closeUi();

		test.waitForServer(server -> {
			ElectricFurnaceBlockEntity furnace = ClientTestHarness.requireBlockEntity(server, MACHINE_POS, ElectricFurnaceBlockEntity.class);
			return furnace.inventory.getItem(1).is(Items.GOLD_INGOT);
		}, 80, "GUI-fed overclocked furnace did not finish");

		test.openUi(MACHINE_POS, ElectricFurnaceBlockEntity.class);
		test.clickMachineSlot(SlotOutput.class, 1);
		test.clickPlayerSlot(1);
		test.screenshotUi("real-gui-machine-output-extraction");
		test.closeUi();
		test.assertServer(server -> {
			var player = server.getPlayerList().getPlayers().getFirst();
			ElectricFurnaceBlockEntity furnace = ClientTestHarness.requireBlockEntity(server, MACHINE_POS, ElectricFurnaceBlockEntity.class);
			return player.getInventory().getItem(1).is(Items.GOLD_INGOT)
				&& furnace.getUpgradeInventory().getItem(0).is(TRContent.Upgrades.OVERCLOCKER.asItem())
				&& furnace.getUpgradeInventory().getItem(1).is(TRContent.Upgrades.OVERCLOCKER.asItem())
				&& furnace.getUpgradeInventory().getItem(2).is(TRContent.Upgrades.OVERCLOCKER.asItem())
				&& furnace.getUpgradeInventory().getItem(3).is(TRContent.Upgrades.OVERCLOCKER.asItem());
		}, "Real GUI transactions did not persist their item movements");
	}

	private static void testGuiRedstoneProcessingControl(ClientTestHarness test) {
		placePoweredFurnace(test);
		test.openUi(MACHINE_POS, ElectricFurnaceBlockEntity.class);
		test.clickGui(-14, 118);
		test.clickGui(125, 105);
		test.screenshotUi("real-gui-redstone-item-input-disabled");
		test.closeUi();

		test.setBlock(MACHINE_POS.above(), Blocks.CHEST);
		test.onServer(server -> {
			ChestBlockEntity chest = ClientTestHarness.requireBlockEntity(server, MACHINE_POS.above(), ChestBlockEntity.class);
			ElectricFurnaceBlockEntity furnace = ClientTestHarness.requireBlockEntity(server, MACHINE_POS, ElectricFurnaceBlockEntity.class);
			SlotConfiguration.SlotConfigHolder holder = furnace.getSlotConfiguration().getSlotDetails(0);
			holder.setFilter(true);
			holder.setInput(true);
			holder.updateSlotConfig(new SlotConfiguration.SlotConfig(Direction.UP,
				new SlotConfiguration.SlotIO(SlotConfiguration.ExtractConfig.INPUT), 0));
			chest.setItem(0, new ItemStack(Items.RAW_IRON));
			chest.setChanged();
		});
		test.waitTicks(10);
		test.assertServer(server -> {
			ElectricFurnaceBlockEntity furnace = ClientTestHarness.requireBlockEntity(server, MACHINE_POS, ElectricFurnaceBlockEntity.class);
			ChestBlockEntity chest = ClientTestHarness.requireBlockEntity(server, MACHINE_POS.above(), ChestBlockEntity.class);
			return furnace.getRedstoneConfiguration().getState(RedstoneConfiguration.Element.ITEM_IO)
				== RedstoneConfiguration.State.ENABLED_ON
				&& furnace.inventory.getItem(0).isEmpty()
				&& chest.getItem(0).is(Items.RAW_IRON);
		}, "GUI redstone configuration did not stop unpowered item input");

		test.setBlock(MACHINE_POS.south(), Blocks.REDSTONE_BLOCK);
		test.waitForServer(server -> {
			ElectricFurnaceBlockEntity furnace = ClientTestHarness.requireBlockEntity(server, MACHINE_POS, ElectricFurnaceBlockEntity.class);
			ChestBlockEntity chest = ClientTestHarness.requireBlockEntity(server, MACHINE_POS.above(), ChestBlockEntity.class);
			return chest.getItem(0).isEmpty()
				&& (furnace.inventory.getItem(0).is(Items.RAW_IRON) || furnace.inventory.getItem(1).is(Items.IRON_INGOT));
		}, 40, "Redstone-enabled configured item input did not transfer");
		test.openUi(MACHINE_POS, ElectricFurnaceBlockEntity.class, "real-gui-redstone-item-input-enabled");
	}

	private static void placePoweredFurnace(ClientTestHarness test) {
		test.setBlock(MACHINE_POS, TRContent.Machine.ELECTRIC_FURNACE.block);
		test.setBlock(MACHINE_POS.east(), TRContent.SolarPanels.CREATIVE.block);
		test.setBlock(MACHINE_POS.above(), Blocks.AIR);
		test.setBlock(MACHINE_POS.west(), Blocks.AIR);
		test.setBlock(MACHINE_POS.south(), Blocks.AIR);
		test.waitTicks(3);
	}
}
