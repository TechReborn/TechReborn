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

	// Gets the position of a block along y axis, yOffset should be -1 (down) or 1 (up), origin is source block
	public static BlockPos getBlockAlongY(BlockPos origin, int yOffset, Block target, World world, boolean goThroughBlocks, Block ignoreBlock){
		BlockPos current = origin.add(0, yOffset,0); // Start at position above origin

		while(!World.isHeightInvalid(current)){
			// If we reach target, return
			Block block = world.getBlockState(current).getBlock();
			if(block == target){
				return current;
			}else if(!goThroughBlocks && block != ignoreBlock && block != Blocks.AIR){
				return null;
			}

			current = current.add(0, yOffset,0);
		}

		// Couldn't find anything, return null
		return null;
	}

	public static BlockPos getBlockAlongY(BlockPos origin, int yOffset, Block target, World world){
		return getBlockAlongY(origin,yOffset,target,world, false,null);
	}

	public static int getBlockCountAlongY(BlockPos origin, int yOffset, Block countBlock, World world) {
		return getBlockCountAlongY(origin,yOffset, Collections.singletonList(countBlock), world);
	}

	public static int getBlockCountAlongY(BlockPos origin, int yOffset, List<Block> countBlocks, World world){
		// Start at position above origin
		BlockPos current = origin.add(0, yOffset,0);

		boolean foundLast = false;
		while(!foundLast && !World.isHeightInvalid(current)) {
			// If we reach target, return
			Block block = world.getBlockState(current).getBlock();

			if(!countBlocks.contains(block)){
				foundLast = true;
				// Go back to last block
				current = current.add(0, -yOffset, 0);
			}else{
				current = current.add(0, yOffset, 0);
			}
		}

		if(!foundLast){
			// Could not find any, means zero
			return 0;
		}

		switch (yOffset){
			case 1: // UP
				return current.getY() - origin.getY();
			case -1: // DOWN
				return origin.getY() - current.getY();
			default:
				return -1;
		}
	}
}
