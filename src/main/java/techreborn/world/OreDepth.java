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

package techreborn.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.gen.HeightContext;
import techreborn.init.TRContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Client synced DTO for ore depth
 */
public record OreDepth(Identifier identifier, int minY, int maxY, TargetDimension dimension) {
	public static final Codec<OreDepth> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			Identifier.CODEC.fieldOf("identifier").forGetter(OreDepth::identifier),
			Codec.INT.fieldOf("minY").forGetter(OreDepth::minY),
			Codec.INT.fieldOf("maxY").forGetter(OreDepth::maxY),
			TargetDimension.CODEC.fieldOf("dimension").forGetter(OreDepth::dimension)
		).apply(instance, OreDepth::new)
	);
	public static Codec<List<OreDepth>> LIST_CODEC = Codec.list(OreDepth.CODEC);

	public static List<OreDepth> create(MinecraftServer server) {

		final List<OreDepth> depths = new ArrayList<>();

		for (TRContent.Ores ore : TRContent.Ores.values()) {
			if (ore.isDeepslate()) continue;

			if (ore.distribution != null) {
				final Identifier blockId = Registry.BLOCK.getId(ore.block);
				final HeightContext heightContext = getHeightContext(server, ore.distribution.dimension);

				final int minY = ore.distribution.minOffset.getY(heightContext);
				final int maxY = ore.distribution.maxY;

				depths.add(new OreDepth(blockId, minY, maxY, ore.distribution.dimension));

				TRContent.Ores deepslate = ore.getDeepslate();
				if (deepslate == null) continue;

				final Identifier deepSlateBlockId = Registry.BLOCK.getId(deepslate.block);
				depths.add(new OreDepth(deepSlateBlockId, minY, maxY, ore.distribution.dimension));
			}
		}

		return Collections.unmodifiableList(depths);
	}

	private static HeightContext getHeightContext(MinecraftServer server, TargetDimension dimension) {
		RegistryKey<World> key = switch (dimension) {
			case OVERWORLD -> World.OVERWORLD;
			case NETHER ->  World.NETHER;
			case END ->  World.END;
		};

		final ServerWorld world = server.getWorld(key);
		return new HeightContext(world.getChunkManager().getChunkGenerator(), world);
	}
}
