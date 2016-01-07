package ic2.api.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class LaserEvent extends WorldEvent
{
    public final Entity lasershot;
    public EntityLivingBase owner;
    public float range;
    public float power;
    public int blockBreaks;
    public boolean explosive;
    public boolean smelt;
    
    public LaserEvent(final World world1, final Entity lasershot1, final EntityLivingBase owner1, final float range1, final float power1, final int blockBreaks1, final boolean explosive1, final boolean smelt1) {
        super(world1);
        this.lasershot = lasershot1;
        this.owner = owner1;
        this.range = range1;
        this.power = power1;
        this.blockBreaks = blockBreaks1;
        this.explosive = explosive1;
        this.smelt = smelt1;
    }
    
    public static class LaserShootEvent extends LaserEvent
    {
        ItemStack laseritem;
        
        public LaserShootEvent(final World world1, final Entity lasershot1, final EntityLivingBase owner1, final float range1, final float power1, final int blockBreaks1, final boolean explosive1, final boolean smelt1, final ItemStack laseritem1) {
            super(world1, lasershot1, owner1, range1, power1, blockBreaks1, explosive1, smelt1);
            this.laseritem = laseritem1;
        }
    }
    
    public static class LaserExplodesEvent extends LaserEvent
    {
        public float explosionpower;
        public float explosiondroprate;
        public float explosionentitydamage;
        
        public LaserExplodesEvent(final World world1, final Entity lasershot1, final EntityLivingBase owner1, final float range1, final float power1, final int blockBreaks1, final boolean explosive1, final boolean smelt1, final float explosionpower1, final float explosiondroprate1, final float explosionentitydamage1) {
            super(world1, lasershot1, owner1, range1, power1, blockBreaks1, explosive1, smelt1);
            this.explosionpower = explosionpower1;
            this.explosiondroprate = explosiondroprate1;
            this.explosionentitydamage = explosionentitydamage1;
        }
    }
    
    public static class LaserHitsBlockEvent extends LaserEvent
    {
        public BlockPos pos;
        public EnumFacing side;
        public boolean removeBlock;
        public boolean dropBlock;
        public float dropChance;
        
        public LaserHitsBlockEvent(final World world1, final Entity lasershot1, final EntityLivingBase owner1, final float range1, final float power1, final int blockBreaks1, final boolean explosive1, final boolean smelt1, final BlockPos pos, final EnumFacing side1, final float dropChance1, final boolean removeBlock1, final boolean dropBlock1) {
            super(world1, lasershot1, owner1, range1, power1, blockBreaks1, explosive1, smelt1);
            this.pos = pos;
            this.side = side1;
            this.removeBlock = removeBlock1;
            this.dropBlock = dropBlock1;
            this.dropChance = dropChance1;
        }
    }
    
    public static class LaserHitsEntityEvent extends LaserEvent
    {
        public Entity hitentity;
        
        public LaserHitsEntityEvent(final World world1, final Entity lasershot1, final EntityLivingBase owner1, final float range1, final float power1, final int blockBreaks1, final boolean explosive1, final boolean smelt1, final Entity hitentity1) {
            super(world1, lasershot1, owner1, range1, power1, blockBreaks1, explosive1, smelt1);
            this.hitentity = hitentity1;
        }
    }
}
