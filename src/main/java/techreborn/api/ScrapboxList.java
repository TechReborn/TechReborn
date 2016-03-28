package techreborn.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import techreborn.Core;

public class ScrapboxList
{

	public static List<ItemStack> stacks = new ArrayList<>();

	public static void addItemStackToList(ItemStack stack)
	{
		if (!hasItems(stack))
		{
			stacks.add(stack);
		}
	}

	private static boolean hasItems(ItemStack stack)
	{
		for (ItemStack s : stacks)
		{
			if (stack.getDisplayName().equals(s.getDisplayName()))
				return true;
		}
		return false;
	}
}
