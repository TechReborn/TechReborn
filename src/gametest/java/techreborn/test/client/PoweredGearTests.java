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
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import reborncore.common.powerSystem.RcEnergyItem;
import techreborn.component.TRDataComponentTypes;
import techreborn.init.TRContent;
import techreborn.items.tool.DrillItem;
import techreborn.utils.TRItemUtils;

final class PoweredGearTests {
	private PoweredGearTests() {
	}

	static void run(ClientTestHarness test) {
		testQuantumSuitEffectsMovementAndRemoval(test);
		testNanosaberActivationCombatAndDepletion(test);
		testBatteryAndBatpackInventoryDistribution(test);
	}

	private static void testQuantumSuitEffectsMovementAndRemoval(ClientTestHarness test) {
		reset(test);
		test.onServer(server -> {
			var player = server.getPlayerList().getPlayers().getFirst();
			ItemStack helmet = charged(TRContent.QUANTUM_HELMET);
			helmet.set(TRDataComponentTypes.IS_ACTIVE, true);
			ItemStack leggings = charged(TRContent.QUANTUM_LEGGINGS);
			leggings.set(TRDataComponentTypes.IS_ACTIVE, true);
			player.setItemSlot(EquipmentSlot.HEAD, helmet);
			player.setItemSlot(EquipmentSlot.CHEST, charged(TRContent.QUANTUM_CHESTPLATE));
			player.setItemSlot(EquipmentSlot.LEGS, leggings);
			player.setItemSlot(EquipmentSlot.FEET, charged(TRContent.QUANTUM_BOOTS));
		});
		test.runCommand("gamemode survival @a");
		test.waitForServer(server -> {
			var player = server.getPlayerList().getPlayers().getFirst();
			return player.getAbilities().mayfly && player.hasEffect(MobEffects.NIGHT_VISION);
		}, 20, "Powered quantum suit did not enable flight and night vision");
		test.assertServer(server -> server.getPlayerList().getPlayers().getFirst().getArmorValue() > 0,
			"Full quantum suit did not apply powered armor attributes");

		long[] leggingsBefore = new long[1];
		test.onServer(server -> {
			var player = server.getPlayerList().getPlayers().getFirst();
			leggingsBefore[0] = energy(player.getItemBySlot(EquipmentSlot.LEGS));
			player.setSprinting(true);
			player.setRemainingFireTicks(100);
		});
		test.waitForServer(server -> !server.getPlayerList().getPlayers().getFirst().isOnFire(), 10,
			"Quantum chestplate did not extinguish the burning player");
		test.waitTicks(5);
		test.assertServer(server -> energy(server.getPlayerList().getPlayers().getFirst()
			.getItemBySlot(EquipmentSlot.LEGS)) < leggingsBefore[0],
			"Active quantum leggings did not spend energy while sprinting");

		BlockPos waterPos = ClientTestHarness.PLAYER_POS;
		test.setBlock(waterPos, Blocks.WATER);
		test.setBlock(waterPos.above(), Blocks.WATER);
		test.movePlayer(waterPos.getX() + 0.5, waterPos.getY(), waterPos.getZ() + 0.5);
		test.onServer(server -> server.getPlayerList().getPlayers().getFirst().setSwimming(true));
		test.waitForServer(server -> {
			var player = server.getPlayerList().getPlayers().getFirst();
			return player.hasEffect(MobEffects.WATER_BREATHING)
				&& player.hasEffect(MobEffects.DOLPHINS_GRACE);
		}, 20, "Quantum helmet and boots did not apply underwater movement effects");
		test.screenshot(waterPos, "gear-quantum-suit-underwater-effects");
		test.screenshotInventory("gear-quantum-full-suit-powered");

		test.onServer(server -> server.getPlayerList().getPlayers().getFirst()
			.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY));
		test.waitForServer(server -> !server.getPlayerList().getPlayers().getFirst().getAbilities().mayfly, 20,
			"Removing the quantum chestplate did not revoke survival flight");
	}

	private static void testNanosaberActivationCombatAndDepletion(ClientTestHarness test) {
		reset(test);
		ItemStack saber = charged(TRContent.NANOSABER);
		long initialEnergy = energy(saber);
		test.setHotbarItem(saber);
		test.runCommand("gamemode survival @a");
		test.sneakUseHeldItem();
		test.assertServer(server -> TRItemUtils.isActive(held(server)),
			"Real sneak-use did not activate the nanosaber");
		test.runCommand("summon minecraft:zombie 2 100 0 {NoAI:1b,Silent:1b}");
		test.waitForServer(server -> !zombies(server).isEmpty(), 10,
			"Nanosaber test zombie was not created");
		test.screenshot(new BlockPos(2, ClientTestHarness.TEST_Y + 1, 0), "gear-nanosaber-active-before-hit");
		test.attackAt(new BlockPos(2, ClientTestHarness.TEST_Y + 1, 0));
		test.assertServer(server -> zombies(server).stream()
			.anyMatch(zombie -> zombie.getHealth() < zombie.getMaxHealth()),
			"Active nanosaber did not damage the targeted entity with real attack input");
		test.assertServer(server -> energy(held(server)) < initialEnergy,
			"Nanosaber hit did not consume energy");
		test.onServer(server -> ((RcEnergyItem) held(server).getItem()).setStoredEnergy(held(server), 0));
		test.waitForServer(server -> !TRItemUtils.isActive(held(server)), 10,
			"Depleted nanosaber did not automatically deactivate");
		test.screenshotInventory("gear-nanosaber-depleted-disabled");
	}

	private static void testBatteryAndBatpackInventoryDistribution(ClientTestHarness test) {
		reset(test);
		ItemStack battery = charged(TRContent.LITHIUM_ION_BATTERY);
		long sourceStart = energy(battery);
		test.setHotbarItem(battery);
		test.onServer(server -> {
			var inventory = server.getPlayerList().getPlayers().getFirst().getInventory();
			inventory.setItem(1, new ItemStack(TRContent.BASIC_DRILL));
			inventory.setItem(2, new ItemStack(TRContent.RED_CELL_BATTERY));
		});
		test.runCommand("gamemode survival @a");
		test.sneakUseHeldItem();
		test.waitForServer(server -> energy(server.getPlayerList().getPlayers().getFirst()
			.getInventory().getItem(1)) > 0, 20, "Active battery did not charge the drill in player inventory");
		test.assertServer(server -> energy(server.getPlayerList().getPlayers().getFirst()
			.getInventory().getItem(2)) == 0, "Battery incorrectly transferred power into another battery");
		test.assertServer(server -> energy(held(server)) < sourceStart,
			"Inventory charging did not reduce the source battery energy");
		test.screenshotInventory("gear-battery-distributes-to-tools-only");

		reset(test);
		test.onServer(server -> {
			var player = server.getPlayerList().getPlayers().getFirst();
			player.setItemSlot(EquipmentSlot.CHEST, charged(TRContent.LITHIUM_ION_BATPACK));
			player.getInventory().setItem(1, new ItemStack(TRContent.BASIC_DRILL));
		});
		test.runCommand("gamemode survival @a");
		test.waitForServer(server -> {
			ItemStack drill = server.getPlayerList().getPlayers().getFirst().getInventory().getItem(1);
			return drill.getItem() instanceof DrillItem && energy(drill) > 0;
		}, 20, "Equipped batpack did not charge a compatible electric tool");
		test.assertServer(server -> {
			ItemStack pack = server.getPlayerList().getPlayers().getFirst().getItemBySlot(EquipmentSlot.CHEST);
			return energy(pack) < ((RcEnergyItem) pack.getItem()).getEnergyCapacity(pack);
		}, "Batpack did not spend energy while charging the tool");
		test.screenshotInventory("gear-batpack-charges-equipped-inventory");
	}

	private static java.util.List<Zombie> zombies(net.minecraft.server.MinecraftServer server) {
		return ClientTestHarness.level(server).getEntitiesOfClass(Zombie.class,
			new net.minecraft.world.phys.AABB(1, 99, -1, 3, 103, 1));
	}

	private static ItemStack charged(Item item) {
		ItemStack stack = new ItemStack(item);
		RcEnergyItem energyItem = (RcEnergyItem) item;
		energyItem.setStoredEnergy(stack, energyItem.getEnergyCapacity(stack));
		return stack;
	}

	private static long energy(ItemStack stack) {
		return stack.getItem() instanceof RcEnergyItem energyItem ? energyItem.getStoredEnergy(stack) : 0;
	}

	private static ItemStack held(net.minecraft.server.MinecraftServer server) {
		return server.getPlayerList().getPlayers().getFirst().getMainHandItem();
	}

	private static void reset(ClientTestHarness test) {
		test.runCommand("gamemode creative @a");
		test.runCommand("kill @e[type=!minecraft:player]");
		test.onServer(server -> {
			var player = server.getPlayerList().getPlayers().getFirst();
			player.getInventory().clearContent();
			player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
			player.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
			player.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
			player.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
			player.setSprinting(false);
			player.setSwimming(false);
		});
		test.movePlayer(ClientTestHarness.PLAYER_POS.getX() + 0.5, ClientTestHarness.TEST_Y,
			ClientTestHarness.PLAYER_POS.getZ() + 0.5);
		test.clearTestArea();
	}
}
