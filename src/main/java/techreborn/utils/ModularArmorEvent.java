package techreborn.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import techreborn.api.armor.ModularArmorUtils;
import techreborn.api.armor.UpgradeHolder;
import techreborn.lib.ModInfo;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = ModInfo.MOD_ID)
public class ModularArmorEvent {

	@SubscribeEvent
	public static void itemToolTipEvent(ItemTooltipEvent event){
		if(ModularArmorUtils.isModularArmor(event.getItemStack())){
			event.getToolTip().add(TextFormatting.LIGHT_PURPLE + "Uprgaded with armor uprgades");
			ModularArmorUtils.getManager(event.getItemStack()).tooltip(event.getToolTip());
		}
		//Lets make it clear it can be used in the moduar armor
		if(ModularArmorUtils.isUprgade(event.getItemStack())){
			event.getToolTip().add(TextFormatting.LIGHT_PURPLE + "Modular armor uprgade");
		}
	}

	@SubscribeEvent
	public static void playerTickEvent(TickEvent.PlayerTickEvent event){
		ModularArmorUtils.getHoldersOnPlayer(event.player).forEach(upgradeHolder -> upgradeHolder.getUpgrade().tick(upgradeHolder, event.player));
	}

	@SubscribeEvent
	public static void playerDamage(LivingHurtEvent event){
		if(event.getEntity() instanceof EntityPlayer){
			for(UpgradeHolder holder : new ArrayList<>(ModularArmorUtils.getHoldersOnPlayer((EntityPlayer) event.getEntity()))){
				if(holder.getUpgrade().hurt(holder, event)){
					event.setCanceled(true);
					break;
				}
			}
		}
	}
}
