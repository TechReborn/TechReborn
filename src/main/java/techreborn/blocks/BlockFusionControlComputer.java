package techreborn.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.tiles.fusionReactor.TileEntityFusionController;

public class BlockFusionControlComputer extends BlockMachineBase {



    public BlockFusionControlComputer(Material material) {
        super(material);
        setUnlocalizedName("techreborn.fusioncontrolcomputer");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TileEntityFusionController tileEntityFusionController = (TileEntityFusionController) world.getTileEntity(new BlockPos(x, y, z));
        tileEntityFusionController.checkCoils();
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.fusionID, world, x, y,
                    z);
        return true;
    }


    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityFusionController();
    }
}
