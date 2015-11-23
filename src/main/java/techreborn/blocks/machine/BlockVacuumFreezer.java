package techreborn.blocks.machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.blocks.BlockMachineBase;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileVacuumFreezer;

public class BlockVacuumFreezer extends BlockMachineBase {


    public BlockVacuumFreezer(Material material) {
        super(material);
        setUnlocalizedName("techreborn.vacuumfreezer");
    }


    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileVacuumFreezer();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {

        TileVacuumFreezer tileVacuumFreezer = (TileVacuumFreezer) world.getTileEntity(x, y, z);
        tileVacuumFreezer.multiBlockStatus = tileVacuumFreezer.checkMachine() ? 1 : 0;

        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.vacuumFreezerID, world, x, y,
                    z);
        return true;
    }
}
