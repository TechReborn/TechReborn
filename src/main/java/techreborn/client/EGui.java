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

package techreborn.client;

public enum EGui {

	THERMAL_GENERATOR(true),
	QUANTUM_TANK(true),
	QUANTUM_CHEST(true),
	CENTRIFUGE(true),
	ROLLING_MACHINE(true),
	BLAST_FURNACE(true),
	ALLOY_SMELTER(true),
	INDUSTRIAL_GRINDER(true),
	IMPLOSION_COMPRESSOR(true),
	MATTER_FABRICATOR(true),
	MANUAL(false),
	CHUNK_LOADER(true),
	ASSEMBLING_MACHINE(true),
	DIESEL_GENERATOR(true),
	INDUSTRIAL_ELECTROLYZER(true),
	AESU(false),
	ALLOY_FURNACE(true),
	SAWMILL(true),
	CHEMICAL_REACTOR(true),
	SEMIFLUID_GENERATOR(true),
	GAS_TURBINE(true),
	DIGITAL_CHEST(true),
	DESTRUCTOPACK(false),
	LESU(false),
	IDSU(false),
	CHARGEBENCH(true),
	FUSION_CONTROLLER(true),
	VACUUM_FREEZER(true),
	GRINDER(true),
	GENERATOR(true),
	EXTRACTOR(true),
	COMPRESSOR(true),
	ELECTRIC_FURNACE(true),
	IRON_FURNACE(true),
	RECYCLER(true),
	SCRAPBOXINATOR(true),
	BATBOX(true),
	MFSU(true),
	MFE(true);

	private final boolean containerBuilder;

	private EGui(final boolean containerBuilder)
	{
		this.containerBuilder = containerBuilder;
	}

	public boolean useContainerBuilder() {
		return this.containerBuilder;
	}
}
