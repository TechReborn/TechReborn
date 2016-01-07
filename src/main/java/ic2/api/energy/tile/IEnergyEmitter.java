package ic2.api.energy.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public interface IEnergyEmitter extends IEnergyTile
{
    boolean emitsEnergyTo(TileEntity p0, EnumFacing p1);
}
