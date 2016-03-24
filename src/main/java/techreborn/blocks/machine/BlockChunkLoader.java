package techreborn.blocks.machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileChunkLoader;

public class BlockChunkLoader extends BlockMachineBase implements IAdvancedRotationTexture {


    public BlockChunkLoader(Material material) {
        super();
        setUnlocalizedName("techreborn.chunkloader");
        setCreativeTab(TechRebornCreativeTab.instance);
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

    private final String prefix = "techreborn:blocks/machine/greg_machines/";

    @Override
    public String getFront(boolean isActive) {
        return prefix + "industrial_chunk_loader_side";
    }

    @Override
    public String getSide(boolean isActive) {
        return prefix + "industrial_chunk_loader_side" ;
    }

    @Override
    public String getTop(boolean isActive) {
        return prefix + "machine_top";
    }

    @Override
    public String getBottom(boolean isActive) {
        return prefix + "machine_bottom";
    }
}
