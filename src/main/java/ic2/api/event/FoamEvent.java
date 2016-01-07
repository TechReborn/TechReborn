package ic2.api.event;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class FoamEvent extends WorldEvent
{
    public BlockPos pos;
    
    public FoamEvent(final World world, final BlockPos pos) {
        super(world);
        this.pos = pos;
    }
    
    @Cancelable
    public static class Check extends FoamEvent
    {
        public Check(final World world, final BlockPos pos) {
            super(world, pos);
        }
    }
    
    @Cancelable
    public static class Foam extends FoamEvent
    {
        public Foam(final World world, final BlockPos pos) {
            super(world, pos);
        }
    }
}
