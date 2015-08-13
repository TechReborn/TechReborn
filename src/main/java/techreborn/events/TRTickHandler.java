package techreborn.events;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import techreborn.init.ModItems;

public class TRTickHandler extends TickEvent {
	public TRTickHandler(Type type, Side side, Phase phase) {
		super(type, side, phase);
		
	}
	public Item previouslyWearing;
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onPlayerTick(TickEvent.PlayerTickEvent e) {
		EntityPlayer player = e.player;
		Item chestslot = player.getEquipmentInSlot(3) != null ? player.getEquipmentInSlot(3).getItem() : null;
		
		if(previouslyWearing != chestslot && previouslyWearing == ModItems.cloakingDevice && player.isInvisible())
			player.setInvisible(false);
		previouslyWearing = chestslot;
	}

	
}
