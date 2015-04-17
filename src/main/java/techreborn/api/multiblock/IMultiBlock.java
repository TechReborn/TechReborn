package techreborn.api.multiblock;

import net.minecraft.tileentity.TileEntity;

import java.util.List;

public interface IMultiBlock {

    /**
     * This is the name of the multiblock
     */
    void getName();

    /**
     * This check to see if the multiblock is complete
     */
    boolean checkIfComplete(int x, int y, int z);

    /**
     * This is a list of all of the tiles that make up the multiblock
     */
    List<TileEntity> getTiles();

    /**
     * This is the controller for the whole multiblock structure.
     *
     * This tile will store the nbt and do the logic for the whole system. Send block actions to this block.
     */
    TileEntity getController();

    /**
     * Call this from the controller to allow to tile to update is completeness.
     */
    void recompute();
}
