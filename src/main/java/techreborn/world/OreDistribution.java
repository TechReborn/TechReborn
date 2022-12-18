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

import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.YOffset;
import org.jetbrains.annotations.NotNull;
import techreborn.config.TechRebornConfig;

import java.util.Objects;

public enum OreDistribution {
	BAUXITE(6, 12, YOffset.aboveBottom(0), 20, TargetDimension.OVERWORLD, TechRebornConfig.enableBauxiteOreGeneration),
	CINNABAR(6, 5, YOffset.aboveBottom(0), 128, TargetDimension.NETHER, TechRebornConfig.enableCinnabarOreGeneration),
	GALENA(8, 12, YOffset.aboveBottom(25), 40, TargetDimension.OVERWORLD, TechRebornConfig.enableGalenaOreGeneration),
	IRIDIUM(3, 4, YOffset.aboveBottom(0), 0, TargetDimension.OVERWORLD, TechRebornConfig.enableIridiumOreGeneration),
	LEAD(6, 16, YOffset.aboveBottom(40), 40, TargetDimension.OVERWORLD, TechRebornConfig.enableLeadOreGeneration),
	PERIDOT(6, 6, YOffset.aboveBottom(0), 360, TargetDimension.END, UniformIntProvider.create(2,6), TechRebornConfig.enablePeridotOreGeneration),
	PYRITE(6, 6, YOffset.aboveBottom(0), 128, TargetDimension.NETHER, TechRebornConfig.enablePyriteOreGeneration),
	RUBY(6, 8, YOffset.fixed(20), 120, TargetDimension.OVERWORLD, UniformIntProvider.create(2,6), TechRebornConfig.enableRubyOreGeneration),
	SAPPHIRE(6, 7, YOffset.fixed(20), 120, TargetDimension.OVERWORLD, UniformIntProvider.create(2,6), TechRebornConfig.enableSapphireOreGeneration),
	SHELDONITE(6, 4, YOffset.aboveBottom(0), 360, TargetDimension.END, TechRebornConfig.enableSheldoniteOreGeneration),
	SILVER(6, 16, YOffset.aboveBottom(40), 60,TargetDimension.OVERWORLD, TechRebornConfig.enableSilverOreGeneration),
	SODALITE(6, 4, YOffset.aboveBottom(0), 360, TargetDimension.END, TechRebornConfig.enableSodaliteOreGeneration),
	SPHALERITE(6, 4, YOffset.aboveBottom(0), 128, TargetDimension.NETHER, TechRebornConfig.enableSphaleriteOreGeneration),
	TIN(8, 16, YOffset.fixed(25), 80, TargetDimension.OVERWORLD, TechRebornConfig.enableTinOreGeneration),
	TUNGSTEN(6, 3, YOffset.aboveBottom(0), 360, TargetDimension.END, TechRebornConfig.enableTungstenOreGeneration);

	public final int veinSize;
	public final int veinsPerChunk;
	public final YOffset minOffset;
	public final int maxY; // Max height of ore in numbers of blocks from the bottom of the world
	public @NotNull final UniformIntProvider experienceDropped;
	public final TargetDimension dimension;
	private final boolean generating;

	OreDistribution(int veinSize, int veinsPerChunk, YOffset minOffset, int maxY, TargetDimension dimension, UniformIntProvider experienceDropped, boolean generating) {
		this.veinSize = veinSize;
		this.veinsPerChunk = veinsPerChunk;
		this.minOffset = minOffset;
		this.maxY = maxY;
		this.experienceDropped = Objects.requireNonNullElse(experienceDropped, UniformIntProvider.create(0,0));
		this.dimension = dimension;
		this.generating = generating;
	}

	OreDistribution(int veinSize, int veinsPerChunk, YOffset minOffset, int maxY, TargetDimension dimension, boolean generating) {
		this(veinSize, veinsPerChunk, minOffset, maxY, dimension, null, generating);
	}

	public boolean isGenerating() {
		return generating;
	}
}
