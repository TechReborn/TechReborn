package techreborn.events;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import techreborn.api.TechRebornItems;
import techreborn.init.ModItems;
import techreborn.items.tools.ItemCloakingDevice;

public class TRTickHandler extends TickEvent {
	public TRTickHandler(Type type, Side side, Phase phase) {
		super(type, side, phase);
		
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onPlayerTick(TickEvent.PlayerTickEvent e) {
		EntityPlayer player = e.player;
		
		if(player.getEquipmentInSlot(3)== null) {
			player.setInvisible(false);
		
		}			
		else if(player.getEquipmentInSlot(3).getItem() != ModItems.cloakingDevice) {
			player.setInvisible(false);
		}
			
		
			
	}

	
}
