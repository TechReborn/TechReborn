package techreborn.tiles.generator;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import reborncore.common.powerSystem.TilePowerAcceptor;

/**
 * Created by mark on 25/02/2016.
 */
public class TileWaterMill extends TilePowerAcceptor {

    public TileWaterMill() {
        super(1);
    }

    int waterblocks = 0;

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(worldObj.getTotalWorldTime() % 20 == 0){
            checkForWater();
        }
        if(waterblocks > 0){
            addEnergy(waterblocks);
        }
    }

    public void checkForWater(){
        waterblocks = 0;
        for(EnumFacing facing : EnumFacing.HORIZONTALS){
            if(worldObj.getBlockState(getPos().offset(facing)).getBlock() == Blocks.water){
                waterblocks ++;
            }
        }
    }


    @Override
    public double getMaxPower() {
        return 1000;
    }

    @Override
    public boolean canAcceptEnergy(EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canProvideEnergy(EnumFacing direction) {
        return true;
    }

    @Override
    public double getMaxOutput() {
        return 32;
    }

    @Override
    public double getMaxInput() {
        return 0;
    }
}
