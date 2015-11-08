package techreborn.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.common.util.ItemUtils;

import java.util.HashMap;

public class OreUnifier {

    public static HashMap<String, ItemStack> oreHash = new HashMap<String, ItemStack>();

    @SubscribeEvent
    public void itemTick(PlayerUseItemEvent.Tick event) {
        int[] oreIds = OreDictionary.getOreIDs(event.item);
        for(int id : oreIds){
            String oreName = OreDictionary.getOreName(id);
            if(oreHash.containsKey(oreName)){
                if(ItemUtils.isItemEqual(event.item, oreHash.get(oreName), true, true, true) && !ItemUtils.isItemEqual(event.item, oreHash.get(oreName), true, true, false)){
                    //TODO set new item
                }
            }
        }
    }

    public static void registerOre(String name, ItemStack ore) {
        oreHash.put(name, ore);
        OreDictionary.registerOre(name, ore);
    }

    public static void registerOre(String name, Item ore) {
        registerOre(name, new ItemStack(ore));
    }

    public static void registerOre(String name, Block ore) {
        registerOre(name, new ItemStack(ore));
    }


}
