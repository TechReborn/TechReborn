/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2024 TechReborn
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

package techreborn.utils;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import reborncore.common.powerSystem.RcEnergyItem;
import techreborn.component.TRDataComponentTypes;

import java.util.List;

public class TRItemUtils {

	/**
	 * Checks if powered item is active
	 *
	 * @param stack {@link ItemStack} Stack to check
	 * @return {@code boolean} True if powered item is active
	 */
	public static boolean isActive(ItemStack stack) {
		return !stack.isEmpty() && Boolean.TRUE.equals(stack.get(TRDataComponentTypes.IS_ACTIVE));
	}

	/**
	 * Check if powered item has enough energy to continue being in active state
	 *
	 * @param stack     {@link ItemStack} Stack to check
	 * @param cost      {@link int} Cost of operation performed by tool
	 */
	public static void checkActive(ItemStack stack, int cost, Entity player) {
		if (!TRItemUtils.isActive(stack)) {
			return;
		}
		if (((RcEnergyItem) stack.getItem()).getStoredEnergy(stack) >= cost) {
			return;
		}

		if (player instanceof ServerPlayerEntity serverPlayerEntity) {
			serverPlayerEntity.sendMessage(Text.translatable("reborncore.message.energyError")
				.formatted(Formatting.GRAY)
				.append(" ")
				.append(
					Text.translatable("reborncore.message.deactivating")
						.formatted(Formatting.GOLD)
				), true);
		}

		stack.set(TRDataComponentTypes.IS_ACTIVE, false);
	}

	/**
	 * Switch active\inactive state for powered item
	 *
	 * @param stack     {@link ItemStack} Stack to switch state
	 * @param cost      {@code int} Cost of operation performed by tool
	 */
	public static void switchActive(ItemStack stack, int cost, Entity entity) {
		TRItemUtils.checkActive(stack, cost, entity);

		if (!TRItemUtils.isActive(stack)) {
			stack.set(TRDataComponentTypes.IS_ACTIVE, true);

			if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
				serverPlayerEntity.sendMessage(Text.translatable("reborncore.message.setTo")
					.formatted(Formatting.GRAY)
					.append(" ")
					.append(
						Text.translatable("reborncore.message.active")
							.formatted(Formatting.GOLD)
					), true);
			}
		} else {
			stack.set(TRDataComponentTypes.IS_ACTIVE, false);
			if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
				serverPlayerEntity.sendMessage(Text.translatable("reborncore.message.setTo")
					.formatted(Formatting.GRAY)
					.append(" ")
					.append(
						Text.translatable("reborncore.message.inactive")
							.formatted(Formatting.GOLD)
					), true);
			}
		}
	}

	/**
	 * Adds active\inactive state to powered item tooltip
	 *
	 * @param stack   {@link ItemStack} Stack to check
	 * @param tooltip {@link List} List of {@link Text} tooltip strings
	 */
	public static void buildActiveTooltip(ItemStack stack, List<Text> tooltip) {
		if (!TRItemUtils.isActive(stack)) {
			tooltip.add(Text.translatable("reborncore.message.inactive").formatted(Formatting.RED));
		} else {
			tooltip.add(Text.translatable("reborncore.message.active").formatted(Formatting.GREEN));
		}
	}
}
