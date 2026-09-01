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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import techreborn.blockentity.generator.basic.SolidFuelGeneratorBlockEntity;
import techreborn.blockentity.machine.iron.IronFurnaceBlockEntity;
import techreborn.blockentity.machine.tier1.AlloySmelterBlockEntity;
import techreborn.blockentity.machine.tier1.CompressorBlockEntity;
import techreborn.blockentity.machine.tier1.ElectricFurnaceBlockEntity;
import techreborn.blockentity.machine.tier1.ExtractorBlockEntity;
import techreborn.blockentity.machine.tier1.GrinderBlockEntity;
import techreborn.blockentity.machine.tier1.RecyclerBlockEntity;
import techreborn.blockentity.machine.tier1.WireMillBlockEntity;
import techreborn.blockentity.machine.tier2.PumpBlockEntity;
import techreborn.blockentity.storage.item.StorageUnitBaseBlockEntity;
import techreborn.init.TRContent;

final class MachineInteractionTests {
	private static final List<UiCase> UI_CASES = List.of(
		new UiCase("iron-furnace", TRContent.Machine.IRON_FURNACE.block, IronFurnaceBlockEntity.class),
		new UiCase("electric-furnace", TRContent.Machine.ELECTRIC_FURNACE.block, ElectricFurnaceBlockEntity.class),
		new UiCase("grinder", TRContent.Machine.GRINDER.block, GrinderBlockEntity.class),
		new UiCase("compressor", TRContent.Machine.COMPRESSOR.block, CompressorBlockEntity.class),
		new UiCase("extractor", TRContent.Machine.EXTRACTOR.block, ExtractorBlockEntity.class),
		new UiCase("alloy-smelter", TRContent.Machine.ALLOY_SMELTER.block, AlloySmelterBlockEntity.class),
		new UiCase("wire-mill", TRContent.Machine.WIRE_MILL.block, WireMillBlockEntity.class),
		new UiCase("recycler", TRContent.Machine.RECYCLER.block, RecyclerBlockEntity.class),
		new UiCase("pump", TRContent.Machine.PUMP.block, PumpBlockEntity.class),
		new UiCase("solid-fuel-generator", TRContent.Machine.SOLID_FUEL_GENERATOR.block, SolidFuelGeneratorBlockEntity.class),
		new UiCase("low-voltage-storage", TRContent.Machine.LOW_VOLTAGE_SU.block, PowerAcceptorBlockEntity.class),
		new UiCase("crude-storage-unit", TRContent.StorageUnit.CRUDE.block, StorageUnitBaseBlockEntity.class)
	);

	private MachineInteractionTests() {
	}

	static void run(ClientTestHarness test) {
		testRealBlockPlacement(test);
		testMachineUis(test);
	}

	private static void testRealBlockPlacement(ClientTestHarness test) {
		BlockPos machinePos = new BlockPos(-2, ClientTestHarness.TEST_Y, -2);
		BlockPos cablePos = machinePos.east();
		BlockPos storagePos = cablePos.east();
		test.placeWithInput(machinePos, TRContent.Machine.IRON_FURNACE.block);
		test.placeWithInput(cablePos, TRContent.Cables.TIN.block);
		test.placeWithInput(storagePos, TRContent.StorageUnit.CRUDE.block);
		test.assertServer(server -> ClientTestHarness.requireBlockEntity(server, machinePos, IronFurnaceBlockEntity.class) != null,
			"Placed furnace did not create a block entity");
		test.assertServer(server -> ClientTestHarness.requireBlockEntity(server, storagePos, StorageUnitBaseBlockEntity.class) != null,
			"Placed storage unit did not create a block entity");
		test.screenshot(cablePos, "real-input-block-placement");
	}

	private static void testMachineUis(ClientTestHarness test) {
		for (UiCase uiCase : UI_CASES) {
			test.setBlock(ClientTestHarness.INTERACTION_POS, uiCase.block());
			test.openUi(ClientTestHarness.INTERACTION_POS, uiCase.blockEntityType(), "ui-" + uiCase.name());
		}
	}

	private record UiCase(String name, Block block, Class<? extends BlockEntity> blockEntityType) {
	}
}
