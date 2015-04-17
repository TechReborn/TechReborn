package techreborn.tiles;

import net.minecraft.tileentity.TileEntity;
import techreborn.api.multiblock.IMultiblockComponent;
import techreborn.multiblocks.MultiBlastfurnace;

public class TileMachineCasing extends TileEntity implements IMultiblockComponent {

    @Override
    public boolean canUpdate() {
        //No need to update this.
        return false;
    }

    @Override
    public Class getMultiblockType() {
        return MultiBlastfurnace.class;
    }
}
