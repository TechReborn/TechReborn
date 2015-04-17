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

    public void setMultiBlock(TileEntity tileEntity, BaseMultiBlock multiBlock){
        this.multiBlock = multiBlock;

    }

    public void setParent(TileEntity tileEntity){
        multiBlock.setParent(tileEntity);
    }
}
