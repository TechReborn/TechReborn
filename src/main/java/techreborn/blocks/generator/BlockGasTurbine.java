package techreborn.blocks.generator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.IC2Items;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import techreborn.Core;
import techreborn.blocks.BlockMachineBase;
import techreborn.client.GuiHandler;
import techreborn.tiles.TileGasTurbine;

import java.util.Random;

public class BlockGasTurbine extends BlockMachineBase {

    @SideOnly(Side.CLIENT)
    private IIcon iconFront;

    @SideOnly(Side.CLIENT)
    private IIcon iconTop;

    @SideOnly(Side.CLIENT)
    private IIcon iconBottom;

    public BlockGasTurbine(Material material) {
        super(material);
        setBlockName("techreborn.gasTurbine");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileGasTurbine();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking())
            player.openGui(Core.INSTANCE, GuiHandler.gasTurbineID, world, x, y,
                    z);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister icon) {
        this.blockIcon = icon.registerIcon("techreborn:machine/machine_side");
        this.iconFront = icon.registerIcon("techreborn:machine/machine_side");
        this.iconTop = icon.registerIcon("techreborn:machine/gas_generator_top");
        this.iconBottom = icon.registerIcon("techreborn:machine/gas_generator_bottom");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {

        return metadata == 0 && side == 3 ? this.iconFront
                : side == 1 ? this.iconTop :
                side == 0 ? this.iconBottom : (side == 0 ? this.iconTop
                        : (side == metadata ? this.iconFront : this.blockIcon));

    }

}
