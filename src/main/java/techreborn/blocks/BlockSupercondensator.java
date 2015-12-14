package techreborn.blocks;

import net.minecraft.block.material.Material;

public class BlockSupercondensator extends BlockMachineBase implements IAdvancedRotationTexture {


    public BlockSupercondensator(Material material) {
        super(material);
        setUnlocalizedName("techreborn.supercondensator");
    }

    private final String prefix = "techreborn:blocks/machine/";

    @Override
    public String getFront(boolean isActive) {
        return prefix + "supercondensator_front";
    }

    @Override
    public String getSide(boolean isActive) {
        return prefix + "supercondensator_side" ;
    }

    @Override
    public String getTop(boolean isActive) {
        return prefix + "supercondensator_side";
    }

    @Override
    public String getBottom(boolean isActive) {
        return prefix + "machine_bottom";
    }

}
