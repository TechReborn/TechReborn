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

package techreborn.enet;

import net.minecraft.util.math.Direction;
import reborncore.common.powerSystem.PowerAcceptorBlockEntity;

public class PowerAcceptorBlockEntityFace {
	private final PowerAcceptorBlockEntity entity;
	private final Direction face;

	public PowerAcceptorBlockEntityFace(PowerAcceptorBlockEntity entity, Direction face) {
		this.entity = entity;
		this.face = face;
	}

	public boolean canAcceptEnergy() {
		return entity.canAcceptEnergy(face);
	}

	public boolean canProvideEnergy() {
		return entity.canProvideEnergy(face);
	}

	public double getEnergy() {
		return entity.getEnergy();
	}

	public double getFreeSpace() { return entity.getFreeSpace(); }

	public boolean hasFreeSpace() { return getFreeSpace() > 0; }

	public double useEnergy(double amount) { return entity.useEnergy(amount); }

	public double addEnergy(double amount) { return entity.addEnergy(amount); }

	public boolean isSameEntity(PowerAcceptorBlockEntityFace other) { return entity == other.entity; }
}
