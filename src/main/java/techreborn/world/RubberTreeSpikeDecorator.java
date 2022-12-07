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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

import java.util.Comparator;

public class RubberTreeSpikeDecorator  extends TreeDecorator {
	public static final Codec<RubberTreeSpikeDecorator> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			Codec.INT.fieldOf("spire_height").forGetter(RubberTreeSpikeDecorator::getSpireHeight),
			BlockStateProvider.TYPE_CODEC.fieldOf("provider").forGetter(RubberTreeSpikeDecorator::getProvider)
		).apply(instance, RubberTreeSpikeDecorator::new)
	);

	private final int spireHeight;
	private final BlockStateProvider provider;

	public RubberTreeSpikeDecorator(int spireHeight, BlockStateProvider spireBlockState) {
		this.spireHeight = spireHeight;
		this.provider = spireBlockState;
	}

	@Override
	protected TreeDecoratorType<?> getType() {
		return WorldGenerator.RUBBER_TREE_SPIKE;
	}

	@Override
	public void generate(Generator generator) {
		generator.getLogPositions().stream()
			.max(Comparator.comparingInt(BlockPos::getY))
			.ifPresent(blockPos -> {
				for (int i = 0; i < spireHeight; i++) {
					BlockPos sPos = blockPos.up(i);
					generator.replace(sPos, provider.get(generator.getRandom(), sPos));
				}
			});
	}

	public int getSpireHeight() {
		return spireHeight;
	}

	public BlockStateProvider getProvider() {
		return provider;
	}
}
