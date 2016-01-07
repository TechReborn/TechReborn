package ic2.api.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class RetextureEvent extends WorldEvent
{
    public final BlockPos pos;
    public final EnumFacing side;
    public final IBlockState referencedState;
    public final EnumFacing referencedSide;
    public boolean applied;
    
    public RetextureEvent(final World world1, final BlockPos pos, final EnumFacing side1, final IBlockState state, final EnumFacing referencedSide1) {
        super(world1);
        this.applied = false;
        this.pos = pos;
        this.side = side1;
        this.referencedState = state;
        this.referencedSide = referencedSide1;
    }
}
