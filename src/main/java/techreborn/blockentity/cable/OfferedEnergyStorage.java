/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 TechReborn
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

package techreborn.blockentity.cable;

import net.minecraft.util.math.Direction;
import team.reborn.energy.api.EnergyStorage;

/**
 * {@link EnergyStorage} adjacent to an energy cable, with some additional info.
 */
class OfferedEnergyStorage {
	final CableBlockEntity sourceCable;
	final Direction direction;
	final EnergyStorage storage;

	OfferedEnergyStorage(CableBlockEntity sourceCable, Direction direction, EnergyStorage storage) {
		this.sourceCable = sourceCable;
		this.direction = direction;
		this.storage = storage;
	}

	void afterTransfer() {
		// Block insertions from this side.
		sourceCable.blockedSides |= 1 << direction.ordinal();
	}
}
