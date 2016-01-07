package ic2.api.reactor;

import net.minecraft.item.*;

public interface ISteamReactorComponent extends IReactorComponent
{
    void processTick(ISteamReactor p0, ItemStack p1, int p2, int p3, boolean p4, boolean p5);
}
