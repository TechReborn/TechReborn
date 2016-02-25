package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.common.BaseTileBlock;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.generator.TileWindMill;

/**
 * Created by mark on 25/02/2016.
 */
public class BlockWindMill extends BaseTileBlock {

    public BlockWindMill() {
        super(Material.iron);
        setUnlocalizedName("techreborn.windmill");
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileWindMill();
    }
}
