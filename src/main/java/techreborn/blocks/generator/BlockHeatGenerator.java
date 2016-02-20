package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.blocks.BlockMachineBase;
import techreborn.blocks.IAdvancedRotationTexture;
import techreborn.tiles.generator.TileHeatGenerator;

public class BlockHeatGenerator extends BlockMachineBase implements IAdvancedRotationTexture {


    public BlockHeatGenerator(Material material) {
        super(material);
        setUnlocalizedName("techreborn.heatgenerator");
    }



    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileHeatGenerator();
    }

    private final String prefix = "techreborn:blocks/machine/";

    @Override
    public String getFront(boolean isActive) {
        return prefix + "heat_generator_side";
    }

    @Override
    public String getSide(boolean isActive) {
        return prefix + "heat_generator_side" ;
    }

    @Override
    public String getTop(boolean isActive) {
        return prefix + "heat_generator_top";
    }

    @Override
    public String getBottom(boolean isActive) {
        return prefix + "heat_generator_bottom";
    }

}
