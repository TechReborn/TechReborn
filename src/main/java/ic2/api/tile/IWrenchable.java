package ic2.api.tile;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.EnumFacing;

public interface IWrenchable
{
    boolean wrenchCanSetFacing(EntityPlayer p0, EnumFacing p1);
    
    EnumFacing getFacing();
    
    void setFacing(EnumFacing facing);
    
    boolean wrenchCanRemove(EntityPlayer p0);
    
    float getWrenchDropRate();
    
    ItemStack getWrenchDrop(EntityPlayer p0);
}
