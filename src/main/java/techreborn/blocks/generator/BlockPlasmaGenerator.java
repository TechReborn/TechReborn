package techreborn.blocks.generator;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import techreborn.blocks.BlockMachineBase;

public class BlockPlasmaGenerator extends BlockMachineBase {

    @SideOnly(Side.CLIENT)
    private IIcon iconFront;

    @SideOnly(Side.CLIENT)
    private IIcon iconTop;

    @SideOnly(Side.CLIENT)
    private IIcon iconBottom;

    public BlockPlasmaGenerator(Material material) {
        super(material);
        setBlockName("techreborn.plasmagenerator");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister icon) {
        this.blockIcon = icon.registerIcon("techreborn:machine/plasma_generator_side_off");
        this.iconFront = icon.registerIcon("techreborn:machine/plasma_generator_front");
        this.iconTop = icon.registerIcon("techreborn:machine/plasma_generator_side_off");
        this.iconBottom = icon.registerIcon("techreborn:machine/plasma_generator_side_off");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {

        return metadata == 0 && side == 3 ? this.iconFront
                : side == 1 ? this.iconTop :
                side == 0 ? this.iconBottom : (side == 0 ? this.iconTop
                        : (side == metadata ? this.iconFront : this.blockIcon));

    }

}
