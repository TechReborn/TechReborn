package techreborn.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by mark on 12/04/15.
 */
public class ItemUtils {
	public static boolean isItemEqual(final ItemStack a, final ItemStack b, final boolean matchDamage, final boolean matchNBT) {
		if (a == null || b == null)
			return false;
		if (a.getItem() != b.getItem())
			return false;
		if (matchNBT && !ItemStack.areItemStackTagsEqual(a, b))
			return false;
		if (matchDamage && a.getHasSubtypes()) {
			if (isWildcard(a) || isWildcard(b))
				return true;
			if (a.getItemDamage() != b.getItemDamage())
				return false;
		}
		return true;
	}

	public static boolean isWildcard(ItemStack stack) {
		return isWildcard(stack.getItemDamage());
	}

	public static boolean isWildcard(int damage) {
		return damage == -1 || damage == OreDictionary.WILDCARD_VALUE;
	}
}
