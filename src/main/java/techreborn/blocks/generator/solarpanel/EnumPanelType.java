/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.blocks.generator.solarpanel;

import net.minecraft.util.IStringSerializable;
import reborncore.api.power.EnumPowerTier;

public enum EnumPanelType implements IStringSerializable {
	Basic("basic", 1, 0, EnumPowerTier.MICRO),
	Hybrid("hybrid", 16, 0, EnumPowerTier.LOW),
	Advanced("advanced", 64, 2,  EnumPowerTier.MEDIUM),
	Ultimate("ultimate", 256, 16, EnumPowerTier.HIGH),
	Quantum("quantum", 1024, 64, EnumPowerTier.EXTREME);

	private String friendlyName;
	// Generation of EU during Day
	public int generationRateD = 10;
	// Generation of EU during Night
	public int generationRateN = 0;
	// Internal EU storage of solar panel
	public int internalCapacity = 1000;
	public EnumPowerTier powerTier;


	EnumPanelType(String friendlyName, int generationRateD, int generationRateN,  EnumPowerTier tier) {
		this.friendlyName = friendlyName;
		this.generationRateD = generationRateD;
		this.generationRateN = generationRateN;
		this.internalCapacity = (generationRateD * 1000);
		this.powerTier = tier;
	}

	@Override
	public String getName() {
		return friendlyName;
	}
}