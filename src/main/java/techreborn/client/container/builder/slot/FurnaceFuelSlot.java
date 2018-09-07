package techreborn.client.container.builder.slot;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.IItemHandler;
import reborncore.client.gui.slots.BaseSlot;

public class FurnaceFuelSlot extends BaseSlot {

	public FurnaceFuelSlot(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return TileEntityFurnace.isItemFuel(stack) || isBucket(stack);
	}

	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return isBucket(stack) ? 1 : super.getItemStackLimit(stack);
	}

	public static boolean isBucket(ItemStack stack)
	{
		return stack.getItem() == Items.BUCKET;
	}
}
