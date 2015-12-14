package techreborn.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class BlockHighlyAdvancedMachine extends BlockMachineBase {

    public BlockHighlyAdvancedMachine(Material material) {
        super(material);
        setUnlocalizedName("techreborn.highlyAdvancedMachine");
    }

    private final String prefix = "techreborn:blocks/machine/";


    @Override
    public String getTextureName(IBlockState blockState, EnumFacing facing) {
        return prefix + "highlyadvancedmachine";
    }

}
