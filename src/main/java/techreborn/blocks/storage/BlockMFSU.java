package techreborn.blocks.storage;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.storage.TileMFSU;

/**
 * Created by modmuss50 on 14/03/2016.
 */
public class BlockMFSU extends BlockBatBox {

    public BlockMFSU() {
        super();
        setUnlocalizedName("techreborn.mfsu");
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileMFSU();
    }

    @Override
    public String getFrontOff() {
        return prefix + "mfsubatbox_front";
    }

    @Override
    public String getFrontOn() {
        return prefix + "mfsu_front";
    }

    @Override
    public String getSide() {
        return prefix + "mfsu_side";
    }

    @Override
    public String getTop() {
        return prefix + "mfsu_top";
    }

    @Override
    public String getBottom() {
        return prefix + "mfsu_bottom";
    }
}
