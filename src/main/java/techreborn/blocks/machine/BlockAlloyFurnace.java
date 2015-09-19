package techreborn.blocks.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.blocks.BlockMachineBase;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileAlloyFurnace;

public class BlockAlloyFurnace extends BlockMachineBase {

    @SideOnly(Side.CLIENT)
    private IIcon iconFront;

    @SideOnly(Side.CLIENT)
    private IIcon iconFrontOn;

    @SideOnly(Side.CLIENT)
    private IIcon iconTop;

    @SideOnly(Side.CLIENT)
    private IIcon iconBottom;

    public BlockAlloyFurnace(Material material) {
        super(material);
        setBlockName("techreborn.alloyfurnace");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileAlloyFurnace();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.alloyFurnaceID, world, x, y,
                    z);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister icon) {
        this.blockIcon = icon.registerIcon("techreborn:machine/alloy_furnace_side");
        this.iconFront = icon.registerIcon("techreborn:machine/alloy_furnace_front_off");
        this.iconFrontOn = icon.registerIcon("techreborn:machine/alloy_furnace_front_on");
        this.iconTop = icon.registerIcon("techreborn:machine/alloy_furnace_top");
        this.iconBottom = icon.registerIcon("techreborn:machine/alloy_furnace_bottom");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
        int metadata = getTileRotation(blockAccess, x, y, z);
        if (side == metadata && blockAccess.getBlockMetadata(x, y, z) == 1) {
            return this.iconFrontOn;
        }
        return metadata == 0 && side == 3 ? this.iconFront
                : side == 1 ? this.iconTop :
                side == 0 ? this.iconBottom : (side == 0 ? this.iconTop
                        : (side == metadata ? this.iconFront : this.blockIcon));
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if(side == 1){
            return this.iconTop;
        } else if(side == 3){
            return this.iconFront;
        } else {
            return this.blockIcon;
        }
    }

}
