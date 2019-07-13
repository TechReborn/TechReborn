package techreborn.tiles.cable;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

/**
 * Just some methods to help with the connectivity checking for cables
 */
public interface IConnectivityChecker {
	/**
	 * What to do when a neighbor changes
	 * @param neighbor the neighbor block that changed
	 * @param neighborPos the neighbor block position
	 */
	void onNeighborChanged(Block neighbor, BlockPos neighborPos);

	/**
	 * Update the connections aka facing properties
	 */
	void updateConnectivity();

	byte getConnectivity();
}
