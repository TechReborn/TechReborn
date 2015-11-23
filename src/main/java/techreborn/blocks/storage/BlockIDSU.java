package techreborn.blocks.storage;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.blocks.BlockMachineBase;
import techreborn.client.GuiHandler;
import techreborn.tiles.idsu.TileIDSU;

public class BlockIDSU extends BlockMachineBase {


    public BlockIDSU(Material material) {
        super(material);
        setUnlocalizedName("techreborn.idsu");
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileIDSU(5, 2048, 100000000);
    }


    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.idsuID, world, x, y,
                    z);
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemstack) {
        super.onBlockPlacedBy(world, x, y, z, player, itemstack);
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        if (tile instanceof TileIDSU) {
            ((TileIDSU) tile).ownerUdid = player.getUniqueID().toString();
        }
    }

}
