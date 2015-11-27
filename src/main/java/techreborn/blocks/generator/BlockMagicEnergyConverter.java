package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import techreborn.blocks.BlockMachineBase;
import techreborn.blocks.IAdvancedRotationTexture;

public class BlockMagicEnergyConverter extends BlockMachineBase implements IAdvancedRotationTexture {

    public BlockMagicEnergyConverter(Material material) {
        super(material);
        setUnlocalizedName("techreborn.magicenergyconverter");
    }


    private final String prefix = "techreborn:/blocks/machine/";

    @Override
    public String getFront(boolean isActive) {
        return prefix + "magic_energy_converter_front_off";
    }

    @Override
    public String getSide(boolean isActive) {
        return prefix + "magic_energy_converter_side" ;
    }

    @Override
    public String getTop(boolean isActive) {
        return prefix + "magic_energy_converter_top";
    }

    @Override
    public String getBottom(boolean isActive) {
        return prefix + "magic_energy_converter_bottom";
    }


}
