package techreborn.blocks.machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.blocks.BlockMachineBase;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileMatterFabricator;

public class BlockMatterFabricator extends BlockMachineBase {


    public BlockMatterFabricator(Material material) {
        super(material);
        setUnlocalizedName("techreborn.matterfabricator");
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileMatterFabricator();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.matterfabID, world, x, y,
                    z);
        return true;
    }


    @Override
    public boolean isAdvanced() {
        return true;
    }

}
