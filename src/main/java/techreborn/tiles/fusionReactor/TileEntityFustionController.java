package techreborn.tiles.fusionReactor;

import net.minecraftforge.common.util.ForgeDirection;
import techreborn.powerSystem.TilePowerAcceptor;


public class TileEntityFustionController extends TilePowerAcceptor {
    public TileEntityFustionController() {
        super(4);
    }

    @Override
    public double getMaxPower() {
        return 100000000;
    }

    @Override
    public boolean canAcceptEnergy(ForgeDirection direction) {
        return true;
    }

    @Override
    public boolean canProvideEnergy(ForgeDirection direction) {
        return true;
    }

    @Override
    public double getMaxOutput() {
        return 1000000;
    }

    @Override
    public double getMaxInput() {
        return 8192;
    }
}
