package techreborn.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import techreborn.init.ModItems;
import techreborn.power.PowerTickEvent;
import techreborn.power.TRPowerNet;

import java.util.ArrayList;
import java.util.List;

public class TRTickHandler {

    public Item previouslyWearing;

    public static List<TRPowerNet> oldNets = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {
        EntityPlayer player = e.player;
        Item chestslot = player.getEquipmentInSlot(3) != null ? player.getEquipmentInSlot(3).getItem() : null;

        if (previouslyWearing != chestslot && previouslyWearing == ModItems.cloakingDevice && player.isInvisible() && !player.isPotionActive(Potion.invisibility)) {
            player.setInvisible(false);
        }

        previouslyWearing = chestslot;
    }

    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent e) {
        if(!oldNets.isEmpty()){
            for(TRPowerNet powerNet : oldNets){
                MinecraftForge.EVENT_BUS.unregister(powerNet);
            }
            oldNets.clear();
        }
        if(!e.world.isRemote)
            MinecraftForge.EVENT_BUS.post(new PowerTickEvent());
    }

}
