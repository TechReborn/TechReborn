package ic2.api.crops;

public class BaseSeed
{
    public final CropCard crop;
    @Deprecated
    public int id;
    public int size;
    public int statGrowth;
    public int statGain;
    public int statResistance;
    public int stackSize;
    
    public BaseSeed(final CropCard crop, final int size, final int statGrowth, final int statGain, final int statResistance, final int stackSize) {
        this.crop = crop;
        this.id = Crops.instance.getIdFor(crop);
        this.size = size;
        this.statGrowth = statGrowth;
        this.statGain = statGain;
        this.statResistance = statResistance;
        this.stackSize = stackSize;
    }
    
    public BaseSeed(final int id, final int size, final int statGrowth, final int statGain, final int statResistance, final int stackSize) {
        this(getCropFromId(id), size, statGrowth, statGain, statResistance, stackSize);
    }
    
    private static CropCard getCropFromId(final int id) {
        final CropCard[] crops = Crops.instance.getCropList();
        if (id < 0 || id >= crops.length) {
            return null;
        }
        return crops[id];
    }
}
