package techreborn.blocks;

import net.minecraft.block.material.Material;


public class BlockElectricCraftingTable extends BlockMachineBase implements IAdvancedRotationTexture {


    public BlockElectricCraftingTable(Material material) {
        super(material);
        setUnlocalizedName("techreborn.electriccraftingtable");
    }

    private final String prefix = "techreborn:/blocks/machine/";

    @Override
    public String getFront(boolean isActive) {
        return prefix + "electric_crafting_table_front";
    }

    @Override
    public String getSide(boolean isActive) {
        return prefix + "machine_side" ;
    }

    @Override
    public String getTop(boolean isActive) {
        return prefix + "electric_crafting_table_top";
    }

    @Override
    public String getBottom(boolean isActive) {
        return prefix + "machine_bottom";
    }

}
