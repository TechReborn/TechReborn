package techreborn.world;

import java.util.Arrays;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import techreborn.items.ItemIngots;

public class DungeonLoot {
	
	public static void init()
	{
		generate(ItemIngots.getIngotByName("steel").getItem(), 5);
	}
	
	public static void generate(Item item, int rare)
	{
		for (String category : Arrays.asList(ChestGenHooks.VILLAGE_BLACKSMITH, ChestGenHooks.MINESHAFT_CORRIDOR, ChestGenHooks.PYRAMID_DESERT_CHEST, ChestGenHooks.PYRAMID_JUNGLE_CHEST, ChestGenHooks.PYRAMID_JUNGLE_DISPENSER, ChestGenHooks.STRONGHOLD_CORRIDOR, ChestGenHooks.STRONGHOLD_LIBRARY, ChestGenHooks.STRONGHOLD_CROSSING, ChestGenHooks.BONUS_CHEST, ChestGenHooks.DUNGEON_CHEST)) 
		{
			ChestGenHooks.addItem(category, new WeightedRandomChestContent(item, 0, 1, 3, rare));
		}
	}
}
