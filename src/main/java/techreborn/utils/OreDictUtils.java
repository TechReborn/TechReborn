package techreborn.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictUtils {

	public static boolean isOre(Block block, String oreName) {
		return isOre(new ItemStack(Item.getItemFromBlock(block)), oreName);
	}

	public static boolean isOre(IBlockState state, String oreName) {
		return isOre(new ItemStack(Item.getItemFromBlock(state.getBlock()), 1, state.getBlock().getMetaFromState(state)), oreName);
	}

	public static boolean isOre(Item item, String oreName) {
		return isOre(new ItemStack(item), oreName);
	}

	public static boolean isOre(ItemStack stack, String oreName) {
		int id = OreDictionary.getOreID(oreName);

		if (stack != null || stack.getItem() != null) {
			int[] ids = OreDictionary.getOreIDs(stack);

			for (int i : ids) {
				if (id == i) {
					return true;
				}
			}
		}

		return false;
	}
}
