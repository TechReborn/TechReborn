package ic2.api.energy.tile;

import java.util.*;
import net.minecraft.tileentity.*;

public interface IMetaDelegate extends IEnergyTile
{
    List<TileEntity> getSubTiles();
}
