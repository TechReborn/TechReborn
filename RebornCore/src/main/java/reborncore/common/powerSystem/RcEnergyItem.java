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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import reborncore.common.util.ItemUtils;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleBatteryItem;

/**
 * Implement on simple energy-containing items and (on top of what {@link SimpleBatteryItem} does):
 * <ul>
 *     <li>A tooltip will be added for the item, indicating the stored power,
 *     the max power and the extraction rates.</li>
 *     <li>Any {@link RcEnergyItem} input in a crafting recipe input will automatically
 *     give its energy to the output if the output implements {@link RcEnergyItem}.</li>
 * </ul>
 * TODO: consider moving this functionality to the energy API?
 */
public interface RcEnergyItem extends SimpleBatteryItem, FabricItem {
	long getEnergyCapacity();

	/**
	 * @return {@link RcEnergyTier} the tier of this {@link EnergyStorage}, used to have standard I/O rates.
	 */
	RcEnergyTier getTier();

	default long getEnergyMaxInput() {
		return getTier().getMaxInput();
	}

	default long getEnergyMaxOutput() {
		return getTier().getMaxOutput();
	}

	@Override
	default boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
		return !ItemUtils.isEqualIgnoreEnergy(oldStack, newStack);
	}

	@Override
	default boolean allowContinuingBlockBreaking(PlayerEntity player, ItemStack oldStack, ItemStack newStack) {
		return ItemUtils.isEqualIgnoreEnergy(oldStack, newStack);
	}
}
