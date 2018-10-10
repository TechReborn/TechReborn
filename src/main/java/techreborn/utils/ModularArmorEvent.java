package techreborn.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
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
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ModInfo.MOD_ID)
public class ModularArmorEvent {

	//TODO is there a better way, make sure that this gets cleaned up as well if the palyer logs out or something
	static Map<String, NonNullList<ItemStack>> armorMap = new HashMap<>();

	@SubscribeEvent
	public static void itemToolTipEvent(ItemTooltipEvent event){
		if(ModularArmorUtils.isModularArmor(event.getItemStack())){
			event.getToolTip().add(TextFormatting.LIGHT_PURPLE + "Uprgaded with armor upgrades");
			ModularArmorUtils.getManager(event.getItemStack()).tooltip(event.getToolTip());
		}
		//Lets make it clear it can be used in the moduar armor
		if(ModularArmorUtils.isUprgade(event.getItemStack())){
			event.getToolTip().add(TextFormatting.LIGHT_PURPLE + "Modular armor uprgade");
		}
	}

	@SubscribeEvent
	public static void playerTickEvent(TickEvent.PlayerTickEvent event){
		if(event.phase == TickEvent.Phase.START){
			return;
		}
		ModularArmorUtils.getHoldersOnPlayer(event.player).forEach(upgradeHolder -> upgradeHolder.getUpgrade().tick(upgradeHolder, event.player));
		EntityPlayer player = event.player;
		if(!armorMap.containsKey(player.getName() + player.world.isRemote)){
			armorMap.put(player.getName() + player.world.isRemote, NonNullList.withSize(player.inventory.armorInventory.size(), ItemStack.EMPTY));
		}
		NonNullList<ItemStack> lastTickList = armorMap.get(player.getName() + player.world.isRemote);
		for (int i = 0; i < player.inventory.armorInventory.size(); i++) {
			if(lastTickList.get(i).isEmpty() != player.inventory.armorInventory.get(i).isEmpty()) {
				ItemStack lastStack = lastTickList.get(i);
				if(ModularArmorUtils.isModularArmor(lastStack)){
					ModularArmorUtils.getManager(lastStack).getAllHolders().forEach(holder -> holder.getUpgrade().unequip(holder, event.player));
				}
				ItemStack newStack = player.inventory.armorInventory.get(i);
				if(ModularArmorUtils.isModularArmor(newStack)){
					ModularArmorUtils.getManager(newStack).getAllHolders().forEach(holder -> holder.getUpgrade().equip(holder, event.player));
				}
			}
		}
		for (int i = 0; i < player.inventory.armorInventory.size(); i++) {
			lastTickList.set(i, player.inventory.armorInventory.get(i).copy());
		}
		armorMap.put(player.getName() + player.world.isRemote, lastTickList);
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
