package techreborn.tiles.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import techreborn.blocks.BlockMachineCasing;
import techreborn.init.ModBlocks;

public class MultiblockChecker {

    public static final BlockPos ZERO_OFFSET = BlockPos.ORIGIN;

    public static final int CASING_NORMAL = 0;
    public static final int CASING_REINFORCED = 1;
    public static final int CASING_ADVANCED = 2;

    private final World world;
    private final BlockPos downCenter;

    public MultiblockChecker(World world, BlockPos downCenter) {
        this.world = world;
        this.downCenter = downCenter;
    }

    public boolean checkCasing(int offX, int offY, int offZ, int type) {
        IBlockState block = getBlock(offX, offY, offZ);
        if(block.getBlock() == ModBlocks.MachineCasing) {
          if(block.getValue(BlockMachineCasing.METADATA) == type)
              return true;
        }
        return false;
    }

    public boolean checkAir(int offX, int offY, int offZ) {
        BlockPos pos = downCenter.add(offX, offY, offZ);
        return world.isAirBlock(pos);
    }

    public IBlockState getBlock(int offX, int offY, int offZ) {
        BlockPos pos = downCenter.add(offX, offY, offZ);
        return world.getBlockState(pos);
    }

    public boolean checkRectY(int sizeX, int sizeZ, int casingType, BlockPos offset) {
        for(int x = -sizeX; x <= sizeX; x++) {
            for(int z = -sizeZ; z <= sizeZ; z++) {
                if(!checkCasing(x + offset.getX(), offset.getY(), z + offset.getZ(), casingType))
                    return false;
            }
        }
        return true;
    }

    public boolean checkRectZ(int sizeX, int sizeY, int casingType, BlockPos offset) {
        for(int x = -sizeX; x <= sizeX; x++) {
            for(int y = -sizeY; y <= sizeY; y++) {
                if(!checkCasing(x + offset.getX(), y + offset.getY(), offset.getZ(), casingType))
                    return false;
            }
        }
        return true;
    }

    public boolean checkRectX(int sizeZ, int sizeY, int casingType, BlockPos offset) {
        for(int z = -sizeZ; z <= sizeZ; z++) {
            for(int y = -sizeY; y <= sizeY; y++) {
                if(!checkCasing(offset.getX(), y + offset.getY(), z + offset.getZ(), casingType))
                    return false;
            }
        }
        return true;
    }

    public boolean checkRingY(int sizeX, int sizeZ, int casingType, BlockPos offset) {
        for(int x = -sizeX; x <= sizeX; x++) {
            for(int z = -sizeZ; z <= sizeZ; z++) {
                if((x == sizeX || x == -sizeX) || (z == sizeZ || z == -sizeZ)) {
                    if (!checkCasing(x + offset.getX(), offset.getY(), z + offset.getZ(), casingType))
                        return false;
                }
            }
        }
        return true;
    }

    public boolean checkRingYHollow(int sizeX, int sizeZ, int casingType, BlockPos offset) {
        for(int x = -sizeX; x <= sizeX; x++) {
            for(int z = -sizeZ; z <= sizeZ; z++) {
                if((x == sizeX || x == -sizeX) || (z == sizeZ || z == -sizeZ)) {
                    if (!checkCasing(x + offset.getX(), offset.getY(), z + offset.getZ(), casingType))
                        return false;
                } else if (!checkAir(x + offset.getX(), offset.getY(), z + offset.getZ()))
                    return false;
            }
        }
        return true;
    }

}
