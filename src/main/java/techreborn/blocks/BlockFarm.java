package techreborn.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.client.GuiHandler;
import techreborn.client.TechRebornCreativeTab;
import techreborn.tiles.TileFarm;

public class BlockFarm extends BlockMachineBase {
    public BlockFarm() {
        super(Material.iron);
        setCreativeTab(TechRebornCreativeTab.instance);
        setBlockName("techreborn.farm");
        setHardness(2F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileFarm();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            player.openGui(Core.INSTANCE, GuiHandler.farmID, world, x, y, z);
            return true;
        }
        return false;
    }


}
