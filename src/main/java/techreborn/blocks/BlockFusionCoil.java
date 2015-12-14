package techreborn.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class BlockFusionCoil extends BlockMachineBase {


    public BlockFusionCoil(Material material) {
        super(material);
        setUnlocalizedName("techreborn.fusioncoil");
    }


    private final String prefix = "techreborn:blocks/machine/";

    @Override
    public String getTextureName(IBlockState blockState, EnumFacing facing) {
        return prefix + "fusion_coil";
    }
}
