package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import techreborn.blocks.BlockMachineBase;
import techreborn.blocks.IAdvancedRotationTexture;

public class BlockLightningRod extends BlockMachineBase implements IAdvancedRotationTexture {


    public BlockLightningRod(Material material) {
        super(material);
        setUnlocalizedName("techreborn.lightningrod");
    }

    private final String prefix = "techreborn:/blocks/machine/";

    @Override
    public String getFront(boolean isActive) {
        return prefix + "idsu_front";
    }

    @Override
    public String getSide(boolean isActive) {
        return prefix + "idsu_front" ;
    }

    @Override
    public String getTop(boolean isActive) {
        return prefix + "lightning_rod_top";
    }

    @Override
    public String getBottom(boolean isActive) {
        return prefix + "extreme_voltage_machine_side";
    }

}
