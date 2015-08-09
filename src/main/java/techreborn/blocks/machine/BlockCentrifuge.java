package techreborn.blocks.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.IC2Items;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.blocks.BlockMachineBase;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileCentrifuge;

import java.util.Random;

public class BlockCentrifuge extends BlockMachineBase {

    @SideOnly(Side.CLIENT)
    private IIcon iconFront;

    @SideOnly(Side.CLIENT)
    private IIcon iconFrontOn;

    @SideOnly(Side.CLIENT)
    private IIcon iconTop;

    @SideOnly(Side.CLIENT)
    private IIcon iconTopOn;

    @SideOnly(Side.CLIENT)
    private IIcon iconBottom;

    public BlockCentrifuge() {
        super(Material.rock);
        setBlockName("techreborn.centrifuge");
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileCentrifuge();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.centrifugeID, world, x, y,
                    z);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister icon) {
        this.iconFront = icon.registerIcon("techreborn:machine/industrial_centrifuge_side_off");
        this.iconFrontOn = icon.registerIcon("techreborn:machine/industrial_centrifuge_side_on");
        this.iconTop = icon.registerIcon("techreborn:machine/industrial_centrifuge_top_off");
        this.iconTopOn = icon.registerIcon("techreborn:machine/industrial_centrifuge_top_on");
        this.iconBottom = icon.registerIcon("techreborn:machine/industrial_centrifuge_bottom");
    }

    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
        int metadata = blockAccess.getBlockMetadata(x, y, z);
        TileCentrifuge tileCentrifuge = (TileCentrifuge) blockAccess.getTileEntity(x, y, z);
        if (side >= 2 && tileCentrifuge.crafter.isActive()) {
            return this.iconFrontOn;
        }

        if (side == 1 && tileCentrifuge.crafter.isActive()) {
            return this.iconTopOn;
        }


        return metadata == 0 && side == 3 ? this.iconFront
                : side == 1 ? this.iconTop :
                side == 0 ? this.iconBottom : (side == 0 ? this.iconTop
                        : (side == metadata ? this.iconFront : this.iconFront));

    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return metadata == 0 && side == 3 ? this.iconFront
                : side == 1 ? this.iconTop :
                side == 0 ? this.iconBottom : (side == 0 ? this.iconTop
                        : (side == metadata ? this.iconFront : this.iconFront));

    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        return IC2Items.getItem("machine").getItem();
    }

}
