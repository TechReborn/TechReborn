/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.init;

import net.minecraft.util.Identifier;

import techreborn.blocks.fluid.BlockFluidTechReborn;
public enum ModFluids {
	BERYLLIUM,
	CALCIUM,
	CALCIUM_CARBONATE,
	CHLORITE,
	DEUTERIUM,
	GLYCERYL,
	HELIUM,
	HELIUM_3,
	HELIUMPLASMA,
	HYDROGEN,
	LITHIUM,
	MERCURY,
	METHANE,
	NITROCOAL_FUEL,
	NITROFUEL,
	NITROGEN,
	NITROGENDIOXIDE,
	POTASSIUM,
	SILICON,
	SODIUM,
	SODIUMPERSULFATE,
	TRITIUM,
	WOLFRAMIUM,
	CARBON,
	CARBON_FIBER,
	NITRO_CARBON,
	SULFUR,
	SODIUM_SULFIDE,
	DIESEL,
	NITRO_DIESEL,
	OIL,
	SULFURIC_ACID,
	COMPRESSED_AIR,
	ELECTROLYZED_WATER;

	public Fluid fluid;
	public BlockFluidTechReborn block;
	public String name;

	ModFluids() {
		this.name = this.name().replace("_", "").toLowerCase(); //Is this ok?
		fluid = new Fluid(name, new Identifier("techreborn", "fixme"), new Identifier("techrebonr", "alsofixme"));
	}

	public Fluid getFluid() {
		return fluid;
	}

	public BlockFluidTechReborn getBlock() {
		return block;
	}

	public String getName() {
		return name;
	}
}
