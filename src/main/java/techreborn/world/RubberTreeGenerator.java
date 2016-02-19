package techreborn.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.gen.feature.WorldGenTrees;

/**
 * Created by Mark on 19/02/2016.
 */
public class RubberTreeGenerator extends WorldGenTrees {


    public RubberTreeGenerator(boolean doBlockNotify, int minTreeHeight, IBlockState woodState, IBlockState leaveState, boolean vinesGrow) {
        super(doBlockNotify, minTreeHeight, woodState, leaveState, vinesGrow);
    }
}
