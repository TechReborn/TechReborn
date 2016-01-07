package ic2.api.item;

import net.minecraft.util.BlockPos;
import net.minecraft.world.*;

public interface ITerraformingBP
{
    int getConsume();
    
    int getRange();
    
    boolean terraform(World p0, BlockPos pos);
}
