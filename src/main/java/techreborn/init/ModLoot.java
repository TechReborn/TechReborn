package techreborn.init;

import java.util.Arrays;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import techreborn.config.ConfigTechReborn;
import techreborn.items.ItemIngots;

public class ModLoot {
    public static ConfigTechReborn config;
    
	public static WeightedRandomChestContent rubberSaplingLoot = new WeightedRandomChestContent(new ItemStack(ModBlocks.rubberSapling), 1, 3, 25);
	public static WeightedRandomChestContent copperIngotLoot = new WeightedRandomChestContent(ItemIngots.getIngotByName("copper"), 1, 4, 20);
	public static WeightedRandomChestContent tinIngotLoot = new WeightedRandomChestContent(ItemIngots.getIngotByName("tin"), 1, 4, 20);
	public static WeightedRandomChestContent steelIngotLoot = new WeightedRandomChestContent(ItemIngots.getIngotByName("steel"), 1, 3, 5);
	
	public static void init(){
		if(config.RubberSaplingLoot){
			generate(rubberSaplingLoot);
		}
		if(config.CopperIngotsLoot){
			generate(copperIngotLoot);
		}
		if(config.TinIngotsLoot){
			generate(tinIngotLoot);
		}
		if(config.SteelIngotsLoot){
			generate(steelIngotLoot);
		}
	}
    public static void generate(WeightedRandomChestContent chestContent) {
        for (String category : Arrays.asList(ChestGenHooks.VILLAGE_BLACKSMITH, ChestGenHooks.MINESHAFT_CORRIDOR, ChestGenHooks.PYRAMID_DESERT_CHEST, ChestGenHooks.PYRAMID_JUNGLE_CHEST, ChestGenHooks.PYRAMID_JUNGLE_DISPENSER, ChestGenHooks.STRONGHOLD_CORRIDOR, ChestGenHooks.STRONGHOLD_LIBRARY, ChestGenHooks.STRONGHOLD_CROSSING, ChestGenHooks.BONUS_CHEST, ChestGenHooks.DUNGEON_CHEST)) {
            ChestGenHooks.addItem(category, chestContent);
        }
    }
}
