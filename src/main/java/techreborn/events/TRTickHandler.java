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

public class TRTickHandler {

	public Item previouslyWearing;

	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void onPlayerTick(TickEvent.PlayerTickEvent e) {
		EntityPlayer player = e.player;
		Item chestslot = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null
		                 ? player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() : null;

		if (previouslyWearing != chestslot && previouslyWearing == ModItems.cloakingDevice && player.isInvisible()
			&& !player.isPotionActive(MobEffects.INVISIBILITY)) {
			player.setInvisible(false);
		}

		previouslyWearing = chestslot;
	}

	int ticksCounted = 0;
	long tickTime = 0;

	@SubscribeEvent
	public void worldTick(TickEvent.WorldTickEvent e) {
		if (e.world.isRemote) {
			return;
		}

		long start = System.nanoTime();
		MinecraftForge.EVENT_BUS.post(new PowerTickEvent());
		long elapsed = System.nanoTime() - start;
		tickTime += elapsed;
		ticksCounted++;
		if (ticksCounted == 20 * 5) {
			//Enable this this line to get some infomation when debuging.
			//System.out.println("Average powernet tick time over last 5 seconds: " + (tickTime / ticksCounted) + "ns");
			ticksCounted = 0;
			tickTime = 0;
		}
	}

}
