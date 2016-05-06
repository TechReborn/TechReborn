package techreborn.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import techreborn.init.ModItems;
import techreborn.power.PowerTickEvent;

public class TRTickHandler
{

	public Item previouslyWearing;

	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void onPlayerTick(TickEvent.PlayerTickEvent e)
	{
		EntityPlayer player = e.player;
		Item chestslot = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null
				? player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() : null;

		if (previouslyWearing != chestslot && previouslyWearing == ModItems.cloakingDevice && player.isInvisible()
				&& !player.isPotionActive(MobEffects.INVISIBILITY))
		{
			player.setInvisible(false);
		}

		previouslyWearing = chestslot;
	}

	@SubscribeEvent
	public void worldTick(TickEvent.WorldTickEvent e)
	{
		if (e.world.isRemote)
		{
			return;
		}
		MinecraftForge.EVENT_BUS.post(new PowerTickEvent());
	}

}
