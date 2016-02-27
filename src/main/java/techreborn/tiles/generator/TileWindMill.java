package techreborn.tiles.generator;

import net.minecraft.util.EnumFacing;
import reborncore.common.powerSystem.TilePowerAcceptor;

/**
 * Created by mark on 25/02/2016.
 */
public class TileWindMill extends TilePowerAcceptor {

    public TileWindMill() {
        super(2);
    }

    int basePower = 4;

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(pos.getY() > 64){
            int actualPower = basePower;
            if(worldObj.isThundering()){
                actualPower *= 1.25;
            }
            addEnergy(actualPower); //Value taken from http://wiki.industrial-craft.net/?title=Wind_Mill Not worth making more complicated
        }
    }

    @Override
    public double getMaxPower() {
        return 10000;
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
        return 128;
    }

    @Override
    public double getMaxInput() {
        return 0;
    }
}
