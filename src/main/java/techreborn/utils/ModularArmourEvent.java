package techreborn.utils;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import techreborn.api.armour.ModularArmourUtils;
import techreborn.lib.ModInfo;


@Mod.EventBusSubscriber(modid = ModInfo.MOD_ID)
public class ModularArmourEvent {

	@SubscribeEvent
	public static void playerTickEvent(TickEvent.PlayerTickEvent event){
		ModularArmourUtils.getHoldersOnPlayer(event.player).forEach(upgradeHolder -> upgradeHolder.getUpgrade().tick(upgradeHolder, event.player));
	}
}
