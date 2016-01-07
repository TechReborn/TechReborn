package ic2.api.reactor;

import net.minecraftforge.fluids.*;

public interface ISteamReactor extends IReactor
{
    FluidTank getWaterTank();
    
    FluidTank getSteamTank();
}
