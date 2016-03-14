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
        return prefix + "batbox_front";
    }

    @Override
    public String getFrontOn() {
        return prefix + "batbox_front";
    }

    @Override
    public String getSide() {
        return prefix + "batbox_side";
    }

    @Override
    public String getTop() {
        return prefix + "batbox_side";
    }

    @Override
    public String getBottom() {
        return prefix + "batbox_side";
    }
}
