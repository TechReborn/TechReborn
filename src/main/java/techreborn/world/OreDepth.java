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

import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import org.jetbrains.annotations.Nullable;
import techreborn.init.TRContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Client synced DTO for ore depth
 */
public record OreDepth(Identifier identifier, int minY, int maxY, TargetDimension dimension) {
	public static final StreamCodec<ByteBuf, OreDepth> PACKET_CODEC = StreamCodec.composite(
		Identifier.STREAM_CODEC, OreDepth::identifier,
		ByteBufCodecs.INT, OreDepth::minY,
		ByteBufCodecs.INT, OreDepth::maxY,
		TargetDimension.PACKET_CODEC, OreDepth::dimension,
		OreDepth::new
	);

	public static List<OreDepth> create(MinecraftServer server) {

		final List<OreDepth> depths = new ArrayList<>();

		for (TRContent.Ores ore : TRContent.Ores.values()) {
			if (ore.isDeepslate()) continue;

			if (ore.distribution != null) {
				final Identifier blockId = BuiltInRegistries.BLOCK.getKey(ore.block);
				final WorldGenerationContext heightContext = getHeightContext(server, ore.distribution.dimension);

				if (heightContext == null) {
					continue;
				}

				final int minY = ore.distribution.minOffset.resolveY(heightContext);
				final int maxY = ore.distribution.maxY;

				depths.add(new OreDepth(blockId, minY, maxY, ore.distribution.dimension));

				TRContent.Ores deepslate = ore.getDeepslate();
				if (deepslate == null) continue;

				final Identifier deepSlateBlockId = BuiltInRegistries.BLOCK.getKey(deepslate.block);
				depths.add(new OreDepth(deepSlateBlockId, minY, maxY, ore.distribution.dimension));
			}
		}

		return Collections.unmodifiableList(depths);
	}

	@Nullable
	private static WorldGenerationContext getHeightContext(MinecraftServer server, TargetDimension dimension) {
		ResourceKey<Level> key = switch (dimension) {
			case OVERWORLD -> Level.OVERWORLD;
			case NETHER ->  Level.NETHER;
			case END ->  Level.END;
		};

		final ServerLevel world = server.getLevel(key);

		if (world == null) {
			return null;
		}

		return new WorldGenerationContext(world.getChunkSource().getGenerator(), world);
	}
}
