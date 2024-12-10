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

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.YOffset;

import java.util.Objects;
import java.util.function.Supplier;

import static techreborn.config.TechRebornConfig.*;
import static techreborn.world.TargetDimension.*;

import java.util.function.Predicate;

public enum OreDistribution {
	BAUXITE(6, 12, YOffset.aboveBottom(0), 20, OVERWORLD, () -> enableBauxiteOreGeneration),
	CINNABAR(6, 5, YOffset.aboveBottom(0), 100, NETHER, () -> enableCinnabarOreGeneration),
	GALENA(8, 12, YOffset.aboveBottom(25), 40, OVERWORLD, () -> enableGalenaOreGeneration),
	IRIDIUM(3, 4, YOffset.aboveBottom(0), 0, OVERWORLD, () -> enableIridiumOreGeneration),
	LEAD(6, 16, YOffset.aboveBottom(40), 40, OVERWORLD, () -> enableLeadOreGeneration),

	PERIDOT_END(6, 6, YOffset.aboveBottom(0), 360, END, UniformIntProvider.create(2, 6), () -> (enablePeridotOreGeneration && enableOresInEnd)),
	PERIDOT_NETHER(7, 4, YOffset.aboveBottom(3), 40, NETHER, UniformIntProvider.create(2, 6), () -> (enablePeridotOreGeneration && !enableOresInEnd)),

	PYRITE(6, 6, YOffset.aboveBottom(80), 128, NETHER, () -> enablePyriteOreGeneration),
	RUBY(6, 8, YOffset.fixed(20), 110, OVERWORLD, UniformIntProvider.create(2, 6), () -> enableRubyOreGeneration),
	SAPPHIRE(6, 7, YOffset.fixed(40), 110, OVERWORLD, UniformIntProvider.create(2, 6), () -> enableSapphireOreGeneration),
	SILVER(6, 16, YOffset.aboveBottom(40), 60, OVERWORLD, () -> enableSilverOreGeneration),

	SPHALERITE(6, 4, YOffset.aboveBottom(40), 90, NETHER, () -> enableSphaleriteOreGeneration),
	TIN(8, 16, YOffset.fixed(25), 80, OVERWORLD, () -> enableTinOreGeneration),

	TUNGSTEN_END(6, 3, YOffset.aboveBottom(0), 360, END, () -> (enableTungstenOreGeneration && enableOresInEnd)),
	TUNGSTEN_NETHER(4, 10, YOffset.aboveBottom(7), 50, NETHER, () -> (enableTungstenOreGeneration && !enableOresInEnd)), // why this is always false if ore gen is true and enableOresInEnd is false?

	NICKEL(7, 10, YOffset.fixed(110), 200, OVERWORLD, () -> enableNickelOreGeneration),

	SODALITE_END(6, 4, YOffset.aboveBottom(0), 360, END, () -> (enableSodaliteOreGeneration && enableOresInEnd)),
	SODALITE_OVERWORLD(5, 7, YOffset.aboveBottom(5), -15, OVERWORLD, () -> (enableSodaliteOreGeneration && !enableOresInEnd)),

	SHELDONITE_END(6, 4, YOffset.aboveBottom(0), 360, END, () -> (enableSheldoniteOreGeneration && enableOresInEnd)),
	SHELDONITE_NETHER(4, 9, YOffset.belowTop(45), 300, NETHER, () -> (enableSheldoniteOreGeneration && !enableOresInEnd)),
	DUMMY_NONE(4, 9, YOffset.belowTop(45), 300, OVERWORLD, () -> false);

	static {
		TUNGSTEN_NETHER.biomeSelector = BiomeSelectors.includeByKey(BiomeKeys.BASALT_DELTAS);
		// next line wont work in nether because of block replacements. Do later.
		// LEAD.biomeSelector = LEAD.dimension.biomeSelector.or(BiomeSelectors.includeByKey(BiomeKeys.BASALT_DELTAS));
	}

	public final int veinSize;
	public final int veinsPerChunk;
	public final YOffset minOffset;
	public final int maxY; // Max height of ore in numbers of blocks from the bottom of the world
	public final UniformIntProvider experienceDropped;
	public final TargetDimension dimension;
	private final Supplier<Boolean> generating;
	private Predicate<BiomeSelectionContext> biomeSelector = null;

	OreDistribution(int veinSize, int veinsPerChunk, YOffset minOffset, int maxY, TargetDimension dimension, UniformIntProvider experienceDropped, Supplier<Boolean> generating) {
		this.veinSize = veinSize;
		this.veinsPerChunk = veinsPerChunk;
		this.minOffset = minOffset;
		this.maxY = maxY;
		this.experienceDropped = Objects.requireNonNullElse(experienceDropped, UniformIntProvider.create(0, 0));
		this.dimension = dimension;
		this.generating = generating;
	}

	OreDistribution(int veinSize, int veinsPerChunk, YOffset minOffset, int maxY, TargetDimension dimension, Supplier<Boolean> generating) {
		this(veinSize, veinsPerChunk, minOffset, maxY, dimension, null, generating);
	}

	public Supplier<Boolean> isGenerating() {
		return generating;
	}

	public Predicate<BiomeSelectionContext> GetBiomeSelector() {
		if (biomeSelector == null)
			return this.dimension.biomeSelector;
		else
			return biomeSelector;
	}
}
