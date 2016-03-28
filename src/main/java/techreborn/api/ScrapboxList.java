package techreborn.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class ScrapboxList
{

	public static List<ItemStack> stacks = new ArrayList<ItemStack>();

	public static void addItemStackToList(ItemStack stack)
	{
		if (!hasItems(stack))
			stacks.add(stack);
	}

	private static boolean hasItems(ItemStack stack)
	{
		for (ItemStack s : stacks)
		{
			if (stack.getItem().getItemStackDisplayName(stack).equals(s.getItem().getItemStackDisplayName(stack)))
				return true;
		}
		return false;
	}
}
