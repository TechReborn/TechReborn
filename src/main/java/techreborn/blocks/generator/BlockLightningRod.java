package techreborn.blocks.generator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.IC2Items;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import techreborn.blocks.BlockMachineBase;

import java.util.Random;

public class BlockLightningRod extends BlockMachineBase {

    @SideOnly(Side.CLIENT)
    private IIcon iconFront;

    @SideOnly(Side.CLIENT)
    private IIcon iconTop;

    @SideOnly(Side.CLIENT)
    private IIcon iconBottom;

    public BlockLightningRod(Material material) {
        super(material);
        setBlockName("techreborn.lightningrod");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister icon) {
        this.blockIcon = icon.registerIcon("techreborn:machine/idsu_front");
        this.iconFront = icon.registerIcon("techreborn:machine/idsu_front");
        this.iconTop = icon.registerIcon("techreborn:machine/lightning_rod_top");
        this.iconBottom = icon.registerIcon("techreborn:machine/extreme_voltage_machine_side");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {

        return metadata == 0 && side == 3 ? this.iconFront
                : side == 1 ? this.iconTop :
                side == 0 ? this.iconBottom : (side == 0 ? this.iconTop
                        : (side == metadata ? this.iconFront : this.blockIcon));

    }

//    @Override
//    public Item getItemDropped(int meta, Random random, int fortune) {
//        return IC2Items.getItem("machine").getItem();
//    }//TODO

}
