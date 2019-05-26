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
import net.minecraft.block.BlockState;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import techreborn.blocks.BlockRubberLog;
import techreborn.init.TRContent;

/**
 * @author drcrazy
 *
 */
public class RubberTreeFeature extends AbstractTreeFeature<DefaultFeatureConfig> {
	
	//TODO: Configs
	private int treeBaseHeight = 5;
	private int sapRarity = 10;
	private int spireHeight = 4;
	final BlockState leafState = TRContent.RUBBER_LEAVES.getDefaultState();

	public RubberTreeFeature() {
		super(false);
	}

	@Override
	protected boolean place(Set<BlockPos> changedBlocks, IWorld worldIn, Random rand, BlockPos saplingPos) {
		int treeHeight = rand.nextInt(5) + treeBaseHeight;
		int worldHeight = worldIn.getWorld().getTopPosition();
		
		int baseY = saplingPos.getY();
		int baseX = saplingPos.getX();
		int baseZ = saplingPos.getZ();
		if (baseY <= 1 && baseY + treeHeight + 1 >= worldHeight) { return false; }
		
		BlockPos rootBlockPos = saplingPos.down();
		BlockState rootBlockState = worldIn.getBlockState(rootBlockPos);
		boolean isSoil = rootBlockState.canSustainPlant(worldIn, rootBlockPos,
				net.minecraft.util.math.Direction.UP,
				(net.minecraft.block.SaplingBlock) TRContent.RUBBER_SAPLING.getBlock());
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

			BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();
			for (xOffset = baseX - radius; xOffset <= baseX + radius; ++xOffset) {
				for (zOffset = baseZ - radius; zOffset <= baseZ + radius; ++zOffset) {
					if (!this.canGrowInto(worldIn, blockpos$mutableblockpos.set(xOffset, yOffset, xOffset))) {
						return false;
					}
				}
			}
		}
		
		// Ok, we are cleared for take off!
		boolean hasPlacedBlock = false;

		// Leaves
		for (yOffset = baseY - 3 + treeHeight; yOffset <= baseY + treeHeight; ++yOffset) {
			int var12 = yOffset - (baseY + treeHeight), center = 1 - var12 / 2;
			for (xOffset = baseX - center; xOffset <= baseX + center; ++xOffset) {
				int xPos = xOffset - baseX, t = xPos >> 15;
				xPos = (xPos + t) ^ t;
				for (zOffset = baseZ - center; zOffset <= baseZ + center; ++zOffset) {
					int zPos = zOffset - baseZ;
					zPos = (zPos + (t = zPos >> 31)) ^ t;
					if ((xPos != center | zPos != center) || rand.nextInt(2) != 0 && var12 != 0) {
	                      BlockPos blockpos = new BlockPos(xOffset, yOffset, zOffset);
	                      hasPlacedBlock = growLeaves(worldIn, blockpos);
					}
				}
			}
		}
		
		// Trunk
		BlockPos topLogPos = null;
		BlockState logState = TRContent.RUBBER_LOG.getDefaultState();
		for (yOffset = 0; yOffset < treeHeight; ++yOffset) {
			BlockPos blockpos = new BlockPos(baseX, baseY + yOffset, baseZ);
			BlockState state1 = worldIn.getBlockState(blockpos);
			if (state1.isAir(worldIn, blockpos) || state1.matches(BlockTags.LEAVES)) {
				
				if (rand.nextInt(sapRarity) == 0) {
					logState = logState.with(BlockRubberLog.HAS_SAP, true)
						.with(BlockRubberLog.SAP_SIDE, Direction.fromHorizontal(rand.nextInt(4)));
				}
	
				//setBlockState(worldIn, blockpos, logState);
				this.setLogState(changedBlocks, worldIn, blockpos, logState);

				hasPlacedBlock = true;
				topLogPos = blockpos;
			}
		}
		
		// Spire
		if (topLogPos != null) {
			for (int i = 0; i < spireHeight; i++) {
				BlockPos blockpos = topLogPos.up(i);
				growLeaves(worldIn, blockpos);
			}
		}
		
		if (hasPlacedBlock) {
			setDirtAt(worldIn, rootBlockPos, saplingPos);
		}

		return hasPlacedBlock;
	}
	
	private boolean growLeaves(IWorld worldIn, BlockPos pos) {
        BlockState state1 = worldIn.getBlockState(pos);
        if (state1.canBeReplacedByLeaves(worldIn, pos)) {
      	  this.setBlockState(worldIn, pos, leafState);
      	  return true;
        }
        		
		return false;
	}
}
