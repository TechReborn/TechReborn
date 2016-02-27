package techreborn.powernet;


import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class PowerNode {

    BlockPos nodePos;

    TileEntity nodeTile;

    PowerType type = PowerType.UNKNOWN;

    EnumFacing facingFromCable;

    public PowerNode(BlockPos nodePos, TileEntity nodeTile, EnumFacing facingFromCable) {
        this.nodePos = nodePos;
        this.nodeTile = nodeTile;
        this.facingFromCable = facingFromCable;
    }
}
