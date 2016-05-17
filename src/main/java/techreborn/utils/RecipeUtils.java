package techreborn.utils;

import net.minecraft.item.ItemStack;
import techreborn.init.ModItems;

public class RecipeUtils
{
	public static ItemStack getEmptyCell(int stackSize)
	{ // TODO ic2
		// if (Loader.isModLoaded("IC2")) {
		// ItemStack cell = IC2Items.getItem("cell").copy();
		// cell.stackSize = stackSize;
		// return cell;
		// } else {
		return new ItemStack(ModItems.emptyCell);
		// }
	}
}
