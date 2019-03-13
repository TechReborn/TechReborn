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

package techreborn.world.feature;

import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import techreborn.init.TRContent;

/**
 * @author drcrazy
 *
 */
public class RubberTreeFeature extends AbstractTreeFeature<NoFeatureConfig> {
	
	//TODO: Configs
	private int treeBaseHeight = 5;

	public RubberTreeFeature() {
		super(false);
	}

	@Override
	protected boolean place(Set<BlockPos> changedBlocks, IWorld worldIn, Random rand, BlockPos saplingPos) {
		int treeHeight = rand.nextInt(5) + treeBaseHeight;
		int worldHeight = worldIn.getWorld().getHeight();
		
		int baseY = saplingPos.getY();
		int baseX = saplingPos.getX();
		int baseZ = saplingPos.getZ();
		if (baseY <= 1 && baseY + treeHeight + 1 >= worldHeight) { return false; }
		
		boolean isSoil = worldIn.getBlockState(saplingPos.down()).canSustainPlant(worldIn, saplingPos.down(),
				net.minecraft.util.EnumFacing.UP,
				(net.minecraft.block.BlockSapling) TRContent.RUBBER_SAPLING.getBlock());
		if (!isSoil) { return false; }
		
		int yOffset;
		int xOffset;
		int zOffset;
		for (yOffset = baseY; yOffset <= baseY + 1 + treeHeight; ++yOffset) {
			byte radius = 1;
			if (yOffset == baseY) {
				radius = 0;
			}
			if (yOffset >= baseY + 1 + treeHeight - 2) {
				radius = 2;
			}

			for (xOffset = baseX - radius; xOffset <= baseX + radius; ++xOffset) {
				for (zOffset = baseZ - radius; zOffset <= baseZ + radius; ++zOffset) {
					//TODO: Change to mutable
					BlockPos pos = new BlockPos(xOffset, yOffset, zOffset);
					Block block = worldIn.getBlockState(pos).getBlock();

					if (block != null && !this.canGrowInto(worldIn, pos)) {
						return false;
					}
				}
			}
		}
		

		return false;
	}
	

}
