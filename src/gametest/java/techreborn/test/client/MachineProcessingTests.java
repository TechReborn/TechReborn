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

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.blockentity.machine.iron.IronFurnaceBlockEntity;
import techreborn.blockentity.machine.tier1.ElectricFurnaceBlockEntity;
import techreborn.init.TRContent;

final class MachineProcessingTests {
	private static final List<RecipeCase> RECIPE_CASES = List.of(
		new RecipeCase("grinder-coal", TRContent.Machine.GRINDER.block,
			List.of(new ItemStack(Items.COAL)), TRContent.Dusts.COAL.asItem(), 1),
		new RecipeCase("compressor-plantball", TRContent.Machine.COMPRESSOR.block,
			List.of(TRContent.Parts.PLANTBALL.getStack()), TRContent.Parts.COMPRESSED_PLANTBALL.asItem(), 1),
		new RecipeCase("extractor-rubber-log", TRContent.Machine.EXTRACTOR.block,
			List.of(new ItemStack(TRContent.RUBBER_LOG)), TRContent.Parts.RUBBER.asItem(), 1),
		new RecipeCase("alloy-smelter-bronze", TRContent.Machine.ALLOY_SMELTER.block,
			List.of(new ItemStack(Items.COPPER_INGOT, 3), TRContent.Ingots.TIN.getStack()), TRContent.Ingots.BRONZE.asItem(), 4),
		new RecipeCase("wire-mill-gold", TRContent.Machine.WIRE_MILL.block,
			List.of(new ItemStack(Items.GOLD_INGOT)), TRContent.Cables.GOLD.asItem(), 6)
	);

	private MachineProcessingTests() {
	}

	static void run(ClientTestHarness test) {
		testIronFurnace(test);
		testOverclockedElectricFurnace(test);
		testOverclockedRecipeMachines(test);
		testBlockedOutput(test);
	}

	private static void testIronFurnace(ClientTestHarness test) {
		test.setBlock(ClientTestHarness.INTERACTION_POS, TRContent.Machine.IRON_FURNACE.block);
		test.onServer(server -> {
			IronFurnaceBlockEntity furnace = ClientTestHarness.requireBlockEntity(server, ClientTestHarness.INTERACTION_POS, IronFurnaceBlockEntity.class);
			furnace.inventory.setItem(IronFurnaceBlockEntity.INPUT_SLOT, new ItemStack(Items.RAW_IRON));
			furnace.inventory.setItem(IronFurnaceBlockEntity.FUEL_SLOT, new ItemStack(Items.COAL));
			furnace.setChanged();
		});
		test.waitForServer(server -> {
			IronFurnaceBlockEntity furnace = ClientTestHarness.requireBlockEntity(server, ClientTestHarness.INTERACTION_POS, IronFurnaceBlockEntity.class);
			return furnace.inventory.getItem(IronFurnaceBlockEntity.OUTPUT_SLOT).is(Items.IRON_INGOT);
		}, 220, "Iron furnace did not smelt raw iron");
		test.openUi(ClientTestHarness.INTERACTION_POS, IronFurnaceBlockEntity.class, "processing-iron-furnace-complete");
	}

	private static void testOverclockedElectricFurnace(ClientTestHarness test) {
		placeSolarPoweredMachine(test, TRContent.Machine.ELECTRIC_FURNACE.block);
		test.onServer(server -> {
			ElectricFurnaceBlockEntity furnace = ClientTestHarness.requireBlockEntity(server, ClientTestHarness.INTERACTION_POS, ElectricFurnaceBlockEntity.class);
			furnace.inventory.setItem(0, new ItemStack(Items.RAW_GOLD));
			installOverclockers(furnace);
			furnace.setChanged();
		});
		test.screenshot(ClientTestHarness.INTERACTION_POS.above(), "creative-solar-powered-machine");
		test.waitForServer(server -> {
			ElectricFurnaceBlockEntity furnace = ClientTestHarness.requireBlockEntity(server, ClientTestHarness.INTERACTION_POS, ElectricFurnaceBlockEntity.class);
			return furnace.inventory.getItem(1).is(Items.GOLD_INGOT);
		}, 70, "Overclocked electric furnace did not smelt raw gold");
		test.openUi(ClientTestHarness.INTERACTION_POS, ElectricFurnaceBlockEntity.class, "processing-overclocked-electric-furnace");
	}

	private static void testOverclockedRecipeMachines(ClientTestHarness test) {
		for (RecipeCase recipe : RECIPE_CASES) {
			placeSolarPoweredMachine(test, recipe.block());
			test.onServer(server -> {
				GenericMachineBlockEntity machine = ClientTestHarness.requireBlockEntity(server, ClientTestHarness.INTERACTION_POS, GenericMachineBlockEntity.class);
				for (int slot = 0; slot < recipe.inputs().size(); slot++) {
					machine.inventory.setItem(slot, recipe.inputs().get(slot).copy());
				}
				installOverclockers(machine);
				machine.setChanged();
			});
			test.waitForServer(server -> {
				GenericMachineBlockEntity machine = ClientTestHarness.requireBlockEntity(server, ClientTestHarness.INTERACTION_POS, GenericMachineBlockEntity.class);
				ItemStack output = machine.inventory.getItem(recipe.inputs().size());
				return output.is(recipe.output()) && output.getCount() == recipe.outputCount();
			}, 140, recipe.name() + " did not produce its expected output");
			test.openUi(ClientTestHarness.INTERACTION_POS, GenericMachineBlockEntity.class, "processing-" + recipe.name());
		}
	}

	private static void testBlockedOutput(ClientTestHarness test) {
		placeSolarPoweredMachine(test, TRContent.Machine.GRINDER.block);
		test.onServer(server -> {
			GenericMachineBlockEntity grinder = ClientTestHarness.requireBlockEntity(server, ClientTestHarness.INTERACTION_POS, GenericMachineBlockEntity.class);
			grinder.inventory.setItem(0, new ItemStack(Items.COAL));
			grinder.inventory.setItem(1, new ItemStack(Items.DIAMOND, 64));
			installOverclockers(grinder);
			grinder.setChanged();
		});
		test.waitTicks(140);
		test.assertServer(server -> {
			GenericMachineBlockEntity grinder = ClientTestHarness.requireBlockEntity(server, ClientTestHarness.INTERACTION_POS, GenericMachineBlockEntity.class);
			return grinder.inventory.getItem(0).is(Items.COAL)
				&& grinder.inventory.getItem(1).is(Items.DIAMOND)
				&& grinder.inventory.getItem(1).getCount() == 64;
		}, "Grinder consumed input despite an incompatible full output slot");
		test.openUi(ClientTestHarness.INTERACTION_POS, GenericMachineBlockEntity.class, "odd-blocked-machine-output");
	}

	private static void installOverclockers(MachineBaseBlockEntity machine) {
		for (int slot = 0; slot < 3; slot++) {
			machine.getUpgradeInventory().setItem(slot, new ItemStack(TRContent.Upgrades.OVERCLOCKER));
		}
	}

	private static void placeSolarPoweredMachine(ClientTestHarness test, Block machine) {
		test.setBlock(ClientTestHarness.INTERACTION_POS, machine);
		test.setBlock(ClientTestHarness.INTERACTION_POS.above(), TRContent.SolarPanels.CREATIVE.block);
	}

	private record RecipeCase(String name, Block block, List<ItemStack> inputs, Item output, int outputCount) {
	}
}
