package techreborn.client.container;

import net.minecraft.inventory.IInventory;
import reborncore.client.gui.BaseSlot;
import net.minecraft.item.ItemStack;
import techreborn.init.ModItems;

public class SlotScrapbox extends BaseSlot
{

	public SlotScrapbox(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
	}

	public boolean isItemValid(ItemStack par1ItemStack)
	{
		return par1ItemStack.getItem() == ModItems.scrapBox;
	}

	public int getSlotStackLimit()
	{
		return 64;
	}
}