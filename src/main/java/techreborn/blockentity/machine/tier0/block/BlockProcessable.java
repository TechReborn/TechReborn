/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2022 TechReborn
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

package techreborn.blockentity.machine.tier0.block;

import reborncore.api.blockentity.InventoryProvider;
import reborncore.common.blockentity.RedstoneConfigurable;
import reborncore.common.recipes.IUpgradeHandler;

/**
 * <b>Aggregated interface for use with BlockEntities that have a processor attached</b>
 * <br>
 * Example of a processor {@link techreborn.blockentity.machine.tier0.block.blockplacer.BlockPlacerProcessor}
 *
 * @author SimonFlapse
 */
public interface BlockProcessable extends IUpgradeHandler, RedstoneConfigurable, InventoryProvider {
	/**
	 * <b>Consume some amount of Energy</b>
	 * <br>
	 * Must only return true if it successfully consumed energy.
	 * Must not consume energy if only partial fulfilled
	 *
	 * @param amount {@code int} amount of energy to consume
	 * @return if the energy could be consumed
	 */
	boolean consumeEnergy(int amount);

	/**
	 * <b>Play a sound to the Minecraft world</b>
	 *
	 * @see #canPlaySound()
	 */
	void playSound();

	/**
	 * <b>If a sound can be played.</b>
	 *
	 * @see #playSound()
	 */
	default boolean canPlaySound() {
		return true;
	}
}
