package ic2.api.crops;

import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Collection;

public abstract class Crops
{
    public static Crops instance;
    public static CropCard weed;
    
    public abstract void addBiomenutrientsBonus(final BiomeDictionary.Type p0, final int p1);
    
    public abstract void addBiomehumidityBonus(final BiomeDictionary.Type p0, final int p1);
    
    public abstract int getHumidityBiomeBonus(final BiomeGenBase p0);
    
    public abstract int getNutrientBiomeBonus(final BiomeGenBase p0);
    
    public abstract CropCard getCropCard(final String p0, final String p1);
    
    public abstract CropCard getCropCard(final ItemStack p0);
    
    public abstract Collection<CropCard> getCrops();
    
    @Deprecated
    public abstract CropCard[] getCropList();
    
    public abstract short registerCrop(final CropCard p0);
    
    public abstract boolean registerCrop(final CropCard p0, final int p1);
    
    @Deprecated
    public abstract boolean registerBaseSeed(final ItemStack p0, final int p1, final int p2, final int p3, final int p4, final int p5);
    
    public abstract boolean registerBaseSeed(final ItemStack p0, final CropCard p1, final int p2, final int p3, final int p4, final int p5);
    
    public abstract BaseSeed getBaseSeed(final ItemStack p0);
    
    @Deprecated
    public abstract int getIdFor(final CropCard p0);
}
