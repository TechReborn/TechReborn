package ic2.api.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IScannerItem extends IElectricItem
{
    int startLayerScan(ItemStack p0);
    
    boolean isValuableOre(ItemStack p0, IBlockState state);
    
    int getOreValue(ItemStack p0, IBlockState state);
    
    int getOreValueOfArea(ItemStack p0, World p1, BlockPos pos);
}
