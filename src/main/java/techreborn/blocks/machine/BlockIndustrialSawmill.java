package techreborn.blocks.machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.blocks.BlockMachineBase;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileIndustrialSawmill;

public class BlockIndustrialSawmill extends BlockMachineBase {

    public BlockIndustrialSawmill(Material material) {
        super(material);
        setUnlocalizedName("techreborn.industrialsawmill");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileIndustrialSawmill();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if(fillBlockWithFluid(world, x, y, z, player, side, hitX, hitY, hitZ)){
            return true;
        }
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.sawMillID, world, x, y,
                    z);
        return true;
    }


}
