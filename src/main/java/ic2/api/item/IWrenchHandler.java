package ic2.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IWrenchHandler
{
	public boolean supportsItem(ItemStack possibleWrench);
	
	public boolean canWrench(ItemStack wrench, int x, int y, int z, EntityPlayer player);
	
	public void useWrench(ItemStack wrench, int x, int y, int z, EntityPlayer player);
	
	public static interface IWrenchRegistry
	{
		public void registerWrenchSupporter(IWrenchHandler par1);
	}
	
}
