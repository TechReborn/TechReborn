package ic2.api.crops;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public abstract class CropCard
{
    private final String modId;
    
    public CropCard() {
        this.modId = getModId();
    }
    
    public abstract String name();
    
    public String owner() {
        return this.modId;
    }
    
    public String displayName() {
        return this.name();
    }
    
    public String discoveredBy() {
        return "unknown";
    }
    
    public String desc(final int i) {
        final String[] att = this.attributes();
        if (att == null || att.length == 0) {
            return "";
        }
        if (i == 0) {
            String s = att[0];
            if (att.length >= 2) {
                s = s + ", " + att[1];
                if (att.length >= 3) {
                    s += ",";
                }
            }
            return s;
        }
        if (att.length < 3) {
            return "";
        }
        String s = att[2];
        if (att.length >= 4) {
            s = s + ", " + att[3];
        }
        return s;
    }
    
    public int getrootslength(final ICropTile crop) {
        return 1;
    }
    
    public abstract int tier();
    
    public abstract int stat(final int p0);
    
    public abstract String[] attributes();
    
    public abstract int maxSize();
    
    public int growthDuration(final ICropTile crop) {
        return this.tier() * 200;
    }
    
    public abstract boolean canGrow(final ICropTile p0);
    
    public int weightInfluences(final ICropTile crop, final float humidity, final float nutrients, final float air) {
        return (int)(humidity + nutrients + air);
    }
    
    public boolean canCross(final ICropTile crop) {
        return crop.getSize() >= 3;
    }
    
    public boolean rightclick(final ICropTile crop, final EntityPlayer player) {
        return crop.harvest(true);
    }
    
    public abstract int getOptimalHavestSize(final ICropTile p0);
    
    public abstract boolean canBeHarvested(final ICropTile p0);
    
    public float dropGainChance() {
        float base = 1.0f;
        for (int i = 0; i < this.tier(); ++i) {
            base *= 0.95;
        }
        return base;
    }
    
    public abstract ItemStack getGain(final ICropTile p0);
    
    public byte getSizeAfterHarvest(final ICropTile crop) {
        return 1;
    }
    
    public boolean leftclick(final ICropTile crop, final EntityPlayer player) {
        return crop.pick(true);
    }
    
    public float dropSeedChance(final ICropTile crop) {
        if (crop.getSize() == 1) {
            return 0.0f;
        }
        float base = 0.5f;
        if (crop.getSize() == 2) {
            base /= 2.0f;
        }
        for (int i = 0; i < this.tier(); ++i) {
            base *= 0.8;
        }
        return base;
    }
    
    public ItemStack getSeeds(final ICropTile crop) {
        return crop.generateSeeds(crop.getCrop(), crop.getGrowth(), crop.getGain(), crop.getResistance(), crop.getScanLevel());
    }
    
    public void onNeighbourChange(final ICropTile crop) {
    }
    
    public int emitRedstone(final ICropTile crop) {
        return 0;
    }
    
    public void onBlockDestroyed(final ICropTile crop) {
    }
    
    public int getEmittedLight(final ICropTile crop) {
        return 0;
    }
    
    public boolean onEntityCollision(final ICropTile crop, final Entity entity) {
        return entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isSprinting();
    }
    
    public void tick(final ICropTile crop) {
    }
    
    public boolean isWeed(final ICropTile crop) {
        return crop.getSize() >= 2 && (crop == Crops.weed || crop.getGrowth() >= 24);
    }
    
    @Deprecated
    public final int getId() {
        return Crops.instance.getIdFor(this);
    }
    
    private static String getModId() {
        final ModContainer modContainer = Loader.instance().activeModContainer();
        if (modContainer != null) {
            return modContainer.getModId();
        }
        assert false;
        return "unknown";
    }
}
