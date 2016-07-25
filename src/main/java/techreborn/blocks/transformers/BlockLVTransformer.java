package techreborn.blocks.transformers;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.tiles.transformers.TileLVTransformer;

/**
 * Created by modmuss50 on 16/03/2016.
 */
public class BlockLVTransformer extends BlockTransformer {

    public BlockLVTransformer() {
        super("lvtransformer");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileLVTransformer();
    }

}
