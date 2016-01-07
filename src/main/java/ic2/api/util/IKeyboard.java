package ic2.api.util;

import net.minecraft.entity.player.*;

public interface IKeyboard
{
    boolean isAltKeyDown(EntityPlayer p0);
    
    boolean isBoostKeyDown(EntityPlayer p0);
    
    boolean isForwardKeyDown(EntityPlayer p0);
    
    boolean isJumpKeyDown(EntityPlayer p0);
    
    boolean isModeSwitchKeyDown(EntityPlayer p0);
    
    boolean isSideinventoryKeyDown(EntityPlayer p0);
    
    boolean isHudModeKeyDown(EntityPlayer p0);
    
    boolean isSneakKeyDown(EntityPlayer p0);
}
