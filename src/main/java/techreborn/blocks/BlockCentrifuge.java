package techreborn.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileCentrifuge;

public class BlockCentrifuge extends BlockContainer {

    public BlockCentrifuge(){
        super(Material.piston);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileCentrifuge();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.centrifugeID, world, x, y, z);
        return true;
    }

}
