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

package techreborn.client;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.api.blockentity.IMachineGuiHandler;

import java.util.Arrays;
import java.util.stream.Stream;

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
	DISTILLATION_TOWER(true),
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
	LESU(true),
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
	VACUUM_FREEZER(true),
	SOLID_CANNING_MACHINE(true),
	WIRE_MILL(true),
	FLUID_REPLICATOR(true);

	private final boolean containerBuilder;

	EGui(final boolean containerBuilder) {
		this.containerBuilder = containerBuilder;
	}

	public static EGui byID(Identifier resourceLocation){
		return Arrays.stream(values())
			.filter(eGui -> eGui.name().toLowerCase().equals(resourceLocation.getPath()))
			.findFirst()
			.orElseThrow(() -> new RuntimeException("Failed to find gui for " + resourceLocation));
	}

	public static Stream<EGui> stream(){
		return Arrays.stream(values());
	}

	public Identifier getID(){
		return new Identifier("techreborn", name().toLowerCase());
	}

	@Override
	public void open(PlayerEntity player, BlockPos pos, World world) {
		if(!world.isClient){
			ContainerProviderRegistry.INSTANCE.openContainer(getID(), player, packetByteBuf -> packetByteBuf.writeBlockPos(pos));
		}
	}

	public boolean useContainerBuilder() {
		return this.containerBuilder;
	}
}
