package techreborn.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import techreborn.api.ScrapboxList;

public class BehaviorDispenseScrapbox extends BehaviorDefaultDispenseItem
{

	@Override
	protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
	{
		int random = source.getWorld().rand.nextInt(ScrapboxList.stacks.size());
		ItemStack out = ScrapboxList.stacks.get(random).copy();
		float xOffset = source.getWorld().rand.nextFloat() * 0.8F + 0.1F;
		float yOffset = source.getWorld().rand.nextFloat() * 0.8F + 0.1F;
		float zOffset = source.getWorld().rand.nextFloat() * 0.8F + 0.1F;
		stack.splitStack(1);

		EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
		IPosition iposition = BlockDispenser.getDispensePosition(source);
		doDispense(source.getWorld(), out, 6, enumfacing, iposition);
		return stack;
	}

}
