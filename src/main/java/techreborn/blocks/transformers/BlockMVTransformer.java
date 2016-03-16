package techreborn.blocks.transformers;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.blocks.storage.BlockBatBox;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.storage.TileMFE;
import techreborn.tiles.transformers.TileMVTransformer;

/**
 * Created by modmuss50 on 16/03/2016.
 */
public class BlockMVTransformer extends BlockLVTransformer {

    public BlockMVTransformer() {
        super();
        setUnlocalizedName("techreborn.mvt");
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileMVTransformer();
    }

    @Override
    public String getFrontOff() {
        return prefix + "mv_transformer_front";
    }

    @Override
    public String getFrontOn() {
        return prefix + "mv_transformer_front";
    }

    @Override
    public String getSide() {
        return prefix + "mv_transformer_side";
    }

    @Override
    public String getTop() {
        return prefix + "mv_transformer_side";
    }

    @Override
    public String getBottom() {
        return prefix + "mv_transformer_side";
    }
}
