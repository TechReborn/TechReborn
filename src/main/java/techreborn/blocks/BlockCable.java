package techreborn.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.common.BaseTileBlock;
import techreborn.client.TechRebornCreativeTabMisc;
import techreborn.tiles.TileCable;

public class BlockCable extends BaseTileBlock {

    public BlockCable() {
        super(Material.iron);
        setUnlocalizedName("techreborn.cable");
        setCreativeTab(TechRebornCreativeTabMisc.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileCable();
    }
}
