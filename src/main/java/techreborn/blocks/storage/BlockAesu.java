package techreborn.blocks.storage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.blocks.BlockMachineBase;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileAesu;

public class BlockAesu extends BlockMachineBase {

    @SideOnly(Side.CLIENT)
    private IIcon iconFront;

    @SideOnly(Side.CLIENT)
    private IIcon iconTop;

    @SideOnly(Side.CLIENT)
    private IIcon iconBottom;

    public BlockAesu(Material material) {
        super(material);
        setBlockName("techreborn.aesu");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileAesu();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.aesuID, world, x, y,
                    z);
        return true;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister icon) {
        this.blockIcon = icon.registerIcon("techreborn:machine/aesu_side");
        this.iconFront = icon.registerIcon("techreborn:machine/aesu_front");
        this.iconTop = icon.registerIcon("techreborn:machine/aesu_side");
        this.iconBottom = icon.registerIcon("techreborn:machine/aesu_side");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
        int metadata = getTileMeta(blockAccess, x, y, z);
        if (side == metadata && blockAccess.getBlockMetadata(x, y, z) == 1) {
            return this.iconFront;
        }
        return metadata == 0 && side == 3 ? this.iconFront
                : side == 1 ? this.iconTop :
                side == 0 ? this.iconBottom : (side == 0 ? this.iconTop
                        : (side == metadata ? this.iconFront : this.blockIcon));
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemstack) {
        super.onBlockPlacedBy(world, x, y, z, player, itemstack);
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileAesu) {
            ((TileAesu) tile).setFacing((short) world.getBlockMetadata(x, y, z));
        }
    }
}
