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
import reborncore.common.fluid.FluidValue;
import reborncore.common.fluid.container.FluidInstance;
import techreborn.blockentity.machine.multiblock.IndustrialBlastFurnaceBlockEntity;
import techreborn.blockentity.machine.tier1.ElectricFurnaceBlockEntity;
import techreborn.blockentity.storage.fluid.TankUnitBaseBlockEntity;
import techreborn.init.TRContent;

final class PersistenceTests {
	private static final BlockPos FURNACE_POS = ClientTestHarness.INTERACTION_POS;
	private static final BlockPos TANK_POS = new BlockPos(4, ClientTestHarness.TEST_Y, 0);
	private static final BlockPos BLAST_FURNACE_POS = new BlockPos(-2, ClientTestHarness.TEST_Y, 0);

	private PersistenceTests() {
	}

	static void prepare(ClientTestHarness test) {
		test.clearTestArea();
		test.setBlock(FURNACE_POS, TRContent.Machine.ELECTRIC_FURNACE.block);
		test.setBlock(TANK_POS, TRContent.TankUnit.ADVANCED.block);
		test.setBlock(BLAST_FURNACE_POS, TRContent.Machine.INDUSTRIAL_BLAST_FURNACE.block);
		test.formMultiblock(BLAST_FURNACE_POS, IndustrialBlastFurnaceBlockEntity.class);
		test.onServer(server -> {
			ElectricFurnaceBlockEntity furnace = ClientTestHarness.requireBlockEntity(server, FURNACE_POS,
				ElectricFurnaceBlockEntity.class);
			furnace.getUpgradeInventory().setItem(0, new ItemStack(TRContent.Upgrades.ENERGY_STORAGE));
			furnace.inventory.setItem(1, new ItemStack(Items.GOLD_INGOT, 7));
			furnace.setStored(furnace.getMaxStoredPower() / 2);
			furnace.setChanged();

			TankUnitBaseBlockEntity tank = ClientTestHarness.requireBlockEntity(server, TANK_POS,
				TankUnitBaseBlockEntity.class);
			tank.getTank().setFluidInstance(new FluidInstance(Fluids.WATER, FluidValue.BUCKET.multiply(2)));
			tank.setChanged();

			IndustrialBlastFurnaceBlockEntity blastFurnace = ClientTestHarness.requireBlockEntity(server,
				BLAST_FURNACE_POS, IndustrialBlastFurnaceBlockEntity.class);
			blastFurnace.inventory.setItem(0, TRContent.Dusts.GALENA.getStack(2));
			blastFurnace.setChanged();
		});
		test.movePlayer(ClientTestHarness.PLAYER_POS.getX() + 0.5, ClientTestHarness.TEST_Y,
			ClientTestHarness.PLAYER_POS.getZ() + 0.5);
		test.screenshot(FURNACE_POS, "persistence-before-world-reload");
	}

	static void verify(ClientTestHarness test) {
		test.movePlayer(ClientTestHarness.PLAYER_POS.getX() + 0.5, ClientTestHarness.TEST_Y,
			ClientTestHarness.PLAYER_POS.getZ() + 0.5);
		test.waitForServer(server -> {
			IndustrialBlastFurnaceBlockEntity blastFurnace = ClientTestHarness.requireBlockEntity(server,
				BLAST_FURNACE_POS, IndustrialBlastFurnaceBlockEntity.class);
			blastFurnace.rematch();
			return blastFurnace.isShapeValid();
		}, 40, "Blast furnace multiblock did not recover after world reload");
		test.assertServer(server -> {
			ElectricFurnaceBlockEntity furnace = ClientTestHarness.requireBlockEntity(server, FURNACE_POS,
				ElectricFurnaceBlockEntity.class);
			TankUnitBaseBlockEntity tank = ClientTestHarness.requireBlockEntity(server, TANK_POS,
				TankUnitBaseBlockEntity.class);
			IndustrialBlastFurnaceBlockEntity blastFurnace = ClientTestHarness.requireBlockEntity(server,
				BLAST_FURNACE_POS, IndustrialBlastFurnaceBlockEntity.class);
			return furnace.inventory.getItem(1).is(Items.GOLD_INGOT)
				&& furnace.inventory.getItem(1).getCount() == 7
				&& furnace.getStored() > 0
				&& furnace.getUpgradeInventory().getItem(0).is(TRContent.Upgrades.ENERGY_STORAGE.asItem())
				&& tank.getTank().getFluid() == Fluids.WATER
				&& tank.getTank().getFluidAmount().equals(FluidValue.BUCKET.multiply(2))
				&& blastFurnace.inventory.getItem(0).getCount() == 2;
		}, "Machine items, upgrades, energy, fluid, or multiblock inventory did not survive world reload");
		test.openUi(FURNACE_POS, ElectricFurnaceBlockEntity.class, "persistence-after-world-reload");
	}
}
