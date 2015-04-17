package techreborn.api.multiblock;

import net.minecraft.tileentity.TileEntity;

public abstract class BaseMultiBlock implements IMultiBlock {

    boolean isComplete = false;

    TileEntity parent;

    public BaseMultiBlock() {
    }

    public void setParent(TileEntity parent) {
        this.parent = parent;
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }

    @Override
    public TileEntity getController() {
        return parent;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }
}
