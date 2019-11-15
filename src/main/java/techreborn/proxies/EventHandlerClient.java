package techreborn.proxies;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import techreborn.client.RegisterItemJsons;
import techreborn.client.render.ModelDynamicCell;
import techreborn.init.ModItems;

public class EventHandlerClient {
    public static final EventHandlerClient INSTANCE = new EventHandlerClient();

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        ModelDynamicCell.init();
        RegisterItemJsons.registerModels();
    }

    @SubscribeEvent
    public void renderPlayer(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.getEntityPlayer();
        Item chestSlot = !player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty()
                ? player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() : null;

        if (chestSlot != null && chestSlot == ModItems.CLOAKING_DEVICE) event.setCanceled(true);
    }
}
