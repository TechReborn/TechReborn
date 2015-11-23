package techreborn.blocks.generator;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import techreborn.blocks.BlockMachineBase;
import techreborn.tiles.TileHeatGenerator;

public class BlockHeatGenerator extends BlockMachineBase {


    public BlockHeatGenerator(Material material) {
        super(material);
        setUnlocalizedName("techreborn.heatgenerator");
    }



    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileHeatGenerator();
    }



}
