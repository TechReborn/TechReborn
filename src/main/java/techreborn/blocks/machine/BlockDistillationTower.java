package techreborn.blocks.machine;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import techreborn.blocks.BlockMachineBase;

public class BlockDistillationTower extends BlockMachineBase {

    @SideOnly(Side.CLIENT)
    private IIcon iconFront;

    @SideOnly(Side.CLIENT)
    private IIcon iconTop;

    @SideOnly(Side.CLIENT)
    private IIcon iconBottom;

    public BlockDistillationTower(Material material) {
        super(material);
        setBlockName("techreborn.distillationtower");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister icon) {
        this.blockIcon = icon.registerIcon("techreborn:machine/machine_side");
        this.iconFront = icon.registerIcon("techreborn:machine/distillation_tower_front_off");
        this.iconTop = icon.registerIcon("techreborn:machine/advanced_machine_side");
        this.iconBottom = icon.registerIcon("techreborn:machine/advanced_machine_side");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side) {
        int metadata = getTileRotation(blockAccess, x, y, z);
        return metadata == 0 && side == 3 ? this.iconFront
                : side == 1 ? this.iconTop :
                side == 0 ? this.iconBottom : (side == 0 ? this.iconTop
                        : (side == metadata ? this.iconFront : this.blockIcon));
    }


    @Override
    public IIcon getIcon(int side, int meta) {
        if (side == 1) {
            return this.iconTop;
        } else if (side == 3) {
            return this.iconFront;
        } else {
            return this.blockIcon;
        }
    }
}
