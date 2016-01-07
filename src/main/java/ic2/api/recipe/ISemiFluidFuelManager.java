package ic2.api.recipe;

import net.minecraftforge.fluids.*;
import java.util.*;

public interface ISemiFluidFuelManager extends ILiquidAcceptManager
{
    void addFluid(String p0, int p1, double p2);
    
    BurnProperty getBurnProperty(Fluid p0);
    
    Map<String, BurnProperty> getBurnProperties();
    
    class BurnProperty
    {
        public final int amount;
        public final double power;
        
        public BurnProperty(int amount1, double power1) {
            this.amount = amount1;
            this.power = power1;
        }
    }
}
