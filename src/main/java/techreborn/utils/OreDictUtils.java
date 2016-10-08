package techreborn.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class OreDictUtils {

	public static String toFirstLower(String string) {
		if (string == null || string.isEmpty())
			return string;
		return Character.toLowerCase(string.charAt(0)) + string.substring(1);
	}

	public static String toFirstUpper(String string) {
		if (string.isEmpty())
			return string;
		return Character.toUpperCase(string.charAt(0)) + string.substring(1);
	}

	public static String joinDictName(String prefix, String name) {
		return prefix + toFirstUpper(name);
	}

	public static String[] getDictData(String prefixed) {
		StringBuilder prefixBuilder = new StringBuilder();
		StringBuilder nameBuilder = new StringBuilder();
		boolean prefixFinished = false;
		for (int i = 0; i < prefixed.length(); i++) {
			char charAt = prefixed.charAt(i);
			if (!prefixFinished) {
				if (Character.isUpperCase(charAt)) {
					nameBuilder.append(Character.toLowerCase(charAt));
					prefixFinished = true;
				} else
					prefixBuilder.append(charAt);
			} else
				nameBuilder.append(charAt);
		}
		return new String[] {
			prefixBuilder.toString(),
			nameBuilder.toString()
		};
	}

	public static boolean isDictPrefixed(String name, String... prefixes) {
		for (String prefix : prefixes)
			if (name.startsWith(prefix))
				return true;
		return false;
	}

	public static ItemStack getDictOreOrNull(String name, int amount) {
		List<ItemStack> ores = OreDictionary.getOres(name);
		if (ores.isEmpty())
			return null;
		ItemStack ore = ores.get(0);
		ore.stackSize = amount;
		return ore;
	}

	public static boolean isOre(Block block, String oreName) {
		return isOre(new ItemStack(Item.getItemFromBlock(block)), oreName);
	}

	public static boolean isOre(IBlockState state, String oreName) {
		return isOre(
			new ItemStack(Item.getItemFromBlock(state.getBlock()), 1, state.getBlock().getMetaFromState(state)),
			oreName);
	}

	public static boolean isOre(Item item, String oreName) {
		return isOre(new ItemStack(item), oreName);
	}

	public static boolean isOre(ItemStack stack, String oreName) {
		if (stack != null && stack.getItem() != null && oreName != null) {
			int id = OreDictionary.getOreID(oreName);
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
