package techreborn.world;

import net.minecraft.block.state.IBlockState;

/**
 * Created by modmuss50 on 11/03/2016.
 */
public class OreConfig {

    public String blockName;

    public String unloclisedName;

    public String blockSate;

    public int meta;

    public String c = "↓↓↓↓CHANGE ME↓↓↓↓";

    public int veinSize;

    public int veinsPerChunk;

    public int minYHeight;

    public int maxYHeight;


    public OreConfig(IBlockState blockSate, int veinSize, int veinsPerChunk, int minYHeight, int maxYHeight) {
        this.blockName = blockSate.getBlock().getLocalizedName();
        this.meta = blockSate.getBlock().getMetaFromState(blockSate);
        this.unloclisedName = blockSate.getBlock().getUnlocalizedName();
        this.blockSate = blockSate.toString();
        this.veinSize = veinSize;
        this.veinsPerChunk = veinsPerChunk;
        this.minYHeight = minYHeight;
        this.maxYHeight = maxYHeight;
    }
}
