package techreborn.events;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import techreborn.items.ItemGems;
import techreborn.items.ItemParts;
import techreborn.utils.OreDictUtils;

/**
 * Created by McKeever on 10/16/2016.
 */
public class BlockBreakHandler {

    @SubscribeEvent
    public void onBlockHarvest(BlockEvent.HarvestDropsEvent event) {
    	boolean shouldAdd = false;
        for (ItemStack ore : event.getDrops()) {
            if (OreDictUtils.isOre(ore, "gemRuby")) {
                shouldAdd = true;
            }
        }
        if(shouldAdd){
	        event.getDrops().add(ItemGems.getGemByName("redGarnet").copy());
        }
    }
}
