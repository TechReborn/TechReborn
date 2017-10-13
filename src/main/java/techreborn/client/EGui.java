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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.api.tile.IMachineGuiHandler;
import techreborn.Core;

public enum EGui implements IMachineGuiHandler {

	AESU(true),
	ALLOY_FURNACE(true),
	ALLOY_SMELTER(true),
	ASSEMBLING_MACHINE(true),
	AUTO_CRAFTING_TABLE(true),
	BLAST_FURNACE(true),
	CENTRIFUGE(true),
	CHARGEBENCH(true),
	CHEMICAL_REACTOR(true),
	CHUNK_LOADER(true),
	COMPRESSOR(true),
	DESTRUCTOPACK(false),
	DIESEL_GENERATOR(true),
	DIGITAL_CHEST(true),
	ELECTRIC_FURNACE(true),
	EXTRACTOR(true),
	FUSION_CONTROLLER(true),
	GAS_TURBINE(true),
	GENERATOR(true),
	GRINDER(true),
	HIGH_VOLTAGE_SU(true),
	IDSU(true),
	IMPLOSION_COMPRESSOR(true),
	INDUSTRIAL_ELECTROLYZER(true),
	INDUSTRIAL_GRINDER(true),
	IRON_FURNACE(true),
	LESU(false),
	LOW_VOLTAGE_SU(true),
	MANUAL(false),
	MATTER_FABRICATOR(true),
	MEDIUM_VOLTAGE_SU(true),
	PLASMA_GENERATOR(true),
	QUANTUM_CHEST(true),
	QUANTUM_TANK(true),
	RECYCLER(true),
	ROLLING_MACHINE(true),
	SAWMILL(true),
	SCRAPBOXINATOR(true),
	SEMIFLUID_GENERATOR(true),
	THERMAL_GENERATOR(true),
	VACUUM_FREEZER(true);

	private final boolean containerBuilder;

	private EGui(final boolean containerBuilder) {
		this.containerBuilder = containerBuilder;
	}

	@Override
	public void open(EntityPlayer player, BlockPos pos, World world) {
		if(!world.isRemote){
			player.openGui(Core.INSTANCE, this.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());
		}
	}

	public boolean useContainerBuilder() {
		return this.containerBuilder;
	}
}
