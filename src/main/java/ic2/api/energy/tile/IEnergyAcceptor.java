package ic2.api.energy.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public interface IEnergyAcceptor extends IEnergyTile
{
    boolean acceptsEnergyFrom(TileEntity p0, EnumFacing p1);
}
