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

package techreborn.world.village;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.VillageGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.world.IWorld;
import techreborn.init.TRContent;
import techreborn.world.RubberTreeGenerator;

import java.util.List;
import java.util.Random;

public class VillageComponentRubberPlantaion extends VillageGenerator.Field1 {

	public static VillageGenerator.Piece buildComponent(VillageGenerator.PieceWeight villagePiece, VillageGenerator.Start startPiece, List<StructurePiece> pieces, Random random, int p1, int p2, int p3, Direction facing, int p5) {
		MutableIntBoundingBox structureboundingbox = MutableIntBoundingBox.createRotated(p1, p2, p3, 0, 0, 0, 13, 4, 9, facing);
		return canVillageGoDeeper(structureboundingbox) && StructurePiece.method_14932(pieces, structureboundingbox) == null ? new VillageComponentRubberPlantaion(startPiece, p5, random, structureboundingbox, facing) : null;
	}

	public VillageComponentRubberPlantaion() {
	}

	public VillageComponentRubberPlantaion(VillageGenerator.Start start, int type, Random rand, MutableIntBoundingBox structureBoundingBox, Direction facing) {
		super(start, type, rand, structureBoundingBox, facing);
	}

	@Override
	protected void fillWithBlocks(IWorld worldIn, MutableIntBoundingBox boundingboxIn, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, BlockState boundaryBlockState, BlockState insideBlockState, boolean existingOnly) {
		//Replaces farmland with dirt, its not great but it works.
		if (boundaryBlockState.getBlock() == Blocks.FARMLAND) {
			boundaryBlockState = Blocks.GRASS.getDefaultState();
			insideBlockState = Blocks.GRASS.getDefaultState();
		}
		//Replaces the water and logs with stone bricks
		if (boundaryBlockState.getBlock() == Blocks.WATER || boundaryBlockState.getBlock() == Blocks.OAK_LOG) {
			boundaryBlockState = Blocks.STONE_BRICKS.getDefaultState();
			insideBlockState = Blocks.STONE_BRICKS.getDefaultState();
		}
		super.fillWithBlocks(worldIn, boundingboxIn, xMin, yMin, zMin, xMax, yMax, zMax, boundaryBlockState, insideBlockState, existingOnly);
	}

	@Override
	protected void setBlockState(IWorld worldIn, BlockState blockstateIn, int x, int y, int z, MutableIntBoundingBox boundingboxIn) {
		if (isCrop(blockstateIn)) {
			blockstateIn = TRContent.RUBBER_SAPLING.getDefaultState();
		}
		super.setBlockState(worldIn, blockstateIn, x, y, z, boundingboxIn);
	}

	@Override
	public boolean addComponentParts(IWorld worldIn, Random randomIn, MutableIntBoundingBox structureBoundingBoxIn, ChunkPos chunkPos) {
		super.addComponentParts(worldIn, randomIn, structureBoundingBoxIn, chunkPos);
		for (int i = 1; i < 7; i++) {
			growRandom(i, 1, structureBoundingBoxIn, randomIn, worldIn);
			growRandom(i, 2, structureBoundingBoxIn, randomIn, worldIn);
			growRandom(i, 4, structureBoundingBoxIn, randomIn, worldIn);
			growRandom(i, 5, structureBoundingBoxIn, randomIn, worldIn);
			growRandom(i, 7, structureBoundingBoxIn, randomIn, worldIn);
			growRandom(i, 8, structureBoundingBoxIn, randomIn, worldIn);
			growRandom(i, 10, structureBoundingBoxIn, randomIn, worldIn);
			growRandom(i, 11, structureBoundingBoxIn, randomIn, worldIn);
		}
		generateChest(worldIn, structureBoundingBoxIn, randomIn, 0, 1, 0, ModLootTables.CHESTS_RUBBER_PLANTATION);
		return true;
	}

	private void growRandom(int coloum, int row, MutableIntBoundingBox structureBoundingBox, Random random, IWorld world) {
		if (random.nextInt(10) == 0) {
			setBlockState(world, Blocks.AIR.getDefaultState(), row, 1, coloum, structureBoundingBox);
			BlockPos pos = new BlockPos(this.getXWithOffset(row, coloum), this.getYWithOffset(1), this.getZWithOffset(row, coloum));
			if (!new RubberTreeGenerator(true).growTree(world, random, pos.getX(), pos.getY(), pos.getZ())) {
				//Puts the sapling back if the tree did not grow
				setBlockState(world, TRContent.RUBBER_SAPLING.getDefaultState(), row, 1, coloum, structureBoundingBox);
			}
		}
	}

	private boolean isCrop(BlockState state) {
		if (state.getBlock() instanceof CropBlock) {
			return true;
		}
		return false;
	}
}
