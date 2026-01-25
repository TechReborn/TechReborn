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

package techreborn.items.reactor;

import java.util.function.Consumer;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import techreborn.blockentity.generator.nuclear.NuclearReactorBlockEntity;
import techreborn.component.TRDataComponentTypes;
import techreborn.init.TRItemSettings;

/**
 * Base class for all reactor components.
 */
public abstract class ReactorComponentItem extends Item {

	protected final int maxDurability;

	public ReactorComponentItem(String name, int maxDurability) {
		super(TRItemSettings.reactorComponent(name));
		this.maxDurability = maxDurability;
	}

	// Component processing

	/**
	 * Process heat operations for this component.
	 * Called during the heat phase - handles heat generation,
	 * distribution, pulling from hull, self-venting, and component cooling.
	 */
	public void processHeat(ItemStack stack, NuclearReactorBlockEntity reactor, int x, int y) {
		// Default: do nothing
	}

	/**
	 * Process energy operations for this component.
	 * Called during the energy phase - handles EU generation
	 * and fuel consumption.
	 */
	public void processEnergy(ItemStack stack, NuclearReactorBlockEntity reactor, int x, int y) {
		// Default: do nothing
	}

	// Component properties

	/**
	 * @return true if this is a fuel rod that generates pulses
	 */
	public boolean isFuelRod() {
		return false;
	}

	/**
	 * @return true if this is a reflector that bounces pulses
	 */
	public boolean isReflector() {
		return false;
	}

	/**
	 * @return true if this component can store heat
	 */
	public boolean canStoreHeat() {
		return false;
	}

	/**
	 * @return maximum heat this component can store
	 */
	public int getMaxHeat(ItemStack stack) {
		return 0;
	}

	/**
	 * @return current heat stored in this component
	 */
	public int getCurrentHeat(ItemStack stack) {
		return stack.getOrDefault(TRDataComponentTypes.STORED_HEAT, 0);
	}

	/**
	 * Add heat to this component.
	 *
	 * @return amount of heat that couldn't be stored (overflow)
	 */
	public int addHeat(ItemStack stack, NuclearReactorBlockEntity reactor, int x, int y, int heat) {
		return heat; // Default: can't store heat
	}

	/**
	 * @return explosion influence (positive adds power, negative reduces by
	 * factor)
	 */
	public float getExplosionInfluence(ItemStack stack) {
		return 0;
	}

	// Durability via data components

	public int getMaxDurability() {
		return maxDurability;
	}

	public int getDurabilityUsed(ItemStack stack) {
		return stack.getOrDefault(TRDataComponentTypes.STORED_HEAT, 0);
	}

	public void setDurabilityUsed(ItemStack stack, int used) {
		stack.set(TRDataComponentTypes.STORED_HEAT, used);
	}

	public boolean consumeDurability(ItemStack stack, int amount) {
		int newUsed = getDurabilityUsed(stack) + amount;
		setDurabilityUsed(stack, newUsed);
		return newUsed >= maxDurability;
	}

	// Durability bar display

	@Override
	public boolean isBarVisible(ItemStack stack) {
		// Show bar only if item has been used (durability consumed)
		return maxDurability > 0 && getDurabilityUsed(stack) > 0;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		if (maxDurability <= 0) return 0;
		int remaining = maxDurability - getDurabilityUsed(stack);
		return Math.round((float) remaining / maxDurability * 13.0f);
	}

	@Override
	public int getBarColor(ItemStack stack) {
		// Green to red gradient based on remaining durability
		if (maxDurability <= 0) return 0x00FF00;
		float ratio = (float) (maxDurability - getDurabilityUsed(stack)) / maxDurability;
		int red = (int) ((1.0f - ratio) * 255);
		int green = (int) (ratio * 255);
		return (red << 16) | (green << 8);
	}

	// Tooltip

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, display, tooltip, flag);

		// Handle items with durability, including infinite
		if (maxDurability != 0) {
			int remaining = maxDurability - getDurabilityUsed(stack);
			tooltip.accept(
				Component.translatable("techreborn.tooltip.reactor.durability")
					.withStyle(ChatFormatting.GRAY)
					.append(": ")
					.append(remaining >= 0 ? Component.literal(remaining + " / " + maxDurability) : Component.translatable("techreborn.tooltip.reactor.infinite"))
					.withStyle(ChatFormatting.GOLD)
			);
		}

		if (canStoreHeat() && getCurrentHeat(stack) > 0) {
			tooltip.accept(Component.translatable("techreborn.tooltip.reactor.heat_warning").withStyle(ChatFormatting.GOLD));
		}
	}
}
