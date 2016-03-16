package techreborn.blocks.transformers;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.blocks.storage.BlockBatBox;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.storage.TileMFE;
import techreborn.tiles.transformers.TileHVTransformer;

/**
 * Created by modmuss50 on 16/03/2016.
 */
public class BlockHVTransformer extends BlockLVTransformer {

    public BlockHVTransformer() {
        super();
        setUnlocalizedName("techreborn.hvt");
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileHVTransformer();
    }

    @Override
    public String getFrontOff() {
        return prefix + "hvt_front";
    }

    @Override
    public String getFrontOn() {
        return prefix + "hvt_front";
    }

    @Override
    public String getSide() {
        return prefix + "hvt_side";
    }

    @Override
    public String getTop() {
        return prefix + "hvt_top";
    }

    @Override
    public String getBottom() {
        return prefix + "hvt_bottom";
    }
    
}
