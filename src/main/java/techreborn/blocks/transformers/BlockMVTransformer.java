package techreborn.blocks.transformers;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.tiles.transformers.TileMVTransformer;

/**
 * Created by modmuss50 on 16/03/2016.
 */
public class BlockMVTransformer extends BlockTransformer {

    public BlockMVTransformer() {
        super("mvtransformer");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileMVTransformer();
    }


}
