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

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import reborncore.common.powerSystem.RcEnergyItem;
import techreborn.blockentity.machine.misc.ChargeOMatBlockEntity;
import techreborn.init.TRContent;
import techreborn.items.tool.DrillItem;

final class ElectricItemTests {
	private static final BlockPos CHARGER_POS = ClientTestHarness.INTERACTION_POS;

	private ElectricItemTests() {
	}

	static void run(ClientTestHarness test) {
		testChargeOMatChargesSeveralToolTiers(test);
		testChargedDrillMinesWithRealInput(test);
	}

	private static void testChargeOMatChargesSeveralToolTiers(ClientTestHarness test) {
		test.clearTestArea();
		test.setBlock(CHARGER_POS, TRContent.Machine.CHARGE_O_MAT.block);
		test.setBlock(CHARGER_POS.east(), TRContent.SolarPanels.CREATIVE.block);
		List<Item> tools = List.of(
			TRContent.BASIC_DRILL,
			TRContent.ADVANCED_DRILL,
			TRContent.INDUSTRIAL_DRILL,
			TRContent.BASIC_CHAINSAW,
			TRContent.ADVANCED_JACKHAMMER,
			TRContent.OMNI_TOOL
		);
		test.onServer(server -> {
			ChargeOMatBlockEntity charger = ClientTestHarness.requireBlockEntity(server, CHARGER_POS,
				ChargeOMatBlockEntity.class);
			for (int slot = 0; slot < tools.size(); slot++) {
				charger.inventory.setItem(slot, new ItemStack(tools.get(slot)));
			}
			charger.setChanged();
		});
		test.waitForServer(server -> {
			ChargeOMatBlockEntity charger = ClientTestHarness.requireBlockEntity(server, CHARGER_POS,
				ChargeOMatBlockEntity.class);
			for (int slot = 0; slot < tools.size(); slot++) {
				ItemStack stack = charger.inventory.getItem(slot);
				if (!(stack.getItem() instanceof RcEnergyItem energyItem) || energyItem.getStoredEnergy(stack) <= 0) {
					return false;
				}
			}
			return true;
		}, 80, "Solar-powered Charge-O-Mat did not charge every electric tool tier");
		test.openUi(CHARGER_POS, ChargeOMatBlockEntity.class, "electric-items-six-tools-charging");
	}

	private static void testChargedDrillMinesWithRealInput(ClientTestHarness test) {
		BlockPos stonePos = CHARGER_POS.west();
		test.setBlock(stonePos, Blocks.STONE);
		test.onServer(server -> {
			ChargeOMatBlockEntity charger = ClientTestHarness.requireBlockEntity(server, CHARGER_POS,
				ChargeOMatBlockEntity.class);
			ItemStack drill = charger.inventory.removeItemNoUpdate(0);
			DrillItem drillItem = (DrillItem) drill.getItem();
			drillItem.setStoredEnergy(drill, drillItem.cost * 2L);
			server.getPlayerList().getPlayers().getFirst().getInventory().setItem(0, drill);
		});
		test.runCommand("gamemode survival @a");
		test.waitTicks(2);
		test.screenshot(stonePos, "electric-drill-before-real-input-mining");
		test.mineBlockWithInput(stonePos, 40);
		test.assertServer(server -> ClientTestHarness.level(server).getBlockState(stonePos).isAir(),
			"Charged drill did not mine stone using held attack input");
		test.assertServer(server -> {
			ItemStack drill = server.getPlayerList().getPlayers().getFirst().getInventory().getItem(0);
			return drill.getItem() instanceof DrillItem drillItem
				&& drillItem.getStoredEnergy(drill) < drillItem.cost * 2L;
		}, "Drill did not consume energy while mining");
		test.screenshot(stonePos, "electric-drill-after-real-input-mining");
		test.runCommand("gamemode creative @a");
		test.waitTicks(2);
	}
}
