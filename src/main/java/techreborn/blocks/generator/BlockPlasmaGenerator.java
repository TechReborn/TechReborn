package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import techreborn.blocks.BlockMachineBase;
import techreborn.blocks.IAdvancedRotationTexture;

public class BlockPlasmaGenerator extends BlockMachineBase implements IAdvancedRotationTexture {



    public BlockPlasmaGenerator(Material material) {
        super(material);
        setUnlocalizedName("techreborn.plasmagenerator");
    }

    private final String prefix = "techreborn:blocks/machine/";

    @Override
    public String getFront(boolean isActive) {
        return prefix + "plasma_generator_front";
    }

    @Override
    public String getSide(boolean isActive) {
        return prefix + "plasma_generator_side_off" ;
    }

    @Override
    public String getTop(boolean isActive) {
        return prefix + "plasma_generator_side_off";
    }

    @Override
    public String getBottom(boolean isActive) {
        return prefix + "plasma_generator_side_off";
    }

}
