package techreborn.utils;

import net.minecraft.item.ItemStack;
import techreborn.items.DynamicCell;

public class RecipeUtils {
	public static ItemStack getEmptyCell(int stackSize) {
		return DynamicCell.getEmptyCell(stackSize);
	}
}
