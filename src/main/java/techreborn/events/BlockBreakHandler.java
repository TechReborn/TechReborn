package techreborn.events;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import techreborn.items.ItemGems;
import techreborn.utils.OreDictUtils;

/**
 * Created by McKeever on 10/16/2016.
 */
public class BlockBreakHandler {

	@SubscribeEvent
	public void onBlockHarvest(BlockEvent.HarvestDropsEvent event) {
		if (!event.isSilkTouching()) {
			if (OreDictUtils.isOre(event.getState(), "oreRuby")) {
				for (d : event.getDrops()) {
					if (OreDictUtils.isOre(d, "gemRuby")) {
						event.getDrops().add(ItemGems.getGemByName("red_garnet").copy());
						break;
					}
				}
			}
		}
	}
}
