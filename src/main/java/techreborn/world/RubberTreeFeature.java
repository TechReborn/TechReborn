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
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

import java.util.Random;
import java.util.function.BiConsumer;

/**
 * @author drcrazy
 */
public class RubberTreeFeature extends TreeFeature {

	public RubberTreeFeature(Codec<TreeFeatureConfig> codec) {
		super(codec);
	}

	public static class FoliagePlacer extends BlobFoliagePlacer {
		public static final Codec<FoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> createCodec(instance)
				.and(
						Codec.INT.fieldOf("spireHeight").forGetter(FoliagePlacer::getSpireHeight)
				)
				.and(
						BlockState.CODEC.fieldOf("spireBlockState").forGetter(FoliagePlacer::getSpireBlockState)
				)
				.apply(instance, FoliagePlacer::new));

		private final int spireHeight;
		private final BlockState spireBlockState;

		public FoliagePlacer(IntProvider radius, IntProvider offset, int height, int spireHeight, BlockState spireBlockState) {
			super(radius, offset, height);
			this.spireHeight = spireHeight;
			this.spireBlockState = spireBlockState;
		}

		@Override
		protected void generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, TreeFeatureConfig config, int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, int offset) {
			super.generate(world, replacer, random, config, trunkHeight, treeNode, foliageHeight, radius, offset);

			spawnSpike(world, treeNode.getCenter(), replacer);
		}

		private void spawnSpike(TestableWorld world, BlockPos pos, BiConsumer<BlockPos, BlockState> replacer) {
			final int startScan = pos.getY();
			BlockPos topPos = null;

			//Limit the scan to 15 blocks
			while (topPos == null && pos.getY() - startScan < 15) {
				pos = pos.up();
				if (world.testBlockState(pos, BlockState::isAir)) {
					topPos = pos;
				}
			}

			if (topPos == null) return;

			for (int i = 0; i < spireHeight; i++) {
				replacer.accept(pos.up(i), spireBlockState);
			}
		}

		@Override
		protected FoliagePlacerType<?> getType() {
			return null;
		}

		public int getSpireHeight() {
			return spireHeight;
		}

		public BlockState getSpireBlockState() {
			return spireBlockState;
		}
	}
}
