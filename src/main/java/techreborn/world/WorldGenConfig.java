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
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.List;

public class WorldGenConfig {
	public static final Codec<WorldGenConfig> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.list(TechRebornOre.CODEC).fieldOf("ores").forGetter(WorldGenConfig::getOres),
					ConfiguredFeature.field_25833.fieldOf("rubberTree").forGetter(WorldGenConfig::getRubberTree)
			)
			.apply(instance, WorldGenConfig::new)
	);

	private final List<TechRebornOre> ores;
	private final ConfiguredFeature<?, ?> rubberTree;

	public WorldGenConfig(List<TechRebornOre> ores, ConfiguredFeature<?, ?> rubberTree) {
		this.ores = ores;
		this.rubberTree = rubberTree;
	}

	public ConfiguredFeature<?, ?> getRubberTree() {
		return rubberTree;
	}

	public List<TechRebornOre> getOres() {
		return ores;
	}
}
