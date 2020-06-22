package techreborn.utils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// Potentially temporary class, only used for shared functions for manipulation in world
public abstract class WorldHelper {

	// Gets the position of a block along y axis, yOffset should be -1 (down) or 1 (up), origin is source block
	public static BlockPos getBlockAlongY(BlockPos origin, int yOffset, Block target, boolean goThroughBlocks, Block ignoreBlock, World world){
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
		return getBlockAlongY(origin,yOffset,target,false,null,world);
	}
}
