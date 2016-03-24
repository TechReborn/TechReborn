package techreborn.blocks.storage;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.storage.TileMFE;

/**
 * Created by modmuss50 on 14/03/2016.
 */
public class BlockMFE extends BlockBatBox {

    public BlockMFE() {
        super();
        setUnlocalizedName("techreborn.mfe");
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileMFE();
    }

    @Override
    public String getFrontOff() {
        return prefix + "mfe_front";
    }

    @Override
    public String getFrontOn() {
        return prefix + "mfe_front";
    }

    @Override
    public String getSide() {
        return prefix + "mfe_side";
    }

    @Override
    public String getTop() {
        return prefix + "mfe_top";
    }

    @Override
    public String getBottom() {
        return prefix + "mfe_bottom";
    }
}
