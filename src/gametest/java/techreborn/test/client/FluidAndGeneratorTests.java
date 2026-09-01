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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import techreborn.blockentity.generator.BaseFluidGeneratorBlockEntity;
import techreborn.blockentity.machine.tier2.PumpBlockEntity;
import techreborn.blockentity.storage.fluid.TankUnitBaseBlockEntity;
import techreborn.init.TRContent;

final class FluidAndGeneratorTests {
	private static final BlockPos MACHINE_POS = ClientTestHarness.INTERACTION_POS;
	private static final List<GeneratorCase> GENERATORS = List.of(
		new GeneratorCase("diesel", TRContent.Machine.DIESEL_GENERATOR.block, TRContent.Cells.DIESEL),
		new GeneratorCase("gas-turbine", TRContent.Machine.GAS_TURBINE.block, TRContent.Cells.HYDROGEN),
		new GeneratorCase("semi-fluid", TRContent.Machine.SEMI_FLUID_GENERATOR.block, TRContent.Cells.BIOFUEL),
		new GeneratorCase("thermal", TRContent.Machine.THERMAL_GENERATOR.block, TRContent.Cells.LAVA),
		new GeneratorCase("plasma", TRContent.Machine.PLASMA_GENERATOR.block, TRContent.Cells.HELIUMPLASMA)
	);

	private FluidAndGeneratorTests() {
	}

	static void run(ClientTestHarness test) {
		testTankCellInteractions(test);
		testPumpCollectsSourceFluid(test);
		testFluidGeneratorLifecycle(test);
		testGeneratorRejectsWrongFluid(test);
	}

	private static void testTankCellInteractions(ClientTestHarness test) {
		test.clearTestArea();
		test.placeWithInput(MACHINE_POS, TRContent.TankUnit.BASIC.block);
		test.useBlockWithItem(MACHINE_POS, TRContent.Cells.WATER.getStack());
		test.assertServer(server -> {
			TankUnitBaseBlockEntity tank = ClientTestHarness.requireBlockEntity(server, MACHINE_POS,
				TankUnitBaseBlockEntity.class);
			return tank.getTank().getFluid() == Fluids.WATER
				&& tank.getTank().getFluidAmount().equals(reborncore.common.fluid.FluidValue.BUCKET);
		}, "Water cell input did not fill the tank by one bucket");
		test.useBlockWithItem(MACHINE_POS, TRContent.Cells.LAVA.getStack());
		test.assertServer(server -> {
			TankUnitBaseBlockEntity tank = ClientTestHarness.requireBlockEntity(server, MACHINE_POS,
				TankUnitBaseBlockEntity.class);
			return tank.getTank().getFluid() == Fluids.WATER
				&& tank.getTank().getFluidAmount().equals(reborncore.common.fluid.FluidValue.BUCKET);
		}, "Tank accepted an incompatible fluid into existing water");
		test.openUi(MACHINE_POS, TankUnitBaseBlockEntity.class, "fluid-tank-real-cell-input-and-rejection");
	}

	private static void testPumpCollectsSourceFluid(ClientTestHarness test) {
		test.clearTestArea();
		test.placeWithInput(MACHINE_POS, TRContent.Machine.PUMP.block);
		test.setBlock(MACHINE_POS.above(), TRContent.SolarPanels.CREATIVE.block);
		test.setBlock(MACHINE_POS.below(), Blocks.WATER);
		test.onServer(server -> {
			PumpBlockEntity pump = ClientTestHarness.requireBlockEntity(server, MACHINE_POS, PumpBlockEntity.class);
			pump.setRange(PumpBlockEntity.DEFAULT_RANGE - 1);
			pump.setRange(PumpBlockEntity.DEFAULT_RANGE);
		});
		test.waitForServer(server -> {
			PumpBlockEntity pump = ClientTestHarness.requireBlockEntity(server, MACHINE_POS, PumpBlockEntity.class);
			return pump.getTank().getFluid() == Fluids.WATER
				&& pump.getTank().getFluidAmount().equalOrMoreThan(reborncore.common.fluid.FluidValue.BUCKET)
				&& ClientTestHarness.level(server).getBlockState(MACHINE_POS.below()).is(Blocks.COBBLESTONE);
		}, 180, "Pump did not collect and replace a water source");
		test.openUi(MACHINE_POS, PumpBlockEntity.class, "fluid-pump-collected-water-source");
	}

	private static void testFluidGeneratorLifecycle(ClientTestHarness test) {
		for (GeneratorCase generatorCase : GENERATORS) {
			test.clearTestArea();
			test.placeWithInput(MACHINE_POS, generatorCase.block());
			test.onServer(server -> {
				BaseFluidGeneratorBlockEntity generator = ClientTestHarness.requireBlockEntity(server, MACHINE_POS,
					BaseFluidGeneratorBlockEntity.class);
				generator.inventory.setItem(0, generatorCase.fuel().getStack());
				generator.setChanged();
			});
			test.waitForServer(server -> {
				BaseFluidGeneratorBlockEntity generator = ClientTestHarness.requireBlockEntity(server, MACHINE_POS,
					BaseFluidGeneratorBlockEntity.class);
				return generator.getStored() > 0
					&& generator.inventory.getItem(1).is(TRContent.Cells.EMPTY.asItem())
					&& !generator.tank.isEmpty();
			}, 40, generatorCase.name() + " generator did not consume its valid fluid cell");
			test.openUi(MACHINE_POS, BaseFluidGeneratorBlockEntity.class, "generator-" + generatorCase.name() + "-active");
		}
	}

	private static void testGeneratorRejectsWrongFluid(ClientTestHarness test) {
		test.clearTestArea();
		test.setBlock(MACHINE_POS, TRContent.Machine.DIESEL_GENERATOR.block);
		test.onServer(server -> {
			BaseFluidGeneratorBlockEntity generator = ClientTestHarness.requireBlockEntity(server, MACHINE_POS,
				BaseFluidGeneratorBlockEntity.class);
			generator.inventory.setItem(0, TRContent.Cells.WATER.getStack());
			generator.setChanged();
		});
		test.waitTicks(30);
		test.assertServer(server -> {
			BaseFluidGeneratorBlockEntity generator = ClientTestHarness.requireBlockEntity(server, MACHINE_POS,
				BaseFluidGeneratorBlockEntity.class);
			return generator.getStored() == 0
				&& generator.tank.isEmpty()
				&& generator.inventory.getItem(0).is(TRContent.Cells.WATER.asItem());
		}, "Diesel generator accepted water as fuel");
		test.openUi(MACHINE_POS, BaseFluidGeneratorBlockEntity.class, "odd-generator-rejected-water-cell");
	}

	private record GeneratorCase(String name, Block block, TRContent.Cells fuel) {
	}
}
