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
import net.minecraft.world.level.material.Fluids;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.blockentity.machine.multiblock.DistillationTowerBlockEntity;
import techreborn.blockentity.machine.multiblock.ImplosionCompressorBlockEntity;
import techreborn.blockentity.machine.multiblock.IndustrialGrinderBlockEntity;
import techreborn.init.TRContent;

final class AdvancedMultiblockTests {
	private static final BlockPos CONTROLLER_POS = ClientTestHarness.INTERACTION_POS;

	private AdvancedMultiblockTests() {
	}

	static void run(ClientTestHarness test) {
		testIndustrialGrinderWaterRecipe(test);
		testDistillationTowerRecipe(test);
		testImplosionCompressorRecipe(test);
	}

	private static void testIndustrialGrinderWaterRecipe(ClientTestHarness test) {
		placeMultiblock(test, TRContent.Machine.INDUSTRIAL_GRINDER.block, IndustrialGrinderBlockEntity.class);
		test.onServer(server -> {
			IndustrialGrinderBlockEntity grinder = ClientTestHarness.requireBlockEntity(server, CONTROLLER_POS,
				IndustrialGrinderBlockEntity.class);
			grinder.tank.setFluidInstance(new FluidInstance(Fluids.WATER, FluidValue.BUCKET));
			grinder.inventory.setItem(0, new ItemStack(Items.IRON_ORE));
			installOverclockers(grinder);
			grinder.setChanged();
		});
		test.waitForServer(server -> {
			IndustrialGrinderBlockEntity grinder = ClientTestHarness.requireBlockEntity(server, CONTROLLER_POS,
				IndustrialGrinderBlockEntity.class);
			return grinder.inventory.getItem(2).is(Items.RAW_IRON) && grinder.inventory.getItem(2).getCount() == 3;
		}, 100, "Industrial grinder did not complete its water recipe");
		test.openUi(CONTROLLER_POS, IndustrialGrinderBlockEntity.class, "multiblock-industrial-grinder-water-recipe");
	}

	private static void testDistillationTowerRecipe(ClientTestHarness test) {
		placeMultiblock(test, TRContent.Machine.DISTILLATION_TOWER.block, DistillationTowerBlockEntity.class);
		test.onServer(server -> {
			DistillationTowerBlockEntity tower = ClientTestHarness.requireBlockEntity(server, CONTROLLER_POS,
				DistillationTowerBlockEntity.class);
			tower.inventory.setItem(0, TRContent.Cells.EMPTY.getStack(16));
			tower.inventory.setItem(1, TRContent.Cells.OIL.getStack(16));
			installOverclockers(tower);
			tower.setChanged();
		});
		test.waitForServer(server -> {
			DistillationTowerBlockEntity tower = ClientTestHarness.requireBlockEntity(server, CONTROLLER_POS,
				DistillationTowerBlockEntity.class);
			return tower.inventory.getItem(2).is(TRContent.Cells.DIESEL.asItem())
				&& tower.inventory.getItem(2).getCount() == 16
				&& tower.inventory.getItem(3).is(TRContent.Cells.SULFURIC_ACID.asItem())
				&& tower.inventory.getItem(4).is(TRContent.Cells.GLYCERYL.asItem());
		}, 180, "Distillation tower did not produce all oil products");
		test.openUi(CONTROLLER_POS, DistillationTowerBlockEntity.class, "multiblock-distillation-tower-products");
	}

	private static void testImplosionCompressorRecipe(ClientTestHarness test) {
		placeMultiblock(test, TRContent.Machine.IMPLOSION_COMPRESSOR.block, ImplosionCompressorBlockEntity.class);
		test.onServer(server -> {
			ImplosionCompressorBlockEntity compressor = ClientTestHarness.requireBlockEntity(server, CONTROLLER_POS,
				ImplosionCompressorBlockEntity.class);
			compressor.inventory.setItem(0, TRContent.Dusts.DIAMOND.getStack(4));
			compressor.inventory.setItem(1, new ItemStack(Items.TNT, 16));
			installOverclockers(compressor);
			compressor.setChanged();
		});
		test.waitForServer(server -> {
			ImplosionCompressorBlockEntity compressor = ClientTestHarness.requireBlockEntity(server, CONTROLLER_POS,
				ImplosionCompressorBlockEntity.class);
			return compressor.inventory.getItem(2).is(Items.DIAMOND)
				&& compressor.inventory.getItem(2).getCount() == 3
				&& compressor.inventory.getItem(3).is(TRContent.Dusts.DARK_ASHES.asItem());
		}, 500, "Implosion compressor did not complete its TNT recipe");
		test.openUi(CONTROLLER_POS, ImplosionCompressorBlockEntity.class, "multiblock-implosion-compressor-products");
	}

	private static void placeMultiblock(ClientTestHarness test, net.minecraft.world.level.block.Block block,
			Class<? extends MachineBaseBlockEntity> type) {
		test.clearTestArea();
		test.placeWithInput(CONTROLLER_POS, block);
		test.formMultiblock(CONTROLLER_POS, type);
		test.setBlock(CONTROLLER_POS.above(), TRContent.SolarPanels.CREATIVE.block);
		test.waitForServer(server -> ClientTestHarness.requireBlockEntity(server, CONTROLLER_POS, type).isShapeValid(),
			40, type.getSimpleName() + " multiblock did not assemble");
		test.screenshot(CONTROLLER_POS.above(2), "multiblock-" + type.getSimpleName().replace("BlockEntity", "").toLowerCase());
	}

	private static void installOverclockers(MachineBaseBlockEntity machine) {
		for (int slot = 0; slot < machine.getUpgradeInventory().getContainerSize(); slot++) {
			machine.getUpgradeInventory().setItem(slot, new ItemStack(TRContent.Upgrades.OVERCLOCKER));
		}
	}
}
