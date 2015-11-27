package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import techreborn.blocks.BlockMachineBase;
import techreborn.blocks.IAdvancedRotationTexture;

public class BlockMagicEnergyAbsorber extends BlockMachineBase implements IAdvancedRotationTexture {

    public BlockMagicEnergyAbsorber(Material material) {
        super(material);
        setUnlocalizedName("techreborn.magicenergyabsorber");
    }


    private final String prefix = "techreborn:/blocks/machine/";

    @Override
    public String getFront(boolean isActive) {
        return prefix + "magic_energy_absorber_side";
    }

    @Override
    public String getSide(boolean isActive) {
        return prefix + "magic_energy_absorber_side" ;
    }

    @Override
    public String getTop(boolean isActive) {
        return prefix + "magic_energy_absorber_top";
    }

    @Override
    public String getBottom(boolean isActive) {
        return prefix + "magic_energy_absorber_bottom";
    }

}
