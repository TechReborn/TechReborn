package ic2.api.recipe;

import net.minecraftforge.fluids.*;
import java.util.*;

public interface ILiquidAcceptManager
{
    boolean acceptsFluid(Fluid p0);
    
    Set<Fluid> getAcceptedFluids();
}
