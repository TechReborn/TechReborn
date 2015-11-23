package techreborn.blocks.machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.blocks.BlockMachineBase;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileIndustrialElectrolyzer;

public class BlockIndustrialElectrolyzer extends BlockMachineBase {


    public BlockIndustrialElectrolyzer(Material material) {
        super(material);
        setUnlocalizedName("techreborn.industrialelectrolyzer");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileIndustrialElectrolyzer();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.industrialElectrolyzerID, world, x, y,
                    z);
        return true;
    }

}
