package techreborn.api.multiblock;

import net.minecraft.tileentity.TileEntity;

public class MultiBlockController extends TileEntity implements IMultiBlockController{

    BaseMultiBlock multiBlock;

    public MultiBlockController(BaseMultiBlock multiBlock) {
        this.multiBlock = multiBlock;
    }

    @Override
    public IMultiBlock getMultiBlock() {
        return multiBlock;
    }
}
