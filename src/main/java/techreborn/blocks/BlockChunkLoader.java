package techreborn.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileChunkLoader;

public class BlockChunkLoader extends BlockMachineBase {


    public BlockChunkLoader(Material material) {
        super(material);
        setUnlocalizedName("techreborn.chunkloader");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileChunkLoader();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.chunkloaderID, world, x, y,
                    z);
        return true;
    }
}
