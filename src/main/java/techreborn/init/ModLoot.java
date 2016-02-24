package techreborn.init;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import techreborn.items.ItemIngots;

public class ModLoot {
	public static WeightedRandomChestContent rubberSaplingLoot = new WeightedRandomChestContent(new ItemStack(ModBlocks.rubberSapling), 1, 3, 50);
	public static WeightedRandomChestContent copperIngotLoot = new WeightedRandomChestContent(ItemIngots.getIngotByName("copper"), 1, 4, 100);
	public static WeightedRandomChestContent tinIngotLoot = new WeightedRandomChestContent(ItemIngots.getIngotByName("tin"), 1, 4, 100);
	
	public static void init(){
		ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(rubberSaplingLoot);
		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(rubberSaplingLoot);
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(rubberSaplingLoot);
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(rubberSaplingLoot);
		ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(rubberSaplingLoot);

		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(copperIngotLoot);
		ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(tinIngotLoot);
	}
}
