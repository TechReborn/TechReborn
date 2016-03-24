package techreborn.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class ScrapboxList
{

	public static List<ItemStack> stacks = new ArrayList<ItemStack>();

	public static void addItemStackToList(ItemStack stack)
	{
		stacks.add(stack);
	}
}
