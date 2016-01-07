package ic2.api.tile;

import net.minecraft.item.*;
import java.util.*;

public interface IMachine
{
    double getEnergy();
    
    boolean useEnergy(double p0, boolean p1);
    
    void setRedstoneSensitive(boolean p0);
    
    boolean isRedstoneSensitive();
    
    boolean getActive();
    
    boolean isValidInput(ItemStack p0);
    
    List<UpgradeType> getSupportedTypes();
    
    enum UpgradeType
    {
        ImportExport, 
        MachineModifierA, 
        MachineModifierB, 
        RedstoneControl, 
        Processing, 
        Sounds, 
        WorldInteraction, 
        Custom;
    }
}
