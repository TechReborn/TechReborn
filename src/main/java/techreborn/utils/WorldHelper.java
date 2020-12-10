package techreborn.utils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Potentially temporary class, only used for shared functions for manipulation in world
public abstract class WorldHelper {

	public static BlockPos getBlockAlongY(BlockPos origin, int yOffset, Block target, World world, boolean goThroughBlocks, Block ignoreBlock) {
		List<BlockPos> blocks = getBlocksAlongY(origin,yOffset,target, world, goThroughBlocks, ignoreBlock);

		if(blocks.size() == 0){
			return null;
		}

		// Return first
		return blocks.get(0);
	}

		// Gets the position of a block along y axis, yOffset should be -1 (down) or 1 (up), origin is source block
	public static List<BlockPos> getBlocksAlongY(BlockPos origin, int yOffset, Block target, World world, boolean goThroughBlocks, Block ignoreBlock){
		ArrayList<BlockPos> blocks = new ArrayList<>();

		BlockPos current = origin.add(0, yOffset,0); // Start at position above origin

		boolean shouldExit = false;

		while(!shouldExit && !World.isOutOfBuildLimitVertically(current)){
			// If we reach target, return
			Block block = world.getBlockState(current).getBlock();
			if(block == target){
				blocks.add(current);
			}else if(!goThroughBlocks && block != ignoreBlock && block != Blocks.AIR){
				shouldExit = true;
			}

			if(!shouldExit){
				current = current.add(0, yOffset,0);
			}
		}

		// Couldn't find anything, return null
		return blocks;
	}

	public static BlockPos getBlockAlongY(BlockPos origin, int yOffset, Block target, World world){
		return getBlockAlongY(origin,yOffset,target,world, false,null);
	}

	public static int getBlockCountAlongY(BlockPos origin, int yOffset, Block countBlock, World world) {
		return getBlockCountAlongY(origin, yOffset, Collections.singletonList(countBlock), world);
	}

	public static int getBlockCountAlongY(BlockPos origin, int yOffset, List<Block> countBlocks, World world){
		// Start at position above origin
		BlockPos current = origin.add(0, yOffset,0);

		int count = 0;

		boolean foundLast = false;
		while(!foundLast && !World.isOutOfBuildLimitVertically(current)) {
			// If we reach target, return
			Block block = world.getBlockState(current).getBlock();

			if(!countBlocks.contains(block)){
				foundLast = true;
				// Go back to last block
				current = current.add(0, -yOffset, 0);
			}else{
				current = current.add(0, yOffset, 0);
				count++;
			}
		}

		return count;
	}
}
