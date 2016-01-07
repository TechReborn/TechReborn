package ic2.api.reactor;

import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.item.*;

public interface IReactor
{
    BlockPos getPosition();
    
    World getWorld();
    
    int getHeat();
    
    void setHeat(int p0);
    
    int addHeat(int p0);
    
    int getMaxHeat();
    
    void setMaxHeat(int p0);
    
    void addEmitHeat(int p0);
    
    float getHeatEffectModifier();
    
    void setHeatEffectModifier(float p0);
    
    float getReactorEnergyOutput();
    
    double getReactorEUEnergyOutput();
    
    float addOutput(float p0);
    
    ItemStack getItemAt(int p0, int p1);
    
    void setItemAt(int p0, int p1, ItemStack p2);
    
    void explode();
    
    int getTickRate();
    
    boolean produceEnergy();
    
    void setRedstoneSignal(boolean p0);
    
    boolean isFluidCooled();
}
