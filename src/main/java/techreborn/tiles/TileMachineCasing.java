package techreborn.tiles;

import erogenousbeef.coreTR.multiblock.MultiblockControllerBase;
import erogenousbeef.coreTR.multiblock.MultiblockValidationException;
import erogenousbeef.coreTR.multiblock.rectangular.RectangularMultiblockTileEntityBase;
import techreborn.multiblocks.MultiBlockCasing;

public class TileMachineCasing extends RectangularMultiblockTileEntityBase {

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public void onMachineActivated() {

    }

    @Override
    public void onMachineDeactivated() {

    }

    @Override
    public MultiblockControllerBase createNewMultiblock() {
        return new MultiBlockCasing(worldObj);
    }

    @Override
    public Class<? extends MultiblockControllerBase> getMultiblockControllerType() {
        return MultiBlockCasing.class;
    }

    @Override
    public void isGoodForFrame() throws MultiblockValidationException {

    }

    @Override
    public void isGoodForSides() throws MultiblockValidationException {

    }

    @Override
    public void isGoodForTop() throws MultiblockValidationException {

    }

    @Override
    public void isGoodForBottom() throws MultiblockValidationException {

    }

    @Override
    public void isGoodForInterior() throws MultiblockValidationException {

    }

    public MultiBlockCasing getMultiblockController() {
        return (MultiBlockCasing) super.getMultiblockController();
    }

}
