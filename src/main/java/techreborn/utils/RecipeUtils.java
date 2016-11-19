package techreborn.utils;

import net.minecraft.item.ItemStack;
import techreborn.items.DynamicCell;

import javax.annotation.Nonnull;

public class RecipeUtils {
	@Nonnull
	public static ItemStack getEmptyCell(int stackSize) {
		return DynamicCell.getEmptyCell(stackSize);
	}
}
