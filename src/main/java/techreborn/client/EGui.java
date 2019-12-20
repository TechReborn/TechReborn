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
import java.util.Locale;
import java.util.stream.Stream;

public enum EGui implements IMachineGuiHandler {

	AESU,
	ALLOY_FURNACE,
	ALLOY_SMELTER,
	ASSEMBLING_MACHINE,
	AUTO_CRAFTING_TABLE,
	BLAST_FURNACE,
	CENTRIFUGE,
	CHARGEBENCH,
	CHEMICAL_REACTOR,
	CHUNK_LOADER,
	COMPRESSOR,
	DIESEL_GENERATOR,
	DIGITAL_CHEST,
	DISTILLATION_TOWER,
	ELECTRIC_FURNACE,
	EXTRACTOR,
	FUSION_CONTROLLER,
	GAS_TURBINE,
	GENERATOR,
	HIGH_VOLTAGE_SU,
	IDSU,
	IMPLOSION_COMPRESSOR,
	INDUSTRIAL_ELECTROLYZER,
	INDUSTRIAL_GRINDER,
	IRON_FURNACE,
	LESU,
	LOW_VOLTAGE_SU,
	MANUAL(false),
	MATTER_FABRICATOR,
	MEDIUM_VOLTAGE_SU,
	PLASMA_GENERATOR,
	QUANTUM_CHEST,
	QUANTUM_TANK,
	RECYCLER,
	ROLLING_MACHINE,
	SAWMILL,
	SCRAPBOXINATOR,
	SOLAR_PANEL,
	SEMIFLUID_GENERATOR,
	THERMAL_GENERATOR,
	VACUUM_FREEZER,
	SOLID_CANNING_MACHINE,
	WIRE_MILL,
	FLUID_REPLICATOR,

	DATA_DRIVEN;

	private final boolean containerBuilder;

	EGui(final boolean containerBuilder) {
		this.containerBuilder = containerBuilder;
	}

	EGui(){
		this(true);
	}

	public static EGui byID(Identifier resourceLocation){
		return Arrays.stream(values())
			.filter(eGui -> eGui.name().toLowerCase(Locale.ROOT).equals(resourceLocation.getPath()))
			.findFirst()
			.orElseThrow(() -> new RuntimeException("Failed to find gui for " + resourceLocation));
	}

	public static Stream<EGui> stream(){
		return Arrays.stream(values());
	}

	public Identifier getID(){
		return new Identifier("techreborn", name().toLowerCase(Locale.ROOT));
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
