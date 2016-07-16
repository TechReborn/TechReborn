package techreborn.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictUtils
{

	public static String toFirstLower(String string) {
		if(string == null || string.isEmpty()) return string;
		return Character.toLowerCase(string.charAt(0)) + string.substring(1);
	}

	public static String toFirstUpper(String string) {
		if(string.isEmpty()) return string;
		return Character.toUpperCase(string.charAt(0)) + string.substring(1);
	}

	public static boolean isOre(Block block, String oreName)
	{
		return isOre(new ItemStack(Item.getItemFromBlock(block)), oreName);
	}

	public static boolean isOre(IBlockState state, String oreName)
	{
		return isOre(
				new ItemStack(Item.getItemFromBlock(state.getBlock()), 1, state.getBlock().getMetaFromState(state)),
				oreName);
	}

	public static boolean isOre(Item item, String oreName)
	{
		return isOre(new ItemStack(item), oreName);
	}

	public static boolean isOre(ItemStack stack, String oreName)
	{
		if (stack != null && stack.getItem() != null && oreName != null)
		{
			int id = OreDictionary.getOreID(oreName);
			int[] ids = OreDictionary.getOreIDs(stack);

			for (int i : ids)
			{
				if (id == i)
				{
					return true;
				}
			}
		}

		return false;
	}
}
