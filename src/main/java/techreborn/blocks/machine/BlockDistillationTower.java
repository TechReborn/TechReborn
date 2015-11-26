package techreborn.blocks.machine;

import net.minecraft.block.material.Material;
import techreborn.blocks.BlockMachineBase;
import techreborn.blocks.IRotationTexture;

public class BlockDistillationTower extends BlockMachineBase implements IRotationTexture {

    public BlockDistillationTower(Material material) {
        super(material);
        setUnlocalizedName("techreborn.distillationtower");
    }

    private final String prefix = "techreborn:/blocks/machine/";

    @Override
    public String getFrontOff() {
        return prefix + "distillation_tower_front_off";
    }

    @Override
    public String getFrontOn() {
        return prefix + "distillation_tower_front_off";
    }

    @Override
    public String getSide() {
        return prefix + "advanced_machine_side";
    }

    @Override
    public String getTop() {
        return prefix + "industrial_centrifuge_top_off";
    }

    @Override
    public String getBottom() {
        return prefix + "industrial_centrifuge_bottom";
    }
}
