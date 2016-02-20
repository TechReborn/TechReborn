package techreborn.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.fusionReactor.TileEntityFusionController;

public class BlockFusionControlComputer extends BlockMachineBase implements IAdvancedRotationTexture {



    public BlockFusionControlComputer(Material material) {
        super();
        setUnlocalizedName("techreborn.fusioncontrolcomputer");
        setCreativeTab(TechRebornCreativeTab.instance);
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

    private final String prefix = "techreborn:blocks/machine/";

    @Override
    public String getFront(boolean isActive) {
        return prefix + "fusion_control_computer_front";
    }

    @Override
    public String getSide(boolean isActive) {
        return prefix + "plasma_generator_side_off" ;
    }

    @Override
    public String getTop(boolean isActive) {
        return prefix + "plasma_generator_side_off";
    }

    @Override
    public String getBottom(boolean isActive) {
        return prefix + "plasma_generator_side_off";
    }
}
