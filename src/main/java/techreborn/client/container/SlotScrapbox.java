package techreborn.client.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import reborncore.client.gui.slots.BaseSlot;
import techreborn.init.ModItems;

public class SlotScrapbox extends BaseSlot {

	public SlotScrapbox(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}

	public boolean isItemValid(ItemStack par1ItemStack) {
		return par1ItemStack.getItem() == ModItems.SCRAP_BOX;
	}

	public int getSlotStackLimit() {
		return 64;
	}
}