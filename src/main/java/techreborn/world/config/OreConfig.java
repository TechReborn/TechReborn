/*
 * This file is part of TechReborn, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 TechReborn
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

package techreborn.world.config;

import net.minecraft.block.state.IBlockState;

/**
 * Created by modmuss50 on 11/03/2016.
 */
public class OreConfig {

	public String blockName;

	public String blockNiceName;

	public int meta;

	// This doesn't get written to the json file
	public transient IBlockState state;

	public int veinSize;

	public int veinsPerChunk;

	public int minYHeight;

	public int maxYHeight;

	public boolean shouldSpawn = true;

	public OreConfig(IBlockState blockSate, int veinSize, int veinsPerChunk, int minYHeight, int maxYHeight) {
		this.meta = blockSate.getBlock().getMetaFromState(blockSate);
		this.state = blockSate;
		this.blockName = blockSate.getBlock().getTranslationKey();
		if (blockSate.getBlock() instanceof IOreNameProvider) {
			this.blockNiceName = ((IOreNameProvider) blockSate.getBlock()).getUserLoclisedName(blockSate);
		} else {
			this.blockNiceName = "unknown";
		}
		this.veinSize = veinSize;
		this.veinsPerChunk = veinsPerChunk;
		this.minYHeight = minYHeight;
		this.maxYHeight = maxYHeight;
	}

	public OreConfig() {
	}
}
