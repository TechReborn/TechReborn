package techreborn.powernet;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IPowerCableContainer {

    World getWorld();

    BlockPos getPos();

    boolean canConnectTo(EnumFacing facing);

    PowerCable getPowerCable();

}
