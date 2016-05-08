package techreborn.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.common.BaseTileBlock;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TilePump;

/**
 * Created by modmuss50 on 08/05/2016.
 */
public class BlockPump extends BaseTileBlock {
    public BlockPump() {
        super(Material.IRON);
        setHardness(2f);
        setUnlocalizedName("techreborn.pump");
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TilePump();
    }
}
