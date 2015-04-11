package techreborn.init;

import techreborn.items.ItemDusts;
import techreborn.items.ItemGems;
import techreborn.items.ItemIngots;
import techreborn.items.ItemParts;
import techreborn.util.LogHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class ModItems {
	
    public static Item dusts;
    public static Item ingots;
    public static Item gems;
    public static Item parts;

    public static void init()
    {
    	dusts = new ItemDusts();
        GameRegistry.registerItem(dusts, "dust");
        ingots = new ItemIngots();
        GameRegistry.registerItem(ingots, "ingot");
        gems = new ItemGems();
        GameRegistry.registerItem(gems, "gem");
        parts = new ItemParts();
        GameRegistry.registerItem(parts, "part");
		LogHelper.info("TechReborns Items Loaded");

        registerOreDict();
    }
    
    public static void registerOreDict()
    {
    	//TODO
    }

}
