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

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.feature.BranchedTreeFeatureConfig;
import net.minecraft.world.gen.feature.OakTreeFeature;
import techreborn.config.TechRebornConfig;
import techreborn.init.TRContent;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

/**
 * @author drcrazy
 *
 */
public class RubberTreeFeature extends OakTreeFeature {

	public RubberTreeFeature(Function<Dynamic<?>, ? extends BranchedTreeFeatureConfig> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean generate(ModifiableTestableWorld world, Random random, BlockPos blockPos, Set<BlockPos> set, Set<BlockPos> set2, BlockBox blockBox, BranchedTreeFeatureConfig branchedTreeFeatureConfig) {
		if(super.generate(world, random, blockPos, set, set2, blockBox, branchedTreeFeatureConfig)) {
			spawnSpike(world, blockPos);
			return true;
		}
		return false;
	}

	private void spawnSpike(ModifiableTestableWorld world, BlockPos pos) {
		final int startScan = pos.getY();
		BlockPos topPos = null;

		//Limit the scan to 15 blocks
		while(topPos == null && pos.getY() - startScan < 15) {
			pos = pos.up();
			if(world.testBlockState(pos, BlockState::isAir)) {
				topPos = pos;
			}
		}

		if(topPos == null) return;

		for (int i = 0; i < TechRebornConfig.RubberTreeSpireHeight; i++) {
			setBlockState(world, pos.up(i), TRContent.RUBBER_LEAVES.getDefaultState());
		}
	}
}
