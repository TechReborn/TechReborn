package techreborn.utils;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import techreborn.api.armor.ModularArmorUtils;
import techreborn.lib.ModInfo;


@Mod.EventBusSubscriber(modid = ModInfo.MOD_ID)
public class ModularArmorEvent {

	@SubscribeEvent
	public static void playerTickEvent(TickEvent.PlayerTickEvent event){
		ModularArmorUtils.getHoldersOnPlayer(event.player).forEach(upgradeHolder -> upgradeHolder.getUpgrade().tick(upgradeHolder, event.player));
	}
}
