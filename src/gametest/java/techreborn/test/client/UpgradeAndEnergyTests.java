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
import techreborn.blockentity.machine.tier1.ElectricFurnaceBlockEntity;
import techreborn.blockentity.transformers.TransformerBlockEntity;
import techreborn.init.TRContent;

final class UpgradeAndEnergyTests {
	private static final BlockPos MACHINE_POS = ClientTestHarness.INTERACTION_POS;

	private UpgradeAndEnergyTests() {
	}

	static void run(ClientTestHarness test) {
		testMixedUpgradesAndHighTierSupply(test);
		testMaximumOverclockers(test);
		testTransformerBlocks(test);
	}

	private static void testMixedUpgradesAndHighTierSupply(ClientTestHarness test) {
		test.clearTestArea();
		test.placeWithInput(MACHINE_POS, TRContent.Machine.ELECTRIC_FURNACE.block);
		test.onServer(server -> {
			ElectricFurnaceBlockEntity furnace = furnace(server);
			furnace.getUpgradeInventory().setItem(0, new ItemStack(TRContent.Upgrades.TRANSFORMER));
			furnace.getUpgradeInventory().setItem(1, new ItemStack(TRContent.Upgrades.ENERGY_STORAGE));
			furnace.getUpgradeInventory().setItem(2, new ItemStack(TRContent.Upgrades.MUFFLER));
			furnace.getUpgradeInventory().setItem(3, new ItemStack(TRContent.Upgrades.OVERCLOCKER));
			furnace.setChanged();
		});
		test.setBlock(MACHINE_POS.above(), TRContent.SolarPanels.CREATIVE.block);
		test.waitForServer(server -> {
			ElectricFurnaceBlockEntity furnace = furnace(server);
			return furnace.extraTier == 1
				&& furnace.extraPowerStorage > 0
				&& furnace.getSpeedMultiplier() > 0
				&& furnace.isMuffled()
				&& furnace.getStored() == furnace.getMaxStoredPower();
		}, 30, "Mixed upgrades were not applied or high-tier solar power was not accepted");
		test.assertServer(server -> ClientTestHarness.level(server).getBlockState(MACHINE_POS)
			.is(TRContent.Machine.ELECTRIC_FURNACE.block), "Powered upgraded machine was unexpectedly destroyed");
		test.openUi(MACHINE_POS, ElectricFurnaceBlockEntity.class, "upgrades-transformer-storage-muffler-overclocker");
	}

	private static void testMaximumOverclockers(ClientTestHarness test) {
		test.clearTestArea();
		test.setBlock(MACHINE_POS, TRContent.Machine.ELECTRIC_FURNACE.block);
		test.onServer(server -> {
			ElectricFurnaceBlockEntity furnace = furnace(server);
			for (int slot = 0; slot < furnace.getUpgradeInventory().getContainerSize(); slot++) {
				furnace.getUpgradeInventory().setItem(slot, new ItemStack(TRContent.Upgrades.OVERCLOCKER));
			}
			furnace.setChanged();
		});
		test.waitForServer(server -> {
			ElectricFurnaceBlockEntity furnace = furnace(server);
			return furnace.getSpeedMultiplier() > 0.5
				&& furnace.getPowerMultiplier() > 1
				&& furnace.getMaxStoredPower() > furnace.getBaseMaxPower();
		}, 20, "Maximum overclocker configuration was not applied");
		test.openUi(MACHINE_POS, ElectricFurnaceBlockEntity.class, "upgrades-maximum-overclockers");
	}

	private static void testTransformerBlocks(ClientTestHarness test) {
		test.clearTestArea();
		BlockPos start = new BlockPos(-3, ClientTestHarness.TEST_Y, 3);
		test.setBlock(start, TRContent.Machine.LV_TRANSFORMER.block);
		test.setBlock(start.east(), TRContent.Machine.MV_TRANSFORMER.block);
		test.setBlock(start.east(2), TRContent.Machine.HV_TRANSFORMER.block);
		test.setBlock(start.east(3), TRContent.Machine.EV_TRANSFORMER.block);
		test.assertServer(server -> {
			long previousTierInput = 0;
			for (int offset = 0; offset < 4; offset++) {
				TransformerBlockEntity transformer = ClientTestHarness.requireBlockEntity(server, start.east(offset),
					TransformerBlockEntity.class);
				if (transformer.inputTier.getMaxInput() <= previousTierInput) {
					return false;
				}
				previousTierInput = transformer.inputTier.getMaxInput();
			}
			return true;
		}, "Transformer blocks did not expose increasing energy tiers");
		test.screenshot(start.east(), "energy-transformer-tier-lineup");
	}

	private static ElectricFurnaceBlockEntity furnace(net.minecraft.server.MinecraftServer server) {
		return ClientTestHarness.requireBlockEntity(server, MACHINE_POS, ElectricFurnaceBlockEntity.class);
	}
}
