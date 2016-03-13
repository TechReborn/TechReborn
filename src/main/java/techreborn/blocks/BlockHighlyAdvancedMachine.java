package techreborn.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import reborncore.common.blocks.BlockMachineBase;
import techreborn.client.TechRebornCreativeTab;

public class BlockHighlyAdvancedMachine extends BlockMachineBase {

    public BlockHighlyAdvancedMachine(Material material) {
        super();
        setUnlocalizedName("techreborn.highlyAdvancedMachine");
        setCreativeTab(TechRebornCreativeTab.instance);
    }

    private final String prefix = "techreborn:blocks/machine/";


    @Override
    public String getTextureNameFromState(IBlockState BlockStateContainer, EnumFacing facing) {
        return prefix + "highlyadvancedmachine";
    }

}
