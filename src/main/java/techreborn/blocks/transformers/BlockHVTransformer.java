package techreborn.blocks.transformers;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.tiles.transformers.TileHVTransformer;

/**
 * Created by modmuss50 on 16/03/2016.
 */
public class BlockHVTransformer extends BlockTransformer {

    public BlockHVTransformer() {
        super("hvtransformer");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileHVTransformer();
    }

}
