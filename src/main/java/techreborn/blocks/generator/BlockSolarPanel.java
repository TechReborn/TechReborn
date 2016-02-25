package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.common.BaseTileBlock;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.generator.TileSolarPanel;

/**
 * Created by mark on 25/02/2016.
 */
public class BlockSolarPanel extends BaseTileBlock {

    public BlockSolarPanel() {
        super(Material.iron);
        setUnlocalizedName("techreborn.solarpanel");
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileSolarPanel();
    }
}
