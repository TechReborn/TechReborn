package techreborn.events;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import reborncore.common.util.OreUtil;
import techreborn.items.ItemGems;
import techreborn.utils.OreDictUtils;

/**
 * Created by McKeever on 10/16/2016.
 */
public class BlockBreakHandler {

	@SubscribeEvent
	public void onBlockHarvest(BlockEvent.HarvestDropsEvent event) {
		if (event.getState() != null && OreUtil.doesOreExistAndValid("oreRuby") && OreDictUtils.isOre(event.getState(), "oreRuby")) {
			event.getDrops().add(ItemGems.getGemByName("red_garnet").copy());
		}
	}
}
