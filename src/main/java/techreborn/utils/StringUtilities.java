package techreborn.utils;

import net.minecraft.item.ItemStack;

@SuppressWarnings({ "ConstantConditions", "JavaDoc" })
public class StringUtilities {
	public static String stackToStringSafe(ItemStack itemStack) {
		if (itemStack == null)
			return "(null)";
		else if (itemStack.getItem() == null)
			return (itemStack == ItemStack.EMPTY ? 0 : itemStack.getCount()) + "x(null)@(unknown)";
		else
			return itemStack.toString();
	}
}
