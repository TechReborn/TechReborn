package techreborn.blocks.transformers;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.tiles.transformers.TileEVTransformer;

public class BlockEVTransformer extends BlockTransformer {

    public BlockEVTransformer() {
        super("evtransformer");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEVTransformer();
    }

}
