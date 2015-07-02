package techreborn.world;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import techreborn.items.ItemIngots;

public class DungeonLoot {
	
	public static void init()
	{
		generate(ItemIngots.getIngotByName("steel"), 5);
	}
	
	public static void generate(ItemStack itemStack, int rare)
	{
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(itemStack, itemStack.getItemDamage(), itemStack.stackSize, rare));
		ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(itemStack, itemStack.getItemDamage(), itemStack.stackSize, rare));
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(itemStack ,itemStack.getItemDamage(), itemStack.stackSize, rare));
		ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(itemStack ,itemStack.getItemDamage(), itemStack.stackSize, rare));
	}

}
