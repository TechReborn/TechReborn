package ic2.api.item;

import net.minecraft.item.*;
import ic2.api.tile.*;
import java.util.*;

public interface IMachineUpgradeItem
{
    int getExtraProcessTime(ItemStack p0, IMachine p1);
    
    double getProcessTimeMultiplier(ItemStack p0, IMachine p1);
    
    int getExtraEnergyDemand(ItemStack p0, IMachine p1);
    
    double getEnergyDemandMultiplier(ItemStack p0, IMachine p1);
    
    int getExtraEnergyStorage(ItemStack p0, IMachine p1);
    
    double getEnergyStorageMultiplier(ItemStack p0, IMachine p1);
    
    int getExtraTier(ItemStack p0, IMachine p1);
    
    boolean onTick(ItemStack p0, IMachine p1);
    
    void onProcessEnd(ItemStack p0, IMachine p1, List<ItemStack> p2);
    
    boolean useRedstoneinverter(ItemStack p0, IMachine p1);
    
    void onInstalling(ItemStack p0, IMachine p1);
    
    float getSoundVolumeMultiplier(ItemStack p0, IMachine p1);
    
    IMachine.UpgradeType getType(ItemStack p0);
}
