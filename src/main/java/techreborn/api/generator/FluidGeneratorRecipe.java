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

package techreborn.api.generator;


import net.minecraft.fluid.Fluid;
import techreborn.utils.FluidUtils;

public class FluidGeneratorRecipe {
	private final EFluidGenerator generatorType;
	private final Fluid fluid;
	private final int energyPerMb;

	public FluidGeneratorRecipe(Fluid fluid, int energyPerMb, EFluidGenerator generatorType) {
		this.fluid = fluid;
		this.energyPerMb = energyPerMb;
		this.generatorType = generatorType;
	}

	public Fluid getFluid() {
		return fluid;
	}

	public int getEnergyPerBucket() {
		return energyPerMb * 1000;
	}

	public EFluidGenerator getGeneratorType() {
		return generatorType;
	}

	@Override
	public String toString() {
		return "FluidGeneratorRecipe [generatorType=" + generatorType + ", fluid=" + fluid + ", energyPerMb="
				+ energyPerMb + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + energyPerMb;
		result = prime * result + ((fluid == null) ? 0 : fluid.hashCode());
		result = prime * result + ((generatorType == null) ? 0 : generatorType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FluidGeneratorRecipe other = (FluidGeneratorRecipe) obj;
		if (energyPerMb != other.energyPerMb)
			return false;
		if (fluid == null) {
			if (other.fluid != null)
				return false;
		} else if (!FluidUtils.fluidEquals(other.fluid, fluid))
			return false;
		return generatorType == other.generatorType;
	}
}
