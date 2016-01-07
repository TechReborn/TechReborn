package ic2.api.event;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class PaintEvent extends WorldEvent
{
    public final BlockPos pos;
    public final EnumFacing side;
    public final EnumDyeColor color;
    public boolean painted;
    
    public PaintEvent(final World world1, final BlockPos pos, final EnumFacing side1, final EnumDyeColor color1) {
        super(world1);
        this.painted = false;
        this.pos = pos;
        this.side = side1;
        this.color = color1;
    }
}
