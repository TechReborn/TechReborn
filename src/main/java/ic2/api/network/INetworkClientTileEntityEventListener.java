package ic2.api.network;

import net.minecraft.entity.player.*;

public interface INetworkClientTileEntityEventListener
{
    void onNetworkEvent(EntityPlayer p0, int p1);
}
