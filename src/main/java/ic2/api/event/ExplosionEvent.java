package ic2.api.event;

import net.minecraftforge.event.world.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;

@Cancelable
public class ExplosionEvent extends WorldEvent
{
    public final Entity entity;
    public double x;
    public double y;
    public double z;
    public double power;
    public final EntityLivingBase igniter;
    public final int radiationRange;
    public final double rangeLimit;
    
    public ExplosionEvent(final World world, final Entity entity, final double x, final double y, final double z, final double power, final EntityLivingBase igniter, final int radiationRange, final double rangeLimit) {
        super(world);
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.power = power;
        this.igniter = igniter;
        this.radiationRange = radiationRange;
        this.rangeLimit = rangeLimit;
    }
}
