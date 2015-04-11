package techreborn.init;

import techreborn.items.ItemDusts;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class ModItems {
	
    public static Item dusts;

    public static void init()
    {
    	dusts = new ItemDusts();
        GameRegistry.registerItem(dusts, "dust");
        
        registerOreDict();
    }
    
    public static void registerOreDict()
    {
    	//TODO
    }

}
