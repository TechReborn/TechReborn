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
		this.blockName = blockSate.getBlock().getUnlocalizedName();
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
}
