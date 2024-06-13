/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TeamReborn
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

package reborncore.common.powerSystem;

import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;
import reborncore.common.util.ItemUtils;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyItem;


/**
 * Implement on simple energy-containing items and (on top of what {@link SimpleEnergyItem} does):
 * <ul>
 *     <li>A tooltip will be added for the item, indicating the stored power,
 *     the max power and the extraction rates.</li>
 *     <li>Any {@link RcEnergyItem} input in a crafting recipe input will automatically
 *     give its energy to the output if the output implements {@link RcEnergyItem}.</li>
 * </ul>
 * TODO: consider moving this functionality to the energy API?
 */
public interface RcEnergyItem extends SimpleEnergyItem, FabricItem {
	long getEnergyCapacity(ItemStack stack);

	/**
	 * @return {@link RcEnergyTier} the tier of this {@link EnergyStorage}, used to have standard I/O rates.
	 */
	RcEnergyTier getTier();

	default long getEnergyMaxInput(ItemStack stack) {
		return getTier().getMaxInput();
	}

	default long getEnergyMaxOutput(ItemStack stack) {
		return getTier().getMaxOutput();
	}

	@Override
	default boolean allowComponentsUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
		return !ItemUtils.isEqualIgnoreEnergy(oldStack, newStack);
	}

	@Override
	default boolean allowContinuingBlockBreaking(PlayerEntity player, ItemStack oldStack, ItemStack newStack) {
		return ItemUtils.isEqualIgnoreEnergy(oldStack, newStack);
	}

	/**
	 * Tries to use energy with honor to Unbreaking enchantment
	 *
	 * @param stack ItemStack representing item with Energy
	 * @param amount Initial amount of energy to use. This will be further decreased according to level of Unbreaking
	 *               enchantment
	 * @return Returns true if was able to use that energy from that item
	 */
	@Override
	default boolean tryUseEnergy(ItemStack stack, long amount){
		Random random = Random.create();

		int unbreakingLevel = getUnbreakingLevel(stack);
		if (unbreakingLevel > 0) {
			amount = amount / (random.nextInt(unbreakingLevel) + 1);
		}
		return SimpleEnergyItem.super.tryUseEnergy(stack, amount);
	}

	// A hack to do this without context of the DRM
	private int getUnbreakingLevel(ItemStack stack) {
		ItemEnchantmentsComponent enchantments = stack.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
		for (RegistryEntry<Enchantment> entry : enchantments.getEnchantments()) {
			if (entry.getKey().equals(Enchantments.UNBREAKING)) {
				return enchantments.getLevel(entry);
			}
		}
		return 0;
	}
}
