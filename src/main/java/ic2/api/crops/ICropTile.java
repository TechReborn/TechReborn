package ic2.api.crops;

import net.minecraft.nbt.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import net.minecraft.item.*;
import net.minecraft.block.*;

public interface ICropTile
{
    CropCard getCrop();
    
    void setCrop(CropCard p0);
    
    @Deprecated
    short getID();
    
    @Deprecated
    void setID(short p0);
    
    byte getSize();
    
    void setSize(byte p0);
    
    byte getGrowth();
    
    void setGrowth(byte p0);
    
    byte getGain();
    
    void setGain(byte p0);
    
    byte getResistance();
    
    void setResistance(byte p0);
    
    byte getScanLevel();
    
    void setScanLevel(byte p0);
    
    NBTTagCompound getCustomData();
    
    int getNutrientStorage();
    
    void setNutrientStorage(int p0);
    
    int getHydrationStorage();
    
    void setHydrationStorage(int p0);
    
    int getWeedExStorage();
    
    void setWeedExStorage(int p0);
    
    byte getHumidity();
    
    byte getNutrients();
    
    byte getAirQuality();
    
    World getWorld();
    
    BlockPos getLocation();
    
    int getLightLevel();
    
    boolean pick(boolean p0);
    
    boolean harvest(boolean p0);
    
    ItemStack[] harvest_automated(boolean p0);
    
    void reset();
    
    void updateState();
    
    boolean isBlockBelow(Block p0);
    
    ItemStack generateSeeds(CropCard p0, byte p1, byte p2, byte p3, byte p4);
    
    @Deprecated
    ItemStack generateSeeds(short p0, byte p1, byte p2, byte p3, byte p4);
}
