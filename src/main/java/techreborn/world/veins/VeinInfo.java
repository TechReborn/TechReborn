/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 TechReborn
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

package techreborn.world.veins;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;

import java.util.Map;
import java.util.Random;

public class VeinInfo {

	private final float minSize, maxSize;
	private final int minHeight, maxHeight;
	private final int chance;

	private final ImmutableMap<Integer, IBlockState> veinBlocks;

	public VeinInfo(float minSize, float maxSize, int minHeight, int maxHeight, int chance, Map<Integer, IBlockState> veinBlocks) {
		this.minSize = minSize;
		this.maxSize = maxSize;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		this.chance = chance;
		this.veinBlocks = ImmutableMap.copyOf(veinBlocks);
	}

	public float getMinSize() {
		return minSize;
	}

	public float getMaxSize() {
		return maxSize;
	}

	public float getRandomSize(Random random) {
		return minSize + random.nextFloat() * maxSize;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public int getRandomY(Random random, int boxHeight, int groundHeight) {
		int maxValue = groundHeight > maxHeight ? maxHeight : groundHeight;
		int randomY = (int) (minHeight + random.nextFloat() * maxValue);
		if (randomY + boxHeight > maxValue) {
			return randomY - (boxHeight - (maxValue - randomY));
		} else if (randomY - boxHeight < minHeight) {
			return randomY + (boxHeight - (randomY - minHeight));
		}
		return randomY;
	}

	public int getChance() {
		return chance;
	}

	public boolean shouldGenerate(Random random) {
		return chance >= random.nextInt(100);
	}

	public ImmutableMap<Integer, IBlockState> getVeinBlocks() {
		return veinBlocks;
	}

}
