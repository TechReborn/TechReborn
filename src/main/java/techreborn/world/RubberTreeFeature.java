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

package techreborn.world;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import techreborn.blocks.misc.BlockRubberLog;
import techreborn.init.TRContent;

/**
 * @author drcrazy
 *
 */
public class RubberTreeFeature extends AbstractTreeFeature<DefaultFeatureConfig> {

	// TODO: Configs
	private int treeBaseHeight = 5;
	private int sapRarity = 10;
	private int spireHeight = 4;
	

	public RubberTreeFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> featureConfig, boolean notify) {
		super(featureConfig, notify);
	}

	@Override
	protected boolean generate(Set<BlockPos> changedBlocks, ModifiableTestableWorld worldIn, Random rand,
			BlockPos saplingPos, BlockBox mutableBoundingBox) {
		int treeHeight = rand.nextInt(5) + treeBaseHeight;
		int baseY = saplingPos.getY();
		int baseX = saplingPos.getX();
		int baseZ = saplingPos.getZ();
		if (baseY <= 1 && baseY + treeHeight + spireHeight + 1 >= 256) {
			return false;
		}

		if (!isDirtOrGrass(worldIn, saplingPos.down())) {
			return false;
		}

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

			BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
			for (xOffset = baseX - radius; xOffset <= baseX + radius; ++xOffset) {
				for (zOffset = baseZ - radius; zOffset <= baseZ + radius; ++zOffset) {
					if (!isAirOrLeaves(worldIn, blockpos$mutable.set(xOffset, yOffset, zOffset))) {
						return false;
					}
				}
			}
		}

		// Ok, we are cleared for take off!
		this.setToDirt(worldIn, saplingPos.down());

		// Leaves
		BlockState leafState = TRContent.RUBBER_LEAVES.getDefaultState();
		for (yOffset = baseY - 3 + treeHeight; yOffset <= baseY + treeHeight; ++yOffset) {
			int var12 = yOffset - (baseY + treeHeight), center = 1 - var12 / 2;
			for (xOffset = baseX - center; xOffset <= baseX + center; ++xOffset) {
				int xPos = xOffset - baseX, t = xPos >> 15;
				xPos = (xPos + t) ^ t;
				for (zOffset = baseZ - center; zOffset <= baseZ + center; ++zOffset) {
					int zPos = zOffset - baseZ;
					zPos = (zPos + (t = zPos >> 31)) ^ t;
					if ((xPos != center | zPos != center) || rand.nextInt(2) != 0 && var12 != 0) {
						BlockPos leafPos = new BlockPos(xOffset, yOffset, zOffset);
						if (isAirOrLeaves(worldIn, leafPos) || isReplaceablePlant(worldIn, leafPos)) {
							this.setBlockState(changedBlocks, worldIn, leafPos, leafState, mutableBoundingBox);
						}
					}
				}
			}
		}

		// Trunk
		BlockPos currentLogPos = null;
		BlockState logState = TRContent.RUBBER_LOG.getDefaultState();
		for (yOffset = 0; yOffset < treeHeight; ++yOffset) {
			currentLogPos = saplingPos.up(yOffset);
			if (!isAirOrLeaves(worldIn, currentLogPos)) {
				continue;
			}
			if (rand.nextInt(sapRarity) == 0) {
				logState = logState.with(BlockRubberLog.HAS_SAP, true).with(BlockRubberLog.SAP_SIDE,
						Direction.fromHorizontal(rand.nextInt(4)));
			}
			this.setBlockState(changedBlocks, worldIn, currentLogPos, logState, mutableBoundingBox);
		}

		// Spire
		if (currentLogPos != null) {
			for (int i = 0; i < spireHeight; i++) {
				BlockPos blockpos = currentLogPos.up(i);
				this.setBlockState(changedBlocks, worldIn, blockpos, leafState, mutableBoundingBox);
			}
		}

		return true;
	}

}
