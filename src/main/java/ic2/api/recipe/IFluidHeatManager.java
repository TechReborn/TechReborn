package ic2.api.recipe;

import net.minecraftforge.fluids.*;
import java.util.*;

public interface IFluidHeatManager extends ILiquidAcceptManager
{
    void addFluid(String p0, int p1, int p2);
    
    BurnProperty getBurnProperty(Fluid p0);
    
    Map<String, BurnProperty> getBurnProperties();
    
    class BurnProperty
    {
        public final int amount;
        public final int heat;
        
        public BurnProperty(int amount1, int heat1) {
            this.amount = amount1;
            this.heat = heat1;
        }
    }
}
