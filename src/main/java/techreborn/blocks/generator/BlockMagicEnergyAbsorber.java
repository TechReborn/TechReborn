package techreborn.blocks.generator;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import techreborn.blocks.BlockMachineBase;

public class BlockMagicEnergyAbsorber extends BlockMachineBase {

    @SideOnly(Side.CLIENT)
    private IIcon iconFront;

    @SideOnly(Side.CLIENT)
    private IIcon iconTop;

    @SideOnly(Side.CLIENT)
    private IIcon iconBottom;

    public BlockMagicEnergyAbsorber(Material material) {
        super(material);
        setBlockName("techreborn.magicenergyabsorber");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister icon) {
        this.blockIcon = icon.registerIcon("techreborn:machine/magic_energy_absorber_side");
        this.iconFront = icon.registerIcon("techreborn:machine/magic_energy_absorber_side");
        this.iconTop = icon.registerIcon("techreborn:machine/magic_energy_absorber_top");
        this.iconBottom = icon.registerIcon("techreborn:machine/magic_energy_absorber_bottom");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {

        return metadata == 0 && side == 3 ? this.iconFront
                : side == 1 ? this.iconTop :
                side == 0 ? this.iconBottom : (side == 0 ? this.iconTop
                        : (side == metadata ? this.iconFront : this.blockIcon));

    }

}
