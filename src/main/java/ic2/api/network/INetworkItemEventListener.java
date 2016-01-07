package ic2.api.network;

import net.minecraft.item.*;
import net.minecraft.entity.player.*;

public interface INetworkItemEventListener
{
    void onNetworkEvent(ItemStack p0, EntityPlayer p1, int p2);
}
