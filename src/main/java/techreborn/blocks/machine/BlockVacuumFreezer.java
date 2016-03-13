package techreborn.blocks.machine;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.blocks.IAdvancedRotationTexture;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileVacuumFreezer;

public class BlockVacuumFreezer extends BlockMachineBase implements IAdvancedRotationTexture{

    public BlockVacuumFreezer(Material material) {
        super();
        setUnlocalizedName("techreborn.vacuumfreezer");
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileVacuumFreezer();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TileVacuumFreezer tileVacuumFreezer = (TileVacuumFreezer) world.getTileEntity(new BlockPos(x, y, z));
        tileVacuumFreezer.multiBlockStatus = tileVacuumFreezer.checkMachine() ? 1 : 0;
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.vacuumFreezerID, world, x, y, z);
        return true;
    }

    private final String prefix = "techreborn:blocks/machine/";

    @Override
    public String getFront(boolean isActive) {
        return prefix + "vacuum_freezer_front";
    }

    @Override
    public String getSide(boolean isActive) {
        return prefix + "machine_side" ;
    }

    @Override
    public String getTop(boolean isActive) {
        return prefix + "vacuum_freezer_top";
    }

    @Override
    public String getBottom(boolean isActive) {
        return prefix + "machine_bottom";
    }
}
