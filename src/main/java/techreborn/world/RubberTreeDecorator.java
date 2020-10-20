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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorContext;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// Big thanks to SuperCoder7979 for this
public class RubberTreeDecorator extends Decorator<ChanceDecoratorConfig> {

	public RubberTreeDecorator(Codec<ChanceDecoratorConfig> codec) {
		super(codec);
	}

	@Override
	public Stream<BlockPos> getPositions(DecoratorContext context, Random random, ChanceDecoratorConfig config, BlockPos pos) {
		// Generate tree clusters randomly
		if (random.nextInt(config.chance) == 0) {
			// Generate 4 - 8 trees
			int treeCount = 4 + random.nextInt(5);
			return IntStream.range(0, treeCount).mapToObj((i) -> {
				int x = random.nextInt(16) + pos.getX();
				int z = random.nextInt(16) + pos.getZ();
				int y = context.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);
				return new BlockPos(x, y, z);
			});
		}
		return Stream.empty();
	}
}