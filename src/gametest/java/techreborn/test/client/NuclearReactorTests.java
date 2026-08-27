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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;
import techreborn.blockentity.generator.nuclear.NuclearReactorBlockEntity;
import techreborn.component.TRDataComponentTypes;
import techreborn.init.TRContent;

final class NuclearReactorTests {
	private static final BlockPos REACTOR_POS = ClientTestHarness.INTERACTION_POS;

	private NuclearReactorTests() {
	}

	static void run(ClientTestHarness test) {
		testInactiveReactorDoesNotConsumeFuel(test);
		testFuelHeatCoolingAndReflectorOutput(test);
		testChambersUnlockAndRelockColumns(test);
	}

	private static void testInactiveReactorDoesNotConsumeFuel(ClientTestHarness test) {
		placeReactor(test);
		test.onServer(server -> {
			NuclearReactorBlockEntity reactor = reactor(server);
			reactor.setItemAt(0, 0, new ItemStack(TRContent.NuclearReactorComponents.URANIUM_FUEL_ROD));
			reactor.setChanged();
		});
		test.waitTicks(30);
		test.assertServer(server -> {
			NuclearReactorBlockEntity reactor = reactor(server);
			return reactor.getEuPerTick() == 0
				&& reactor.getHeat() == 0
				&& reactor.getItemAt(0, 0).getOrDefault(TRDataComponentTypes.STORED_HEAT, 0) == 0;
		}, "Unpowered nuclear reactor consumed fuel or generated heat/energy");
		test.openUi(REACTOR_POS, NuclearReactorBlockEntity.class, "nuclear-inactive-fuel-preserved");
	}

	private static void testFuelHeatCoolingAndReflectorOutput(ClientTestHarness test) {
		placeReactor(test);
		BlockPos storagePos = REACTOR_POS.east(2);
		test.setBlock(REACTOR_POS.south(), Blocks.REDSTONE_BLOCK);
		test.setBlock(REACTOR_POS.east(), TRContent.Cables.TIN.block);
		test.setBlock(storagePos, TRContent.Machine.LOW_VOLTAGE_SU.block);
		test.onServer(server -> {
			NuclearReactorBlockEntity reactor = reactor(server);
			reactor.setItemAt(1, 0, new ItemStack(TRContent.NuclearReactorComponents.URANIUM_FUEL_ROD));
			reactor.setItemAt(0, 0, new ItemStack(TRContent.NuclearReactorComponents.WATER_COOLANT_CELL_10K));
			reactor.setItemAt(2, 0, new ItemStack(TRContent.NuclearReactorComponents.NEUTRON_REFLECTOR));
			reactor.setChanged();
		});
		test.waitForServer(server -> reactor(server).getEuPerTick() == 10, 45,
			"Reflected uranium fuel rod did not produce its expected 10 EU/t");
		test.waitForServer(server -> ClientTestHarness.requireBlockEntity(server, storagePos,
			PowerAcceptorBlockEntity.class).getEnergy() > 0, 45,
			"Nuclear reactor did not deliver generated energy through its cable");
		test.assertServer(server -> reactor(server).getHeat() == 0,
			"Coolant-adjacent fuel added heat to the reactor hull");
		test.assertServer(server -> reactor(server).getItemAt(0, 0)
			.getOrDefault(TRDataComponentTypes.STORED_HEAT, 0) > 0, "Coolant cell did not absorb fuel heat");
		test.assertServer(server -> reactor(server).getItemAt(1, 0)
			.getOrDefault(TRDataComponentTypes.STORED_HEAT, 0) > 0, "Fuel rod did not consume durability");
		test.assertServer(server -> reactor(server).getItemAt(2, 0)
			.getOrDefault(TRDataComponentTypes.STORED_HEAT, 0) > 0, "Neutron reflector did not consume durability");
		test.openUi(REACTOR_POS, NuclearReactorBlockEntity.class, "nuclear-reflector-coolant-energy-cycle");
	}

	private static void testChambersUnlockAndRelockColumns(ClientTestHarness test) {
		placeReactor(test);
		for (Direction direction : Direction.values()) {
			test.setBlock(REACTOR_POS.relative(direction), TRContent.Machine.REACTOR_CHAMBER.block);
		}
		test.waitForServer(server -> reactor(server).getChamberCount() == NuclearReactorBlockEntity.MAX_CHAMBERS,
			45, "Six reactor chambers did not unlock the complete grid");
		test.onServer(server -> {
			NuclearReactorBlockEntity reactor = reactor(server);
			reactor.setItemAt(8, 0, new ItemStack(TRContent.NuclearReactorComponents.REACTOR_PLATING));
			reactor.setChanged();
		});
		test.assertServer(server -> reactor(server).isSlotAvailable(8),
			"Final reactor column was not available with six chambers");
		test.screenshot(REACTOR_POS, "nuclear-six-chamber-grid");

		test.setBlock(REACTOR_POS.west(), Blocks.AIR);
		test.waitForServer(server -> reactor(server).getChamberCount() == 5 && reactor(server).getItemAt(8, 0) == null,
			45, "Removing a chamber did not relock the final reactor column");
		test.assertServer(server -> !ClientTestHarness.level(server).getEntitiesOfClass(ItemEntity.class,
			new AABB(REACTOR_POS).inflate(2)).isEmpty(), "Component in a newly locked reactor slot was not ejected");
		test.openUi(REACTOR_POS, NuclearReactorBlockEntity.class, "nuclear-chamber-removed-slot-ejected");
	}

	private static void placeReactor(ClientTestHarness test) {
		test.clearTestArea();
		test.setBlock(REACTOR_POS, TRContent.Machine.NUCLEAR_REACTOR.block);
		test.waitTicks(3);
	}

	private static NuclearReactorBlockEntity reactor(net.minecraft.server.MinecraftServer server) {
		return ClientTestHarness.requireBlockEntity(server, REACTOR_POS, NuclearReactorBlockEntity.class);
	}
}
