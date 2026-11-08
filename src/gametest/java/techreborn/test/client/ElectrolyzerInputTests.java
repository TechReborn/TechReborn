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

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import reborncore.common.screen.slot.FilteredSlot;
import techreborn.blockentity.machine.tier1.IndustrialElectrolyzerBlockEntity;
import techreborn.init.TRContent;

public class ElectrolyzerInputTests implements FabricClientGameTest {
	private static final BlockPos MACHINE_POS = ClientTestHarness.INTERACTION_POS;

	@Override
	public void runTest(ClientGameTestContext context) {
		try (TestSingleplayerContext singleplayer = context.worldBuilder().create()) {
			ClientTestHarness test = new ClientTestHarness(context, singleplayer.getServer());
			test.prepareWorld();
			testRecipe(test, context, new ItemStack(Items.BONE_MEAL, 6), TRContent.Cells.SULFURIC_ACID.getStack(3),
				false, TRContent.Cells.CALCIUM.getStack(2), TRContent.Cells.FLUORINE.getStack(),
				TRContent.Dusts.PHOSPHOROUS.getStack());
			testRecipe(test, context, TRContent.Dusts.SALTPETER.getStack(10), TRContent.Cells.SULFURIC_ACID.getStack(4),
				true, TRContent.Cells.NITRIC_ACID.getStack(2), TRContent.Cells.POTASSIUM.getStack(2),
				TRContent.Dusts.SULFUR.getStack(4), TRContent.Dusts.DARK_ASHES.getStack(2));
			testRecipe(test, context, TRContent.Dusts.BAUXITE.getStack(12), TRContent.Cells.EMPTY.getStack(8),
				true, TRContent.Dusts.ALUMINUM.getStack(8), TRContent.SmallDusts.TITANIUM.getStack(2),
				TRContent.Cells.HYDROGEN.getStack(5), TRContent.Cells.COMPRESSED_AIR.getStack(3));
			testRecipe(test, context, TRContent.Cells.SULFURIC_ACID.getStack(5), ItemStack.EMPTY,
				false, TRContent.Cells.HYDROGEN.getStack(2), TRContent.Cells.SULFUR.getStack(),
				TRContent.Cells.COMPRESSED_AIR.getStack(2));
			testSlotFilters(test);
		}
	}

	private static void testRecipe(ClientTestHarness test, ClientGameTestContext context, ItemStack material,
			ItemStack cells, boolean shiftCells, ItemStack... outputs) {
		test.resetTestState();
		test.setBlock(MACHINE_POS, TRContent.Machine.INDUSTRIAL_ELECTROLYZER.block);
		test.onServer(server -> {
			var machine = ClientTestHarness.requireBlockEntity(server, MACHINE_POS, IndustrialElectrolyzerBlockEntity.class);
			for (int slot = 0; slot < machine.getUpgradeInventory().getContainerSize(); slot++) {
				machine.getUpgradeInventory().setItem(slot, new ItemStack(TRContent.Upgrades.OVERCLOCKER));
			}
		});
		test.openUi(MACHINE_POS, IndustrialElectrolyzerBlockEntity.class);
		insert(test, material, 0);
		if (!cells.isEmpty()) {
			if (shiftCells) {
				test.setHotbarItem(cells.copy());
				context.runOnClient(client -> {
					var menu = client.player.containerMenu;
					var slot = menu.slots.stream()
						.filter(candidate -> candidate.container == client.player.getInventory()
							&& candidate.getContainerSlot() == 0)
						.findFirst().orElseThrow();
					client.gameMode.handleContainerInput(menu.containerId, slot.index, 0,
						ContainerInput.QUICK_MOVE, client.player);
				});
				context.waitTicks(2);
			} else {
				insert(test, cells, 1);
			}
		}
		test.assertServer(server -> {
			var machine = ClientTestHarness.requireBlockEntity(server, MACHINE_POS, IndustrialElectrolyzerBlockEntity.class);
			return ItemStack.matches(machine.inventory.getItem(0), material)
				&& ItemStack.matches(machine.inventory.getItem(1), cells);
		}, "GUI insertion did not place the electrolyzer ingredients in their input slots");
		test.closeUi();
		test.setBlock(MACHINE_POS.above(), TRContent.SolarPanels.CREATIVE.block);
		test.waitForServer(server -> {
			var machine = ClientTestHarness.requireBlockEntity(server, MACHINE_POS, IndustrialElectrolyzerBlockEntity.class);
			if (!machine.inventory.getItem(0).isEmpty() || !machine.inventory.getItem(1).isEmpty()) {
				return false;
			}
			for (int slot = 0; slot < 4; slot++) {
				ItemStack expected = slot < outputs.length ? outputs[slot] : ItemStack.EMPTY;
				if (!ItemStack.matches(machine.inventory.getItem(slot + 2), expected)) {
					return false;
				}
			}
			return true;
		}, 300, "GUI-fed electrolyzer did not consume the inputs and produce the expected outputs");
	}

	private static void insert(ClientTestHarness test, ItemStack stack, int slot) {
		test.setHotbarItem(stack.copy());
		test.clickPlayerSlot(0);
		test.clickMachineSlot(FilteredSlot.class, slot);
	}

	private static void testSlotFilters(ClientTestHarness test) {
		test.onServer(server -> {
			var machine = ClientTestHarness.requireBlockEntity(server, MACHINE_POS, IndustrialElectrolyzerBlockEntity.class);
			var player = server.getPlayerList().getPlayers().getFirst();
			var menu = machine.createScreenHandler(0, player);
			var cellSlot = menu.slots.stream()
				.filter(slot -> slot.container == machine && slot.getContainerSlot() == 1)
				.findFirst().orElseThrow();
			var materialSlot = menu.slots.stream()
				.filter(slot -> slot.container == machine && slot.getContainerSlot() == 0)
				.findFirst().orElseThrow();
			for (TRContent.Cells cell : TRContent.Cells.values()) {
				if (!cellSlot.mayPlace(cell.getStack())) {
					throw new AssertionError("Cell input rejected " + cell.getName());
				}
			}
			for (ItemStack invalid : new ItemStack[]{
				new ItemStack(Items.BONE_MEAL), new ItemStack(Items.BUCKET), new ItemStack(TRContent.RED_CELL_BATTERY),
				new ItemStack(TRContent.NuclearReactorComponents.WATER_COOLANT_CELL_10K)
			}) {
				if (cellSlot.mayPlace(invalid)) {
					throw new AssertionError("Cell input accepted a non-cell: " + invalid);
				}
			}
			if (materialSlot.mayPlace(TRContent.Cells.EMPTY.getStack())
				|| !materialSlot.mayPlace(TRContent.Cells.SULFURIC_ACID.getStack())) {
				throw new AssertionError("Material input no longer preserves its existing cell behavior");
			}
		});
	}
}
