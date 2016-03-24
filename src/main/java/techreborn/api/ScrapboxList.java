package techreborn.api;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ScrapboxList {
	
	public static List<ItemStack> stacks = new ArrayList<ItemStack>();
	
	public static void addItemStackToList(ItemStack stack){
		stacks.add(stack);
	}
}
